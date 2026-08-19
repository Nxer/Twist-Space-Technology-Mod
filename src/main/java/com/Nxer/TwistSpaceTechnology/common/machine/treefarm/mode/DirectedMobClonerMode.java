package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereFluidCache;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.DirectedMobClonerFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;

public final class DirectedMobClonerMode implements IEcoSphereMode {

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
        FluidStack fluidInput = EcoSphereFluidCache.findFirstValidFluid(machine);
        if (fluidInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        Fluid inputFluid = fluidInput.getFluid();
        FluidStack bloodInput = DirectedMobClonerFakeRecipe.BLOOD_STACK;
        if (bloodInput != null && inputFluid == bloodInput.getFluid())
            return processFallback(machine, bloodInput, euTier);

        FluidStack lifeEssenceInput = DirectedMobClonerFakeRecipe.LIFE_ESSENCE_STACK;
        if (lifeEssenceInput == null || inputFluid != lifeEssenceInput.getFluid() || !machine.isTierTwo())
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);

        // Numbered recipes are selected only after life essence wins the shared fluid scan.
        int recipeId = machine.getCloningRecipeId();
        DirectedMobClonerRecipeCache.CachedRecipe recipe = DirectedMobClonerRecipeCache.findRecipe(recipeId);
        if (recipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        boolean tierTwoBeacon = machine.hasDirectedMobClonerTierTwoBeacon();
        // Boss recipes additionally require the upgraded cloning beacon.
        if (recipe.boss() && !tierTwoBeacon) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);
        int baseTier = recipe.baseTier();
        int overclocks = euTier - baseTier;
        if (overclocks < 0)
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.insufficientPower(GTValues.V[baseTier]));
        long parallelFromEUt = EcoSphereModeSupport.powerOfFour(overclocks);
        return EcoSphereModeSupport.processRecipeWithParallelLimit(
            machine,
            lifeEssenceInput.getFluid(),
            lifeEssenceInput.amount,
            euTier,
            parallelFromEUt,
            parallelResult -> DirectedMobClonerRecipeCache
                .process(machine, recipe, parallelResult.parallel(), parallelFromEUt));
    }

    private static EcoSphereModeResult processFallback(TST_EcoSphereSimulator machine, FluidStack bloodInput,
        int euTier) {
        return EcoSphereModeSupport.processModeRecipeWithTier(
            machine,
            bloodInput.getFluid(),
            bloodInput.amount,
            euTier,
            parallelResult -> createFallbackResult(machine, parallelResult));
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
