// spotless:off
package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemMultiStructuresLinkTool;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public abstract class GT_TileEntity_MultiStructureMachine extends
    GTCM_MultiMachineBase<GT_TileEntity_MultiStructureMachine> implements IConstructable, ISurvivalConstructable {

    public String MainStructName;
    public int horizontalOffSet;
    public int verticalOffSet;
    public int depthOffSet;
    // ONLY main block can process recipe or do anything machine need to do.
    // the sub structure actually only add functional models or additional
    // bonus. once the sub structure registry and link to the main machine,
    // no need to load the chunk or even dimension where substructure is.
    // but every time sub structure reloaded, the main block will also auto reload
    // its main structure

    public int ID = -1;
    public int Type = -1;
    public String[][] shape;
    public int fatherID = -1;
    // block or materials in Object[0], meta in object[1],if meta not required, use 0;
    public ArrayList<Object[]> staticStructureDefine;

    protected GT_TileEntity_MultiStructureMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, "MultiStructure." + aNameRegional);

    }

    public GT_TileEntity_MultiStructureMachine(String mName) {
        super(mName);
    }

    public void setSubType(ArrayList<Integer> subType) {
        MultiStructureManager.registryTypeMap(Type, subType);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(MainStructName, horizontalOffSet, verticalOffSet, depthOffSet)) {
            return false;
        }
        return MultiStructureManager.isComplete(this);
    }

    @Override
    public void onBlockDestroyed() {
        LOG.info("DO YOU ACCESS HERE?");
        MultiStructureManager.removeMachine(this);
        super.onBlockDestroyed();
    }

    protected abstract boolean isEnablePerfectOverclock();

    protected abstract float getSpeedBonus();

    protected abstract int getMaxParallelRecipes();

    public abstract void setShape();

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("ID", ID);
        aNBT.setInteger("TYPE", Type);
        aNBT.setInteger("fatherID", fatherID);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        ID = aNBT.getInteger("ID");
        Type = aNBT.getInteger("TYPE");
        fatherID = aNBT.getInteger("fatherID");
        super.loadNBTData(aNBT);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick == 1) {
            if (aBaseMetaTileEntity.isClientSide()) {
                return;
            }
            LOG.info(
                "on First Tick machine loaded and registry:" + aBaseMetaTileEntity.getMetaTileEntity()
                    .getLocalName());
            MultiStructureManager.registryMachine(this);
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onSetActive(boolean active) {
        super.onSetActive(active);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onWorldLoad(File aSaveDirectory) {
        super.onWorldLoad(aSaveDirectory);
    }

    protected abstract <T extends GT_TileEntity_MultiStructureMachine> IStructureDefinition<T> internalStructureDefine();

    @Override
    public IStructureDefinition<GT_TileEntity_MultiStructureMachine> getStructureDefinition() {
        return internalStructureDefine();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        var itemInUse = aPlayer.getHeldItem();
        if (itemInUse.getItem() != null) {
            LOG.info(
                itemInUse.getItem()
                    .getUnlocalizedName());
        }
        if (itemInUse.getItem() instanceof ItemMultiStructuresLinkTool item) {
            item.secondPosition = ID;
            item.link(aPlayer);
            return true;
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        var itemInUse = aPlayer.getHeldItem();
        if (itemInUse.getItem() != null) {
            LOG.info(
                itemInUse.getItem()
                    .getUnlocalizedName());
        }
        if (itemInUse.getItem() instanceof ItemMultiStructuresLinkTool item) {
            item.firstPosition = ID;
            item.link(aPlayer);
            return;
        }
        super.onLeftclick(aBaseMetaTileEntity, aPlayer);
    }
}
