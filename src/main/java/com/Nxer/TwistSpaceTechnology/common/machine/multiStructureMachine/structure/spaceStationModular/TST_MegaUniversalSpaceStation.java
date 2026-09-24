package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structure.spaceStationModular;// spotless:off

import static bartworks.common.loaders.ItemRegistry.bw_realglas2;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.SpaceStationAntiGravityBlock;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.SpaceStationStructureBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.GT_TileEntity_MultiStructureMachine;
import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.StructureLoader;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import gtnhintergalactic.client.IGTextures;
import tectech.thing.casing.TTCasingsContainer;

@SkipGenerateDescription
public class TST_MegaUniversalSpaceStation extends GT_TileEntity_MultiStructureMachine<TST_MegaUniversalSpaceStation> {

    // region Class Constructor
    public TST_MegaUniversalSpaceStation(int aID, String mName, String aNameRegional) {
        super(aID, mName, aNameRegional);
        turnOffMaintenance();
    }

    public TST_MegaUniversalSpaceStation(String mName) {
        super(mName);
        turnOffMaintenance();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaUniversalSpaceStation(this.mName);
    }
    // endregion

    // region Structure
    public static IStructureDefinition<TST_MegaUniversalSpaceStation> structureDefinition;

    @Override
    public IStructureDefinition<TST_MegaUniversalSpaceStation> getStructureDefinition() {
        if (structureDefinition == null) {
            var builder = StructureDefinition.<TST_MegaUniversalSpaceStation>builder();
            for (int i = 0; i < StructureLoader.readStructure(mName).shape.size(); i++) {
                builder.addShape(mName + i, StructureLoader.getShape(mName, mName + i));
            }
            structureDefinition = builder.addElement('A', ofBlock(bw_realglas2, 0))
                .addElement(
                    'R',
                    StructureUtility.ofChain(
                        GTStructureUtility.ofHatchAdder(
                            TST_MegaUniversalSpaceStation::addMaintenanceToMachineList,
                            IGTextures.CASING_INDEX_SIPHON,
                            1),
                        GTStructureUtility.ofHatchAdder(
                            TST_MegaUniversalSpaceStation::addExoticEnergyInputToMachineList,
                            IGTextures.CASING_INDEX_SIPHON,
                            1),
                        GTStructureUtility.ofHatchAdder(
                            TST_MegaUniversalSpaceStation::addInputToMachineList,
                            IGTextures.CASING_INDEX_SIPHON,
                            1),
                        GTStructureUtility.ofHatchAdder(
                            TST_MegaUniversalSpaceStation::addOutputToMachineList,
                            IGTextures.CASING_INDEX_SIPHON,
                            1),
                        StructureUtility.ofBlock(SpaceStationAntiGravityBlock, 13)))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings9, 1))
                .addElement('E', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 10))
                .addElement('F', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 12))
                .addElement('G', ofBlock(GregTechAPI.sBlockCasingsSE, 1))
                .addElement('H', ofBlock(GregTechAPI.sBlockCasingsSE, 2))
                .addElement('I', ofBlock(TTCasingsContainer.sBlockCasingsTT, 2))
                .addElement('J', ofBlock(TTCasingsContainer.sBlockCasingsTT, 3))
                .addElement('K', ofBlock(GregTechAPI.sBlockMetal9, 6))
                .addElement('L', ofBlock(GregTechAPI.sBlockMetal9, 7))
                .addElement('M', ofBlock(SpaceStationStructureBlock, 13))
                .addElement('N', ofBlock(ModBlocks.blockCasings5Misc, 10))
                .addElement('O', ofBlock(ModBlocks.blockCasings5Misc, 14))
                .addElement('P', ofBlock(ModBlocks.blockCasings6Misc, 0))
                .addElement('Q', ofBlock(ModBlocks.blockSpecialMultiCasings, 15))
                .addElement('B', ofBlock(Block.getBlockById(1), 0))
                .addElement('C', ofBlock(SpaceStationStructureBlock, 13))
                // .addElement('C', ofBlock(Block.getBlockById(1),0))
                // .addElement('D', ofBlock(Block.getBlockById(1),0))
                // .addElement('E', ofBlock(Block.getBlockById(1),0))
                // .addElement('F', ofBlock(Block.getBlockById(1),0))
                // .addElement('G', ofBlock(Block.getBlockById(1),0))
                // .addElement('H', ofBlock(Block.getBlockById(1),0))
                // .addElement('I', ofBlock(Block.getBlockById(1),0))
                // .addElement('J', ofBlock(Block.getBlockById(1),0))
                // .addElement('K', ofBlock(Block.getBlockById(1),0))
                // .addElement('L', ofBlock(Block.getBlockById(1),0))
                // .addElement('M', ofBlock(Block.getBlockById(1),0))
                // .addElement('N', ofBlock(Block.getBlockById(1),0))
                // .addElement('O', ofBlock(Block.getBlockById(1),0))
                // .addElement('P', ofBlock(Block.getBlockById(1),0))
                // .addElement('Q', ofBlock(Block.getBlockById(1),0))
                .addElement('S', ofBlock(Block.getBlockById(0), 0))//
                .addElement('T', ofBlock(Block.getBlockById(0), 0))//
                .addElement('U', ofBlock(Block.getBlockById(0), 0))//
                .addElement('V', ofBlock(Block.getBlockById(0), 0))//
                .addElement('W', ofBlock(Block.getBlockById(0), 0))//
                .addElement('X', ofBlock(Block.getBlockById(0), 0))//
                .build();
        }
        return structureDefinition;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        super.construct(stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        GTUtility.sendChatTrans(
            env.getActor(),
            "warning! you should not use general method to construct such a "
                + "big structure! this will still process for you anyway.");
        elementBudget = Math.max(100, elementBudget);
        return super.survivalConstruct(stackSize, elementBudget, env);
    }
    // endregion

    // region Processing Logic
    static {
        StructureLoader.setOffSet("namemegauniversalspacestation", 215, 45, 223);
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    protected float getSpeedBonus() {
        return 0;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        if (InConstruct.isEmpty()) {
            return SimpleCheckRecipeResult.ofSuccess("space station running fine");
        }
        // return early if no input busses are present, the first bus is invalid or the TE is not on a space station
        if (runningTick % 100 == 0) {
            // GT_Hatch_SpaceStationRepairHatch bus;
            // try {
            // bus = (GT_Hatch_SpaceStationRepairHatch) mInputBusses.get(0);
            // } catch (Exception e) {
            // return SimpleCheckRecipeResult.ofFailure(
            // "space station is not complete or destroyed by someone, not you right?\n"
            // + "no repair hatch find, please set one");
            // }
            // ItemStack repairItem = bus.getBaseMetaTileEntity()
            // .getStackInSlot(0);
            // if (repairItem == null) {
            // return SimpleCheckRecipeResult.ofFailure(
            // "space station is not complete or destroyed by someone, not you right?\n" + "no repair item find!");
            // }
            // if (Objects.equals(repairItem.getItem(), spaceStationConstructingMaterialMax.getItem())
            // && repairItem.stackSize >= 1) {
            // repairItem.stackSiz
            int num = InConstruct.iterator()
                .next();
            repair(num);
            construct(null, false);
            return SimpleCheckRecipeResult.ofFailure(
                "space station is not complete or destroyed by someone, not you right?\n"
                    + "repairing or constructing space station, please wait");
            // } else {
            // return SimpleCheckRecipeResult.ofFailure(
            // "space station is not complete or destroyed by someone, not you right?\n" + "repair item not fit!");
            // }
            // count mining pipes, get depth
        }
        return SimpleCheckRecipeResult
            .ofFailure("space station is not complete or destroyed by someone, not you right?");

    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183) };
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.MegaUniversalSpaceStation.tooltip.machine_type
        // # space station
        // #zh_CN temp
        tt.addMachineType(TSTUtils.tr("tst.common.machine.MegaUniversalSpaceStation.tooltip.machine_type"))
            // #tr tst.common.machine.MegaUniversalSpaceStation.tooltip.info.01
            // # Use auto build item to build instead of build your self
            // #zh_CN temp
            .addInfo(TSTUtils.tr("tst.common.machine.MegaUniversalSpaceStation.tooltip.info.01"))
            // #tr tst.common.machine.MegaUniversalSpaceStation.tooltip.info.02
            // # Auto-SpaceStation build core
            // #zh_CN temp
            .addInfo(TSTUtils.tr("tst.common.machine.MegaUniversalSpaceStation.tooltip.info.02"))
            // #tr tst.common.machine.MegaUniversalSpaceStation.tooltip.info.03
            // # If your station broke, you can put fix block inside the input hatch to fix it
            // #zh_CN temp
            .addInfo(TSTUtils.tr("tst.common.machine.MegaUniversalSpaceStation.tooltip.info.03"))
            // #tr tst.common.machine.MegaUniversalSpaceStation.tooltip.info.04
            // # temp
            // #zh_CN temp
            .addInfo(TSTUtils.tr("tst.common.machine.MegaUniversalSpaceStation.tooltip.info.04"))
            // #tr tst.common.machine.MegaUniversalSpaceStation.tooltip.info.05
            // # temp
            // #zh_CN temp
            .addInfo(TSTUtils.tr("tst.common.machine.MegaUniversalSpaceStation.tooltip.info.05"))
            // #tr tst.common.machine.MegaUniversalSpaceStation.tooltip.info.06
            // # temp
            // #zh_CN temp
            .addInfo(TSTUtils.tr("tst.common.machine.MegaUniversalSpaceStation.tooltip.info.06"))
            // #tr tst.common.machine.MegaUniversalSpaceStation.tooltip.info.07
            // # temp
            // #zh_CN temp
            .addInfo(TSTUtils.tr("tst.common.machine.MegaUniversalSpaceStation.tooltip.info.07"))
            .addInfo(TSTSharedLocalization.MachineTooltip.Tooltip_GlassTierLimitEnergyHatchTier)
            .addInfo(TSTSharedLocalization.MachineTooltip.textScrewdriverChangeMode)
            .beginStructureBlock(448, 256, 431, false)
            .addInputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 2)
            .addOutputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 2)
            .addInputBus(TSTSharedLocalization.Structure.textUseBlueprint, 2)
            .addOutputBus(TSTSharedLocalization.Structure.textUseBlueprint, 2)
            .addMaintenanceHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addEnergyHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .toolTipFinisher(TSTSharedLocalization.General.ModName);
        // spotless:on
        return tt;
    }

    // endregion

    // region Hatch Registration

    @Override
    public boolean addInputBusOrOutputBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addInputBusOrOutputBusToMachineList(aTileEntity, aBaseCasingIndex);
    }

    // endregion

}
