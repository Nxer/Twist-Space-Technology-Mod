package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeDuration;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeEUt;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import ic2.core.Ic2Items;

public class OP_NormalProcessing {

    /**
     * Instance of this class.
     */
    public static final OP_NormalProcessing instance = new OP_NormalProcessing();

    /**
     * Ore stone types enum
     */
    public final Set<OrePrefixes> basicStoneTypes = Sets.newHashSet(
        OrePrefixes.ore,
        OrePrefixes.oreBasalt,
        OrePrefixes.oreBlackgranite,
        OrePrefixes.oreRedgranite,
        OrePrefixes.oreMarble,
        OrePrefixes.oreNetherrack,
        OrePrefixes.oreEndstone);

    public final Set<OrePrefixes> basicStoneTypesExceptNormalStone = Sets.newHashSet(
        OrePrefixes.oreBasalt,
        OrePrefixes.oreBlackgranite,
        OrePrefixes.oreRedgranite,
        OrePrefixes.oreMarble,
        OrePrefixes.oreNetherrack,
        OrePrefixes.oreEndstone);

    // public final List<Integer> insteadMaterialOresMetas = Arrays.asList(
    // 19, 20, 28, 32, 33, 35, 57, 86, 89, 98, 347, 382, 500, 501, 514, 522, 526, 530,
    // 535, 540, 541, 542, 543, 544, 545, 770, 810, 817, 826, 884, 894, 918, 920
    // );

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
        for (int i = 0; i < GregTech_API.sGeneratedMaterials.length; i++) {
            if (GregTech_API.sGeneratedMaterials[i] == null) continue;

            Materials material = GregTech_API.sGeneratedMaterials[i];

            // rule out special materials
            if (!specialProcesses.isEmpty() && specialProcesses.contains(material)) {
                specialProcesses.remove(material);
                continue;
            }
            // generate recipes
            processOreRecipe(material, i);
        }

