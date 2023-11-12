package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemMultiStructuresLinkTool;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings8;

public abstract class GT_TileEntity_MultiStructureMachine<T extends GT_TileEntity_MultiStructureMachine<T>>
    extends GTCM_MultiMachineBase<T> implements IConstructable, ISurvivalConstructable {

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
    public boolean isMainBlock = false;
    public int ID = -1;

    public int Type = -1;

    public String[][] shape;

    public int fatherID = -1;

    // block or materials in Object[0], meta in object[1],if meta not required, use 0;
    public ArrayList<Object[]> staticStructureDefine;

    protected GT_TileEntity_MultiStructureMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        MultiStructureManager.registryMachine(this);
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
        MultiStructureManager.removeMachine(this);
        super.onBlockDestroyed();
    }

    // used for define unique structure blocks
    public StructureDefinition.Builder<T> additionalStructureDefinition() {
        return defaultStructureDefinition();
    };

    // define A as bus and hatch,B as energy, c as Maintenance;
    public StructureDefinition.Builder<T> defaultStructureDefinition() {
        StructureDefinition.Builder<T> defaultStructure = StructureDefinition.<T>builder()
            .addShape("default", transpose(shape))
            .addElement(
                'A',
                GT_HatchElementBuilder.<T>builder()
                    .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                    .adder(T::addToMachineList)
                    .dot(1)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(2))
                    .buildAndChain(GregTech_API.sBlockCasings8, 2))
            .addElement(
                'B',
                GT_HatchElementBuilder.<T>builder()
                    .atLeast(Energy.or(ExoticEnergy))
                    .adder(T::addToMachineList)
                    .dot(2)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(3))
                    .buildAndChain(GregTech_API.sBlockCasings8, 3))
            .addElement(
                'C',
                GT_HatchElementBuilder.<T>builder()
                    .atLeast(Maintenance)
                    .adder(T::addToMachineList)
                    .dot(3)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(10))
                    .buildAndChain(GregTech_API.sBlockCasings8, 10));
        char now = 'D';
        for (Object[] block : staticStructureDefine) {
            if (now == 'Z') {
                now = 'a';
            }
            if (block[0].getClass() == Block.class) {
                defaultStructure.addElement(now, ofBlock((Block) block[0], (int) block[1]));
            } else if (block[0].getClass() == Materials.class) {
                defaultStructure.addElement(now, ofFrame((Materials) block[0]));
            }
            now++;
        }
        return defaultStructure;
    }

    protected abstract boolean isEnablePerfectOverclock();

    protected abstract float getSpeedBonus();

    protected abstract int getMaxParallelRecipes();

    @Override
    public IStructureDefinition<T> getStructureDefinition() {
        return additionalStructureDefinition().build();
    }

    public abstract void setShape();

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("ID", ID);
        aNBT.setInteger("TYPE", Type);
        aNBT.setBoolean("isMain", isMainBlock);
        aNBT.setInteger("fatherID", fatherID);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        ID = aNBT.getInteger("ID");
        Type = aNBT.getInteger("TYPE");
        isMainBlock = aNBT.getBoolean("isMain");
        fatherID = aNBT.getInteger("fatherID");
        super.loadNBTData(aNBT);
    }

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {

        super.onCreated(aStack, aWorld, aPlayer);
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        ItemStack itemStackInuse = aPlayer.getItemInUse();
        if (itemStackInuse.getItem() instanceof ItemMultiStructuresLinkTool tool) {
            if (!isMainBlock) {
                GT_Utility
                    .sendChatToPlayer(aPlayer, "can not bind main Structure with left click, use right lick instead");
                return;
            }
            tool.firstPosition = ID;
            tool.checkLink(aPlayer);
            return;
        }

        super.onLeftclick(aBaseMetaTileEntity, aPlayer);

    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        ItemStack itemStackInuse = aPlayer.getItemInUse();
        if (itemStackInuse.getItem() instanceof ItemMultiStructuresLinkTool tool) {
            tool.secondPosition = ID;
            tool.checkLink(aPlayer);
            return true;
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

}
