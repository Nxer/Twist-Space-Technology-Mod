package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator.MODE_RECIPE_DURATION;
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

import gregtech.api.enums.GTValues;
import gregtech.api.objects.XSTR;
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
        int recipeId = machine.getCloningRecipeId();
        FluidStack bloodInput = DirectedMobClonerFakeRecipe.BLOOD_STACK;
        if (recipeId == 0) {
            if (bloodInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
            return processFallback(machine, bloodInput, euTier);
        }

        FluidStack lifeEssenceInput = DirectedMobClonerFakeRecipe.LIFE_ESSENCE_STACK;
        if (lifeEssenceInput == null || !machine.isTierTwo())
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);

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
        boolean pulverize = machine.hasSpecialUpgrade(EcoSphereSpecialUpgrade.AUTO_PULVERIZE_EQUIPMENT);
        WeaponTags weaponTags = DirectedMobClonerWeaponHandler.process(machine.getCloningWeapons());
        double allOutputsBonus = weaponTags.get(DirectedMobClonerWeaponHandler.FunctionTag.ALL_OUTPUTS_CHANCE_BONUS);
        double durationMultiplier = 100 / ((double) recipe.eecDuration() * recipe.eecDuration());
        long baseEut = EcoSphereModeSupport.calculateEut(baseTier);
        long eut = baseEut > Long.MAX_VALUE / parallelFromEUt ? Long.MAX_VALUE : baseEut * parallelFromEUt;
        return EcoSphereModeSupport.processRecipeWithParallelLimit(
            machine,
            lifeEssenceInput.getFluid(),
            lifeEssenceInput.amount,
            euTier,
            parallelFromEUt,
            parallelResult -> {
                List<ItemStack> outputs = new ArrayList<>();
                for (int tableIndex = 0; tableIndex < 2; tableIndex++) {
                    List<DirectedMobClonerRecipeCache.CachedOutput> outputTable = tableIndex == 0
                        ? recipe.ordinaryOutputs()
                        : recipe.equipmentOutputs(pulverize);
                    for (DirectedMobClonerRecipeCache.CachedOutput output : outputTable) {
                        double outputAmount = output.stack().stackSize * (double) parallelResult.parallel()
                            * (output.chance() + allOutputsBonus)
                            / 10_000
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
                        / 10_000
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
                return new EcoSphereModeResult(
                    // #tr GT5U.gui.text.recipe_result.processing_mob_drops
                    // # Processing mob drops
                    // #zh_CN 生物掉落处理中
                    SimpleCheckRecipeResult.ofSuccess("processing_mob_drops"),
                    outputs.toArray(new ItemStack[0]),
                    eut,
                    MODE_RECIPE_DURATION);
            });
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