        processSpecialOreRecipe();
        new OP_GTPP_OreHandler().processGTPPOreRecipes();
        new OP_Bartworks_OreHandler().processBWOreRecipes();

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
                if (GT_OreDictUnificator.get(prefixes, Materials.Cerium, 1) == null) continue;
                registryOreProcessRecipe(
                    GT_OreDictUnificator.get(prefixes, Materials.Cerium, 1),
                    isRich(prefixes) ? outputsRich : outputs
                );
            }
        }

        // Samarium Ore
        {
            ItemStack[] outputs = new ItemStack[] {
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 11) };
            ItemStack[] outputsRich = new ItemStack[] {
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 22) };
            for (OrePrefixes prefixes : basicStoneTypes) {
                if (GT_OreDictUnificator.get(prefixes, Materials.Samarium, 1) == null) continue;
                registryOreProcessRecipe(
                    GT_OreDictUnificator.get(prefixes, Materials.Samarium, 1),
                    isRich(prefixes) ? outputsRich : outputs
                );
            }
        }

        // Naquadah Ore
        {
            ItemStack[] outputs = new ItemStack[] { MyMaterial.naquadahEarth.get(OrePrefixes.dust, 8),
                MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 3), };
            ItemStack[] outputsRich = new ItemStack[] { MyMaterial.naquadahEarth.get(OrePrefixes.dust, 16),
                MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 8), };
            for (OrePrefixes prefixes : basicStoneTypes) {
                if (GT_OreDictUnificator.get(prefixes, Materials.Naquadah, 1) == null) continue;
                registryOreProcessRecipe(
                    GT_OreDictUnificator.get(prefixes, Materials.Naquadah, 1),
                    isRich(prefixes) ? outputsRich : outputs
                );
            }
        }

        // Enriched Naquadah Ore
        {
            ItemStack[] outputs = new ItemStack[] { MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 8),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 3) };
            ItemStack[] outputsRich = new ItemStack[] { MyMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 16),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 6) };
            for (OrePrefixes prefixes : basicStoneTypes) {
                if (GT_OreDictUnificator.get(prefixes, Materials.NaquadahEnriched, 1) == null) continue;
                registryOreProcessRecipe(
                    GT_OreDictUnificator.get(prefixes, Materials.NaquadahEnriched, 1),
                    isRich(prefixes) ? outputsRich : outputs
                );
            }
        }

        // Naquadria Ore
        {
            ItemStack[] outputs = new ItemStack[] { MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 8),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 3), };
            ItemStack[] outputsRich = new ItemStack[] { MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 16),
                MyMaterial.naquadriaEarth.get(OrePrefixes.dust, 6), };
            for (OrePrefixes prefixes : basicStoneTypes) {
                if (GT_OreDictUnificator.get(prefixes, Materials.Naquadria, 1) == null) continue;
                registryOreProcessRecipe(
                    GT_OreDictUnificator.get(prefixes, Materials.Naquadria, 1),
                    isRich(prefixes) ? outputsRich : outputs
                );
            }
        }

        // Tinker Construct
        // Cobalt ore
        processOreRecipe(
            GT_ModHandler.getModItem("TConstruct","SearedBrick", 1, 1),
            Materials.Cobalt,
            true
        );

        // Ardite ore
        processOreRecipe(
            GT_ModHandler.getModItem("TConstruct","SearedBrick", 1, 2),
            Materials.Ardite,
            true
        );

        // IC2 Uranium ore
        processOreRecipe(
            copyAmount(1,Ic2Items.uraniumOre),
            Materials.Uranium,
            false
        );

        // HEE end powder
        registryOreProcessRecipe(
            GT_ModHandler.getModItem("HardcoreEnderExpansion","end_powder_ore",1),
            new ItemStack[]{GT_ModHandler.getModItem("HardcoreEnderExpansion", "end_powder", 24)}
        );

        // spotless:on
    }

    /**
     * Generate normal ore recipes
     *
     * @param material The ore's Material.
     * @param ID       The material ID.
     */
    public void processOreRecipe(Materials material, int ID) {
        if (GT_OreDictUnificator.get(OrePrefixes.ore, material, 1) == null) return;
        ItemStack[] outputs = getOutputs(material, false);
        ItemStack[] outputsRich = getOutputs(material, true);

        // registry normal stone ore
        registryOreProcessRecipe(GT_ModHandler.getModItem("gregtech", "gt.blockores", 1, ID), outputs);

        // registry gt stone ore
        for (OrePrefixes prefixes : basicStoneTypesExceptNormalStone) {
            if (GT_OreDictUnificator.get(prefixes, material, 1) == null) {
                TwistSpaceTechnology.LOG.info("Failed to get ore: material=" + material + " , prefixes=" + prefixes);
                continue;
            }
            registryOreProcessRecipe(
                GT_OreDictUnificator.get(prefixes, material, 1),
                isRich(prefixes) ? outputsRich : outputs);
        }
    }

    /**
     * Process other mods' ore but normal style.
     *
     * @param inputOreItems Input ore item stack.
     * @param material      Input ore's material in GT design.
     * @param isRich        Is this ore a rich type.
     */
    public void processOreRecipe(ItemStack inputOreItems, Materials material, boolean isRich) {
        registryOreProcessRecipe(inputOreItems, getOutputs(material, isRich));
    }

    public ItemStack[] getOutputs(Materials material, boolean isRich) {
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

        if (isRich) {
            for (ItemStack out : outputs) {
                out.stackSize *= 2;
            }
        }

        return outputs.toArray(new ItemStack[0]);
    }

    public void registryOreProcessRecipe(ItemStack input, ItemStack[] output) {
        GT_Values.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .fluidInputs(Materials.Lubricant.getFluid(1))
            .noFluidOutputs()
            .noOptimize()
            .eut(OreProcessRecipeEUt)
            .duration(OreProcessRecipeDuration)
            .addTo(GTCMRecipe.instance.OreProcessingRecipes);
    }

    /**
     * Check is this OrePrefix is rich ore style.
     *
     * @param prefixes The style to check.
     * @return True is rich ore.
     */
    public boolean isRich(OrePrefixes prefixes) {
        return prefixes == OrePrefixes.oreNetherrack || prefixes == OrePrefixes.oreEndstone;
    }

}
