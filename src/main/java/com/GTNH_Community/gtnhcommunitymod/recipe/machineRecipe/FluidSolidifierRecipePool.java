package com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe;

import static com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList.MoldSingularity;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UV;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.GTNH_Community.gtnhcommunitymod.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

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

        // ShadowStell
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

        // come from eternal singularity
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GT_ModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Quartz, 64))
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
            .itemOutputs(GT_ModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 0))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GT_ModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Emerald, 64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("dye.chemical.dyeblue"), 72 * 9 * 1215),
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.Enderium.getMolten(144 * 9 * 608),
                Materials.Aluminium.getMolten(144 * 9 * 1824),
                Materials.Brass.getMolten(144 * 9 * 1824),
                Materials.Bronze.getMolten(144 * 9 * 1824),
                Materials.Infinity.getMolten(144 * 2 * 4))
            .itemOutputs(GT_ModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 1))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GT_ModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Olivine, 64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Ruby, 64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Sapphire, 64))
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
            .itemOutputs(GT_ModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 2))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GT_ModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.TricalciumPhosphate, 64))
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
            .itemOutputs(GT_ModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 3))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GT_ModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.NetherStar, 64))
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
            .itemOutputs(GT_ModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 4))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GT_ModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64))
            .fluidInputs(
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.Draconium.getMolten(144 * 9 * 1296),
                Materials.DraconiumAwakened.getMolten(144 * 9 * 760),
                Materials.ConductiveIron.getMolten(144 * 9 * 912),
                Materials.ElectricalSteel.getMolten(144 * 9 * 912),
                Materials.EnergeticAlloy.getMolten(144 * 9 * 191),
                Materials.PhasedIron.getMolten(144 * 9 * 912),
                Materials.DarkSteel.getMolten(144 * 9 * 912),
                Materials.RedstoneAlloy.getMolten(144 * 9 * 912),
                Materials.Soularium.getMolten(144 * 9 * 456))
            .itemOutputs(GT_ModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 5))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                MoldSingularity.get(0),
                ItemList.Shape_Mold_Block.get(0),
                GT_ModHandler.getModItem("miscutils", "MU-metaitem.01", 0, 32100),
                ItemList.MSFMixture.get(64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Unstable, 64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.EnderPearl, 64))
            .fluidInputs(
                Materials.BlackPlutonium.getMolten(144 * 9 * 12),
                Materials.Bedrockium.getMolten(144 * 9 * 4),
                Materials.VibrantAlloy.getMolten(144 * 9 * 145),
                Materials.Electrotine.getMolten(144 * 9 * 1215),
                Materials.ConductiveIron.getMolten(144 * 9 * 912),
                Materials.Aluminium.getMolten(144 * 9 * 1824),
                Materials.Alumite.getMolten(144 * 9 * 229),
                Materials.Ardite.getMolten(144 * 9 * 304),
                Materials.Cobalt.getMolten(144 * 9 * 1824),
                Materials.Manyullyn.getMolten(144 * 9 * 380),
                Materials.Infinity.getMolten(144 * 2 * 2))
            .itemOutputs(GT_ModHandler.getModItem("eternalsingularity", "combined_singularity", 1, 6))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(200)
            .addTo(GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT);

        // other singularity(no molten)

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Quartz, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Singularity", 1, 4))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.vanilla.singularity", 1, 0))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Emerald, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.vanilla.singularity", 1, 1))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Diamond, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.vanilla.singularity", 1, 2))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 3))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Olivine, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 8))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Ruby, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 9))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Sapphire, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 10))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.TricalciumPhosphate, 64),
                MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 16))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.NetherStar, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(GT_ModHandler.getModItem("universalsingularities", "universal.general.singularity", 1, 24))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Unstable, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(
                GT_ModHandler.getModItem("universalsingularities", "universal.extraUtilities.singularity", 1, 0))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.EnderPearl, 64), MoldSingularity.get(0))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(
                GT_ModHandler.getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 4))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

    }
}
