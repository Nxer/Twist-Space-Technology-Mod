package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WhiteDwarfMold_Ingot;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class SingleItemRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        // 流汗黄豆
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(17),
                new ItemStack(Items.golden_apple, 1, 1),
                ItemList.Emitter_LV.get(64),
                ItemList.Field_Generator_LV.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 64))
            .itemOutputs(GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.get(1))
            .eut(RECIPE_LV)
            .duration(20 * 114)
            .addTo(assemblerRecipes);

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
