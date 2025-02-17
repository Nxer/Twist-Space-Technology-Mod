package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipePool.circuitItemsToWrapped;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTUtility.areStacksEqual;
import static gregtech.api.util.GTUtility.copyAmount;
import static gregtech.api.util.GTUtility.copyAmountUnsafe;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import bartworks.system.material.CircuitGeneration.BWMetaItems;
import bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
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

public class CircuitAssemblyLineWithoutImprintRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        for (GTRecipe originalRecipe : circuitAssemblerRecipes.getAllRecipes()) {
            if (originalRecipe == null) continue;
            ItemStack output = originalRecipe.mOutputs[0];
            boolean isOrePass = isCircuitOreDict(output);
            String unlocalizedName = output.getUnlocalizedName();
            if (areStacksEqual(output, GTModHandler.getModItem(Mods.IndustrialCraft2.ID, "itemPartCircuit", 1))
                || areStacksEqual(output, GTModHandler.getModItem(Mods.IndustrialCraft2.ID, "itemPartCircuitAdv", 1)))
                continue;
            if (isOrePass || unlocalizedName.contains("Circuit") || unlocalizedName.contains("circuit")) {
                if (originalRecipe.mFluidInputs[0].isFluidEqual(Materials.SolderingAlloy.getMolten(0))
                    || originalRecipe.mFluidInputs[0].isFluidEqual(MaterialsAlloy.INDALLOY_140.getFluidStack(0))
                    || originalRecipe.mFluidInputs[0]
                        .isFluidEqual(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(0))) {

                    GTRecipeBuilder.builder()
                        .itemInputs(ModifyInput(originalRecipe.mInputs))
                        .fluidInputs(originalRecipe.mFluidInputs)
                        .itemOutputs(
                            copyAmountUnsafe(originalRecipe.mOutputs[0].stackSize * 16, originalRecipe.mOutputs[0]))
                        .special(
                            BWMetaItems.getCircuitParts()
                                .getStackWithNBT(
                                    CircuitImprintLoader.getTagFromStack(originalRecipe.mOutputs[0]),
                                    0,
                                    0))
                        .eut(originalRecipe.mEUt)
                        .duration(originalRecipe.mDuration * 12)
                        .addTo(GTCMRecipe.advCircuitAssemblyLineRecipes);
                }
            }

        }

    }

    // Modify the circuit part to warp, others turn 16 times.
    public ItemStack[] ModifyInput(ItemStack[] input) {
        if (!(input != null && input.length > 0)) return new ItemStack[0];
        ArrayList<ItemStack> inputItems = new ArrayList<>();

        for (ItemStack aStack : input) {
            boolean isItemModified = false;
            // Modify circuit part
            for (Map.Entry<ItemStack, ItemStack> entry : circuitItemsToWrapped.entrySet()) {
                if (GTUtility.areStacksEqual(entry.getKey(), aStack)) {
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
