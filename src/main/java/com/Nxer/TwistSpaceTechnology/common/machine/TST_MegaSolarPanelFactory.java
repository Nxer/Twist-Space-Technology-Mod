package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing02;
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
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;

public class TST_MegaSolarPanelFactory extends GTCM_MultiMachineBase<TST_MegaSolarPanelFactory> {

    // region Class Constructor

    public TST_MegaSolarPanelFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName , aNameRegional);
    }

    public TST_MegaSolarPanelFactory(String aName) { super(aName); }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaSolarPanelFactory(mName);
    }

    //endregion

    //region Logic
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.solarFactoryRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
            return Collections.singleton(RecipeMaps.solarFactoryRecipes);
    }
    @Override
    protected boolean isEnablePerfectOverclock() { return true;}

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    //endregion

    //region Structure

    protected static final int horizontalOffSet = 10;
    protected static final int verticalOffSet = 7;
    protected static final int depthOffSet = 5;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static IStructureDefinition<TST_MegaSolarPanelFactory> STRUCTURE_DEFINITION;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
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
    public IStructureDefinition<TST_MegaSolarPanelFactory> getStructureDefinition() {
        if (null == STRUCTURE_DEFINITION) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaSolarPanelFactory>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                //A -> ofBlock...(BW_GlasBlocks2, 0, ...);
                //B -> ofBlock...(gt.blockcasings10, 11, ...);
                //C -> ofBlock...(gt.godforgecasing, 0, ...);
                //D -> ofBlock...(tile.MetaBlockCasing02, 2, ...);

                .addElement('A', chainAllGlasses())
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings10, 11))
                .addElement('C', ofBlock(GodforgeCasings, 0))
                .addElement(
                    'D',
                    HatchElementBuilder.<TST_MegaSolarPanelFactory>builder()
                        .atLeast(
                            InputBus,
                            OutputBus,
                            InputHatch,
                            OutputHatch,
                            Energy.or(ExoticEnergy))
                        .adder(TST_MegaSolarPanelFactory::addToMachineList)
                        .dot(1)
                        .casingIndex(1024)
                        .buildAndChain(MetaBlockCasing02, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // spotless:off
    protected static final String[][] shape = new String[][]{
        {"                     ", "                     ", "                     ", " B                 B ", " B                 B ", "BBBB             BBBB", "DDD               DDD", "BBBB             BBBB", " B                 B ", " B                 B ", "                     ", "                     ", "                     "},
        {"                     ", "                     ", "BBB               BBB", "                     ", "                     ", " B                 B ", "                     ", " B                 B ", "                     ", "                     ", "BBB               BBB", "                     ", "                     "},
        {"                     ", "BBB               BBB", "                     ", "                     ", "                     ", " B                 B ", "                     ", " B                 B ", "                     ", "                     ", "                     ", "BBB               BBB", "                     "},
        {" B                 B ", "                     ", "                     ", "                     ", " B                 B ", "                     ", "                     ", "                     ", " B                 B ", "                     ", "                     ", "                     ", " B                 B "},
        {" B                 B ", "                     ", "                     ", "                     ", " B                 B ", "                     ", "                     ", "                     ", " B                 B ", "                     ", "                     ", "                     ", " B                 B "},
        {"BBBB             BBBB", " B                 B ", "                     ", " B                 B ", "                     ", "       DDDDDDD       ", " CC     DDDDD     CC ", "       DDDDDDD       ", "                     ", " B                 B ", "                     ", " B                 B ", "BBBB             BBBB"},
        {"DDD               DDD", "                     ", "                     ", " B                 B ", "                     ", " CC    AAAAAAA     CC", " CCCCCC       CCCCCC ", " CC    AAAAAAA    CC ", "                     ", " B                 B ", "                     ", "                     ", "DDD               DDD"},
        {"BBBB             BBBB", " B                 B ", "                     ", " B                 B ", "                     ", "       DDD~DDD       ", " CC     DDDDD     CC ", "       DDDDDDD       ", "                     ", " B                 B ", "                     ", " B                 B ", "BBBB             BBBB"},
        {" B                 B ", "                     ", "                     ", "                     ", " B                 B ", "                     ", "                     ", "                     ", " B                 B ", "                     ", "                     ", "                     ", " B                 B "},
        {" B                 B ", "                     ", "                     ", "                     ", " B                 B ", "                     ", "                     ", "                     ", " B                 B ", "                     ", "                     ", "                     ", " B                 B "},
        {"                     ", "BBB               BBB", "                     ", "                     ", "                     ", " B                 B ", "                     ", " B                 B ", "                     ", "                     ", "                     ", "BBB               BBB", "                     "},
        {"                     ", "                     ", "BBB               BBB", "                     ", "                     ", " B                 B ", "                     ", " B                 B ", "                     ", "                     ", "BBB               BBB", "                     ", "                     "},
        {"                     ", "                     ", "                     ", " B                 B ", " B                 B ", "BBBB             BBBB", "DDD               DDD", "BBBB             BBBB", " B                 B ", " B                 B ", "                     ", "                     ", "                     "}
    };
    // spotless:on

    //region General

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side,
                                 ForgeDirection forgeDirection, int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == forgeDirection) {
            if (aActive) return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(181),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(181),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(181) };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tttt = new MultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_MegaSolarPanelFactory
        // # SolarPanelFactory
        // #zh_CN 太阳能板制作厂
        tttt.addMachineType(tr("Tooltip_MegaSolarPanelFactory_MachineType"))
            // #tr Tooltip_MegaSolarPanelFactory_1_00
            // # {\BOLD}bigger is better!
            // #zh_CN {\BOLD}大就是好！
            .addInfo(tr("Tooltip_MegaSolarPanelFactory_1_00"))
            // #tr Tooltip_MegaSolarPanelFactory_1_01
            // # {\LIGHT_PURPLE}Perfect overclocking{\GRAY},that's all
            // #zh_CN {\LIGHT_PURPLE}无损超频{\GRAY},仅仅如此.
            .addInfo(tr("Tooltip_MegaSolarPanelFactory_1_01"))
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(21, 13, 13, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        // spotless:on
        return tttt;
    }

    // endregion

}
