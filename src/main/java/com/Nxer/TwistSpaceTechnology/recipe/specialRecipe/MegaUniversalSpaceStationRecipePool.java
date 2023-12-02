package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MiracleTop;
import static com.github.technus.tectech.thing.CustomItemList.*;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;

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
    ItemStack[] spaceStationStructureBlock = new ItemStack[] { GTCMItemList.spaceStationStructureBlockLV.get(1),
        GTCMItemList.spaceStationStructureBlockMV.get(1), GTCMItemList.spaceStationStructureBlockHV.get(1),
        GTCMItemList.spaceStationStructureBlockEV.get(1), GTCMItemList.spaceStationStructureBlockIV.get(1),
        GTCMItemList.spaceStationStructureBlockLuV.get(1), GTCMItemList.spaceStationStructureBlockZPM.get(1),
        GTCMItemList.spaceStationStructureBlockUV.get(1), GTCMItemList.spaceStationStructureBlockUHV.get(1),
        GTCMItemList.spaceStationStructureBlockUEV.get(1), GTCMItemList.spaceStationStructureBlockUIV.get(1),
        GTCMItemList.spaceStationStructureBlockUMV.get(1), GTCMItemList.spaceStationStructureBlockUXV.get(1),
        GTCMItemList.spaceStationStructureBlockMAX.get(1) };
    ItemStack[] SpaceStationAntiGravityBlock = new ItemStack[] { GTCMItemList.SpaceStationAntiGravityBlockLV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockMV.get(1), GTCMItemList.SpaceStationAntiGravityBlockHV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockEV.get(1), GTCMItemList.SpaceStationAntiGravityBlockIV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockLuV.get(1), GTCMItemList.SpaceStationAntiGravityBlockZPM.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockUV.get(1), GTCMItemList.SpaceStationAntiGravityBlockUHV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockUEV.get(1), GTCMItemList.SpaceStationAntiGravityBlockUIV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockUMV.get(1), GTCMItemList.SpaceStationAntiGravityBlockUXV.get(1),
        GTCMItemList.SpaceStationAntiGravityBlockMAX.get(1) };
    ItemStack[] blockCasings = new ItemStack[] { ItemList.Casing_ULV.get(4), ItemList.Casing_LV.get(4),
        ItemList.Casing_MV.get(4), ItemList.Casing_HV.get(4), ItemList.Casing_EV.get(4), ItemList.Casing_IV.get(4),
        ItemList.Casing_LuV.get(16), ItemList.Casing_ZPM.get(16), ItemList.Casing_UV.get(16),
        ItemList.Casing_MAX.get(16),// ATTENTION! THIS IS UHV CASING!
    };
    ItemStack[] blockCasing6s = new ItemStack[] { ItemList.Casing_Tank_0.get(1), ItemList.Casing_Tank_1.get(1),
        ItemList.Casing_Tank_2.get(1), ItemList.Casing_Tank_3.get(1), ItemList.Casing_Tank_4.get(1),
        ItemList.Casing_Tank_5.get(1), ItemList.Casing_Tank_6.get(1), ItemList.Casing_Tank_7.get(1),
        ItemList.Casing_Tank_8.get(1), ItemList.Casing_Tank_9.get(1) };

    ItemStack[] processor = new ItemStack[] { ItemList.Circuit_Processor.get(1), ItemList.Circuit_Nanoprocessor.get(1),
        ItemList.Circuit_Quantumprocessor.get(1), ItemList.Circuit_Crystalprocessor.get(1),
        ItemList.Circuit_Neuroprocessor.get(1), ItemList.Circuit_Bioprocessor.get(1),
        ItemList.Circuit_OpticalProcessor.get(1), ItemList.Circuit_ExoticProcessor.get(1),
        ItemList.Circuit_CosmicProcessor.get(1), ItemList.Circuit_TranscendentProcessor.get(1), };
    ItemStack[] Conveyor_Modules = new ItemStack[] { ItemList.Conveyor_Module_LV.get(1),
        ItemList.Conveyor_Module_MV.get(2), ItemList.Conveyor_Module_HV.get(3), ItemList.Conveyor_Module_EV.get(4),
        ItemList.Conveyor_Module_IV.get(5), ItemList.Conveyor_Module_LuV.get(6), ItemList.Conveyor_Module_ZPM.get(7),
        ItemList.Conveyor_Module_UV.get(8), ItemList.Conveyor_Module_UHV.get(9), ItemList.Conveyor_Module_UEV.get(10),
        ItemList.Conveyor_Module_UIV.get(11), ItemList.Conveyor_Module_UMV.get(12),
        ItemList.Conveyor_Module_UXV.get(13), ItemList.Conveyor_Module_MAX.get(14) };
    ItemStack[] Electric_Pistons = new ItemStack[] { ItemList.Electric_Piston_LV.get(1),
        ItemList.Electric_Piston_MV.get(2), ItemList.Electric_Piston_HV.get(3), ItemList.Electric_Piston_EV.get(4),
        ItemList.Electric_Piston_IV.get(5), ItemList.Electric_Piston_LuV.get(6), ItemList.Electric_Piston_ZPM.get(7),
        ItemList.Electric_Piston_UV.get(8), ItemList.Electric_Piston_UHV.get(9), ItemList.Electric_Piston_UEV.get(10),
        ItemList.Electric_Piston_UIV.get(11), ItemList.Electric_Piston_UMV.get(12),
        ItemList.Electric_Piston_UXV.get(13), ItemList.Electric_Piston_MAX.get(14) };
    ItemStack[] Robot_Arms = new ItemStack[] { ItemList.Robot_Arm_LV.get(1), ItemList.Robot_Arm_MV.get(2),
        ItemList.Robot_Arm_HV.get(3), ItemList.Robot_Arm_EV.get(4), ItemList.Robot_Arm_IV.get(5),
        ItemList.Robot_Arm_LuV.get(6), ItemList.Robot_Arm_ZPM.get(7), ItemList.Robot_Arm_UV.get(8),
        ItemList.Robot_Arm_UHV.get(9), ItemList.Robot_Arm_UEV.get(10), ItemList.Robot_Arm_UIV.get(11),
        ItemList.Robot_Arm_UMV.get(12), ItemList.Robot_Arm_UXV.get(13), ItemList.Robot_Arm_MAX.get(14) };
    ItemStack[] Emitters = new ItemStack[] { ItemList.Emitter_LV.get(1), ItemList.Emitter_MV.get(2),
        ItemList.Emitter_HV.get(3), ItemList.Emitter_EV.get(4), ItemList.Emitter_IV.get(5), ItemList.Emitter_LuV.get(6),
        ItemList.Emitter_ZPM.get(7), ItemList.Emitter_UV.get(8), ItemList.Emitter_UHV.get(9),
        ItemList.Emitter_UEV.get(10), ItemList.Emitter_UIV.get(11), ItemList.Emitter_UMV.get(12),
        ItemList.Emitter_UXV.get(13), ItemList.Emitter_MAX.get(14) };
    ItemStack[] Sensors = new ItemStack[] { ItemList.Sensor_LV.get(1), ItemList.Sensor_MV.get(2),
        ItemList.Sensor_HV.get(3), ItemList.Sensor_EV.get(4), ItemList.Sensor_IV.get(5), ItemList.Sensor_LuV.get(6),
        ItemList.Sensor_ZPM.get(7), ItemList.Sensor_UV.get(8), ItemList.Sensor_UHV.get(9), ItemList.Sensor_UEV.get(10),
        ItemList.Sensor_UIV.get(11), ItemList.Sensor_UMV.get(12), ItemList.Sensor_UXV.get(13),
        ItemList.Sensor_MAX.get(14) };
    ItemStack[] Field_Generators = new ItemStack[] { ItemList.Field_Generator_LV.get(4),
        ItemList.Field_Generator_MV.get(4), ItemList.Field_Generator_HV.get(4), ItemList.Field_Generator_EV.get(4),
        ItemList.Field_Generator_IV.get(4), ItemList.Field_Generator_LuV.get(4), ItemList.Field_Generator_ZPM.get(4),
        ItemList.Field_Generator_UV.get(4), ItemList.Field_Generator_UHV.get(4), ItemList.Field_Generator_UEV.get(4),
        ItemList.Field_Generator_UIV.get(4), ItemList.Field_Generator_UMV.get(4), ItemList.Field_Generator_UXV.get(4),
        ItemList.Field_Generator_MAX.get(4) };

    ItemStack[] maxIngot = new ItemStack[] { MaterialPool.eventHorizonDiffusers.get(OrePrefixes.ingot),
        MaterialPool.entropyReductionProcess.get(OrePrefixes.ingot),
        MaterialPool.realSingularity.get(OrePrefixes.ingot) };
    // endregion

    @Override
    public void loadRecipes() {
        loadOriginalRecipeForConstruct();
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
                .itemInputs(blockCasings[i], blockCasing6s[i], processor[i])
                .fluidInputs(Materials.Plastic.getFluid(1440))
                .itemOutputs(spaceStationStructureBlock[i])
                .noFluidOutputs()
                .noOptimize()
                .specialValue(3)
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
                .specialValue(3)
                .eut((int) (32 * Math.pow(2, i)))
                .duration(20 * 10 * i)
                .addTo(sAssemblerRecipes);
        }

    }
}
