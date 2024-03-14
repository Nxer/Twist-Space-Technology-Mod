package com.Nxer.TwistSpaceTechnology.common.machine;

import static bloodasp.galacticgreg.registry.GalacticGregRegistry.getModContainers;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SPACE_ELEVATOR_BASE_CASING_INDEX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.github.bartimaeusnek.crossmod.galacticgreg.GT_TileEntity_VoidMiner_Base.getExtraDropsDimMap;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.system.VoidMinerRework.logic.VoidMinerData;
import com.Nxer.TwistSpaceTechnology.system.VoidMinerRework.logic.VoidMinerDataGenerator;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.oregen.BW_OreLayer;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;

import bloodasp.galacticgreg.GT_Worldgen_GT_Ore_Layer_Space;
import bloodasp.galacticgreg.GT_Worldgen_GT_Ore_SmallPieces_Space;
import bloodasp.galacticgreg.GalacticGreg;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_Layer_Space;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_SmallOre_Space;
import cpw.mods.fml.common.registry.GameRegistry;
import galaxyspace.core.register.GSBlocks;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;

public class TST_StarcoreMiner extends GTCM_MultiMachineBase<TST_StarcoreMiner> {

    // region Class Constructor
    public TST_StarcoreMiner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_StarcoreMiner(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_StarcoreMiner(this.mName);
    }

    // endregion

    // region Structure
    // spotless:off
    private static final int horizontalOffSetMain = 10;
    private static final int verticalOffSetMain = 22;
    private static final int depthOffSetMain = 1;
    private static final int horizontalOffSetMiddle = 4;
    private static final int verticalOffSetMiddle = -4;
    private static final int depthOffSetMiddle = -15;
    private static final int horizontalOffSetEnd = 0;
    private static final int depthOffSetEnd = -19;
    private static final String STRUCTURE_PIECE_MAIN = "mainStarcoreMiner";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleStarcoreMiner";
    private static final String STRUCTURE_PIECE_END = "endStarcoreMiner";
    private static IStructureDefinition<TST_StarcoreMiner> STRUCTURE_DEFINITION;
    private short y_value = 1;

