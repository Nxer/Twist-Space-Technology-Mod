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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerDropConversion.ConvertedOutput;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerWeaponHandler.FunctionTag;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerWeaponHandler.WeaponTags;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.DirectedMobClonerFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
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
import cpw.mods.fml.common.registry.GameRegistry;

public final class DirectedMobClonerRecipeCache {

    private static final int EEC_MIN_DURATION = 55;
    private static final int NORMAL_RECIPE_BASE_TIER = 4;
    private static final int BOSS_RECIPE_BASE_TIER = 6;
    private static final double EEC_DIAMOND_SPIKES_DAMAGE = 9d;
    private static final WeaponTags NO_WEAPON_TAGS = DirectedMobClonerWeaponHandler.process(new ItemStack[0]);
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
        Map<TST_ItemID, SpecialOutputBuilder> specialOutputs = new LinkedHashMap<>();
        ItemStack firstBaseOutput = null;
        int damageWeight = calculateDamageWeight(sourceDrops);
        for (MobDrop drop : sourceDrops) {
            if (drop.stack == null || drop.stack.getItem() == null) continue;

            double durabilityExpectation = calculateDurabilityExpectation(drop, damageWeight);
            List<CachedChanceModifier> modifiers = cacheChanceModifiers(drop);
            boolean weaponDependent = drop.playerOnly;
            for (CachedChanceModifier modifier : modifiers) weaponDependent |= modifier.weaponDependent();

            ItemStack outputTemplate = sanitizeCachedDrop(drop.stack);
            double baseChance = evaluateChance(drop, modifiers, NO_WEAPON_TAGS);
            if (baseChance > 0d) {
                if (firstBaseOutput == null && !NeiDisplayExclusion.contains(outputTemplate)) {
                    firstBaseOutput = outputTemplate.copy();
                }
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
                CachedActivationRoute activation = new CachedActivationRoute(
                    baseChance,
                    durabilityExpectation,
                    drop.chance,
                    drop.playerOnly,
                    modifiers);
                specialOutputs
                    .computeIfAbsent(
                        TST_ItemID.create(outputTemplate),
                        ignored -> new SpecialOutputBuilder(outputTemplate))
                    .add(activation);
            }
        }

        List<CachedSpecialOutput> special = new ArrayList<>(specialOutputs.size());
        for (SpecialOutputBuilder output : specialOutputs.values()) special.add(output.build());

        return new CachedOutputLists(
            immutableOutputs(ordinaryOutputs),
            immutableOutputs(pulverizableOutputs),
            immutableOutputs(pulverizedOutputs),
            Collections.unmodifiableList(special),
            firstBaseOutput);
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

    private static List<CachedChanceModifier> cacheChanceModifiers(MobDrop drop) {
        List<IChanceModifier> sourceModifiers = drop.chanceModifiers;
        if (sourceModifiers == null || sourceModifiers.isEmpty()) return Collections.emptyList();
        List<CachedChanceModifier> modifiers = new ArrayList<>(sourceModifiers.size());
        for (IChanceModifier modifier : sourceModifiers) {
            CachedChanceModifier cached = cacheChanceModifier(modifier, drop.stack);
            if (cached != null) modifiers.add(cached);
        }
        return Collections.unmodifiableList(modifiers);
    }

