package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

// spotless:off
public class GTCM_TestMultiMachine
    extends GTCM_MultiMachineBase<GTCM_TestMultiMachine> {
    public GTCM_TestMultiMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_TestMultiMachine(String aName) {
        super(aName);
    }
    // region Processing Logic

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    protected float getSpeedBonus() {
        return 1.0F / 16;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    // endregion

    protected int mode = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final String[][] shape = new String[][]{
        {"AAA", "AAA", "AAA"},
        {"A~A", "AAA", "AAA"},
        {"AAA", "AAA", "AAA"}
    };
    private static final int horizontalOffSet = 1;
    private static final int verticalOffSet = 1;
    private static final int depthOffSet = 0;

    @Override
    public IStructureDefinition<GTCM_TestMultiMachine> getStructureDefinition() {
        return StructureDefinition
            .<GTCM_TestMultiMachine>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
            .addElement(
                'A',
                GT_HatchElementBuilder.<GTCM_TestMultiMachine>builder()
                    .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy), Maintenance)
                    .adder(GTCM_TestMultiMachine::addToMachineList)
                    .casingIndex(BasicBlocks.MetaBlockCasing01.getTextureIndex(2))
                    .dot(1)
                    .buildAndChain(BasicBlocks.MetaBlockCasing01, 2))
            .build();
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % 2;
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("IntensifyChemicalDistorter.mode." + this.mode));
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // this.casingAmountActual = 0; // re-init counter
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }


    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mode = aNBT.getInteger("mode");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_TestMultiMachine(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
                                 int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[]{casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build()};
            return new ITexture[]{casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build()};
        }
        return new ITexture[]{casingTexturePages[1][48]};
    }

    // Tooltips
    private static GT_Multiblock_Tooltip_Builder tooltip;
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        if (tooltip == null) {
            tooltip = new GT_Multiblock_Tooltip_Builder();
            tooltip.addMachineType("test")
                   .addInfo("testing")
                   .addSeparator()
                   .addInfo(TextLocalization.StructureTooComplex)
                   .addInfo(TextLocalization.BLUE_PRINT_INFO)
                   .beginStructureBlock(3, 3, 3, false)
                   .addInputHatch(TextLocalization.textUseBlueprint, 1)
                   .addOutputHatch(TextLocalization.textUseBlueprint, 1)
                   .addInputBus(TextLocalization.textUseBlueprint, 2)
                   .addOutputBus(TextLocalization.textUseBlueprint, 2)
                   .addEnergyHatch(TextLocalization.textUseBlueprint, 3)
                   .toolTipFinisher(TextLocalization.ModName);

        }
        return tooltip;
//        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
//        tt.addMachineType("test")
//            .addInfo("testing")
//            .addSeparator()
//            .addInfo(TextLocalization.StructureTooComplex)
//            .addInfo(TextLocalization.BLUE_PRINT_INFO)
//            .beginStructureBlock(3, 3, 3, false)
//            .addInputHatch(TextLocalization.textUseBlueprint, 1)
//            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
//            .addInputBus(TextLocalization.textUseBlueprint, 2)
//            .addOutputBus(TextLocalization.textUseBlueprint, 2)
//            .addEnergyHatch(TextLocalization.textUseBlueprint, 3)
//            .toolTipFinisher(TextLocalization.ModName);
    }

}

// spotless:on
