package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeDuration;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeEUt;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static gregtech.api.util.GT_ModHandler.getModItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.google.common.collect.Sets;

import goodgenerator.items.MyMaterial;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class OP_NormalProcessing {

    /**
     * Instance of this class.
     */
    public static final OP_NormalProcessing instance = new OP_NormalProcessing();

    /**
     * Ore stone types enum
     */
    public static final Set<OrePrefixes> basicStoneTypes = Sets.newHashSet(
        OrePrefixes.ore,
        OrePrefixes.oreBasalt,
        OrePrefixes.oreBlackgranite,
        OrePrefixes.oreRedgranite,
        OrePrefixes.oreMarble,
        OrePrefixes.oreNetherrack,
        OrePrefixes.oreEndstone);

    public static ItemStack getDustStack(Materials material, int amount) {
        return setStackSize(GT_OreDictUnificator.get(OrePrefixes.dust, material, 1), amount);
    }

    /**
     * Generate recipes.
     */
    public void enumOreProcessingRecipes() {
        Set<Materials> specialProcesses = Sets.newHashSet(
            Materials.Samarium,
            Materials.Cerium,
            Materials.Naquadah,
            Materials.NaquadahEnriched,
            Materials.Naquadria);

        // generate normal materials' ore processing recipes
        for (Materials material : GregTech_API.sGeneratedMaterials) {
            // rule out special materials
            if (!specialProcesses.isEmpty() && specialProcesses.contains(material)) {
                specialProcesses.remove(material);
                continue;
            }
            // generate recipes
            processOreRecipe(material);
        }

        processSpecialOreRecipe();
        processInsteadOre();
        OP_GTPP_OreHandler.instance.processGTPPOreRecipes();
        OP_Bartworks_OreHandler.instance.processBWOreRecipes();

    }

    /**
     * Generate special ores recipes
     */
    public void processSpecialOreRecipe() {
        // spotless:off

        // Cerium ore
        {
            ItemStack[] outputs = new ItemStack[] {
                WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 11) };
            ItemStack[] outputsRich = new ItemStack[] {
                WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 22) };
            for (OrePrefixes prefixes : basicStoneTypes) {
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 1) == null) continue;
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.Cerium, 1))
                    .itemOutputs(isRich(prefixes) ? outputsRich : outputs)
                    .fluidInputs(Materials.Lubricant.getFluid(1))
                    .noFluidOutputs()
                    .eut(OreProcessRecipeEUt)
                    .duration(OreProcessRecipeDuration)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

        // Samarium Ore
        {
            ItemStack[] outputs = new ItemStack[] {
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 11) };
            ItemStack[] outputsRich = new ItemStack[] {
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 22) };
            for (OrePrefixes prefixes : basicStoneTypes) {
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 1) == null) continue;
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.Samarium, 1))
                    .itemOutputs(isRich(prefixes) ? outputsRich : outputs)
                    .fluidInputs(Materials.Lubricant.getFluid(1))
                    .noFluidOutputs()
                    .eut(OreProcessRecipeEUt)
                    .duration(OreProcessRecipeDuration)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

        // Naquadah Ore
        {
            ItemStack[] outputs = new ItemStack[] { MyMaterial.naquadahEarth.get(OrePrefixes.dust, 8),
                MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 3), };
            ItemStack[] outputsRich = new ItemStack[] { MyMaterial.naquadahEarth.get(OrePrefixes.dust, 16),
                MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 8), };
            for (OrePrefixes prefixes : basicStoneTypes) {
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 1) == null) continue;
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.Naquadah, 1))
                    .itemOutputs(isRich(prefixes) ? outputsRich : outputs)
                    .fluidInputs(Materials.Lubricant.getFluid(1))
                    .noFluidOutputs()
                    .eut(OreProcessRecipeEUt)
                    .duration(OreProcessRecipeDuration)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

        // Enriched Naquadah Ore
        {
            ItemStack[] outputs = new ItemStack[] { MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 8),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 3) };
            ItemStack[] outputsRich = new ItemStack[] { MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 16),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 6) };
            for (OrePrefixes prefixes : basicStoneTypes) {
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 11) == null) continue;
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.NaquadahEnriched, 1))
                    .itemOutputs(isRich(prefixes) ? outputsRich : outputs)
                    .fluidInputs(Materials.Lubricant.getFluid(1))
                    .noFluidOutputs()
                    .eut(OreProcessRecipeEUt)
                    .duration(OreProcessRecipeDuration)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

        // Naquadria Ore
        {
            ItemStack[] outputs = new ItemStack[] { MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 8),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 3), };
            ItemStack[] outputsRich = new ItemStack[] { MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 16),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 6), };
            for (OrePrefixes prefixes : basicStoneTypes) {
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 16) == null) continue;
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.Naquadria, 1))
                    .itemOutputs(isRich(prefixes) ? outputsRich : outputs)
                    .fluidInputs(Materials.Lubricant.getFluid(1))
                    .noFluidOutputs()
                    .eut(OreProcessRecipeEUt)
                    .duration(OreProcessRecipeDuration)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

        // spotless:on
    }

    /**
     * Generate normal ore recipes
     *
     * @param material The ore's Material.
     */
    public void processOreRecipe(Materials material) {
        if (GT_OreDictUnificator.get(OrePrefixes.ore, material, 1) == null) return;

        List<ItemStack> outputs = new ArrayList<>();

        // check byproduct
        if (!material.mOreByProducts.isEmpty()) {
            // the basic output the material
            outputs.add(getDustStack(material, 4));
            if (material.mOreByProducts.size() == 1) {
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null) continue;
                    outputs.add(getDustStack(byproduct, 3));
                }
            } else {
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null || byproduct == Materials.Netherrack
                        || byproduct == Materials.Endstone
                        || byproduct == Materials.Stone) continue;

                    outputs.add(getDustStack(byproduct, 2));
                }
            }

        } else {
            outputs.add(getDustStack(material, 8));
        }

        // check gem style
        if (GT_OreDictUnificator.get(OrePrefixes.gem, material, 1) != null) {
            if (GT_OreDictUnificator.get(OrePrefixes.gemExquisite, material, 1) != null) {
                // has gem style
                outputs.add(GT_OreDictUnificator.get(OrePrefixes.gemExquisite, material, 1));
                outputs.add(GT_OreDictUnificator.get(OrePrefixes.gemFlawless, material, 2));
                outputs.add(GT_OreDictUnificator.get(OrePrefixes.gem, material, 2));

            } else {
                // just normal gem
                outputs.add(GT_OreDictUnificator.get(OrePrefixes.gem, material, 4));
            }
        }

        for (OrePrefixes prefixes : basicStoneTypes) {
            if (GT_OreDictUnificator.get(prefixes, material, 1) == null) {
                TwistSpaceTechnology.LOG.info("Failed to get ore: material=" + material + " , prefixes=" + prefixes);
                continue;
            }
            List<ItemStack> finalOutputs = new ArrayList<>();
            boolean isRich = isRich(prefixes);
            for (ItemStack items : outputs) {
                ItemStack newOne = items.copy();
                if (isRich) newOne.stackSize *= 2;
                finalOutputs.add(newOne);
            }

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(prefixes.get(material), 1))
                .itemOutputs(finalOutputs.toArray(new ItemStack[] {}))
                .fluidInputs(Materials.Lubricant.getFluid(1))
                .noFluidOutputs()
                .noOptimize()
                .eut(OreProcessRecipeEUt)
                .duration(OreProcessRecipeDuration)
                .addTo(GTCMRecipe.instance.OreProcessingRecipes);
        }

    }

    public void processInsteadOre(){
        // spotless:off
        int[] metas = {19,20,32,33,35,86,57,89,98,382,500,501,514,522,526,530,540,535,541,542,543,544,545,810,826,884,894,918};
        for (int ID : metas){
            TwistSpaceTechnology.LOG.info("Process special instead ore: " + GregTech_API.sGeneratedMaterials[ID]);
            Materials material = GregTech_API.sGeneratedMaterials[ID];

            List<ItemStack> outputs = new ArrayList<>();

            // check byproduct
            if (!material.mOreByProducts.isEmpty()) {
                // the basic output the material
                outputs.add(getDustStack(material, 4));
                if (material.mOreByProducts.size() == 1) {
                    for (Materials byproduct : material.mOreByProducts) {
                        if (byproduct == null) continue;
                        outputs.add(getDustStack(byproduct, 3));
                    }
                } else {
                    for (Materials byproduct : material.mOreByProducts) {
                        if (byproduct == null || byproduct == Materials.Netherrack
                                || byproduct == Materials.Endstone
                                || byproduct == Materials.Stone) continue;

                        outputs.add(getDustStack(byproduct, 2));
                    }
                }

            } else {
                outputs.add(getDustStack(material, 8));
            }

            // check gem style
            if (GT_OreDictUnificator.get(OrePrefixes.gem, material, 1) != null) {
                if (GT_OreDictUnificator.get(OrePrefixes.gemExquisite, material, 1) != null) {
                    // has gem style
                    outputs.add(GT_OreDictUnificator.get(OrePrefixes.gemExquisite, material, 1));
                    outputs.add(GT_OreDictUnificator.get(OrePrefixes.gemFlawless, material, 2));
                    outputs.add(GT_OreDictUnificator.get(OrePrefixes.gem, material, 2));

                } else {
                    // just normal gem
                    outputs.add(GT_OreDictUnificator.get(OrePrefixes.gem, material, 4));
                }
            }

            GT_Values.RA
                .stdBuilder()
                .itemInputs(GT_ModHandler.getModItem("gregtech","gt.blockores",1,ID))
                .itemOutputs(outputs.toArray(new ItemStack[] {}))
                .fluidInputs(Materials.Lubricant.getFluid(1))
                .noFluidOutputs()
                .noOptimize()
                .eut(OreProcessRecipeEUt)
                .duration(OreProcessRecipeDuration)
                .addTo(GTCMRecipe.instance.OreProcessingRecipes);
        }
        // spotless:on
    }

    /**
     * Check is this OrePrefix is rich ore style.
     *
     * @param prefixes The style to check.
     * @return True is rich.
     */
    public boolean isRich(OrePrefixes prefixes) {
        return prefixes == OrePrefixes.oreNetherrack || prefixes == OrePrefixes.oreEndstone;
    }

}
