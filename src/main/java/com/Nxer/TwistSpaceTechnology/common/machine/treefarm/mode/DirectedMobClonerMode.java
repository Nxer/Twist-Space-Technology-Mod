package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.DirectedMobClonerFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import gregtech.api.recipe.RecipeMap;
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
        if (hasDebugItem(machine)) return processDebug(machine);
        machine.resetDirectedMobClonerDebugRun();

        // The tier-one structure always runs recipe zero, regardless of circuit input.
        if (!machine.isTierTwo()) return processFallback(machine, euTier);

        // Missing and invalid circuit sums both use the fallback life-essence recipe.
        int recipeId = TstUtils.getIntegratedCircuitConfigurationSum(machine.getStoredInputs());
        if (!DirectedMobClonerRecipeCache.isValidRecipeId(recipeId)) return processFallback(machine, euTier);
        boolean infiniteUpgrade = machine.hasDirectedMobClonerInfiniteUpgrade();
        boolean bossRecipe = DirectedMobClonerRecipeCache.isBossRecipe(recipeId);
        // Boss recipes additionally require the upgraded cloning beacon.
        if (bossRecipe && !infiniteUpgrade) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);
        int voltageTier = (int) Math.floor(TstUtils.calculateVoltageTier(machine.getAvailableInputPower()));
        if (infiniteUpgrade) voltageTier += 4;
        // Every tier above the recipe offset adds one perfect overclock.
        int tierOffset = bossRecipe ? 6 : 4;
        int maximumOverclocks = Math.max(0, voltageTier - tierOffset);
        Fluid lifeEssenceFluid = FluidRegistry.getFluid("lifeessence");
        EcoSphereModeSupport.PerfectOverclockResult overclockResult = EcoSphereModeSupport
            .consumeFluidForPerfectOverclock(
                machine,
                lifeEssenceFluid,
                DirectedMobClonerFakeRecipe.LIFE_ESSENCE_PER_PARALLEL,
                maximumOverclocks);
        if (overclockResult == null) return EcoSphereModeResult.failure(
            EcoSphereModeSupport
                .missingFluid(machine, lifeEssenceFluid, DirectedMobClonerFakeRecipe.LIFE_ESSENCE_PER_PARALLEL));
        return DirectedMobClonerRecipeCache
            .process(machine, recipeId, overclockResult.tier(), overclockResult.multiplier(), infiniteUpgrade);
    }

    private static EcoSphereModeResult processFallback(TST_EcoSphereSimulator machine, int euTier) {
        Fluid bloodFluid = FluidRegistry.getFluid("blood");
        long fluidPerParallel = machine
            .applyStructureFluidDiscount(DirectedMobClonerFakeRecipe.FALLBACK_BLOOD_PER_PARALLEL);
        EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport
            .consumeFluidForParallel(machine, bloodFluid, fluidPerParallel, euTier);
        // The lowest power tier runs two parallels, so startup requires twice the per-parallel blood.
        if (parallelResult == null) return EcoSphereModeResult
            .failure(EcoSphereModeSupport.missingFluid(machine, bloodFluid, fluidPerParallel * 2));

        long outputAmount = parallelResult.parallel()
            > Integer.MAX_VALUE / DirectedMobClonerFakeRecipe.FALLBACK_LIFE_ESSENCE_OUTPUT_PER_PARALLEL
                ? Integer.MAX_VALUE
                : parallelResult.parallel() * DirectedMobClonerFakeRecipe.FALLBACK_LIFE_ESSENCE_OUTPUT_PER_PARALLEL;
        FluidStack lifeEssence = BloodMagicHelper.getLifeEssence((int) outputAmount);
        if (lifeEssence == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
        // #tr GT5U.gui.text.recipe_result.generating_life_essence
        // # Generating Life Essence
        // #zh_CN 生命本源生成中

        // #tr EcoSphereSimulator.gui.tierOneCloningRecipe
        // # Tier I Structure: Recipe Number 0 Only
        // #zh_CN 一级结构: 仅执行配方编号 0
        return EcoSphereModeResult.standard(
            machine.isTierTwo() ? SimpleCheckRecipeResult.ofSuccess("generating_life_essence")
                : SimpleResultWithText.ofSuccessText(
                    translateToLocal("GT5U.gui.text.recipe_result.generating_life_essence") + "\n"
                        + translateToLocal("EcoSphereSimulator.gui.tierOneCloningRecipe")),
            new ItemStack[0],
            new FluidStack[] { lifeEssence },
            parallelResult.tier());
    }

    private static EcoSphereModeResult processDebug(TST_EcoSphereSimulator machine) {
        int recipeId = machine.beginDirectedMobClonerDebugRun();
        EcoSphereModeResult result = DirectedMobClonerRecipeCache.processDebug(machine, recipeId);
        if (result.result()
            .wasSuccessful()) {
            machine.advanceDirectedMobClonerDebugRun(recipeId >= DirectedMobClonerRecipeCache.getLastRecipeId());
        }
        return result;
    }

    private static boolean hasDebugItem(TST_EcoSphereSimulator machine) {
        ItemStack debugItem = GTCMItemList.TestItem0.get(1);
        for (ItemStack input : machine.getStoredInputs()) {
            if (input != null && input.isItemEqual(debugItem)) return true;
        }
        return false;
    }

}
