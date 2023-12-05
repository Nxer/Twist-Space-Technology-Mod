// spotless:off
package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
import static com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structureChecker.checkQueue;

import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemMultiStructuresLinkTool;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public abstract class GT_TileEntity_MultiStructureMachine<T extends GT_TileEntity_MultiStructureMachine<T>>
    extends GTCM_MultiMachineBase<T> implements IConstructable, ISurvivalConstructable {

    // ONLY main block can process recipe or do anything machine need to do.
    // the sub structure actually only add functional models or additional
    // bonus. once the sub structure registry and link to the main machine,
    // no need to load the chunk or even dimension where substructure is.
    // but every time sub structure reloaded, the main block will also auto reload
    // its main structure
    public int ID = -1;
    public int Type = -1;
    public int fatherID = -1;
    protected long runningTick = 0;
    public HashSet<Integer> InConstruct = new HashSet<>();

    protected GT_TileEntity_MultiStructureMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        setShape();

    }

    public GT_TileEntity_MultiStructureMachine(String mName) {
        super(mName);
    }

    @Override
    public void onBlockDestroyed() {
        MultiStructureManager.removeMachine(this);
        super.onBlockDestroyed();
    }

    protected abstract boolean isEnablePerfectOverclock();

    protected abstract float getSpeedBonus();

    protected abstract int getMaxParallelRecipes();

    public void setShape() {
        StructureLoader.load(mName, mName);
        var pieces = StructureLoader.getPieces(mName)
            .size();
        for (int i = 0; i < pieces; i++) {
            InConstruct.add(i);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {

        return super.survivalConstruct(stackSize, elementBudget, env);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int num = InConstruct.iterator()
            .next();
        StructureLoader.MultiStructureDefinition.OffSet offSet = StructureLoader.getOffSet(mName, mName + num);
        if (this.buildPiece(
            mName + num,
            stackSize,
            hintsOnly,
            offSet.horizontalOffSet,
            offSet.verticalOffSet,
            offSet.depthOffSet)) {
            InConstruct.remove(num);
        }

    }

    public void repair(int num) {
        StructureLoader.MultiStructureDefinition.OffSet offSet = StructureLoader.getOffSet(mName, mName + num);
        if (this
            .buildPiece(mName + num, null, false, offSet.horizontalOffSet, offSet.verticalOffSet, offSet.depthOffSet)) {
            InConstruct.remove(num);
        }

    }

    int checkStructureCount = 0;

    // need to be optimized and rewrite
    @Override
    public boolean checkStructure(boolean aForceReset, IGregTechTileEntity aBaseMetaTileEntity) {
        StructureLoader.MultiStructureDefinition.OffSet offSet = StructureLoader
            .getOffSet(mName, mName + checkStructureCount);
        if (checkPiece(
            mName + checkStructureCount,
            offSet.horizontalOffSet,
            offSet.verticalOffSet,
            offSet.depthOffSet)) {
            InConstruct.remove(checkStructureCount);
            checkStructureCount++;
            if (checkStructureCount == StructureLoader.readStructure(mName).pieces.size()) {
                checkStructureCount = 0;
            }
        } else {
            InConstruct.add(checkStructureCount);
        }
        return InConstruct.isEmpty();
        // return isComplete;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return InConstruct.isEmpty();
    }

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
        runningTick++;
        if (runningTick % 100 == 0 && checkQueue.size() <= 1000) {
            checkQueue.add(this);
        }
        if (aTick == 1 && aBaseMetaTileEntity.isServerSide()) {
            MultiStructureManager.registryMachine(this);
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
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
