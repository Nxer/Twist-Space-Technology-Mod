package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static gregtech.api.util.GTModHandler.addCraftingRecipe;

import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;

public class SingleBlockRecipes implements IRecipePool {

    // spotless:off
    @Override
    public void loadRecipes() {
        loadTTBlockRecipes();
        loadGTBlockRecipes();
        loadTSTBlockRecipes();
    }

    void loadTTBlockRecipes() {}

    void loadGTBlockRecipes() {}

    void loadTSTBlockRecipes() {
        // Borophene Based Nanowire Composite Thermal Conductive Casing
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.InfinityCatalyst, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 4),
                GTCMItemList.BoropheneBasedNanowireCompositeThermalConductiveMedium.get(3),
                ItemList.neutroniumHeatCapacitor.get(1))
            .fluidInputs(
                MaterialsAlloy.BLACK_TITANIUM.getFluidStack(144 * 8),
                FluidRegistry.getFluidStack("sodiumpotassium", 1000 * 6),
                Materials.Boron.getPlasma(144 * 16),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 12))
            .itemOutputs(GTCMItemList.BoropheneBasedNanowireCompositeThermalConductiveCasing.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(20 * 30)
            .specialValue(3)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);

        // Neutronium Pipe Casing
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(GTCMItemList.NeutroniumPipeCasing.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(20 * 5)
            .addTo(RecipeMaps.assemblerRecipes);

        addCraftingRecipe(
            GTCMItemList.NeutroniumPipeCasing.get(1),
            new Object[] { "ABA", "BCB", "ABA",
                'A', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 1),
                'B', GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 1),
                'C', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1)});
    }
    // spotless:on
}
