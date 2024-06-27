package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class SpaceAssemblerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final RecipeMap<?> SA = com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;

        final Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        {
            // adv radiation proof plate
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(24),
                    Materials.Neutronium.getNanite(1),
                    ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 0, 3),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.ElectrumFlux, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64))
                .fluidInputs(
                    new FluidStack(solderUEV, 144 * 1024),
                    Materials.Lead.getMolten(144 * 16 * 1024),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 8),
                    Materials.UUMatter.getFluid(1000 * 16))
                .itemOutputs(setStackSize(ItemRefer.Advanced_Radiation_Protection_Plate.get(1), 1024))
                .specialValue(2)
                .eut(RECIPE_UXV)
                .duration(20 * 10)
                .addTo(SA);
        }

    }
}
