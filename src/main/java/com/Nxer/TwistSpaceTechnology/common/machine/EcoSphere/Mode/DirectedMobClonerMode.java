package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereSpecialUpgrade;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerRecipeCache;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerWeaponHandler;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerWeaponHandler.WeaponTags;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.DirectedMobClonerFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;

import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;

public final class DirectedMobClonerMode implements IEcoSphereMode {

    /** Looting X granted by the tier-2 cloning beacon. */
    private static final int LOOTING_T2_LEVEL = 10;
    /** Maximum looting level used anywhere in the yield bonus. */
    private static final int LOOTING_CAP_LEVEL = 10;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.DirectedMobClonerFakeRecipes;
    }

    @Override
    public String getDisplayName() {
        return translateToLocal("EcoSphereSimulator.modeMsg.3");
    }

    @Override
    public boolean displaysFluidArea() {
        return true;
    }

    @Override
    public EcoSphereModeResult process(TST_EcoSphereSimulator machine, int euTier) {
        int recipeId = machine.getCloningRecipeId();

        // Recipe #0 converts blood to life essence and runs on both structure tiers.
        if (recipeId == 0) {
            FluidStack bloodInput = DirectedMobClonerFakeRecipe.BLOOD_STACK;
            if (bloodInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
            return EcoSphereModeSupport.processModeRecipeWithTier(
                machine,
                bloodInput.getFluid(),
                bloodInput.amount,
                euTier,
                parallelResult -> createFallbackResult(machine, parallelResult));
        }

        FluidStack lifeEssenceInput = DirectedMobClonerFakeRecipe.LIFE_ESSENCE_STACK;
        if (lifeEssenceInput == null || !machine.isTierTwo())
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);

        DirectedMobClonerRecipeCache.CachedRecipe recipe = DirectedMobClonerRecipeCache.findRecipe(recipeId);
        if (recipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        boolean pulverize = machine.hasSpecialUpgrade(EcoSphereSpecialUpgrade.AUTO_PULVERIZE_EQUIPMENT);
        WeaponTags weaponTags = DirectedMobClonerWeaponHandler.process(machine.getCloningWeapons());
        // The Avaritia Cosmos sword counts as the tier-2 cloning beacon.
        boolean tierTwo = machine.hasDirectedMobClonerTierTwoBeacon()
            || weaponTags.get(DirectedMobClonerWeaponHandler.FunctionTag.HAS_COSMOS) > 0;
        // Boss recipes additionally require the tier-2 cloning beacon.
        if (recipe.boss() && !tierTwo) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);

        // Looting X semantics: the tier-2 beacon and the Cosmos sword (inherent Looting X) both grant it; any source
        // caps at Looting X.
        double lootingBonus = weaponTags.get(DirectedMobClonerWeaponHandler.FunctionTag.ALL_OUTPUTS_CHANCE_BONUS);
        double tierTwoBonus = tierTwo ? LOOTING_T2_LEVEL * 5_000d : 0;
        double allOutputsBonus = Math.min(LOOTING_CAP_LEVEL * 5_000d, Math.max(lootingBonus, tierTwoBonus));
        // 36x EEC at 20 HP, about 4x at 300 HP each run in 5s.
        double durationMultiplier = 36 * 15125d / 1024d / ((double) recipe.eecDuration() * recipe.eecDuration());
        return EcoSphereModeSupport.processModeRecipeWithTier(
            machine,
            lifeEssenceInput.getFluid(),
            lifeEssenceInput.amount,
            euTier,
            parallelResult -> {
                List<ItemStack> outputs = new ArrayList<>();
                for (int tableIndex = 0; tableIndex < 2; tableIndex++) {
                    List<DirectedMobClonerRecipeCache.CachedOutput> outputTable = tableIndex == 0
                        ? recipe.ordinaryOutputs()
                        : recipe.equipmentOutputs(pulverize);
                    for (DirectedMobClonerRecipeCache.CachedOutput output : outputTable) {
                        double outputAmount = output.stack().stackSize * (double) parallelResult.parallel()
                            * (output.chance() + allOutputsBonus)
                            / 10_000d
                            * durationMultiplier
                            * output.durabilityExpectation()
                            * output.probabilityMultiplier();
                        long amount = outputAmount >= Long.MAX_VALUE ? Long.MAX_VALUE : (long) outputAmount;
                        EcoSphereModeSupport.addSplitStack(outputs, output.stack(), amount);
                    }
                }
                for (DirectedMobClonerRecipeCache.CachedOutput output : recipe.activatedOutputs(weaponTags)) {
                    double outputAmount = output.stack().stackSize * (double) parallelResult.parallel()
                        * output.chance()
                        / 10_000d
                        * durationMultiplier
                        * output.durabilityExpectation();
                    long amount;
                    if (outputAmount >= Long.MAX_VALUE) {
                        amount = Long.MAX_VALUE;
                    } else {
                        amount = (long) outputAmount;
                        // Preserve low-probability special drops without rolling once for every parallel operation.
                        if (XSTR.XSTR_INSTANCE.nextDouble() < outputAmount - amount) amount++;
                    }
                    EcoSphereModeSupport.addSplitStack(outputs, output.stack(), amount);
                }
                return EcoSphereModeResult.standard(
                    // #tr GT5U.gui.text.recipe_result.processing_mob_drops
                    // # Processing mob drops
                    // #zh_CN 生物掉落处理中
                    SimpleCheckRecipeResult.ofSuccess("processing_mob_drops"),
                    outputs.toArray(new ItemStack[0]),
                    parallelResult.tier());
            });
    }

    private static EcoSphereModeResult createFallbackResult(TST_EcoSphereSimulator machine,
        EcoSphereModeSupport.ParallelResult parallelResult) {
        FluidStack outputTemplate = DirectedMobClonerFakeRecipe.FALLBACK_LIFE_ESSENCE_OUTPUT_STACK;
        if (outputTemplate == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
        int outputAmount = Integer.MAX_VALUE;
        if (parallelResult.parallel() <= Integer.MAX_VALUE / outputTemplate.amount)
            outputAmount = (int) (outputTemplate.amount * parallelResult.parallel());
        FluidStack lifeEssence = BloodMagicHelper.getLifeEssence(outputAmount);
        if (lifeEssence == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
        // #tr GT5U.gui.text.recipe_result.generating_life_essence
        // # Generating Life Essence
        // #zh_CN 生命本源生成中

        // #tr EcoSphereSimulator.gui.tierOneCloningRecipe
        // # Tier I Structure: Recipe Number 0 Only
        // #zh_CN 一级结构: 仅执行配方编号 0
        CheckRecipeResult runningResult;
        if (machine.isTierTwo()) {
            runningResult = SimpleCheckRecipeResult.ofSuccess("generating_life_essence");
        } else {
            runningResult = SimpleResultWithText.ofSuccessText(
                translateToLocal("GT5U.gui.text.recipe_result.generating_life_essence") + "\n"
                    + translateToLocal("EcoSphereSimulator.gui.tierOneCloningRecipe"));
        }
        return EcoSphereModeResult
            .standard(runningResult, new ItemStack[0], new FluidStack[] { lifeEssence }, parallelResult.tier());
    }

}
