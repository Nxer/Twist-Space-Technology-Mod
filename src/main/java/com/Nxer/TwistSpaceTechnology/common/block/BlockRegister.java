package com.Nxer.TwistSpaceTechnology.common.block;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.BlockArcaneHole;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.BlockStar;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.MetaBlock01;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.MetaBlockCasing02;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.NuclearReactorBlock;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.PhotonControllerUpgrade;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.SpaceStationAntiGravityBlock;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.SpaceStationStructureBlock;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockStar;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.MetaItemBlockCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.PhotonControllerUpgradeCasingItemBlock;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.nuclear.BlockNuclearReactor;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationAntiGravityCasingItemBlock;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationStructureCasingItemBlock;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockPowerChair;
import com.Nxer.TwistSpaceTechnology.common.tile.TileArcaneHole;
import com.Nxer.TwistSpaceTechnology.common.tile.TilePowerChair;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStar;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BlockRegister {

    public static void registryBlocks() {

        GameRegistry.registerBlock(MetaBlock01, ItemBlockBase01.class, MetaBlock01.getUnlocalizedName());

        GameRegistry.registerBlock(
            BasicBlocks.MetaBlockCasing01,
            MetaItemBlockCasing.class,
            BasicBlocks.MetaBlockCasing01.getUnlocalizedName());

        GameRegistry
            .registerBlock(MetaBlockCasing02, MetaItemBlockCasing.class, MetaBlockCasing02.getUnlocalizedName());

        GameRegistry.registerBlock(
            PhotonControllerUpgrade,
            PhotonControllerUpgradeCasingItemBlock.class,
            PhotonControllerUpgrade.getUnlocalizedName());

        GameRegistry.registerBlock(
            BasicBlocks.SpaceTimeOscillator,
            MetaItemBlockCasing.class,
            BasicBlocks.SpaceTimeOscillator.getUnlocalizedName());
        GameRegistry.registerBlock(
            BasicBlocks.SpaceTimeConstraintor,
            MetaItemBlockCasing.class,
            BasicBlocks.SpaceTimeConstraintor.getUnlocalizedName());
        GameRegistry.registerBlock(
            BasicBlocks.SpaceTimeMerger,
            MetaItemBlockCasing.class,
            BasicBlocks.SpaceTimeMerger.getUnlocalizedName());

        GameRegistry.registerBlock(
            SpaceStationStructureBlock,
            SpaceStationStructureCasingItemBlock.class,
            SpaceStationStructureBlock.getUnlocalizedName());
        GameRegistry.registerBlock(
            SpaceStationAntiGravityBlock,
            SpaceStationAntiGravityCasingItemBlock.class,
            SpaceStationAntiGravityBlock.getUnlocalizedName());
        GameRegistry.registerBlock(
            NuclearReactorBlock,
            BlockNuclearReactor.innerItemBlock.class,
            NuclearReactorBlock.getUnlocalizedName());
        GameRegistry.registerBlock(BlockArcaneHole, "BlockArcaneHole");

        GameRegistry.registerBlock(BasicBlocks.BlockPowerChair, ItemBlockPowerChair.class, "BlockPowerChair");
        BlockStar = new BlockStar();
        GameRegistry.registerTileEntity(TileStar.class, "StarRender");
        GameRegistry.registerTileEntity(TilePowerChair.class, "TilePowerChair");
        GameRegistry.registerTileEntity(TileArcaneHole.class, "TileArcaneHole");

        GameRegistry.registerBlock(BasicBlocks.timeBendingSpeedRune, "TimeBendingSpeedRune");
    }

    public static void registryBlockContainers() {

        // #tr tile.MetaBlock01.0.name
        // # Test Block
        // #zh_CN 测试方块
        GTCMItemList.TestMetaBlock01_0.set(ItemUtils.simpleMetaStack(MetaBlock01, 0, 1));

        // #tr tile.MetaBlockCasing01.0.name
        // # Test Casing
        // #zh_CN Test Casing
        GTCMItemList.TestCasing.set(TstUtils.newItemWithMeta(MetaBlockCasing01, 0));

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
        GTCMItemList.SpaceTimeOscillatorT1.set(TstUtils.newMetaBlockItemStack(BasicBlocks.SpaceTimeOscillator, 0));

        // #tr SpaceTimeOscillator.1.name
        // # SpaceTime Oscillator T2
        // #zh_CN 时空振荡器T2
        GTCMItemList.SpaceTimeOscillatorT2.set(TstUtils.newMetaBlockItemStack(BasicBlocks.SpaceTimeOscillator, 1));

        // #tr tile.SpaceTimeOscillator.2.name
        // # SpaceTime Oscillator T3
        // #zh_CN 时空振荡器T3
        GTCMItemList.SpaceTimeOscillatorT3.set(TstUtils.newMetaBlockItemStack(BasicBlocks.SpaceTimeOscillator, 2));

        // endregion

        // region SpaceTimeConstraintor

        // #tr tile.SpaceTimeConstraintor.0.name
        // # SpaceTime Constraintor T1
        // #zh_CN 时空约束器T1
        GTCMItemList.SpaceTimeConstraintorT1.set(TstUtils.newMetaBlockItemStack(BasicBlocks.SpaceTimeConstraintor, 0));

        // #tr tile.SpaceTimeConstraintor.1.name
        // # SpaceTime Constraintor T2
        // #zh_CN 时空约束器T2
        GTCMItemList.SpaceTimeConstraintorT2.set(TstUtils.newMetaBlockItemStack(BasicBlocks.SpaceTimeConstraintor, 1));

        // #tr tile.SpaceTimeConstraintor.2.name
        // # SpaceTime Constraintor T3
        // #zh_CN 时空约束器T3
        GTCMItemList.SpaceTimeConstraintorT3.set(TstUtils.newMetaBlockItemStack(BasicBlocks.SpaceTimeConstraintor, 2));

        // endregion

        // region SpaceTimeMerger

        // #tr tile.SpaceTimeMerger.0.name
        // # SpaceTime Merger T1
        // #zh_CN 时空归并器T1
        GTCMItemList.SpaceTimeMergerT1.set(TstUtils.newMetaBlockItemStack(BasicBlocks.SpaceTimeMerger, 0));

        // #tr tile.SpaceTimeMerger.1.name
        // # SpaceTime Merger T2
        // #zh_CN 时空归并器T2
        GTCMItemList.SpaceTimeMergerT2.set(TstUtils.newMetaBlockItemStack(BasicBlocks.SpaceTimeMerger, 1));

        // #tr tile.SpaceTimeMerger.2.name
        // # SpaceTime Merger T3
        // #zh_CN 时空归并器T3
        GTCMItemList.SpaceTimeMergerT3.set(TstUtils.newMetaBlockItemStack(BasicBlocks.SpaceTimeMerger, 2));

        // endregion

        // region PhotonControllerUpgrade

        // #tr tile.PhotonControllerUpgrades.0.name
        // # Photonic Intensifier LV Tier
        // #zh_CN 光量子增幅器LV Tier
        GTCMItemList.PhotonControllerUpgradeLV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 0));

        // #tr tile.PhotonControllerUpgrades.1.name
        // # Photonic Intensifier MV Tier
        // #zh_CN 光量子增幅器MV Tier
        GTCMItemList.PhotonControllerUpgradeMV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 1));

        // #tr tile.PhotonControllerUpgrades.2.name
        // # Photonic Intensifier HV Tier
        // #zh_CN 光量子增幅器HV Tier
        GTCMItemList.PhotonControllerUpgradeHV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 2));

        // #tr tile.PhotonControllerUpgrades.3.name
        // # Photonic Intensifier EV Tier
        // #zh_CN 光量子增幅器EV Tier
        GTCMItemList.PhotonControllerUpgradeEV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 3));

        // #tr tile.PhotonControllerUpgrades.4.name
        // # Photonic Intensifier IV Tier
        // #zh_CN 光量子增幅器IV Tier
        GTCMItemList.PhotonControllerUpgradeIV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 4));

        // #tr tile.PhotonControllerUpgrades.5.name
        // # Photonic Intensifier LuV Tier
        // #zh_CN 光量子增幅器LuV Tier
        GTCMItemList.PhotonControllerUpgradeLuV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 5));

        // #tr tile.PhotonControllerUpgrades.6.name
        // # Photonic Intensifier ZPM Tier
        // #zh_CN 光量子增幅器ZPM Tier
        GTCMItemList.PhotonControllerUpgradeZPM.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 6));

        // #tr tile.PhotonControllerUpgrades.7.name
        // # Photonic Intensifier UV Tier
        // #zh_CN 光量子增幅器UV Tier
        GTCMItemList.PhotonControllerUpgradeUV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 7));

        // #tr tile.PhotonControllerUpgrades.8.name
        // # Photonic Intensifier UHV Tier
        // #zh_CN 光量子增幅器UHV Tier
        GTCMItemList.PhotonControllerUpgradeUHV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 8));

        // #tr tile.PhotonControllerUpgrades.9.name
        // # Photonic Intensifier UEV Tier
        // #zh_CN 光量子增幅器UEV Tier
        GTCMItemList.PhotonControllerUpgradeUEV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 9));

        // #tr tile.PhotonControllerUpgrades.10.name
        // # Photonic Intensifier UIV Tier
        // #zh_CN 光量子增幅器UIV Tier
        GTCMItemList.PhotonControllerUpgradeUIV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 10));

        // #tr tile.PhotonControllerUpgrades.11.name
        // # Photonic Intensifier UMV Tier
        // #zh_CN 光量子增幅器UMV Tier
        GTCMItemList.PhotonControllerUpgradeUMV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 11));

        // #tr tile.PhotonControllerUpgrades.12.name
        // # Photonic Intensifier UXV Tier
        // #zh_CN 光量子增幅器UXV Tier
        GTCMItemList.PhotonControllerUpgradeUXV.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 12));

        // #tr tile.PhotonControllerUpgrades.13.name
        // # Photonic Intensifier MAX Tier
        // #zh_CN 光量子增幅器MAX Tier
        GTCMItemList.PhotonControllerUpgradeMAX.set(TstUtils.newMetaBlockItemStackUnsafe(PhotonControllerUpgrade, 13));

        // endregion
        // ---------------------------------------------------------------------------------------------------------------------------//
        // region MegaSpaceStation
        if (Config.activateMegaSpaceStation) {

            // #tr tile.SpaceStationStructureBlock.0.name
            // # SpaceStationStructureBlock LV Tier
            // #zh_CN 空间站结构方块 LV Tier
            GTCMItemList.spaceStationStructureBlockLV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 0));

            // #tr tile.SpaceStationStructureBlock.1.name
            // # SpaceStationStructureBlock MV Tier
            // #zh_CN 空间站结构方块 MV Tier
            GTCMItemList.spaceStationStructureBlockMV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 1));

            // #tr tile.SpaceStationStructureBlock.2.name
            // # SpaceStationStructureBlock HV Tier
            // #zh_CN 空间站结构方块 HV Tier
            GTCMItemList.spaceStationStructureBlockHV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 2));

            // #tr tile.SpaceStationStructureBlock.3.name
            // # SpaceStationStructureBlock EV Tier
            // #zh_CN 空间站结构方块 EV Tier
            GTCMItemList.spaceStationStructureBlockEV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 3));

            // #tr tile.SpaceStationStructureBlock.4.name
            // # SpaceStationStructureBlock IV Tier
            // #zh_CN 空间站结构方块 IV Tier
            GTCMItemList.spaceStationStructureBlockIV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 4));

            // #tr tile.SpaceStationStructureBlock.5.name
            // # SpaceStationStructureBlock LuV Tier
            // #zh_CN 空间站结构方块 LuV Tier
            GTCMItemList.spaceStationStructureBlockLuV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 5));

            // #tr tile.SpaceStationStructureBlock.6.name
            // # SpaceStationStructureBlock ZPM Tier
            // #zh_CN 空间站结构方块 ZPM Tier
            GTCMItemList.spaceStationStructureBlockZPM
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 6));

            // #tr tile.SpaceStationStructureBlock.7.name
            // # SpaceStationStructureBlock UV Tier
            // #zh_CN 空间站结构方块 UV Tier
            GTCMItemList.spaceStationStructureBlockUV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 7));

            // #tr tile.SpaceStationStructureBlock.8.name
            // # SpaceStationStructureBlock UHV Tier
            // #zh_CN 空间站结构方块 UHV Tier
            GTCMItemList.spaceStationStructureBlockUHV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 8));

            // #tr tile.SpaceStationStructureBlock.9.name
            // # SpaceStationStructureBlock UEV Tier
            // #zh_CN 空间站结构方块 UEV Tier
            GTCMItemList.spaceStationStructureBlockUEV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 9));

            // #tr tile.SpaceStationStructureBlock.10.name
            // # SpaceStationStructureBlock UIV Tier
            // #zh_CN 空间站结构方块 UIV Tier
            GTCMItemList.spaceStationStructureBlockUIV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 10));

            // #tr tile.SpaceStationStructureBlock.11.name
            // # SpaceStationStructureBlock UMV Tier
            // #zh_CN 空间站结构方块 UMV Tier
            GTCMItemList.spaceStationStructureBlockUMV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 11));

            // #tr tile.SpaceStationStructureBlock.12.name
            // # SpaceStationStructureBlock UXV Tier
            // #zh_CN 空间站结构方块 UXV Tier
            GTCMItemList.spaceStationStructureBlockUXV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 12));

            // #tr tile.SpaceStationStructureBlock.13.name
            // # SpaceStationStructureBlock MAX Tier
            // #zh_CN 空间站结构方块 MAX Tier
            GTCMItemList.spaceStationStructureBlockMAX
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationStructureBlock, 13));

            // ----------------------------------------

            // #tr tile.SpaceStationAntiGravityBlock.0.name
            // # SpaceStationAntiGravityBlock LV Tier
            // #zh_CN 空间站反重力方块 Low Tier
            GTCMItemList.SpaceStationAntiGravityBlockLV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 0));

            // #tr tile.SpaceStationAntiGravityBlock.1.name
            // # SpaceStationAntiGravityBlock MV Tier
            // #zh_CN 空间站反重力方块 Middle Tier
            GTCMItemList.SpaceStationAntiGravityBlockMV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 1));

            // #tr tile.SpaceStationAntiGravityBlock.2.name
            // # SpaceStationAntiGravityBlock HV Tier
            // #zh_CN 空间站反重力方块 HV Tier
            GTCMItemList.SpaceStationAntiGravityBlockHV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 2));

            // #tr tile.SpaceStationAntiGravityBlock.3.name
            // # SpaceStationAntiGravityBlock EV Tier
            // #zh_CN 空间站反重力方块 EV Tier
            GTCMItemList.SpaceStationAntiGravityBlockEV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 3));

            // #tr tile.SpaceStationAntiGravityBlock.4.name
            // # SpaceStationAntiGravityBlock IV Tier
            // #zh_CN 空间站反重力方块 IV Tier
            GTCMItemList.SpaceStationAntiGravityBlockIV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 4));

            // #tr tile.SpaceStationAntiGravityBlock.5.name
            // # SpaceStationAntiGravityBlock LuV Tier
            // #zh_CN 空间站反重力方块 LuV Tier
            GTCMItemList.SpaceStationAntiGravityBlockLuV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 5));

            // #tr tile.SpaceStationAntiGravityBlock.6.name
            // # SpaceStationAntiGravityBlock ZPM Tier
            // #zh_CN 空间站反重力方块 ZPM Tier
            GTCMItemList.SpaceStationAntiGravityBlockZPM
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 6));

            // #tr tile.SpaceStationAntiGravityBlock.7.name
            // # SpaceStationAntiGravityBlock UV Tier
            // #zh_CN 空间站反重力方块 UV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 7));

            // #tr tile.SpaceStationAntiGravityBlock.8.name
            // # SpaceStationAntiGravityBlock UHV Tier
            // #zh_CN 空间站反重力方块 UHV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUHV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 8));

            // #tr tile.SpaceStationAntiGravityBlock.9.name
            // # SpaceStationAntiGravityBlock UEV Tier
            // #zh_CN 空间站反重力方块 UEV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUEV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 9));

            // #tr tile.SpaceStationAntiGravityBlock.10.name
            // # SpaceStationAntiGravityBlock UIV Tier
            // #zh_CN 空间站反重力方块 UIV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUIV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 10));

            // #tr tile.SpaceStationAntiGravityBlock.11.name
            // # SpaceStationAntiGravityBlock UMV Tier
            // #zh_CN 空间站反重力方块 UMV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUMV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 11));

            // #tr tile.SpaceStationAntiGravityBlock.12.name
            // # SpaceStationAntiGravityBlock UXV Tier
            // #zh_CN 空间站反重力方块 UXV Tier
            GTCMItemList.SpaceStationAntiGravityBlockUXV
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 12));

            // #tr tile.SpaceStationAntiGravityBlock.13.name
            // # SpaceStationAntiGravityBlock MAX Tier
            // #zh_CN 空间站反重力方块 MAX Tier
            GTCMItemList.SpaceStationAntiGravityBlockMAX
                .set(TstUtils.newMetaBlockItemStackUnsafe(SpaceStationAntiGravityBlock, 13));

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
