package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.LongPredicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellHelper.InputSource;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.github.bsideup.jabel.Desugar;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.common.summoning.meteor.Meteor;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorComponent;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorReagent;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorReagentRegistry;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;

public final class StarRecipePlanner {

    public StarBatch batch;
    public final StarInputPlan inputs;
    private final Meteor meteor;
    private final ItemStack focus;

    private StarRecipePlanner(StarInputPlan inputs, Meteor meteor, ItemStack focus) {
        this.inputs = inputs;
        this.meteor = meteor;
        this.focus = focus;
    }

    @Nullable
    public static StarRecipePlanner match(List<InputSource> itemSources, List<InputSource> fluidSources) {
        StarInputPlan inputs = new StarInputPlan(itemSources, fluidSources);
        for (ItemStack stack : inputs.itemTypes()) {
            if (stack == null || stack.stackSize <= 0) continue;
            int id = MeteorRegistry.getMeteorIDForItem(stack);
            if (id >= 0) {
                Meteor meteor = MeteorRegistry.meteorList.get(id);
                if (meteor != null) return new StarRecipePlanner(inputs, meteor, stack);
            }
        }
        return null;
    }

    public CheckRecipeResult checkPower(long miningEut, long availablePower) {
        if (meteor.radius < 1 || meteor.radius > StarMath.MAX_RADIUS)
            return SimpleResultWithText.ofFailure("tst.star.result.star_radius");
        long requiredPower = Math.max(GTValues.VP[StarMath.recipeTier(meteor.radius)], miningEut);
        if (miningEut < 1 || availablePower < requiredPower)
            return CheckRecipeResultRegistry.insufficientPower(requiredPower);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public CheckRecipeResult plan(StarDronePorts ports, int fortune, int miningTier, int referenceTicks, long miningEut,
        long availablePower, long seed) {
        if (miningTier < 1 || miningTier > 3) return SimpleResultWithText.ofFailure("tst.star.result.miner_tier");
        if (!Mods.ExtraUtilities.isModLoaded()) return CheckRecipeResultRegistry.NO_RECIPE;
        ItemStack octuple = ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 7);
        if (octuple == null
            || octuple.getItem() != GameRegistry.findItem(Mods.ExtraUtilities.ID, "cobblestone_compressed"))
            return CheckRecipeResultRegistry.NO_RECIPE;
        ItemStack expansion = GTCMItemList.MineralSeedCore.get(1);
        FluidStack lifeEssence = FluidRegistry.getFluidStack("lifeessence", 1);

        batch = new StarBatch();
        batch.fortune = fortune;
        batch.miningTier = miningTier;
        batch.seed = seed;
        List<ItemStack> physicalDrones = ports.drones();
        int droneTier = 0;
        for (ItemStack stack : physicalDrones) {
            droneTier = Math.max(droneTier, StarDroneFleet.effectiveTier(stack));
        }
        int ordinary = 0;
        long idleOrdinary = 0;
        for (ItemStack stack : physicalDrones) {
            int tier = StarDroneFleet.effectiveTier(stack);
            if (tier == 0) continue;
            int count = stack.stackSize;
            if (tier == droneTier) {
                if (count > StarMath.MAX_DRONES - batch.drones)
                    return SimpleResultWithText.ofFailure("tst.star.result.star_drone_limit");
                batch.drones += count;
                if (!StarDroneFleet.isInfused(stack)) ordinary += count;
            } else if (!StarDroneFleet.isInfused(stack)) idleOrdinary += count;
            batch.addDroneReturn(stack, count);
        }
        batch.fleet.initialize(droneTier, ordinary, batch.drones - ordinary, idleOrdinary);
        long parallelLimit = StarMath.droneParallel(droneTier, batch.drones);
        InputSource fuel = ports.fuelSource();
        MeteorInputs recipe = new MeteorInputs(meteor, focus, expansion, octuple, lifeEssence, ports, fuel);
        CheckRecipeResult size = selectSizeAndParallel(recipe, referenceTicks, availablePower, parallelLimit);
        if (!size.wasSuccessful()) return size;
        int radius = batch.radius;
        // Reserve only the chosen batch; no candidate trial changes the reservation ledger or inventories.
        batch.formationTicks = StarMeteorFormation.duration(radius);
        batch.beamWarmupTicks = StarBeamSequence.FIELD_CONNECTION;
        batch.duration = StarMath.batchDuration(radius, batch.miningTier, referenceTicks, batch.parallel);
        batch.fleet.paidHelium = batch.fleet.fuelForMiningTicks(batch.miningDuration());
        if (!inputs.reserveFrom(fuel, batch.fleet.paidHelium)) return recipe.missingFuel(batch.fleet.paidHelium);
        ItemStackLong[] solids = recipe.solidCosts(radius, batch.parallel);
        for (ItemStackLong solid : solids) {
            if (!inputs.item(solid.itemStack(), solid.stackSize())) return missingItem(solid.itemStack(), 0, solids);
        }
        batch.lifeEssence = recipe.lifeEssenceCost(radius, batch.parallel);
        if (!inputs.fluid(lifeEssence, batch.lifeEssence)) return recipe.missingEssence(batch.lifeEssence);

        Map<Reagent, ItemStack> reagentItems = new LinkedHashMap<>();
        for (ItemStack stack : inputs.itemTypes()) {
            if (stack == null || stack.stackSize <= 0) continue;
            ReagentStack reagent = ReagentRegistry.getReagentStackForItem(stack);
            if (reagent != null && reagent.amount >= 1000 && inputs.available(stack) >= batch.parallel) {
                reagentItems.putIfAbsent(reagent.reagent, stack);
            }
        }
        List<Reagent> reagents = new ArrayList<>();
        if (meteor.ores == null || meteor.filler == null)
            return SimpleResultWithText.ofFailure("tst.star.result.invalid_meteor");
        List<MeteorComponent> components = new ArrayList<>(meteor.ores);
        components.addAll(meteor.filler);
        for (Reagent reagent : reagentItems.keySet()) {
            MeteorReagent effect = MeteorReagentRegistry.reagents.get(reagent);
            if (effect != null && effect.filler != null) components.addAll(effect.filler);
        }
        for (MeteorComponent component : components) {
            if (component == null || component.getRequiredReagents() == null)
                return SimpleResultWithText.ofFailure("tst.star.result.invalid_meteor");
        }
        for (Map.Entry<Reagent, ItemStack> entry : reagentItems.entrySet()) {
            Reagent reagent = entry.getKey();
            MeteorReagent effect = MeteorReagentRegistry.reagents.get(reagent);
            boolean affectsOutput = effect != null
                && (effect.fillerChanceChange != 0 || effect.rawFillerChanceChange != 0
                    || (effect.filler != null && !effect.filler.isEmpty()));
            for (MeteorComponent component : components) {
                if (component.getRequiredReagents()
                    .contains(reagent)) affectsOutput = true;
            }
            // Do not charge obsolete radius-only or explosion-only powders in this virtual meteor.
            if (!affectsOutput) continue;
            if (!inputs.item(entry.getValue(), batch.parallel))
                return missingItem(entry.getValue(), batch.parallel, solids);
            reagents.add(reagent);
        }

        // Physical drone stacks are held last, after all potentially short network extractions.
        for (ItemStack stack : physicalDrones) {
            if (StarDroneFleet.effectiveTier(stack) != 0
                && !inputs.reserveFrom(InputSource.of(stack, stack), stack.stackSize))
                return SimpleResultWithText.ofFailure("tst.star.result.input_changed");
        }
        batch.formationEut = availablePower;
        batch.eut = miningEut;
        Random random = new Random(seed);

        double filler = Meteor.getNewFillerChance(meteor.fillerChance, reagents) / 100.0;
        addComponents(
            Meteor.removeBlocksMissingRequiredReagents(meteor.ores, reagents),
            1 - filler,
            fortune,
            random,
            false);
        addComponents(Meteor.getNewFillerList(meteor.filler, reagents), filler, 0, random, true);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Desugar
    private record MeteorInputs(Meteor meteor, ItemStack focus, ItemStack expansion, ItemStack stone,
        FluidStack essence, StarDronePorts ports, InputSource fuel) {

        ItemStackLong[] solidCosts(int radius, long parallel) {
            return new ItemStackLong[] { new ItemStackLong(focus, parallel),
                new ItemStackLong(
                    expansion,
                    Math.multiplyExact(StarMath.expansionCatalysts(meteor.radius, radius), parallel)),
                new ItemStackLong(stone, StarMath.compressedStone(radius, parallel)) };
        }

        long lifeEssenceCost(int radius, long parallel) {
            return StarMath.lifeEssence(meteor.cost, meteor.radius, radius, parallel);
        }

        CheckRecipeResult missingEssence(long required) {
            if (essence == null) return CheckRecipeResultRegistry.INTERNAL_ERROR;
            return SimpleResultWithText.outOfFluid(essence, required);
        }

        CheckRecipeResult missingFuel(long required) {
            return ports.hasFuelHatch() ? StarDronePorts.missingFuel(required) : CheckRecipeResultRegistry.NO_RECIPE;
        }
    }

    private static CheckRecipeResult missingItem(ItemStack item, long additional, ItemStackLong[] solids) {
        if (item == null) return CheckRecipeResultRegistry.INTERNAL_ERROR;
        GTUtility.ItemId type = GTUtility.ItemId.create(item);
        long required = additional;
        for (ItemStackLong solid : solids) {
            if (solid.stackSize() > 0 && solid.itemStack() != null
                && type.equals(GTUtility.ItemId.create(solid.itemStack())))
                required = Math.addExact(required, solid.stackSize());
        }
        return SimpleResultWithText.outOfItem(item, required);
    }

    /** Maximize native-radius parallel first, then grow without sacrificing that parallel. */
    private CheckRecipeResult selectSizeAndParallel(MeteorInputs recipe, int referenceTicks, long availablePower,
        long parallelLimit) {
        int nativeRadius = recipe.meteor.radius;
        Supplier<CheckRecipeResult> missing = limitingResource(recipe, referenceTicks, nativeRadius, 1);
        if (missing != null) return missing.get();
        batch.parallel = largestFeasible(
            1,
            Math.min(parallelLimit, inputs.available(recipe.focus)),
            parallel -> limitingResource(recipe, referenceTicks, nativeRadius, parallel) == null);
        batch.radius = (int) largestFeasible(
            nativeRadius,
            StarMath.MAX_RADIUS,
            radius -> availablePower >= GTValues.VP[StarMath.recipeTier((int) radius)]
                && limitingResource(recipe, referenceTicks, (int) radius, batch.parallel) == null);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private static long largestFeasible(long low, long high, LongPredicate feasible) {
        while (low < high) {
            long middle = low + (high - low + 1) / 2;
            if (feasible.test(middle)) low = middle;
            else high = middle - 1;
        }
        return low;
    }

    // Only the minimum batch reports a shortage. Larger trials can fall back without formatting a player message.
    @Nullable
    private Supplier<CheckRecipeResult> limitingResource(MeteorInputs recipe, int referenceTicks, int radius,
        long parallel) {
        try {
            ItemStackLong[] solids = recipe.solidCosts(radius, parallel);
            int missing = inputs.firstMissingItem(solids);
            if (missing >= 0) return () -> missingItem(solids[missing].itemStack(), 0, solids);
            long essence = recipe.lifeEssenceCost(radius, parallel);
            if (inputs.available(recipe.essence) < essence) return () -> recipe.missingEssence(essence);
            int duration = StarMath.batchDuration(radius, batch.miningTier, referenceTicks, parallel);
            int miningTicks = duration - StarMeteorFormation.duration(radius) - StarBeamSequence.FIELD_CONNECTION;
            long helium = batch.fleet.fuelForMiningTicks(miningTicks);
            if (helium > (recipe.fuel == null ? 0 : recipe.fuel.amount)) return () -> recipe.missingFuel(helium);
            return null;
        } catch (ArithmeticException overflow) {
            return () -> SimpleResultWithText.ofFailure("tst.star.result.numeric_limit");
        }
    }

    private void addComponents(List<MeteorComponent> components, double fraction, int fortune, Random random,
        boolean filler) {
        if (fraction <= 0) return;
        long total = 0;
        for (MeteorComponent component : components) {
            if (component == null) throw new IllegalArgumentException("Missing meteor component");
            total = Math.addExact(total, Math.max(0, component.getWeight()));
        }
        if (total == 0) throw new IllegalArgumentException("Meteor has no positive component weights");
        long blocks = Math.multiplyExact(StarMath.volume(batch.radius), batch.parallel);
        for (MeteorComponent component : components) {
            if (component.getWeight() <= 0) continue;
            ItemStack stack = component.getBlock();
            if (stack == null || stack.getItem() == null)
                throw new IllegalArgumentException("Missing meteor component");
            double probability = fraction * component.getWeight() / total;
            int bonus = component == MeteorComponent.defaultMeteorBlock ? 0 : fortune;
            batch.addOutput(stack, StarMath.expectedOutput(blocks, probability, bonus, random));
            // This is the original distribution, deliberately unaffected by fortune.
            batch.surface.add(new StarBatch.SurfaceMaterial(stack.copy(), probability, filler));
        }
    }

    // #tr tst.star.result.star_radius
    // # Meteor radius is outside the current test range.
    // #zh_CN 陨星半径超出当前测试范围.

    // #tr tst.star.result.numeric_limit
    // # This meteor batch exceeds the supported resource or duration range.
    // #zh_CN 本批陨星的耗材, 产出或耗时超出可处理的数值范围.

    // #tr tst.star.result.star_drone_limit
    // # At most 4096 drones of the highest grade can participate.
    // #zh_CN 最高等级的参与无人机不能超过 4096 架。

    // #tr tst.star.result.invalid_meteor
    // # Invalid or unavailable Blood Magic meteor recipe.
    // #zh_CN 血魔法陨星配方无效或包含未注册物品。

    // #tr tst.star.result.input_changed
    // # Inputs changed while starting the ritual.
    // #zh_CN 仪式启动时输入发生变化。

    // #tr tst.star.result.miner_missing
    // # Meteor miner not ready
    // #zh_CN 坠星矿机未就绪

    // #tr tst.star.result.miner_tier
    // # Meteor miner tier mismatch
    // #zh_CN 坠星矿机等级不符
}
