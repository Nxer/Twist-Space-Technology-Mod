package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MiracleTop;
import static com.github.technus.tectech.thing.CustomItemList.*;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import goodgenerator.items.MyMaterial;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.dreammaster.gthandler.CustomItemList;

import gregtech.api.enums.*;
import gregtech.api.util.GT_Recipe;

public class MegaUniversalSpaceStationRecipePool implements IRecipePool {

    final GT_Recipe.GT_Recipe_Map stationRecipe = GTCMRecipe.instance.megaUniversalSpaceStationRecipePool;
    // region item
    ItemStack[] spaceStationStructureBlock = new ItemStack[]{GTCMItemList.spaceStationStructureBlockLV.get(1),
        GTCMItemList.spaceStationStructureBlockMV.get(1), GTCMItemList.spaceStationStructureBlockHV.get(1),
        GTCMItemList.spaceStationStructureBlockEV.get(1), GTCMItemList.spaceStationStructureBlockIV.get(1),
        GTCMItemList.spaceStationStructureBlockLuV.get(1), GTCMItemList.spaceStationStructureBlockZPM.get(1),
        GTCMItemList.spaceStationStructureBlockUV.get(1), GTCMItemList.spaceStationStructureBlockUHV.get(1),
        GTCMItemList.spaceStationStructureBlockUEV.get(1), GTCMItemList.spaceStationStructureBlockUIV.get(1),
        GTCMItemList.spaceStationStructureBlockUMV.get(1), GTCMItemList.spaceStationStructureBlockUXV.get(1),
        GTCMItemList.spaceStationStructureBlockMAX.get(1)};
    ItemStack[] SpaceStationAntiGravityBlock = new ItemStack[]{GTCMItemList.SpaceStationAntiGravityBlockLV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockMV.get(1), GTCMItemList.SpaceStationAntiGravityBlockHV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockEV.get(1), GTCMItemList.SpaceStationAntiGravityBlockIV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockLuV.get(1), GTCMItemList.SpaceStationAntiGravityBlockZPM.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockUV.get(1), GTCMItemList.SpaceStationAntiGravityBlockUHV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockUEV.get(1), GTCMItemList.SpaceStationAntiGravityBlockUIV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockUMV.get(1), GTCMItemList.SpaceStationAntiGravityBlockUXV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockMAX.get(1)};
    ItemStack[] blockCasings = new ItemStack[]{ItemList.Casing_ULV.get(4), ItemList.Casing_LV.get(4),
        ItemList.Casing_MV.get(4), ItemList.Casing_HV.get(4), ItemList.Casing_EV.get(4), ItemList.Casing_IV.get(4),
        ItemList.Casing_LuV.get(16), ItemList.Casing_ZPM.get(16), ItemList.Casing_UV.get(16),
        ItemList.Casing_MAX.get(16),// ATTENTION! THIS IS UHV CASING!
    };
    ItemStack[] blockCasing6s = new ItemStack[]{ItemList.Casing_Tank_0.get(1), ItemList.Casing_Tank_1.get(1),
        ItemList.Casing_Tank_2.get(1), ItemList.Casing_Tank_3.get(1), ItemList.Casing_Tank_4.get(1),
        ItemList.Casing_Tank_5.get(1), ItemList.Casing_Tank_6.get(1), ItemList.Casing_Tank_7.get(1),
        ItemList.Casing_Tank_8.get(1), ItemList.Casing_Tank_9.get(1)};

