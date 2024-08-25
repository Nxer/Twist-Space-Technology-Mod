package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.TCRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.TCRecipes.TCBasic.EVOLUTION;
import static fox.spiteful.avaritia.compat.thaumcraft.Lucrum.ULTRA_DEATH;
import static gregtech.api.enums.TC_Aspects.ELECTRUM;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_FishingPond;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_TreeFarm;
import static kubatech.api.enums.ItemList.ExtremeEntityCrusher;
import static kubatech.api.enums.ItemList.ExtremeIndustrialGreenhouse;
import static net.minecraft.init.Items.diamond_sword;
import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;
import static thaumcraft.common.config.ConfigBlocks.blockStoneDevice;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.material.ALLOY;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public class TCRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        /* Elven Workshop */
        ThaumcraftApi.addInfusionCraftingRecipe(
            "BH_ELVEN_WORKSHOP",
            GTCMItemList.ElvenWorkshop.get(1, 0),
            10,
            (new AspectList()).merge(Aspect.LIFE, 64)
                .merge(Aspect.EARTH, 64)
                .merge(Aspect.MAGIC, 64)
                .merge(Aspect.MECHANISM, 64),
            new ItemStack(ModBlocks.terraPlate),
            new ItemStack[] { ItemList.Field_Generator_EV.get(1), ItemList.Casing_IV.get(1),
                Materials.Steeleaf.getPlates(1), new ItemStack(ModItems.spawnerMover, 1),
                ItemList.Field_Generator_EV.get(1), ItemList.Casing_IV.get(1), Materials.Steeleaf.getPlates(1),
                new ItemStack(ModItems.spawnerMover, 1) });

        /* INDUSTRIAL_MAGIC_MATRIX */
        if (Config.Enable_IndustrialMagicMatrix) {
            ThaumcraftApi.addInfusionCraftingRecipe(
                "INDUSTRIAL_MAGIC_MATRIX",
                GTCMItemList.IndustrialMagicMatrix.get(1, 0),
                25,
                (new AspectList()).merge(Aspect.LIFE, 128)
                    .merge(Aspect.EARTH, 128)
                    .merge(Aspect.MAGIC, 128)
                    .merge(Aspect.MECHANISM, 128)
                    .merge(Aspect.AIR, 128)
                    .merge(Aspect.EARTH, 128)
                    .merge(Aspect.FIRE, 128)
                    .merge(Aspect.WATER, 128)
                    .merge(Aspect.ORDER, 128)
                    .merge(Aspect.ENTROPY, 128),
                ItemList.Machine_Multi_Assemblyline.get(1, 0),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12) });

            /* ECO_SPHERE_SIMULATOR */
            if (Config.Enable_MegaTreeFarm) {
                ThaumcraftApi.addInfusionCraftingRecipe(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.MegaTreeFarm.get(1),
                    100,
                    (new AspectList()).merge(Aspect.MECHANISM, 256)
                        .merge(Aspect.TREE, 1024)
                        .merge(Aspect.HARVEST, 2048)
                        .merge(Aspect.WATER, 1024)
                        .merge(Aspect.LIFE, 2048)
                        .merge(Aspect.PLANT, 1024)
                        .merge(Aspect.CROP, 2048)
                        .merge(Aspect.FLESH, 1024)
                        .merge(Aspect.WEAPON, 2048)
                        .merge((Aspect) ELECTRUM.mAspect, 8192),

                    GT_ModHandler.getModItem(Mods.Botania.ID, "manaResource", 1, 5),
                    new ItemStack[] { Industrial_TreeFarm.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),
                        ALLOY.TITANSTEEL.getPlateDense(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),

                        Industrial_FishingPond.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),
                        ALLOY.TITANSTEEL.getPlateDense(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),

                        ExtremeIndustrialGreenhouse.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),
                        ALLOY.TITANSTEEL.getPlateDense(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),

                        Mods.EnderIO.isModLoaded() ? ExtremeEntityCrusher.get(1) : new ItemStack(diamond_sword, 1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),
                        ALLOY.TITANSTEEL.getPlateDense(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1) });

                ThaumcraftApi.addInfusionCraftingRecipe(
                    "FONT_OF_ECOLOGY",
                    GTCMItemList.FountOfEcology.get(1),
                    200,
                    (new AspectList()).merge(EVOLUTION, 1024)
                        .merge(Aspect.WATER, 65536)
                        .merge(Aspect.LIFE, 16384)
                        .merge(Aspect.FLESH, 4096)
                        .merge(ULTRA_DEATH, 256),
                    Mods.Witchery.isModLoaded() ? GT_ModHandler.getModItem(Mods.Witchery.ID, "infinityegg", 1)
                        : new ItemStack(Blocks.dragon_egg, 1),
                    new ItemStack[] { GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1) });
            }
        }
    }

}
