package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.MODE_RECIPE_DURATION;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.DirectedMobClonerFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.github.bsideup.jabel.Desugar;
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
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;

public final class DirectedMobClonerRecipeCache {

    private static final int EEC_MIN_DURATION = 55;
    private static final int MOB_INFO_CHANCE_SCALE = 10_000;
    private static final long EEC_RECIPE_EUT = 1920L;
    private static final double EEC_DIAMOND_SPIKES_DAMAGE = 9d;
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
        pending.setDrops(buildCachedDrops(event.drops));
    }

    private static List<CachedDrop> buildCachedDrops(List<MobDrop> sourceDrops) {
        List<CachedDrop> drops = new ArrayList<>();
        int damageWeight = calculateDamageWeight(sourceDrops);
        for (MobDrop drop : sourceDrops) {
            if (drop.stack == null || drop.stack.getItem() == null
                || drop.playerOnly
                || DropWhenExclusion.contains(drop.stack)) continue;
            double durabilityExpectation = calculateDurabilityExpectation(drop, damageWeight);
            ItemStack sanitized = sanitizeCachedDrop(drop.stack);
            DirectedMobClonerDropConversion.ConversionResult conversion = DirectedMobClonerDropConversion
                .convert(sanitized);
            if (!conversion.matched()) {
                if (sanitized.isItemStackDamageable()) sanitized.setItemDamage(0);
                drops.add(
                    new CachedDrop(
                        sanitized,
                        Math.max(0, Math.min(MOB_INFO_CHANCE_SCALE, drop.chance)),
                        durabilityExpectation,
                        1d));
                continue;
            }
            for (DirectedMobClonerDropConversion.ConvertedOutput converted : conversion.outputs()) {
                drops.add(
                    new CachedDrop(
                        converted.stack(),
                        Math.max(0, Math.min(MOB_INFO_CHANCE_SCALE, drop.chance)),
                        durabilityExpectation,
                        converted.probabilityMultiplier()));
            }
        }
        return Collections.unmodifiableList(drops);
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

    private static ItemStack sanitizeCachedDrop(ItemStack source) {
        ItemStack sanitized = source.copy();
        NBTTagCompound tag = sanitized.getTagCompound();
        if (tag == null) return sanitized;
        tag.removeTag("ench");
        tag.removeTag("StoredEnchantments");
        tag.removeTag("display");
        if (tag.hasNoTags()) sanitized.setTagCompound(null);
        return sanitized;
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
                    pending.drops()));
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

    public static int getLastRecipeId() {
        return recipesById.isEmpty() ? 0 : recipesById.size();
    }

    public static boolean isBossRecipe(int recipeId) {
        CachedRecipe recipe = recipesById.get(recipeId);
        return recipe != null && recipe.boss();
    }

    public static boolean isValidRecipeId(int recipeId) {
        return recipeId >= 1 && recipeId <= getLastRecipeId();
    }

    public static EcoSphereModeResult process(TST_MegaTreeFarm machine, int recipeId, int effectiveTier,
        long multiplier, boolean infiniteUpgrade) {
        return process(machine, recipeId, effectiveTier, multiplier, infiniteUpgrade, MODE_RECIPE_DURATION);
    }

    public static EcoSphereModeResult processDebug(TST_MegaTreeFarm machine, int recipeId) {
        boolean infiniteUpgrade = machine.hasDirectedMobClonerInfiniteUpgrade();
        if (isBossRecipe(recipeId) && !infiniteUpgrade) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);
        int voltageTier = (int) Math.floor(TstUtils.calculateVoltageTier(machine.getAvailableInputPower()));
        if (infiniteUpgrade) voltageTier += 4;
        int overclocks = Math.max(0, voltageTier - (isBossRecipe(recipeId) ? 6 : 4));
        return process(machine, recipeId, overclocks, EcoSphereModeSupport.powerOfFour(overclocks), infiniteUpgrade, 5);
    }

    private static EcoSphereModeResult process(TST_MegaTreeFarm machine, int recipeId, int effectiveTier,
        long multiplier, boolean infiniteUpgrade, int duration) {
        CachedRecipe recipe = recipesById.get(recipeId);
        if (recipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        if (recipe.boss() && !infiniteUpgrade) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);

        List<ItemStack> outputs = new ArrayList<>();
        double durationMultiplier = 100 / ((double) recipe.eecDuration() * recipe.eecDuration());
        for (CachedDrop drop : recipe.drops()) {
            double outputAmount = drop.stack().stackSize * (double) multiplier
                * drop.chance()
                / MOB_INFO_CHANCE_SCALE
                * durationMultiplier
                * drop.durabilityExpectation()
                * drop.probabilityMultiplier();
            long amount = outputAmount >= Long.MAX_VALUE ? Long.MAX_VALUE : (long) outputAmount;
            EcoSphereModeSupport.addSplitStack(outputs, drop.stack(), amount);
        }
        return new EcoSphereModeResult(
            SimpleCheckRecipeResult.ofSuccess("processing_mob_drops"),
            outputs.toArray(new ItemStack[0]),
            multiplySaturated(EEC_RECIPE_EUT, multiplier),
            duration);
    }

    private static long multiplySaturated(long value, long multiplier) {
        if (value <= 0 || multiplier <= 0) return 0;
        if (value > Long.MAX_VALUE / multiplier) return Long.MAX_VALUE;
        return value * multiplier;
    }

    private enum DropWhenExclusion {

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

        DropWhenExclusion(String modId, String registryName) {
            this(modId, registryName, ANY_META);
        }

        DropWhenExclusion(String modId, String registryName, int metadata) {
            this.modId = modId;
            this.registryName = registryName;
            this.metadata = metadata;
        }

        private static boolean contains(ItemStack stack) {
            if (stack == null || stack.getItem() == null) return false;
            GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(stack.getItem());
            if (identifier == null) return false;
            for (DropWhenExclusion exclusion : values()) {
                if (!exclusion.modId.equalsIgnoreCase(identifier.modId)) continue;
                if (!exclusion.registryName.equals(identifier.name)) continue;
                if (exclusion.metadata == ANY_META || exclusion.metadata == stack.getItemDamage()) return true;
            }
            return false;
        }
    }

    private static final class PendingRecipe {

        private final String mobName;
        private final String localizedName;
        private final String modName;
        private final boolean boss;
        private final int eecDuration;
        private List<CachedDrop> drops = Collections.emptyList();

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

        private List<CachedDrop> drops() {
            return drops;
        }

        private void setDrops(List<CachedDrop> drops) {
            this.drops = drops;
        }
    }

    @Desugar
    private record CachedRecipe(int id, String mobName, String localizedName, boolean boss, int eecDuration,
        List<CachedDrop> drops) {

        private ItemStack firstOutput() {
            return drops.isEmpty() ? null
                : drops.get(0)
                    .stack()
                    .copy();
        }
    }

    @Desugar
    private record CachedDrop(ItemStack stack, int chance, double durabilityExpectation,
        double probabilityMultiplier) {}

}
