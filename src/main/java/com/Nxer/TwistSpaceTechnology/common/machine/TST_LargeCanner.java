package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

public class TST_LargeCanner extends GTCM_MultiMachineBase<TST_LargeCanner> {

    // region Constructor
    public TST_LargeCanner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_LargeCanner(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_LargeCanner(this.mName);
    }
    // region end

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "LargeCanner_main";
    private static final int horizontalOffSet = 6;
    private static final int verticalOffSet = 15;
    private static final int depthOffSet = 0;
    private static IStructureDefinition<TST_LargeCanner> STRUCTURE_DEFINITION = null;

    // spotless:off
    private final String[][] shapeMain = new String[][]{
        {"    BBBBB    ","  BBCCCCCBB  "," BBCCCCCCCBB "," BCCCCCCCCCB ","BCCCCCCCCCCCB","BCCCCCCCCCCCB","BCCCCCCCCCCCB","BCCCCCCCCCCCB","BCCCCCCCCCCCB"," BCCCCCCCCCB "," BBCCCCCCCBB ","  BBCCCCCBB  ","    BBBBB    "},
        {"    BBBBB    ","  BB  G  BB  "," BB   G   BB "," B    G    B ","B     G     B","B     G     B","BGGGGG GGGGGB","B     G     B","B     G     B"," B    G    B "," BB   G   BB ","  BB  G  BB  ","    BBBBB    "},
        {"    BBBBB    ","  BB     BB  "," BB       BB "," B         B ","B           B","B     G     B","B    G G    B","B     G     B","B           B"," B         B "," BB       BB ","  BB     BB  ","    BBBBB    "},
        {"    KKKKK    ","  KK     KK  "," KK       KK "," K         K ","K           K","K     G     K","K    G G    K","K     G     K","K           K"," K         K "," KK       KK ","  KK     KK  ","    KKKKK    "},
        {"             ","   K     K   ","             "," K         K ","             ","      G      ","     G G     ","      G      ","             "," K         K ","             ","   K     K   ","             "},
        {"             ","   K     K   ","             "," K         K ","             ","      G      ","     G G     ","      G      ","             "," K         K ","             ","   K     K   ","             "},
        {"             ","   K     K   ","             "," K         K ","             ","     RRR     ","     R R     ","     RRR     ","             "," K         K ","             ","   K     K   ","             "},
        {"             ","   K     K   ","             "," K         K ","     RRR     ","    R   R    ","    R   R    ","    R   R    ","     RRR     "," K         K ","             ","   K     K   ","             "},
        {"    KKKKK    ","  KK     KK  "," K         K "," K         K ","K    RRR    K","K   R   R   K","K   R   R   K","K   R   R   K","K    RRR    K"," K         K "," K         K ","  KK     KK  ","    KKKKK    "},
        {"             ","   K     K   ","             "," K         K ","     RRR     ","    R   R    ","    R   R    ","    R   R    ","     RRR     "," K         K ","             ","   K     K   ","             "},
        {"             ","   K     K   ","             "," K         K ","             ","     RRR     ","     R R     ","     RRR     ","             "," K         K ","             ","   K     K   ","             "},
        {"             ","   K     K   ","             "," K         K ","             ","      G      ","     G G     ","      G      ","             "," K         K ","             ","   K     K   ","             "},
        {"             ","   K     K   ","             "," K         K ","             ","      G      ","     G G     ","      G      ","             "," K         K ","             ","   K     K   ","             "},
        {"    BBBBB    ","  BB     BB  "," BB       BB "," B         B ","B           B","B     G     B","B    G G    B","B     G     B","B           B"," B         B "," BB       BB ","  BB     BB  ","    BBBBB    "},
        {"    BBBBB    ","  BB     BB  "," BB       BB "," B         B ","B           B","B     G     B","B    G G    B","B     G     B","B           B"," B         B "," BB       BB ","  BB     BB  ","    BBBBB    "},
        {"    BB~BB    ","  BB  G  BB  "," BB   G   BB "," B    G    B ","B     G     B","B     G     B","BGGGGG GGGGGB","B     G     B","B     G     B"," B    G    B "," BB   G   BB ","  BB  G  BB  ","    BBBBB    "},
        {"    BBBBB    ","  BBCCCCCBB  "," BBCCCCCCCBB "," BCCCCCCCCCB ","BCCCCCCCCCCCB","BCCCCCCCCCCCB","BCCCCCCCCCCCB","BCCCCCCCCCCCB","BCCCCCCCCCCCB"," BCCCCCCCCCB "," BBCCCCCCCBB ","  BBCCCCCBB  ","    BBBBB    "}
    };
    // spotless:on

    @Override
    public IStructureDefinition<TST_LargeCanner> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = IStructureDefinition.<TST_LargeCanner>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement('G', ofBlock(GregTechAPI.sBlockCasings2, 15))
                .addElement('K', ofFrame(Materials.NaquadahAlloy))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 0))
                .addElement('R', ofBlock(Loaders.MAR_Casing, 0))
                .addElement(
                    'B',
                    HatchElementBuilder.<TST_LargeCanner>builder()
                        .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy))
                        .adder(TST_LargeCanner::addToMachineList)
                        .dot(1)
                        .casingIndex(48)
                        .buildAndChain(GregTechAPI.sBlockCasings4, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
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
        return this.survivialBuildPiece(
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
    // region end

    // process
    @Override
    public int totalMachineMode() {
        /*
         * 0 - Canner
         * 1 - Fluid Canner
         */
        return 2;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    }

    @Override
    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal("LargeCanner.modeMsg." + mode);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return machineMode == 1 ? RecipeMaps.fluidCannerRecipes : RecipeMaps.cannerRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.fluidCannerRecipes, RecipeMaps.cannerRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("fluidMode", machineMode == 1);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getBoolean("fluidMode") ? 1 : 0;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(48), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(48), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(48) };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_LargeCanner_MachineType
        // # Fluid/Solid Canner
        // #zh_CN 流体/固体装罐机
        tt.addMachineType(TextEnums.tr("Tooltip_LargeCanner_MachineType"))
            // #tr Tooltip_LargeCanner_Controller
            // # Controller block for the Large Canner
            // #zh_CN 大型灌装机的控制方块
            .addInfo(TextEnums.tr("Tooltip_LargeCanner_Controller"))
            // #tr Tooltip_LargeCanner_01
            // # "Use unimaginable force to press items into containers!"
            // #zh_CN "使用超乎想象的力量把物品压入容器中！”
            .addInfo(TextEnums.tr("Tooltip_LargeCanner_01"))
            // #tr Tooltip_LargeCanner_02
            // # Having almost infinite parallelism!
            // #zh_CN 拥有近乎无限的并行！
            .addInfo(TextEnums.tr("Tooltip_LargeCanner_02"))
            // #tr Tooltip_LargeCanner_03
            // # Please use a screwdriver to switch modes.
            // #zh_CN 请使用螺丝刀来切换模式。
            .addInfo(TextEnums.tr("Tooltip_LargeCanner_03"))
            .addInfo(TextLocalization.StructureTooComplex)
            .addSeparator()
            .beginStructureBlock(13, 17, 13, false)
            .addInputBus(TextLocalization.BLUE_PRINT_INFO)
            .addOutputBus(TextLocalization.BLUE_PRINT_INFO)
            .addInputHatch(TextLocalization.BLUE_PRINT_INFO)
            .addOutputHatch(TextLocalization.BLUE_PRINT_INFO)
            .addEnergyHatch(TextLocalization.BLUE_PRINT_INFO)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }
    //

}
