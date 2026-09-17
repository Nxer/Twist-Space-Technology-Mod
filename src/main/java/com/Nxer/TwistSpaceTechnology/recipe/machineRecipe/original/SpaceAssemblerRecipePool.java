package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.glodblock.github.loader.ItemAndBlockHolder;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialMisc;
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
                    Materials.SpaceTime.getMolten(144 * 8),
                    Materials.UUMatter.getFluid(1000 * 16))
                .itemOutputs(GTUtility.copyAmountUnsafe(1024, ItemRefer.Advanced_Radiation_Protection_Plate.get(1)))
                .metadata(IGRecipeMaps.MODULE_TIER, 2)
                .eut(RECIPE_UIV)
                .duration(20 * 10)
                .addTo(SA);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Multi_2x2_UIV.get(4),
                ItemList.Hatch_Input_ME_Advanced.get(2),
                new ItemStack(ItemAndBlockHolder.INTERFACE),
                GTCMItemList.PerfectEngravedEnergyChip.get(1),
                ItemList.Electric_Pump_UEV.get(1),
                ModItem.getModItem(Mods.AE2FluidCraft.ID, ItemAndBlockHolder.SINGULARITY_CELL == null ? null
                    : new ItemStack(ItemAndBlockHolder.SINGULARITY_CELL), "Fluid Storage Singularity", 1))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(64 * 144),
                MaterialsTST.NeutroniumAlloy.getMolten(32 * 144),
                Materials.DimensionallyShiftedSuperfluid.getFluid(8000))
            .itemOutputs(GTCMItemList.AEStorageCellInputHatch.get(1))
            .metadata(IGRecipeMaps.MODULE_TIER, 2)
            .eut(RECIPE_UEV)
            .duration(20 * 30)
            .addTo(SA);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_MAX.get(16),
                ItemList.Hatch_Input_Bus_ME_Advanced.get(2),
                ModItem.getModItem("appliedenergistics2", "tile.BlockInterface", 1, 0),
                GTCMItemList.PerfectEngravedEnergyChip.get(1),
                ItemList.Conveyor_Module_UEV.get(1),
                ModItem.getModItem("appliedenergistics2", "item.ItemExtremeStorageCell.Singularity", 1, 0))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(64 * 144),
                MaterialsTST.NeutroniumAlloy.getMolten(32 * 144),
                Materials.DimensionallyShiftedSuperfluid.getFluid(8000))
            .itemOutputs(GTCMItemList.AEStorageCellInputBus.get(1))
            .metadata(IGRecipeMaps.MODULE_TIER, 2)
            .eut(RECIPE_UEV)
            .duration(20 * 30)
            .addTo(SA);

    }
}
