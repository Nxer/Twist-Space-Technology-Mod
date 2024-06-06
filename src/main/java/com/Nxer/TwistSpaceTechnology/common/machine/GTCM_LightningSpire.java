package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM.HatchElement.DynamoMulti;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Dynamo;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.ItemList.Machine_HV_LightningRod;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class GTCM_LightningSpire extends GT_MetaTileEntity_MultiblockBase_EM
    implements IConstructable, ISurvivalConstructable {

    // region Construct
    public GTCM_LightningSpire(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        super.useLongPower = true;
    }

    public GTCM_LightningSpire(String name) {
        super(name);
        super.useLongPower = true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_LightningSpire(this.mName);
    }
    // region end

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "STRUCTURE_PIECE_MAIN_LR";
    private final int hOffset = 5, vOffset = 20, dOffset = 3;
    private static IStructureDefinition<GTCM_LightningSpire> STRUCTURE_DEFINITION = null;
    // spotless:off
    protected final String[][] shapeMain = new String[][]{
            {"           ","           ","           ","           ","    CCC    ","    CCC    ","    CCC    ","           ","           ","           ","           "},
            {"           ","           ","           ","    CCC    ","   CCCCC   ","   CCCCC   ","   CCCCC   ","    CCC    ","           ","           ","           "},
            {"           ","           ","           ","    CCC    ","   CCCCC   ","   CCCCC   ","   CCCCC   ","    CCC    ","           ","           ","           "},
            {"           ","           ","           ","    CCC    ","   CCCCC   ","   CCCCC   ","   CCCCC   ","    CCC    ","           ","           ","           "},
            {"           ","           ","           ","           ","    CCC    ","    CCC    ","    CCC    ","           ","           ","           ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","           ","           ","    CCC    ","   C C C   ","   CCDCC   ","   C C C   ","    CCC    ","           ","           ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","           ","   CCCCC   ","  C  C  C  ","  C  C  C  ","  CCCDCCC  ","  C  C  C  ","  C  C  C  ","   CCCCC   ","           ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","   CCCCC   ","  C  C  C  "," C   C   C "," C   C   C "," CCCCDCCCC "," C   C   C "," C   C   C ","  C  C  C  ","   CCCCC   ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","           ","           ","           ","           ","     D     ","           ","           ","           ","           ","           "},
            {"           ","           ","           ","           ","    BBB    ","    BDB    ","    BBB    ","           ","           ","           ","           "},
            {"           ","           ","           ","    BBB    ","   BCCCB   ","   BCDCB   ","   BCCCB   ","    BBB    ","           ","           ","           "},
            {"           ","           ","           ","    B~B    ","   BCCCB   ","   BCDCB   ","   BCCCB   ","    BBB    ","           ","           ","           "},
            {"           ","           ","    BBB    ","   BBBBB   ","  BBAAABB  ","  BBADABB  ","  BBAAABB  ","   BBBBB   ","    BBB    ","           ","           "},
            {"   BBBBB   ","  BBBBBBB  "," BBBBBBBBB ","BBBBBBBBBBB","BBBBAAABBBB","BBBBABABBBB","BBBBAAABBBB","BBBBBBBBBBB"," BBBBBBBBB ","  BBBBBBB  ","   BBBBB   "}
    };
    //spotless:on

    @Override
    public IStructureDefinition<GTCM_LightningSpire> getStructure_EM() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GTCM_LightningSpire>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement('C', ofBlock(sBlockCasingsBA0, 7))
                .addElement('D', ofBlock(sBlockCasingsBA0, 8))
                .addElement('A', ofBlock(sBlockCasingsBA0, 4))
                .addElement(
                    'B',
                    buildHatchAdder(GTCM_LightningSpire.class)
                        .atLeast(Dynamo.or(DynamoMulti), InputBus, InputHatch, OutputBus, Maintenance)
                        .dot(1)
                        .casingIndex(textureOffset + 16 + 6)
                        .buildAndChain(sBlockCasingsBA0, 6))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hOffset,
            vOffset,
            dOffset,
            elementBudget,
            env,
            false,
            true);
    }
    // region end

    // region Process
    List<ItemStack> mStored = new ArrayList<>();
    private final int MAXRODS = 512;
    private long tStored;
    private long tProduct;
    private long tMaxStored;
    private int OperatingMode = 0;
    private int tRods;
    private int aX;
    private int aY;
    private int aZ;

    private void setLightningPosition(ForgeDirection face) {
        aY = this.getBaseMetaTileEntity()
            .getYCoord() + 21;
        if (face == NORTH) {
            aX = this.getBaseMetaTileEntity()
                .getXCoord();
            aZ = this.getBaseMetaTileEntity()
                .getZCoord() + 2;
        } else if (face == SOUTH) {
            aX = this.getBaseMetaTileEntity()
                .getXCoord();
            aZ = this.getBaseMetaTileEntity()
                .getZCoord() - 2;
        } else if (face == WEST) {
            aX = this.getBaseMetaTileEntity()
                .getXCoord() + 2;
            aZ = this.getBaseMetaTileEntity()
                .getZCoord();
        } else if (face == EAST) {
            aX = this.getBaseMetaTileEntity()
                .getXCoord() - 2;
            aZ = this.getBaseMetaTileEntity()
                .getZCoord();
        } else {
            aX = this.getBaseMetaTileEntity()
                .getXCoord();
            aZ = this.getBaseMetaTileEntity()
                .getZCoord();
        }
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing_EM() {

        if (OperatingMode == 0 && tRods > 0) {

            int tCryotheum = 0;
            int tMoltenIron = 0;

            if (mRuntime % 256 == 0) {

                List<FluidStack> tFluids = getStoredFluids();

                World aWorld = this.getBaseMetaTileEntity()
                    .getWorld();

                setLightningPosition(
                    this.getBaseMetaTileEntity()
                        .getFrontFacing());

                for (FluidStack tFluid : tFluids) {
                    if (tFluid != null && tFluid.isFluidEqual(FluidUtils.getFluidStack("cryotheum", 1))) {
                        tCryotheum += tFluid.amount;
                    }
                    if (tFluid != null && tFluid.isFluidEqual(Materials.Iron.getMolten(1))) {
                        tMoltenIron += tFluid.amount;
                    }
                }

                if (tCryotheum == 128 && tMoltenIron == tRods * 72) {
                    depleteInput(FluidUtils.getFluidStack("cryotheum", 128));
                    depleteInput(Materials.Iron.getMolten(tMoltenIron));
                    tStored = Math.min(tStored + tProduct, tMaxStored);
                    aWorld.addWeatherEffect(new EntityLightningBolt(aWorld, aX, aY, aZ));
                } else {
                    stopMachine();
                    return CheckRecipeResultRegistry.INTERNAL_ERROR;
                }
                //
            }
            this.mEUt = 0;
            this.mProgresstime = 1;
            this.mMaxProgresstime = 1;
            return CheckRecipeResultRegistry.GENERATING;
        }

        if (OperatingMode > 0) {

            if (OperatingMode == 1 && tRods < MAXRODS) {
                List<ItemStack> tInput = getStoredInputs();
                for (ItemStack machine : tInput) {
                    if (machine.isItemEqual(Machine_HV_LightningRod.get(1))) {
                        if (MAXRODS - tRods >= 64) {
                            mStored.add(GT_Utility.copy(machine));
                            tRods += machine.stackSize;
                            machine.stackSize = 0;
                        } else {
                            mStored.add(GT_Utility.copyAmount(MAXRODS - tRods, machine));
                            machine.stackSize -= MAXRODS - tRods;
                            tRods = MAXRODS;
                        }
                    }
                }
                tProduct = tRods * 28000000L;
                tMaxStored = tRods * 280000000L;
                this.mEUt = 0;
                this.mMaxProgresstime = 1;
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            if (OperatingMode == 2 && tRods > 0 && tStored == 0) {
                this.mOutputItems = mStored.toArray(new ItemStack[0]);
                mStored.clear();
                this.updateSlots();
                tRods = 0;
                tProduct = 0;
                tMaxStored = 0;
                this.mEUt = 0;
                this.mMaxProgresstime = 1;
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }
        this.mEUt = 0;
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {

        // push eu to dynamo
        for (GT_MetaTileEntity_Hatch_Dynamo eDynamo : super.mDynamoHatches) {
            if (eDynamo == null || !eDynamo.isValid()) {
                continue;
            }
            final long power = eDynamo.maxEUStore() - eDynamo.getEUVar();
            if (tStored >= power) {
                eDynamo.setEUVar(eDynamo.getEUVar() + power);
                tStored -= power;
            } else {
                eDynamo.setEUVar(eDynamo.getEUVar() + tStored);
                tStored = 0L;
            }
        }

        for (GT_MetaTileEntity_Hatch_DynamoMulti eDynamo : eDynamoMulti) {
            if (eDynamo == null || !eDynamo.isValid()) {
                continue;
            }
            final long power = eDynamo.maxEUStore() - eDynamo.getEUVar();
            if (tStored >= power) {
                eDynamo.setEUVar(eDynamo.getEUVar() + power);
                tStored -= power;
            } else {
                eDynamo.setEUVar(eDynamo.getEUVar() + tStored);
                tStored = 0L;
            }
        }
        //
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("tStored", tStored);
        aNBT.setLong("tProduct", tProduct);
        aNBT.setLong("tMaxStored", tMaxStored);
        aNBT.setInteger("tRods", tRods);
        aNBT.setInteger("OperatingMode", OperatingMode);
        NBTTagList tTags = new NBTTagList();
        for (ItemStack titem : mStored) {
            tTags.appendTag(titem.writeToNBT(new NBTTagCompound()));
        }
        aNBT.setTag("tTags", tTags);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tStored = aNBT.getLong("tStored");
        tProduct = aNBT.getLong("tProduct");
        tMaxStored = aNBT.getLong("tMaxStored");
        tRods = aNBT.getInteger("tRods");
        OperatingMode = aNBT.getInteger("OperatingMode");
        NBTTagList tTags = aNBT.getTagList("tTags", 10);
        for (int i = 0; i < tTags.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = tTags.getCompoundTagAt(i);
            mStored.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
        }
    }
    // region end

    // region tooltip
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][16 + 6],
                new TT_RenderedExtendedFacingTexture(
                    active ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                        : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][16 + 6] };
    }

    // spotless:off
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        // #tr GTCM_LightningSpire_MachineType
        // # Multi Lightning Rod
        // #zh_CN 多方块避雷针
        tt.addMachineType(TextEnums.tr("GTCM_LightningSpire_MachineType"))
            // #tr GTCM_LightningSpire_01
            // # {\BLUE}"Thunder is God's cannon."
            // #zh_CN {\BLUE}“雷霆是上帝的大炮”
            .addInfo(TextEnums.tr("GTCM_LightningSpire_01"))
            // #tr GTCM_LightningSpire_02
            // # {\DARK_BLUE}"But now we will control the thunder."
            // #zh_CN {\DARK_BLUE}“但现在我们将掌控雷霆”
            .addInfo(TextEnums.tr("GTCM_LightningSpire_02"))
            .addSeparator()
            // #tr GTCM_LightningSpire_03
            // # {\AQUA}Maximum storage capacity of 512 lightning rods(I).
            // #zh_CN {\AQUA}最大存储512个避雷针（I）
            .addInfo(TextEnums.tr("GTCM_LightningSpire_03"))
            // #tr GTCM_LightningSpire_04
            // # {\AQUA}Each lightning rod produces 28 MEU per lightning strike and stores 280 MEU.
            // #zh_CN {\AQUA}每个避雷针每次雷击生产28MEU，并且存储280MEU
            .addInfo(TextEnums.tr("GTCM_LightningSpire_04"))
            // #tr GTCM_LightningSpire_05
            // # {\AQUA}Ignoring thunderstorm weather for power generation.
            // #zh_CN {\AQUA}无视雷雨天气发电
            .addInfo(TextEnums.tr("GTCM_LightningSpire_05"))
            // #tr GTCM_LightningSpire_06
            // # {\AQUA}Each lightning strike requires accurate input of 128 cruotheum and 72 times the number of internal lightning rods of molten iron
            // #zh_CN {\AQUA}每次雷击必须精准输入128凛冰和内部避雷针数量72倍的熔融铁
            .addInfo(TextEnums.tr("GTCM_LightningSpire_06"))
            .addSeparator()
            // #tr GTCM_LightningSpire_07
            // # {\UNDERLINE}Use a screwdriver to switch input, output, and power generation modes.
            // #zh_CN {\UNDERLINE}使用螺丝刀切换输入，输出，发电模式
            .addInfo(TextEnums.tr("GTCM_LightningSpire_07"))
            // #tr GTCM_LightningSpire_08
            // # {\UNDERLINE}Please clear the internal cache power before outputting the machine
            // #zh_CN {\UNDERLINE}输出机器前请先输出完内部电力缓存
            .addInfo(TextEnums.tr("GTCM_LightningSpire_08"))
            // #tr GTCM_LightningSpire_09
            // # {\UNDERLINE}Before dismantling the machine, please output the lightning rod first!
            // #zh_CN {\UNDERLINE}拆除机器前请先输出避雷针
            .addInfo(TextEnums.tr("GTCM_LightningSpire_09"))
            // #tr GTCM_LightningSpire_10
            // # {\UNDERLINE}Otherwise all internal lightning rods will be lost!
            // #zh_CN {\UNDERLINE}否则会丢失所有内部避雷针！
            .addInfo(TextEnums.tr("GTCM_LightningSpire_10"))
            .addSeparator()
            .beginStructureBlock(11, 23, 11, false)
            .addMaintenanceHatch(TextLocalization.BLUE_PRINT_INFO)
            .addInputHatch(TextLocalization.BLUE_PRINT_INFO)
            .addInputBus(TextLocalization.BLUE_PRINT_INFO)
            .addOutputBus(TextLocalization.BLUE_PRINT_INFO)
            .addDynamoHatch(TextLocalization.BLUE_PRINT_INFO)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }
    // spotless:on
    // region end

    // region UI

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.OperatingMode = (this.OperatingMode + 1) % 3;
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal(
                    "Lightning Spire is in" + (this.OperatingMode == 0 ? "Operate Mode"
                        : this.OperatingMode == 1 ? "Input Mode" : "Output Mode")));
        }
    }

    @Override
    public boolean isPowerPassButtonEnabled() {
        return false;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new ProgressBar().setProgress(() -> (float) tStored / tMaxStored)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setTexture(GT_UITextures.PROGRESSBAR_STORED_EU, 147)
                .setPos(7, 85)
                .setSize(130, 5));
    }

    protected static NumberFormatMUI numberFormatLocal;
    static {
        numberFormatLocal = new NumberFormatMUI();
        numberFormatLocal.setMaximumFractionDigits(8);
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget().setStringSupplier(() -> "Currently stored LR:" + numberFormatLocal.format(tRods))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> tRods, val -> tRods = val))
            .widget(
                new TextWidget().setStringSupplier(() -> "EU Gen per strike:" + numberFormatLocal.format(tProduct))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> tProduct, val -> tProduct = val));
    }

    //
}
