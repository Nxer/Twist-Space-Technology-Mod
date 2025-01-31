package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.Utils.removeIntegratedCircuitFromStacks;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gtPlusPlus.core.material.Material.mComponentMap;
import static net.minecraft.item.ItemStack.areItemStacksEqual;
import static tectech.thing.CustomItemList.DATApipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;

import bartworks.util.BWUtil;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;

public class MiracleTopRecipePool implements IRecipePool {

    final RecipeMap<?> MT = GTCMRecipe.MiracleTopRecipes;
    public static final HashSet<ItemStack> NotModifyRecipeOutputs = new HashSet<>();
    public static final HashSet<ItemStack> IgnoreRecipeOutputs = new HashSet<>();
    public static final HashMap<ItemStack, ItemStack> circuitItemsToWrapped = new HashMap<>();
    public static final HashSet<Materials> superConductorMaterialList = new HashSet<>();
    public static final HashSet<OrePrefixes> targetModifyOreDict = new HashSet<>();
    public static final HashMap<ItemStack, FluidStack> specialMaterialCantAutoModify = new HashMap<>();

    @Override
    public void loadRecipes() {
        TwistSpaceTechnology.LOG.info("MiracleTopRecipePool loading recipes.");
        initStatics();
        loadCircuitAssemblerRecipes();
        loadCustumRecipes();
    }

    private void loadCircuitAssemblerRecipes() {

        // Exclude low-level solder recipe
        ArrayList<GTRecipe> recipeCache = new ArrayList<>();
        for (GTRecipe originalRecipe : circuitAssemblerRecipes.getAllRecipes()) {
            if (IgnoreRecipeOutputs.contains(copyAmount(1, originalRecipe.mOutputs[0]))) continue;
            boolean isRecipeAdded = false;
            for (GTRecipe cachedRecipe : recipeCache) {
                if (isRecipeInputItemSame(originalRecipe, cachedRecipe)) {
                    isRecipeAdded = true;
                    break;
                }
            }

            if (!isRecipeAdded) {
                GTRecipe recipeCopy = originalRecipe.copy();
                FluidStack recipeFluid = recipeCopy.mFluidInputs[0];
                if (recipeFluid.isFluidEqual(Materials.Lead.getMolten(1)))
                    recipeFluid = Materials.SolderingAlloy.getMolten(recipeFluid.amount / 4);
                else if (recipeFluid.isFluidEqual(Materials.Tin.getMolten(1)))
                    recipeFluid = Materials.SolderingAlloy.getMolten(recipeFluid.amount / 2);
                recipeCopy.mFluidInputs[0] = recipeFluid;
                recipeCache.add(recipeCopy);
            }
        }

        for (GTRecipe aRecipe : ModifyRecipe(recipeCache)) {
            addRecipeMT(addIntegratedCircuitToRecipe(reduplicateRecipe(aRecipe, 3, 3, 4, 4, 4, 3), 16));
        }

    }

