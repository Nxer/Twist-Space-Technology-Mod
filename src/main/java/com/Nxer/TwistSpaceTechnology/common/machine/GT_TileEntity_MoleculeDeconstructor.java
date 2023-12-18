package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Mode_Default_MoleculeDeconstructor;
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
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
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
    private byte mode = Mode_Default_MoleculeDeconstructor;

    @Override
    protected boolean isEnablePerfectOverclock() {
        return piece >= PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor;
    }

    protected int getMaxParallelRecipes() {
        return Parallel_PerPiece_MoleculeDeconstructor * this.piece;
    }

    protected float getSpeedBonus() {
        return (float) (Math
            .pow(SpeedBonus_MultiplyPerTier_MoleculeDeconstructor, GT_Utility.getTier(this.getMaxInputEu())));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (mode) {
            case 1:
                return GTPPRecipeMaps.centrifugeNonCellRecipes;
            default:
                return GTPPRecipeMaps.electrolyzerNonCellRecipes;
        }
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 2);
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("MoleculeDeconstructor.modeMsg." + this.mode));
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
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

        if (glassTier < 12) {
            for (GT_MetaTileEntity_Hatch hatch : this.mExoticEnergyHatches) {
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
            }
        }

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
	public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
		if (this.mMachine) return -1;
		int built = 0;

		built += survivialBuildPiece(
			STRUCTURE_PIECE_MAIN,
			stackSize,
			horizontalOffSet,
			verticalOffSet,
			depthOffSet,
			elementBudget,
			source,
			actor,
			false,
			true);

		int piece = stackSize.stackSize;
		if (piece>1) {
			for (int i = 1; i < piece; i++) {
				built += survivialBuildPiece(
					STRUCTURE_PIECE_MIDDLE,
					stackSize,
					horizontalOffSet,
					verticalOffSet,
					depthOffSet - i * 4,
					elementBudget,
					source,
					actor,
					false,
					true);
			}
		}

		built += survivialBuildPiece(
			STRUCTURE_PIECE_END,
			stackSize,
			horizontalOffSet,
			verticalOffSet,
			depthOffSet - piece*4,
			elementBudget,
			source,
			actor,
			false,
			true);

		return built;
	}
	private static final String STRUCTURE_PIECE_MAIN = "mainMoleculeDeconstructor";
	private static final String STRUCTURE_PIECE_MIDDLE = "middleMoleculeDeconstructor";
	private static final String STRUCTURE_PIECE_END = "endMoleculeDeconstructor";
	private final int horizontalOffSet = 7;
	private final int verticalOffSet = 9;
	private final int depthOffSet = 0;
	@Override
	public IStructureDefinition<GT_TileEntity_MoleculeDeconstructor> getStructureDefinition() {
		return StructureDefinition.<GT_TileEntity_MoleculeDeconstructor>builder()
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
			       .addElement('B', ofBlock(GregTech_API.sBlockCasings2, 15))
			       .addElement('C', ofBlock(GregTech_API.sBlockCasings4, 14))
			       .addElement('D',
			                   GT_HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
			                                         .atLeast(Energy.or(ExoticEnergy))
									                 .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
									                 .dot(1)
									                 .casingIndex(1024)
									                 .buildAndChain(sBlockCasingsTT, 0))
			       .addElement('E', ofBlock(sBlockCasingsTT, 8))
			       .addElement('F',
			                   GT_HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
			                                         .atLeast(OutputBus, OutputHatch)
			                                         .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
			                                         .dot(2)
			                                         .casingIndex(62)
			                                         .buildAndChain(GregTech_API.sBlockCasings4, 14))
			       .addElement('G',
			                   GT_HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
			                                         .atLeast(Maintenance)
			                                         .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
			                                         .dot(3)
			                                         .casingIndex(62)
			                                         .buildAndChain(GregTech_API.sBlockCasings4, 14))
			       .addElement('H',
			                   GT_HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
			                                         .atLeast(InputBus, InputHatch)
			                                         .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
			                                         .dot(4)
			                                         .casingIndex(62)
			                                         .buildAndChain(GregTech_API.sBlockCasings4, 14))
			       .addElement('I', ofFrame(Materials.CosmicNeutronium))
			       .build();
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
        ret[origin.length - 2] = EnumChatFormatting.AQUA + "Parallels: "
            + EnumChatFormatting.GOLD
            + this.getMaxParallelRecipes();
        ret[origin.length - 1] = EnumChatFormatting.AQUA + "Speed multiplier: "
            + EnumChatFormatting.GOLD
            + this.getSpeedBonus();
        ret[origin.length] = EnumChatFormatting.AQUA + "Pieces: " + EnumChatFormatting.GOLD + this.piece;
        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("piece", piece);
        aNBT.setByte("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        piece = aNBT.getInteger("piece");
        mode = aNBT.getByte("mode");
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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
            .addMaintenanceHatch(textUseBlueprint, 3)
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
