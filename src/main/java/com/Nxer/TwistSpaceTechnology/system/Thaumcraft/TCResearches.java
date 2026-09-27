package com.Nxer.TwistSpaceTechnology.system.Thaumcraft;

import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic.EVOLUTION;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.crucibleRecipeArcaneHole;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeBloodHatch;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeBloodyHell;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereExecutionProtocol1;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereExecutionProtocol2;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereExecutionProtocol3;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereExecutionProtocol4;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereExecutionProtocol5;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereExecutionProtocol6;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereExecutionProtocol7;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereExecutionProtocol8;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereExecutionProtocol9;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereInputInterface;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereSimulator;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereUpgrade1;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereUpgrade2;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereUpgrade3;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereUpgrade4;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereUpgrade5;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereUpgrade6;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereUpgrade7;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEcoSphereUpgradeInterface;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeElvenWorkshop;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeEssentiaDiscretizer;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeFountOfEcology;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeIndustrialAlchemyTower;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeIndustrialMagicMatrix;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeInfusionMaterialDispenser;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipePrimordialDisjunctus;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeSkypiercerTower;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeTimeBendingSpeedRune;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool.infusionRecipeTimeBendingSpeedRuneTimewood;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BlockEssentiaDiscretizer;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class TCResearches {

    private static final String TST_Path = "gtnhcommunitymod";
    private static final String ROOT_RESEARCH = "TST_WELCOME";

    private static AspectList getResearchAspects(InfusionRecipe recipe) {
        AspectList researchAspects = new AspectList();
        if (recipe == null || recipe.getAspects() == null) return researchAspects;
        for (Aspect aspect : recipe.getAspects()
            .getAspects()) {
            researchAspects.merge(aspect, 1);
        }
        return researchAspects;
    }

    public static void register() {
        loadResearchTab();
        loadResearches();
    }

    public static void loadResearchTab() {
        ResearchCategories.registerCategory(
            "TST",
            new ResourceLocation(TST_Path, "textures/items/MetaItem01/33.png"),
            new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));
        // spotless:off
        // #tr tc.research_category.TST
        // # Twist Space Technology
        // #zh_CN 扭曲空间科技
        // spotless:on
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
                .setPages(new ResearchPage(TSTUtils.tr("tc.research_text.TST_WELCOME.1")))
                .setSpecial()
                .registerResearchItem();

        new ResearchItem(
            // #tr tc.research_name.BH_ELVEN_WORKSHOP
            // # Elven Workshop
            // #zh_CN 精灵工坊

            // #tr tc.research_text.BH_ELVEN_WORKSHOP
            // # First attempt of GT-styled Mana Pool
            // #zh_CN GT化魔力池的第一次尝试
            "BH_ELVEN_WORKSHOP",
            "TST",
            (new AspectList()).merge(Aspect.EARTH, 1)
                .merge(Aspect.MECHANISM, 1)
                .merge(Aspect.MAGIC, 1),
            5,
            0,
            3,
            GTCMItemList.ElvenWorkshop.get(1, 0)).setParents("BH_GAIA_PYLON")
                .setPages(
                    // spotless:off
                    // #tr tc.research_text.BH_ELVEN_WORKSHOP.1
                    // # Having mastered the power of Gaia's Spirit, you have finally explored a way to mechanize a facility capable of plant magic. The speed of the mana pool is limited to 1 recipe/t, making it unsuitable for large-scale processing. However, the machine is still unable to execute elven recipes. The elves' control of magic is so precise that it exceeds the machine's accuracy.
                    // #zh_CN 在掌控了盖亚之魂的力量之后, 你终于探索出一种能够机械化植物魔法的设施的方法. 魔力池的速度被限制在1个配方/t, 使得其不适用于大规模处理. 然而, 这台机器仍然无法执行精灵配方. 精灵们对魔法的掌控过于精准以至于超过了机器的精度.
                    // spotless:on
                    new ResearchPage("tc.research_text.BH_ELVEN_WORKSHOP.1"),
                    // #tr tc.research_text.BH_ELVEN_WORKSHOP.2
                    // # Forturately, there is always a way out.
                    // #zh_CN 好在天无绝人之路.
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
                // #tr tc.research_name.INDUSTRIAL_MAGIC_MATRIX
                // # IndustrialMagicMatrix
                // #zh_CN 工业注魔矩阵

                // #tr tc.research_text.INDUSTRIAL_MAGIC_MATRIX
                // # Hey, hey, industrial infusion!
                // #zh_CN 嘿嘿嘿, 工业化注魔!
                "TST",
                (new AspectList()).merge(Aspect.EARTH, 1)
                    .merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1),
                5,
                2,
                5,
                GTCMItemList.IndustrialMagicMatrix.get(1, 0))/* .setParents("ICHORIUM") */
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.INDUSTRIAL_MAGIC_MATRIX.1")),
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
            // # {\BOLD}Purpose and Function<BR>{\RESET}The Eco-Sphere Simulator recreates selected natural processes within a sealed habitat. It requires dedicated input and upgrade interfaces. Install an execution protocol, submit its samples through the input interface, and provide a compatible medium to prepare the chosen environment.<BR>The chamber follows these instructions rather than choosing what lives inside. Controlled exceptions are accepted only through the upgrade interface.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}拟似生态圈会在封闭栖息地内复现特定自然过程. 完整结构需要专用的输入与升级接口. 将执行协议装入主机, 通过输入接口提交样本并提供相容介质, 舱室便会准备指定环境.<BR>它只遵循这些指令, 不会自行选择其中的生命. 受控例外仅由升级接口接纳.

            // #tr tc.research_text.ECO_SPHERE_SIMULATOR.2
            // # <LINE>{\BOLD}Operation Record ESS-00<BR>{\RESET}The first complete structure accepted power but remained inert. Every casing and both interfaces reported valid connections, yet the chamber refused to define itself.<BR>Once an execution protocol was installed, the controller recognized an unfinished habitat. Preparation still waited for the required inputs and medium.<BR>Status: controller operational. Protocol recognized.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-00<BR>{\RESET}首座完整结构接受供能后仍毫无反应. 所有机械方块与两个接口均报告连接有效, 但舱室拒绝定义自身.<BR>装入执行协议后, 主机才识别出尚未完成的栖息地. 准备流程仍会等待所需输入与介质.<BR>状态: 主机正常. 协议已识别.

            // #tr tc.research_text.ECO_SPHERE_SIMULATOR.3
            // # <LINE>{\BOLD}Operation Record ESS-01<BR>{\RESET}Changing protocols did not replace the habitat at once. Readings vanished from the top down until the chamber was empty. After the next protocol's inputs were accepted, a new environment occupied the cleared volume in a different sequence.<BR>No residue remained between states, yet the controller briefly marked both absent and active.<BR>Status: transition complete. Contradiction archived.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-01<BR>{\RESET}更换协议不会立刻替换原有栖息地. 各项读数从上向下消失, 直至舱室清空. 下一协议的输入得到接纳后, 新环境才以另一种次序占据空置区域.<BR>两种状态之间没有残留物, 但主机曾短暂将二者同时标记为不存在且仍在运行.<BR>状态: 切换完成. 矛盾已归档.

            // #tr tc.research_text.ECO_SPHERE_SIMULATOR.4
            // # <LINE>{\BOLD}Operation Record ESS-02<BR>{\RESET}All stable protocols reacted to increased supplied power in the same manner. The operating interval did not visibly shorten; instead, the final record contained several mutually complete histories for what instruments insisted had been a single cycle.<BR>The recovered matter agreed with every history at once.<BR>Status: output verified. Sequence count under dispute.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-02<BR>{\RESET}所有稳定协议都以相同方式回应了更高的输入功率. 可见的运行间隔并未缩短; 相反, 最终记录中出现了数段彼此完整的环境历史, 而仪器坚持整个过程只经历了一次循环.<BR>回收物质同时符合每一段历史.<BR>状态: 回收结果已确认. 循环数量存在争议.

            // #tr tc.research_text.ECO_SPHERE_SIMULATOR.5
            // # <LINE>{\BOLD}Operation Record ESS-03<BR>{\RESET}During an unscheduled shutdown, circulation ceased and every physical sensor returned to baseline. The controller continued reporting rainfall inside a sealed dry section for several minutes.<BR>Inspection found no water. When the record was reopened, the word "dry" had been replaced with "not yet filled."<BR>Status: no structural damage. Semantic fault unresolved.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-03<BR>{\RESET}一次计划外停机中, 循环已经停止, 所有实体传感器也恢复至基准值, 但主机仍持续报告一处封闭干燥区域内存在降水.<BR>检查没有发现水. 再次打开记录时, 其中的“干燥”已被替换为“尚未填充”.<BR>状态: 结构未受损. 语义故障未解决.
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
                -8,
                10,
                GTCMItemList.EcoSphereSimulator.get(1, 0))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_SIMULATOR.1")),
                        new ResearchPage(infusionRecipeEcoSphereSimulator),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_SIMULATOR.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_SIMULATOR.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_SIMULATOR.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_SIMULATOR.5")))
                    .setParents("TST_WELCOME")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_INPUT_INTERFACE
            // # Eco-Sphere Input Interface
            // #zh_CN 生态圈输入接口

            // #tr tc.research_text.ECO_SPHERE_INPUT_INTERFACE
            // # An entrance that waits for the habitat to define what may enter.
            // #zh_CN 一处等待栖息地决定何物可以进入的接口.

            // #tr tc.research_text.ECO_SPHERE_INPUT_INTERFACE.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}The input interface carries samples and instructions. It reshapes itself for the installed protocol, exposing cultivation, targeting, tree-selection, or biological-address controls.<BR>Records from other protocols remain isolated. Removing the protocol disables the interface but preserves its contents.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}输入接口负责接纳样本与指令, 并依照已安装协议显示培育、定向、树体选择或生物地址控制.<BR>其他协议的记录会被隔离保存. 取出协议只会停用接口, 不会丢失内容.

            // #tr tc.research_text.ECO_SPHERE_INPUT_INTERFACE.2
            // # <LINE>{\BOLD}Operation Record ESS-IN-01<BR>{\RESET}Tests under each stable protocol changed the visible entries but kept every sample with its original layout.<BR>No request crossed into another environment, and registration consumed nothing.<BR>Status: routing normal. Isolation confirmed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-IN-01<BR>{\RESET}各稳定协议会改变可见登记项, 但每份样本仍归属于原有布局.<BR>没有请求进入其他环境, 登记过程也不会消耗样本.<BR>状态: 分流正常. 隔离已确认.

            // #tr tc.research_text.ECO_SPHERE_INPUT_INTERFACE.3
            // # <LINE>{\BOLD}Operation Record ESS-IN-02<BR>{\RESET}A protocol was removed while entries remained stored. Labels vanished and the controller saw no usable input, but inventory remained intact.<BR>Reinstalling it restored every position and selection. The interface called the hidden contents "retained, not admissible."<BR>Status: records restored.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-IN-02<BR>{\RESET}存有内容时取出协议, 标签随即消失, 主机也不再识别输入, 但库存保持完整.<BR>重新装入后, 所有位置与选择均恢复. 接口将隐藏内容称为“保留但不接纳”.<BR>状态: 记录已恢复.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_INPUT_INTERFACE",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereInputInterface),
                0,
                -10,
                5,
                GTCMItemList.EcoSphereInputInterface.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_INPUT_INTERFACE.1")),
                        new ResearchPage(infusionRecipeEcoSphereInputInterface),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_INPUT_INTERFACE.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_INPUT_INTERFACE.3")))
                    .setParents("ECO_SPHERE_SIMULATOR")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_UPGRADE_INTERFACE
            // # Eco-Sphere Upgrade Interface
            // #zh_CN 生态圈升级接口

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_INTERFACE
            // # A reserved connection through which the habitat accepts controlled exceptions.
            // #zh_CN 一处供栖息地接纳受控例外的预留连接.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_INTERFACE.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}The upgrade interface accepts amendments, not ordinary inputs. Four positions remain visible; unavailable ones stay locked. Reinforcing the surrounding structure and its containment materials allows the interface to sustain more amendments, though its capacity remains finite.<BR>Ordinary amendments may repeat. Specialized ones reject duplicates; incompatible ones remain stored but inactive.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}升级接口接纳环境修订, 而非普通输入. 四个位置始终可见, 未开放者保持锁定. 强化周边结构与承载材料可使接口容纳更多修订, 但其容量仍有极限.<BR>普通修订可重复登记. 特殊修订拒绝重复项; 不相容者保留但不生效.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_INTERFACE.2
            // # <LINE>{\BOLD}Operation Record ESS-UP-01<BR>{\RESET}A registered amendment was installed before a stable cycle. The controller kept it separate from recipe inputs and applied it only after the protocol accepted the environment.<BR>Removing it restored prior behavior next cycle without cleaning or damage.<BR>Status: separation confirmed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-01<BR>{\RESET}稳定循环前装入一项修订. 主机将其与配方输入分开, 并在协议接纳环境后才应用效果.<BR>取出后, 下一循环无需清理便恢复原有行为, 升级也未受损.<BR>状态: 分离已确认.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_UPGRADE_INTERFACE",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereUpgradeInterface),
                2,
                -8,
                5,
                GTCMItemList.EcoSphereUpgradeInterface.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_INTERFACE.1")),
                        new ResearchPage(infusionRecipeEcoSphereUpgradeInterface),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_INTERFACE.2")))
                    .setParents("ECO_SPHERE_SIMULATOR")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_EXECUTION_PROTOCOL_1
            // # Eco-Sphere Execution Protocol: Arboreal Genesis
            // #zh_CN 生态圈执行协议: 原木拟生

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_1
            // # A forest begins with an instruction no tree can hear.
            // #zh_CN 森林始于一道树木无法听见的指令.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_1.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This protocol configures the Eco-Sphere as a managed forest. Submit valid saplings, supply water, and select the tree parts to recover. Distinct species may run together; duplicate entries retain only the largest group.<BR>Each retained specimen consumes its own medium. Unselected parts are redirected toward selected requests.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该协议会将拟似生态圈配置为受控林地. 提交有效树苗、提供水, 再选择需要回收的树体部分. 不同物种可同时运行; 重复记录只保留数量最多的一组.<BR>每份保留样本都会消耗介质. 未选择部分会转向已选请求.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_1.3
            // # <LINE>{\BOLD}Operation Record ESS-AG-01<BR>{\RESET}Subject: oak sapling. Medium: water. All collection categories enabled.<BR>The chamber completed a full seasonal model in one cycle. Four signatures corresponding to the tree's structure, renewal, canopy, and maturity were separated without visible damage to the template. Recovery increased with supplied power while the cycle duration remained fixed.<BR>Status: normal.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-01<BR>{\RESET}对象: 橡树树苗. 介质: 水. 全部收集类别已启用.<BR>舱室在一个循环内完成了完整季节模型. 对应树体结构、更新、冠层与成熟阶段的四组特征被分别回收, 模板未出现可见损伤. 回收量随输入功率提高, 循环时间保持不变.<BR>状态: 正常.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_1.4
            // # <LINE>{\BOLD}Operation Record ESS-AG-02<BR>{\RESET}Only the primary structural signature remained selected. Its recovery rose despite unchanged water and power; restoring all selections returned the distribution to normal.<BR>No extra growth occurred. Uncollected parts may have resurfaced under the remaining request, but no transfer was observed.<BR>Status: reproducible. Allocation unknown.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-02<BR>{\RESET}仅保留主体结构特征后, 水与功率未变, 回收量却上升. 恢复全部选择后分布回归正常.<BR>没有检测到额外生长. 未收集部分可能转入了剩余请求, 但未观测到过程.<BR>状态: 可复现. 分配机制未知.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_1.5
            // # <LINE>{\BOLD}Operation Record ESS-AG-03<BR>{\RESET}An unregistered fluid entered before circulation. The tree produced traces matching neither species, season, nor habitat; some persisted after removal but vanished under water.<BR>The protocol reset without identifying the medium or completing recovery.<BR>Status: unexpected response. Source unclassified.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-03<BR>{\RESET}未登记流体在循环前进入管线. 树木随即产生不符物种、季节与环境的痕迹; 部分在移除后仍存, 却无法以水复现.<BR>协议未能识别介质或完成回收便重置舱室.<BR>状态: 预期外响应. 来源未分类.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_EXECUTION_PROTOCOL_1",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereExecutionProtocol1),
                -4,
                -14,
                5,
                GTCMItemList.EcoSphereExecutionProtocol1.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_1.1")),
                        new ResearchPage(infusionRecipeEcoSphereExecutionProtocol1),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_1.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_1.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_1.5")))
                    .setParents("ECO_SPHERE_INPUT_INTERFACE")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_EXECUTION_PROTOCOL_2
            // # Eco-Sphere Execution Protocol: Arboreal Genesis II
            // #zh_CN 生态圈执行协议: 原木拟生 II

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2
            // # The forest no longer asks which season it is.
            // #zh_CN 森林不再询问此刻属于哪个季节.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This advanced protocol admits media whose growth records ignore normal season, habitat, or lineage. A valid sapling remains the initial reference, but the medium may replace what its species would leave behind.<BR>The sapling permits growth; each recognized medium decides how it ends.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该进阶协议允许不遵循季节、环境或血统记录的介质进入循环. 有效树苗仍是初始参照, 但介质可能替换该物种原本留下的事物.<BR>树苗允许生长开始; 每种介质决定生长如何结束.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.3
            // # <LINE>{\BOLD}Operation Record ESS-AG-04<BR>{\RESET}Medium: temporal fluid. Reference: valid.<BR>A complete seasonal sequence formed, but mature traces no longer matched the submitted species. Growth rings predated their power pulses, and recovered mechanisms bore years of use.<BR>Status: stable. Chronology disputed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-04<BR>{\RESET}介质: 时间流体. 参照: 有效.<BR>完整季节序列形成, 但成熟痕迹不再属于提交物种. 年轮早于对应供能脉冲出现, 回收结构也带有多年使用痕迹.<BR>状态: 稳定. 时间顺序存疑.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.4
            // # <LINE>{\BOLD}Operation Record ESS-AG-05<BR>{\RESET}Medium: death water. Reference: valid.<BR>The chamber contained a corrupted life cycle unrelated to the submitted lineage. Roots moved before life instruments registered anything and continued after the reference ceased to count as living.<BR>Status: stable. Biological state unresolved.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-05<BR>{\RESET}介质: 死亡之水. 参照: 有效.<BR>舱室收容了一段与提交谱系无关的腐化生命周期. 根系在生命仪器出现读数前便开始活动, 并在参照不再被视为活物后继续.<BR>状态: 稳定. 生物状态未决.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.5
            // # <LINE>{\BOLD}Operation Record ESS-AG-06<BR>{\RESET}Medium: unknown liquid. Botanical reference: valid.<BR>The recovered signatures matched off-world botanical records rather than the submitted species. During growth, the branches consistently turned toward coordinates that do not correspond to any visible star in this dimension.<BR>Status: cycle stable. External reference not found.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-06<BR>{\RESET}介质: 不明液体. 植物参照: 有效.<BR>回收特征与异星植物记录一致, 而非来自提交的物种. 生长期间, 枝条持续朝向一组无法对应本维度任何可见恒星的坐标.<BR>状态: 循环稳定. 未找到外部参照物.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.6
            // # <LINE>{\BOLD}Operation Record ESS-AG-07<BR>{\RESET}Medium: UU-matter. Reference: valid.<BR>Each selected tree category was drawn independently from the full registry; one cycle could combine unrelated species.<BR>Status: stable. Parent ecology nonexistent.<BR>The archive refuses to call it a forest.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AG-07<BR>{\RESET}介质: UU物质. 参照: 有效.<BR>每个树体类别都独立从完整登记表中抽取, 同一循环可能混合无关物种.<BR>状态: 稳定. 母体生态不存在.<BR>档案拒绝称其为森林.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_EXECUTION_PROTOCOL_2",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereExecutionProtocol2),
                -4,
                -16,
                5,
                GTCMItemList.EcoSphereExecutionProtocol2.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.1")),
                        new ResearchPage(infusionRecipeEcoSphereExecutionProtocol2),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.5")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_2.6")))
                    .setParents("ECO_SPHERE_EXECUTION_PROTOCOL_1")
                    .setConcealed()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("ECO_SPHERE_EXECUTION_PROTOCOL_2", 2);
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_EXECUTION_PROTOCOL_3
            // # Eco-Sphere Execution Protocol: Aquatic Simulation
            // #zh_CN 生态圈执行协议: 水域模拟

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3
            // # A lake needs neither shore nor sky.
            // #zh_CN 湖泊并不需要岸与天空.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This protocol establishes a stable aquatic habitat from distilled water, reproducing registered aquatic life and plants.<BR>One valid sample may direct recovery toward its signature. More copies strengthen that request; an empty interface preserves the natural distribution.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该协议以蒸馏水建立稳定水域, 复现已登记的水生生物与植物.<BR>一种有效样本可使回收偏向对应特征. 更多同类样本会强化请求; 接口为空时维持自然分布.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.3
            // # <LINE>{\BOLD}Operation Record ESS-AS-01<BR>{\RESET}Medium: distilled water. Directional sample: none.<BR>The chamber reproduced the registered aquatic distribution. Greater supplied power increased the total recovery without changing the operating interval. Plants associated with the habitat appeared beside aquatic animals without disturbing either population.<BR>Status: normal.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-01<BR>{\RESET}介质: 蒸馏水. 定向样本: 无.<BR>舱室复现了已登记水域分布. 提高输入功率会增加总回收量, 但不会改变运行间隔. 与水域相关的植物会与水生动物同时出现, 两类群落互不干扰.<BR>状态: 正常.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.4
            // # <LINE>{\BOLD}Operation Record ESS-AS-02<BR>{\RESET}Directional sample: raw fish.<BR>The selected signature dominated subsequent recoveries. Other aquatic signatures remained detectable but were reduced to background noise. Removing the sample restored the former distribution without cleaning or recalibration.<BR>Status: normal. The sample was not consumed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-02<BR>{\RESET}定向样本: 生鱼.<BR>后续回收明显由目标特征主导. 其他水产特征仍可被检测到, 但已降低至背景噪声. 移除样本后, 无需清理或重新校准即可恢复原有分布.<BR>状态: 正常. 样本未被消耗.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.5
            // # <LINE>{\BOLD}Operation Record ESS-AS-03<BR>{\RESET}No unregistered life appeared below the maximum conventional input threshold.<BR>At the first stable setting beyond it, invisible pressure formed and the ordinary population scattered from an empty point.<BR>Status: threshold confirmed. Observation extended.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-03<BR>{\RESET}常规输入的最大阈值内从未出现未登记生命.<BR>首次稳定越界后, 水域形成无形压力, 常规种群短暂避开一处空点.<BR>状态: 临界值已确认. 延长观察.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.6
            // # <LINE>{\BOLD}Operation Record ESS-AS-04<BR>{\RESET}After prolonged operation beyond that boundary, a faint jellyfish-like organism appeared at the empty point. Its aspect response alternated between known life readings and one no instrument could name.<BR>It was absent from the registry and rejected as a target.<BR>Status: isolated. Report sealed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-04<BR>{\RESET}越过边界持续运行后, 空点出现了近似水母的微弱生物. 其源质响应在已知生命读数与无法命名的读数间交替.<BR>登记表中没有该样本, 定向请求也遭拒绝.<BR>状态: 已隔离. 报告封存.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.7
            // # <LINE>{\BOLD}Operation Record ESS-AS-05<BR>{\RESET}An unregistered fluid entered the aquatic line. Before rejection, the chamber detected overlapping photosynthetic and nutrient signatures in visibly empty water.<BR>Restoring distilled water erased them without producing a valid recovery table.<BR>Status: unexpected response. Medium unclassified.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-05<BR>{\RESET}未登记流体进入水域管线. 主机拒绝前, 舱室在空水域中检测到重叠的光合与营养特征.<BR>恢复蒸馏水后读数消失, 未形成有效回收表.<BR>状态: 预期外响应. 介质未分类.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_EXECUTION_PROTOCOL_3",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereExecutionProtocol3),
                2,
                -13,
                5,
                GTCMItemList.EcoSphereExecutionProtocol3.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.1")),
                        new ResearchPage(infusionRecipeEcoSphereExecutionProtocol3),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.5")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.6")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_3.7")))
                    .setParents("ECO_SPHERE_INPUT_INTERFACE")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_EXECUTION_PROTOCOL_4
            // # Eco-Sphere Execution Protocol: Aquatic Simulation II
            // #zh_CN 生态圈执行协议: 水域模拟 II

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4
            // # The water has begun cultivating its own answer.
            // #zh_CN 水正在培育属于自己的答案.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This advanced protocol admits an unfamiliar medium and cultivates life recorded beyond the local environment. Directional samples still work, but results are less predictable than under distilled water.<BR>Several signatures may overlap, and complete biological activity may leave nothing recoverable.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该进阶协议接纳陌生介质, 培育本地环境之外的生命记录. 定向样本仍然有效, 但结果比蒸馏水环境更难预测.<BR>多项特征可能重叠, 完整生物活动也未必留下可回收物质.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.3
            // # <LINE>{\BOLD}Operation Record ESS-AS-06<BR>{\RESET}Medium: unknown liquid. Target: none.<BR>All registered off-world photosynthetic and matrix classes appeared. Incompatible strains shared one volume without competition, while changing chamber capacity produced no consistent shift in distribution or mass.<BR>Status: stable. Population limit unknown.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-06<BR>{\RESET}介质: 不明液体. 目标: 无.<BR>所有已登记异星光合株与基质类别均出现. 不相容株系共处一处却没有竞争; 改变舱室容量也未稳定影响分布或总质量.<BR>状态: 稳定. 种群上限未明.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.4
            // # <LINE>{\BOLD}Operation Record ESS-AS-07<BR>{\RESET}Every registered signature was targeted. The selected organism dominated while others remained as traces. Some cycles returned several species; others nothing.<BR>Empty cycles still consumed full medium and recorded complete biological activity.<BR>Status: expected. Recovery not guaranteed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-07<BR>{\RESET}所有登记特征均完成定向测试. 目标成为主体, 其他生长仅余微量. 部分循环回收数种生物, 部分则为空.<BR>空循环仍消耗完整介质并记录完整活动.<BR>状态: 符合预期. 不保证回收.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.5
            // # <LINE>{\BOLD}Operation Record ESS-AS-08<BR>{\RESET}An off-world photosynthetic sample was sealed without light, nutrients, or medium. Its measured mass grew as its visible area shrank.<BR>Returned to unknown liquid, matrix signatures appeared around it in ledger order, though no matching cells existed inside.<BR>Status: contained.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-08<BR>{\RESET}异星光合样本被隔绝光、营养与介质后, 测得质量仍在增长, 可见面积却持续缩小.<BR>放回不明液体后, 基质特征依照记录次序出现在周围, 样本内部却没有对应细胞.<BR>状态: 收容中.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.6
            // # <LINE>{\BOLD}Operation Record ESS-AS-09<BR>{\RESET}Personnel are forbidden from drinking the medium. The rule remains necessary despite the absence of volunteers admitting the act.<BR>After the notice was posted, the chamber produced an empty cycle and printed the observer roster as its recovery table.<BR>Status: log access restricted.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AS-09<BR>{\RESET}禁止人员饮用该介质. 尽管无人承认进行过此行为, 本条仍有保留必要.<BR>告示张贴后, 舱室完成了一次空循环, 并将观察人员名单打印为回收表.<BR>状态: 日志访问受限.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_EXECUTION_PROTOCOL_4",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereExecutionProtocol4),
                2,
                -15,
                5,
                GTCMItemList.EcoSphereExecutionProtocol4.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.1")),
                        new ResearchPage(infusionRecipeEcoSphereExecutionProtocol4),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.5")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_4.6")))
                    .setParents("ECO_SPHERE_EXECUTION_PROTOCOL_3")
                    .setConcealed()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("ECO_SPHERE_EXECUTION_PROTOCOL_4", 2);
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_EXECUTION_PROTOCOL_5
            // # Eco-Sphere Execution Protocol: Artificial Greenhouse
            // #zh_CN 生态圈执行协议: 人工温室

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_5
            // # Growth, disciplined by glass and calculation.
            // #zh_CN 生长被玻璃与计算驯服.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_5.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This protocol forms a controlled greenhouse from ordinary seeds or registered cultivation samples and enriched fertilizer.<BR>Several species may run together. Duplicate records keep the largest submitted group; ties retain the strongest hereditary profile. Each retained seed consumes its own medium.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该协议以普通种子、已登记培育样本与富集肥料建立受控温室.<BR>数种植物可同时运行. 重复记录优先保留数量最多的一组, 数量相同时保留遗传表现最佳者. 每枚保留种子都会消耗介质.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_5.3
            // # <LINE>{\BOLD}Operation Record ESS-AGH-01<BR>{\RESET}Template: wheat seeds. Medium: enriched fertilizer.<BR>The simulator reconstructed repeated harvests from the ordinary plant model. Reproductive and mature signatures followed the observed behavior rather than a fixed recipe. Raising power increased the expected recovery without changing the operating interval.<BR>Status: normal.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-01<BR>{\RESET}模板: 小麦种子. 介质: 富集肥料.<BR>机器根据普通植物模型重构连续收获. 繁殖阶段与成熟阶段的特征遵循观测行为, 而非固定配方. 提高功率会增加期望回收量, 但不会改变运行间隔.<BR>状态: 正常.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_5.4
            // # <LINE>{\BOLD}Operation Record ESS-AGH-02<BR>{\RESET}A registered alternate seed with no ordinary field block was accepted. Its complete cultivation profile was reproduced, including lower-frequency secondary signatures.<BR>The chamber contained no visible crop during the cycle. Harvest counters advanced normally.<BR>Status: normal. Physical growth stage not required.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-02<BR>{\RESET}一枚没有普通田间方块的已登记替代种子被机器接受. 其完整培育档案得到复现, 包括记录频率较低的次级特征.<BR>循环期间舱室内没有出现可见作物, 收获计数仍正常推进.<BR>状态: 正常. 不要求物理生长阶段.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_5.5
            // # <LINE>{\BOLD}Operation Record ESS-AGH-03<BR>{\RESET}An unregistered seed entered calibration. Though rejected, it briefly projected overlapping plots with conflicting development.<BR>One empty plot persisted after removal and vanished only when power was cut. Nothing was recovered.<BR>Status: unexpected response. Classification incomplete.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-03<BR>{\RESET}未登记种子进入校准后虽被拒绝, 却短暂投影出数块发育互相矛盾的重叠培养区.<BR>样本移除后仍有一块空地残留, 直到断电才消失. 未回收任何事物.<BR>状态: 预期外响应. 分类未完成.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_EXECUTION_PROTOCOL_5",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereExecutionProtocol5),
                -2,
                -13,
                5,
                GTCMItemList.EcoSphereExecutionProtocol5.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_5.1")),
                        new ResearchPage(infusionRecipeEcoSphereExecutionProtocol5),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_5.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_5.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_5.5")))
                    .setParents("ECO_SPHERE_INPUT_INTERFACE")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_EXECUTION_PROTOCOL_6
            // # Eco-Sphere Execution Protocol: Artificial Greenhouse II
            // #zh_CN 生态圈执行协议: 人工温室 II

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_6
            // # The seed remembers more than its species.
            // #zh_CN 种子记得的远比物种更多.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_6.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This advanced protocol reads analyzed hybrid seeds as complete profiles: growth, gain, resistance, and every environmental preference. The chamber must satisfy them all.<BR>Ordinary cultivation samples remain compatible and use their existing records.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该进阶协议将已分析杂交种子的生长、产量、抗性及全部环境喜好视为完整档案, 舱室必须全部满足.<BR>普通培育样本仍可沿用原有记录.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_6.3
            // # <LINE>{\BOLD}Operation Record ESS-AGH-04<BR>{\RESET}Two analyzed seeds of the same crop were tested with different growth values. The faster profile completed a larger fraction of its harvest cycle and produced proportionally more output. Neither profile changed the fixed machine cycle time.<BR>Status: normal. Growth affects simulated progress, not controller speed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-04<BR>{\RESET}使用两枚同种作物但生长数值不同的已分析种子进行测试. 较快的档案在单次中完成了更大比例的收获周期, 产出随之提高. 两种档案都没有改变机器固定循环时间.<BR>状态: 正常. 生长影响模拟进度, 不影响主机速度.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_6.4
            // # <LINE>{\BOLD}Operation Record ESS-AGH-05<BR>{\RESET}Gain was increased while crop and growth remained unchanged. Average harvest rounds rose, and individual recovered stacks occasionally gained additional items. Rare products retained their registered relation to common products.<BR>Status: normal. Gain alters quantity, not identity.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-05<BR>{\RESET}在作物与生长保持不变时提高产量数值. 平均收获轮数上升, 单次回收堆叠也会额外增加物品. 稀有产物与常见产物之间仍保持原登记比例.<BR>状态: 正常. 产量改变数量, 不改变身份.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_6.5
            // # <LINE>{\BOLD}Operation Record ESS-AGH-06<BR>{\RESET}Resistance varied between otherwise identical hybrids. Quantity and composition stayed unchanged, but stronger samples left fewer unstable growth echoes.<BR>One observer's notes changed without recorded contact.<BR>Status: harvest unchanged. Residue under review.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-AGH-06<BR>{\RESET}仅改变相同杂交档案的抗性后, 回收数量与构成未变, 但高抗性样本留下的异常生长回声更少.<BR>一名未接触样本的观察员笔记发生了变化.<BR>状态: 收获未变. 残留审查中.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_EXECUTION_PROTOCOL_6",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereExecutionProtocol6),
                -2,
                -15,
                5,
                GTCMItemList.EcoSphereExecutionProtocol6.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_6.1")),
                        new ResearchPage(infusionRecipeEcoSphereExecutionProtocol6),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_6.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_6.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_6.5")))
                    .setParents("ECO_SPHERE_EXECUTION_PROTOCOL_5")
                    .setConcealed()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("ECO_SPHERE_EXECUTION_PROTOCOL_6", 2);
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_EXECUTION_PROTOCOL_7
            // # Eco-Sphere Execution Protocol: Directed Mob Cloning
            // #zh_CN 生态圈执行协议: 定向克隆

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7
            // # Life reduced to an address.
            // #zh_CN 生命被简化为一个地址.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This protocol attempts directed biological reconstruction. The interface exposes a biological-address field and auxiliary slots, but the basic controller lacks a catalog and cannot complete registered targets.<BR>Blood under the initial address still triggers failed manifestations, leaving an unnamed stable medium.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该协议尝试定向生物重构. 接口会显示生物地址栏与辅助槽, 但基础主机没有清单, 无法完成已登记目标.<BR>初始地址下输入血液仍会触发失败的显现, 并留下一种未命名的稳定介质.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.3
            // # <LINE>{\BOLD}Operation Record ESS-DMC-01<BR>{\RESET}Medium: blood. Address: initial.<BR>Without a destination, blood formed unfinished tissue, folded inward, and dissolved into an equal volume of unfamiliar red fluid. No anatomy or recoverable matter remained.<BR>Status: reconstruction failed. Byproduct retained.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-01<BR>{\RESET}介质: 血液. 地址: 初始.<BR>没有目标时, 血液聚成未完成组织, 向内扭曲并溶解为等量陌生红色液体. 没有留下解剖结构或可回收物质.<BR>状态: 重构失败. 意外产物已留存.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.4
            // # <LINE>{\BOLD}Operation Record ESS-DMC-02<BR>{\RESET}The address field accepted several arbitrary values, but no catalog linked them to targets. Circulation never began, and each trial left identical traces.<BR>Returning to the initial address restored the failed blood process.<BR>Status: address field operational. Catalog source absent.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-02<BR>{\RESET}地址栏接受了多个任意数值, 却没有清单将其关联至目标. 循环从未开始, 各次试验痕迹完全相同.<BR>恢复初始地址后, 失败的血液过程重新出现.<BR>状态: 地址栏正常. 清单来源缺失.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.5
            // # <LINE>{\BOLD}Operation Record ESS-DMC-03<BR>{\RESET}The byproduct was sent to a department known only as Blood Magic. Its staff recognized the sealed sample but would not explain how.<BR>They named it Life Essence, returned handling instructions, and requested controller records and structural scans.<BR>Status: classification provisional. Joint trial approved.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-03<BR>{\RESET}意外产物被送往仅称“血魔法”的部门. 对方在开封前便认出样本, 却拒绝说明原因.<BR>他们称其为生命本源, 附回处理规范, 并索取主机记录与结构扫描.<BR>状态: 暂时接受分类. 联合试验已批准.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.6
            // # <LINE>{\BOLD}Operation Record ESS-DMC-04<BR>{\RESET}Medium: Life Essence. Address: unverified.<BR>The controller recognized the medium, then denied execution before circulation. No tissue or biological reading appeared; the fluid remained untouched.<BR>Blood Magic staff called this proof, without evidence we could observe.<BR>Status: denied. Catalog absent.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-04<BR>{\RESET}介质: 生命本源. 地址: 未验证.<BR>主机识别介质后在循环前拒绝执行. 舱室没有组织或生物读数, 流体也未被触及.<BR>血魔法部门称这足以证明介质正确, 却没有可观测证据.<BR>状态: 已拒绝. 清单缺失.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.7
            // # <LINE>{\BOLD}Operation Record ESS-DMC-05<BR>{\RESET}Structural scans returned a second alignment beneath the known controller layer. When the records were overlaid, incomplete marks appeared and vanished before they could be cataloged.<BR>No one could determine whether they described targets or errors left by the first stage.<BR>Status: source suspected. Recovery deferred.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-05<BR>{\RESET}结构扫描在已知主机记录下方发现了第二层对齐关系. 两份记录重叠后, 数个残缺标记短暂出现, 却在完成登记前消失.<BR>无人能确定它们描述的是目标, 还是第一阶段遗留的错误.<BR>状态: 来源存疑. 暂缓复原.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_EXECUTION_PROTOCOL_7",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereExecutionProtocol7),
                4,
                -14,
                5,
                GTCMItemList.EcoSphereExecutionProtocol7.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.1")),
                        new ResearchPage(infusionRecipeEcoSphereExecutionProtocol7),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.5")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.6")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_7.7")))
                    .setParents("ECO_SPHERE_INPUT_INTERFACE")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_EXECUTION_PROTOCOL_8
            // # Eco-Sphere Execution Protocol: Directed Mob Cloning II
            // #zh_CN 生态圈执行协议: 定向克隆 II

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8
            // # Structure and controller complete a catalog neither could read alone.
            // #zh_CN 结构与主机共同完成了一份两者都无法独自读取的清单.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This Tier II protocol exposes the address catalog recovered from the Tier II structure and calibrated controller. Operators select recorded addresses; they do not create entries. Ordinary addresses use Life Essence to reconstruct aftermath without manifesting a living target.<BR>Non-initial addresses require Tier II. The initial address remains the failed blood process; restricted targets need stronger authorization.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该二级协议会显示由二级结构与校准主机共同复原的地址清单. 操作者只能选择既有地址, 无法编写条目. 普通地址以生命本源重构事后痕迹, 不显现活体目标.<BR>非初始地址需要二级结构. 初始地址仍对应失败的血液过程; 受限目标需要更高授权.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.2
            // # <LINE>{\BOLD}Operation Record ESS-DMC-06<BR>{\RESET}After Tier II calibration, the catalog appeared without new entries. Selecting an ordinary address shaped only a brief outline on the fluid before recovery traces emerged.<BR>The public cache showed one identifying trace; actual recovery followed the full table.<BR>Status: catalog verified.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-06<BR>{\RESET}二级校准后, 清单自行显现, 没有新条目被写入. 选择普通地址后, 液面仅短暂形成轮廓, 随后出现回收痕迹.<BR>公开缓存只显示一项识别痕迹; 实际回收遵循完整表.<BR>状态: 清单已验证.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.3
            // # <LINE>{\BOLD}Operation Record ESS-DMC-07<BR>{\RESET}Recovered equipment traces were cleaned before physical reconstruction. Names, enchantments, and other attached records were removed. Damage records were not reproduced; every equipment item emerged intact, while its underlying identity remained unchanged.<BR>Status: default equipment reconstruction consistent.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-07<BR>{\RESET}回收的装备痕迹会在实体重构前经过清理. 名称、附魔与其他附加记录均被移除. 损伤记录没有被重现; 每件装备都以未破损状态出现, 而装备本身的身份保持不变.<BR>状态: 默认装备重构一致.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.4
            // # <LINE>{\BOLD}Operation Record ESS-DMC-08<BR>{\RESET}Weapons and held conditions were registered through auxiliary slots. Each admitted only associated traces; duplicate properties did not stack, and matching enchantments kept only their highest level.<BR>Some ordinary and equipment traces then recurred beyond any single table. Repeating a property changed nothing.<BR>Status: stable. Residual influence unknown.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-08<BR>{\RESET}武器与持有条件通过辅助槽登记. 各条件只放行对应痕迹; 重复属性不叠加, 相同附魔只取最高等级.<BR>随后部分普通与装备痕迹以单项表无法解释的频率重现. 重复属性没有进一步效果.<BR>状态: 稳定. 残留影响未解.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.5
            // # <LINE>{\BOLD}Operation Record ESS-DMC-09<BR>{\RESET}More power increased Life Essence use and complete histories together, preserving registered proportions. If the medium supported fewer histories, it set the limit.<BR>A restricted address remained unauthorized. One sealed proposal claimed an object without a finite upper boundary might be read as authority and make scarce traces recur.<BR>Status: speculation sealed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-09<BR>{\RESET}提高功率会同步增加生命本源消耗与完整历程, 登记比例不变. 介质能维持的历程较少时, 它会成为上限.<BR>受限地址仍未获授权. 一份封存猜想认为, 不存在有限上界的物件可能被误读为权限, 并令稀少痕迹重现.<BR>状态: 猜想已封存.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_EXECUTION_PROTOCOL_8",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereExecutionProtocol8),
                4,
                -16,
                5,
                GTCMItemList.EcoSphereExecutionProtocol8.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.1")),
                        new ResearchPage(infusionRecipeEcoSphereExecutionProtocol8),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_8.5")))
                    .setParents("ECO_SPHERE_EXECUTION_PROTOCOL_7", "ECO_SPHERE_TIER_TWO")
                    .setConcealed()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("ECO_SPHERE_EXECUTION_PROTOCOL_8", 3);
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_EXECUTION_PROTOCOL_9
            // # Eco-Sphere Execution Protocol: Directed Mob Cloning III
            // #zh_CN 生态圈执行协议: 定向克隆 III

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_9
            // # The specimen no longer accepts the word impossible.
            // #zh_CN 样本不再接受“不可能”这个词.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_9.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This Tier III protocol authorizes biological addresses rejected as too dominant or destructive. They still require the Tier II structure.<BR>It also makes low-frequency ordinary and equipment traces recur more often without adding categories. Ordinary addresses remain available.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该三级协议授权因过于强势或危险而被拒绝的生物地址, 此类目标仍需二级结构.<BR>它也会提高普通与装备记录中低频痕迹的重现机会, 但不增加类别. 普通地址仍可使用.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_9.3
            // # <LINE>{\BOLD}Operation Record ESS-DMC-10<BR>{\RESET}A restricted address passed preparation under Tier II but failed final authorization. Repeating it with Tier III in the same structure allowed execution.<BR>No living target appeared; only its registered aftermath did.<BR>Status: authorization boundary confirmed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-10<BR>{\RESET}受限地址通过二级协议的准备检查, 却未获最终授权. 在同一结构中改用三级协议后, 执行成功.<BR>没有活体目标出现; 只有登记过的事后痕迹.<BR>状态: 授权边界已确认.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_9.4
            // # <LINE>{\BOLD}Operation Record ESS-DMC-11<BR>{\RESET}The same target was compared under Tier II and III. Tier III created no category, but low-frequency traces recurred more often, as if each chance were retried.<BR>The effect followed authorization and did not alter medium demand.<BR>Status: bounded increase confirmed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-11<BR>{\RESET}同一目标分别以二级和三级协议测试. 三级没有增加类别, 但低频痕迹更常重现, 仿佛每次机会都被重新尝试.<BR>该效果在授权后生效, 不改变介质需求.<BR>状态: 有界提升已确认.

            // #tr tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_9.6
            // # <LINE>{\BOLD}Operation Record ESS-DMC-13<BR>{\RESET}The log was sealed after the first restricted reconstruction. During the following cycle, the chamber produced no item output and no entity signature.<BR>The sealed record opened from the inside. Its authorization field now names the observer who ordered the seal.<BR>Status: protocol access revoked pending review.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-DMC-13<BR>{\RESET}首次受限重构后, 日志已经封存. 随后的循环中, 舱室没有产出物品, 也没有记录到实体特征.<BR>封存记录从内部被打开. 其授权栏如今写着下令封存的观察员姓名.<BR>状态: 协议访问权限已撤销, 等待审查.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_EXECUTION_PROTOCOL_9",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereExecutionProtocol9),
                4,
                -18,
                5,
                GTCMItemList.EcoSphereExecutionProtocol9.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_9.1")),
                        new ResearchPage(infusionRecipeEcoSphereExecutionProtocol9),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_9.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_9.4")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_EXECUTION_PROTOCOL_9.6")))
                    .setParents("ECO_SPHERE_EXECUTION_PROTOCOL_8")
                    .setConcealed()
                    .setSpecial()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("ECO_SPHERE_EXECUTION_PROTOCOL_9", 5);
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
            // # <LINE>{\BOLD}Observation Record ESS-OFF-01<BR>{\RESET}The specimen appeared only after the machine crossed the maximum threshold recognized by conventional machinery. Even then, most cycles returned nothing unusual.<BR>Attempts to select it as a directional target produced no response. It can be encountered, but it cannot be requested.<BR>Status: recovery condition confirmed. Probability indeterminate.
            // #zh_CN <LINE>{\BOLD}观察记录 ESS-OFF-01<BR>{\RESET}只有当机器越过常规机械所能识别的最大阈值后, 该样本才曾出现. 即便如此, 绝大多数循环仍未发现异常.<BR>尝试将其设为定向目标时没有得到任何响应. 它可以被遇见, 却无法被索取.<BR>状态: 已确认回收条件. 概率无法确定.

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
                -14,
                10,
                GTCMItemList.OffSpring.get(1, 0))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.OFFSPRING.1")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.OFFSPRING.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.OFFSPRING.3")))
                    .setHidden()
                    .setParents("ECO_SPHERE_EXECUTION_PROTOCOL_3")
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.FOUNT_OF_ECOLOGY
            // # {\BLUE}{\BOLD}Fount of Ecology
            // #zh_CN {\BLUE}{\BOLD}生态泉源

            // #tr tc.research_text.FOUNT_OF_ECOLOGY
            // # A source beneath inheritance.
            // #zh_CN 藏在遗传之下的源头.

            // #tr tc.research_text.FOUNT_OF_ECOLOGY.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}The strange aquatic specimen contains more than the pattern of a single organism. Its structure can be condensed into a stable thaumaturgical source that helps the Eco-Sphere sustain environments beyond ordinary imitation.<BR>The Fount does not create life by itself. It provides the habitat with a reference for what life is allowed to become.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}那份诡异的水域样本所包含的并不只是单一生物的模式. 其结构可以被凝聚为稳定的神秘学源头, 帮助拟似生态圈维持超出普通模仿范围的环境.<BR>泉源本身不会创造生命. 它只是为栖息地提供一份生命被允许成为何物的参照.

            // #tr tc.research_text.FOUNT_OF_ECOLOGY.2
            // # <LINE>{\BOLD}Analysis Record ESS-FE-01<BR>{\RESET}The first stable assembly reacted before the Offspring entered the apparatus. Nearby life-aspect instruments formed a repeating sequence; the Fount returned a pattern absent from the specimen.<BR>Beside the Eco-Sphere plans, decorative notes resolved into containment instructions.<BR>Status: source stable. Relation unresolved.
            // #zh_CN <LINE>{\BOLD}分析记录 ESS-FE-01<BR>{\RESET}首个稳定成品在子代进入仪器前便作出反应. 附近的生命源质仪器排列出重复序列, 泉源则回应了一段样本中不存在的模式.<BR>置于拟似生态圈图纸旁后, 原本用于装饰的注记显现为收容指令.<BR>状态: 源头稳定. 关系未决.

            // #tr tc.research_text.FOUNT_OF_ECOLOGY.3
            // # <LINE>{\BOLD}Analysis Record ESS-FE-02<BR>{\RESET}Repeated comparison found no parent signature, no origin habitat, and no point at which the specimen's pattern could have entered the natural record. The Fount nevertheless recognizes every tested lineage as familiar.<BR>The final sequence indicates that the specimen is not a descendant, but
            // #zh_CN <LINE>{\BOLD}分析记录 ESS-FE-02<BR>{\RESET}反复比对未找到亲本特征、起源环境, 也未找到该样本模式进入自然记录的时间点. 然而, 泉源却将每一条受测谱系都识别为熟悉对象.<BR>最后一段序列说明该样本并非后代, 而是
            // spotless:on
            new ResearchItem(
                "FOUNT_OF_ECOLOGY",
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
                -16,
                10,
                GTCMItemList.FountOfEcology.get(1, 0))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.FOUNT_OF_ECOLOGY.1")),
                        new ResearchPage(infusionRecipeFountOfEcology),
                        new ResearchPage(TSTUtils.tr("tc.research_text.FOUNT_OF_ECOLOGY.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.FOUNT_OF_ECOLOGY.3")))
                    .setParents("OFFSPRING")
                    .setSiblings("ECO_SPHERE_TIER_TWO")
                    .setHidden()
                    .setSpecial()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("FOUNT_OF_ECOLOGY", 6);
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_TIER_TWO
            // # Eco-Sphere Simulator: Tier II Structure
            // #zh_CN 拟似生态圈: 二级结构

            // #tr tc.research_text.ECO_SPHERE_TIER_TWO
            // # The habitat no longer imitates nature. It negotiates with it.
            // #zh_CN 这片环境不再模仿自然, 而是在与自然交涉.

            // #tr tc.research_text.ECO_SPHERE_TIER_TWO.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}The Fount of Ecology reveals how to rebuild the habitat with cleaner, denser components. The reinforced chamber sustains advanced protocols and confines hostile simulations.<BR>For processes available to the basic structure, added power no longer shortens the fixed cycle. It preserves several complete histories in that interval, each consuming its own medium.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}生态泉源揭示了以更洁净、更致密的部件重建栖息地的方法. 强化后的舱室能够承载进阶执行协议, 并收容危险模拟.<BR>执行基础结构可用的流程时, 额外功率不再缩短固定循环. 舱室会在同一间隔内维持数段完整历史, 每段都消耗各自所需的介质.

            // #tr tc.research_text.ECO_SPHERE_TIER_TWO.2
            // # <LINE>{\BOLD}Operation Record ESS-T2-01<BR>{\RESET}A basic protocol ran within the reinforced structure. The visible cycle remained fixed, but its record held several complete environmental histories in the same interval.<BR>Each history consumed its own medium. Reducing the extra supply reduced their number without damaging the rest.<BR>Status: lossless acceleration confirmed. Conservation remains inconvenient.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-T2-01<BR>{\RESET}一项基础协议在强化结构中运行. 可见循环时间保持不变, 但记录中出现了数段占据同一间隔的完整环境历史.<BR>每段历史都消耗各自所需的介质. 减少额外供能后, 历史数量随之减少, 其余部分仍保持完整.<BR>状态: 无损超频已确认. 守恒依然令人不便.

            // #tr tc.research_text.ECO_SPHERE_TIER_TWO.3
            // # <LINE>{\BOLD}Operation Record ESS-T2-02<BR>{\RESET}The chamber was instructed to maintain an advanced habitat whose readings had previously escaped the basic boundary. The reinforced casing returned every hostile pressure inward until the simulation accepted the structure itself as the edge of its world.<BR>After shutdown, one sensor continued reporting that nothing existed beyond the wall.<BR>Status: containment stable. External reality not queried.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-T2-02<BR>{\RESET}舱室被要求维持一种读数曾越过基础边界的进阶栖息地. 强化机械方块将所有危险压力向内折返, 直到模拟将结构本身视为其世界的边缘.<BR>停机后, 仍有一枚传感器持续报告墙外不存在任何事物.<BR>状态: 收容稳定. 未查询外部现实.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_TIER_TWO",
                "TST",
                new AspectList(),
                2,
                -17,
                5,
                GTCMItemList.AsepticGreenhouseCasing.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_TIER_TWO.1")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_TIER_TWO.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_TIER_TWO.3")))
                    .setParents("FOUNT_OF_ECOLOGY")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_UPGRADE_1
            // # Eco-Sphere Upgrade: Fluid Reduction
            // #zh_CN 生态圈升级: 流体减免

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_1
            // # The habitat learns to waste less of what sustains it.
            // #zh_CN 栖息地学会少浪费一些维系自身的介质.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_1.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This upgrade revises how the Eco-Sphere distributes its operating medium. Each installed copy reduces the amount assigned to every complete parallel cycle, and repeated revisions continue from the already reduced value.<BR>The requirement can become very small, but the habitat refuses to describe a process sustained by nothing. At least one measurable unit must remain.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级会修订拟似生态圈分配运行介质的方式. 每安装一枚, 每个完整并行循环所需的份额都会减少, 后续修订则继续作用于已经降低的数值.<BR>需求可以变得很小, 但栖息地拒绝承认一种由虚无维持的过程. 至少必须保留一个可测量单位.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_1.2
            // # <LINE>{\BOLD}Operation Record ESS-UP-FR-01<BR>{\RESET}A single revision was installed during an otherwise unchanged arboreal trial. The same complete growth histories were recovered while each received visibly less medium than before.<BR>Output identity, cycle duration, and power response remained unchanged.<BR>Status: amendment stable.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-FR-01<BR>{\RESET}在其他条件不变的原木试验中安装了一次修订. 每段完整生长历史获分配的介质明显少于此前, 回收内容保持一致.<BR>回收身份、循环时间及功率响应均未改变.<BR>状态: 修订稳定.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_1.3
            // # <LINE>{\BOLD}Operation Record ESS-UP-FR-02<BR>{\RESET}Further copies continued reducing the assigned share until the ledger reached its smallest admissible entry. The next revision was accepted by the interface but produced no further reduction.<BR>The controller annotated the unchanged value with: "A habitat may be economical. It may not be imaginary."<BR>Status: lower boundary confirmed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-FR-02<BR>{\RESET}继续安装副本后, 分配份额持续下降, 直到记录表抵达允许写入的最小值. 下一次修订仍被接口接纳, 却没有继续降低需求.<BR>主机在未变化的数值后留下了注记: “栖息地可以节省, 但不能是想象出来的.”<BR>状态: 下限已确认.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_UPGRADE_1",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereUpgrade1),
                5,
                -8,
                5,
                GTCMItemList.EcoSphereUpgrade1.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_1.1")),
                        new ResearchPage(infusionRecipeEcoSphereUpgrade1),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_1.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_1.3")))
                    .setParents("ECO_SPHERE_UPGRADE_INTERFACE")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_UPGRADE_2
            // # Eco-Sphere Upgrade: Capacity
            // #zh_CN 生态圈升级: 扩容

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_2
            // # More samples remain legible at once.
            // #zh_CN 更多样本得以同时保持可读.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_2.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This upgrade expands the active portion of the input interface. Each installed copy exposes another sample position; arboreal, aquatic, and greenhouse layouts also become capable of retaining a larger amount in each active position.<BR>Cloning auxiliary conditions remain singular records. If an expansion is removed, anything left in positions that can no longer exist is expelled beside the interface rather than sealed behind an inactive display.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级会扩展输入接口的有效区域. 每安装一枚便会开放另一个样本位置; 原木、水域与温室布局中的每个有效位置也会获得更高的容纳能力.<BR>克隆辅助条件仍然保持为单份记录. 若拆除扩容修订, 留在失效位置中的内容会被弹出至接口旁, 而不会封存在无法操作的显示层后.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_2.2
            // # <LINE>{\BOLD}Operation Record ESS-UP-CP-01<BR>{\RESET}The interface unfolded one additional registration position without changing its external dimensions. A greenhouse trial accepted another distinct cultivation record, while an aquatic trial used the same space to reinforce its single directional request.<BR>The controller continued to reject mixed aquatic targets.<BR>Status: expansion stable. Protocol restrictions preserved.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-CP-01<BR>{\RESET}接口在外部尺寸没有变化的情况下展开了一个额外登记位置. 温室试验因此接纳了另一种不同的培育记录, 水域试验则用同一空间强化了唯一的定向请求.<BR>主机仍然拒绝混合的水域目标.<BR>状态: 扩展稳定. 协议限制得到保留.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_2.3
            // # <LINE>{\BOLD}Operation Record ESS-UP-CP-02<BR>{\RESET}An expansion was removed while its outermost position remained occupied. The position darkened, and its contents appeared intact on the floor beside the interface during the same update.<BR>Reinstalling the upgrade restored an empty position. The interface refused the request to remember what it had just expelled.<BR>Status: contraction safe. Automatic recovery confirmed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-CP-02<BR>{\RESET}在最外侧位置仍有内容时拆除了一枚扩容修订. 该位置随即变暗, 其中内容也在同一次更新中完整出现在接口旁的地面上.<BR>重新安装升级后, 恢复的是一个空位置. 接口拒绝了记住刚刚被自己弹出之物的请求.<BR>状态: 收缩安全. 自动回收已确认.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_UPGRADE_2",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereUpgrade2),
                5,
                -6,
                5,
                GTCMItemList.EcoSphereUpgrade2.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_2.1")),
                        new ResearchPage(infusionRecipeEcoSphereUpgrade2),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_2.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_2.3")))
                    .setParents("ECO_SPHERE_UPGRADE_INTERFACE")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_UPGRADE_3
            // # Eco-Sphere Upgrade: Output
            // #zh_CN 生态圈升级: 产量

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_3
            // # The recovered pattern leaves a denser physical trace.
            // #zh_CN 被回收的模式留下了更致密的物质痕迹.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_3.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This upgrade strengthens the conversion from a completed environmental record into recoverable matter. Each installed copy increases the amount left by ordinary item and fluid traces, and multiple copies compound rather than merely repeating the same instruction.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级会强化完整环境记录向可回收物质的转换. 每安装一枚, 普通物品与流体痕迹留下的数量都会提高, 多枚修订会彼此相乘, 而不是简单重复同一条指令.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_3.2
            // # <LINE>{\BOLD}Operation Record ESS-UP-OU-01<BR>{\RESET}Identical cycles were compared before and after the amendment. Every ordinary recovery trace became denser, though small variations remained between individual entries. Their identities and registered relative chances did not change.<BR>Status: output increase stable. Residual variation expected.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-OU-01<BR>{\RESET}对安装修订前后的相同循环进行了比较. 每项普通回收痕迹都变得更为致密, 但不同条目之间仍保留少量波动. 它们的身份与已登记相对机会没有改变.<BR>状态: 产量提升稳定. 残余波动符合预期.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_UPGRADE_3",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereUpgrade3),
                5,
                -10,
                5,
                GTCMItemList.EcoSphereUpgrade3.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_3.1")),
                        new ResearchPage(infusionRecipeEcoSphereUpgrade3),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_3.2")))
                    .setParents("ECO_SPHERE_UPGRADE_INTERFACE")
                    .setConcealed()
                    .registerResearchItem();
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_UPGRADE_4
            // # Eco-Sphere Upgrade: Speed
            // #zh_CN 生态圈升级: 速度

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_4
            // # The habitat folds its operating interval inward.
            // #zh_CN 栖息地将自身的运行间隔向内折叠.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_4.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This upgrade compresses the Eco-Sphere's standard operating cycle into its shortest stable interval. It changes when a completed history is released, not how much power can be converted into parallel histories.<BR>The design is built around a fluid-reduction core and retains one copy of that effect. Additional speed revisions do not shorten the cycle further, although their inherited reduction remains cumulative.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级会将拟似生态圈的标准运行循环压缩至最短稳定间隔. 它改变的是完整历史何时释放, 而不是有多少功率可以转化为并行历史.<BR>其设计以流体减免核心为基础, 因而会保留一份对应效果. 继续安装速度修订不会进一步缩短循环, 但继承的减免仍会累积.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_4.2
            // # <LINE>{\BOLD}Operation Record ESS-UP-SP-01<BR>{\RESET}The controller normally waited through its full standard interval before releasing a completed cycle. With the temporal amendment installed, the same event was reported almost immediately.<BR>Internal records still described a full history. Nothing inside the chamber admitted that any interval was missing.<BR>Status: compression stable. Missing interval unlocated.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-SP-01<BR>{\RESET}主机通常要等待完整的标准间隔才会释放一次循环. 安装时间修订后, 同一事件几乎立刻得到报告.<BR>内部记录仍描述着完整历史. 舱室内没有任何事物承认曾有间隔消失.<BR>状态: 压缩稳定. 缺失间隔无法定位.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_4.3
            // # <LINE>{\BOLD}Operation Record ESS-UP-SP-02<BR>{\RESET}Two copies were installed. Release timing remained fixed at the first compressed interval, but medium use fell again according to the reduction pattern embedded in each assembly.<BR>One clock outside the chamber advanced normally. The matching clock inside displayed the correct time and denied ever containing the intervening ticks.<BR>Status: speed limit confirmed. Secondary effect retained.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-SP-02<BR>{\RESET}安装了两枚副本. 释放时间仍固定在第一次压缩后的间隔, 但介质消耗会按照每个组件内嵌的减免模式再次下降.<BR>舱室外的一枚时钟正常前进. 内部的对应时钟显示正确时间, 却否认自己曾经包含中间的刻度.<BR>状态: 速度上限已确认. 次级效果保留.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_UPGRADE_4",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereUpgrade4),
                7,
                -8,
                5,
                GTCMItemList.EcoSphereUpgrade4.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_4.1")),
                        new ResearchPage(infusionRecipeEcoSphereUpgrade4),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_4.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_4.3")))
                    .setParents("ECO_SPHERE_UPGRADE_1")
                    .setConcealed()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("ECO_SPHERE_UPGRADE_4", 2);
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_UPGRADE_5
            // # Eco-Sphere Upgrade: Blood Orb
            // #zh_CN 生态圈升级: 气血宝珠

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_5
            // # A reservoir beyond the chamber begins to answer.
            // #zh_CN 舱室之外的一处储备开始回应.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_5.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This cloning-only upgrade opens an auxiliary position for a Blood Orb, linking the chamber to its owner's LP network. A bound orb lets the network supply Life Essence before physical fluid is consumed.<BR>During initial blood reconstruction, produced Life Essence returns to that network before any excess is released. Its expansion pattern remains active under other protocols.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级仅对克隆生效. 它开放一个辅助位置, 供气血宝珠连接舱室与所有者的LP网络. 已绑定的宝珠会令网络先于实体流体供应生命本源.<BR>初始血液重构产生的生命本源会优先回灌网络, 多余部分才作为流体释放. 其他协议下气血功能失效, 但扩容效果仍然保留.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_5.2
            // # <LINE>{\BOLD}Operation Record ESS-UP-BO-01<BR>{\RESET}A bound orb was registered beside an ordinary biological address. The physical tank remained untouched while the owner's distant network decreased by the exact biological requirement.<BR>The owner was not present in the facility. Their pulse changed at the moment circulation began.<BR>Status: remote payment confirmed. Medical observation declined.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-BO-01<BR>{\RESET}一枚已绑定宝珠与普通生物地址共同完成登记. 实体储罐保持未被触及, 宝珠所有者的远程网络则减少了与生物需求完全一致的数值.<BR>所有者当时并不在设施内. 循环开始的瞬间, 其脉搏发生了变化.<BR>状态: 远程支付已确认. 医疗观察遭到拒绝.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_5.3
            // # <LINE>{\BOLD}Operation Record ESS-UP-BO-02<BR>{\RESET}The initial blood reconstruction was repeated with the same orb. No red medium reached the output line; the network accepted it before the fluid could acquire a stable position in the chamber.<BR>For three seconds the ledger described the transfer as both repayment and recollection.<BR>Status: return path stable. Ownership terminology under review.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-BO-02<BR>{\RESET}使用同一宝珠重复了初始血液重构. 输出管线没有收到红色介质; 在流体能够于舱室内取得稳定位置前, 网络已经将其接纳.<BR>记录表曾有三秒同时将此次转移描述为“偿还”与“回忆”.<BR>状态: 回灌路径稳定. 所有权术语审查中.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_UPGRADE_5",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereUpgrade5),
                7,
                -6,
                5,
                GTCMItemList.EcoSphereUpgrade5.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_5.1")),
                        new ResearchPage(infusionRecipeEcoSphereUpgrade5),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_5.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_5.3")))
                    .setParents("ECO_SPHERE_UPGRADE_2")
                    .setConcealed()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("ECO_SPHERE_UPGRADE_5", 4);
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_UPGRADE_6
            // # Eco-Sphere Upgrade: Perfect Genetics
            // #zh_CN 生态圈升级: 完美基因

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_6
            // # A specimen is measured against its unrealized ideal.
            // #zh_CN 样本将以其未曾实现的理想形态接受衡量.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_6.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This upgrade replaces selected hereditary measurements with an ideal profile during recovery. With ordinary water it affects Forestry saplings; in the greenhouse, analyzed hybrid seeds. Unanalyzed plants and exceptional arboreal media lack compatible records and remain unchanged.<BR>Built upon an output upgrade, it retains the inherited yield increase even when idealization cannot apply.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级会在回收计算时, 将选定遗传测量值替换为理想档案. 普通水环境下影响林业树苗, 温室中影响已分析的杂交种子. 未分析植物与原木模式的异常介质没有相容记录, 因而不变.<BR>该组件以产量升级为基础, 即使无法应用遗传理想化, 仍保留产量提升.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_6.2
            // # <LINE>{\BOLD}Operation Record ESS-UP-PG-01<BR>{\RESET}A Forestry specimen of modest height, girth, and fertility was submitted under ordinary water. Its species remained unchanged, but the chamber evaluated a taller, broader, more fertile growth history than the sapling could express.<BR>Under an exceptional medium, it showed only the amendment's inherited output increase.<BR>Status: arboreal boundary confirmed.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-PG-01<BR>{\RESET}在普通水环境中提交了一份高度、树围与繁殖能力均较低的林业样本. 物种没有改变, 但舱室评估的生长历史比实体树苗所能表达的形态更高、更宽且更易繁殖.<BR>处于异常介质时, 它只表现出该修订继承的产量提升.<BR>状态: 原木作用边界已确认.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_6.3
            // # <LINE>{\BOLD}Operation Record ESS-UP-PG-02<BR>{\RESET}An analyzed hybrid seed entered the greenhouse with all three recorded dimensions below the simulated ideal. Its species and preferred environments remained unchanged, but growth and gain were calculated from traits it never possessed.<BR>Resistance rose as well, without directly increasing recovery.<BR>Status: idealization stable. Trait source unavailable.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-PG-02<BR>{\RESET}一枚三项记录值均低于模拟理想值的已分析杂交种子进入温室. 其物种与环境喜好不变, 但生长与产量按样本从未拥有的性状计算.<BR>抗性也随之提高, 却没有直接增加回收数量.<BR>状态: 理想化稳定. 性状来源不可用.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_UPGRADE_6",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereUpgrade6),
                7,
                -11,
                5,
                GTCMItemList.EcoSphereUpgrade6.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_6.1")),
                        new ResearchPage(infusionRecipeEcoSphereUpgrade6),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_6.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_6.3")))
                    .setParents("ECO_SPHERE_UPGRADE_3")
                    .setConcealed()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("ECO_SPHERE_UPGRADE_6", 3);
            // spotless:off
            // #tr tc.research_name.ECO_SPHERE_UPGRADE_7
            // # Eco-Sphere Upgrade: Output Pulverization
            // #zh_CN 生态圈升级: 产物粉碎

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_7
            // # Recovered matter is reduced to a more useful truth.
            // #zh_CN 回收物质被还原为更实用的真相.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_7.1
            // # {\BOLD}Purpose and Function<BR>{\RESET}This cloning-only upgrade replaces recognized grindable recoveries with their registered pulverized forms before they leave the chamber. The conversion follows a prepared whitelist: equipment and other mapped matter are reduced, while entries without a known relation remain unchanged.<BR>The assembly is built around an output upgrade and retains that increase in every mode, but its pulverization instruction is ignored outside directed cloning.
            // #zh_CN {\BOLD}用途与功能<BR>{\RESET}该升级仅在克隆模式中将已识别的可粉碎回收内容替换为登记过的粉碎形态, 随后才令其离开舱室. 转换遵循预先准备的白名单: 装备及其他已建立映射的物质会被还原, 没有已知关系的条目则保持原样.<BR>该组件以产量升级为基础构筑, 因而会在所有模式中保留对应提升, 但粉碎指令在定向克隆之外会被忽略.

            // #tr tc.research_text.ECO_SPHERE_UPGRADE_7.2
            // # <LINE>{\BOLD}Operation Record ESS-UP-PV-01<BR>{\RESET}A cloning table containing equipment, flesh, minerals, and several unreadable constructions was processed under the amendment. Every whitelisted entry emerged as its mapped powder or material trace. Unmapped constructions passed through unchanged after the normal removal of attached records.<BR>Status: conversion table stable.
            // #zh_CN <LINE>{\BOLD}运行记录 ESS-UP-PV-01<BR>{\RESET}在该修订下处理了一份同时包含装备、血肉、矿物及数种无法读取构造的克隆表. 每项白名单内容都以对应粉末或材料痕迹出现. 未建立映射的构造则在完成默认附加记录清理后保持原样通过.<BR>状态: 转换表稳定.
            // spotless:on
            new ResearchItem(
                "ECO_SPHERE_UPGRADE_7",
                "TST",
                getResearchAspects(infusionRecipeEcoSphereUpgrade7),
                7,
                -10,
                5,
                GTCMItemList.EcoSphereUpgrade7.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_7.1")),
                        new ResearchPage(infusionRecipeEcoSphereUpgrade7),
                        new ResearchPage(TSTUtils.tr("tc.research_text.ECO_SPHERE_UPGRADE_7.2")))
                    .setParents("ECO_SPHERE_UPGRADE_3")
                    .setConcealed()
                    .registerResearchItem();
            ThaumcraftApi.addWarpToResearch("ECO_SPHERE_UPGRADE_7", 3);
            // spotless:off
            // #tr tc.research_name.EVOLUTIO
            // # Evolutio
            // #zh_CN Evolutio

            // #tr tc.research_text.EVOLUTIO
            // # Change given thaumic form.
            // #zh_CN 被赋予神秘学形态的改变.

            // #tr tc.research_text.EVOLUTIO.1
            // # {\BOLD}Discovery<BR>{\RESET}While examining liquid genetic records, you found a trace that cannot be reduced to Life or Exchange, though it is formed from both. It describes neither a creature nor an inheritance, but the moment a living pattern begins to depart from what was recorded.<BR>This aspect has been named Evolutio: change given thaumic form.
            // #zh_CN {\BOLD}发现<BR>{\RESET}在检视液态遗传记录时, 你发现了一道无法还原为生命或交换的痕迹, 尽管它由二者构成. 它描述的不是某种生物或遗传结果, 而是生命模式开始偏离既有记录的瞬间.<BR>这种源质被命名为 Evolutio: 被赋予神秘学形态的改变.

            // #tr tc.research_text.EVOLUTIO.2
            // # <LINE>{\BOLD}Observation Record ESS-EVO-01<BR>{\RESET}Several genetic samples were compared before and after exposure. None changed immediately. When later placed under unfamiliar conditions, each produced a stable response absent from its original record.<BR>The effect resembles memory, except the remembered condition had not yet been encountered.<BR>Status: aspect stable. Causality unresolved.
            // #zh_CN <LINE>{\BOLD}观察记录 ESS-EVO-01<BR>{\RESET}数份遗传样本在接触该源质前后接受了比对. 它们没有立刻变化, 却在之后进入陌生条件时产生了原始记录中不存在的稳定响应.<BR>这种效果近似记忆, 但被记住的条件当时尚未经历.<BR>状态: 源质稳定. 因果关系未明.
            // spotless:on
            new ResearchItem(
                "EVOLUTIO",
                "TST",
                (new AspectList()).add(EVOLUTION, 1)
                    .add(Aspect.LIFE, 1)
                    .add(Aspect.EXCHANGE, 1),
                -4,
                -18,
                5,
                Mods.Gendustry.isModLoaded() ? GTModHandler.getModItem(Mods.Gendustry.ID, "LiquidDNABucket", 1)
                    : new ItemStack(Items.water_bucket, 1))
                        .setPages(
                            new ResearchPage(TSTUtils.tr("tc.research_text.EVOLUTIO.1")),
                            new ResearchPage((new AspectList()).add(EVOLUTION, 1)),
                            new ResearchPage(TSTUtils.tr("tc.research_text.EVOLUTIO.2")))
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
                5,
                -2,
                5,
                GTCMItemList.BloodyHell.get(1, 0))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.BLOODY_HELL.1")),
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
                    7,
                    -3,
                    5,
                    GTCMItemList.BloodOrbHatch.get(1, 0))
                        .setPages(
                            new ResearchPage(TSTUtils.tr("tc.research_text.BLOOD_HATCH.1")),
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
                7,
                -2,
                5,
                new ItemStack(TstBlocks.TimeBendingSpeedRune))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.TIME_BENDING_SPEED_RUNE.1")),
                        new ResearchPage(infusionRecipeTimeBendingSpeedRune),
                        new ResearchPage(infusionRecipeTimeBendingSpeedRuneTimewood))
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
                    5,
                    4,
                    9,
                    GTCMItemList.IndustrialAlchemyTower.get(1))
                        .setPages(
                            new ResearchPage(TSTUtils.tr("tc.research_text.INDUSTRIAL_ALCHEMY_TOWER.1")),
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
                7,
                4,
                1,
                new ItemStack(TstBlocks.BlockArcaneHole))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.TST_ARCANE_HOLE.1")),
                        new ResearchPage(crucibleRecipeArcaneHole))
                    .setParents("INDUSTRIAL_ALCHEMY_TOWER")
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
                -7,
                -2,
                9,
                GTCMItemList.PrimordialDisjunctus.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.PRIMORDIAL_DISJUNCTUS.1")),
                        new ResearchPage(infusionRecipePrimordialDisjunctus))
                    .setParents("ESSENTIA_DISCRETIZER")
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
            // spotless:on
            new ResearchItem(
                "SKYPIERCER_TOWER",
                "TST",
                new AspectList().merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAGIC, 1)
                    .merge(Aspect.AURA, 1)
                    .merge(Aspect.ENERGY, 1),
                -7,
                0,
                9,
                GTCMItemList.SkypiercerTower.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.SKYPIERCER_TOWER.1")),
                        new ResearchPage(infusionRecipeSkypiercerTower),
                        new ResearchPage(TSTUtils.tr("tc.research_text.SKYPIERCER_TOWER.2")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.SKYPIERCER_TOWER.3")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.SKYPIERCER_TOWER.4")))
                    // 插入图需要如下格式,且大小最好为128*128
                    // <IMG>gtnhcommunitymod:textures/icons/Thaumonomicon/Automation_Diagram_of_the_Skypiercer_Tower.png:0:0:256:256:1</IMG>
                    .setParents("ESSENTIA_DISCRETIZER")
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
                -7,
                2,
                9,
                GTCMItemList.InfusionMaterialDispenser.get(1))
                    .setPages(
                        new ResearchPage(TSTUtils.tr("tc.research_text.INFUSION_MATERIAL_DISPENSER.1")),
                        new ResearchPage(TSTUtils.tr("tc.research_text.INFUSION_MATERIAL_DISPENSER.2")),
                        new ResearchPage(infusionRecipeInfusionMaterialDispenser))
                    .setParents("ESSENTIA_DISCRETIZER")
                    .registerResearchItem();
        }

        if (Config.Enable_EssentiaDiscretizer) {
            // spotless:off
            // #tr tc.research_name.ESSENTIA_DISCRETIZER
            // # Essentia Discretizer
            // #zh_CN 源质离散器

            // #tr tc.research_text.ESSENTIA_DISCRETIZER
            // # Free movement!
            // #zh_CN 自由流动!
            // spotless:on
            new ResearchItem(
                "ESSENTIA_DISCRETIZER",
                "TST",
                new AspectList().merge(Aspect.MECHANISM, 1)
                    .merge(Aspect.MAN, 1)
                    .merge(Aspect.MAGIC, 1)
                    .merge(Aspect.SOUL, 1),
                -5,
                0,
                9,
                BlockEssentiaDiscretizer.stack()).setPages(
                    // spotless:off
                    // #tr tc.research_text.ESSENTIA_DISCRETIZER.0
                    // # As a thaumaturge versed in the art of technology, you have long been vexed by the management of essentia. The properties unveiled upon crystallization are precisely what you seek. Through the study of the crystallizer and the fluid discretizer, and by melding mind with machine, the Essentia Discretizer has come into being!
                    // #zh_CN 作为一名进修过科技的魔法使,你常常为源质发配感到头疼,而源质结晶后所展现的特性正是你所需的,通过对结晶器与流体离散器的研究,配合大脑与电路的控制,源质离散器就此而生!
                    new ResearchPage(TSTUtils.tr("tc.research_text.ESSENTIA_DISCRETIZER.0")),
                    // #tr tc.research_text.ESSENTIA_DISCRETIZER.1
                    // # In the past, the Essentia Discretizer is a container that monitors both item and fluid channels, operating with the highest priority. When either item-based or fluid-based essentia enters, the Discretizer first detects it. If it is indeed essentia, the device inserts it into the corresponding component or container, while simultaneously creating a crystallized essentia as a duplicate that stays synchronized with the original. Conversely, when the crystallized essentia is consumed, the corresponding original essentia undergoes the same consumption process.
                    // #zh_CN 在过去,源质离散器,是一个容器,监听物品与流体信道,且具有最高优先级,当物品源质亦或者流体版源质进入时,首先被离散器检测,如果确实为源质则将其插入至对应的元件或者容器,并且本身创建一份晶化源质作为副本,与其同步变化,反过来,将晶化源质被使用时对应的本体也做一样的消耗行为.
                    new ResearchPage(TSTUtils.tr("tc.research_text.ESSENTIA_DISCRETIZER.1")),
                    // #tr tc.research_text.ESSENTIA_DISCRETIZER.2
                    // # With the update to version 2.9.0, Thaumic Energistics has been refactored. Essentia now has its own native essentia channel and no longer relies on the fluid channel. Therefore, the relationship is now more direct: CrystalEssence on the item channel directly corresponds to native essentia on the essentia channel.
                    // #zh_CN 随着2.9.0版本的更新,神秘能源的源质也做出了重构,源质有原生信道,不再依托流体信道,因此现在更加直接,就是晶化源质对应源质.
                    new ResearchPage(TSTUtils.tr("tc.research_text.ESSENTIA_DISCRETIZER.2")),
                    // spotless:on
                    new ResearchPage(infusionRecipeEssentiaDiscretizer))
                    .setParents("TST_WELCOME")
                    .registerResearchItem();
        }

    }

}
