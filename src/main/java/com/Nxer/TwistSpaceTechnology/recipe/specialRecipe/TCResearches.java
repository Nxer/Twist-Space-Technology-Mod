package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;
import static thaumcraft.common.config.ConfigBlocks.blockStoneDevice;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
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
        }

        if(Config.Enable_BloodHell) {

            // #tr tc.research_name.BLOODY_HELL
            // # Bloody Hell
            // #zh_CN 血狱
            new ResearchItem(
                "BLOODY_HELL",
                "BASICS",
                new AspectList().merge(Aspect.LIFE, 1)
                    .merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1),
                4,
                -10,
                5,
                GTCMItemList.BloodyHell.get(1, 0)).setPages(
                    // #tr tc.research_text.BLOODY_HELL.1
                    // # BLOOD, BLOOD, BLOOD!
                    // #zh_CN 血！血！血！
                    new ResearchPage(TextEnums.tr("tc.research_text.BLOODY_HELL.1")),
                    new ResearchPage(TCRecipePool.infusionRecipeBloodyHell))
                .registerResearchItem();

            if(Config.Enable_BloodHatch) {
                // #tr tc.research_name.BLOOD_HATCH
                // # Blood Hatch
                // #zh_CN 血液仓
                new ResearchItem(
                    "BLOOD_HATCH",
                    "BASICS",
                    new AspectList().merge(Aspect.LIFE, 1)
                        .merge(Aspect.MAGIC, 1)
                        .merge(Aspect.TOOL, 1),
                    5,
                    -10,
                    5,
                    GTCMItemList.BloodOrbHatch.get(1, 0)).setPages(
                        // #tr tc.research_text.BLOOD_HATCH.1
                        // # The zombie brains are thirst for blood. Maybe we can make use of this.
                        // #zh_CN 僵尸的脑子渴望得到血液。也许我们能够利用这一点。
                        new ResearchPage(TextEnums.tr("tc.research_text.BLOOD_HATCH.1")),
                        new ResearchPage(TCRecipePool.infusionRecipeBloodHatch))
                    .registerResearchItem();
            }

            // #tr tc.research_name.TIME_BENDING_SPEED_RUNE
            // # Time-bending Speed Rune
            // #zh_CN 时间扭曲速度符文
            new ResearchItem(
                "TIME_BENDING_SPEED_RUNE",
                "BASICS",
                new AspectList().merge(Aspect.LIFE, 1)
                    .merge(Aspect.MAGIC, 1)
                    .merge(Aspect.TOOL, 1),
                6,
                -10,
                5,
                new ItemStack(BasicBlocks.timeBendingSpeedRune)).setPages(
                    // #tr tc.research_text.TIME_BENDING_SPEED_RUNE.1
                    // # The SpaceTime bends with Speed Runes and Accelerators, and it showed the compatibility to the
                    // advanced Altars.
                    // #zh_CN 使用速度符文和世界加速器扭曲的时空展现出对高级祭坛的兼容性。
                    new ResearchPage(TextEnums.tr("tc.research_text.TIME_BENDING_SPEED_RUNE.1")),
                    new ResearchPage(TCRecipePool.infusionRecipeTimeBendingSpeedRune))
                .registerResearchItem();
        }
    }
}
