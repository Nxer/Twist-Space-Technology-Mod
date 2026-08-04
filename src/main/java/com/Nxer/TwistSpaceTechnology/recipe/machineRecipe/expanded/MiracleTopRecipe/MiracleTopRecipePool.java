package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.clearMTRecipeCache;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.flushMTRecipeCache;
import static com.Nxer.TwistSpaceTechnology.util.TstUtils.setStackSize;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.util.GTUtility.copyAmount;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class MiracleTopRecipePool {

    private static final RecipeMap<?> MT = GTCMRecipe.MiracleTopRecipes;

    public static void loadRecipes() {
        TwistSpaceTechnology.LOG.info("MiracleTopRecipePool loading recipes.");
        MiracleTopRecipeInitialization.init();
        clearMTRecipeCache();
        NACRecipeGenerator.load();
        // TODO directly run NAC recipes is too OP, additional limitation will be imposed later
        CircuitAssemblerRecipeGenerator.load();
        CircuitAssemblyLineRecipeGenerator.load();
        AssemblyLineRecipeGenerator.load();
        SpaceAssemblerRecipeGenerator.load();
        flushMTRecipeCache();
        loadCustomRecipes();
    }

    public static void loadCustomRecipes() {
        // Do Not Add Messy Recipe to MT

        final ItemStack ringBlock = getModItem("SGCraft", "stargateRing", 1, 0);
        final ItemStack chevronBlock = getModItem("SGCraft", "stargateRing", 1, 1);
        final ItemStack irisUpgrade = getModItem("SGCraft", "sgIrisUpgrade", 1, 0);

        // region Proof Of Heroes
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.SpaceWarper.get(64),
                getModItem("eternalsingularity", "eternal_singularity", 64),
                getModItem("eternalsingularity", "combined_singularity", 64, 15),
                ItemList.Timepiece.get(64),
                ItemList.GigaChad.get(64),
                tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 64 },
                // getModItem("dreamcraft", "item.QuantumCircuit", 64),
                // getModItem(GTPlusPlus.ID, "particleBase", 64, 15),
                // getModItem(GTPlusPlus.ID, "particleBase", 64, 16),
                // getModItem(GTPlusPlus.ID, "particleBase", 64, 20),
                // getModItem(GTPlusPlus.ID, "particleBase", 64, 21),
                // getModItem(GTPlusPlus.ID, "particleBase", 64, 17),
                ItemList.ZPM6.get(64),
                GTCMItemList.IndistinctTentacle.get(64))
            .fluidInputs(
                Materials.Time.getMolten(1000 * 114514),
                Materials.Space.getMolten(1000 * 114514),
                Materials.MHDCSM.getMolten(1000 * 114514), // MagnetohydrodynamicallyConstrainedStarMatter
                GGMaterial.shirabon.getMolten(1000 * 114514),
                Materials.Universium.getMolten(1000 * 114514),
                Materials.Eternity.getMolten(1000 * 114514),
                Materials.PrimordialMatter.getFluid(1000 * 114514))
            .itemOutputs(GTCMItemList.ProofOfHeroes.get(1))
            .specialValue(13500)
            .eut(RECIPE_MAX)
            .duration(20 * 1919810)
            .addTo(MT);

        // endregion

        // Optical SoC Shield
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                GregtechItemList.InfinityInfusedShieldingCore.get(0),
                ItemList.Optical_Cpu_Containment_Housing.get(1),
                Materials.Glowstone.getNanite(4))
            .fluidInputs(Materials.Space.getMolten(36), Materials.Time.getMolten(36))
            .itemOutputs(GTCMItemList.ParticleTrapTimeSpaceShield.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 64)
            .addTo(MT);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                GregtechItemList.SpaceTimeBendingCore.get(0),
                ItemList.Optical_Cpu_Containment_Housing.get(2),
                Materials.Glowstone.getNanite(4))
            .fluidInputs(
                Materials.Space.getMolten(144),
                Materials.Time.getMolten(144),
                Materials.SpaceTime.getMolten(288))
            .itemOutputs(GTCMItemList.ParticleTrapTimeSpaceShield.get(16))
            .fluidOutputs(Materials.DTR.getFluid(2500))
            .eut(RECIPE_UMV)
            .duration(20 * 64)
            .addTo(MT);

        // region Endgame Challenge content

        // Liquid Stargate
        GTValues.RA.stdBuilder()
            .itemInputs(
                copyAmount(1, ringBlock),
                copyAmount(1, chevronBlock),
                copyAmount(1, chevronBlock),
                copyAmount(1, ringBlock),

                copyAmount(1, chevronBlock),
                copyAmount(1, irisUpgrade),
                copyAmount(1, irisUpgrade),
                copyAmount(1, chevronBlock),

                copyAmount(1, ringBlock),
                copyAmount(1, irisUpgrade),
                copyAmount(1, irisUpgrade),
                copyAmount(1, ringBlock),

                copyAmount(1, chevronBlock),
                copyAmount(1, ringBlock),
                copyAmount(1, ringBlock),
                copyAmount(1, chevronBlock))
            .fluidInputs(MaterialPool.StabiliseVoidMatter.getFluidOrGas(1_000))
            .fluidOutputs(MaterialPool.LiquidStargate.getFluidOrGas(1000))
            .specialValue(13500)
            .eut(RECIPE_MAX)
            .duration(20 * 99_999_999)
            .addTo(MT);

        // StabiliseVoidMatter
        TST_RecipeBuilder.builder()
            .itemInputs(
                setStackSize(Materials.CosmicNeutronium.getDust(1), 10_000_000),
                setStackSize(Materials.Bedrockium.getDust(1), 10_000_000),
                setStackSize(Materials.Carbon.getDust(1), 10_000_000),
                setStackSize(Materials.Oilsands.getDust(1), 10_000_000),
                setStackSize(Materials.NiobiumTitanium.getDust(1), 10_000_000),
                setStackSize(MaterialsElements.STANDALONE.BLACK_METAL.getDust(1), 10_000_000),
                setStackSize(Materials.Naquadria.getDust(1), 10_000_000),
                setStackSize(Materials.Obsidian.getDust(1), 10_000_000),
                setStackSize(Materials.Coal.getDust(1), 10_000_000),
                setStackSize(Materials.NaquadahAlloy.getDust(1), 10_000_000),
                setStackSize(Materials.Tungsten.getDust(1), 10_000_000),
                setStackSize(Materials.TranscendentMetal.getDust(1), 10_000_000),
                setStackSize(Materials.Perlite.getDust(1), 10_000_000),
                setStackSize(Materials.AshDark.getDust(1), 10_000_000),
                setStackSize(Materials.GraniticMineralSand.getDust(1), 10_000_000),
                setStackSize(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1), 10_000_000))
            .fluidInputs(
                Materials.Polycaprolactam.getMolten(10_000_000),
                Materials.NickelZincFerrite.getMolten(10_000_000),
                Materials.DarkSteel.getMolten(10_000_000),
                Materials.Polybenzimidazole.getMolten(10_000_000),
                GGMaterial.tairitsu.getMolten(10_000_000),
                Materials.Tungsten.getMolten(10_000_000),
                GGMaterial.marM200.getMolten(10_000_000),
                Materials.Vanadium.getMolten(10_000_000),
                MaterialsElements.STANDALONE.BLACK_METAL.getFluidStack(10_000_000),
                Materials.ShadowIron.getMolten(10_000_000),
                Materials.NaquadahAlloy.getMolten(10_000_000),
                Materials.ShadowSteel.getMolten(10_000_000),
                Materials.Cadmium.getMolten(10_000_000),
                Materials.Desh.getMolten(10_000_000),
                Materials.BlackPlutonium.getMolten(10_000_000),
                Materials.BlackSteel.getMolten(10_000_000),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(10_000_000))
            .fluidOutputs(MaterialPool.StabiliseVoidMatter.getFluidOrGas(1))
            .eut(RECIPE_MAX)
            .duration(20 * 99_999_999)
            .addTo(MT);

        // ProofOfGods
        // TODO -- Temporarily, be revised in the next version
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.UxvFlask.get(1),
                GTCMItemList.ProofOfHeroes.get(64),
                setStackSize(Materials.Silver.getNanite(1), 1_000),
                setStackSize(Materials.Gold.getNanite(1), 1_000),
                setStackSize(Materials.Neutronium.getNanite(1), 1_000),
                setStackSize(Materials.Universium.getNanite(1), 1_000),
                setStackSize(Materials.Eternity.getNanite(1), 1_000),
                setStackSize(Materials.TranscendentMetal.getNanite(1), 1_000),
                setStackSize(Materials.Glowstone.getNanite(1), 1_000),
                setStackSize(Materials.WhiteDwarfMatter.getNanite(1), 1_000),
                setStackSize(Materials.BlackDwarfMatter.getNanite(1), 1_000))
            .fluidInputs(
                MaterialPool.LiquidStargate.getFluidOrGas(50_000),
                MaterialPool.StabiliseVoidMatter.getFluidOrGas(1_000))
            .itemOutputs(GTCMItemList.ProofOfGods.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 99_999_999)
            .addTo(MT);

        // FLASK
        loadFlaskRecipe();

        // endregion
        if (Config.activateMegaSpaceStation) {
            loadMaxRecipe();
        }
    }

    public static void loadMaxRecipe() {
        ItemStack[] inStack = new ItemStack[] { ItemList.Circuit_Parts_ResistorXSMD.get(16),
            ItemList.Circuit_Parts_DiodeXSMD.get(16), ItemList.Circuit_Parts_TransistorXSMD.get(16),
            ItemList.Circuit_Parts_CapacitorXSMD.get(16), ItemList.Circuit_Parts_InductorXSMD.get(16) };
        ItemStack[] outStack = new ItemStack[] { GTCMItemList.HighDimensionalResistor.get(64),
            GTCMItemList.HighDimensionalDiode.get(64), GTCMItemList.HighDimensionalTransistor.get(64),
            GTCMItemList.HighDimensionalCapacitor.get(64), GTCMItemList.HighDimensionalInterface.get(64), };
        for (int i = 0; i < 5; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(12),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.TranscendentMetal, 4),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Universium, 2),
                    inStack[i],
                    GTCMItemList.HighDimensionalExtend.get(1))
                .fluidInputs(Materials.Time.getMolten(144))
                .itemOutputs(outStack[i])
                .eut(RECIPE_UEV)
                .duration(20)
                .addTo(MT);
        }

    }

    public static void loadFlaskRecipe() {
        final int ITEMS_FLASK_COUNT = 100_000;
        // TODO -- Temporarily, be revised in the next version
        // LV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                setStackSize(ItemList.Electric_Motor_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Microprocessor.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedstoneAlloy, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Iron.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.LvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(32 * 20)
            .addTo(MT);

        // MV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.LvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Processor.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Copper.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.MvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(128 * 20)
            .addTo(MT);

        // HV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.MvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Nanoprocessor.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Nickel.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.HvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(512 * 20)
            .addTo(MT);

        // EV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.HvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Quantumprocessor.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Titanium.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.EvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(2_048 * 20)
            .addTo(MT);

        // IV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.EvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Crystalprocessor.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Tungsten.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.IvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(8_192 * 20)
            .addTo(MT);

        // LUV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.IvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Neuroprocessor.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Osmium.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.LuvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(32_768 * 20)
            .addTo(MT);

        // ZPM FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.LuvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Bioprocessor.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Naquadah.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.ZpmFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(131_072 * 20)
            .addTo(MT);

        // UV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.ZpmFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_OpticalProcessor.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Neutronium.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.UvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(524_288 * 20)
            .addTo(MT);

        // UHV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_OpticalAssembly.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Samarium.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.UhvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(2_097_152 * 20)
            .addTo(MT);

        // UEV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UhvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_CosmicProcessor.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Americium.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.UevFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(8_388_608 * 20)
            .addTo(MT);

        // UIV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UevFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_CosmicAssembly.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Thorium.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.UivFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(33_554_432 * 20)
            .addTo(MT);

        // UMV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UivFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_CosmicComputer.get(1), ITEMS_FLASK_COUNT),
                setStackSize(
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 1),
                    ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Plutonium241.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.UmvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(100_000_000 * 20)
            .addTo(MT);

        // UXV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UmvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_CosmicMainframe.get(1), ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Infinity, 1), ITEMS_FLASK_COUNT))
            .fluidInputs(Materials.Radon.getPlasma(1_000_000_000))
            .itemOutputs(GTCMItemList.UxvFlask.get(1))
            .eut(RECIPE_MAX)
            .duration(100_000_000 * 20)
            .addTo(MT);

    }

}
