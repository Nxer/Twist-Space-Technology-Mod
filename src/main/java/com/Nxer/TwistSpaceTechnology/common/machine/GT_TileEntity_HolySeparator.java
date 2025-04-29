package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.ParallelPerPiece_HolySeparator;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Piece_EnablePerfectOverclock_HolySeparator;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MultiplyPerTier_HolySeparator;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.TstProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;
import gtPlusPlus.core.block.ModBlocks;

public class GT_TileEntity_HolySeparator extends GTCM_MultiMachineBase<GT_TileEntity_HolySeparator> {

    // region Class Constructor
    public GT_TileEntity_HolySeparator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_HolySeparator(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic
    private int piece = 1;

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Cutting Machine
         * 1 - Slicer
         * 2 - Lathe
         */
        return 3;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_CUTTING);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SLICING);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SINGULARITY);
    }

    @Override
    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal("HolySeparator.modeMsg." + mode);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new TstProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    piece >= Piece_EnablePerfectOverclock_HolySeparator ? OverclockType.PerfectOverclock
                        : OverclockType.NormalOverclock);
                return super.process();
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return piece >= Piece_EnablePerfectOverclock_HolySeparator;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (machineMode) {
            case 1:
                return RecipeMaps.slicerRecipes;
            case 2:
                return RecipeMaps.latheRecipes;
            default:
                return RecipeMaps.cutterRecipes;
        }
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.slicerRecipes, RecipeMaps.latheRecipes, RecipeMaps.cutterRecipes);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.piece = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) {
            return false;
        }
        while (checkPiece(
            STRUCTURE_PIECE_MIDDLE,
            horizontalOffSet,
            verticalOffSet + (this.piece + 1) * 4,
            depthOffSet)) {
            this.piece++;
        }
        if (piece < 1) {
            return false;
        }
        if (!checkPiece(STRUCTURE_PIECE_END, horizontalOffSet, verticalOffSet + (this.piece + 1) * 4, depthOffSet)) {
            return false;
        }

        speedBonus = (float) (Math.pow(SpeedBonus_MultiplyPerTier_HolySeparator, getTotalPowerTier()));

        maxParallel = ParallelPerPiece_HolySeparator * piece;

        return true;
    }

    // endregion

    // region Structure
    // spotless:off
	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
		int piece = stackSize.stackSize;
		for (int i=1; i<=piece; i++){
			this.buildPiece(STRUCTURE_PIECE_MIDDLE, stackSize, hintsOnly, horizontalOffSet, verticalOffSet + i*4, depthOffSet);
		}
		this.buildPiece(STRUCTURE_PIECE_END, stackSize, hintsOnly, horizontalOffSet, verticalOffSet + piece*4 +4, depthOffSet);
	}

	@Override
	public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env){
		if (this.mMachine) return -1;
		int[] built = new int[stackSize.stackSize + 2];

		built[0] = survivialBuildPiece(
			STRUCTURE_PIECE_MAIN,
			stackSize,
			horizontalOffSet,
			verticalOffSet,
			depthOffSet,
			elementBudget,
            env,
			false,
			true);

		int piece = stackSize.stackSize;
		for (int i=1; i<=piece; i++){
			built[i] = survivialBuildPiece(
				STRUCTURE_PIECE_MIDDLE,
				stackSize,
				horizontalOffSet,
				verticalOffSet + i*4,
				depthOffSet,
				elementBudget,
                env,
				false,
				true);
		}

		built[stackSize.stackSize + 1] += survivialBuildPiece(
			STRUCTURE_PIECE_END,
			stackSize,
			horizontalOffSet,
			verticalOffSet + piece*4 +4,
			depthOffSet,
			elementBudget,
            env,
			false,
			true);

		return TstUtils.multiBuildPiece(built);
	}

	private static final String STRUCTURE_PIECE_MAIN = "mainHolySeparator";
	private static final String STRUCTURE_PIECE_MIDDLE = "middleHolySeparator";
	private static final String STRUCTURE_PIECE_END = "endHolySeparator";
	private final int horizontalOffSet = 7;
	private final int verticalOffSet = 2;
	private final int depthOffSet = 0;
    private static IStructureDefinition<GT_TileEntity_HolySeparator> STRUCTURE_DEFINITION = null;

	@Override
	public IStructureDefinition<GT_TileEntity_HolySeparator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_HolySeparator>builder()
                                                      .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                                                      .addShape(STRUCTURE_PIECE_MIDDLE, transpose(shapeMiddle))
                                                      .addShape(STRUCTURE_PIECE_END, transpose(shapeEnd))
                                                      .addElement('A',
                                                                  HatchElementBuilder.<GT_TileEntity_HolySeparator>builder()
                                                                                        .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                                                                                        .adder(GT_TileEntity_HolySeparator::addToMachineList)
                                                                                        .dot(1)
                                                                                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                                                                                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                                                      .addElement('B',ofBlock(GregTechAPI.sBlockCasings8, 10))
                                                      .addElement('C',ofBlock(sBlockCasingsTT, 0))
                                                      .addElement('D',ofBlock(sBlockCasingsTT, 4))
                                                      .addElement('E',ofBlock(sBlockCasingsTT, 6))
                                                      .addElement('F',ofBlock(sBlockCasingsTT, 8))
                                                      .addElement('G',ofBlock(ModBlocks.blockCasings3Misc, 15))
                                                      .build();
        }
		return STRUCTURE_DEFINITION;
	}

	/*
	Blocks:
A -> ofBlock...(gt.blockcasings8, 7, ...); // Hatches
B -> ofBlock...(gt.blockcasings8, 10, ...);
C -> ofBlock...(gt.blockcasingsTT, 0, ...);
D -> ofBlock...(gt.blockcasingsTT, 4, ...);
E -> ofBlock...(gt.blockcasingsTT, 6, ...);
F -> ofBlock...(gt.blockcasingsTT, 8, ...);
G -> ofBlock...(gtplusplus.blockcasings.3, 15, ...);
	 */

	private final String[][] shapeMain = new String[][]{
		{"               ","               ","               ","               ","               ","               ","       G       ","      GFG      ","       G       ","               ","               ","               ","               ","               ","               "},
		{"      AAA      ","      AAA      ","    AABBBAA    ","   ABBBBBBBA   ","  ABBBBBBBBBA  ","  ABBBBBBBBBA  ","AABBBBDDDBBBBAA","AABBBBDDDBBBBAA","AABBBBDDDBBBBAA","  ABBBBBBBBBA  ","  ABBBBBBBBBA  ","   ABBBBBBBA   ","    AABBBAA    ","      AAA      ","      AAA      "},
		{"      A~A      ","      AAA      ","    AABBBAA    ","   ABBBBBBBA   ","  ABBBBBBBBBA  ","  ABBBBBBBBBA  ","AABBBBBBBBBBBAA","AABBBBBBBBBBBAA","AABBBBBBBBBBBAA","  ABBBBBBBBBA  ","  ABBBBBBBBBA  ","   ABBBBBBBA   ","    AABBBAA    ","      AAA      ","      AAA      "},
		{"      AAA      ","    AAAAAAA    ","   AAABBBAAA   ","  AABBBBBBBAA  "," AABBBBBBBBBAA "," AABBBBBBBBBAA ","AABBBBBBBBBBBAA","AABBBBBBBBBBBAA","AABBBBBBBBBBBAA"," AABBBBBBBBBAA "," AABBBBBBBBBAA ","  AABBBBBBBAA  ","   AAABBBAAA   ","    AAAAAAA    ","      AAA      "}
	};
	private final String[][] shapeMiddle = new String[][]{
		{"       E       ","     AAAAA     ","               ","               ","               "," A           A "," A     G     A ","EA    GFG    AE"," A     G     A "," A           A ","               ","               ","               ","     AAAAA     ","       E       "},
		{"      ECE      ","    AAACAAA    ","       C       ","       C       "," A           A "," A           A ","EA     G     AE","CCCC  GFG  CCCC","EA     G     AE"," A           A "," A           A ","       C       ","       C       ","    AAACAAA    ","      ECE      "},
		{"       E       ","     AAAAA     ","               ","               ","               "," A           A "," A     G     A ","EA    GFG    AE"," A     G     A "," A           A ","               ","               ","               ","     AAAAA     ","       E       "},
		{"               ","               ","               ","               ","               ","               ","       G       ","      GFG      ","       G       ","               ","               ","               ","               ","               ","               "}
	};
	private final String[][] shapeEnd = new String[][]{
		{"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
		{"               ","      CCC      ","    CCDDDCC    ","   CDDDDDDDC   ","  CDDDDDDDDDC  ","  CDDDDDDDDDC  "," CDDDDDEDDDDDC "," CDDDDEEEDDDDC "," CDDDDDEDDDDDC ","  CDDDDDDDDDC  ","  CDDDDDDDDDC  ","   CDDDDDDDC   ","    CCDDDCC    ","      CCC      ","               "},
		{"               ","               ","               ","               ","               ","               ","       G       ","      GFG      ","       G       ","               ","               ","               ","               ","               ","               "},
		{"               ","               ","               ","               ","               ","               ","       G       ","      GFG      ","       G       ","               ","               ","               ","               ","               ","               "}

	};

	@Override
	public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		return super.addToMachineList(aTileEntity, aBaseCasingIndex)
			       || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
	}

	// spotless:on
    // endregion

    // region Overrides

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "Parallels: "
            + EnumChatFormatting.GOLD
            + this.getMaxParallelRecipes();
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "Speed multiplier: "
            + EnumChatFormatting.GOLD
            + this.getSpeedBonus();
        ret[origin.length + 2] = EnumChatFormatting.AQUA + "Pieces: " + EnumChatFormatting.GOLD + this.piece;
        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("piece", piece);
        aNBT.setByte("mode", (byte) machineMode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        piece = aNBT.getInteger("piece");
        machineMode = aNBT.getByte("mode");
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_HolySeparator_MachineType)
            .addInfo(TextLocalization.Tooltip_HolySeparator_00)
            .addInfo(TextLocalization.Tooltip_HolySeparator_01)
            .addInfo(TextLocalization.Tooltip_HolySeparator_02)
            .addInfo(TextLocalization.Tooltip_HolySeparator_03)
            .addInfo(TextLocalization.Tooltip_HolySeparator_04)
            .addInfo(TextLocalization.Tooltip_HolySeparator_05)
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_HolySeparator(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183) };
    }

    // endregion
}
