package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
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
    public EcoSphereModeResult process(TST_MegaTreeFarm machine, int euTier) {
        if (hasDebugItem(machine)) return processDebug(machine);
        machine.resetDirectedMobClonerDebugRun();

        // Missing and invalid circuit sums both use the fallback life-essence recipe.
        int recipeId = TstUtils.getIntegratedCircuitConfigurationSum(machine.getStoredInputs());
        if (!DirectedMobClonerRecipeCache.isValidRecipeId(recipeId)) return processFallback(machine, euTier);
        // Numbered cloning recipes require the tier-two structure.
        if (!machine.isTierTwo())
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.insufficientMachineTier(2));
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
            EcoSphereModeSupport.missingFluid(lifeEssenceFluid, DirectedMobClonerFakeRecipe.LIFE_ESSENCE_PER_PARALLEL));
        return DirectedMobClonerRecipeCache
            .process(machine, recipeId, overclockResult.tier(), overclockResult.multiplier(), infiniteUpgrade);
    }

    private static EcoSphereModeResult processFallback(TST_MegaTreeFarm machine, int euTier) {
        Fluid lifeEssenceFluid = FluidRegistry.getFluid("lifeessence");
        long fluidPerParallel = machine
            .applyStructureFluidDiscount(DirectedMobClonerFakeRecipe.LIFE_ESSENCE_PER_PARALLEL);
        EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport
            .consumeFluidForParallel(machine, lifeEssenceFluid, fluidPerParallel, euTier);
        // The lowest power tier runs two parallels, so startup requires twice the per-parallel life essence.
        if (parallelResult == null) return EcoSphereModeResult
            .failure(EcoSphereModeSupport.missingFluid(lifeEssenceFluid, fluidPerParallel * 2));

        FluidStack lifeEssence = BloodMagicHelper
            .getLifeEssence((int) Math.min(Integer.MAX_VALUE, parallelResult.fluidCost()));
        if (lifeEssence == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
        return EcoSphereModeResult.standard(
            SimpleCheckRecipeResult.ofSuccess("generating_life_essence"),
            new ItemStack[0],
            new FluidStack[] { lifeEssence },
            parallelResult.tier());
    }

    private static EcoSphereModeResult processDebug(TST_MegaTreeFarm machine) {
        int recipeId = machine.beginDirectedMobClonerDebugRun();
        EcoSphereModeResult result = DirectedMobClonerRecipeCache.processDebug(machine, recipeId);
        if (result.result()
            .wasSuccessful()) {
            machine.advanceDirectedMobClonerDebugRun(recipeId >= DirectedMobClonerRecipeCache.getLastRecipeId());
        }
        return result;
    }

    private static boolean hasDebugItem(TST_MegaTreeFarm machine) {
        ItemStack debugItem = GTCMItemList.TestItem0.get(1);
        for (ItemStack input : machine.getStoredInputs()) {
            if (input != null && input.isItemEqual(debugItem)) return true;
        }
        return false;
    }

}
