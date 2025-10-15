package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.enums.TierEU.RECIPE_UIV;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.ModItems;
import gtnhintergalactic.recipe.IGRecipeMaps;

public class SpaceAssemblerRecipePool {

    public static void loadRecipes() {

        final RecipeMap<?> SA = IGRecipeMaps.spaceAssemblerRecipes;

        final Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        {
            // adv radiation proof plate
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(24),
                    Materials.Neutronium.getNanite(1),
                    new ItemStack(ModItems.itemStandarParticleBase, 3),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.ElectrumFlux, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64))
                .fluidInputs(
                    new FluidStack(solderUEV, 144 * 1024),
                    Materials.Lead.getMolten(144 * 16 * 1024),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 8),
                    Materials.UUMatter.getFluid(1000 * 16))
                .itemOutputs(GTUtility.copyAmountUnsafe(1024, ItemRefer.Advanced_Radiation_Protection_Plate.get(1)))
                .metadata(IGRecipeMaps.MODULE_TIER, 2)
                .eut(RECIPE_UIV)
                .duration(20 * 10)
                .addTo(SA);
        }

    }
}
