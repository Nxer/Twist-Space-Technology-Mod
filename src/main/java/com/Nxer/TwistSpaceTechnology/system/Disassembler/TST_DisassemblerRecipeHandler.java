package com.Nxer.TwistSpaceTechnology.system.Disassembler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.google.common.collect.Sets;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;

public class TST_DisassemblerRecipeHandler {

    public static final Map<TST_ItemID, TST_SimpleDisassemblyRecipe> DisassemblerRecipeMap = new HashMap<>();

    public static boolean initialized = false;

    public static void initDisassemblerRecipes() {
        if (initialized) return;
        initialized = true;

        new initializer().initDisassemblerRecipes();
    }

    private static class initializer {

        public void initDisassemblerRecipes() {
            processSpecialDisassemblyRecipes();
            generateDisassemblyRecipes(GoodGeneratorRecipeMaps.componentAssemblyLineRecipes.getAllRecipes());
            generateDisassemblyRecipes(GTCMRecipe.MiracleTopRecipes.getAllRecipes());
            generateDisassemblyRecipes(RecipeMaps.assemblylineVisualRecipes.getAllRecipes());
            generateDisassemblyRecipes(RecipeMaps.assemblerRecipes.getAllRecipes());
            generateDisassemblyRecipes(GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipes.getAllRecipes());
        }

        private void processSpecialDisassemblyRecipes() {
            DisassemblerRecipeMap.put(
                TST_ItemID.createNoNBT(ItemList.Casing_Fusion_Coil.get(1)),
                new TST_SimpleDisassemblyRecipe()
                    .setItemToDisassemble(TST_ItemID.createNoNBT(ItemList.Casing_Fusion_Coil.get(1)))
                    .setItemAmount(1)
                    .setOutputItems(
                        ItemList.Casing_Coil_Superconductor.get(1),
                        ItemList.Neutron_Reflector.get(2),
                        ItemList.Field_Generator_MV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 4))
                    .setEut(480)
                    .setTier(GT_Utility.getTier(480)));
        }

        private final Set<TST_ItemID> blackList = Sets.newHashSet(
            TST_ItemID.createNoNBT(ItemList.Casing_Coil_Superconductor.get(1)),
            TST_ItemID.createNoNBT(ItemList.Casing_Fusion_Coil.get(1)),
            TST_ItemID.createNoNBT(Materials.Carbon.getNanite(1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1)),
            TST_ItemID.createNoNBT(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 1)));

        private void generateDisassemblyRecipes(Collection<GT_Recipe> originRecipes) {
            if (originRecipes == null || originRecipes.isEmpty()) return;
            for (GT_Recipe recipe : originRecipes) {
                try {
                    if (recipe == null || recipe.mOutputs == null || recipe.mOutputs.length != 1) continue;
                    if (recipe.mFluidOutputs != null && recipe.mFluidOutputs.length > 0) continue;
                    ItemStack toDisassemble = recipe.mOutputs[0];
                    if (toDisassemble == null) continue;
                    if (isGTTool(toDisassemble)) continue;
                    TST_ItemID itemIDtd = TST_ItemID.createNoNBT(toDisassemble);
                    if (blackList.contains(itemIDtd)) continue;
                    if (DisassemblerRecipeMap.containsKey(itemIDtd)) continue;

                    TST_SimpleDisassemblyRecipe disassemblyRecipe = new TST_SimpleDisassemblyRecipe()
                        .setItemToDisassemble(itemIDtd)
                        .setItemAmount(toDisassemble.stackSize)
                        .setOutputItems(recipe.mInputs)
                        .setOutputFluids(recipe.mFluidInputs)
                        .setEut(recipe.mEUt)
                        .setTier(GT_Utility.getTier(recipe.mEUt));

                    DisassemblerRecipeMap.put(itemIDtd, disassemblyRecipe);

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }

        private boolean isGTTool(ItemStack itemStack) {
            return itemStack.getItem() instanceof GT_MetaGenerated_Tool_01;
        }

    }
}
