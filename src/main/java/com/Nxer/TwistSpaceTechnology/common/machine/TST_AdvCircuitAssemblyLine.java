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
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

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
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
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
    private static final String STRUCTURE_PIECE_MIDDLE = "middleAdvCAL";
    private static final String STRUCTURE_PIECE_MIDDLE_HINT = "middleHintAdvCAL";
    private static final String STRUCTURE_PIECE_END = "endAdvCAL";
    // spotless:off
    private static final String[][] shapeMain =new String[][]{
        {" ","E"," "},
        {"~","C","g"},
        {"A","B","A"},
        {"F","D","F"}
    };

    private static final String[][] shapeMiddle =new String[][]{
        {" ","E"," "},
        {"G","C","G"},
        {"A","B","A"},
        {"F","D","F"}
    };

    private static final String[][] shapeMiddleHint =new String[][]{
        {" ","E"," "},
        {"G","C","G"},
        {"A","B","A"},
        {"F","d","F"}
    };

    private static final String[][] shapeEnd =new String[][]{
        {" ","E"," "},
        {"G","C","G"},
        {"A","B","A"},
        {"F","H","F"}
    };
    // spotless:on
    private static final IStructureDefinition<TST_AdvCircuitAssemblyLine> STRUCTURE_DEFINITION = StructureDefinition
        .<TST_AdvCircuitAssemblyLine>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
        .addShape(STRUCTURE_PIECE_MIDDLE, transpose(shapeMiddle))
        .addShape(STRUCTURE_PIECE_MIDDLE_HINT, transpose(shapeMiddleHint))
        .addShape(STRUCTURE_PIECE_END, transpose(shapeEnd))
        .addElement('A', ofGlassTieredMixed((byte) 4, (byte) 127, 5))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 5))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 9))
        .addElement('d', InputBus.newAny(16, 3, ForgeDirection.DOWN))
        .addElement(
            'D',
            buildHatchAdder(TST_AdvCircuitAssemblyLine.class).atLeast(InputBus, OutputBus)
                .casingIndex(16)
                .dot(2)
                .disallowOnly(ForgeDirection.EAST, ForgeDirection.WEST)
                .buildAndChain(GregTechAPI.sBlockCasings2, 0))
        .addElement(
            'E',
            buildHatchAdder(TST_AdvCircuitAssemblyLine.class).atLeast(Energy.or(ExoticEnergy))
                .casingIndex(16)
                .dot(1)
                .buildAndChain(GregTechAPI.sBlockCasings2, 6))
        .addElement(
            'F',
            buildHatchAdder(TST_AdvCircuitAssemblyLine.class).atLeast(InputHatch, Maintenance)
                .casingIndex(16)
                .dot(3)
                .disallowOnly(ForgeDirection.EAST, ForgeDirection.WEST)
                .buildAndChain(GregTechAPI.sBlockCasings2, 0))
        .addElement('g', ofBlock(GregTechAPI.sBlockCasings3, 10))
        .addElement(
            'G',
            buildHatchAdder(TST_AdvCircuitAssemblyLine.class).atLeast(CircuitImprintHatchElement.CircuitAccess)
                .dot(5)
                .casingIndex(42)
                .allowOnly(ForgeDirection.NORTH)
                .buildAndChain(GregTechAPI.sBlockCasings3, 10))
        .addElement('H', OutputBus.newAny(16, 4, ForgeDirection.DOWN))
        .build();

    @Override
    public IStructureDefinition<TST_AdvCircuitAssemblyLine> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int built;
        built = survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet,
            elementBudget,
            env,
            false,
            true);
        if (built >= 0) return built;
        int tLength = Math.min(stackSize.stackSize + 1, 7);

        for (int i = 1; i < tLength - 1; ++i) {
            built = survivialBuildPiece(
                STRUCTURE_PIECE_MIDDLE_HINT,
                stackSize,
                baseHorizontalOffSet - i,
                baseVerticalOffSet,
                baseDepthOffSet,
                elementBudget,
                env,
                false,
                true);
            if (built >= 0) return built;
        }
        return survivialBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            baseHorizontalOffSet - (tLength - 1),
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
        int layer = 1;
        for (; layer < Math.min(6, stackSize.stackSize + 1); layer++) {
            buildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                hintsOnly,
                baseHorizontalOffSet - layer,
                baseVerticalOffSet,
                baseDepthOffSet);
        }
        buildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            hintsOnly,
            baseHorizontalOffSet - layer,
            baseVerticalOffSet,
            baseDepthOffSet);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        mCircuitImprintHatches.clear();
        this.length = 1;

        // check the main layer.
        if (!checkPiece(STRUCTURE_PIECE_MAIN, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet)) {
            return false;
        }

        // check the middle layer
        while (checkPiece(
            STRUCTURE_PIECE_MIDDLE_HINT,
            baseHorizontalOffSet - this.length,
            baseVerticalOffSet,
            baseDepthOffSet)) {
            this.length++;
            if (length > 7) {
                return false;
            }
        }

        // check the end layer
        boolean signal = checkPiece(
            STRUCTURE_PIECE_END,
            baseHorizontalOffSet - this.length,
            baseVerticalOffSet,
            baseDepthOffSet);

        this.length++;
        // only one imprint circuit hatch allowed
        if (mCircuitImprintHatches.size() > 1) return false;

        // only one and 64a exotic energy hatch allowed
        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) return false;
            if (mExoticEnergyHatches.size() > 1) return false;
            return mExoticEnergyHatches.get(0)
                .maxWorkingAmperesIn() <= 64;
        }

        return signal;
    }

    // endregion

    // region Processing Logic
    private static ItemStack circuitImprint;
    int length = 1;
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
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 16;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // return BartWorksRecipeMaps.circuitAssemblyLineRecipes;
        return GTCMRecipe.advCircuitAssemblyLineRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(BartWorksRecipeMaps.circuitAssemblyLineRecipes);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("length", length);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        length = aNBT.getInteger("length");
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
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (MTEHatchInputBus tHatch : validMTEList(mInputBusses)) {
            tHatch.mRecipeMap = this.getRecipeMap();
            for (int i = 0; i < tHatch.getBaseMetaTileEntity()
                .getSizeInventory(); i++) {
                if (tHatch.getBaseMetaTileEntity()
                    .getStackInSlot(i) != null) {
                    rList.add(
                        tHatch.getBaseMetaTileEntity()
                            .getStackInSlot(i));
                    break;
                }
            }
        }
        return rList;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (recipe.mEUt > TST_AdvCircuitAssemblyLine.this.getMaxInputVoltage()) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }

                NBTTagCompound outputTag = CircuitImprintLoader.getTagFromStack(recipe.mOutputs[0]);

                // Check controller
                ItemStack controllerStack = TST_AdvCircuitAssemblyLine.this.getControllerSlot();
                if (controllerStack != null) {
                    if (controllerStack.isItemEqual(circuitImprint) && controllerStack.stackTagCompound == outputTag) {
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
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
        // # Circuit Assembler
        // #zh_CN 电路组装机
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
            // # Install imprint circuit hatch for more recipe support
            // #zh_CN 安装压印电路仓以获得更多配方支持
            .addInfo(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.3"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addEnergyHatch(textUseBlueprint, 1)
            .addInputBus(textUseBlueprint, 2)
            .addInputHatch(textUseBlueprint, 3)
            .addOutputBus(textEndSides, 2)
            // #tr Tooltip_AdvCircuitAssemblyLine.4
            // # Imprint circuit hatch
            // #zh_CN 压印电路仓
            // #tr Tooltip_AdvCircuitAssemblyLine.5
            // # Any grate machine casing
            // #zh_CN 任意格栅机械方块
            .addOtherStructurePart(
                TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.4"),
                TextEnums.tr("Tooltip_AdvCircuitAssemblyLine.5"),
                5)
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
