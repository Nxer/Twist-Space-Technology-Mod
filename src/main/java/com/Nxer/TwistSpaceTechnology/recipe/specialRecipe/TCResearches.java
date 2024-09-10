package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class TCResearches {

    public void loadResearches() {
        if (Config.Enable_BloodHell) {

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

            if (Config.Enable_BloodHatch) {
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
