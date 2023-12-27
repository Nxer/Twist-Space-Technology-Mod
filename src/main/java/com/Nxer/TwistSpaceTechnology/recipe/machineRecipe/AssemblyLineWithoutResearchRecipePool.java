package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.debugLogInfo;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getItemContainer;
import static com.github.technus.tectech.thing.CustomItemList.*;
import static com.google.common.math.LongMath.pow;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gtPlusPlus.core.material.MISC_MATERIALS.MUTATED_LIVING_SOLDER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic.TieredCircuits;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.dreammaster.gthandler.CustomItemList;

import goodgenerator.items.MyMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import wanion.avaritiaddons.block.chest.infinity.BlockInfinityChest;

public class AssemblyLineWithoutResearchRecipePool implements IRecipePool {

    public ItemStack transToWildCircuit(ItemStack items) {
        for (TieredCircuits circuit : TieredCircuits.values()) {
            if (circuit.contains(items)) {
                return circuit.getPatternCircuit(items.stackSize);
            }
        }
        return null;
    }

    public static List<ItemStack[]> generateAllItemInput(ItemStack[] baseStack, ItemStack[][] wildCard) {
        List<ItemStack[]> result = new ArrayList<>();
        result.add(Utils.copyItemStackArray(baseStack));
        int len = baseStack.length;
        for (int i = 0; i < len; i++) {
            if (wildCard[i] == null) continue;
            for (int j = 1; j < wildCard[i].length; j++) {
                if (wildCard[i][j] == null) continue;
                ItemStack wildCardCopy = wildCard[i][j].copy();
                int resultSize = result.size();
                for (int k = 0; k < resultSize; k++) {
                    ItemStack[] inputList = Utils.copyItemStackArray(result.get(k));
                    inputList[i] = wildCardCopy;
                    result.add(inputList);
                }
            }
        }
        return result;
    }

