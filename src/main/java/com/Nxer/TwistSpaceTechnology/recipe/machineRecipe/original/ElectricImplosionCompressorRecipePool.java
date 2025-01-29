package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import appeng.items.materials.MaterialType;
import bartworks.API.recipe.BartWorksRecipeMaps;
import gregtech.api.enums.GTValues;
import singulariteam.eternalsingularity.item.EternalSingularityItem;

public class ElectricImplosionCompressorRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // AE Singularity
        GTValues.RA.stdBuilder()
            .itemInputs(Laser_Lens_Special.get(1), new ItemStack(EternalSingularityItem.instance, 1))
            .itemOutputs(MaterialType.Singularity.stack(16), MaterialType.Singularity.stack(16))
            .outputChances(5000, 5000)
            .eut(RECIPE_UXV)
            .duration(20 * 100)
            .addTo(BartWorksRecipeMaps.electricImplosionCompressorRecipes);
    }
}
