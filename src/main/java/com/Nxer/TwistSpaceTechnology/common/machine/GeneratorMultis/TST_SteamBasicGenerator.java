package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings2;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.filterByMTETier;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings2;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class TST_SteamBasicGenerator extends GTCM_MultiMachineBase<TST_SteamBasicGenerator> {

    // region Class Constructor
    public TST_SteamBasicGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_SteamBasicGenerator(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic

    private final int STEAM_PER_SEC = 915;
    private final int EU_PER_TICK = 32;
    private long DYNAMO_AMP = 0;
    private int mCasing = 0;
    public static FluidStack steamFluid = FluidUtils.getSteam(1);

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        final ArrayList<FluidStack> storedFluids = super.getStoredFluids();
        for (FluidStack hatchFluid : storedFluids) {
            if (hatchFluid.isFluidEqual(steamFluid) && steamFluid != null) {
                steamFluid.amount = (int) (STEAM_PER_SEC * getDynamoAmperage());
                if (getAllMaxDynamoBuffer() != getAllDynamoBuffer()) {
                    if (super.depleteInput(steamFluid)) {
                        super.lEUt = EU_PER_TICK * getDynamoAmperage();
                        super.mMaxProgresstime = 20;
                        super.mEfficiency = getMaxEfficiency(null);
                        return CheckRecipeResultRegistry.GENERATING;
                    }
                }
            }
        }
        super.lEUt = 0;
        super.mEfficiency = 0;
        super.mMaxProgresstime = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mCasing = 0;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        DYNAMO_AMP = getDynamoAmperage();

        return (this.mCasing >= 10 && !this.mInputHatches.isEmpty()
            && this.mDynamoHatches.size() <= 2
            && setDynamoTier(1, true));
    }

    // endregion

    // region Structure
    // spotless:off
    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    private static final String STRUCTURE_PIECE_MAIN = "mainSteamBasicGenerator";
    private final int horizontalOffSet = 2;
    private final int verticalOffSet = 2;
    private final int depthOffSet = 1;
    private final int mainTextureID = ((BlockCasings2) sBlockCasings2).getTextureIndex(0);
    private static IStructureDefinition<TST_SteamBasicGenerator> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_SteamBasicGenerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_SteamBasicGenerator>builder()
                .addShape(STRUCTURE_PIECE_MAIN,
                    transpose(shapeMain))
                .addElement(
                    'A',
                    ofChain(
                        buildHatchAdder(TST_SteamBasicGenerator.class)
                            .atLeast(Dynamo)
                            .hatchItemFilterAnd(t -> filterByMTETier(1, 1))
                            .casingIndex(mainTextureID)
                            .dot(1)
                            .build(),
                        buildHatchAdder(TST_SteamBasicGenerator.class)
                            .atLeast(InputHatch)
                            .casingIndex(mainTextureID)
                            .dot(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(sBlockCasings2, 0))))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings3, 14))
                .addElement('C', ofFrame(Materials.Bronze))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

	/*
	Blocks:
A -> ofBlock...(gt.blockcasings2, 0, ...);
B -> ofBlock...(gt.blockcasings3, 14, ...);
C -> ofBlock...(gt.blockframes, 300, ...);
	 */

    private final String[][] shapeMain = new String[][]{
        {"     ", " C C ", "  A  ", " C C ", "     "},
        {"C   C", "  A  ", " ABA ", "  A  ", "C   C"},
        {"C   C", "  ~  ", " ABA ", "  A  ", "C   C"},
        {"C   C", " AAA ", " AAA ", " AAA ", "C   C"}
    };
    // spotless:on
    // endregion

    // region Overrides
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_SteamBasicGenerator_MachineType)
            .addInfo(TextLocalization.Tooltip_SteamBasicGenerator_00)
            .addInfo(TextLocalization.Tooltip_SteamBasicGenerator_01)
            .addInfo(TextLocalization.Tooltip_SteamBasicGenerator_02)
            .addInfo(TextLocalization.Tooltip_SteamBasicGenerator_03)
            .addInfo(TextEnums.Author_EvgenWarGold.getText())
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addDynamoHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_SteamBasicGenerator(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(mainTextureID),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(mainTextureID), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(mainTextureID) };
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements.widget(
            new TextWidget().setStringSupplier(
                () -> EnumChatFormatting.WHITE
                    // #tr TST_SteamBasicGenerator.gui.01
                    // # Steam consumption:
                    + TextEnums.tr("TST_SteamBasicGenerator.gui.01")
                    + " "
                    + EnumChatFormatting.GOLD
                    + numberFormat.format(STEAM_PER_SEC * DYNAMO_AMP)
                    + EnumChatFormatting.WHITE
                    // #tr TST_SteamBasicGenerator.gui.02
                    // # /s
                    + TextEnums.tr("TST_SteamBasicGenerator.gui.02")
                    + EnumChatFormatting.RESET)
                .setEnabled((STEAM_PER_SEC * DYNAMO_AMP) != 0))
            .widget(
                new TextWidget().setStringSupplier(
                    () -> EnumChatFormatting.WHITE
                        // #tr TST_SteamBasicGenerator.gui.03
                        // # Currently generates:
                        + TextEnums.tr("TST_SteamBasicGenerator.gui.03")
                        + " "
                        + EnumChatFormatting.GOLD
                        + numberFormat.format(EU_PER_TICK * DYNAMO_AMP)
                        + EnumChatFormatting.WHITE
                        // #tr TST_SteamBasicGenerator.gui.04
                        // # eu/t
                        + TextEnums.tr("TST_SteamBasicGenerator.gui.04")
                        + EnumChatFormatting.RESET)
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.LongSyncer(() -> DYNAMO_AMP, val -> DYNAMO_AMP = val));
    }
    // endregion
}
