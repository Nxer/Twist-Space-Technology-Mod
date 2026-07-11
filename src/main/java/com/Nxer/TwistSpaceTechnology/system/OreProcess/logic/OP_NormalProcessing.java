package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeDuration;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeEUt;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.SpecialProcessingLineMaterialInstead;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.google.common.collect.Sets;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import ic2.core.Ic2Items;

public class OP_NormalProcessing {

    public static final Map<Materials, ItemStack> processingLineMaterials = new HashMap<>();

    public static void initProcessingLineMaterials() {
        processingLineMaterials.put(Materials.Platinum, WerkstoffLoader.PTMetallicPowder.get(OrePrefixes.dust, 1));
        processingLineMaterials.put(Materials.Palladium, WerkstoffLoader.PDMetallicPowder.get(OrePrefixes.dust, 1));
        processingLineMaterials.put(Materials.Iridium, WerkstoffLoader.IrLeachResidue.get(OrePrefixes.dust, 1));
        processingLineMaterials.put(Materials.Osmium, WerkstoffLoader.IrOsLeachResidue.get(OrePrefixes.dust, 1));
        processingLineMaterials
            .put(Materials.Samarium, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
        processingLineMaterials
            .put(Materials.Cerium, WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 1));
    }

    // public final List<Integer> insteadMaterialOresMetas = Arrays.asList(
    // 19, 20, 28, 32, 33, 35, 57, 86, 89, 98, 347, 382, 500, 501, 514, 522, 526, 530,
    // 535, 540, 541, 542, 543, 544, 545, 770, 810, 817, 826, 884, 894, 918, 920
    // );

    public static ItemStack getDustStack(Materials material, int amount) {
        if (SpecialProcessingLineMaterialInstead) {
            ItemStack t = processingLineMaterials.get(material);
            if (t != null) {
                return GTUtility.copyAmountUnsafe(amount * 3, t);
            }
        }
        return GTUtility.copyAmountUnsafe(amount, GTOreDictUnificator.get(OrePrefixes.dust, material, 1));
    }

    /**
     * Generate recipes.
     */
    public static void enumOreProcessingRecipes() {
        initProcessingLineMaterials();
        Set<Materials> specialProcesses = Sets.newHashSet(
            Materials.Samarium,
            Materials.Cerium,
            Materials.Naquadah,
            Materials.NaquadahEnriched,
            Materials.Naquadria);

        processOreDictionaryRecipes(specialProcesses);

        OP_GTPP_OreHandler.processGTPPOreRecipes();
        OP_Bartworks_OreHandler.processBWOreRecipes();

        processSpecialOreRecipe();
    }