    public ArrayList<GTRecipe> ModifyRecipe(ArrayList<GTRecipe> oRecipes) {
        ArrayList<GTRecipe> rRecipes = new ArrayList<>();
        for (GTRecipe baseRecipe : oRecipes) {
            if (NotModifyRecipeOutputs.contains(copyAmount(1, baseRecipe.mOutputs[0]))) continue;
            ArrayList<ItemStack> inputItems = new ArrayList<>();
            ArrayList<FluidStack> inputFluids = new ArrayList<>();
            inputFluids.add(baseRecipe.mFluidInputs[0]);

            for (ItemStack aStack : removeIntegratedCircuitFromStacks(baseRecipe.mInputs)) {
                boolean isItemModified = false;
                boolean isNeedTraverse = true;
                for (Map.Entry<ItemStack, ItemStack> entry : circuitItemsToWrapped.entrySet()) {
                    if (GTUtility.areStacksEqual(entry.getKey(), aStack)) {
                        inputItems.add(copyAmount(aStack.stackSize, entry.getValue()));
                        isItemModified = true;
                        break;
                    }
                }

                if (!isItemModified && BWUtil.checkStackAndPrefix(aStack)) {
                    ItemData Data = Objects.requireNonNull(GTOreDictUnificator.getAssociation(aStack));
                    Materials Material = Data.mMaterial.mMaterial;
                    OrePrefixes OreDict = Data.mPrefix;
                    if (Material.getMolten(1) != null && targetModifyOreDict.contains(OreDict)) {
                        inputFluids.add(
                            Material
                                .getMolten(OreDict.mMaterialAmount * GTValues.L * aStack.stackSize / GTValues.M * 16));
                        isItemModified = true;
                    } else if (superConductorMaterialList.contains(Material)) {
                        inputItems.add(
                            copyAmount(
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
                                .add(setStackSize(entry.getValue(), entry.getValue().amount * aStack.stackSize * 16));
                            isItemModified = true;
                            break;
                        }
                    }
                }

                if (!isItemModified) inputItems.add(copyAmount(aStack.stackSize * 16, aStack));
            }

            rRecipes.add(
                new GTRecipe(
                    false,
                    inputItems.toArray(new ItemStack[0]),
                    new ItemStack[] { copyAmount(baseRecipe.mOutputs[0].stackSize * 16, baseRecipe.mOutputs[0]) },
                    null,
                    null,
                    mergeSameFluid(inputFluids.toArray(new FluidStack[0])),
                    null,
                    baseRecipe.mDuration * 12,
                    baseRecipe.mEUt,
                    0));
        }
        return rRecipes;
    }

    public GTRecipe reduplicateRecipe(GTRecipe oRecipe, int inputItemMultiTimes, int inputFluidMultiTimes,
        int outputItemMultiTimes, int outputFluidMultiTimes, int eutMultiTimes, int durationMultiTimes) {
        ArrayList<ItemStack> inputItems = new ArrayList<>();
        ArrayList<FluidStack> inputFluids = new ArrayList<>();
        ArrayList<ItemStack> outputItems = new ArrayList<>();
        ArrayList<FluidStack> outputFluids = new ArrayList<>();

        if (oRecipe == null) return null;

        for (ItemStack aStack : oRecipe.mInputs) {
            if (aStack != null) inputItems.add(copyAmount(aStack.stackSize * inputItemMultiTimes, aStack));
        }
        for (FluidStack aStack : oRecipe.mFluidInputs) {
            if (aStack != null) inputFluids.add(setStackSize(aStack, aStack.amount * inputFluidMultiTimes));
        }

        for (ItemStack aStack : oRecipe.mOutputs) {
            if (aStack != null) outputItems.add(copyAmount(aStack.stackSize * outputItemMultiTimes, aStack));
        }
        for (FluidStack aStack : oRecipe.mFluidOutputs) {
            if (aStack != null) outputFluids.add(setStackSize(aStack, aStack.amount * outputFluidMultiTimes));
        }

        return new GTRecipe(
            false,
            inputItems.toArray(new ItemStack[0]),
            outputItems.toArray(new ItemStack[0]),
            null,
            null,
            inputFluids.toArray(new FluidStack[0]),
            outputFluids.toArray(new FluidStack[0]),
            oRecipe.mDuration * eutMultiTimes,
            oRecipe.mEUt * durationMultiTimes,
            0);
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

    public FluidStack[] mergeSameFluid(FluidStack[] fluidStacks) {

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

    private boolean isRecipeInputItemSame(GTRecipe a, GTRecipe b) {
        if (!areItemStacksEqual(a.mOutputs[0], b.mOutputs[0])) return false;
        if (a.mInputs.length != b.mInputs.length) return false;
        for (int i = 0; i < a.mInputs.length; i++) {
            if (!areItemStacksEqual(a.mInputs[i], b.mInputs[i])) {
                return false;
            }
        }
        return true;
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

    private static void initStatics() {

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
            ItemList.Circuit_Parts_Resistor.get(1),
            //ItemList.Circuit_Parts_ResistorSMD.get(1),
            ItemList.Circuit_Parts_Coil.get(1),
            //ItemList.Circuit_Parts_InductorSMD.get(1),
            ItemList.Circuit_Parts_Diode.get(1),
            //ItemList.Circuit_Parts_DiodeSMD.get(1),
            ItemList.Circuit_Parts_Transistor.get(1),
            //ItemList.Circuit_Parts_TransistorSMD.get(1),
            ItemList.Circuit_Parts_Capacitor.get(1),
            //ItemList.Circuit_Parts_CapacitorSMD.get(1),
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
        // spotless:on

        int Count = 0;
        for (WarpCircuitItem item : WarpCircuitItem.values()) {
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

    }

    private static Material getMaterialByUnlocalizedName(String unlocalizedName) {

        for (Material material : Material.mMaterialMap) {
            if (material.getUnlocalizedName()
                .equals(unlocalizedName)) {
                return material;
            }
        }

        return null;
    }

    // spotless:off
    public void loadCustumRecipes(){


        final ItemStack ringBlock = GTModHandler.getModItem("SGCraft", "stargateRing" , 1, 0);
        final ItemStack chevronBlock = GTModHandler.getModItem("SGCraft", "stargateRing", 1, 1);
        final ItemStack irisUpgrade = GTModHandler.getModItem("SGCraft", "sgIrisUpgrade" , 1, 0);





        // region ME Storage Component
//        {
//
//            // Item
//            TST_RecipeBuilder
//                .builder()
//                .itemInputs(
//                    GTUtility.getIntegratedCircuit(1),
//                    copyAmount(4, Wrapped_Circuit_UV),
//                    copyAmount(16, Wrapped_Circuit_LuV),
//                    CustomItemList.EngineeringProcessorItemAdvEmeraldCore.get(16),
//                    Wrapped_Circuit_Board_Extreme_Wetware)
//                .fluidInputs(
//                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(72))
//                .itemOutputs(MaterialType.Cell16384kPart.stack(16))
//                .eut(RECIPE_UV)
//                .duration(20 * 10 * 4)
//                .addTo(MT);
//
//            // Fluid
//            TST_RecipeBuilder
//                .builder()
//                .itemInputs(
//                    GTUtility.getIntegratedCircuit(1),
//                    copyAmount(4, Wrapped_Circuit_UV),
//                    copyAmount(16, Wrapped_Circuit_LuV),
//                    ItemList.Electric_Pump_EV.get(32),
//                    EngineeringProcessorFluidEmeraldCore.getIS(16),
//                    Wrapped_Circuit_Board_Extreme_Wetware)
//                .fluidInputs(
//                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(72))
//                .itemOutputs(CellType.Cell16384kPart.stack(16))
//                .eut(RECIPE_UV)
//                .duration(20 * 10 * 4)
//                .addTo(MT);
//
//            // Essentia
//            TST_RecipeBuilder
//                .builder()
//                .itemInputs(
//                    GTUtility.getIntegratedCircuit(1),
//                    copyAmount(4, Wrapped_Circuit_UV),
//                    copyAmount(16, Wrapped_Circuit_LuV),
//                    CustomItemList.EngineeringProcessorEssentiaPulsatingCore.get(16),
//                    Wrapped_Circuit_Board_Extreme_Wetware)
//                .fluidInputs(
//                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(72))
//                .itemOutputs(ThEAPIImplementation.instance().items().EssentiaStorageComponent_16384k.getStacks(16))
//                .eut(RECIPE_UV)
//                .duration(20 * 10 * 4)
//                .addTo(MT);
//
//        }
        // endregion


        // region Quantum Circuit and Piko Circuit
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 2),
                ItemList.Circuit_Parts_CapacitorXSMD.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64),
                ItemList.Circuit_Parts_TransistorXSMD.get(64),
                ItemList.Circuit_Parts_ResistorXSMD.get(64),
                ItemList.Circuit_Chip_QPIC.get(64))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(3744),
                Materials.UUMatter.getFluid(1000 * 24),
                Materials.Osmium.getMolten(144 * 16),
                Materials.Neutronium.getMolten(144 * 8),
                GGMaterial.shirabon.getMolten(144 * 8),
                Materials.Indium.getMolten(144 * 8),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 4),
                Materials.Lanthanum.getMolten(144 * 2))
            .itemOutputs(CustomItemList.QuantumCircuit.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 1000)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(1L),
                CustomItemList.PicoWafer.get(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 2),
                ItemList.Circuit_Parts_TransistorXSMD.get(48L),
                ItemList.Circuit_Parts_ResistorXSMD.get(48L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(48L),
                ItemList.Circuit_Parts_DiodeXSMD.get(48L),
                ItemList.Circuit_Chip_PPIC.get(64L))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(3744),
                Materials.UUMatter.getFluid(1000 * 8),
                Materials.Osmium.getMolten(144 * 8),
                Materials.RadoxPolymer.getMolten(144 * 4),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 4),
                Materials.Neutronium.getMolten(144 * 2),
                Materials.Lanthanum.getMolten(144 * 8))
            .itemOutputs(CustomItemList.PikoCircuit.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 500)
            .addTo(MT);
        // endregion

        // region Optical Component

        // optical cpu
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                setStackSize(ItemList.Circuit_Chip_Optical.get(1), 192),
                setStackSize(ItemList.Optical_Cpu_Containment_Housing.get(1), 192),
                setStackSize(DATApipe.get(1), 192)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1152 * 12),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 24),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 24),
                Materials.Tritanium.getMolten(144 * 24),
                GGMaterial.enrichedNaquadahAlloy.getMolten(144 * 24),
                GGMaterial.shirabon.getMolten(144 * 24)
            )
            .itemOutputs(setStackSize(ItemList.Optically_Perfected_CPU.get(1), 256))
            .eut(RECIPE_UIV)
            .duration(20 * 20 * 12)
            .addTo(MT);

        // optical memory
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                setStackSize(ItemList.Circuit_Chip_Optical.get(1), 192),
                getModItem(SuperSolarPanels.ID, "solarsplitter", 192),
                setStackSize(DATApipe.get(1), 768),
                setStackSize(ItemList.Circuit_Chip_Ram.get(1), 192),
                setStackSize(ItemList.Circuit_Chip_SoC.get(1), 192),
                setStackSize(ItemList.Circuit_Chip_NAND.get(1), 192)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1152 * 12),
                Materials.VanadiumGallium.getMolten(144 * 48),
                Materials.Infinity.getMolten(144 * 48),
                Materials.SuperconductorUMVBase.getMolten(144 * 24),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 7)
            )
            .itemOutputs(setStackSize(ItemList.Optically_Compatible_Memory.get(1), 512))
            .eut(RECIPE_UIV)
            .duration(20 * 20 * 12)
            .addTo(MT);
        // endregion

        // region Optical Circuit
