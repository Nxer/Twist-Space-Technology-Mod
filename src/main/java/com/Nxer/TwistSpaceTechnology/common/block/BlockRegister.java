package com.Nxer.TwistSpaceTechnology.common.block;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.BlockStar;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.MetaBlock01;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.NuclearReactorBlock;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.PhotonControllerUpgrade;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.SpaceStationAntiGravityBlock;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.spaceStationStructureBlock;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockStar;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.MetaItemBlockCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.PhotonControllerUpgradeCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.PhotonControllerUpgradeCasingItemBlock;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.nuclear.BlockNuclearReactor;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationAntiGravityCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationAntiGravityCasingItemBlock;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationStructureCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationStructureCasingItemBlock;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockPowerChair;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.MetaBlockBase;
import com.Nxer.TwistSpaceTechnology.common.tile.TilePowerChair;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStar;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRegister {

    public static void registryBlocks() {

        GameRegistry.registerBlock(MetaBlock01, ItemBlockBase01.class, MetaBlock01.getUnlocalizedName());

        GameRegistry.registerBlock(
            BasicBlocks.MetaBlockCasing01,
            MetaItemBlockCasing.class,
            BasicBlocks.MetaBlockCasing01.getUnlocalizedName());

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
            spaceStationStructureBlock,
            SpaceStationStructureCasingItemBlock.class,
            spaceStationStructureBlock.getUnlocalizedName());
        GameRegistry.registerBlock(
            SpaceStationAntiGravityBlock,
            SpaceStationAntiGravityCasingItemBlock.class,
            SpaceStationAntiGravityBlock.getUnlocalizedName());
        GameRegistry.registerBlock(
            NuclearReactorBlock,
            BlockNuclearReactor.innerItemBlock.class,
            NuclearReactorBlock.getUnlocalizedName());

        GameRegistry.registerBlock(BasicBlocks.BlockPowerChair, ItemBlockPowerChair.class, "BlockPowerChair");
        BlockStar = new BlockStar();
        GameRegistry.registerTileEntity(TileStar.class, "StarRender");
        GameRegistry.registerTileEntity(TilePowerChair.class, "TilePowerChair");
    }

    public static void registryBlockContainers() {

        GTCMItemList.TestMetaBlock01_0.set(ItemBlockBase01.initMetaBlock01("TestMetaBlock01_0", 0));
        GTCMItemList.TestCasing
            .set(MetaBlockConstructors.initMetaBlockCasing("Test Casing", (byte) 0, BasicBlocks.MetaBlockCasing01));
        GTCMItemList.HighPowerRadiationProofCasing.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "High Power Radiation Proof Casing",
                (byte) 1,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltips_HighPowerRadiationProofCasing.01"),
                    TextEnums.tr("Tooltips_HighPowerRadiationProofCasing.02") }));

        GTCMItemList.AdvancedHighPowerCoilBlock.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Advanced High Power Coil Block",
                (byte) 2,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltips_AdvancedHighPowerCoil.01"),
                    TextEnums.tr("Tooltips_AdvancedHighPowerCoil.02") }));

        GTCMItemList.ParallelismCasing0.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Parallelism Casing Mark 0",
                (byte) 3,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.3.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.3.name_1") }));

        GTCMItemList.ParallelismCasing1.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Parallelism Casing Mark 1",
                (byte) 4,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.4.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.4.name_1") }));

        GTCMItemList.ParallelismCasing2.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Parallelism Casing Mark 2",
                (byte) 5,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.5.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.5.name_1") }));

        GTCMItemList.ParallelismCasing3.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Parallelism Casing Mark 3",
                (byte) 6,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.6.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.6.name_1") }));

        GTCMItemList.ParallelismCasing4.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Parallelism Casing Mark 4",
                (byte) 7,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltip_MetaBlockCasing01.7.name_0"),
                    TextEnums.tr("Tooltip_MetaBlockCasing01.7.name_1") }));

        GTCMItemList.AntiMagneticCasing.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Anti-Magnetic Casing",
                // #tr MetaBlockCasing01.8.name
                // # Anti-Magnetic Casing
                // #zh_CN 抗磁机械方块
                (byte) 8,
                BasicBlocks.MetaBlockCasing01));

        GTCMItemList.ReinforcedStoneBrickCasing.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Reinforced Stone Brick Casing",
                // #tr MetaBlockCasing01.9.name
                // # Reinforced Stone Brick Casing
                // #zh_CN 强化石砖机械方块
                (byte) 9,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltip_ReinforcedStoneBrickCasing")
                // #tr Tooltip_ReinforcedStoneBrickCasing
                // # Stronger than bedrock!
                // #zh_CN 比磐石更坚！
                }));

        GTCMItemList.CompositeFarmCasing.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Composite Farm Casing",
                // #tr MetaBlockCasing01.10.name
                // # Composite Farm Casing
                // #zh_CN 复合农场机械方块
                (byte) 10,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltip_CompositeFarmCasing")
                // #tr Tooltip_CompositeFarmCasing
                // # A force stronger than four combined.
                // #zh_CN 一个更比四个强
                }));

        GTCMItemList.DenseCyclotronOuterCasing.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Dense Cyclotron Outer Casing",
                // #tr MetaBlockCasing01.11.name
                // # Dense Cyclotron Outer Casing
                // #zh_CN 致密回旋加速器机械方块
                (byte) 11,
                BasicBlocks.MetaBlockCasing01));

        GTCMItemList.CompactCyclotronCoil.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Compact Cyclotron Coil",
                // #tr MetaBlockCasing01.12.name
                // # Compact Cyclotron Coil
                // #zh_CN 压缩回旋加速器线圈
                (byte) 12,
                BasicBlocks.MetaBlockCasing01));

        GTCMItemList.AsepticGreenhouseCasing.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Aseptic Greenhouse Casing",
                // #tr MetaBlockCasing01.13.name
                // # Aseptic Greenhouse Casing
                // #zh_CN 无菌温室机械方块
                (byte) 13,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltip_AsepticGreenhouseCasing")
                // #tr Tooltip_AsepticGreenhouseCasing
                // # Absolutely Clean!
                // #zh_CN 一尘不染!
                }));

        // region SpaceTimeOscillator

        // #tr SpaceTimeOscillator.0.name
        // # SpaceTime Oscillator T1
        // #zh_CN 时空振荡器T1
        GTCMItemList.SpaceTimeOscillatorT1.set(
            MetaBlockConstructors
                .initMetaBlock("SpaceTime Oscillator T1", (byte) 0, (MetaBlockBase) BasicBlocks.SpaceTimeOscillator));

        // #tr SpaceTimeOscillator.1.name
        // # SpaceTime Oscillator T2
        // #zh_CN 时空振荡器T2
        GTCMItemList.SpaceTimeOscillatorT2.set(
            MetaBlockConstructors
                .initMetaBlock("SpaceTime Oscillator T2", (byte) 1, (MetaBlockBase) BasicBlocks.SpaceTimeOscillator));

        // #tr SpaceTimeOscillator.2.name
        // # SpaceTime Oscillator T3
        // #zh_CN 时空振荡器T3
        GTCMItemList.SpaceTimeOscillatorT3.set(
            MetaBlockConstructors
                .initMetaBlock("SpaceTime Oscillator T3", (byte) 2, (MetaBlockBase) BasicBlocks.SpaceTimeOscillator));

        // endregion

        // region SpaceTimeConstraintor

        // #tr SpaceTimeConstraintor.0.name
        // # SpaceTime Constraintor T1
        // #zh_CN 时空约束器T1
        GTCMItemList.SpaceTimeConstraintorT1.set(
            MetaBlockConstructors.initMetaBlock(
                "SpaceTime Constraintor T1",
                (byte) 0,
                (MetaBlockBase) BasicBlocks.SpaceTimeConstraintor));

        // #tr SpaceTimeConstraintor.1.name
        // # SpaceTime Constraintor T2
        // #zh_CN 时空约束器T2
        GTCMItemList.SpaceTimeConstraintorT2.set(
            MetaBlockConstructors.initMetaBlock(
                "SpaceTime Constraintor T2",
                (byte) 1,
                (MetaBlockBase) BasicBlocks.SpaceTimeConstraintor));

        // #tr SpaceTimeConstraintor.2.name
        // # SpaceTime Constraintor T3
        // #zh_CN 时空约束器T3
        GTCMItemList.SpaceTimeConstraintorT3.set(
            MetaBlockConstructors.initMetaBlock(
                "SpaceTime Constraintor T3",
                (byte) 2,
                (MetaBlockBase) BasicBlocks.SpaceTimeConstraintor));

        // endregion

        // region SpaceTimeMerger

        // #tr SpaceTimeMerger.0.name
        // # SpaceTime Merger T1
        // #zh_CN 时空归并器T1
        GTCMItemList.SpaceTimeMergerT1.set(
            MetaBlockConstructors
                .initMetaBlock("SpaceTime Merger T1", (byte) 0, (MetaBlockBase) BasicBlocks.SpaceTimeMerger));

        // #tr SpaceTimeMerger.1.name
        // # SpaceTime Merger T2
        // #zh_CN 时空归并器T2
        GTCMItemList.SpaceTimeMergerT2.set(
            MetaBlockConstructors
                .initMetaBlock("SpaceTime Merger T2", (byte) 1, (MetaBlockBase) BasicBlocks.SpaceTimeMerger));

        // #tr SpaceTimeMerger.2.name
        // # SpaceTime Merger T3
        // #zh_CN 时空归并器T3
        GTCMItemList.SpaceTimeMergerT3.set(
            MetaBlockConstructors
                .initMetaBlock("SpaceTime Merger T3", (byte) 2, (MetaBlockBase) BasicBlocks.SpaceTimeMerger));

        // endregion

        // region PhotonControllerUpgrade
        GTCMItemList.PhotonControllerUpgradeLV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier LV Tier", 0));
        GTCMItemList.PhotonControllerUpgradeMV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier MV Tier", 1));
        GTCMItemList.PhotonControllerUpgradeHV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier HV Tier", 2));
        GTCMItemList.PhotonControllerUpgradeEV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier EV Tier", 3));
        GTCMItemList.PhotonControllerUpgradeIV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier IV Tier", 4));
        GTCMItemList.PhotonControllerUpgradeLuV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier LuV Tier", 5));
        GTCMItemList.PhotonControllerUpgradeZPM
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier ZPM Tier", 6));
        GTCMItemList.PhotonControllerUpgradeUV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UV Tier", 7));
        GTCMItemList.PhotonControllerUpgradeUHV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UHV Tier", 8));
        GTCMItemList.PhotonControllerUpgradeUEV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UEV Tier", 9));
        GTCMItemList.PhotonControllerUpgradeUIV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UIV Tier", 10));
        GTCMItemList.PhotonControllerUpgradeUMV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UMV Tier", 11));
        GTCMItemList.PhotonControllerUpgradeUXV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UXV Tier", 12));
        GTCMItemList.PhotonControllerUpgradeMAX
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier MAX Tier", 13));
        // endregion
        // ---------------------------------------------------------------------------------------------------------------------------//
        // region MegaSpaceStation
        if (Config.activateMegaSpaceStation) {
            GTCMItemList.spaceStationStructureBlockLV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock LV Tier", 0));
            GTCMItemList.spaceStationStructureBlockMV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock MV Tier", 1));
            GTCMItemList.spaceStationStructureBlockHV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock HV Tier", 2));
            GTCMItemList.spaceStationStructureBlockEV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock EV Tier", 3));
            GTCMItemList.spaceStationStructureBlockIV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock IV Tier", 4));
            GTCMItemList.spaceStationStructureBlockLuV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock LuV Tier", 5));
            GTCMItemList.spaceStationStructureBlockZPM.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock ZPM Tier", 6));
            GTCMItemList.spaceStationStructureBlockUV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock UV Tier", 7));
            GTCMItemList.spaceStationStructureBlockUHV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock UHV Tier", 8));
            GTCMItemList.spaceStationStructureBlockUEV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock UEV Tier", 9));
            GTCMItemList.spaceStationStructureBlockUIV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock UIV Tier", 10));
            GTCMItemList.spaceStationStructureBlockUMV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock UMV Tier", 11));
            GTCMItemList.spaceStationStructureBlockUXV.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock UXV Tier", 12));
            GTCMItemList.spaceStationStructureBlockMAX.set(
                SpaceStationStructureCasing.SpaceStationStructureCasingMeta("spaceStationStructureBlock MAX Tier", 13));
            // ----------------------------------------
            GTCMItemList.SpaceStationAntiGravityBlockLV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock LV Tier", 0));
            GTCMItemList.SpaceStationAntiGravityBlockMV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock MV Tier", 1));
            GTCMItemList.SpaceStationAntiGravityBlockHV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock HV Tier", 2));
            GTCMItemList.SpaceStationAntiGravityBlockEV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock EV Tier", 3));
            GTCMItemList.SpaceStationAntiGravityBlockIV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock IV Tier", 4));
            GTCMItemList.SpaceStationAntiGravityBlockLuV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock LuV Tier", 5));
            GTCMItemList.SpaceStationAntiGravityBlockZPM.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock ZPM Tier", 6));
            GTCMItemList.SpaceStationAntiGravityBlockUV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock UV Tier", 7));
            GTCMItemList.SpaceStationAntiGravityBlockUHV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock UHV Tier", 8));
            GTCMItemList.SpaceStationAntiGravityBlockUEV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock UEV Tier", 9));
            GTCMItemList.SpaceStationAntiGravityBlockUIV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock UIV Tier", 10));
            GTCMItemList.SpaceStationAntiGravityBlockUMV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock UMV Tier", 11));
            GTCMItemList.SpaceStationAntiGravityBlockUXV.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock UXV Tier", 12));
            GTCMItemList.SpaceStationAntiGravityBlockMAX.set(
                SpaceStationAntiGravityCasing
                    .SpaceStationAntiGravityCasingMeta("SpaceStationAntiGravityBlock MAX Tier", 13));
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