    /**
     * Generate special ores recipes
     */
    public static void processSpecialOreRecipe() {
        // spotless:off

        // Cerium ore
        {
            ItemStack[] outputs = new ItemStack[] {
                WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 11) };
            ItemStack[] outputsRich = new ItemStack[] {
                WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 22) };
            registryOreDictionaryRecipes(Materials.Cerium, outputs, outputsRich);
        }

        // Samarium Ore
        {
            ItemStack[] outputs = new ItemStack[] {
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 11) };
            ItemStack[] outputsRich = new ItemStack[] {
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 22) };
            registryOreDictionaryRecipes(Materials.Samarium, outputs, outputsRich);
        }

        // Naquadah Ore
        {
            ItemStack[] outputs = new ItemStack[] { GGMaterial.naquadahEarth.get(OrePrefixes.dust, 8),
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 3), };
            ItemStack[] outputsRich = new ItemStack[] { GGMaterial.naquadahEarth.get(OrePrefixes.dust, 16),
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 8), };
            registryOreDictionaryRecipes(Materials.Naquadah, outputs, outputsRich);
        }

        // Enriched Naquadah Ore
        {
            ItemStack[] outputs = new ItemStack[] { GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 8),
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 3) };
            ItemStack[] outputsRich = new ItemStack[] { GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 16),
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 6) };
            registryOreDictionaryRecipes(Materials.NaquadahEnriched, outputs, outputsRich);
        }

        // Naquadria Ore
        {
            ItemStack[] outputs = new ItemStack[] { GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 8),
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 3), };
            ItemStack[] outputsRich = new ItemStack[] { GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 16),
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 6), };
            registryOreDictionaryRecipes(Materials.Naquadria, outputs, outputsRich);
        }

        // Tinker Construct
        // Cobalt ore
        processOreRecipe(
            getModItem("TConstruct","SearedBrick", 1, 1),
            Materials.Cobalt,
            true
        );

        // Ardite ore
        processOreRecipe(
            getModItem("TConstruct","SearedBrick", 1, 2),
            Materials.Ardite,
            true
        );

        // IC2 Uranium ore
        processOreRecipe(
                GTUtility.copyAmountUnsafe(1, Ic2Items.uraniumOre),
                Materials.Uranium,
                false
        );

        // HEE end powder
        registryOreProcessRecipe(
            getModItem("HardcoreEnderExpansion","end_powder_ore",1),
            new ItemStack[]{getModItem("HardcoreEnderExpansion", "end_powder", 24)}
        );

        // Minecraft Iron ore
        processOreRecipe(
            new ItemStack(Blocks.iron_ore),
            Materials.Iron,
            false
        );

        // spotless:on
    }

    /**
     * Generate normal ore recipes from OreDictionary.
     *
     * @param excludedMaterials The special Materials to exclude.
     */
    private static void processOreDictionaryRecipes(Set<Materials> excludedMaterials) {
        Map<Materials, ItemStack[]> outputs = new HashMap<>();
        Map<Materials, ItemStack[]> outputsRich = new HashMap<>();
        Set<Integer> registered = new HashSet<>();
        for (String oreName : OreDictionary.getOreNames()) {
            if (!isProcessableOreName(oreName)) continue;
            for (ItemStack oreStack : OreDictionary.getOres(oreName)) {
                ItemData association = GTOreDictUnificator.getAssociation(oreStack);
                if (association == null || association.mMaterial == null || association.mMaterial.mMaterial == null)
                    continue;
                Materials material = association.mMaterial.mMaterial.mMaterialInto;
                if (material == null || excludedMaterials.contains(material)) continue;
                int stackId = GTUtility.stackToInt(oreStack);
                if (!registered.add(stackId)) continue;
                ItemStack[] normalOutputs = outputs.computeIfAbsent(material, key -> getOutputs(key, false));
                if (normalOutputs.length == 0) continue;
                ItemStack[] richOutputs = outputsRich.computeIfAbsent(material, key -> getOutputs(key, true));
                registryOreProcessRecipe(
                    GTUtility.copyAmountUnsafe(1, oreStack),
                    isRichOre(association.mPrefix) ? richOutputs : normalOutputs);
            }
        }
    }

    /**
     * Register special ore recipes from OreDictionary.
     *
     * @param material    The ore's Material.
     * @param outputs     The normal ore outputs.
     * @param outputsRich The rich ore outputs.
     */
    private static void registryOreDictionaryRecipes(Materials material, ItemStack[] outputs, ItemStack[] outputsRich) {
        Set<Integer> registered = new HashSet<>();
        for (String oreName : OreDictionary.getOreNames()) {
            if (!isProcessableOreName(oreName)) continue;
            for (ItemStack oreStack : OreDictionary.getOres(oreName)) {
                ItemData association = GTOreDictUnificator.getAssociation(oreStack);
                if (association == null || association.mMaterial == null
                    || association.mMaterial.mMaterial == null
                    || association.mMaterial.mMaterial.mMaterialInto != material) continue;
                if (!registered.add(GTUtility.stackToInt(oreStack))) continue;
                registryOreProcessRecipe(
                    GTUtility.copyAmountUnsafe(1, oreStack),
                    isRichOre(association.mPrefix) ? outputsRich : outputs);
            }
        }
    }

    /**
     * Check is this OreDictionary name a processable ore style.
     *
     * @param oreName The OreDictionary name to check.
     * @return True is processable ore.
     */
    private static boolean isProcessableOreName(String oreName) {
        return oreName != null
            && (oreName.startsWith("rawOre") || oreName.startsWith("ore") && !oreName.startsWith("oreSmall")
                && !oreName.startsWith("orePoor")
                && !oreName.startsWith("oreNormal"));
    }

    /**
     * Check is this OrePrefix a rich ore style.
     *
     * @param prefix The OrePrefix to check.
     * @return True is rich ore.
     */
    private static boolean isRichOre(OrePrefixes prefix) {
        if (prefix == OrePrefixes.oreRich) return true;
        for (StoneType stoneType : StoneType.values()) {
            if (stoneType.getPrefix() == prefix) return stoneType.isRich();
        }
        return false;
    }

    /**
     * Process other mods' ore but normal style.
     *
     * @param inputOreItems Input ore item stack.
     * @param material      Input ore's material in GT design.
     * @param isRich        Is this ore a rich type.
     */
    public static void processOreRecipe(ItemStack inputOreItems, Materials material, boolean isRich) {
        registryOreProcessRecipe(inputOreItems, getOutputs(material, isRich));
    }

    public static ItemStack[] getOutputs(Materials material, boolean isRich) {
        List<ItemStack> outputs = new ArrayList<>();

        // check byproduct
        if (!material.mOreByProducts.isEmpty()) {
            // the basic output the material
            addValidOutput(outputs, getDustStack(material, 4));
            if (material.mOreByProducts.size() == 1) {
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null) continue;
                    addValidOutput(outputs, getDustStack(byproduct, 3));
                }
            } else {
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null || byproduct == Materials.Netherrack
                        || byproduct == Materials.Endstone
                        || byproduct == Materials.Stone) continue;

                    addValidOutput(outputs, getDustStack(byproduct, 2));
                }
            }

        } else {
            addValidOutput(outputs, getDustStack(material, 8));
        }

        // check gem style
        if (GTOreDictUnificator.get(OrePrefixes.gem, material, 1) != null) {
            if (GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1) != null) {
                // has gem style
                addValidOutput(outputs, GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1));
                addValidOutput(outputs, GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, 2));
                addValidOutput(outputs, GTOreDictUnificator.get(OrePrefixes.gem, material, 2));

            } else {
                // just normal gem
                addValidOutput(outputs, GTOreDictUnificator.get(OrePrefixes.gem, material, 4));
            }
        }

        if (isRich) {
            for (ItemStack out : outputs) {
                out.stackSize *= 2;
            }
        }

        return outputs.toArray(new ItemStack[0]);
    }

    /**
     * Check is this ItemStack a valid output.
     *
     * @param outputs The output list to add to.
     * @param output  The output ItemStack to check.
     */
    private static void addValidOutput(List<ItemStack> outputs, ItemStack output) {
        if (output != null) outputs.add(output);
    }

    public static void registryOreProcessRecipe(ItemStack input, ItemStack[] output) {
        if (input == null) return;
        TST_RecipeBuilder.builder()
            .itemInputs(input)
            .itemOutputs(output)
            .fluidInputs(Materials.Lubricant.getFluid(1))
            .eut(OreProcessRecipeEUt)
            .duration(OreProcessRecipeDuration)
            .addTo(GTCMRecipe.OreProcessingRecipes);
    }
}
