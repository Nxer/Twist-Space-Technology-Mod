package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.dreammaster.gthandler.CustomItemList;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.config.ConfigItems;

public class TCRecipePool implements IRecipePool {

    public static InfusionRecipe infusionRecipeBloodyHell;
    public static InfusionRecipe infusionRecipeBloodHatch;
    public static InfusionRecipe infusionRecipeTimeBendingSpeedRune;

    @Override
    public void loadRecipes() {
        if (Config.Enable_BloodHell) {
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
            if (Config.Enable_BloodHatch) {
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
