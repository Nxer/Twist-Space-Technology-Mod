package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.ElvenWorkshopTexture;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings8;
import vazkii.botania.common.block.ModBlocks;

public class GTCM_ElvenWorkshop extends GTCM_MultiMachineBase<GTCM_ElvenWorkshop> {

    private byte mode = 1;

    // region Class Constructor
    public GTCM_ElvenWorkshop(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_ElvenWorkshop(String aName) {
        super(aName);
    }

    // endregion
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
    public RecipeMap<?> getRecipeMap() {
        switch (mode) {
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
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.mode = (byte) ((this.mode + 1) % 2);
        GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("ElvenWorkshop.modeMsg." + this.mode));

    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
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
                                                          GT_HatchElementBuilder.<GTCM_ElvenWorkshop>builder()
                                                                                .atLeast(Energy.or(ExoticEnergy),InputBus, OutputBus, InputHatch)
                                                                                .adder(GTCM_ElvenWorkshop::addToMachineList)
                                                                                .dot(1)
                                                                                .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(2))
                                                                                .buildAndChain(GregTech_API.sBlockStones, 7))
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
        aNBT.setByte("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mode = aNBT.getByte("mode");
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_ElvenWorkshop_MachineType)
            .addSeparator()
            .addInfo(
                texter(
                    "For its unique structure, you may need to use Blueprint to build the machine.",
                    "ElvenWorkshopStructureNote"))
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
            Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)),
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

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
    // endregion
}