/*
        // Optical SoC frame
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                InfinityInfusedShieldingCore.get(0),
                ItemList.Optical_Cpu_Containment_Housing.get(1),
                Materials.Glowstone.getNanite(4))
            .fluidInputs(
                MaterialsUEVplus.Space.getMolten(36),
                MaterialsUEVplus.Time.getMolten(36))
            .itemOutputs(GTCMItemList.ParticleTrapTimeSpaceShield.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 64)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                SpaceTimeBendingCore.get(0),
                ItemList.Optical_Cpu_Containment_Housing.get(2),
                Materials.Glowstone.getNanite(4))
            .fluidInputs(
                MaterialsUEVplus.Space.getMolten(144),
                MaterialsUEVplus.Time.getMolten(144),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 2))
            .itemOutputs(GTCMItemList.ParticleTrapTimeSpaceShield.get(16))
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(2500))
            .eut(RECIPE_UMV)
            .duration(20 * 64)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                SpaceTimeBendingCore.get(0),
                SpaceTimeSuperconductingInlaidMotherboard.get(1),
                Materials.Glowstone.getNanite(16))
            .fluidInputs(
                MaterialsUEVplus.Space.getMolten(144),
                MaterialsUEVplus.Time.getMolten(144),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 2))
            .itemOutputs(GTCMItemList.ParticleTrapTimeSpaceShield.get(64))
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(2500))
            .eut(TierEU.RECIPE_UMV)
            .duration(20 * 64)
            .addTo(GTCMRecipe.MiracleTopRecipes);

        // Optical Frame
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.Silicone.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.StyreneButadieneRubber.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 32L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.Silicone.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 32L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.StyreneButadieneRubber.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 16L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.Silicone.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 16L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.StyreneButadieneRubber.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        // Optical Computer
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(2L),
                ItemList.Circuit_OpticalAssembly.get(2L),
                ItemList.Circuit_Parts_TransistorXSMD.get(24L),
                ItemList.Circuit_Parts_ResistorXSMD.get(24L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(24L),
                ItemList.Circuit_Parts_DiodeXSMD.get(24L),
                ItemList.Circuit_Chip_NOR.get(64L),
                ItemList.Circuit_Chip_SoC2.get(32L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 20),
                Materials.Radon.getPlasma(144 * 20),
                Materials.SuperCoolant.getFluid(1000L * 20),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000),
                GGMaterial.lumiium.getMolten(144 * 4),
                Materials.Silicone.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalComputer.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 200)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(2L),
                ItemList.Circuit_OpticalAssembly.get(2L),
                ItemList.Circuit_Parts_TransistorXSMD.get(24L),
                ItemList.Circuit_Parts_ResistorXSMD.get(24L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(24L),
                ItemList.Circuit_Parts_DiodeXSMD.get(24L),
                ItemList.Circuit_Chip_NOR.get(64L),
                ItemList.Circuit_Chip_SoC2.get(32L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 20),
                Materials.Radon.getPlasma(144 * 20),
                Materials.SuperCoolant.getFluid(1000L * 20),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000),
                GGMaterial.lumiium.getMolten(144 * 4),
                Materials.StyreneButadieneRubber.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalComputer.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 200)
            .addTo(MT);

        // Optical Assembly
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(1L),
                ItemList.Circuit_OpticalProcessor.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(16L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(20L),
                ItemList.Circuit_Parts_ResistorXSMD.get(20L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.Radon.getPlasma(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                WerkstoffLoader.Oganesson.getFluidOrGas(500),
                GGMaterial.lumiium.getMolten(144 * 3),
                Materials.Silicone.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalAssembly.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(1L),
                ItemList.Circuit_OpticalProcessor.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(16L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(20L),
                ItemList.Circuit_Parts_ResistorXSMD.get(20L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.Radon.getPlasma(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                WerkstoffLoader.Oganesson.getFluidOrGas(500),
                GGMaterial.lumiium.getMolten(144 * 3),
                Materials.StyreneButadieneRubber.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalAssembly.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(MT);

        // Optical Processor
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Optically_Perfected_CPU),
                Utils.copyAmount(2, Wrapped_Optically_Compatible_Memory),
                Utils.copyAmount(16, Wrapped_Circuit_Parts_CapacitorXSMD),
                Utils.copyAmount(16, Wrapped_Circuit_Parts_DiodeXSMD),
                DATApipe.get(64)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 2),
                Materials.EnrichedHolmium.getMolten(144 * 8)
            )
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(16))

            .eut(RECIPE_UHV)
            .duration(20 * 240)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Optical),
                GTCMItemList.OpticalSOC.get(1),
                DATApipe.get(16)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 2),
                Materials.EnrichedHolmium.getMolten(144 * 2)
            )
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(16))

            .eut(9830400)
            .duration(20 * 10)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                Utils.copyAmount(16, Wrapped_Circuit_Board_Optical),
                GTCMItemList.OpticalSOC.get(16),
                DATApipe.get(64)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 32),
                Materials.EnrichedHolmium.getMolten(144 * 16)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_OpticalProcessor.get(64), 64*4))
            .eut(9830400 * 4)
            .duration(20 * 10 * 4)
            .addTo(MT);

        // endregion

        // region Bio Circuit

        // Bio Mainframe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorASMD.get(24L),
                ItemList.Circuit_Parts_TransistorASMD.get(24L),
                ItemList.Circuit_Parts_ResistorASMD.get(24L),
                ItemList.Circuit_Parts_CapacitorASMD.get(24L),
                ItemList.Circuit_Parts_DiodeASMD.get(24L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
                }
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880),
                Materials.BioMediumSterilized.getFluid(2880L),
                Materials.SuperCoolant.getFluid(20_000L),
                Materials.Tritanium.getMolten(144 * 8),
                Materials.Silicone.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biomainframe.get(1L)
            )

            .eut(RECIPE_UHV)
            .duration(20 * 300)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(6L),
                ItemList.Circuit_Parts_TransistorXSMD.get(6L),
                ItemList.Circuit_Parts_ResistorXSMD.get(6L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(6L),
                ItemList.Circuit_Parts_DiodeXSMD.get(6L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
                }
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880),
                Materials.BioMediumSterilized.getFluid(2880L),
                Materials.SuperCoolant.getFluid(20_000L),
                Materials.Tritanium.getMolten(144 * 8),
                Materials.Silicone.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biomainframe.get(1L)
            )

            .eut(RECIPE_UHV)
            .duration(20 * 150)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorASMD.get(24L),
                ItemList.Circuit_Parts_TransistorASMD.get(24L),
                ItemList.Circuit_Parts_ResistorASMD.get(24L),
                ItemList.Circuit_Parts_CapacitorASMD.get(24L),
                ItemList.Circuit_Parts_DiodeASMD.get(24L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
                }
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880),
                Materials.BioMediumSterilized.getFluid(2880L),
                Materials.SuperCoolant.getFluid(20_000L),
                Materials.Tritanium.getMolten(144 * 8),
                Materials.StyreneButadieneRubber.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biomainframe.get(1L)
            )

            .eut(RECIPE_UHV)
            .duration(20 * 300)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(6L),
                ItemList.Circuit_Parts_TransistorXSMD.get(6L),
                ItemList.Circuit_Parts_ResistorXSMD.get(6L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(6L),
                ItemList.Circuit_Parts_DiodeXSMD.get(6L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
                }
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880),
                Materials.BioMediumSterilized.getFluid(2880L),
                Materials.SuperCoolant.getFluid(20_000L),
                Materials.Tritanium.getMolten(144 * 8),
                Materials.StyreneButadieneRubber.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biomainframe.get(1L)
            )

            .eut(RECIPE_UHV)
            .duration(20 * 150)
            .addTo(MT);

        // Bio SuperComputer
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Bio_Ultra.get(2L),
                ItemList.Circuit_Biowarecomputer.get(2L),
                ItemList.Circuit_Parts_TransistorASMD.get(16L),
                ItemList.Circuit_Parts_ResistorASMD.get(16L),
                ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                ItemList.Circuit_Parts_DiodeASMD.get(16L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.BioMediumSterilized.getFluid(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                Materials.NiobiumTitanium.getMolten(144 * 4),
                Materials.Silicone.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biowaresupercomputer.get(1L)
            )

            .eut(RECIPE_UV)
            .duration(20 * 200)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Bio_Ultra.get(2L),
                ItemList.Circuit_Biowarecomputer.get(2L),
                ItemList.Circuit_Parts_TransistorXSMD.get(4L),
                ItemList.Circuit_Parts_ResistorXSMD.get(4L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(4L),
                ItemList.Circuit_Parts_DiodeXSMD.get(4L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.BioMediumSterilized.getFluid(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                Materials.NiobiumTitanium.getMolten(144 * 4),
                Materials.Silicone.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biowaresupercomputer.get(1L)
            )

            .eut(RECIPE_UV)
            .duration(20 * 100)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Bio_Ultra.get(2L),
                ItemList.Circuit_Biowarecomputer.get(2L),
                ItemList.Circuit_Parts_TransistorASMD.get(16L),
                ItemList.Circuit_Parts_ResistorASMD.get(16L),
                ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                ItemList.Circuit_Parts_DiodeASMD.get(16L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.BioMediumSterilized.getFluid(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                Materials.NiobiumTitanium.getMolten(144 * 4),
                Materials.StyreneButadieneRubber.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biowaresupercomputer.get(1L)
            )

            .eut(RECIPE_UV)
            .duration(20 * 200)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Bio_Ultra.get(2L),
                ItemList.Circuit_Biowarecomputer.get(2L),
                ItemList.Circuit_Parts_TransistorXSMD.get(4L),
                ItemList.Circuit_Parts_ResistorXSMD.get(4L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(4L),
                ItemList.Circuit_Parts_DiodeXSMD.get(4L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.BioMediumSterilized.getFluid(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                Materials.NiobiumTitanium.getMolten(144 * 4),
                Materials.StyreneButadieneRubber.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biowaresupercomputer.get(1L)
            )

            .eut(RECIPE_UV)
            .duration(20 * 100)
            .addTo(MT);

        // Bio Assembly
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Bio_Ultra),
                ItemList.Circuit_Bioprocessor.get(32),
                Utils.copyAmount(12, Wrapped_Circuit_Parts_InductorASMD),
                Utils.copyAmount(16, Wrapped_Circuit_Parts_CapacitorASMD),
                Utils.copyAmount(32, Wrapped_Circuit_Chip_Ram)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                Materials.NiobiumTitanium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Biowarecomputer.get(16)
            )

            .eut(RECIPE_UV)
            .duration(20 * 240)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Bio_Ultra),
                ItemList.Circuit_Bioprocessor.get(32),
                Utils.copyAmount(3, Wrapped_Circuit_Parts_InductorXSMD),
                Utils.copyAmount(4, Wrapped_Circuit_Parts_CapacitorXSMD),
                Utils.copyAmount(32, Wrapped_Circuit_Chip_Ram)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                Materials.NiobiumTitanium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Biowarecomputer.get(16)
            )

            .eut(RECIPE_UHV)
            .duration(20 * 30)
            .addTo(MT);

        // Bio Processor
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32714),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32716),
                Utils.copyAmount(2, Wrapped_Circuit_Chip_NanoCPU),
                Utils.copyAmount(12, Wrapped_Circuit_Parts_CapacitorASMD),
                Utils.copyAmount(12, Wrapped_Circuit_Parts_TransistorASMD)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.NiobiumTitanium.getMolten(144 * 32)
            )
            .itemOutputs(
                ItemList.Circuit_Bioprocessor.get(16)
            )

            .eut(RECIPE_UV)
            .duration(20 * 180)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32714),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32716),
                Utils.copyAmount(2, Wrapped_Circuit_Chip_NanoCPU),
                Utils.copyAmount(3, Wrapped_Circuit_Parts_CapacitorXSMD),
                Utils.copyAmount(3, Wrapped_Circuit_Parts_TransistorXSMD)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.NiobiumTitanium.getMolten(144 * 32)
            )
            .itemOutputs(
                ItemList.Circuit_Bioprocessor.get(16)
            )

            .eut(RECIPE_UHV)
            .duration(444)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Bio_Ultra),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32699)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                new FluidStack(FluidRegistry.getFluid("molten.chromaticglass"), 144 * 8),
                Materials.NiobiumTitanium.getMolten(144 * 32)
            )
            .itemOutputs(
                ItemList.Circuit_Bioprocessor.get(16)
            )

            .eut(RECIPE_UEV)
            .duration(450)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                Utils.copyAmount(12, Wrapped_Circuit_Board_Bio_Ultra),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32699)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144 * 12),
                new FluidStack(FluidRegistry.getFluid("molten.chromaticglass"), 144 * 8 * 12),
                Materials.NiobiumTitanium.getMolten(144 * 32 * 12)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Bioprocessor.get(64), 64*4))
            .eut(RECIPE_UIV)
            .duration(450 * 3)
            .addTo(MT);

        // endregion

        // region Wetware Processor SoC
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32750),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32700)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                Materials.YttriumBariumCuprate.getMolten(144 * 16),
                Materials.CosmicNeutronium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Neuroprocessor.get(16)
            )
            .eut(614400)
            .duration(20 * 15)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32750),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32700)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144 * 12),
                Materials.YttriumBariumCuprate.getMolten(144 * 16 * 12),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 12)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Neuroprocessor.get(64), 64*4))
            .eut(614400 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region Crystal Processor SoC
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32753),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32717)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.NiobiumTitanium.getMolten(144 * 16),
                Materials.YttriumBariumCuprate.getMolten(144 * 8))
            .itemOutputs(
                ItemList.Circuit_Crystalprocessor.get(16)
            )

            .eut(153600)
            .duration(20 * 15)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32753),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32717)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72 * 12),
                Materials.NiobiumTitanium.getMolten(144 * 16 * 12),
                Materials.YttriumBariumCuprate.getMolten(144 * 8 * 12))
            .itemOutputs(setStackSize(ItemList.Circuit_Crystalprocessor.get(64), 64*4))
            .eut(153600 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region Quantum Processor SoC
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32754),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.Platinum.getMolten(144 * 32),
                Materials.NiobiumTitanium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Quantumprocessor.get(16)
            )

            .eut(38400)
            .duration(20 * 15)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32754),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72 * 12),
                Materials.Platinum.getMolten(144 * 32 * 12),
                Materials.NiobiumTitanium.getMolten(144 * 8 * 12)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Quantumprocessor.get(64), 64*4))
            .eut(38400 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region Nano Processor SoC
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32756),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.Electrum.getMolten(144 * 16),
                Materials.Platinum.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Nanoprocessor.get(16)
            )
            .eut(9600)
            .duration(20 * 15)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32756),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72 * 12),
                Materials.Electrum.getMolten(144 * 16 * 12),
                Materials.Platinum.getMolten(144 * 8 * 12)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Nanoprocessor.get(64), 64*4))
            .eut(9600 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region High Energy Flow Circuit
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32753),
                GTModHandler.getModItem("GoodGenerator", "circuitWrap", 2, 7),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 8, 32721)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 288),
                Materials.Infinity.getMolten(144)
            )
            .itemOutputs(
                GTModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 16)
            )

            .eut(RECIPE_IV)
            .duration(20 * 720)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32753),
                GTModHandler.getModItem("GoodGenerator", "circuitWrap", 24, 7),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 48, 32721)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 288 * 12),
                Materials.Infinity.getMolten(144 * 12)
            )
            .itemOutputs(setStackSize(GTModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 64),64*4))
            .eut(RECIPE_LuV)
            .duration(20 * 720 * 3)
            .addTo(MT);

        //ULV LV and MV circuit
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32756),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 48, 32728)
            )
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(18 * 4),
                Materials.AnnealedCopper.getMolten(144 * 2 * 4),
                Materials.RedAlloy.getMolten(144 * 8)
            )
            .itemOutputs(setStackSize(CustomItemList.NandChipBoard.get(64), 64*16))
            .eut(RECIPE_EV)
            .duration(20 * 90 * 4)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32748),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32731)
            )
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(72 * 12),
                Materials.Copper.getMolten(144 * 96)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Microprocessor.get(64),64*8))
            .eut(600)
            .duration(20 * 30 * 4)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32748),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32731)
            )
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(72 * 12),
                Materials.AnnealedCopper.getMolten(144 * 8 * 12),
                Materials.RedAlloy.getMolten(144 * 24)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Processor.get(1), 64*4))
            .eut(RECIPE_EV)
            .duration(20 * 90 * 4)
            .addTo(MT);

        // endregion

        // region Neuro Processing Unit and Bio Processing Unit
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32750),
                setStackSize(ItemList.Circuit_Chip_Stemcell.get(64), 64*4)
            )
            .fluidInputs(
                Materials.ReinforceGlass.getMolten(16 * 16 * 288),
                Materials.Polybenzimidazole.getMolten(16 * 8 * 72),
                Materials.NaquadahEnriched.getMolten(16 * 4 * 72),
                Materials.Silicone.getMolten(16 * 16 * 144),
                Materials.TungstenSteel.getMolten(16 * 32 * 18),
                Materials.GrowthMediumSterilized.getFluid(16 * 250),
                Materials.UUMatter.getFluid(16 * 250),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000 * 16)
            )
            .itemOutputs(ItemList.Circuit_Chip_NeuroCPU.get(16))
            .eut(RECIPE_ZPM)
            .duration(20 * 30 * 12)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32746),
                setStackSize(ItemList.Circuit_Chip_Biocell.get(64), 64*4)
            )
            .fluidInputs(
                Materials.ReinforceGlass.getMolten(16 * 16 * 288),
                Materials.Polybenzimidazole.getMolten(16 * 16 * 72),
                Materials.ElectrumFlux.getMolten(16 * 16 * 72),
                Materials.Silicone.getMolten(16 * 16 * 144),
                Materials.HSSS.getMolten(16 * 32 * 18),
                Materials.BioMediumSterilized.getFluid(16 * 500),
                Materials.UUMatter.getFluid(16 * 500),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000 * 16)
            )
            .itemOutputs(ItemList.Circuit_Chip_BioCPU.get(16))
            .eut(RECIPE_UHV)
            .duration(20 * 30 * 12)
            .addTo(MT);
        // endregion

        */



        // region Proof Of Heroes
