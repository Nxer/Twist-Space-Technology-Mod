package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.itemRecipeList;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WhiteDwarfMold_Ingot;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.recipe.RecipeMaps;

public class SingleItemRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        // White Dwarf Mold(Ingot)
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Neutronium.getIngots(1))
            .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(576))
            .itemOutputs(WhiteDwarfMold_Ingot.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 10)
            .addTo(RecipeMaps.fluidSolidifierRecipes);
    }
}
