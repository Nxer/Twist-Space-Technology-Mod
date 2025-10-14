package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_IntegratedAssemblyMatrix.DataHatchElement.DataAccess;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTUtility.validMTEList;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.thing.block.BlockQuantumGlass;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

public class TST_IntegratedAssemblyMatrix extends GTCM_MultiMachineBase<TST_IntegratedAssemblyMatrix> {

    // region Class Constructor

    public TST_IntegratedAssemblyMatrix(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_IntegratedAssemblyMatrix(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_IntegratedAssemblyMatrix(mName);
    }

    // endregion

    // region Logic
    protected final Collection<TST_ItemID> allowedRecipes = new HashSet<>();
    protected int cachedData;
    protected long maxVoltage;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {

                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.mOutputs == null) return CheckRecipeResultRegistry.NO_RECIPE;
                if (!allowedRecipes.contains(TST_ItemID.create(recipe.mOutputs[0]))) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }

                if (recipe.mEUt > maxVoltage) return CheckRecipeResultRegistry.insufficientVoltage(recipe.mEUt);

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {

        // check data
        if (!flushData()) return CheckRecipeResultRegistry.NO_DATA_STICKS;

        // general processing
        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);
        // inputs are consumed at this point
        updateSlots();
        if (!result.wasSuccessful()) return result;

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = processingLogic.getDuration();
        setEnergyUsage(processingLogic);

        mOutputItems = processingLogic.getOutputItems();
        mOutputFluids = processingLogic.getOutputFluids();

