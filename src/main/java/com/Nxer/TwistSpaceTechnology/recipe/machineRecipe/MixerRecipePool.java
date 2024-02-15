package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_EV;
import static galaxyspace.core.register.GSMaterials.tantalumCarbideHafniumCarbideMixture;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class MixerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        // region Ta4HfC5
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.dust, 1),
                Materials.Tantalum.getDust(4),
                Materials.Carbon.getDust(5))
            .itemOutputs(tantalumCarbideHafniumCarbideMixture.get(OrePrefixes.dust, 10))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20 * 10)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        // endregion
    }
}
