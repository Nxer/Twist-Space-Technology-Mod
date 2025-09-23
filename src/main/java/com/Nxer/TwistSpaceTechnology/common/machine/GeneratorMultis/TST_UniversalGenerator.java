package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings1;
import gtPlusPlus.core.block.ModBlocks;

public class TST_UniversalGenerator extends GTCM_MultiMachineBase<TST_UniversalGenerator> {

    // region Class Constructor
    public TST_UniversalGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_UniversalGenerator(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic
    private int mCasing = 0;

    private int mSetTier = 1;
    private double fuelBurning;
    private long DYNAMO_AMP;
    private long DYNAMO_TIER;
    private long euPerTick;
    private String fuelName = "";

    @Override
    public RecipeMap<FuelBackend> getRecipeMap() {
        if (mSetTier == 2) {
            return RecipeMaps.dieselFuels;
        }
        return RecipeMaps.gasTurbineFuels;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.gasTurbineFuels, RecipeMaps.dieselFuels);
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        this.mSetTier = 0;
        this.mCasing = 0;
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        ArrayList<FluidStack> tFluids = getStoredFluids();

        if (!tFluids.isEmpty()) {
            for (FluidStack tFluid : tFluids) {
                GTRecipe tRecipe = getRecipeMap().getBackend()
                    .findFuel(tFluid);

                if (tRecipe == null) continue;
                // checkProcessing
                int fuelValue = tRecipe.mSpecialValue * 1_000;
                euPerTick = DYNAMO_TIER * DYNAMO_AMP;
                fuelBurning = (double) fuelValue / (euPerTick * 20);
                fuelName = tFluid.getLocalizedName();

                FluidStack consumeFluid = tFluid.copy();
                consumeFluid.amount = 1_000;

                if (tFluid.amount >= 1_000) {
                    if (depleteInput(consumeFluid)) {
                        this.mMaxProgresstime = (int) (fuelBurning * 20);
                        this.mEfficiency = getMaxEfficiency(null);
                        this.lEUt = euPerTick;
                        return CheckRecipeResultRegistry.GENERATING;
                    }
                }
            }
        }

        this.lEUt = 0;
        this.mEfficiency = 0;
        this.mProgresstime = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        Object[][] pieces = {
            // Gas shape 1 tier
            { STRUCTURE_PIECE_GAS, horizontalOffSetGas, verticalOffSetGas, depthOffSetGas, 1 },
            // Fuel shape 2 tier
            { STRUCTURE_PIECE_FUEL, horizontalOffSetFuel, verticalOffSetFuel, depthOffSetFuel, 2 } };

        for (Object[] piece : pieces) {
            clearHatches();
            if (checkPiece((String) piece[0], (int) piece[1], (int) piece[2], (int) piece[3])) {
                mSetTier = (int) piece[4];
                break;
            }
        }

        if (mSetTier == 0) return false;

        DYNAMO_AMP = getDynamoAmperage();
        DYNAMO_TIER = getTierDynamo();

        return (this.mCasing >= 45 && checkCountDynamo(2) && !checkMixedDynamo() && setDynamoTier(3, false));
    }

    // endregion

