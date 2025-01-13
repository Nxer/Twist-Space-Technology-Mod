package com.Nxer.TwistSpaceTechnology.common.material;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static goodgenerator.util.CrackRecipeAdder.reAddBlastRecipe;
import static goodgenerator.util.MaterialFix.MaterialFluidExtractionFix;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.transcendentPlasmaMixerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.Werkstoff;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;

public class MaterialFix {

    // spotless:off
    public static void loadRecipes() {
//        reAddBlastRecipe(MaterialsTST.NeutroniumAlloy, 2880, (int) RECIPE_UIV, 12500, true);
//        addMixerRecipe(
//            new ItemStack[] {
//                Materials.Neutronium.getDust(11),
//                GGMaterial.adamantiumAlloy.get(OrePrefixes.dust, 2),
//                Materials.Duranium.getDust(1),
//                Materials.Flerovium.getDust(1),
//                MaterialsElements.STANDALONE.WHITE_METAL.getDust(1),
//                GTUtility.getIntegratedCircuit(2) },
//            new FluidStack[]{ Materials.Hydrogen.getPlasma(1000 * 22)},
//            new ItemStack[] { MaterialsTST.NeutroniumAlloy.get(OrePrefixes.dust,16) },
//            16 * 20,
//            (int) RECIPE_UHV);
        addAlloySmelterRecipe(new ItemStack[]{
            GTUtility.getIntegratedCircuit(5),
            Materials.Neutronium.getDust(11),
            GGMaterial.adamantiumAlloy.get(OrePrefixes.dust, 2),
            Materials.Duranium.getDust(1),
            Materials.Flerovium.getDust(1),
            MaterialsElements.STANDALONE.WHITE_METAL.getDust(1)},
            new FluidStack[]{ Materials.Hydrogen.getPlasma(1000 * 22)},
            new FluidStack[]{MaterialsTST.NeutroniumAlloy.getMolten(16 * 144)},
            (int) RECIPE_UIV,
            660 * 20);
//        addVacuumFreezerRecipe(MaterialsTST.NeutroniumAlloy,(int)RECIPE_UEV,2304);

        reAddBlastRecipe(MaterialPool.AxonisAlloy, 720, (int) RECIPE_UMV, 13200, true);
        MaterialFluidExtractionFix(MaterialPool.AxonisAlloy);
        addAlloySmelterRecipe(
            new ItemStack[] {
                MaterialsElements.STANDALONE.DRAGON_METAL.getDust(5),
                MaterialsElements.STANDALONE.HYPOGEN.getDust(3),
                MaterialsUEVplus.Creon.getDust(2),
                Materials.Ichorium.getDust(1),
                Materials.Terbium.getDust(1),
                GGMaterial.shirabon.get(OrePrefixes.dust,1),
                GTUtility.getIntegratedCircuit(6) },
            new FluidStack[]{ MaterialPool.AxonisAlloy.getMolten(144 * 12)},
            720 * 20,
            (int) RECIPE_UMV);
        addPlasmaMixerRecipe(
            GTUtility.getIntegratedCircuit(20),
            new FluidStack[] {
                new FluidStack( MaterialsElements.STANDALONE.DRAGON_METAL.getPlasma(),5000),
                new FluidStack( MaterialsElements.STANDALONE.HYPOGEN.getPlasma(),3000),
                MaterialsUEVplus.Creon.getPlasma(2000),
                Materials.Ichorium.getPlasma(1000),
                Materials.Terbium.getPlasma(1000),
                GGMaterial.shirabon.getMolten(1000),
                MaterialsUEVplus.PrimordialMatter.getFluid(1000)},
            MaterialPool.AxonisAlloy.getMolten(120000),
            720 * 20,
            (int) RECIPE_UMV);
    }

    // spotless:on
    public static void addAlloySmelterRecipe(ItemStack[] itemIn, FluidStack[] fluidIn, ItemStack[] itemOut,
        FluidStack[] fluidOut, int eut, int ticks) {
        GTValues.RA.stdBuilder()
            .itemInputs(itemIn)
            .fluidInputs(fluidIn)
            .itemOutputs(itemOut)
            .fluidOutputs(fluidOut)
            .eut(eut)
            .duration(ticks)
            .addTo(alloyBlastSmelterRecipes);
    }

    public static void addAlloySmelterRecipe(ItemStack[] itemIn, FluidStack[] fluidIn, FluidStack[] fluidOut, int eut,
        int ticks) {
        addAlloySmelterRecipe(itemIn, fluidIn, new ItemStack[] {}, fluidOut, eut, ticks);
    }

    public static void addAlloySmelterRecipe(ItemStack[] itemIn, FluidStack[] fluidOut, int eut, int ticks) {
        addAlloySmelterRecipe(itemIn, new FluidStack[] {}, new ItemStack[] {}, fluidOut, eut, ticks);
    }

    public static void addVacuumFreezerRecipe(Werkstoff aMaterial, FluidStack[] fluidIn, FluidStack[] fluidOut, int eut,
        int ticks) {
        GTValues.RA.stdBuilder()
            .itemInputs(aMaterial.get(OrePrefixes.ingotHot, 1))
            .fluidInputs(fluidIn)
            .itemOutputs(aMaterial.get(OrePrefixes.ingot, 1))
            .fluidOutputs(fluidOut)
            .eut(eut)
            .duration(ticks)
            .addTo(vacuumFreezerRecipes);
    }

    public static void addVacuumFreezerRecipe(Werkstoff aMaterial, int eut, int ticks) {
        addVacuumFreezerRecipe(aMaterial, new FluidStack[] {}, new FluidStack[] {}, eut, ticks);
    }

    public static void addMixerRecipe(ItemStack[] itemIn, ItemStack[] itemOut, int EUt, int duration) {
        addMixerRecipe(itemIn, new FluidStack[] {}, itemOut, EUt, duration);
    }

    public static void addMixerRecipe(ItemStack[] itemIn, FluidStack[] fluidIn, ItemStack[] itemOut, int EUt,
        int duration) {
        GTValues.RA.stdBuilder()
            .itemInputs(itemIn)
            .fluidInputs(fluidIn)
            .itemOutputs(itemOut)
            .eut(EUt)
            .duration(duration)
            .addTo(mixerRecipes);
    }

    public static void addPlasmaMixerRecipe(ItemStack circuit, FluidStack[] fluidIn, FluidStack fluidOut, int EUt,
        int duration) {
        GTValues.RA.stdBuilder()
            .itemInputs(circuit)
            .fluidInputs(fluidIn)
            .fluidOutputs(fluidOut)
            .eut(EUt)
            .duration(duration)
            .addTo(transcendentPlasmaMixerRecipes);
    }

    public static void addFusionRecipe(FluidStack[] fluidIn, FluidStack fluidOut, int EUt, int duration,
        int recipeTier) {
        GTValues.RA.stdBuilder()
            .fluidInputs(fluidIn)
            .fluidOutputs(fluidOut)
            .eut(EUt)
            .duration(duration)
            .specialValue(recipeTier)
            .addTo(fusionRecipes);
    }

    public void run() {

    }
}
