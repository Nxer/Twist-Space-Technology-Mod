package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_EV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LuV;
import static galaxyspace.core.register.GSMaterials.tantalumCarbideHafniumCarbideMixture;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class MixerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        // region Ta4HfC5
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.dust, 1),
                Materials.Tantalum.getDust(4),
                Materials.Carbon.getDust(5))
            .itemOutputs(tantalumCarbideHafniumCarbideMixture.get(OrePrefixes.dust, 10))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20 * 10)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        // One Step HSS-S
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                Materials.TungstenSteel.getDust(10),
                Materials.Iridium.getDust(6),
                Materials.Molybdenum.getDust(4),
                Materials.Osmium.getDust(3),
                Materials.Chrome.getDust(2),
                Materials.Vanadium.getDust(2))
            .itemOutputs(Materials.HSSS.getDust(27))
            .noOptimize()
            .eut(RECIPE_LuV)
            .duration(20 * 30)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

    }
}
