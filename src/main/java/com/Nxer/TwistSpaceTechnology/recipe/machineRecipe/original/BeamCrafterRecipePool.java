package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.recipe.RecipeMaps.beamcrafterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;
import static gtnhlanth.common.beamline.Particle.PROTON;

import appeng.items.materials.MaterialType;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.loaders.postload.recipes.beamcrafter.BeamCrafterMetadata;

public class BeamCrafterRecipePool {

    public static void loadRecipes() {

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialType.Singularity.stack(1), Laser_Lens_Special.get(1))
            .itemOutputs(MaterialType.Singularity.stack(1), Laser_Lens_Special.get(1))
            .fluidOutputs(Materials.Hydrogen.getPlasma(2000))
            .outputChances(9900, 9900)
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(PROTON.getId())
                    .amount_A(100)
                    .amount_B(100)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(beamcrafterRecipes);

    }

}
