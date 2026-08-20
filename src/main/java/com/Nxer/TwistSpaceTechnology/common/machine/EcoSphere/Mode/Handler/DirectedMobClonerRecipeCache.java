package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerDropConversion.ConvertedOutput;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerWeaponHandler.WeaponEffects;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.DirectedMobClonerFakeRecipe;
import com.github.bsideup.jabel.Desugar;
import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;
import com.kuba6000.mobsinfo.api.event.PostMobRegistrationEvent;
import com.kuba6000.mobsinfo.api.event.PostMobsRegistrationEvent;
import com.kuba6000.mobsinfo.api.event.PreMobRegistrationEvent;
import com.kuba6000.mobsinfo.api.event.PreMobsRegistrationEvent;
import com.kuba6000.mobsinfo.api.utils.ModUtils;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class DirectedMobClonerRecipeCache {

    private static final int EEC_MIN_DURATION = 55;
    private static final int NORMAL_RECIPE_BASE_TIER = 4;
    private static final int BOSS_RECIPE_BASE_TIER = 6;
    private static final double EEC_DIAMOND_SPIKES_DAMAGE = 9d;
    private static final WeaponEffects NO_WEAPON_EFFECTS = DirectedMobClonerWeaponHandler.process(new ItemStack[0]);
    private static final Map<String, PendingRecipe> PENDING_RECIPES = new LinkedHashMap<>();
    private static volatile Map<Integer, CachedRecipe> recipesById = Collections.emptyMap();
    private static boolean registered;

    private DirectedMobClonerRecipeCache() {}

    public static void init() {
        if (registered) return;
        registered = true;
        MinecraftForge.EVENT_BUS.register(new DirectedMobClonerRecipeCache());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPreMobsRegistration(PreMobsRegistrationEvent event) {
        PENDING_RECIPES.clear();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPreMobRegistration(PreMobRegistrationEvent event) {
        String localizedName = getLocalizedName(event);
        PENDING_RECIPES.put(
            event.currentMob,
            new PendingRecipe(
                event.currentMob,
                localizedName,
                ModUtils.getModNameFromClassName(
                    event.recipe.entity.getClass()
                        .getName()),
                isBoss(event),
                getEecRecipeDuration(event.recipe.maxEntityHealth)));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPostMobRegistration(PostMobRegistrationEvent event) {
        PendingRecipe pending = PENDING_RECIPES.get(event.currentMob);
        if (pending == null) return;
        pending.setOutputs(buildCachedOutputs(event.drops));
    }

    private static CachedOutputLists buildCachedOutputs(List<MobDrop> sourceDrops) {
        // The conversion enum is the equipment whitelist; keep each runtime source in its own cache table.
        List<CachedOutput> ordinaryOutputs = new ArrayList<>();
        List<CachedOutput> pulverizableOutputs = new ArrayList<>();
        List<CachedOutput> pulverizedOutputs = new ArrayList<>();
        List<CachedSpecialOutput> special = new ArrayList<>();
        int damageWeight = calculateDamageWeight(sourceDrops);
        for (MobDrop drop : sourceDrops) {
            if (drop.stack == null || drop.stack.getItem() == null) continue;

            double durabilityExpectation = calculateDurabilityExpectation(drop, damageWeight);
            List<CachedChanceModifier> modifiers = cacheChanceModifiers(drop.chanceModifiers);
            boolean weaponDependent = drop.playerOnly || drop.lootable;
            for (CachedChanceModifier modifier : modifiers) weaponDependent |= modifier.weaponDependent();

            ItemStack outputTemplate = sanitizeCachedDrop(drop.stack);
            double baseChance = evaluateChance(drop, modifiers, NO_WEAPON_EFFECTS);
            if (baseChance > 0d) {
                CachedOutput output = cacheOutput(outputTemplate, baseChance, durabilityExpectation, 1d);
                DirectedMobClonerDropConversion.ConversionResult conversion = DirectedMobClonerDropConversion
                    .convert(outputTemplate);
                if (conversion.matched()) {
                    pulverizableOutputs.add(output);
                    pulverizedOutputs
                        .addAll(cacheConvertedOutputs(conversion.outputs(), baseChance, durabilityExpectation));
                } else {
                    ordinaryOutputs.add(output);
                }
            }
            if (weaponDependent) {
                special.add(
                    new CachedSpecialOutput(
                        outputTemplate,
                        baseChance,
                        durabilityExpectation,
                        drop.chance,
                        drop.playerOnly,
                        drop.lootable,
                        modifiers));
            }
        }

        return new CachedOutputLists(
            immutableOutputs(ordinaryOutputs),
            immutableOutputs(pulverizableOutputs),
            immutableOutputs(pulverizedOutputs),
            Collections.unmodifiableList(special));
    }

    private static ItemStack sanitizeCachedDrop(ItemStack source) {
        ItemStack sanitized = source.copy();
        if (sanitized.isItemStackDamageable()) sanitized.setItemDamage(0);
        NBTTagCompound tag = sanitized.getTagCompound();
        if (tag == null) return sanitized;
        tag.removeTag("ench");
        tag.removeTag("StoredEnchantments");
        tag.removeTag("display");
        if (tag.hasNoTags()) sanitized.setTagCompound(null);
        return sanitized;
    }

    private static List<CachedOutput> cacheConvertedOutputs(List<ConvertedOutput> convertedOutputs, double chance,
        double durabilityExpectation) {
        List<CachedOutput> outputs = new ArrayList<>(convertedOutputs.size());
        for (ConvertedOutput converted : convertedOutputs) {
            outputs
                .add(cacheOutput(converted.stack(), chance, durabilityExpectation, converted.probabilityMultiplier()));
        }
        return outputs;
    }

    private static CachedOutput cacheOutput(ItemStack stack, double chance, double durabilityExpectation,
        double probabilityMultiplier) {
        return new CachedOutput(stack, Math.max(0d, chance), durabilityExpectation, probabilityMultiplier);
    }

    private static List<CachedChanceModifier> cacheChanceModifiers(List<IChanceModifier> sourceModifiers) {
        if (sourceModifiers == null || sourceModifiers.isEmpty()) return Collections.emptyList();
        List<CachedChanceModifier> modifiers = new ArrayList<>(sourceModifiers.size());
        for (IChanceModifier modifier : sourceModifiers) {
            CachedChanceModifier cached = cacheChanceModifier(modifier);
            if (cached != null) modifiers.add(cached);
        }
        return Collections.unmodifiableList(modifiers);
    }

    private static CachedChanceModifier cacheChanceModifier(IChanceModifier modifier) {
        if (modifier == null) return null;
        Class<?> modifierClass = modifier.getClass();
        String className = modifierClass.getName();
        if (modifier instanceof IChanceModifier.OrUsing) {
            ItemStack weapon = readField(modifier, "weapon", ItemStack.class);
            Number newChance = readField(modifier, "newChance", Number.class);
            return weapon == null || weapon.getItem() == null || newChance == null ? CachedChanceModifier.DISABLED
                : new CachedChanceModifier(
                    ModifierType.OR_WEAPON,
                    weapon.getItem(),
                    -1,
                    0,
                    newChance.doubleValue() * 100d);
        }
        if (modifierClass == IChanceModifier.DropsOnlyUsing.class) {
            ItemStack weapon = readField(modifier, "weapon", ItemStack.class);
            return weapon == null || weapon.getItem() == null ? CachedChanceModifier.DISABLED
                : new CachedChanceModifier(ModifierType.REQUIRE_WEAPON, weapon.getItem(), -1, 0, 0d);
        }
        if (modifierClass == IChanceModifier.DropsOnlyWithEnchant.class) {
            EnchantmentData enchantment = readField(modifier, "enchantmentData", EnchantmentData.class);
            return enchantment == null || enchantment.enchantmentobj == null ? CachedChanceModifier.DISABLED
                : new CachedChanceModifier(
                    ModifierType.REQUIRE_ENCHANTMENT,
                    null,
                    enchantment.enchantmentobj.effectId,
                    enchantment.enchantmentLevel,
                    0d);
        }
        if (modifierClass == IChanceModifier.EachLevelOfGives.class) {
            Enchantment enchantment = readField(modifier, "enchantment", Enchantment.class);
            Number change = readField(modifier, "change", Number.class);
            return enchantment == null || change == null ? CachedChanceModifier.DISABLED
                : new CachedChanceModifier(
                    ModifierType.ENCHANTMENT_BONUS,
                    null,
                    enchantment.effectId,
                    1,
                    change.doubleValue() * 100d);
        }
        if (modifier instanceof IChanceModifier.NormalChance) {
            Number chance = readField(modifier, "chance", Number.class);
            return chance == null ? CachedChanceModifier.DISABLED
                : new CachedChanceModifier(ModifierType.SET_CHANCE, null, -1, 0, chance.doubleValue() * 100d);
        }
        if (className.endsWith("Avaritia$AvaritiaSkullSwordModifier")) {
            return new CachedChanceModifier(ModifierType.AVARITIA_SKULL_SWORD, null, -1, 0, 0d);
        }
        if (className.endsWith("DraconicEvolution$DraconicEvolutionSoulChanceModifier")) {
            Number baseChance = readField(modifier, "baseChance", Number.class);
            return baseChance == null ? CachedChanceModifier.DISABLED
                : new CachedChanceModifier(ModifierType.DRACONIC_SOUL, null, -1, 0, baseChance.doubleValue() * 100d);
        }
        if (className.endsWith("TinkersConstruct$BeheadingModifier")) {
            Number chancePerLevel = readField(modifier, "m1", Number.class);
            return chancePerLevel == null ? CachedChanceModifier.DISABLED
                : new CachedChanceModifier(
                    ModifierType.TINKERS_BEHEADING,
                    null,
                    -1,
                    0,
                    chancePerLevel.doubleValue() * 100d);
        }
        if (className.endsWith("ForbiddenMagic$EachLevelOfGivesFocus") || modifier instanceof IChanceModifier.OrBiome) {
            return null;
        }
        return CachedChanceModifier.DISABLED;
    }

    private static <T> T readField(Object source, String fieldName, Class<T> expectedType) {
        for (Class<?> type = source.getClass(); type != null; type = type.getSuperclass()) {
            try {
                Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(source);
                return expectedType.isInstance(value) ? expectedType.cast(value) : null;
            } catch (NoSuchFieldException ignored) {
                // The standard modifiers keep shared fields on their parent classes.
            } catch (ReflectiveOperationException | LinkageError ignored) {
                return null;
            }
        }
        return null;
    }

    private static double evaluateChance(MobDrop drop, List<CachedChanceModifier> modifiers, WeaponEffects effects) {
        if (drop.playerOnly && !effects.hasWeapon()) return 0d;
        double chance = drop.chance;
        for (CachedChanceModifier modifier : modifiers) {
            chance = modifier.apply(chance, effects);
        }
        if (drop.lootable) chance += effects.getEnchantmentLevel(Enchantment.looting.effectId) * 5_000d;
        return Math.max(0d, chance);
    }

    private static int calculateDamageWeight(List<MobDrop> drops) {
        int totalWeight = 0;
        for (MobDrop drop : drops) {
            if (drop.damages == null) continue;
            for (int weight : drop.damages.values()) totalWeight += Math.max(0, weight);
        }
        return totalWeight;
    }

    private static double calculateDurabilityExpectation(MobDrop drop, int recipeDamageWeight) {
        ItemStack stack = drop.stack;
        if (!stack.isItemStackDamageable()) return 1d;
        int maximumDamage = stack.getMaxDamage();
        if (maximumDamage <= 0) return 1d;
        double templateDurability = getRemainingDurabilityRatio(maximumDamage, stack.getItemDamage());
        if (drop.damages == null || drop.damages.isEmpty() || recipeDamageWeight <= 0) return templateDurability;
        long cumulativeWeight = 0;
        long assignedRolls = 0;
        double weightedDurability = 0d;
        for (Map.Entry<Integer, Integer> damage : drop.damages.entrySet()) {
            int weight = Math.max(0, damage.getValue());
            if (weight == 0) continue;
            long previousCumulativeWeight = cumulativeWeight;
            cumulativeWeight += weight;
            long upperRoll = Math.min((long) recipeDamageWeight - 1, cumulativeWeight);
            long lowerRoll = previousCumulativeWeight == 0 ? 0 : previousCumulativeWeight + 1;
            long rollCount = Math.max(0, upperRoll - lowerRoll + 1);
            assignedRolls += rollCount;
            weightedDurability += getRemainingDurabilityRatio(maximumDamage, damage.getKey()) * rollCount;
        }
        long fallbackRolls = Math.max(0L, (long) recipeDamageWeight - assignedRolls);
        weightedDurability += templateDurability * fallbackRolls;
        return weightedDurability / recipeDamageWeight;
    }

    private static double getRemainingDurabilityRatio(int maximumDamage, int damage) {
        return Math.max(0d, Math.min(1d, (double) (maximumDamage - damage) / maximumDamage));
    }

    private static int getEecRecipeDuration(float maxEntityHealth) {
        return Math.max(EEC_MIN_DURATION, (int) ((maxEntityHealth / EEC_DIAMOND_SPIKES_DAMAGE) * 10d));
    }

    private static boolean isBoss(PreMobRegistrationEvent event) {
        return event.recipe.entity instanceof IBossDisplayData || event.recipe.entity.getClass()
            .getName()
            .contains(".entity.boss.");
    }

    private static String getLocalizedName(PreMobRegistrationEvent event) {
        String mobName = EntityList.getEntityString(event.recipe.entity);
        if ("Skeleton".equals(mobName) && event.recipe.entity instanceof EntitySkeleton
            && ((EntitySkeleton) event.recipe.entity).getSkeletonType() == 1) {
            return "Wither Skeleton";
        }
        String localizedName = event.recipe.entity.getCommandSenderName();
        return localizedName.startsWith("entity.") ? event.currentMob : localizedName;
    }

    @SubscribeEvent
    public void onPostMobsRegistration(PostMobsRegistrationEvent event) {
        PENDING_RECIPES.keySet()
            .retainAll(MobRecipe.MobNameToRecipeMap.keySet());
        List<PendingRecipe> sorted = new ArrayList<>(PENDING_RECIPES.values());
        sorted.sort(
            Comparator.comparing((PendingRecipe recipe) -> !"Minecraft".equals(recipe.modName()))
                .thenComparing(PendingRecipe::modName)
                .thenComparing(PendingRecipe::localizedName));
        Map<Integer, CachedRecipe> rebuilt = new LinkedHashMap<>();
        int id = 1;
        for (PendingRecipe pending : sorted) {
            rebuilt.put(
                id,
                new CachedRecipe(
                    id,
                    pending.mobName(),
                    pending.localizedName(),
                    pending.boss(),
                    pending.eecDuration(),
                    pending.outputs()));
            id++;
        }
        recipesById = Collections.unmodifiableMap(rebuilt);
        PENDING_RECIPES.clear();
        Map<Integer, DirectedMobClonerFakeRecipe.MobRecipeDisplay> displaysById = new LinkedHashMap<>();
        for (CachedRecipe recipe : recipesById.values()) {
            displaysById.put(
                recipe.id(),
                new DirectedMobClonerFakeRecipe.MobRecipeDisplay(
                    recipe.mobName(),
                    recipe.boss(),
                    recipe.firstOutput()));
        }
        DirectedMobClonerFakeRecipe.rebuildFakeRecipes(displaysById);
    }

    private static List<CachedOutput> immutableOutputs(List<CachedOutput> outputs) {
        return Collections.unmodifiableList(outputs);
    }

    public static CachedRecipe findRecipe(int recipeId) {
        return recipesById.get(recipeId);
    }

    public static Iterable<CachedRecipe> getDebugRecipes() {
        return recipesById.values();
    }

    private static final class PendingRecipe {

        private final String mobName;
        private final String localizedName;
        private final String modName;
        private final boolean boss;
        private final int eecDuration;
        private CachedOutputLists outputs = CachedOutputLists.EMPTY;

        private PendingRecipe(String mobName, String localizedName, String modName, boolean boss, int eecDuration) {
            this.mobName = mobName;
            this.localizedName = localizedName;
            this.modName = modName;
            this.boss = boss;
            this.eecDuration = eecDuration;
        }

        private String mobName() {
            return mobName;
        }

        private String localizedName() {
            return localizedName;
        }

        private String modName() {
            return modName;
        }

        private boolean boss() {
            return boss;
        }

        private int eecDuration() {
            return eecDuration;
        }

        private CachedOutputLists outputs() {
            return outputs;
        }

        private void setOutputs(CachedOutputLists outputs) {
            this.outputs = outputs;
        }
    }

    @Desugar
    public record CachedRecipe(int id, String mobName, String localizedName, boolean boss, int eecDuration,
        CachedOutputLists cachedOutputs) {

        public int baseTier() {
            return boss ? BOSS_RECIPE_BASE_TIER : NORMAL_RECIPE_BASE_TIER;
        }

        public List<CachedOutput> ordinaryOutputs() {
            return cachedOutputs.ordinaryOutputs();
        }

        public List<CachedOutput> equipmentOutputs(boolean pulverizeEquipment) {
            return pulverizeEquipment ? cachedOutputs.pulverizedOutputs() : cachedOutputs.pulverizableOutputs();
        }

        public List<CachedSpecialOutput> specialOutputs() {
            return cachedOutputs.specialOutputs();
        }

        private ItemStack firstOutput() {
            List<CachedOutput> outputs = cachedOutputs.ordinaryOutputs();
            if (outputs.isEmpty()) outputs = cachedOutputs.pulverizableOutputs();
            if (!outputs.isEmpty()) return outputs.get(0)
                .stack()
                .copy();
            List<CachedSpecialOutput> specialOutputs = cachedOutputs.specialOutputs();
            return specialOutputs.isEmpty() ? null
                : specialOutputs.get(0)
                    .stack()
                    .copy();
        }
    }

    @Desugar
    public record CachedOutput(ItemStack stack, double chance, double durabilityExpectation,
        double probabilityMultiplier) {}

    @Desugar
    public record CachedSpecialOutput(ItemStack stack, double baseChance, double durabilityExpectation,
        int originalChance, boolean playerOnly, boolean lootable, List<CachedChanceModifier> modifiers) {

        public double extraChance(WeaponEffects effects) {
            if (playerOnly && !effects.hasWeapon()) return 0d;
            double chance = originalChance;
            for (CachedChanceModifier modifier : modifiers) {
                chance = modifier.apply(chance, effects);
            }
            if (lootable) chance += effects.getEnchantmentLevel(Enchantment.looting.effectId) * 5_000d;
            return Math.max(0d, chance - baseChance);
        }
    }

    private enum ModifierType {
        SET_CHANCE,
        REQUIRE_WEAPON,
        OR_WEAPON,
        REQUIRE_ENCHANTMENT,
        ENCHANTMENT_BONUS,
        AVARITIA_SKULL_SWORD,
        DRACONIC_SOUL,
        TINKERS_BEHEADING,
        DISABLED
    }

    @Desugar
    private record CachedChanceModifier(ModifierType type, Item item, int enchantmentId, int enchantmentLevel,
        double value) {

        private static final CachedChanceModifier DISABLED = new CachedChanceModifier(
            ModifierType.DISABLED,
            null,
            -1,
            0,
            0d);

        private boolean weaponDependent() {
            return switch (type) {
                case REQUIRE_WEAPON, OR_WEAPON, REQUIRE_ENCHANTMENT, ENCHANTMENT_BONUS, AVARITIA_SKULL_SWORD, DRACONIC_SOUL, TINKERS_BEHEADING -> true;
                default -> false;
            };
        }

        private double apply(double chance, WeaponEffects effects) {
            return switch (type) {
                case SET_CHANCE -> value;
                case REQUIRE_WEAPON -> effects.hasWeapon(item) ? chance : 0d;
                case OR_WEAPON -> effects.hasWeapon(item) ? value : chance;
                case REQUIRE_ENCHANTMENT -> effects.getEnchantmentLevel(enchantmentId) >= enchantmentLevel ? chance
                    : 0d;
                case ENCHANTMENT_BONUS -> effects.getEnchantmentLevel(enchantmentId) > 0 ? chance + value : chance;
                case AVARITIA_SKULL_SWORD -> effects.hasAvaritiaSkullSword() ? chance : 0d;
                case DRACONIC_SOUL -> value * effects.getDraconicSoulMultiplier();
                case TINKERS_BEHEADING -> value * effects.getBeheadingLevel();
                case DISABLED -> 0d;
            };
        }
    }

    @Desugar
    private record CachedOutputLists(List<CachedOutput> ordinaryOutputs, List<CachedOutput> pulverizableOutputs,
        List<CachedOutput> pulverizedOutputs, List<CachedSpecialOutput> specialOutputs) {

        private static final CachedOutputLists EMPTY = new CachedOutputLists(
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList());
    }
}
