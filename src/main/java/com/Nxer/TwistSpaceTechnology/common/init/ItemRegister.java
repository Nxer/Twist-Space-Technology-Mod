package com.Nxer.TwistSpaceTechnology.common.init;

import static com.Nxer.TwistSpaceTechnology.common.init.TstItems.MetaItem01;
import static com.Nxer.TwistSpaceTechnology.common.init.TstItems.MetaItemIzumik;
import static com.Nxer.TwistSpaceTechnology.common.init.TstItems.MetaItemRune;
import static com.Nxer.TwistSpaceTechnology.common.init.TstItems.MultiStructuresLinkTool;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.RiseOfDarkFog;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.DSPName;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegister {

    public static void registryItems() {
        Item[] itemsToReg = { MetaItem01, TstItems.ProofOfHeroes, TstItems.ProofOfGods, MultiStructuresLinkTool,

            TstItems.MetaItemRune, TstItems.MetaItemIzumik, TstItems.Yamato };

        for (Item item : itemsToReg) {
            GameRegistry.registerItem(item, item.unlocalizedName);
        }

    }

    // spotless:off
    public static void registryItemContainers() {

        // #tr item.MetaItem01.0.name
        // # Test Item
        // #zh_CN 测试物品
        // #tr tooltips.TestItem0.line1
        // # A test item, no use.
        // #zh_CN A test item, no use.
        GTCMItemList.TestItem0.set(TstUtils.registerItemAdder(MetaItem01,0, new String[]{TextEnums.tr("tooltips.TestItem0.line1")}));

        // #tr item.MetaItem01.1.name
        // # Space Warper
        // #zh_CN 空间翘曲器
        // #tr tooltips.SpaceWarper.line1
        // # Power of gravitation !
        // #zh_CN {\DARK_BLUE}Power of gravitation !
        GTCMItemList.SpaceWarper.set(TstUtils.registerItemAdder(MetaItem01, 1, new String[]{
            TextEnums.tr("tooltips.SpaceWarper.line1")}));

        // #tr item.MetaItem01.2.name
        // # Gravitational Constraint Optical Quantum Crystal
        // #zh_CN 引力约束光量子晶体
        // #tr tooltips.OpticalSOC.line1
        // # These Photons have their own mind.
        // #zh_CN 这些光子有他们自己的想法.
        GTCMItemList.OpticalSOC.set(TstUtils.registerItemAdder(MetaItem01, 2, new String[]{
            TextEnums.tr("tooltips.OpticalSOC.line1")}));

        // #tr item.MetaItem01.3.name
        // # Mold (Singularity)
        // #zh_CN 模具 (奇点)
        // #tr tooltips.MoldSingularity.line1
        // # Mold for making Singularity
        // #zh_CN 用来制作奇点的模具
        GTCMItemList.MoldSingularity.set(TstUtils.registerItemAdder(MetaItem01, 3, new String[]{
            TextEnums.tr("tooltips.MoldSingularity.line1")}));

        // #tr item.MetaItem01.4.name
        // # Particle Trap - SpaceTime Shield
        // #zh_CN 粒子阱 - 时空鞘
        // #tr tooltips.ParticleTrapTimeSpaceShield.line1
        // # Constrain the operator(the photon) to a miniature spacetime.
        // #zh_CN 将算子(光子)限制在一个微型时空中.
        GTCMItemList.ParticleTrapTimeSpaceShield.set(TstUtils.registerItemAdder(MetaItem01, 4, new String[]{
            TextEnums.tr("tooltips.ParticleTrapTimeSpaceShield.line1")}));

        // #tr item.MetaItem01.5.name
        // # Lapotron Shard
        // #zh_CN 兰波顿碎片
        // #tr tooltips.LapotronShard.line1
        // # Even though it's just a shard, the energy fluctuations inside are also visible to the naked eye.
        // #zh_CN 尽管只是一块碎片, 它里面的能量波动也是肉眼可见的.
        GTCMItemList.LapotronShard.set(TstUtils.registerItemAdder(MetaItem01, 5, new String[]{
            TextEnums.tr("tooltips.LapotronShard.line1")}));

        // #tr item.MetaItem01.6.name
        // # Perfect Lapotron Crystal
        // #zh_CN 完美兰波顿水晶
        // #tr tooltips.PerfectLapotronCrystal.line1
        // # Immaculate !
        // #zh_CN 完美无瑕 !
        GTCMItemList.PerfectLapotronCrystal.set(TstUtils.registerItemAdder(MetaItem01, 6, new String[]{
            TextEnums.tr("tooltips.PerfectLapotronCrystal.line1")}));

        // #tr item.MetaItem01.7.name
        // # Energy Crystal Shard
        // #zh_CN 能量水晶碎片
        // #tr tooltips.EnergyCrystalShard.line1
        // # A red crystal shard, doesn't look like anything special.
        // #zh_CN 一块红色的水晶碎片, 看起来没什么特殊的.
        GTCMItemList.EnergyCrystalShard.set(TstUtils.registerItemAdder(MetaItem01, 7, new String[]{
            TextEnums.tr("tooltips.EnergyCrystalShard.line1")}));

        // #tr item.MetaItem01.8.name
        // # Perfect Energy Crystal
        // #zh_CN 完美能量水晶
        // #tr tooltips.PerfectEnergyCrystal.line1
        // # As it grew in size, it displayed incredible traits on energy control.
        // #zh_CN 随着体型变大，它在能量控制方面表现出了不可思议的特性.
        GTCMItemList.PerfectEnergyCrystal.set(TstUtils.registerItemAdder(MetaItem01, 8, new String[]{
            TextEnums.tr("tooltips.PerfectEnergyCrystal.line1")}));

        // #tr item.MetaItem01.9.name
        // # Solar Sail
        // #zh_CN 太阳帆
        // #tr tooltips.SolarSail.line1
        // # Collect and concentrate light energy.
        // #zh_CN 收集并浓缩光能.
        GTCMItemList.SolarSail.set(TstUtils.registerItemAdder(MetaItem01, 9, new String[]{
            TextEnums.tr("tooltips.SolarSail.line1"), DSPName}));

        // #tr item.MetaItem01.10.name
        // # Dyson Sphere Frame Component
        // #zh_CN 戴森球框架部件
        // #tr tooltips.DysonSphereFrameComponent.line1
        // # Stellar gravity can't destroy these structures, even black hole.
        // #zh_CN 恒星的引力无法破坏这些结构, 黑洞也不行.
        GTCMItemList.DysonSphereFrameComponent.set(TstUtils.registerItemAdder(MetaItem01, 10, new String[]{
            TextEnums.tr("tooltips.DysonSphereFrameComponent.line1"), DSPName}));

        // #tr item.MetaItem01.11.name
        // # Small Launch Vehicle
        // #zh_CN 小型运载火箭
        // #tr tooltips.SmallLaunchVehicle.line1
        // # Subtle and sophisticated.
        // #zh_CN 巧妙且精致.
        GTCMItemList.SmallLaunchVehicle.set(TstUtils.registerItemAdder(MetaItem01, 11, new String[]{
            TextEnums.tr("tooltips.SmallLaunchVehicle.line1"), DSPName}));

        // #tr item.MetaItem01.12.name
        // # Empty Small Launch Vehicle
        // #zh_CN 空的小型运载火箭
        // #tr tooltips.EmptySmallLaunchVehicle.line1
        // # Subtle and sophisticated but Empty.
        // #zh_CN 巧妙且精致, 但是是空的.
        GTCMItemList.EmptySmallLaunchVehicle.set(TstUtils.registerItemAdder(MetaItem01, 12, new String[]{
            TextEnums.tr("tooltips.EmptySmallLaunchVehicle.line1"), DSPName}));

        // #tr item.MetaItem01.13.name
        // # Critical Photon
        // #zh_CN 临界光子
        // #tr tooltips.CriticalPhoton.line1
        // # The future has arrived.
        // #zh_CN 未来已至.
        GTCMItemList.CriticalPhoton.set(TstUtils.registerItemAdder(MetaItem01, 13, new String[]{
            TextEnums.tr("tooltips.CriticalPhoton.line1"), DSPName}));

        // #tr item.MetaItem01.14.name
        // # Antimatter
        // #zh_CN 反物质
        // #tr tooltips.Antimatter.line1
        // # The Other Side of Matter.
        // #zh_CN 物质的另一面.
        GTCMItemList.Antimatter.set(TstUtils.registerItemAdder(MetaItem01, 14, new String[]{
            TextEnums.tr("tooltips.Antimatter.line1"), DSPName}));

        // #tr item.MetaItem01.15.name
        // # Annihilation Constrainer
        // #zh_CN 湮灭约束器
        // #tr tooltips.AnnihilationConstrainer.line1
        // # Encourage indirect operation.
        // #zh_CN 鼓励间接操纵.
        GTCMItemList.AnnihilationConstrainer.set(TstUtils.registerItemAdder(MetaItem01, 15, new String[]{
            TextEnums.tr("tooltips.AnnihilationConstrainer.line1"), DSPName}));

        // #tr item.MetaItem01.16.name
        // # Antimatter Fuel Rod
        // #zh_CN 反物质燃料棒
        // #tr tooltips.AntimatterFuelRod.line1
        // # More...
        // #zh_CN More...
        GTCMItemList.AntimatterFuelRod.set(TstUtils.registerItemAdder(MetaItem01, 16, new String[]{
            TextEnums.tr("tooltips.AntimatterFuelRod.line1"), DSPName}));

        // #tr item.MetaItem01.17.name
        // # Stellar Construction Frame Material
        // #zh_CN 恒星结构框架材料
        // #tr tooltips.StellarConstructionFrameMaterial.line1
        // # Perfect and expensive.
        // #zh_CN 完美且昂贵.
        GTCMItemList.StellarConstructionFrameMaterial.set(TstUtils.registerItemAdder(MetaItem01, 17, new String[]{
            TextEnums.tr("tooltips.StellarConstructionFrameMaterial.line1"), DSPName}));

        // #tr item.MetaItem01.18.name
        // # Gravitational Lens
        // #zh_CN 引力透镜
        // #tr tooltips.GravitationalLens.line1
        // # Its twisted and powerful gravitational field is shielded in a container.
        // #zh_CN 其扭曲而强大的引力场被屏蔽在容器中.
        // #tr tooltips.GravitationalLens.line2
        // # It is usually utilized to work and alter spatial structures,
        // #zh_CN 通常会利用它来加工和改变空间结构,
        // #tr tooltips.GravitationalLens.line3
        // # but that doesn't stop some people from taking it and focusing sunlight to light fires for fun.
        // #zh_CN  但也不妨碍有些人会拿它聚焦阳光点火玩.
        GTCMItemList.GravitationalLens.set(TstUtils.registerItemAdder(MetaItem01, 18, new String[]{
            TextEnums.tr("tooltips.GravitationalLens.line1"),
            TextEnums.tr("tooltips.GravitationalLens.line2"),
            TextEnums.tr("tooltips.GravitationalLens.line3"),
            DSPName}));

        // #tr item.MetaItem01.19.name
        // # Purple Magnolia Petal
        // #zh_CN 紫玉兰花瓣
        // #tr tooltips.PurpleMagnoliaPetal.line1
        // # Petals falling from Alfheim...
        // #zh_CN {\ITALIC}{\GRAY}于精灵之乡飘落...
        GTCMItemList.PurpleMagnoliaPetal.set(TstUtils.registerItemAdder(MetaItem01, 19, new String[]{
            TextEnums.tr("tooltips.PurpleMagnoliaPetal.line1")}));

        // #tr item.MetaItem01.20.name
        // # Purple Magnolia Sapling
        // #zh_CN 紫玉兰树苗
        // #tr tooltips.PurpleMagnoliaSapling.line1
        // # Not plantable. Need to be on ic2 crop sticks.
        // #zh_CN 不可种植.需要使用ic2作物架.
        GTCMItemList.PurpleMagnoliaSapling.set(TstUtils.registerItemAdder(MetaItem01, 20, new String[]{
            TextEnums.tr("tooltips.PurpleMagnoliaSapling.line1")}));

        // #tr item.MetaItem01.21.name
        // # Void Pollen
        // #zh_CN 虚空花粉
        // #tr tooltips.VoidPollen.line1
        // # Pollen yet to be arisen.
        // #zh_CN 未有之花的花粉.
        GTCMItemList.VoidPollen.set(TstUtils.registerItemAdder(MetaItem01, 21, new String[]{
            TextEnums.tr("tooltips.VoidPollen.line1")}));

        // #tr item.MetaItem01.22.name
        // # Primitive Man's SpaceTime Distortion Device
        // #zh_CN 原始人的时空扭曲装置
        // #tr tooltips.PrimitiveMansSpaceTimeDistortionDevice.line1
        // # Anyway...
        // #zh_CN 反正吧...
        GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.set(TstUtils.registerItemAdder(MetaItem01, 22, new String[]{
            TextEnums.tr("tooltips.PrimitiveMansSpaceTimeDistortionDevice.line1")}));

        // #tr item.MetaItem01.23.name
        // # Wireless Computation Update Circuit
        // #zh_CN 无线算力网络升级芯片
        GTCMItemList.WirelessUpdateItem.set(TstUtils.registerItemAdder(MetaItem01, 23));

        // #tr item.MetaItem01.24.name
        // # Ball Lightning Upgrade Chip
        // #zh_CN 球状闪电升级芯片
        // #tr tooltips.ItemBallLightningUpgradeChip.line1
        // # Power, give me, more power!
        // #zh_CN {\AQUA}Power, give me, more power!
        GTCMItemList.BallLightningUpgradeChip.set(TstUtils.registerItemAdder(MetaItem01, 24, new String[]{
            TextEnums.tr("tooltips.ItemBallLightningUpgradeChip.line1")}));

        // #tr item.MetaItem01.25.name
        // # Energy Shard
        // #zh_CN 能量碎片
        // #tr EnergyShard.tooltips.01
        // # A piece of pure energy, from dark...
        // #zh_CN 一片纯净的能量, 来自黑暗的...
        GTCMItemList.EnergyShard.set(TstUtils.registerItemAdder(MetaItem01, 25, new String[]{tr("EnergyShard.tooltips.01"), DSPName, RiseOfDarkFog.getText()}));

        // #tr item.MetaItem01.26.name
        // # Silicon-based Neuron
        // #zh_CN 硅基神经元
        // #tr SiliconBasedNeuron.tooltips.01
        // # Very... uh, natural.
        // #zh_CN 非常的... 呃, 自然.
        GTCMItemList.SiliconBasedNeuron.set(TstUtils.registerItemAdder(MetaItem01, 26, new String[]{tr("SiliconBasedNeuron.tooltips.01"), DSPName, RiseOfDarkFog.getText()}));

        // #tr item.MetaItem01.27.name
        // # Matter Recombinator
        // #zh_CN 物质重组器
        // #tr MatterRecombinator.tooltips.01
        // # The fundamental unit of material manipulation at the scale of elementary particles.
        // #zh_CN 基本粒子尺度上物质操作的基本单元.
        GTCMItemList.MatterRecombinator.set(TstUtils.registerItemAdder(MetaItem01, 27, new String[]{tr("MatterRecombinator.tooltips.01"), DSPName, RiseOfDarkFog.getText()}));

        // #tr item.MetaItem01.28.name
        // # Core Element
        // #zh_CN 核心素
        // #tr CoreElement.tooltips.01
        // # Adding core elements to the singularization reaction of strange matter
        // #zh_CN 在奇异物质的奇异化反应中加入核心素,
        // #tr CoreElement.tooltips.02
        // # can slow down the singularization reaction
        // #zh_CN 可以慢化奇异化反应,
        // #tr CoreElement.tooltips.03
        // # and make it proceed in an orderly manner.
        // #zh_CN 使其有序进行.
        GTCMItemList.CoreElement.set(TstUtils.registerItemAdder(MetaItem01, 28, new String[]{tr("CoreElement.tooltips.01"), tr("CoreElement.tooltips.02"), tr("CoreElement.tooltips.03"), DSPName, RiseOfDarkFog.getText()}));

        // #tr item.MetaItem01.29.name
        // # Strange Annihilation Fuel Rod
        // #zh_CN 奇异湮灭燃料棒
        // #tr StrangeAnnihilationFuelRod.tooltips.01
        // # Using strange matter as the main annihilation reaction raw material,
        // #zh_CN 使用了奇异物质为主要的湮灭反应原料,
        // #tr StrangeAnnihilationFuelRod.tooltips.02
        // # the energy density and output power are greatly improved.
        // #zh_CN 能量密度和输出功率均大幅提高.
        GTCMItemList.StrangeAnnihilationFuelRod.set(TstUtils.registerItemAdder(MetaItem01, 29, new String[]{tr("StrangeAnnihilationFuelRod.tooltips.01"), tr("StrangeAnnihilationFuelRod.tooltips.02"), DSPName, RiseOfDarkFog.getText()}));

        // #tr item.MetaItem01.30.name
        // # SpaceTime Superconducting Inlaid Motherboard
        // #zh_CN 时空超导镶嵌主板
        GTCMItemList.SpaceTimeSuperconductingInlaidMotherboard.set(TstUtils.registerItemAdder(MetaItem01, 30));
        // #tr item.MetaItem01.31.name
        // # Packet Information Translation Array
        // #zh_CN 封装信息转译阵列
        GTCMItemList.PacketInformationTranslationArray.set(TstUtils.registerItemAdder(MetaItem01, 31));
        // #tr item.MetaItem01.32.name
        // # Information Horizon Intervention Shell
        // #zh_CN 信系介入外壳
        GTCMItemList.InformationHorizonInterventionShell.set(TstUtils.registerItemAdder(MetaItem01, 32));
        // #tr item.MetaItem01.33.name
        // # Energy Fluctuation Self-Harmonizer
        // #zh_CN 能量涨落自谐调器
        GTCMItemList.EnergyFluctuationSelfHarmonizer.set(TstUtils.registerItemAdder(MetaItem01, 33));
        // #tr item.MetaItem01.34.name
        // # Encapsulated Micro SpaceTime Unit
        // #zh_CN 封装微型时空单元
        GTCMItemList.EncapsulatedMicroSpaceTimeUnit.set(TstUtils.registerItemAdder(MetaItem01, 34));
        // #tr item.MetaItem01.35.name
        // # Seeds of Space and Time
        // #zh_CN 时空之种
        GTCMItemList.SeedsSpaceTime.set(TstUtils.registerItemAdder(MetaItem01, 35));
        // #tr item.MetaItem01.36.name
        // # White Dwarf Mold (Ingot)
        // #zh_CN 白矮星模具(锭)
        GTCMItemList.WhiteDwarfMold_Ingot.set(TstUtils.registerItemAdder(MetaItem01, 36));
        if (Config.activateMegaSpaceStation) {
            // #tr item.MetaItem01.176.name
            // # High-dimensional extend
            // #zh_CN High-dimensional extend
            GTCMItemList.HighDimensionalExtend.set(TstUtils.registerItemAdder(MetaItem01, 176));
            // #tr item.MetaItem01.177.name
            // # High-dimensional circuit board
            // #zh_CN High-dimensional circuit board
            GTCMItemList.HighDimensionalCircuitDoard.set(TstUtils.registerItemAdder(MetaItem01, 177));
            // #tr item.MetaItem01.178.name
            // # High-dimensional capacitor
            // #zh_CN High-dimensional capacitor
            GTCMItemList.HighDimensionalCapacitor.set(TstUtils.registerItemAdder(MetaItem01, 178));
            // #tr item.MetaItem01.179.name
            // # High-dimensional interface
            // #zh_CN High-dimensional interface
            GTCMItemList.HighDimensionalInterface.set(TstUtils.registerItemAdder(MetaItem01, 179));
            // #tr item.MetaItem01.180.name
            // # High-dimensional SMD diode
            // #zh_CN High-dimensional SMD diode
            GTCMItemList.HighDimensionalDiode.set(TstUtils.registerItemAdder(MetaItem01, 180));
            // #tr item.MetaItem01.181.name
            // # High-dimensional Resistor
            // #zh_CN High-dimensional Resistor
            GTCMItemList.HighDimensionalResistor.set(TstUtils.registerItemAdder(MetaItem01, 181));
            // #tr item.MetaItem01.182.name
            // # High-dimensional Transistor
            // #zh_CN High-dimensional Transistor
            GTCMItemList.HighDimensionalTransistor.set(TstUtils.registerItemAdder(MetaItem01, 182));

            // #tr item.MetaItem01.300.name
            // # Cosmic Circuit Board
            // #zh_CN 寰宇电路基板
            GTCMItemList.CosmicCircuitBoard.set(TstUtils.registerItemAdder(MetaItem01, 300));
            // #tr item.MetaItem01.301.name
            // # Intelligent imitation neutron star core
            // #zh_CN 智能仿中子星核心
            GTCMItemList.IntelligentImitationNeutronStarCore.set(TstUtils.registerItemAdder(MetaItem01, 301));
            // #tr item.MetaItem01.302.name
            // # Event Horizon Nano Swarm
            // #zh_CN 事件视界纳米蜂群
            GTCMItemList.EventHorizonNanoSwarm.set(TstUtils.registerItemAdder(MetaItem01, 302));
            // #tr item.MetaItem01.303.name
            // # Micro dimension output
            // #zh_CN 微缩维度输出端
            GTCMItemList.MicroDimensionOutput.set(TstUtils.registerItemAdder(MetaItem01, 303));
            // #tr item.MetaItem01.304.name
            // # Entropy reduction material nanoswarm
            // #zh_CN 熵还原物质纳米蜂群
            GTCMItemList.EntropyReductionMaterialNanoswarm.set(TstUtils.registerItemAdder(MetaItem01, 304));
            // #tr item.MetaItem01.305.name
            // # Transcendent Circuit Board
            // #zh_CN 超时空电路基板
            GTCMItemList.TranscendentCircuitBoard.set(TstUtils.registerItemAdder(MetaItem01, 305));
            // #tr item.MetaItem01.306.name
            // # Narrative layer overwriting device
            // #zh_CN 叙事层覆写装置
            GTCMItemList.NarrativeLayerOverwritingDevice.set(TstUtils.registerItemAdder(MetaItem01, 306));
            // #tr item.MetaItem01.307.name
            // # Hyperspace Narrative Layer Adaptive Special SRA
            // #zh_CN 超时空叙事层适应型特种SRA
            GTCMItemList.HyperspaceNarrativeLayerAdaptiveSpecialSRA.set(TstUtils.registerItemAdder(MetaItem01, 307));
            // #tr item.MetaItem01.308.name
            // # Real Singularity Nano Swarm
            // #zh_CN 奇点蜂群
            GTCMItemList.RealSingularityNanoSwarm.set(TstUtils.registerItemAdder(MetaItem01, 308));
            // #tr item.MetaItem01.309.name
            // # paradox engine
            // #zh_CN 悖论引擎
            GTCMItemList.ParadoxEngine.set(TstUtils.registerItemAdder(MetaItem01, 309));
            // #tr item.MetaItem01.310.name
            // # quasar soc
            // #zh_CN 类星体SOC
            GTCMItemList.QuasarSoc.set(TstUtils.registerItemAdder(MetaItem01, 310));
            // #tr item.MetaItem01.311.name
            // # miniature galaxy
            // #zh_CN 微缩银河
            GTCMItemList.MiniatureGalaxy.set(TstUtils.registerItemAdder(MetaItem01, 311));
            // #tr item.MetaItem01.312.name
            // # self-adaptive AI Ⅰ
            // #zh_CN 第一代自适应全人工智能
            GTCMItemList.Self_adaptiveAI1.set(TstUtils.registerItemAdder(MetaItem01, 312));
            // #tr item.MetaItem01.313.name
            // # self-adaptive AI Ⅱ
            // #zh_CN 第二代自适应全人工智能
            GTCMItemList.Self_adaptiveAI2.set(TstUtils.registerItemAdder(MetaItem01, 313));
            // #tr item.MetaItem01.314.name
            // # self-adaptive AI Ⅲ
            // #zh_CN 第三代自适应全人工智能
            GTCMItemList.Self_adaptiveAI3.set(TstUtils.registerItemAdder(MetaItem01, 314));
            // #tr item.MetaItem01.315.name
            // # self-adaptive AI Ⅳ
            // #zh_CN 第四代自适应全人工智能
            GTCMItemList.Self_adaptiveAI4.set(TstUtils.registerItemAdder(MetaItem01, 315));
            // #tr item.MetaItem01.316.name
            // # self-adaptive AI Ⅴ
            // #zh_CN 第五代自适应全人工智能
            GTCMItemList.Self_adaptiveAI5.set(TstUtils.registerItemAdder(MetaItem01, 316));
            // #tr item.MetaItem01.317.name
            // # core of T800
            // #zh_CN T800核心
            GTCMItemList.CoreOfT800.set(TstUtils.registerItemAdder(MetaItem01, 317));
            // #tr item.MetaItem01.318.name
            // # Exotic Circuit Board
            // #zh_CN 天顶星电路基板
            GTCMItemList.ExoticCircuitBoard.set(TstUtils.registerItemAdder(MetaItem01, 318));
            // #tr item.MetaItem01.319.name
            // # very good item
            // #zh_CN very good item
            GTCMItemList.spaceStationConstructingMaterialMax.set(TstUtils.registerItemAdder(MetaItem01, 319));
            // #tr item.MetaItem01.320.name
            // # Light Quantum Matrix
            // #zh_CN 光量子矩阵
            GTCMItemList.LightQuantumMatrix.set(TstUtils.registerItemAdder(MetaItem01, 320));

            // #tr item.MetaItem01.322.name
            // # Casimir Quantum Fiber
            // #zh_CN 卡西米尔量子纤维
            GTCMItemList.CasimirQuantumFiber.set(TstUtils.registerItemAdder(MetaItem01, 322));
            // #tr item.MetaItem01.323.name
            // # Superstring structure
            // #zh_CN 超弦结构
            GTCMItemList.SuperstringStructure.set(TstUtils.registerItemAdder(MetaItem01, 323));
            // #tr item.MetaItem01.324.name
            // # Dynamic Paradox Body
            // #zh_CN 动力学悖论体
            GTCMItemList.DynamicParadoxBody.set(TstUtils.registerItemAdder(MetaItem01, 324));
            // #tr item.MetaItem01.325.name
            // # Void Prism
            // #zh_CN 虚空棱镜
            GTCMItemList.VoidPrism.set(TstUtils.registerItemAdder(MetaItem01, 325));
            // #tr item.MetaItem01.326.name
            // # Pulsar core
            // #zh_CN 脉冲星核核心
            GTCMItemList.PulsarCore.set(TstUtils.registerItemAdder(MetaItem01, 326));
            // #tr item.MetaItem01.327.name
            // # Star Crystal I
            // #zh_CN 星晶I
            GTCMItemList.StarCrystalI.set(TstUtils.registerItemAdder(MetaItem01, 327));
            // #tr item.MetaItem01.328.name
            // # Super Dimensional Ring
            // #zh_CN 超维环
            GTCMItemList.SuperDimensionalRing.set(TstUtils.registerItemAdder(MetaItem01, 328));
            // #tr item.MetaItem01.329.name
            // # Hyperdimensional expansion
            // #zh_CN 超维度展开
            GTCMItemList.HyperdimensionalExpansion.set(TstUtils.registerItemAdder(MetaItem01, 329));
            // #tr item.MetaItem01.330.name
            // # optical layer
            // #zh_CN 光缘层
            GTCMItemList.OpticalLayer.set(TstUtils.registerItemAdder(MetaItem01, 330));
            // #tr item.MetaItem01.331.name
            // # Magnetic Spin I
            // #zh_CN 磁旋I
            GTCMItemList.MagneticSpinI.set(TstUtils.registerItemAdder(MetaItem01, 331));
            // #tr item.MetaItem01.332.name
            // # star axis
            // #zh_CN 星轴
            GTCMItemList.Staraxis.set(TstUtils.registerItemAdder(MetaItem01, 332));
            // #tr item.MetaItem01.333.name
            // # Boltzmann Brain
            // #zh_CN 玻尔兹曼大脑
            GTCMItemList.BoltzmannBrain.set(TstUtils.registerItemAdder(MetaItem01, 333));
            // #tr item.MetaItem01.334.name
            // # Remnants of the Big Bang
            // #zh_CN 宇宙大爆炸残留
            GTCMItemList.RemnantsOfTheBigBang.set(TstUtils.registerItemAdder(MetaItem01, 334));
            // #tr item.MetaItem01.335.name
            // # Strange Film
            // #zh_CN 奇异片
            GTCMItemList.StrangeFilm.set(TstUtils.registerItemAdder(MetaItem01, 335));
            // #tr item.MetaItem01.336.name
            // # Pulse Manganese
            // #zh_CN 脉冲锰
            GTCMItemList.PulseManganese.set(TstUtils.registerItemAdder(MetaItem01, 336));
            // #tr item.MetaItem01.337.name
            // # Superdimensional Web
            // #zh_CN 超维网
            GTCMItemList.SuperdimensionalWeb.set(TstUtils.registerItemAdder(MetaItem01, 337));
            // #tr item.MetaItem01.338.name
            // # Pinoan Structure
            // #zh_CN 皮诺亚结构
            GTCMItemList.PinoanStructure.set(TstUtils.registerItemAdder(MetaItem01, 338));
            // #tr item.MetaItem01.339.name
            // # Quantum Chain
            // #zh_CN 量子链
            GTCMItemList.QuantumChain.set(TstUtils.registerItemAdder(MetaItem01, 339));
            // #tr item.MetaItem01.340.name
            // # Star Belt
            // #zh_CN 星带
            GTCMItemList.StarBelt.set(TstUtils.registerItemAdder(MetaItem01, 340));
            // #tr item.MetaItem01.341.name
            // # Nanoflow
            // #zh_CN 纳米流
            GTCMItemList.Nanoflow.set(TstUtils.registerItemAdder(MetaItem01, 341));
            // #tr item.MetaItem01.342.name
            // # Space-time layer
            // #zh_CN 时空层
            GTCMItemList.Space_TimeLayer.set(TstUtils.registerItemAdder(MetaItem01, 342));
            // #tr item.MetaItem01.343.name
            // # Superconducting ring
            // #zh_CN 超导环
            GTCMItemList.SuperconductingRing.set(TstUtils.registerItemAdder(MetaItem01, 343));
            // #tr item.MetaItem01.344.name
            // # Quantized Superstring Structure
            // #zh_CN 量子化超弦结构
            GTCMItemList.QuantizedSuperstringStructure.set(TstUtils.registerItemAdder(MetaItem01, 344));
            // #tr item.MetaItem01.345.name
            // # The zero point of vacuum can manifest objects
            // #zh_CN 真空零点能具现物
            GTCMItemList.ThezeroPointOfVacuumCanManifestObjects.set(TstUtils.registerItemAdder(MetaItem01, 345));
            // #tr item.MetaItem01.346.name
            // # Star Core
            // #zh_CN 星核核心
            GTCMItemList.StarCore.set(TstUtils.registerItemAdder(MetaItem01, 346));
            // #tr item.MetaItem01.347.name
            // # quasar remnant
            // #zh_CN 类星体残留
            GTCMItemList.QuasarRemnant.set(TstUtils.registerItemAdder(MetaItem01, 347));
            // #tr item.MetaItem01.348.name
            // # Infinite Divine Machine
            // #zh_CN 无限神机
            GTCMItemList.InfiniteDivineMachine.set(TstUtils.registerItemAdder(MetaItem01, 348));
            // #tr item.MetaItem01.349.name
            // # Original Soup
            // #zh_CN 原始汤
            GTCMItemList.OriginalSoup.set(TstUtils.registerItemAdder(MetaItem01, 349));
            // #tr item.MetaItem01.350.name
            // # Gravity Belt
            // #zh_CN 引力带
            GTCMItemList.GravityBelt.set(TstUtils.registerItemAdder(MetaItem01, 350));
            // #tr item.MetaItem01.351.name
            // # anti-gravity engine
            // #zh_CN 反重力引擎
            GTCMItemList.anti_GravityEngine.set(TstUtils.registerItemAdder(MetaItem01, 351));
            // #tr item.MetaItem01.352.name
            // # Condensed Dark Matter Polymer
            // #zh_CN 浓缩暗物质聚合物
            GTCMItemList.CondensedDarkMatterPolymer.set(TstUtils.registerItemAdder(MetaItem01, 352));
            // #tr item.MetaItem01.353.name
            // # Low Density Dark Matter Polymer
            // #zh_CN 低密度暗物质聚合物
            GTCMItemList.LowDensityDarkMatterPolymer.set(TstUtils.registerItemAdder(MetaItem01, 353));
            // #tr item.MetaItem01.354.name
            // # infinite recursion
            // #zh_CN 无限递归
            GTCMItemList.InfiniteRecursion.set(TstUtils.registerItemAdder(MetaItem01, 354));
            // #tr item.MetaItem01.355.name
            // # Superdimensional Spiral
            // #zh_CN 超维螺旋
            GTCMItemList.SuperdimensionalSpiral.set(TstUtils.registerItemAdder(MetaItem01, 355));
            // #tr item.MetaItem01.356.name
            // # Infinite Divine Machine I
            // #zh_CN 无限神机I
            GTCMItemList.InfiniteDivineMachineI.set(TstUtils.registerItemAdder(MetaItem01, 356));
            // #tr item.MetaItem01.357.name
            // # nuclear axis fluctuation
            // #zh_CN 核轴波动
            GTCMItemList.NuclearaxisFluctuation.set(TstUtils.registerItemAdder(MetaItem01, 357));
            // #tr item.MetaItem01.358.name
            // # Strange fluctuations
            // #zh_CN 奇异波动
            GTCMItemList.StrangeFluctuations.set(TstUtils.registerItemAdder(MetaItem01, 358));
            // #tr item.MetaItem01.359.name
            // # Pulse Copper
            // #zh_CN 脉冲铜
            GTCMItemList.PulseCopper.set(TstUtils.registerItemAdder(MetaItem01, 359));
            // #tr item.MetaItem01.360.name
            // # Dark Matter Crystallization
            // #zh_CN 暗物质结晶
            GTCMItemList.DarkMatterCrystallization.set(TstUtils.registerItemAdder(MetaItem01, 360));
            // #tr item.MetaItem01.361.name
            // # Quantum Core
            // #zh_CN 量子核
            GTCMItemList.QuantumCore.set(TstUtils.registerItemAdder(MetaItem01, 361));
            // #tr item.MetaItem01.362.name
            // # Photon flow
            // #zh_CN 光子流
            GTCMItemList.PhotonFlow.set(TstUtils.registerItemAdder(MetaItem01, 362));
            // #tr item.MetaItem01.363.name
            // # nuclear belt
            // #zh_CN 核带
            GTCMItemList.NuclearBelt.set(TstUtils.registerItemAdder(MetaItem01, 363));
            // #tr item.MetaItem01.364.name
            // # Life Guide
            // #zh_CN 生命导
            GTCMItemList.LifeGuide.set(TstUtils.registerItemAdder(MetaItem01, 364));
            // #tr item.MetaItem01.365.name
            // # Quantized Superstring Structure I
            // #zh_CN 量子化超弦结构I
            GTCMItemList.QuantizedSuperstringStructureI.set(TstUtils.registerItemAdder(MetaItem01, 365));
            // #tr item.MetaItem01.366.name
            // # empty heart
            // #zh_CN 虚空心
            GTCMItemList.EmptyHeart.set(TstUtils.registerItemAdder(MetaItem01, 366));
            // #tr item.MetaItem01.367.name
            // # Star Core Belt
            // #zh_CN 星核带
            GTCMItemList.StarCoreBelt.set(TstUtils.registerItemAdder(MetaItem01, 367));
            // #tr item.MetaItem01.368.name
            // # Space-time Spiral
            // #zh_CN 时空螺旋
            GTCMItemList.Space_TimeSpiral.set(TstUtils.registerItemAdder(MetaItem01, 368));
            // #tr item.MetaItem01.369.name
            // # Magnetic Spin IV
            // #zh_CN 磁旋IV
            GTCMItemList.MagneticSpinIV.set(TstUtils.registerItemAdder(MetaItem01, 369));
            // #tr item.MetaItem01.370.name
            // # Nuclear Fluctuation
            // #zh_CN 核波动
            GTCMItemList.NuclearFluctuation.set(TstUtils.registerItemAdder(MetaItem01, 370));
            // #tr item.MetaItem01.371.name
            // # Celestial Resonance Crystal
            // #zh_CN 天体共鸣水晶
            GTCMItemList.CelestialResonanceCrystal.set(TstUtils.registerItemAdder(MetaItem01, 371));
            // #tr item.MetaItem01.372.name
            // # Low Density Dark Matter Polymer I
            // #zh_CN 低密度暗物质聚合物I
            GTCMItemList.LowDensityDarkMatterPolymerI.set(TstUtils.registerItemAdder(MetaItem01, 372));
            // #tr item.MetaItem01.373.name
            // # Quantum Core
            // #zh_CN 量子核心
            GTCMItemList.QuantumCore.set(TstUtils.registerItemAdder(MetaItem01, 373));
            // #tr item.MetaItem01.374.name
            // # Superdimensional life
            // #zh_CN 超维生命
            GTCMItemList.SuperdimensionalLife.set(TstUtils.registerItemAdder(MetaItem01, 374));
            // #tr item.MetaItem01.375.name
            // # Gravity Fluctuation
            // #zh_CN 引力波动
            GTCMItemList.GravityFluctuation.set(TstUtils.registerItemAdder(MetaItem01, 375));
            // #tr item.MetaItem01.376.name
            // # Light Spiral
            // #zh_CN 光螺旋
            GTCMItemList.LightSpiral.set(TstUtils.registerItemAdder(MetaItem01, 376));
            // #tr item.MetaItem01.377.name
            // # nuclear axis belt
            // #zh_CN 核轴带
            GTCMItemList.NuclearaxisBelt.set(TstUtils.registerItemAdder(MetaItem01, 377));
            // #tr item.MetaItem01.378.name
            // # Superconducting Network
            // #zh_CN 超导网
            GTCMItemList.SuperconductingNetwork.set(TstUtils.registerItemAdder(MetaItem01, 378));
            // #tr item.MetaItem01.379.name
            // # Nanolife
            // #zh_CN 纳米生命
            GTCMItemList.Nanolife.set(TstUtils.registerItemAdder(MetaItem01, 379));
            // #tr item.MetaItem01.380.name
            // # Core of Ancient Creation
            // #zh_CN 上古造物核心
            GTCMItemList.CoreOfAncientCreation.set(TstUtils.registerItemAdder(MetaItem01, 380));
            // #tr item.MetaItem01.381.name
            // # Quantum Heart
            // #zh_CN 量子心
            GTCMItemList.QuantumHeart.set(TstUtils.registerItemAdder(MetaItem01, 381));
            // #tr item.MetaItem01.382.name
            // # fluctuating life
            // #zh_CN 波动生命
            GTCMItemList.FluctuatingLife.set(TstUtils.registerItemAdder(MetaItem01, 382));
            // #tr item.MetaItem01.383.name
            // # Pioneer Remains
            // #zh_CN 先驱者遗骸
            GTCMItemList.PioneerRemains.set(TstUtils.registerItemAdder(MetaItem01, 383));
            // #tr item.MetaItem01.384.name
            // # Life is empty
            // #zh_CN 生命空
            GTCMItemList.LifeIsEmpty.set(TstUtils.registerItemAdder(MetaItem01, 384));
            // #tr item.MetaItem01.385.name
            // # Superstring structure V
            // #zh_CN 超弦结构V
            GTCMItemList.SuperstringStructureV.set(TstUtils.registerItemAdder(MetaItem01, 385));
            // #tr item.MetaItem01.386.name
            // # Superdimensional fluctuations
            // #zh_CN 超维波动
            GTCMItemList.SuperdimensionalFluctuations.set(TstUtils.registerItemAdder(MetaItem01, 386));
            // #tr item.MetaItem01.387.name
            // # Creations from the Outer Universe
            // #zh_CN 外宇宙造物
            GTCMItemList.CreationsFromTheOuterUniverse.set(TstUtils.registerItemAdder(MetaItem01, 387));
            // #tr item.MetaItem01.388.name
            // # Magnetic Vortex
            // #zh_CN 磁旋V
            GTCMItemList.MagneticVortex.set(TstUtils.registerItemAdder(MetaItem01, 388));
            // #tr item.MetaItem01.389.name
            // # Space-time core
            // #zh_CN 时空核
            GTCMItemList.Space_TimeCore.set(TstUtils.registerItemAdder(MetaItem01, 389));
            // #tr item.MetaItem01.390.name
            // # Subspace Heart
            // #zh_CN 子空间心
            GTCMItemList.SubspaceHeart.set(TstUtils.registerItemAdder(MetaItem01, 390));
            // #tr item.MetaItem01.391.name
            // # cosmic expansion effect fluctuations
            // #zh_CN 宇宙膨胀效应波动
            GTCMItemList.CosmicExpansionEffectFluctuations.set(TstUtils.registerItemAdder(MetaItem01, 391));
            // #tr item.MetaItem01.392.name
            // # Star Crystal IV
            // #zh_CN 星晶IV
            GTCMItemList.StarCrystalIV.set(TstUtils.registerItemAdder(MetaItem01, 392));
            // #tr item.MetaItem01.393.name
            // # Infinite Recursive Net
            // #zh_CN 无限递归网
            GTCMItemList.InfiniteRecursiveNet.set(TstUtils.registerItemAdder(MetaItem01, 393));
            // #tr item.MetaItem01.394.name
            // # Superconducting Life Waves
            // #zh_CN 超导生命波动
            GTCMItemList.SuperconductingLifeWaves.set(TstUtils.registerItemAdder(MetaItem01, 394));
            // #tr item.MetaItem01.395.name
            // # gravitational heart
            // #zh_CN 引力心
            GTCMItemList.GravitationalHeart.set(TstUtils.registerItemAdder(MetaItem01, 395));
            // #tr item.MetaItem01.396.name
            // # Celestial Resonance Crystal Spiral
            // #zh_CN 天体共鸣水晶螺旋
            GTCMItemList.CelestialResonanceCrystalSpiral.set(TstUtils.registerItemAdder(MetaItem01, 396));
            // #tr item.MetaItem01.397.name
            // # nuclear axis core
            // #zh_CN 核轴核
            GTCMItemList.NuclearaxisCore.set(TstUtils.registerItemAdder(MetaItem01, 397));
            // #tr item.MetaItem01.398.name
            // # Void Fluctuation
            // #zh_CN 虚空波动
            GTCMItemList.VoidFluctuation.set(TstUtils.registerItemAdder(MetaItem01, 398));
            // #tr item.MetaItem01.399.name
            // # Ancient Creation Fluctuation
            // #zh_CN 上古造物波动
            GTCMItemList.AncientCreationFluctuation.set(TstUtils.registerItemAdder(MetaItem01, 399));
            // #tr item.MetaItem01.400.name
            // # Infinite recursive heart
            // #zh_CN 无限递归心
            GTCMItemList.InfiniteRecursiveHeart.set(TstUtils.registerItemAdder(MetaItem01, 400));
            // #tr item.MetaItem01.401.name
            // # Alien Star Core
            // #zh_CN 异星星核
            GTCMItemList.AlienStarCore.set(TstUtils.registerItemAdder(MetaItem01, 401));
            // #tr item.MetaItem01.402.name
            // # spiral spiral
            // #zh_CN 螺旋螺旋
            GTCMItemList.SpiralSpiral.set(TstUtils.registerItemAdder(MetaItem01, 402));
            // #tr item.MetaItem01.403.name
            // # Magnetic Spin Life
            // #zh_CN 磁旋生命
            GTCMItemList.MagneticSpinLife.set(TstUtils.registerItemAdder(MetaItem01, 403));
            // #tr item.MetaItem01.404.name
            // # Light Waves
            // #zh_CN 光波动
            GTCMItemList.LightWaves.set(TstUtils.registerItemAdder(MetaItem01, 404));
            // #tr item.MetaItem01.405.name
            // # nuclear spiral
            // #zh_CN 核核螺旋
            GTCMItemList.NuclearSpiral.set(TstUtils.registerItemAdder(MetaItem01, 405));
            // #tr item.MetaItem01.406.name
            // # Cosmic expansion effect core
            // #zh_CN 宇宙膨胀效应核
            GTCMItemList.CosmicExpansionEffectCore.set(TstUtils.registerItemAdder(MetaItem01, 406));
            // #tr item.MetaItem01.407.name
            // # Gravity Life
            // #zh_CN 引力生命
            GTCMItemList.GravityLife.set(TstUtils.registerItemAdder(MetaItem01, 407));
            // #tr item.MetaItem01.408.name
            // # Celestial Resonance Crystal Network
            // #zh_CN 天体共鸣水晶网
            GTCMItemList.CelestialResonanceCrystalNetwork.set(TstUtils.registerItemAdder(MetaItem01, 408));
            // #tr item.MetaItem01.409.name
            // # Life in Time and Space
            // #zh_CN 时空生命
            GTCMItemList.LifeInTimeandSpace.set(TstUtils.registerItemAdder(MetaItem01, 409));
            // #tr item.MetaItem01.410.name
            // # Core Axis Life Heart
            // #zh_CN 核轴生命心
            GTCMItemList.CoreAxisLifeHeart.set(TstUtils.registerItemAdder(MetaItem01, 410));
            // #tr item.MetaItem01.411.name
            // # Alien Star Core
            // #zh_CN 异星星核
            GTCMItemList.AlienStarCore.set(TstUtils.registerItemAdder(MetaItem01, 411));
            // #tr item.MetaItem01.412.name
            // # Nano Life Heart
            // #zh_CN 纳米生命心
            GTCMItemList.NanoLifeHeart.set(TstUtils.registerItemAdder(MetaItem01, 412));
            // #tr item.MetaItem01.413.name
            // # Ancient Creation Life
            // #zh_CN 上古造物生命
            GTCMItemList.AncientCreationLife.set(TstUtils.registerItemAdder(MetaItem01, 413));
            // #tr item.MetaItem01.414.name
            // # infinite recursive kernel
            // #zh_CN 无限递归核
            GTCMItemList.InfiniteRecursiveKernel.set(TstUtils.registerItemAdder(MetaItem01, 414));
            // #tr item.MetaItem01.415.name
            // # Superconducting Life Zone
            // #zh_CN 超导生命带
            GTCMItemList.SuperconductingLifeZone.set(TstUtils.registerItemAdder(MetaItem01, 415));
            // #tr item.MetaItem01.416.name
            // # Gravity Life Fluctuation
            // #zh_CN 引力生命波动
            GTCMItemList.GravityLifeFluctuation.set(TstUtils.registerItemAdder(MetaItem01, 416));
            // #tr item.MetaItem01.417.name
            // # Pioneer Remains Life Core
            // #zh_CN 先驱者遗骸生命核心
            GTCMItemList.PioneerRemainsLifeCore.set(TstUtils.registerItemAdder(MetaItem01, 417));
            // #tr item.MetaItem01.418.name
            // # Subspace Fluctuation
            // #zh_CN 子空间波动
            GTCMItemList.SubspaceFluctuation.set(TstUtils.registerItemAdder(MetaItem01, 418));
            // #tr item.MetaItem01.419.name
            // # Space-time life core
            // #zh_CN 时空生命核
            GTCMItemList.Space_TimeLifeCore.set(TstUtils.registerItemAdder(MetaItem01, 419));
        }

        // Endgame Challenge content

        // #tr item.MetaItem01.420.name
        // # LV FLASK
        // #zh_CN LV FLASK
        GTCMItemList.LvFlask.set(TstUtils.registerItemAdder(MetaItem01, 420));
        // #tr item.MetaItem01.421.name
        // # MV FLASK
        // #zh_CN MV FLASK
        GTCMItemList.MvFlask.set(TstUtils.registerItemAdder(MetaItem01, 421));
        // #tr item.MetaItem01.422.name
        // # HV FLASK
        // #zh_CN HV FLASK
        GTCMItemList.HvFlask.set(TstUtils.registerItemAdder(MetaItem01, 422));
        // #tr item.MetaItem01.423.name
        // # EV FLASK
        // #zh_CN EV FLASK
        GTCMItemList.EvFlask.set(TstUtils.registerItemAdder(MetaItem01, 423));
        // #tr item.MetaItem01.424.name
        // # IV FLASK
        // #zh_CN IV FLASK
        GTCMItemList.IvFlask.set(TstUtils.registerItemAdder(MetaItem01, 424));
        // #tr item.MetaItem01.425.name
        // # LUV FLASK
        // #zh_CN LUV FLASK
        GTCMItemList.LuvFlask.set(TstUtils.registerItemAdder(MetaItem01, 425));
        // #tr item.MetaItem01.426.name
        // # ZPM FLASK
        // #zh_CN ZPM FLASK
        GTCMItemList.ZpmFlask.set(TstUtils.registerItemAdder(MetaItem01, 426));
        // #tr item.MetaItem01.427.name
        // # UV FLASK
        // #zh_CN UV FLASK
        GTCMItemList.UvFlask.set(TstUtils.registerItemAdder(MetaItem01, 427));
        // #tr item.MetaItem01.428.name
        // # UHV FLASK
        // #zh_CN UHV FLASK
        GTCMItemList.UhvFlask.set(TstUtils.registerItemAdder(MetaItem01, 428));
        // #tr item.MetaItem01.429.name
        // # UEV FLASK
        // #zh_CN UEV FLASK
        GTCMItemList.UevFlask.set(TstUtils.registerItemAdder(MetaItem01, 429));
        // #tr item.MetaItem01.430.name
        // # UIV FLASK
        // #zh_CN UIV FLASK
        GTCMItemList.UivFlask.set(TstUtils.registerItemAdder(MetaItem01, 430));
        // #tr item.MetaItem01.431.name
        // # UMV FLASK
        // #zh_CN UMV FLASK
        GTCMItemList.UmvFlask.set(TstUtils.registerItemAdder(MetaItem01, 431));
        // #tr item.MetaItem01.432.name
        // # UXV FLASK
        // #zh_CN UXV FLASK
        GTCMItemList.UxvFlask.set(TstUtils.registerItemAdder(MetaItem01, 432));

        // #tr item.MetaItemRune.0.name
        // # Rune of Vigilance
        // #zh_CN 恂戒符文
        // #tr tooltips.Rune_of_Vigilance.line1
        // # Vigilance.
        // #zh_CN {\RED}笼中之鸟从未意识到警戒的重要性。
        GTCMItemList.Rune_of_Vigilance.set(TstUtils.registerVariantMetaItemStackUnsafe(MetaItemRune, 0, new String[]{TextEnums.tr("tooltips.Rune_of_Vigilance.line1")}));
        // #tr item.MetaItemRune.1.name
        // # Rune of Erelong
        // #zh_CN 须臾符文
        // #tr tooltips.Rune_of_Erelong.line1
        // # Erelong.
        // #zh_CN {\DARK_BLUE}永恒不过是一刻的缩影。
        GTCMItemList.Rune_of_Erelong.set(TstUtils.registerVariantMetaItemStackUnsafe(MetaItemRune, 1, new String[]{TextEnums.tr("tooltips.Rune_of_Erelong.line1")}));
        // #tr item.MetaItemRune.2.name
        // # Rune of Ether
        // #zh_CN 以太符文
        // #tr tooltips.Rune_of_Ether.line1
        // # Ether.
        // #zh_CN {\AQUA}对未来最大的慷慨，是把一切献给现在。
        GTCMItemList.Rune_of_Ether.set(TstUtils.registerVariantMetaItemStackUnsafe(MetaItemRune, 2, new String[]{TextEnums.tr("tooltips.Rune_of_Ether.line1")}));
        // #tr item.MetaItemRune.3.name
        // # Rune of Perdition
        // #zh_CN 永劫符文
        // #tr tooltips.Rune_of_Perdition.line1
        // # Perdition.
        // #zh_CN {\DARK_RED}永远不为从未存在的救赎祈祷。
        GTCMItemList.Rune_of_Perdition.set(TstUtils.registerVariantMetaItemStackUnsafe(MetaItemRune, 3, new String[]{TextEnums.tr("tooltips.Rune_of_Perdition.line1")}));

        // #tr item.MetaItemIzumik.0.name
        // # {\BLUE}{\BOLD}Fount Of Ecology
        // #zh_CN {\BLUE}{\BOLD}生态泉源
        // #tr FountOfEcology.tooltips.01
        // # {\AQUA}A unique looking jellyfish
        // #zh_CN {\AQUA}一只长相奇特的水母
        // #tr FountOfEcology.tooltips.02
        // # {\AQUA}Well......
        // #zh_CN {\AQUA}等下......
        // #tr FountOfEcology.tooltips.03
        // # {\GOLD}A perfect creature close to the singularity of evolution, The counselor and lear of The Many.
        // #zh_CN {\GOLD}临近进化奇点的完美生物 大群的建言者与引航者
        // #tr FountOfEcology.tooltips.04
        // # {\GOLD}"The Afterborn Firstborn", Seaborn
        // #zh_CN {\GOLD}"后生的出初生" 海嗣
        GTCMItemList.FountOfEcology.set(TstUtils.registerVariantMetaItemStackWithAdvancedTooltipsUnsafe(
            MetaItemIzumik, 0,
            new String[]{tr("FountOfEcology.tooltips.01"), tr("FountOfEcology.tooltips.02")},
            new String[]{tr("FountOfEcology.tooltips.03"), tr("FountOfEcology.tooltips.04")}
        ));

        // #tr item.MetaItemIzumik.1.name
        // # {\DARK_AQUA}"Offspring"
        // #zh_CN {\DARK_AQUA}"子代"
        // #tr Offspring.tooltips.01
        // # {\AQUA}A weak little jellyfish
        // #zh_CN {\AQUA}一只弱不禁风的小水母
        // #tr Offspring.tooltips.02
        // # {\AQUA}Seems to be containing additional information
        // #zh_CN {\AQUA}似乎包含着额外的信息
        // #tr Offspring.tooltips.03
        // # {\LIGHT_PURPLE}The offspring derived from Izu'mik's evolutionary branches
        // #zh_CN {\LIGHT_PURPLE}伊祖米克进化分支中衍生出的子代
        // #tr Offspring.tooltips.04
        // # {\LIGHT_PURPLE}are returning to their parent with the genetic information collected from all of The Overworld
        // #zh_CN {\LIGHT_PURPLE}正携带着从主世界各地收集到的遗传信息返回母体
        GTCMItemList.OffSpring.set(TstUtils.registerVariantMetaItemStackWithAdvancedTooltipsUnsafe(
            MetaItemIzumik, 1,
            new String[]{tr("Offspring.tooltips.01"), tr("Offspring.tooltips.02")},
            new String[]{tr("Offspring.tooltips.03"), tr("Offspring.tooltips.04")}
        ));

        GTCMItemList.ProofOfGods.set(new ItemStack(TstItems.ProofOfGods, 1));
        GTCMItemList.ProofOfHeroes.set(new ItemStack(TstItems.ProofOfHeroes, 2));
//        GTCMItemList.PowerChair.set(new ItemStack(TstItems.PowerChair, 1));

        GTCMItemList.HatchUpdateTool.set(new ItemStack(TstItems.HatchUpdateTool, 1));

        GTCMItemList.Yamato.set(new ItemStack(TstItems.Yamato, 1));

    }

    // spotless:on
    public static void registry() {
        registryItems();
        registryItemContainers();
    }
}
