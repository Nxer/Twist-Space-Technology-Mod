package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static gregtech.api.util.GTRecipeConstants.QFT_CATALYST;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class QFTRecipePool {

    public static void loadRecipes() {
        final IRecipeMap QFT = GTPPRecipeMaps.quantumForceTransformerRecipes;

        // Samarium
        GTValues.RA.stdBuilder()
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .itemInputs(WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 32))
            .itemOutputs(
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Phosphorus.getDust(64),
                Materials.Thorium.getDust(64),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 64))
            .outputChances(2000, 2000, 2000, 2000, 2000)
            .specialValue(2)
            .eut(RECIPE_UHV)
            .duration(400)
            .addTo(QFT);
    }
}