//        GTValues.RA.stdBuilder()
//            .itemInputs(
//                GTCMItemList.SpaceWarper.get(64),
//                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 64),
//                GTModHandler.getModItem("eternalsingularity", "combined_singularity", 64, 15),
//                ItemList.Timepiece.get(64),
//                ItemList.GigaChad.get(64),
//                tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
//                tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
//                tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(64),
//                GTModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 64),
//                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 15),
//                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 16),
//                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 20),
//                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 21),
//                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 17),
//                ItemList.ZPM6.get(64),
//                GTCMItemList.IndistinctTentacle.get(64)
//            )
//            .fluidInputs(
//                MaterialsUEVplus.Time.getMolten(1000 * 114514),
//                MaterialsUEVplus.Space.getMolten(1000 * 114514),
//                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1000 * 114514),
//                GGMaterial.shirabon.getMolten(1000 * 114514),
//                MaterialsUEVplus.Universium.getMolten(1000 * 114514),
//                MaterialsUEVplus.Eternity.getMolten(1000 * 114514),
//                MaterialsUEVplus.PrimordialMatter.getFluid(1000 * 114514)
//            )
//            .itemOutputs(GTCMItemList.ProofOfHeroes.get(1))
//            .noOptimize()
//            .specialValue(13500)
//            .eut(RECIPE_MAX)
//            .duration(20 * 1919810)
//            .addTo(MT);


        // endregion

        // region Endgame Challenge content

        // Liquid Stargate
