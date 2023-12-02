// spotless:off
package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemMultiStructuresLinkTool;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;

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
    public ArrayList<String> pieces = new ArrayList<>();
    public int fatherID = -1;

    public boolean isComplete = false;

    protected GT_TileEntity_MultiStructureMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pieces.add(aName.toLowerCase());
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
        for (String piece : pieces) {
            StructureLoader.load(mName, piece);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return super.survivalConstruct(stackSize, elementBudget, env);
    }

    int constructCount = 0;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        StructureLoader.MultiStructureDefinition.OffSet offSet = StructureLoader
            .getOffSet(mName, mName + constructCount);

        if (this.buildPiece(
            mName + constructCount,
            stackSize,
            hintsOnly,
            offSet.horizontalOffSet,
            offSet.verticalOffSet,
            offSet.depthOffSet)) {
            constructCount++;
        }
        if (constructCount == StructureLoader.readStructure(mName).pieces.size()) {
            constructCount = 0;
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
            checkStructureCount++;
            if (constructCount == StructureLoader.readStructure(mName).pieces.size()) {
                checkStructureCount = 0;
            }
            return true;
        }
        return false;
        // return isComplete;
    }

    int checkMachineCount = 0;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        StructureLoader.MultiStructureDefinition.OffSet offSet = StructureLoader
            .getOffSet(mName, mName + checkMachineCount);
        if (checkPiece(mName + checkMachineCount, offSet.horizontalOffSet, offSet.verticalOffSet, offSet.depthOffSet)) {
            checkMachineCount++;
            if (constructCount == StructureLoader.readStructure(mName).pieces.size()) {
                checkMachineCount = 0;
            }
            return true;
        }
        return false;
        // return isComplete;
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
        if (aTick == 1 && !aBaseMetaTileEntity.isClientSide()) {

            MultiStructureManager.registryMachine(this);
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    protected void setExoticEnergyHatches(List<GT_MetaTileEntity_Hatch> ExoticEnergyHatches) {
        var father = MultiStructureManager.getMachine(fatherID);
        if (father == null) {
            stopMachine();
            return;
        }
        super.setExoticEnergyHatches(father.getExoticEnergyHatches());
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        var father = MultiStructureManager.getMachine(fatherID);
        return father != null ? father.getStoredInputs() : super.getStoredInputs();
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
