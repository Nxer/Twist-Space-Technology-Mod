package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class SpaceAssemblerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final RecipeMap<?> SA = com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;

        {
            // adv radiation proof plate
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.ElectrumFlux, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Trinium, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.VibrantAlloy, 4),
                    ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 0, 3))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8),
                    Materials.Lead.getMolten(144 * 16 * 9))
                .itemOutputs(ItemRefer.Advanced_Radiation_Protection_Plate.get(9))
                .specialValue(2)
                .eut(RECIPE_UEV)
                .duration(250)
                .noOptimize()
                .addTo(SA);

            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Lanthanum, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.ElectrumFlux, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Trinium, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.VibrantAlloy, 4),
                    ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 0, 12))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 48),
                    Materials.Lead.getPlasma(144 * 16 * 64))
                .itemOutputs(ItemRefer.Advanced_Radiation_Protection_Plate.get(64))
                .specialValue(3)
                .eut(RECIPE_UXV)
                .duration(250)
                .noOptimize()
                .addTo(SA);
        }

    }
}
