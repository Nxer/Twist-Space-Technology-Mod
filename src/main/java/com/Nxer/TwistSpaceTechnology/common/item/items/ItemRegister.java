package com.Nxer.TwistSpaceTechnology.common.item.items;

import static com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdder01.initItem01;
import static com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdderIzumik.initItemIzumik;
import static com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdderRune.initItemRune;
import static com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems.MultiStructuresLinkTool;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.RiseOfDarkFog;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.DSPName;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegister {

    public static void registryItems() {
        Item[] itemsToReg = { BasicItems.MetaItem01, BasicItems.ProofOfHeroes, BasicItems.ProofOfGods,
            MultiStructuresLinkTool,

            BasicItems.MetaItemRune, BasicItems.MetaItemIzumik, BasicItems.PowerChair, BasicItems.HatchUpdateTool,
            BasicItems.Yamato };

        for (Item item : itemsToReg) {
            GameRegistry.registerItem(item, item.getUnlocalizedName());
        }

    }

    // spotless:off
    public static void registryItemContainers() {

        // #tr tooltips.TestItem0.line1
        // # A test item, no use.
        // #zh_CN A test item, no use.
        GTCMItemList.TestItem0.set(initItem01("Test Item 0", 0, new String[]{
            TextEnums.tr("tooltips.TestItem0.line1")}));

        // #tr tooltips.SpaceWarper.line1
        // # Power of gravitation !
        // #zh_CN {\DARK_BLUE}Power of gravitation !
        GTCMItemList.SpaceWarper.set(initItem01("Space Warper", 1, new String[]{
            TextEnums.tr("tooltips.SpaceWarper.line1")}));

        // #tr tooltips.OpticalSOC.line1
        // # These Photons have their own mind.
        // #zh_CN 这些光子有他们自己的想法.
        GTCMItemList.OpticalSOC.set(initItem01("Gravitational Constraint Optical Quantum Crystal", 2, new String[]{
            TextEnums.tr("tooltips.OpticalSOC.line1")}));

        // #tr tooltips.MoldSingularity.line1
        // # Mold for making Singularity
        // #zh_CN 用来制作奇点的模具
        GTCMItemList.MoldSingularity.set(initItem01("Mold(Singularity)", 3, new String[]{
            TextEnums.tr("tooltips.MoldSingularity.line1")}));

        // #tr tooltips.ParticleTrapTimeSpaceShield.line1
        // # Constrain the operator(the photon) to a miniature spacetime.
        // #zh_CN 将算子(光子)限制在一个微型时空中.
        GTCMItemList.ParticleTrapTimeSpaceShield.set(initItem01("Particle Trap - SpaceTime Shield", 4, new String[]{
            TextEnums.tr("tooltips.ParticleTrapTimeSpaceShield.line1")}));

        // #tr tooltips.LapotronShard.line1
        // # Even though it's just a shard, the energy fluctuations inside are also visible to the naked eye.
        // #zh_CN 尽管只是一块碎片, 它里面的能量波动也是肉眼可见的.
        GTCMItemList.LapotronShard.set(initItem01("Lapotron Shard", 5, new String[]{
            TextEnums.tr("tooltips.LapotronShard.line1")}));

        // #tr tooltips.PerfectLapotronCrystal.line1
        // # Immaculate !
        // #zh_CN 完美无瑕 !
        GTCMItemList.PerfectLapotronCrystal.set(initItem01("Perfect Lapotron Crystal", 6, new String[]{
            TextEnums.tr("tooltips.PerfectLapotronCrystal.line1")}));

        // #tr tooltips.EnergyCrystalShard.line1
        // # A red crystal shard, doesn't look like anything special.
        // #zh_CN 一块红色的水晶碎片, 看起来没什么特殊的.
        GTCMItemList.EnergyCrystalShard.set(initItem01("Energy Crystal Shard", 7, new String[]{
            TextEnums.tr("tooltips.EnergyCrystalShard.line1")}));

        // #tr tooltips.PerfectEnergyCrystal.line1
        // # As it grew in size, it displayed incredible traits on energy control.
        // #zh_CN 随着体型变大，它在能量控制方面表现出了不可思议的特性.
        GTCMItemList.PerfectEnergyCrystal.set(initItem01("Perfect Energy Crystal", 8, new String[]{
            TextEnums.tr("tooltips.PerfectEnergyCrystal.line1")}));

        // #tr tooltips.SolarSail.line1
        // # Collect and concentrate light energy.
        // #zh_CN 收集并浓缩光能.
        GTCMItemList.SolarSail.set(initItem01("Solar Sail", 9, new String[]{
            TextEnums.tr("tooltips.SolarSail.line1"), DSPName}));

        // #tr tooltips.DysonSphereFrameComponent.line1
        // # Stellar gravity can't destroy these structures, even black hole.
        // #zh_CN 恒星的引力无法破坏这些结构, 黑洞也不行.
        GTCMItemList.DysonSphereFrameComponent.set(initItem01("Dyson Sphere Frame Component", 10, new String[]{
            TextEnums.tr("tooltips.DysonSphereFrameComponent.line1"), DSPName}));

        // #tr tooltips.SmallLaunchVehicle.line1
        // # Subtle and sophisticated.
        // #zh_CN 巧妙且精致.
        GTCMItemList.SmallLaunchVehicle.set(initItem01("Small Launch Vehicle", 11, new String[]{
            TextEnums.tr("tooltips.SmallLaunchVehicle.line1"), DSPName}));

        // #tr tooltips.EmptySmallLaunchVehicle.line1
        // # Subtle and sophisticated but Empty.
        // #zh_CN 巧妙且精致, 但是是空的.
        GTCMItemList.EmptySmallLaunchVehicle.set(initItem01("Empty Small Launch Vehicle", 12, new String[]{
            TextEnums.tr("tooltips.EmptySmallLaunchVehicle.line1"), DSPName}));

        // #tr tooltips.CriticalPhoton.line1
        // # The future has arrived.
        // #zh_CN 未来已至.
        GTCMItemList.CriticalPhoton.set(initItem01("Critical Photon", 13, new String[]{
            TextEnums.tr("tooltips.CriticalPhoton.line1"), DSPName}));

        // #tr tooltips.Antimatter.line1
        // # The Other Side of Matter.
        // #zh_CN 物质的另一面.
        GTCMItemList.Antimatter.set(initItem01("Antimatter", 14, new String[]{
            TextEnums.tr("tooltips.Antimatter.line1"), DSPName}));

        // #tr tooltips.AnnihilationConstrainer.line1
        // # Encourage indirect operation.
        // #zh_CN 鼓励间接操纵.
        GTCMItemList.AnnihilationConstrainer.set(initItem01("Annihilation Constrainer", 15, new String[]{
            TextEnums.tr("tooltips.AnnihilationConstrainer.line1"), DSPName}));

        // #tr tooltips.AntimatterFuelRod.line1
        // # More...
        // #zh_CN More...
        GTCMItemList.AntimatterFuelRod.set(initItem01("Antimatter Fuel Rod", 16, new String[]{
            TextEnums.tr("tooltips.AntimatterFuelRod.line1"), DSPName}));

        // #tr tooltips.StellarConstructionFrameMaterial.line1
        // # Perfect and expensive.
        // #zh_CN 完美且昂贵.
        GTCMItemList.StellarConstructionFrameMaterial.set(initItem01("Stellar Construction Frame Material", 17, new String[]{
            TextEnums.tr("tooltips.StellarConstructionFrameMaterial.line1"), DSPName}));

        // #tr tooltips.GravitationalLens.line1
        // # Its twisted and powerful gravitational field is shielded in a container.
        // #zh_CN 其扭曲而强大的引力场被屏蔽在容器中.
        // #tr tooltips.GravitationalLens.line2
        // # It is usually utilized to work and alter spatial structures,
        // #zh_CN 通常会利用它来加工和改变空间结构,
        // #tr tooltips.GravitationalLens.line3
        // # but that doesn't stop some people from taking it and focusing sunlight to light fires for fun.
        // #zh_CN  但也不妨碍有些人会拿它聚焦阳光点火玩.
        GTCMItemList.GravitationalLens.set(initItem01("Gravitational Lens", 18, new String[]{
            TextEnums.tr("tooltips.GravitationalLens.line1"),
            TextEnums.tr("tooltips.GravitationalLens.line2"),
            TextEnums.tr("tooltips.GravitationalLens.line3"),
            DSPName}));

        // #tr tooltips.PurpleMagnoliaPetal.line1
        // # Petals falling from Alfheim...
        // #zh_CN {\ITALIC}{\GRAY}于精灵之乡飘落...
        GTCMItemList.PurpleMagnoliaPetal.set(initItem01("Purple Magnolia Petal", 19, new String[]{
            TextEnums.tr("tooltips.PurpleMagnoliaPetal.line1")}));

        // #tr tooltips.PurpleMagnoliaSapling.line1
        // # Not plantable. Need to be on ic2 crop sticks.
        // #zh_CN 不可种植.需要使用ic2作物架.
        GTCMItemList.PurpleMagnoliaSapling.set(initItem01("Purple Magnolia Sapling", 20, new String[]{
            TextEnums.tr("tooltips.PurpleMagnoliaSapling.line1")}));

        // #tr tooltips.VoidPollen.line1
        // # Pollen yet to be arisen.
        // #zh_CN 未有之花的花粉.
        GTCMItemList.VoidPollen.set(initItem01("Void Pollen", 21, new String[]{
            TextEnums.tr("tooltips.VoidPollen.line1")}));

        // #tr tooltips.PrimitiveMansSpaceTimeDistortionDevice.line1
        // # Anyway...
        // #zh_CN 反正吧...
        GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.set(initItem01("Primitive Man's SpaceTime Distortion Device", 22, new String[]{
            TextEnums.tr("tooltips.PrimitiveMansSpaceTimeDistortionDevice.line1")}));

        GTCMItemList.WirelessUpdateItem.set(initItem01("Wireless Computation update circuit", 23, new String[]{}));

        // #tr tooltips.ItemBallLightningUpgradeChip.line1
        // # Power, give me, more power!
        // #zh_CN {\AQUA}Power, give me, more power!
        GTCMItemList.BallLightningUpgradeChip.set(initItem01("Ball Lightning Upgrade Chip", 24, new String[]{
            TextEnums.tr("tooltips.ItemBallLightningUpgradeChip.line1")}));

        // #tr EnergyShard.tooltips.01
        // # A piece of pure energy, from dark...
        // #zh_CN 一片纯净的能量, 来自黑暗的...
        GTCMItemList.EnergyShard.set(initItem01("Energy Shard", 25, new String[]{tr("EnergyShard.tooltips.01"), DSPName, RiseOfDarkFog.getText()}));

        // #tr SiliconBasedNeuron.tooltips.01
        // # Very... uh, natural.
        // #zh_CN 非常的... 呃, 自然.
        GTCMItemList.SiliconBasedNeuron.set(initItem01("Silicon-based Neuron", 26, new String[]{tr("SiliconBasedNeuron.tooltips.01"), DSPName, RiseOfDarkFog.getText()}));

        // #tr MatterRecombinator.tooltips.01
        // # The fundamental unit of material manipulation at the scale of elementary particles.
        // #zh_CN 基本粒子尺度上物质操作的基本单元.
        GTCMItemList.MatterRecombinator.set(initItem01("Matter Recombinator", 27, new String[]{tr("MatterRecombinator.tooltips.01"), DSPName, RiseOfDarkFog.getText()}));

        // #tr CoreElement.tooltips.01
        // # Adding core elements to the singularization reaction of strange matter
        // #zh_CN 在奇异物质的奇异化反应中加入核心素,
        // #tr CoreElement.tooltips.02
        // # can slow down the singularization reaction
        // #zh_CN 可以慢化奇异化反应,
        // #tr CoreElement.tooltips.03
        // # and make it proceed in an orderly manner.
        // #zh_CN 使其有序进行.
        GTCMItemList.CoreElement.set(initItem01("Core Element", 28, new String[]{tr("CoreElement.tooltips.01"), tr("CoreElement.tooltips.02"), tr("CoreElement.tooltips.03"), DSPName, RiseOfDarkFog.getText()}));

        // #tr StrangeAnnihilationFuelRod.tooltips.01
        // # Using strange matter as the main annihilation reaction raw material,
        // #zh_CN 使用了奇异物质为主要的湮灭反应原料,
        // #tr StrangeAnnihilationFuelRod.tooltips.02
        // # the energy density and output power are greatly improved.
        // #zh_CN 能量密度和输出功率均大幅提高.
        GTCMItemList.StrangeAnnihilationFuelRod.set(initItem01("Strange Annihilation Fuel Rod", 29, new String[]{tr("StrangeAnnihilationFuelRod.tooltips.01"), tr("StrangeAnnihilationFuelRod.tooltips.02"), DSPName, RiseOfDarkFog.getText()}));

        // #tr item.MetaItem01.30.name
        // # SpaceTime Superconducting Inlaid Motherboard
        // #zh_CN 时空超导镶嵌主板
        GTCMItemList.SpaceTimeSuperconductingInlaidMotherboard.set(initItem01("SpaceTime Superconducting Inlaid Motherboard", 30));
        // #tr item.MetaItem01.31.name
        // # Packet Information Translation Array
        // #zh_CN 封装信息转译阵列
        GTCMItemList.PacketInformationTranslationArray.set(initItem01("Packet Information Translation Array", 31));
        // #tr item.MetaItem01.32.name
        // # Information Horizon Intervention Shell
        // #zh_CN 信系介入外壳
        GTCMItemList.InformationHorizonInterventionShell.set(initItem01("Information Horizon Intervention Shell", 32));
        // #tr item.MetaItem01.33.name
        // # Energy Fluctuation Self-Harmonizer
        // #zh_CN 能量涨落自谐调器
        GTCMItemList.EnergyFluctuationSelfHarmonizer.set(initItem01("Energy Fluctuation Self-Harmonizer", 33));
        // #tr item.MetaItem01.34.name
        // # Encapsulated Micro SpaceTime Unit
        // #zh_CN 封装微型时空单元
        GTCMItemList.EncapsulatedMicroSpaceTimeUnit.set(initItem01("Encapsulated Micro SpaceTime Unit", 34));
        // #tr item.MetaItem01.35.name
        // # Seeds of Space and Time
        // #zh_CN 时空之种
        GTCMItemList.SeedsSpaceTime.set(initItem01("Seeds of Space and Time", 35));
        // #tr item.MetaItem01.36.name
        // # White Dwarf Mold (Ingot)
        // #zh_CN 白矮星模具(锭)
        GTCMItemList.WhiteDwarfMold_Ingot.set(initItem01("White Dwarf Mold(Ingot)", 36));
        if (Config.activateMegaSpaceStation) {
            // #tr item.MetaItem01.176.name
            // # High-dimensional extend
            // #zh_CN High-dimensional extend
            GTCMItemList.HighDimensionalExtend.set(initItem01("High-dimensional extend", 176));
            // #tr item.MetaItem01.177.name
            // # High-dimensional circuit board
            // #zh_CN High-dimensional circuit board
            GTCMItemList.HighDimensionalCircuitDoard.set(initItem01("High-dimensional circuit board", 177));
            // #tr item.MetaItem01.178.name
            // # High-dimensional capacitor
            // #zh_CN High-dimensional capacitor
            GTCMItemList.HighDimensionalCapacitor.set(initItem01("High-dimensional capacitor", 178));
            // #tr item.MetaItem01.179.name
            // # High-dimensional interface
            // #zh_CN High-dimensional interface
            GTCMItemList.HighDimensionalInterface.set(initItem01("High-dimensional interface", 179));
            // #tr item.MetaItem01.180.name
            // # High-dimensional SMD diode
            // #zh_CN High-dimensional SMD diode
            GTCMItemList.HighDimensionalDiode.set(initItem01("High-dimensional SMD diode", 180));
            // #tr item.MetaItem01.181.name
            // # High-dimensional Resistor
            // #zh_CN High-dimensional Resistor
            GTCMItemList.HighDimensionalResistor.set(initItem01("High-dimensional Resistor", 181));
            // #tr item.MetaItem01.182.name
            // # High-dimensional Transistor
            // #zh_CN High-dimensional Transistor
            GTCMItemList.HighDimensionalTransistor.set(initItem01("High-dimensional Transistor", 182));

            // #tr item.MetaItem01.300.name
            // # Cosmic Circuit Board
            // #zh_CN 寰宇电路基板
            GTCMItemList.CosmicCircuitBoard.set(initItem01("Cosmic Circuit Board", 300));
            // #tr item.MetaItem01.301.name
            // # Intelligent imitation neutron star core
            // #zh_CN 智能仿中子星核心
            GTCMItemList.IntelligentImitationNeutronStarCore.set(initItem01("Intelligent imitation neutron star core", 301));
            // #tr item.MetaItem01.302.name
            // # Event Horizon Nano Swarm
            // #zh_CN 事件视界纳米蜂群
            GTCMItemList.EventHorizonNanoSwarm.set(initItem01("Event Horizon Nano Swarm", 302));
            // #tr item.MetaItem01.303.name
            // # Micro dimension output
            // #zh_CN 微缩维度输出端
            GTCMItemList.MicroDimensionOutput.set(initItem01("Micro dimension output", 303));
            // #tr item.MetaItem01.304.name
            // # Entropy reduction material nanoswarm
            // #zh_CN 熵还原物质纳米蜂群
            GTCMItemList.EntropyReductionMaterialNanoswarm.set(initItem01("Entropy reduction material nanoswarm", 304));
            // #tr item.MetaItem01.305.name
            // # Transcendent Circuit Board
            // #zh_CN 超时空电路基板
            GTCMItemList.TranscendentCircuitBoard.set(initItem01("Transcendent Circuit Board", 305));
            // #tr item.MetaItem01.306.name
            // # Narrative layer overwriting device
            // #zh_CN 叙事层覆写装置
            GTCMItemList.NarrativeLayerOverwritingDevice.set(initItem01("Narrative layer overwriting device", 306));
            // #tr item.MetaItem01.307.name
            // # Hyperspace Narrative Layer Adaptive Special SRA
            // #zh_CN 超时空叙事层适应型特种SRA
            GTCMItemList.HyperspaceNarrativeLayerAdaptiveSpecialSRA.set(initItem01("Hyperspace Narrative Layer Adaptive Special SRA", 307));
            // #tr item.MetaItem01.308.name
            // # Real Singularity Nano Swarm
            // #zh_CN 奇点蜂群
            GTCMItemList.RealSingularityNanoSwarm.set(initItem01("Real Singularity Nano Swarm", 308));
            // #tr item.MetaItem01.309.name
            // # paradox engine
            // #zh_CN 悖论引擎
            GTCMItemList.ParadoxEngine.set(initItem01("paradox engine", 309));
            // #tr item.MetaItem01.310.name
            // # quasar soc
            // #zh_CN 类星体SOC
            GTCMItemList.QuasarSoc.set(initItem01("quasar soc", 310));
            // #tr item.MetaItem01.311.name
            // # miniature galaxy
            // #zh_CN 微缩银河
            GTCMItemList.MiniatureGalaxy.set(initItem01("miniature galaxy", 311));
            // #tr item.MetaItem01.312.name
            // # self-adaptive AI Ⅰ
            // #zh_CN 第一代自适应全人工智能
            GTCMItemList.Self_adaptiveAI1.set(initItem01("self-adaptive AI Ⅰ", 312));
            // #tr item.MetaItem01.313.name
            // # self-adaptive AI Ⅱ
            // #zh_CN 第二代自适应全人工智能
            GTCMItemList.Self_adaptiveAI2.set(initItem01("self-adaptive AI Ⅱ", 313));
            // #tr item.MetaItem01.314.name
            // # self-adaptive AI Ⅲ
            // #zh_CN 第三代自适应全人工智能
            GTCMItemList.Self_adaptiveAI3.set(initItem01("self-adaptive AI Ⅲ", 314));
            // #tr item.MetaItem01.315.name
            // # self-adaptive AI Ⅳ
            // #zh_CN 第四代自适应全人工智能
            GTCMItemList.Self_adaptiveAI4.set(initItem01("self-adaptive AI Ⅳ", 315));
            // #tr item.MetaItem01.316.name
            // # self-adaptive AI Ⅴ
            // #zh_CN 第五代自适应全人工智能
            GTCMItemList.Self_adaptiveAI5.set(initItem01("self-adaptive AI Ⅴ", 316));
            // #tr item.MetaItem01.317.name
            // # core of T800
            // #zh_CN T800核心
            GTCMItemList.CoreOfT800.set(initItem01("core of T800", 317));
            // #tr item.MetaItem01.318.name
            // # Exotic Circuit Board
            // #zh_CN 天顶星电路基板
            GTCMItemList.ExoticCircuitBoard.set(initItem01("Exotic Circuit Board", 318));
            // #tr item.MetaItem01.319.name
            // # very good item
            // #zh_CN very good item
            GTCMItemList.spaceStationConstructingMaterialMax.set(initItem01("very good item", 319));
            // #tr item.MetaItem01.320.name
            // # Light Quantum Matrix
            // #zh_CN 光量子矩阵
            GTCMItemList.LightQuantumMatrix.set(initItem01("Light Quantum Matrix", 320));

            // #tr item.MetaItem01.322.name
            // # Casimir Quantum Fiber
            // #zh_CN 卡西米尔量子纤维
            GTCMItemList.CasimirQuantumFiber.set(initItem01("Casimir Quantum Fiber", 322));
            // #tr item.MetaItem01.323.name
            // # Superstring structure
            // #zh_CN 超弦结构
            GTCMItemList.SuperstringStructure.set(initItem01("Superstring structure", 323));
            // #tr item.MetaItem01.324.name
            // # Dynamic Paradox Body
            // #zh_CN 动力学悖论体
            GTCMItemList.DynamicParadoxBody.set(initItem01("Dynamic Paradox Body", 324));
            // #tr item.MetaItem01.325.name
            // # Void Prism
            // #zh_CN 虚空棱镜
            GTCMItemList.VoidPrism.set(initItem01("Void Prism", 325));
            // #tr item.MetaItem01.326.name
            // # Pulsar core
            // #zh_CN 脉冲星核核心
            GTCMItemList.PulsarCore.set(initItem01("Pulsar core", 326));
            // #tr item.MetaItem01.327.name
            // # Star Crystal I
            // #zh_CN 星晶I
            GTCMItemList.StarCrystalI.set(initItem01("Star Crystal I", 327));
            // #tr item.MetaItem01.328.name
            // # Super Dimensional Ring
            // #zh_CN 超维环
            GTCMItemList.SuperDimensionalRing.set(initItem01("Super Dimensional Ring", 328));
            // #tr item.MetaItem01.329.name
            // # Hyperdimensional expansion
            // #zh_CN 超维度展开
            GTCMItemList.HyperdimensionalExpansion.set(initItem01("Hyperdimensional expansion", 329));
            // #tr item.MetaItem01.330.name
            // # optical layer
            // #zh_CN 光缘层
            GTCMItemList.OpticalLayer.set(initItem01("optical layer", 330));
            // #tr item.MetaItem01.331.name
            // # Magnetic Spin I
            // #zh_CN 磁旋I
            GTCMItemList.MagneticSpinI.set(initItem01("Magnetic Spin I", 331));
            // #tr item.MetaItem01.332.name
            // # star axis
            // #zh_CN 星轴
            GTCMItemList.Staraxis.set(initItem01("star axis", 332));
            // #tr item.MetaItem01.333.name
            // # Boltzmann Brain
            // #zh_CN 玻尔兹曼大脑
            GTCMItemList.BoltzmannBrain.set(initItem01("Boltzmann Brain", 333));
            // #tr item.MetaItem01.334.name
            // # Remnants of the Big Bang
            // #zh_CN 宇宙大爆炸残留
            GTCMItemList.RemnantsOfTheBigBang.set(initItem01("Remnants of the Big Bang", 334));
            // #tr item.MetaItem01.335.name
            // # Strange Film
            // #zh_CN 奇异片
            GTCMItemList.StrangeFilm.set(initItem01("Strange Film", 335));
            // #tr item.MetaItem01.336.name
            // # Pulse Manganese
            // #zh_CN 脉冲锰
            GTCMItemList.PulseManganese.set(initItem01("Pulse Manganese", 336));
            // #tr item.MetaItem01.337.name
            // # Superdimensional Web
            // #zh_CN 超维网
            GTCMItemList.SuperdimensionalWeb.set(initItem01("Superdimensional Web", 337));
            // #tr item.MetaItem01.338.name
            // # Pinoan Structure
            // #zh_CN 皮诺亚结构
            GTCMItemList.PinoanStructure.set(initItem01("Pinoan Structure", 338));
            // #tr item.MetaItem01.339.name
            // # Quantum Chain
            // #zh_CN 量子链
            GTCMItemList.QuantumChain.set(initItem01("Quantum Chain", 339));
            // #tr item.MetaItem01.340.name
            // # Star Belt
            // #zh_CN 星带
            GTCMItemList.StarBelt.set(initItem01("Star Belt", 340));
            // #tr item.MetaItem01.341.name
            // # Nanoflow
            // #zh_CN 纳米流
            GTCMItemList.Nanoflow.set(initItem01("Nanoflow", 341));
            // #tr item.MetaItem01.342.name
            // # Space-time layer
            // #zh_CN 时空层
            GTCMItemList.Space_TimeLayer.set(initItem01("Space-time layer", 342));
            // #tr item.MetaItem01.343.name
            // # Superconducting ring
            // #zh_CN 超导环
            GTCMItemList.SuperconductingRing.set(initItem01("Superconducting ring", 343));
            // #tr item.MetaItem01.344.name
            // # Quantized Superstring Structure
            // #zh_CN 量子化超弦结构
            GTCMItemList.QuantizedSuperstringStructure.set(initItem01("Quantized Superstring Structure", 344));
            // #tr item.MetaItem01.345.name
            // # The zero point of vacuum can manifest objects
            // #zh_CN 真空零点能具现物
            GTCMItemList.ThezeroPointOfVacuumCanManifestObjects.set(initItem01("The zero point of vacuum can manifest objects", 345));
            // #tr item.MetaItem01.346.name
            // # Star Core
            // #zh_CN 星核核心
            GTCMItemList.StarCore.set(initItem01("Star Core", 346));
            // #tr item.MetaItem01.347.name
            // # quasar remnant
            // #zh_CN 类星体残留
            GTCMItemList.QuasarRemnant.set(initItem01("quasar remnant", 347));
            // #tr item.MetaItem01.348.name
            // # Infinite Divine Machine
            // #zh_CN 无限神机
            GTCMItemList.InfiniteDivineMachine.set(initItem01("Infinite Divine Machine", 348));
            // #tr item.MetaItem01.349.name
            // # Original Soup
            // #zh_CN 原始汤
            GTCMItemList.OriginalSoup.set(initItem01("Original Soup", 349));
            // #tr item.MetaItem01.350.name
            // # Gravity Belt
            // #zh_CN 引力带
            GTCMItemList.GravityBelt.set(initItem01("Gravity Belt", 350));
            // #tr item.MetaItem01.351.name
            // # anti-gravity engine
            // #zh_CN 反重力引擎
            GTCMItemList.anti_GravityEngine.set(initItem01("anti-gravity engine", 351));
            // #tr item.MetaItem01.352.name
            // # Condensed Dark Matter Polymer
            // #zh_CN 浓缩暗物质聚合物
            GTCMItemList.CondensedDarkMatterPolymer.set(initItem01("Condensed Dark Matter Polymer", 352));
            // #tr item.MetaItem01.353.name
            // # Low Density Dark Matter Polymer
            // #zh_CN 低密度暗物质聚合物
            GTCMItemList.LowDensityDarkMatterPolymer.set(initItem01("Low Density Dark Matter Polymer", 353));
            // #tr item.MetaItem01.354.name
            // # infinite recursion
            // #zh_CN 无限递归
            GTCMItemList.InfiniteRecursion.set(initItem01("infinite recursion", 354));
            // #tr item.MetaItem01.355.name
            // # Superdimensional Spiral
            // #zh_CN 超维螺旋
            GTCMItemList.SuperdimensionalSpiral.set(initItem01("Superdimensional Spiral", 355));
            // #tr item.MetaItem01.356.name
            // # Infinite Divine Machine I
            // #zh_CN 无限神机I
            GTCMItemList.InfiniteDivineMachineI.set(initItem01("Infinite Divine Machine I", 356));
            // #tr item.MetaItem01.357.name
            // # nuclear axis fluctuation
            // #zh_CN 核轴波动
            GTCMItemList.NuclearaxisFluctuation.set(initItem01("nuclear axis fluctuation", 357));
            // #tr item.MetaItem01.358.name
            // # Strange fluctuations
            // #zh_CN 奇异波动
            GTCMItemList.StrangeFluctuations.set(initItem01("Strange fluctuations", 358));
            // #tr item.MetaItem01.359.name
            // # Pulse Copper
            // #zh_CN 脉冲铜
            GTCMItemList.PulseCopper.set(initItem01("Pulse Copper", 359));
            // #tr item.MetaItem01.360.name
            // # Dark Matter Crystallization
            // #zh_CN 暗物质结晶
            GTCMItemList.DarkMatterCrystallization.set(initItem01("Dark Matter Crystallization", 360));
            // #tr item.MetaItem01.361.name
            // # Quantum Core
            // #zh_CN 量子核
            GTCMItemList.QuantumCore.set(initItem01("Quantum Core", 361));
            // #tr item.MetaItem01.362.name
            // # Photon flow
            // #zh_CN 光子流
            GTCMItemList.PhotonFlow.set(initItem01("Photon flow", 362));
            // #tr item.MetaItem01.363.name
            // # nuclear belt
            // #zh_CN 核带
            GTCMItemList.NuclearBelt.set(initItem01("nuclear belt", 363));
            // #tr item.MetaItem01.364.name
            // # Life Guide
            // #zh_CN 生命导
            GTCMItemList.LifeGuide.set(initItem01("Life Guide", 364));
            // #tr item.MetaItem01.365.name
            // # Quantized Superstring Structure I
            // #zh_CN 量子化超弦结构I
            GTCMItemList.QuantizedSuperstringStructureI.set(initItem01("Quantized Superstring Structure I", 365));
            // #tr item.MetaItem01.366.name
            // # empty heart
            // #zh_CN 虚空心
            GTCMItemList.EmptyHeart.set(initItem01("empty heart", 366));
            // #tr item.MetaItem01.367.name
            // # Star Core Belt
            // #zh_CN 星核带
            GTCMItemList.StarCoreBelt.set(initItem01("Star Core Belt", 367));
            // #tr item.MetaItem01.368.name
            // # Space-time Spiral
            // #zh_CN 时空螺旋
            GTCMItemList.Space_TimeSpiral.set(initItem01("Space-time Spiral", 368));
            // #tr item.MetaItem01.369.name
            // # Magnetic Spin IV
            // #zh_CN 磁旋IV
            GTCMItemList.MagneticSpinIV.set(initItem01("Magnetic Spin IV", 369));
            // #tr item.MetaItem01.370.name
            // # Nuclear Fluctuation
            // #zh_CN 核波动
            GTCMItemList.NuclearFluctuation.set(initItem01("Nuclear Fluctuation", 370));
            // #tr item.MetaItem01.371.name
            // # Celestial Resonance Crystal
            // #zh_CN 天体共鸣水晶
            GTCMItemList.CelestialResonanceCrystal.set(initItem01("Celestial Resonance Crystal", 371));
            // #tr item.MetaItem01.372.name
            // # Low Density Dark Matter Polymer I
            // #zh_CN 低密度暗物质聚合物I
            GTCMItemList.LowDensityDarkMatterPolymerI.set(initItem01("Low Density Dark Matter Polymer I", 372));
            // #tr item.MetaItem01.373.name
            // # Quantum Core
            // #zh_CN 量子核心
            GTCMItemList.QuantumCore.set(initItem01("Quantum Core", 373));
            // #tr item.MetaItem01.374.name
            // # Superdimensional life
            // #zh_CN 超维生命
            GTCMItemList.SuperdimensionalLife.set(initItem01("Superdimensional life", 374));
            // #tr item.MetaItem01.375.name
            // # Gravity Fluctuation
            // #zh_CN 引力波动
            GTCMItemList.GravityFluctuation.set(initItem01("Gravity Fluctuation", 375));
            // #tr item.MetaItem01.376.name
            // # Light Spiral
            // #zh_CN 光螺旋
            GTCMItemList.LightSpiral.set(initItem01("Light Spiral", 376));
            // #tr item.MetaItem01.377.name
            // # nuclear axis belt
            // #zh_CN 核轴带
            GTCMItemList.NuclearaxisBelt.set(initItem01("nuclear axis belt", 377));
            // #tr item.MetaItem01.378.name
            // # Superconducting Network
            // #zh_CN 超导网
            GTCMItemList.SuperconductingNetwork.set(initItem01("Superconducting Network", 378));
            // #tr item.MetaItem01.379.name
            // # Nanolife
            // #zh_CN 纳米生命
            GTCMItemList.Nanolife.set(initItem01("Nanolife", 379));
            // #tr item.MetaItem01.380.name
            // # Core of Ancient Creation
            // #zh_CN 上古造物核心
            GTCMItemList.CoreOfAncientCreation.set(initItem01("Core of Ancient Creation", 380));
            // #tr item.MetaItem01.381.name
            // # Quantum Heart
            // #zh_CN 量子心
            GTCMItemList.QuantumHeart.set(initItem01("Quantum Heart", 381));
            // #tr item.MetaItem01.382.name
            // # fluctuating life
            // #zh_CN 波动生命
            GTCMItemList.FluctuatingLife.set(initItem01("fluctuating life", 382));
            // #tr item.MetaItem01.383.name
            // # Pioneer Remains
            // #zh_CN 先驱者遗骸
            GTCMItemList.PioneerRemains.set(initItem01("Pioneer Remains", 383));
            // #tr item.MetaItem01.384.name
            // # Life is empty
            // #zh_CN 生命空
            GTCMItemList.LifeIsEmpty.set(initItem01("Life is empty", 384));
            // #tr item.MetaItem01.385.name
            // # Superstring structure V
            // #zh_CN 超弦结构V
            GTCMItemList.SuperstringStructureV.set(initItem01("Superstring structure V", 385));
            // #tr item.MetaItem01.386.name
            // # Superdimensional fluctuations
            // #zh_CN 超维波动
            GTCMItemList.SuperdimensionalFluctuations.set(initItem01("Superdimensional fluctuations", 386));
            // #tr item.MetaItem01.387.name
            // # Creations from the Outer Universe
            // #zh_CN 外宇宙造物
            GTCMItemList.CreationsFromTheOuterUniverse.set(initItem01("Creations from the Outer Universe", 387));
            // #tr item.MetaItem01.388.name
            // # Magnetic Vortex
            // #zh_CN 磁旋V
            GTCMItemList.MagneticVortex.set(initItem01("Magnetic Vortex", 388));
            // #tr item.MetaItem01.389.name
            // # Space-time core
            // #zh_CN 时空核
            GTCMItemList.Space_TimeCore.set(initItem01("Space-time core", 389));
            // #tr item.MetaItem01.390.name
            // # Subspace Heart
            // #zh_CN 子空间心
            GTCMItemList.SubspaceHeart.set(initItem01("Subspace Heart", 390));
            // #tr item.MetaItem01.391.name
            // # cosmic expansion effect fluctuations
            // #zh_CN 宇宙膨胀效应波动
            GTCMItemList.CosmicExpansionEffectFluctuations.set(initItem01("cosmic expansion effect fluctuations", 391));
            // #tr item.MetaItem01.392.name
            // # Star Crystal IV
            // #zh_CN 星晶IV
            GTCMItemList.StarCrystalIV.set(initItem01("Star Crystal IV", 392));
            // #tr item.MetaItem01.393.name
            // # Infinite Recursive Net
            // #zh_CN 无限递归网
            GTCMItemList.InfiniteRecursiveNet.set(initItem01("Infinite Recursive Net", 393));
            // #tr item.MetaItem01.394.name
            // # Superconducting Life Waves
            // #zh_CN 超导生命波动
            GTCMItemList.SuperconductingLifeWaves.set(initItem01("Superconducting Life Waves", 394));
            // #tr item.MetaItem01.395.name
            // # gravitational heart
            // #zh_CN 引力心
            GTCMItemList.GravitationalHeart.set(initItem01("gravitational heart", 395));
            // #tr item.MetaItem01.396.name
            // # Celestial Resonance Crystal Spiral
            // #zh_CN 天体共鸣水晶螺旋
            GTCMItemList.CelestialResonanceCrystalSpiral.set(initItem01("Celestial Resonance Crystal Spiral", 396));
            // #tr item.MetaItem01.397.name
            // # nuclear axis core
            // #zh_CN 核轴核
            GTCMItemList.NuclearaxisCore.set(initItem01("nuclear axis core", 397));
            // #tr item.MetaItem01.398.name
            // # Void Fluctuation
            // #zh_CN 虚空波动
            GTCMItemList.VoidFluctuation.set(initItem01("Void Fluctuation", 398));
            // #tr item.MetaItem01.399.name
            // # Ancient Creation Fluctuation
            // #zh_CN 上古造物波动
            GTCMItemList.AncientCreationFluctuation.set(initItem01("Ancient Creation Fluctuation", 399));
            // #tr item.MetaItem01.400.name
            // # Infinite recursive heart
            // #zh_CN 无限递归心
            GTCMItemList.InfiniteRecursiveHeart.set(initItem01("Infinite recursive heart", 400));
            // #tr item.MetaItem01.401.name
            // # Alien Star Core
            // #zh_CN 异星星核
            GTCMItemList.AlienStarCore.set(initItem01("Alien Star Core", 401));
            // #tr item.MetaItem01.402.name
            // # spiral spiral
            // #zh_CN 螺旋螺旋
            GTCMItemList.SpiralSpiral.set(initItem01("spiral spiral", 402));
            // #tr item.MetaItem01.403.name
            // # Magnetic Spin Life
            // #zh_CN 磁旋生命
            GTCMItemList.MagneticSpinLife.set(initItem01("Magnetic Spin Life", 403));
            // #tr item.MetaItem01.404.name
            // # Light Waves
            // #zh_CN 光波动
            GTCMItemList.LightWaves.set(initItem01("Light Waves", 404));
            // #tr item.MetaItem01.405.name
            // # nuclear spiral
            // #zh_CN 核核螺旋
            GTCMItemList.NuclearSpiral.set(initItem01("nuclear spiral", 405));
            // #tr item.MetaItem01.406.name
            // # Cosmic expansion effect core
            // #zh_CN 宇宙膨胀效应核
            GTCMItemList.CosmicExpansionEffectCore.set(initItem01("Cosmic expansion effect core", 406));
            // #tr item.MetaItem01.407.name
            // # Gravity Life
            // #zh_CN 引力生命
            GTCMItemList.GravityLife.set(initItem01("Gravity Life", 407));
            // #tr item.MetaItem01.408.name
            // # Celestial Resonance Crystal Network
            // #zh_CN 天体共鸣水晶网
            GTCMItemList.CelestialResonanceCrystalNetwork.set(initItem01("Celestial Resonance Crystal Network", 408));
            // #tr item.MetaItem01.409.name
            // # Life in Time and Space
            // #zh_CN 时空生命
            GTCMItemList.LifeInTimeandSpace.set(initItem01("Life in Time and Space", 409));
            // #tr item.MetaItem01.410.name
            // # Core Axis Life Heart
            // #zh_CN 核轴生命心
            GTCMItemList.CoreAxisLifeHeart.set(initItem01("Core Axis Life Heart", 410));
            // #tr item.MetaItem01.411.name
            // # Alien Star Core
            // #zh_CN 异星星核
            GTCMItemList.AlienStarCore.set(initItem01("Alien Star Core", 411));
            // #tr item.MetaItem01.412.name
            // # Nano Life Heart
            // #zh_CN 纳米生命心
            GTCMItemList.NanoLifeHeart.set(initItem01("Nano Life Heart", 412));
            // #tr item.MetaItem01.413.name
            // # Ancient Creation Life
            // #zh_CN 上古造物生命
            GTCMItemList.AncientCreationLife.set(initItem01("Ancient Creation Life", 413));
            // #tr item.MetaItem01.414.name
            // # infinite recursive kernel
            // #zh_CN 无限递归核
            GTCMItemList.InfiniteRecursiveKernel.set(initItem01("infinite recursive kernel", 414));
            // #tr item.MetaItem01.415.name
            // # Superconducting Life Zone
            // #zh_CN 超导生命带
            GTCMItemList.SuperconductingLifeZone.set(initItem01("Superconducting Life Zone", 415));
            // #tr item.MetaItem01.416.name
            // # Gravity Life Fluctuation
            // #zh_CN 引力生命波动
            GTCMItemList.GravityLifeFluctuation.set(initItem01("Gravity Life Fluctuation", 416));
            // #tr item.MetaItem01.417.name
            // # Pioneer Remains Life Core
            // #zh_CN 先驱者遗骸生命核心
            GTCMItemList.PioneerRemainsLifeCore.set(initItem01("Pioneer Remains Life Core", 417));
            // #tr item.MetaItem01.418.name
            // # Subspace Fluctuation
            // #zh_CN 子空间波动
            GTCMItemList.SubspaceFluctuation.set(initItem01("Subspace Fluctuation", 418));
            // #tr item.MetaItem01.419.name
            // # Space-time life core
            // #zh_CN 时空生命核
            GTCMItemList.Space_TimeLifeCore.set(initItem01("Space-time life core", 419));
        }

        // Endgame Challenge content

        // #tr item.MetaItem01.420.name
        // # LV FLASK
        // #zh_CN LV FLASK
        GTCMItemList.LvFlask.set(initItem01("LV FLASK", 420));
        // #tr item.MetaItem01.421.name
        // # MV FLASK
        // #zh_CN MV FLASK
        GTCMItemList.MvFlask.set(initItem01("MV FLASK", 421));
        // #tr item.MetaItem01.422.name
        // # HV FLASK
        // #zh_CN HV FLASK
        GTCMItemList.HvFlask.set(initItem01("HV FLASK", 422));
        // #tr item.MetaItem01.423.name
        // # EV FLASK
        // #zh_CN EV FLASK
        GTCMItemList.EvFlask.set(initItem01("EV FLASK", 423));
        // #tr item.MetaItem01.424.name
        // # IV FLASK
        // #zh_CN IV FLASK
        GTCMItemList.IvFlask.set(initItem01("IV FLASK", 424));
        // #tr item.MetaItem01.425.name
        // # LUV FLASK
        // #zh_CN LUV FLASK
        GTCMItemList.LuvFlask.set(initItem01("LUV FLASK", 425));
        // #tr item.MetaItem01.426.name
        // # ZPM FLASK
        // #zh_CN ZPM FLASK
        GTCMItemList.ZpmFlask.set(initItem01("ZPM FLASK", 426));
        // #tr item.MetaItem01.427.name
        // # UV FLASK
        // #zh_CN UV FLASK
        GTCMItemList.UvFlask.set(initItem01("UV FLASK", 427));
        // #tr item.MetaItem01.428.name
        // # UHV FLASK
        // #zh_CN UHV FLASK
        GTCMItemList.UhvFlask.set(initItem01("UHV FLASK", 428));
        // #tr item.MetaItem01.429.name
        // # UEV FLASK
        // #zh_CN UEV FLASK
        GTCMItemList.UevFlask.set(initItem01("UEV FLASK", 429));
        // #tr item.MetaItem01.430.name
        // # UIV FLASK
        // #zh_CN UIV FLASK
        GTCMItemList.UivFlask.set(initItem01("UIV FLASK", 430));
        // #tr item.MetaItem01.431.name
        // # UMV FLASK
        // #zh_CN UMV FLASK
        GTCMItemList.UmvFlask.set(initItem01("UMV FLASK", 431));
        // #tr item.MetaItem01.432.name
        // # UXV FLASK
        // #zh_CN UXV FLASK
        GTCMItemList.UxvFlask.set(initItem01("UXV FLASK", 432));

        // #tr item.MetaItemRune.0.name
        // # Rune of Vigilance
        // #zh_CN 恂戒符文
        // #tr tooltips.Rune_of_Vigilance.line1
        // # Vigilance.
        // #zh_CN {\RED}笼中之鸟从未意识到警戒的重要性。
        GTCMItemList.Rune_of_Vigilance.set(initItemRune("Rune of Vigilance", 0, new String[]{TextEnums.tr("tooltips.Rune_of_Vigilance.line1")}));
        // #tr item.MetaItemRune.1.name
        // # Rune of Erelong
        // #zh_CN 须臾符文
        // #tr tooltips.Rune_of_Erelong.line1
        // # Erelong.
        // #zh_CN {\DARK_BLUE}永恒不过是一刻的缩影。
        GTCMItemList.Rune_of_Erelong.set(initItemRune("Rune of Erelong", 1, new String[]{TextEnums.tr("tooltips.Rune_of_Erelong.line1")}));
        // #tr item.MetaItemRune.2.name
        // # Rune of Ether
        // #zh_CN 以太符文
        // #tr tooltips.Rune_of_Ether.line1
        // # Ether.
        // #zh_CN {\AQUA}对未来最大的慷慨，是把一切献给现在。
        GTCMItemList.Rune_of_Ether.set(initItemRune("Rune of Ether", 2, new String[]{TextEnums.tr("tooltips.Rune_of_Ether.line1")}));
        // #tr item.MetaItemRune.3.name
        // # Rune of Perdition
        // #zh_CN 永劫符文
        // #tr tooltips.Rune_of_Perdition.line1
        // # Perdition.
        // #zh_CN {\DARK_RED}永远不为从未存在的救赎祈祷。
        GTCMItemList.Rune_of_Perdition.set(initItemRune("Rune of Perdition", 3, new String[]{TextEnums.tr("tooltips.Rune_of_Perdition.line1")}));

        GTCMItemList.FountOfEcology.set(initItemIzumik(
            "Fount Of Ecology", 0,
            // #tr item.MetaItemIzumik.0.name
            // # {\BLUE}{\BOLD}Fount Of Ecology
            // #zh_CN {\BLUE}{\BOLD}生态泉源
            new String[]{tr("FountOfEcology.tooltips.01"), tr("FountOfEcology.tooltips.02")},
            // #tr FountOfEcology.tooltips.01
            // # {\AQUA}A unique looking jellyfish
            // #zh_CN {\AQUA}一只长相奇特的水母
            // #tr FountOfEcology.tooltips.02
            // # {\AQUA}Well......
            // #zh_CN {\AQUA}等下......
            new String[]{tr("FountOfEcology.tooltips.03"), tr("FountOfEcology.tooltips.04")}
            // #tr FountOfEcology.tooltips.03
            // # {\GOLD}A perfect creature close to the singularity of evolution, The counselor and lear of The Many.
            // #zh_CN {\GOLD}临近进化奇点的完美生物 大群的建言者与引航者
            // #tr FountOfEcology.tooltips.04
            // # {\GOLD}"The Afterborn Firstborn", Seaborn
            // #zh_CN {\GOLD}"后生的出初生" 海嗣
        ));

        GTCMItemList.OffSpring.set(initItemIzumik(
            "OffSpring", 1,
            // #tr item.MetaItemIzumik.1.name
            // # {\DARK_AQUA}"Offspring"
            // #zh_CN {\DARK_AQUA}"子代"
            new String[]{tr("Offspring.tooltips.01"), tr("Offspring.tooltips.02")},
            // #tr Offspring.tooltips.01
            // # {\AQUA}A weak little jellyfish
            // #zh_CN {\AQUA}一只弱不禁风的小水母
            // #tr Offspring.tooltips.02
            // # {\AQUA}Seems to be containing additional information
            // #zh_CN {\AQUA}似乎包含着额外的信息
            new String[]{tr("Offspring.tooltips.03"), tr("Offspring.tooltips.04")}
            // #tr Offspring.tooltips.03
            // # {\LIGHT_PURPLE}The offspring derived from Izu'mik's evolutionary branches
            // #zh_CN {\LIGHT_PURPLE}伊祖米克进化分支中衍生出的子代
            // #tr Offspring.tooltips.04
            // # {\LIGHT_PURPLE}are returning to their parent with the genetic information collected from all of The Overworld
            // #zh_CN {\LIGHT_PURPLE}正携带着从主世界各地收集到的遗传信息返回母体
        ));
        GTCMItemList.ProofOfGods.set(new ItemStack(BasicItems.ProofOfGods, 1));
        GTCMItemList.ProofOfHeroes.set(new ItemStack(BasicItems.ProofOfHeroes, 2));
        GTCMItemList.PowerChair.set(new ItemStack(BasicItems.PowerChair, 1));

        GTCMItemList.HatchUpdateTool.set(new ItemStack(BasicItems.HatchUpdateTool, 1));

        GTCMItemList.Yamato.set(new ItemStack(BasicItems.Yamato, 1));

    }

    // spotless:on
    public static void registry() {
        registryItems();
        registryItemContainers();
    }
}
