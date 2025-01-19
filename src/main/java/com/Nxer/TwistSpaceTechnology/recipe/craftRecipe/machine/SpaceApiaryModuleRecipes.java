package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GTOreDictUnificator;

public class SpaceApiaryModuleRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        if (Config.EnableSpaceApiaryModule) {
            final IRecipeMap SpaceAssembler = IGRecipeMaps.spaceAssemblerRecipes;

            // t1
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UHV.get(16),
                    ItemList.Conveyor_Module_UHV.get(16),
                    ItemList.Robot_Arm_UHV.get(16),
                    ItemList.Electric_Pump_UHV.get(16),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64))
                .fluidInputs(Materials.UUMatter.getFluid(1000 * 128), Materials.Honey.getFluid(1000 * 256))
                .itemOutputs(GTCMItemList.SpaceApiaryT1.get(1))
                .specialValue(1)
                .eut(RECIPE_UHV)
                .duration(20 * 300)
                .addTo(SpaceAssembler);

            // t2
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UEV.get(16),
                    ItemList.Conveyor_Module_UEV.get(16),
                    ItemList.Robot_Arm_UEV.get(16),
                    ItemList.Electric_Pump_UEV.get(16),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64))
                .fluidInputs(Materials.UUMatter.getFluid(1000 * 256), Materials.Honey.getFluid(1000 * 512))
                .itemOutputs(GTCMItemList.SpaceApiaryT2.get(1))
                .specialValue(1)
                .eut(RECIPE_UEV)
                .duration(20 * 600)
                .addTo(SpaceAssembler);

            // t3
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UIV.get(16),
                    ItemList.Conveyor_Module_UIV.get(16),
                    ItemList.Robot_Arm_UIV.get(16),
                    ItemList.Electric_Pump_UIV.get(16),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 64))
                .fluidInputs(Materials.UUMatter.getFluid(1000 * 512), Materials.Honey.getFluid(1000 * 1024))
                .itemOutputs(GTCMItemList.SpaceApiaryT3.get(1))
                .specialValue(2)
                .eut(RECIPE_UIV)
                .duration(20 * 1200)
                .addTo(SpaceAssembler);

            // t4
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UMV.get(16),
                    ItemList.Conveyor_Module_UMV.get(16),
                    ItemList.Robot_Arm_UMV.get(16),
                    ItemList.Electric_Pump_UMV.get(16),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64))
                .fluidInputs(Materials.UUMatter.getFluid(1000 * 1024), Materials.Honey.getFluid(1000 * 2048))
                .itemOutputs(GTCMItemList.SpaceApiaryT4.get(1))
                .specialValue(3)
                .eut(RECIPE_UMV)
                .duration(20 * 2400)
                .addTo(SpaceAssembler);

        }
    }
}