//        GTValues.RA.stdBuilder()
//            .itemInputs(
//                Utils.copyAmount(1, ringBlock),
//                Utils.copyAmount(1, chevronBlock),
//                Utils.copyAmount(1, chevronBlock),
//                Utils.copyAmount(1, ringBlock),
//
//                Utils.copyAmount(1, chevronBlock),
//                Utils.copyAmount(1, irisUpgrade),
//                Utils.copyAmount(1, irisUpgrade),
//                Utils.copyAmount(1, chevronBlock),
//
//                Utils.copyAmount(1, ringBlock),
//                Utils.copyAmount(1, irisUpgrade),
//                Utils.copyAmount(1, irisUpgrade),
//                Utils.copyAmount(1, ringBlock),
//
//                Utils.copyAmount(1, chevronBlock),
//                Utils.copyAmount(1, ringBlock),
//                Utils.copyAmount(1, ringBlock),
//                Utils.copyAmount(1, chevronBlock)
//            )
//            .fluidInputs(
//                MaterialPool.StabiliseVoidMatter.getFluidOrGas(1_000)
//            )
//            .fluidOutputs(MaterialPool.LiquidStargate.getFluidOrGas(1000))
//            .noOptimize()
//            .specialValue(13500)
//            .eut(RECIPE_MAX)
//            .duration(20 *99_999_999)
//            .addTo(MT);
//
//        // StabiliseVoidMatter
//        TST_RecipeBuilder
//            .builder()
//            .itemInputs(
//                setStackSize(Materials.CosmicNeutronium.getDust(1), 10_000_000),
//                setStackSize(Materials.Bedrockium.getDust(1),10_000_000),
//                setStackSize(Materials.Carbon.getDust(1),10_000_000),
//                setStackSize(Materials.Oilsands.getDust(1),10_000_000),
//                setStackSize(Materials.NiobiumTitanium.getDust(1),10_000_000),
//                setStackSize(MaterialsElements.STANDALONE.BLACK_METAL.getDust(1),10_000_000),
//                setStackSize(Materials.Naquadria.getDust(1),10_000_000),
//                setStackSize(Materials.Obsidian.getDust(1),10_000_000),
//                setStackSize(Materials.Coal.getDust(1),10_000_000),
//                setStackSize(Materials.NaquadahAlloy.getDust(1),10_000_000),
//                setStackSize(Materials.Tungsten.getDust(1),10_000_000),
//                setStackSize(MaterialsUEVplus.TranscendentMetal.getDust(1),10_000_000),
//                setStackSize(Materials.Perlite.getDust(1),10_000_000),
//                setStackSize(Materials.DarkAsh.getDust(1),10_000_000),
//                setStackSize(Materials.GraniticMineralSand.getDust(1),10_000_000),
//                setStackSize(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1),10_000_000)
//            )
//            .fluidInputs(
//                Materials.Polycaprolactam.getMolten(10_000_000),
//                Materials.NickelZincFerrite.getMolten(10_000_000),
//                Materials.DarkSteel.getMolten(10_000_000),
//                Materials.Polybenzimidazole.getMolten(10_000_000),
//                GGMaterial.tairitsu.getMolten(10_000_000),
//                Materials.Tungsten.getMolten(10_000_000),
//                GGMaterial.marM200.getMolten(10_000_000),
//                Materials.Vanadium.getMolten(10_000_000),
//                MaterialsElements.STANDALONE.BLACK_METAL.getFluidStack(10_000_000),
//                Materials.ShadowIron.getMolten(10_000_000),
//                Materials.NaquadahAlloy.getMolten(10_000_000),
//                Materials.ShadowSteel.getMolten(10_000_000),
//                Materials.Cadmium.getMolten(10_000_000),
//                Materials.Desh.getMolten(10_000_000),
//                Materials.BlackPlutonium.getMolten(10_000_000),
//                Materials.BlackSteel.getMolten(10_000_000),
//                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(10_000_000)
//            )
//            .fluidOutputs(MaterialPool.StabiliseVoidMatter.getFluidOrGas(1))
//            .eut(RECIPE_MAX)
//            .duration(20 * 99_999_999)
//            .addTo(MT);

        // ProofOfGods
        // TODO -- Temporarily, be revised in the next version
