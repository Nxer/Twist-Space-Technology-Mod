package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;
import static thaumcraft.common.config.ConfigBlocks.blockStoneDevice;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.dreammaster.gthandler.CustomItemList;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.config.ConfigItems;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public class TCRecipePool implements IRecipePool {

    public static InfusionRecipe infusionRecipeBloodyHell;
    public static InfusionRecipe infusionRecipeBloodHatch;
    public static InfusionRecipe infusionRecipeTimeBendingSpeedRune;

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
        }
        if(Config.Enable_BloodHell) {
            infusionRecipeBloodyHell = ThaumcraftApi.addInfusionCraftingRecipe(
                "BLOODY_HELL",
                GTCMItemList.BloodyHell.get(1, 0),
                25,
                new AspectList().merge(Aspect.LIFE, 128)
                    .merge(Aspect.HEAL, 128)
                    .merge(Aspect.MAGIC, 128)
                    .merge(Aspect.MAN, 64)
                    .merge(Aspect.DEATH, 64)
                    .merge(Aspect.UNDEAD, 64)
                    .merge(Aspect.MECHANISM, 16),
                new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.blockAltar),
                new ItemStack[] { new ItemStack(WayofTime.alchemicalWizardry.ModItems.transcendentBloodOrb),
                    new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.blockMasterStone),
                    new ItemStack(WayofTime.alchemicalWizardry.ModItems.activationCrystal, 1, 1),
                    new ItemStack(WayofTime.alchemicalWizardry.ModItems.itemSeerSigil),
                    new ItemStack(WayofTime.alchemicalWizardry.ModItems.divinationSigil),
                    new ItemStack(WayofTime.alchemicalWizardry.ModItems.waterSigil),
                    new ItemStack(WayofTime.alchemicalWizardry.ModItems.lavaSigil),
                    new ItemStack(WayofTime.alchemicalWizardry.ModItems.voidSigil),
                    new ItemStack(WayofTime.alchemicalWizardry.ModItems.airSigil), });
            if(Config.Enable_BloodHatch) {
                infusionRecipeBloodHatch = ThaumcraftApi.addInfusionCraftingRecipe(
                    "BLOOD_HATCH",
                    GTCMItemList.BloodOrbHatch.get(1, 0),
                    5,
                    new AspectList().merge(Aspect.LIFE, 16)
                        .merge(Aspect.MECHANISM, 16)
                        .merge(Aspect.TOOL, 12),
                    ItemList.Hatch_Input_IV.get(1),
                    new ItemStack[] { new ItemStack(WayofTime.alchemicalWizardry.ModItems.weakBloodOrb),
                        new ItemStack(WayofTime.alchemicalWizardry.ModItems.sacrificialDagger),
                        new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(ConfigItems.itemZombieBrain), });
            }
            infusionRecipeTimeBendingSpeedRune = ThaumcraftApi.addInfusionCraftingRecipe(
                "TIME_BENDING_SPEED_RUNE",
                new ItemStack(BasicBlocks.timeBendingSpeedRune),
                10,
                new AspectList().merge(Aspect.LIFE, 64)
                    .merge(Aspect.MOTION, 256)
                    .merge((Mods.MagicBees.isModLoaded() ? Aspect.getAspect("tempus") : Aspect.AIR), 64),
                MaterialsUEVplus.SpaceTime.getBlocks(1),
                new ItemStack[] { CustomItemList.AcceleratorZPM.get(1), CustomItemList.AcceleratorZPM.get(1),
                    new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.bloodRune, 1, 5), // Rune of Acceleration
                    new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.bloodRune, 1, 5), });
        }
    }

}
