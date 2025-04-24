package com.Nxer.TwistSpaceTechnology.system.Thaumcraft;

import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic.EVOLUTION;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.crucibleRecipeArcaneHole;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeBloodHatch;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeBloodyHell;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereSimulator;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeElvenWorkshop;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeFontOfEcology;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeIndustrialAlchemyTower;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeIndustrialMagicMatrix;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipePrimordialDisjunctus;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeSkypiercerTower;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeTimeBendingSpeedRune;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class TCResearches {

    private static final String TST_Path = "gtnhcommunitymod";

    public static void register() {
        loadResearchTab();
        loadResearches();
    }

    public static void loadResearchTab() {
        ResearchCategories.registerCategory(
            "TST",
            new ResourceLocation(TST_Path, "textures/items/MetaItem01/33.png"),
            new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));
        // #tr tc.research_category.TST
        // # Twist Space Technology
        // #zh_CN 扭曲空间科技
    }

    public static void loadResearches() {
        new ResearchItem(
            "TST_WELCOME",
            // #tr tc.research_name.TST_WELCOME
            // # Twist Space Technology
            // #zh_CN 扭曲空间科技
            // #tr tc.research_text.TST_WELCOME
            // # It's so cooooooooooooooooool
            // #zh_CN It's so cooooooooooooooooool
            "TST",
            (new AspectList()),
            0,
            0,
            0,
            new ResourceLocation(TST_Path, "textures/items/MegaDreamMasterXXL.png")).setAutoUnlock()
                .registerResearchItem()
                .setPages(
                    // #tr tc.research_text.TST_WELCOME.1
                    // # Welcome to the New Horizons
                    // #zh_CN 欢迎来到新世界
                    new ResearchPage(TextEnums.tr("tc.research_text.TST_WELCOME.1")))
                .setSpecial()
                .registerResearchItem();

        new ResearchItem(
            "BH_ELVEN_WORKSHOP",
            "TST",
            (new AspectList()).merge(Aspect.EARTH, 1)
                .merge(Aspect.MECHANISM, 1)
                .merge(Aspect.MAGIC, 1),
            -1,
            -2,
            3,
            GTCMItemList.ElvenWorkshop.get(1, 0)).setParents("BH_GAIA_PYLON")
                .setPages(
                    new ResearchPage("tc.research_text.BH_ELVEN_WORKSHOP.1"),
                    new ResearchPage("tc.research_text.BH_ELVEN_WORKSHOP.2"),
                    new ResearchPage(infusionRecipeElvenWorkshop))
                .setParents("TST_WELCOME")
                .registerResearchItem();

        if (Config.Enable_IndustrialMagicMatrix) {
            new ResearchItem(
                "INDUSTRIAL_MAGIC_MATRIX",
                "TST",
                (new AspectList()).merge(Aspect.EARTH, 1)
                    .merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1),
                1,
                -2,
                5,
                GTCMItemList.IndustrialMagicMatrix.get(1, 0))/* .setParents("ICHORIUM") */
                    .setPages(
                        // spotless:off
                        // #tr tc.research_text.INDUSTRIAL_MAGIC_MATRIX.1
                        // # Death, Evil, Abomination, Grievance, Murderous Intent, Curse of Misfortune, Hell, Ethics, Fool, Tyrant, Sinner, Cunning, Thief, Despicable, Evil, Poison, Hunger, Epidemic, Earthquake, Heavenly Change, Alien, Human, Calamity Forever, Time, Spirit, Root, Fiction, Darkness, Innocence, Life, or Something Called Fear.
                        // #zh_CN 死、邪恶、憎恶、怨嗟、杀意、不幸诅咒、地狱、伦理、愚者、暴君、罪人、狡猾、贼徒、卑劣、恶、毒、饥饿、疫病、地震、天变、异形、人间、灾厄永远、时间、精神、根源、虚构、黑暗、无垢、命或者被称为恐惧之物.
                        // spotless:on
                        new ResearchPage(TextEnums.tr("tc.research_text.INDUSTRIAL_MAGIC_MATRIX.1")),
                        new ResearchPage(infusionRecipeIndustrialMagicMatrix))
                    .setParents("TST_WELCOME")
                    .registerResearchItem();
        }

        if (Config.Enable_MegaTreeFarm) {
            // Machine
            new ResearchItem(
                "ECO_SPHERE_SIMULATOR",
                // #tr tc.research_name.ECO_SPHERE_SIMULATOR
                // # Eco-Sphere Simulator
                // #zh_CN 拟似生态圈
                // #tr tc.research_text.ECO_SPHERE_SIMULATOR
                // # Cool, when can we cage the Wither?
                // #zh_CN 酷, 我们什么时候能圈养凋零？
                "TST",
                (new AspectList()).merge(Aspect.TREE, 1)
                    .merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.WATER, 1)
                    .merge(Aspect.PLANT, 1)
                    .merge(Aspect.ELDRITCH, 1)
                    .merge(Aspect.FLESH, 1),
                2,
                0,
                10,
                GTCMItemList.MegaTreeFarm.get(1, 0)).setPages(
                    // spotless:off
                    // #tr tc.research_text.ECO_SPHERE_SIMULATOR.1
                    // # A device that fuses arcane and technological forces, designed to simulate a natural ecosystem.<BR>{\BOLD}Usage Instructions: <BR>{\RESET}Place the machine and connect it to an adjustable power source. The device will draw ambient vis, ensuring natural tree and aquatic life growth.<BR>{\BOLD}Precautions: <BR>{\RESET}Under high voltage, efficiency may decrease, but output will increase. Adjust power input accordingly.<BR>Aquatic life is usually stable; report any non-listed entities immediately.
                    // #zh_CN 一种将奥术与科技融合的装置, 用于模拟自然生态. <BR>{\BOLD}使用方法:<BR>{\RESET}将机器放置在合适的地点, 并连接到可调节功率的外部电源. 装置会自动汲取环境的源质, 保障树木与水生生物的自然生长. <BR>{\BOLD}注意事项:<BR>{\RESET}高电压下运行时, 尽管效率降低, 总产出会有所增加, 使用者应合理调整电力输入. <BR>水生生物通常不会异常, 如果出现非列表生物, 请立即通报上级部门.
                    new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_SIMULATOR.1")),
                    new ResearchPage(infusionRecipeEcoSphereSimulator),
                    // #tr tc.research_text.ECO_SPHERE_SIMULATOR.2
                    // # <LINE>{\BOLD}Operation Log Record:<BR>{\RESET}When operating at low voltage, the machine exhibits extremely high simulation efficiency, remaining the ecosystem exceptionally stable. <BR>As the voltage exceeds a threshold, the machine's efficiency decreases, while the aquatic area shows invisible pressure, causing subtle distortions in the simulation.<BR>In rare cases, observers have reported seeing a strange entity in the water. It emits a faint glow, resembling a jellyfish but with an indescribable quality.
                    // #zh_CN <LINE>{\BOLD}运行日志记录：<BR>{\RESET}在低电压运行时, 机器展现出极高的模拟效率, 模拟生态系统的状态异常稳定. <BR>当电压超过某个临界值时, 机器的运行效率开始逐渐下降, 但水域却似乎处于某种不可见的压力之下, 使得模拟出现微妙的扭曲与偏差. <BR>极少数情况下, 观察者报告称他们目睹了一种罕见且奇异的生物出现在水域中. 这种生物散发着微弱的光芒, 形态如同水母的轮廓, 但却拥有某种不可名状的特质.
                    new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_SIMULATOR.2")),
                    // #tr tc.research_text.ECO_SPHERE_SIMULATOR.3
                    // # <LINE><BR><BR>Rumor has it that those thaumaturges who encounter this mutated entity will gain an indescribable revelation.<BR>This revelation not only deepens their understanding of the arcane but could even drive the machine's own evolution, making its functions more powerful and extraordinary.<BR>Though this phenomenon is exceedingly rare, thaumaturges devoted to the exploration of the arcane seem to glimpse a higher pursuit within it, fueling their desire for the unknown.
                    // #zh_CN <LINE><BR><BR>有传言称,遇见这种变异生物的神秘使,将会从中获得一种无法言明的启示.<BR>这种启示不仅能深化对奥术的理解,甚至能够推动机器本身的进化,使其功能变得更为强大且不可思议.<BR>尽管这种现象极其罕见,但那些执着于奥术探索的神秘使们,似乎从中看到了某种更高的追求,激发了他们对未知的渴望.
                    // spotless:on
                    new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_SIMULATOR.3")))
                    .setParents("TST_WELCOME")
                    .setConcealed()
                    .registerResearchItem();

            // Offspring
            new ResearchItem(
                "OFFSPRING",
                // #tr tc.research_name.OFFSPRING
                // # {\DARK_AQUA}"Offspring"
                // #zh_CN {\DARK_AQUA}"子代"
                // #tr tc.research_text.OFFSPRING
                // # Little jellyfish. So can we cage the Wither now?
                // #zh_CN 小水母, 所以我们能圈养凋零了吗?
                "TST",
                (new AspectList()).merge(Aspect.WATER, 1)
                    .merge(Aspect.EXCHANGE, 1)
                    .merge(Aspect.LIFE, 1),
                3,
                0,
                10,
                GTCMItemList.OffSpring.get(1, 0))
                    // #tr tc.research_text.OFFSPRING.1
                    // # What's this?
                    // #zh_CN 这是什么?
                    .setPages(new ResearchPage("tc.research_text.OFFSPRING.1"))
                    .setHidden()
                    .setAspectTriggers(EVOLUTION)
                    .setParents("ECO_SPHERE_SIMULATOR")
                    .registerResearchItem();

            // Evolution
            new ResearchItem(
                "EVOLUTIO",
                // #tr tc.research_name.EVOLUTIO
                // # Evolutio
                // #zh_CN Evolutio
                // #tr tc.research_text.EVOLUTIO
                // # New Aspect. What does it have to do with caging the Wither?
                // #zh_CN 新源质, 这和圈养凋零有什么关系?
                "TST",
                (new AspectList()).add(EVOLUTION, 1)
                    .add(Aspect.LIFE, 1)
                    .add(Aspect.EXCHANGE, 1),
                4,
                -1,
                5,
                Mods.Gendustry.isModLoaded() ? GTModHandler.getModItem(Mods.Gendustry.ID, "LiquidDNABucket", 1)
                    : new ItemStack(Items.water_bucket, 1))
                        .setPages(
                            new ResearchPage((new AspectList()).add(EVOLUTION, 1)),
                            // #tr tc.research_text.EVOLUTIO.1
                            // # <LINE>A sense of familiarity?
                            // #zh_CN <LINE>似曾相识?
                            new ResearchPage("tc.research_text.EVOLUTIO.1"))
                        .setParents("OFFSPRING")
                        .setConcealed()
                        .setRound()
                        .registerResearchItem();

            // Font of Ecology
            new ResearchItem(
                "FONT_OF_ECOLOGY",
                // #tr tc.research_name.FONT_OF_ECOLOGY
                // # {\BLUE}{\BOLD}Font of Ecology
                // #zh_CN {\BLUE}{\BOLD}生态泉源
                // #tr tc.research_text.FONT_OF_ECOLOGY
                // # We can cage the Wither now!
                // #zh_CN 我们能圈养凋零了!
                "TST",
                (new AspectList()).add(EVOLUTION, 1)
                    .add(Aspect.ENTROPY, 1)
                    .add(Aspect.ELDRITCH, 1)
                    .add(Aspect.LIFE, 1)
                    .add(EVOLUTION, 1)
                    .add(Aspect.ORDER, 1)
                    .add(Aspect.WATER, 1)
                    .add(Aspect.EXCHANGE, 1),
                5,
                0,
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
                    new ResearchPage(infusionRecipeFontOfEcology))
                    .setParents("OFFSPRING")
                    .setConcealed()
                    .registerResearchItem();
        }

        if (Config.Enable_BloodHell) {

            // #tr tc.research_name.BLOODY_HELL
            // # Bloody Hell
            // #zh_CN 血狱
            // #tr tc.research_text.BLOODY_HELL
            // # BLOOD, BLOOD, BLOOD!
            // #zh_CN 血！血！血！
            new ResearchItem(
                "BLOODY_HELL",
                "TST",
                new AspectList().merge(Aspect.LIFE, 1)
                    .merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1),
                1,
                2,
                5,
                GTCMItemList.BloodyHell.get(1, 0)).setPages(
                    // #tr tc.research_text.BLOODY_HELL.1
                    // # BLOOD, BLOOD, BLOOD!
                    // #zh_CN 血！血！血！
                    new ResearchPage(TextEnums.tr("tc.research_text.BLOODY_HELL.1")),
                    new ResearchPage(infusionRecipeBloodyHell))
                    .setParents("TST_WELCOME")
                    .registerResearchItem();

            if (Config.Enable_BloodHatch) {
                // #tr tc.research_name.BLOOD_HATCH
                // # Blood Hatch
                // #zh_CN 血液仓
                // #tr tc.research_text.BLOOD_HATCH
                // # BLOOD, BLOOD, BLOOD!
                // #zh_CN 血！血！血！
                new ResearchItem(
                    "BLOOD_HATCH",
                    "TST",
                    new AspectList().merge(Aspect.LIFE, 1)
                        .merge(Aspect.MAGIC, 1)
                        .merge(Aspect.TOOL, 1),
                    2,
                    3,
                    5,
                    GTCMItemList.BloodOrbHatch.get(1, 0)).setPages(
                        // #tr tc.research_text.BLOOD_HATCH.1
                        // # The zombie brains are thirst for blood. Maybe we can make use of this.
                        // #zh_CN 僵尸的脑子渴望得到血液。也许我们能够利用这一点。
                        new ResearchPage(TextEnums.tr("tc.research_text.BLOOD_HATCH.1")),
                        new ResearchPage(infusionRecipeBloodHatch))
                        .setParents("BLOODY_HELL")
                        .setSecondary()
                        .registerResearchItem();
            }

            // #tr tc.research_name.TIME_BENDING_SPEED_RUNE
            // # Time-bending Speed Rune
            // #zh_CN 时间扭曲速度符文
            // #tr tc.research_text.TIME_BENDING_SPEED_RUNE
            // # Electrotine Torch,Start!
            // #zh_CN 蓝石火把,启动!
            new ResearchItem(
                "TIME_BENDING_SPEED_RUNE",
                "TST",
                new AspectList().merge(Aspect.LIFE, 1)
                    .merge(Aspect.MAGIC, 1)
                    .merge(Aspect.TOOL, 1),
                2,
                5,
                5,
                new ItemStack(TstBlocks.TimeBendingSpeedRune)).setPages(
                    // spotless:off
                    // #tr tc.research_text.TIME_BENDING_SPEED_RUNE.1
                    // # The SpaceTime bends with Speed Runes and Accelerators, and it showed the compatibility to the advanced Altars.
                    // #zh_CN 使用速度符文和世界加速器扭曲的时空展现出对高级祭坛的兼容性。
                    // spotless:on
                    new ResearchPage(TextEnums.tr("tc.research_text.TIME_BENDING_SPEED_RUNE.1")),
                    new ResearchPage(infusionRecipeTimeBendingSpeedRune))
                    .setParents("BLOODY_HELL")
                    .setSecondary()
                    .registerResearchItem();

            if (Config.Enable_IndustrialAlchemyTower) {
                // #tr tc.research_name.INDUSTRIAL_ALCHEMY_TOWER
                // # Industrial Alchemy Tower
                // #zh_CN 工业炼金塔
                // #tr tc.research_text.INDUSTRIAL_ALCHEMY_TOWER
                // # Batch alchemy!
                // #zh_CN 批量化炼金!
                new ResearchItem(
                    "INDUSTRIAL_ALCHEMY_TOWER",
                    "TST",
                    new AspectList().merge(Aspect.AIR, 1)
                        .merge(Aspect.FIRE, 1)
                        .merge(Aspect.ENTROPY, 1)
                        .merge(Aspect.ORDER, 1)
                        .merge(Aspect.EXCHANGE, 1),
                    -1,
                    2,
                    9,
                    GTCMItemList.IndustrialAlchemyTower.get(1)).setPages(
                        // spotless:off
                        // #tr tc.research_text.INDUSTRIAL_ALCHEMY_TOWER.1
                        // # Your power is unprecedentedly strong, and with a little experimentation, you have created this machine: a machine capable of batch processing thaumic crucible recipes. It's just that this machine needs to be sealed, which is a good thing, right?
                        // #zh_CN 你的力量空前强大，稍加尝试便创造出了这台机器：一台能够批量化进行神秘坩埚配方的机器。只不过这台机器需要密封，这是件好事对吧？
                        // spotless:on
                        new ResearchPage(TextEnums.tr("tc.research_text.INDUSTRIAL_ALCHEMY_TOWER.1")),
                        new ResearchPage(infusionRecipeIndustrialAlchemyTower))
                        .setParents("TST_WELCOME")
                        .registerResearchItem();
            }

            // #tr tc.research_name.TST_ARCANE_HOLE
            // # Arcane Hole
            // #zh_CN 奥术裂隙
            // #tr tc.research_text.TST_ARCANE_HOLE
            // # Block in the void
            // #zh_CN 虚空中之物
            new ResearchItem(
                "TST_ARCANE_HOLE",
                "TST",
                new AspectList().merge(Aspect.DARKNESS, 4)
                    .merge(Aspect.VOID, 4)
                    .merge(Aspect.SENSES, 8),
                -2,
                2,
                1,
                new ItemStack(TstBlocks.BlockArcaneHole)).setPages(
                    // spotless:off
                // #tr tc.research_text.TST_ARCANE_HOLE.1
                // # Can be used to replace the warded glass on both sides of industrial alchemy tower. Perhaps it's still a good building block?
                // #zh_CN 可以用来替代工业炼金塔两侧的守卫者玻璃。或许还是一种不错的建筑方块？
                // spotless:on
                    new ResearchPage(TextEnums.tr("tc.research_text.TST_ARCANE_HOLE.1")),
                    new ResearchPage(crucibleRecipeArcaneHole))
                    .setParents("TST_WELCOME")
                    .setSecondary()
                    .registerResearchItem();
        }
        if (Config.Enable_PrimordialDisjunctus) {
            // #tr tc.research_name.PRIMORDIAL_DISJUNCTUS
            // # Primordial Disjunctus
            // #zh_CN 初源解离机
            // #tr tc.research_text.PRIMORDIAL_DISJUNCTUS
            // # Elementary essentia free!
            // #zh_CN 初等源质自由!
            new ResearchItem(
                "PRIMORDIAL_DISJUNCTUS",
                "TST",
                new AspectList().merge(Aspect.TOOL, 1)
                    .merge(Aspect.HUNGER, 1)
                    .merge(Aspect.MINE, 1)
                    .merge(Aspect.AURA, 1),
                0,
                4,
                9,
                GTCMItemList.PrimordialDisjunctus.get(1)).setPages(
                    // spotless:off
                    // #tr tc.research_text.PRIMORDIAL_DISJUNCTUS.1
                    // # The first step in the freedom of source matter
                    // #zh_CN 源质自由的第一步!
                    // spotless:on
                    new ResearchPage(TextEnums.tr("tc.research_text.PRIMORDIAL_DISJUNCTUS.1")),
                    new ResearchPage(infusionRecipePrimordialDisjunctus))
                    .setParents("TST_WELCOME")
                    .registerResearchItem();
        }
        if (Config.Enable_SkypiercerTower) {
            // #tr tc.research_name.SKYPIERCER_TOWER
            // # Skypiercer Tower
            // #zh_CN 穿云尖塔
            // #tr tc.research_text.SKYPIERCER_TOWER
            // # Crafting Essentia On Demand!
            // #zh_CN 源质自由!
            new ResearchItem(
                "SKYPIERCER_TOWER",
                "TST",
                new AspectList().merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1)
                    .merge(Aspect.AURA, 1)
                    .merge(Aspect.ENERGY, 1),
                0,
                6,
                9,
                GTCMItemList.SkypiercerTower.get(1)).setPages(
                    // spotless:off
                    // #tr tc.research_text.SKYPIERCER_TOWER.1
                    // # {\BOLD}Aspect tier and machine processing time rules:{\RESET}<BR><BR>{\BOLD}Aspect Tier:<BR>{\RESET}Primal aspects are Tier 0.<BR> Composite aspect (aspects made from composite/primal aspects) take the highest tier component and add 1 to determine its tier.<BR><BR>{\BOLD}Processing Time:<BR>{\RESET}An aspect tier of 'x' requires 2 * x seconds (excluding time to synthesize its components).
                    // #zh_CN {\BOLD}要素等级与机器加工时间规则:{\RESET}<BR><BR>{\BOLD}要素等级:<BR>{\RESET}初等要素为0级。<BR>复合要素(由初等或其他复合要素组成)等级为其子要素等级较高者加1.<BR><BR>{\BOLD}加工时间:<BR>{\RESET}等级为 x 的要素需加工 2 * x 秒(不包括合成其组成部分所需的时间).
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.1")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.2
                    // # {\BOLD}Recursive Synthesis Note:{\RESET}<BR><BR>All composite aspects are synthesized entirely from primal aspects. Each composite aspect must be synthesized step by step. Meaning that the total synthesized time for high tier composite aspects can differ quite a bit.<BR>{\BOLD}(see next page for timings){\RESET}<BR><BR>{\BOLD}Critical:<BR>{\RESET}Primal aspects (tier 0) cannot be synthesized. They must be made available to the Skypiercer Tower through the infusion provider or the machine will fail to start.
                    // #zh_CN {\BOLD}关于递归合成的说明:{\RESET}<BR><BR>所有复合要素都必须由初等要素逐步合成,每一个复合要素都需一层层构建.因此,高等级复合要素的总合成时间会迅速增加(实际上是指数级增长)<BR>{\BOLD}(具体时间请参见下一页){\RESET}<BR><BR>{\BOLD}注意:<BR>{\RESET}初等要素(0级)无法被合成,必须通过注魔供应器提供给穿云尖塔,否则机器将无法启动.
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.2")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.3
                    // #en_US {\BOLD}Tier 1 Compound Aspects{\RESET}<BR>{\BOLD}Gelum:{\RESET} 2 seconds<BR>{\BOLD}Lux:{\RESET} 2 seconds<BR>{\BOLD}Motus:{\RESET} 2 seconds<BR>{\BOLD}Permutatio:{\RESET} 2 seconds<BR>{\BOLD}Potentia:{\RESET} 2 seconds<BR>{\BOLD}Tempestas:{\RESET} 2 seconds<BR>{\BOLD}Vacuos:{\RESET} 2 seconds<BR>{\BOLD}Venenum:{\RESET} 2 seconds<BR>{\BOLD}Victus:{\RESET} 2 seconds<BR>{\BOLD}Vitreus:{\RESET} 2 seconds
                    // #zh_CN {\BOLD}一级复合要素{\RESET}<BR>{\BOLD}寒冰:{\RESET} 2 秒<BR>{\BOLD}光明:{\RESET} 2 秒<BR>{\BOLD}移动:{\RESET} 2 秒<BR>{\BOLD}交换:{\RESET} 2 秒<BR>{\BOLD}能量:{\RESET} 2 秒<BR>{\BOLD}气候:{\RESET} 2 秒<BR>{\BOLD}虚空:{\RESET} 2 秒<BR>{\BOLD}毒药:{\RESET} 2 秒<BR>{\BOLD}生命:{\RESET} 2 秒<BR>{\BOLD}水晶:{\RESET} 2 秒
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.3")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.4
                    // #en_US {\BOLD}Tier 2 Compound Aspects (1/2){\RESET}<BR>{\BOLD}Bestia:{\RESET} 8 seconds<BR>{\BOLD}Fames:{\RESET} 8 seconds<BR>{\BOLD}Herba:{\RESET} 6 seconds<BR>{\BOLD}Iter:{\RESET} 6 seconds<BR>{\BOLD}Limus:{\RESET} 6 seconds<BR>{\BOLD}Metalum:{\RESET} 6 seconds<BR>{\BOLD}Mortuus:{\RESET} 6 seconds<BR>{\BOLD}Praecantio:{\RESET} 8 seconds<BR>{\BOLD}Radio:{\RESET} 8 seconds<BR>{\BOLD}Sano:{\RESET} 6 seconds
                    // #zh_CN {\BOLD}二级复合要素 (1/2){\RESET}<BR>{\BOLD}野兽:{\RESET} 8 秒<BR>{\BOLD}饥饿:{\RESET} 8 秒<BR>{\BOLD}植物:{\RESET} 6 秒<BR>{\BOLD}旅行:{\RESET} 6 秒<BR>{\BOLD}粘液:{\RESET} 6 秒<BR>{\BOLD}金属:{\RESET} 6 秒<BR>{\BOLD}死亡:{\RESET} 6 秒<BR>{\BOLD}魔力:{\RESET} 8 秒<BR>{\BOLD}Radio:{\RESET} 8 秒<BR>{\BOLD}治疗:{\RESET} 6 秒

                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.4")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.5
                    // #en_US {\BOLD}Tier 2 Compound Aspects (2/2){\RESET}<BR>{\BOLD}Tempus:{\RESET} 6 seconds<BR>{\BOLD}Tenebrae:{\RESET} 6 seconds<BR>{\BOLD}Vinculum:{\RESET} 6 seconds<BR>{\BOLD}Volatus:{\RESET} 6 seconds
                    // #zh_CN {\BOLD}二级复合要素 (2/2){\RESET}<BR>{\BOLD}Tempus:{\RESET} 6 秒<BR>{\BOLD}黑暗:{\RESET} 6 秒<BR>{\BOLD}陷阱:{\RESET} 6 秒<BR>{\BOLD}飞行:{\RESET} 6 秒
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.5")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.6
                    // #en_US {\BOLD}Tier 3 Compound Aspects (1/2){\RESET}<BR>{\BOLD}Alienis:{\RESET} 14 seconds<BR>{\BOLD}Arbor:{\RESET} 12 seconds<BR>{\BOLD}Auram:{\RESET} 14 seconds<BR>{\BOLD}Corpus:{\RESET} 20 seconds<BR>{\BOLD}Exanimis:{\RESET} 14 seconds<BR>{\BOLD}Gula:{\RESET} 16 seconds<BR>{\BOLD}Infernus:{\RESET} 14 seconds<BR>{\BOLD}Magneto:{\RESET} 18 seconds<BR>{\BOLD}Spiritus:{\RESET} 14 seconds<BR>{\BOLD}Superbia:{\RESET} 14 seconds
                    // #zh_CN {\BOLD}三级复合要素 (1/2){\RESET}<BR>{\BOLD}异域:{\RESET} 14 秒<BR>{\BOLD}树木:{\RESET} 12 秒<BR>{\BOLD}灵气:{\RESET} 14 秒<BR>{\BOLD}肉体:{\RESET} 20 秒<BR>{\BOLD}不死:{\RESET} 14 秒<BR>{\BOLD}Gula:{\RESET} 16 秒<BR>{\BOLD}Infernus:{\RESET} 14 秒<BR>{\BOLD}Magneto:{\RESET} 18 秒<BR>{\BOLD}灵魂:{\RESET} 14 秒<BR>{\BOLD}Superbia:{\RESET} 14 秒
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.6")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.7
                    // #en_US {\BOLD}Tier 3 Compound Aspects (2/2){\RESET}<BR>{\BOLD}Vitium:{\RESET} 14 seconds
                    // #zh_CN {\BOLD}三级复合要素 (2/2){\RESET}<BR>{\BOLD}污染:{\RESET} 14 秒
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.7")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.8
                    // #en_US {\BOLD}Tier 4 Compound Aspects{\RESET}<BR>{\BOLD}Cognito:{\RESET} 22 seconds<BR>{\BOLD}Desidia:{\RESET} 28 seconds<BR>{\BOLD}Luxuria:{\RESET} 36 seconds<BR>{\BOLD}Sensus:{\RESET} 22 seconds
                    // #zh_CN {\BOLD}四级复合要素{\RESET}<BR>{\BOLD}认知:{\RESET} 22 秒<BR>{\BOLD}Desidia:{\RESET} 28 秒<BR>{\BOLD}Luxuria:{\RESET} 36 秒<BR>{\BOLD}感官:{\RESET} 22 秒
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.8")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.9
                    // #en_US {\BOLD}Tier 5 Compound Aspects{\RESET}<BR>{\BOLD}Humanus:{\RESET} 40 seconds<BR>{\BOLD}Invidia:{\RESET} 40 seconds<BR>{\BOLD}Strontio:{\RESET} 32 seconds
                   // #zh_CN {\BOLD}五级复合要素{\RESET}<BR>{\BOLD}人类:{\RESET} 40 秒<BR>{\BOLD}Invidia:{\RESET} 40 秒<BR>{\BOLD}Strontio:{\RESET} 32 秒
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.9")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.10
                    // #en_US {\BOLD}Tier 6 Compound Aspects{\RESET}<BR>{\BOLD}Instrumentum:{\RESET} 52 seconds<BR>{\BOLD}Lucrum:{\RESET} 60 seconds<BR>{\BOLD}Messis:{\RESET} 58 seconds<BR>{\BOLD}Perforio:{\RESET} 52 seconds
                    // #zh_CN {\BOLD}六级复合要素{\RESET}<BR>{\BOLD}工具:{\RESET} 52 秒<BR>{\BOLD}贪婪:{\RESET} 60 秒<BR>{\BOLD}作物:{\RESET} 58 秒<BR>{\BOLD}矿藏:{\RESET} 52 秒
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.10")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.11
                    // #en_US {\BOLD}Tier 7 Compound Aspects{\RESET}<BR>{\BOLD}Fabrico:{\RESET} 106 seconds<BR>{\BOLD}Machina:{\RESET} 68 seconds<BR>{\BOLD}Meto:{\RESET} 124 seconds<BR>{\BOLD}Nebrisum:{\RESET} 126 seconds<BR>{\BOLD}Pannus:{\RESET} 74 seconds<BR>{\BOLD}Telum:{\RESET} 66 seconds<BR>{\BOLD}Terminus:{\RESET} 88 seconds<BR>{\BOLD}Tutamen:{\RESET} 66 seconds
                    // #zh_CN {\BOLD}七级复合要素{\RESET}<BR>{\BOLD}合成:{\RESET} 106 秒<BR>{\BOLD}机械:{\RESET} 68 秒<BR>{\BOLD}收获:{\RESET} 124 秒<BR>{\BOLD}Nebrisum:{\RESET} 126 秒<BR>{\BOLD}布匹:{\RESET} 74 秒<BR>{\BOLD}武器:{\RESET} 66 秒<BR>{\BOLD}Terminus:{\RESET} 88 秒<BR>{\BOLD}防护:{\RESET} 66 秒
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.11")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.12
                    // #en_US {\BOLD}Tier 8 Compound Aspects{\RESET}<BR>{\BOLD}Electrum:{\RESET} 86 seconds<BR>{\BOLD}Ira:{\RESET} 82 seconds
                    // #zh_CN {\BOLD}八级复合要素{\RESET}<BR>{\BOLD}Electrum:{\RESET} 86 秒<BR>{\BOLD}Ira:{\RESET} 82 秒
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.12")),
                    // #tr tc.research_text.SKYPIERCER_TOWER.13
                    // #en_US {\BOLD}Piercing the sky:{\RESET}<BR><BR>As an upgrade to this multi-block you can increase its processing speed by 16x for each layer (additively). If you want to produce a lot of aspects with this multi-block on demand or maintained up to a level you will need a lot of time, power or rings. So... better install more rings. Otherwise, are you truly worthy of the name "Skypiercer"?<BR>
                    // #zh_CN {\BOLD}穿云尖塔:{\RESET}<BR><BR>作为该多方块结构的附加升级,你可以通过每增加一层提升其16倍的处理速度(可叠加).如果你希望随时或维持地大量产出要素,你将需要大量的时间,电力或环装置.所以……还是多装些环吧.不然怎么称得上“穿云”？<BR>
                    // spotless:on
                    new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.13")),
                    new ResearchPage(infusionRecipeSkypiercerTower))
                    .setParents("PRIMORDIAL_DISJUNCTUS")
                    .registerResearchItem();
        }
    }
}
