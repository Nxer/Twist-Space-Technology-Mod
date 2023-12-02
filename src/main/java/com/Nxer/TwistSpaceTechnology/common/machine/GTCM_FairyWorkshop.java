package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.github.technus.tectech.thing.casing.TT_Container_Casings.StabilisationFieldGenerators;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_Utility.getBlockFromStack;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings1;
import gregtech.common.blocks.GT_Block_Casings8;
import gregtech.common.blocks.GT_Block_Stones;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.block.ModBlocks;;

public class GTCM_FairyWorkshop extends GTCM_MultiMachineBase<GTCM_FairyWorkshop> {

    // region Class Constructor
    public GTCM_FairyWorkshop(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_FairyWorkshop(String aName) {
        super(aName);
    }

    // endregion

    public byte casingTier = 0;
    // region Processing Logic
    protected float getSpeedBonus() {
        return 1.0F;
    };

    protected int getMaxParallelRecipes() {
        return 1;
    };

    protected boolean isEnablePerfectOverclock() {
        return false;
    };

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes;
    }

    

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        boolean sign = checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
        return sign;
    }
    // endregion

    // region Structure
    // spotless:off
	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
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
	private static final String STRUCTURE_PIECE_MAIN = "mainFairyWorkshop";
	private final int horizontalOffSet = 15;
	private final int verticalOffSet = 34;
	private final int depthOffSet = 0;
	@Override
	public IStructureDefinition<GTCM_FairyWorkshop> getStructureDefinition() {
		return StructureDefinition.<GTCM_FairyWorkshop>builder()
			       .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
			       .addElement(
				       'B',
                       ofBlockAnyMeta(getBlockFromStack(MaterialsBotania.BotaniaDragonstone.getBlocks(1))))
			       .addElement(
					   'A',
					   GT_HatchElementBuilder.<GTCM_FairyWorkshop>builder()
						   .atLeast(Maintenance, Energy.or(ExoticEnergy),InputBus, OutputBus, InputHatch, OutputHatch)
						   .adder(GTCM_FairyWorkshop::addToMachineList)
						   .dot(1)
						   .casingIndex(((GT_Block_Casings1) GregTech_API.sBlockCasings8).getTextureIndex(2))
						   .buildAndChain(GregTech_API.sBlockStones, 7))
			       .addElement('C',ofBlockAnyMeta(ModBlocks.prism))
			       .addElement('D',ofBlock(ModBlocks.pylon, 0))
			       .addElement('E',ofBlock(ModBlocks.livingwood,5))
			       .addElement('F',ofBlockAnyMeta(ModBlocks.manaGlass))
			       .build();
	}
	/*
	Blocks:
	 */
	public static final String[][] shape = new String[][]{
        {"E   E","     ","     ","     ","E   E"},
        {"C   C"," D D ","  ~  "," D D ","C   C"},
        {"EAAAE","AFEFA","AEBEA","AFEFA","EAAAE"}
    };
	
	// spotless:on
    // endregion

    // region Overrides

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_CrystallineInfinitier_MachineType)
            .addInfo(TextLocalization.Tooltip_CrystallineInfinitier_00)
            .addInfo(TextLocalization.Tooltip_CrystallineInfinitier_01)
            .addInfo(TextLocalization.Tooltip_CrystallineInfinitier_02)
            .addInfo(TextLocalization.Tooltip_CrystallineInfinitier_03)
            .addInfo(TextLocalization.Tooltip_CrystallineInfinitier_04)
            .addInfo(TextLocalization.Tooltip_CrystallineInfinitier_05)
            .addInfo(TextLocalization.Tooltip_CrystallineInfinitier_06)
            .addInfo(TextLocalization.Tooltip_GlassTierLimitEnergyHatchTier)
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(31, 36, 32, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 2)
            .addOutputHatch(TextLocalization.textUseBlueprint, 2)
            .addInputBus(TextLocalization.textUseBlueprint, 2)
            .addOutputBus(TextLocalization.textUseBlueprint, 2)
            .addMaintenanceHatch(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_CrystallineInfinitier(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[] {
            Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                .extFacing()
                .glow()
                .build() };
    }
    // endregion
}
