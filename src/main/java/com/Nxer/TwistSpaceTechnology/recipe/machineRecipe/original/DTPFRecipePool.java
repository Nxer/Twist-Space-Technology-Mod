package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTUtility.copyAmount;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.material.MaterialsElements;

public class DTPFRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
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
            final ItemStack InfiniteCatalyst = GTModHandler
                .getModItem(Avaritia.ID, "Resource", 1, 5, GTCMItemList.TestItem0.get(1));

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
                    .duration(Infinity_time[i] * 20)
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
