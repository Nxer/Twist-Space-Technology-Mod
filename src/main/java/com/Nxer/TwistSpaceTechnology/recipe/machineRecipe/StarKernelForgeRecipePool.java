package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.material.ELEMENT;

public class StarKernelForgeRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        loadGeneralRecipes();
        loadManualRecipes();
    }

    private void addRecipe(ItemStack[] itemInputs, FluidStack[] fluidInputs, FluidStack[] fluidOutputs, int eut,
        int duration) {
        TST_RecipeBuilder.builder()
            .itemInputs(itemInputs)
            .fluidInputs(fluidInputs)
            .fluidOutputs(fluidOutputs)
            .specialValue(13500)
            .eut(eut)
            .duration(duration)
            .addTo(GTCMRecipe.BallLightningRecipes);
    }

    private void addRecipe(FluidStack[] fluidInputs, FluidStack[] fluidOutputs, int eut, int duration) {
        addRecipe(null, fluidInputs, fluidOutputs, eut, duration);
    }

    private void addRecipe(FluidStack fluidInputs, FluidStack fluidOutputs, int eut) {
        addRecipe(new FluidStack[] { fluidInputs }, new FluidStack[] { fluidOutputs }, eut, 50);
    }

    private void addRecipe(ItemStack itemInputs, FluidStack fluidOutputs, int eut) {
        addRecipe(new ItemStack[] { itemInputs }, null, new FluidStack[] { fluidOutputs }, eut, 50);
    }

    private void loadGeneralRecipes() {
        for (GT_Recipe plasmaFuel : RecipeMaps.plasmaFuels.getAllRecipes()) {
            FluidStack plasma = GT_Utility.getFluidForFilledItem(plasmaFuel.mInputs[0], true);
            if (plasma == null) {
                continue;
            }
            // EU/L
            int EUL = plasmaFuel.mSpecialValue;
            String plasmaName = FluidRegistry.getFluidName(plasma);
            String[] plasmaNameSplit = plasmaName.split("\\.", 2);
            if (plasmaNameSplit.length != 2) continue;
            String liquidName = plasmaNameSplit[1];
            plasma.amount = 1000;
            // Try finding cooled liquid
            FluidStack liquid = FluidRegistry.getFluidStack(liquidName, plasma.amount);
            if (liquid == null) liquid = FluidRegistry.getFluidStack("molten." + liquidName, plasma.amount);
            if (liquid == null) {
                switch (liquidName) {
                    case "helium_3" -> liquid = Materials.Helium_3.getGas(plasma.amount);
                    case "sodium" -> liquid = Materials.Sodium.getFluid(plasma.amount);
                }
            }

            if (liquid == null) continue; // turn to manually process

            addRecipe(liquid, plasma, EUL * 1000);

        }
    }

    public void loadManualRecipes() {

        // Neon
        addRecipe(
            WerkstoffLoader.Neon.getFluidOrGas(1000),
            new FluidStack(ELEMENT.getInstance().NEON.getPlasma(), 1000),
            10_000_000);

        // Krypton
        addRecipe(
            WerkstoffLoader.Krypton.getFluidOrGas(1000),
            new FluidStack(ELEMENT.getInstance().KRYPTON.getPlasma(), 1000),
            30_000_000);

        // Xenon
        addRecipe(
            WerkstoffLoader.Xenon.getFluidOrGas(1000),
            new FluidStack(ELEMENT.getInstance().XENON.getPlasma(), 1000),
            90_000_000);

        // Technetium
        addRecipe(
            new FluidStack(ELEMENT.getInstance().TECHNETIUM.getFluid(), 1000),
            new FluidStack(ELEMENT.getInstance().TECHNETIUM.getPlasma(), 1000),
            100_000_000);

        // Bromine
        addRecipe(
            new FluidStack(ELEMENT.getInstance().BROMINE.getFluid(), 1000),
            new FluidStack(ELEMENT.getInstance().BROMINE.getPlasma(), 1000),
            100_000_000);

        // Tritanium
        addRecipe(Materials.Tritanium.getMolten(1000), Materials.Tritanium.getPlasma(1000), 100_000_000);

        // Carbon
        addRecipe(
            Materials.Carbon.getDust(1),
            Materials.Carbon.getPlasma(144),
            GT_Utility.getPlasmaFuelValueInEUPerLiterFromMaterial(Materials.Carbon) * 144);

        // Sulfur
        addRecipe(
            Materials.Sulfur.getDust(1),
            Materials.Sulfur.getPlasma(144),
            GT_Utility.getPlasmaFuelValueInEUPerLiterFromMaterial(Materials.Sulfur) * 144);

        // Phosphorus
        addRecipe(
            Materials.Phosphorus.getDust(1),
            Materials.Phosphorus.getPlasma(144),
            GT_Utility.getPlasmaFuelValueInEUPerLiterFromMaterial(Materials.Phosphorus) * 144);

        // Strontium
        addRecipe(
            Materials.Strontium.getDust(1),
            Materials.Strontium.getPlasma(144),
            GT_Utility.getPlasmaFuelValueInEUPerLiterFromMaterial(Materials.Strontium) * 144);

        // Cadmium
        addRecipe(
            Materials.Cadmium.getDust(1),
            Materials.Cadmium.getPlasma(144),
            GT_Utility.getPlasmaFuelValueInEUPerLiterFromMaterial(Materials.Cadmium) * 144);

    }

}
