package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static com.Nxer.TwistSpaceTechnology.common.init.TstItems.EssentiaPatternTerminalEx;
import static com.glodblock.github.loader.ItemAndBlockHolder.FLUID_TERMINAL_EX;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class SingleItemRecipes {

    // spotless:off
    public static void loadRecipes() {
        // Borophene Foil
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Silver, 1),
                CustomItemList.ChromaticLens.get(0))
            .fluidInputs(Materials.Boron.getPlasma(144 * 32))
            .itemOutputs(GTCMItemList.BoropheneFoil.get(1))
            .outputChances(2500)
            .eut(TierEU.RECIPE_UV)
            .duration(20 * 30)
            .addTo(RecipeMaps.laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 0, false))
            .fluidInputs(Materials.Boron.getPlasma(144 * 2))
            .itemOutputs(GTCMItemList.BoropheneFoil.get(2))
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 5)
            .addTo(RecipeMaps.laserEngraverRecipes);

        // Borophene Based Nanowire Composite Thermal Conductive Medium
        TST_RecipeBuilder.builder()
            .itemInputs(
                ItemList.TaHfCNanofibers.get(5),
                ItemList.NtNanofibers.get(5),
                GTCMItemList.BoropheneFoil.get(8),
                ItemRefer.Special_Ceramics_Plate.get(16))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 5),
                new FluidStack(TFFluids.fluidCryotheum,1000 * 2),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(1000 * 6),
                GGMaterial.hikarium.getMolten(144 * 10))
            .itemOutputs(GTCMItemList.BoropheneBasedNanowireCompositeThermalConductiveMedium.get(3))
            .eut(TierEU.RECIPE_ZPM)
            .duration(20 * 10)
            .specialValue(3)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);

        //Essentia Pattern Terminal Ex
        TST_RecipeBuilder.builder()
            .itemInputs(
                new ItemStack(FLUID_TERMINAL_EX, 1)
            )
            .itemOutputs(new ItemStack(EssentiaPatternTerminalEx, 1))
            .eut(TierEU.RECIPE_HV)
            .duration(20 * 10)
            .addTo(RecipeMaps.assemblerRecipes);
    }
    // spotless:on
}
