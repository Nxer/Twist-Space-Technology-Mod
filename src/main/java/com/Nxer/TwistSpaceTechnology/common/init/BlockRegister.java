package com.Nxer.TwistSpaceTechnology.common.init;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.BlockArcaneHole;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.BlockEyeOfWoodRender;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.BlockStar;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlock01;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing02;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.NuclearReactorBlock;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.PhotonControllerUpgrade;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.SpaceStationAntiGravityBlock;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.SpaceStationStructureBlock;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BlockPowerChair;
import com.Nxer.TwistSpaceTechnology.common.entity.TileEntityLaserBeacon;
import com.Nxer.TwistSpaceTechnology.common.item.blockItem.TstMetaBlockItem;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.common.tile.TileArcaneHole;
import com.Nxer.TwistSpaceTechnology.common.tile.TileEyeOfWoodRender;
import com.Nxer.TwistSpaceTechnology.common.tile.TilePowerChair;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStar;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.blocks.BlockMetal;

public class BlockRegister {

    public static void registryBlocks() {

        GameRegistry.registerBlock(MetaBlock01, TstMetaBlockItem.class, MetaBlock01.unlocalizedName);

        GameRegistry
            .registerBlock(TstBlocks.MetaBlockCasing01, TstMetaBlockItem.class, MetaBlockCasing01.unlocalizedName);

        GameRegistry.registerBlock(MetaBlockCasing02, TstMetaBlockItem.class, MetaBlockCasing02.unlocalizedName);

        GameRegistry
            .registerBlock(PhotonControllerUpgrade, TstMetaBlockItem.class, PhotonControllerUpgrade.unlocalizedName);

        GameRegistry.registerBlock(
            TstBlocks.SpaceTimeOscillator,
            TstMetaBlockItem.class,
            TstBlocks.SpaceTimeOscillator.unlocalizedName);
        GameRegistry.registerBlock(
            TstBlocks.SpaceTimeConstraintor,
            TstMetaBlockItem.class,
            TstBlocks.SpaceTimeConstraintor.unlocalizedName);
        GameRegistry.registerBlock(
            TstBlocks.SpaceTimeMerger,
            TstMetaBlockItem.class,
            TstBlocks.SpaceTimeMerger.unlocalizedName);
        GameRegistry.registerBlock(
            TstBlocks.StabilisationFieldGenerator,
            TstMetaBlockItem.class,
            TstBlocks.StabilisationFieldGenerator.unlocalizedName);
        GameRegistry.registerBlock(
            TstBlocks.EnergySustainmentMatrix,
            TstMetaBlockItem.class,
            TstBlocks.EnergySustainmentMatrix.unlocalizedName);

        if (Config.activateMegaSpaceStation) {
            GameRegistry.registerBlock(
                SpaceStationStructureBlock,
                TstMetaBlockItem.class,
                SpaceStationStructureBlock.unlocalizedName);
            GameRegistry.registerBlock(
                SpaceStationAntiGravityBlock,
                TstMetaBlockItem.class,
                SpaceStationAntiGravityBlock.unlocalizedName);
            GameRegistry
                .registerBlock(NuclearReactorBlock, TstMetaBlockItem.class, NuclearReactorBlock.unlocalizedName);
        }

        GameRegistry.registerBlock(BlockArcaneHole, "BlockArcaneHole");

        GameRegistry
            .registerBlock(TstBlocks.BlockPowerChair, BlockPowerChair.ItemBlockPowerChair.class, "BlockPowerChair");
        GameRegistry.registerBlock(BlockStar, BlockStar.unlocalizedName);
        GameRegistry.registerTileEntity(TileStar.class, "StarRender");
        GameRegistry.registerBlock(BlockEyeOfWoodRender, BlockEyeOfWoodRender.unlocalizedName);
        GameRegistry.registerTileEntity(TileEyeOfWoodRender.class, "EyeOfWoodRender");
        GameRegistry.registerTileEntity(TilePowerChair.class, "TilePowerChair");
        GameRegistry.registerTileEntity(TileArcaneHole.class, "TileArcaneHole");
        GameRegistry.registerTileEntity(TileEntityLaserBeacon.class, "MeteorMinerRenderer");

        GameRegistry.registerBlock(TstBlocks.TimeBendingSpeedRune, "TimeBendingSpeedRune");
    }

