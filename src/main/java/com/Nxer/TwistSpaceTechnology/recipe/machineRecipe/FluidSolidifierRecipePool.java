package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MoldSingularity;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UV;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class FluidSolidifierRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        final IRecipeMap fs = RecipeMaps.fluidSolidifierRecipes;
        // Mold Singularity
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 1))
            .fluidInputs(Materials.Bedrockium.getMolten(144 * 1919))
            .itemOutputs(MoldSingularity.get(1))

            .eut(19198100)
            .duration(20 * 514)
            .addTo(fs);
        // Iron Singularity
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Iron.getMolten(144 * 9 * 7296))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 0))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);
        // Gold
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Gold.getMolten(144 * 9 * 1215))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 1))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);
        // Lapis
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("dye.chemical.dyeblue"), 72 * 9 * 1215))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 2))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Redstone
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Redstone.getMolten(144 * 9 * 7296))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 3))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Copper
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Copper.getMolten(144 * 9 * 3648))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 5))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Tin
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Tin.getMolten(144 * 9 * 3648))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 6))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Lead
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Lead.getMolten(144 * 9 * 3648))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 7))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Silver
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Silver.getMolten(144 * 9 * 7296))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 8))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Nickel
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Nickel.getMolten(144 * 9 * 3648))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 9))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Enderium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Enderium.getMolten(144 * 9 * 608))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 10))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Aluminium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Aluminium.getMolten(144 * 9 * 1824))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 0))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Brass
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Brass.getMolten(144 * 9 * 1824))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 1))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Bronze
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Bronze.getMolten(144 * 9 * 1824))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 2))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Electrum
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Electrum.getMolten(144 * 9 * 912))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 4))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Invar
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Invar.getMolten(144 * 9 * 1824))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 5))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Magnesium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Magnesium.getMolten(144 * 9 * 3648))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 6))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Osmium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Osmium.getMolten(144 * 9 * 406))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 7))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Steel
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Steel.getMolten(144 * 9 * 912))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 11))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Titanium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Titanium.getMolten(144 * 9 * 2024))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 12))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Tungsten
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Tungsten.getMolten(144 * 9 * 244))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 13))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Uranium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Uranium.getMolten(144 * 9 * 507))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 14))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Zinc
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Zinc.getMolten(144 * 9 * 3648))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 15))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Palladium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Palladium.getMolten(144 * 9 * 136))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 17))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Damascus Steel
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.DamascusSteel.getMolten(144 * 9 * 153))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 18))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Black Steel
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.BlackSteel.getMolten(144 * 9 * 304))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 19))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Electrum Flux
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.ElectrumFlux.getMolten(144 * 9 * 16))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 20))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Mercury
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Mercury.getFluid(1000 * 9 * 1824))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 21))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // ShadowStell
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.ShadowSteel.getMolten(144 * 9 * 406))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 22))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Irdium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Iridium.getMolten(144 * 9 * 62))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 23))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Platinum
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Platinum.getMolten(144 * 9 * 406))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 25))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Naquadria
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Naquadria.getMolten(144 * 9 * 66))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 26))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Plutonium.getMolten(144 * 9 * 244))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 27))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // MeteoricIron
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.MeteoricIron.getMolten(144 * 9 * 912))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 28))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Desh
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Desh.getMolten(144 * 9 * 203))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 29))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Europium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Europium.getMolten(144 * 9 * 62))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 30))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Draconium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Draconium.getMolten(144 * 9 * 1296))
            .itemOutputs(
                GTModHandler.getModItem("universalsingularities", "universal.draconicEvolution.singularity", 1, 0))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // DraconiumAwakened
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.DraconiumAwakened.getMolten(144 * 9 * 760))
            .itemOutputs(
                GTModHandler.getModItem("universalsingularities", "universal.draconicEvolution.singularity", 1, 1))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // ConductiveIron
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.ConductiveIron.getMolten(144 * 9 * 912))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 0))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // ElectricalSteel
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.ElectricalSteel.getMolten(144 * 9 * 912))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 1))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // EnergeticAlloy
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.EnergeticAlloy.getMolten(144 * 9 * 191))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 2))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // DarkSteel
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.DarkSteel.getMolten(144 * 9 * 912))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 3))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // RedstoneAlloy
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.RedstoneAlloy.getMolten(144 * 9 * 912))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 5))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // PulsatingIron
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.PulsatingIron.getMolten(144 * 9 * 912))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 4))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Soularium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Soularium.getMolten(144 * 9 * 456))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 6))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // VibrantAlloy
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.VibrantAlloy.getMolten(144 * 9 * 145))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 7))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Electrotine
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Electrotine.getMolten(144 * 9 * 1215))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.projectRed.singularity", 1, 0))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Aluminium
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Aluminium.getMolten(144 * 9 * 1824))
            .itemOutputs(
                GTModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 0))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Alumite
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Alumite.getMolten(144 * 9 * 229))
            .itemOutputs(
                GTModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 1))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Ardite
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Ardite.getMolten(144 * 9 * 304))
            .itemOutputs(
                GTModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 2))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Cobalt
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Cobalt.getMolten(144 * 9 * 1824))
            .itemOutputs(
                GTModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 3))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Manyullyn
        GTValues.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Manyullyn.getMolten(144 * 9 * 380))
            .itemOutputs(
                GTModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 6))

            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // come from eternal singularity
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GTModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Quartz, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("dye.chemical.dyeblue"), 72 * 9 * 1215),
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.Silver.getMolten(144 * 9 * 7296),
                Materials.Gold.getMolten(144 * 9 * 1215),
                Materials.Iron.getMolten(144 * 9 * 7296),
                Materials.Redstone.getMolten(144 * 9 * 7296),
                Materials.Tin.getMolten(144 * 9 * 3648),
                Materials.Lead.getMolten(144 * 9 * 3648),
                Materials.Copper.getMolten(144 * 9 * 3648),
                Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 0))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GTModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Emerald, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("dye.chemical.dyeblue"), 72 * 9 * 1215),
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.Enderium.getMolten(144 * 9 * 608),
                Materials.Aluminium.getMolten(144 * 9 * 1824),
                Materials.Brass.getMolten(144 * 9 * 1824),
                Materials.Bronze.getMolten(144 * 9 * 1824),
                Materials.Infinity.getMolten(144 * 2 * 4))
            .itemOutputs(GTModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 1))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GTModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Olivine, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Ruby, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Sapphire, 64))
            .fluidInputs(
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.Electrum.getMolten(144 * 9 * 912),
                Materials.Invar.getMolten(144 * 9 * 1824),
                Materials.Magnesium.getMolten(144 * 9 * 3648),
                Materials.Osmium.getMolten(144 * 9 * 406),
                Materials.Steel.getMolten(144 * 9 * 912),
                Materials.Titanium.getMolten(144 * 9 * 2024),
                Materials.Infinity.getMolten(144 * 2 * 3))
            .itemOutputs(GTModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 2))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GTModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.TricalciumPhosphate, 64))
            .fluidInputs(
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.Tungsten.getMolten(144 * 9 * 244),
                Materials.Zinc.getMolten(144 * 9 * 3648),
                Materials.Palladium.getMolten(144 * 9 * 136),
                Materials.DamascusSteel.getMolten(144 * 9 * 153),
                Materials.BlackSteel.getMolten(144 * 9 * 304),
                Materials.ElectrumFlux.getMolten(144 * 9 * 16),
                Materials.Mercury.getFluid(1000 * 9 * 1824),
                Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 3))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GTModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.NetherStar, 64))
            .fluidInputs(
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.ShadowSteel.getMolten(144 * 9 * 406),
                Materials.Iridium.getMolten(144 * 9 * 62),
                Materials.Platinum.getMolten(144 * 9 * 406),
                Materials.Uranium.getMolten(144 * 9 * 507),
                Materials.Naquadria.getMolten(144 * 9 * 66),
                Materials.Plutonium.getMolten(144 * 9 * 244),
                Materials.MeteoricIron.getMolten(144 * 9 * 912),
                Materials.Desh.getMolten(144 * 9 * 203),
                Materials.Europium.getMolten(144 * 9 * 62),
                Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 4))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GTModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64))
            .fluidInputs(
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.Draconium.getMolten(144 * 9 * 1296),
                Materials.DraconiumAwakened.getMolten(144 * 9 * 760),
                Materials.ConductiveIron.getMolten(144 * 9 * 912),
                Materials.ElectricalSteel.getMolten(144 * 9 * 912),
                Materials.EnergeticAlloy.getMolten(144 * 9 * 191),
                Materials.PulsatingIron.getMolten(144 * 9 * 912),
                Materials.DarkSteel.getMolten(144 * 9 * 912),
                Materials.RedstoneAlloy.getMolten(144 * 9 * 912),
                Materials.Soularium.getMolten(144 * 9 * 456))
            .itemOutputs(GTModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 5))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GTModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Unstable, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.EnderPearl, 64))
            .fluidInputs(
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.VibrantAlloy.getMolten(144 * 9 * 145),
                Materials.Electrotine.getMolten(144 * 9 * 1215),
                FluidRegistry.getFluidStack("aluminumbrass.molten", 144 * 9 * 1824),
                Materials.Aluminium.getMolten(144 * 9 * 1824),
                Materials.Alumite.getMolten(144 * 9 * 229),
                Materials.Ardite.getMolten(144 * 9 * 304),
                Materials.Cobalt.getMolten(144 * 9 * 1824),
                Materials.Manyullyn.getMolten(144 * 9 * 380),
                Materials.Infinity.getMolten(144 * 2 * 2))
            .itemOutputs(GTModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 6))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        // other singularity(no molten)

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Quartz, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("Avaritia", "Singularity", 1, 4))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.vanilla.singularity", 1, 0))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Emerald, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.vanilla.singularity", 1, 1))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Diamond, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.vanilla.singularity", 1, 2))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 3))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Olivine, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 8))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Ruby, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 9))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Sapphire, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 10))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.TricalciumPhosphate, 64),
                MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 16))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.NetherStar, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GTModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 24))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Unstable, 66), MoldSingularity.get(0))
            .itemOutputs(
                GTModHandler.getModItem("universalsingularities", "universal.extraUtilities.singularity", 1, 0))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.EnderPearl, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(
                GTModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 4))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(RecipeMaps.autoclaveRecipes);

    }
}