    private static CachedChanceModifier cacheChanceModifier(IChanceModifier modifier, ItemStack output) {
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
        if (modifierClass == IChanceModifier.DropsOnlyInDimension.class) {
            Number dimension = readField(modifier, "dimension", Number.class);
            if (dimension == null) return CachedChanceModifier.DISABLED;
            if (dimension.intValue() == 1) return new CachedChanceModifier(ModifierType.END_DROP, null, -1, 0, 0d);
            if (dimension.intValue() == -1) return new CachedChanceModifier(ModifierType.NETHER_DROP, null, -1, 0, 0d);
            return CachedChanceModifier.DISABLED;
        }
        if (output != null && output.getItem() == Items.skull
            && "BeheadingModifier".equals(modifierClass.getSimpleName())) {
            Number chancePerLevel = readField(modifier, "m1", Number.class);
            Number maximumChance = readField(modifier, "m2", Number.class);
            if (chancePerLevel == null || maximumChance == null) return CachedChanceModifier.DISABLED;
            double chance = chancePerLevel.doubleValue() * 100d;
            return new CachedChanceModifier(ModifierType.TINKERS_BEHEADING, null, -1, 0, chance);
        }
        if (className.endsWith("ForbiddenMagic$NonPlayerEntity")
            || className.endsWith("BloodMagic$MinorDemonGruntShards")) {
            return new CachedChanceModifier(ModifierType.PASS_THROUGH, null, -1, 0, 0d);
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

    private static double evaluateChance(MobDrop drop, List<CachedChanceModifier> modifiers, WeaponTags tags) {
        if (drop.playerOnly && !tags.hasWeapon()) return 0d;
        double chance = drop.chance;
        for (CachedChanceModifier modifier : modifiers) {
            chance = modifier.apply(chance, tags);
        }
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

        public List<CachedOutput> activatedOutputs(WeaponTags tags) {
            List<CachedOutput> outputs = new ArrayList<>();
            double allOutputsBonus = tags.get(FunctionTag.ALL_OUTPUTS_CHANCE_BONUS);
            for (CachedSpecialOutput output : cachedOutputs.specialOutputs()) {
                CachedOutput activated = output.activate(tags);
                if (activated == null) continue;
                double chance = activated.chance();
                if (!output.hasBaseOutput()) chance += allOutputsBonus;
                outputs.add(new CachedOutput(activated.stack(), chance, activated.durabilityExpectation(), 1d));
            }
            return outputs;
        }

        private ItemStack firstOutput() {
            ItemStack output = cachedOutputs.firstBaseOutput();
            return output == null ? null : output.copy();
        }
    }

    @Desugar
    public record CachedOutput(ItemStack stack, double chance, double durabilityExpectation,
        double probabilityMultiplier) {}

    @Desugar
    private record CachedSpecialOutput(ItemStack stack, boolean hasBaseOutput,
        List<CachedActivationRoute> activations) {

        private CachedOutput activate(WeaponTags tags) {
            CachedActivationRoute strongest = null;
            double highestChance = 0d;
            for (CachedActivationRoute activation : activations) {
                double chance = activation.extraChance(tags);
                if (chance <= highestChance) continue;
                highestChance = chance;
                strongest = activation;
            }
            return strongest == null ? null
                : new CachedOutput(stack, highestChance, strongest.durabilityExpectation(), 1d);
        }
    }

    @Desugar
    private record CachedActivationRoute(double baseChance, double durabilityExpectation, int originalChance,
        boolean playerOnly, List<CachedChanceModifier> modifiers) {

        private double extraChance(WeaponTags tags) {
            if (playerOnly && !tags.hasWeapon()) return 0d;
            double chance = originalChance;
            for (CachedChanceModifier modifier : modifiers) {
                chance = modifier.apply(chance, tags);
            }
            return Math.max(0d, chance - baseChance);
        }
    }

    private static final class SpecialOutputBuilder {

        private final ItemStack stack;
        private final List<CachedActivationRoute> activations = new ArrayList<>();
        private boolean hasBaseOutput;

        private SpecialOutputBuilder(ItemStack stack) {
            this.stack = stack;
        }

        private SpecialOutputBuilder add(CachedActivationRoute activation) {
            hasBaseOutput |= activation.baseChance() > 0d;
            activations.add(activation);
            return this;
        }

        private CachedSpecialOutput build() {
            return new CachedSpecialOutput(
                stack,
                hasBaseOutput,
                Collections.unmodifiableList(new ArrayList<>(activations)));
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
        END_DROP,
        NETHER_DROP,
        PASS_THROUGH,
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
                case REQUIRE_WEAPON, OR_WEAPON, REQUIRE_ENCHANTMENT, ENCHANTMENT_BONUS, AVARITIA_SKULL_SWORD, DRACONIC_SOUL, TINKERS_BEHEADING, END_DROP, NETHER_DROP -> true;
                default -> false;
            };
        }

        private double apply(double chance, WeaponTags tags) {
            return switch (type) {
                case SET_CHANCE -> value;
                case REQUIRE_WEAPON -> tags.hasWeapon(item) ? chance : 0d;
                case OR_WEAPON -> tags.hasWeapon(item) ? value : chance;
                case REQUIRE_ENCHANTMENT -> tags.getEnchantmentLevel(enchantmentId) >= enchantmentLevel ? chance : 0d;
                case ENCHANTMENT_BONUS -> tags.getEnchantmentLevel(enchantmentId) > 0 ? chance + value : chance;
                case AVARITIA_SKULL_SWORD -> tags.get(FunctionTag.AVARITIA_SKULL_CHANCE);
                case DRACONIC_SOUL -> value * tags.get(FunctionTag.DRACONIC_SOUL_MULTIPLIER);
                case TINKERS_BEHEADING -> value * tags.get(FunctionTag.TINKERS_BEHEADING_LEVEL);
                case END_DROP -> tags.get(FunctionTag.END_DROP_CHANCE) > 0d ? chance : 0d;
                case NETHER_DROP -> tags.get(FunctionTag.NETHER_DROP_CHANCE) > 0d ? chance : 0d;
                case PASS_THROUGH -> chance;
                case DISABLED -> 0d;
            };
        }
    }

    private enum NeiDisplayExclusion {

        CRYSTALLIZED_ESSENCE("Thaumcraft", "ItemCrystalEssence"),
        OPENBLOCKS_TROPHY("OpenBlocks", "trophy"),
        WEAK_BLOOD_SHARD("AWWayofTime", "weakBloodShard"),
        DRACONIC_MOB_SOUL("DraconicEvolution", "mobSoul"),
        FORBIDDEN_EMERALD_FRAGMENT("ForbiddenMagic", "FMResource", 0),
        FORBIDDEN_SIN_SHARDS("ForbiddenMagic", "NetherShard"),
        WITCHERY_TORN_PAGE("witchery", "ingredient", 160),
        TCONSTRUCT_RED_HEART("TConstruct", "heartCanister", 1),
        TCONSTRUCT_YELLOW_HEART("TConstruct", "heartCanister", 3),
        ETFUTURUM_WITHER_ROSE("etfuturum", "wither_rose"),
        EXTRA_UTILITIES_SOUL_FRAGMENT("ExtraUtilities", "mini-soul", 3);

        private static final int ANY_META = -1;

        private final String modId;
        private final String registryName;
        private final int metadata;

        NeiDisplayExclusion(String modId, String registryName) {
            this(modId, registryName, ANY_META);
        }

        NeiDisplayExclusion(String modId, String registryName, int metadata) {
            this.modId = modId;
            this.registryName = registryName;
            this.metadata = metadata;
        }

        private static boolean contains(ItemStack stack) {
            GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(stack.getItem());
            if (identifier == null) return false;
            for (NeiDisplayExclusion exclusion : values()) {
                if (!exclusion.modId.equalsIgnoreCase(identifier.modId)) continue;
                if (!exclusion.registryName.equals(identifier.name)) continue;
                if (exclusion.metadata == ANY_META || exclusion.metadata == stack.getItemDamage()) return true;
            }
            return false;
        }
    }

    @Desugar
    private record CachedOutputLists(List<CachedOutput> ordinaryOutputs, List<CachedOutput> pulverizableOutputs,
        List<CachedOutput> pulverizedOutputs, List<CachedSpecialOutput> specialOutputs, ItemStack firstBaseOutput) {

        private static final CachedOutputLists EMPTY = new CachedOutputLists(
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            null);
    }
}
