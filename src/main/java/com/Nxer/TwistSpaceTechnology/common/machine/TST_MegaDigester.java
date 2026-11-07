package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.config.Config.EnablePerfectOverclock_MegaDigester;
import static com.Nxer.TwistSpaceTechnology.config.Config.Parallel_MegaDigester;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textFrontBottom;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.api.recipe.LanthanidesRecipeMaps;

@SkipGenerateDescription
public class TST_MegaDigester extends GTCM_MultiMachineBase<TST_MegaDigester> {

    // region Class Constructor
    public TST_MegaDigester(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_MegaDigester(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaDigester(this.mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "mainMegaDigester";
    private static final int horizontalOffSet = 3;
    private static final int verticalOffSet = 3;
    private static final int depthOffSet = 0;
    private static IStructureDefinition<TST_MegaDigester> STRUCTURE_DEFINITION = null;

    // Same structure as the Lanthanides Digester.
    // spotless:off
    private final String[][] shapeMain = new String[][]{
        { "       ", " ttttt ", " t---t ", " t---t ", " t---t ", " ttttt ", "       " },
        { "  ttt  ", " t---t ", "t-----t", "t-----t", "t-----t", " t---t ", "  ttt  " },
        { " tccct ", "tc---ct", "c-----c", "c-----c", "c-----c", "tc---ct", " tccct " },
        { " tt~tt ", "thhhhht", "thsssht", "thsssht", "thsssht", "thhhhht", " ttttt " }
    };
    // spotless:on

    private int casingAmount = 0;
    private HeatingCoilLevel coilLevel = HeatingCoilLevel.None;

    @Override
    public IStructureDefinition<TST_MegaDigester> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaDigester>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement(
                    't',
                    buildHatchAdder(TST_MegaDigester.class)
                        .atLeast(
                            InputHatch,
                            OutputHatch,
                            InputBus,
                            OutputBus,
                            Maintenance,
                            Muffler,
                            Energy.or(ExoticEnergy))
                        .casingIndex(48)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(TST_MegaDigester::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings4, 0))))
                .addElement('h', ofBlock(GregTechAPI.sBlockCasings1, 11))
                .addElement('s', ofBlock(GregTechAPI.sBlockCasings4, 1))
                .addElement(
                    'c',
                    GTStructureChannels.HEATING_COIL
                        .use(activeCoils(ofCoil(TST_MegaDigester::setCoilLevel, TST_MegaDigester::getCoilLevel))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
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
        casingAmount = 0;
        coilLevel = HeatingCoilLevel.None;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) return;
        checkCasingMin(errors, casingAmount, 40);
        checkHasMufflerHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }
    // endregion

    // region Processing Logic

    @Override
    public RecipeMap<?> getRecipeMap() {
        return LanthanidesRecipeMaps.digesterRecipes;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    public int getMaxParallelRecipes() {
        return Parallel_MegaDigester;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return EnablePerfectOverclock_MegaDigester;
    }

    // Initial speed is 100%, and each coil tier adds another 100%.
    @Override
    protected float getSpeedBonus() {
        return 1F / (coilLevel.getTier() + 1);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @Nonnull
            @Override
            public CheckRecipeResult process() {
                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
            }

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                return recipe.mSpecialValue <= getCoilLevel().getHeat() ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 400;
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
            Textures.BlockIcons.getCasingTextureForId(48),
            OVERLAY_FRONT_OIL_CRACKER,
            OVERLAY_FRONT_OIL_CRACKER_GLOW,
            OVERLAY_FRONT_OIL_CRACKER_ACTIVE,
            OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final TSTMultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.MegaDigester.tooltip.machine_type
        // # Digester
        // #zh_CN 煮解池
        tt.addMachineType(TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.machine_type"))
            .addMarkdown(new ResourceLocation("gregtech", "digester"))
            // #tr tst.common.machine.MegaDigester.tooltip.controller
            // # Controller block for the Mega Digester
            // #zh_CN 巨型煮解池的控制方块
            .addInfo(TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.controller"))
            // #tr tst.common.machine.MegaDigester.tooltip.info.02
            // # Performs {\LIGHT_PURPLE}Perfect Overclocking{\GRAY}.
            // #zh_CN 执行§d无损超频§7.
            .addInfo(TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.info.02"))
            // #tr tst.common.machine.MegaDigester.tooltip.info.03
            // # Initial speed is {\AQUA}100%{\GRAY}, each coil tier adds {\AQUA}100%{\GRAY} speed.
            // #zh_CN 初始速度为§b100%§7，每提升一级线圈等级增加§b100%§7速度.
            .addInfo(TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.info.03"))
            // #tr tst.common.machine.MegaDigester.tooltip.info.04
            // # Has near-infinite parallel!
            // #zh_CN 拥有近乎无限的并行！
            .addInfo(TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.info.04"))
            .addPerfectOCInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 4, 7, true)
            .addController(textFrontBottom)
            .addCasing("40-54", Casings.RobustTungstenSteelMachineCasing.getLocalizedName(), false)
            .addCasing("16", Casings.HeatProofMachineCasing.getLocalizedName(), false)
            .addCasing("16", TSTUtils.tr("GT5U.MBTT.Tiers.Coil"), false)
            .addCasing("9", Casings.CleanStainlessSteelMachineCasing.getLocalizedName(), false)
            // #tr tst.common.machine.MegaDigester.tooltip.structure.any_casing
            // # Any Robust Tungstensteel Casing
            // #zh_CN 任意钨钢机械方块
            .addEnergyHatch("1+", TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.structure.any_casing"), 1)
            .addMaintenanceHatch("1", TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.structure.any_casing"), 1)
            .addMufflerHatch("1", TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.structure.any_casing"), 1)
            .addInputAny("1+", TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.structure.any_casing"), 1)
            .addOutputAny("1+", TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.structure.any_casing"), 1)
            // #tr tst.common.machine.MegaDigester.tooltip.structure.air
            // # Interior and top of the structure
            // #zh_CN 结构内部与顶部
            .addAir(TSTUtils.tr("tst.common.machine.MegaDigester.tooltip.structure.air"))
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

}