//        TST_RecipeBuilder
//            .builder()
//            .itemInputs(
//                GTCMItemList.UxvFlask.get(1),
//                GTCMItemList.ProofOfHeroes.get(64),
//                setStackSize(Materials.Silver.getNanite(1), 1_000),
//                setStackSize(Materials.Gold.getNanite(1), 1_000),
//                setStackSize(Materials.Neutronium.getNanite(1), 1_000),
//                setStackSize(MaterialsUEVplus.Universium.getNanite(1), 1_000),
//                setStackSize(MaterialsUEVplus.Eternity.getNanite(1), 1_000),
//                setStackSize(MaterialsUEVplus.TranscendentMetal.getNanite(1), 1_000),
//                setStackSize(Materials.Glowstone.getNanite(1), 1_000),
//                setStackSize(MaterialsUEVplus.WhiteDwarfMatter.getNanite(1), 1_000),
//                setStackSize(MaterialsUEVplus.BlackDwarfMatter.getNanite(1), 1_000)
//            )
//            .fluidInputs(
//                MaterialPool.LiquidStargate.getFluidOrGas(50_000),
//                MaterialPool.StabiliseVoidMatter.getFluidOrGas(1_000)
//            )
//            .itemOutputs(
//                GTCMItemList.ProofOfGods.get(1)
//            )
//            .eut(RECIPE_MAX)
//            .duration(20 * 99_999_999)
//            .addTo(MT);

        // FLASK
