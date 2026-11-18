package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.LongPredicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellHelper.InputSource;
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

public final class StarRecipePlanner {

    public StarBatch batch;
    public StarInputPlan inputs;
    private long availableHelium;
    public String failure = "no_recipe";

    public boolean plan(List<InputSource> itemSources, List<InputSource> fluidSources, List<ItemStack> physicalDrones,
        InputSource fuel, int fortune, int miningTier, int referenceTicks, long miningEut, long availablePower,
        long seed) {
        if (miningTier < 1 || miningTier > 3) return fail("miner_missing");
        inputs = new StarInputPlan(itemSources, fluidSources);
        List<ItemStack> items = inputs.itemTypes();
        availableHelium = fuel == null ? 0 : fuel.amount;
        ItemStack focus = null;
        Meteor meteor = null;
        for (ItemStack stack : items) {
            if (stack == null || stack.stackSize <= 0) continue;
            int id = MeteorRegistry.getMeteorIDForItem(stack);
            if (id < 0) continue;
            focus = stack;
            meteor = MeteorRegistry.meteorList.get(id);
            break;
        }
        if (meteor == null) return false;
        if (meteor.radius < 1 || meteor.radius > StarMath.MAX_RADIUS) return fail("star_radius");
        // Power is fixed independently of parallel. Reject an impossible native recipe before filling the fleet.
        if (miningEut < 1 || availablePower < Math.max(GTValues.VP[StarMath.recipeTier(meteor.radius)], miningEut))
            return fail("star_power");
        if (!Mods.ExtraUtilities.isModLoaded()) return fail("star_stone");
        ItemStack octuple = ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 7);
        if (octuple == null
            || octuple.getItem() != GameRegistry.findItem(Mods.ExtraUtilities.ID, "cobblestone_compressed"))
            return fail("star_stone");
        ItemStack expansion = GTCMItemList.MineralSeedCore.get(1);
        FluidStack lifeEssence = FluidRegistry.getFluidStack("lifeessence", 1);

