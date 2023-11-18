package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUPerCriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.util.Utils.metaItemEqual;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataCell;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.IDSP_IO;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_IntegratedCircuit_Item;

public class TST_DSPReceiver extends GTCM_MultiMachineBase<TST_DSPReceiver>
    implements IConstructable, ISurvivalConstructable, IDSP_IO, IGlobalWirelessEnergy {

    // region Class Constructor
    public TST_DSPReceiver(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_DSPReceiver(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_DSPReceiver(this.mName);
    }

    // endregion

    // region Processing Logic

    private String ownerName;
    private UUID ownerUUID;
    private byte mode = 0;
    private long usedPowerPoint = 0;
    private boolean isUsing = false;
    private long storageEU = 0;
    private int dimID;
    private DSP_DataCell dspDataCell;
    private IGregTechTileEntity baseMetaTileEntity;

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length - 2] = EnumChatFormatting.GOLD + "Generating EU/t: "
            + EnumChatFormatting.RESET
            + generateTickEU();
        ret[origin.length - 1] = EnumChatFormatting.GOLD + "Used Power Point: "
            + EnumChatFormatting.RESET
            + usedPowerPoint;
        ret[origin.length] = EnumChatFormatting.GOLD + "DSP Data Cell: " + EnumChatFormatting.RESET + dspDataCell;
        return ret;
        /*
         * generateTickEU
         * usedPowerPoint
         */
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 2);
            GT_Utility
                .sendChatToPlayer(aPlayer, StatCollector.translateToLocal("TST_DSPReceiver.modeMsg." + this.mode));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", mode);
        aNBT.setLong("usedPowerPoint", usedPowerPoint);
        aNBT.setLong("storageEU", storageEU);
        aNBT.setBoolean("isUsing", isUsing);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mode = aNBT.getByte("mode");
        usedPowerPoint = aNBT.getLong("usedPowerPoint");
        storageEU = aNBT.getLong("storageEU");
        isUsing = aNBT.getBoolean("isUsing");
    }

    /**
     * Get how many DSP power points this machine use.
     * Limited by putting integrated circuit in controller block slot.
     * 
     * @return The amount.
     */
    private long getPowerPoint() {
        if (dspDataCell == null) return 0;
        // Limit dsp max * circuit.meta / circuit.stackSize
        ItemStack controllerStack = getControllerSlot();
        long maxPowerPointLimit = dspDataCell.getMaxDSPPowerPoint();
        long canUse = dspDataCell.getDSPPowerPointCanUse();
        if (controllerStack != null && controllerStack.getItem() instanceof GT_IntegratedCircuit_Item) {
            double multiplier = Math
                .min(1, ((double) controllerStack.getItemDamage()) / ((double) controllerStack.stackSize));
            long limited = (long) (multiplier * maxPowerPointLimit);
            return Math.min(limited, canUse);
        } else if (controllerStack == null) {
            return canUse;
        } else {
            return 0;
        }
    }

    private void startUsingDSP() {
        isUsing = true;
        usedPowerPoint = getPowerPoint();
        if (!dspDataCell.tryUsePowerPoint(usedPowerPoint)) {
            usedPowerPoint = 0;
            this.stopMachine();
            TwistSpaceTechnology.LOG.info("Error ! DSPReceiver try use DSP Power Point FAILED at " + this);
            TwistSpaceTechnology.LOG.info("Check your Dyson Sphere Program Information!");
        }
    }

    private void stopUsingDSP() {
        isUsing = false;
        if (!dspDataCell.tryDecreaseUsedPowerPoint(usedPowerPoint)) {
            dspDataCell.setUsedPowerPointUnsafely(0);
        }
        usedPowerPoint = 0;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        mMaxProgresstime = 128;
        return CheckRecipeResultRegistry.GENERATING;
    }

    private long generateTickEU() {
        return (long) (dspDataCell.getGalaxy().stellarCoefficient
            * DSP_Planet.getPlanetaryCoefficientWithDimID(this.dimID)
            * this.usedPowerPoint);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        this.storageEU += this.generateTickEU();
        return true;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isServerSide()) {
            this.baseMetaTileEntity = aBaseMetaTileEntity;
            this.dimID = getDimID(aBaseMetaTileEntity);
            this.ownerName = getOwnerNameAndInitMachine(aBaseMetaTileEntity);
            this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
            this.dspDataCell = getOrInitDSPData(ownerName, dimID);
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

            // Synchronize the DSP source when run machine
            if (!isUsing && mProgresstime == 1) {
                startUsingDSP();
            }

            // Release resource when stop machine
            if (isUsing && mMaxProgresstime == 0) {
                stopUsingDSP();
            }

            if (mode == 0) {
                // Generate EU directly
                if (this.storageEU > 0 && aTick % 128 == 0) {
                    TwistSpaceTechnology.LOG
                        .info("test addEUToGlobalEnergyMap: ownerUUID: " + ownerUUID + " ; storageEU: " + storageEU);
                    addEUToGlobalEnergyMap(ownerUUID.toString(), this.storageEU);
                    this.storageEU = 0;
                }
            } else if (mode == 1) {
                // Generate Photon per int.MAX EU
                if (storageEU >= EUPerCriticalPhoton) {
                    int amount = (int) (storageEU / EUPerCriticalPhoton);
                    if (this.mOutputItems == null) {
                        this.mOutputItems = new ItemStack[] { CriticalPhoton.get(amount) };
                    } else {
                        // safe and more calculate
                        for (ItemStack items : this.mOutputItems) {
                            if (metaItemEqual(items, CriticalPhoton.get(1))) {
                                items.stackSize += amount;
                            }
                        }
                        // unsafe, low calculate and enough
                        // this.mOutputItems[0].stackSize += amount;
                    }
                    storageEU -= EUPerCriticalPhoton * amount;
                }
            }
        }
    }

    // endregion

    // region Structure
    // spotless:off
	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
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
	
	private static final String STRUCTURE_PIECE_MAIN = "mainDSPReceiver";
	private final int horizontalOffSet = 1;
	private final int verticalOffSet = 1;
	private final int depthOffSet = 0;
	@Override
	public IStructureDefinition<TST_DSPReceiver> getStructureDefinition() {
		return IStructureDefinition.<TST_DSPReceiver>builder()
		                           .addShape(STRUCTURE_PIECE_MAIN, shapeMain)
		                           .addElement(
			                           'A',
			                           GT_HatchElementBuilder.<TST_DSPReceiver>builder()
			                                                 .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy), Maintenance)
			                                                 .adder(TST_DSPReceiver::addToMachineList)
			                                                 .casingIndex(176)
			                                                 .dot(1)
			                                                 .buildAndChain(GregTech_API.sBlockCasings8, 0))
		                           .build();
	}
	private final String[][] shapeMain = new String[][]{{
		"AAA",
		"A~A",
		"AAA"
	},{
		"AAA",
		"AAA",
		"AAA"
	},{
		"AAA",
		"AAA",
		"AAA"
	}};
	// spotless:on
    // endregion

    // region Overrides

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Test")
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addSeparator()
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {

            if (aActive) {
                return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_ON)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FUSION1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }

        return new ITexture[] { casingTexturePages[0][12] };
    }

    // endregion
}
