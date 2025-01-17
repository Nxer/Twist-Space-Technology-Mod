package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_IV;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.MaterialsElements;

public class AlloySmelterRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(15),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sphalerite, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4))
            .fluidInputs(Materials.SulfuricAcid.getFluid(250))

            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().GERMANIUM.getFluid(), 36))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration((int) (20 * 37.5))
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4))
            .fluidInputs(Materials.SulfuricAcid.getFluid(625))

            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().RHENIUM.getFluid(), 36))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration((int) (20 * 37.5))
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Scheelite, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1250))

            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().RHENIUM.getFluid(), 18))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration((int) (20 * 37.5))
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenite, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 8))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1875))

            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().RHENIUM.getFluid(), 36))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20 * 75)
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrite, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 16))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1250))

            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().THALLIUM.getFluid(), 288))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20 * 75)
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        // endregion
    }
}
