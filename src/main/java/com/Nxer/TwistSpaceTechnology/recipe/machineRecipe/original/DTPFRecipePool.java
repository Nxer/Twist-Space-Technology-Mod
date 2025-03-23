package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTUtility.copyAmount;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.material.MaterialsElements;

public class DTPFRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // Entropic Flux

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.NaquadriaSupersolid.get(1))
            .fluidInputs(
                MaterialsTST.Dubnium.getPlasma(144 * 8),
                GGMaterial.extremelyUnstableNaquadah.getMolten(144 * 12),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(1),

                MaterialsUEVplus.Space.getMolten(144),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(500),
                MaterialsUEVplus.ExcitedDTPC.getFluid(1000))
            .fluidOutputs(
                MaterialPool.EntropicFlux.getFluidOrGas(500),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(250))
            .specialValue(10800)
            .eut(TierEU.RECIPE_UIV)
            .duration(20 * 30)
            .addTo(RecipeMaps.plasmaForgeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.NaquadriaSupersolid.get(1))
            .fluidInputs(
                MaterialsTST.Dubnium.getPlasma(144 * 8 * 2),
                GGMaterial.extremelyUnstableNaquadah.getMolten(144 * 12 * 2),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(10),

                MaterialsUEVplus.Space.getMolten(144 * 2),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(500 * 2),
                MaterialsUEVplus.ExcitedDTRC.getFluid(2000))
            .fluidOutputs(
                MaterialPool.EntropicFlux.getFluidOrGas(500 * 4),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1000))
            .specialValue(11700)
            .eut(TierEU.RECIPE_UMV)
            .duration(20 * 25)
            .addTo(RecipeMaps.plasmaForgeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.NaquadriaSupersolid.get(1))
            .fluidInputs(
                MaterialsTST.Dubnium.getPlasma(144 * 8 * 2 * 3),
                GGMaterial.extremelyUnstableNaquadah.getMolten(144 * 12 * 2 * 3),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(100),

                MaterialsUEVplus.Space.getMolten(144 * 4),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(500 * 4),
                MaterialsUEVplus.ExcitedDTEC.getFluid(4000))
            .fluidOutputs(
                MaterialPool.EntropicFlux.getFluidOrGas(500 * 16),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(4000))
            .specialValue(12600)
            .eut(TierEU.RECIPE_UXV)
            .duration(20 * 20)
            .addTo(RecipeMaps.plasmaForgeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.NaquadriaSupersolid.get(1))
            .fluidInputs(
                MaterialsTST.Dubnium.getPlasma(144 * 8 * 2 * 3 * 4),
                GGMaterial.extremelyUnstableNaquadah.getMolten(144 * 12 * 2 * 3 * 4),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000),

                MaterialsUEVplus.Space.getMolten(144 * 8),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(500 * 8),
                MaterialsUEVplus.ExcitedDTSC.getFluid(8000))
            .fluidOutputs(
                MaterialPool.EntropicFlux.getFluidOrGas(500 * 64),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(16000))
            .specialValue(13500)
            .eut(TierEU.RECIPE_MAX)
            .duration(20 * 15)
            .addTo(RecipeMaps.plasmaForgeRecipes);

        loadExtraRecipes();
    }

    public void loadExtraRecipes() {
        if (!Config.Registry_DTPF_ExtraRecipe) return;

        final IRecipeMap DTPF = RecipeMaps.plasmaForgeRecipes;

        // Coils
        final int awakened_heat = 10800;
        final int infinity_heat = awakened_heat + 900;
        final int hypogen_heat = infinity_heat + 900;
        final int eternal_heat = hypogen_heat + 900;

        final int[] Coils = { awakened_heat, infinity_heat, hypogen_heat, eternal_heat };

        // Catalyst
        final Materials[] Catalysts = { MaterialsUEVplus.ExcitedDTCC, MaterialsUEVplus.ExcitedDTPC,
            MaterialsUEVplus.ExcitedDTRC, MaterialsUEVplus.ExcitedDTEC, MaterialsUEVplus.ExcitedDTSC };

        final int[] multiply = { 1, 2, 4, 8 };

        // Infinity
        final int[] Infinity_amount = { 10_000, 50_000, 100_000, 200_000 };

        final int[] Infinity_time = { 1, 100, 50, 25 };

        final int[] Infinity_eut = { 1_000_000, 50_000_000, 100_000_000, 250_000_000 };

        final int[] Infinity_catalyst = { 1_000, 25_000, 10_000, 5_000 };

        // Hypogen
        final int[] Hypogen_amount = { 5_000, 10_000, 20_000, 40_000 };

        final int[] Hypogen_time = { 35, 35, 25, 25 };

        final int[] Hypogen_eut = { 500_000_000, 1_000_000_000, 1_500_000_000, 2_000_000_000 };

        // Infinity
        {
            final ItemStack InfiniteCatalyst = ModItemHandler.getInfinityCatalyst(1);

            for (int i = 0; i < Coils.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(copyAmount(64 / multiply[i], InfiniteCatalyst), GTUtility.getIntegratedCircuit(5))
                    .fluidInputs(
                        Catalysts[i + 1].getFluid(Infinity_catalyst[i]),
                        Materials.CosmicNeutronium.getMolten(1_152 / multiply[i]),
                        Materials.Neutronium.getMolten(1_152 / multiply[i]))
                    .fluidOutputs(
                        Materials.Infinity.getMolten(Infinity_amount[i]),
                        MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(Infinity_catalyst[i] / 2))
                    .duration(Infinity_time[i] * 20)
                    .eut(Infinity_eut[i])
                    .metadata(COIL_HEAT, Coils[i])
                    .addTo(DTPF);
            }

            // Infinity Bee comb
            for (int i = 0; i < Coils.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        copyAmount(32 / multiply[i], InfiniteCatalyst),
                        GTBees.combs.getStackForType(CombType.INFINITY, 64 / multiply[i]),
                        GTUtility.getIntegratedCircuit(6))
                    .fluidInputs(
                        Catalysts[i + 1].getFluid(Infinity_catalyst[i] / 2),
                        Materials.CosmicNeutronium.getMolten(1_152 / multiply[i] / 2),
                        Materials.Neutronium.getMolten(1_152 / multiply[i] / 2))
                    .fluidOutputs(
                        Materials.Infinity.getMolten(Infinity_amount[i]),
                        MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(Infinity_catalyst[i] / 4))
                    .duration(Infinity_time[i] * 10)
                    .eut(Infinity_eut[i])
                    .metadata(COIL_HEAT, Coils[i])
                    .addTo(DTPF);
            }
        }

        // Hypogen
        for (int i = 0; i < Coils.length; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(i < 1 ? GTUtility.getIntegratedCircuit(7) : ItemList.SuperconductorComposite.get(0))
                .fluidInputs(
                    Materials.Grade8PurifiedWater.getFluid(10_000L * multiply[i]),
                    Materials.Infinity.getMolten(1_440),
                    Materials.CosmicNeutronium.getMolten(1_440),
                    Catalysts[i + 1].getFluid(1_000))
                .fluidOutputs(
                    new FluidStack(MaterialsElements.STANDALONE.HYPOGEN.getFluid(), Hypogen_amount[i]),
                    MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(500))
                .duration(Hypogen_time[i] * 20)
                .eut(Hypogen_eut[i])
                .metadata(COIL_HEAT, Coils[i])
                .addTo(DTPF);
        }

        // SpaceTime
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.EnergisedTesseract.get(1))
            .fluidInputs(
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(10_000),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(1_152),
                Materials.Infinity.getMolten(9_608))
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(2_304))
            .duration(20 * 20)
            .eut(1_000_000_000)
            .metadata(COIL_HEAT, hypogen_heat)
            .addTo(DTPF);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.NaquadriaSupersolid.get(0))
            .fluidInputs(
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(20_000),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(576),
                Materials.Infinity.getMolten(9_608))
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(2_304))
            .duration(5 * 20)
            .eut(2_000_000_000)
            .metadata(COIL_HEAT, hypogen_heat)
            .addTo(DTPF);
    }
}
