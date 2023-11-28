package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class OP_NormalProcessing {

    public static final OP_NormalProcessing instance = new OP_NormalProcessing();

    public static ItemStack getDustStack(Materials material, int amount) {
        return setStackSize(GT_OreDictUnificator.get(OrePrefixes.dust, material, 1), amount);
    }

    public void enumOreProcessingRecipes() {
        for (Materials material : GregTech_API.sGeneratedMaterials) {

            // normal stone ore
            if (GT_OreDictUnificator.get(OrePrefixes.ore, material, 1) != null) {
                processOreRecipe(material, OrePrefixes.ore, false);
            }

            // Basalt
            if (GT_OreDictUnificator.get(OrePrefixes.oreBasalt, material, 1) != null) {
                processOreRecipe(material, OrePrefixes.oreBasalt, false);
            }

            // Black granite
            if (GT_OreDictUnificator.get(OrePrefixes.oreBlackgranite, material, 1) != null) {
                processOreRecipe(material, OrePrefixes.oreBlackgranite, false);
            }

            // TODO: Integrated Ore Processing Recipes
        }
    }

    public void processOreRecipe(Materials material, OrePrefixes orePrefixes, boolean isRich) {
        ItemStack inputs = GT_OreDictUnificator.get(orePrefixes, material, isRich ? 8 : 16);
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
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null) continue;
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

        // GT_Values.RA
        // .stdBuilder()
        // .itemInputs(inputs)
        // .noFluidInputs()
        // .itemOutputs(outputs.toArray(new ItemStack[]{}))
        // .noFluidOutputs()
        // .noOptimize()
        // .eut(120)
        // .duration(128)
        // .addTo(GTCMRecipe.instance.OreProcessingRecipes);
    }

}
