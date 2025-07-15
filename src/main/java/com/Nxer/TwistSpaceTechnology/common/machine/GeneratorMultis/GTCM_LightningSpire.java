package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.ItemList.Machine_HV_LightningRod;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import static tectech.thing.casing.BlockGTCasingsTT.textureOffset;
import static tectech.thing.casing.BlockGTCasingsTT.texturePage;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsBA0;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.DynamoMulti;

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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TT_MultiMachineBase_EM;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults;
import com.Nxer.TwistSpaceTechnology.common.misc.MachineShutDownReasons.SimpleShutDownReasons;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
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
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

public class GTCM_LightningSpire extends TT_MultiMachineBase_EM implements IConstructable, ISurvivalConstructable {

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
                        .atLeast(Dynamo.or(DynamoMulti), InputBus, InputHatch, OutputBus)
                        .dot(1)
                        .casingIndex(textureOffset + 16 + 6)
                        .buildAndChain(sBlockCasingsBA0, 6))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!structureCheck_EM(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset)) return false;
        setLightningPosition(getBaseMetaTileEntity().getFrontFacing());
        return true;
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
    // endregion end

    // region Process
    public static final int CRYOTHEUM_CONSUMPTION = 128;
    protected static Fluid MOLTEN_IRON;
    protected static Fluid CRYOTHEUM;
    private static final int MAXRODS = 512;
    List<ItemStack> mStored = new ArrayList<>();
    private long tStored;
    private long tProduct;
    private long tMaxStored;
    private int OperatingMode = 0;
    protected boolean enable_lightning = true;
    private int tRods;
    private int aX;
    private int aY;
    private int aZ;

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
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
    @NotNull
    public CheckRecipeResult checkProcessing_EM() {

        if (OperatingMode == 0 && tRods > 0) {
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

                // add stored eu
                tStored = Math.min(tStored + tProduct, tMaxStored);

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

        } else if (OperatingMode > 0) {

            if (OperatingMode == 1 && tRods < MAXRODS) {
                TST_ItemID LightningRod = TST_ItemID.createNoNBT(Machine_HV_LightningRod.get(1));
                int canAdd = MAXRODS - tRods;
                List<ItemStack> tInput = getStoredInputs();
                if (tInput.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;
                for (ItemStack machine : tInput) {
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

                tProduct = tRods * 28000000L;
                tMaxStored = tRods * 280000000L;
                this.mMaxProgresstime = 20;
                updateSlots();
                return CheckRecipeResultRegistry.SUCCESSFUL;
            } else if (OperatingMode == 2 && tRods > 0 && tStored == 0) {
                this.mOutputItems = mStored.toArray(new ItemStack[0]);
                mStored.clear();
                this.updateSlots();
                tRods = 0;
                tProduct = 0;
                tMaxStored = 0;
                this.mMaxProgresstime = 20;
                updateSlots();
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }

        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        if (tStored > 0) {
            // push eu to dynamo
            for (MTEHatchDynamo eDynamo : super.mDynamoHatches) {
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

            for (MTEHatchDynamoMulti eDynamo : eDynamoMulti) {
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
        }

        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("enable_lightning", enable_lightning);
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
        enable_lightning = aNBT.getBoolean("enable_lightning");
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
                new TTRenderedExtendedFacingTexture(active ? TTMultiblockBase.ScreenON : TTMultiblockBase.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][16 + 6] };
    }

    // spotless:off
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr GTCM_LightningSpire_MachineType
        // # Multi Lightning Rod
        // #zh_CN 多方块避雷针
        tt.addMachineType(tr("GTCM_LightningSpire_MachineType"))
            // #tr GTCM_LightningSpire_01
            // # {\BLUE}"Thunder is God's cannon."
            // #zh_CN {\BLUE}“雷霆是上帝的大炮”
            .addInfo(tr("GTCM_LightningSpire_01"))
            // #tr GTCM_LightningSpire_02
            // # {\DARK_BLUE}"But now we will control the thunder."
            // #zh_CN {\DARK_BLUE}“但现在我们将掌控雷霆”
            .addInfo(tr("GTCM_LightningSpire_02"))
            .addSeparator()
            // #tr GTCM_LightningSpire_03
            // # {\AQUA}Maximum storage capacity of 512 lightning rods(I).
            // #zh_CN {\AQUA}最大存储512个避雷针（I）
            .addInfo(tr("GTCM_LightningSpire_03"))
            // #tr GTCM_LightningSpire_04
            // # {\AQUA}Each lightning rod produces 28 MEU per lightning strike and stores 280 MEU.
            // #zh_CN {\AQUA}每个避雷针每次雷击生产28MEU，并且存储280MEU
            .addInfo(tr("GTCM_LightningSpire_04"))
            // #tr GTCM_LightningSpire_05
            // # {\AQUA}Ignoring thunderstorm weather for power generation.
            // #zh_CN {\AQUA}无视雷雨天气发电
            .addInfo(tr("GTCM_LightningSpire_05"))
            // #tr GTCM_LightningSpire_06
            // # {\AQUA}Consume 128mb cruotheum and 72mb*the number of lightning rods of molten iron ever lightning
            // #zh_CN {\AQUA}每次雷击均会消耗128mb的极寒之凛冰以及72mb*避雷针数量的熔融铁
            .addInfo(tr("GTCM_LightningSpire_06"))
            // #tr GTCM_LightningSpire_07
            // # {\AQUA}Quantitative input is required, too much or too little can lead to power generation failure
            // #zh_CN {\AQUA}需要定量输入,过多过少均会导致发电失败
            .addInfo(tr("GTCM_LightningSpire_07"))
            .addSeparator()
            // #tr GTCM_LightningSpire_08
            // # {\UNDERLINE}Use a screwdriver to switch input, output, and power generation modes.
            // #zh_CN {\UNDERLINE}使用螺丝刀切换输入，输出，发电模式
            .addInfo(tr("GTCM_LightningSpire_08"))
            // #tr GTCM_LightningSpire_09
            // # {\UNDERLINE}Please clear the internal cache power before outputting the machine
            // #zh_CN {\UNDERLINE}输出机器前请先输出完内部电力缓存
            .addInfo(tr("GTCM_LightningSpire_09"))
            // #tr GTCM_LightningSpire_10
            // # {\UNDERLINE}Before dismantling the machine, please output the lightning rod first!
            // #zh_CN {\UNDERLINE}拆除机器前请先输出避雷针
            .addInfo(tr("GTCM_LightningSpire_10"))
            // #tr GTCM_LightningSpire_11
            // # {\UNDERLINE}Otherwise all internal lightning rods will be lost!
            // #zh_CN {\UNDERLINE}否则会丢失所有内部避雷针！
            .addInfo(tr("GTCM_LightningSpire_11"))
            // #tr GTCM_LightningSpire_12
            // # Use a wire cutter to enable/disable lightning animation.
            // #zh_CN 使用剪线钳开启/关闭闪电特效
            .addInfo(tr("GTCM_LightningSpire_12"))
            .addSeparator()
            .beginStructureBlock(11, 23, 11, false)
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aStack) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.OperatingMode = (this.OperatingMode + 1) % 3;
            GTUtility.sendChatToPlayer(
                aPlayer,
                // #tr LightningSpire.ModeMsg.0
                // # Lightning Spire is in Operate Mode
                // #zh_CN 闪电尖塔设置为发电模式
                // #tr LightningSpire.ModeMsg.1
                // # Lightning Spire is in Input Mode
                // #zh_CN 闪电尖塔设置为输入模式
                // #tr LightningSpire.ModeMsg.2
                // # Lightning Spire is in Output Mode
                // #zh_CN 闪电尖塔设置为输出模式
                StatCollector.translateToLocal(tr("LightningSpire.ModeMsg." + OperatingMode)));
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (getBaseMetaTileEntity().isServerSide()) {
            enable_lightning = !enable_lightning;
            // #tr LightningSpire.enable_lightning.true
            // # Enable lightning animation
            // #zh_CN 启用闪电特效
            // #tr LightningSpire.enable_lightning.false
            // # Disable lightning animation
            // #zh_CN 禁用闪电特效
            GTUtility.sendChatToPlayer(aPlayer, tr("LightningSpire.enable_lightning." + enable_lightning));
            return true;
        }
        return false;
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
                new TextWidget().setStringSupplier(() -> "EU Gen per strike:" + numberFormat.format(tProduct))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> tProduct, val -> tProduct = val));
    }

    //
}
