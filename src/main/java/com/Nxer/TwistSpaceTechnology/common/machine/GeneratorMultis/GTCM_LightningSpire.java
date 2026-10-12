package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import static com.Nxer.TwistSpaceTechnology.common.machine.MiscHelper.LightningRod;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.NoLightningRods;
import static com.Nxer.TwistSpaceTechnology.util.TSTUtils.tr;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.AUTHOR;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.MAINTAINER;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import static tectech.thing.casing.BlockGTCasingsTT.textureOffset;
import static tectech.thing.casing.BlockGTCasingsTT.texturePage;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsBA0;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_LightningSpire;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TST_GeneratorBase;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults;
import com.Nxer.TwistSpaceTechnology.common.misc.MachineShutDownReasons.SimpleShutDownReasons;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@SkipGenerateDescription
public class GTCM_LightningSpire extends TST_GeneratorBase<GTCM_LightningSpire>
    implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GTCM_LightningSpire(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        registerTooltipCredits(AUTHOR, ID.SNOW_DREAM, MAINTAINER, ID.NXER);
    }

    public GTCM_LightningSpire(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_LightningSpire(this.mName);
    }
    // endregion

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
    // spotless:on

    @Override
    public IStructureDefinition<GTCM_LightningSpire> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GTCM_LightningSpire>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement('C', ofBlock(sBlockCasingsBA0, 7))
                .addElement('D', ofBlock(sBlockCasingsBA0, 8))
                .addElement('A', ofBlock(sBlockCasingsBA0, 4))
                .addElement(
                    'B',
                    buildHatchAdder(GTCM_LightningSpire.class)
                        .atLeast(Dynamo.or(ExoticDynamo), InputBus, InputHatch, OutputBus)
                        .hint(1)
                        .casingIndex(textureOffset + 16 + 6)
                        .buildAndChain(sBlockCasingsBA0, 6))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, hOffset, vOffset, dOffset);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
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

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset, errors)) return;
        setLightningPosition(getBaseMetaTileEntity().getFrontFacing());
    }
    // endregion

    // region Processing Logic
    // endregion end
    public static final int CRYOTHEUM_CONSUMPTION = 128;

    protected static Fluid MOLTEN_IRON;
    protected static Fluid CRYOTHEUM;
    private static final int MAXRODS = 512;
    List<ItemStack> mStored = new ArrayList<>();
    public long tStoredEU;
    public long tProductEU;
    public long tMaxStoredEU;
    public boolean enable_lightning = true;
    public boolean outputtingRods = false;
    public boolean powerGeneration = false;
    public int tRods;
    private int aX;
    private int aY;
    private int aZ;

    public int getRods() {
        return tRods;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    protected void outputAfterRecipe() {
        super.outputAfterRecipe();
        if (powerGeneration) {
            powerGeneration = false;
            tStoredEU = Math.min(tStoredEU + tProductEU, tMaxStoredEU);
        }
    }

    public void checkInputRods() {
        if (tRods >= MAXRODS) return;
        List<ItemStack> inputs = getStoredInputs();
        if (inputs.isEmpty()) return;

        int canAdd = MAXRODS - tRods;
        for (ItemStack machine : inputs) {
            if (null == machine || machine.stackSize < 1) continue;
            if (LightningRod.equalItemStack(machine)) {
                if (canAdd > machine.stackSize) {
                    mStored.add(machine.copy());
                    tRods += machine.stackSize;
                    canAdd -= machine.stackSize;
                    machine.stackSize = 0;
                } else {
                    mStored.add(GTUtility.copyAmountUnsafe(MAXRODS - tRods, machine));
                    machine.stackSize -= canAdd;
                    tRods = MAXRODS;
                    break;
                }
            }
        }

        tProductEU = tRods * 28000000L;
        tMaxStoredEU = tRods * 280000000L;
        updateSlots();

    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {

        if (outputtingRods) {
            outputtingRods = false;
            if (tRods > 0) {
                mOutputItems = mStored.toArray(new ItemStack[0]);
                mStored.clear();
                this.updateSlots();
                tRods = 0;
                tProductEU = 0;
                tStoredEU = 0;
                tMaxStoredEU = 0;
                this.mMaxProgresstime = 20;
                updateSlots();
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }

        checkInputRods();

        if (tRods < 1) {
            return NoLightningRods;
        }

        List<FluidStack> tFluids = getStoredFluids();
        // If no fluids input, return failed directly.
        if (tFluids.isEmpty()) {
            stopMachine(SimpleShutDownReasons.NoCorrectFluidInput);
            return CheckRecipeResults.NoCorrectFluidInput;
        }

        int tCryotheum = 0;
        List<FluidStack> cryotheums = new ArrayList<>();
        int tMoltenIron = 0;
        List<FluidStack> moltenIrons = new ArrayList<>();

        // check fluids
        for (FluidStack f : tFluids) {
            if (null == f || f.amount < 1) continue;
            if (f.getFluid() == CRYOTHEUM) {
                tCryotheum += f.amount;
                cryotheums.add(f);
            } else if (f.getFluid() == MOLTEN_IRON) {
                tMoltenIron += f.amount;
                moltenIrons.add(f);
            }
        }

        int moltenIronConsumption = tRods * 72;
        if (tCryotheum == CRYOTHEUM_CONSUMPTION && tMoltenIron == moltenIronConsumption) {
            // consume cryotheum
            int toConsume = CRYOTHEUM_CONSUMPTION;
            for (FluidStack f : cryotheums) {
                if (f.amount >= toConsume) {
                    f.amount -= toConsume;
                    break;
                } else {
                    toConsume -= f.amount;
                    f.amount = 0;
                }
            }

            // consume molten iron
            toConsume = moltenIronConsumption;
            for (FluidStack f : moltenIrons) {
                if (f.amount >= toConsume) {
                    f.amount -= toConsume;
                    break;
                } else {
                    toConsume -= f.amount;
                    f.amount = 0;
                }
            }

            if (Config.MoreSafetyPowerGeneration_LightningSpire) {
                powerGeneration = true;
            } else {
                // add stored eu
                tStoredEU = Math.min(tStoredEU + tProductEU, tMaxStoredEU);
            }

            // light animation
            lightOnWorld();
        } else {
            // generating failed
            stopMachine(SimpleShutDownReasons.NoCorrectFluidInput);
            return CheckRecipeResults.NoCorrectFluidInput;
        }

        this.mMaxProgresstime = 256;
        updateSlots();
        return CheckRecipeResultRegistry.GENERATING;

    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (null == MOLTEN_IRON) {
                MOLTEN_IRON = Materials.Iron.getMolten(1)
                    .getFluid();
            }
            if (null == CRYOTHEUM) {
                CRYOTHEUM = FluidRegistry.getFluid("cryotheum");
            }
        }
    }

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

    protected void lightOnWorld() {
        if (!enable_lightning) return;
        World world = getBaseMetaTileEntity().getWorld();
        world.addWeatherEffect(new EntityLightningBolt(world, aX, aY, aZ));
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        if (tStoredEU > 0) {
            // push eu to dynamo
            for (MTEHatchDynamo eDynamo : super.mDynamoHatches) {
                if (eDynamo == null || !eDynamo.isValid()) {
                    continue;
                }
                final long power = eDynamo.maxEUStore() - eDynamo.getEUVar();
                if (tStoredEU >= power) {
                    eDynamo.setEUVar(eDynamo.getEUVar() + power);
                    tStoredEU -= power;
                } else {
                    eDynamo.setEUVar(eDynamo.getEUVar() + tStoredEU);
                    tStoredEU = 0L;
                }
            }

            for (MTEHatch eDynamo : mExoticDynamoHatches) {
                if (eDynamo == null || !eDynamo.isValid()) {
                    continue;
                }
                final long power = eDynamo.maxEUStore() - eDynamo.getEUVar();
                if (tStoredEU >= power) {
                    eDynamo.setEUVar(eDynamo.getEUVar() + power);
                    tStoredEU -= power;
                } else {
                    eDynamo.setEUVar(eDynamo.getEUVar() + tStoredEU);
                    tStoredEU = 0L;
                }
            }
        }

        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (getBaseMetaTileEntity().isServerSide()) {
            enable_lightning = !enable_lightning;
            // #tr tst.common.machine.LightningSpire.message.enable_lightning.true
            // # Enable lightning animation
            // #zh_CN 启用闪电特效

            // #tr tst.common.machine.LightningSpire.message.enable_lightning.false
            // # Disable lightning animation
            // #zh_CN 禁用闪电特效
            GTUtility.sendChatTrans(
                aPlayer,
                tr("tst.common.machine.LightningSpire.message.enable_lightning." + enable_lightning));
            return true;
        }
        return false;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new TST_Gui_LightningSpire(this);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new ProgressBar().setProgress(() -> (float) tStoredEU / tMaxStoredEU)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setTexture(GTUITextures.PROGRESSBAR_STORED_EU, 147)
                .setPos(7, 85)
                .setSize(130, 5));
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget().setStringSupplier(() -> "Currently stored LR:" + numberFormat.format(tRods))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> tRods, val -> tRods = val))
            .widget(
                new TextWidget().setStringSupplier(() -> "EU Gen per strike:" + numberFormat.format(tProductEU))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> tProductEU, val -> tProductEU = val));
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("enable_lightning", enable_lightning);
        aNBT.setLong("tStoredEU", tStoredEU);
        aNBT.setLong("tProductEU", tProductEU);
        aNBT.setLong("tMaxStoredEU", tMaxStoredEU);
        aNBT.setInteger("tRods", tRods);
        aNBT.setBoolean("powerGeneration", powerGeneration);
        NBTTagList tTags = new NBTTagList();
        for (ItemStack titem : mStored) {
            tTags.appendTag(titem.writeToNBT(new NBTTagCompound()));
        }
        aNBT.setTag("tTags", tTags);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        enable_lightning = aNBT.getBoolean("enable_lightning");
        tStoredEU = aNBT.getLong("tStoredEU");
        tProductEU = aNBT.getLong("tProductEU");
        tMaxStoredEU = aNBT.getLong("tMaxStoredEU");
        tRods = aNBT.getInteger("tRods");
        powerGeneration = aNBT.getBoolean("powerGeneration");
        NBTTagList tTags = aNBT.getTagList("tTags", 10);
        for (int i = 0; i < tTags.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = tTags.getCompoundTagAt(i);
            mStored.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
        }
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return TSTControllerTextures.getTexture(
            side,
            facing,
            active,
            Textures.BlockIcons.casingTexturePages[texturePage][16 + 6],
            OVERLAY_DTPF_OFF,
            OVERLAY_DTPF_OFF_GLOW,
            OVERLAY_DTPF_ON,
            OVERLAY_FUSION1_GLOW);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final TSTMultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.LightningSpire.tooltip.machine_type
        // # Multi Lightning Rod
        // #zh_CN 多方块避雷针
        tt.addMachineType(tr("tst.common.machine.LightningSpire.tooltip.machine_type"))
            // #tr tst.common.machine.LightningSpire.tooltip.info.01
            // # {\BLUE}"Thunder is God's cannon."
            // #zh_CN {\BLUE}“雷霆是上帝的大炮”
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.01"))
            // #tr tst.common.machine.LightningSpire.tooltip.info.02
            // # {\DARK_BLUE}"But now we will control the thunder."
            // #zh_CN {\DARK_BLUE}“但现在我们将掌控雷霆”
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.02"))
            .addSeparator()
            // #tr tst.common.machine.LightningSpire.tooltip.info.03
            // # {\AQUA}Maximum storage capacity of 512 lightning rods(I).
            // #zh_CN {\AQUA}最大存储512个避雷针（I）
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.03"))
            // #tr tst.common.machine.LightningSpire.tooltip.info.04
            // # {\AQUA}Each lightning rod produces 28 MEU per lightning strike and stores 280 MEU.
            // #zh_CN {\AQUA}每个避雷针每次雷击生产28MEU，并且存储280MEU
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.04"))
            // #tr tst.common.machine.LightningSpire.tooltip.info.05
            // # {\AQUA}Ignoring thunderstorm weather for power generation.
            // #zh_CN {\AQUA}无视雷雨天气发电
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.05"))
            // #tr tst.common.machine.LightningSpire.tooltip.info.06
            // # {\AQUA}Consume 128mb cruotheum and 72mb*the number of lightning rods of molten iron ever lightning
            // #zh_CN {\AQUA}每次雷击均会消耗128mb的极寒之凛冰以及72mb*避雷针数量的熔融铁
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.06"))
            // #tr tst.common.machine.LightningSpire.tooltip.info.07
            // # {\AQUA}Quantitative input is required, too much or too little can lead to power generation failure
            // #zh_CN {\AQUA}需要定量输入,过多过少均会导致发电失败
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.07"))
            .addSeparator()
            // #tr tst.common.machine.LightningSpire.tooltip.info.10
            // # {\UNDERLINE}Before dismantling the machine, please output the lightning rod first!
            // #zh_CN {\UNDERLINE}拆除机器前请先输出避雷针
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.10"))
            // #tr tst.common.machine.LightningSpire.tooltip.info.11
            // # {\UNDERLINE}Otherwise all internal lightning rods will be lost!
            // #zh_CN {\UNDERLINE}否则会丢失所有内部避雷针！
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.11"))
            // #tr tst.common.machine.LightningSpire.tooltip.info.12
            // # Use a wire cutter to enable/disable lightning animation.
            // #zh_CN 使用剪线钳开启/关闭闪电特效
            .addInfo(tr("tst.common.machine.LightningSpire.tooltip.info.12"))
            .beginStructureBlock(shapeMain)
            .addInputHatch(TSTSharedLocalization.Structure.textUseBlueprint)
            .addInputBus(TSTSharedLocalization.Structure.textUseBlueprint)
            .addOutputBus(TSTSharedLocalization.Structure.textUseBlueprint)
            .addDynamoHatch(TSTSharedLocalization.Structure.textUseBlueprint)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

}
