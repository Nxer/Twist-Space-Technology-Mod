package com.GTNH_Community.gtnhcommunitymod.common.machine;

import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.BLUE_PRINT_INFO;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.ModName;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.StructureTooComplex;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhysicalFormSwitcher_00;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhysicalFormSwitcher_01;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhysicalFormSwitcher_02;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhysicalFormSwitcher_03;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhysicalFormSwitcher_04;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhysicalFormSwitcher_05;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhysicalFormSwitcher_MachineType;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textAnyCasing;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.MAR_Casing;
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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

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
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings8;

public class GT_TileEntity_PhysicalFormSwitcher
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_PhysicalFormSwitcher>
    implements IConstructable, ISurvivalConstructable {

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
    public boolean mode = false; // false = sFluidSolidficationRecipes; true = sFluidExtractionRecipes

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                return super.process();
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (glassTier < 12 && glassTier < GT_Utility.getTier(recipe.mEUt)) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(GT_Utility.getTier(recipe.mEUt));
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public int getMaxParallelRecipes() {
        return (int) (Math.pow(4, GT_Utility.getTier(this.getMaxInputVoltage())));
    }

    public float getSpeedBonus() {
        return (float) Math.pow(0.9, GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {

        if (mode) return GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes;

        return GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = !this.mode;

            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("PhysicalFormSwitcher.modeMsg." + (this.mode ? "0" : "1")));
        }
    }

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

    // endregion

    // region Structure
    // spotless:off
	private final int horizontalOffSet = 7;
	private final int verticalOffSet = 18;
	private final int depthOffSet = 0;
	private static final String STRUCTURE_PIECE_MAIN = "main";
	
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
		
		return StructureDefinition
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
			       .addElement('C', ofBlock(GregTech_API.sBlockCasings2,8))
			       .addElement('D', ofBlock(GregTech_API.sBlockCasings2,15))
			       .addElement('E',
	                    GT_HatchElementBuilder.<GT_TileEntity_PhysicalFormSwitcher>builder()
		                    .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance,Energy.or(ExoticEnergy))
		                    .adder(GT_TileEntity_PhysicalFormSwitcher::addToMachineList)
		                    .dot(1)
		                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(10))
		                    .buildAndChain(GregTech_API.sBlockCasings8,10))
			       .addElement('F', ofFrame(Materials.NaquadahAlloy))
			       .build();
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

        aNBT.setBoolean("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mode = aNBT.getBoolean("mode");
    }

    // Scanner Info
    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length - 2] = EnumChatFormatting.AQUA + "Glass Tier: " + EnumChatFormatting.GOLD + this.glassTier;
        ret[origin.length - 1] = EnumChatFormatting.AQUA + "Parallel: "
            + EnumChatFormatting.GOLD
            + this.getMaxParallelRecipes();
        ret[origin.length] = EnumChatFormatting.AQUA + "Recipe Time multiplier: "
            + EnumChatFormatting.GOLD
            + this.getSpeedBonus();
        return ret;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(Tooltip_PhysicalFormSwitcher_MachineType)
            .addInfo(Tooltip_PhysicalFormSwitcher_00)
            .addInfo(Tooltip_PhysicalFormSwitcher_01)
            .addInfo(Tooltip_PhysicalFormSwitcher_02)
            .addInfo(Tooltip_PhysicalFormSwitcher_03)
            .addInfo(Tooltip_PhysicalFormSwitcher_04)
            .addInfo(Tooltip_PhysicalFormSwitcher_05)
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(15, 20, 15, false)
            .addInputHatch(textAnyCasing, 1)
            .addOutputHatch(textAnyCasing, 1)
            .addInputBus(textAnyCasing, 1)
            .addOutputBus(textAnyCasing, 1)
            .addMaintenanceHatch(textAnyCasing, 1)
            .addEnergyHatch(textAnyCasing, 1)
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.glassTier = 0;
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_PhysicalFormSwitcher(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
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
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)),
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
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)) };
    }

    // endregion
}
