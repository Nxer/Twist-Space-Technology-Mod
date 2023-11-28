// spotless:off
package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structure;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.*;
import static gregtech.api.enums.Textures.BlockIcons.*;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.GT_TileEntity_MultiStructureMachine;
import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.StructureLoader;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class Test_MultiStructMachine extends GT_TileEntity_MultiStructureMachine<Test_MultiStructMachine> {

    public Test_MultiStructMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public Test_MultiStructMachine(String mName) {
        super(mName);
    }

    @Override
    public IStructureDefinition<Test_MultiStructMachine> getStructureDefinition() {
        return null;
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

    @Override
    public void setShape() {
        super.setShape();
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        StructureLoader.MultiStructureDefinition.OffSet offSet = StructureLoader
            .getOffSet(this.mName, STRUCTURE_PIECE_MAIN);
        int horizontalOffSet = offSet.horizontalOffSet;
        int verticalOffSet = offSet.verticalOffSet;
        int depthOffSet = offSet.depthOffSet;
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        StructureLoader.MultiStructureDefinition.OffSet offSet = StructureLoader
            .getOffSet(this.mName, STRUCTURE_PIECE_MAIN);
        int horizontalOffSet = offSet.horizontalOffSet;
        int verticalOffSet = offSet.verticalOffSet;
        int depthOffSet = offSet.depthOffSet;
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            source,
            actor,
            false,
            true);
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new Test_MultiStructMachine(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    // Tooltips
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_ICD_MachineType)
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(11, 13, 11, false)
            .addController(textFrontBottom)
            .addCasingInfoRange(textCasing, 8, 26, false)
            .addInputHatch(textAnyCasing, 1)
            .addOutputHatch(textAnyCasing, 1)
            .addInputBus(textAnyCasing, 2)
            .addOutputBus(textAnyCasing, 2)
            .addMaintenanceHatch(textAnyCasing, 2)
            .addEnergyHatch(textAnyCasing, 3)
            .toolTipFinisher(ModName);
        return tt;
    }

}
