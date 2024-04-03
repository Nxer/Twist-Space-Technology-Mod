package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.config.Config.OutputMoltenFluidInsteadIngotInStellarForgeRecipe;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.DRAGON_METAL;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_CombType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_DropType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_PropolisType;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class DragonBloodRecipe implements IRecipePool {

    @Override
    public void loadRecipes() {
        if (Config.Registry_DragonBlood_ExtraRecipe) {
            // Fluid Heater
            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.dragon_egg, 1, 0))
                .fluidInputs(getFluidStack("potion.dragonblood", 1000))
                .fluidOutputs(DRAGON_METAL.getFluidStack(144))
                .noOptimize()
                .eut(RECIPE_UXV)
                .duration(20 * 6)
                .addTo(RecipeMaps.fluidHeaterRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(Utils.setStackSize(GT_ModHandler.getModItem("witchery", "infinityegg", 0), 0))
                .fluidInputs(getFluidStack("potion.dragonblood", 1000))
                .fluidOutputs(DRAGON_METAL.getFluidStack(144))
                .noOptimize()
                .eut(RECIPE_UXV)
                .duration(20 * 3)
                .addTo(RecipeMaps.fluidHeaterRecipes);

            // Chemical Bath
            GT_Values.RA.stdBuilder()
                .itemInputs(GTPP_CombType.DRAGONBLOOD.getStackForType(8))
                .fluidInputs(getFluidStack("fieryblood", 2000))
                .itemOutputs(
                    GT_ModHandler.getModItem(Forestry.ID, "refractoryWax", 1L, 0),
                    GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.DRAGONBLOOD),
                    GTPP_Bees.drop.getStackForType(GTPP_DropType.DRAGONBLOOD))
                .outputChances(4000, 2250, 750)
                .fluidOutputs(getFluidStack("fieryblood", 500))
                .noOptimize()
                .eut(RECIPE_UEV)
                .duration(20 * 150)
                .addTo(chemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GTPP_CombType.DRAGONBLOOD.getStackForType(8))
                .fluidInputs(Materials.DraconiumAwakened.getMolten(288))
                .itemOutputs(
                    Materials.DarkAsh.getDust(8),
                    GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.DRAGONBLOOD),
                    GTPP_Bees.drop.getStackForType(GTPP_DropType.DRAGONBLOOD))
                .fluidOutputs(DRAGON_METAL.getFluidStack(72))
                .outputChances(10000, 6000, 2000)
                .noOptimize()
                .eut(RECIPE_UEV)
                .duration(20 * 100)
                .addTo(chemicalBathRecipes);

            // Stellar Forge
            TST_RecipeBuilder bd = TST_RecipeBuilder.builder()
                .itemInputs(
                    new ItemStack(Blocks.dragon_egg, 1),
                    Materials.DraconiumAwakened.getDust(64),
                    GTPP_CombType.DRAGONBLOOD.getStackForType(4));
            bd.fluidInputs(Materials.Radon.getPlasma(144 * 4));
            if (OutputMoltenFluidInsteadIngotInStellarForgeRecipe) {
                bd.fluidOutputs(DRAGON_METAL.getFluidStack(144 * 4));
            } else {
                bd.itemOutputs(DRAGON_METAL.getIngot(4));
            }
            bd.eut(RECIPE_UIV)
                .duration(20 * 30)
                .addTo(GTCMRecipe.StellarForgeRecipes);

            bd = TST_RecipeBuilder.builder()
                .itemInputs(
                    Utils.setStackSize(GT_ModHandler.getModItem("witchery", "infinityegg", 0), 1),
                    Materials.DraconiumAwakened.getDust(64),
                    GTPP_CombType.DRAGONBLOOD.getStackForType(4));
            bd.fluidInputs(Materials.Radon.getPlasma(144 * 2));
            if (OutputMoltenFluidInsteadIngotInStellarForgeRecipe) {
                bd.fluidOutputs(DRAGON_METAL.getFluidStack(144 * 4 * 64));
            } else {
                bd.itemOutputs(DRAGON_METAL.getIngot(4 * 64));
            }
            bd.eut(RECIPE_UIV)
                .duration(20 * 20)
                .addTo(GTCMRecipe.StellarForgeRecipes);

            bd = TST_RecipeBuilder.builder()
                .itemInputs(new ItemStack(Blocks.dragon_egg, 1), Materials.DraconiumAwakened.getDust(64));
            bd.fluidInputs(Materials.Radon.getPlasma(144 * 4));
            if (OutputMoltenFluidInsteadIngotInStellarForgeRecipe) {
                bd.fluidOutputs(DRAGON_METAL.getFluidStack(144 * 4));
            } else {
                bd.itemOutputs(DRAGON_METAL.getIngot(4));
            }
            bd.eut(RECIPE_UIV)
                .duration(20 * 60)
                .addTo(GTCMRecipe.StellarForgeRecipes);

            bd = TST_RecipeBuilder.builder()
                .itemInputs(
                    Utils.setStackSize(GT_ModHandler.getModItem("witchery", "infinityegg", 0), 1),
                    Materials.DraconiumAwakened.getDust(64));
            bd.fluidInputs(Materials.Radon.getPlasma(144 * 2));
            if (OutputMoltenFluidInsteadIngotInStellarForgeRecipe) {
                bd.fluidOutputs(DRAGON_METAL.getFluidStack(144 * 4 * 64));
            } else {
                bd.itemOutputs(DRAGON_METAL.getIngot(4 * 64));
            }
            bd.eut(RECIPE_UIV)
                .duration(20 * 40)
                .addTo(GTCMRecipe.StellarForgeRecipes);

            // Star Kernel
            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.dragon_egg, 1))
                .fluidOutputs(new FluidStack(DRAGON_METAL.getPlasma(), 144 * 2))
                .noOptimize()
                .eut(RECIPE_MAX)
                .duration(20 * 10)
                .addTo(GTCMRecipe.BallLightningRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(Utils.setStackSize(GT_ModHandler.getModItem("witchery", "infinityegg", 0), 1))
                .fluidOutputs(new FluidStack(DRAGON_METAL.getPlasma(), 144 * 2 * 256))
                .noOptimize()
                .eut(RECIPE_MAX)
                .duration(20 * 10)
                .addTo(GTCMRecipe.BallLightningRecipes);
        }
    }

}
