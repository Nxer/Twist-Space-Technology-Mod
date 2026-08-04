package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addIntegratedCircuitToRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addRecipeMT;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.convertCircuitRecipeItems;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.hasCircuitOreDict;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.packageCircuitRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.reduplicateRecipe;
import static com.Nxer.TwistSpaceTechnology.util.TstUtils.removeIntegratedCircuitFromStacks;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTRecipe;

public final class CircuitAssemblerRecipeGenerator {

    private CircuitAssemblerRecipeGenerator() {}

    public static void load() {
        HashSet<TST_ItemID> IgnoreRecipeOutputs = new HashSet<>();
        // spotless:off
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 220)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 461)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 462)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 463)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 466)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 467)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 468)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 470)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 472)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("ae2fc", "part_fluid_storage_bus", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsAstroMiner", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsMoonBuggy", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsCargoRocket", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsTier1", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsTier2", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsTier3", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsTier4", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsTier5", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsTier6", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsTier7", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("dreamcraft", "SchematicsTier8", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(tectech.thing.CustomItemList.parametrizerMemory.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Board_Wetware.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Board_Bio.get(1)));
        // spotless:on

        for (GTRecipe originalRecipe : circuitAssemblerRecipes.getAllRecipes()) {
            if (IgnoreRecipeOutputs.contains(TST_ItemID.createNoNBT(originalRecipe.mOutputs[0]))) continue;
            // Skip recipes from unsupported mods.
            String itemName = Item.itemRegistry.getNameForObject(originalRecipe.mOutputs[0].getItem());
            if (itemName == null || itemName.contains(Mods.Railcraft.ID)
                || itemName.contains(Mods.Forestry.ID)
                || itemName.contains(Mods.StevesCarts2.ID)
                || itemName.contains(Mods.ProjectRedCore.ID)
                || itemName.contains(Mods.ProjectRedIllumination.ID)
                || itemName.contains(Mods.ProjectRedIntegration.ID)
                || itemName.contains(Mods.ProjectRedTransportation.ID)) continue;

            // GT circuit recipes are generated from CAL instead.
            if (hasCircuitOreDict(originalRecipe.mOutputs[0])) continue;

            GTRecipe recipeCopy = new GTRecipe(
                false,
                removeIntegratedCircuitFromStacks(originalRecipe.mInputs),
                originalRecipe.mOutputs == null ? null : originalRecipe.mOutputs.clone(),
                null,
                null,
                null,
                null,
                null,
                originalRecipe.mFluidInputs == null ? null : originalRecipe.mFluidInputs.clone(),
                originalRecipe.mFluidOutputs == null ? null : originalRecipe.mFluidOutputs.clone(),
                originalRecipe.mDuration,
                originalRecipe.mEUt,
                0);
            int integratedCircuitNum = 16;
            if (originalRecipe.mInputs != null) {
                for (ItemStack itemStack : originalRecipe.mInputs) {
                    if (itemStack.getItem() == ItemList.Circuit_Integrated.getItem()) {
                        integratedCircuitNum += itemStack.getItemDamage();
                        if (integratedCircuitNum > 24) integratedCircuitNum -= 24;
                        break;
                    }
                }
            }
            FluidStack solderingFluid = getHighestTierSolderingFluid(recipeCopy);
            GTRecipe generatedRecipe = packageCircuitRecipe(removeSolderingFluids(recipeCopy));
            generatedRecipe = convertCircuitRecipeItems(generatedRecipe);
            generatedRecipe = addSolderingFluidFirst(generatedRecipe, solderingFluid);
            generatedRecipe = reduplicateRecipe(generatedRecipe, 3, 3, 4, 4, 1, 3);
            addRecipeMT(addIntegratedCircuitToRecipe(generatedRecipe, integratedCircuitNum));
        }

    }

    // Get the highest-tier soldering fluid without changing its amount.
    private static FluidStack getHighestTierSolderingFluid(GTRecipe recipe) {
        FluidStack solderingFluid = null;
        int solderRank = 0;

        if (recipe.mFluidInputs != null) {
            for (FluidStack stack : recipe.mFluidInputs) {
                int rank = stack.isFluidEqual(Materials.Lead.getMolten(1)) ? 1
                    : stack.isFluidEqual(Materials.Tin.getMolten(1)) ? 2
                        : stack.isFluidEqual(Materials.SolderingAlloy.getMolten(1)) ? 3 : 0;
                if (rank > solderRank) {
                    solderingFluid = stack.copy();
                    solderRank = rank;
                }
            }
        }

        return solderingFluid;
    }

    // Remove lead, tin and soldering alloy from the fluid inputs.
    private static GTRecipe removeSolderingFluids(GTRecipe recipe) {
        ArrayList<FluidStack> fluidInputs = new ArrayList<>();
        if (recipe.mFluidInputs != null) {
            for (FluidStack stack : recipe.mFluidInputs) {
                if (stack.isFluidEqual(Materials.Lead.getMolten(1)) || stack.isFluidEqual(Materials.Tin.getMolten(1))
                    || stack.isFluidEqual(Materials.SolderingAlloy.getMolten(1))) continue;
                fluidInputs.add(stack.copy());
            }
        }

        return new GTRecipe(
            false,
            recipe.mInputs == null ? null : recipe.mInputs.clone(),
            recipe.mOutputs == null ? null : recipe.mOutputs.clone(),
            null,
            null,
            null,
            null,
            null,
            fluidInputs.toArray(new FluidStack[0]),
            recipe.mFluidOutputs == null ? null : recipe.mFluidOutputs.clone(),
            recipe.mDuration,
            recipe.mEUt,
            0);
    }

    // Add the selected soldering fluid as the first fluid input.
    private static GTRecipe addSolderingFluidFirst(GTRecipe recipe, FluidStack solderingFluid) {
        if (solderingFluid == null) return recipe;
        ArrayList<FluidStack> fluidInputs = new ArrayList<>();
        fluidInputs.add(solderingFluid.copy());
        if (recipe.mFluidInputs != null) Collections.addAll(fluidInputs, recipe.mFluidInputs);

        return new GTRecipe(
            false,
            recipe.mInputs == null ? null : recipe.mInputs.clone(),
            recipe.mOutputs == null ? null : recipe.mOutputs.clone(),
            null,
            null,
            null,
            null,
            null,
            fluidInputs.toArray(new FluidStack[0]),
            recipe.mFluidOutputs == null ? null : recipe.mFluidOutputs.clone(),
            recipe.mDuration,
            recipe.mEUt,
            0);
    }
}
