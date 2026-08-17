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
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeInfusionMaterialDispenser;
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

import crazypants.enderio.EnderIO;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class TCResearches {

    private static final String TST_Path = "gtnhcommunitymod";
    private static final String ROOT_RESEARCH = "TST_WELCOME";

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
        // spotless:off
        // #tr tc.research_name.TST_WELCOME
        // # Twist Space Technology
        // #zh_CN 扭曲空间科技

        // #tr tc.research_text.TST_WELCOME
        // # It's so cooooooooooooooooool
        // #zh_CN It's so cooooooooooooooooool

        // #tr tc.research_text.TST_WELCOME.1
        // # Welcome to the New Horizons
        // #zh_CN 欢迎来到新世界
        // spotless:on
        new ResearchItem(
            "TST_WELCOME",
            "TST",
            (new AspectList()),
            0,
            0,
            0,
            new ResourceLocation(TST_Path, "textures/items/MegaDreamMasterXXL.png")).setAutoUnlock()
                .registerResearchItem()
                .setPages(new ResearchPage(TextEnums.tr("tc.research_text.TST_WELCOME.1")))
                .setSpecial()
                .registerResearchItem();

        new ResearchItem(
            "BH_ELVEN_WORKSHOP",
            "TST",
            (new AspectList()).merge(Aspect.EARTH, 1)
                .merge(Aspect.MECHANISM, 1)
                .merge(Aspect.MAGIC, 1),
            4,
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
            // spotless:off
            // #tr tc.research_text.INDUSTRIAL_MAGIC_MATRIX.1
            // # Death, Evil, Abomination, Grievance, Murderous Intent, Curse of Misfortune, Hell, Ethics, Fool, Tyrant, Sinner, Cunning, Thief, Despicable, Evil, Poison, Hunger, Epidemic, Earthquake, Heavenly Change, Alien, Human, Calamity Forever, Time, Spirit, Root, Fiction, Darkness, Innocence, Life, or Something Called Fear.
            // #zh_CN 死、邪恶、憎恶、怨嗟、杀意、不幸诅咒、地狱、伦理、愚者、暴君、罪人、狡猾、贼徒、卑劣、恶、毒、饥饿、疫病、地震、天变、异形、人间、灾厄永远、时间、精神、根源、虚构、黑暗、无垢、命或者被称为恐惧之物.
            // spotless:on
            new ResearchItem(
                "INDUSTRIAL_MAGIC_MATRIX",
                "TST",
                (new AspectList()).merge(Aspect.EARTH, 1)
                    .merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1),
                -4,
                -2,
                5,
                GTCMItemList.IndustrialMagicMatrix.get(1, 0))/* .setParents("ICHORIUM") */
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.INDUSTRIAL_MAGIC_MATRIX.1")),
                        new ResearchPage(infusionRecipeIndustrialMagicMatrix))
                    .setParents("TST_WELCOME")
                    .registerResearchItem();
        }

        if (Config.Enable_EcoSphereSimulator) {
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_SIMULATOR
            // # Eco-Sphere Simulator
            // #zh_CN 拟似生态圈

            // #tr tc.research_text.ECO_SPHERE_SIMULATOR
            // # A habitat assembled from instructions rather than soil.
            // #zh_CN 一座由指令而非土壤构成的栖息地.

            // #tr tc.research_text.ECO_SPHERE_SIMULATOR.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}The Eco-Sphere Simulator is a large habitat that imitates selected natural processes. It does not decide what kind of environment to create on its own: place a mode beacon in the controller, provide the requested samples and medium, and the structure will prepare itself before operation.<BR>Changing the beacon causes the habitat to drain, clean, and wait for the next environment.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}拟似生态圈是一座用于模仿特定自然过程的大型栖息地. 它不会自行决定要形成何种环境: 将模式信标放入主机, 再提供对应样本与介质, 结构便会在运行前自行完成准备.<BR>更换信标后, 栖息地会排空、清理并等待下一种环境.

            // #tr tc.research_text.ECO_SPHERE_SIMULATOR.2
            // # <LINE>{\BOLD}Operation Record ESS-00<BR>{\RESET}The first complete structure remained silent until a beacon was placed in the controller. Removing the beacon afterward did not erase the selected environment; installing a different beacon did.<BR>During the transition, the habitat emptied from the upper layers downward. The replacement medium then rose from the lowest layer as if the chamber were remembering gravity in reverse.<BR>Status: normal initialization.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-00<BR>{\RESET}首座完整结构在主机放入信标前始终保持沉默. 随后取出信标并不会清除已选择环境; 只有放入另一枚信标才会令模式改变.<BR>切换期间, 栖息地从上层开始向下排空. 新介质随后从最底层向上升起, 仿佛舱室以相反方向回忆重力.<BR>状态: 初始化正常.

            // #tr tc.research_text.ECO_SPHERE_SIMULATOR.3
            // # <LINE>{\BOLD}Operation Record ESS-01<BR>{\RESET}Four stable environmental patterns have been recorded. Their internal processes differ, but every one responds to greater supplied power by compressing more simulated activity into the same operating interval.<BR>One aquatic trial produced a pressure reading at an empty coordinate. The instruments disagreed on whether anything occupied it.<BR>Status: machine stable. Observation retained.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-01<BR>{\RESET}目前已记录四种稳定环境模式. 它们的内部过程各不相同, 但都会在输入功率提高时, 将更多模拟活动压缩进相同的运行间隔.<BR>一次水域试验在空无一物的坐标记录到压力. 仪器无法就那里是否存在某物达成一致.<BR>状态: 机器稳定. 保留观察记录.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_SIMULATOR",
                "TST",
                (new AspectList()).merge(Aspect.TREE, 1)
                    .merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.WATER, 1)
                    .merge(Aspect.PLANT, 1)
                    .merge(Aspect.ELDRITCH, 1)
                    .merge(Aspect.FLESH, 1),
                0,
                -4,
                10,
                GTCMItemList.EcoSphereSimulator.get(1, 0))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_SIMULATOR.1")),
                        new ResearchPage(infusionRecipeEcoSphereSimulator),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_SIMULATOR.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_SIMULATOR.3")))
                    .setParents("TST_WELCOME")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_MODE_BEACON_1
            // # Eco-Sphere Mode Beacon: Arboreal Genesis
            // #zh_CN 生态圈模式信标: 原木拟生

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_1
            // # A forest begins with an instruction no tree can hear.
            // #zh_CN 森林始于一道树木无法听见的指令.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_1.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This beacon turns the Eco-Sphere Simulator into a managed forest. Place a sapling in an input bus, supply water, and use programmed circuits to indicate which parts of the simulated growth should be collected.<BR>Requesting fewer categories causes the habitat to concentrate on what remains.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该信标会将拟似生态圈转变为一片受控林地. 将树苗放入输入总线、提供水, 再用编程电路指出希望从模拟生长中收集的部分.<BR>选择的类别越少, 栖息地就越会集中于剩余目标.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_1.2
            // # {\BOLD}Infusion Recipe (Temporarily Unavailable)<BR>{\RESET}No stable infusion procedure has been recorded for this beacon. The matrix recognizes its pattern, but every attempted arrangement loses coherence before completion.<BR>This page is reserved for a later revision.
            // #zh_CN {\BOLD}注魔配方（暂时无）<BR>{\RESET}目前尚未记录到该信标的稳定注魔流程. 注魔矩阵能够识别它的结构, 但所有材料组合都会在完成前失去一致性.<BR>本页留待后续修订.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_1.3
            // # <LINE>{\BOLD}Operation Record ESS-AG-01<BR>{\RESET}Subject: oak sapling. Medium: water. All collection categories enabled.<BR>The chamber completed a full seasonal model in one cycle. Four signatures corresponding to the tree's structure, renewal, canopy, and maturity were separated without visible damage to the template. Recovery increased with supplied power while the cycle duration remained fixed.<BR>Status: normal.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-01<BR>{\RESET}对象: 橡树树苗. 介质: 水. 全部收集类别已启用.<BR>舱室在一个循环内完成了完整季节模型. 对应树体结构、更新、冠层与成熟阶段的四组特征被分别回收, 模板未出现可见损伤. 回收量随输入功率提高, 循环时间保持不变.<BR>状态: 正常.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_1.4
            // # <LINE>{\BOLD}Operation Record ESS-AG-02<BR>{\RESET}All collection requests except the primary structural signature were removed. Its recovery rose despite unchanged water and power input. Restoring every request returned the distribution to baseline.<BR>No additional growth was detected during either trial. The change suggests that uncollected portions of the same complete tree may have reappeared under the remaining request, but the chamber exposed no transfer process.<BR>Status: result reproducible. Allocation mechanism unconfirmed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-02<BR>{\RESET}除主体结构特征外的全部收集请求均被移除. 在水与功率输入不变的情况下, 该特征的回收量有所提高. 恢复全部请求后, 分布回到基准值.<BR>两次试验中均未检测到额外生长. 变化表明同一完整树体中未被收集的部分可能重新出现在剩余请求下, 但舱室没有暴露任何转移过程.<BR>状态: 结果可复现. 分配机制未确认.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_1.5
            // # <LINE>{\BOLD}Operation Record ESS-AG-03<BR>{\RESET}An unregistered fluid entered the calibration line before circulation began. The tree template responded with growth traces that did not match its species, season, or recorded habitat. Several traces persisted after the fluid was removed, but none could be reproduced with water.<BR>The beacon reset the chamber without identifying the medium or completing a recovery cycle.<BR>Status: unexpected response. Source unclassified.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-03<BR>{\RESET}一种未登记流体在循环开始前进入校准管线. 树木模板随即产生了与其物种、季节及记录环境均不相符的生长痕迹. 流体被移除后仍有数项痕迹残留, 但使用水无法复现.<BR>信标在未能识别介质、也未完成回收循环的情况下重置了舱室.<BR>状态: 预期外响应. 来源未分类.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_MODE_BEACON_1",
                "TST",
                new AspectList(),
                -4,
                -5,
                5,
                GTCMItemList.EcoSphereModeBeacon1.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_1.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_1.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_1.3")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_1.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_1.5")))
                    .setParents("ECO_SPHERE_SIMULATOR")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_MODE_BEACON_2
            // # Eco-Sphere Mode Beacon: Arboreal Genesis
            // #zh_CN 生态圈模式信标: 原木拟生

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_2
            // # The forest no longer asks which season it is.
            // #zh_CN 森林不再询问此刻属于哪个季节.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_2.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This upgraded beacon preserves ordinary forest simulation while accepting several specimens that do not obey a normal season, habitat, or lineage. Their unusual media are recognized automatically when the matching sapling is present.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级信标保留普通林地模拟, 同时能够接纳数种不遵循正常的季节、环境或血统的样本. 当对应树苗存在时, 机器会自动识别其异常介质.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_2.2
            // # {\BOLD}Infusion Recipe (Temporarily Unavailable)<BR>{\RESET}No stable infusion procedure has been recorded for this beacon. The matrix recognizes its pattern, but every attempted arrangement loses coherence before completion.<BR>This page is reserved for a later revision.
            // #zh_CN {\BOLD}注魔配方（暂时无）<BR>{\RESET}目前尚未记录到该信标的稳定注魔流程. 注魔矩阵能够识别它的结构, 但所有材料组合都会在完成前失去一致性.<BR>本页留待后续修订.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_2.3
            // # <LINE>{\BOLD}Operation Record ESS-AG-04<BR>{\RESET}Template: Timewood sapling. Medium: temporal fluid.<BR>The complete seasonal signature was recovered normally, but growth rings formed before the corresponding power pulses appeared in the log. One sample had already weathered for several years when removed from a chamber that had operated for one second.<BR>Status: recipe stable. Chronology disputed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-04<BR>{\RESET}模板: 时光树树苗. 介质: 时间流体.<BR>完整季节特征被正常回收, 但年轮形成时间早于对应供能脉冲的日志记录. 一份样本从仅运行一秒的舱室中取出时, 已经呈现数年风化痕迹.<BR>状态: 配方稳定. 时间顺序存在争议.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_2.4
            // # <LINE>{\BOLD}Operation Record ESS-AG-05<BR>{\RESET}Template: tainted sapling. Medium: death water.<BR>The simulator reproduced every registered trace of the corrupted tree's life cycle without spreading taint outside the chamber. Root activity began while all life-aspect instruments still reported zero.<BR>Status: recipe stable. Biological status unresolved.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-05<BR>{\RESET}模板: 腐化树苗. 介质: 死亡之水.<BR>机器复现了腐化树生命周期内全部已登记痕迹, 且没有让腐化扩散至舱室之外. 根系活动在所有生命源质仪器仍显示为零时便已开始.<BR>状态: 配方稳定. 生物状态未决.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_2.5
            // # <LINE>{\BOLD}Operation Record ESS-AG-06<BR>{\RESET}Template: Barnarda-C sapling. Medium: unknown liquid.<BR>The recovered signatures matched the off-world botanical records. During growth, the branches consistently turned toward coordinates that do not correspond to any visible star from this dimension.<BR>Status: recipe stable. External reference not found.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-06<BR>{\RESET}模板: 巴纳德C树苗. 介质: 不明液体.<BR>回收特征与异星植物记录一致. 生长期间, 枝条持续朝向一组无法对应本维度任何可见恒星的坐标.<BR>状态: 配方稳定. 未找到外部参照物.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_2.6
            // # <LINE>{\BOLD}Operation Record ESS-AG-07<BR>{\RESET}Template: any registered sapling. Medium: UU-matter.<BR>Each selected anatomical category was drawn independently from the complete tree registry. The structural mass, renewal sample, canopy tissue, and mature trace recovered in one cycle did not necessarily belong to the same species.<BR>Status: recipe stable. Parent ecology nonexistent.<BR>Do not describe the resulting collection as a forest within hearing range of the controller.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-07<BR>{\RESET}模板: 任意已登记树苗. 介质: UU物质.<BR>每个已选择的树体类别都会独立从完整登记表中抽取. 同一循环回收的主体、更新样本、冠层组织与成熟痕迹未必属于同一物种.<BR>状态: 配方稳定. 母体生态不存在.<BR>请勿在主机可能听见的范围内将这批回收物称为森林.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_MODE_BEACON_2",
                "TST",
                new AspectList(),
                -4,
                -7,
                5,
                GTCMItemList.EcoSphereModeBeacon2.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_2.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_2.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_2.3")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_2.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_2.5")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_2.6")))
                    .setParents("ECO_SPHERE_MODE_BEACON_1")
                    .setConcealed()
                    .setSpecial()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_MODE_BEACON_3
            // # Eco-Sphere Mode Beacon: Aquatic Simulation
            // #zh_CN 生态圈模式信标: 水域模拟

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_3
            // # A lake needs neither shore nor sky.
            // #zh_CN 湖泊并不需要岸与天空.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_3.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This beacon creates a stable aquatic habitat inside the Eco-Sphere Simulator. Supply clean water and the chamber will reproduce the registered balance of aquatic life and associated plants.<BR>Placing one valid sample in an input bus causes the habitat to instinctively orient itself toward that organism. Leaving the input empty preserves the natural distribution.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该信标会在拟似生态圈内建立稳定水域. 提供洁净水后, 舱室会复现已登记水生生物及伴生植物之间的分布.<BR>将一种有效样本放入输入总线即可令栖息地本能的朝向该生物; 输入为空时则维持自然分布.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_3.2
            // # {\BOLD}Infusion Recipe (Temporarily Unavailable)<BR>{\RESET}No stable infusion procedure has been recorded for this beacon. The matrix recognizes its pattern, but every attempted arrangement loses coherence before completion.<BR>This page is reserved for a later revision.
            // #zh_CN {\BOLD}注魔配方（暂时无）<BR>{\RESET}目前尚未记录到该信标的稳定注魔流程. 注魔矩阵能够识别它的结构, 但所有材料组合都会在完成前失去一致性.<BR>本页留待后续修订.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_3.3
            // # <LINE>{\BOLD}Operation Record ESS-AS-01<BR>{\RESET}Medium: distilled water. Directional sample: none.<BR>The chamber reproduced the registered aquatic distribution. Greater supplied power increased the total recovery without changing the operating interval. Plants associated with the habitat appeared beside aquatic animals without disturbing either population.<BR>Status: normal.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-01<BR>{\RESET}介质: 蒸馏水. 定向样本: 无.<BR>舱室复现了已登记水域分布. 提高输入功率会增加总回收量, 但不会改变运行间隔. 与水域相关的植物会与水生动物同时出现, 两类群落互不干扰.<BR>状态: 正常.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_3.4
            // # <LINE>{\BOLD}Operation Record ESS-AS-02<BR>{\RESET}Directional sample: raw fish.<BR>The selected signature dominated subsequent recoveries. Other aquatic signatures remained detectable but were reduced to background noise. Removing the sample restored the former distribution without cleaning or recalibration.<BR>Status: normal. The sample was not consumed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-02<BR>{\RESET}定向样本: 生鱼.<BR>后续回收明显由目标特征主导. 其他水产特征仍可被检测到, 但已降低至背景噪声. 移除样本后, 无需清理或重新校准即可恢复原有分布.<BR>状态: 正常. 样本未被消耗.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_3.5
            // # <LINE>{\BOLD}Operation Record ESS-AS-03<BR>{\RESET}No unregistered life appeared while total input remained within the maximal conventional boundary. The absence was absolute across every tested arrangement.<BR>At the first stable setting beyond that maximized range, the water developed an invisible pressure and the ordinary population briefly scattered from an empty point in the chamber.<BR>Status: threshold confirmed. Observation window extended.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-03<BR>{\RESET}当总输入仍处于常规系统可表达的最大边界内时, 所有测试组合中均未出现任何未登记生命, 结果完全一致.<BR>在首次稳定越过该上限范围后, 水域出现了不可见的压力, 常规种群短暂避开了舱室内一个空无一物的位置.<BR>状态: 临界值已确认. 观察周期延长.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_3.6
            // # <LINE>{\BOLD}Operation Record ESS-AS-04<BR>{\RESET}After an extended sequence beyond the confirmed boundary, a faint organism appeared where the empty point had been. Its outline resembled a jellyfish, yet its aspect response alternated between familiar biological readings and one that no instrument could name.<BR>The specimen was not present in the registered table and could not be selected as a directional target. Returning it to the habitat produced no immediate change.<BR>Status: isolated. Report forwarded under sealed classification.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-04<BR>{\RESET}在越过已确认边界并持续运行很久后, 原本空无一物的位置出现了一个微弱生物. 其轮廓近似水母, 但源质响应在数种熟悉的生命读数与一种所有仪器都无法命名的读数之间交替.<BR>该样本不在已登记水产表内, 也无法被选为定向目标. 将其重新投入水域后未立即产生变化.<BR>状态: 已隔离. 报告按封存等级上交.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_3.7
            // # <LINE>{\BOLD}Operation Record ESS-AS-05<BR>{\RESET}An unregistered fluid was detected in the aquatic circulation line. Before the controller rejected it, the chamber registered several overlapping photosynthetic and nutrient signatures in water that appeared visually empty.<BR>The readings vanished when distilled water was restored. No valid recovery table was produced.<BR>Status: unexpected response. Medium unclassified.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-05<BR>{\RESET}水域循环管线中检测到一种未登记流体. 在主机将其拒绝前, 舱室从看似空无一物的水域中记录到数项彼此重叠的光合与营养特征.<BR>恢复蒸馏水后读数消失, 且没有形成有效回收表.<BR>状态: 预期外响应. 介质未分类.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_MODE_BEACON_3",
                "TST",
                new AspectList(),
                2,
                -6,
                5,
                GTCMItemList.EcoSphereModeBeacon3.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_3.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_3.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_3.3")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_3.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_3.5")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_3.6")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_3.7")))
                    .setParents("ECO_SPHERE_SIMULATOR")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_MODE_BEACON_4
            // # Eco-Sphere Mode Beacon: Aquatic Simulation
            // #zh_CN 生态圈模式信标: 水域模拟

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_4
            // # The water has begun cultivating its own answer.
            // #zh_CN 水正在培育属于自己的答案.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_4.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This upgraded beacon allows the aquatic habitat to accept unfamiliar media and cultivate life patterns recorded beyond the local environment. Directional samples remain valid, but the resulting habitat is less predictable than ordinary water.<BR>Several compatible signatures may appear together, and some cycles leave nothing that can be recovered.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级信标允许水域接受陌生介质, 并培育记录于本地环境之外的生命特征. 定向样本仍然有效, 但这种栖息地比普通水域更难预测.<BR>数种相容特征可能同时出现, 也有部分循环不会留下任何可回收内容.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_4.2
            // # {\BOLD}Infusion Recipe (Temporarily Unavailable)<BR>{\RESET}No stable infusion procedure has been recorded for this beacon. The matrix recognizes its pattern, but every attempted arrangement loses coherence before completion.<BR>This page is reserved for a later revision.
            // #zh_CN {\BOLD}注魔配方（暂时无）<BR>{\RESET}目前尚未记录到该信标的稳定注魔流程. 注魔矩阵能够识别它的结构, 但所有材料组合都会在完成前失去一致性.<BR>本页留待后续修订.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_4.3
            // # <LINE>{\BOLD}Operation Record ESS-AS-05<BR>{\RESET}Medium: unknown liquid. Directional sample: none.<BR>Every registered off-world photosynthetic strain and nutrient-matrix class was confirmed. Multiple incompatible strains occupied the same volume without visible competition. Altering the usable chamber space did not produce a consistent change in their relative recovery or total measured mass.<BR>Status: result stable. Population limit undetermined.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-05<BR>{\RESET}介质: 不明液体. 定向样本: 无.<BR>所有已登记异星光合株系与营养基质类别均得到确认. 多种互不相容的株系占据同一片空间却没有出现可见竞争. 改变舱室可用空间后, 各特征的相对回收量与测得总质量均未呈现一致变化.<BR>状态: 结果稳定. 种群上限未明.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_4.4
            // # <LINE>{\BOLD}Operation Record ESS-AS-06<BR>{\RESET}Every registered signature completed a directional trial. The selected organism became dominant, but secondary growth continued at trace levels. Some cycles returned several species together; others returned none.<BR>The empty cycles still consumed the full medium and registered complete biological activity.<BR>Status: expected. Recovery is not guaranteed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-06<BR>{\RESET}所有已登记特征均完成定向测试. 被选择的生物成为主要回收目标, 但其他生长仍以微量形式持续. 部分循环会同时回收数种生物, 也有部分循环没有任何内容.<BR>空循环仍会消耗完整介质, 且记录到完整生物活动.<BR>状态: 符合预期. 不保证回收内容.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_4.5
            // # <LINE>{\BOLD}Operation Record ESS-AS-07<BR>{\RESET}One off-world photosynthetic sample was sealed without light, nutrients, or medium. Its measured biological mass continued to increase while its visible area diminished. When returned to unknown liquid, the registered matrix signatures appeared around it in the same order as the internal ledger.<BR>The specimen's internal structure contained no cells corresponding to those recoveries.<BR>Status: containment maintained.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-07<BR>{\RESET}一份异星光合样本在无光、无营养、无介质环境中密封. 测得的生物质量仍在增加, 可见面积却持续缩小. 将其放回不明液体后, 已登记基质特征按照内部记录的顺序出现在周围.<BR>样本内部没有任何可对应这些回收物的细胞结构.<BR>状态: 维持收容.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_4.6
            // # <LINE>{\BOLD}Operation Record ESS-AS-08<BR>{\RESET}Personnel are forbidden from drinking the medium. The rule remains necessary despite the absence of volunteers admitting the act.<BR>After the notice was posted, the chamber produced an empty cycle and printed the observer roster as its output table.<BR>Status: log access restricted.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-08<BR>{\RESET}禁止人员饮用该介质. 尽管无人承认进行过此行为, 本条仍有保留必要.<BR>告示张贴后, 舱室完成了一次空循环, 并将观察人员名单打印为产物表.<BR>状态: 日志访问受限.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_MODE_BEACON_4",
                "TST",
                new AspectList(),
                2,
                -8,
                5,
                GTCMItemList.EcoSphereModeBeacon4.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_4.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_4.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_4.3")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_4.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_4.5")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_4.6")))
                    .setParents("ECO_SPHERE_MODE_BEACON_3")
                    .setConcealed()
                    .setSpecial()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_MODE_BEACON_5
            // # Eco-Sphere Mode Beacon: Artificial Greenhouse
            // #zh_CN 生态圈模式信标: 人工温室

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_5
            // # Growth, disciplined by glass and calculation.
            // #zh_CN 生长被玻璃与计算驯服.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_5.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This beacon turns the Eco-Sphere Simulator into a controlled greenhouse. Place an ordinary seed or another registered cultivation sample in an input bus and supply enriched fertilizer.<BR>The chamber follows the plant's known harvest behavior. Samples outside the cultivation registry are not accepted.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该信标会将拟似生态圈转变为受控温室. 将普通种子或其他已登记培育样本放入输入总线, 并提供富集肥料.<BR>舱室会遵循植物已知的收获表现. 培育登记表之外的样本不会被接受.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_5.2
            // # {\BOLD}Infusion Recipe (Temporarily Unavailable)<BR>{\RESET}No stable infusion procedure has been recorded for this beacon. The matrix recognizes its pattern, but every attempted arrangement loses coherence before completion.<BR>This page is reserved for a later revision.
            // #zh_CN {\BOLD}注魔配方（暂时无）<BR>{\RESET}目前尚未记录到该信标的稳定注魔流程. 注魔矩阵能够识别它的结构, 但所有材料组合都会在完成前失去一致性.<BR>本页留待后续修订.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_5.3
            // # <LINE>{\BOLD}Operation Record ESS-AGH-01<BR>{\RESET}Template: wheat seeds. Medium: enriched fertilizer.<BR>The simulator reconstructed repeated harvests from the ordinary plant model. Reproductive and mature signatures followed the observed behavior rather than a fixed recipe. Raising power increased the expected recovery without changing the operating interval.<BR>Status: normal.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-01<BR>{\RESET}模板: 小麦种子. 介质: 富集肥料.<BR>机器根据普通植物模型重构连续收获. 繁殖阶段与成熟阶段的特征遵循观测行为, 而非固定配方. 提高功率会增加期望回收量, 但不会改变运行间隔.<BR>状态: 正常.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_5.4
            // # <LINE>{\BOLD}Operation Record ESS-AGH-02<BR>{\RESET}A registered alternate seed with no ordinary field block was accepted. Its complete cultivation profile was reproduced, including lower-frequency secondary signatures.<BR>The chamber contained no visible crop during the cycle. Harvest counters advanced normally.<BR>Status: normal. Physical growth stage not required.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-02<BR>{\RESET}一枚没有普通田间方块的已登记替代种子被机器接受. 其完整培育档案得到复现, 包括记录频率较低的次级特征.<BR>循环期间舱室内没有出现可见作物, 收获计数仍正常推进.<BR>状态: 正常. 不要求物理生长阶段.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_5.5
            // # <LINE>{\BOLD}Operation Record ESS-AGH-03<BR>{\RESET}An unregistered seed record entered calibration. The beacon rejected it, yet the chamber briefly projected several overlapping cultivation plots with mutually inconsistent development traces.<BR>One empty plot remained after the sample was removed and vanished only when power was cut. No harvest was recovered.<BR>Status: unexpected response. Sample classification incomplete.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-03<BR>{\RESET}一份未登记种子记录进入校准流程. 信标拒绝了它, 但舱室仍短暂投影出数块彼此重叠、发育痕迹互相矛盾的培养区.<BR>样本被移除后仍有一块空培养区残留, 直到切断电源才消失. 没有回收到任何收获物.<BR>状态: 预期外响应. 样本分类未完成.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_MODE_BEACON_5",
                "TST",
                new AspectList(),
                -2,
                -6,
                5,
                GTCMItemList.EcoSphereModeBeacon5.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_5.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_5.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_5.3")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_5.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_5.5")))
                    .setParents("ECO_SPHERE_SIMULATOR")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_MODE_BEACON_6
            // # Eco-Sphere Mode Beacon: Artificial Greenhouse
            // #zh_CN 生态圈模式信标: 人工温室

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_6
            // # The seed remembers more than its species.
            // #zh_CN 种子记得的远比物种更多.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_6.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This upgraded beacon allows the greenhouse to understand analyzed hybrid seeds. Their recorded growth, gain, and resistance become part of the simulated cultivation profile rather than being discarded as irregular data.<BR>Ordinary seeds remain compatible.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级信标允许温室理解已分析的杂交种子. 其记录的生长、产量与抗性会成为模拟培育档案的一部分, 而不再被当作异常数据忽略.<BR>普通种子仍然兼容.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_6.2
            // # {\BOLD}Infusion Recipe (Temporarily Unavailable)<BR>{\RESET}No stable infusion procedure has been recorded for this beacon. The matrix recognizes its pattern, but every attempted arrangement loses coherence before completion.<BR>This page is reserved for a later revision.
            // #zh_CN {\BOLD}注魔配方（暂时无）<BR>{\RESET}目前尚未记录到该信标的稳定注魔流程. 注魔矩阵能够识别它的结构, 但所有材料组合都会在完成前失去一致性.<BR>本页留待后续修订.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_6.3
            // # <LINE>{\BOLD}Operation Record ESS-AGH-04<BR>{\RESET}Two analyzed seeds of the same crop were tested with different growth values. The faster profile completed a larger fraction of its harvest cycle and produced proportionally more output. Neither profile changed the fixed machine cycle time.<BR>Status: normal. Growth affects simulated progress, not controller speed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-04<BR>{\RESET}使用两枚同种作物但生长数值不同的已分析种子进行测试. 较快的档案在单次中完成了更大比例的收获周期, 产出随之提高. 两种档案都没有改变机器固定循环时间.<BR>状态: 正常. 生长影响模拟进度, 不影响主机速度.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_6.4
            // # <LINE>{\BOLD}Operation Record ESS-AGH-05<BR>{\RESET}Gain was increased while crop and growth remained unchanged. Average harvest rounds rose, and individual recovered stacks occasionally gained additional items. Rare products retained their registered relation to common products.<BR>Status: normal. Gain alters quantity, not identity.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-05<BR>{\RESET}在作物与生长保持不变时提高产量数值. 平均收获轮数上升, 单次回收堆叠也会额外增加物品. 稀有产物与常见产物之间仍保持原登记比例.<BR>状态: 正常. 产量改变数量, 不改变身份.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_6.5
            // # <LINE>{\BOLD}Operation Record ESS-AGH-06<BR>{\RESET}Resistance was varied across otherwise identical hybrid profiles. Recovered quantity and composition remained unchanged within measurement error, but high-resistance specimens left fewer unstable growth echoes in the chamber after completion.<BR>One observer's notes changed despite no recorded contact with either specimen.<BR>Status: harvest unchanged. Residual phenomenon under review.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-06<BR>{\RESET}对其他参数完全相同的杂交档案调整抗性. 回收数量与构成在测量误差内保持不变, 但高抗性样本在循环结束后留下的异常生长回声更少.<BR>一名观察员的笔记发生了变化, 尽管记录中其未与任一样本接触.<BR>状态: 收获结果未变. 残留现象审查中.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_MODE_BEACON_6",
                "TST",
                new AspectList(),
                -2,
                -8,
                5,
                GTCMItemList.EcoSphereModeBeacon6.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_6.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_6.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_6.3")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_6.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_6.5")))
                    .setParents("ECO_SPHERE_MODE_BEACON_5")
                    .setConcealed()
                    .setSpecial()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_MODE_BEACON_7
            // # Eco-Sphere Mode Beacon: Directed Mob Cloning
            // #zh_CN 生态圈模式信标: 定向克隆

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_7
            // # Life reduced to an address.
            // #zh_CN 生命被简化为一个地址.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_7.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This beacon prepares the Eco-Sphere Simulator for directed cloning. Programmed circuits provide a biological address, while a suitable living medium must supply the continuity needed to form the requested specimen.<BR>The correct medium and containment conditions were not included in the recovered design. Early operation therefore consisted of repeated reconstruction attempts, failed manifestations, and one unexpected liquid byproduct that later became the key to further work.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该信标会令拟似生态圈进入定向克隆状态. 编程电路用于提供生物地址, 合适的生命介质则必须为指定样本的形成提供连续性.<BR>回收的设计中没有记录正确介质与收容条件. 因此早期运行由反复重构、显现失败, 以及一种后来成为继续研究关键的意外液体产物构成.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_7.2
            // # {\BOLD}Infusion Recipe (Temporarily Unavailable)<BR>{\RESET}No stable infusion procedure has been recorded for this beacon. The matrix recognizes its pattern, but every attempted arrangement loses coherence before completion.<BR>This page is reserved for a later revision.
            // #zh_CN {\BOLD}注魔配方（暂时无）<BR>{\RESET}目前尚未记录到该信标的稳定注魔流程. 注魔矩阵能够识别它的结构, 但所有材料组合都会在完成前失去一致性.<BR>本页留待后续修订.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_7.3
            // # <LINE>{\BOLD}Operation Record ESS-DMC-01<BR>{\RESET}Medium: blood. Programmed circuits: none.<BR>The chamber attempted to build a specimen without an address. The blood repeatedly gathered into unfinished tissue, folded inward, and dissolved into an equal volume of an unfamiliar red fluid. No recognizable anatomy or recoverable material remained.<BR>Status: reconstruction failed. Byproduct retained for analysis.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-01<BR>{\RESET}介质: 血液. 编程电路: 无.<BR>舱室尝试在没有地址的情况下构筑样本. 血液反复聚集成未完成的组织, 随后向内扭曲并溶解为等量的陌生红色液体. 没有留下可识别的解剖结构或可回收物质.<BR>状态: 重构失败. 意外产物已留存分析.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_7.4
            // # <LINE>{\BOLD}Operation Record ESS-DMC-02<BR>{\RESET}Several ordinary biological addresses were tested with blood. Each produced a different arrangement of unfinished organs, confirming that the controller understood the circuit requests.<BR>None survived long enough to become a specimen. Every trial collapsed into the same unfamiliar fluid recovered from the addressless test.<BR>Status: address recognition confirmed. Blood unsuitable for manifestation.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-02<BR>{\RESET}使用血液测试了数个普通生物地址. 每个地址都形成了不同排列的未完成器官, 证明主机能够理解电路请求.<BR>没有任何结构维持到样本形成. 每次试验最终都坍缩为无地址测试中回收的同一种陌生液体.<BR>状态: 地址识别已确认. 血液不适合用于显现.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_7.5
            // # <LINE>{\BOLD}Operation Record ESS-DMC-03<BR>{\RESET}The byproduct was submitted to the department listed in the archive only as Blood Magic. At the time, no member of this project could explain the department's function or why its personnel recognized the sample before opening the container.<BR>Their report named the fluid Life Essence and returned the sample with handling instructions, a request for circuit data, and no explanation of how the identification was made.<BR>Status: external classification accepted provisionally. Joint trial approved.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-03<BR>{\RESET}意外产物被送往档案中仅标记为“血魔法”的部门. 当时本项目无人能够说明该部门的职能, 也无人能解释其人员为何在开启容器前便认出了样本.<BR>对方报告将该液体称为生命本源, 并随样本附回处理规范与电路数据请求, 但没有说明识别过程.<BR>状态: 暂时接受外部分类. 联合试验已批准.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_7.6
            // # <LINE>{\BOLD}Operation Record ESS-DMC-04<BR>{\RESET}Medium: Life Essence. Address: ordinary registered organism.<BR>The chamber no longer produced twisted tissue. A complete reconstruction outline formed and remained stable far longer than any blood-fed attempt, but dispersed before manifestation. The medium was left untouched and the controller retained the address after shutdown.<BR>Status: medium requirement confirmed. Containment incomplete.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-04<BR>{\RESET}介质: 生命本源. 地址: 普通已登记生物.<BR>舱室不再产生扭曲组织. 完整重构轮廓已经形成, 且维持时间远超任何血液试验, 但仍在显现前消散. 介质没有被消耗, 主机在停机后保留了该地址.<BR>状态: 介质需求已确认. 收容条件不完整.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_7.7
            // # <LINE>{\BOLD}Operation Record ESS-DMC-05<BR>{\RESET}The Blood Magic team repeated the Life Essence trial while structure staff monitored the chamber boundary. Several ordinary addresses reached the same complete outline and failed at the same point. Increasing power changed neither the failure stage nor the untouched medium.<BR>The external team concluded only that the fluid was correct. Structural staff found unused containment instructions embedded in the reinforced habitat plans.<BR>Status: ordinary reconstruction unresolved. Second-tier review opened.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-05<BR>{\RESET}血魔法团队重复了生命本源试验, 同时由结构人员监测舱室边界. 数个普通地址均形成了同样完整的轮廓, 并在同一阶段失败. 提高功率既没有改变失败阶段, 也没有触及输入介质.<BR>外部团队只能确认流体选择正确. 结构人员则在强化环境图纸中发现了尚未采用的收容指令.<BR>状态: 普通重构仍未解决. 二级结构审查已启动.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_MODE_BEACON_7",
                "TST",
                new AspectList(),
                4,
                -5,
                5,
                GTCMItemList.EcoSphereModeBeacon7.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_7.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_7.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_7.3")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_7.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_7.5")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_7.6")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_7.7")))
                    .setParents("ECO_SPHERE_SIMULATOR")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_MODE_BEACON_8
            // # Eco-Sphere Mode Beacon: Directed Mob Cloning
            // #zh_CN 生态圈模式信标: 定向克隆

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_8
            // # The specimen no longer accepts the word impossible.
            // #zh_CN 样本不再接受“不可能”这个词.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_8.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This upgraded beacon extends directed cloning to biological addresses normally rejected as too dominant or destructive. It also permits a second acceleration state in which the chamber performs more work than the measured supply should support, without damaging the cached pattern.<BR>Ordinary targets remain available. The origin of the unaccounted operating margin has not been identified.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级信标会将定向克隆扩展到通常因过于强势或危险而被拒绝的生物地址. 它还允许舱室进入第二种加速状态: 在不损伤缓存模式的情况下, 完成超出实测输入所能支持的工作.<BR>普通目标仍然可用. 尚未查明这部分额外运行余量来自何处.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_8.2
            // # {\BOLD}Infusion Recipe (Temporarily Unavailable)<BR>{\RESET}No stable infusion procedure has been recorded for this beacon. The matrix recognizes its pattern, but every attempted arrangement loses coherence before completion.<BR>This page is reserved for a later revision.
            // #zh_CN {\BOLD}注魔配方（暂时无）<BR>{\RESET}目前尚未记录到该信标的稳定注魔流程. 注魔矩阵能够识别它的结构, 但所有材料组合都会在完成前失去一致性.<BR>本页留待后续修订.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_8.3
            // # <LINE>{\BOLD}Operation Record ESS-DMC-06<BR>{\RESET}The same ordinary target was processed with both beacon grades under an unchanged external supply. The upgraded trial completed several additional acceleration transitions. Life Essence use and recovery rose together, yet the input meters recorded no corresponding increase before or during the cycle.<BR>Cached recovery proportions and integrity expectations remained unchanged. Repeated inspection found no hidden conductor, reserve cell, or delayed discharge.<BR>Status: result reproducible. Energy balance unresolved.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-06<BR>{\RESET}在外部供能不变的条件下, 使用两种等级信标处理同一普通目标. 升级试验额外完成了数次加速跃迁. 生命本源消耗与回收量同步提高, 但输入仪表在循环前后均未记录到对应增长.<BR>缓存回收比例与完整度期望保持不变. 重复检查未发现隐藏导体、储能单元或延迟放电.<BR>状态: 结果可复现. 能量收支无法解释.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_8.4
            // # <LINE>{\BOLD}Operation Record ESS-DMC-07<BR>{\RESET}Boss-grade trial approved. The target never entered the chamber; instead, the chamber briefly adopted every condition required for the target to have always been present. The recovered signatures matched the registered aftermath profile.<BR>No damage to the structure was recorded. Three observers reported memories of a fight that did not occur.<BR>Status: recipe stable. Witness statements sealed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-07<BR>{\RESET}首领级试验获准执行. 目标从未进入舱室; 相反, 舱室短暂形成了“目标一直存在于此”所需的全部条件. 回收特征与已登记的事后档案一致.<BR>结构没有记录到任何损伤. 三名观察员报告了并未发生过的战斗记忆.<BR>状态: 配方稳定. 证词已封存.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_8.5
            // # <LINE>{\BOLD}Operation Record ESS-DMC-08<BR>{\RESET}At high overclock count, the cached pattern began listing the observation staff as possible drops. The entries carried names, enchantments, and durability values despite the staff remaining outside the chamber.<BR>The next cycle removed the names and enchantments exactly as prescribed by the cache sanitation procedure.<BR>Status: machine behavior compliant. Source data unacceptable.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-08<BR>{\RESET}在较高超频次数下, 缓存模式开始将观察人员列为可能掉落物. 尽管人员始终位于舱室外, 这些条目仍带有名称、附魔与耐久数值.<BR>下一循环按照缓存清理流程准确移除了名称与附魔.<BR>状态: 机器行为符合规范. 数据来源不可接受.

            // #tr tc.research_text.ECO_SPHERE_MODE_BEACON_8.6
            // # <LINE>{\BOLD}Operation Record ESS-DMC-09<BR>{\RESET}The log was sealed after the previous event. During the following cycle, the chamber produced no item output and no entity signature.<BR>The sealed record opened from the inside.<BR>Status: authorization revoked pending review.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-09<BR>{\RESET}上次事件后日志已封存. 随后的循环中, 舱室没有产出物品, 也没有记录到实体特征.<BR>封存记录从内部被打开.<BR>状态: 授权已撤销, 等待审查.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_MODE_BEACON_8",
                "TST",
                new AspectList(),
                4,
                -10,
                5,
                GTCMItemList.EcoSphereModeBeacon8.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_8.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_8.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_8.3")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_8.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_8.5")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_MODE_BEACON_8.6")))
                    .setParents("ECO_SPHERE_MODE_BEACON_7", "ECO_SPHERE_TIER_TWO")
                    .setConcealed()
                    .setSpecial()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL
            // # Eco-Sphere Simulator: Directed Cloning Protocol
            // #zh_CN 拟似生态圈: 定向克隆协议

            // #tr tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL
            // # A biological address becomes executable only after the habitat learns how to contain its consequences.
            // #zh_CN 只有当环境学会容纳其后果, 生物地址才会成为可执行内容.

            // #tr tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.1
            // # {\BOLD}Second-Tier Cloning Protocol<BR>{\RESET}The reinforced Eco-Sphere structure completes the containment sequence first observed during the joint Life Essence trials. Valid circuit totals act as biological addresses, while Life Essence preserves the requested pattern long enough for the chamber to reconstruct its registered aftermath profile.<BR>Invalid addresses still fall back to the failed blood reconstruction that produces Life Essence instead of a specimen.
            // #zh_CN {\BOLD}二级克隆协议<BR>{\RESET}强化后的拟似生态圈结构补全了生命本源联合试验中首次观察到的收容序列. 有效电路总和作为生物地址, 生命本源则使请求模式维持到舱室能够重构其已登记事后档案.<BR>无效地址仍会退回失败的血液重构, 不形成样本而是产生生命本源.

            // #tr tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.2
            // # <LINE>{\BOLD}Operation Record ESS-DMC-T2-01<BR>{\RESET}A valid ordinary address was submitted after the reinforced habitat completed calibration. No organism entered the chamber. Instead, the fluid surface briefly formed the outline of an absent anatomy, followed by the recovery traces expected from its recorded aftermath.<BR>The cache displayed the first valid trace for identification, while actual recovery followed the complete registered table.<BR>Status: protocol stable.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-T2-01<BR>{\RESET}强化环境完成校准后, 输入了一个有效普通地址. 没有生物进入舱室; 液面只是短暂形成一副缺席躯体的轮廓, 随后出现与其登记事后档案一致的回收痕迹.<BR>缓存会显示第一项有效痕迹用于识别, 实际回收则遵循完整登记表.<BR>状态: 协议稳定.

            // #tr tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.3
            // # <LINE>{\BOLD}Operation Record ESS-DMC-T2-02<BR>{\RESET}Recovered equipment traces were sanitized before material evaluation. Names, enchantments, and ownership marks were removed; recognized equipment collapsed into material-equivalent signatures, while unreadable constructions remained unchanged.<BR>Remaining integrity influenced the recorded recovery value.<BR>Status: sanitation table consistent.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-T2-02<BR>{\RESET}回收的装备痕迹会在材料评估前经过清理. 名称、附魔与归属标记会被移除; 已识别装备坍缩为等效材料特征, 无法读取的构造则保持原状.<BR>剩余完整度会影响记录的回收价值.<BR>状态: 清理表一致.

            // #tr tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.4
            // # <LINE>{\BOLD}Operation Record ESS-DMC-T2-03<BR>{\RESET}Increasing the supplied power raised both biological medium use and recovery together while preserving the proportions stored in the registered table. Targets with shorter original processing histories produced denser traces.<BR>No living specimen appeared in the chamber.<BR>Status: scaling behavior stable. Ethical review not requested.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-T2-03<BR>{\RESET}提高输入功率会令生物介质消耗与回收量同步增加, 登记表内的比例保持不变. 原处理历程较短的目标会形成更密集的痕迹.<BR>舱室内没有出现活体样本.<BR>状态: 增益行为稳定. 未申请伦理审查.

            // #tr tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.5
            // # <LINE>{\BOLD}Operation Record ESS-DMC-T2-04<BR>{\RESET}After ordinary addresses completed successfully, a restricted address was submitted under the same structural and medium conditions. The chamber completed the containment model, then rejected the final authorization layer without disclosing the target.<BR>The request persisted after shutdown, marked only as exceeding the basic beacon's authority.<BR>Status: structure and medium confirmed. Higher authorization required.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-T2-04<BR>{\RESET}普通地址成功执行后, 在相同结构与介质条件下输入了一个受限地址. 舱室完成了收容模型, 随后拒绝最终授权层, 且没有透露目标内容.<BR>请求在停机后仍然保留, 唯一标记是超出基础信标权限.<BR>状态: 结构与介质已确认. 需要更高授权.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_DIRECTED_CLONING_PROTOCOL",
                "TST",
                new AspectList(),
                4,
                -7,
                5,
                new ItemStack(EnderIO.itemSoulVessel))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.3")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_DIRECTED_CLONING_PROTOCOL.5")))
                    .setParents("ECO_SPHERE_MODE_BEACON_7", "ECO_SPHERE_TIER_TWO")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.OFFSPRING
            // # {\DARK_AQUA}"Offspring"
            // #zh_CN {\DARK_AQUA}"子代"

            // #tr tc.research_text.OFFSPRING
            // # A specimen absent from every natural lineage.
            // #zh_CN 一个不存在于任何自然谱系中的样本.

            // #tr tc.research_text.OFFSPRING.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This small translucent organism can occasionally be recovered from the aquatic habitat. It resembles a jellyfish, but no known taxonomy accepts it.<BR>Preserve the specimen and examine it carefully. Treating it as an ordinary catch would discard the only useful evidence of where it came from.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}这种半透明的小型生物偶尔会从水域环境中被回收. 它看似水母, 却不被任何已知分类所承认.<BR>请保存样本并仔细观察. 若将其当作普通水产处理, 便会丢失追查其来源的唯一有效证据.

            // #tr tc.research_text.OFFSPRING.2
            // # <LINE>{\BOLD}Observation Record ESS-OFF-01<BR>{\RESET}The specimen appeared only after the supplied power crossed the greatest boundary recognized by conventional machinery. Even then, most cycles returned nothing unusual.<BR>Attempts to select it as a directional target produced no response. It can be encountered, but it cannot be requested.<BR>Status: recovery condition confirmed. Probability indeterminate.
            // #zh_CN <LINE>{\BOLD}观察记录 ESS-OFF-01<BR>{\RESET}只有当输入功率越过常规机械所能识别的最大边界后, 该样本才曾出现. 即便如此, 绝大多数循环仍未发现异常.<BR>尝试将其设为定向目标时没有得到任何响应. 它可以被遇见, 却无法被索取.<BR>状态: 已确认回收条件. 概率无法确定.

            // #tr tc.research_text.OFFSPRING.3
            // # <LINE>{\BOLD}Observation Record ESS-OFF-02<BR>{\RESET}The specimen remained alive without feeding and showed no measurable growth. Its internal pattern alternated between an undeveloped organism and a complete archive of unrelated life.<BR>When the container was moved away from the simulator, the aquatic chamber continued reporting one additional occupant until the next initialization.<BR>Status: specimen contained. Occupant count disputed.
            // #zh_CN <LINE>{\BOLD}观察记录 ESS-OFF-02<BR>{\RESET}样本在未进食的情况下保持存活, 且没有可测量的生长. 其内部模式在未发育生物与无关生命的完整档案之间反复变化.<BR>容器被移离机器后, 水域舱室仍持续报告多出一个个体, 直至下一次初始化.<BR>状态: 样本已收容. 个体计数存在争议.
            // spotless:on
            new ResearchItem(
                "OFFSPRING",
                "TST",
                (new AspectList()).merge(Aspect.WATER, 1)
                    .merge(Aspect.EXCHANGE, 1)
                    .merge(Aspect.LIFE, 1),
                0,
                -7,
                10,
                GTCMItemList.OffSpring.get(1, 0))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.OFFSPRING.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.OFFSPRING.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.OFFSPRING.3")))
                    .setHidden()
                    .setParents("ECO_SPHERE_MODE_BEACON_3")
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.FONT_OF_ECOLOGY
            // # {\BLUE}{\BOLD}Font of Ecology
            // #zh_CN {\BLUE}{\BOLD}生态泉源

            // #tr tc.research_text.FONT_OF_ECOLOGY
            // # A source beneath inheritance.
            // #zh_CN 藏在遗传之下的源头.

            // #tr tc.research_text.FONT_OF_ECOLOGY.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}The strange aquatic specimen contains more than the pattern of a single organism. Its structure can be condensed into a stable thaumaturgical source that helps the Eco-Sphere sustain environments beyond ordinary imitation.<BR>The upgraded Eco-Sphere does not create life by itself. It provides the habitat with a reference for what life is allowed to become.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}那份诡异的水域样本所包含的并不只是单一生物的模式. 其结构可以被凝聚为稳定的神秘学源头, 帮助拟似生态圈维持超出普通模仿范围的环境.<BR>升级后的生态圈不会自行创造生命. 它只是为栖息地提供一份生命被允许成为何物的参照.

            // #tr tc.research_text.FONT_OF_ECOLOGY.2
            // # <LINE>{\BOLD}Analysis Record ESS-FE-01<BR>{\RESET}The first stable assembly reacted to the Offspring before the specimen entered the apparatus. Nearby life-aspect instruments aligned into a repeating sequence, while the Font answered with a pattern absent from the specimen itself.<BR>When placed beside the Eco-Sphere plans, several previously decorative structural notes became readable as containment instructions.<BR>Status: source stable. Relationship unresolved.
            // #zh_CN <LINE>{\BOLD}分析记录 ESS-FE-01<BR>{\RESET}首个稳定成品在子代样本进入仪器前便对其产生反应. 附近的生命源质仪器排列出重复序列, 泉源则回应了一段样本自身并不具备的模式.<BR>将其置于拟似生态圈图纸旁后, 数条原本被视为装饰的结构注记显现为收容指令.<BR>状态: 源头稳定. 关系未决.

            // #tr tc.research_text.FONT_OF_ECOLOGY.3
            // # <LINE>{\BOLD}Analysis Record ESS-FE-02<BR>{\RESET}Repeated comparison found no parent signature, no origin habitat, and no point at which the specimen's pattern could have entered the natural record. The Font nevertheless recognizes every tested lineage as familiar.<BR>The final sequence indicates that the specimen is not a descendant, but
            // #zh_CN <LINE>{\BOLD}分析记录 ESS-FE-02<BR>{\RESET}反复比对未找到亲本特征、起源环境, 也未找到该样本模式进入自然记录的时间点. 然而, 泉源却将每一条受测谱系都识别为熟悉对象.<BR>最后一段序列说明该样本并非后代, 而是
            // spotless:on
            new ResearchItem(
                "FONT_OF_ECOLOGY",
                "TST",
                (new AspectList()).add(EVOLUTION, 1)
                    .add(Aspect.ENTROPY, 1)
                    .add(Aspect.ELDRITCH, 1)
                    .add(Aspect.LIFE, 1)
                    .add(EVOLUTION, 1)
                    .add(Aspect.ORDER, 1)
                    .add(Aspect.WATER, 1)
                    .add(Aspect.EXCHANGE, 1),
                0,
                -9,
                10,
                GTCMItemList.FountOfEcology.get(1, 0))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.FONT_OF_ECOLOGY.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.FONT_OF_ECOLOGY.2")),
                        new ResearchPage(TextEnums.tr("tc.research_text.FONT_OF_ECOLOGY.3")),
                        new ResearchPage(infusionRecipeFontOfEcology))
                    .setParents("OFFSPRING")
                    .setSiblings("ECO_SPHERE_TIER_TWO")
                    .setHidden()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_TIER_TWO
            // # Eco-Sphere Simulator: Tier II Structure
            // #zh_CN 拟似生态圈: 二级结构

            // #tr tc.research_text.ECO_SPHERE_TIER_TWO
            // # The habitat no longer imitates nature. It negotiates with it.
            // #zh_CN 这片环境不再模仿自然, 而是在与自然交涉.

            // #tr tc.research_text.ECO_SPHERE_TIER_TWO.1
            // # {\BOLD}Second-Tier Structure<BR>{\RESET}The Font of Ecology reveals how to rebuild the original habitat with cleaner and denser components. The reinforced chamber can sustain advanced mode beacons, keep hostile simulations inside their assigned boundaries, and perform ordinary processes while consuming only a small fraction of their former medium.<BR>This structure does not merely reproduce an ecosystem. It preserves the conditions under which an impossible ecosystem can remain coherent.
            // #zh_CN {\BOLD}二级结构<BR>{\RESET}生态泉源揭示了以更洁净、更致密的部件重建原有栖息地的方法. 强化后的舱室能够承载升级模式信标、阻止危险模拟越过为其划定的边界, 并让普通流程只消耗原先很小一部分介质.<BR>这套结构不再只是复现生态. 它维持着一片不可能生态得以保持完整的条件.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_TIER_TWO",
                "TST",
                new AspectList(),
                2,
                -10,
                5,
                GTCMItemList.AsepticGreenhouseCasing.get(1))
                    .setPages(new ResearchPage(TextEnums.tr("tc.research_text.ECO_SPHERE_TIER_TWO.1")))
                    .setParents("FONT_OF_ECOLOGY")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.EVOLUTIO
            // # Evolutio
            // #zh_CN Evolutio

            // #tr tc.research_text.EVOLUTIO
            // # Change given thaumic form.
            // #zh_CN 被赋予神秘学形态的改变.

            // #tr tc.research_text.EVOLUTIO.1
            // # {\BOLD}Discovery<BR>{\RESET}The pattern extracted from the Offspring is neither Life nor Exchange, though it touches both. It describes the transition by which a living system becomes capable of answering a condition that did not exist when it began.<BR>This aspect has been named Evolutio: not growth itself, but the permission to become otherwise.
            // #zh_CN {\BOLD}发现<BR>{\RESET}从子代中提取出的模式既不是生命, 也不是交换, 尽管它同时触及二者. 它描述的是一种转变: 生命系统开始能够回应在其诞生时尚不存在的条件.<BR>这种源质被命名为 Evolutio: 它并非生长本身, 而是成为他物的许可.

            // #tr tc.research_text.EVOLUTIO.2
            // # <LINE>{\BOLD}Observation Record ESS-EVO-01<BR>{\RESET}Samples exposed to this aspect did not immediately change. Instead, they retained a response to conditions introduced only after the exposure ended.<BR>The effect resembles memory, except the remembered event had not yet occurred.<BR>Status: aspect stable. Temporal interpretation rejected by two reviewers.
            // #zh_CN <LINE>{\BOLD}观察记录 ESS-EVO-01<BR>{\RESET}接触该源质的样本没有立刻发生变化. 相反, 它们保留了对暴露结束后才被引入之条件的响应.<BR>这种效果近似记忆, 但被记住的事件在当时尚未发生.<BR>状态: 源质稳定. 两名复核者拒绝采用时间解释.

            // #tr tc.research_text.EVOLUTIO.3
            // # <LINE>{\BOLD}Observation Record ESS-EVO-02<BR>{\RESET}The aspect was compared against the Font and the original aquatic specimen. All three produced the same terminal stroke, but only Evolutio continued writing after the instruments were disconnected.<BR>The added line does not describe adaptation to an environment. It describes an environment adapting to the specimen.<BR>Status: transcription suspended.
            // #zh_CN <LINE>{\BOLD}观察记录 ESS-EVO-02<BR>{\RESET}将该源质与生态泉源及原始水域样本进行比对后, 三者都产生了相同的末端笔画, 但只有 Evolutio 在仪器断开后仍继续书写.<BR>新增的线条并非描述生命适应环境. 它描述的是环境正在适应样本.<BR>状态: 转录已暂停.
            // spotless:on
            new ResearchItem(
                "EVOLUTIO",
                "TST",
                (new AspectList()).add(EVOLUTION, 1)
                    .add(Aspect.LIFE, 1)
                    .add(Aspect.EXCHANGE, 1),
                -4,
                -11,
                5,
                Mods.Gendustry.isModLoaded() ? GTModHandler.getModItem(Mods.Gendustry.ID, "LiquidDNABucket", 1)
                    : new ItemStack(Items.water_bucket, 1))
                        .setPages(
                            new ResearchPage((new AspectList()).add(EVOLUTION, 1)),
                            new ResearchPage(TextEnums.tr("tc.research_text.EVOLUTIO.1")),
                            new ResearchPage(TextEnums.tr("tc.research_text.EVOLUTIO.2")),
                            new ResearchPage(TextEnums.tr("tc.research_text.EVOLUTIO.3")))
                        .setHidden()
                        .setRound()
                        .registerResearchItem();
        }

        if (Config.Enable_BloodHell) {
            // spotless:off
            // #tr tc.research_name.BLOODY_HELL
            // # Bloody Hell
            // #zh_CN 血狱

            // #tr tc.research_text.BLOODY_HELL
            // # BLOOD, BLOOD, BLOOD!
            // #zh_CN 血！血！血！

            // #tr tc.research_text.BLOODY_HELL.1
            // # BLOOD, BLOOD, BLOOD!
            // #zh_CN 血！血！血！
            // spotless:on
            new ResearchItem(
                "BLOODY_HELL",
                "TST",
                new AspectList().merge(Aspect.LIFE, 1)
                    .merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1),
                4,
                -1,
                5,
                GTCMItemList.BloodyHell.get(1, 0))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.BLOODY_HELL.1")),
                        new ResearchPage(infusionRecipeBloodyHell))
                    .setParents("TST_WELCOME")
                    .registerResearchItem();

            if (Config.Enable_BloodHatch) {
                // spotless:off
                // #tr tc.research_name.BLOOD_HATCH
                // # Blood Hatch
                // #zh_CN 血液仓

                // #tr tc.research_text.BLOOD_HATCH
                // # BLOOD, BLOOD, BLOOD!
                // #zh_CN 血！血！血！

                // #tr tc.research_text.BLOOD_HATCH.1
                // # The zombie brains are thirst for blood. Maybe we can make use of this.
                // #zh_CN 僵尸的脑子渴望得到血液。也许我们能够利用这一点。
                // spotless:on
                new ResearchItem(
                    "BLOOD_HATCH",
                    "TST",
                    new AspectList().merge(Aspect.LIFE, 1)
                        .merge(Aspect.MAGIC, 1)
                        .merge(Aspect.TOOL, 1),
                    6,
                    0,
                    5,
                    GTCMItemList.BloodOrbHatch.get(1, 0))
                        .setPages(
                            new ResearchPage(TextEnums.tr("tc.research_text.BLOOD_HATCH.1")),
                            new ResearchPage(infusionRecipeBloodHatch))
                        .setParents("BLOODY_HELL")
                        .setSecondary()
                        .registerResearchItem();
            }
            // spotless:off
            // #tr tc.research_name.TIME_BENDING_SPEED_RUNE
            // # Time-bending Speed Rune
            // #zh_CN 时间扭曲速度符文

            // #tr tc.research_text.TIME_BENDING_SPEED_RUNE
            // # Electrotine Torch,Start!
            // #zh_CN 蓝石火把,启动!

            // #tr tc.research_text.TIME_BENDING_SPEED_RUNE.1
            // # The SpaceTime bends with Speed Runes and Accelerators, and it showed the compatibility to the advanced Altars.
            // #zh_CN 使用速度符文和世界加速器扭曲的时空展现出对高级祭坛的兼容性。
            // spotless:on
            new ResearchItem(
                "TIME_BENDING_SPEED_RUNE",
                "TST",
                new AspectList().merge(Aspect.LIFE, 1)
                    .merge(Aspect.MAGIC, 1)
                    .merge(Aspect.TOOL, 1),
                6,
                -1,
                5,
                new ItemStack(TstBlocks.TimeBendingSpeedRune))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.TIME_BENDING_SPEED_RUNE.1")),
                        new ResearchPage(infusionRecipeTimeBendingSpeedRune))
                    .setParents("BLOODY_HELL")
                    .setSecondary()
                    .registerResearchItem();

            if (Config.Enable_IndustrialAlchemyTower) {
                // spotless:off
                // #tr tc.research_name.INDUSTRIAL_ALCHEMY_TOWER
                // # Industrial Alchemy Tower
                // #zh_CN 工业炼金塔

                // #tr tc.research_text.INDUSTRIAL_ALCHEMY_TOWER
                // # Batch alchemy!
                // #zh_CN 批量化炼金!

                // #tr tc.research_text.INDUSTRIAL_ALCHEMY_TOWER.1
                // # Your power is unprecedentedly strong, and with a little experimentation, you have created this machine: a machine capable of batch processing thaumic crucible recipes. It's just that this machine needs to be sealed, which is a good thing, right?
                // #zh_CN 你的力量空前强大，稍加尝试便创造出了这台机器：一台能够批量化进行神秘坩埚配方的机器。只不过这台机器需要密封，这是件好事对吧？
                // spotless:on
                new ResearchItem(
                    "INDUSTRIAL_ALCHEMY_TOWER",
                    "TST",
                    new AspectList().merge(Aspect.AIR, 1)
                        .merge(Aspect.FIRE, 1)
                        .merge(Aspect.ENTROPY, 1)
                        .merge(Aspect.ORDER, 1)
                        .merge(Aspect.EXCHANGE, 1),
                    -4,
                    -1,
                    9,
                    GTCMItemList.IndustrialAlchemyTower.get(1))
                        .setPages(
                            new ResearchPage(TextEnums.tr("tc.research_text.INDUSTRIAL_ALCHEMY_TOWER.1")),
                            new ResearchPage(infusionRecipeIndustrialAlchemyTower))
                        .setParents("TST_WELCOME")
                        .registerResearchItem();
            }
            // spotless:off
            // #tr tc.research_name.TST_ARCANE_HOLE
            // # Arcane Hole
            // #zh_CN 奥术裂隙

            // #tr tc.research_text.TST_ARCANE_HOLE
            // # Block in the void
            // #zh_CN 虚空中之物

            // #tr tc.research_text.TST_ARCANE_HOLE.1
            // # Can be used to replace the warded glass on both sides of industrial alchemy tower. Perhaps it's still a good building block?
            // #zh_CN 可以用来替代工业炼金塔两侧的守卫者玻璃。或许还是一种不错的建筑方块？
            // spotless:on
            new ResearchItem(
                "TST_ARCANE_HOLE",
                "TST",
                new AspectList().merge(Aspect.DARKNESS, 4)
                    .merge(Aspect.VOID, 4)
                    .merge(Aspect.SENSES, 8),
                -6,
                -1,
                1,
                new ItemStack(TstBlocks.BlockArcaneHole))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.TST_ARCANE_HOLE.1")),
                        new ResearchPage(crucibleRecipeArcaneHole))
                    .setParents(existingParentOrRoot("INDUSTRIAL_ALCHEMY_TOWER"))
                    .setSecondary()
                    .registerResearchItem();
        }
        if (Config.Enable_PrimordialDisjunctus) {
            // spotless:off
            // #tr tc.research_name.PRIMORDIAL_DISJUNCTUS
            // # Primordial Disjunctus
            // #zh_CN 初源解离机

            // #tr tc.research_text.PRIMORDIAL_DISJUNCTUS
            // # Elementary essentia free!
            // #zh_CN 初等源质自由!

            // #tr tc.research_text.PRIMORDIAL_DISJUNCTUS.1
            // # The first step in the freedom of source matter
            // #zh_CN 源质自由的第一步!
            // spotless:on
            new ResearchItem(
                "PRIMORDIAL_DISJUNCTUS",
                "TST",
                new AspectList().merge(Aspect.TOOL, 1)
                    .merge(Aspect.HUNGER, 1)
                    .merge(Aspect.MINE, 1)
                    .merge(Aspect.AURA, 1),
                -4,
                0,
                9,
                GTCMItemList.PrimordialDisjunctus.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.PRIMORDIAL_DISJUNCTUS.1")),
                        new ResearchPage(infusionRecipePrimordialDisjunctus))
                    .setParents(existingParentOrRoot("ESSENTIA_DISCRETIZER"))
                    .registerResearchItem();
        }
        if (Config.Enable_SkypiercerTower) {
            // spotless:off
            // #tr tc.research_name.SKYPIERCER_TOWER
            // # Skypiercer Tower
            // #zh_CN 穿云尖塔

            // #tr tc.research_text.SKYPIERCER_TOWER
            // # Crafting Essentia On Demand!
            // #zh_CN 源质自由!

            // #tr tc.research_text.SKYPIERCER_TOWER.1
            // #en_US {\BOLD}Piercing the sky:{\RESET}<BR><BR>As an upgrade to this multi-block you can increase its processing speed for each layer (additively). If you want to produce a lot of aspects with this multi-block on demand or maintained up to a level you will need a lot of time, power or rings. So... better install more rings. Otherwise, are you truly worthy of the name "Skypiercer"?<BR>
            // #zh_CN {\BOLD}穿云尖塔:{\RESET}<BR><BR>作为该多方块结构的附加升级,你可以通过每增加一层提升其处理速度(可叠加).如果你希望随时或维持地大量产出要素,你将需要大量的时间,电力或环装置.所以……还是多装些环吧.不然怎么称得上“穿云”？<BR>

            // #tr tc.research_text.SKYPIERCER_TOWER.2
            // #en_US {\BOLD}Automation Tips:{\RESET}<BR><BR>Without input and output buses, full automation becomes a bit more challenging. However, it is worth noting that the Essentia used in ThaumicEnergistics operates through {\ITALIC}fluid channels{\RESET}. This reveals a solution: place a fluid interface (for pattern distribution) directly adjacent to a subnet interface (which accepts Crystal Essentia). Inside the subnet, install an Essentia Discretizer — as Crystal Essentia enters, it is automatically converted into a fluid form.<BR><BR>Then, by attaching Fluid Storage Buses to two super tanks as buffers for essentia fluids, and taking advantage of the smart blocking mode of the interface, you can create a natural blocking mechanism. This setup works perfectly — even though the exported items become fluids upon entry, everything functions seamlessly.Finally, connecting infusion provider as a component of SkypiercerTower, and the essentia ExportBus is connected to the main network.
            // #zh_CN {\BOLD}自动化提示:{\RESET}<BR><BR>没有输入输出总线,自动化显然变得困难了一些,然而不得不提及的是神秘能源的源质使用的通道是{\ITALIC}流体{\RESET}.这揭示了一种方案,具体来说:将主网的接口(用于样板发配)紧贴子网的二合一接口(接受晶化源质).子网内需放置一个源质离散器,使晶化源质在进入时自动转化为流体.<BR><BR>随后在两个超级缸上贴上流体存储总线作为缓存源质的容器,并借助二合一接口的智能阻挡模式,可以自然地产生阻挡效果.这种设计恰到好处——即使发配是物品在进入后成为流体,整个系统依旧能正确运作,最后连接提供器做为穿云尖塔的组成部分,而源质输出仓连接主网即可.

            // #tr tc.research_text.SKYPIERCER_TOWER.3
            // #en_US The schematic diagram can be found on the next page. Due to the limitations of code implementation, it is slightly out of style.Because the image insertion of 128*128 is just right, but in that case it would be difficult to identify the content. Therefore, the size of 256*256 was still adopted.I'm not quite sure how to position this picture in the middle. Maybe it would be better this way.
            // #zh_CN 示意图见下一页,碍于代码实现,稍微有点不合风格,因为图片插入128*128刚刚好,但是那样就完全看不清了,因此仍然采用了256*256大小.我不是很清楚怎么把这个图片放中间,这样的话也许会好点.

            // #tr tc.research_text.SKYPIERCER_TOWER.4
            // #en_US Automation diagram for the Skypiercer Tower.<IMG>gtnhcommunitymod:textures/icons/Thaumonomicon/Automation_Diagram_of_the_Skypiercer_Tower.png:0:0:256:256:1</IMG>
            // #zh_CN 穿云尖塔自动化示意图.<IMG>gtnhcommunitymod:textures/icons/Thaumonomicon/Automation_Diagram_of_the_Skypiercer_Tower.png:0:0:256:256:1</IMG>

            // #tr tc.research_text.SKYPIERCER_TOWER.5
            // # {\BOLD}Aspect tier and machine processing time rules:{\RESET}<BR><BR>{\BOLD}Aspect Tier:<BR>{\RESET}Primal aspects are Tier 0.<BR> Composite aspect (aspects made from composite/primal aspects) take the highest tier component and add 1 to determine its tier.<BR><BR>{\BOLD}Processing Time:<BR>{\RESET}An aspect tier of 'x' requires 2 * x seconds (excluding time to synthesize its components).
            // #zh_CN {\BOLD}要素等级与机器加工时间规则:{\RESET}<BR><BR>{\BOLD}要素等级:<BR>{\RESET}初等要素为0级。<BR>复合要素(由初等或其他复合要素组成)等级为其子要素等级较高者加1.<BR><BR>{\BOLD}加工时间:<BR>{\RESET}等级为 x 的要素需加工 2 * x 秒(不包括合成其组成部分所需的时间).

            // #tr tc.research_text.SKYPIERCER_TOWER.6
            // # {\BOLD}Recursive Synthesis Note:{\RESET}<BR><BR>All composite aspects are synthesized entirely from primal aspects. Each composite aspect must be synthesized step by step. Meaning that the total synthesized time for high tier composite aspects can differ quite a bit.<BR>{\BOLD}(see next page for timings){\RESET}<BR><BR>{\BOLD}Critical:<BR>{\RESET}Primal aspects (tier 0) cannot be synthesized. They must be made available to the Skypiercer Tower through the infusion provider or the machine will fail to start.
            // #zh_CN {\BOLD}关于递归合成的说明:{\RESET}<BR><BR>所有复合要素都必须由初等要素逐步合成,每一个复合要素都需一层层构建.因此,高等级复合要素的总合成时间会迅速增加(实际上是指数级增长)<BR>{\BOLD}(具体时间请参见下一页){\RESET}<BR><BR>{\BOLD}注意:<BR>{\RESET}初等要素(0级)无法被合成,必须通过注魔供应器提供给穿云尖塔,否则机器将无法启动.

            // #tr tc.research_text.SKYPIERCER_TOWER.7
            // #en_US {\BOLD}Tier 1 Compound Aspects{\RESET}<BR>{\BOLD}Gelum:{\RESET} 2 seconds<BR>{\BOLD}Lux:{\RESET} 2 seconds<BR>{\BOLD}Motus:{\RESET} 2 seconds<BR>{\BOLD}Permutatio:{\RESET} 2 seconds<BR>{\BOLD}Potentia:{\RESET} 2 seconds<BR>{\BOLD}Tempestas:{\RESET} 2 seconds<BR>{\BOLD}Vacuos:{\RESET} 2 seconds<BR>{\BOLD}Venenum:{\RESET} 2 seconds<BR>{\BOLD}Victus:{\RESET} 2 seconds<BR>{\BOLD}Vitreus:{\RESET} 2 seconds
            // #zh_CN {\BOLD}一级复合要素{\RESET}<BR>{\BOLD}寒冰:{\RESET} 2 秒<BR>{\BOLD}光明:{\RESET} 2 秒<BR>{\BOLD}移动:{\RESET} 2 秒<BR>{\BOLD}交换:{\RESET} 2 秒<BR>{\BOLD}能量:{\RESET} 2 秒<BR>{\BOLD}气候:{\RESET} 2 秒<BR>{\BOLD}虚空:{\RESET} 2 秒<BR>{\BOLD}毒药:{\RESET} 2 秒<BR>{\BOLD}生命:{\RESET} 2 秒<BR>{\BOLD}水晶:{\RESET} 2 秒

            // #tr tc.research_text.SKYPIERCER_TOWER.8
            // #en_US {\BOLD}Tier 2 Compound Aspects (1/2){\RESET}<BR>{\BOLD}Bestia:{\RESET} 8 seconds<BR>{\BOLD}Fames:{\RESET} 8 seconds<BR>{\BOLD}Herba:{\RESET} 6 seconds<BR>{\BOLD}Iter:{\RESET} 6 seconds<BR>{\BOLD}Limus:{\RESET} 6 seconds<BR>{\BOLD}Metalum:{\RESET} 6 seconds<BR>{\BOLD}Mortuus:{\RESET} 6 seconds<BR>{\BOLD}Praecantio:{\RESET} 8 seconds<BR>{\BOLD}Radio:{\RESET} 8 seconds<BR>{\BOLD}Sano:{\RESET} 6 seconds
            // #zh_CN {\BOLD}二级复合要素 (1/2){\RESET}<BR>{\BOLD}野兽:{\RESET} 8 秒<BR>{\BOLD}饥饿:{\RESET} 8 秒<BR>{\BOLD}植物:{\RESET} 6 秒<BR>{\BOLD}旅行:{\RESET} 6 秒<BR>{\BOLD}粘液:{\RESET} 6 秒<BR>{\BOLD}金属:{\RESET} 6 秒<BR>{\BOLD}死亡:{\RESET} 6 秒<BR>{\BOLD}魔力:{\RESET} 8 秒<BR>{\BOLD}Radio:{\RESET} 8 秒<BR>{\BOLD}治疗:{\RESET} 6 秒

            // #tr tc.research_text.SKYPIERCER_TOWER.9
            // #en_US {\BOLD}Tier 2 Compound Aspects (2/2){\RESET}<BR>{\BOLD}Tempus:{\RESET} 6 seconds<BR>{\BOLD}Tenebrae:{\RESET} 6 seconds<BR>{\BOLD}Vinculum:{\RESET} 6 seconds<BR>{\BOLD}Volatus:{\RESET} 6 seconds
            // #zh_CN {\BOLD}二级复合要素 (2/2){\RESET}<BR>{\BOLD}Tempus:{\RESET} 6 秒<BR>{\BOLD}黑暗:{\RESET} 6 秒<BR>{\BOLD}陷阱:{\RESET} 6 秒<BR>{\BOLD}飞行:{\RESET} 6 秒

            // #tr tc.research_text.SKYPIERCER_TOWER.10
            // #en_US {\BOLD}Tier 3 Compound Aspects (1/2){\RESET}<BR>{\BOLD}Alienis:{\RESET} 14 seconds<BR>{\BOLD}Arbor:{\RESET} 12 seconds<BR>{\BOLD}Auram:{\RESET} 14 seconds<BR>{\BOLD}Corpus:{\RESET} 20 seconds<BR>{\BOLD}Exanimis:{\RESET} 14 seconds<BR>{\BOLD}Gula:{\RESET} 16 seconds<BR>{\BOLD}Infernus:{\RESET} 14 seconds<BR>{\BOLD}Magneto:{\RESET} 18 seconds<BR>{\BOLD}Spiritus:{\RESET} 14 seconds<BR>{\BOLD}Superbia:{\RESET} 14 seconds
            // #zh_CN {\BOLD}三级复合要素 (1/2){\RESET}<BR>{\BOLD}异域:{\RESET} 14 秒<BR>{\BOLD}树木:{\RESET} 12 秒<BR>{\BOLD}灵气:{\RESET} 14 秒<BR>{\BOLD}肉体:{\RESET} 20 秒<BR>{\BOLD}不死:{\RESET} 14 秒<BR>{\BOLD}Gula:{\RESET} 16 秒<BR>{\BOLD}Infernus:{\RESET} 14 秒<BR>{\BOLD}Magneto:{\RESET} 18 秒<BR>{\BOLD}灵魂:{\RESET} 14 秒<BR>{\BOLD}Superbia:{\RESET} 14 秒

            // #tr tc.research_text.SKYPIERCER_TOWER.11
            // #en_US {\BOLD}Tier 3 Compound Aspects (2/2){\RESET}<BR>{\BOLD}Vitium:{\RESET} 14 seconds
            // #zh_CN {\BOLD}三级复合要素 (2/2){\RESET}<BR>{\BOLD}污染:{\RESET} 14 秒

            // #tr tc.research_text.SKYPIERCER_TOWER.12
            // #en_US {\BOLD}Tier 4 Compound Aspects{\RESET}<BR>{\BOLD}Cognito:{\RESET} 22 seconds<BR>{\BOLD}Desidia:{\RESET} 28 seconds<BR>{\BOLD}Luxuria:{\RESET} 36 seconds<BR>{\BOLD}Sensus:{\RESET} 22 seconds
            // #zh_CN {\BOLD}四级复合要素{\RESET}<BR>{\BOLD}认知:{\RESET} 22 秒<BR>{\BOLD}Desidia:{\RESET} 28 秒<BR>{\BOLD}Luxuria:{\RESET} 36 秒<BR>{\BOLD}感官:{\RESET} 22 秒

            // #tr tc.research_text.SKYPIERCER_TOWER.13
            // #en_US {\BOLD}Tier 5 Compound Aspects{\RESET}<BR>{\BOLD}Humanus:{\RESET} 40 seconds<BR>{\BOLD}Invidia:{\RESET} 40 seconds<BR>{\BOLD}Strontio:{\RESET} 32 seconds
            // #zh_CN {\BOLD}五级复合要素{\RESET}<BR>{\BOLD}人类:{\RESET} 40 秒<BR>{\BOLD}Invidia:{\RESET} 40 秒<BR>{\BOLD}Strontio:{\RESET} 32 秒

            // #tr tc.research_text.SKYPIERCER_TOWER.14
            // #en_US {\BOLD}Tier 6 Compound Aspects{\RESET}<BR>{\BOLD}Instrumentum:{\RESET} 52 seconds<BR>{\BOLD}Lucrum:{\RESET} 60 seconds<BR>{\BOLD}Messis:{\RESET} 58 seconds<BR>{\BOLD}Perforio:{\RESET} 52 seconds
            // #zh_CN {\BOLD}六级复合要素{\RESET}<BR>{\BOLD}工具:{\RESET} 52 秒<BR>{\BOLD}贪婪:{\RESET} 60 秒<BR>{\BOLD}作物:{\RESET} 58 秒<BR>{\BOLD}矿藏:{\RESET} 52 秒

            // #tr tc.research_text.SKYPIERCER_TOWER.15
            // #en_US {\BOLD}Tier 7 Compound Aspects{\RESET}<BR>{\BOLD}Fabrico:{\RESET} 106 seconds<BR>{\BOLD}Machina:{\RESET} 68 seconds<BR>{\BOLD}Meto:{\RESET} 124 seconds<BR>{\BOLD}Nebrisum:{\RESET} 126 seconds<BR>{\BOLD}Pannus:{\RESET} 74 seconds<BR>{\BOLD}Telum:{\RESET} 66 seconds<BR>{\BOLD}Terminus:{\RESET} 88 seconds<BR>{\BOLD}Tutamen:{\RESET} 66 seconds
            // #zh_CN {\BOLD}七级复合要素{\RESET}<BR>{\BOLD}合成:{\RESET} 106 秒<BR>{\BOLD}机械:{\RESET} 68 秒<BR>{\BOLD}收获:{\RESET} 124 秒<BR>{\BOLD}Nebrisum:{\RESET} 126 秒<BR>{\BOLD}布匹:{\RESET} 74 秒<BR>{\BOLD}武器:{\RESET} 66 秒<BR>{\BOLD}Terminus:{\RESET} 88 秒<BR>{\BOLD}防护:{\RESET} 66 秒

            // #tr tc.research_text.SKYPIERCER_TOWER.16
            // #en_US {\BOLD}Tier 8 Compound Aspects{\RESET}<BR>{\BOLD}Electrum:{\RESET} 86 seconds<BR>{\BOLD}Ira:{\RESET} 82 seconds
            // #zh_CN {\BOLD}八级复合要素{\RESET}<BR>{\BOLD}Electrum:{\RESET} 86 秒<BR>{\BOLD}Ira:{\RESET} 82 秒
            // spotless:on
            new ResearchItem(
                "SKYPIERCER_TOWER",
                "TST",
                new AspectList().merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1)
                    .merge(Aspect.AURA, 1)
                    .merge(Aspect.ENERGY, 1),
                -4,
                1,
                9,
                GTCMItemList.SkypiercerTower.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.1")),
                        new ResearchPage(infusionRecipeSkypiercerTower),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.2")),
                        new ResearchPage("tc.research_text.SKYPIERCER_TOWER.3"),
                        new ResearchPage(" "),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.4")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.5")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.6")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.7")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.8")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.9")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.10")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.11")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.12")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.13")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.14")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.15")),
                        new ResearchPage(TextEnums.tr("tc.research_text.SKYPIERCER_TOWER.16")))
                    .setParents(existingParentOrRoot("ESSENTIA_DISCRETIZER"))
                    .registerResearchItem();
        }
        if (Config.Enable_InfusionMaterialDispenser) {
            // spotless:off
            // #tr tc.research_name.INFUSION_MATERIAL_DISPENSER
            // # Infusion Material Dispenser
            // #zh_CN 注魔原料分配器

            // #tr tc.research_text.INFUSION_MATERIAL_DISPENSER
            // # automatically dispense!
            // #zh_CN 自动分配!

            // #tr tc.research_text.INFUSION_MATERIAL_DISPENSER.1
            // # Many have been confounded by such mechanisms. To those who delight in the art of automation, they are challenges to be mastered, not removed. Yet some, restrained by their craft, can no longer tread the path of Thaumic automation, their mystical experience thus diminished. This device was forged to aid those who struggle with the intricacies of automated infusion.
            // #zh_CN 常常有人被这些东西难住,对于享受自动化的玩家来说这是一种挑战,不应当剥夺他们的乐趣,然而也不乏玩家受限于水平因而对神秘自动化再也无法踏足,进一步导致对神秘体验的下降,此机器旨在帮助自动化较为困难的玩家进行辅助注魔.

            // #tr tc.research_text.INFUSION_MATERIAL_DISPENSER.2
            // # {\BOLD}Automation Tips:{\RESET}<BR><BR>Similar to the Skypiercer Tower, but this time the pattern itself contains items that will be consumed during infusion. Because of this, the subnet must include a Storage Bus attached to the Infusion Dispenser’s input bus. After the crystallized Essentia is converted back into Essentia by the Essentia Discretizer, the remaining items will naturally be stored into the input bus — exactly the behavior we want.<BR><BR>Furthermore, since the types of Essentia required for infusion are no longer limited to only two, Essentia Cells should be used within the subnet. Accordingly, an CardAdvancedBlocking must be inserted into the subnet’s fluid_interface and configured to {\BOLD}AdvancedBlockingModeAll{\RESET}.
            // #zh_CN {\BOLD}自动化提示:{\RESET}<BR><BR>与穿云尖塔的情形类似.不过这次由于样板里含有被用于注魔的物品,所以需要子网配有一个存储总线贴到注魔分配器的输入总线.这样在晶化源质被注魔离散器转为真正源质后剩余物品自然被存储到了输入总线——这正是我们所需要的。<BR><BR>此外，注魔所需的源质种类不再只局限于两种，因此这次应当使用源质元件,对应的需要在子网的二合一接口额外插入一张阻挡卡并设置为{\BOLD}严格阻挡模式{\RESET}。
            // spotless:on
            new ResearchItem(
                "INFUSION_MATERIAL_DISPENSER",
                "TST",
                new AspectList().merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.HUNGER, 1)
                    .merge(Aspect.MOTION, 1)
                    .merge(Aspect.EXCHANGE, 1),
                -4,
                2,
                9,
                GTCMItemList.InfusionMaterialDispenser.get(1))
                    .setPages(
                        new ResearchPage(TextEnums.tr("tc.research_text.INFUSION_MATERIAL_DISPENSER.1")),
                        new ResearchPage(TextEnums.tr("tc.research_text.INFUSION_MATERIAL_DISPENSER.2")),
                        new ResearchPage(infusionRecipeInfusionMaterialDispenser))
                    .setParents(existingParentOrRoot("ESSENTIA_DISCRETIZER"))
                    .registerResearchItem();
        }

        /*
         * if (Config.Enable_EssentiaDiscretizer) {
         * // #tr tc.research_name.ESSENTIA_DISCRETIZER
         * // # Essentia Discretizer
         * // #zh_CN 源质离散器
         * // #tr tc.research_text.ESSENTIA_DISCRETIZER
         * // # Free movement!
         * // #zh_CN 自由流动!
         * // spotless:off
         * // #tr tc.research_text.ESSENTIA_DISCRETIZER.0
         * // # As a thaumaturge versed in the art of technology, you have long been vexed by the management of
         * essentia. The properties unveiled upon crystallization are precisely what you seek. Through the study of the
         * crystallizer and the fluid discretizer, and by melding mind with machine, the Essentia Discretizer has come
         * into being!
         * // #zh_CN 作为一名进修过科技的魔法使,你常常为源质发配感到头疼,而源质结晶后所展现的特性正是你所需的,通过对结晶器与流体离散器的研究,配合大脑与电路的控制,源质离散器就此而生!
         *
         * // #tr tc.research_text.ESSENTIA_DISCRETIZER.1
         * // # Getting back to the point, the Essentia Discretizer is a container that monitors both item and fluid
         * channels, operating with the highest priority. When either item-based or fluid-based essentia enters, the
         * Discretizer first detects it. If it is indeed essentia, the device inserts it into the corresponding
         * component or container, while simultaneously creating a crystallized essentia as a duplicate that stays
         * synchronized with the original. Conversely, when the crystallized essentia is consumed, the corresponding
         * original essentia undergoes the same consumption process.
         * // #zh_CN
         * 言归正传,源质离散器,是一个容器,监听物品与流体通道,且具有最高优先级,当物品源质亦或者流体版源质进入时,首先被离散器检测,如果确实为源质则将其插入至对应的元件或者容器,并且本身创建一份晶化源质作为副本,与其同步变化,
         * 反过来,将晶化源质被使用时对应的本体也做一样的消耗行为.
         * // spotless:on
         * new ResearchItem(
         * "ESSENTIA_DISCRETIZER",
         * "TST",
         * new AspectList().merge(Aspect.MECHANISM, 1)
         * .merge(Aspect.MAN, 1)
         * .merge(Aspect.MAGIC, 1)
         * .merge(Aspect.SOUL, 1),
         * 0,
         * 6,
         * 9,
         * BlockEssentiaDiscretizer.stack()).setPages(
         * new ResearchPage(TextEnums.tr("tc.research_text.ESSENTIA_DISCRETIZER.0")),
         * new ResearchPage(TextEnums.tr("tc.research_text.ESSENTIA_DISCRETIZER.1")),
         * new ResearchPage(infusionRecipeEssentiaDiscretizer))
         * .setParents("TST_WELCOME")
         * .registerResearchItem();
         * }
         */

    }

    private static String existingParentOrRoot(String researchKey) {
        return ResearchCategories.getResearch(researchKey) == null ? ROOT_RESEARCH : researchKey;
    }

}
