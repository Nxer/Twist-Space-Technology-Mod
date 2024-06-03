package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.VP;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;
import com.dreammaster.gthandler.GT_CoreModSupport;

import goodgenerator.items.MyMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class ModularHatchesRecipePool implements IRecipePool {

    private long getRecipeVoltageFromModuleTier(int t) {
        return VP[7 + t];
    }

    @Override
    public void loadRecipes() {
        if (!Config.EnableModularizedMachineSystem) return;

        Materials[] materials = new Materials[] { Materials.NaquadahAlloy, Materials.Neutronium,
            Materials.CosmicNeutronium, Materials.Infinity, MaterialsUEVplus.TranscendentMetal,
            MaterialsUEVplus.SpaceTime, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
            MaterialsUEVplus.MagMatter };

        ItemStack[] hulls = new ItemStack[] { ItemList.Hull_ZPM.get(1), ItemList.Hull_UV.get(1),
            ItemList.Hull_MAX.get(1), CustomItemList.Hull_UEV.get(1), CustomItemList.Hull_UIV.get(1),
            CustomItemList.Hull_UMV.get(1), CustomItemList.Hull_UXV.get(1), CustomItemList.Hull_MAXV.get(1) };

        ItemStack[] circuits = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 1),
            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Infinite, 1),
            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 1),
            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Optical, 1),
            // TODO piko circuit and quantum circuit
            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Piko, 1),
            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Quantum, 1),
            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Transcendent, 1), };

        ItemStack[] fieldGenerators = new ItemStack[] { ItemList.Field_Generator_ZPM.get(1),
            ItemList.Field_Generator_UV.get(1), ItemList.Field_Generator_UHV.get(1),
            ItemList.Field_Generator_UEV.get(1), ItemList.Field_Generator_UIV.get(1),
            ItemList.Field_Generator_UMV.get(1), ItemList.Field_Generator_UXV.get(1),
            ItemList.Field_Generator_MAX.get(1) };

        ItemStack[] robotArms = new ItemStack[] { ItemList.Robot_Arm_ZPM.get(1), ItemList.Robot_Arm_UV.get(1),
            ItemList.Robot_Arm_UHV.get(1), ItemList.Robot_Arm_UEV.get(1), ItemList.Robot_Arm_UIV.get(1),
            ItemList.Robot_Arm_UMV.get(1), ItemList.Robot_Arm_UXV.get(1), ItemList.Robot_Arm_MAX.get(1) };

        ItemStack[] conveyors = new ItemStack[] { ItemList.Conveyor_Module_ZPM.get(1),
            ItemList.Conveyor_Module_UV.get(1), ItemList.Conveyor_Module_UHV.get(1),
            ItemList.Conveyor_Module_UEV.get(1), ItemList.Conveyor_Module_UIV.get(1),
            ItemList.Conveyor_Module_UMV.get(1), ItemList.Conveyor_Module_UXV.get(1),
            ItemList.Conveyor_Module_MAX.get(1) };

        ItemStack[] emitters = new ItemStack[] { ItemList.Emitter_ZPM.get(1), ItemList.Emitter_UV.get(1),
            ItemList.Emitter_UHV.get(1), ItemList.Emitter_UEV.get(1), ItemList.Emitter_UIV.get(1),
            ItemList.Emitter_UMV.get(1), ItemList.Emitter_UXV.get(1), ItemList.Emitter_MAX.get(1) };

        ItemStack[] motors = new ItemStack[] { ItemList.Electric_Motor_ZPM.get(1), ItemList.Electric_Motor_UV.get(1),
            ItemList.Electric_Motor_UHV.get(1), ItemList.Electric_Motor_UEV.get(1), ItemList.Electric_Motor_UIV.get(1),
            ItemList.Electric_Motor_UMV.get(1), ItemList.Electric_Motor_UXV.get(1),
            ItemList.Electric_Motor_MAX.get(1) };

        ItemStack[] pistons = new ItemStack[] { ItemList.Electric_Piston_ZPM.get(1), ItemList.Electric_Piston_UV.get(1),
            ItemList.Electric_Piston_UHV.get(1), ItemList.Electric_Piston_UEV.get(1),
            ItemList.Electric_Piston_UIV.get(1), ItemList.Electric_Piston_UMV.get(1),
            ItemList.Electric_Piston_UXV.get(1), ItemList.Electric_Piston_MAX.get(1) };

        // region Execution Cores
        {
            // Normal Execution Core
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Cover_Screen.get(1))
                .metadata(RESEARCH_TIME, 24 * HOURS)
                .itemInputs(
                    CustomItemList.Hull_UIV.get(1),
                    ItemList.Electric_Motor_UIV.get(24),
                    ItemList.Conveyor_Module_UIV.get(16),
                    ItemList.Robot_Arm_UIV.get(32),

                    ItemList.Electric_Pump_UIV.get(12),
                    ItemList.Field_Generator_UIV.get(3),
                    ItemList.Sensor_UIV.get(3),
                    new Object[] { OrePrefixes.circuit.get(Materials.Optical), 16 },

                    CustomItemList.HighEnergyFlowCircuit.get(36),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Infinity, 18),
                    GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.TranscendentMetal, 16),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 48),

                    GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 32),
                    GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 64))
                .fluidInputs(
                    ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 64 * 16),
                    Materials.Infinity.getMolten(144 * 64 * 16),
                    Materials.Quantium.getMolten(144 * 64 * 64),
                    MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128))
                .itemOutputs(GTCMItemList.ExecutionCore.get(1))
                .eut(RECIPE_UIV)
                .duration(20 * 600)
                .addTo(AssemblyLine);

            // Advanced Execution Core
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GTCMItemList.ExecutionCore.get(1))
                .metadata(RESEARCH_TIME, 48 * HOURS)
                .itemInputs(
                    CustomItemList.Hull_UMV.get(1),
                    ItemList.Electric_Motor_UMV.get(64),
                    ItemList.Conveyor_Module_UMV.get(32),
                    ItemList.Robot_Arm_UMV.get(48),

                    ItemList.Electric_Pump_UMV.get(32),
                    ItemList.Field_Generator_UMV.get(48),
                    ItemList.Sensor_UMV.get(8),
                    // TODO Piko circuit will be deprecated.
                    new Object[] { OrePrefixes.circuit.get(Materials.Piko), 64 },

                    CustomItemList.HighEnergyFlowCircuit.get(64),
                    ItemList.EnergisedTesseract.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, 16),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, 64),

                    GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.SpaceTime, 64),
                    GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 64),
                    GT_OreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.SpaceTime, 64))
                .fluidInputs(
                    ELEMENT.STANDALONE.HYPOGEN.getFluidStack(144 * 64 * 64 * 4),
                    ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 64 * 64 * 4),
                    Materials.Infinity.getMolten(144 * 64 * 64 * 4 * 4),
                    MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128 * 4))
                .itemOutputs(GTCMItemList.AdvancedExecutionCore.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 1200)
                .addTo(AssemblyLine);

            // Perfect Execution Core
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GTCMItemList.AdvancedExecutionCore.get(1))
                .metadata(RESEARCH_TIME, 96 * HOURS)
                .itemInputs(
                    CustomItemList.Hull_UXV.get(1),
                    ItemList.Electric_Motor_UXV.get(64),
                    ItemList.Conveyor_Module_UXV.get(48),
                    ItemList.Robot_Arm_UXV.get(64),

                    ItemList.Electric_Pump_UXV.get(48),
                    ItemList.Field_Generator_UXV.get(64),
                    ItemList.Sensor_UXV.get(16),
                    // TODO Quantum circuit will be deprecated.
                    new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 64 },

                    MaterialsUEVplus.Eternity.getNanite(64),
                    MaterialsUEVplus.Eternity.getNanite(64),
                    MaterialsUEVplus.Eternity.getNanite(64),
                    MaterialsUEVplus.Eternity.getNanite(64),

                    MaterialsUEVplus.Universium.getNanite(64),
                    MaterialsUEVplus.Universium.getNanite(64),
                    MaterialsUEVplus.Universium.getNanite(64),
                    MaterialsUEVplus.Universium.getNanite(64))
                .fluidInputs(
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64 * 64 * 6),
                    MaterialsUEVplus.Universium.getMolten(144 * 64 * 64 * 12),
                    MaterialsUEVplus.Eternity.getMolten(144 * 64 * 64 * 24),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 64 * 64))
                .itemOutputs(GTCMItemList.PerfectExecutionCore.get(1))
                .eut(RECIPE_UXV)
                .duration(20 * 3600)
                .addTo(AssemblyLine);

        }
        // endregion

        // region Overclock Controllers
        {
            // low speed
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 2),
                    com.github.technus.tectech.thing.CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.HighEnergyFlowCircuit.get(64),

                    GregtechItemList.Casing_Coil_QuantumForceTransformer.get(32),
                    ItemList.UHV_Coil.get(64),
                    ItemList.Field_Generator_UIV.get(32),

                    ItemList.Emitter_UIV.get(64),
                    Materials.Gold.getNanite(16))
                .fluidInputs(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(144 * 64 * 6))
                .itemOutputs(GTCMItemList.LowSpeedPerfectOverclockController.get(1))
                .eut(RECIPE_UIV)
                .duration(20 * 336)
                .addTo(assemblerRecipes);

            // perfect overclock
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GTCMItemList.LowSpeedPerfectOverclockController.get(1))
                .metadata(RESEARCH_TIME, 48 * HOURS)
                .itemInputs(
                    GTCMItemList.LowSpeedPerfectOverclockController.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 16),
                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Emitter_UMV.get(64),

                    ItemRefer.Compact_Fusion_Coil_T4.get(64),
                    ItemRefer.Compact_Fusion_Coil_T4.get(64),
                    Materials.Gold.getNanite(64),
                    Materials.Gold.getNanite(64),

                    new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 64 },
                    MyMaterial.metastableOganesson.get(OrePrefixes.plateDense, 64),
                    MyMaterial.shirabon.get(OrePrefixes.plateDense, 64))
                .fluidInputs(
                    GT_CoreModSupport.RadoxPolymer.getMolten(144 * 64 * 64),
                    MaterialsUEVplus.Space.getMolten(144 * 320),
                    MaterialsUEVplus.Time.getMolten(144 * 320),
                    MyMaterial.shirabon.getMolten(144 * 640))
                .itemOutputs(GTCMItemList.PerfectOverclockController.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 600)
                .addTo(AssemblyLine);

            // singularity overclock
            TST_RecipeBuilder.builder()
                .itemInputs(
                    copyAmount(0, GTCMItemList.ProofOfHeroes.get(1)),
                    copyAmount(1024, GTCMItemList.PerfectOverclockController.get(1)),
                    copyAmount(2048, ItemList.Field_Generator_UXV.get(1)),
                    copyAmount(6144, GT_OreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.Eternity, 1)),

                    copyAmount(2048, MaterialsUEVplus.Universium.getNanite(1)),
                    copyAmount(2048, MaterialsUEVplus.Eternity.getNanite(1)),
                    copyAmount(2048, MaterialsUEVplus.BlackDwarfMatter.getNanite(1)),
                    copyAmount(2048, MaterialsUEVplus.WhiteDwarfMatter.getNanite(1)),

                    copyAmount(65536, ItemList.Timepiece.get(1)),
                    copyAmount(131072, GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Quantum, 1)))
                .fluidInputs(
                    Materials.DarkIron.getMolten(144 * 16384),
                    Materials.Force.getMolten(144 * 65536),
                    MaterialsUEVplus.Universium.getMolten(144 * 131072))
                .itemOutputs(GTCMItemList.SingularityPerfectOverclockController.get(1))
                .eut(RECIPE_MAX)
                .duration(20 * 3600 * 24 * 64)
                .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);
        }
        // endregion

        // region Parallel Controllers
        {

            ItemStack[] staticParallelControllers = new ItemStack[] { GTCMItemList.StaticParallelControllerT1.get(1),
                GTCMItemList.StaticParallelControllerT2.get(1), GTCMItemList.StaticParallelControllerT3.get(1),
                GTCMItemList.StaticParallelControllerT4.get(1), GTCMItemList.StaticParallelControllerT5.get(1),
                GTCMItemList.StaticParallelControllerT6.get(1), GTCMItemList.StaticParallelControllerT7.get(1),
                GTCMItemList.StaticParallelControllerT8.get(1) };

            ItemStack[] dynamicParallelControllers = new ItemStack[] { GTCMItemList.DynamicParallelControllerT1.get(1),
                GTCMItemList.DynamicParallelControllerT2.get(1), GTCMItemList.DynamicParallelControllerT3.get(1),
                GTCMItemList.DynamicParallelControllerT4.get(1), GTCMItemList.DynamicParallelControllerT5.get(1),
                GTCMItemList.DynamicParallelControllerT6.get(1), GTCMItemList.DynamicParallelControllerT7.get(1),
                GTCMItemList.DynamicParallelControllerT8.get(1) };

            for (int i = 0; i < 8; i++) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(18),
                        copyAmount(1, hulls[i]),
                        copyAmount(64, circuits[i]),

                        copyAmount(64, fieldGenerators[i]),
                        copyAmount(64, robotArms[i]),
                        copyAmount(64, conveyors[i]),

                        GT_OreDictUnificator.get(OrePrefixes.plate, materials[i], 64))
                    .fluidInputs(materials[i].getMolten(144 * 64))
                    .itemOutputs(staticParallelControllers[i])
                    .eut(getRecipeVoltageFromModuleTier(i))
                    .duration(20 * 60 * (i + 1))
                    .addTo(assemblerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(18),
                        staticParallelControllers[i],
                        ItemList.Cover_Screen.get(1),
                        ItemList.Tool_DataOrb.get(1),
                        copyAmount(2, circuits[i]))
                    .fluidInputs()
                    .itemOutputs(dynamicParallelControllers[i])
                    .eut(getRecipeVoltageFromModuleTier(i))
                    .duration(20 * (i + 1))
                    .addTo(assemblerRecipes);
            }
        }
        // endregion

        // region Speed Controllers
        {
            ItemStack[] staticSpeedControllers = new ItemStack[] { GTCMItemList.StaticSpeedControllerT1.get(1),
                GTCMItemList.StaticSpeedControllerT2.get(1), GTCMItemList.StaticSpeedControllerT3.get(1),
                GTCMItemList.StaticSpeedControllerT4.get(1), GTCMItemList.StaticSpeedControllerT5.get(1),
                GTCMItemList.StaticSpeedControllerT6.get(1), GTCMItemList.StaticSpeedControllerT7.get(1),
                GTCMItemList.StaticSpeedControllerT8.get(1) };

            ItemStack[] dynamicSpeedControllers = new ItemStack[] { GTCMItemList.DynamicSpeedControllerT1.get(1),
                GTCMItemList.DynamicSpeedControllerT2.get(1), GTCMItemList.DynamicSpeedControllerT3.get(1),
                GTCMItemList.DynamicSpeedControllerT4.get(1), GTCMItemList.DynamicSpeedControllerT5.get(1),
                GTCMItemList.DynamicSpeedControllerT6.get(1), GTCMItemList.DynamicSpeedControllerT7.get(1),
                GTCMItemList.DynamicSpeedControllerT8.get(1) };

            for (int i = 0; i < 8; i++) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(18),
                        copyAmount(1, hulls[i]),
                        copyAmount(64, circuits[i]),

                        copyAmount(64, fieldGenerators[i]),
                        copyAmount(64, motors[i]),
                        copyAmount(64, motors[i]),

                        copyAmount(64, pistons[i]),
                        copyAmount(64, pistons[i]),
                        GT_OreDictUnificator.get(OrePrefixes.plate, materials[i], 64))
                    .fluidInputs(materials[i].getMolten(144 * 64))
                    .itemOutputs(staticSpeedControllers[i])
                    .eut(getRecipeVoltageFromModuleTier(i))
                    .duration(20 * 60 * (i + 1))
                    .addTo(assemblerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(18),
                        staticSpeedControllers[i],
                        ItemList.Cover_Screen.get(1),
                        ItemList.Tool_DataOrb.get(1),
                        copyAmount(2, circuits[i]))
                    .fluidInputs()
                    .itemOutputs(dynamicSpeedControllers[i])
                    .eut(getRecipeVoltageFromModuleTier(i))
                    .duration(20 * (i + 1))
                    .addTo(assemblerRecipes);
            }

        }
        // endregion

        // region Power Consumption Controllers
        {
            ItemStack[] staticPowerConsumptionControllers = new ItemStack[] {
                GTCMItemList.StaticPowerConsumptionControllerT1.get(1),
                GTCMItemList.StaticPowerConsumptionControllerT2.get(1),
                GTCMItemList.StaticPowerConsumptionControllerT3.get(1),
                GTCMItemList.StaticPowerConsumptionControllerT4.get(1),
                GTCMItemList.StaticPowerConsumptionControllerT5.get(1),
                GTCMItemList.StaticPowerConsumptionControllerT6.get(1),
                GTCMItemList.StaticPowerConsumptionControllerT7.get(1),
                GTCMItemList.StaticPowerConsumptionControllerT8.get(1) };

            for (int i = 0; i < 8; i++) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(18),
                        copyAmount(1, hulls[i]),
                        copyAmount(64, circuits[i]),

                        copyAmount(64, fieldGenerators[i]),
                        copyAmount(64, fieldGenerators[i]),
                        copyAmount(64, emitters[i]),

                        GT_OreDictUnificator.get(OrePrefixes.plate, materials[i], 64))
                    .fluidInputs(materials[i].getMolten(144 * 64))
                    .itemOutputs(staticPowerConsumptionControllers[i])
                    .eut(getRecipeVoltageFromModuleTier(i))
                    .duration(20 * 60 * (i + 1))
                    .addTo(assemblerRecipes);

            }
        }
        // endregion

    }

}