    ItemStack[] processor = new ItemStack[]{ItemList.Circuit_Processor.get(16), ItemList.Circuit_Nanoprocessor.get(16),
        ItemList.Circuit_Quantumprocessor.get(16), ItemList.Circuit_Crystalprocessor.get(16),
        ItemList.Circuit_Neuroprocessor.get(16), ItemList.Circuit_Bioprocessor.get(16),
        ItemList.Circuit_OpticalProcessor.get(16), ItemList.Circuit_ExoticProcessor.get(16),
        ItemList.Circuit_CosmicProcessor.get(16), ItemList.Circuit_TranscendentProcessor.get(16),};
    ItemStack[] Conveyor_Modules = new ItemStack[]{ItemList.Conveyor_Module_LV.get(1),
        ItemList.Conveyor_Module_MV.get(2), ItemList.Conveyor_Module_HV.get(3), ItemList.Conveyor_Module_EV.get(4),
        ItemList.Conveyor_Module_IV.get(5), ItemList.Conveyor_Module_LuV.get(6), ItemList.Conveyor_Module_ZPM.get(7),
        ItemList.Conveyor_Module_UV.get(8), ItemList.Conveyor_Module_UHV.get(9), ItemList.Conveyor_Module_UEV.get(10),
        ItemList.Conveyor_Module_UIV.get(11), ItemList.Conveyor_Module_UMV.get(12),
        ItemList.Conveyor_Module_UXV.get(13), ItemList.Conveyor_Module_MAX.get(14)};
    ItemStack[] Electric_Pistons = new ItemStack[]{ItemList.Electric_Piston_LV.get(1),
        ItemList.Electric_Piston_MV.get(2), ItemList.Electric_Piston_HV.get(3), ItemList.Electric_Piston_EV.get(4),
        ItemList.Electric_Piston_IV.get(5), ItemList.Electric_Piston_LuV.get(6), ItemList.Electric_Piston_ZPM.get(7),
        ItemList.Electric_Piston_UV.get(8), ItemList.Electric_Piston_UHV.get(9), ItemList.Electric_Piston_UEV.get(10),
        ItemList.Electric_Piston_UIV.get(11), ItemList.Electric_Piston_UMV.get(12),
        ItemList.Electric_Piston_UXV.get(13), ItemList.Electric_Piston_MAX.get(14)};
    ItemStack[] Robot_Arms = new ItemStack[]{ItemList.Robot_Arm_LV.get(1), ItemList.Robot_Arm_MV.get(2),
        ItemList.Robot_Arm_HV.get(3), ItemList.Robot_Arm_EV.get(4), ItemList.Robot_Arm_IV.get(5),
        ItemList.Robot_Arm_LuV.get(6), ItemList.Robot_Arm_ZPM.get(7), ItemList.Robot_Arm_UV.get(8),
        ItemList.Robot_Arm_UHV.get(9), ItemList.Robot_Arm_UEV.get(10), ItemList.Robot_Arm_UIV.get(11),
        ItemList.Robot_Arm_UMV.get(12), ItemList.Robot_Arm_UXV.get(13), ItemList.Robot_Arm_MAX.get(14)};
    ItemStack[] Emitters = new ItemStack[]{ItemList.Emitter_LV.get(1), ItemList.Emitter_MV.get(2),
        ItemList.Emitter_HV.get(3), ItemList.Emitter_EV.get(4), ItemList.Emitter_IV.get(5), ItemList.Emitter_LuV.get(6),
        ItemList.Emitter_ZPM.get(7), ItemList.Emitter_UV.get(8), ItemList.Emitter_UHV.get(9),
        ItemList.Emitter_UEV.get(10), ItemList.Emitter_UIV.get(11), ItemList.Emitter_UMV.get(12),
        ItemList.Emitter_UXV.get(13), ItemList.Emitter_MAX.get(14)};
    ItemStack[] Sensors = new ItemStack[]{ItemList.Sensor_LV.get(1), ItemList.Sensor_MV.get(2),
        ItemList.Sensor_HV.get(3), ItemList.Sensor_EV.get(4), ItemList.Sensor_IV.get(5), ItemList.Sensor_LuV.get(6),
        ItemList.Sensor_ZPM.get(7), ItemList.Sensor_UV.get(8), ItemList.Sensor_UHV.get(9), ItemList.Sensor_UEV.get(10),
        ItemList.Sensor_UIV.get(11), ItemList.Sensor_UMV.get(12), ItemList.Sensor_UXV.get(13),
        ItemList.Sensor_MAX.get(14)};
    ItemStack[] Field_Generators = new ItemStack[]{ItemList.Field_Generator_LV.get(4),
        ItemList.Field_Generator_MV.get(4), ItemList.Field_Generator_HV.get(4), ItemList.Field_Generator_EV.get(4),
        ItemList.Field_Generator_IV.get(4), ItemList.Field_Generator_LuV.get(4), ItemList.Field_Generator_ZPM.get(4),
        ItemList.Field_Generator_UV.get(4), ItemList.Field_Generator_UHV.get(4), ItemList.Field_Generator_UEV.get(4),
        ItemList.Field_Generator_UIV.get(4), ItemList.Field_Generator_UMV.get(4), ItemList.Field_Generator_UXV.get(4),
        ItemList.Field_Generator_MAX.get(4)};

