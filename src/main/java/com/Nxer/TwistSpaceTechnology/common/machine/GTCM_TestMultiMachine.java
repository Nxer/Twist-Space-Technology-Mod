package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

// spotless:off
public class GTCM_TestMultiMachine
	extends GTCM_MultiMachineBase<GTCM_TestMultiMachine> {
	public GTCM_TestMultiMachine(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	public GTCM_TestMultiMachine(String aName) {
		super(aName);
	}
	// region Processing Logic
	
	@Override
	protected boolean isEnablePerfectOverclock(){
		return true;
	}
	@Override
	protected float getSpeedBonus() {
		return 1.0F/16;
	}
	@Override
	protected int getMaxParallelRecipes() {
		return Integer.MAX_VALUE;
	}
	
	// endregion
	
	protected int mode = 0;
	private static final String STRUCTURE_PIECE_MAIN = "main";
	private final String[][] shape = new String[][] {
		{"AAA","AAA","AAA"},
		{"A~A","AAA","AAA"},
		{"AAA","AAA","AAA"}
	};
	private final int horizontalOffSet = 1;
	private final int verticalOffSet = 1;
	private final int depthOffSet = 0;
	@Override
	public IStructureDefinition<GTCM_TestMultiMachine> getStructureDefinition() {
		return StructureDefinition
			       .<GTCM_TestMultiMachine>builder()
			       .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
			       .addElement(
					   'A',
					   GT_HatchElementBuilder.<GTCM_TestMultiMachine>builder()
					                         .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy), Maintenance)
					                         .adder(GTCM_TestMultiMachine::addToMachineList)
					                         .casingIndex(176)
					                         .dot(1)
					                         .buildAndChain(GregTech_API.sBlockCasings8, 0))
			       .build();
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
	
	@Override
	public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (getBaseMetaTileEntity().isServerSide()) {
			this.mode = (this.mode + 1) % 2;
			GT_Utility.sendChatToPlayer(
				aPlayer,
				StatCollector.translateToLocal("IntensifyChemicalDistorter.mode." + this.mode));
		}
	}
	
	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}
	
	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		// this.casingAmountActual = 0; // re-init counter
		return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
	}
	
	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}
	
	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}
	
	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}
	
	@Override
	public boolean supportsVoidProtection() {
		return true;
	}
	
	@Override
	public boolean supportsInputSeparation() {
		return true;
	}
	
	@Override
	public boolean supportsBatchMode() {
		return true;
	}
	
	@Override
	public boolean supportsSingleRecipeLocking() {
		return true;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		
		aNBT.setInteger("mode", mode);
	}
	
	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		
		mode = aNBT.getInteger("mode");
	}
	
	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GTCM_TestMultiMachine(this.mName);
	}
	
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
		  .addInfo(TextLocalization.StructureTooComplex)
		  .addInfo(TextLocalization.BLUE_PRINT_INFO)
		  .addSeparator()
		  .beginStructureBlock(11, 13, 11, false)
		  .addController(TextLocalization.textFrontBottom)
		  .addCasingInfoRange(TextLocalization.textCasing, 8, 26, false)
		  .addInputHatch(TextLocalization.textAnyCasing, 1)
		  .addOutputHatch(TextLocalization.textAnyCasing, 1)
		  .addInputBus(TextLocalization.textAnyCasing, 2)
		  .addOutputBus(TextLocalization.textAnyCasing, 2)
		  .addMaintenanceHatch(TextLocalization.textAnyCasing, 2)
		  .addEnergyHatch(TextLocalization.textAnyCasing, 3)
		  .toolTipFinisher(TextLocalization.ModName);
		return tt;
	}
	
}

// spotless:on
