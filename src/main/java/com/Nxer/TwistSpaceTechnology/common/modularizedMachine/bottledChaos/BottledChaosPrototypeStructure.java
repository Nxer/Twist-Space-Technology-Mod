package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.OverclockController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.ParallelController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.PowerConsumptionController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.SpeedController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineBase.hatchElement;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineBase.threadElement;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.block.meta.BottledChaosCapacityBlock;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.ItemMachines;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

public final class BottledChaosPrototypeStructure extends BottledChaosStructure {

    public static final String PREVIEW_CHANNEL = "bottledchaosprototype";

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_CAPACITY_BASE_EXTENSION = "capacity_base_extension";
    private static final String STRUCTURE_PIECE_CAPACITY_EXTENSION = "capacity_extension";
    private static final String STRUCTURE_PIECE_CAPACITY_BASE_TOP = "capacity_base_top";
    private static final String STRUCTURE_PIECE_CAPACITY_TOP = "capacity_top";
    private static final int HORIZONTAL_OFFSET = 7;
    private static final int VERTICAL_OFFSET = 4;
    private static final int DEPTH_OFFSET = 0;
    private static final int CAPACITY_HORIZONTAL_OFFSET = 3;
    private static final int CAPACITY_DEPTH_OFFSET = -4;
    private static final int CAPACITY_SEGMENT_HEIGHT = 4;
    private static final int MAX_EXTRA_SEGMENTS = 3;

    // spotless:off
    // Closed cabinets and a steel gantry join the controller to the enclosed capacity column.
    private static final String[][] SHAPE = {
        {"              ","              ","     EEDEE    ","      EDE     ","      EDE     ","              ","              ","              ","              ","              ","              "},
        {"EEEEE     EEEE","EEEEEE   EEEEE","     REFER    ","      EFE     ","      EFE     ","     EIIIE    ","    AIIBIIA   ","    AIBFBIA   ","    AIIBIIA   ","     EIIIE    ","      AAA     "},
        {"EJJJE EEE EJJE","EDDDE EHE EDDE","     REDER    ","      EDE     ","      EDE     ","     EIIIE    ","    AIIBIIA   ","    AIBFBIA   ","    AIIBIIA   ","     EIIIE    ","      AAA     "},
        {"EKKKE AAA EKKE","EDDDE ACA EDDE","     RAAAR    ","              ","      AAA     ","     EIIIE    ","    AIIBIIA   ","    AIBFBIA   ","    AIIBIIA   ","     EIIIE    ","      AAA     "},
        {"ELLLE M~M ELLE","EDDDE MHM EDDE","     EMMME    ","              ","      EEE     ","     EDDDE    ","    EDDBDDE   ","    EDBFBDE   ","    EDDBDDE   ","     EDDDE    ","      EEE     "},
        {"GGGGGGGGGGGGGG","GGGGGGGFGGGGGG","GGGGGGGFGGGGGG","    GGGFGGG   ","    GGGFGGG   ","    GGGFGGG   ","    GGGFGGG   ","    GGGFGGG   ","    GGGGGGG   ","    GGGGGGG   ","    GGGGGGG   "},
    };
    private static final String[] CAPACITY_LAYER = {
        "  AAA  "," EIIIE ","AIIBIIA","AIBFBIA","AIIBIIA"," EIIIE ","  AAA  "
    };
    // The main piece owns the bridge's three blocks at the first capacity seam.
    private static final String[][] CAPACITY_BASE_EXTENSION = {
        CAPACITY_LAYER, CAPACITY_LAYER, CAPACITY_LAYER,
        {"       "," EDDDE ","EDDBDDE","EDBFBDE","EDDBDDE"," EDDDE ","  EEE  "}
    };
    private static final String[][] CAPACITY_EXTENSION = {
        CAPACITY_LAYER, CAPACITY_LAYER, CAPACITY_LAYER,
        {"  EEE  "," EDDDE ","EDDBDDE","EDBFBDE","EDDBDDE"," EDDDE ","  EEE  "}
    };
    private static final String[][] CAPACITY_BASE_TOP = {
        {"       "," EDDDE ","EDDDDDE","EDDHDDE","EDDDDDE"," EDDDE ","  EEE  "}
    };
    private static final String[][] CAPACITY_TOP = {
        {"  EEE  "," EDDDE ","EDDDDDE","EDDHDDE","EDDDDDE"," EDDDE ","  EEE  "}
    };
    // spotless:on

