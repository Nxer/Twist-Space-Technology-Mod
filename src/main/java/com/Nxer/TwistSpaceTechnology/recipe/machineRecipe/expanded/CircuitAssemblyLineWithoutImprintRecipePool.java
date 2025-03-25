package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipePool.circuitItemsToWrapped;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTUtility.copyAmount;
import static gregtech.api.util.GTUtility.copyAmountUnsafe;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import bartworks.util.BWUtil;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;

public class CircuitAssemblyLineWithoutImprintRecipePool {

    public static void loadRecipes() {
        TST_ItemID IC2_Circuit = TST_ItemID
            .create(GTModHandler.getModItem(Mods.IndustrialCraft2.ID, "itemPartCircuit", 1));
        TST_ItemID IC2_AdvCircuit = TST_ItemID
            .create(GTModHandler.getModItem(Mods.IndustrialCraft2.ID, "itemPartCircuitAdv", 1));
        FluidStack SolderingAlloy = Materials.SolderingAlloy.getMolten(0);
        FluidStack INDALLOY_140 = MaterialsAlloy.INDALLOY_140.getFluidStack(0);
        FluidStack MUTATED_LIVING_SOLDER = MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(0);

        for (GTRecipe originalRecipe : circuitAssemblerRecipes.getAllRecipes()) {
            if (originalRecipe == null) continue;
            ItemStack output = originalRecipe.mOutputs[0];
            // skip IC2 circuit
            if (IC2_Circuit.equalItemStack(output) || IC2_AdvCircuit.equalItemStack(output)) continue;

            // check fluid
            if (originalRecipe.mFluidInputs == null || originalRecipe.mFluidInputs.length < 1) continue;
            if (!originalRecipe.mFluidInputs[0].isFluidEqual(SolderingAlloy)
                && !originalRecipe.mFluidInputs[0].isFluidEqual(INDALLOY_140)
                && !originalRecipe.mFluidInputs[0].isFluidEqual(MUTATED_LIVING_SOLDER)) continue;

            // check output item whether is a circuit
            String unlocalizedName = output.getUnlocalizedName();
            if (!isCircuitOreDict(output) && !unlocalizedName.contains("Circuit")
                && !unlocalizedName.contains("circuit")) continue;

            GTRecipeBuilder.builder()
                .itemInputs(ModifyInput(originalRecipe.mInputs.clone()))
                .fluidInputs(originalRecipe.mFluidInputs)
                .itemOutputs(copyAmountUnsafe(originalRecipe.mOutputs[0].stackSize * 16, originalRecipe.mOutputs[0]))
                .eut(originalRecipe.mEUt)
                .duration(originalRecipe.mDuration * 12)
                .addTo(GTCMRecipe.advCircuitAssemblyLineRecipes);
        }

    }

    // Modify the circuit part to warp, others turn 16 times.
    public static ItemStack[] ModifyInput(ItemStack[] input) {
        if (!(input != null && input.length > 0)) return new ItemStack[0];
        ArrayList<ItemStack> inputItems = new ArrayList<>();

        for (ItemStack aStack : input) {
            boolean isItemModified = false;
            // Modify circuit part
            for (Map.Entry<ItemStack, ItemStack> entry : circuitItemsToWrapped.entrySet()) {
                if (GTUtility.areStacksEqual(entry.getKey(), aStack)) {
                    // not modify to wrap circuit
                    if (Item.itemRegistry.getNameForObject(
                        entry.getValue()
                            .getItem())
                        .contains(Mods.GoodGenerator.ID)) break;
                    inputItems.add(copyAmount(aStack.stackSize, entry.getValue()));
                    isItemModified = true;
                    break;
                }
            }

            // Modify wire
            if (!isItemModified && BWUtil.checkStackAndPrefix(aStack)) {
                ItemData Data = Objects.requireNonNull(GTOreDictUnificator.getAssociation(aStack));
                Materials Material = Data.mMaterial.mMaterial;
                OrePrefixes OreDict = Data.mPrefix;
                if (OreDict == OrePrefixes.wireGt01) {
                    ItemStack newStack = GTOreDictUnificator.get(OrePrefixes.wireGt16, Material, aStack.stackSize);
                    if (newStack != null) {
                        inputItems.add(newStack);
                        isItemModified = true;
                    }
                } else if (OreDict == OrePrefixes.wireFine) {
                    ItemStack newStack = GTOreDictUnificator.get(OrePrefixes.wireGt04, Material, aStack.stackSize);
                    if (newStack != null) {
                        inputItems.add(newStack);
                        isItemModified = true;
                    }
                }
            }

            if (!isItemModified) inputItems.add(copyAmount(aStack.stackSize * 16, aStack));
        }

        return inputItems.toArray(new ItemStack[0]);

    }

    public static boolean isCircuitOreDict(ItemStack item) {
        return BWUtil.isTieredCircuit(item) || BWUtil.getOreNames(item)
            .stream()
            .anyMatch("circuitPrimitiveArray"::equals);
    }
}