    Werkstoff[] maxMaterials = new Werkstoff[]{MaterialPool.eventHorizonDiffusers,
        MaterialPool.entropyReductionProcess,
        MaterialPool.realSingularity,
    };

    ItemStack[] uevPlusCircuit = new ItemStack[]{
        ItemList.Circuit_ExoticProcessor.get(8),
        ItemList.Circuit_ExoticAssembly.get(4),
        ItemList.Circuit_ExoticComputer.get(2),
        ItemList.Circuit_ExoticMainframe.get(1),
        ItemList.Circuit_CosmicProcessor.get(8),
        ItemList.Circuit_CosmicAssembly.get(4),
        ItemList.Circuit_CosmicComputer.get(2),
        ItemList.Circuit_CosmicMainframe.get(1),
        ItemList.Circuit_TranscendentProcessor.get(8),
        ItemList.Circuit_TranscendentAssembly.get(4),
        ItemList.Circuit_TranscendentComputer.get(2),
        ItemList.Circuit_TranscendentMainframe.get(1)
    };

    ItemStack[] highDimensionalItem = new ItemStack[]{
        GTCMItemList.HighDimensionalCapacitor.get(64),
        GTCMItemList.HighDimensionalInterface.get(64),
        GTCMItemList.HighDimensionalDiode.get(64),
        GTCMItemList.HighDimensionalResistor.get(64),
        GTCMItemList.HighDimensionalTransistor.get(64)
    };
    // endregion

