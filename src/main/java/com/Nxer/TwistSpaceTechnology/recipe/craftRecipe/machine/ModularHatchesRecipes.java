package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.VP;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.enums.TierEU;

import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class ModularHatchesRecipes implements IRecipePool {

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
            ItemList.Hull_MAX.get(1), ItemList.Hull_UEV.get(1), ItemList.Hull_UIV.get(1), ItemList.Hull_UMV.get(1),
            ItemList.Hull_UXV.get(1), ItemList.Hull_MAXV.get(1) };

        ItemStack[] circuits = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MAX, 1), };

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

        ItemStack[] nanites = new ItemStack[] { Materials.Carbon.getNanite(1), Materials.Neutronium.getNanite(1),
            Materials.Silver.getNanite(1), Materials.Gold.getNanite(1), MaterialsUEVplus.SixPhasedCopper.getNanite(1),
            MaterialsTST.Axonium.getNanite(1), MaterialsUEVplus.Eternity.getNanite(1),
            MaterialsUEVplus.MagMatter.getNanite(1) };

        ItemStack[] speedUps = new ItemStack[] { GTCMItemList.PhotonControllerUpgradeZPM.get(1),
            GTCMItemList.PhotonControllerUpgradeUV.get(1), GTCMItemList.PhotonControllerUpgradeUHV.get(1),
            GTCMItemList.PhotonControllerUpgradeUEV.get(1), GTCMItemList.PhotonControllerUpgradeUIV.get(1),
            GTCMItemList.PhotonControllerUpgradeUMV.get(1), GTCMItemList.PhotonControllerUpgradeUXV.get(1),
            GTCMItemList.PhotonControllerUpgradeMAX.get(1), };

        ItemStack[] batteries = new ItemStack[] { ItemList.Energy_LapotronicOrb2.get(1), ItemList.Energy_Module.get(1),
            ItemList.Energy_Cluster.get(1), ItemList.ZPM2.get(1), ItemList.ZPM3.get(1), ItemList.ZPM4.get(1),
            ItemList.ZPM5.get(1), ItemList.ZPM6.get(1), };

        // spotless:off
        // region Execution Cores
        {
            // Normal Execution Core
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Cover_Screen.get(1),
                1_024_000,
                512,
                (int) TierEU.RECIPE_UV,
                16,
                new Object[] {
                    ItemList.Hull_UIV.get(1),
                    ItemList.Electric_Motor_UV.get(16),
                    ItemList.Conveyor_Module_UV.get(12),
                    ItemList.Robot_Arm_UV.get(18),

                    ItemList.Electric_Pump_UV.get(12),
                    ItemList.Field_Generator_UV.get(1),
                    ItemList.Sensor_UV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 2),

                    HighEnergyFlowCircuit.get(36),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 32),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Neutronium, 6),
                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 16),

                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 32),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16) },
                new FluidStack[] {
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                    Materials.Quantium.getMolten(144 * 64 * 16),
                    Materials.CosmicNeutronium.getMolten(144 * 64),
                    Materials.UUMatter.getFluid(1000 * 1024) },
                GTCMItemList.ExecutionCore.get(1),
                20 * 300,
                (int) RECIPE_UV);

            // Advanced Execution Core
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTCMItemList.ExecutionCore.get(1),
                65_536_000,
                16384,
                (int) RECIPE_UIV,
                16,
                new Object[] { ItemList.Hull_UMV.get(1),
                    ItemList.Electric_Motor_UMV.get(64),
                    ItemList.Conveyor_Module_UMV.get(32),
                    ItemList.Robot_Arm_UMV.get(48),

                    ItemList.Electric_Pump_UMV.get(32),
                    ItemList.Field_Generator_UMV.get(8),
                    ItemList.Sensor_UMV.get(16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 64),

                    GTCMItemList.UltimateEnergyFlowCircuit.get(28),
                    ItemList.EnergisedTesseract.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsTST.Axonium, 24),
                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsTST.Axonium, 64),

                    GTOreDictUnificator.get(OrePrefixes.stickLong, MaterialsTST.Axonium, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsTST.Axonium, 64),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsTST.Axonium, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.SpaceTime, 64) },
                new FluidStack[] {
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128 * 16),
                    Materials.Infinity.getPlasma(144 * 64 * 64 * 4 * 4),
                    MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 64 * 64 * 4),
                    MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000 * 10) },
                GTCMItemList.AdvancedExecutionCore.get(1),
                20 * 1200,
                (int) RECIPE_UMV);

            // Perfect Execution Core
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTCMItemList.AdvancedExecutionCore.get(1),
                524_288_000,
                262144,
                (int) RECIPE_UXV,
                64,
                new Object[] { ItemList.Hull_UXV.get(1),
                    ItemList.Electric_Motor_UXV.get(64),
                    ItemList.Conveyor_Module_UXV.get(48),
                    ItemList.Robot_Arm_UXV.get(64),

                    ItemList.Electric_Pump_UXV.get(48),
                    ItemList.Field_Generator_UXV.get(16),
                    ItemList.Sensor_UXV.get(32),
                    MaterialsUEVplus.MagMatter.getNanite(64),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                    GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gear, MaterialsTST.NeutroniumAlloy, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64),

                    MaterialsUEVplus.Eternity.getNanite(64),
                    MaterialsUEVplus.Eternity.getNanite(64),
                    MaterialsUEVplus.Eternity.getNanite(64),
                    MaterialsUEVplus.Eternity.getNanite(64) },
                new FluidStack[] {
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 64 * 512),
                    MaterialsTST.Axonium.getPlasma(144 * 64 * 64 * 64),
                    MaterialsUEVplus.Universium.getMolten(144 * 64 * 64 * 12),
                    MaterialsUEVplus.Eternity.getMolten(144 * 64 * 64 * 24) },
                GTCMItemList.PerfectExecutionCore.get(1),
                20 * 3600,
                (int) RECIPE_UXV);

        }
        // endregion

        // region Overclock Controllers
        {
            // low speed
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(18),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 2),
                    tectech.thing.CustomItemList.Machine_Multi_Transformer.get(1),

                    HighEnergyFlowCircuit.get(64),
                    GregtechItemList.Casing_Coil_QuantumForceTransformer.get(32),
                    ItemList.UHV_Coil.get(64),

                    ItemList.Field_Generator_UEV.get(16),
                    ItemList.Emitter_UEV.get(32),
                    Materials.Gold.getNanite(16))
                .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(144 * 4))
                .itemOutputs(GTCMItemList.LowSpeedPerfectOverclockController.get(1))
                .eut(RECIPE_UIV)
                .duration(20 * 336)
                .addTo(assemblerRecipes);

            // perfect overclock
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTCMItemList.LowSpeedPerfectOverclockController.get(1),
                524_288_000,
                262144,
                (int) TierEU.RECIPE_UXV,
                64,
                new Object[] { GTCMItemList.LowSpeedPerfectOverclockController.get(64),
                    GTCMItemList.LowSpeedPerfectOverclockController.get(64),
                    GTCMItemList.LowSpeedPerfectOverclockController.get(64),
                    GTCMItemList.LowSpeedPerfectOverclockController.get(64),

                    GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 16),
                    CustomItemList.eM_Dimensional.get(16),
                    ItemRefer.Compact_Fusion_Coil_T4.get(64),
                    MaterialsUEVplus.SixPhasedCopper.getNanite(64),

                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Field_Generator_UMV.get(64),

                    ItemList.Emitter_UMV.get(64),
                    ItemList.Emitter_UMV.get(64),
                    ItemList.Emitter_UMV.get(64),
                    ItemList.Emitter_UMV.get(64),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                    GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUMVBase, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.WhiteDwarfMatter, 16) },
                new FluidStack[] {
                    Materials.RadoxPolymer.getMolten(144 * 64 * 128),
                    MaterialsUEVplus.Time.getMolten(144 * 320), MaterialsUEVplus.PhononMedium.getFluid(1000 * 256),
                    GGMaterial.shirabon.getMolten(144 * 640) },
                GTCMItemList.PerfectOverclockController.get(1),
                20 * 600,
                (int) RECIPE_UMV);

            // singularity overclock
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTCMItemList.PerfectOverclockController.get(1),
                2_097_152_000,
                1048576,
                (int) RECIPE_UXV,
                64,
                new Object[] {
                    GTCMItemList.PerfectOverclockController.get(64),
                    GTCMItemList.PerfectOverclockController.get(64),
                    GTCMItemList.PerfectOverclockController.get(64),
                    GTCMItemList.PerfectOverclockController.get(64),

                    GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64),
                    GTCMItemList.AdvancedHighPowerCoilBlock.get(64),
                    GTCMItemList.StabilisationFieldGeneratorUXV.get(15),
                    GTCMItemList.HarmoniousWirelessEnergyHatch.get(1),

                    ItemList.Field_Generator_UXV.get(64),
                    MaterialsUEVplus.MagMatter.getNanite(64),
                    GTCMItemList.ProofOfHeroes.get(1),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64),

                    GTCMItemList.PerfectOverclockController.get(64),
                    GTCMItemList.PerfectOverclockController.get(64),
                    GTCMItemList.PerfectOverclockController.get(64),
                    GTCMItemList.PerfectOverclockController.get(64) },
                new FluidStack[] {
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 65536),
                    MaterialsTST.NeutroniumAlloy.getMolten(144 * 14913080),
                    Materials.Force.getMolten(144 * 14913080),
                    MaterialsUEVplus.Universium.getMolten(144 * 131072) },
                GTCMItemList.SingularityPerfectOverclockController.get(1),
                20 * 3600 * 24 * 64,
                (int) RECIPE_MAX);

        }
        // endregion
        // spotless:on

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
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(18),
                        copyAmount(1, hulls[i]),
                        copyAmount(64, circuits[i]),

                        copyAmount(64, fieldGenerators[i]),
                        copyAmount(64, robotArms[i]),
                        copyAmount(64, conveyors[i]),

                        copyAmount(64, nanites[i]),
                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, materials[i], 2))
                    .fluidInputs(materials[i].getMolten(144 * 64))
                    .itemOutputs(staticParallelControllers[i])
                    .eut(getRecipeVoltageFromModuleTier(i))
                    .duration(20 * 60 * (i + 1))
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(18),
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
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(18),
                        copyAmount(1, hulls[i]),
                        copyAmount(1, speedUps[i]),

                        copyAmount(64, circuits[i]),
                        copyAmount(64, fieldGenerators[i]),
                        copyAmount(64, fieldGenerators[i]),

                        copyAmount(64, motors[i]),
                        copyAmount(64, pistons[i]),
                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, materials[i], 4))
                    .fluidInputs(materials[i].getMolten(144 * 64))
                    .itemOutputs(staticSpeedControllers[i])
                    .eut(getRecipeVoltageFromModuleTier(i))
                    .duration(20 * 60 * (i + 1))
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(18),
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
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(18),
                        copyAmount(1, hulls[i]),
                        copyAmount(64, circuits[i]),

                        copyAmount(64, fieldGenerators[i]),
                        copyAmount(64, fieldGenerators[i]),
                        copyAmount(64, emitters[i]),

                        copyAmount(8 - i, batteries[i]),
                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, materials[i], 1))
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
