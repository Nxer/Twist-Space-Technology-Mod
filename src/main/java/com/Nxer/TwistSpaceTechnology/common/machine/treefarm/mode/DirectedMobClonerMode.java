package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.common.items.ItemIntegratedCircuit;

import static net.minecraft.util.StatCollector.translateToLocal;

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
    public EcoSphereModeResult process(TST_MegaTreeFarm machine, int euTier) {
        if (hasDebugItem(machine)) return processDebug(machine, euTier);
        machine.resetDirectedMobClonerDebugRun();
        if (!hasIntegratedCircuit(machine))
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_correct_Circuit"));
        int recipeId = TstUtils.getIntegratedCircuitConfigurationSum(machine.getStoredInputs());
        if (recipeId == 0) {
            EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport
                .consumeFluidForParallel(machine, FluidRegistry.WATER, 1000L, euTier);
            if (parallelResult == null)
                return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_enough_input"));
            long lifeEssenceAmount = machine.isTierTwo() ? parallelResult.fluidCost()
                : (10L + XSTR.XSTR_INSTANCE.nextInt(991)) * parallelResult.parallel();
            FluidStack lifeEssence = BloodMagicHelper
                .getLifeEssence((int) Math.min(Integer.MAX_VALUE, lifeEssenceAmount));
            if (lifeEssence == null) return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_recipe"));
            return EcoSphereModeResult.standard(
                SimpleCheckRecipeResult.ofSuccess("generating_life_essence"),
                new ItemStack[0],
                new FluidStack[] { lifeEssence },
                parallelResult.tier());
        }
        if (!machine.isTierTwo())
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("mega_tree_farm_tier_two_required"));
        boolean infiniteUpgrade = machine.hasDirectedMobClonerInfiniteUpgrade();
        boolean bossRecipe = DirectedMobClonerRecipeCache.isBossRecipe(recipeId);
        if (bossRecipe && !infiniteUpgrade)
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("boss_upgrade_required"));
        int voltageTier = (int) Math.floor(TstUtils.calculateVoltageTier(machine.getAvailableInputPower()));
        if (infiniteUpgrade) voltageTier += 4;
        int tierOffset = bossRecipe ? 6 : 4;
        int maximumOverclocks = Math.max(0, voltageTier - tierOffset);
        EcoSphereModeSupport.PerfectOverclockResult overclockResult = EcoSphereModeSupport
            .consumeFluidForPerfectOverclock(machine, FluidRegistry.getFluid("lifeessence"), 100L, maximumOverclocks);
        if (overclockResult == null)
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_enough_input"));
        return DirectedMobClonerRecipeCache
            .process(machine, recipeId, overclockResult.tier(), overclockResult.multiplier(), infiniteUpgrade);
    }

    private static EcoSphereModeResult processDebug(TST_MegaTreeFarm machine, int euTier) {
        int recipeId = machine.beginDirectedMobClonerDebugRun();
        EcoSphereModeResult result;
        if (recipeId == 0) {
            FluidStack lifeEssence = BloodMagicHelper.getLifeEssence(20);
            if (lifeEssence == null) return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_recipe"));
            result = new EcoSphereModeResult(
                SimpleCheckRecipeResult.ofSuccess("generating_life_essence"),
                new ItemStack[0],
                new FluidStack[] { lifeEssence },
                EcoSphereModeSupport.calculateEut(euTier),
                5);
        } else {
            result = DirectedMobClonerRecipeCache.processDebug(machine, recipeId);
        }
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

    private static boolean hasIntegratedCircuit(TST_MegaTreeFarm machine) {
        for (ItemStack input : machine.getStoredInputs()) {
            if (input != null && input.getItem() instanceof ItemIntegratedCircuit) return true;
        }
        return false;
    }
}