    @Override
    public void loadRecipes() {
        loadOriginalRecipeForConstruct();
        var processor = uevPlusCircuit[4].copy();
        processor.stackSize = 64;
        GT_Values.RA.stdBuilder()
            .itemInputs(
                //highDimensionalItem[0],
                //highDimensionalItem[1],
                //highDimensionalItem[2],
                highDimensionalItem[3],
                highDimensionalItem[4],
                GTCMItemList.HighDimensionalCircuitDoard.get(48),
                maxMaterials[0].get(OrePrefixes.bolt),
                maxMaterials[0].get(OrePrefixes.wireGt16),
                MyMaterial.shirabon.get(OrePrefixes.plate,64)
            )
            .fluidInputs(MaterialsUEVplus.Universium.getFluid(1440))
            .itemOutputs(processor)
            .noFluidOutputs()
            .noOptimize()
            .eut((long) (Integer.MAX_VALUE * Math.pow(16, 1)))
            .duration(2000)
            .addTo(stationRecipe);
        var assembly = uevPlusCircuit[5].copy();
        assembly.stackSize = 32;
        GT_Values.RA.stdBuilder()
            .itemInputs(
                highDimensionalItem[0],
                highDimensionalItem[1],
                processor,
                GTCMItemList.Antimatter.get(64),
                GTCMItemList.AnnihilationConstrainer.get(64),
                MaterialsUEVplus.Universium.getNanite(64)
            )
            .fluidInputs(MaterialPool.entropyReductionProcess.getBridgeMaterial().getFluid(1440))
            .itemOutputs(assembly)
            .noFluidOutputs()
            .noOptimize()
            .eut((long) (Integer.MAX_VALUE * Math.pow(16, 2)))
            .duration(20000)
            .addTo(stationRecipe);
        var computer=uevPlusCircuit[6].copy();
        computer.stackSize=16;
        GT_Values.RA.stdBuilder()
            .itemInputs(
                highDimensionalItem[2],
                highDimensionalItem[4],
                assembly,
                MaterialsUEVplus.DimensionallyTranscendentStellarCatalyst.getPlates(64),
                MaterialPool.entropyReductionProcess.getBridgeMaterial().getNanite(64),
                MaterialPool.realSingularity.getBridgeMaterial().getPlates(64)
            )
            .fluidInputs(MaterialPool.realSingularity.getBridgeMaterial().getFluid(1440))
            .itemOutputs(computer)
            .noFluidOutputs()
            .noOptimize()
            .eut((long) (Integer.MAX_VALUE * Math.pow(16, 2)))
            .duration(200000)
            .addTo(stationRecipe);
        var frame=uevPlusCircuit[7].copy();
        frame.stackSize=8;
        GT_Values.RA.stdBuilder()
            .itemInputs(
                highDimensionalItem[1],
                highDimensionalItem[3],
                computer,
                MaterialsUEVplus.DimensionallyTranscendentStellarCatalyst.getPlates(64),
                MaterialPool.entropyReductionProcess.getBridgeMaterial().getNanite(64),
                ItemList.ZPM3.get(64)
            )
            .fluidInputs(MaterialPool.realSingularity.getBridgeMaterial().getFluid(14400))
            .itemOutputs(frame)
            .noFluidOutputs()
            .noOptimize()
            .eut((long) (Integer.MAX_VALUE * Math.pow(16, 2)))
            .duration(200000)
            .addTo(stationRecipe);


    }
    // this method loads recipe for recipe that not use MUSS but related to MUSS, eg, structure blocks and controller
    // blocks recipes.
    public void loadOriginalRecipeForConstruct() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_OpticalMainframe.get(64),
                ItemList.Circuit_OpticalMainframe.get(64),
                CustomItemList.StargateFramePart.get(64),
                CustomItemList.StargateShieldingFoil.get(64),
                CustomItemList.StargateFramePart.get(64),
                CustomItemList.StargateChevron.get(64),
                Machine_Multi_EyeOfHarmony.get(64),
                TimeAccelerationFieldGeneratorTier8.get(64),
                TimeAccelerationFieldGeneratorTier8.get(64),
                TimeAccelerationFieldGeneratorTier8.get(64),
                TimeAccelerationFieldGeneratorTier8.get(64),
                StabilisationFieldGeneratorTier8.get(64),
                StabilisationFieldGeneratorTier8.get(64),
                StabilisationFieldGeneratorTier8.get(64),
                StabilisationFieldGeneratorTier8.get(64),
                MiracleTop.get(64))
            .fluidInputs(
                MaterialsUEVplus.SpaceTime.getFluid(1000000000),
                MaterialsUEVplus.Time.getFluid(1000000000),
                MaterialsUEVplus.RawStarMatter.getPlasma(1000000000),
                MaterialsUEVplus.DimensionallyTranscendentStellarCatalyst.getFluid(1000000000))
            .itemOutputs(GTCMItemList.megaUniversalSpaceStation.get(1))
            .noFluidOutputs()
            .noOptimize()
            .eut(1024 * RECIPE_MAX)
            .duration(20 * 1000)
            .addTo(GTCMRecipe.instance.MiracleTopRecipes);

        for (int i = 0; i < 10; i++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(blockCasings[i], blockCasing6s[i], processor[Math.max(i-3,0)])
                .fluidInputs(Materials.Plastic.getFluid(1440 * i))
                .itemOutputs(spaceStationStructureBlock[i])
                .noFluidOutputs()
                .noOptimize()
                .eut((int) (32 * Math.pow(4, i)))
                .duration(20 * 10 * i)
                .addTo(sAssemblerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    spaceStationStructureBlock[2 + i],
                    Field_Generators[2 + i],
                    Sensors[2 + i],
                    Robot_Arms[2 + i])
                .fluidInputs()
                .itemOutputs(SpaceStationAntiGravityBlock[i])
                .noFluidOutputs()
                .noOptimize()
                .eut((long) (32 * Math.pow(2, i)))
                .duration(20 * 10 * i)
                .addTo(sAssemblerRecipes);
        }
        for (int i = 10; i < 12; i++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    spaceStationStructureBlock[i-1],
                    processor[i-3],
                    Emitters[i],
                    Electric_Pistons[i],
                    Conveyor_Modules[i],
                    Materials.SuperconductorUMV.getPlates(64)

                )
                .fluidInputs(MaterialsUEVplus.TranscendentMetal.getFluid(1440 * i))
                .itemOutputs(spaceStationStructureBlock[i])
                .noFluidOutputs()
                .noOptimize()
                .eut((long) (Math.pow(16, i)))
                .duration(200 * 10 * i)
                .addTo(GTCMRecipe.instance.MiracleTopRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    spaceStationStructureBlock[i],
                    Field_Generators[i],
                    Sensors[i],
                    Robot_Arms[i],
                    MaterialPool.eventHorizonDiffusers.getBridgeMaterial().getPlates(64)
                    )
                .fluidInputs()
                .itemOutputs(SpaceStationAntiGravityBlock[i])
                .noFluidOutputs()
                .noOptimize()
                .eut((long) (32 * Math.pow(2, i)))
                .duration(20 * 10 * i)
                .addTo(sAssemblerRecipes);
        }


    }
}