    private static IStructureDefinition<MM_BottledChaos> structureDefinition;
    private static MultiblockTooltipBuilder tooltip;
    private int extraCapacitySegments;

    public BottledChaosPrototypeStructure(MM_BottledChaos machine) {
        super(machine);
    }

    @Override
    public int getProcessingGroupSlot(int relativeLeft, int relativeBack) {
        if (relativeBack != 0) return -1;
        if (relativeLeft >= -6 && relativeLeft <= -4) return relativeLeft + 6;
        return relativeLeft >= 4 && relativeLeft <= 5 ? relativeLeft - 1 : -1;
    }

    @Override
    public int getProcessingGroupLimit() {
        return 5;
    }

    @Override
    public int getCredentialHatchLimit(int parallelTier) {
        return Math.min(getProcessingGroupLimit(), 3 + parallelTier);
    }

    @Override
    public long getMaximumRecipeVoltage() {
        return GTValues.V[VoltageIndex.UV];
    }

    @Override
    public boolean isWirelessMode() {
        return false;
    }

    @Override
    public boolean checkStructurePieces(List<StructureError> errors) {
        extraCapacitySegments = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET, errors)) return false;
        for (int extra = 0; extra <= MAX_EXTRA_SEGMENTS; extra++) {
            List<StructureError> topErrors = new ArrayList<>();
            int topOffset = VERTICAL_OFFSET + extra * CAPACITY_SEGMENT_HEIGHT;
            if (checkPiece(
                getCapacityTopPiece(extra),
                CAPACITY_HORIZONTAL_OFFSET,
                topOffset,
                CAPACITY_DEPTH_OFFSET,
                topErrors)) return true;
            if (extra == MAX_EXTRA_SEGMENTS) {
                errors.addAll(topErrors);
                return false;
            }
            if (!checkPiece(
                getCapacityExtensionPiece(extra),
                CAPACITY_HORIZONTAL_OFFSET,
                topOffset + CAPACITY_SEGMENT_HEIGHT - 1,
                CAPACITY_DEPTH_OFFSET,
                errors)) return false;
            extraCapacitySegments++;
        }
        return false;
    }

    private static String getCapacityExtensionPiece(int extra) {
        return extra == 0 ? STRUCTURE_PIECE_CAPACITY_BASE_EXTENSION : STRUCTURE_PIECE_CAPACITY_EXTENSION;
    }

    private static String getCapacityTopPiece(int extra) {
        return extra == 0 ? STRUCTURE_PIECE_CAPACITY_BASE_TOP : STRUCTURE_PIECE_CAPACITY_TOP;
    }

    @Override
    public int getCapacityRegionCount() {
        return 1 + extraCapacitySegments;
    }

    @Override
    public int getCapacityRegionBlockCount() {
        return 48;
    }

    @Override
    public int getCapacityLevelCount() {
        return 2;
    }

    @Override
    public int getCapacityRegion(int x, int y, int z) {
        return Math.floorDiv(
            y - machine.getBaseMetaTileEntity()
                .getYCoord() - 1,
            CAPACITY_SEGMENT_HEIGHT);
    }

    @Override
    public StructureError getInvalidCapacityRegionError() {
        return StructureErrors.of("tst.machine.BottledChaos.structure_error.invalid_capacity_region");
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
        int extensions = Math.min(MAX_EXTRA_SEGMENTS, Math.max(0, stackSize.stackSize - 1));
        for (int extra = 0; extra < extensions; extra++) {
            buildPiece(
                getCapacityExtensionPiece(extra),
                stackSize,
                hintsOnly,
                CAPACITY_HORIZONTAL_OFFSET,
                VERTICAL_OFFSET + (extra + 1) * CAPACITY_SEGMENT_HEIGHT - 1,
                CAPACITY_DEPTH_OFFSET);
        }
        buildPiece(
            getCapacityTopPiece(extensions),
            stackSize,
            hintsOnly,
            CAPACITY_HORIZONTAL_OFFSET,
            VERTICAL_OFFSET + extensions * CAPACITY_SEGMENT_HEIGHT,
            CAPACITY_DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment environment) {
        if (machine.mMachine) return -1;
        boolean preview = GregTechAPI.isDummyWorld(
            machine.getBaseMetaTileEntity()
                .getWorld());
        int built = survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            environment,
            preview,
            !preview);
        if (built >= 0) return built;
        int extensions = Math.min(MAX_EXTRA_SEGMENTS, Math.max(0, stackSize.stackSize - 1));
        for (int extra = 0; extra < extensions; extra++) {
            built = survivalBuildPiece(
                getCapacityExtensionPiece(extra),
                stackSize,
                CAPACITY_HORIZONTAL_OFFSET,
                VERTICAL_OFFSET + (extra + 1) * CAPACITY_SEGMENT_HEIGHT - 1,
                CAPACITY_DEPTH_OFFSET,
                elementBudget,
                environment,
                preview,
                !preview);
            if (built >= 0) return built;
        }
        return survivalBuildPiece(
            getCapacityTopPiece(extensions),
            stackSize,
            CAPACITY_HORIZONTAL_OFFSET,
            VERTICAL_OFFSET + extensions * CAPACITY_SEGMENT_HEIGHT,
            CAPACITY_DEPTH_OFFSET,
            elementBudget,
            environment,
            preview,
            !preview);
    }

    @Override
    public IStructureDefinition<MM_BottledChaos> getStructureDefinition() {
        if (structureDefinition == null) {
            structureDefinition = StructureDefinition.<MM_BottledChaos>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(SHAPE))
                .addShape(STRUCTURE_PIECE_CAPACITY_BASE_EXTENSION, transpose(CAPACITY_BASE_EXTENSION))
                .addShape(STRUCTURE_PIECE_CAPACITY_EXTENSION, transpose(CAPACITY_EXTENSION))
                .addShape(STRUCTURE_PIECE_CAPACITY_BASE_TOP, transpose(CAPACITY_BASE_TOP))
                .addShape(STRUCTURE_PIECE_CAPACITY_TOP, transpose(CAPACITY_TOP))
                .addElement('A', Casings.BorosilicateGlassAny.asElement())
                .addElement(
                    'B',
                    ofBlock(
                        Block.getBlockFromItem(
                            MaterialsElements.getInstance().GERMANIUM.getFrameBox(1)
                                .getItem()),
                        0))
                .addElement('C', ofBlock(compactFusionCoil, 0))
                .addElement('D', Casings.AdvancedIridiumPlatedMachineCasing.asElement())
                .addElement('E', Casings.RadiantNaquadahAlloyCasing.asElement())
                .addElement('F', Casings.HighPowerCasing.asElement())
                .addElement('G', ofBlock(GregTechAPI.sBlockCasingsDyson, 9))
                .addElement('H', ofBlock(TstBlocks.MetaBlockCasing02, 2))
                .addElement(
                    'I',
                    withChannel(
                        BottledChaosCapacityBlock.STRUCTURE_CHANNEL,
                        BottledChaosMachineBase.<MM_BottledChaos>capacityElement()))
                .addElement('J', threadElement(0, 3))
                .addElement('K', threadElement(1, 3))
                .addElement('L', threadElement(2, 3))
                .addElement('M', prototypeHatches())
                .addElement('R', ofFrame(Materials.Steel))
                .build();
        }
        return structureDefinition;
    }

    private static IStructureElement<MM_BottledChaos> prototypeHatches() {
        // Seven controller positions; the processing groups already provide dedicated inputs.
        Map<IHatchElement<? super MM_BottledChaos>, Integer> hatches = new LinkedHashMap<>();
        hatches.put(InputBus, 0);
        hatches.put(InputHatch, 0);
        hatches.put(OutputBus, 1);
        hatches.put(OutputHatch, 1);
        hatches.put(Energy.or(ExoticEnergy), 1);
        hatches.put(ParallelController, 1);
        hatches.put(SpeedController, 1);
        hatches.put(PowerConsumptionController, 1);
        hatches.put(OverclockController, 1);
        HatchElementBuilder<MM_BottledChaos> builder = HatchElementBuilder.<MM_BottledChaos>builder()
            .atLeast(hatches);
        var adder = builder.getAdder();
        return hatchElement(
            builder
                .adder(
                    (machine, tile, texture) -> tile != null && isSupportedHatch(tile.getMetaTileEntity())
                        && adder.apply(machine, tile, texture))
                .hatchItemFilterAnd(machine -> stack -> isSupportedHatch(ItemMachines.getMetaTileEntity(stack))),
            1);
    }

    private static boolean isSupportedHatch(IMetaTileEntity tile) {
        if (tile instanceof MTEHatchEnergyTunnel) return false;
        return !(tile instanceof MTEHatch hatch) || !(Energy.matchesHatch(tile) || ExoticEnergy.matchesHatch(tile))
            || hatch.maxWorkingAmperesIn() <= 64;
    }

    @Override
    public MultiblockTooltipBuilder getTooltip() {
        if (tooltip == null) {
            // spotless:off
            tooltip = new TSTMultiblockTooltipBuilder()
                .addMachineType(TSTUtils.tr("tst.machine.BottledChaos.tooltip.machine_type"))
                // #tr tst.machine.BottledChaosPrototype.tooltip.info.1
                // # Controller block for the Recipe Chain Validation Prototype
                // #zh_CN 配方链验证原型机的控制方块
                .addInfo(TSTUtils.tr("tst.machine.BottledChaosPrototype.tooltip.info.1"))
                // #tr tst.machine.BottledChaosPrototype.tooltip.info.2
                // # {\AQUA}{\ITALIC}A first step towards unattended production.
                // #zh_CN {\AQUA}{\ITALIC}让产线自行运转的第一步.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaosPrototype.tooltip.info.2"))
                .addInfo("")
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.4"))
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.5"))
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.6"))
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.7"))
                // #tr tst.machine.BottledChaosPrototype.tooltip.info.8
                // # Supports recipes up to UV.
                // #zh_CN 最高支持UV配方.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaosPrototype.tooltip.info.8"))
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.10"))
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.11"))
                .beginStructureBlock(14, 6, 11, false)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.ModularizedMachineSystemDescription01)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.ModularizedMachineSystemDescription02)
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.1"))
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.SpeedControllerDescription)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.PowerConsumptionControllerDescription)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.OverclockControllerDescription)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.NotMultiplyInstallSameTypeModuleAll)
                .addStructureInfo(TSTSharedLocalization.General.Text_SeparatingLine)
                // #tr tst.machine.BottledChaosPrototype.tooltip.structure.1
                // # Supports 3 credential hatches; ZPM/UV Parallel Controllers raise the limit to 4/5.
                // #zh_CN 默认支持3个凭证仓, ZPM/UV并行控制器将上限提升至4/5个.
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaosPrototype.tooltip.structure.1"))
                // #tr tst.machine.BottledChaosPrototype.tooltip.structure.2
                // # Supports energy hatches up to 64A; laser hatches are not supported.
                // #zh_CN 能源仓最高支持64A, 不支持激光仓.
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaosPrototype.tooltip.structure.2"))
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.2"))
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.5"))
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.6"))
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.7"))
                .addStructureInfo(TSTSharedLocalization.General.Text_SeparatingLine)
                .addOtherStructurePart(TSTSharedLocalization.ModularizedMachine.ModularHatch,
                    TSTSharedLocalization.Structure.getBlueprintWithDot(1), 1)
                .addOtherStructurePart(TSTUtils.tr("tst.machine.BottledChaosRecipeTokenHatch.name"),
                    TSTSharedLocalization.Structure.getBlueprintWithDot(2), 2)
                .addInputBus("", TSTSharedLocalization.Structure.getBlueprintWithDot(1, 3), 1, 3)
                .addInputHatch("", TSTSharedLocalization.Structure.getBlueprintWithDot(1, 3), 1, 3)
                .addOutputBus("", TSTSharedLocalization.Structure.getBlueprintWithDot(1), 1)
                .addOutputHatch("", TSTSharedLocalization.Structure.getBlueprintWithDot(1), 1)
                .addEnergyHatch("", TSTSharedLocalization.Structure.getBlueprintWithDot(1), 1)
                .addStructureHint(TSTSharedLocalization.ModularizedMachine.ModularHatchKey, 1)
                .toolTipFinisher();
            // spotless:on
        }
        return tooltip;
    }
}
