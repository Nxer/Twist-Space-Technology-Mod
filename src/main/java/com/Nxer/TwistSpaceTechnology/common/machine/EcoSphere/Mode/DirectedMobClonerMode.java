package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    /** Looting X granted by the tier-3 cloning beacon. */
    private static final int LOOTING_TIER_THREE_LEVEL = 10;
    /** Maximum looting level used anywhere in the yield bonus. */
    private static final int LOOTING_CAP_LEVEL = 10;
    /** One network LP pays for five units of physical Life Essence. */
    public static final int LP_NETWORK_CONVERSION_DIVISOR = 5;

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
        if (machine.getModeBeaconTier() < 2) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);

        DirectedMobClonerRecipeCache.CachedRecipe recipe = DirectedMobClonerRecipeCache.findRecipe(recipeId);
        if (recipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        boolean pulverize = machine.hasSpecialUpgrade(EcoSphereSpecialUpgrade.AUTO_PULVERIZE_EQUIPMENT);
        WeaponTags weaponTags = DirectedMobClonerWeaponHandler.process(machine.getCloningWeapons());
        // The Infinity Sword keeps its existing role as an alternative boss prerequisite and source of Looting X.
        boolean tierThree = machine.hasDirectedMobClonerTierThreeBeacon()
            || weaponTags.get(DirectedMobClonerWeaponHandler.FunctionTag.HAS_COSMOS) > 0;
        // Boss recipes require tier-3 authorization in addition to the tier-2 structure checked above.
        if (recipe.boss() && !tierThree) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);

        // Looting X stays unchanged: either the tier-three beacon or the Infinity Sword grants it, capped at X.
        double lootingBonus = weaponTags.get(DirectedMobClonerWeaponHandler.FunctionTag.ALL_OUTPUTS_CHANCE_BONUS);
        double tierThreeBonus = tierThree ? LOOTING_TIER_THREE_LEVEL * 5_000d : 0;
        double allOutputsBonus = Math.min(LOOTING_CAP_LEVEL * 5_000d, Math.max(lootingBonus, tierThreeBonus));
        // 36x EEC at 20 HP, about 4x at 300 HP each run in 5s.
        double durationMultiplier = 36 * 15125d / 1024d / ((double) recipe.eecDuration() * recipe.eecDuration());
        return processCloningRecipeWithLifeEssence(machine, lifeEssenceInput, euTier, parallelResult -> {
            List<ItemStack> outputs = new ArrayList<>();
            for (int tableIndex = 0; tableIndex < 2; tableIndex++) {
                List<DirectedMobClonerRecipeCache.CachedOutput> outputTable = tableIndex == 0 ? recipe.ordinaryOutputs()
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

    private static EcoSphereModeResult processCloningRecipeWithLifeEssence(TST_EcoSphereSimulator machine,
        FluidStack lifeEssenceInput, int powerTier,
        Function<EcoSphereModeSupport.ParallelResult, EcoSphereModeResult> processor) {
        long parallelFromEUt = EcoSphereModeSupport.getParallelFromEUt(powerTier, machine.isTierTwo());
        long fluidPerParallel = machine.applyFluidDiscount(lifeEssenceInput.amount);

        ItemStack orb = machine.getCloningBloodOrb();
        boolean creativeOrb = BloodMagicHelper.isCreativeOrb(orb);
        long availableLp = creativeOrb ? 0 : Math.max(0, BloodMagicHelper.getOrbOwnerLpAmount(orb));
        long availableFluid = EcoSphereModeSupport.getAvailableFluid(machine, lifeEssenceInput.getFluid());
        long availableEquivalent;
        // Creative input is unbounded; an overflowing LP-plus-fluid sum saturates at Long.MAX_VALUE.
        if (creativeOrb || availableFluid > Long.MAX_VALUE - availableLp * LP_NETWORK_CONVERSION_DIVISOR) {
            availableEquivalent = Long.MAX_VALUE;
        } else {
            availableEquivalent = availableFluid + availableLp * LP_NETWORK_CONVERSION_DIVISOR;
        }

        long parallel = Math.min(parallelFromEUt, availableEquivalent / fluidPerParallel);
        if (parallel <= 0) {
            return EcoSphereModeResult
                .failure(EcoSphereModeSupport.missingFluid(machine, lifeEssenceInput.getFluid(), fluidPerParallel));
        }
        // The visible fluid area follows the selected cloning recipe even when an orb supplies the entire operation.
        if (!machine.prepareFluidAreaForConsumption(lifeEssenceInput.getFluid())) {
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        }

        // Parallel is capped by availableEquivalent, so this product remains representable.
        long totalEquivalentCost = fluidPerParallel * parallel;
        // Pay with network LP first, then use physical Life Essence for the remainder.
        int lpToDrain = creativeOrb ? 0
            : (int) Math.min(availableLp, totalEquivalentCost / LP_NETWORK_CONVERSION_DIVISOR);
        long fluidToDrain = creativeOrb ? 0 : totalEquivalentCost - (long) lpToDrain * LP_NETWORK_CONVERSION_DIVISOR;
        if (fluidToDrain > 0
            && EcoSphereModeSupport.getAvailableFluid(machine, lifeEssenceInput.getFluid()) < fluidToDrain) {
            return EcoSphereModeResult
                .failure(EcoSphereModeSupport.missingFluid(machine, lifeEssenceInput.getFluid(), fluidToDrain));
        }

        EcoSphereModeResult result = processor.apply(new EcoSphereModeSupport.ParallelResult(powerTier, parallel));
        if (result.result()
            .wasSuccessful()) {
            machine.setPendingRecipeConsumption(
                () -> consumeCloningInputs(machine, orb, lpToDrain, lifeEssenceInput, fluidToDrain));
            machine.setCurrentParallel(parallel);
        }
        return result;
    }

    public static boolean consumeLifeEssenceForFluidArea(TST_EcoSphereSimulator machine, long fluidAmount) {
        if (fluidAmount <= 0) return true;
        FluidStack lifeEssence = DirectedMobClonerFakeRecipe.LIFE_ESSENCE_STACK;
        if (lifeEssence == null || lifeEssence.getFluid() == null) return false;

        ItemStack orb = machine.getCloningBloodOrb();
        if (BloodMagicHelper.isCreativeOrb(orb)) return true;
        // Structure layers use the same LP-first payment order as cloning recipes.
        long availableLp = Math.max(0, BloodMagicHelper.getOrbOwnerLpAmount(orb));
        int lpToDrain = (int) Math.min(availableLp, fluidAmount / LP_NETWORK_CONVERSION_DIVISOR);
        long fluidToDrain = fluidAmount - (long) lpToDrain * LP_NETWORK_CONVERSION_DIVISOR;
        if (EcoSphereModeSupport.getAvailableFluid(machine, lifeEssence.getFluid()) < fluidToDrain) return false;
        return consumeCloningInputs(machine, orb, lpToDrain, lifeEssence, fluidToDrain);
    }

    private static boolean consumeCloningInputs(TST_EcoSphereSimulator machine, ItemStack orb, int lpToDrain,
        FluidStack lifeEssenceInput, long fluidToDrain) {
        int drainedLp = BloodMagicHelper.drainBloodFromNetwork(orb, lpToDrain);
        if (drainedLp != lpToDrain) {
            // Blood Magic may return a partial drain; undo it before failing the payment.
            if (drainedLp > 0) BloodMagicHelper.addBloodToNetwork(orb, drainedLp);
            return false;
        }
        if (fluidToDrain > 0 && !EcoSphereModeSupport.drainFluid(machine, lifeEssenceInput.getFluid(), fluidToDrain)) {
            // Keep mixed payment atomic when the physical-fluid half fails.
            if (drainedLp > 0) BloodMagicHelper.addBloodToNetwork(orb, drainedLp);
            return false;
        }
        return true;
    }

    public static FluidStack[] routeLifeEssenceLpOutputToNetwork(ItemStack orb, FluidStack[] outputs) {
        if (outputs == null || outputs.length == 0) return outputs;
        FluidStack lifeEssenceTemplate = DirectedMobClonerFakeRecipe.LIFE_ESSENCE_STACK;
        if (lifeEssenceTemplate == null) return outputs;

        // Recipe 0 reaches this hook as physical output already quantized to complete LP units.
        long lpAmount = 0;
        List<FluidStack> remainingOutputs = new ArrayList<>(outputs.length);
        for (FluidStack output : outputs) {
            if (output == null) continue;
            if (isPreconvertedLpOutput(output)) {
                lpAmount += output.amount / LP_NETWORK_CONVERSION_DIVISOR;
            } else {
                remainingOutputs.add(output);
            }
        }

        int requestedLp = (int) Math.min(Integer.MAX_VALUE, lpAmount);
        int addedLp = requestedLp <= 0 || !BloodMagicHelper.isBloodOrb(orb)
            || BloodMagicHelper.getOrbOwnerName(orb) == null ? 0
                : Math.max(0, Math.min(requestedLp, BloodMagicHelper.addBloodToNetwork(orb, requestedLp)));
        addLifeEssenceFromLp(remainingOutputs, lifeEssenceTemplate, lpAmount - addedLp);
        return remainingOutputs.toArray(new FluidStack[0]);
    }

    private static boolean isPreconvertedLpOutput(FluidStack output) {
        return isLifeEssenceOutput(output) && output.amount % LP_NETWORK_CONVERSION_DIVISOR == 0;
    }

    public static boolean isLifeEssenceOutput(FluidStack output) {
        FluidStack template = DirectedMobClonerFakeRecipe.LIFE_ESSENCE_STACK;
        return output != null && template != null && output.getFluid() == template.getFluid();
    }

    public static FluidStack[] getRecipeZeroFluidOutputsForCapacityCheck(ItemStack orb, FluidStack[] outputs) {
        if (outputs == null || outputs.length == 0) return outputs;
        long remainingNetworkCapacity = BloodMagicHelper.getOrbOwnerRemainingLpCapacity(orb);
        // Void protection only reserves physical space for output the LP network cannot absorb.
        List<FluidStack> remainingOutputs = new ArrayList<>(outputs.length);
        for (FluidStack output : outputs) {
            if (output == null) continue;
            FluidStack remainingOutput = output.copy();
            if (remainingNetworkCapacity > 0 && isPreconvertedLpOutput(output)) {
                long outputLp = output.amount / LP_NETWORK_CONVERSION_DIVISOR;
                long networkLp = Math.min(outputLp, remainingNetworkCapacity);
                remainingNetworkCapacity -= networkLp;
                remainingOutput.amount = (int) ((outputLp - networkLp) * LP_NETWORK_CONVERSION_DIVISOR);
            }
            if (remainingOutput.amount > 0) remainingOutputs.add(remainingOutput);
        }
        return remainingOutputs.toArray(new FluidStack[0]);
    }

    private static void addLifeEssenceFromLp(List<FluidStack> outputs, FluidStack template, long lpAmount) {
        int maximumLpPerStack = Integer.MAX_VALUE / LP_NETWORK_CONVERSION_DIVISOR;
        while (lpAmount > 0) {
            int splitLp = (int) Math.min(maximumLpPerStack, lpAmount);
            FluidStack split = template.copy();
            split.amount = splitLp * LP_NETWORK_CONVERSION_DIVISOR;
            outputs.add(split);
            lpAmount -= splitLp;
        }
    }

    private static void addSplitPreconvertedLifeEssenceOutput(List<FluidStack> outputs, FluidStack template,
        long amount) {
        int maximumAmountPerStack = Integer.MAX_VALUE - Integer.MAX_VALUE % LP_NETWORK_CONVERSION_DIVISOR;
        while (amount > 0) {
            FluidStack split = template.copy();
            split.amount = (int) Math.min(maximumAmountPerStack, amount);
            outputs.add(split);
            amount -= split.amount;
        }
    }

    private static EcoSphereModeResult createFallbackResult(TST_EcoSphereSimulator machine,
        EcoSphereModeSupport.ParallelResult parallelResult) {
        FluidStack outputTemplate = DirectedMobClonerFakeRecipe.FALLBACK_LIFE_ESSENCE_OUTPUT_STACK;
        if (outputTemplate == null || outputTemplate.amount % LP_NETWORK_CONVERSION_DIVISOR != 0)
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
        long outputAmount;
        try {
            // Keep the physical amount for GT output-capacity checks; shared scaling converts through LP units.
            outputAmount = Math.multiplyExact((long) outputTemplate.amount, parallelResult.parallel());
        } catch (ArithmeticException ignored) {
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
        }
        List<FluidStack> lifeEssenceOutputs = new ArrayList<>();
        addSplitPreconvertedLifeEssenceOutput(lifeEssenceOutputs, outputTemplate, outputAmount);
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
        return EcoSphereModeResult.standard(
            runningResult,
            new ItemStack[0],
            lifeEssenceOutputs.toArray(new FluidStack[0]),
            parallelResult.tier());
    }

}