        return result;
    }

    public boolean flushData() {
        if (dataAccessHatches.isEmpty()) {
            allowedRecipes.clear();
            return false;
        }

        List<GTRecipe.RecipeAssemblyLine> fromHatch = null;
        for (MTEHatchDataAccess dataAccess : validMTEList(dataAccessHatches)) {
            fromHatch = dataAccess.getAssemblyLineRecipes();
        }

        if (fromHatch == null) return false;

        int newHash = fromHatch.hashCode();
        if (newHash == cachedData) return true;

        // or data changed

        cachedData = newHash;
        allowedRecipes.clear();
        for (GTRecipe.RecipeAssemblyLine r : fromHatch) {
            allowedRecipes.add(TST_ItemID.create(r.mOutput));
        }

        return !allowedRecipes.isEmpty();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.AssemblyLineWithoutResearchRecipe;
    }

    // endregion

    // region Structure

    protected static final int horizontalOffSet = 4;
    protected static final int verticalOffSet = 4;
    protected static final int depthOffSet = 0;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static IStructureDefinition<TST_IntegratedAssemblyMatrix> STRUCTURE_DEFINITION;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;

        // get voltage limitation
        checkEnergyLimitation();
        speedBonus = (float) Config.TimeCostMultiplier_IntegratedAssemblyMatrix;
        maxParallel = Config.Parallel_IntegratedAssemblyMatrix;

        return dataAccessHatches.size() == 1;
    }

    public void checkEnergyLimitation() {
        int tier = 0;
        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
            tier = Math.max(tHatch.mTier, tier);
        }
        for (MTEHatch tHatch : validMTEList(mExoticEnergyHatches)) {
            tier = Math.max(tHatch.mTier, tier);
        }
        maxVoltage = GTValues.V[tier];
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(
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
    public IStructureDefinition<TST_IntegratedAssemblyMatrix> getStructureDefinition() {
        if (null == STRUCTURE_DEFINITION) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_IntegratedAssemblyMatrix>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                // A -> ofBlock...(BW_GlasBlocks, 14, ...);
                // B -> ofBlock...(gt.blockcasings2, 5, ...);
                // C -> ofBlock...(gt.blockcasings2, 9, ...);
                // D -> ofBlock...(gt.blockcasingsSE, 0, ...);
                // E -> ofBlock...(gt.blockcasingsTT, 0, ...);
                // F -> ofBlock...(gt.blockcasingsTT, 4, ...);
                // G -> ofBlock...(gt.blockcasingsTT, 6, ...);
                // H -> ofBlock...(tile.quantumGlass, 0, ...);
                .addElement('A', chainAllGlasses())
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 5))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 9))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasingsSE, 0))
                .addElement(
                    'E',
                    HatchElementBuilder.<TST_IntegratedAssemblyMatrix>builder()
                        .atLeast(DataAccess, InputBus, OutputBus, InputHatch, OutputHatch, Energy.or(ExoticEnergy))
                        .adder(TST_IntegratedAssemblyMatrix::addToMachineList)
                        .dot(1)
                        .casingIndex(1024)
                        .buildAndChain(sBlockCasingsTT, 0))
                .addElement('F', ofBlock(sBlockCasingsTT, 4))
                .addElement('G', ofBlock(sBlockCasingsTT, 6))
                .addElement('H', ofBlock(BlockQuantumGlass.INSTANCE, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // spotless:off
    protected static final String[][] shape = new String[][]{
        {"         ","         ","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FFFFFFFFF"},
        {"F       F","F       F","F       F"," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," DDDDDDD ","         "},
        {"F       F","         ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HCEEECH ","         ","         "},
        {"FE FFF EF","    F    ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," HEEEEEH ","         ","         "},
        {"FEEE~EEEF","   FGF   ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," H BGB H ","   AGA   "," H BGB H "," HEEEEEH ","         ","         "},
        {"FE FFF EF","    F    ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," H  B  H ","    A    "," H  B  H "," HEEEEEH ","         ","         "},
        {"F       F","         ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HC   CH ","         "," HC   CH "," HCEEECH ","         ","         "},
        {"F       F","F       F","F       F"," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," D     D "," DHHHHHD "," DHHHHHD "," DDDDDDD ","         "},
        {"         ","         ","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FE     EF","FFFFFFFFF"}
    };
    // spotless:on

    // endregion

    // region General

    protected static Textures.BlockIcons.CustomIcon ActiveFace;
    protected static Textures.BlockIcons.CustomIcon InactiveFace;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ActiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Adv_on");
        InactiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Adv_off");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[8][12],
                new TTRenderedExtendedFacingTexture(active ? ActiveFace : InactiveFace) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[8][12] };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tttt = new MultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_IntegratedAssemblyMatrix_MachineType
        // # Assembly Line
        // #zh_CN 装配线
        tttt.addMachineType(tr("Tooltip_IntegratedAssemblyMatrix_MachineType"))
            // #tr Tooltip_IntegratedAssemblyMatrix_1_00
            // # {\GOLD}{\BOLD}Imagination can take you anywhere
            // #zh_CN {\GOLD}{\BOLD}想象力能带你去任何地方
            .addInfo(tr("Tooltip_IntegratedAssemblyMatrix_1_00"))
            // #tr Tooltip_IntegratedAssemblyMatrix_1_01
            // # The built-in integrated logistics system allows you to no longer worry about external logistics design.
            // #zh_CN 内置集成物流系统允许你不再为外部物流设计发愁.
            .addInfo(tr("Tooltip_IntegratedAssemblyMatrix_1_01"))
            // #tr Tooltip_IntegratedAssemblyMatrix_1_02
            // # Spatial isolation technology brings higher batch production capabilities.
            // #zh_CN 空间隔离技术则带来更高的批量生产能力.
            .addInfo(tr("Tooltip_IntegratedAssemblyMatrix_1_02"))
            // #tr Tooltip_IntegratedAssemblyMatrix_1_03
            // # The cost is {\RED}{\BOLD}4{\RESET}{\GRAY} times the time.
            // #zh_CN 代价是{\RED}{\BOLD}4{\RESET}{\GRAY}倍耗时.
            .addInfo(tr("Tooltip_IntegratedAssemblyMatrix_1_03"))
            // #tr Tooltip_IntegratedAssemblyMatrix_1_04
            // # Still need to use the data access hatch to provide data stick.
            // #zh_CN 仍然需要使用数据访问仓提供闪存.
            .addInfo(tr("Tooltip_IntegratedAssemblyMatrix_1_04"))
            // #tr Tooltip_IntegratedAssemblyMatrix_1_05
            // # The energy hatch tier limits the voltage tier at which recipes can be executed.
            // #zh_CN 能源仓等级限制了可执行配方电压等级.
            .addInfo(tr("Tooltip_IntegratedAssemblyMatrix_1_05"))
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(15, 16, 3, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.data_access_hatch"),
                TextLocalization.textUseBlueprint,
                1)
            // #tr Tooltips_IntegratedAssemblyMatrix_DataHatchLimit
            // # Only allow {\GOLD}1{\GRAY} Data Access Hatch
            // #zh_CN 只允许安装{\GOLD}1{\GRAY}个数据访问仓
            .addStructureInfo(tr("Tooltips_IntegratedAssemblyMatrix_DataHatchLimit"))
            .toolTipFinisher(TextLocalization.ModName);
        // spotless:on
        return tttt;
    }

    // endregion

    // region Data Hatch

    public final List<MTEHatchDataAccess> dataAccessHatches = new ArrayList<>();

    protected enum DataHatchElement implements IHatchElement<TST_IntegratedAssemblyMatrix> {

        DataAccess;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchDataAccess.class);
        }

        @Override
        public IGTHatchAdder<TST_IntegratedAssemblyMatrix> adder() {
            return TST_IntegratedAssemblyMatrix::addDataAccessToMachineList;
        }

        @Override
        public long count(TST_IntegratedAssemblyMatrix t) {
            return t.dataAccessHatches.size();
        }
    }

    public boolean addDataAccessToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchDataAccess) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return dataAccessHatches.add((MTEHatchDataAccess) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addDataAccessToMachineList(aTileEntity, aBaseCasingIndex)
            || super.addToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        dataAccessHatches.clear();
    }
    // endregion

}
