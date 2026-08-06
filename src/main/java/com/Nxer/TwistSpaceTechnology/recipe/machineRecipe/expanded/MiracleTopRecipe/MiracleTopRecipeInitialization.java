package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addGTCircuitOreDictNames;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gtPlusPlus.core.material.Material.mComponentMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.dreammaster.item.NHItemList;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.Material;

public final class MiracleTopRecipeInitialization {

    private MiracleTopRecipeInitialization() {}

    static RecipeMap<?>[] NAC_UNWRAP_RECIPE_MAPS;
    static final HashMap<TST_ItemID, ItemStack> circuitItemWrappedMap = new HashMap<>();
    static final HashSet<Materials> superConductorMaterialList = new HashSet<>();
    static final HashSet<OrePrefixes> targetModifyOreDict = new HashSet<>();
    static final HashSet<String> circuitGTOreDict = new HashSet<>();
    static final HashSet<TST_ItemID> recipeComparisonWhitelist = new HashSet<>();
    static final HashMap<ItemStack, FluidStack> specialMaterialCantAutoModify = new HashMap<>();

    public static void init() {
        circuitItemWrappedMap.clear();
        superConductorMaterialList.clear();
        targetModifyOreDict.clear();
        circuitGTOreDict.clear();
        recipeComparisonWhitelist.clear();
        specialMaterialCantAutoModify.clear();

        NAC_UNWRAP_RECIPE_MAPS = new RecipeMap<?>[] { RecipeMaps.nanochipSMDProcessorRecipes,
            RecipeMaps.nanochipBoardProcessorRecipes, RecipeMaps.nanochipEtchingArray,
            RecipeMaps.nanochipCuttingChamber, RecipeMaps.nanochipWireTracer, RecipeMaps.nanochipSuperconductorSplitter,
            RecipeMaps.nanochipOpticalOrganizer, RecipeMaps.nanochipEncasementWrapper,
            RecipeMaps.nanochipBiologicalCoordinator };

        // Outputs in this list keep every scanned recipe and skip the final recipe comparison.
        recipeComparisonWhitelist.add(TST_ItemID.create(ItemList.Optically_Perfected_CPU.get(1)));

        // spotless:off

        // Map circuit items to their wrapped forms.
            // GT ore-dict circuits.
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ULV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 0));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 1));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 2));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 3));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 4));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 5));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 6));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 7));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 8));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 9));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 10));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 11));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 12));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 13));
            circuitItemWrappedMap.put(TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MAX, 1)), getModItem("GoodGenerator", "circuitWrap", 1, 14));

            // Circuit boards with direct wrapped mappings.
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Coated_Basic.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32760));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Phenolic_Good.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32758));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Plastic_Advanced.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32748));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Epoxy_Advanced.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32756));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Fiberglass_Advanced.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32754));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Multifiberglass_Elite.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32753));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Wetware_Extreme.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32750));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Bio_Ultra.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32746));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Optical.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32704));

            // Legacy boards and special chips with direct wrapped mappings.
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32763));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_Crystal_Chip_Master.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32762));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Coated.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32761));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Phenolic.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32759));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Epoxy.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32757));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Fiberglass.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32755));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Multifiberglass.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32752));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Wetware.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32751));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Plastic.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32749));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Board_Bio.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32747));

            // Basic SMD, ASMD and XSMD circuit parts.
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_ResistorSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32745));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_ResistorASMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32740));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_ResistorXSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32711));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_DiodeSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32743));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_DiodeASMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32739));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_DiodeXSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32710));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_TransistorSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32742));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_TransistorASMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32738));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_TransistorXSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32709));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_CapacitorSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32741));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_CapacitorASMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32737));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_CapacitorXSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32708));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_InductorSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32744));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_InductorASMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32707));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_InductorXSMD.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32706));

            // IC and chip parts with direct wrapped mappings.
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_ILC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32736));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_Ram.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32735));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_NAND.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32734));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_NOR.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32733));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_CPU.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32732));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_SoC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32731));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_SoC2.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32730));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_PIC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32729));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_Simple_SoC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32728));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_HPIC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32727));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_UHPIC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32726));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_ULPIC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32725));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_LPIC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32724));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_NPIC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32723));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_PPIC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32722));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_QPIC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32721));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_NanoCPU.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32720));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_QuantumCPU.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32719));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_CrystalCPU.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32718));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_CrystalSoC.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32717));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_CrystalSoC2.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32716));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_NeuroCPU.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32715));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_BioCPU.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32714));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_Stemcell.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32713));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_Biocell.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32712));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32700));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_Chip_Bioware.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32699));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Chip_Optical.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32705));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Optically_Perfected_CPU.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32703));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Optical_Cpu_Containment_Housing.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32702));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Optically_Compatible_Memory.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32701));

            // Basic parts use their SMD wrapped items.
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_Resistor.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32745));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_Coil.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32744));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_Diode.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32743));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_Transistor.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32742));
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_Parts_Capacitor.get(1)), getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32741));

            // Custom circuits reuse matching wrapped circuit tiers.
            circuitItemWrappedMap.put(TST_ItemID.create(ItemList.Circuit_OpticalMainframe.get(1)), getModItem("GoodGenerator", "circuitWrap", 1, 11));
            circuitItemWrappedMap.put(TST_ItemID.create(NHItemList.PikoCircuit.get(1)), getModItem("GoodGenerator", "circuitWrap", 1, 12));
            circuitItemWrappedMap.put(TST_ItemID.create(NHItemList.QuantumCircuit.get(1)), getModItem("GoodGenerator", "circuitWrap", 1, 13));

        // spotless:on

        // Initialize the GTPP material conversion map.
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

            for (Map.Entry<String, ItemStack> innerEntry : innerMap.entrySet()) {
                String orePrefixName = innerEntry.getKey();
                ItemStack aStack = innerEntry.getValue();

                OrePrefixes OreDict = OrePrefixes.getPrefix(orePrefixName);

                int amount = (int) (OreDict.getMaterialAmount() * INGOTS * aStack.stackSize / GTValues.M);
                FluidStack fluidStack = material.getFluidStack(amount);

                if (fluidStack != null) {
                    specialMaterialCantAutoModify.put(aStack, fluidStack);
                }
            }
        }

        specialMaterialCantAutoModify
            .put(ItemList.Circuit_Parts_Reinforced_Glass_Tube.get(1), Materials.ReinforcedGlass.getMolten(288));

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

        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ULV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1));
        addGTCircuitOreDictNames(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MAX, 1));
    }

}
