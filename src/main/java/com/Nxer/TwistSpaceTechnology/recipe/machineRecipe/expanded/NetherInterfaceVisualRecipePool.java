package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.NetherInterfaceVisualRecipeMap;
import static com.Nxer.TwistSpaceTechnology.util.TstUtils.setStackSize;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.util.GTModHandler.getModItem;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.machine.MiscHelper;
import com.Nxer.TwistSpaceTechnology.config.Config;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;

public class NetherInterfaceVisualRecipePool {

    public static void loadRecipes() {
        GTValues.RA.stdBuilder()
            .fluidInputs(setStackSize(MiscHelper.distilledWater.copy(), Config.BasicDistilledWaterCost_NetherInterface))
            .fluidOutputs(
                Materials.PoorNetherWaste.getFluid(Config.BasicDistilledWaterCost_NetherInterface),
                Materials.HellishMetal.getMolten(288L))
            .itemOutputs(
                getModItem(
                    EtFuturumRequiem.ID,
                    "ancient_debris",
                    1,
                    new ItemStack(Blocks.fire).setStackDisplayName("EtFuturumRequiem:ancient_debris")),
                ItemList.Heavy_Hellish_Mud.get(48),
                ItemList.Brittle_Netherite_Scrap.get(64),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(64),
                getModItem(
                    ThaumicTinkerer.ID,
                    "kamiResource",
                    8,
                    6,
                    new ItemStack(Blocks.fire).setStackDisplayName("ThaumicTinkerer:kamiResource:6")))
            .outputChances(100, 4900, 3000, 1000, 1000)
            .eut(RECIPE_LuV)
            .duration(20 * 60)
            .addTo(NetherInterfaceVisualRecipeMap);
    }
}
