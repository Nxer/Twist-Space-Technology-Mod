package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.internal_structure_issue;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textFrontBottom;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
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
import static gregtech.api.util.GTRecipeConstants.GLASS;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.configs.Configuration;
import bartworks.common.tileentities.tiered.MTERadioHatch;
import bartworks.util.BWUtil;
import bartworks.util.MathUtils;
import bartworks.util.ResultWrongSievert;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.recipe.Sievert;

@SkipGenerateDescription
public class TST_BiosphereIII extends GTCM_MultiMachineBase<TST_BiosphereIII> {

    // region Class Constructor
    public TST_BiosphereIII(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.HOLEFISH);
    }

    public TST_BiosphereIII(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BiosphereIII(mName);
    }
    // endregion

    // region Structure
    private final int horizontalOffSet = 8;
    private final int verticalOffSet = 17;
    private final int depthOffSet = 1;
    private final String STRUCTURE_PIECE_MAIN = "mainBiosphereIII";

    // spotless:off
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
    // spotless:on

    private static final int STAINLESS_STEEL_CASING_INDEX = 49;
    private static IStructureDefinition<TST_BiosphereIII> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_BiosphereIII> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_BiosphereIII>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement('A', chainAllGlasses(-1, (te, t) -> te.mGlassTier = t, te -> te.mGlassTier))
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
                            .hint(1)
                            .casingIndex(STAINLESS_STEEL_CASING_INDEX)
                            .buildAndChain(GregTechAPI.sBlockCasings4, 1)))
                .addElement('I', isAir())
                .addElement('J', ofFrame(Materials.Osmiridium))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
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

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        mRadHatches.clear();
        mGlassTier = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) return;
        if (this.mGlassTier <= 0) {
            errors.add(internal_structure_issue);
            return;
        }
        checkOneOutputHatch(errors);
        if (mRadHatches.size() > 1) {
            errors.add(StructureErrors.of("GT5U.gui.text.recipe_result.structure_error.too_many_radiation_hatch"));
        }
    }
    // endregion

    // region Processing Logic
    private int mGlassTier = 0;
    private int mNeededGlassTier = 0;
    private int mSievert = 0;
    private int mNeededSievert = 0;
    private int efficiency = 1;
    private ArrayList<MTERadioHatch> mRadHatches = new ArrayList<>();
    private final Sievert defaultSievertData = new Sievert(0, false);

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SIMPLEWASHER, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT,
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID };

    @Override
    public RecipeMap<?> getRecipeMap() {
        return switch (machineMode) {
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
    public int totalMachineMode() {
        /*
         * 0 - Bacterial Vat
         * 1 - Bacterial Vat Auto
         * 2 - Brewery
         * 3 - Fermenter
         */
        return 4;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        return switch (machineMode) {
            // spotless:off
            // #tr tst.common.machine.BiosphereIII.mode.00
            // # {\GOLD}----- Bacterial Vat Mode -----
            // #zh_CN {\GOLD}----- 细菌培养缸模式 -----
            case 0 -> TSTUtils.tr("tst.common.machine.BiosphereIII.mode.00");
            // #tr tst.common.machine.BiosphereIII.mode.01
            // # {\GOLD}----- Bacterial Vat Automation Mode -----
            // #zh_CN {\GOLD}----- 细菌培养缸自动化模式 -----
            case 1 -> TSTUtils.tr("tst.common.machine.BiosphereIII.mode.01");
            // #tr tst.common.machine.BiosphereIII.mode.02
            // # {\GOLD}----- Brewing Machine Mode -----
            // #zh_CN {\GOLD}----- 酿造室模式 -----
            case 2 -> TSTUtils.tr("tst.common.machine.BiosphereIII.mode.02");
            // #tr tst.common.machine.BiosphereIII.mode.03
            // # {\GOLD}----- Fermenter Mode -----
            // #zh_CN {\GOLD}----- 发酵槽模式 -----
            default -> TSTUtils.tr("tst.common.machine.BiosphereIII.mode.03");
            // spotless:on
        };
    }

    @Override
    public int getMaxParallelRecipes() {
        return switch (machineMode) {
            case 0 -> (getControllerSlot() == null) ? 0 : getControllerSlot().stackSize * 4; // Bio Vat normal
            case 1 -> (getControllerSlot() == null) ? 0 : getControllerSlot().stackSize; // Bio Vat automation
            default -> 1 << Math.max(mGlassTier * 2 - 6, 0); // Brewing && Fermenting
        };
    }

    @Override
    protected float getSpeedBonus() {
        return switch (machineMode) {
            case 0 -> 0.5f; // Bio Vat normal
            case 1 -> 1; // Bio Vat automation
            default -> 0.25f; // Brewing && Fermenting
        };
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                // no check for Brewing & Fermenting
                if (machineMode == 2 || machineMode == 3) return CheckRecipeResultRegistry.SUCCESSFUL;

                // Bio Vat check
                // Petri Dish check
                if (!BWUtil
                    .areStacksEqualOrNull((ItemStack) recipe.mSpecialItems, TST_BiosphereIII.this.getControllerSlot()))
                    return CheckRecipeResultRegistry.NO_RECIPE;

                Sievert data = recipe.getMetadataOrDefault(GTRecipeConstants.SIEVERT, defaultSievertData);
                TST_BiosphereIII.this.mNeededGlassTier = recipe.getMetadataOrDefault(GLASS, 0);
                TST_BiosphereIII.this.mNeededSievert = data.sievert;

                // Glass tier check
                if (TST_BiosphereIII.this.mGlassTier < TST_BiosphereIII.this.mNeededGlassTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(TST_BiosphereIII.this.mNeededGlassTier);
                }

                // Sievert check
                if (!data.isExact) {
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

            @NotNull
            @Override
            public CheckRecipeResult process() {

                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
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
        if (machineMode == 2 || machineMode == 3) return recipe;

        GTRecipe tRecipe = recipe.copy();
        if (machineMode == 0) efficiency = getExpectedMultiplier(tRecipe.mFluidOutputs[0]);// Bio Vat Normal
        else efficiency = (int) (((mGlassTier - mNeededGlassTier) * 600 + 1601.0) / 1000
            * Configuration.Multiblocks.bioVatMaxParallelBonus);// Bio Vat Automation

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
        double y = getOutputCapacity() / 2D, z = Configuration.Multiblocks.bioVatMaxParallelBonus;
        int ret = (int) Math.ceil((-1D / y * Math.pow(x - y, 2D) + y) / y * z);
        return MathUtils.clamp(1, ret, Configuration.Multiblocks.bioVatMaxParallelBonus);
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

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = TSTSharedLocalization.MachineInfo.glassTier(this.mGlassTier);
        ret[origin.length + 1] = EnumChatFormatting.BLUE + getMachineModeName();
        // #tr tst.common.machine.BiosphereIII.info.efficiency
        // # Efficiency:
        // #zh_CN 效率:
        ret[origin.length + 2] = TSTUtils.tr("tst.common.machine.BiosphereIII.info.efficiency")
            + ((machineMode == 2 || machineMode == 3) ?
            // Brewing & Fermenting
                (EnumChatFormatting.GREEN + "100" + EnumChatFormatting.RESET + "%") :
                // Bio Vat
                (EnumChatFormatting.GREEN + formatNumber(efficiency) + EnumChatFormatting.RESET + "x"));
        return ret;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mSievert", mSievert);
        aNBT.setInteger("mNeededSievert", mNeededSievert);
        aNBT.setByte("mode", (byte) machineMode);
        aNBT.setInteger("efficiency", efficiency);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mSievert = aNBT.getInteger("mSievert");
        mNeededSievert = aNBT.getInteger("mNeededSievert");
        machineMode = aNBT.getByte("mode");
        efficiency = aNBT.getInteger("efficiency");
        super.loadNBTData(aNBT);
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return TSTControllerTextures.getTexture(
            side,
            aFacing,
            aActive,
            Textures.BlockIcons.getCasingTextureForId(STAINLESS_STEEL_CASING_INDEX),
            OVERLAY_FRONT_DISTILLATION_TOWER,
            OVERLAY_FRONT_DISTILLATION_TOWER_GLOW,
            OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE,
            OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.BiosphereIII.tooltip.machine_type
        // # Bacterial Vat | Brewing Machine | Fermenter
        // #zh_CN 细菌培养缸 | 酿造室 | 发酵槽
        tt.addMachineType(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.machine_type"))
            // #tr tst.common.machine.BiosphereIII.tooltip.controller
            // # Controller block for Biosphere III
            // #zh_CN 生物圈III号的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.controller"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.01
            // # {\AQUA}{\BOLD}Control the thoughts of those microorganisms...
            // #zh_CN {\BLUE}{\BOLD}操控微生物们的思想……
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.01"))
            .addInfo(TSTSharedLocalization.MachineTooltip.textScrewdriverChangeMode)
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.mode.00"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.02
            // # Need Petri Dish in controller slot
            // #zh_CN 需要在主机中放入培养皿.加速{\RED}100%{\GOLD}.
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.02"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.03
            // # Every Petri Dish provides {\AQUA}4x{\GRAY} parallel
            // #zh_CN 每个培养皿提供{\AQUA}4x{\GRAY}并行
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.03"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.04
            // # Keep the Output Hatch always half filled for maximum efficiency
            // #zh_CN 保持输出仓半满以达到最高效率
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.04"))
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.mode.01"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.05
            // # Need Petri Dish in controller slot
            // #zh_CN 需要在主机中放入培养皿
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.05"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.06
            // # Every Petri Dish provides {\AQUA}1x{\GRAY} parallel
            // #zh_CN 每个培养皿提供{\AQUA}1x{\GRAY}并行
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.06"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.07
            // # Advanced artificial intelligence controls the breeding of bacteria
            // #zh_CN 使用先进的人工智能控制细菌的繁殖
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.07"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.08
            // # Original efficiency of control is {\RED}40%{\GRAY} of the maximum. Each glass tier over recipe requirement improve the efficiency by {\RED}15%{\GRAY}.
            // #zh_CN 初始控制效率为最高效率的{\RED}160%{\GRAY}.玻璃等级每超出配方要求1级,将控制效率提高{\RED}60%{\GRAY}.
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.08"))
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.mode.02"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.09
            // # Don't need Petri Dish
            // #zh_CN 不需要培养皿.加速300%%.
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.09"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.10
            // # Each glass tier over HV provides 4 times parallel
            // #zh_CN 玻璃等级每超出HV一级,提供4倍并行
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.10"))
            // #tr tst.common.machine.BiosphereIII.tooltip.info.11
            // # EV glass provides {\AQUA}4x{\GRAY} parallel, IV glass provides {\AQUA}16x{\GRAY} parallel, etc.
            // #zh_CN EV玻璃提供{\AQUA}4x{\GRAY}并行,IV玻璃提供{\AQUA}16x{\GRAY}并行,以此类推
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.11"))
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.mode.03"))
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.09"))
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.10"))
            .addInfo(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.info.11"))
            .beginStructureBlock(13, 19, 17, false)
            .addController(textFrontBottom)
            // #tr tst.common.machine.BiosphereIII.tooltip.structure.01
            // # Any Bottom Clean Stainless Steel Machine Casing
            // #zh_CN 任意底层洁净不锈钢机械方块
            .addInputHatch(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.structure.01"), 1)
            .addOutputHatch(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.structure.01"), 1)
            .addInputBus(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.structure.01"), 1)
            .addOutputBus(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.structure.01"), 1)
            .addEnergyHatch(TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.structure.01"), 1)
            .addStructureInfo(
                // #tr tst.common.machine.BiosphereIII.tooltip.structure.02
                // # Radiation Hatch: 0-1x
                // #zh_CN 放射仓: 0-1x
                TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.structure.02") + ", " + TSTUtils.tr("tst.common.machine.BiosphereIII.tooltip.structure.01"))
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Hatch Registration

    private boolean addRadiationInputToMachineList(IGregTechTileEntity aTileEntity, int CasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!(aMetaTileEntity instanceof MTERadioHatch)) {
            return false;
        } else {
            ((MTERadioHatch) aMetaTileEntity).updateTexture(CasingIndex);
            return this.mRadHatches.add((MTERadioHatch) aMetaTileEntity);
        }
    }

    // endregion

}
