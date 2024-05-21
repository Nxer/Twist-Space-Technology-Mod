package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textAnyCasing;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.LoaderReference;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class TST_MegaTreeFarm extends GTCM_MultiMachineBase<TST_MegaTreeFarm> {

    // region Class Constructor
    public TST_MegaTreeFarm(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_MegaTreeFarm(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaTreeFarm(this.mName);
    }

    // region Processing Logic

    private Byte glassTier;

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Only for NEI, not used in processing logic.
        return GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    // region Structure

    private final int horizontalOffSet = 5;
    private final int verticalOffSet = 18;
    private final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainMegaTreeFarm";
    private static IStructureDefinition<TST_MegaTreeFarm> STRUCTURE_DEFINITION = null;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(
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
    public IStructureDefinition<TST_MegaTreeFarm> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaTreeFarm>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    withChannel(
                        "glass",
                        BorosilicateGlass.ofBoroGlass(
                            (byte) 0,
                            (byte) 1,
                            Byte.MAX_VALUE,
                            (te, t) -> te.glassTier = t,
                            te -> te.glassTier)))
                .addElement('B', ofBlock(GregTech_API.sBlockCasings1, 10))
                .addElement('C', ofBlock(GregTech_API.sBlockCasings2, 2))
                .addElement('D', ofBlock(GregTech_API.sBlockCasings3, 10))
                .addElement('E', ofBlock(GregTech_API.sBlockCasings5, 8))
                .addElement('F', ofBlock(GregTech_API.sBlockCasings9, 1))
                .addElement('G', ofBlock(ModBlocks.blockCasings2Misc, 15))
                .addElement(
                    'g',
                    GT_HatchElementBuilder.<TST_MegaTreeFarm>builder()
                        .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy))
                        .adder(TST_MegaTreeFarm::addToMachineList)
                        .dot(1)
                        .casingIndex(TAE.getIndexFromPage(1, 15))
                        .buildAndChain(ModBlocks.blockCasings2Misc, 15))
                .addElement('H', ofBlock(ModBlocks.blockCasingsTieredGTPP, 8))
                .addElement('I', ofBlock(Block.getBlockFromName("Forestry:soil"), 0))
                // .addElement('I', ofBlock(Blocks.dirt, 0))
                .addElement(
                    'J',
                    LoaderReference.ProjRedIllumination
                        ? ofBlock(Block.getBlockFromName("ProjRed|Illumination:projectred.illumination.lamp"), 10)
                        // : ofChain(ofBlock(Blocks.redstone_lamp, 0), ofBlock(Blocks.lit_redstone_lamp, 0)))
                        : ofBlock(Blocks.redstone_lamp, 0))
                .addElement('K', ofBlock(Blocks.stone, 0))
                .addElement('L', ofBlock(Blocks.stonebrick, 0))
                .addElement('M', ofFrame(Materials.Vulcanite))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(1, 15)),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(1, 15)),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                    .extFacing()
                    .build() };
        }

        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(1, 15)) };
    }

    // spotless:off

    /*
    A -> ofBlock...(BW_GlasBlocks, 13, ...);
    B -> ofBlock...(gt.blockcasings, 10, ...);
    C -> ofBlock...(gt.blockcasings2, 2, ...);
    D -> ofBlock...(gt.blockcasings3, 10, ...);
    E -> ofBlock...(gt.blockcasings5, 8, ...);
    F -> ofBlock...(gt.blockcasings9, 1, ...);
    G -> ofBlock...(gtplusplus.blockcasings.2, 15, ...);
    H -> ofBlock...(gtplusplus.blocktieredcasings.1, 8, ...);
    I -> ofBlock...(tile.for.soil, 0, ...);
    J -> ofBlock...(tile.redstoneLight, 0, ...);
    K -> ofBlock...(tile.stone, 0, ...);        -Farm Block
    L -> ofBlock...(tile.stonebrick, 0, ...);   -Fram Ctrl
    M -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...);
    */

    private final String[][] shape = new String[][]{
        {"           ","           ","           ","    BBB    ","   BBBBB   ","   BBBBB   ","   BBBBB   ","    BBB    ","           ","           ","           "},
        {"           ","           ","   BBBBB   ","  BBFFFBB  ","  BFDDDFB  ","  BFDDDFB  ","  BFDDDFB  ","  BBFFFBB  ","   BBBBB   ","           ","           "},
        {"           ","   BBBBB   ","  B     B  "," B       B "," B       B "," B       B "," B       B "," B       B ","  B     B  ","   BBBBB   ","           "},
        {"           ","  B M M B  "," BM     MB ","    DDD    "," M DDDDD M ","   DDDDD   "," M DDDDD M ","    DDD    "," BM     MB ","  B M M B  ","           "},
        {"   BBBBB   ","  BCCCCCB  "," BCFFFFFCB ","BCFFHHHFFCB","BCFHHHHHFCB","BCFHHHHHFCB","BCFHHHHHFCB","BCFFHHHFFCB"," BCFFFFFCB ","  BCCCCCB  ","   BBBBB   "},
        {"   BGGGB   ","  GHHHHHG  "," GHMMMMMHG ","BHMMEEEMMHB","GHMEEEEEMHG","GHMEEEEEMHG","GHMEEEEEMHG","BHMMEEEMMHB"," GHMMMMMHG ","  GHHHHHG  ","   BGGGB   "},
        {"   BBBBB   ","  BGGGGGB  "," BGFFFFFGB ","BGFFJJJFFGB","BGFJJJJJFGB","BGFJJJJJFGB","BGFJJJJJFGB","BGFFJJJFFGB"," BGFFFFFGB ","  BGGGGGB  ","   BBBBB   "},
        {"           ","   AAAAA   ","  A     A  "," A       A "," A       A "," A       A "," A       A "," A       A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   AAAAA   ","  A     A  "," A       A "," A       A "," A       A "," A       A "," A       A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   AAAAA   ","  A     A  "," A       A "," A       A "," A       A "," A       A "," A       A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   AAAAA   ","  A     A  "," A       A "," A       A "," A       A "," A       A "," A       A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   AAAAA   ","  A     A  "," A       A "," A       A "," A       A "," A       A "," A       A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   AAAAA   ","  A     A  "," A       A "," A       A "," A       A "," A       A "," A       A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   AAAAA   ","  A     A  "," A       A "," A       A "," A       A "," A       A "," A       A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   AAAAA   ","  A     A  "," A       A "," A       A "," A       A "," A       A "," A       A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   AAAAA   ","  A     A  "," A       A "," A       A "," A       A "," A       A "," A       A ","  A     A  ","   AAAAA   ","           "},
        {"   BBBBB   ","  BGGGGGB  "," BGIIIIIGB ","BGIIIIIIIGB","BGIIIIIIIGB","BGIIIIIIIGB","BGIIIIIIIGB","BGIIIIIIIGB"," BGIIIIIGB ","  BGGGGGB  ","   BBBBB   "},
        {"   MgggM   ","  GFKKKFG  "," GHKKKKKHG ","MFKKKKKKKFM","GKKKLLLKKKG","GKKKLLLKKKG","GKKKLLLKKKG","MFKKKKKKKFM"," GHKKKKKHG ","  GFKKKFG  ","   MGGGM   "},
        {"   Mg~gM   ","  GFKLKFG  "," GHKKLKKHG ","MFKKKLKKKFM","GKKKLLLKKKG","GKKKLLLKKKG","GKKKLLLKKKG","MFKKKKKKKFM"," GHKKKKKHG ","  GFKKKFG  ","   MGGGM   "},
        {"   MgggM   ","  GFKKKFG  "," GHKKKKKHG ","MFKKKKKKKFM","GKKKLLLKKKG","GKKKLLLKKKG","GKKKLLLKKKG","MFKKKKKKKFM"," GHKKKKKHG ","  GFKKKFG  ","   MGGGM   "},
        {"   BBBBB   ","  BGGGGGB  "," BGBBBBBGB ","BGBGGGGGBGB","BGBGBBBGBGB","BGBGBgBGBGB","BGBGBBBGBGB","BGBGGGGGBGB"," BGBBBBBGB ","  BGGGGGB  ","   BBBBB   "}
    };
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        // #tr Tooltip_MegaTreeFarm_MachineType
        // # Tree Farm
        // #zh_CN 树厂
        tt.addMachineType(TextEnums.tr("Tooltip_MegaTreeFarm_MachineType"))
            // #tr Tooltip_IndustrialMagnetarSeparator_Controller
            // # Controller block for the Mega Tree Growth Simulator
            // #zh_CN 巨型原木拟生厂的控制方块
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm_Controller"))
            .addInputBus(textAnyCasing, 1)
            .addOutputBus(textAnyCasing, 1)
            .addEnergyHatch(textAnyCasing, 1)
            .addMaintenanceHatch(textAnyCasing, 1)
            .addMufflerHatch(textAnyCasing,1)
            .toolTipFinisher(ModName);
        return tt;
    }

    // spotless:on
}
