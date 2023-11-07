package com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe;

import static com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList.MoldSingularity;
import static gregtech.api.enums.TierEU.RECIPE_UV;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.GTNH_Community.gtnhcommunitymod.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;

public class FluidSolidifierRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        final GT_Recipe.GT_Recipe_Map fs = GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes;
        // Mold Singularity
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1))
            .fluidInputs(Materials.Bedrockium.getMolten(144 * 1919))
            .itemOutputs(MoldSingularity.get(1))
            .noFluidOutputs()
            .eut(19198100)
            .duration(20 * 514)
            .addTo(fs);
        // Iron Singularity
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Iron.getMolten(144 * 9 * 7296))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 0))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);
        // Gold
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Gold.getMolten(144 * 9 * 1215))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 1))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);
        // Lapis
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("dye.chemical.dyeblue"), 72 * 9 * 1215))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 2))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Redstone
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Redstone.getMolten(144 * 9 * 7296))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 3))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Copper
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Copper.getMolten(144 * 9 * 3648))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 5))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Tin
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Tin.getMolten(144 * 9 * 3648))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 6))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Lead
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Lead.getMolten(144 * 9 * 3648))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 7))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Silver
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Silver.getMolten(144 * 9 * 7296))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 8))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Nickel
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Nickel.getMolten(144 * 9 * 3648))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 9))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Enderium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Enderium.getMolten(144 * 9 * 608))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 10))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Aluminium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Aluminium.getMolten(144 * 9 * 1824))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 0))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Brass
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Brass.getMolten(144 * 9 * 1824))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 1))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Bronze
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Bronze.getMolten(144 * 9 * 1824))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 2))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Electrum
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Electrum.getMolten(144 * 9 * 912))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 4))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Invar
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Invar.getMolten(144 * 9 * 1824))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 5))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Magnesium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Magnesium.getMolten(144 * 9 * 3648))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 6))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Osmium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Osmium.getMolten(144 * 9 * 406))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 7))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Steel
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Steel.getMolten(144 * 9 * 912))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 11))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Titanium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Titanium.getMolten(144 * 9 * 2024))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 12))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Tungsten
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Tungsten.getMolten(144 * 9 * 244))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 13))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Uranium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Uranium.getMolten(144 * 9 * 507))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 14))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Zinc
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Zinc.getMolten(144 * 9 * 3648))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 15))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Palladium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Palladium.getMolten(144 * 9 * 136))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 17))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Damascus Steel
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.DamascusSteel.getMolten(144 * 9 * 153))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 18))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Black Steel
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.BlackSteel.getMolten(144 * 9 * 304))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 19))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Electrum Flux
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.ElectrumFlux.getMolten(144 * 9 * 16))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 20))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Mercury
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Mercury.getFluid(1000 * 9 * 1824))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 21))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // ShadowSteel
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.ShadowSteel.getMolten(144 * 9 * 406))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 22))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Irdium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Iridium.getMolten(144 * 9 * 62))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 23))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Platinum
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Platinum.getMolten(144 * 9 * 406))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 25))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Naquadria
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Naquadria.getMolten(144 * 9 * 66))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 26))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Plutonium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Plutonium.getMolten(144 * 9 * 244))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 27))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // MeteoricIron
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.MeteoricIron.getMolten(144 * 9 * 912))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 28))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Desh
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Desh.getMolten(144 * 9 * 203))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 29))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Europium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Europium.getMolten(144 * 9 * 62))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 30))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Draconium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Draconium.getMolten(144 * 9 * 1296))
            .itemOutputs(
                GT_ModHandler.getModItem("universalsingularities", "universal.draconicEvolution.singularity", 1, 0))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // DraconiumAwakened
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.DraconiumAwakened.getMolten(144 * 9 * 760))
            .itemOutputs(
                GT_ModHandler.getModItem("universalsingularities", "universal.draconicEvolution.singularity", 1, 1))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // ConductiveIron
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.ConductiveIron.getMolten(144 * 9 * 912))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 0))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // ElectricalSteel
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.ElectricalSteel.getMolten(144 * 9 * 912))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 1))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // EnergeticAlloy
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.EnergeticAlloy.getMolten(144 * 9 * 191))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 2))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // DarkSteel
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.DarkSteel.getMolten(144 * 9 * 912))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 3))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // RedstoneAlloy
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.RedstoneAlloy.getMolten(144 * 9 * 912))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 5))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // PhasedIron
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.PhasedIron.getMolten(144 * 9 * 912))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 4))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Soularium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Soularium.getMolten(144 * 9 * 456))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 6))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // VibrantAlloy
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.VibrantAlloy.getMolten(144 * 9 * 145))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.enderIO.singularity", 1, 7))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Electrotine
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Electrotine.getMolten(144 * 9 * 1215))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.projectRed.singularity", 1, 0))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Aluminium
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Aluminium.getMolten(144 * 9 * 1824))
            .itemOutputs(
                GT_ModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 0))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Alumite
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Alumite.getMolten(144 * 9 * 229))
            .itemOutputs(
                GT_ModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 1))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Ardite
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Ardite.getMolten(144 * 9 * 304))
            .itemOutputs(
                GT_ModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 2))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Cobalt
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Cobalt.getMolten(144 * 9 * 1824))
            .itemOutputs(
                GT_ModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 3))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);

        // Manyullyn
        GT_Values.RA.stdBuilder()
            .itemInputs(MoldSingularity.get(0))
            .fluidInputs(Materials.Manyullyn.getMolten(144 * 9 * 380))
            .itemOutputs(
                GT_ModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 6))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(fs);
    }
}
