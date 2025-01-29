package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.DRAGON_METAL;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPCombType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPDropType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPPropolisType;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class ChemicalBathRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        // Dragon Blood Bee Comb
        // TODO -- Temporarily, be revised in the next versions
        if (Config.Registry_DragonBlood_ExtraRecipe) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTPPCombType.DRAGONBLOOD.getStackForType(4))
                .fluidInputs(getFluidStack("fieryblood", 2000))
                .itemOutputs(
                    GTModHandler.getModItem(Forestry.ID, "refractoryWax", 1L, 0),
                    GTPP_Bees.propolis.getStackForType(GTPPPropolisType.DRAGONBLOOD),
                    GTPP_Bees.drop.getStackForType(GTPPDropType.DRAGONBLOOD))
                .outputChances(4000, 3250, 1250)
                .fluidOutputs(getFluidStack("fieryblood", 500))
                .noOptimize()
                .eut(RECIPE_UIV)
                .duration(20 * 120)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTPPCombType.DRAGONBLOOD.getStackForType(8))
                .fluidInputs(Materials.DraconiumAwakened.getMolten(144 * 16))
                .itemOutputs(
                    Materials.DarkAsh.getDust(8),
                    GTPP_Bees.propolis.getStackForType(GTPPPropolisType.DRAGONBLOOD),
                    GTPP_Bees.drop.getStackForType(GTPPDropType.DRAGONBLOOD))
                .fluidOutputs(DRAGON_METAL.getFluidStack(36))
                .outputChances(10000, 8000, 2500)
                .noOptimize()
                .eut(RECIPE_UIV)
                .duration(20 * 80)
                .addTo(chemicalBathRecipes);

        }
    }
}
