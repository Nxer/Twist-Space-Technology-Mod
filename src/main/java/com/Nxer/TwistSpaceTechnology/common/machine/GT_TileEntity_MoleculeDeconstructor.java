package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.client.texture.TstMachineTextures.OVERLAY_FRONT_PROCESSING_ARRAY;
import static com.Nxer.TwistSpaceTechnology.client.texture.TstMachineTextures.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static com.Nxer.TwistSpaceTechnology.client.texture.TstMachineTextures.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static com.Nxer.TwistSpaceTechnology.client.texture.TstMachineTextures.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_PerPiece_MoleculeDeconstructor;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MultiplyPerTier_MoleculeDeconstructor;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_MoleculeDeconstructor_00;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_MoleculeDeconstructor_01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_MoleculeDeconstructor_02;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_MoleculeDeconstructor_03;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_MoleculeDeconstructor_04;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_MoleculeDeconstructor_05;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_MoleculeDeconstructor_MachineType;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textScrewdriverChangeMode;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.ofFrame;
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
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class GT_TileEntity_MoleculeDeconstructor extends GTCM_MultiMachineBase<GT_TileEntity_MoleculeDeconstructor>
    implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GT_TileEntity_MoleculeDeconstructor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MoleculeDeconstructor(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic
    private byte glassTier = 0;
    private int piece = 1;

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Electrolyzer
         * 1 - Centrifuge
         */
        return 2;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_CHEMBATH);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR);
    }

    @Override
    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal("MoleculeDeconstructor.modeMsg." + mode);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return piece >= PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor;
    }

    public int getMaxParallelRecipes() {
        return Parallel_PerPiece_MoleculeDeconstructor * this.piece;
    }

    protected float getSpeedBonus() {
        return speedBonus;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (machineMode) {
            case 1:
                return GTPPRecipeMaps.centrifugeNonCellRecipes;
            default:
                return GTPPRecipeMaps.electrolyzerNonCellRecipes;
        }
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTPPRecipeMaps.centrifugeNonCellRecipes, GTPPRecipeMaps.electrolyzerNonCellRecipes);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.glassTier = 0;
        this.piece = 1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) {
            return false;
        }

        while (checkPiece(STRUCTURE_PIECE_MIDDLE, horizontalOffSet, verticalOffSet, depthOffSet - piece * 4)) {
            this.piece++;
        }

        if (!checkPiece(STRUCTURE_PIECE_END, horizontalOffSet, verticalOffSet, depthOffSet - piece * 4)) {
            return false;
        }

        if (this.glassTier <= 0) return false;
        if (glassTier < 12) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
            }
        }

        speedBonus = (float) (Math.pow(SpeedBonus_MultiplyPerTier_MoleculeDeconstructor, getTotalPowerTier()));

        return true;
    }
    // endregion

    // region Structure
    // spotless:off

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
		int piece = stackSize.stackSize;
		for (int i=1; i<piece; i++){
			this.buildPiece(STRUCTURE_PIECE_MIDDLE, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet - i*4);
		}
		this.buildPiece(STRUCTURE_PIECE_END, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet - piece*4);
	}

	@Override
	public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
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
		if (piece > 1) {
			for (int i = 1; i < piece; i++) {
				built[i] = survivialBuildPiece(
					STRUCTURE_PIECE_MIDDLE,
					stackSize,
					horizontalOffSet,
					verticalOffSet,
					depthOffSet - i * 4,
					elementBudget,
                    env,
					false,
					true);
			}
		}

		built[piece + 1] += survivialBuildPiece(
			STRUCTURE_PIECE_END,
			stackSize,
			horizontalOffSet,
			verticalOffSet,
			depthOffSet - piece*4,
			elementBudget,
            env,
			false,
			true);

		return TstUtils.multiBuildPiece(built);
	}

	private static final String STRUCTURE_PIECE_MAIN = "mainMoleculeDeconstructor";
	private static final String STRUCTURE_PIECE_MIDDLE = "middleMoleculeDeconstructor";
	private static final String STRUCTURE_PIECE_END = "endMoleculeDeconstructor";
	private final int horizontalOffSet = 7;
	private final int verticalOffSet = 9;
	private final int depthOffSet = 0;
    private static IStructureDefinition<GT_TileEntity_MoleculeDeconstructor> STRUCTURE_DEFINITION = null;
	@Override
	public IStructureDefinition<GT_TileEntity_MoleculeDeconstructor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MoleculeDeconstructor>builder()
                                                      .addShape(STRUCTURE_PIECE_MAIN, shapeMain)
                                                      .addShape(STRUCTURE_PIECE_MIDDLE, shapeMiddle)
                                                      .addShape(STRUCTURE_PIECE_END, shapeEnd)
                                                      .addElement('A',
                                                                  withChannel("glass",
                                                                              BorosilicateGlass.ofBoroGlass(
                                                                                  (byte) 0,
                                                                                  (byte) 1,
                                                                                  Byte.MAX_VALUE,
                                                                                  (te, t) -> te.glassTier = t,
                                                                                  te -> te.glassTier
                                                                              )))
                                                      .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 15))
                                                      .addElement('C', ofBlock(GregTechAPI.sBlockCasings4, 14))
                                                      .addElement('D',
                                                                  HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
                                                                                        .atLeast(Energy.or(ExoticEnergy))
                                                                                        .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
                                                                                        .dot(1)
                                                                                        .casingIndex(1024)
                                                                                        .buildAndChain(sBlockCasingsTT, 0))
                                                      .addElement('E', ofBlock(sBlockCasingsTT, 8))
                                                      .addElement('F',
                                                                  HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
                                                                                        .atLeast(OutputBus, OutputHatch)
                                                                                        .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
                                                                                        .dot(2)
                                                                                        .casingIndex(62)
                                                                                        .buildAndChain(GregTechAPI.sBlockCasings4, 14))
                                                      .addElement('G',
                                                                  HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
                                                                                        .atLeast(Maintenance)
                                                                                        .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
                                                                                        .dot(3)
                                                                                        .casingIndex(62)
                                                                                        .buildAndChain(GregTechAPI.sBlockCasings4, 14))
                                                      .addElement('H',
                                                                  HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
                                                                                        .atLeast(InputBus, InputHatch)
                                                                                        .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
                                                                                        .dot(4)
                                                                                        .casingIndex(62)
                                                                                        .buildAndChain(GregTechAPI.sBlockCasings4, 14))
                                                      .addElement('I', ofFrame(Materials.CosmicNeutronium))
                                                      .build();
        }
		return STRUCTURE_DEFINITION;
	}


	/*
	Blocks:
A -> ofBlock...(BW_GlasBlocks, 14, ...); // glass
B -> ofBlock...(gt.blockcasings2, 15, ...);
C -> ofBlock...(gt.blockcasings4, 14, ...);
D -> ofBlock...(gt.blockcasingsTT, 0, ...); // energy
E -> ofBlock...(gt.blockcasingsTT, 8, ...);
F -> ofBlock...(gt.blockcasings4, 14, ...); // output
G -> ofBlock...(gt.blockcasings4, 14, ...); // maintenance
H -> ofBlock...(gt.blockcasings4, 14, ...); // input
I -> ofFrame...();
	 */
	private final String[][] shapeMain = new String[][]{{
		"               ",
		"               ",
		"               ",
		"               ",
		"               ",
		"               ",
		"      HHH      ",
		"      HHH      ",
		"      HHH      ",
		"      G~G      "
	},{
		"IIIIIIIIIIIIIII",
		"I     FFF     I",
		"I     DDD     I",
		"I             I",
		"I             I",
		"I             I",
		"IFD   AAA   DFI",
		"IFD   A A   DFI",
		"IFD   CCC   DFI",
		"CCD         DCC"
	},{
		"               ",
		"      FFF      ",
		"      DED      ",
		"       B       ",
		"       B       ",
		"       B       ",
		" FD   AEA   DF ",
		" FEBBBE EBBBEF ",
		" FD   CCC   DF ",
		"CCD         DCC"
	},{
		"IIIIIIIIIIIIIII",
		"I     FFF     I",
		"I     DDD     I",
		"I             I",
		"I             I",
		"I             I",
		"IFD   AAA   DFI",
		"IFD   A A   DFI",
		"IFD   CCC   DFI",
		"CCD         DCC"
	}};

	private final String[][] shapeMiddle = new String[][]{{
		"               ",
		"               ",
		"               ",
		"               ",
		"               ",
		"               ",
		"      AAA      ",
		"      A A      ",
		"      CCC      ",
		"               "
	},{
		"IIIIIIIIIIIIIII",
		"I     FFF     I",
		"I     DDD     I",
		"I             I",
		"I             I",
		"I             I",
		"IFD   AAA   DFI",
		"IFD   A A   DFI",
		"IFD   CCC   DFI",
		"CCD         DCC"
	},{
		"               ",
		"      FFF      ",
		"      DED      ",
		"       B       ",
		"       B       ",
		"       B       ",
		" FD   AEA   DF ",
		" FEBBBE EBBBEF ",
		" FD   CCC   DF ",
		"CCD         DCC"
	},{
		"IIIIIIIIIIIIIII",
		"I     FFF     I",
		"I     DDD     I",
		"I             I",
		"I             I",
		"I             I",
		"IFD   AAA   DFI",
		"IFD   A A   DFI",
		"IFD   CCC   DFI",
		"CCD         DCC"
	}};

	private final String[][] shapeEnd = new String[][]{{
		"               ",
		"               ",
		"               ",
		"               ",
		"               ",
		"               ",
		"      FFF      ",
		"      FFF      ",
		"      FFF      ",
		"      GGG      "
	}};
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
        tt.addMachineType(Tooltip_MoleculeDeconstructor_MachineType)
            .addInfo(Tooltip_MoleculeDeconstructor_00)
            .addInfo(Tooltip_MoleculeDeconstructor_01)
            .addInfo(Tooltip_MoleculeDeconstructor_02)
            .addInfo(Tooltip_MoleculeDeconstructor_03)
            .addInfo(Tooltip_MoleculeDeconstructor_04)
            .addInfo(Tooltip_MoleculeDeconstructor_05)
            .addInfo(textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addController(textFrontBottom)
            .addInputHatch(textUseBlueprint, 4)
            .addOutputHatch(textUseBlueprint, 2)
            .addInputBus(textUseBlueprint, 4)
            .addOutputBus(textUseBlueprint, 2)
            .addEnergyHatch(textUseBlueprint, 1)
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MoleculeDeconstructor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(62), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(62), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(62) };
    }

    // endregion
}
