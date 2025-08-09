package com.Nxer.TwistSpaceTechnology.common.machine;

import static bartworks.util.BWUtil.ofGlassTieredMixed;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textEndSides;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_CircuitImprintHatch;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.system.material.CircuitGeneration.BWMetaItems;
import bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;

public class TST_AdvCircuitAssemblyLine extends GTCM_MultiMachineBase<TST_AdvCircuitAssemblyLine> {

    // region Class Constructor
    public TST_AdvCircuitAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_AdvCircuitAssemblyLine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_AdvCircuitAssemblyLine(this.mName);
    }

    // endregion

    // region Structure
    private static final int baseHorizontalOffSet = 0;
    private static final int baseVerticalOffSet = 1;
    private static final int baseDepthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainAdvCAL";
    // spotless:off
    private static final String[][] shapeMain = new String[][]{
        {"       ","DDDDDDD","       "},
        {"~GFFFFF","EEEEEEE","FFFFFFF"},
        {"AAAAAAA","CCCCCCC","AAAAAAA"},
        {"BBBBBBB","BBBBBBB","BBBBBBB"}
    };

    // spotless:on
    private static IStructureDefinition<TST_AdvCircuitAssemblyLine> STRUCTURE_DEFINITION;

    @Override
    public IStructureDefinition<TST_AdvCircuitAssemblyLine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_AdvCircuitAssemblyLine>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement('A', ofGlassTieredMixed((byte) 4, (byte) 127, 5))
                .addElement(
                    'B',
                    buildHatchAdder(TST_AdvCircuitAssemblyLine.class).atLeast(InputBus, OutputBus, InputHatch)
                        .adder(TST_AdvCircuitAssemblyLine::addToMachineList)
                        .casingIndex(16)
                        .dot(1)
                        .buildAndChain(GregTechAPI.sBlockCasings2, 0))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 5))
                .addElement(
                    'D',
                    buildHatchAdder(TST_AdvCircuitAssemblyLine.class).atLeast(Energy.or(ExoticEnergy))
                        .casingIndex(16)
                        .dot(2)
                        .buildAndChain(GregTechAPI.sBlockCasings2, 6))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings2, 9))
                .addElement('F', ofBlock(GregTechAPI.sBlockCasings3, 10))
                .addElement(
                    'G',
                    buildHatchAdder(TST_AdvCircuitAssemblyLine.class).atLeast(CircuitImprintHatchElement.CircuitAccess)
                        .dot(3)
                        .casingIndex(42)
                        .buildAndChain(GregTechAPI.sBlockCasings3, 10))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        mCircuitImprintHatches.clear();
        maxVoltageAllow = 0;

        // check the main layer.
        if (!checkPiece(STRUCTURE_PIECE_MAIN, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet)) {
            return false;
        }

        // only one imprint circuit hatch allowed
        if (mCircuitImprintHatches.size() > 1) return false;

        // only one and 64a exotic energy hatch allowed
        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) return false;
            if (mExoticEnergyHatches.size() > 1) return false;
            if (mExoticEnergyHatches.get(0)
                .maxWorkingAmperesIn() > 64) return false;

        } else if (mEnergyHatches.size() > 1) {
            return false;
        }

        maxVoltageAllow = getMaxInputVoltage();

        return maxVoltageAllow > 0;
    }

    // endregion

    // region Processing Logic
    private static ItemStack circuitImprint;
    protected long maxVoltageAllow = 0;
    ArrayList<TST_CircuitImprintHatch> mCircuitImprintHatches = new ArrayList<>();
    HashSet<NBTTagCompound> circuitType = new HashSet<>();

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (circuitImprint == null) circuitImprint = BWMetaItems.getCircuitParts()
            .getStack(0, 0);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 16;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.advCircuitAssemblyLineRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(BartWorksRecipeMaps.circuitAssemblyLineRecipes, GTCMRecipe.advCircuitAssemblyLineRecipes);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("maxVoltageAllow", maxVoltageAllow);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        maxVoltageAllow = aNBT.getLong("maxVoltageAllow");
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addCircuitImprintHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof TST_CircuitImprintHatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mCircuitImprintHatches.add((TST_CircuitImprintHatch) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (recipe.mEUt > maxVoltageAllow) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                }

                NBTTagCompound outputTag = CircuitImprintLoader.getTagFromStack(recipe.mOutputs[0]);

                // Check controller
                ItemStack controllerStack = TST_AdvCircuitAssemblyLine.this.getControllerSlot();
                if (controllerStack != null && controllerStack.isItemEqual(circuitImprint)
                    && controllerStack.stackTagCompound.equals(outputTag)) {
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }

                // Check imprint hatch
                mCircuitImprintHatches.get(0)
                    .refreshImprint();
                circuitType = mCircuitImprintHatches.get(0)
                    .getStoredCircuitImprints();
                if (circuitType.contains(outputTag)) return CheckRecipeResultRegistry.SUCCESSFUL;
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

        }.enablePerfectOverclock()
            .setMaxParallelSupplier(this::getMaxParallelRecipes);

    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        for (TST_CircuitImprintHatch hatch_circuitImprint : mCircuitImprintHatches) {
            hatch_circuitImprint.setActive(true);
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(16), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(16), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(16) };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_AdvCircuitAssemblyLine_MachineType
        // # Circuit Assembly Line
        // #zh_CN 电路装配线
        tt.addMachineType(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine_MachineType"))
            // #tr Tooltip_AdvCircuitAssemblyLine_Controller
            // # Controller block for the Advanced Circuit Assembly Line
            // #zh_CN 进阶电路装配线的控制方块
            .addInfo(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine_Controller"))
            // #tr Tooltip_AdvCircuitAssemblyLine.1
            // # {\AQUA}Crystal Circuit Ti Super OC Crafting not D version
            // #zh_CN {\AQUA}晶体电路板Ti Super OC Crafting not D version
            .addInfo(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.1"))
            // #tr Tooltip_AdvCircuitAssemblyLine.2
            // # Circuit assembly line with 64 times overclocking
            // #zh_CN 拥有64倍超频上限的电路装配线
            .addInfo(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.2"))
            // #tr Tooltip_AdvCircuitAssemblyLine.3
            // # Allows installation of one energy hatch with max 64 amp limitation
            // #zh_CN 允许安装一个能源仓, 最高64A电流
            .addInfo(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.3"))
            // #tr Tooltip_AdvCircuitAssemblyLine.4
            // # Allows installation of crafting input buffer
            // #zh_CN 允许使用样板输入总成
            .addInfo(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.4"))
            // #tr Tooltip_AdvCircuitAssemblyLine.5
            // # Install imprint circuit hatch for more recipe support (more than one hatch is not allowed)
            // #zh_CN 安装压印电路仓以获得更多配方支持 (只允许安装一个压印电路仓)
            .addInfo(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.5"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addEnergyHatch(textUseBlueprint, 2)
            .addInputBus(textUseBlueprint, 1)
            .addInputHatch(textUseBlueprint, 1)
            .addOutputBus(textEndSides, 1)
            // #tr Tooltip_AdvCircuitAssemblyLine.6
            // # Imprint circuit hatch
            // #zh_CN 压印电路仓
            // #tr Tooltip_AdvCircuitAssemblyLine.7
            // # Grate machine casing next to the controller
            // #zh_CN 主机旁的格栅机械方块
            .addOtherStructurePart(
                TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.6"),
                TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.7"),
                3)
            .addStructureInfo(Text_SeparatingLine)
            .toolTipFinisher(ModName);
        return tt;
    }

    private enum CircuitImprintHatchElement implements IHatchElement<TST_AdvCircuitAssemblyLine> {

        CircuitAccess;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(TST_CircuitImprintHatch.class);
        }

        @Override
        public IGTHatchAdder<TST_AdvCircuitAssemblyLine> adder() {
            return TST_AdvCircuitAssemblyLine::addCircuitImprintHatchToMachineList;
        }

        @Override
        public long count(TST_AdvCircuitAssemblyLine t) {
            return t.mCircuitImprintHatches.size();
        }
    }
}