    public static void registryBlockContainers() {

        // #tr tile.MetaBlock01.0.name
        // # Test Block
        // #zh_CN 测试方块
        GTCMItemList.TestMetaBlock01_0.set(MetaBlock01.registerVariant(0));

        // #tr tile.MetaBlockCasing01.0.name
        // # Test Casing
        // #zh_CN Test Casing
        GTCMItemList.TestCasing.set(MetaBlockCasing01.registerVariant(0));

        // #tr tile.MetaBlockCasing01.1.name
        // # High Power Radiation Proof Casing
        // #zh_CN 高能防辐射机械方块
        GTCMItemList.HighPowerRadiationProofCasing.set(
            MetaBlockCasing01.registerVariantWithTooltips(
                1,
                new String[] { TextEnums.tr("Tooltips_HighPowerRadiationProofCasing.01"),
                    TextEnums.tr("Tooltips_HighPowerRadiationProofCasing.02") }));

        // #tr tile.MetaBlockCasing01.2.name
        // # Advanced High Power Coil Block
        // #zh_CN 进阶高能线圈
        GTCMItemList.AdvancedHighPowerCoilBlock.set(
            MetaBlockCasing01.registerVariantWithTooltips(
                2,
                new String[] { TextEnums.tr("Tooltips_AdvancedHighPowerCoil.01"),
                    TextEnums.tr("Tooltips_AdvancedHighPowerCoil.02") }));

        // #tr tile.MetaBlockCasing01.3.name
        // # Parallelism Casing Mark 0
        // #zh_CN 初等处理阵列并行机械方块
        GTCMItemList.ParallelismCasing0.set(
            MetaBlockCasing01.registerVariantWithTooltips(
                3,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.3.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.3.name_1") }));

        // #tr tile.MetaBlockCasing01.4.name
        // # Parallelism Casing Mark 1
        // #zh_CN 进阶处理阵列并行机械方块
        GTCMItemList.ParallelismCasing1.set(
            MetaBlockCasing01.registerVariantWithTooltips(
                4,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.4.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.4.name_1") }));

        // #tr tile.MetaBlockCasing01.5.name
        // # Parallelism Casing Mark 2
        // #zh_CN 高能处理阵列并行机械方块
        GTCMItemList.ParallelismCasing2.set(
            MetaBlockCasing01.registerVariantWithTooltips(
                5,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.5.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.5.name_1") }));

        // #tr tile.MetaBlockCasing01.6.name
        // # Parallelism Casing Mark 3
        // #zh_CN 超能处理阵列并行机械方块
        GTCMItemList.ParallelismCasing3.set(
            MetaBlockCasing01.registerVariantWithTooltips(
                6,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.6.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.6.name_1") }));

        // #tr tile.MetaBlockCasing01.7.name
        // # Parallelism Casing Mark 4
        // #zh_CN 寰宇处理阵列并行机械方块
        GTCMItemList.ParallelismCasing4.set(
            MetaBlockCasing01.registerVariantWithTooltips(
                7,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.7.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.7.name_1") }));

        // #tr tile.MetaBlockCasing01.8.name
        // # Anti-Magnetic Casing
        // #zh_CN 抗磁机械方块
        GTCMItemList.AntiMagneticCasing.set(MetaBlockCasing01.registerVariant(8));

        // #tr tile.MetaBlockCasing01.9.name
        // # Reinforced Stone Brick Casing
        // #zh_CN 强化石砖机械方块
        // #tr Tooltip_ReinforcedStoneBrickCasing
        // # Just a stone?
        // #zh_CN 只是块石头?
        GTCMItemList.ReinforcedStoneBrickCasing.set(
            MetaBlockCasing01
                .registerVariantWithTooltips(9, new String[] { TextEnums.tr("Tooltip_ReinforcedStoneBrickCasing") }));

        // #tr tile.MetaBlockCasing01.10.name
        // # Composite Farm Casing
        // #zh_CN 复合农场机械方块
        // #tr Tooltip_CompositeFarmCasing
        // # A force stronger than four combined.
        // #zh_CN 一个更比四个强
        GTCMItemList.CompositeFarmCasing.set(
            MetaBlockCasing01
                .registerVariantWithTooltips(10, new String[] { TextEnums.tr("Tooltip_CompositeFarmCasing") }));

        // #tr tile.MetaBlockCasing01.11.name
        // # Dense Particle Constraint Casing
        // #zh_CN 致密粒子约束机械方块
        GTCMItemList.DenseCyclotronOuterCasing.set(MetaBlockCasing01.registerVariant(11));

        // #tr tile.MetaBlockCasing01.12.name
        // # Compact Particle Acceleration Coil
        // #zh_CN 压缩粒子加速线圈
        GTCMItemList.CompactCyclotronCoil.set(MetaBlockCasing01.registerVariant(12));

        // #tr tile.MetaBlockCasing01.13.name
        // # Aseptic Greenhouse Casing
        // #zh_CN 无菌温室机械方块
        // #tr Tooltip_AsepticGreenhouseCasing
        // # Absolutely Clean!
        // #zh_CN 一尘不染!
        GTCMItemList.AsepticGreenhouseCasing.set(
            MetaBlockCasing01
                .registerVariantWithTooltips(13, new String[] { TextEnums.tr("Tooltip_AsepticGreenhouseCasing") }));

        // #tr tile.MetaBlockCasing01.14.name
        // # Reinforced Bedrock Casing
        // #zh_CN 强化基岩机械方块
        // #tr Tooltip_ReinforcedBedrockCasing
        // # Stronger than bedrock!
        // #zh_CN 比磐石更坚！
        GTCMItemList.ReinforcedBedrockCasing.set(
            MetaBlockCasing01
                .registerVariantWithTooltips(14, new String[] { TextEnums.tr("Tooltip_ReinforcedBedrockCasing") }));

        // #tr tile.MetaBlockCasing01.15.name
        // # Swelegfyr Casing
        // #zh_CN 熯焱机械方块
        // #tr Tooltip_SwelegfyrCasing
        // # {\GOLD}Withstands the inferno!
        // #zh_CN {\GOLD}御火而生!
        GTCMItemList.SwelegfyrCasing.set(
            MetaBlockCasing01
                .registerVariantWithTooltips(15, new String[] { TextEnums.tr("Tooltip_SwelegfyrCasing") }));

        // #tr tile.MetaBlockCasing02.0.name
        // # Gore Casing
        // #zh_CN 凝血机械方块
        GTCMItemList.BloodyCasing1.set(MetaBlockCasing02.registerVariant(0));

        // #tr tile.MetaBlockCasing02.1.name
        // # Ichor Draconic Block
        // #zh_CN 血腥龙块
        GTCMItemList.BloodyCasing2.set(MetaBlockCasing02.registerVariant(1));

        // #tr tile.MetaBlockCasing02.2.name
        // # Iridium-Reinforced Neutronium Casing
        // #zh_CN 铱强化中子机械方块
        GTCMItemList.ReinforcedIridiumAlloyCasing.set(MetaBlockCasing02.registerVariant(2));

        // #tr tile.MetaBlockCasing02.3.name
        // # Borophene-Based Nanowire Composite Thermal Conductive Casing
        // #zh_CN 硼烯基纳米线复合导热外壳
        GTCMItemList.BoropheneBasedNanowireCompositeThermalConductiveCasing.set(MetaBlockCasing02.registerVariant(3));

        // #tr tile.MetaBlockCasing02.4.name
        // # Neutronium Pipe Casing
        // #zh_CN 中子管道方块
        GTCMItemList.NeutroniumPipeCasing.set(MetaBlockCasing02.registerVariant(4));

        // region SpaceTimeOscillator

        // #tr tile.SpaceTimeOscillator.0.name
        // # SpaceTime Oscillator T1
        // #zh_CN 时空振荡器T1
        GTCMItemList.SpaceTimeOscillatorT1.set(TstBlocks.SpaceTimeOscillator.registerVariant(0));

        // #tr tile.SpaceTimeOscillator.1.name
        // # SpaceTime Oscillator T2
        // #zh_CN 时空振荡器T2
        GTCMItemList.SpaceTimeOscillatorT2.set(TstBlocks.SpaceTimeOscillator.registerVariant(1));

        // #tr tile.SpaceTimeOscillator.2.name
        // # SpaceTime Oscillator T3
        // #zh_CN 时空振荡器T3
        GTCMItemList.SpaceTimeOscillatorT3.set(TstBlocks.SpaceTimeOscillator.registerVariant(2));

        // endregion

        // region SpaceTimeConstraintor

        // #tr tile.SpaceTimeConstraintor.0.name
        // # SpaceTime Constraintor T1
        // #zh_CN 时空约束器T1
        GTCMItemList.SpaceTimeConstraintorT1.set(TstBlocks.SpaceTimeConstraintor.registerVariant(0));

        // #tr tile.SpaceTimeConstraintor.1.name
        // # SpaceTime Constraintor T2
        // #zh_CN 时空约束器T2
        GTCMItemList.SpaceTimeConstraintorT2.set(TstBlocks.SpaceTimeConstraintor.registerVariant(1));

        // #tr tile.SpaceTimeConstraintor.2.name
        // # SpaceTime Constraintor T3
        // #zh_CN 时空约束器T3
        GTCMItemList.SpaceTimeConstraintorT3.set(TstBlocks.SpaceTimeConstraintor.registerVariant(2));

        // endregion

        // region SpaceTimeMerger

        // #tr tile.SpaceTimeMerger.0.name
        // # SpaceTime Merger T1
        // #zh_CN 时空归并器T1
        GTCMItemList.SpaceTimeMergerT1.set(TstBlocks.SpaceTimeMerger.registerVariant(0));

        // #tr tile.SpaceTimeMerger.1.name
        // # SpaceTime Merger T2
        // #zh_CN 时空归并器T2
        GTCMItemList.SpaceTimeMergerT2.set(TstBlocks.SpaceTimeMerger.registerVariant(1));

        // #tr tile.SpaceTimeMerger.2.name
        // # SpaceTime Merger T3
        // #zh_CN 时空归并器T3
        GTCMItemList.SpaceTimeMergerT3.set(TstBlocks.SpaceTimeMerger.registerVariant(2));

        // endregion

        // Stabilisation Field Generator

        // #tr tile.StabilisationFieldGenerator.0.name
        // # Stabilisation Field Generator Framework
        // #zh_CN 稳定力场发生器框架
        // #tr Tooltip_StabilisationFieldGenerator.0
        // # The Beginning?
        // #zh_CN 开始了?
        GTCMItemList.StabilisationFieldGeneratorFramework.set(
            TstBlocks.StabilisationFieldGenerator.registerVariantWithTooltips(
                0,
                new String[] { TextEnums.tr("Tooltip_StabilisationFieldGenerator.0") }));

        // #tr tile.StabilisationFieldGenerator.1.name
        // # Stabilisation Field Generator UEV Tier
        // #zh_CN 稳定力场发生器(UEV)
        // #tr Tooltip_StabilisationFieldGenerator.1
        // # The Beginning
        // #zh_CN 开始了
        GTCMItemList.StabilisationFieldGeneratorUEV.set(
            TstBlocks.StabilisationFieldGenerator.registerVariantWithTooltips(
                1,
                new String[] { TextEnums.tr("Tooltip_StabilisationFieldGenerator.1") }));

        // #tr tile.StabilisationFieldGenerator.2.name
        // # Stabilisation Field Generator UIV Tier
        // #zh_CN 稳定力场发生器(UIV)
        // #tr Tooltip_StabilisationFieldGenerator.2
        // # How Did We Get Here?
        // #zh_CN 为什么会变成这样呢？
        GTCMItemList.StabilisationFieldGeneratorUIV.set(
            TstBlocks.StabilisationFieldGenerator.registerVariantWithTooltips(
                2,
                new String[] { TextEnums.tr("Tooltip_StabilisationFieldGenerator.2") }));

        // #tr tile.StabilisationFieldGenerator.3.name
        // # Stabilisation Field Generator UMV Tier
        // #zh_CN 稳定力场发生器(UMV)
        // #tr Tooltip_StabilisationFieldGenerator.3
        // # We Need to Go Deeper
        // #zh_CN 我们需要再深入些
        GTCMItemList.StabilisationFieldGeneratorUMV.set(
            TstBlocks.StabilisationFieldGenerator.registerVariantWithTooltips(
                3,
                new String[] { TextEnums.tr("Tooltip_StabilisationFieldGenerator.3") }));

        // #tr tile.StabilisationFieldGenerator.4.name
        // # Stabilisation Field Generator UXV Tier
        // #zh_CN 稳定力场发生器(UXV)
        // #tr Tooltip_StabilisationFieldGenerator.4
        // # The End?
        // #zh_CN 结束了?
        GTCMItemList.StabilisationFieldGeneratorUXV.set(
            TstBlocks.StabilisationFieldGenerator.registerVariantWithTooltips(
                4,
                new String[] { TextEnums.tr("Tooltip_StabilisationFieldGenerator.4") }));

        // #tr tile.StabilisationFieldGenerator.5.name
        // # Stabilisation Field Generator MAX Tier
        // #zh_CN 稳定力场发生器(MAX)
        // #tr Tooltip_StabilisationFieldGenerator.5
        // # The End
        // #zh_CN 结束了
        GTCMItemList.StabilisationFieldGeneratorMAX.set(
            TstBlocks.StabilisationFieldGenerator.registerVariantWithTooltips(
                5,
                new String[] { TextEnums.tr("Tooltip_StabilisationFieldGenerator.5") }));

        // #tr tile.EnergySustainmentMatrix.0.name
        // # Energy Sustainment Matrix Framework
        // #zh_CN 能量维持矩阵框架
        // #tr Tooltip_EnergySustainmentMatrix.0
        // # With Our Powers Combined
        // #zh_CN 小心轻放
        GTCMItemList.EnergySustainmentMatrixFramework.set(
            TstBlocks.EnergySustainmentMatrix
                .registerVariantWithTooltips(0, new String[] { TextEnums.tr("Tooltip_EnergySustainmentMatrix.0") }));

        // #tr tile.EnergySustainmentMatrix.1.name
        // # Energy Sustainment Matrix UEV Tier
        // #zh_CN 能量维持矩阵(UEV)
        // #tr Tooltip_EnergySustainmentMatrix.1
        // # Infinator
        // #zh_CN 无尽工程师
        GTCMItemList.EnergySustainmentMatrixUEV.set(
            TstBlocks.EnergySustainmentMatrix
                .registerVariantWithTooltips(1, new String[] { TextEnums.tr("Tooltip_EnergySustainmentMatrix.1") }));

        // #tr tile.EnergySustainmentMatrix.2.name
        // # Energy Sustainment Matrix UIV Tier
        // #zh_CN 能量维持矩阵(UIV)
        // #tr Tooltip_EnergySustainmentMatrix.2
        // # Subspace Cube
        // #zh_CN 亚空间立方体
        GTCMItemList.EnergySustainmentMatrixUIV.set(
            TstBlocks.EnergySustainmentMatrix
                .registerVariantWithTooltips(2, new String[] { TextEnums.tr("Tooltip_EnergySustainmentMatrix.2") }));

        // #tr tile.EnergySustainmentMatrix.3.name
        // # Energy Sustainment Matrix UMV Tier
        // #zh_CN 能量维持矩阵(UMV)
        // #tr Tooltip_EnergySustainmentMatrix.3
        // # Paradox
        // #zh_CN 悖论
        GTCMItemList.EnergySustainmentMatrixUMV.set(
            TstBlocks.EnergySustainmentMatrix
                .registerVariantWithTooltips(3, new String[] { TextEnums.tr("Tooltip_EnergySustainmentMatrix.3") }));

        // #tr tile.EnergySustainmentMatrix.4.name
        // # Energy Sustainment Matrix UXV Tier
        // #zh_CN 能量维持矩阵(UXV)
        // #tr Tooltip_EnergySustainmentMatrix.4
        // # Lava Topic
        // #zh_CN 热门话题
        GTCMItemList.EnergySustainmentMatrixUXV.set(
            TstBlocks.EnergySustainmentMatrix
                .registerVariantWithTooltips(4, new String[] { TextEnums.tr("Tooltip_EnergySustainmentMatrix.4") }));

        // #tr tile.EnergySustainmentMatrix.5.name
        // # Energy Sustainment Matrix MAX Tier
        // #zh_CN 能量维持矩阵(MAX)
        // #tr Tooltip_EnergySustainmentMatrix.5
        // # Great Horizons From Up Here
        // #zh_CN 这上面的视野不错
        GTCMItemList.EnergySustainmentMatrixMAX.set(
            TstBlocks.EnergySustainmentMatrix
                .registerVariantWithTooltips(5, new String[] { TextEnums.tr("Tooltip_EnergySustainmentMatrix.5") }));

        // end region

        // MaterialBlock
        TstBlocks.MetalBlock = new BlockMetal(
            "tst.blockmetal01",
            new Materials[] { MaterialsTST.NeutroniumAlloy, MaterialsTST.AxonisAlloy, MaterialsTST.Axonium,
                MaterialsTST.Dubnium },
            OrePrefixes.block,
            new IIconContainer[] { new CustomIcon(RESOURCE_ROOT_ID + ":MetalBlock/BlockNeutroniumAlloy"),
                new CustomIcon(RESOURCE_ROOT_ID + ":MetalBlock/BlockAxonisAlloy"),
                new CustomIcon(RESOURCE_ROOT_ID + ":MetalBlock/BlockAxonium"),
                new CustomIcon(RESOURCE_ROOT_ID + ":MetalBlock/BlockDubnium") });

        // region PhotonControllerUpgrade

        // #tr tile.PhotonControllerUpgrades.0.name
        // # Photonic Intensifier LV Tier
        // #zh_CN 光量子增幅器LV Tier
        GTCMItemList.PhotonControllerUpgradeLV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                0,
                new String[] { TextEnums.tr("PhotonControllerUpgradeLV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.1.name
        // # Photonic Intensifier MV Tier
        // #zh_CN 光量子增幅器MV Tier
        GTCMItemList.PhotonControllerUpgradeMV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                1,
                new String[] { TextEnums.tr("PhotonControllerUpgradeMV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.2.name
        // # Photonic Intensifier HV Tier
        // #zh_CN 光量子增幅器HV Tier
        GTCMItemList.PhotonControllerUpgradeHV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                2,
                new String[] { TextEnums.tr("PhotonControllerUpgradeHV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.3.name
        // # Photonic Intensifier EV Tier
        // #zh_CN 光量子增幅器EV Tier
        GTCMItemList.PhotonControllerUpgradeEV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                3,
                new String[] { TextEnums.tr("PhotonControllerUpgradeEV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.4.name
        // # Photonic Intensifier IV Tier
        // #zh_CN 光量子增幅器IV Tier
        GTCMItemList.PhotonControllerUpgradeIV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                4,
                new String[] { TextEnums.tr("PhotonControllerUpgradeIV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.5.name
        // # Photonic Intensifier LuV Tier
        // #zh_CN 光量子增幅器LuV Tier
        GTCMItemList.PhotonControllerUpgradeLuV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                5,
                new String[] { TextEnums.tr("PhotonControllerUpgradeLuV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.6.name
        // # Photonic Intensifier ZPM Tier
        // #zh_CN 光量子增幅器ZPM Tier
        GTCMItemList.PhotonControllerUpgradeZPM.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                6,
                new String[] { TextEnums.tr("PhotonControllerUpgradeZPM.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.7.name
        // # Photonic Intensifier UV Tier
        // #zh_CN 光量子增幅器UV Tier
        GTCMItemList.PhotonControllerUpgradeUV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                7,
                new String[] { TextEnums.tr("PhotonControllerUpgradeUV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.8.name
        // # Photonic Intensifier UHV Tier
        // #zh_CN 光量子增幅器UHV Tier
        GTCMItemList.PhotonControllerUpgradeUHV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                8,
                new String[] { TextEnums.tr("PhotonControllerUpgradeUHV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.9.name
        // # Photonic Intensifier UEV Tier
        // #zh_CN 光量子增幅器UEV Tier
        GTCMItemList.PhotonControllerUpgradeUEV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                9,
                new String[] { TextEnums.tr("PhotonControllerUpgradeUEV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.10.name
        // # Photonic Intensifier UIV Tier
        // #zh_CN 光量子增幅器UIV Tier
        GTCMItemList.PhotonControllerUpgradeUIV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                10,
                new String[] { TextEnums.tr("PhotonControllerUpgradeUIV.tooltips.01") }));

        // #tr tile.PhotonControllerUpgrades.11.name
        // # Photonic Intensifier UMV Tier
        // #zh_CN 光量子增幅器UMV Tier
        GTCMItemList.PhotonControllerUpgradeUMV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                11,
                new String[] { TextEnums.tr("PhotonControllerUpgradeUMV.tooltips.01"),
                    TextEnums.tr("PhotonControllerUpgradeUMV.tooltips.02") }));

        // #tr tile.PhotonControllerUpgrades.12.name
        // # Photonic Intensifier UXV Tier
        // #zh_CN 光量子增幅器UXV Tier
        GTCMItemList.PhotonControllerUpgradeUXV.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                12,
                new String[] { TextEnums.tr("PhotonControllerUpgradeUXV.tooltips.01"),
                    TextEnums.tr("PhotonControllerUpgradeUXV.tooltips.02") }));

        // #tr tile.PhotonControllerUpgrades.13.name
        // # Photonic Intensifier MAX Tier
        // #zh_CN 光量子增幅器MAX Tier
        GTCMItemList.PhotonControllerUpgradeMAX.set(
            PhotonControllerUpgrade.registerVariantWithTooltips(
                13,
                new String[] { TextEnums.tr("PhotonControllerUpgradeMAX.tooltips.01"),
                    TextEnums.tr("PhotonControllerUpgradeMAX.tooltips.02") }));

        // endregion
        // ---------------------------------------------------------------------------------------------------------------------------//
        // region MegaSpaceStation
        if (Config.activateMegaSpaceStation) {

            // #tr tile.SpaceStationStructureBlock.0.name
            // # SpaceStationStructureBlock LV Tier
            // #zh_CN 空间站结构方块 LV Tier
            GTCMItemList.spaceStationStructureBlockLV.set(SpaceStationStructureBlock.registerVariant(0));

            // #tr tile.SpaceStationStructureBlock.1.name
            // # SpaceStationStructureBlock MV Tier
            // #zh_CN 空间站结构方块 MV Tier
            GTCMItemList.spaceStationStructureBlockMV.set(SpaceStationStructureBlock.registerVariant(1));

            // #tr tile.SpaceStationStructureBlock.2.name
            // # SpaceStationStructureBlock HV Tier
            // #zh_CN 空间站结构方块 HV Tier
            GTCMItemList.spaceStationStructureBlockHV.set(SpaceStationStructureBlock.registerVariant(2));

            // #tr tile.SpaceStationStructureBlock.3.name
            // # SpaceStationStructureBlock EV Tier
            // #zh_CN 空间站结构方块 EV Tier
            GTCMItemList.spaceStationStructureBlockEV.set(SpaceStationStructureBlock.registerVariant(3));

            // #tr tile.SpaceStationStructureBlock.4.name
            // # SpaceStationStructureBlock IV Tier
            // #zh_CN 空间站结构方块 IV Tier
            GTCMItemList.spaceStationStructureBlockIV.set(SpaceStationStructureBlock.registerVariant(4));

            // #tr tile.SpaceStationStructureBlock.5.name
            // # SpaceStationStructureBlock LuV Tier
            // #zh_CN 空间站结构方块 LuV Tier
            GTCMItemList.spaceStationStructureBlockLuV.set(SpaceStationStructureBlock.registerVariant(5));

            // #tr tile.SpaceStationStructureBlock.6.name
            // # SpaceStationStructureBlock ZPM Tier
            // #zh_CN 空间站结构方块 ZPM Tier
            GTCMItemList.spaceStationStructureBlockZPM.set(SpaceStationStructureBlock.registerVariant(6));

            // #tr tile.SpaceStationStructureBlock.7.name
            // # SpaceStationStructureBlock UV Tier
            // #zh_CN 空间站结构方块 UV Tier
            GTCMItemList.spaceStationStructureBlockUV.set(SpaceStationStructureBlock.registerVariant(7));

            // #tr tile.SpaceStationStructureBlock.8.name
            // # SpaceStationStructureBlock UHV Tier
            // #zh_CN 空间站结构方块 UHV Tier
            GTCMItemList.spaceStationStructureBlockUHV.set(SpaceStationStructureBlock.registerVariant(8));

            // #tr tile.SpaceStationStructureBlock.9.name
            // # SpaceStationStructureBlock UEV Tier
            // #zh_CN 空间站结构方块 UEV Tier
            GTCMItemList.spaceStationStructureBlockUEV.set(SpaceStationStructureBlock.registerVariant(9));

            // #tr tile.SpaceStationStructureBlock.10.name
            // # SpaceStationStructureBlock UIV Tier
            // #zh_CN 空间站结构方块 UIV Tier
            GTCMItemList.spaceStationStructureBlockUIV.set(SpaceStationStructureBlock.registerVariant(10));

            // #tr tile.SpaceStationStructureBlock.11.name
            // # SpaceStationStructureBlock UMV Tier
            // #zh_CN 空间站结构方块 UMV Tier
            GTCMItemList.spaceStationStructureBlockUMV.set(SpaceStationStructureBlock.registerVariant(11));

            // #tr tile.SpaceStationStructureBlock.12.name
            // # SpaceStationStructureBlock UXV Tier
            // #zh_CN 空间站结构方块 UXV Tier
            GTCMItemList.spaceStationStructureBlockUXV.set(SpaceStationStructureBlock.registerVariant(12));

            // #tr tile.SpaceStationStructureBlock.13.name
            // # SpaceStationStructureBlock MAX Tier
            // #zh_CN 空间站结构方块 MAX Tier
            GTCMItemList.spaceStationStructureBlockMAX.set(SpaceStationStructureBlock.registerVariant(13));

            // ----------------------------------------

            // #tr tile.SpaceStationAntiGravityBlock.0.name
            // # SpaceStationAntiGravityBlock LV Tier
            // #zh_CN 空间站反重力方块 Low Tier
            GTCMItemList.SpaceStationAntiGravityBlockLV.set(SpaceStationAntiGravityBlock.registerVariant(0));

            // #tr tile.SpaceStationAntiGravityBlock.1.name
            // # SpaceStationAntiGravityBlock MV Tier
            // #zh_CN 空间站反重力方块 Middle Tier
            GTCMItemList.SpaceStationAntiGravityBlockMV.set(SpaceStationAntiGravityBlock.registerVariant(1));

            // #tr tile.SpaceStationAntiGravityBlock.2.name
            // # SpaceStationAntiGravityBlock HV Tier
            // #zh_CN 空间站反重力方块 HV Tier
            GTCMItemList.SpaceStationAntiGravityBlockHV.set(SpaceStationAntiGravityBlock.registerVariant(2));

            // #tr tile.SpaceStationAntiGravityBlock.3.name
            // # SpaceStationAntiGravityBlock EV Tier
            // #zh_CN 空间站反重力方块 EV Tier
            GTCMItemList.SpaceStationAntiGravityBlockEV.set(SpaceStationAntiGravityBlock.registerVariant(3));

            // #tr tile.SpaceStationAntiGravityBlock.4.name
            // # SpaceStationAntiGravityBlock IV Tier
            // #zh_CN 空间站反重力方块 IV Tier
            GTCMItemList.SpaceStationAntiGravityBlockIV.set(SpaceStationAntiGravityBlock.registerVariant(4));

            // #tr tile.SpaceStationAntiGravityBlock.5.name
            // # SpaceStationAntiGravityBlock LuV Tier
            // #zh_CN 空间站反重力方块 LuV Tier
            GTCMItemList.SpaceStationAntiGravityBlockLuV.set(SpaceStationAntiGravityBlock.registerVariant(5));

            // #tr tile.SpaceStationAntiGravityBlock.6.name
            // # SpaceStationAntiGravityBlock ZPM Tier
            // #zh_CN 空间站反重力方块 ZPM Tier
            GTCMItemList.SpaceStationAntiGravityBlockZPM.set(SpaceStationAntiGravityBlock.registerVariant(6));

            // #tr tile.SpaceStationAntiGravityBlock.7.name
            // # SpaceStationAntiGravityBlock UV Tier
            // #zh_CN 空间站反重力方块 UV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUV.set(SpaceStationAntiGravityBlock.registerVariant(7));

            // #tr tile.SpaceStationAntiGravityBlock.8.name
            // # SpaceStationAntiGravityBlock UHV Tier
            // #zh_CN 空间站反重力方块 UHV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUHV.set(SpaceStationAntiGravityBlock.registerVariant(8));

            // #tr tile.SpaceStationAntiGravityBlock.9.name
            // # SpaceStationAntiGravityBlock UEV Tier
            // #zh_CN 空间站反重力方块 UEV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUEV.set(SpaceStationAntiGravityBlock.registerVariant(9));

            // #tr tile.SpaceStationAntiGravityBlock.10.name
            // # SpaceStationAntiGravityBlock UIV Tier
            // #zh_CN 空间站反重力方块 UIV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUIV.set(SpaceStationAntiGravityBlock.registerVariant(10));

            // #tr tile.SpaceStationAntiGravityBlock.11.name
            // # SpaceStationAntiGravityBlock UMV Tier
            // #zh_CN 空间站反重力方块 UMV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUMV.set(SpaceStationAntiGravityBlock.registerVariant(11));

            // #tr tile.SpaceStationAntiGravityBlock.12.name
            // # SpaceStationAntiGravityBlock UXV Tier
            // #zh_CN 空间站反重力方块 UXV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUXV.set(SpaceStationAntiGravityBlock.registerVariant(12));

            // #tr tile.SpaceStationAntiGravityBlock.13.name
            // # SpaceStationAntiGravityBlock MAX Tier
            // #zh_CN 空间站反重力方块 MAX Tier
            GTCMItemList.SpaceStationAntiGravityBlockMAX.set(SpaceStationAntiGravityBlock.registerVariant(13));

            // GTCMItemList.NuclearReactorStructure0.set(NuclearReactorBlockMeta("Nuclear Reactor structure block0",
            // 0));
            // GTCMItemList.NuclearReactorStructure1.set(NuclearReactorBlockMeta("Nuclear Reactor structure block1",
            // 1));
            // GTCMItemList.NuclearReactorStructure2.set(NuclearReactorBlockMeta("Nuclear Reactor structure block2",
            // 2));
            // GTCMItemList.NuclearReactorStructure3.set(NuclearReactorBlockMeta("Nuclear Reactor structure block3",
            // 3));
        }
        // endregion
    }

    public static void registry() {
        registryBlocks();
        registryBlockContainers();
    }
}
