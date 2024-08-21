package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static fox.spiteful.avaritia.compat.thaumcraft.Lucrum.ULTRA_DEATH;
import static gregtech.api.enums.TC_Aspects.ELECTRUM;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_FishingPond;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_TreeFarm;
import static kubatech.api.enums.ItemList.ExtremeEntityCrusher;
import static kubatech.api.enums.ItemList.ExtremeIndustrialGreenhouse;
import static net.minecraft.init.Items.diamond_sword;
import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;
import static thaumcraft.common.config.ConfigBlocks.blockStoneDevice;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.material.ALLOY;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public class TCResearches {

    public void loadResearches() {
        new ResearchItem(
            "BH_ELVEN_WORKSHOP",
            "botanichorizons",
            (new AspectList()).merge(Aspect.EARTH, 1)
                .merge(Aspect.MECHANISM, 1)
                .merge(Aspect.MAGIC, 1),
            4,
            -9,
            3,
            GTCMItemList.ElvenWorkshop.get(1, 0)).setParents("BH_GAIA_PYLON")
                .setPages(
                    new ResearchPage("tc.research_text.BH_ELVEN_WORKSHOP.1"),
                    new ResearchPage("tc.research_text.BH_ELVEN_WORKSHOP.2"),
                    new ResearchPage(
                        new InfusionRecipe(
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
                                ItemList.Field_Generator_EV.get(1), ItemList.Casing_IV.get(1),
                                Materials.Steeleaf.getPlates(1), new ItemStack(ModItems.spawnerMover, 1) })))
                .registerResearchItem();
        if (Config.Enable_IndustrialMagicMatrix) {
            new ResearchItem(
                "INDUSTRIAL_MAGIC_MATRIX",
                "BASICS",
                (new AspectList()).merge(Aspect.EARTH, 1)
                    .merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1),
                4,
                -9,
                5,
                GTCMItemList.IndustrialMagicMatrix.get(1, 0))/* .setParents("ICHORIUM") */
                    .setPages(
                        // #tr tc.research_text.INDUSTRIAL_MAGIC_MATRIX.1
                        // # Death, Evil, Abomination, Grievance, Murderous Intent, Curse of Misfortune, Hell,
                        // Ethics,Fool,
                        // Tyrant, Sinner, Cunning, Thief, Despicable, Evil, Poison, Hunger, Epidemic,
                        // Earthquake,Heavenly
                        // Change, Alien, Human, Calamity Forever, Time, Spirit, Root, Fiction, Darkness,
                        // Innocence,Life, or
                        // Something Called Fear.
                        // #zh_CN
                        // 死、邪恶、憎恶、怨嗟、杀意、不幸诅咒、地狱、伦理、愚者、暴君、罪人、狡猾、贼徒、卑劣、恶、毒、饥饿、疫病、地震、天变、异形、人间、灾厄永远、时间、精神、根源、虚构、黑暗、无垢、命或者被称为恐惧之物。
                        new ResearchPage(TextEnums.tr("tc.research_text.INDUSTRIAL_MAGIC_MATRIX.1")),
                        new ResearchPage(
                            new InfusionRecipe(
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
                                    new ItemStack(blockMetalDevice, 1, 12) })))
                    .registerResearchItem();

            if (Config.Enable_MegaTreeFarm) {
                new ResearchItem(
                    "ECO_SPHERE_SIMULATOR",
                    "BASICS",
                    (new AspectList()).merge(Aspect.TREE, 1)
                        .merge(Aspect.MECHANISM, 1)
                        .merge(Aspect.WATER, 1)
                        .merge(Aspect.PLANT, 1)
                        .merge(Aspect.ELDRITCH, 1)
                        .merge(Aspect.FLESH, 1),
                    7,
                    -12,
                    10,
                    GTCMItemList.MegaTreeFarm.get(1, 0))
                        .setPages(
                            new ResearchPage("A_STRING"),
                            new ResearchPage(
                                new InfusionRecipe(
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
//                                        .merge(ULTRA_DEATH, 64)
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

                                        Mods.EnderIO.isModLoaded() ? ExtremeEntityCrusher.get(1)
                                            : new ItemStack(diamond_sword, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),
                                        ALLOY.TITANSTEEL.getPlateDense(1),
                                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1) })))
                        .setParents("INDUSTRIAL_MAGIC_MATRIX")
                        // .setHidden()
                        .setConcealed()
                        .registerResearchItem();;
            }
        }
    }
}
