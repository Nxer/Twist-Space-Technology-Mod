package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.google.common.collect.Sets;
import goodgenerator.items.MyMaterial;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;

import java.util.ArrayList;
import java.util.Set;

import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static gregtech.api.util.GT_ModHandler.getModItem;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
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
        OrePrefixes.oreEndstone
    );

    public static ItemStack getDustStack(Materials material, int amount){
        return setStackSize(GT_OreDictUnificator.get(OrePrefixes.dust, material,1), amount);
    }

    /**
     * Generate recipes.
     */
    public void enumOreProcessingRecipes(){
        Set<Materials> specialProcesses = Sets.newHashSet(
            Materials.Samarium,
            Materials.Cerium,
            Materials.Cinnabar,
            Materials.Naquadah,
            Materials.NaquadahEnriched,
            Materials.Naquadria
        );

        // generate normal materials' ore processing recipes
        for (Materials material : GregTech_API.sGeneratedMaterials){
            // rule out special materials
            if (!specialProcesses.isEmpty() && specialProcesses.contains(material)) {
                specialProcesses.remove(material);
                continue;
            }
            // generate recipes
            processOreRecipe(material);
        }

        processSpecialOreRecipe();
        OP_GTPP_OreHandler.instance.processGTPPOreRecipes();

    }

    /**
     * Generate special ores recipes
     */
    public void processSpecialOreRecipe(){
        // Cinnabar ore
        {
            ItemStack[] outputs = new ItemStack[]{
                getDustStack(Materials.Cinnabar,64),
                getDustStack(Materials.Redstone, 24),
                getDustStack(Materials.Sulfur, 24),
                getDustStack(Materials.Glowstone, 24),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 64)};
            for (OrePrefixes prefixes : basicStoneTypes){
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 16) == null) continue;
                GT_Values.RA
                    .stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, isRich(prefixes) ? 8 : 16))
                    .itemOutputs(outputs)
                    .noFluidInputs()
                    .noFluidOutputs()
                    .eut(120)
                    .duration(128)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
            GT_Values.RA
                .stdBuilder()
                .itemInputs(getModItem("gregtech","gt.blockores",16, 826))
                .itemOutputs(outputs)
                .noFluidInputs()
                .noFluidOutputs()
                .eut(120)
                .duration(128)
                .addTo(GTCMRecipe.instance.OreProcessingRecipes);

        }

        // Cerium ore
        {
            ItemStack[] outputs = new ItemStack[]{
                WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 64),
                WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 64),
                WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 48),
            };
            for (OrePrefixes prefixes : basicStoneTypes){
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 16) == null) continue;
                GT_Values.RA
                    .stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.Cerium, isRich(prefixes) ? 8 : 16))
                    .itemOutputs(outputs)
                    .noFluidInputs()
                    .noFluidOutputs()
                    .eut(120)
                    .duration(128)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

        // Samarium Ore
        {
            ItemStack[] outputs = new ItemStack[]{
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 64),
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 64),
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 48),
            };
            for (OrePrefixes prefixes : basicStoneTypes){
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 16) == null) continue;
                GT_Values.RA
                    .stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.Samarium, isRich(prefixes) ? 8 : 16))
                    .itemOutputs(outputs)
                    .noFluidInputs()
                    .noFluidOutputs()
                    .eut(120)
                    .duration(128)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

        // Naquadah Ore
        {
            ItemStack[] outputs = new ItemStack[]{
                MyMaterial.naquadahEarth.get(OrePrefixes.dust, 64),
                MyMaterial.naquadahEarth.get(OrePrefixes.dust, 64),
                MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 48),
            };
            for (OrePrefixes prefixes : basicStoneTypes){
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 16) == null) continue;
                GT_Values.RA
                    .stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.Naquadah, isRich(prefixes) ? 8 : 16))
                    .itemOutputs(outputs)
                    .noFluidInputs()
                    .noFluidOutputs()
                    .eut(120)
                    .duration(128)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

        // Enriched Naquadah Ore
        {
            ItemStack[] outputs = new ItemStack[]{
                MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 64),
                MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 64),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 48),
            };
            for (OrePrefixes prefixes : basicStoneTypes){
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 16) == null) continue;
                GT_Values.RA
                    .stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.NaquadahEnriched, isRich(prefixes) ? 8 : 16))
                    .itemOutputs(outputs)
                    .noFluidInputs()
                    .noFluidOutputs()
                    .eut(120)
                    .duration(128)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

        // Naquadria Ore
        {
            ItemStack[] outputs = new ItemStack[]{
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 64),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 64),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 48),
            };
            for (OrePrefixes prefixes : basicStoneTypes){
                if (GT_OreDictUnificator.get(prefixes, Materials.Cinnabar, 16) == null) continue;
                GT_Values.RA
                    .stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefixes, Materials.Naquadria, isRich(prefixes) ? 8 : 16))
                    .itemOutputs(outputs)
                    .noFluidInputs()
                    .noFluidOutputs()
                    .eut(120)
                    .duration(128)
                    .addTo(GTCMRecipe.instance.OreProcessingRecipes);
            }
        }

    }

    /**
     * Generate normal ore recipes
     * @param material      The ore's Material.
     */
    public void processOreRecipe(Materials material){
        if (GT_OreDictUnificator.get(OrePrefixes.ore, material, 1) == null) return;

        ArrayList<ItemStack> outputs = new ArrayList<>();
        // the basic output the material
        outputs.add(getDustStack(material, 64));

        // check byproduct
        if (!material.mOreByProducts.isEmpty()) {
            if (material.mOreByProducts.size() == 1) {
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null) continue;
                    outputs.add(getDustStack(byproduct, 48));
                }
            } else {
                for (Materials byproduct : material.mOreByProducts){
                    if (byproduct == null
                            || byproduct == Materials.Netherrack
                            || byproduct == Materials.Endstone
                            || byproduct == Materials.Stone) continue;

                    outputs.add(getDustStack(byproduct, 24));
                }
            }

        } else {
            outputs.add(getDustStack(material, 48));
        }

        // check gem style
        if (GT_OreDictUnificator.get(OrePrefixes.gem, material, 1) != null) {
            if (GT_OreDictUnificator.get(OrePrefixes.gemExquisite, material, 1) != null) {
                // has gem style
                outputs.add(GT_OreDictUnificator.get(OrePrefixes.gemExquisite, material, 16));
                outputs.add(GT_OreDictUnificator.get(OrePrefixes.gemFlawless, material, 24));
                outputs.add(GT_OreDictUnificator.get(OrePrefixes.gem, material, 32));

            } else {
                // just normal gem
                outputs.add(GT_OreDictUnificator.get(OrePrefixes.gem, material, 64));
            }
        }

        for (OrePrefixes prefixes : basicStoneTypes){
            if (GT_OreDictUnificator.get(prefixes, material, 1) == null) continue;
            ItemStack inputs = GT_OreDictUnificator.get(prefixes, material, isRich(prefixes) ? 8 : 16);
            GT_Values.RA
                .stdBuilder()
                .itemInputs(inputs)
                .noFluidInputs()
                .itemOutputs(outputs.toArray(new ItemStack[]{}))
                .noFluidOutputs()
                .noOptimize()
                .eut(120)
                .duration(128)
                .addTo(GTCMRecipe.instance.OreProcessingRecipes);
        }

    }

    /**
     * Check is this OrePrefix is rich ore style.
     * @param prefixes  The style to check.
     * @return          True is rich.
     */
    public boolean isRich(OrePrefixes prefixes){
        return prefixes == OrePrefixes.oreNetherrack
            || prefixes == OrePrefixes.oreEndstone;
    }



}
