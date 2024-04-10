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
        GameRegistry.registerBlock(BlockPowerChair, ItemBlockPowerChair.class, "BlockPowerChair");
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
                new String[] { TextEnums.tr("Tooltips.ParallelismCasing.0") }));

        GTCMItemList.ParallelismCasing1.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Parallelism Casing Mark 1",
                (byte) 4,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltips.ParallelismCasing.1") }));

        GTCMItemList.ParallelismCasing2.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Parallelism Casing Mark 2",
                (byte) 5,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltips.ParallelismCasing.2") }));

        GTCMItemList.ParallelismCasing3.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Parallelism Casing Mark 3",
                (byte) 6,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltips.ParallelismCasing.4") }));

        GTCMItemList.ParallelismCasing4.set(
            MetaBlockConstructors.initMetaBlockCasing(
                "Parallelism Casing Mark 4",
                (byte) 7,
                BasicBlocks.MetaBlockCasing01,
                new String[] { TextEnums.tr("Tooltips.ParallelismCasing.4") }));

        GTCMItemList.AntiMagneticCasing.set(
            MetaBlockConstructors.initMetaBlockCasing("Anti-Magnetic Casing", (byte) 8, BasicBlocks.MetaBlockCasing01));

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
