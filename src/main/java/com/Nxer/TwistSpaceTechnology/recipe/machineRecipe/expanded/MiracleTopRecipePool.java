package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.util.TstUtils.removeIntegratedCircuitFromStacks;
import static com.Nxer.TwistSpaceTechnology.util.TstUtils.setStackSize;
import static com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes;
import static gregtech.api.util.GTUtility.copyAmount;
import static gregtech.api.util.GTUtility.copyAmountUnsafe;
import static gtPlusPlus.core.material.Material.mComponentMap;
import static net.minecraft.item.ItemStack.areItemStacksEqual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.dreammaster.gthandler.CustomItemList;

import bartworks.util.BWUtil;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class MiracleTopRecipePool implements IRecipePool {

    final RecipeMap<?> MT = GTCMRecipe.MiracleTopRecipes;
    public static final HashMap<ItemStack, ItemStack> circuitItemsToWrapped = new HashMap<>();
    public final HashSet<Materials> superConductorMaterialList = new HashSet<>();
    public final HashSet<OrePrefixes> targetModifyOreDict = new HashSet<>();
    public final HashMap<ItemStack, FluidStack> specialMaterialCantAutoModify = new HashMap<>();

    @Override
    public void loadRecipes() {
        TwistSpaceTechnology.LOG.info("MiracleTopRecipePool loading recipes.");
        initStatics();
        loadCircuitAssemblerRecipes();
        loadAssemblyLineRecipes();
        loadSpaceAssemblerRecipes();
        loadCustomRecipes();
    }

    private void loadCircuitAssemblerRecipes() {
        HashSet<TST_ItemID> IgnoreRecipeOutputs = new HashSet<>();

        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 220)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(GTModHandler.getModItem("ae2fc", "part_fluid_storage_bus", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsAstroMiner", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsMoonBuggy", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsCargoRocket", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsTier1", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsTier2", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsTier3", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsTier4", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsTier5", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsTier6", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsTier7", 1)));
        IgnoreRecipeOutputs
            .add(TST_ItemID.createNoNBT(GTModHandler.getModItem("dreamcraft", "item.SchematicsTier8", 1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(tectech.thing.CustomItemList.parametrizerMemory.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Board_Wetware.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Board_Bio.get(1)));

        // Exclude low-level solder recipe
        ArrayList<GTRecipe> recipeCache = new ArrayList<>();
        for (GTRecipe originalRecipe : circuitAssemblerRecipes.getAllRecipes()) {
            if (IgnoreRecipeOutputs.contains(TST_ItemID.createNoNBT(originalRecipe.mOutputs[0]))) continue;
            // Do not generate some Mod recipe
            String itemName = Item.itemRegistry.getNameForObject(originalRecipe.mOutputs[0].getItem());
            if (itemName.contains(Mods.Railcraft.ID) || itemName.contains(Mods.Forestry.ID)
                || itemName.contains(Mods.StevesCarts2.ID)
                || itemName.contains(Mods.ProjectRedCore.ID)
                || itemName.contains(Mods.ProjectRedTransportation.ID)) continue;

            boolean isRecipeAdded = false;
            for (GTRecipe cachedRecipe : recipeCache) {
                if (isRecipeInputItemSame(originalRecipe, cachedRecipe)) {
                    isRecipeAdded = true;
                    break;
                }
            }

            if (!isRecipeAdded) {
                GTRecipe recipeCopy = originalRecipe.copy();
                if (recipeCopy.mFluidInputs != null && recipeCopy.mFluidInputs.length > 0) {
                    FluidStack recipeFluid = recipeCopy.mFluidInputs[0];
                    if (recipeFluid.isFluidEqual(Materials.Lead.getMolten(1)))
                        recipeFluid = Materials.SolderingAlloy.getMolten(recipeFluid.amount / 4);
                    else if (recipeFluid.isFluidEqual(Materials.Tin.getMolten(1)))
                        recipeFluid = Materials.SolderingAlloy.getMolten(recipeFluid.amount / 2);
                    recipeCopy.mFluidInputs[0] = recipeFluid;
                }
                recipeCache.add(recipeCopy);
            }
        }

        for (GTRecipe aRecipe : recipeCache) {
            int IntegratedCircuitNum = 16;
            for (ItemStack aStack : aRecipe.mInputs) {
                if (aStack.getItem() == ItemList.Circuit_Integrated.getItem()) {
                    IntegratedCircuitNum += aStack.getItemDamage();
                    if (IntegratedCircuitNum > 24) IntegratedCircuitNum -= 24;
                    break;
                }
            }

            addRecipeMT(
                addIntegratedCircuitToRecipe(
                    reduplicateRecipe(ModifyRecipe(aRecipe, false), 3, 3, 4, 4, 1, 3),
                    IntegratedCircuitNum));
        }

    }

    private void loadAssemblyLineRecipes() {
        HashSet<TST_ItemID> GenerateRecipeOutputs = new HashSet<>();
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Wetwaremainframe.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Biowaresupercomputer.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Biomainframe.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_OpticalAssembly.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_OpticalComputer.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_OpticalMainframe.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Chip_NeuroCPU.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Chip_BioCPU.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(CustomItemList.PikoCircuit.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(CustomItemList.QuantumCircuit.get(1)));

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
                if (aRecipe.mOreDictAlt != null && aRecipe.mOreDictAlt.length > 0) {
                    List<List<ItemStack>> choiceList = new ArrayList<>();

                    for (int i = 0; i < aRecipe.mInputs.length; i++) {
                        boolean hasCircuit = false;

                        // Check if the Alt includes circuit.
                        // If stack has not itemData, it is a regular item and skip Ore Dict inspection
                        // If stack equals circuit, not process
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
                            // If is tiered circuit, add ore dict form, and it will be modified to warp next.
                            ItemStack circuitStack = aRecipe.mOreDictAlt[i][0];
                            choiceList.add(
                                Collections.singletonList(
                                    GTOreDictUnificator.get(
                                        OrePrefixes.circuit,
                                        Objects.requireNonNull(
                                            GTOreDictUnificator.getAssociation(circuitStack)).mMaterial.mMaterial,
                                        circuitStack.stackSize)));
                        } else if (i < aRecipe.mOreDictAlt.length && aRecipe.mOreDictAlt[i] != null) {
                            // Regular Alt input
                            choiceList.add(Arrays.asList(aRecipe.mOreDictAlt[i]));
                        } else {
                            // Only one input
                            choiceList.add(Collections.singletonList(aRecipe.mInputs[i]));
                        }
                    }

                    List<ItemStack[]> validRecipes = new ArrayList<>();
                    int totalSlots = choiceList.size();
                    int[] indexArray = new int[totalSlots];

                    // check for valid recipe
                    while (true) {
                        List<ItemStack> currentCombination = new ArrayList<>();
                        boolean hasAdvanced = false, hasOptical = false;
                        boolean illegalRubber = false;
                        Materials usedMaterial = null;

                        for (int i = 0; i < totalSlots; i++) {
                            ItemStack aChoice = choiceList.get(i)
                                .get(indexArray[i]);
                            currentCombination.add(aChoice);

                            // Check rubber for same (Material.AnyRubber)
                            ItemData stackData = GTOreDictUnificator.getAssociation(aChoice);
                            if (stackData != null) {
                                Materials material = stackData.mMaterial.mMaterial;

                                if (material == Materials.StyreneButadieneRubber || material == Materials.Silicone) {
                                    if (usedMaterial == null) {
                                        usedMaterial = material;
                                    } else if (usedMaterial != material) {
                                        illegalRubber = true;
                                    }
                                }

                            }
                            // Check circuit part for same
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
                        addRecipeMT(
                            addIntegratedCircuitToRecipe(
                                ModifyRecipe(
                                    new GTRecipe(
                                        false,
                                        newInputs,
                                        new ItemStack[] { aRecipe.mOutput },
                                        null,
                                        null,
                                        aRecipe.mFluidInputs,
                                        null,
                                        aRecipe.mDuration,
                                        aRecipe.mEUt,
                                        0),
                                    true),
                                4));
                    }
                } else {
                    addRecipeMT(
                        addIntegratedCircuitToRecipe(
                            ModifyRecipe(
                                new GTRecipe(
                                    false,
                                    aRecipe.mInputs,
                                    new ItemStack[] { aRecipe.mOutput },
                                    null,
                                    null,
                                    aRecipe.mFluidInputs,
                                    null,
                                    aRecipe.mDuration,
                                    aRecipe.mEUt,
                                    0),
                                true),
                            4));
                }
            }
        }
    }

    private void loadSpaceAssemblerRecipes() {
        HashSet<TST_ItemID> GenerateRecipeOutputs = new HashSet<>();
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(GTModHandler.getModItem("OpenComputers", "item", 1, 39)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Optically_Perfected_CPU.get(1)));
        GenerateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Optically_Compatible_Memory.get(1)));

        for (GTRecipe aRecipe : spaceAssemblerRecipes.getAllRecipes()) {
            if (GenerateRecipeOutputs.contains(TST_ItemID.createNoNBT(aRecipe.mOutputs[0]))) {
                addRecipeMT(addIntegratedCircuitToRecipe(reduplicateRecipe(ModifyRecipe(aRecipe, true), 4, 1), 16));
            }
        }
    }

    // Modify the circuit part to warp, others turn 16 times. All Material to molten.
    public GTRecipe ModifyRecipe(GTRecipe baseRecipe, boolean isFluidInputMultiply) {

        ArrayList<ItemStack> inputItems = new ArrayList<>();
        ArrayList<FluidStack> inputFluids = new ArrayList<>();

        if (baseRecipe.mFluidInputs != null && baseRecipe.mFluidInputs.length > 0) {
            if (isFluidInputMultiply)
                Collections.addAll(inputFluids, reduplicateRecipe(baseRecipe, 16, 1).mFluidInputs);
            else Collections.addAll(inputFluids, baseRecipe.mFluidInputs);
        }

        if (baseRecipe.mInputs != null && baseRecipe.mInputs.length > 0) {
            for (ItemStack aStack : removeIntegratedCircuitFromStacks(baseRecipe.mInputs)) {
                boolean isItemModified = false;
                boolean isNeedTraverse = true;
                for (Map.Entry<ItemStack, ItemStack> entry : circuitItemsToWrapped.entrySet()) {
                    if (GTUtility.areStacksEqual(entry.getKey(), aStack)) {
                        inputItems.add(copyAmountUnsafe(aStack.stackSize, entry.getValue()));
                        isItemModified = true;
                        break;
                    }
                }

                if (!isItemModified && BWUtil.checkStackAndPrefix(aStack)) {
                    ItemData Data = Objects.requireNonNull(GTOreDictUnificator.getAssociation(aStack));
                    Materials Material = Data.mMaterial.mMaterial;
                    OrePrefixes OreDict = Data.mPrefix;
                    if (Material.getMolten(1) != null && targetModifyOreDict.contains(OreDict)) {
                        if (Material == Materials.TengamAttuned) Material = Materials.TengamPurified;
                        inputFluids.add(
                            Material
                                .getMolten(OreDict.mMaterialAmount * GTValues.L * aStack.stackSize / GTValues.M * 16));
                        isItemModified = true;
                    } else if (superConductorMaterialList.contains(Material) && OreDict != OrePrefixes.circuit) {
                        inputItems.add(
                            copyAmountUnsafe(
                                (int) (OreDict.mMaterialAmount * aStack.stackSize * 2 / GTValues.M),
                                GTOreDictUnificator.get(OrePrefixes.wireGt16, Material, 1)));
                        isItemModified = true;
                    }
                    // if an item has GT ore dict, It not requires additional processing (like GTPP Materials)
                    isNeedTraverse = false;
                }

                if (!isItemModified && isNeedTraverse) {
                    // It's better to determine whether it contains ore dict

                    for (Map.Entry<ItemStack, FluidStack> entry : specialMaterialCantAutoModify.entrySet()) {
                        if (GTUtility.areStacksEqual(entry.getKey(), aStack)) {
                            inputFluids
                                .add(copyAmount(entry.getValue().amount * aStack.stackSize * 16, entry.getValue()));
                            isItemModified = true;
                            break;
                        }
                    }
                }

                if (!isItemModified) inputItems.add(copyAmountUnsafe(aStack.stackSize * 16, aStack));
            }

        }
        return new GTRecipe(
            false,
            inputItems.toArray(new ItemStack[0]),
            new ItemStack[] { copyAmountUnsafe(baseRecipe.mOutputs[0].stackSize * 16, baseRecipe.mOutputs[0]) },
            null,
            null,
            mergeSameFluid(inputFluids.toArray(new FluidStack[0])),
            null,
            baseRecipe.mDuration * 12,
            baseRecipe.mEUt,
            0);
    }

    public GTRecipe reduplicateRecipe(GTRecipe oRecipe, int inputItemMultiTimes, int inputFluidMultiTimes,
        int outputItemMultiTimes, int outputFluidMultiTimes, int eutMultiTimes, int durationMultiTimes) {
        ArrayList<ItemStack> inputItems = new ArrayList<>();
        ArrayList<FluidStack> inputFluids = new ArrayList<>();
        ArrayList<ItemStack> outputItems = new ArrayList<>();
        ArrayList<FluidStack> outputFluids = new ArrayList<>();

        if (oRecipe == null) return null;

        for (ItemStack aStack : oRecipe.mInputs) {
            if (aStack != null) inputItems.add(copyAmountUnsafe(aStack.stackSize * inputItemMultiTimes, aStack));
        }
        for (FluidStack aStack : oRecipe.mFluidInputs) {
            if (aStack != null) inputFluids.add(copyAmount(aStack.amount * inputFluidMultiTimes, aStack));
        }

        for (ItemStack aStack : oRecipe.mOutputs) {
            if (aStack != null) outputItems.add(copyAmountUnsafe(aStack.stackSize * outputItemMultiTimes, aStack));
        }
        for (FluidStack aStack : oRecipe.mFluidOutputs) {
            if (aStack != null) outputFluids.add(copyAmount(aStack.amount * outputFluidMultiTimes, aStack));
        }

        return new GTRecipe(
            false,
            inputItems.toArray(new ItemStack[0]),
            outputItems.toArray(new ItemStack[0]),
            null,
            null,
            inputFluids.toArray(new FluidStack[0]),
            outputFluids.toArray(new FluidStack[0]),
            oRecipe.mDuration * durationMultiTimes,
            oRecipe.mEUt * eutMultiTimes,
            0);
    }

    public GTRecipe reduplicateRecipe(GTRecipe oRecipe, int n, int eut) {
        return reduplicateRecipe(oRecipe, n, n, n, n, eut, n);
    }

    public GTRecipe addIntegratedCircuitToRecipe(GTRecipe oRecipe, int circuitNum) {
        ArrayList<ItemStack> inputItems = new ArrayList<>();
        inputItems.add(GTUtility.getIntegratedCircuit(circuitNum));

        if (oRecipe == null) return null;
        Collections.addAll(inputItems, oRecipe.mInputs);

        return new GTRecipe(
            false,
            inputItems.toArray(new ItemStack[0]),
            oRecipe.mOutputs,
            null,
            null,
            oRecipe.mFluidInputs,
            oRecipe.mFluidOutputs,
            oRecipe.mDuration,
            oRecipe.mEUt,
            0);
    }

    public static boolean isRecipeInputItemSame(GTRecipe a, GTRecipe b) {
        if (!areItemStacksEqual(a.mOutputs[0], b.mOutputs[0])) return false;
        if (a.mInputs.length != b.mInputs.length) return false;
        for (int i = 0; i < a.mInputs.length; i++) {
            if (!areItemStacksEqual(a.mInputs[i], b.mInputs[i])) {
                return false;
            }
        }
        return true;
    }

    public static FluidStack[] mergeSameFluid(FluidStack[] fluidStacks) {

        Map<Fluid, Integer> fluidMap = new LinkedHashMap<>();

        for (FluidStack aStack : fluidStacks) {
            fluidMap.put(aStack.getFluid(), fluidMap.getOrDefault(aStack.getFluid(), 0) + aStack.amount);
        }

        ArrayList<FluidStack> mergedList = new ArrayList<>();
        for (Map.Entry<Fluid, Integer> entry : fluidMap.entrySet()) {
            mergedList.add(new FluidStack(entry.getKey(), entry.getValue()));
        }

        return mergedList.toArray(new FluidStack[0]);
    }

    public static ItemStack[] mergeSameItem(ItemStack[] itemStacks) {

        Map<Item, Integer> itemMap = new LinkedHashMap<>();

        for (ItemStack aStack : itemStacks) {
            itemMap.put(aStack.getItem(), itemMap.getOrDefault(aStack.getItem(), 0) + aStack.stackSize);
        }

        ArrayList<ItemStack> mergedList = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : itemMap.entrySet()) {
            mergedList.add(new ItemStack(entry.getKey(), entry.getValue()));
        }

        return mergedList.toArray(new ItemStack[0]);
    }

    private void addRecipeMT(GTRecipe aRecipe) {
        if (aRecipe == null) return;
        TST_RecipeBuilder.builder()
            .itemInputs(aRecipe.mInputs)
            .fluidInputs(aRecipe.mFluidInputs)
            .itemOutputs(aRecipe.mOutputs)
            .eut(aRecipe.mEUt)
            .duration(aRecipe.mDuration)
            .noOptimize()
            .addTo(MT);
    }

    private void initStatics() {

        /**
         * init Wrap circuit parts
         */
        // spotless:off
        ItemStack[] CircuitParts = new ItemStack[] {
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ULV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MAX, 1),
            ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1),
            ItemList.Circuit_Parts_Crystal_Chip_Master.get(1),
            ItemList.Circuit_Board_Coated.get(1),
            ItemList.Circuit_Board_Coated_Basic.get(1),
            ItemList.Circuit_Board_Phenolic.get(1),
            ItemList.Circuit_Board_Phenolic_Good.get(1),
            ItemList.Circuit_Board_Epoxy.get(1),
            ItemList.Circuit_Board_Epoxy_Advanced.get(1),
            ItemList.Circuit_Board_Fiberglass.get(1),
            ItemList.Circuit_Board_Fiberglass_Advanced.get(1),
            ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
            ItemList.Circuit_Board_Multifiberglass.get(1),
            ItemList.Circuit_Board_Wetware.get(1),
            ItemList.Circuit_Board_Wetware_Extreme.get(1),
            ItemList.Circuit_Board_Plastic.get(1),
            ItemList.Circuit_Board_Plastic_Advanced.get(1),
            ItemList.Circuit_Board_Bio.get(1),
            ItemList.Circuit_Board_Bio_Ultra.get(1),
            ItemList.Circuit_Parts_ResistorSMD.get(1),
            ItemList.Circuit_Parts_InductorSMD.get(1),
            ItemList.Circuit_Parts_DiodeSMD.get(1),
            ItemList.Circuit_Parts_TransistorSMD.get(1),
            ItemList.Circuit_Parts_CapacitorSMD.get(1),
            ItemList.Circuit_Parts_ResistorASMD.get(1),
            ItemList.Circuit_Parts_DiodeASMD.get(1),
            ItemList.Circuit_Parts_TransistorASMD.get(1),
            ItemList.Circuit_Parts_CapacitorASMD.get(1),
            ItemList.Circuit_Chip_ILC.get(1),
            ItemList.Circuit_Chip_Ram.get(1),
            ItemList.Circuit_Chip_NAND.get(1),
            ItemList.Circuit_Chip_NOR.get(1),
            ItemList.Circuit_Chip_CPU.get(1),
            ItemList.Circuit_Chip_SoC.get(1),
            ItemList.Circuit_Chip_SoC2.get(1),
            ItemList.Circuit_Chip_PIC.get(1),
            ItemList.Circuit_Chip_Simple_SoC.get(1),
            ItemList.Circuit_Chip_HPIC.get(1),
            ItemList.Circuit_Chip_UHPIC.get(1),
            ItemList.Circuit_Chip_ULPIC.get(1),
            ItemList.Circuit_Chip_LPIC.get(1),
            ItemList.Circuit_Chip_NPIC.get(1),
            ItemList.Circuit_Chip_PPIC.get(1),
            ItemList.Circuit_Chip_QPIC.get(1),
            ItemList.Circuit_Chip_NanoCPU.get(1),
            ItemList.Circuit_Chip_QuantumCPU.get(1),
            ItemList.Circuit_Chip_CrystalCPU.get(1),
            ItemList.Circuit_Chip_CrystalSoC.get(1),
            ItemList.Circuit_Chip_CrystalSoC2.get(1),
            ItemList.Circuit_Chip_NeuroCPU.get(1),
            ItemList.Circuit_Chip_BioCPU.get(1),
            ItemList.Circuit_Chip_Stemcell.get(1),
            ItemList.Circuit_Chip_Biocell.get(1),
            ItemList.Circuit_Parts_ResistorXSMD.get(1),
            ItemList.Circuit_Parts_DiodeXSMD.get(1),
            ItemList.Circuit_Parts_TransistorXSMD.get(1),
            ItemList.Circuit_Parts_CapacitorXSMD.get(1),
            ItemList.Circuit_Parts_InductorASMD.get(1),
            ItemList.Circuit_Parts_InductorXSMD.get(1),
            ItemList.Circuit_Chip_Optical.get(1),
            ItemList.Circuit_Board_Optical.get(1),
            ItemList.Optically_Perfected_CPU.get(1),
            ItemList.Optical_Cpu_Containment_Housing.get(1),
            ItemList.Optically_Compatible_Memory.get(1),
            ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(1),
            ItemList.Circuit_Parts_Chip_Bioware.get(1) };

        int Count = 0;
        for (WrappedCircuitItem item : WrappedCircuitItem.values()) {
            if (Count < 15) {
                item.set(GTModHandler.getModItem("GoodGenerator", "circuitWrap", 1, Count));
            } else {
                item.set(GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32778 - Count));
            }
            if (CircuitParts[Count] != null && item.get(1) != null) {
                circuitItemsToWrapped.put(CircuitParts[Count], item.get(1));
            } else circuitItemsToWrapped.put(GTCMItemList.TestItem0.get(1), GTCMItemList.TestItem0.get(1));
            Count++;
        }

        circuitItemsToWrapped.put(ItemList.Circuit_Parts_Resistor.get(1), WrappedCircuitItem.Wrapped_Circuit_Parts_ResistorSMD.get(1));
        circuitItemsToWrapped.put(ItemList.Circuit_Parts_Coil.get(1), WrappedCircuitItem.Wrapped_Circuit_Parts_InductorSMD.get(1));
        circuitItemsToWrapped.put(ItemList.Circuit_Parts_Diode.get(1), WrappedCircuitItem.Wrapped_Circuit_Parts_DiodeSMD.get(1));
        circuitItemsToWrapped.put(ItemList.Circuit_Parts_Transistor.get(1), WrappedCircuitItem.Wrapped_Circuit_Parts_TransistorSMD.get(1));
        circuitItemsToWrapped.put(ItemList.Circuit_Parts_Capacitor.get(1), WrappedCircuitItem.Wrapped_Circuit_Parts_CapacitorSMD.get(1));
        circuitItemsToWrapped.put(CustomItemList.PikoCircuit.get(1), WrappedCircuitItem.Wrapped_Circuit_UMV.get(1));
        circuitItemsToWrapped.put(CustomItemList.QuantumCircuit.get(1), WrappedCircuitItem.Wrapped_Circuit_UXV.get(1));
        // spotless:on

        /**
         * init GTPP Material Map
         */
        // Set<Material> generateMaterialList =new HashSet<>();
        // generateMaterialList.add(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS);
        // generateMaterialList.add(MaterialsAlloy.QUANTUM);

        for (Map.Entry<String, Map<String, ItemStack>> outerEntry : mComponentMap.entrySet()) {
            String materialName = outerEntry.getKey();
            Map<String, ItemStack> innerMap = outerEntry.getValue();

            Material material = null;

            for (Material aMaterial : Material.mMaterialMap) {
                if (aMaterial.getUnlocalizedName()
                    .equals(materialName)) {
                    material = aMaterial;
                }
            }

            if (material == null) continue;
            // if (!generateMaterialList.contains(material)) continue;

            for (Map.Entry<String, ItemStack> innerEntry : innerMap.entrySet()) {
                String orePrefixName = innerEntry.getKey();
                ItemStack aStack = innerEntry.getValue();

                OrePrefixes OreDict = OrePrefixes.valueOf(orePrefixName);

                int amount = (int) (OreDict.mMaterialAmount * GTValues.L * aStack.stackSize / GTValues.M);
                FluidStack fluidStack = material.getFluidStack(amount);

                if (fluidStack != null) {
                    specialMaterialCantAutoModify.put(aStack, fluidStack);
                }
            }
        }

        specialMaterialCantAutoModify
            .put(ItemList.Circuit_Parts_Reinforced_Glass_Tube.get(1), Materials.ReinforceGlass.getMolten(288));

        superConductorMaterialList.add(Materials.SuperconductorMV);
        superConductorMaterialList.add(Materials.SuperconductorHV);
        superConductorMaterialList.add(Materials.SuperconductorEV);
        superConductorMaterialList.add(Materials.SuperconductorIV);
        superConductorMaterialList.add(Materials.SuperconductorLuV);
        superConductorMaterialList.add(Materials.SuperconductorZPM);
        superConductorMaterialList.add(Materials.SuperconductorUV);
        superConductorMaterialList.add(Materials.SuperconductorUHV);
        superConductorMaterialList.add(Materials.SuperconductorUEV);
        superConductorMaterialList.add(Materials.SuperconductorUIV);
        superConductorMaterialList.add(Materials.SuperconductorUMV);

        targetModifyOreDict.add(OrePrefixes.wireGt01);
        targetModifyOreDict.add(OrePrefixes.wireGt02);
        targetModifyOreDict.add(OrePrefixes.wireGt04);
        targetModifyOreDict.add(OrePrefixes.wireGt08);
        targetModifyOreDict.add(OrePrefixes.wireGt12);
        targetModifyOreDict.add(OrePrefixes.wireGt16);
        targetModifyOreDict.add(OrePrefixes.frameGt);
        targetModifyOreDict.add(OrePrefixes.dust);
        targetModifyOreDict.add(OrePrefixes.nugget);
        targetModifyOreDict.add(OrePrefixes.ingot);
        targetModifyOreDict.add(OrePrefixes.plate);
        targetModifyOreDict.add(OrePrefixes.plateDouble);
        targetModifyOreDict.add(OrePrefixes.plateDense);
        targetModifyOreDict.add(OrePrefixes.rod);
        targetModifyOreDict.add(OrePrefixes.round);
        targetModifyOreDict.add(OrePrefixes.bolt);
        targetModifyOreDict.add(OrePrefixes.screw);
        targetModifyOreDict.add(OrePrefixes.ring);
        targetModifyOreDict.add(OrePrefixes.foil);
        targetModifyOreDict.add(OrePrefixes.itemCasing);
        targetModifyOreDict.add(OrePrefixes.wireFine);
        targetModifyOreDict.add(OrePrefixes.gearGt);
        targetModifyOreDict.add(OrePrefixes.gearGtSmall);
        targetModifyOreDict.add(OrePrefixes.rotor);
        targetModifyOreDict.add(OrePrefixes.stickLong);
        targetModifyOreDict.add(OrePrefixes.spring);
        targetModifyOreDict.add(OrePrefixes.springSmall);
        targetModifyOreDict.add(OrePrefixes.plateSuperdense);
        targetModifyOreDict.add(OrePrefixes.pipeTiny);
        targetModifyOreDict.add(OrePrefixes.pipeSmall);
        targetModifyOreDict.add(OrePrefixes.pipeMedium);
        targetModifyOreDict.add(OrePrefixes.pipeLarge);
        targetModifyOreDict.add(OrePrefixes.pipeHuge);
        targetModifyOreDict.add(OrePrefixes.pipeQuadruple);
        targetModifyOreDict.add(OrePrefixes.pipeNonuple);
    }

    // spotless:off
    public void loadCustomRecipes(){
        // Do Not Add Messy Recipe to MT

        final ItemStack ringBlock = GTModHandler.getModItem("SGCraft", "stargateRing" , 1, 0);
        final ItemStack chevronBlock = GTModHandler.getModItem("SGCraft", "stargateRing", 1, 1);
        final ItemStack irisUpgrade = GTModHandler.getModItem("SGCraft", "sgIrisUpgrade" , 1, 0);

        // region Proof Of Heroes
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.SpaceWarper.get(64),
                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 64),
                GTModHandler.getModItem("eternalsingularity", "combined_singularity", 64, 15),
                ItemList.Timepiece.get(64),
                ItemList.GigaChad.get(64),
                tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                GTModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 64),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 15),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 16),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 20),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 21),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 17),
                ItemList.ZPM6.get(64),
                GTCMItemList.IndistinctTentacle.get(64)
            )
            .fluidInputs(
                MaterialsUEVplus.Time.getMolten(1000 * 114514),
                MaterialsUEVplus.Space.getMolten(1000 * 114514),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1000 * 114514),
                GGMaterial.shirabon.getMolten(1000 * 114514),
                MaterialsUEVplus.Universium.getMolten(1000 * 114514),
                MaterialsUEVplus.Eternity.getMolten(1000 * 114514),
                MaterialsUEVplus.PrimordialMatter.getFluid(1000 * 114514)
            )
            .itemOutputs(GTCMItemList.ProofOfHeroes.get(1))
            .noOptimize()
            .specialValue(13500)
            .eut(RECIPE_MAX)
            .duration(20 * 1919810)
            .addTo(MT);

        // endregion

        // Optical SoC Shield
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                GregtechItemList.InfinityInfusedShieldingCore.get(0),
                ItemList.Optical_Cpu_Containment_Housing.get(1),
                Materials.Glowstone.getNanite(4))
            .fluidInputs(MaterialsUEVplus.Space.getMolten(36), MaterialsUEVplus.Time.getMolten(36))
            .itemOutputs(GTCMItemList.ParticleTrapTimeSpaceShield.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 64)
            .addTo(MT);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                GregtechItemList.SpaceTimeBendingCore.get(0),
                ItemList.Optical_Cpu_Containment_Housing.get(2),
                Materials.Glowstone.getNanite(4))
            .fluidInputs(
                MaterialsUEVplus.Space.getMolten(144),
                MaterialsUEVplus.Time.getMolten(144),
                MaterialsUEVplus.SpaceTime.getMolten(288))
            .itemOutputs(GTCMItemList.ParticleTrapTimeSpaceShield.get(16))
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(2500))
            .eut(RECIPE_UMV)
            .duration(20 * 64)
            .addTo(MT);

        // region Endgame Challenge content

        // Liquid Stargate
        GTValues.RA.stdBuilder()
            .itemInputs(
                copyAmount(1, ringBlock),
                copyAmount(1, chevronBlock),
                copyAmount(1, chevronBlock),
                copyAmount(1, ringBlock),

                copyAmount(1, chevronBlock),
                copyAmount(1, irisUpgrade),
                copyAmount(1, irisUpgrade),
                copyAmount(1, chevronBlock),

                copyAmount(1, ringBlock),
                copyAmount(1, irisUpgrade),
                copyAmount(1, irisUpgrade),
                copyAmount(1, ringBlock),

                copyAmount(1, chevronBlock),
                copyAmount(1, ringBlock),
                copyAmount(1, ringBlock),
                copyAmount(1, chevronBlock)
            )
            .fluidInputs(
                MaterialPool.StabiliseVoidMatter.getFluidOrGas(1_000)
            )
            .fluidOutputs(MaterialPool.LiquidStargate.getFluidOrGas(1000))
            .noOptimize()
            .specialValue(13500)
            .eut(RECIPE_MAX)
            .duration(20 *99_999_999)
            .addTo(MT);

        // StabiliseVoidMatter
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                setStackSize(Materials.CosmicNeutronium.getDust(1), 10_000_000),
                setStackSize(Materials.Bedrockium.getDust(1),10_000_000),
                setStackSize(Materials.Carbon.getDust(1),10_000_000),
                setStackSize(Materials.Oilsands.getDust(1),10_000_000),
                setStackSize(Materials.NiobiumTitanium.getDust(1),10_000_000),
                setStackSize(MaterialsElements.STANDALONE.BLACK_METAL.getDust(1),10_000_000),
                setStackSize(Materials.Naquadria.getDust(1),10_000_000),
                setStackSize(Materials.Obsidian.getDust(1),10_000_000),
                setStackSize(Materials.Coal.getDust(1),10_000_000),
                setStackSize(Materials.NaquadahAlloy.getDust(1),10_000_000),
                setStackSize(Materials.Tungsten.getDust(1),10_000_000),
                setStackSize(MaterialsUEVplus.TranscendentMetal.getDust(1),10_000_000),
                setStackSize(Materials.Perlite.getDust(1),10_000_000),
                setStackSize(Materials.DarkAsh.getDust(1),10_000_000),
                setStackSize(Materials.GraniticMineralSand.getDust(1),10_000_000),
                setStackSize(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1),10_000_000)
            )
            .fluidInputs(
                Materials.Polycaprolactam.getMolten(10_000_000),
                Materials.NickelZincFerrite.getMolten(10_000_000),
                Materials.DarkSteel.getMolten(10_000_000),
                Materials.Polybenzimidazole.getMolten(10_000_000),
                GGMaterial.tairitsu.getMolten(10_000_000),
                Materials.Tungsten.getMolten(10_000_000),
                GGMaterial.marM200.getMolten(10_000_000),
                Materials.Vanadium.getMolten(10_000_000),
                MaterialsElements.STANDALONE.BLACK_METAL.getFluidStack(10_000_000),
                Materials.ShadowIron.getMolten(10_000_000),
                Materials.NaquadahAlloy.getMolten(10_000_000),
                Materials.ShadowSteel.getMolten(10_000_000),
                Materials.Cadmium.getMolten(10_000_000),
                Materials.Desh.getMolten(10_000_000),
                Materials.BlackPlutonium.getMolten(10_000_000),
                Materials.BlackSteel.getMolten(10_000_000),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(10_000_000)
            )
            .fluidOutputs(MaterialPool.StabiliseVoidMatter.getFluidOrGas(1))
            .eut(RECIPE_MAX)
            .duration(20 * 99_999_999)
            .addTo(MT);

        // ProofOfGods
        // TODO -- Temporarily, be revised in the next version
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTCMItemList.UxvFlask.get(1),
                GTCMItemList.ProofOfHeroes.get(64),
                setStackSize(Materials.Silver.getNanite(1), 1_000),
                setStackSize(Materials.Gold.getNanite(1), 1_000),
                setStackSize(Materials.Neutronium.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.Universium.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.Eternity.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.TranscendentMetal.getNanite(1), 1_000),
                setStackSize(Materials.Glowstone.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.WhiteDwarfMatter.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.BlackDwarfMatter.getNanite(1), 1_000)
            )
            .fluidInputs(
                MaterialPool.LiquidStargate.getFluidOrGas(50_000),
                MaterialPool.StabiliseVoidMatter.getFluidOrGas(1_000)
            )
            .itemOutputs(
                GTCMItemList.ProofOfGods.get(1)
            )
            .eut(RECIPE_MAX)
            .duration(20 * 99_999_999)
            .addTo(MT);

        // FLASK
        loadFlaskRecipe();

        // endregion
        if (Config.activateMegaSpaceStation) {
            loadMaxRecipe();
        }
    }

    public void loadMaxRecipe() {
        ItemStack[] inStack = new ItemStack[]{
            ItemList.Circuit_Parts_ResistorXSMD.get(16),
            ItemList.Circuit_Parts_DiodeXSMD.get(16),
            ItemList.Circuit_Parts_TransistorXSMD.get(16),
            ItemList.Circuit_Parts_CapacitorXSMD.get(16),
            ItemList.Circuit_Parts_InductorXSMD.get(16)
        };
        ItemStack[] outStack = new ItemStack[]{
            GTCMItemList.HighDimensionalResistor.get(64),
            GTCMItemList.HighDimensionalDiode.get(64),
            GTCMItemList.HighDimensionalTransistor.get(64),
            GTCMItemList.HighDimensionalCapacitor.get(64),
            GTCMItemList.HighDimensionalInterface.get(64),
        };
        for (int i = 0; i < 5; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(12),
                    GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.TranscendentMetal, 4),
                    GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 2),
                    inStack[i],
                    GTCMItemList.HighDimensionalExtend.get(1)
                )
                .fluidInputs(
                    MaterialsUEVplus.Time.getMolten(144)
                )
                .itemOutputs(
                    outStack[i]
                )
                .eut(RECIPE_UEV)
                .duration(20)
                .addTo(MT);
        }


    }

    public void loadFlaskRecipe() {
        final int ITEMS_FLASK_COUNT = 100_000;
        // TODO -- Temporarily, be revised in the next version
        // LV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                setStackSize(ItemList.Electric_Motor_LV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_LV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_LV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_LV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Microprocessor.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedstoneAlloy, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Iron.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.LvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(32 * 20)
            .addTo(MT);

        // MV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.LvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_MV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_MV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_MV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_MV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Processor.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Copper.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.MvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(128 * 20)
            .addTo(MT);

        // HV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.MvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_HV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_HV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_HV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_HV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Nanoprocessor.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Nickel.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.HvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(512 * 20)
            .addTo(MT);

        // EV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.HvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_EV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_EV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_EV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_EV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Quantumprocessor.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Titanium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.EvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(2_048 * 20)
            .addTo(MT);

        // IV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.EvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_IV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_IV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_IV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_IV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Crystalprocessor.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Tungsten.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.IvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(8_192 * 20)
            .addTo(MT);

        // LUV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.IvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_LuV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_LuV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_LuV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_LuV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Neuroprocessor.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Osmium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.LuvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(32_768 * 20)
            .addTo(MT);

        // ZPM FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.LuvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_ZPM.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_ZPM.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_ZPM.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_ZPM.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_Bioprocessor.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Naquadah.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.ZpmFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(131_072 * 20)
            .addTo(MT);

        // UV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.ZpmFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_OpticalProcessor.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Neutronium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(524_288 * 20)
            .addTo(MT);

        // UHV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UHV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UHV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UHV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UHV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_OpticalAssembly.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Samarium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UhvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(2_097_152 * 20)
            .addTo(MT);

        // UEV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UhvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UEV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UEV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UEV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UEV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_CosmicProcessor.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Americium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UevFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(8_388_608 * 20)
            .addTo(MT);

        // UIV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UevFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UIV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UIV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UIV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UIV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_CosmicAssembly.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Thorium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UivFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(33_554_432 * 20)
            .addTo(MT);

        // UMV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UivFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UMV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UMV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UMV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UMV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_CosmicComputer.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Plutonium241.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UmvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(100_000_000 * 20)
            .addTo(MT);

        // UXV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UmvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UXV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UXV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UXV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UXV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Circuit_CosmicMainframe.get(1),ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Infinity, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Radon.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UxvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(100_000_000 * 20)
            .addTo(MT);

    }

    // pattern
    /*
    GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1)

            )
            .fluidInputs(

            )
            .itemOutputs(

            )

            .eut()
            .duration(20)
            .addTo(MT);
     */
    public enum WrappedCircuitItem {
        Wrapped_Circuit_ULV,
        Wrapped_Circuit_LV,
        Wrapped_Circuit_MV,
        Wrapped_Circuit_HV,
        Wrapped_Circuit_EV ,
        Wrapped_Circuit_IV ,
        Wrapped_Circuit_LuV,
        Wrapped_Circuit_ZPM ,
        Wrapped_Circuit_UV ,
        Wrapped_Circuit_UHV ,
        Wrapped_Circuit_UEV ,
        Wrapped_Circuit_UIV ,
        Wrapped_Circuit_UMV ,
        Wrapped_Circuit_UXV ,
        Wrapped_Circuit_MAX ,

        Wrapped_Circuit_Parts_Crystal_Chip_Elite,
        Wrapped_Circuit_Parts_Crystal_Chip_Master,
        Wrapped_Circuit_Board_Coated,
        Wrapped_Circuit_Board_Coated_Basic,
        Wrapped_Circuit_Board_Phenolic,
        Wrapped_Circuit_Board_Phenolic_Good,
        Wrapped_Circuit_Board_Epoxy,
        Wrapped_Circuit_Board_Epoxy_Advanced,
        Wrapped_Circuit_Board_Fiberglass,
        Wrapped_Circuit_Board_Fiberglass_Advanced,
        Wrapped_Circuit_Board_Multifiberglass_Elite,
        Wrapped_Circuit_Board_Multifiberglass,
        Wrapped_Circuit_Board_Wetware,
        Wrapped_Circuit_Board_Wetware_Extreme,
        Wrapped_Circuit_Board_Plastic,
        Wrapped_Circuit_Board_Plastic_Advanced,
        Wrapped_Circuit_Board_Bio,
        Wrapped_Circuit_Board_Bio_Ultra,
        Wrapped_Circuit_Parts_ResistorSMD,
        Wrapped_Circuit_Parts_InductorSMD,
        Wrapped_Circuit_Parts_DiodeSMD,
        Wrapped_Circuit_Parts_TransistorSMD,
        Wrapped_Circuit_Parts_CapacitorSMD,
        Wrapped_Circuit_Parts_ResistorASMD,
        Wrapped_Circuit_Parts_DiodeASMD,
        Wrapped_Circuit_Parts_TransistorASMD,
        Wrapped_Circuit_Parts_CapacitorASMD,
        Wrapped_Circuit_Chip_ILC,
        Wrapped_Circuit_Chip_Ram,
        Wrapped_Circuit_Chip_NAND,
        Wrapped_Circuit_Chip_NOR,
        Wrapped_Circuit_Chip_CPU,
        Wrapped_Circuit_Chip_SoC,
        Wrapped_Circuit_Chip_SoC2,
        Wrapped_Circuit_Chip_PIC,
        Wrapped_Circuit_Chip_Simple_SoC,
        Wrapped_Circuit_Chip_HPIC,
        Wrapped_Circuit_Chip_UHPIC,
        Wrapped_Circuit_Chip_ULPIC,
        Wrapped_Circuit_Chip_LPIC,
        Wrapped_Circuit_Chip_NPIC,
        Wrapped_Circuit_Chip_PPIC,
        Wrapped_Circuit_Chip_QPIC,
        Wrapped_Circuit_Chip_NanoCPU,
        Wrapped_Circuit_Chip_QuantumCPU,
        Wrapped_Circuit_Chip_CrystalCPU,
        Wrapped_Circuit_Chip_CrystalSoC,
        Wrapped_Circuit_Chip_CrystalSoC2,
        Wrapped_Circuit_Chip_NeuroCPU,
        Wrapped_Circuit_Chip_BioCPU,
        Wrapped_Circuit_Chip_Stemcell,
        Wrapped_Circuit_Chip_Biocell,
        Wrapped_Circuit_Parts_ResistorXSMD,
        Wrapped_Circuit_Parts_DiodeXSMD,
        Wrapped_Circuit_Parts_TransistorXSMD,
        Wrapped_Circuit_Parts_CapacitorXSMD,
        Wrapped_Circuit_Parts_InductorASMD,
        Wrapped_Circuit_Parts_InductorXSMD,
        Wrapped_Circuit_Chip_Optical,
        Wrapped_Circuit_Board_Optical,
        Wrapped_Optically_Perfected_CPU,
        Wrapped_Optical_Cpu_Containment_Housing,
        Wrapped_Optically_Compatible_Memory,
        Wrapped_Circuit_Parts_Crystal_Chip_Wetware,
        Wrapped_Circuit_Parts_Chip_Bioware;
        private ItemStack itemStack;
        public ItemStack get(int amount){
            return copyAmount(amount, this.itemStack);
        }
        public void set(ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    }
}
// spotless:on
