package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MiracleTop;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UXV;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.dreammaster.item.NHItemList;

import bartworks.system.material.Werkstoff;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class MegaUniversalSpaceStationRecipePool {

    final IRecipeMap stationRecipe = GTCMRecipe.megaUniversalSpaceStationRecipePool;
    static final IRecipeMap MT = GTCMRecipe.MiracleTopRecipes;
    // region item

    static ItemStack[] spaceStationStructureBlock;
    static ItemStack[] SpaceStationAntiGravityBlock;
    static ItemStack[] highDimensionalItem;
    static ItemStack[] blockCasings = new ItemStack[] { ItemList.Casing_ULV.get(4), ItemList.Casing_LV.get(4),
        ItemList.Casing_MV.get(4), ItemList.Casing_HV.get(4), ItemList.Casing_EV.get(4), ItemList.Casing_IV.get(4),
        ItemList.Casing_LuV.get(16), ItemList.Casing_ZPM.get(16), ItemList.Casing_UV.get(16),
        ItemList.Casing_MAX.get(16),// ATTENTION! THIS IS UHV CASING!
    };
    static ItemStack[] blockCasing6s = new ItemStack[] { ItemList.Casing_Tank_0.get(1), ItemList.Casing_Tank_1.get(1),
        ItemList.Casing_Tank_2.get(1), ItemList.Casing_Tank_3.get(1), ItemList.Casing_Tank_4.get(1),
        ItemList.Casing_Tank_5.get(1), ItemList.Casing_Tank_6.get(1), ItemList.Casing_Tank_7.get(1),
        ItemList.Casing_Tank_8.get(1), ItemList.Casing_Tank_9.get(1) };

    static ItemStack[] processor = new ItemStack[] { ItemList.Circuit_Processor.get(16),
        ItemList.Circuit_Nanoprocessor.get(16), ItemList.Circuit_Quantumprocessor.get(16),
        ItemList.Circuit_Crystalprocessor.get(16), ItemList.Circuit_Neuroprocessor.get(16),
        ItemList.Circuit_Bioprocessor.get(16), ItemList.Circuit_OpticalProcessor.get(16),
        ItemList.Circuit_ExoticProcessor.get(16), ItemList.Circuit_CosmicProcessor.get(16),
        ItemList.Circuit_TranscendentProcessor.get(16), };
    static ItemStack[] Conveyor_Modules = new ItemStack[] { ItemList.Conveyor_Module_LV.get(1),
        ItemList.Conveyor_Module_MV.get(2), ItemList.Conveyor_Module_HV.get(3), ItemList.Conveyor_Module_EV.get(4),
        ItemList.Conveyor_Module_IV.get(5), ItemList.Conveyor_Module_LuV.get(6), ItemList.Conveyor_Module_ZPM.get(7),
        ItemList.Conveyor_Module_UV.get(8), ItemList.Conveyor_Module_UHV.get(9), ItemList.Conveyor_Module_UEV.get(10),
        ItemList.Conveyor_Module_UIV.get(11), ItemList.Conveyor_Module_UMV.get(12),
        ItemList.Conveyor_Module_UXV.get(13), ItemList.Conveyor_Module_MAX.get(14) };
    static ItemStack[] Electric_Pistons = new ItemStack[] { ItemList.Electric_Piston_LV.get(1),
        ItemList.Electric_Piston_MV.get(2), ItemList.Electric_Piston_HV.get(3), ItemList.Electric_Piston_EV.get(4),
        ItemList.Electric_Piston_IV.get(5), ItemList.Electric_Piston_LuV.get(6), ItemList.Electric_Piston_ZPM.get(7),
        ItemList.Electric_Piston_UV.get(8), ItemList.Electric_Piston_UHV.get(9), ItemList.Electric_Piston_UEV.get(10),
        ItemList.Electric_Piston_UIV.get(11), ItemList.Electric_Piston_UMV.get(12),
        ItemList.Electric_Piston_UXV.get(13), ItemList.Electric_Piston_MAX.get(14) };
    static ItemStack[] Robot_Arms = new ItemStack[] { ItemList.Robot_Arm_LV.get(1), ItemList.Robot_Arm_MV.get(2),
        ItemList.Robot_Arm_HV.get(3), ItemList.Robot_Arm_EV.get(4), ItemList.Robot_Arm_IV.get(5),
        ItemList.Robot_Arm_LuV.get(6), ItemList.Robot_Arm_ZPM.get(7), ItemList.Robot_Arm_UV.get(8),
        ItemList.Robot_Arm_UHV.get(9), ItemList.Robot_Arm_UEV.get(10), ItemList.Robot_Arm_UIV.get(11),
        ItemList.Robot_Arm_UMV.get(12), ItemList.Robot_Arm_UXV.get(13), ItemList.Robot_Arm_MAX.get(14) };
    static ItemStack[] Emitters = new ItemStack[] { ItemList.Emitter_LV.get(1), ItemList.Emitter_MV.get(2),
        ItemList.Emitter_HV.get(3), ItemList.Emitter_EV.get(4), ItemList.Emitter_IV.get(5), ItemList.Emitter_LuV.get(6),
        ItemList.Emitter_ZPM.get(7), ItemList.Emitter_UV.get(8), ItemList.Emitter_UHV.get(9),
        ItemList.Emitter_UEV.get(10), ItemList.Emitter_UIV.get(11), ItemList.Emitter_UMV.get(12),
        ItemList.Emitter_UXV.get(13), ItemList.Emitter_MAX.get(14) };
    static ItemStack[] Sensors = new ItemStack[] { ItemList.Sensor_LV.get(1), ItemList.Sensor_MV.get(2),
        ItemList.Sensor_HV.get(3), ItemList.Sensor_EV.get(4), ItemList.Sensor_IV.get(5), ItemList.Sensor_LuV.get(6),
        ItemList.Sensor_ZPM.get(7), ItemList.Sensor_UV.get(8), ItemList.Sensor_UHV.get(9), ItemList.Sensor_UEV.get(10),
        ItemList.Sensor_UIV.get(11), ItemList.Sensor_UMV.get(12), ItemList.Sensor_UXV.get(13),
        ItemList.Sensor_MAX.get(14) };
    static ItemStack[] Field_Generators = new ItemStack[] { ItemList.Field_Generator_LV.get(4),
        ItemList.Field_Generator_MV.get(4), ItemList.Field_Generator_HV.get(4), ItemList.Field_Generator_EV.get(4),
        ItemList.Field_Generator_IV.get(4), ItemList.Field_Generator_LuV.get(4), ItemList.Field_Generator_ZPM.get(4),
        ItemList.Field_Generator_UV.get(4), ItemList.Field_Generator_UHV.get(4), ItemList.Field_Generator_UEV.get(4),
        ItemList.Field_Generator_UIV.get(4), ItemList.Field_Generator_UMV.get(4), ItemList.Field_Generator_UXV.get(4),
        ItemList.Field_Generator_MAX.get(4) };

    static Werkstoff[] maxMaterials;

    static ItemStack[] uevPlusCircuit = new ItemStack[] { ItemList.Circuit_ExoticProcessor.get(8),
        ItemList.Circuit_ExoticAssembly.get(4), ItemList.Circuit_ExoticComputer.get(2),
        ItemList.Circuit_ExoticMainframe.get(1), ItemList.Circuit_CosmicProcessor.get(8),
        ItemList.Circuit_CosmicAssembly.get(4), ItemList.Circuit_CosmicComputer.get(2),
        ItemList.Circuit_CosmicMainframe.get(1), ItemList.Circuit_TranscendentProcessor.get(8),
        ItemList.Circuit_TranscendentAssembly.get(4), ItemList.Circuit_TranscendentComputer.get(2),
        ItemList.Circuit_TranscendentMainframe.get(1) };

    // endregion

    public static void loadRecipes() {
        if (Config.activateMegaSpaceStation) {
            spaceStationStructureBlock = new ItemStack[] { GTCMItemList.spaceStationStructureBlockLV.get(1),
                GTCMItemList.spaceStationStructureBlockMV.get(1), GTCMItemList.spaceStationStructureBlockHV.get(1),
                GTCMItemList.spaceStationStructureBlockEV.get(1), GTCMItemList.spaceStationStructureBlockIV.get(1),
                GTCMItemList.spaceStationStructureBlockLuV.get(1), GTCMItemList.spaceStationStructureBlockZPM.get(1),
                GTCMItemList.spaceStationStructureBlockUV.get(1), GTCMItemList.spaceStationStructureBlockUHV.get(1),
                GTCMItemList.spaceStationStructureBlockUEV.get(1), GTCMItemList.spaceStationStructureBlockUIV.get(1),
                GTCMItemList.spaceStationStructureBlockUMV.get(1), GTCMItemList.spaceStationStructureBlockUXV.get(1),
                GTCMItemList.spaceStationStructureBlockMAX.get(1) };
            SpaceStationAntiGravityBlock = new ItemStack[] { GTCMItemList.SpaceStationAntiGravityBlockLV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockMV.get(1), GTCMItemList.SpaceStationAntiGravityBlockHV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockEV.get(1), GTCMItemList.SpaceStationAntiGravityBlockIV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockLuV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockZPM.get(1), GTCMItemList.SpaceStationAntiGravityBlockUV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockUHV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockUEV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockUIV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockUMV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockUXV.get(1),
                GTCMItemList.SpaceStationAntiGravityBlockMAX.get(1) };
            highDimensionalItem = new ItemStack[] { GTCMItemList.HighDimensionalCapacitor.get(64),
                GTCMItemList.HighDimensionalInterface.get(64), GTCMItemList.HighDimensionalDiode.get(64),
                GTCMItemList.HighDimensionalResistor.get(64), GTCMItemList.HighDimensionalTransistor.get(64) };
            maxMaterials = new Werkstoff[] { MaterialPool.eventHorizonDiffusers, MaterialPool.entropyReductionProcess,
                MaterialPool.realSingularity, };
            loadOriginalRecipeForConstruct();
            loadCircuitRecipe();
        }

    }

    public static void loadCircuitRecipe() {

        // region umv
        var processor = uevPlusCircuit[4].copy();
        processor.stackSize = 4;
        GTValues.RA.stdBuilder()
            .itemInputs(
                highDimensionalItem[3],
                highDimensionalItem[4],
                GTCMItemList.HighDimensionalCircuitDoard.get(48),
                maxMaterials[0].get(OrePrefixes.bolt),
                maxMaterials[0].get(OrePrefixes.wireGt16),
                GGMaterial.shirabon.get(OrePrefixes.plate, 64))
            .fluidInputs(Materials.Universium.getFluid(1440))
            .itemOutputs(processor)

            .eut(RECIPE_UMV)
            .duration(20)
            .addTo(MT);
        var assembly = uevPlusCircuit[5].copy();
        assembly.stackSize = 2;
        GTValues.RA.stdBuilder()
            .itemInputs(
                highDimensionalItem[0],
                highDimensionalItem[1],
                processor,
                GTCMItemList.Antimatter.get(64),
                GTCMItemList.AnnihilationConstrainer.get(64),
                Materials.Universium.getNanite(64))
            .fluidInputs(
                MaterialPool.entropyReductionProcess.getBridgeMaterial()
                    .getFluid(1440))
            .itemOutputs(assembly)

            .eut(RECIPE_UMV)
            .duration(200)
            .addTo(MT);
        var computer = uevPlusCircuit[6].copy();
        computer.stackSize = 1;
        GTValues.RA.stdBuilder()
            .itemInputs(
                highDimensionalItem[2],
                highDimensionalItem[4],
                assembly,
                Materials.DimensionallyTranscendentStellarCatalyst.getPlates(64),
                MaterialPool.entropyReductionProcess.getBridgeMaterial()
                    .getNanite(64),
                MaterialPool.realSingularity.getBridgeMaterial()
                    .getPlates(64))
            .fluidInputs(
                MaterialPool.realSingularity.getBridgeMaterial()
                    .getFluid(1440))
            .itemOutputs(computer)

            .eut(RECIPE_UMV)
            .duration(2000)
            .addTo(MT);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UEV.get(16),
                GTCMItemList.HighDimensionalResistor.get(16),
                GTCMItemList.HighDimensionalDiode.get(16),
                GTCMItemList.HighDimensionalTransistor.get(16),
                GTCMItemList.HighDimensionalCapacitor.get(16),
                GTCMItemList.HighDimensionalInterface.get(16),
                GTCMItemList.CosmicCircuitBoard.get(16),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(16),
                GTCMItemList.EventHorizonNanoSwarm.get(8),
                ItemList.Circuit_ExoticComputer.get(2))
            .fluidInputs(
                Materials.SpaceTime.getMolten(144 * 8 * 16),
                Materials.TranscendentMetal.getMolten(144 * 8 * 16),
                Materials.Neutronium.getMolten(144 * 8 * 16 * 144),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 16 * 144))
            .itemOutputs(ItemList.Circuit_ExoticMainframe.get(1))

            .eut(RECIPE_UMV)
            .duration(20 * 1000)
            .addTo(MT);

        // endregion

        // region uxv
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UEV.get(2),
                GTCMItemList.HighDimensionalResistor.get(2),
                GTCMItemList.HighDimensionalDiode.get(2),
                GTCMItemList.HighDimensionalTransistor.get(2),
                GTCMItemList.HighDimensionalCapacitor.get(2),
                GTCMItemList.HighDimensionalInterface.get(2),
                GTCMItemList.CosmicCircuitBoard.get(2),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(2),
                GTCMItemList.EventHorizonNanoSwarm.get(2),
                GTCMItemList.Self_adaptiveAI1.get(16))
            .fluidInputs(
                Materials.SpaceTime.getMolten(144 * 8 * 6),
                Materials.TranscendentMetal.getMolten(144 * 8 * 6),
                Materials.Neutronium.getMolten(144 * 8 * 6 * 144),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 6 * 144))
            .itemOutputs(ItemList.Circuit_CosmicProcessor.get(1))

            .eut(RECIPE_UXV)
            .duration(1600)
            .addTo(MT);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UEV.get(8),
                GTCMItemList.HighDimensionalResistor.get(8),
                GTCMItemList.HighDimensionalDiode.get(8),
                GTCMItemList.HighDimensionalTransistor.get(8),
                GTCMItemList.HighDimensionalCapacitor.get(8),
                GTCMItemList.HighDimensionalInterface.get(8),
                GTCMItemList.CosmicCircuitBoard.get(8),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(8),
                GTCMItemList.EventHorizonNanoSwarm.get(4),
                GTCMItemList.Self_adaptiveAI3.get(16),
                ItemList.Circuit_CosmicProcessor.get(2))
            .fluidInputs(
                Materials.SpaceTime.getMolten(144 * 8 * 6),
                Materials.TranscendentMetal.getMolten(144 * 8 * 6),
                Materials.Neutronium.getMolten(144 * 8 * 6 * 144),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 6 * 144))
            .itemOutputs(ItemList.Circuit_CosmicAssembly.get(1))

            .eut(RECIPE_UXV)
            .duration(1600)
            .addTo(MT);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UEV.get(12),
                GTCMItemList.HighDimensionalResistor.get(12),
                GTCMItemList.HighDimensionalDiode.get(12),
                GTCMItemList.HighDimensionalTransistor.get(12),
                GTCMItemList.HighDimensionalCapacitor.get(12),
                GTCMItemList.HighDimensionalInterface.get(12),
                GTCMItemList.CosmicCircuitBoard.get(12),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(12),
                GTCMItemList.EventHorizonNanoSwarm.get(6),
                ItemList.Circuit_CosmicAssembly.get(2))
            .fluidInputs(
                Materials.SpaceTime.getMolten(144 * 8 * 12),
                Materials.TranscendentMetal.getMolten(144 * 8 * 12),
                Materials.Neutronium.getMolten(144 * 8 * 12 * 144),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 12 * 144))
            .itemOutputs(ItemList.Circuit_CosmicComputer.get(1))

            .eut(RECIPE_UXV)
            .duration(20 * 1000)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Field_Generator_UIV.get(16),
                GTCMItemList.HighDimensionalResistor.get(32),
                GTCMItemList.HighDimensionalDiode.get(32),
                GTCMItemList.HighDimensionalTransistor.get(32),
                GTCMItemList.HighDimensionalCapacitor.get(32),
                GTCMItemList.HighDimensionalInterface.get(32),
                GTCMItemList.ExoticCircuitBoard.get(16),
                GTCMItemList.MicroDimensionOutput.get(16),
                GTCMItemList.EntropyReductionMaterialNanoswarm.get(16),
                ItemList.Circuit_CosmicComputer.get(2))
            .fluidInputs(Materials.Time.getMolten(1000 * 16), Materials.Space.getMolten(1000 * 16))
            .itemOutputs(ItemList.Circuit_CosmicMainframe.get(1))

            .eut(RECIPE_MAX)
            .duration(20 * 1000)
            .addTo(MT);
        // endregion

        // region max

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UMV.get(1),
                GTCMItemList.HighDimensionalResistor.get(2),
                GTCMItemList.HighDimensionalDiode.get(2),
                GTCMItemList.HighDimensionalTransistor.get(2),
                GTCMItemList.HighDimensionalCapacitor.get(2),
                GTCMItemList.HighDimensionalInterface.get(2),
                GTCMItemList.CosmicCircuitBoard.get(2),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(2),
                GTCMItemList.RealSingularityNanoSwarm.get(4),
                GTCMItemList.CoreOfT800.get(2))
            .fluidInputs(
                Materials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(144),
                MaterialPool.entropyReductionProcess.getMolten(1440),
                MaterialPool.eventHorizonDiffusers.getMolten(1440),
                MaterialPool.realSingularity.getMolten(144))
            .itemOutputs(ItemList.Circuit_TranscendentProcessor.get(1))

            .eut(RECIPE_UXV)
            .duration(20 * 1000)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UEV.get(8),
                GTCMItemList.HighDimensionalResistor.get(8),
                GTCMItemList.HighDimensionalDiode.get(8),
                GTCMItemList.HighDimensionalTransistor.get(8),
                GTCMItemList.HighDimensionalCapacitor.get(8),
                GTCMItemList.HighDimensionalInterface.get(8),
                GTCMItemList.CosmicCircuitBoard.get(8),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(8),
                GTCMItemList.EventHorizonNanoSwarm.get(4),
                ItemList.Circuit_TranscendentProcessor.get(2))
            .fluidInputs(
                Materials.SpaceTime.getMolten(144 * 8 * 12),
                Materials.TranscendentMetal.getMolten(144 * 8 * 12),
                Materials.Neutronium.getMolten(144 * 8 * 12 * 144),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 12 * 144))
            .itemOutputs(ItemList.Circuit_TranscendentAssembly.get(1))

            .eut(RECIPE_UXV)
            .duration(20 * 1000)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UEV.get(16),
                GTCMItemList.HighDimensionalResistor.get(16),
                GTCMItemList.HighDimensionalDiode.get(16),
                GTCMItemList.HighDimensionalTransistor.get(16),
                GTCMItemList.HighDimensionalCapacitor.get(16),
                GTCMItemList.HighDimensionalInterface.get(16),
                GTCMItemList.CosmicCircuitBoard.get(16),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(16),
                GTCMItemList.EventHorizonNanoSwarm.get(8),
                ItemList.Circuit_TranscendentAssembly.get(2),
                GTCMItemList.CoreOfT800.get(16))
            .fluidInputs(
                Materials.SpaceTime.getMolten(144 * 8 * 20),
                MaterialPool.entropyReductionProcess.getMolten(1440000),
                MaterialPool.eventHorizonDiffusers.getMolten(1440000),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 20 * 144))
            .itemOutputs(ItemList.Circuit_TranscendentComputer.get(1))

            .eut(RECIPE_UXV)
            .duration(20 * 1000)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.EnergisedTesseract.get(64),
                ItemList.Field_Generator_UMV.get(16),
                GTCMItemList.HighDimensionalResistor.get(64),
                GTCMItemList.HighDimensionalDiode.get(64),
                GTCMItemList.HighDimensionalTransistor.get(64),
                GTCMItemList.HighDimensionalCapacitor.get(64),
                GTCMItemList.HighDimensionalInterface.get(64),
                GTCMItemList.TranscendentCircuitBoard.get(16),
                GTCMItemList.NarrativeLayerOverwritingDevice.get(16),
                GTCMItemList.HyperspaceNarrativeLayerAdaptiveSpecialSRA.get(16),
                GTCMItemList.RealSingularityNanoSwarm.get(8),
                ItemList.Circuit_TranscendentComputer.get(2))
            .fluidInputs(
                Materials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(144000),
                MaterialPool.entropyReductionProcess.getMolten(14400000),
                MaterialPool.eventHorizonDiffusers.getMolten(14400000),
                MaterialPool.realSingularity.getMolten(144000))
            .itemOutputs(ItemList.Circuit_TranscendentMainframe.get(1))

            .eut(RECIPE_MAX * 16)
            .duration(20 * 1500)
            .addTo(MT);
        // endregion

        // MAX电动马达
        // 物品输入：
        // 充能超立方体 4
        // 长realSingularity杆 16
        // realSingularity环 8
        // realSingularity滚珠 32
        // 细 熵还原物质导线 64*8
        // 16*时空导线 64
        // 16*无尽导线 64
        // 宇宙素纳米蜂群 4
        // 流体输入：
        // 576mB 熵还原物质
        // 576mB 事件视界
        // 576mB realSingularity
        // 2304mB 蓝框岩浆

        // MAX电动泵
        // 物品输入：
        // 充能超立方体 4
        // 大型realSingularity流体管道 16
        // realSingularity板 4
        // realSingularity螺丝 16
        // 拉多X聚合物环 64*4
        // 熵还原物质转子 4
        // 事件视界转子 4
        // 16*时空导线 64
        // 16*无尽导线 64
        // 宇宙素纳米蜂群 4
        // 流体输入：
        // 576mB 熵还原物质
        // 576mB 事件视界
        // 576mB realSingularity
        // 2304mB 蓝框岩浆

        // MAX电动泵
        // 物品输入：
        // 充能超立方体 4
        // 大型realSingularity流体管道 16
        // realSingularity板 4
        // realSingularity螺丝 16
        // 拉多X聚合物环 64*4
        // 熵还原物质转子 4
        // 事件视界转子 4
        // 16*时空导线 64
        // 16*无尽导线 64
        // 宇宙素纳米蜂群 4
        // 流体输入：
        // 576mB 熵还原物质
        // 576mB 事件视界
        // 576mB realSingularity
        // 2304mB 蓝框岩浆

        // MAX机械臂
        // 物品输入：
        // 充能超立方体 4
        // MAX电动马达 2
        // MAX电动活塞 2
        // 长realSingularity杆 16
        // realSingularity齿轮 4
        // 熵还原物质齿轮 4
        // 小realSingularity齿轮 16
        // 小熵还原物质齿轮 16
        // 熵还原物质转子 4
        // 事件视界转子 4
        // MAX电路板 4
        // UXV电路板 8
        // UMV电路板 16
        // 16*时空导线 64
        // 16*无尽导线 64
        // 宇宙素纳米蜂群 4
        // 流体输入：
        // 576mB 熵还原物质
        // 576mB 事件视界
        // 576mB realSingularity
        // 2304mB 蓝框岩浆

        // MAX传送带
        // 物品输入：
        // 充能超立方体 4
        // MAX电动马达 2
        // 致密realSingularity板 4
        // realSingularity环 16
        // realSingularity滚珠 64
        // 16*时空导线 64
        // 16*无尽导线 64
        // 致密凯芙拉板 64+32
        // 致密拉多X聚合物板 64+32
        // 宇宙素纳米蜂群 4
        // 流体输入：
        // 576mB 熵还原物质
        // 576mB 事件视界
        // 576mB realSingularity
        // 2304mB 蓝框岩浆

        // MAX电动活塞
        // 物品输入：
        // 充能超立方体 4
        // MAX电动马达 1
        // 致密realSingularity板 8
        // realSingularity环 16
        // realSingularity滚珠 64
        // realSingularity杆 16
        // realSingularity齿轮 4
        // 熵还原物质齿轮 4
        // 小型realSingularity齿轮 8
        // 16*时空导线 64
        // 16*无尽导线 64
        // 宇宙素纳米蜂群 4
        // 流体输入：
        // 576mB 熵还原物质
        // 576mB 事件视界
        // 576mB realSingularity
        // 2304mB 蓝框岩浆

        // MAX发射器
        // 物品输入：
        // 充能超立方体 16
        // MAX电动马达 1
        // 长realSingularity杆 64
        // 扭曲时空之星 64
        // realSingularity箔 64
        // 熵还原物质箔 64
        // 事件视界物质箔 64
        // 蓝框岩浆箔 64
        // MAX电路板 16
        // 16*时空导线 64
        // 16*无尽导线 64
        // 宇宙素纳米蜂群 16
        // 流体输入：
        // 576mB 熵还原物质
        // 576mB 事件视界
        // 576mB realSingularity
        // 2304mB 蓝框岩浆

        // MAX传感器
        // 物品输入：
        // 充能超立方体 16
        // MAX电动马达 1
        // 致密realSingularity板 32
        // 扭曲时空之星 64
        // realSingularity箔 64
        // 熵还原物质箔 64
        // 事件视界物质箔 64
        // 蓝框岩浆箔 64
        // MAX电路板 16
        // 16*时空导线 64
        // 16*无尽导线 64
        // 宇宙素纳米蜂群 16
        // 流体输入：
        // 576mB 熵还原物质
        // 576mB 事件视界
        // 576mB realSingularity
        // 2304mB 蓝框岩浆

        // MAX力场发生器
        // 物品输入：
        // 充能超立方体 64
        // MAX发射器 16
        // 致密realSingularity板 64
        // 扭曲时空之星 64（576mB时空+核能之星，流体固化，UIV20S）
        // 细realSingularity导线 64*2
        // 细熵还原物质导线 64*2
        // 细事件视界物质导线 64*2
        // 细蓝框岩浆导线 64*2
        // MAX电路板 64
        // 16*时空导线 64
        // 16*无尽导线 64
        // 宇宙素纳米蜂群 32
        // 流体输入：
        // 576mB 熵还原物质
        // 576mB 事件视界
        // 576mB realSingularity
        // 2304mB 蓝框岩浆

        // 所有的MAX部件配方加工时间均为UXV 50S
        // 放进空间站的配方，只需要除了流体输入和合成表中输入的MAX部件以外，其余物品材料数量均除以2
    }

    // this method loads recipe for recipe that not use MUSS but related to MUSS, eg, structure blocks and controller
    // blocks recipes.
    public static void loadOriginalRecipeForConstruct() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_OpticalMainframe.get(64),
                ItemList.Circuit_OpticalMainframe.get(64),
                NHItemList.StargateFramePart.get(64),
                NHItemList.StargateShieldingFoil.get(64),
                NHItemList.StargateFramePart.get(64),
                // GT_material.TestMaterial.getPlates(64),
                NHItemList.StargateChevron.get(64),
                tectech.thing.CustomItemList.Machine_Multi_EyeOfHarmony.get(64),
                tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                MiracleTop.get(64))

            .itemOutputs(GTCMItemList.megaUniversalSpaceStation.get(1))

            .eut(1024 * RECIPE_MAX)
            .duration(20 * 1000)
            .addTo(GTCMRecipe.MiracleTopRecipes);

        for (int i = 0; i < 10; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(blockCasings[i], blockCasing6s[i], processor[Math.max(i - 3, 0)])
                .fluidInputs(Materials.Plastic.getFluid(1440 * i))
                .itemOutputs(spaceStationStructureBlock[i])

                .eut((int) (32 * Math.pow(4, i)))
                .duration(20 * 10 * i)
                .addTo(GTRecipeConstants.AssemblerOD);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    spaceStationStructureBlock[2 + i],
                    Field_Generators[2 + i],
                    Sensors[2 + i],
                    Robot_Arms[2 + i])

                .itemOutputs(SpaceStationAntiGravityBlock[i])

                .eut((long) (32 * Math.pow(2, i)))
                .duration(20 * 10 * i)
                .addTo(GTRecipeConstants.AssemblerOD);
        }
        for (int i = 10; i < 12; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    spaceStationStructureBlock[i - 1],
                    processor[i - 3],
                    Emitters[i],
                    Electric_Pistons[i],
                    Conveyor_Modules[i],
                    Materials.SuperconductorUMV.getPlates(64)

                )
                .fluidInputs(Materials.TranscendentMetal.getFluid(1440 * i))
                .itemOutputs(spaceStationStructureBlock[i])

                .eut((long) (Math.pow(16, i)))
                .duration(200 * 10 * i)
                .addTo(GTCMRecipe.MiracleTopRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    spaceStationStructureBlock[i],
                    Field_Generators[i],
                    Sensors[i],
                    Robot_Arms[i],
                    MaterialPool.eventHorizonDiffusers.getBridgeMaterial()
                        .getPlates(64))
                .fluidInputs()
                .itemOutputs(SpaceStationAntiGravityBlock[i])

                .eut((long) (32 * Math.pow(2, i)))
                .duration(20 * 10 * i)
                .addTo(GTRecipeConstants.AssemblerOD);
        }

    }
}
