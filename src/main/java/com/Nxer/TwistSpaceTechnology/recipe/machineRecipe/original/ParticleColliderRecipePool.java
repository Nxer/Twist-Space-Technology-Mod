package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IRecipeMap;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.Particle;

public class ParticleColliderRecipePool {

    public static void loadRecipes() {
        final IRecipeMap PCRP = GTPPRecipeMaps.cyclotronRecipes;
        GTValues.RA.stdBuilder()
            .itemInputs(Particle.getBaseParticle(Particle.PROTON), Particle.getBaseParticle(Particle.ELECTRON))
            .fluidInputs(Materials.Hydrogen.getGas(1000))
            .itemOutputs(Particle.getBaseParticle(Particle.NEUTRON), Particle.getBaseParticle(Particle.UNKNOWN))
            .outputChances(1000, 1000)
            .fluidOutputs(Materials.Hydrogen.getPlasma(250))
            .noOptimize()
            .eut(RECIPE_UV)
            .duration(15 * 20)
            .addTo(PCRP);
    }
}
