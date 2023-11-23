// spotless:off
package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemMultiStructureMachineBuilder;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemMultiStructuresLinkTool;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public abstract class GT_TileEntity_MultiStructureMachine<T extends GT_TileEntity_MultiStructureMachine<T>> extends
    GTCM_MultiMachineBase<T> implements IConstructable, ISurvivalConstructable {

    // ONLY main block can process recipe or do anything machine need to do.
    // the sub structure actually only add functional models or additional
    // bonus. once the sub structure registry and link to the main machine,
    // no need to load the chunk or even dimension where substructure is.
    // but every time sub structure reloaded, the main block will also auto reload
    // its main structure
    public int ID = -1;
    public int Type = -1;

    public int fatherID = -1;


    protected GT_TileEntity_MultiStructureMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, "MultiStructure." + aNameRegional);
        setShape();

    }

    public GT_TileEntity_MultiStructureMachine(String mName) {
        super(mName);
    }

    public StructureLoader.MultiStructureDefinition getMultiStructureDefinition(){
        return StructureLoader.readStructure(mName);
    }

//    @Override
//    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
//        if (!checkPiece(MainStructName, horizontalOffSet, verticalOffSet, depthOffSet)) {
//            return false;
//        }
//        return MultiStructureManager.isComplete(this);
//    }

    @Override
    public void onBlockDestroyed() {
        MultiStructureManager.removeMachine(this);
        super.onBlockDestroyed();
    }

    protected abstract boolean isEnablePerfectOverclock();

    protected abstract float getSpeedBonus();

    protected abstract int getMaxParallelRecipes();

    public void setShape() {
//        for (String piece : pieces) {
//            shape.add(StructureLoader.readStructure(MainStructName));
//
//        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (!this.mMachine) {
            return -1;
        }
        Item item=stackSize.getItem();
        if(item instanceof ItemMultiStructureMachineBuilder){
            var pieces = StructureLoader.getPieces(this.mName);
            for(var name:pieces.entrySet()){
                StructureLoader.MultiStructureDefinition.OffSet offSet = StructureLoader.readStructure(mName).offSet.get(name.getValue());
                buildPiece(name.getKey(),stackSize,false,offSet.horizontalOffSet,offSet.verticalOffSet,offSet.depthOffSet);
            }
            return 0;
        }
        return super.survivalConstruct(stackSize, elementBudget, env);
    }


    //need to be optimized and rewrite
    @Override
    public boolean checkStructure(boolean aForceReset, IGregTechTileEntity aBaseMetaTileEntity) {
        if(MultiStructureManager.isComplete(this)){
            var pieces = StructureLoader.getPieces(this.mName);
            for(var name:pieces.entrySet()){
                StructureLoader.MultiStructureDefinition.OffSet offSet = StructureLoader.readStructure(mName).offSet.get(name.getValue());
                if(!checkPiece(mName, offSet.horizontalOffSet, offSet.verticalOffSet, offSet.depthOffSet)){
                    return false;
                };
            }
            return true;
        }
        return false;
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
        if (aTick == 1) {
            if (aBaseMetaTileEntity.isClientSide()) {
                return;
            }
            MultiStructureManager.registryMachine(this);
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    protected void setEnergyHatches(ArrayList<GT_MetaTileEntity_Hatch_Energy> EnergyHatches) {
        super.setEnergyHatches(EnergyHatches);
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

    @Override
    public void onWorldSave(File aSaveDirectory) {
        super.onWorldSave(aSaveDirectory);
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