    // region Structure
    // spotless:off
    @Override
    public void construct(ItemStack itemStack, boolean b) {
        if (itemStack.stackSize == 1) {
            buildPiece(STRUCTURE_PIECE_GAS, itemStack, b, horizontalOffSetGas, verticalOffSetGas, depthOffSetGas);
        } else {
            buildPiece(STRUCTURE_PIECE_FUEL, itemStack, b, horizontalOffSetFuel, verticalOffSetFuel, depthOffSetFuel);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int built = 0;
        if (stackSize.stackSize == 1) {
            mSetTier = 1;
            built += this.survivalBuildPiece(STRUCTURE_PIECE_GAS, stackSize, horizontalOffSetGas, verticalOffSetGas, depthOffSetGas, elementBudget, env, false, true);
        } else {
            mSetTier = 2;
            built += this.survivalBuildPiece(STRUCTURE_PIECE_FUEL, stackSize, horizontalOffSetFuel, verticalOffSetFuel, depthOffSetFuel, elementBudget, env, false, true);
        }
        return built;
    }

    private static final String STRUCTURE_PIECE_GAS = "mainGas";
    private static final String STRUCTURE_PIECE_FUEL = "mainFuel";
    private final int horizontalOffSetGas = 4;
    private final int verticalOffSetGas = 3;
    private final int depthOffSetGas = 0;

    private final int horizontalOffSetFuel = 2;
    private final int verticalOffSetFuel = 5;
    private final int depthOffSetFuel = 0;

    private final int mainTextureID = ((BlockCasings1) sBlockCasings1).getTextureIndex(11);
    private static IStructureDefinition<TST_UniversalGenerator> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_UniversalGenerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_UniversalGenerator>builder()
                .addShape(STRUCTURE_PIECE_GAS,
                    transpose(shapeGas))
                .addShape(STRUCTURE_PIECE_FUEL,
                    transpose(shapeFuel))
                .addElement(
                    'A',
                    ofChain(
                        buildHatchAdder(TST_UniversalGenerator.class)
                            .atLeast(Dynamo)
                            .casingIndex(mainTextureID)
                            .dot(1)
                            .build(),
                        buildHatchAdder(TST_UniversalGenerator.class)
                            .atLeast(InputHatch)
                            .casingIndex(mainTextureID)
                            .dot(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(sBlockCasings1, 11))))
                .addElement('B',ofBlock(GregTechAPI.sBlockCasings2, 3))
                .addElement('C',ofBlock(ModBlocks.blockCasingsMisc,2))
                .addElement('D', ofFrame(Materials.Steel))
                .addElement('E', ofBlock(GregTechAPI.sBlockMetal6, 13))
                .addElement('F',ofBlock(GregTechAPI.sBlockTintedGlass, 0))

                .build();
        }
        return STRUCTURE_DEFINITION;
    }

	/*
	Blocks:
A -> ofBlock...(gt.blockcasings, 11, ...);
B -> ofBlock...(gt.blockcasings2, 3, ...);
С -> ofBlock...(miscutils.blockcasings, 2, ...);
D -> ofBlock...(gt.blockframes, 305, ...);
E -> ofBlock...(gt.blockmetal1, 12, ...);
F -> ofBlock...(gt.blocktintedglass, 0, ...);
	 */

    private final String[][] shapeGas = new String[][]{
        {"         ","         ","         ","         ","         ","D   A   D","DAAAAAAAD","DAAAAAAAD","DAAAAAAAD","D   A   D"},
        {"         ","    D    ","         ","         ","         ","DAFFAFFAD","A       A","AE   E  A","A       A","DAFFAFFAD"},
        {"    D    ","   DAD   ","    D    ","         ","         ","DAFFAFFAD","A   E   A","ACCCCCCCA","A E   E A","DAFFAFFAD"},
        {"   D~D   ","   ABA   ","   DAD   ","         ","         ","DAFFAFFAD","A       A","A  E   EA","A       A","DAFFAFFAD"},
        {"   DAD   ","   AAA   ","   DAD   ","         ","         ","D   A   D"," AAAAAAA "," AAAAAAA "," AAAAAAA ","D   A   D"}
    };

    	/*
	Blocks:
A -> ofBlock...(gt.blockcasings, 11, ...);
B -> ofBlock...(gt.blockcasings2, 3, ...);
С -> ofBlock...(miscutils.blockcasings, 2, ...);
D -> ofBlock...(gt.blockmetal1, 12, ...);
E -> ofBlock...(gt.blocktintedglass, 0, ...);
F -> ofBlock...(gt.blockframes, 305, ...);

	 */

    private final String[][] shapeFuel = new String[][]{
        {"     ","     ","     ","     ","     ","  D  "," AAA ","DAAAD"," AAA ","  D  "},
        {"     ","     ","     ","     ","     ","DAAAD","A   A","A CEA","A   A","DAAAD"},
        {"     ","     ","     ","     ","     ","DFFFD","F   F","F C F","F E F","DFFFD"},
        {"     ","  D  ","     ","     ","     ","DFFFD","F   F","FEC F","F   F","DFFFD"},
        {"  D  "," DAD ","  D  ","     ","     ","DFFFD","F E F","F C F","F   F","DFFFD"},
        {" D~D "," ABA "," DAD ","     ","     ","DFFFD","F   F","F CEF","F   F","DFFFD"},
        {" DAD "," AAA "," DAD ","     ","     ","DAAAD","AAAAA","AAAAA","AAAAA","DAAAD"}
    };
    // spotless:on
    // endregion

    // region Overrides
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_UniversalGenerator_MachineType)
            .addInfo(TextLocalization.Tooltip_UniversalGenerator_00)
            .addInfo(TextLocalization.Tooltip_UniversalGenerator_01)
            .addInfo(TextLocalization.Tooltip_UniversalGenerator_02)
            .addInfo(TextLocalization.Tooltip_UniversalGenerator_03)
            .addInfo(TextEnums.Author_EvgenWarGold.getText())
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
        return new TST_UniversalGenerator(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(mainTextureID),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(mainTextureID), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
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
                    // #tr TST_UniversalGenerator.gui.01
                    // # Mode:
                    // #zh_CN 模式 :
                    + TextEnums.tr("TST_UniversalGenerator.gui.01")
                    + " "
                    + EnumChatFormatting.GOLD
                    // #tr TST_UniversalGenerator.gui.02
                    // # Gas:
                    // #zh_CN 燃气 :

                    // #tr TST_UniversalGenerator.gui.03
                    // # Fuel:
                    // #zh_CN 燃油 :
                    + (mSetTier == 2 ? TextEnums.tr("TST_UniversalGenerator.gui.03")
                        : TextEnums.tr("TST_UniversalGenerator.gui.02"))
                    + EnumChatFormatting.RESET)
                .setEnabled(mSetTier != 0))
            .widget(
                new TextWidget().setStringSupplier(
                    () -> EnumChatFormatting.WHITE
                        // #tr TST_UniversalGenerator.gui.04
                        // # Current fluid:
                        // #zh_CN 当前使用:
                        + TextEnums.tr("TST_UniversalGenerator.gui.04")
                        + " "
                        + EnumChatFormatting.GOLD
                        + fuelName
                        + EnumChatFormatting.RESET)
                    .setEnabled(!Objects.equals(fuelName, "")))
            .widget(
                new TextWidget().setStringSupplier(
                    () -> EnumChatFormatting.WHITE
                        // #tr TST_UniversalGenerator.gui.05
                        // # Eu per tick:
                        // #zh_CN 发电量 EU/t :
                        + TextEnums.tr("TST_UniversalGenerator.gui.05")
                        + " "
                        + EnumChatFormatting.GOLD
                        + numberFormat.format(euPerTick)
                        + EnumChatFormatting.RESET)
                    .setEnabled(euPerTick != 0))
            .widget(
                new TextWidget().setStringSupplier(
                    () -> EnumChatFormatting.WHITE
                        // #tr TST_UniversalGenerator.gui.06
                        // # Fuel burning:
                        // #zh_CN 燃料消耗速度 :
                        + TextEnums.tr("TST_UniversalGenerator.gui.06")
                        + " "
                        + EnumChatFormatting.GOLD
                        + numberFormat.format(fuelBurning)
                        + EnumChatFormatting.WHITE
                        // #tr TST_SteamBasicGenerator.gui.02
                        // # /s
                        + TextEnums.tr("TST_SteamBasicGenerator.gui.02")
                        + EnumChatFormatting.RESET)
                    .setEnabled(fuelBurning != 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> euPerTick, val -> euPerTick = val))
            .widget(new FakeSyncWidget.DoubleSyncer(() -> fuelBurning, val -> fuelBurning = val))
            .widget(new FakeSyncWidget.StringSyncer(() -> fuelName, val -> fuelName = val))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> mSetTier, val -> mSetTier = val));
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mSetTier", mSetTier);
        aNBT.setLong("euPerTick", euPerTick);
        aNBT.setDouble("fuelBurning", fuelBurning);
        aNBT.setString("fuelName", fuelName);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mSetTier = aNBT.getInteger("mSetTier");
        euPerTick = aNBT.getLong("euPerTick");
        fuelBurning = aNBT.getDouble("fuelBurning");
        fuelName = aNBT.getString("fuelName");
    }
    // endregion
}
