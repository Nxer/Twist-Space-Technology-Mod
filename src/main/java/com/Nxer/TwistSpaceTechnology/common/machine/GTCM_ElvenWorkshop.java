package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.ElvenWorkshopTexture;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;
import vazkii.botania.common.block.ModBlocks;

public class GTCM_ElvenWorkshop extends GTCM_MultiMachineBase<GTCM_ElvenWorkshop> {

    // region Class Constructor
    public GTCM_ElvenWorkshop(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_ElvenWorkshop(String aName) {
        super(aName);
    }

    // endregion
    // region Processing Logic

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Rune Engraver
         * 1 - Elven Workshop
         */
        return 2;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SINGULARITY);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT);
    }

    @Override
    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal("ElvenWorkshop.modeMsg." + mode);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (machineMode) {
            case 1:
                return GTCMRecipe.ElvenWorkshopRecipes;
            default:
                return GTCMRecipe.RuneEngraverRecipes;
        }
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTCMRecipe.ElvenWorkshopRecipes, GTCMRecipe.RuneEngraverRecipes);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        enablePerfectOverclock = true;
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }
    // endregion

    // region Structure
    // spotless:off
	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
	}

	@Override
	public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
		if (this.mMachine) return -1;
		return this.survivalBuildPiece(
			STRUCTURE_PIECE_MAIN,
			stackSize,
			horizontalOffSet,
			verticalOffSet,
			depthOffSet,
            elementBudget,
			env,
			false,
			true);
	}
	private static final String STRUCTURE_PIECE_MAIN = "mainElvenWorkshop";
	private final int horizontalOffSet = 2;
	private final int verticalOffSet = 1;
	private final int depthOffSet = 2;
    private static IStructureDefinition<GTCM_ElvenWorkshop> STRUCTURE_DEFINITION = null;
	@Override
	public IStructureDefinition<GTCM_ElvenWorkshop> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GTCM_ElvenWorkshop>builder()
                                                      .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                                                      .addElement(
                                                          'A',
                                                          HatchElementBuilder.<GTCM_ElvenWorkshop>builder()
                                                                                .atLeast(Energy.or(ExoticEnergy),InputBus, OutputBus, InputHatch)
                                                                                .adder(GTCM_ElvenWorkshop::addToMachineList)
                                                                                .dot(1)
                                                                                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                                                                                .buildAndChain(GregTechAPI.sBlockStones, 7))
                                                      .addElement('B',ofBlock(ModBlocks.storage, 4))
                                                      .addElement('C',ofBlockAnyMeta(ModBlocks.prism))
                                                      .addElement('D',ofBlock(ModBlocks.pylon, 0))
                                                      .addElement('E',ofBlock(ModBlocks.livingwood,5))
                                                      .addElement('F',ofBlockAnyMeta(ModBlocks.manaGlass))
                                                      .build();
        }
		return STRUCTURE_DEFINITION;
	}
	/*
	Blocks:
    A: Hatches/Smooth Marble
    B: Dragonstone Block
    C: Prism
    D: Mana Pylon
    E: Glimmering Livingwood
    F: Managlass
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
        aNBT.setByte("mode", (byte) machineMode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getByte("mode");
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_ElvenWorkshop_MachineType)
            .addSeparator()
            .addInfo(
                // #tr ElvenWorkshopStructureNote
                // # For its unique structure, you may need to use Blueprint to build the machine.
                // #zh_CN 由于该机器独特的结构，你可能需要在建成之后重新摆放主机以通过结构检测。
                TextEnums.tr("ElvenWorkshopStructureNote"))
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(5, 3, 5, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_ElvenWorkshop(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
            TextureFactory.builder()
                .addIcon(new ElvenWorkshopTexture())
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(new ElvenWorkshopTexture())
                .extFacing()
                .glow()
                .build() };
    }

    // endregion
}
