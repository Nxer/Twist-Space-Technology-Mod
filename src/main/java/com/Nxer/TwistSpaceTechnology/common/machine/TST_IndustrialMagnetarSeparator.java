package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EuModifier_IndustrialMagnetarSeparator;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.ParallelMultiply_IndustrialMagnetarSeparator;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBouns_IndustrialMagnetarSeparator;
import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.multi_Amp_hatch_incompatible;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textAnyCasing;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textFrontCenter;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

@SkipGenerateDescription
public class TST_IndustrialMagnetarSeparator extends GTCM_MultiMachineBase<TST_IndustrialMagnetarSeparator> {

    // region Class Constructor
    public TST_IndustrialMagnetarSeparator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.GODERIUM);
    }

    public TST_IndustrialMagnetarSeparator(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_IndustrialMagnetarSeparator(this.mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "STRUCTURE_PIECE_MAIN";
    private final int horizontalOffSet = 1, verticalOffSet = 1, depthOffSet = 0;
    private static IStructureDefinition<TST_IndustrialMagnetarSeparator> STRUCTURE_DEFINITION = null;

    // spotless:off
    protected final String[][] STRUCTURE = new String[][] {
        { "CCC", "CCC", "CCC" },
        { "C~C", "C-C", "CCC" },
        { "CCC", "CCC", "CCC" } };
    // spotless:on

    @Override
    public IStructureDefinition<TST_IndustrialMagnetarSeparator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_IndustrialMagnetarSeparator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(STRUCTURE))
                .addElement(
                    'C',
                    HatchElementBuilder.<TST_IndustrialMagnetarSeparator>builder()
                        .atLeast(InputBus, OutputBus, Maintenance, Muffler, Energy)
                        .adder(TST_IndustrialMagnetarSeparator::addToMachineList)
                        .hint(1)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(8))
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(TstBlocks.MetaBlockCasing01, 8))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mCasing = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) return;
        checkCasingMin(errors, mCasing, 9);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        if (!mExoticEnergyHatches.isEmpty()) {
            errors.add(multi_Amp_hatch_incompatible);
        }
    }
    // endregion

    // region Processing Logic
    int mCasing = 0;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.electroMagneticSeparatorRecipes;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    public int getMaxParallelRecipes() {
        return ParallelMultiply_IndustrialMagnetarSeparator * GTUtility.getTier(this.getMaxInputVoltage());
    }

    @Override
    protected float getEuModifier() {
        return EuModifier_IndustrialMagnetarSeparator;
    }

    @Override
    protected float getSpeedBonus() {
        return SpeedBouns_IndustrialMagnetarSeparator;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return 300;
    }

    public boolean checkHatch() {
        return mMaintenanceHatches.size() <= 1 && (this.getPollutionPerSecond(null) <= 0 || !mMufflerHatches.isEmpty())
            && mExoticEnergyHatches.isEmpty();
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        ITexture base = casingTexturePages[115][MetaBlockCasing01.getTextureIndexInPage(8)];
        if (side == facing) {
            if (aActive) return new ITexture[] { base, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { base, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { base };
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.IndustrialMagnetarSeparator.tooltip.machine_type
        // # Electromagnetic Separator
        // #zh_CN 电磁离析机
        tt.addMachineType(TSTUtils.tr("tst.common.machine.IndustrialMagnetarSeparator.tooltip.machine_type"))
            // #tr tst.common.machine.IndustrialMagnetarSeparator.tooltip.controller
            // # Controller block for the Industrial Magnetar Separator
            // #zh_CN 工业电磁离析机的控制方块
            .addInfo(TSTUtils.tr("tst.common.machine.IndustrialMagnetarSeparator.tooltip.controller"))
            // #tr tst.common.machine.IndustrialMagnetarSeparator.tooltip.info.01
            // # 300%% faster than using single block machines of the same voltage
            // #zh_CN 比相同电压的单方块机器快300%%
            .addInfo(TSTUtils.tr("tst.common.machine.IndustrialMagnetarSeparator.tooltip.info.01"))
            // #tr tst.common.machine.IndustrialMagnetarSeparator.tooltip.info.02
            // # Only uses 80%% of the EU/t normally required
            // #zh_CN 只需要使用配方要求功率的80%%
            .addInfo(TSTUtils.tr("tst.common.machine.IndustrialMagnetarSeparator.tooltip.info.02"))
            // #tr tst.common.machine.IndustrialMagnetarSeparator.tooltip.info.03
            // # Processes 4 items per voltage tier
            // #zh_CN 每提升一个电压等级，每次运行可以多处理4个物品
            .addInfo(TSTUtils.tr("tst.common.machine.IndustrialMagnetarSeparator.tooltip.info.03"))
            .addPollutionAmount(300)
            .addController(textFrontCenter)
            // #tr tst.common.machine.IndustrialMagnetarSeparator.tooltip.structure.01
            // # §69x §7Anti-Magnetic Casing (minimum)
            // #zh_CN 剩余方块为§7抗磁机械方块§r(至少§69§r个！)
            .addStructureInfo(TSTUtils.tr("tst.common.machine.IndustrialMagnetarSeparator.tooltip.structure.01"))
            .addInputBus(textAnyCasing, 1)
            .addOutputBus(textAnyCasing, 1)
            .addEnergyHatch(textAnyCasing, 1)
            .addMaintenanceHatch(textAnyCasing, 1)
            .addMufflerHatch(textAnyCasing,1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

}
