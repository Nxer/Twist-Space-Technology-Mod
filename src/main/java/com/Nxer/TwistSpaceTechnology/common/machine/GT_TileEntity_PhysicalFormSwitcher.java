package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.MAR_Casing;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import ggfab.api.GGFabRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;

public class GT_TileEntity_PhysicalFormSwitcher extends GTCM_MultiMachineBase<GT_TileEntity_PhysicalFormSwitcher> {

    // region Class Constructor
    public GT_TileEntity_PhysicalFormSwitcher(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_PhysicalFormSwitcher(String aName) {
        super(aName);
    }
    // endregion

    // region Processing Logic
    public byte glassTier;

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Fluid Solidifier
         * 1 - Fluid Extractor
         */
        return 2;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_FORMING);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    }

    @Override
    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal("PhysicalFormSwitcher.modeMsg." + mode);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.glassTier = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) {
            return false;
        }

        speedBonus = (float) Math.pow(SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher, getTotalPowerTier());

        return true;
    }

    public int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            RecipeMap<?> currentRecipeMap = RecipeMaps.fluidSolidifierRecipes;

            @Override
            protected RecipeMap<?> getCurrentRecipeMap() {
                if (machineMode != 0) return super.getCurrentRecipeMap();

                // add tool casting recipes to solidifier mode
                if (lastRecipeMap != RecipeMaps.fluidSolidifierRecipes
                    && lastRecipeMap != GGFabRecipeMaps.toolCastRecipes) {
                    lastRecipe = null;
                    lastRecipeMap = currentRecipeMap;
                }

                if (maxParallelSupplier != null) {
                    maxParallel = maxParallelSupplier.get();
                }

                return currentRecipeMap;
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                if (machineMode != 0) return super.process();

                currentRecipeMap = RecipeMaps.fluidSolidifierRecipes;
                CheckRecipeResult result = super.process();
                if (result.wasSuccessful()) return result;

                currentRecipeMap = GGFabRecipeMaps.toolCastRecipes;
                return super.process();
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (glassTier < 12 && glassTier < GTUtility.getTier(recipe.mEUt)) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(GTUtility.getTier(recipe.mEUt));
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {

        if (machineMode == 1) return RecipeMaps.fluidExtractionRecipes;

        return RecipeMaps.fluidSolidifierRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(
            RecipeMaps.fluidExtractionRecipes,
            RecipeMaps.fluidSolidifierRecipes,
            GGFabRecipeMaps.toolCastRecipes);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(
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

    // endregion

    // region Structure
    // spotless:off
	private final int horizontalOffSet = 7;
	private final int verticalOffSet = 18;
	private final int depthOffSet = 0;
	private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<GT_TileEntity_PhysicalFormSwitcher> STRUCTURE_DEFINITION = null;

	/*
	Blocks:
		A -> ofBlock...(BW_GlasBlocks, 14, ...);
		B -> ofBlock...(MAR_Casing, 0, ...);
		C -> ofBlock...(gt.blockcasings2, 8, ...);
		D -> ofBlock...(gt.blockcasings2, 15, ...);
		E -> ofBlock...(gt.blockcasings8, 10, ...); // Hatches
		F -> ofFrame
	 */

	public IStructureDefinition<GT_TileEntity_PhysicalFormSwitcher> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition
                                       .<GT_TileEntity_PhysicalFormSwitcher>builder()
                                       .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                                       .addElement(
                                           'A',
                                           withChannel("glass",
                                                       BorosilicateGlass.ofBoroGlass(
                                                           (byte) 0,
                                                           (byte) 1,
                                                           Byte.MAX_VALUE,
                                                           (te, t) -> te.glassTier = t,
                                                           te -> te.glassTier
                                                       )))
                                       .addElement('B', ofBlock(MAR_Casing,0))
                                       .addElement('C', ofBlock(GregTechAPI.sBlockCasings2,8))
                                       .addElement('D', ofBlock(GregTechAPI.sBlockCasings2,15))
                                       .addElement('E',
                                                   HatchElementBuilder.<GT_TileEntity_PhysicalFormSwitcher>builder()
                                                                         .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Energy.or(ExoticEnergy))
                                                                         .adder(GT_TileEntity_PhysicalFormSwitcher::addToMachineList)
                                                                         .dot(1)
                                                                         .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                                                                         .buildAndChain(GregTechAPI.sBlockCasings8,10))
                                       .addElement('F', ofFrame(Materials.NaquadahAlloy))
                                       .build();
        }

		return STRUCTURE_DEFINITION;
	}

	@Override
	public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		return super.addToMachineList(aTileEntity, aBaseCasingIndex)
			       || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
	}

	private String[][] shape = new String[][]{
		{"               ","      EEE      ","      EEE      ","      EEE      ","     EEEEE     ","    EEEEEEE    "," EEEEEEEEEEEEE "," EEEEEEEEEEEEE "," EEEEEEEEEEEEE ","    EEEEEEE    ","     EEEEE     ","      EEE      ","      EEE      ","      EEE      ","               "},
		{"               ","      F F      ","               ","               ","               ","       C       "," F    CBC    F ","     CBDBC     "," F    CBC    F ","       C       ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","       C       "," F    CBC    F ","     CBDBC     "," F    CBC    F ","       C       ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","       C       "," F    CBC    F ","     CBDBC     "," F    CBC    F ","       C       ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","       C       "," F    CBC    F ","     CBDBC     "," F    CBC    F ","       C       ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","               "," F     B     F ","      BDB      "," F     B     F ","               ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","               "," F     A     F ","      ADA      "," F     A     F ","               ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","      AAA      "," F   AA AA   F ","     A   A     "," F   AA AA   F ","      AAA      ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","       A       ","     AA AA     "," F   A   A   F ","    A     A    "," F   A   A   F ","     AA AA     ","       A       ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","      AAA      ","     A   A     "," F  A     A  F ","    A     A    "," F  A     A  F ","     A   A     ","      AAA      ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","       A       ","     AA AA     "," F   A   A   F ","    A     A    "," F   A   A   F ","     AA AA     ","       A       ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","      AAA      "," F   AA AA   F ","     A   A     "," F   AA AA   F ","      AAA      ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","               "," F     A     F ","      ADA      "," F     A     F ","               ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","               "," F     B     F ","      BDB      "," F     B     F ","               ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","       C       "," F    CBC    F ","     CBDBC     "," F    CBC    F ","       C       ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","       C       "," F    CBC    F ","     CBDBC     "," F    CBC    F ","       C       ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","       C       "," F    CBC    F ","     CBDBC     "," F    CBC    F ","       C       ","               ","               ","               ","      F F      ","               "},
		{"               ","      F F      ","               ","               ","               ","       C       "," F    CBC    F ","     CBDBC     "," F    CBC    F ","       C       ","               ","               ","               ","      F F      ","               "},
		{"      E~E      ","      EEE      ","    EEEEEEE    ","   EEEEEEEEE   ","  EEEEEEEEEEE  ","  EEEEEEEEEEE  ",
			"EEEEEEEEEEEEEEE","EEEEEEEEEEEEEEE","EEEEEEEEEEEEEEE","  EEEEEEEEEEE  ","  EEEEEEEEEEE  ","   EEEEEEEEE   ","    EEEEEEE    ","      EEE      ","      EEE      "},
		{"      EEE      ","    EEEEEEE    ","   EEEEEEEEE   ","  EEEEEEEEEEE  "," EEEEEEEEEEEEE "," EEEEEEEEEEEEE ","EEEEEEEEEEEEEEE","EEEEEEEEEEEEEEE","EEEEEEEEEEEEEEE"," EEEEEEEEEEEEE "," EEEEEEEEEEEEE ","  EEEEEEEEEEE  ","   EEEEEEEEE   ","    EEEEEEE    ","      EEE      "}
	};
	// spotless:on

    // endregion

    // region Overrides

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setBoolean("mode", machineMode == 1);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        machineMode = aNBT.getBoolean("mode") ? 1 : 0;
    }

    // Scanner Info
    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "Glass Tier: " + EnumChatFormatting.GOLD + this.glassTier;
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "Parallel: "
            + EnumChatFormatting.GOLD
            + this.getMaxParallelRecipes();
        ret[origin.length + 2] = EnumChatFormatting.AQUA + "Recipe Time multiplier: "
            + EnumChatFormatting.GOLD
            + this.getSpeedBonus();
        return ret;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_PhysicalFormSwitcher_MachineType)
            .addInfo(TextLocalization.Tooltip_PhysicalFormSwitcher_00)
            .addInfo(TextLocalization.Tooltip_PhysicalFormSwitcher_01)
            .addInfo(TextLocalization.Tooltip_PhysicalFormSwitcher_02)
            .addInfo(TextLocalization.Tooltip_PhysicalFormSwitcher_03)
            .addInfo(TextLocalization.Tooltip_PhysicalFormSwitcher_04)
            .addInfo(TextLocalization.Tooltip_PhysicalFormSwitcher_05)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(15, 20, 15, false)
            .addInputHatch(TextLocalization.textAnyCasing, 1)
            .addOutputHatch(TextLocalization.textAnyCasing, 1)
            .addInputBus(TextLocalization.textAnyCasing, 1)
            .addOutputBus(TextLocalization.textAnyCasing, 1)
            .addEnergyHatch(TextLocalization.textAnyCasing, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_PhysicalFormSwitcher(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)) };
    }

    // endregion
}