    @Override
    public IStructureDefinition<TST_StarcoreMiner> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION =
                StructureDefinition
                    .<TST_StarcoreMiner>builder()
                    .addShape(
                        STRUCTURE_PIECE_MAIN,
                        transpose(new String[][]{
                            {"        GGGGG        ","       GGGGGGG       ","      GGGGGGGGG      ","      GGGGGGGGG      ","      GGGGGGGGG      ","      GGGGGGGGG      ","      GGGGGGGGG      ","       GGGGGGG       ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","        GGGGG        ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HGIGH        ","        GGIGG        ","         AIA         ","         AIA         ","         AIA         ","         AIA         ","         AIA         ","         AIA         ","         AIA         ","         AIA         ","         AIA         ","         AIA         ","         AIA         ","         AIA         ","         CCC         ","         CIC         ","         CCC         ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDGDH        ","        GDGDG        ","         DGD         ","         DGD         ","         DGD         ","         DGD         ","         DGD         ","         DGD         ","         DGD         ","         DGD         ","         DGD         ","         DGD         ","         DGD         ","         DGD         ","         CCC         ","         CIC         ","         CCC         ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","         GGG         ","         GIG         ","         GGG         ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","          K          ","         KIK         ","          K          ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","                     ","        BBBBB        ","      BBBBBBBBB      ","     BBB     BBB     ","    BBB       BBB    ","    BB         BB    ","   BB           BB   ","   BB     K     BB   ","   BB    KIK    BB   ","   BB     K     BB   ","   BB           BB   ","    BB         BB    ","    BBB       BBB    ","     BBB     BBB     ","      BBBBBBBBB      ","        BBBBB        ","                     ","                     ","                     "},
                            {"                     ","                     ","        G   G        ","        HHAHH        ","         GIG         ","        HDADH        ","        G   G        ","                     ","                     ","                     ","                     ","                     ","        EEEEE        ","      EEEEEEEEE      ","    EEEEEEEEEEEEE    ","    EEEEEEEEEEEEE    ","   EEEEEFFFFFEEEEE   ","   EEEEFF   FFEEEE   ","  EEEEFF     FFEEEE  ","  EEEEF   K   FEEEE  ","  EEEEF  KIK  FEEEE  ","  EEEEF   K   FEEEE  ","  EEEEFF     FFEEEE  ","   EEEEFF   FFEEEE   ","   EEEEEFFFFFEEEEE   ","    EEEEEEEEEEEEE    ","    EEEEEEEEEEEEE    ","      EEEEEEEEE      ","        EEEEE        ","                     ","                     "},
                            {"                     ","         LLL         ","        GGGGG        ","       HHGGGHH       ","         GIG         ","       HHGGGHH       ","        GLLLG        ","        G   G        ","                     ","                     ","                     ","                     ","        EEEEE        ","      EEEEEEEEE      ","    EEEEEEEEEEEEE    ","    EEEEEEEEEEEEE    ","   EEEEEFFFFFEEEEE   ","   EEEEFFFFFFFEEEE   ","  EEEEFFF   FFFEEEE  ","  EEEEFF  K  FFEEEE  ","  EEEEFF KIK FFEEEE  ","  EEEEFF  K  FFEEEE  ","  EEEEFFF   FFFEEEE  ","   EEEEFFFFFFFEEEE   ","   EEEEEFFFFFEEEEE   ","    EEEEEEEEEEEEE    ","    EEEEEEEEEEEEE    ","      EEEEEEEEE      ","        EEEEE        ","                     ","                     "},
                            {"                     ","         L~L         ","        GGIGG        ","      HHHGIGHHH      ","         GIG         ","      HHHGGGHHH      ","        GLLLG        ","        G   G        ","        G   G        ","                     ","                     ","                     ","        EEEEE        ","      EEEEEEEEE      ","    EEEEEEEEEEEEE    ","    EEEEEEEEEEEEE    ","   EEEEEFFFFFEEEEE   ","   EEEEFFFFFFFEEEE   ","  EEEEFFF   FFFEEEE  ","  EEEEFF  K  FFEEEE  ","  EEEEFF KIK FFEEEE  ","  EEEEFF  K  FFEEEE  ","  EEEEFFF   FFFEEEE  ","   EEEEFFFFFFFEEEE   ","   EEEEEFFFFFEEEEE   ","    EEEEEEEEEEEEE    ","    EEEEEEEEEEEEE    ","      EEEEEEEEE      ","        EEEEE        ","                     ","                     "},
                            {"                     ","         LLL         ","        GGGGG        ","     HHHHGGGHHHH     ","         GGG         ","     HHHHGGGHHHH     ","        GLLLG        ","        G   G        ","        G   G        ","        G   G        ","                     ","                     ","        EEEEE        ","      EEEEEEEEE      ","    EEEEEEEEEEEEE    ","    EEEEEEEEEEEEE    ","   EEEEEFFFFFEEEEE   ","   EEEEFFFFFFFEEEE   ","  EEEEFFF   FFFEEEE  ","  EEEEFF  K  FFEEEE  ","  EEEEFF KIK FFEEEE  ","  EEEEFF  K  FFEEEE  ","  EEEEFFF   FFFEEEE  ","   EEEEFFFFFFFEEEE   ","   EEEEEFFFFFEEEEE   ","    EEEEEEEEEEEEE    ","    EEEEEEEEEEEEE    ","      EEEEEEEEE      ","        EEEEE        ","                     ","                     "},
                            {"                     ","       JJLLLJJ       ","      JJJJJJJJJ      ","     HHHHJJJHHHH     ","      JJJJJJJJJ      ","     HHHHJJJHHHH     ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","    JJJJJJJJJJJJJ    ","   JJJJJJJJJJJJJJJ   ","  JJJJJJJJJJJJJJJJJ  ","  JJJJJJJJJJJJJJJJJ  "," JJJJJJJFFFFFJJJJJJJ "," JJJJJJFFFFFFFJJJJJJ ","JJJJJJFFF   FFFJJJJJJ","JJJJJJFF  K  FFJJJJJJ","JJJJJJFF KIK FFJJJJJJ","JJJJJJFF  K  FFJJJJJJ","JJJJJJFFF   FFFJJJJJJ"," JJJJJJFFFFFFFJJJJJJ "," JJJJJJJFFFFFJJJJJJJ ","  JJJJJJJJJJJJJJJJJ  ","  JJJJJJJJJJJJJJJJJ  ","   JJJJJJJJJJJJJJJ   ","    JJJJJJJJJJJJJ    ","      JJJJJJJJJ      ","        JJJJJ        "},
                            {"                     ","       JJLLLJJ       ","      JJJJJJJJJ      ","     HHHHJJJHHHH     ","      JJJJJJJJJ      ","     HHHHJJJHHHH     ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","      JJJJJJJJJ      ","    JJJJJJJJJJJJJ    ","   JJJJJJJJJJJJJJJ   ","  JJJJJJJJJJJJJJJJJ  ","  JJJJJJJJJJJJJJJJJ  "," JJJJJJJFFFFFJJJJJJJ "," JJJJJJFFFFFFFJJJJJJ ","JJJJJJFFF   FFFJJJJJJ","JJJJJJFF  K  FFJJJJJJ","JJJJJJFF KIK FFJJJJJJ","JJJJJJFF  K  FFJJJJJJ","JJJJJJFFF   FFFJJJJJJ"," JJJJJJFFFFFFFJJJJJJ "," JJJJJJJFFFFFJJJJJJJ ","  JJJJJJJJJJJJJJJJJ  ","  JJJJJJJJJJJJJJJJJ  ","   JJJJJJJJJJJJJJJ   ","    JJJJJJJJJJJJJ    ","      JJJJJJJJJ      ","        JJJJJ        "}
                        })
                    )
                    .addShape(
                        STRUCTURE_PIECE_MIDDLE,
                        transpose(new String[][]{
                            {"  FFFFF  "," FFFFFFF ","FFF   FFF","FF  K  FF","FF KIK FF","FF  K  FF","FFF   FFF"," FFFFFFF ","  FFFFF  "}
                        })
                    )
                    .addShape(
                        STRUCTURE_PIECE_END,
                        new String[][]{
                            {"Z"}
                        }
                    )
                    .addElement('A', BorosilicateGlass.ofBoroGlassAnyTier())
                    .addElement('B', ofBlock(GregTech_API.sBlockCasings1, 11))
                    .addElement('C', ofBlock(GregTech_API.sBlockCasings1, 14))
                    .addElement('D', ofBlock(GregTech_API.sBlockCasings2, 15))
                    .addElement('E', ofBlock(GregTech_API.sBlockCasings8, 7))
                    .addElement('F', ofBlock(GregTech_API.sBlockCasings8, 10))
                    .addElement('G', ofBlock(IGBlocks.SpaceElevatorCasing, 0))
                    .addElement('H', ofBlock(IGBlocks.SpaceElevatorCasing, 1))
                    .addElement('I', ofBlock(sBlockCasingsTT, 8))
                    .addElement('J', ofBlock(GSBlocks.DysonSwarmBlocks, 9))
                    .addElement(
                        'K',
                        ofChain(
                            BorosilicateGlass.ofBoroGlassAnyTier(),
                            ofFrame(Materials.NaquadahAlloy),
                            ofBlock(GregTech_API.sBlockCasings8, 10)
                        ))
                    .addElement(
                        'L',
                        GT_HatchElementBuilder
                            .<TST_StarcoreMiner>builder()
                            .atLeast(InputBus, InputHatch, OutputBus, Energy.or(ExoticEnergy))
                            .adder(TST_StarcoreMiner::addToMachineList)
                            .dot(1)
                            .casingIndex(SPACE_ELEVATOR_BASE_CASING_INDEX)
                            .buildAndChain(IGBlocks.SpaceElevatorCasing, 0)
                    )
                    /*
                    Blocks:
                        A -> ofBlock...(BW_GlasBlocks, 0, ...); // any glass
                        B -> ofBlock...(gt.blockcasings, 11, ...);
                        C -> ofBlock...(gt.blockcasings, 14, ...);
                        D -> ofBlock...(gt.blockcasings2, 15, ...);
                        E -> ofBlock...(gt.blockcasings8, 7, ...);
                        F -> ofBlock...(gt.blockcasings8, 10, ...); // Nq alloy casing
                        G -> ofBlock...(gt.blockcasingsSE, 0, ...);
                        H -> ofBlock...(gt.blockcasingsSE, 1, ...);
                        I -> ofBlock...(gt.blockcasingsTT, 8, ...); // holo casing
                        J -> ofBlock...(tile.DysonSwarmPart, 9, ...);
                        K -> ofBlock...(tile.glass, 0, ...); // any glass or ofFrame(Materials.NaquadahAlloy) or ofBlock(NaquadahAlloy casing)
                        L -> ofBlock...(gt.blockcasingsSE, 0); // hatches
                     */
                    .addElement('Z', ofBlock(Blocks.bedrock, 0))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSetMain, verticalOffSetMain, depthOffSetMain);
        int piece = stackSize.stackSize;
        for (int i=0; i<piece; i++){
            this.buildPiece(STRUCTURE_PIECE_MIDDLE, stackSize, hintsOnly, horizontalOffSetMiddle, verticalOffSetMiddle - i, depthOffSetMiddle);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int builtMain = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSetMain, verticalOffSetMain, depthOffSetMain, elementBudget, env, false, true);

        // Construct Pipe Structure
        int piece = stackSize.stackSize - 1;
        int builtMiddle = 0;
        int builtMiddleCurrent = 0;
        boolean triedBuiltMiddle = false;

        if (piece > 0) {
            for (int i=0;i<piece;i++) {
                builtMiddleCurrent = survivialBuildPiece(STRUCTURE_PIECE_MIDDLE, stackSize, horizontalOffSetMiddle, verticalOffSetMiddle - i, depthOffSetMiddle, elementBudget, env, false, true);
                if (builtMiddleCurrent != -1) {
                    // if a piece has been built, mark the signal to confirm whether structure has been built complete
                    builtMiddle += builtMiddleCurrent;
                    triedBuiltMiddle = true;
                }
            }
        }

        if (builtMiddle == 0 && !triedBuiltMiddle) builtMiddle = -1;

        // building finished
        if (builtMain == -1 && builtMiddle == -1) return -1;

        // only Main finished
        if (builtMain == -1 && builtMiddle >= 0) return builtMiddle;

        // only Middle finished
        if (builtMiddle == -1 && builtMain >= 0) return builtMain;

        // building
        return builtMain + builtMiddle;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSetMain, verticalOffSetMain, depthOffSetMain)) return false;
        if (y_value > 20) {
            // if controller block is at location higher than 20
            // continue check pipe structure
            int needPiece = y_value - 20;
            int p = 0;
            for (;p<needPiece;p++) {
                if (checkPiece(STRUCTURE_PIECE_MIDDLE, horizontalOffSetMiddle, verticalOffSetMiddle - p, depthOffSetMiddle)) continue;
                // or check if stopped by a bedrock block
                if (checkPiece(STRUCTURE_PIECE_END, horizontalOffSetEnd, verticalOffSetMiddle - p, depthOffSetEnd)) {
                    break;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    // spotless:on
    // endregion

    // region Processing Logic

    private Map<Pair<Integer, Boolean>, Float> dropmap = null;
    private float totalWeight = 0;
    private static final int multiplier = 1024;

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        // if the dropmap has never been initialised or if the dropmap is empty
        // if (this.dropmap == null || this.totalWeight == 0) this.calculateDropMap();
        if (dropmap == null || totalWeight == 0) {
            TwistSpaceTechnology.LOG.info("ERROR! Starcore Miner dropmap = null when checkProcessing!");
        }

        if (this.totalWeight != 0.f) {
            mOutputItems = new ItemStack[] { getOreItemStack(getOreDamage()), getOreItemStack(getOreDamage()),
                getOreItemStack(getOreDamage()), getOreItemStack(getOreDamage()), getOreItemStack(getOreDamage()),
                getOreItemStack(getOreDamage()), getOreItemStack(getOreDamage()), getOreItemStack(getOreDamage()) };
            setElectricityStats();
            updateSlots();
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
        this.stopMachine();
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    protected void setElectricityStats() {
        this.mProgresstime = 0;
        this.mMaxProgresstime = 10;
        this.mEfficiency = 10000;
        this.mEfficiencyIncrease = 10000;
        this.lEUt = -RECIPE_UIV;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.y_value = aBaseMetaTileEntity.getYCoord();
        World world = aBaseMetaTileEntity.getWorld();
        int dimID = world.provider.dimensionId;
        if (!VoidMinerData.OrePool.containsKey(dimID)) {
            VoidMinerDataGenerator.generate(world)
                .done();
        }
        dropmap = VoidMinerData.OrePool.get(dimID);
        totalWeight = VoidMinerData.OrePoolTotalWeightPool.get(dimID);

    }

    private Pair<Integer, Boolean> getOreDamage() {
        float curentWeight = 0.f;
        while (true) {
            float randomnumber = XSTR.XSTR_INSTANCE.nextFloat() * this.totalWeight;
            for (Map.Entry<Pair<Integer, Boolean>, Float> entry : this.dropmap.entrySet()) {
                curentWeight += entry.getValue();
                if (randomnumber < curentWeight) return entry.getKey();
            }
        }
    }

    private void calculateDropMap() {
        this.dropmap = new HashMap<>();
        int id = this.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId;
        this.handleModDimDef(id);
        this.handleExtraDrops(id);
        this.calculateTotalWeight();
    }

    // region Method from com.github.bartimaeusnek.crossmod.galacticgreg.GT_TileEntity_VoidMiner_Base
    private void calculateTotalWeight() {
        this.totalWeight = 0.0f;
        this.dropmap.values()
            .forEach(f -> this.totalWeight += f);
    }

    private void handleExtraDrops(int id) {
        Optional.ofNullable(getExtraDropsDimMap().get(id))
            .ifPresent(e -> e.forEach(f -> this.addDrop(f.getKey(), f.getValue())));
    }

    private void handleModDimDef(int id) {
        // vanilla dims or TF
        if (id <= 1 && id >= -1 || id == 7) {
            this.getDropsVanillaVeins(this.makeOreLayerPredicate());
            this.getDropsVanillaSmallOres(this.makeSmallOresPredicate());

            // ross dims
        } else if (id == ConfigHandler.ross128BID || id == ConfigHandler.ross128BAID) {
            this.getDropMapRoss(id);

            // other space dims
        } else {
            Optional.ofNullable(this.makeModDimDef())
                .ifPresent(def -> {
                    // normal space dim
                    this.getDropsOreVeinsSpace(def);
                    this.getDropsSmallOreSpace(def);

                    // BW space dim
                    Consumer<BW_OreLayer> addToList = this.makeAddToList();
                    this.addOresVeinsBartworks(def, addToList);
                    this.addSmallOresBartworks(def);
                });
        }
    }

    private void addSmallOresBartworks(ModDimensionDef finalDef) {
        try {
            GalacticGreg.smallOreWorldgenList.stream()
                .filter(
                    gt_worldgen -> gt_worldgen.mEnabled
                        && gt_worldgen instanceof BW_Worldgen_Ore_SmallOre_Space smallOreSpace
                        && smallOreSpace.isEnabledForDim(finalDef))
                .map(gt_worldgen -> (BW_Worldgen_Ore_SmallOre_Space) gt_worldgen)
                .forEach(
                    element -> this.addDrop(new Pair<>(element.mPrimaryMeta, element.bwOres != 0), element.mDensity));
        } catch (NullPointerException ignored) {}
    }

    private void addOresVeinsBartworks(ModDimensionDef finalDef, Consumer<BW_OreLayer> addToList) {
        try {
            GalacticGreg.oreVeinWorldgenList.stream()
                .filter(
                    gt_worldgen -> gt_worldgen.mEnabled
                        && gt_worldgen instanceof BW_Worldgen_Ore_Layer_Space oreLayerSpace
                        && oreLayerSpace.isEnabledForDim(finalDef))
                .map(gt_worldgen -> (BW_Worldgen_Ore_Layer_Space) gt_worldgen)
                .forEach(addToList);
        } catch (NullPointerException ignored) {}
    }

    private void getDropsSmallOreSpace(ModDimensionDef finalDef) {
        GalacticGreg.smallOreWorldgenList.stream()
            .filter(
                gt_worldgen -> gt_worldgen.mEnabled
                    && gt_worldgen instanceof GT_Worldgen_GT_Ore_SmallPieces_Space oreSmallPiecesSpace
                    && oreSmallPiecesSpace.isEnabledForDim(finalDef))
            .map(gt_worldgen -> (GT_Worldgen_GT_Ore_SmallPieces_Space) gt_worldgen)
            .forEach(element -> this.addDrop(new Pair<>((int) element.mMeta, false), element.mAmount));
    }

    private void getDropsOreVeinsSpace(ModDimensionDef finalDef) {
        GalacticGreg.oreVeinWorldgenList.stream()
            .filter(
                gt_worldgen -> gt_worldgen.mEnabled
                    && gt_worldgen instanceof GT_Worldgen_GT_Ore_Layer_Space oreLayerSpace
                    && oreLayerSpace.isEnabledForDim(finalDef))
            .map(gt_worldgen -> (GT_Worldgen_GT_Ore_Layer_Space) gt_worldgen)
            .forEach(element -> {
                this.addDrop(new Pair<>((int) element.mPrimaryMeta, false), element.mWeight);
                this.addDrop(new Pair<>((int) element.mSecondaryMeta, false), element.mWeight);
                this.addDrop(new Pair<>((int) element.mSporadicMeta, false), element.mWeight / 8f);
                this.addDrop(new Pair<>((int) element.mBetweenMeta, false), element.mWeight / 8f);
            });
    }

    private ModDimensionDef makeModDimDef() {
        return getModContainers().stream()
            .flatMap(
                modContainer -> modContainer.getDimensionList()
                    .stream())
            .filter(
                modDimensionDef -> modDimensionDef.getChunkProviderName()
                    .equals(
                        ((ChunkProviderServer) this.getBaseMetaTileEntity()
                            .getWorld()
                            .getChunkProvider()).currentChunkProvider.getClass()
                                .getName()))
            .findFirst()
            .orElse(null);
    }

    private void getDropMapRoss(int aID) {
        Consumer<BW_OreLayer> addToList = this.makeAddToList();
        BW_OreLayer.sList.stream()
            .filter(
                gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof BW_OreLayer
                    && gt_worldgen.isGenerationAllowed(null, aID, 0))
            .forEach(addToList);
    }

    private Consumer<BW_OreLayer> makeAddToList() {
        return element -> {
            List<Pair<Integer, Boolean>> data = element.getStacksRawData();
            for (int i = 0; i < data.size(); i++) {
                if (i < data.size() - 2) this.addDrop(data.get(i), element.mWeight);
                else this.addDrop(data.get(i), element.mWeight / 8f);
            }
        };
    }

    private Predicate<GT_Worldgen_GT_Ore_SmallPieces> makeSmallOresPredicate() {
        World world = this.getBaseMetaTileEntity()
            .getWorld();
        return switch (world.provider.dimensionId) {
            case -1 -> gt_worldgen -> gt_worldgen.mNether;
            case 0 -> gt_worldgen -> gt_worldgen.mOverworld;
            case 1 -> gt_worldgen -> gt_worldgen.mEnd;
            /*
             * explicitely giving different dim numbers so it default to false in the config, keeping compat with the
             * current worldgen config
             */
            case 7 -> gt_worldgen -> gt_worldgen.isGenerationAllowed(world, 0, 7);
            default -> throw new IllegalStateException();
        };
    }

    private void getDropsVanillaSmallOres(Predicate<GT_Worldgen_GT_Ore_SmallPieces> smallOresPredicate) {
        GT_Worldgen_GT_Ore_SmallPieces.sList.stream()
            .filter(gt_worldgen -> gt_worldgen.mEnabled && smallOresPredicate.test(gt_worldgen))
            .forEach(element -> this.addDrop(new Pair<>((int) element.mMeta, false), element.mAmount));
    }

    private void getDropsVanillaVeins(Predicate<GT_Worldgen_GT_Ore_Layer> oreLayerPredicate) {
        GT_Worldgen_GT_Ore_Layer.sList.stream()
            .filter(gt_worldgen -> gt_worldgen.mEnabled && oreLayerPredicate.test(gt_worldgen))
            .forEach(element -> {
                this.addDrop(new Pair<>((int) element.mPrimaryMeta, false), element.mWeight);
                this.addDrop(new Pair<>((int) element.mSecondaryMeta, false), element.mWeight);
                this.addDrop(new Pair<>((int) element.mSporadicMeta, false), element.mWeight / 8f);
                this.addDrop(new Pair<>((int) element.mBetweenMeta, false), element.mWeight / 8f);
            });
    }

    private Predicate<GT_Worldgen_GT_Ore_Layer> makeOreLayerPredicate() {
        World world = this.getBaseMetaTileEntity()
            .getWorld();
        return switch (world.provider.dimensionId) {
            case -1 -> gt_worldgen -> gt_worldgen.mNether;
            case 0 -> gt_worldgen -> gt_worldgen.mOverworld;
            case 1 -> gt_worldgen -> gt_worldgen.mEnd || gt_worldgen.mEndAsteroid;
            /*
             * explicitely giving different dim numbers so it default to false in the config, keeping compat with the
             * current worldgen config
             */
            case 7 -> gt_worldgen -> gt_worldgen.isGenerationAllowed(world, 0, 7);
            default -> throw new IllegalStateException();
        };
    }

    private void addDrop(Pair<Integer, Boolean> key, float value) {
        final ItemStack ore = this.getOreItemStack(key);
        if (ConfigHandler.voidMinerBlacklist.contains(
            String.format(
                "%s:%d",
                GameRegistry.findUniqueIdentifierFor(ore.getItem())
                    .toString(),
                ore.getItemDamage())))
            return;
        if (!this.dropmap.containsKey(key)) this.dropmap.put(key, value);
        else this.dropmap.put(key, this.dropmap.get(key) + value);
    }

    private ItemStack getOreItemStack(Pair<Integer, Boolean> stats) {
        return new ItemStack(
            stats.getValue() ? WerkstoffLoader.BWOres : GregTech_API.sBlockOres1,
            multiplier,
            stats.getKey());
    }

    // endregion

    @Override
    public boolean isRotationChangeAllowed() {
        return false;
    }

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
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    // endregion

    // region General

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("test")
            .addInfo("testing")
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(3, 3, 3, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 2)
            .addOutputBus(TextLocalization.textUseBlueprint, 2)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 3)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {

            if (aActive) {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(SPACE_ELEVATOR_BASE_CASING_INDEX),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_DTPF_ON)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FUSION1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(SPACE_ELEVATOR_BASE_CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_OFF)
                    .extFacing()
                    .build() };
        }

        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(SPACE_ELEVATOR_BASE_CASING_INDEX) };
    }
    // endregion

}
