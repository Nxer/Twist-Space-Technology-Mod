package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
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
            .itemOutputs(GTModHandler.getModItem(Mods.BartWorks.ID, "gt.bwMetaGenerateddust", 10, 11502))
            .noOptimize()
            .eut(TierEU.RECIPE_EV)
            .duration(20 * 10)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        // endregion

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
            .eut(TierEU.RECIPE_LuV)
            .duration(20 * 30)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

    }
}