        batch = new StarBatch();
        batch.fortune = fortune;
        batch.miningTier = miningTier;
        batch.seed = seed;
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
                if (count > StarMath.MAX_DRONES - batch.drones) return fail("star_drone_limit");
                batch.drones += count;
                if (!StarDroneFleet.isInfused(stack)) ordinary += count;
            } else if (!StarDroneFleet.isInfused(stack)) idleOrdinary += count;
            batch.addDroneReturn(stack, count);
        }
        batch.fleet.initialize(droneTier, ordinary, batch.drones - ordinary, idleOrdinary);
        long parallelLimit = StarMath.droneParallel(droneTier, batch.drones);
        MeteorInputs recipe = new MeteorInputs(meteor, focus, expansion, octuple, lifeEssence);
        if (!selectSizeAndParallel(recipe, referenceTicks, availablePower, parallelLimit)) return false;
        int radius = batch.radius;
        // Reserve only the chosen batch; no candidate trial changes the reservation ledger or inventories.
        batch.formationTicks = StarMeteorFormation.duration(radius);
        batch.beamWarmupTicks = StarBeamSequence.FIELD_CONNECTION;
        batch.duration = StarMath.batchDuration(radius, batch.miningTier, referenceTicks, batch.parallel);
        batch.fleet.paidHelium = batch.fleet.fuelForMiningTicks(batch.miningDuration());
        if (!inputs.reserveFrom(fuel, batch.fleet.paidHelium)) return fail("drone_fuel");
        ItemStackLong[] solids = recipe.solidCosts(radius, batch.parallel);
        for (int i = 0; i < solids.length; i++) {
            if (!inputs.item(solids[i].itemStack(), solids[i].stackSize())) return fail(solidFailure(i));
        }
        batch.lifeEssence = recipe.lifeEssenceCost(radius, batch.parallel);
        if (!inputs.fluid(lifeEssence, batch.lifeEssence)) return fail("star_lp");

        Map<Reagent, ItemStack> reagentItems = new LinkedHashMap<>();
        for (ItemStack stack : items) {
            if (stack == null || stack.stackSize <= 0) continue;
            ReagentStack reagent = ReagentRegistry.getReagentStackForItem(stack);
            if (reagent != null && reagent.amount >= 1000 && inputs.available(stack) >= batch.parallel) {
                reagentItems.putIfAbsent(reagent.reagent, stack);
            }
        }
        List<Reagent> reagents = new ArrayList<>();
        List<MeteorComponent> components = new ArrayList<>(meteor.ores);
        components.addAll(meteor.filler);
        for (Reagent reagent : reagentItems.keySet()) {
            MeteorReagent effect = MeteorReagentRegistry.reagents.get(reagent);
            if (effect != null && effect.filler != null) components.addAll(effect.filler);
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
            if (!inputs.item(entry.getValue(), batch.parallel)) return fail("star_catalyst");
            reagents.add(reagent);
        }

        // Physical drone stacks are held last, after all potentially short network extractions.
        for (ItemStack stack : physicalDrones) {
            if (StarDroneFleet.effectiveTier(stack) != 0
                && !inputs.reserveFrom(InputSource.of(stack, stack), stack.stackSize)) return fail("input_changed");
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
        return true;
    }

    @Desugar
    private record MeteorInputs(Meteor meteor, ItemStack focus, ItemStack expansion, ItemStack stone,
        FluidStack essence) {

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
    }

    private static String solidFailure(int index) {
        return switch (index) {
            case 0 -> "star_focus";
            case 1 -> "star_catalyst";
            case 2 -> "star_stone";
            default -> throw new IllegalArgumentException("Unknown meteor ingredient");
        };
    }

    /** Maximize native-radius parallel first, then grow without sacrificing that parallel. */
    private boolean selectSizeAndParallel(MeteorInputs recipe, int referenceTicks, long availablePower,
        long parallelLimit) {
        int nativeRadius = recipe.meteor.radius;
        String missing = limitingResource(recipe, referenceTicks, availablePower, nativeRadius, 1);
        if (missing != null) return fail(missing);
        batch.parallel = largestFeasible(
            1,
            Math.min(parallelLimit, inputs.available(recipe.focus)),
            parallel -> limitingResource(recipe, referenceTicks, availablePower, nativeRadius, parallel) == null);
        batch.radius = (int) largestFeasible(
            nativeRadius,
            StarMath.MAX_RADIUS,
            radius -> limitingResource(recipe, referenceTicks, availablePower, (int) radius, batch.parallel) == null);
        return true;
    }

    private static long largestFeasible(long low, long high, LongPredicate feasible) {
        while (low < high) {
            long middle = low + (high - low + 1) / 2;
            if (feasible.test(middle)) low = middle;
            else high = middle - 1;
        }
        return low;
    }

    private String limitingResource(MeteorInputs recipe, int referenceTicks, long availablePower, int radius,
        long parallel) {
        if (availablePower < GTValues.VP[StarMath.recipeTier(radius)]) return "star_power";
        try {
            int missing = inputs.firstMissingItem(recipe.solidCosts(radius, parallel));
            if (missing >= 0) return solidFailure(missing);
            if (inputs.available(recipe.essence) < recipe.lifeEssenceCost(radius, parallel)) return "star_lp";
            int duration = StarMath.batchDuration(radius, batch.miningTier, referenceTicks, parallel);
            int miningTicks = duration - StarMeteorFormation.duration(radius) - StarBeamSequence.FIELD_CONNECTION;
            if (batch.fleet.fuelForMiningTicks(miningTicks) > availableHelium) return "drone_fuel";
            return null;
        } catch (ArithmeticException overflow) {
            return "numeric_limit";
        }
    }

    private void addComponents(List<MeteorComponent> components, double fraction, int fortune, Random random,
        boolean filler) {
        if (fraction <= 0) return;
        long total = 0;
        for (MeteorComponent component : components) total = Math.addExact(total, Math.max(0, component.getWeight()));
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

    private boolean fail(String reason) {
        failure = reason;
        batch = null;
        return false;
    }

    // #tr tst.star.result.star_radius
    // # Meteor radius is outside the current test range.
    // #zh_CN 陨星半径超出当前测试范围.

    // #tr tst.star.result.numeric_limit
    // # This meteor batch exceeds the supported resource or duration range.
    // #zh_CN 本批陨星的耗材, 产出或耗时超出可处理的数值范围.

    // #tr tst.star.result.star_catalyst
    // # Not enough meteor catalysts.
    // #zh_CN 陨星催化剂不足。

    // #tr tst.star.result.star_focus
    // # Not enough meteor focus items for the selected parallel.
    // #zh_CN 陨星标志物不足以启动当前并行。

    // #tr tst.star.result.mining_source
    // # Install a meteor miner or supply mining drones.
    // #zh_CN 需要安装坠星矿机或提供采矿无人机。

    // #tr tst.star.result.star_drone_limit
    // # At most 4096 drones of the highest grade can participate.
    // #zh_CN 最高等级的参与无人机不能超过 4096 架。

    // #tr tst.star.result.star_stone
    // # Not enough octuple compressed cobblestone for this meteor batch.
    // #zh_CN 八重压缩圆石不足以生成本批陨星.

    // #tr tst.star.result.star_lp
    // # Not enough Life Essence for this meteor size.
    // #zh_CN 生命本源不足以生成此尺寸的陨星。

    // #tr tst.star.result.star_power
    // # Insufficient input power for this meteor size.
    // #zh_CN 输入功率不足以处理此尺寸的陨星。

    // #tr tst.star.result.invalid_meteor
    // # Invalid or unavailable Blood Magic meteor recipe.
    // #zh_CN 血魔法陨星配方无效或包含未注册物品。

    // #tr tst.star.result.input_changed
    // # Inputs changed while starting the ritual.
    // #zh_CN 仪式启动时输入发生变化。

    // #tr tst.star.result.miner_missing
    // # Restore the linked meteor miner's mining and Fortune tiers to resume the paid ritual.
    // #zh_CN 请恢复联动坠星矿机的采掘与时运等级，以继续已扣料的仪式。
}