//        loadFlaskRecipe();

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

//    HighDimensionalExtend,
//    HighDimensionalCircuitDoard,

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
    public enum WarpCircuitItem{
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

        Warp_Circuit_Parts_Crystal_Chip_Elite,
        Warp_Circuit_Parts_Crystal_Chip_Master,
        Warp_Circuit_Board_Coated,
        Warp_Circuit_Board_Coated_Basic,
        Warp_Circuit_Board_Phenolic,
        Warp_Circuit_Board_Phenolic_Good,
        Warp_Circuit_Board_Epoxy,
        Warp_Circuit_Board_Epoxy_Advanced,
        Warp_Circuit_Board_Fiberglass,
        Warp_Circuit_Board_Fiberglass_Advanced,
        Warp_Circuit_Board_Multifiberglass_Elite,
        Warp_Circuit_Board_Multifiberglass,
        Warp_Circuit_Board_Wetware,
        Warp_Circuit_Board_Wetware_Extreme,
        Warp_Circuit_Board_Plastic,
        Warp_Circuit_Board_Plastic_Advanced,
        Warp_Circuit_Board_Bio,
        Warp_Circuit_Board_Bio_Ultra,
        Warp_Circuit_Parts_ResistorSMD,
        Warp_Circuit_Parts_InductorSMD,
        Warp_Circuit_Parts_DiodeSMD,
        Warp_Circuit_Parts_TransistorSMD,
        Warp_Circuit_Parts_CapacitorSMD,
        Warp_Circuit_Parts_ResistorASMD,
        Warp_Circuit_Parts_DiodeASMD,
        Warp_Circuit_Parts_TransistorASMD,
        Warp_Circuit_Parts_CapacitorASMD,
        Warp_Circuit_Chip_ILC,
        Warp_Circuit_Chip_Ram,
        Warp_Circuit_Chip_NAND,
        Warp_Circuit_Chip_NOR,
        Warp_Circuit_Chip_CPU,
        Warp_Circuit_Chip_SoC,
        Warp_Circuit_Chip_SoC2,
        Warp_Circuit_Chip_PIC,
        Warp_Circuit_Chip_Simple_SoC,
        Warp_Circuit_Chip_HPIC,
        Warp_Circuit_Chip_UHPIC,
        Warp_Circuit_Chip_ULPIC,
        Warp_Circuit_Chip_LPIC,
        Warp_Circuit_Chip_NPIC,
        Warp_Circuit_Chip_PPIC,
        Warp_Circuit_Chip_QPIC,
        Warp_Circuit_Chip_NanoCPU,
        Warp_Circuit_Chip_QuantumCPU,
        Warp_Circuit_Chip_CrystalCPU,
        Warp_Circuit_Chip_CrystalSoC,
        Warp_Circuit_Chip_CrystalSoC2,
        Warp_Circuit_Chip_NeuroCPU,
        Warp_Circuit_Chip_BioCPU,
        Warp_Circuit_Chip_Stemcell,
        Warp_Circuit_Chip_Biocell,
        Warp_Circuit_Parts_ResistorXSMD,
        Warp_Circuit_Parts_DiodeXSMD,
        Warp_Circuit_Parts_TransistorXSMD,
        Warp_Circuit_Parts_CapacitorXSMD,
        Warp_Circuit_Parts_InductorASMD,
        Warp_Circuit_Parts_InductorXSMD,
        Warp_Circuit_Chip_Optical,
        Warp_Circuit_Board_Optical,
        Warp_Optically_Perfected_CPU,
        Warp_Optical_Cpu_Containment_Housing,
        Warp_Optically_Compatible_Memory,
        Warp_Circuit_Parts_Crystal_Chip_Wetware,
        Warp_Circuit_Parts_Chip_Bioware;
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
