package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SmallLaunchVehicle;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SolarSail;
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

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_OverclockCalculator;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.machine.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataCell;
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
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;

public class TST_DSPLauncher extends GTCM_MultiMachineBase<TST_DSPLauncher>
    implements IConstructable, ISurvivalConstructable, IDSP_IO, IGlobalWirelessEnergy {

    // region Class Constructor
    public TST_DSPLauncher(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_DSPLauncher(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_DSPLauncher(this.mName);
    }

    // endregion

    // region Processing Logic
    private String ownerName;
    private UUID ownerUUID;
    private int dimID;
    private DSP_DataCell dspDataCell;
    private IGregTechTileEntity baseMetaTileEntity;

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length - 2] = EnumChatFormatting.GOLD + "Owner Name: " + EnumChatFormatting.RESET + ownerName;
        ret[origin.length - 1] = EnumChatFormatting.GOLD + "UUID: " + EnumChatFormatting.RESET + ownerUUID;
        ret[origin.length] = EnumChatFormatting.GOLD + "DSP Data Cell: " + EnumChatFormatting.RESET + dspDataCell;
        return ret;
    }

    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
                if (!addEUToGlobalEnergyMap(ownerUUID, -recipe.mEUt * recipe.mDuration)) {
                    return CheckRecipeResultRegistry.insufficientPower((long) recipe.mEUt * recipe.mDuration);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return GTCM_OverclockCalculator.ofNoOverclock(recipe);
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                // Power will be directly consumed through wireless
                setCalculatedEut(0);
                return result;
            }
        };
    }

    @Override
    @Nonnull
    public CheckRecipeResult checkProcessing() {
        // If no logic is found, try legacy checkRecipe
        if (processingLogic == null) {
            return checkRecipe(mInventory[1]) ? CheckRecipeResultRegistry.SUCCESSFUL
                : CheckRecipeResultRegistry.NO_RECIPE;
        }

        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);
        // inputs are consumed at this point
        updateSlots();
        if (!result.wasSuccessful()) return result;

        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = processingLogic.getDuration();
        setEnergyUsage(processingLogic);

        ItemStack[] outputItems = processingLogic.getOutputItems();
        for (ItemStack items : outputItems) {
            if (metaItemEqual(items, SolarSail.get(1))) {
                dspDataCell.addDSPSolarSail(items.stackSize);
            } else if (metaItemEqual(items, SmallLaunchVehicle.get(1))) {
                dspDataCell.addDSPNode(items.stackSize);
            }
        }

        // mOutputItems = processingLogic.getOutputItems();
        mOutputFluids = processingLogic.getOutputFluids();

        return result;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        // The voltage is only used for recipe finding
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(false);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTCMRecipe.instance.DSP_LauncherRecipes;
    }

    /**
     * Init information.
     * 
     * @param aBaseMetaTileEntity This machine tile entity.
     */
    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        if (aBaseMetaTileEntity.isServerSide() && (aTick == 1)) {
            this.baseMetaTileEntity = aBaseMetaTileEntity;
            this.dimID = getDimID(aBaseMetaTileEntity);
            this.ownerName = getOwnerNameAndInitMachine(aBaseMetaTileEntity);
            this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
            this.dspDataCell = getOrInitDSPData(ownerName, dimID);
        }

        super.onPreTick(aBaseMetaTileEntity, aTick);

    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }
    // endregion

    // region Structure
    // spotless:off
	
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
	private static final String STRUCTURE_PIECE_MAIN = "mainDSPLauncher";
	private final int horizontalOffSet = 1;
	private final int verticalOffSet = 1;
	private final int depthOffSet = 0;
	@Override
	public IStructureDefinition<TST_DSPLauncher> getStructureDefinition() {
		return IStructureDefinition.<TST_DSPLauncher>builder()
			       .addShape(STRUCTURE_PIECE_MAIN, shapeMain)
			                       .addElement(
				                       'A',
				                       GT_HatchElementBuilder.<TST_DSPLauncher>builder()
				                                             .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy), Maintenance)
				                                             .adder(TST_DSPLauncher::addToMachineList)
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
