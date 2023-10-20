/*
 * Copyright (c) 2022 Nxer
 */
package com.GTNH_Community.gtnhcommunitymod.common.machine;

import static com.GTNH_Community.gtnhcommunitymod.util.DescTextLocalization.BLUE_PRINT_INFO;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.Textures.BlockIcons.*;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;

// GT_TileEntity_IntensifyChemicalDistorter
public class GT_TileEntity_IntensifyChemicalDistorter
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_IntensifyChemicalDistorter>
    implements IConstructable, ISurvivalConstructable {

    public GT_TileEntity_IntensifyChemicalDistorter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_IntensifyChemicalDistorter(String aName) {
        super(aName);
    }

    // Structure def
    private static IStructureDefinition<GT_TileEntity_IntensifyChemicalDistorter> Structure;

    /**
     * Due to limitation of Java type system, you might need to do an unchecked cast. HOWEVER, the returned
     * IStructureDefinition is expected to be evaluated against current instance only, and should not be used against
     * other instances, even for those of the same class.
     */
    final int Casing_Index = 176;// texture of Hatch base
    protected int casingAmountInNeed = 8;// casing amount in need
    protected int casingAmountActual;

    @Override
    public IStructureDefinition<GT_TileEntity_IntensifyChemicalDistorter> getStructureDefinition() {
        Structure = StructureDefinition.<GT_TileEntity_IntensifyChemicalDistorter>builder()
            .addShape(
                mName,
                transpose(new String[][] { { "ttt", "ttt", "ttt" }, { "t~t", "ttt", "ttt" }, { "ttt", "ttt", "ttt" } }))
            .addElement(
                't',
                GT_HatchElementBuilder.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                    .atLeast(
                        InputBus,
                        InputHatch,
                        OutputHatch,
                        OutputBus,
                        Maintenance,
                        Muffler,
                        ExoticEnergy.or(Energy))
                    .adder(GT_TileEntity_IntensifyChemicalDistorter::addToMachineList)
                    .casingIndex(Casing_Index)
                    .dot(1)
                    .buildAndChain(onElementPass(x -> x.casingAmountActual++, ofBlock(GregTech_API.sBlockCasings8, 0))))
            .build();
        return Structure;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivialBuildPiece(this.mName, stackSize, 1, 1, 0, realBudget, source, actor, false, true);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Testing Machine")
            .addInfo("Controller block for the Precise Assembler")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(3, 3, 3, false)
            .addController("Front middle")
            .addCasingInfoRange("Casing", 8, 26, false)
            .addInputHatch("Any Casing")
            .addInputBus("Any Casing")
            .addOutputHatch("Any Casing")
            .addOutputBus("Any Casing")
            .addEnergyHatch("Any Casing")
            .addMufflerHatch("Any Casing")
            .addMaintenanceHatch("Any Casing")
            .toolTipFinisher("GTNH Community Mod");
        return tt;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes;
    }

    /**
     * Checks if this is a Correct Machine Part for this kind of Machine (Turbine Rotor for example)
     *
     */
    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return false;
    }

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     *
     * @param aBaseMetaTileEntity
     * @param aStack
     */
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.casingAmountActual = 0;
        if (checkPiece(mName, 1, 1, 0)) {
            return casingAmountActual >= casingAmountInNeed;
        }
        return false;
    }

    /**
     * Gets the maximum Efficiency that spare Part can get (0 - 10000)
     *
     * @param aStack
     */
    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    /**
     * Gets the damage to the ItemStack, usually 0 or 1.
     *
     * @param aStack
     */
    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    /**
     * If it explodes when the Component has to be replaced.
     *
     * @param aStack
     */
    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    /**
     * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return a newly created and ready MetaTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_IntensifyChemicalDistorter(this.mName);
    }

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param aBaseMetaTileEntity
     * @param side                is the Side of the Block
     * @param facing              is the direction the Block is facing
     * @param aColorIndex         The Minecraft Color the Block is having
     * @param aActive             if the Machine is currently active (use this instead of calling
     *                            {@code mBaseMetaTileEntity.mActive)}. Note: In case of Pipes this means if this Side
     *                            is
     *                            connected to something or not.
     * @param aRedstone           if the Machine is currently outputting a RedstoneSignal (use this instead of calling
     *                            {@code mBaseMetaTileEntity.mRedstone} !!!)
     */
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
}
