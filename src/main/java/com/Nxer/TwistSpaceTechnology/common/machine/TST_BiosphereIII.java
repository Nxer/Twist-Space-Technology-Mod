package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontBottom;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.configs.ConfigHandler;
import bartworks.common.tileentities.multis.MTEBioVat;
import bartworks.common.tileentities.tiered.GT_MetaTileEntity_RadioHatch;
import bartworks.util.BWUtil;
import bartworks.util.MathUtils;
import bartworks.util.ResultWrongSievert;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
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
import gregtech.api.util.ParallelHelper;

public class TST_BiosphereIII extends GTCM_MultiMachineBase<TST_BiosphereIII> {

    // region Class Constructor
    public TST_BiosphereIII(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_BiosphereIII(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BiosphereIII(mName);
    }

    // endregion

    // region Processing Logic
    private byte mode = 0;
    private byte mGlassTier = 0;
    private int mNeededGlassTier = 0;
    private int mSievert = 0;
    private int mNeededSievert = 0;
    private int efficiency = 1;
    private ArrayList<GT_MetaTileEntity_RadioHatch> mRadHatches = new ArrayList<>();

    @Override
    public RecipeMap<?> getRecipeMap() {
        return switch (mode) {
            case 0, 1 -> BartWorksRecipeMaps.bacterialVatRecipes;
            case 2 -> RecipeMaps.brewingRecipes;
            default -> RecipeMaps.fermentingRecipes;
        };
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays
            .asList(BartWorksRecipeMaps.bacterialVatRecipes, RecipeMaps.brewingRecipes, RecipeMaps.fermentingRecipes);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return switch (mode) {
            case 0 -> 0.5f; // Bio Vat normal
            case 1 -> 1; // Bio Vat automation
            default -> 0.25f; // Brewing && Fermenting
        };
    }

    @Override
    protected int getMaxParallelRecipes() {
        return switch (mode) {
            case 0 -> (getControllerSlot() == null) ? 0 : getControllerSlot().stackSize * 4; // Bio Vat normal
            case 1 -> (getControllerSlot() == null) ? 0 : getControllerSlot().stackSize; // Bio Vat automation
            default -> 1 << Math.max(mGlassTier * 2 - 6, 0); // Brewing && Fermenting
        };
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                // no check for Brewing & Fermenting
                if (mode == 2 || mode == 3) return CheckRecipeResultRegistry.SUCCESSFUL;

                // Bio Vat check
                // Petri Dish check
                if (!BWUtil
                    .areStacksEqualOrNull((ItemStack) recipe.mSpecialItems, TST_BiosphereIII.this.getControllerSlot()))
                    return CheckRecipeResultRegistry.NO_RECIPE;

                int[] conditions = MTEBioVat.specialValueUnpack(recipe.mSpecialValue);
                TST_BiosphereIII.this.mNeededGlassTier = conditions[0];
                TST_BiosphereIII.this.mNeededSievert = conditions[3];

                // Glass tier check
                if (TST_BiosphereIII.this.mGlassTier < TST_BiosphereIII.this.mNeededGlassTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(TST_BiosphereIII.this.mNeededGlassTier);
                }

                // Sievert check
                if (conditions[2] == 0) {
                    if (TST_BiosphereIII.this.mSievert < TST_BiosphereIII.this.mNeededSievert) {
                        return ResultWrongSievert.insufficientSievert(TST_BiosphereIII.this.mNeededSievert);
                    }
                } else if (TST_BiosphereIII.this.mSievert != TST_BiosphereIII.this.mNeededSievert) {
                    return ResultWrongSievert.wrongSievert(TST_BiosphereIII.this.mNeededSievert);
                }

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected ParallelHelper createParallelHelper(@NotNull GTRecipe recipe) {
                return super.createParallelHelper(recipeAfterEfficiencyCalculation(recipe, inputFluids));
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.setSpecialSlotItem(this.getControllerSlot());
    }

    private GTRecipe recipeAfterEfficiencyCalculation(GTRecipe recipe, FluidStack[] inputFluids) {
        // Brewing & Fermenting, no change to the recipe
        if (mode == 2 || mode == 3) return recipe;

        GTRecipe tRecipe = recipe.copy();
        if (mode == 0) efficiency = getExpectedMultiplier(tRecipe.mFluidOutputs[0]);// Bio Vat Normal
        else efficiency = (int) (((mGlassTier - mNeededGlassTier) * 600 + 1601.0) / 1000
            * ConfigHandler.bioVatMaxParallelBonus);// Bio Vat Automation

        long fluidAmount = 0;
        for (FluidStack fluid : inputFluids) {
            if (fluid.isFluidEqual(recipe.mFluidInputs[0])) {
                fluidAmount += fluid.amount;
            }
        }
        efficiency = (int) Math.min(efficiency, fluidAmount / recipe.mFluidInputs[0].amount);
        efficiency = Math.max(efficiency, 1);

        tRecipe.mFluidInputs[0].amount *= efficiency;
        tRecipe.mFluidOutputs[0].amount *= efficiency;
        return tRecipe;
    }

    private int getExpectedMultiplier(@Nullable FluidStack recipeFluidOutput) {
        FluidStack storedFluidOutputs = this.getStoredFluidOutputs();
        if (storedFluidOutputs == null) return 1;
        if (storedFluidOutputs.isFluidEqual(recipeFluidOutput)) {
            return this.calcMod(storedFluidOutputs.amount) + 1;
        }
        return 1;
    }

    private int calcMod(double x) {
        double y = getOutputCapacity() / 2D, z = ConfigHandler.bioVatMaxParallelBonus;
        int ret = (int) Math.ceil((-1D / y * Math.pow(x - y, 2D) + y) / y * z);
        return MathUtils.clamp(1, ret, ConfigHandler.bioVatMaxParallelBonus);
    }

    private int getOutputCapacity() {
        return mOutputHatches.get(0)
            .getCapacity();
    }

    private FluidStack getStoredFluidOutputs() {
        return mOutputHatches.get(0)
            .getFluid();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        mRadHatches.clear();
        mGlassTier = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (this.mGlassTier <= 0) return false;
        if (mRadHatches.size() > 1 && mOutputHatches.size() > 1) return false;
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (this.getBaseMetaTileEntity()
            .isServerSide() && this.mRadHatches.size() == 1) {
            this.mSievert = this.mRadHatches.get(0)
                .getSievert();
            if (this.getBaseMetaTileEntity()
                .isActive() && this.mNeededSievert > this.mSievert) this.mOutputFluids = null;
        }
    }
    // endregion

    // region Structure
    // spotless:off
    private final int horizontalOffSet = 8;
    private final int verticalOffSet = 17;
    private final int depthOffSet = 1;
    private final String STRUCTURE_PIECE_MAIN = "mainBiosphereIII";
    @SuppressWarnings("SpellCheckingInspection")
    private final String[][] shapeMain = new String[][] {
        { "                 ", "                 ", "                 ", "                 ", "                 ", "EEEEEEEEEEEEEEEEE", "EJJJJJJJJJJJJJJJE", "EEEEEEEEEEEEEEEEE", "                 ", "                 ", "                 ", "                 ", "                 " },
        { "                 ", "                 ", "                 ", "                 ", "                 ", "EJJJJJJJJJJJJJJJE", "JBBBBBBBBBBBBBBBJ", "EJJJJJJJJJJJJJJJE", "                 ", "                 ", "                 ", "                 ", "                 " },
        { "                 ", "                 ", "                 ", "                 ", "                 ", "EJ             JE", "JBJJJJJ B JJJJJBJ", "EJ             JE", "                 ", "                 ", "                 ", "                 ", "                 " },
        { "                 ", "                 ", "                 ", "                 ", "                 ", "EJ     CCC     JE", "JBJ    CBC    JBJ", "EJ     CCC     JE", "                 ", "                 ", "                 ", "                 ", "                 " },
        { "                 ", "                 ", "                 ", "       CCC       ", "      CCCCC      ", "EJ   CCCCCCC   JE", "JBJ  CCCBCCC  JBJ", "EJ   CCCCCCC   JE", "      CCCCC      ", "       CCC       ", "                 ", "                 ", "                 " },
        { "                 ", "       CCC       ", "     CCDDDCC     ", "    CDDDDDDDC    ", "    CDDDDDDDC    ", "EJ CDDDDDDDDDC JE", "JBJCDDDDBDDDDCJBJ", "EJ CDDDDDDDDDC JE", "    CDDDDDDDC    ", "    CDDDDDDDC    ", "     CCDDDCC     ", "       CCC       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIIIIIIC    ", "    AIIICIIIA    ", "EJ AIIICCCIIIA JE", "JB AIICCBCCIIA BJ", "EJ AIIICCCIIIA JE", "    AIIICIIIA    ", "    CIIIIIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIIIIIIC    ", "    AIIIIIIIA    ", "EJ AIIIICIIIIA JE", "JB AIIICBCIIIA BJ", "EJ AIIIICIIIIA JE", "    AIIIIIIIA    ", "    CIIIIIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIIIIIIC    ", "    AIIIIIIIA    ", "EJ AIIIIIIIIIA JE", "JB AIIIIBIIIIA BJ", "EJ AIIIIIIIIIA JE", "    AIIIIIIIA    ", "    CIIIIIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIICIIIC    ", "    AIIICIIIA    ", "EJ AIIIICIIIIA JE", "JBBBBCCCBCCCBBBBJ", "EJ AIIIICIIIIA JE", "    AIIICIIIA    ", "    CIIICIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIICIIIC    ", "    AIIICIIIA    ", "EJ AIIIICIIIIA JE", "JB AICCCBCCCIA BJ", "EJ AIIIICIIIIA JE", "    AIIICIIIA    ", "    CIIICIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIICIIIC    ", "    AIIICIIIA    ", "EJ AIIIICIIIIA JE", "JBBBBCCCBCCCBBBBJ", "EJ AIIIICIIIIA JE", "    AIIICIIIA    ", "    CIIICIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIICIIIC    ", "    AIIICIIIA    ", "EJ AIIIICIIIIA JE", "JB AICCCBCCCIA BJ", "EJ AIIIICIIIIA JE", "    AIIICIIIA    ", "    CIIICIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIICIIIC    ", "    AIIICIIIA    ", "EJ AIIIICIIIIA JE", "JBBBBCCCBCCCBBBBJ", "EJ AIIIICIIIIA JE", "    AIIICIIIA    ", "    CIIICIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIIIIIIC    ", "    AIIIIIIIA    ", "EJ AIIIIIIIIIA JE", "JB AIIIIBIIIIA BJ", "EJ AIIIIIIIIIA JE", "    AIIIIIIIA    ", "    CIIIIIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIIIIIIC    ", "    AIIIIIIIA    ", "EJ AIIIICIIIIA JE", "JB AIIICBCIIIA BJ", "EJ AIIIICIIIIA JE", "    AIIIIIIIA    ", "    CIIIIIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       AAA       ", "     CAIIIAC     ", "    CIIIIIIIC    ", "    AIIICIIIA    ", "EJ AIIICCCIIIA JE", "JB AIICCBCCIIA BJ", "EJ AIIICCCIIIA JE", "    AIIICIIIA    ", "    CIIIIIIIC    ", "     CAIIIAC     ", "       AAA       ", "                 " },
        { "                 ", "       H~H       ", "     HHDDDHH     ", "    HDDDDDDDH    ", "    HDDDDDDDH    ", "EJ HDDDDDDDDDH JE", "JBBBBBBBBBBBBBBBJ", "EJ HDDDDDDDDDH JE", "    HDDDDDDDH    ", "    HDDDDDDDH    ", "     HHDDDHH     ", "       HHH       ", "                 " },
        { "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFF" } };
    private static final int STAINLESS_STEEL_CASING_INDEX = 49;
    private static IStructureDefinition<TST_BiosphereIII> STRUCTURE_DEFINITION = null;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<TST_BiosphereIII> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_BiosphereIII>builder()
                                                      .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                                                      .addElement(
                                                          'A',
                                                          withChannel(
                                                              "glass",
                                                              BorosilicateGlass.ofBoroGlass(
                                                                  (byte) 0,
                                                                  (byte) 1,
                                                                  Byte.MAX_VALUE,
                                                                  (te, t) -> te.mGlassTier = t,
                                                                  te -> te.mGlassTier)))
                                                      .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 15))
                                                      .addElement('C', ofBlock(GregTechAPI.sBlockCasings4, 1))
                                                      .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 0))
                                                      .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 6))
                                                      .addElement('F', ofBlock(GregTechAPI.sBlockCasings8, 5))
                                                      .addElement(
                                                          'H',
                                                          ofChain(
                                                              ofHatchAdder(TST_BiosphereIII::addRadiationInputToMachineList, STAINLESS_STEEL_CASING_INDEX, 1),
                                                              HatchElementBuilder.<TST_BiosphereIII>builder()
                                                                                    .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                                                                                    .adder(TST_BiosphereIII::addToMachineList)
                                                                                    .dot(1)
                                                                                    .casingIndex(STAINLESS_STEEL_CASING_INDEX)
                                                                                    .buildAndChain(GregTechAPI.sBlockCasings4, 1)))
                                                      .addElement('I', isAir())
                                                      .addElement('J', ofFrame(Materials.Osmiridium))
                                                      .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private boolean addRadiationInputToMachineList(IGregTechTileEntity aTileEntity, int CasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!(aMetaTileEntity instanceof GT_MetaTileEntity_RadioHatch)) {
            return false;
        } else {
            ((GT_MetaTileEntity_RadioHatch) aMetaTileEntity).updateTexture(CasingIndex);
            return this.mRadHatches.add((GT_MetaTileEntity_RadioHatch) aMetaTileEntity);
        }
    }
    // spotless:on
    // endregion

    // region Info
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_BiosphereIII_MachineType)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Controller)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_00)
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            .addInfo(TextLocalization.BiosphereIII_Mode_00)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode0_00)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode0_01)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode0_02)
            .addInfo(TextLocalization.BiosphereIII_Mode_01)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode1_00)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode1_01)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode1_02)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode1_03)
            .addInfo(TextLocalization.BiosphereIII_Mode_02)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode2n3_00)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode2n3_01)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode2n3_02)
            .addInfo(TextLocalization.BiosphereIII_Mode_03)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode2n3_00)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode2n3_01)
            .addInfo(TextLocalization.Tooltip_BiosphereIII_Mode2n3_02)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(13, 19, 17, false)
            .addController(textFrontBottom)
            .addInputHatch(TextLocalization.textBiosphereIIIHatchLocation, 1)
            .addOutputHatch(TextLocalization.textBiosphereIIIHatchLocation, 1)
            .addInputBus(TextLocalization.textBiosphereIIIHatchLocation, 1)
            .addOutputBus(TextLocalization.textBiosphereIIIHatchLocation, 1)
            .addEnergyHatch(TextLocalization.textBiosphereIIIHatchLocation, 1)
            .addStructureInfo(
                TextLocalization.textBiosphereIIIRadioHatch + ", " + TextLocalization.textBiosphereIIIHatchLocation)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    // endregion

    // region General

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(STAINLESS_STEEL_CASING_INDEX), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(STAINLESS_STEEL_CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(STAINLESS_STEEL_CASING_INDEX) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mSievert", mSievert);
        aNBT.setInteger("mNeededSievert", mNeededSievert);
        aNBT.setByte("mode", mode);
        aNBT.setInteger("efficiency", efficiency);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mSievert = aNBT.getInteger("mSievert");
        mNeededSievert = aNBT.getInteger("mNeededSievert");
        mode = aNBT.getByte("mode");
        efficiency = aNBT.getInteger("efficiency");
        super.loadNBTData(aNBT);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            mode = (byte) ((mode + 1) % 4);
            String des = getDisplayMode(mode);
            GTUtility.sendChatToPlayer(aPlayer, String.join("", des));
        }
    }

    private String getDisplayMode(int mode) {
        return switch (mode) {
            case 0 -> TextLocalization.BiosphereIII_Mode_00;
            case 1 -> TextLocalization.BiosphereIII_Mode_01;
            case 2 -> TextLocalization.BiosphereIII_Mode_02;
            default -> TextLocalization.BiosphereIII_Mode_03;
        };
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + texter("Glass Tier: ", "MachineInfoData.GlassTier")
            + ":"
            + EnumChatFormatting.GOLD
            + this.mGlassTier;
        ret[origin.length + 1] = EnumChatFormatting.BLUE + getDisplayMode(mode);
        ret[origin.length + 2] = TextLocalization.BiosphereIIIEfficiency + ((mode == 2 || mode == 3) ?
        // Brewing & Fermenting
            (EnumChatFormatting.GREEN + "100" + EnumChatFormatting.RESET + "%") :
            // Bio Vat
            (EnumChatFormatting.GREEN + GTUtility.formatNumbers(efficiency) + EnumChatFormatting.RESET + "x"));
        return ret;
    }
}
