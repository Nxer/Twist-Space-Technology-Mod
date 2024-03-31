package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.metaItemEqual;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.material.ELEMENT;

public class StarKernelForgeRecipePool implements IRecipePool {

    public void SpawnRecipes() {

        for (GT_Recipe plasmaFuel : RecipeMaps.plasmaFuels.getAllRecipes()) {
            FluidStack PlasmaOutput = GT_Utility.getFluidForFilledItem(plasmaFuel.mInputs[0], true);
            if (PlasmaOutput == null) continue;
            int EuCost = plasmaFuel.mSpecialValue * 1000;
            int PlasmaInputAmount = getFluidAmount(getcellFluids(plasmaFuel.mInputs[0]));
            if (PlasmaInputAmount == 144) PlasmaOutput.amount = 144;
            if (PlasmaInputAmount == 1000) PlasmaOutput.amount = 1000;

            String PlasmaOutputName = FluidRegistry.getFluidName(PlasmaOutput);

            if (PlasmaOutputName.split("\\.", 2).length == 2) {
                String InputName = PlasmaOutputName.split("\\.", 2)[1];
                FluidStack InputFluid = FluidRegistry.getFluidStack(InputName, PlasmaOutput.amount);
                ItemStack InputItem = null;
                if (InputFluid == null)
                    InputFluid = FluidRegistry.getFluidStack("molten." + InputName, PlasmaOutput.amount);
                if (InputFluid == null) {
                    if (Objects.equals(InputName, "helium_3"))
                        InputFluid = Materials.Helium_3.getGas(PlasmaOutput.amount);
                    if (Objects.equals(InputName, "sodium"))
                        InputFluid = Materials.Sodium.getFluid(PlasmaOutput.amount);
                    if (Objects.equals(InputName, "carbon")) InputItem = Materials.Carbon.getDust(1);
                    if (Objects.equals(InputName, "sulfur")) InputItem = Materials.Sulfur.getDust(1);
                    if (Objects.equals(InputName, "phosphorus")) InputItem = Materials.Phosphorus.getDust(1);
                    if (Objects.equals(InputName, "strontium")) InputItem = Materials.Strontium.getDust(1);
                    if (Objects.equals(InputName, "cadmium")) InputItem = Materials.Cadmium.getDust(1);
                }
                if (InputFluid != null || InputItem != null) {
                    addToRecipes(InputItem, InputFluid, PlasmaOutput, EuCost, getDuration(EuCost));
                }
            }

        }

    }

    public FluidStack getcellFluids(ItemStack Cell) {
        FluidStack out = null;
        for (GT_Recipe recipe : RecipeMaps.fluidCannerRecipes.getAllRecipes()) {
            if (metaItemEqual(Cell, recipe.mInputs[0])) {
                if (recipe.mFluidOutputs[0] != null) {
                    out = recipe.mFluidOutputs[0].copy();
                    if (getFluidAmount(recipe.mFluidOutputs[0]) == 144) out.amount = 144;
                    if (getFluidAmount(recipe.mFluidOutputs[0]) == 1000) out.amount = 1000;
                }
                break;
            }
        }
        return out;
    }

    public static int getFluidAmount(FluidStack fluid) {
        return fluid.amount;
    };

    public static int getDuration(int eut) {
        return (int) Math.ceil(Math.log(eut) / Math.log(Math.E) * 5);
    }

    public void addToRecipes(ItemStack inputItems, FluidStack inputFluids, FluidStack outputFluids, int eut,
        int duration) {
        GT_RecipeBuilder ra = GT_Values.RA.stdBuilder();

        if (inputItems != null) {
            ra.itemInputs(inputItems);
        }

        if (inputFluids != null) {
            ra.fluidInputs(inputFluids);
        }

        if (outputFluids != null) {
            ra.fluidOutputs(outputFluids);
        }

        ra.eut(eut)
            .duration(duration)
            .specialValue(13500)
            .addTo(GTCMRecipe.BallLightningRecipes);
    }

    public static Collection<GT_Recipe> StarKernelForgeRecipeListCache;

    private void cacheRecipeList() {
        StarKernelForgeRecipeListCache = new HashSet<>(GTCMRecipe.BallLightningRecipes.getAllRecipes());
    }

    private void loadRecipeListCache() {
        for (GT_Recipe recipe : StarKernelForgeRecipeListCache) {
            GTCMRecipe.BallLightningRecipes.addRecipe(recipe);
        }
    }

    public void loadManualRecipes() {

        TST_RecipeBuilder bd = TST_RecipeBuilder.builder()
            .fluidInputs(WerkstoffLoader.Neon.getFluidOrGas(1000));
        bd.fluidOutputs(new FluidStack(ELEMENT.getInstance().NEON.getPlasma(), 1000));
        bd.specialValue(13500);
        bd.eut((int) (1024000L * 20))
            .duration(getDuration((int) (1024000L * 20)))
            .addTo(GTCMRecipe.BallLightningRecipes);

        bd = TST_RecipeBuilder.builder()
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(1000));
        bd.fluidOutputs(new FluidStack(ELEMENT.getInstance().KRYPTON.getPlasma(), 1000));
        bd.specialValue(13500);
        bd.eut((int) (1024000L * 36))
            .duration(getDuration((int) (1024000L * 36)))
            .addTo(GTCMRecipe.BallLightningRecipes);

        bd = TST_RecipeBuilder.builder()
            .fluidInputs(WerkstoffLoader.Xenon.getFluidOrGas(1000));
        bd.fluidOutputs(new FluidStack(ELEMENT.getInstance().XENON.getPlasma(), 1000));
        bd.specialValue(13500);
        bd.eut((int) (1024000L * 131))
            .duration(getDuration((int) (1024000L * 131)))
            .addTo(GTCMRecipe.BallLightningRecipes);

        bd = TST_RecipeBuilder.builder()
            .fluidInputs(new FluidStack(ELEMENT.getInstance().TECHNETIUM.getFluid(), 1000));
        bd.fluidOutputs(new FluidStack(ELEMENT.getInstance().TECHNETIUM.getPlasma(), 1000));
        bd.specialValue(13500);
        bd.eut((int) (1024000L * 98))
            .duration(getDuration((int) (1024000L * 98)))
            .addTo(GTCMRecipe.BallLightningRecipes);

        bd = TST_RecipeBuilder.builder()
            .fluidInputs(new FluidStack(ELEMENT.getInstance().BROMINE.getFluid(), 1000));
        bd.fluidOutputs(new FluidStack(ELEMENT.getInstance().BROMINE.getPlasma(), 1000));
        bd.specialValue(13500);
        bd.eut((int) (1024000L * 80))
            .duration(getDuration((int) (1024000L * 80)))
            .addTo(GTCMRecipe.BallLightningRecipes);

        bd = TST_RecipeBuilder.builder()
            .fluidInputs(Materials.Tritanium.getMolten(144));
        bd.fluidOutputs(Materials.Tritanium.getPlasma(144));
        bd.specialValue(13500);
        bd.eut((int) 1024000L * 727)
            .duration(getDuration((int) 1024000L * 727))
            .addTo(GTCMRecipe.BallLightningRecipes);
    }

    @Override
    public void loadRecipes() {
        SpawnRecipes();
        loadManualRecipes();
        cacheRecipeList();
    }

    public void loadOnServerStarted() {
        SpawnRecipes();
        loadRecipeListCache();
    }
}
