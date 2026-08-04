package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addIntegratedCircuitToRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addRecipeMT;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.convertCircuitRecipeItems;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.packageCircuitRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.reduplicateRecipe;
import static gregtech.api.util.GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;

public final class AssemblyLineRecipeGenerator {

    private AssemblyLineRecipeGenerator() {}

    public static void load() {
        HashSet<TST_ItemID> GenerateRecipeOutputs = new HashSet<>();

        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Chip_NeuroCPU.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Chip_BioCPU.get(1)));

        HashSet<TST_ItemID> AdvanceCircuitPart = new HashSet<>();
        Collections.addAll(
            AdvanceCircuitPart,
            TST_ItemID.create(
                ItemList.Circuit_Parts_ResistorASMD.get(1),
                ItemList.Circuit_Parts_DiodeASMD.get(1),
                ItemList.Circuit_Parts_TransistorASMD.get(1),
                ItemList.Circuit_Parts_CapacitorASMD.get(1),
                ItemList.Circuit_Parts_InductorASMD.get(1)));
        HashSet<TST_ItemID> OpticalCircuitPart = new HashSet<>();
        Collections.addAll(
            OpticalCircuitPart,
            TST_ItemID.create(
                ItemList.Circuit_Parts_ResistorXSMD.get(1),
                ItemList.Circuit_Parts_DiodeXSMD.get(1),
                ItemList.Circuit_Parts_TransistorXSMD.get(1),
                ItemList.Circuit_Parts_CapacitorXSMD.get(1),
                ItemList.Circuit_Parts_InductorXSMD.get(1)));

        for (var aRecipe : sAssemblylineRecipes) {
            if (GenerateRecipeOutputs.contains(TST_ItemID.createNoNBT(aRecipe.mOutput))) {
                List<GTRecipe> generatedRecipes = new ArrayList<>();
                if (aRecipe.mOreDictAlt != null && aRecipe.mOreDictAlt.length > 0) {
                    List<List<ItemStack>> choiceList = new ArrayList<>();

                    for (int i = 0; i < aRecipe.mInputs.length; i++) {
                        boolean hasCircuit = false;

                        // Check whether this alternative input is an ore-dict circuit.
                        if (i < aRecipe.mOreDictAlt.length && aRecipe.mOreDictAlt[i] != null) {
                            for (ItemStack stack : aRecipe.mOreDictAlt[i]) {

                                ItemData stackData = GTOreDictUnificator.getAssociation(stack);
                                if (stackData == null) break;
                                OrePrefixes prefix = stackData.mPrefix;
                                if (prefix == OrePrefixes.circuit) {
                                    hasCircuit = true;
                                    break;
                                }
                            }
                        }

                        if (hasCircuit) {
                            // Keep tiered circuits in ore-dict form so they can be wrapped later.
                            ItemStack circuitStack = aRecipe.mOreDictAlt[i][0];
                            choiceList.add(
                                Collections.singletonList(
                                    GTOreDictUnificator.get(
                                        OrePrefixes.circuit,
                                        Objects.requireNonNull(
                                            GTOreDictUnificator.getAssociation(circuitStack)).mMaterial.mMaterial,
                                        circuitStack.stackSize)));
                        } else if (i < aRecipe.mOreDictAlt.length && aRecipe.mOreDictAlt[i] != null) {
                            // Keep every normal alternative input.
                            choiceList.add(Arrays.asList(aRecipe.mOreDictAlt[i]));
                        } else {
                            // Use the fixed input when no alternatives exist.
                            choiceList.add(Collections.singletonList(aRecipe.mInputs[i]));
                        }
                    }

                    List<ItemStack[]> validRecipes = new ArrayList<>();
                    int totalSlots = choiceList.size();
                    int[] indexArray = new int[totalSlots];

                    // Build every valid input combination.
                    while (true) {
                        List<ItemStack> currentCombination = new ArrayList<>();
                        boolean hasAdvanced = false, hasOptical = false;
                        boolean illegalRubber = false;
                        Materials usedMaterial = null;

                        for (int i = 0; i < totalSlots; i++) {
                            ItemStack aChoice = choiceList.get(i)
                                .get(indexArray[i]);
                            currentCombination.add(aChoice);

                            // Do not mix silicone rubber and SBR in one recipe.
                            ItemData stackData = GTOreDictUnificator.getAssociation(aChoice);
                            if (stackData != null) {
                                Materials material = stackData.mMaterial.mMaterial;

                                if (material == Materials.StyreneButadieneRubber
                                    || material == Materials.RubberSilicone) {
                                    if (usedMaterial == null) {
                                        usedMaterial = material;
                                    } else if (usedMaterial != material) {
                                        illegalRubber = true;
                                    }
                                }

                            }

                            // Do not mix advanced and optical SMD parts in one assembly-line recipe.
                            if (AdvanceCircuitPart.contains(TST_ItemID.create(aChoice))) hasAdvanced = true;
                            if (OpticalCircuitPart.contains(TST_ItemID.create(aChoice))) hasOptical = true;
                        }

                        if (!((hasAdvanced && hasOptical) || illegalRubber)) {
                            validRecipes.add(currentCombination.toArray(new ItemStack[0]));
                        }

                        int slot = totalSlots - 1;
                        while (slot >= 0) {
                            indexArray[slot]++;
                            if (indexArray[slot] < choiceList.get(slot)
                                .size()) break;
                            indexArray[slot] = 0;
                            slot--;
                        }
                        if (slot < 0) break;
                    }

                    for (ItemStack[] newInputs : validRecipes) {
                        generatedRecipes.add(
                            new GTRecipe(
                                false,
                                newInputs,
                                new ItemStack[] { aRecipe.mOutput },
                                null,
                                null,
                                null,
                                null,
                                null,
                                aRecipe.mFluidInputs,
                                null,
                                aRecipe.mDuration,
                                aRecipe.mEUt,
                                0));
                    }
                } else {
                    generatedRecipes.add(
                        new GTRecipe(
                            false,
                            aRecipe.mInputs,
                            new ItemStack[] { aRecipe.mOutput },
                            null,
                            null,
                            null,
                            null,
                            null,
                            aRecipe.mFluidInputs,
                            null,
                            aRecipe.mDuration,
                            aRecipe.mEUt,
                            0));
                }

                for (GTRecipe recipe : generatedRecipes) {
                    GTRecipe generatedRecipe = packageCircuitRecipe(recipe);
                    generatedRecipe = convertCircuitRecipeItems(generatedRecipe);
                    generatedRecipe = reduplicateRecipe(generatedRecipe, 3, 3, 4, 4, 1, 3);
                    addRecipeMT(addIntegratedCircuitToRecipe(generatedRecipe, 15));
                }
            }
        }
    }
}
