package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EnablePerfectOverclock_IncompactCyclotron;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EuModifier_IncompactCyclotron;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.MaxParallel_IncompactCyclotron;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBouns_IncompactCyclotron;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.getBlueprintWithDot;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontCenter;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class TST_IncompactCyclotron extends GTCM_MultiMachineBase<TST_IncompactCyclotron> {

    // region Class Constructor
    public TST_IncompactCyclotron(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_IncompactCyclotron(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_IncompactCyclotron(this.mName);
    }

    // endregion

    // region Processing Logic

    @Override
    protected float getEuModifier() {
        return EuModifier_IncompactCyclotron;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return EnablePerfectOverclock_IncompactCyclotron;
    }

    @Override
    protected float getSpeedBonus() {
        return SpeedBouns_IncompactCyclotron;
    }

    @Override
    public int getMaxParallelRecipes() {
        return MaxParallel_IncompactCyclotron;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.cyclotronRecipes;
    }

    // endregion

    // region Structure

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

    private final int horizontalOffSet = 23;
    private final int verticalOffSet = 3;
    private final int depthOffSet = 40;
    private static final String STRUCTURE_PIECE_MAIN = "mainIncompactCyclotron";
    private static IStructureDefinition<TST_IncompactCyclotron> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_IncompactCyclotron> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_IncompactCyclotron>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement('A', BorosilicateGlass.ofBoroGlass(10))
                .addElement(
                    'B',
                    ofBlock(Objects.requireNonNull(Block.getBlockFromName("miscutils:blockFrameGtQuantum")), 0))
                .addElement('C', ofBlock(MetaBlockCasing01, 12))
                .addElement('D', ofBlock(MetaBlockCasing01, 11))
                .addElement(
                    'E',
                    buildHatchAdder(TST_IncompactCyclotron.class)
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Energy.or(ExoticEnergy))
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(11))
                        .dot(1)
                        .buildAndChain(BorosilicateGlass.ofBoroGlass(10)))
                .addElement(
                    'F',
                    buildHatchAdder(TST_IncompactCyclotron.class).atLeast(Energy.or(ExoticEnergy))
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(11))
                        .dot(2)
                        .buildAndChain(TstBlocks.MetaBlockCasing01, 11))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }
    // spotless:off

    /*
     * A -> ofBlock...(BW_GlasBlocks, 14, ...);
     * B -> ofBlock...(block.Quantum.frame, 0, ...);
     * C -> ofBlock...(gtplusplus.blockcasings.2, 9, ...);
     * D -> ofBlock...(gtplusplus.blockcasings.2, 10, ...);
     * E -> ofBlock...(tile.wood, 0, ...);
     * F -> ofBlock...(tile.wood, 1, ...);
     */

    private final String[][] shapeMain = new String[][]{
        {"                                               ","                                               ","                    BDDDDDB                    ","                    BDEAEDB                    ","                    BDDDDDB                    ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","  BBB                                     BBB  ","  DDD                                     DDD  ","  DED                                     DED  ","  DAD                                     DAD  ","  DED                                     DED  ","  DDD                                     DDD  ","  BBB                                     BBB  ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    BDDDDDB                    ","                    BDEAEDB                    ","                    BDDDDDB                    ","                                               ","                                               "},
        {"                                               ","                    BDEAEDB                    ","                   DD     DD                   ","                DDDDD     DDDDD                ","              DDDDDDD     DDDDDDD              ","            DDDDDDD BDEAEDB DDDDDDD            ","           DDDDD               DDDDD           ","          DDDD                   DDDD          ","         DDD                       DDD         ","        DDD                         DDD        ","       DDD                           DDD       ","      DDD                             DDD      ","     DDD                               DDD     ","     DDD                               DDD     ","    DDD                                 DDD    ","    DDD                                 DDD    ","   DDD                                   DDD   ","   DDD                                   DDD   ","   DDD                                   DDD   ","  DDD                                     DDD  "," BDDDB                                   BDDDB "," D   D                                   D   D "," E   E                                   E   E "," A   A                                   A   A "," E   E                                   E   E "," D   D                                   D   D "," BDDDB                                   BDDDB ","  DDD                                     DDD  ","   DDD                                   DDD   ","   DDD                                   DDD   ","   DDD                                   DDD   ","    DDD                                 DDD    ","    DDD                                 DDD    ","     DDD                               DDD     ","     DDD                               DDD     ","      DDD                             DDD      ","       DDD                           DDD       ","        DDD                         DDD        ","         DDD                       DDD         ","          DDDD                   DDDD          ","           DDDDD               DDDDD           ","            DDDDDDD BDEAEDB DDDDDDD            ","              DDDDDDD     DDDDDDD              ","                DDDDD     DDDDD                ","                   DD     DD                   ","                    BDEAEDB                    ","                                               "},
        {"                    BDDDDDB                    ","                   DD     DD                   ","                DDDDD     DDDDD                ","              DDDDDCCCCCCCCCDDDDD              ","            DDDDCCCDD     DDCCCDDDD            ","           DDDCCDDDDD     DDDDDCCDDD           ","          FDCCDDDDD BDDDDDB DDDDDCCDF          ","         DDCDDDD               DDDDCDD         ","        DDCDDD                   DDDCDD        ","       DDCDF                       FDCDD       ","      FDCDD                         DDCDF      ","     DDCDF                           FDCDD     ","    DDCDD                             DDCDD    ","    DDCDD                             DDCDD    ","   DDCDD                               DDCDD   ","   DDCDD                               DDCDD   ","  DDCDD                                 DDCDD  ","  DDCDD                                 DDCDD  ","  DDCDD                                 DDCDD  "," DDCDD                                   DDCDD ","BDDCDDB                                 BDDCDDB","D  C  D                                 D  C  D","D  C  D                                 D  C  D","D  C  D                                 D  C  D","D  C  D                                 D  C  D","D  C  D                                 D  C  D","BDDCDDB                                 BDDCDDB"," DDCDD                                   DDCDD ","  DDCDD                                 DDCDD  ","  DDCDD                                 DDCDD  ","  DDCDD                                 DDCDD  ","   DDCDD                               DDCDD   ","   DDCDD                               DDCDD   ","    DDCDD                             DDCDD    ","    DDCDD                             DDCDD    ","     DDCDF                           FDCDD     ","      FDCDD                         DDCDF      ","       DDCDF                       FDCDD       ","        DDCDDD                   DDDCDD        ","         DDCDDDD               DDDDCDD         ","          FDCCDDDDD BDDDDDB DDDDDCCDF          ","           DDDCCDDDDD     DDDDDCCDDD           ","            DDDDCCCDD     DDCCCDDDD            ","              DDDDDCCCCCCCCCDDDDD              ","                DDDDD     DDDDD                ","                   DD     DD                   ","                    BDDDDDB                    "},
        {"                    BDEAEDB                    ","                   DD     DD                   ","                DDDCCCCCCCCCDDD                ","              DDCCCCCCCCCCCCCCCDD              ","            DDCCCCCCCCCCCCCCCCCCCDD            ","           DCCCCCCCDD     DDCCCCCCCD           ","          DCCCCCDDD BDEAEDB DDDCCCCCD          ","         DCCCCDD               DDCCCCD         ","        DCCCDD                   DDCCCD        ","       DCCCD                       DCCCD       ","      DCCCD                         DCCCD      ","     DCCCD                           DCCCD     ","    DCCCD                             DCCCD    ","    DCCCD                             DCCCD    ","   DCCCD                               DCCCD   ","   DCCCD                               DCCCD   ","  DCCCD                                 DCCCD  ","  DCCCD                                 DCCCD  ","  DCCCD                                 DCCCD  "," DCCCD                                   DCCCD ","BDCCCDB                                 BDCCCDB","D CCC D                                 D CCC D","E CCC E                                 E CCC E","A CCC A                                 A CCC A","E CCC E                                 E CCC E","D CCC D                                 D CCC D","BDCCCDB                                 BDCCCDB"," DCCCD                                   DCCCD ","  DCCCD                                 DCCCD  ","  DCCCD                                 DCCCD  ","  DCCCD                                 DCCCD  ","   DCCCD                               DCCCD   ","   DCCCD                               DCCCD   ","    DCCCD                             DCCCD    ","    DCCCD                             DCCCD    ","     DCCCD                           DCCCD     ","      DCCCD                         DCCCD      ","       DCCCD                       DCCCD       ","        DCCCDD                   DDCCCD        ","         DCCCCDD               DDCCCCD         ","          DCCCCCDDD BDE~EDB DDDCCCCCD          ","           DCCCCCCCDD     DDCCCCCCCD           ","            DDCCCCCCCCCCCCCCCCCCCDD            ","              DDCCCCCCCCCCCCCCCDD              ","                DDDCCCCCCCCCDDD                ","                   DD     DD                   ","                    BDEAEDB                    "},
        {"                    BDDDDDB                    ","                   DD     DD                   ","                DDDDD     DDDDD                ","              DDDDDCCCCCCCCCDDDDD              ","            DDDDCCCDD     DDCCCDDDD            ","           DDDCCDDDDD     DDDDDCCDDD           ","          FDCCDDDDD BDDDDDB DDDDDCCDF          ","         DDCDDDD               DDDDCDD         ","        DDCDDD                   DDDCDD        ","       DDCDF                       FDCDD       ","      FDCDD                         DDCDF      ","     DDCDF                           FDCDD     ","    DDCDD                             DDCDD    ","    DDCDD                             DDCDD    ","   DDCDD                               DDCDD   ","   DDCDD                               DDCDD   ","  DDCDD                                 DDCDD  ","  DDCDD                                 DDCDD  ","  DDCDD                                 DDCDD  "," DDCDD                                   DDCDD ","BDDCDDB                                 BDDCDDB","D  C  D                                 D  C  D","D  C  D                                 D  C  D","D  C  D                                 D  C  D","D  C  D                                 D  C  D","D  C  D                                 D  C  D","BDDCDDB                                 BDDCDDB"," DDCDD                                   DDCDD ","  DDCDD                                 DDCDD  ","  DDCDD                                 DDCDD  ","  DDCDD                                 DDCDD  ","   DDCDD                               DDCDD   ","   DDCDD                               DDCDD   ","    DDCDD                             DDCDD    ","    DDCDD                             DDCDD    ","     DDCDF                           FDCDD     ","      FDCDD                         DDCDF      ","       DDCDF                       FDCDD       ","        DDCDDD                   DDDCDD        ","         DDCDDDD               DDDDCDD         ","          FDCCDDDDD BDDDDDB DDDDDCCDF          ","           DDDCCDDDDD     DDDDDCCDDD           ","            DDDDCCCDD     DDCCCDDDD            ","              DDDDDCCCCCCCCCDDDDD              ","                DDDDD     DDDDD                ","                   DD     DD                   ","                    BDDDDDB                    "},
        {"                                               ","                    BDEAEDB                    ","                   DD     DD                   ","                DDDDD     DDDDD                ","              DDDDDDD     DDDDDDD              ","            DDDDDDD BDEAEDB DDDDDDD            ","           DDDDD               DDDDD           ","          DDDD                   DDDD          ","         DDD                       DDD         ","        DDD                         DDD        ","       DDD                           DDD       ","      DDD                             DDD      ","     DDD                               DDD     ","     DDD                               DDD     ","    DDD                                 DDD    ","    DDD                                 DDD    ","   DDD                                   DDD   ","   DDD                                   DDD   ","   DDD                                   DDD   ","  DDD                                     DDD  "," BDDDB                                   BDDDB "," D   D                                   D   D "," E   E                                   E   E "," A   A                                   A   A "," E   E                                   E   E "," D   D                                   D   D "," BDDDB                                   BDDDB ","  DDD                                     DDD  ","   DDD                                   DDD   ","   DDD                                   DDD   ","   DDD                                   DDD   ","    DDD                                 DDD    ","    DDD                                 DDD    ","     DDD                               DDD     ","     DDD                               DDD     ","      DDD                             DDD      ","       DDD                           DDD       ","        DDD                         DDD        ","         DDD                       DDD         ","          DDDD                   DDDD          ","           DDDDD               DDDDD           ","            DDDDDDD BDEAEDB DDDDDDD            ","              DDDDDDD     DDDDDDD              ","                DDDDD     DDDDD                ","                   DD     DD                   ","                    BDEAEDB                    ","                                               "},
        {"                                               ","                                               ","                    BDDDDDB                    ","                    BDEAEDB                    ","                    BDDDDDB                    ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","  BBB                                     BBB  ","  DDD                                     DDD  ","  DED                                     DED  ","  DAD                                     DAD  ","  DED                                     DED  ","  DDD                                     DDD  ","  BBB                                     BBB  ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    BDDDDDB                    ","                    BDEAEDB                    ","                    BDDDDDB                    ","                                               ","                                               "}
    };

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        ITexture base = casingTexturePages[115][MetaBlockCasing01.getTextureIndexInPage(11)];
        if (side == facing) {
            if (aActive) return new ITexture[] { base, TextureFactory.builder()
                .addIcon(TexturesGtBlock.Overlay_MatterFab_Active_Animated)
                .extFacing()
                .build() };
            return new ITexture[] { base, TextureFactory.builder()
                .addIcon(TexturesGtBlock.Overlay_MatterFab_Animated)
                .extFacing()
                .build() };
        }
        return new ITexture[] { base };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_IncompactCyclotron_MachineType
        // # Particle Accelerator
        // #zh_CN 粒子加速器
        tt.addMachineType(TextEnums.tr("Tooltip_IncompactCyclotron_MachineType"))
            // #tr Tooltip_IncompactCyclotron_Controller
            // # Controller block for the Incompact Cyclotron
            // #zh_CN 非紧凑式回旋加速器的控制方块
            .addInfo(TextEnums.tr("Tooltip_IncompactCyclotron_Controller"))
            // #tr Tooltip_IncompactCyclotron_01
            // # {\DARK_PURPLE}Pulsed Ultrafast Linear Synchronized Accelerator Ring
            // #zh_CN {\DARK_PURPLE}脉冲超快线性同步加速器
            .addInfo(TextEnums.tr("Tooltip_IncompactCyclotron_01"))
            // #tr Tooltip_IncompactCyclotron_02
            // # The particles are accelerated to {\YELLOW}99%%{\GRAY} of the speed of light by the electric field!
            // #zh_CN 粒子在电场下被加速到{\YELLOW}99%%{\GRAY}光速！
            .addInfo(TextEnums.tr("Tooltip_IncompactCyclotron_02"))
            // #tr Tooltip_IncompactCyclotron_03
            // # But you can extract the accelerated particles at any time.
            // #zh_CN 但是你随时可以取出加工的粒子
            .addInfo(TextEnums.tr("Tooltip_IncompactCyclotron_03"))
            // #tr Tooltip_IncompactCyclotron_04
            // # "That's incredible!"
            // #zh_CN 这太神奇了！
            .addInfo(TextEnums.tr("Tooltip_IncompactCyclotron_04"))
            // #tr Tooltip_IncompactCyclotron_05
            // # Thanks to the upgrade of technology
            // #zh_CN 得益于工艺的升级
            .addInfo(TextEnums.tr("Tooltip_IncompactCyclotron_05"))
            // #tr Tooltip_IncompactCyclotron_06
            // # The accelerator upgraded 100%% speedup and 256 parallel
            // #zh_CN 加速器拥有了额外100%%的额外加速,以及256并行
            .addInfo(TextEnums.tr("Tooltip_IncompactCyclotron_06"))
            // #tr Tooltip_IncompactCyclotron_07
            // # But it requires additional 60%% of power to work
            // #zh_CN 但是需要额外60%%的供电来运行
            .addInfo(TextEnums.tr("Tooltip_IncompactCyclotron_07"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addController(textFrontCenter)
            .addInputHatch(getBlueprintWithDot(1))
            .addOutputHatch(getBlueprintWithDot(1))
            .addInputBus(getBlueprintWithDot(1))
            .addOutputBus(getBlueprintWithDot(1))
            .addEnergyHatch(getBlueprintWithDot(2))
            .addStructureInfo(Text_SeparatingLine)
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .toolTipFinisher(ModName);
        return tt;
    }
    // spotless:on
}
