/*
 * Copyright (c) 2022 Nxer
 */
package com.GTNH_Community.gtnhcommunitymod.common.machine;

import static com.GTNH_Community.gtnhcommunitymod.util.DescTextLocalization.BLUE_PRINT_INFO;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
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
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.GTNH_Community.gtnhcommunitymod.common.machine.recipeMap.GTCMRecipe;
import com.GTNH_Community.gtnhcommunitymod.util.TextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

// GT_MetaTileEntity_EnhancedMultiBlockBase called failed to use 64A energy hatch
// GT_TileEntity_MegaMultiBlockBase then use megaClass to success to apply 64A hatch
// Why ?
//
// GT_TileEntity_IntensifyChemicalDistorter
public class GT_TileEntity_IntensifyChemicalDistorter
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_IntensifyChemicalDistorter>
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
    final int Casing_Index_ChemInsertCasing = 176;// texture of Hatch base Chem Inert Casing
    // protected int casingAmountInNeed = 8;// casing amount in need
    protected int casingAmountActual;
    protected int mode = 0;// 0 means IntensifyChemicalDistorter; 1 means LCR adv

    private HeatingCoilLevel coilLevel;

    private static final String STRUCTURE_PIECE_MAIN = "main";

    /**
     * 's' = Stainless casing ;
     * 'v' = Chemically inert casing ;
     * 'h' = input Hatch ;
     * 'p' = PTFE Pipe casing ;
     * 'c' = coil ;
     * 'b' = input Bus ;
     * '~' = machine ;
     * 'e' = Energy hatch ;
     */
    private final String[][] shape = new String[][] {
        { "    sss    ", "  ssvvvss  ", " svvvvvvvs ", " svvvvvvvs ", "svvvvvvvvvs", "svvvvevvvvs", "svvvvvvvvvs",
            " svvvvvvvs ", " svvvvvvvs ", "  ssvvvss  ", "    sss    " },
        { "           ", "     h     ", "     p     ", "     p     ", "     p     ", " hpppcppph ", "     p     ",
            "     p     ", "     p     ", "     h     ", "           " },
        { "           ", "    h      ", "           ", "           ", "     p   h ", "    pcp    ", " h   p     ",
            "           ", "           ", "      h    ", "           " },
        { "           ", "           ", "   h       ", "        h  ", "     p     ", "    pcp    ", "     p     ",
            "  h        ", "       h   ", "           ", "           " },
        { "           ", "           ", "       h   ", "  h        ", "     p     ", "    pcp    ", "     p     ",
            "        h  ", "   h       ", "           ", "           " },
        { "           ", "      h    ", "           ", "           ", " h   p     ", "    pcp    ", "     p   h ",
            "           ", "           ", "    h      ", "           " },
        { "           ", "     h     ", "           ", "           ", "     p     ", " h  pcp  h ", "     p     ",
            "           ", "           ", "     h     ", "           " },
        { "           ", "    h      ", "           ", "           ", "     p   h ", "    pcp    ", " h   p     ",
            "           ", "           ", "      h    ", "           " },
        { "           ", "           ", "   h       ", "        h  ", "     p     ", "    pcp    ", "     p     ",
            "  h        ", "       h   ", "           ", "           " },
        { "           ", "           ", "       h   ", "  h        ", "     p     ", "    pcp    ", "     p     ",
            "        h  ", "   h       ", "           ", "           " },
        { "           ", "      h    ", "           ", "           ", " h   p     ", "    pcp    ", "     p   h ",
            "           ", "           ", "    h      ", "           " },
        { "           ", "     h     ", "     p     ", "     p     ", "     p     ", " hpppcppph ", "     p     ",
            "     p     ", "     p     ", "     h     ", "           " },
        { "    b~b    ", "  ssvvvss  ", " svvvvvvvs ", " svvvvvvvs ", "bvvvvvvvvvb", "bvvvvevvvvb", "bvvvvvvvvvb",
            " svvvvvvvs ", " svvvvvvvs ", "  ssvvvss  ", "    bbb    " } };
    private final int horizontalOffSet = 5;
    private final int verticalOffSet = 12;
    private final int depthOffSet = 0;

    @Override
    public IStructureDefinition<GT_TileEntity_IntensifyChemicalDistorter> getStructureDefinition() {
        Structure = StructureDefinition.<GT_TileEntity_IntensifyChemicalDistorter>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
            /**
             * √ 's' = Stainless casing ;
             * √ 'v' = Chemically inert casing ;
             * √ 'h' = input Hatch & output Hatch;
             * √ 'p' = PTFE Pipe casing ;
             * √ 'c' = coil ;
             * √ 'b' = input Bus or output Bus or Maintenance;
             * √ '~' = machine ;
             * √ 'e' = Energy hatch ;
             */
            .addElement('s', ofBlock(GregTech_API.sBlockCasings4, 1))
            .addElement('v', ofBlock(GregTech_API.sBlockCasings8, 0))
            .addElement('p', ofBlock(GregTech_API.sBlockCasings8, 1))
            .addElement(
                'c',
                withChannel(
                    "coil",
                    ofCoil(
                        GT_TileEntity_IntensifyChemicalDistorter::setCoilLevel,
                        GT_TileEntity_IntensifyChemicalDistorter::getCoilLevel)))
            .addElement(
                'h',
                GT_HatchElementBuilder.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                    .atLeast(InputHatch, OutputHatch)
                    .adder(GT_TileEntity_IntensifyChemicalDistorter::addToMachineList)
                    .casingIndex(176)/* index of stainless steal casing */
                    .dot(1)/* preview channel of blueprint */
                    .buildAndChain(GregTech_API.sBlockCasings8, 0))
            .addElement(
                'b',
                GT_HatchElementBuilder.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                    .atLeast(InputBus, OutputBus, Maintenance)
                    .adder(GT_TileEntity_IntensifyChemicalDistorter::addToMachineList)
                    .casingIndex(49)/* index of chem inert casing */
                    .dot(2)/* preview channel of blueprint */
                    .buildAndChain(GregTech_API.sBlockCasings4, 1))
            .addElement(
                'e',
                GT_HatchElementBuilder.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                    .atLeast(Energy.or(ExoticEnergy))
                    .adder(GT_TileEntity_IntensifyChemicalDistorter::addToMachineList)
                    .casingIndex(11)
                    .dot(3)
                    .buildAndChain(GregTech_API.sBlockCasings1, 11))
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
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
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
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        if (mode == 0) return GTCMRecipe.instance.IntensifyChemicalDistorterRecipes;
        return GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes;
    }

    // Recipe Processing Handler
    //
    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().enablePerfectOverclock()
            .setMaxParallel(/* this.mode == 0 ? 16 : */ 1024)
            .setSpeedBonus(/* this.mode == 0 ? 1 : */ 0.1F);

    }

    // Power Logic
    // @Override
    // protected void setProcessingLogicPower(ProcessingLogic logic) {
    // logic.setAvailableVoltage(this.getMaxInputEu());
    // logic.setAvailableAmperage(1);
    // }

    // @Override
    // protected void setProcessingLogicPower(ProcessingLogic logic) {
    // logic.setAvailableVoltage(getAverageInputVoltage());
    // logic.setAvailableAmperage(getMaxInputAmps());
    // logic.setAmperageOC(true);
    // }

    // @Override
    // protected void setProcessingLogicPower(ProcessingLogic logic) {
    //// boolean useSingleAmp = mEnergyHatches.size() == 1 && mExoticEnergyHatches.size() == 0;
    // logic.setAvailableVoltage(getMaxInputEu());
    // logic.setAvailableAmperage();
    // logic.setAmperageOC(true);
    // }

    //
    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % 2;
            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("preciseassembler.chat." + this.mode));
        }
    }

    //
    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    /**
     * Checks if this is a Correct Machine Part for this kind of Machine (Turbine Rotor for example)
     *
     */
    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     *
     * @param aBaseMetaTileEntity
     * @param aStack
     */
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // this.casingAmountActual = 0; // re-init counter
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
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

    // Tooltips
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_ICD_MachineType)
            .addInfo("Controller block for the Intensify Chemical Distorter")
            .addInfo(
                EnumChatFormatting.AQUA + "I! "
                    + EnumChatFormatting.BLUE
                    + "AM! "
                    + EnumChatFormatting.AQUA
                    + "THE! "
                    + EnumChatFormatting.BLUE
                    + "CHEM! "
                    + EnumChatFormatting.AQUA
                    + "THAT! "
                    + EnumChatFormatting.BLUE
                    + "IS! "
                    + EnumChatFormatting.AQUA
                    + "APROOOOOACHING !!")
            .addInfo("The most advanced base chemical reactor.")
            .addInfo("Use screwdriver to change mode.")
            .addInfo(EnumChatFormatting.GOLD + "Intensify Chemical Distorter mode: ")
            .addInfo("Focus on processing the most complex chemical reaction - 16x Parallel.")
            .addInfo(EnumChatFormatting.GOLD + "Chemical Reactor mode: ")
            .addInfo("1024 Parallel and 900% faster than using LCR of the same voltage.")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(11, 13, 11, false)
            .addController("Front center")
            .addCasingInfoRange("Casing", 8, 26, false)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addInputBus("Any Casing", 2)
            .addOutputBus("Any Casing", 2)
            .addMaintenanceHatch("Any Casing", 2)
            .addEnergyHatch("Any Casing", 3)
            .toolTipFinisher("GTNH Community Mod");
        return tt;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        this.coilLevel = aCoilLevel;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.coilLevel;
    }
}
