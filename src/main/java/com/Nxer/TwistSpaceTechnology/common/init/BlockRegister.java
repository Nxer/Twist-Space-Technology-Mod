package com.Nxer.TwistSpaceTechnology.common.init;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.BlockArcaneHole;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.BlockStar;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlock01;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing02;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.NuclearReactorBlock;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.PhotonControllerUpgrade;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.SpaceStationAntiGravityBlock;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.SpaceStationStructureBlock;

import com.Nxer.TwistSpaceTechnology.common.block.BlockPowerChair;
import com.Nxer.TwistSpaceTechnology.common.item.blockItem.TstMetaBlockItem;
import com.Nxer.TwistSpaceTechnology.common.tile.TileArcaneHole;
import com.Nxer.TwistSpaceTechnology.common.tile.TilePowerChair;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStar;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRegister {

    public static void registryBlocks() {

        GameRegistry.registerBlock(MetaBlock01, TstMetaBlockItem.class, MetaBlock01.unlocalizedName);

        GameRegistry.registerBlock(
            TstBlocks.MetaBlockCasing01,
            TstMetaBlockItem.class,
            MetaBlockCasing01.unlocalizedName);

        GameRegistry.registerBlock(MetaBlockCasing02, TstMetaBlockItem.class, MetaBlockCasing02.unlocalizedName);

        GameRegistry.registerBlock(
            PhotonControllerUpgrade,
            TstMetaBlockItem.class,
            PhotonControllerUpgrade.unlocalizedName);

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
            SpaceStationStructureBlock,
            TstMetaBlockItem.class,
            SpaceStationStructureBlock.unlocalizedName);
        GameRegistry.registerBlock(
            SpaceStationAntiGravityBlock,
            TstMetaBlockItem.class,
            SpaceStationAntiGravityBlock.unlocalizedName);
        GameRegistry
            .registerBlock(NuclearReactorBlock, TstMetaBlockItem.class, NuclearReactorBlock.unlocalizedName);
        GameRegistry.registerBlock(BlockArcaneHole, "BlockArcaneHole");

        GameRegistry
            .registerBlock(TstBlocks.BlockPowerChair, BlockPowerChair.ItemBlockPowerChair.class, "BlockPowerChair");
        GameRegistry.registerBlock(BlockStar, BlockStar.unlocalizedName);
        GameRegistry.registerTileEntity(TileStar.class, "StarRender");
        GameRegistry.registerTileEntity(TilePowerChair.class, "TilePowerChair");
        GameRegistry.registerTileEntity(TileArcaneHole.class, "TileArcaneHole");

        GameRegistry.registerBlock(TstBlocks.TimeBendingSpeedRune, "TimeBendingSpeedRune");
    }

    public static void registryBlockContainers() {

        // #tr tile.MetaBlock01.0.name
        // # Test Block
        // #zh_CN 测试方块
        GTCMItemList.TestMetaBlock01_0.set(TstUtils.registerMetaBlockItemStack(MetaBlock01, 0));

        // #tr tile.MetaBlockCasing01.0.name
        // # Test Casing
        // #zh_CN Test Casing
        GTCMItemList.TestCasing.set(TstUtils.registerCasingBlockItemStack(MetaBlockCasing01, 0));

        // #tr tile.MetaBlockCasing01.1.name
        // # High Power Radiation Proof Casing
        // #zh_CN 高能防辐射机械方块
        GTCMItemList.HighPowerRadiationProofCasing.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                1,
                new String[] { TextEnums.tr("Tooltips_HighPowerRadiationProofCasing.01"),
                    TextEnums.tr("Tooltips_HighPowerRadiationProofCasing.02") }));

        // #tr tile.MetaBlockCasing01.2.name
        // # Advanced High Power Coil Block
        // #zh_CN 进阶高能线圈
        GTCMItemList.AdvancedHighPowerCoilBlock.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                2,
                new String[] { TextEnums.tr("Tooltips_AdvancedHighPowerCoil.01"),
                    TextEnums.tr("Tooltips_AdvancedHighPowerCoil.02") }));

        // #tr tile.MetaBlockCasing01.3.name
        // # Parallelism Casing Mark 0
        // #zh_CN 初等处理阵列并行机械方块
        GTCMItemList.ParallelismCasing0.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                3,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.3.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.3.name_1") }));

        // #tr tile.MetaBlockCasing01.4.name
        // # Parallelism Casing Mark 1
        // #zh_CN 进阶处理阵列并行机械方块
        GTCMItemList.ParallelismCasing1.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                4,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.4.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.4.name_1") }));

        // #tr tile.MetaBlockCasing01.5.name
        // # Parallelism Casing Mark 2
        // #zh_CN 高能处理阵列并行机械方块
        GTCMItemList.ParallelismCasing2.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                5,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.5.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.5.name_1") }));

        // #tr tile.MetaBlockCasing01.6.name
        // # Parallelism Casing Mark 3
        // #zh_CN 超能处理阵列并行机械方块
        GTCMItemList.ParallelismCasing3.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                6,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.6.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.6.name_1") }));

        // #tr tile.MetaBlockCasing01.7.name
        // # Parallelism Casing Mark 4
        // #zh_CN 寰宇处理阵列并行机械方块
        GTCMItemList.ParallelismCasing4.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                7,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.7.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.7.name_1") }));

        // #tr tile.MetaBlockCasing01.8.name
        // # Anti-Magnetic Casing
        // #zh_CN 抗磁机械方块
        GTCMItemList.AntiMagneticCasing.set(TstUtils.registerCasingBlockItemStack(MetaBlockCasing01, 8));

        // #tr tile.MetaBlockCasing01.9.name
        // # Reinforced Stone Brick Casing
        // #zh_CN 强化石砖机械方块
        // #tr Tooltip_ReinforcedStoneBrickCasing
        // # Just a stone?
        // #zh_CN 只是块石头?
        GTCMItemList.ReinforcedStoneBrickCasing.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                9,
                new String[] { TextEnums.tr("Tooltip_ReinforcedStoneBrickCasing") }));

        // #tr tile.MetaBlockCasing01.10.name
        // # Composite Farm Casing
        // #zh_CN 复合农场机械方块
        // #tr Tooltip_CompositeFarmCasing
        // # A force stronger than four combined.
        // #zh_CN 一个更比四个强
        GTCMItemList.CompositeFarmCasing.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                10,
                new String[] { TextEnums.tr("Tooltip_CompositeFarmCasing") }));

        // #tr tile.MetaBlockCasing01.11.name
        // # Dense Cyclotron Outer Casing
        // #zh_CN 致密回旋加速器机械方块
        GTCMItemList.DenseCyclotronOuterCasing.set(TstUtils.registerCasingBlockItemStack(MetaBlockCasing01, 11));

        // #tr tile.MetaBlockCasing01.12.name
        // # Compact Cyclotron Coil
        // #zh_CN 压缩回旋加速器线圈
        GTCMItemList.CompactCyclotronCoil.set(TstUtils.registerCasingBlockItemStack(MetaBlockCasing01, 12));

        // #tr tile.MetaBlockCasing01.13.name
        // # Aseptic Greenhouse Casing
        // #zh_CN 无菌温室机械方块
        // #tr Tooltip_AsepticGreenhouseCasing
        // # Absolutely Clean!
        // #zh_CN 一尘不染!
        GTCMItemList.AsepticGreenhouseCasing.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                13,
                new String[] { TextEnums.tr("Tooltip_AsepticGreenhouseCasing") }));

        // #tr tile.MetaBlockCasing01.14.name
        // # Reinforced Bedrock Casing
        // #zh_CN 强化基岩机械方块
        // #tr Tooltip_ReinforcedBedrockCasing
        // # Stronger than bedrock!
        // #zh_CN 比磐石更坚！
        GTCMItemList.ReinforcedBedrockCasing.set(
            TstUtils.registerCasingBlockItemStack(
                MetaBlockCasing01,
                14,
                new String[] { TextEnums.tr("Tooltip_ReinforcedBedrockCasing") }));

        // #tr tile.MetaBlockCasing02.0.name
        // # Gore Casing
        // #zh_CN 凝血机械方块
        GTCMItemList.BloodyCasing1.set(TstUtils.registerCasingBlockItemStack(MetaBlockCasing02, 0));

        // #tr tile.MetaBlockCasing02.1.name
        // # Ichor Draconic Block
        // #zh_CN 血腥龙块
        GTCMItemList.BloodyCasing2.set(TstUtils.registerCasingBlockItemStack(MetaBlockCasing02, 1));

        // region SpaceTimeOscillator

        // #tr tile.SpaceTimeOscillator.0.name
        // # SpaceTime Oscillator T1
        // #zh_CN 时空振荡器T1
        GTCMItemList.SpaceTimeOscillatorT1.set(TstUtils.registerMetaBlockItemStack(TstBlocks.SpaceTimeOscillator, 0));

        // #tr tile.SpaceTimeOscillator.1.name
        // # SpaceTime Oscillator T2
        // #zh_CN 时空振荡器T2
        GTCMItemList.SpaceTimeOscillatorT2.set(TstUtils.registerMetaBlockItemStack(TstBlocks.SpaceTimeOscillator, 1));

        // #tr tile.SpaceTimeOscillator.2.name
        // # SpaceTime Oscillator T3
        // #zh_CN 时空振荡器T3
        GTCMItemList.SpaceTimeOscillatorT3.set(TstUtils.registerMetaBlockItemStack(TstBlocks.SpaceTimeOscillator, 2));

        // endregion

        // region SpaceTimeConstraintor

        // #tr tile.SpaceTimeConstraintor.0.name
        // # SpaceTime Constraintor T1
        // #zh_CN 时空约束器T1
        GTCMItemList.SpaceTimeConstraintorT1
            .set(TstUtils.registerMetaBlockItemStack(TstBlocks.SpaceTimeConstraintor, 0));

        // #tr tile.SpaceTimeConstraintor.1.name
        // # SpaceTime Constraintor T2
        // #zh_CN 时空约束器T2
        GTCMItemList.SpaceTimeConstraintorT2
            .set(TstUtils.registerMetaBlockItemStack(TstBlocks.SpaceTimeConstraintor, 1));

        // #tr tile.SpaceTimeConstraintor.2.name
        // # SpaceTime Constraintor T3
        // #zh_CN 时空约束器T3
        GTCMItemList.SpaceTimeConstraintorT3
            .set(TstUtils.registerMetaBlockItemStack(TstBlocks.SpaceTimeConstraintor, 2));

        // endregion

        // region SpaceTimeMerger

        // #tr tile.SpaceTimeMerger.0.name
        // # SpaceTime Merger T1
        // #zh_CN 时空归并器T1
        GTCMItemList.SpaceTimeMergerT1.set(TstUtils.registerMetaBlockItemStack(TstBlocks.SpaceTimeMerger, 0));

        // #tr tile.SpaceTimeMerger.1.name
        // # SpaceTime Merger T2
        // #zh_CN 时空归并器T2
        GTCMItemList.SpaceTimeMergerT2.set(TstUtils.registerMetaBlockItemStack(TstBlocks.SpaceTimeMerger, 1));

        // #tr tile.SpaceTimeMerger.2.name
        // # SpaceTime Merger T3
        // #zh_CN 时空归并器T3
        GTCMItemList.SpaceTimeMergerT3.set(TstUtils.registerMetaBlockItemStack(TstBlocks.SpaceTimeMerger, 2));

        // endregion

        // region PhotonControllerUpgrade

        // #tr tile.PhotonControllerUpgrades.0.name
        // # Photonic Intensifier LV Tier
        // #zh_CN 光量子增幅器LV Tier
        GTCMItemList.PhotonControllerUpgradeLV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 0));

        // #tr tile.PhotonControllerUpgrades.1.name
        // # Photonic Intensifier MV Tier
        // #zh_CN 光量子增幅器MV Tier
        GTCMItemList.PhotonControllerUpgradeMV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 1));

        // #tr tile.PhotonControllerUpgrades.2.name
        // # Photonic Intensifier HV Tier
        // #zh_CN 光量子增幅器HV Tier
        GTCMItemList.PhotonControllerUpgradeHV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 2));

        // #tr tile.PhotonControllerUpgrades.3.name
        // # Photonic Intensifier EV Tier
        // #zh_CN 光量子增幅器EV Tier
        GTCMItemList.PhotonControllerUpgradeEV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 3));

        // #tr tile.PhotonControllerUpgrades.4.name
        // # Photonic Intensifier IV Tier
        // #zh_CN 光量子增幅器IV Tier
        GTCMItemList.PhotonControllerUpgradeIV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 4));

        // #tr tile.PhotonControllerUpgrades.5.name
        // # Photonic Intensifier LuV Tier
        // #zh_CN 光量子增幅器LuV Tier
        GTCMItemList.PhotonControllerUpgradeLuV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 5));

        // #tr tile.PhotonControllerUpgrades.6.name
        // # Photonic Intensifier ZPM Tier
        // #zh_CN 光量子增幅器ZPM Tier
        GTCMItemList.PhotonControllerUpgradeZPM.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 6));

        // #tr tile.PhotonControllerUpgrades.7.name
        // # Photonic Intensifier UV Tier
        // #zh_CN 光量子增幅器UV Tier
        GTCMItemList.PhotonControllerUpgradeUV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 7));

        // #tr tile.PhotonControllerUpgrades.8.name
        // # Photonic Intensifier UHV Tier
        // #zh_CN 光量子增幅器UHV Tier
        GTCMItemList.PhotonControllerUpgradeUHV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 8));

        // #tr tile.PhotonControllerUpgrades.9.name
        // # Photonic Intensifier UEV Tier
        // #zh_CN 光量子增幅器UEV Tier
        GTCMItemList.PhotonControllerUpgradeUEV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 9));

        // #tr tile.PhotonControllerUpgrades.10.name
        // # Photonic Intensifier UIV Tier
        // #zh_CN 光量子增幅器UIV Tier
        GTCMItemList.PhotonControllerUpgradeUIV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 10));

        // #tr tile.PhotonControllerUpgrades.11.name
        // # Photonic Intensifier UMV Tier
        // #zh_CN 光量子增幅器UMV Tier
        GTCMItemList.PhotonControllerUpgradeUMV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 11));

        // #tr tile.PhotonControllerUpgrades.12.name
        // # Photonic Intensifier UXV Tier
        // #zh_CN 光量子增幅器UXV Tier
        GTCMItemList.PhotonControllerUpgradeUXV.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 12));

        // #tr tile.PhotonControllerUpgrades.13.name
        // # Photonic Intensifier MAX Tier
        // #zh_CN 光量子增幅器MAX Tier
        GTCMItemList.PhotonControllerUpgradeMAX.set(TstUtils.registerMetaBlockItemStack(PhotonControllerUpgrade, 13));

        // endregion
        // ---------------------------------------------------------------------------------------------------------------------------//
        // region MegaSpaceStation
        if (Config.activateMegaSpaceStation) {

            // #tr tile.SpaceStationStructureBlock.0.name
            // # SpaceStationStructureBlock LV Tier
            // #zh_CN 空间站结构方块 LV Tier
            GTCMItemList.spaceStationStructureBlockLV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 0));

            // #tr tile.SpaceStationStructureBlock.1.name
            // # SpaceStationStructureBlock MV Tier
            // #zh_CN 空间站结构方块 MV Tier
            GTCMItemList.spaceStationStructureBlockMV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 1));

            // #tr tile.SpaceStationStructureBlock.2.name
            // # SpaceStationStructureBlock HV Tier
            // #zh_CN 空间站结构方块 HV Tier
            GTCMItemList.spaceStationStructureBlockHV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 2));

            // #tr tile.SpaceStationStructureBlock.3.name
            // # SpaceStationStructureBlock EV Tier
            // #zh_CN 空间站结构方块 EV Tier
            GTCMItemList.spaceStationStructureBlockEV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 3));

            // #tr tile.SpaceStationStructureBlock.4.name
            // # SpaceStationStructureBlock IV Tier
            // #zh_CN 空间站结构方块 IV Tier
            GTCMItemList.spaceStationStructureBlockIV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 4));

            // #tr tile.SpaceStationStructureBlock.5.name
            // # SpaceStationStructureBlock LuV Tier
            // #zh_CN 空间站结构方块 LuV Tier
            GTCMItemList.spaceStationStructureBlockLuV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 5));

            // #tr tile.SpaceStationStructureBlock.6.name
            // # SpaceStationStructureBlock ZPM Tier
            // #zh_CN 空间站结构方块 ZPM Tier
            GTCMItemList.spaceStationStructureBlockZPM
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 6));

            // #tr tile.SpaceStationStructureBlock.7.name
            // # SpaceStationStructureBlock UV Tier
            // #zh_CN 空间站结构方块 UV Tier
            GTCMItemList.spaceStationStructureBlockUV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 7));

            // #tr tile.SpaceStationStructureBlock.8.name
            // # SpaceStationStructureBlock UHV Tier
            // #zh_CN 空间站结构方块 UHV Tier
            GTCMItemList.spaceStationStructureBlockUHV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 8));

            // #tr tile.SpaceStationStructureBlock.9.name
            // # SpaceStationStructureBlock UEV Tier
            // #zh_CN 空间站结构方块 UEV Tier
            GTCMItemList.spaceStationStructureBlockUEV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 9));

            // #tr tile.SpaceStationStructureBlock.10.name
            // # SpaceStationStructureBlock UIV Tier
            // #zh_CN 空间站结构方块 UIV Tier
            GTCMItemList.spaceStationStructureBlockUIV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 10));

            // #tr tile.SpaceStationStructureBlock.11.name
            // # SpaceStationStructureBlock UMV Tier
            // #zh_CN 空间站结构方块 UMV Tier
            GTCMItemList.spaceStationStructureBlockUMV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 11));

            // #tr tile.SpaceStationStructureBlock.12.name
            // # SpaceStationStructureBlock UXV Tier
            // #zh_CN 空间站结构方块 UXV Tier
            GTCMItemList.spaceStationStructureBlockUXV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 12));

            // #tr tile.SpaceStationStructureBlock.13.name
            // # SpaceStationStructureBlock MAX Tier
            // #zh_CN 空间站结构方块 MAX Tier
            GTCMItemList.spaceStationStructureBlockMAX
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationStructureBlock, 13));

            // ----------------------------------------

            // #tr tile.SpaceStationAntiGravityBlock.0.name
            // # SpaceStationAntiGravityBlock LV Tier
            // #zh_CN 空间站反重力方块 Low Tier
            GTCMItemList.SpaceStationAntiGravityBlockLV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 0));

            // #tr tile.SpaceStationAntiGravityBlock.1.name
            // # SpaceStationAntiGravityBlock MV Tier
            // #zh_CN 空间站反重力方块 Middle Tier
            GTCMItemList.SpaceStationAntiGravityBlockMV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 1));

            // #tr tile.SpaceStationAntiGravityBlock.2.name
            // # SpaceStationAntiGravityBlock HV Tier
            // #zh_CN 空间站反重力方块 HV Tier
            GTCMItemList.SpaceStationAntiGravityBlockHV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 2));

            // #tr tile.SpaceStationAntiGravityBlock.3.name
            // # SpaceStationAntiGravityBlock EV Tier
            // #zh_CN 空间站反重力方块 EV Tier
            GTCMItemList.SpaceStationAntiGravityBlockEV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 3));

            // #tr tile.SpaceStationAntiGravityBlock.4.name
            // # SpaceStationAntiGravityBlock IV Tier
            // #zh_CN 空间站反重力方块 IV Tier
            GTCMItemList.SpaceStationAntiGravityBlockIV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 4));

            // #tr tile.SpaceStationAntiGravityBlock.5.name
            // # SpaceStationAntiGravityBlock LuV Tier
            // #zh_CN 空间站反重力方块 LuV Tier
            GTCMItemList.SpaceStationAntiGravityBlockLuV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 5));

            // #tr tile.SpaceStationAntiGravityBlock.6.name
            // # SpaceStationAntiGravityBlock ZPM Tier
            // #zh_CN 空间站反重力方块 ZPM Tier
            GTCMItemList.SpaceStationAntiGravityBlockZPM
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 6));

            // #tr tile.SpaceStationAntiGravityBlock.7.name
            // # SpaceStationAntiGravityBlock UV Tier
            // #zh_CN 空间站反重力方块 UV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 7));

            // #tr tile.SpaceStationAntiGravityBlock.8.name
            // # SpaceStationAntiGravityBlock UHV Tier
            // #zh_CN 空间站反重力方块 UHV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUHV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 8));

            // #tr tile.SpaceStationAntiGravityBlock.9.name
            // # SpaceStationAntiGravityBlock UEV Tier
            // #zh_CN 空间站反重力方块 UEV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUEV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 9));

            // #tr tile.SpaceStationAntiGravityBlock.10.name
            // # SpaceStationAntiGravityBlock UIV Tier
            // #zh_CN 空间站反重力方块 UIV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUIV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 10));

            // #tr tile.SpaceStationAntiGravityBlock.11.name
            // # SpaceStationAntiGravityBlock UMV Tier
            // #zh_CN 空间站反重力方块 UMV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUMV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 11));

            // #tr tile.SpaceStationAntiGravityBlock.12.name
            // # SpaceStationAntiGravityBlock UXV Tier
            // #zh_CN 空间站反重力方块 UXV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUXV
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 12));

            // #tr tile.SpaceStationAntiGravityBlock.13.name
            // # SpaceStationAntiGravityBlock MAX Tier
            // #zh_CN 空间站反重力方块 MAX Tier
            GTCMItemList.SpaceStationAntiGravityBlockMAX
                .set(TstUtils.registerMetaBlockItemStack(SpaceStationAntiGravityBlock, 13));

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
