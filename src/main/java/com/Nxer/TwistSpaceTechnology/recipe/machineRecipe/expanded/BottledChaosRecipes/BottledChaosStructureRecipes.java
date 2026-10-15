package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructureCatalog;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructureCatalog.Rule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructureDisplay;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructureDisplay.Frame;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructurePackaging;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_BottledChaosStructureFrontend;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import appeng.api.AEApi;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.util.GTModHandler;
import gregtech.common.blocks.ItemMachines;

/** NEI examples use the same cached assets and disk transaction as the GUI. They do not execute as GT recipes. */
public final class BottledChaosStructureRecipes {

    public static void registerRecipes() {
        GTModHandler.addShapelessCraftingRecipe(
            GTCMItemList.BottledChaosMachineModule.get(1),
            new Object[] { ItemList.Tool_DataStick.get(1) });
        int registered = 0;
        boolean diskExample = false;
        Map<String, List<Rule>> machines = new LinkedHashMap<>();
        for (var rule : BottledChaosStructureCatalog.displayRules()) {
            if (rule.example() == null) {
                TwistSpaceTechnology.LOG.warn("Cannot resolve structure example {}.", rule.machineKey());
                continue;
            }
            // Tier variants of a single-block pool share one input/output animation.
            String group = ItemMachines.getMetaTileEntity(rule.controller()) instanceof MTEBasicMachine
                ? "single:" + Long.toUnsignedString(rule.poolMask())
                : rule.machineKey();
            machines.computeIfAbsent(group, key -> new ArrayList<>())
                .add(rule);
        }
        for (List<Rule> machine : machines.values()) {
            var display = new BottledChaosStructureDisplay(machine);
            if (!display.fits(TST_BottledChaosStructureFrontend.REGION_SLOTS)) {
                TwistSpaceTechnology.LOG.warn(
                    "Too many material types to display structure {}.",
                    machine.get(0)
                        .machineKey());
                continue;
            }
            if (add(display)) registered++;
            if (!diskExample) {
                var structure = machine.get(0)
                    .example();
                ItemStack module = ItemBottledChaosMachineModule.create(structure, 1);
                ItemStack emptyDisk = AEApi.instance()
                    .definitions()
                    .items()
                    .cell16384k()
                    .maybeStack(1)
                    .orNull();
                if (emptyDisk == null) continue;
                ItemStackHandler example = new ItemStackHandler(11);
                example.setStackInSlot(0, module.copy());
                example.setStackInSlot(1, emptyDisk.copy());
                if (BottledChaosStructurePackaging.execute(example, true)
                    .endsWith(".success")) {
                    ItemStack disk = example.getStackInSlot(10);
                    if (disk != null && add(
                        new BottledChaosStructureDisplay(
                            new Frame(
                                new ItemStack[] { disk.copy() },
                                new ItemStack[0],
                                GTCMItemList.BottledChaosMachineModule.get(1),
                                new ItemStack[] { module.copy(), emptyDisk.copy() },
                                0,
                                structure))))
                        registered++;
                    if (disk != null && add(
                        new BottledChaosStructureDisplay(
                            new Frame(
                                new ItemStack[] { emptyDisk.copy() },
                                new ItemStack[0],
                                module.copy(),
                                new ItemStack[] { GTCMItemList.BottledChaosMachineModule.get(1), disk.copy() },
                                0,
                                null))))
                        registered++;
                    diskExample = true;
                }
            }
        }
        TwistSpaceTechnology.LOG.info("Registered {} Bottled Chaos structure packaging examples.", registered);
    }

    private static boolean add(BottledChaosStructureDisplay display) {
        var frame = display.frame(0);
        var recipe = GTValues.RA.stdBuilder()
            .itemInputsUnsafe(frame.inputs())
            .itemOutputs(frame.outputs())
            .metadata(BottledChaosStructureDisplay.DISPLAY, display)
            .specialValue(frame.profile())
            .duration(1)
            .eut(0)
            .fake()
            .ignoreCollision()
            .build();
        recipe.ifPresent(GTCMRecipe.BottledChaosStructureRecipes::add);
        return recipe.isPresent();
    }
}
