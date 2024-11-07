package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.StellarForgeRecipePool.addToMiracleDoorRecipes;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.DRAGON_METAL;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.Utils;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPCombType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPDropType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPPropolisType;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class DragonBloodRecipe implements IRecipePool {

    @Override
    public void loadRecipes() {
        if (Config.Registry_DragonBlood_ExtraRecipe) {
            // // Fluid Heater
            // GTValues.RA.stdBuilder()
            // .itemInputs(new ItemStack(Blocks.dragon_egg, 0, 0))
            // .fluidInputs(getFluidStack("potion.dragonblood", 1000))
            // .fluidOutputs(DRAGON_METAL.getFluidStack(36))
            // .noOptimize()
            // .eut(RECIPE_UXV)
            // .duration(20 * 6)
            // .addTo(RecipeMaps.fluidHeaterRecipes);
            //
            // GTValues.RA.stdBuilder()
            // .itemInputs(
            // Utils.setStackSize(
            // GTModHandler.getModItem("witchery", "infinityegg", 0, GTCMItemList.TestItem0.get(1)),
            // 0))
            // .fluidInputs(getFluidStack("potion.dragonblood", 1000))
            // .fluidOutputs(DRAGON_METAL.getFluidStack(144))
            // .noOptimize()
            // .eut(RECIPE_UXV)
            // .duration(20 * 3)
            // .addTo(RecipeMaps.fluidHeaterRecipes);

            // Chemical Bath
            GTValues.RA.stdBuilder()
                .itemInputs(GTPPCombType.DRAGONBLOOD.getStackForType(8))
                .fluidInputs(getFluidStack("fieryblood", 2000))
                .itemOutputs(
                    GTModHandler.getModItem(Forestry.ID, "refractoryWax", 1L, 0),
                    GTPP_Bees.propolis.getStackForType(GTPPPropolisType.DRAGONBLOOD),
                    GTPP_Bees.drop.getStackForType(GTPPDropType.DRAGONBLOOD))
                .outputChances(4000, 2250, 750)
                .fluidOutputs(getFluidStack("fieryblood", 500))
                .noOptimize()
                .eut(RECIPE_UEV)
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
                .outputChances(10000, 6000, 2000)
                .noOptimize()
                .eut(RECIPE_UEV)
                .duration(20 * 100)
                .addTo(chemicalBathRecipes);

            // Stellar Forge
            addToMiracleDoorRecipes(
                new ItemStack[] { new ItemStack(Blocks.dragon_egg, 1), GTPPCombType.DRAGONBLOOD.getStackForType(4) },
                new FluidStack[] { Materials.DraconiumAwakened.getMolten(144 * 96) },
                null,
                new FluidStack[] { DRAGON_METAL.getFluidStack(144 * 12) },
                (int) RECIPE_UIV,
                20 * 10,
                GTCMRecipe.StellarForgeRecipes);

            addToMiracleDoorRecipes(
                new ItemStack[] { Utils.setStackSize(
                    GTModHandler.getModItem("witchery", "infinityegg", 0, GTCMItemList.TestItem0.get(1)),
                    0), GTPPCombType.DRAGONBLOOD.getStackForType(4) },
                new FluidStack[] { Materials.DraconiumAwakened.getMolten(144 * 48) },
                null,
                new FluidStack[] { DRAGON_METAL.getFluidStack(144 * 3) },
                (int) RECIPE_UIV,
                20 * 20,
                GTCMRecipe.StellarForgeRecipes);

            addToMiracleDoorRecipes(
                new ItemStack[] { new ItemStack(Blocks.dragon_egg, 1) },
                new FluidStack[] { Materials.DraconiumAwakened.getMolten(144 * 128) },
                null,
                new FluidStack[] { DRAGON_METAL.getFluidStack(144 * 8) },
                (int) RECIPE_UIV,
                20 * 60,
                GTCMRecipe.StellarForgeRecipes);

            addToMiracleDoorRecipes(
                new ItemStack[] { Utils.setStackSize(
                    GTModHandler.getModItem("witchery", "infinityegg", 0, GTCMItemList.TestItem0.get(1)),
                    0) },
                new FluidStack[] { Materials.DraconiumAwakened.getMolten(144 * 64) },
                null,
                new FluidStack[] { DRAGON_METAL.getFluidStack(144 * 2) },
                (int) RECIPE_UIV,
                20 * 40,
                GTCMRecipe.StellarForgeRecipes);

            // Star Kernel
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.dragon_egg, 1))
                .fluidOutputs(new FluidStack(DRAGON_METAL.getPlasma(), 144 * 16))
                .noOptimize()
                .specialValue(13500)
                .eut(RECIPE_MAX)
                .duration(20 * 10)
                .addTo(GTCMRecipe.BallLightningRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    Utils.setStackSize(
                        GTModHandler.getModItem("witchery", "infinityegg", 1, GTCMItemList.TestItem0.get(1)),
                        1))
                .fluidInputs(getFluidStack("fieryblood", 1000 * 16384))
                .fluidOutputs(new FluidStack(DRAGON_METAL.getPlasma(), 144 * 16384))
                .noOptimize()
                .specialValue(13500)
                .eut(RECIPE_MAX)
                .duration(20 * 10)
                .addTo(GTCMRecipe.BallLightningRecipes);
        }
    }

}
