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
                        // # Death,Evil,Abomination,Grievance,Murderous Intent,Curse of Misfortune,Hell,
                        // Ethics,Fool,
                        // Tyrant,Sinner,Cunning,Thief,Despicable,Evil,Poison,Hunger,Epidemic,
                        // Earthquake,Heavenly
                        // Change,Alien,Human,Calamity Forever,Time,Spirit,Root,Fiction,Darkness,
                        // Innocence,Life,or
                        // Something Called Fear.
                        // #zh_CN
                        // 死、邪恶、憎恶、怨嗟、杀意、不幸诅咒、地狱、伦理、愚者、暴君、罪人、狡猾、贼徒、卑劣、恶、毒、饥饿、疫病、地震、天变、异形、人间、灾厄永远、时间、精神、根源、虚构、黑暗、无垢、命或者被称为恐惧之物.
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
                // Machine
                new ResearchItem(
                    "ECO_SPHERE_SIMULATOR",
                    // #tr tc.research_name.ECO_SPHERE_SIMULATOR
                    // # Eco-Sphere Simulator
                    // #zh_CN 拟似生态圈
                    "BASICS",
                    (new AspectList()).merge(Aspect.TREE, 1)
                        .merge(Aspect.MECHANISM, 1)
                        .merge(Aspect.WATER, 1)
                        .merge(Aspect.PLANT, 1)
                        .merge(Aspect.ELDRITCH, 1)
                        .merge(Aspect.FLESH, 1),
                    7,
                    -9,
                    10,
                    GTCMItemList.MegaTreeFarm.get(1, 0)).setPages(
                        // spotless:off
                        // #tr tc.research_text.ECO_SPHERE_SIMULATOR.1
                        // # A device that fuses arcane and technological forces, designed to simulate a natural ecosystem.<BR>{\BOLD}Usage Instructions: <BR>{\RESET}Place the machine and connect it to an adjustable power source. The device will draw ambient vis, ensuring natural tree and aquatic life growth.<BR>{\BOLD}Precautions: <BR>{\RESET}Under high voltage, efficiency may decrease, but output will increase. Adjust power input accordingly.<BR>Aquatic life is usually stable; report any non-listed entities immediately.
                        // #zh_CN 一种将奥术与科技融合的装置, 用于模拟自然生态. <BR>{\BOLD}使用方法:<BR>{\RESET}将机器放置在合适的地点, 并连接到可调节功率的外部电源. 装置会自动汲取环境的源质, 保障树木与水生生物的自然生长. <BR>{\BOLD}注意事项:<BR>{\RESET}高电压下运行时, 尽管效率降低, 总产出会有所增加, 使用者应合理调整电力输入. <BR>水生生物通常不会异常, 如果出现非列表生物, 请立即通报上级部门.
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_SIMULATOR.1")),
                        new ResearchPage(
                            new InfusionRecipe(
                                "ECO_SPHERE_SIMULATOR",
                                GTCMItemList.MegaTreeFarm.get(1),
                                100,
                                (new AspectList()).merge(Aspect.MECHANISM,256)
                                    .merge(Aspect.TREE,1024)
                                    .merge(Aspect.HARVEST,2048)
                                    .merge(Aspect.WATER,1024)
                                    .merge(Aspect.LIFE,2048)
                                    .merge(Aspect.PLANT,1024)
                                    .merge(Aspect.CROP,2048)
                                    .merge(Aspect.FLESH,1024)
                                    .merge(Aspect.WEAPON,2048)
                                    .merge((Aspect) ELECTRUM.mAspect,8192),

                                GT_ModHandler.getModItem(Mods.Botania.ID,"manaResource",1,5),
                                new ItemStack[] { Industrial_TreeFarm.get(1),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite),1),
                                    ALLOY.TITANSTEEL.getPlateDense(1),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite),1),

                                    Industrial_FishingPond.get(1),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite),1),
                                    ALLOY.TITANSTEEL.getPlateDense(1),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite),1),

                                    ExtremeIndustrialGreenhouse.get(1),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite),1),
                                    ALLOY.TITANSTEEL.getPlateDense(1),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite),1),

                                    Mods.EnderIO.isModLoaded() ? ExtremeEntityCrusher.get(1)
                                        : new ItemStack(diamond_sword,1),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite),1),
                                    ALLOY.TITANSTEEL.getPlateDense(1),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite),1) })),
                        // #tr tc.research_text.ECO_SPHERE_SIMULATOR.2
                        // # <LINE>{\BOLD}Operation Log Record:<BR>{\RESET}When operating at low voltage, the machine exhibits extremely high simulation efficiency, remaining the ecosystem exceptionally stable. <BR>As the voltage exceeds a threshold, the machine's efficiency decreases, while the aquatic area shows invisible pressure, causing subtle distortions in the simulation.<BR>In rare cases, observers have reported seeing a strange entity in the water. It emits a faint glow, resembling a jellyfish but with an indescribable quality.
                        // #zh_CN <LINE>{\BOLD}运行日志记录：<BR>{\RESET}在低电压运行时, 机器展现出极高的模拟效率, 模拟生态系统的状态异常稳定. <BR>当电压超过某个临界值时, 机器的运行效率开始逐渐下降, 但水域却似乎处于某种不可见的压力之下, 使得模拟出现微妙的扭曲与偏差. <BR>极少数情况下, 观察者报告称他们目睹了一种罕见且奇异的生物出现在水域中. 这种生物散发着微弱的光芒, 形态如同水母的轮廓, 但却拥有某种不可名状的特质.
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_SIMULATOR.2")),
                        // #tr tc.research_text.ECO_SPHERE_SIMULATOR.3
                        // # <LINE><BR><BR>Rumor has it that those thaumaturges who encounter this mutated entity will gain an indescribable revelation.<BR>This revelation not only deepens their understanding of the arcane but could even drive the machine's own evolution, making its functions more powerful and extraordinary.<BR>Though this phenomenon is exceedingly rare, thaumaturges devoted to the exploration of the arcane seem to glimpse a higher pursuit within it, fueling their desire for the unknown.
                        // #zh_CN <LINE><BR><BR>有传言称,遇见这种变异生物的神秘使,将会从中获得一种无法言明的启示.<BR>这种启示不仅能深化对奥术的理解,甚至能够推动机器本身的进化,使其功能变得更为强大且不可思议.<BR>尽管这种现象极其罕见,但那些执着于奥术探索的神秘使们,似乎从中看到了某种更高的追求,激发了他们对未知的渴望.
                        // spotless:on
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_SIMULATOR.3")))
                        .setParents("INDUSTRIAL_MAGIC_MATRIX")
                        // .setHidden()
                        .setConcealed()
                        .registerResearchItem();

                // Offspring
                new ResearchItem(
                    "OFFSPRING",
                    // #tr tc.research_name.OFFSPRING
                    // # {\DARK_AQUA}"Offspring"
                    // #zh_CN {\DARK_AQUA}"子代"
                    "BASICS",
                    (new AspectList()).merge(Aspect.WATER, 1)
                        .merge(Aspect.EXCHANGE, 1)
                        .merge(Aspect.LIFE, 1),
                    8,
                    -9,
                    10,
                    GTCMItemList.OffSpring.get(1, 0))
                        // #tr tc.research_text.OFFSPRING.1
                        // # What's this?
                        // #zh_CN 这是什么?
                        .setPages(new ResearchPage("tc.research_text.OFFSPRING.1"))
                        .setParents("ECO_SPHERE_SIMULATOR")
                        .setConcealed()
                        .registerResearchItem();

                // Font of Ecology
                new ResearchItem(
                    "FONT_OF_ECOLOGY",
                    // #tr tc.research_name.FONT_OF_ECOLOGY
                    // # {\BLUE}{\BOLD}Font of Ecology
                    // #zh_CN {\BLUE}{\BOLD}生态泉源
                    "BASICS",
                    (new AspectList()).add(EVOLUTION, 1)
                        .add(Aspect.ENTROPY, 1)
                        .add(Aspect.ELDRITCH, 1)
                        .add(Aspect.LIFE, 1)
                        .add(EVOLUTION, 1)
                        .add(Aspect.ORDER, 1)
                        .add(Aspect.WATER, 1)
                        .add(Aspect.EXCHANGE, 1),
                    9,
                    -9,
                    10,
                    GTCMItemList.FountOfEcology.get(1, 0)).setPages(
                        // spotless:off
                        // #tr tc.research_text.FONT_OF_ECOLOGY.1
                        // # In the vortex of time, life’s threads weave a complex tapestry, with genes as invisible brushstrokes shaping nature’s will. These silent connections guide evolution subtly.<BR>Yet, behind this facade, thaumaturges discovered a peculiar jellyfish—an origin hidden within natural law’s cracks, embodying all genetic expressions and whispering ancient secrets.<BR>It represents the ultimate convergence of evolution, with countless interwoven genes, as if mocking life itself.<BR>No one knows why it’s here.
                        // #zh_CN 在时间的漩涡中, 生命的脉络编织成复杂的画卷, 基因作为无形的笔触, 暗中涂抹着自然的意志. 这些沉默的串联悄然引导着生命的每一步演化. <BR>然而, 在这看似秩序井然的背后, 神秘使们发现了一只诡异的水母——一个隐匿于自然法则裂缝中的源头, 凝聚了万物的基因表现, 仿佛在低语着远古的秘密. <BR>它是所有生物进化的终极汇聚, 包含着无数基因的交错与融合, 仿佛宇宙对生命的反讽.<BR>没有人知道它为什么在这里.
                        new ResearchPage("tc.research_text.FONT_OF_ECOLOGY.1"),
                        // #tr tc.research_text.FONT_OF_ECOLOGY.2
                        // # Thaumaturges attempted to interpret this strange phenomenon but discovered it was not a product of natural evolution, but a distortion caused by a deeper force, as if nature itself were guiding life towards an indescribable end.<BR>Through reverse engineering it
                        // #zh_CN 神秘使们试图解读这诡谲的现象, 却发现它并非自然演化的产物, 而是某种更为深邃的力量所引发的扭曲, 仿佛自然本身在引导着生命朝向某个不可名状的终点. <BR>通过对它的逆向研究
                        // spotless:on
                        new ResearchPage("tc.research_text.FONT_OF_ECOLOGY.2"),
                        new ResearchPage(
                            new InfusionRecipe(
                                "FONT_OF_ECOLOGY",
                                GTCMItemList.FountOfEcology.get(1),
                                200,
                                (new AspectList()).merge(EVOLUTION, 1024)
                                    .merge(Aspect.WATER, 65536)
                                    .merge(Aspect.LIFE, 16384)
                                    .merge(Aspect.FLESH, 4096)
                                    .merge(ULTRA_DEATH, 256),
                                Mods.Witchery.isModLoaded()
                                    ? GT_ModHandler.getModItem(Mods.Witchery.ID, "infinityegg", 1)
                                    : new ItemStack(Blocks.dragon_egg, 1),
                                new ItemStack[] { GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                                    GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                                    GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                                    GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                                    GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                                    GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                                    GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                                    GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                                    GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                                    GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1) })))
                        .setParents("OFFSPRING")
                        .setConcealed()
                        .registerResearchItem();
            }
        }
    }
}