    @Override
    public void loadRecipes() {
        TwistSpaceTechnology.LOG.info("Loading Mega Assembly Line Recipes.");

        // skip these recipes
        ItemStack[] skipRecipeOutputs = new ItemStack[] { ItemList.Circuit_Wetwaremainframe.get(1),
            ItemList.Circuit_Biowaresupercomputer.get(1), ItemList.Circuit_Biomainframe.get(1),
            ItemList.Circuit_OpticalAssembly.get(1), ItemList.Circuit_OpticalComputer.get(1),
            ItemList.Circuit_OpticalMainframe.get(1), SpacetimeCompressionFieldGeneratorTier0.get(1),
            SpacetimeCompressionFieldGeneratorTier1.get(1), SpacetimeCompressionFieldGeneratorTier2.get(1),
            SpacetimeCompressionFieldGeneratorTier3.get(1), SpacetimeCompressionFieldGeneratorTier4.get(1),
            SpacetimeCompressionFieldGeneratorTier5.get(1), SpacetimeCompressionFieldGeneratorTier6.get(1),
            SpacetimeCompressionFieldGeneratorTier7.get(1), SpacetimeCompressionFieldGeneratorTier8.get(1),
            TimeAccelerationFieldGeneratorTier0.get(1), TimeAccelerationFieldGeneratorTier1.get(1),
            TimeAccelerationFieldGeneratorTier2.get(1), TimeAccelerationFieldGeneratorTier3.get(1),
            TimeAccelerationFieldGeneratorTier4.get(1), TimeAccelerationFieldGeneratorTier5.get(1),
            TimeAccelerationFieldGeneratorTier6.get(1), TimeAccelerationFieldGeneratorTier7.get(1),
            TimeAccelerationFieldGeneratorTier8.get(1), StabilisationFieldGeneratorTier0.get(1),
            StabilisationFieldGeneratorTier1.get(1), StabilisationFieldGeneratorTier2.get(1),
            StabilisationFieldGeneratorTier3.get(1), StabilisationFieldGeneratorTier4.get(1),
            StabilisationFieldGeneratorTier5.get(1), StabilisationFieldGeneratorTier6.get(1),
            StabilisationFieldGeneratorTier7.get(1), StabilisationFieldGeneratorTier8.get(1), };

        // start check assembly line recipes
        checkRecipe: for (var recipe : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes) {
            debugLogInfo("Recipe output: " + recipe.mOutput);

            for (ItemStack skip : skipRecipeOutputs) {
                // skip recipes need skip
                if (Utils.metaItemEqual(recipe.mOutput, skip)) {
                    debugLogInfo("Skip recipe.");
                    continue checkRecipe;
                }
            }

            ItemStack[] inputItems = new ItemStack[recipe.mInputs.length];
            ItemStack[][] inputWildcards = new ItemStack[recipe.mInputs.length][];
            boolean hasCustomWildcardItemList = false;

            if (recipe.mOreDictAlt != null && recipe.mOreDictAlt.length > 0) {
                // wildcards recipe
                for (int i = 0; i < recipe.mOreDictAlt.length; i++) {
                    if (recipe.mOreDictAlt[i] != null && recipe.mOreDictAlt[i].length > 0) {
                        ItemStack circuitStack = transToWildCircuit(recipe.mOreDictAlt[i][0]);
                        if (circuitStack != null) {
                            // this wildcard is a circuit stack
                            // replace it by dreamcraft:anyCircuit then the recipe will check this stack by any circuit
                            inputItems[i] = circuitStack;
                        } else {
                            // this wildcard is a custom list
                            hasCustomWildcardItemList = true;
                            inputWildcards[i] = recipe.mOreDictAlt[i];
                        }
                    } else {
                        // this stack is normal
                        inputItems[i] = recipe.mInputs[i];
                    }
                }
            } else {
                // no wildcards recipe
                inputItems = recipe.mInputs;
            }

            if (!hasCustomWildcardItemList) {
                debugLogInfo("Normal recipe generating.");
                GT_RecipeBuilder ra = GT_Values.RA.stdBuilder();
                ra.itemInputs(Utils.sortNoNullArray(inputItems))
                    .itemOutputs(recipe.mOutput);
                if (recipe.mFluidInputs != null) {
                    ra.fluidInputs(Utils.sortNoNullArray(recipe.mFluidInputs));
                }
                ra.noOptimize()
                    .eut(recipe.mEUt)
                    .duration(recipe.mDuration)
                    .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);

            } else {
                debugLogInfo("Wildcard recipe generating.");
                for (int i = 0; i < inputItems.length; i++) {
                    if (inputItems[i] == null) {
                        inputItems[i] = inputWildcards[i][0];
                    }
                }
                List<ItemStack[]> inputCombine = generateAllItemInput(inputItems, inputWildcards);
                debugLogInfo("inputCombine.size " + inputCombine.size());
                int loopFlag = 1;
                for (ItemStack[] inputs : inputCombine) {
                    debugLogInfo("generate " + loopFlag);
                    debugLogInfo("Input item list: " + Arrays.toString(inputs));
                    loopFlag++;

                    GT_RecipeBuilder ra = GT_Values.RA.stdBuilder();
                    ra.itemInputs(Utils.sortNoNullArray(inputs))
                        .itemOutputs(recipe.mOutput);
                    if (recipe.mFluidInputs != null) {
                        ra.fluidInputs(Utils.sortNoNullArray(recipe.mFluidInputs));
                    }
                    ra.noOptimize()
                        .eut(recipe.mEUt)
                        .duration(recipe.mDuration)
                        .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);
                }
            }
        }

        TwistSpaceTechnology.LOG.info("load Special Recipes.");
        loadSpecialRecipes();
        TwistSpaceTechnology.LOG.info("Mega Assembly Line Recipes loading complete.");
        debugLogInfo(
            "Mega Assembly Line Recipe List size: " + GTCMRecipe.AssemblyLineWithoutResearchRecipe.getAllRecipes()
                .size());
    }

    public void loadSpecialRecipes() {
        final IRecipeMap MASL = GTCMRecipe.AssemblyLineWithoutResearchRecipe;

        // EOH Blocks
        {
            // ME Digital singularity.
            final ItemStack ME_Singularity = getModItem(
                "appliedenergistics2",
                "item.ItemExtremeStorageCell.Singularity",
                1);

            final ItemStack[] boltList = new ItemStack[] {
                // Dense Shirabon plate.
                GT_OreDictUnificator.get("boltShirabon", 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 8),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 32),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 8),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 32),
                GT_OreDictUnificator
                    .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                GT_OreDictUnificator
                    .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8) };
            // spacetime 1
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T7.get(1),
                    ItemList.Quantum_Tank_IV.get(4),
                    new ItemStack(BlockInfinityChest.instance, 1),

                    GregtechItemList.CosmicFabricManipulator.get(1),
                    Utils.copyAmount(1, ME_Singularity),
                    MyMaterial.shirabon.get(OrePrefixes.bolt, 2),
                    CustomItemList.QuantumCircuit.get(1))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 20),
                    MaterialsUEVplus.Space.getMolten(144 * 10),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier0.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000)
                .addTo(MASL);

            // spacetime 2
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T7.get(2),
                    ItemList.Quantum_Tank_IV.get(4 * 2),
                    new ItemStack(BlockInfinityChest.instance, 2),

                    GregtechItemList.CosmicFabricManipulator.get(2),
                    Utils.copyAmount(2, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 2),
                    CustomItemList.QuantumCircuit.get(1))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 40),
                    MaterialsUEVplus.Space.getMolten(144 * 20),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier1.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 2)
                .addTo(MASL);

            // spacetime 3
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(3),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T7.get(3),
                    ItemList.Quantum_Tank_IV.get(4 * 3),
                    new ItemStack(BlockInfinityChest.instance, 3),

                    GregtechItemList.CosmicFabricManipulator.get(3),
                    Utils.copyAmount(3, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 8),
                    CustomItemList.QuantumCircuit.get(1))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 80),
                    MaterialsUEVplus.Space.getMolten(144 * 30),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier2.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 3)
                .addTo(MASL);

            // spacetime 4
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(4),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T8.get(1),
                    ItemList.Quantum_Tank_IV.get(4 * 4),
                    new ItemStack(BlockInfinityChest.instance, 4),

                    GregtechItemList.InfinityInfusedManipulator.get(1),
                    Utils.copyAmount(4, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 32),
                    CustomItemList.QuantumCircuit.get(2))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 160),
                    MaterialsUEVplus.Space.getMolten(144 * 40),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier3.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 4)
                .addTo(MASL);

            // spacetime 5
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(5),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T8.get(2),
                    ItemList.Quantum_Tank_IV.get(4 * 5),
                    new ItemStack(BlockInfinityChest.instance, 5),

                    GregtechItemList.InfinityInfusedManipulator.get(2),
                    Utils.copyAmount(5, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 2),
                    CustomItemList.QuantumCircuit.get(2))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 320),
                    MaterialsUEVplus.Space.getMolten(144 * 50),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier4.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 5)
                .addTo(MASL);

            // spacetime 6
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(6),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T8.get(3),
                    ItemList.Quantum_Tank_IV.get(4 * 6),
                    new ItemStack(BlockInfinityChest.instance, 6),

                    GregtechItemList.InfinityInfusedManipulator.get(3),
                    Utils.copyAmount(6, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 8),
                    CustomItemList.QuantumCircuit.get(2))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 640),
                    MaterialsUEVplus.Space.getMolten(144 * 60),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier5.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 6)
                .addTo(MASL);

            // spacetime 7
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(7),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T9.get(1),
                    ItemList.Quantum_Tank_IV.get(4 * 7),
                    new ItemStack(BlockInfinityChest.instance, 7),

                    GregtechItemList.SpaceTimeContinuumRipper.get(1),
                    Utils.copyAmount(7, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 32),
                    CustomItemList.QuantumCircuit.get(3))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 1280),
                    MaterialsUEVplus.Space.getMolten(144 * 70),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier6.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 7)
                .addTo(MASL);

            // spacetime 8
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(8),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T9.get(2),
                    ItemList.Quantum_Tank_IV.get(4 * 8),
                    new ItemStack(BlockInfinityChest.instance, 8),

                    GregtechItemList.SpaceTimeContinuumRipper.get(2),
                    Utils.copyAmount(8, ME_Singularity),
                    GT_OreDictUnificator
                        .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                    CustomItemList.QuantumCircuit.get(3))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 2560),
                    MaterialsUEVplus.Space.getMolten(144 * 80),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier7.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 8)
                .addTo(MASL);

            // spacetime 9
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(9),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T9.get(3),
                    ItemList.Quantum_Tank_IV.get(4 * 9),
                    new ItemStack(BlockInfinityChest.instance, 9),

                    GregtechItemList.SpaceTimeContinuumRipper.get(3),
                    Utils.copyAmount(9, ME_Singularity),
                    GT_OreDictUnificator
                        .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8),
                    CustomItemList.QuantumCircuit.get(3))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 5120),
                    MaterialsUEVplus.Space.getMolten(144 * 90),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier8.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 9)
                .addTo(MASL);

            // EOH Time Dilation Field Generators.
            {
                final ItemStack baseCasing = com.github.technus.tectech.thing.CustomItemList.EOH_Reinforced_Temporal_Casing
                    .get(1);

                // T0 - Shirabon
                // T1 - White Dwarf Matter
                // T2 - White Dwarf Matter
                // T3 - White Dwarf Matter
                // T4 - Black Dwarf Matter
                // T5 - Black Dwarf Matter
                // T6 - Black Dwarf Matter
                // T7 - Black Dwarf Matter
                // T8 - MHDCSM.

                final ItemStack[] fusionReactors = new ItemStack[] { ItemList.FusionComputer_ZPMV.get(1),
                    ItemList.FusionComputer_ZPMV.get(2), ItemList.FusionComputer_ZPMV.get(3),
                    ItemList.FusionComputer_UV.get(1), ItemList.FusionComputer_UV.get(2),
                    ItemList.FusionComputer_UV.get(3),
                    // MK4 Fusion Computer.
                    getModItem(GregTech.ID, "gt.blockmachines", 1, 965),
                    getModItem(GregTech.ID, "gt.blockmachines", 2, 965),
                    getModItem(GregTech.ID, "gt.blockmachines", 3, 965) };

                final ItemStack[] fusionCoils = new ItemStack[] {
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 1),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 2, 1),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 3, 1),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 2),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 2, 2),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 3, 2),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 3),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 2, 3),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 3, 3) };

                final ItemStack[] researchStuff = new ItemStack[] { baseCasing,
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier1.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier2.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier3.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier4.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier5.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier6.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier7.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(1) };

                for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_Utility.getIntegratedCircuit(absoluteTier + 1),

                            baseCasing,
                            fusionReactors[absoluteTier],
                            fusionCoils[absoluteTier],
                            getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", absoluteTier + 1, 0),

                            getItemContainer("QuantumCircuit").get(absoluteTier + 1),
                            getModItem(SuperSolarPanels.ID, "redcomponent", 64),
                            getModItem(SuperSolarPanels.ID, "greencomponent", 64),
                            getModItem(SuperSolarPanels.ID, "bluecomponent", 64),

                            boltList[absoluteTier],
                            getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 2),
                            getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 1),
                            getModItem(GregTech.ID, "gt.blockmachines", (absoluteTier + 1) * 4, 11107),

                            ItemList.Energy_Module.get(absoluteTier + 1),
                            GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, (absoluteTier + 1) * 4))
                        .fluidInputs(
                            MUTATED_LIVING_SOLDER.getFluidStack((int) (2_880 * pow(2, absoluteTier))),
                            MaterialsUEVplus.Time.getMolten(1_440 * (absoluteTier + 1)),
                            MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                        .itemOutputs(researchStuff[absoluteTier + 1])
                        .eut(RECIPE_UMV)
                        .duration((absoluteTier + 1) * 4_000 * 20)
                        .addTo(MASL);
                }

            }

            {
                final ItemStack baseCasing = com.github.technus.tectech.thing.CustomItemList.EOH_Infinite_Energy_Casing
                    .get(1);

                int baseCompPerSec = 16_384;

                // T0 - Shirabon
                // T1 - White Dwarf Matter
                // T2 - White Dwarf Matter
                // T3 - White Dwarf Matter
                // T4 - Black Dwarf Matter
                // T5 - Black Dwarf Matter
                // T6 - Black Dwarf Matter
                // T7 - Black Dwarf Matter
                // T8 - MHDCSM.

                final ItemStack[] researchStuff = new ItemStack[] { baseCasing,
                    com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier0.get(1),
                    com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier1.get(1),
                    com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier2.get(1),
                    com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier3.get(1),
                    com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier4.get(1),
                    com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier5.get(1),
                    com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier6.get(1),
                    com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier7.get(1),
                    com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(1) };

                final ItemStack[] timeCasings = new ItemStack[] {
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier1.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier2.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier3.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier4.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier5.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier6.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier7.get(1),
                    com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(1) };

                final ItemStack[] spatialCasings = new ItemStack[] {
                    com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                    com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier1.get(1),
                    com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(1),
                    com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier3.get(1),
                    com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier4.get(1),
                    com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier5.get(1),
                    com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier6.get(1),
                    com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier7.get(1),
                    com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1) };

                for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            timeCasings[absoluteTier],
                            spatialCasings[absoluteTier],
                            baseCasing,
                            // Dyson Swarm Module.
                            getModItem(GalaxySpace.ID, "item.DysonSwarmParts", 4 * (absoluteTier + 1), 0),

                            GT_OreDictUnificator
                                .get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 4 * (absoluteTier + 1)),
                            GT_OreDictUnificator
                                .get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 4 * (absoluteTier + 1)),
                            GT_OreDictUnificator
                                .get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 4 * (absoluteTier + 1)),
                            GT_OreDictUnificator.get(
                                OrePrefixes.frameGt,
                                Materials.Longasssuperconductornameforuhvwire,
                                4 * (absoluteTier + 1)),

                            // Gravitation Engine
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),

                            boltList[absoluteTier],
                            getItemContainer("QuantumCircuit").get(2 * (absoluteTier + 1)),
                            GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, absoluteTier + 1),
                            GT_OreDictUnificator
                                .get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, absoluteTier + 1))
                        .fluidInputs(
                            MUTATED_LIVING_SOLDER.getFluidStack((int) (2_880 * pow(2, absoluteTier))),
                            MaterialsUEVplus.Time.getMolten(1_440 * (absoluteTier + 1)),
                            MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                            MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                        .itemOutputs(researchStuff[absoluteTier + 1])
                        .eut(RECIPE_UMV)
                        .duration((absoluteTier + 1) * 4_000 * 20)
                        .addTo(MASL);
                }

            }

        }

    }

}
