package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.DurationPerMining_StarcoreMiner;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Eut_StarcoreMiner;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SPACE_ELEVATOR_BASE_CASING_INDEX;
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

import java.util.Map;
import java.util.UUID;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.bartimaeusnek.crossmod.galacticgreg.VoidMinerUtility;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;

import galaxyspace.core.register.GSBlocks;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public class TST_StarcoreMiner extends GTCM_MultiMachineBase<TST_StarcoreMiner> implements IGlobalWirelessEnergy {

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
    private short y_value = -1;

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
        if (y_value == -1) {
            y_value = aBaseMetaTileEntity.getYCoord();
        }
        if (y_value > ValueEnum.HeightValueLimit_StarcoreMiner) {
            // if controller block is at location higher than 20
            // continue check pipe structure
            int needPiece = y_value - ValueEnum.HeightValueLimit_StarcoreMiner;
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

        isWirelessMode = this.mEnergyHatches.isEmpty() && this.mExoticEnergyHatches.isEmpty();
        return true;
    }

    // spotless:on
    // endregion

    // region Processing Logic

    protected VoidMinerUtility.DropMap dropMap = null;
    protected VoidMinerUtility.DropMap extraDropMap = null;
    protected float totalWeight = 0;
    protected boolean isWirelessMode = false;
    protected UUID ownerUUID;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("isWirelessMode", isWirelessMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        isWirelessMode = aNBT.getBoolean("isWirelessMode");
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        if (dropMap == null || totalWeight == 0) {
            TwistSpaceTechnology.LOG.info(
                "WARNING! Starcore Miner dropmap = null when checkProcessing ! Dim = "
                    + getBaseMetaTileEntity().getWorld().provider.dimensionId);
            if (Config.DebugMode_StarcoreMiner) {
                TwistSpaceTechnology.LOG.info("Trying to re-generate drop map.");

                // TODO Debug Regeneration
            }
        }

        if (this.totalWeight != 0.f) {

            if (isWirelessMode) {
                lEUt = 0;
                if (!addEUToGlobalEnergyMap(ownerUUID, -1L * DurationPerMining_StarcoreMiner * Eut_StarcoreMiner)) {
                    this.stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                    return CheckRecipeResultRegistry
                        .insufficientPower((long) DurationPerMining_StarcoreMiner * Eut_StarcoreMiner);
                }
            } else {
                lEUt = -Eut_StarcoreMiner;
            }

            mOutputItems = new ItemStack[ValueEnum.AmountOfOreStackPerMining_StarcoreMiner];
            for (int i = 0; i < ValueEnum.AmountOfOreStackPerMining_StarcoreMiner; i++) {
                mOutputItems[i] = generateOneStackOre();
            }
            updateSlots();
            mMaxProgresstime = DurationPerMining_StarcoreMiner;
            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;

            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        this.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        ownerUUID = aBaseMetaTileEntity.getOwnerUuid();

        if (aBaseMetaTileEntity.isServerSide()) {
            initDropMap();
        }

    }

    protected void initDropMap() {
        this.dropMap = new VoidMinerUtility.DropMap();
        this.extraDropMap = new VoidMinerUtility.DropMap();
        int id = this.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId;
        this.handleModDimDef(id);
        this.handleExtraDrops(id);
        this.totalWeight = dropMap.getTotalWeight() + extraDropMap.getTotalWeight();
    }

    /**
     * Gets the DropMap of the dim for the specified dim id
     *
     * @param id the dim number
     */
    private void handleModDimDef(int id) {
        if (VoidMinerUtility.dropMapsByDimId.containsKey(id)) {
            this.dropMap = VoidMinerUtility.dropMapsByDimId.get(id);
        } else {
            String chunkProviderName = ((ChunkProviderServer) this.getBaseMetaTileEntity()
                .getWorld()
                .getChunkProvider()).currentChunkProvider.getClass()
                    .getName();

            if (VoidMinerUtility.dropMapsByChunkProviderName.containsKey(chunkProviderName)) {
                this.dropMap = VoidMinerUtility.dropMapsByChunkProviderName.get(chunkProviderName);
            }
        }
    }

    /**
     * Handles the ores added manually with {@link VoidMinerUtility#addMaterialToDimensionList}
     *
     * @param id the specified dim id
     */
    private void handleExtraDrops(int id) {
        if (VoidMinerUtility.extraDropsDimMap.containsKey(id)) {
            extraDropMap = VoidMinerUtility.extraDropsDimMap.get(id);
        }
    }

    private ItemStack generateOneStackOre() {
        ItemStack nextOre = nextOre();
        nextOre.stackSize = ValueEnum.StackSizeOfEveryOreItemStackWhenMining_StarcoreMiner;
        return nextOre;
    }

    /**
     * method used to pick the next ore in the dropMap.
     *
     * @return the chosen ore
     */
    private ItemStack nextOre() {
        float currentWeight = 0.f;
        while (true) {
            float randomNumber = XSTR.XSTR_INSTANCE.nextFloat() * this.totalWeight;
            for (Map.Entry<GT_Utility.ItemId, Float> entry : this.dropMap.getInternalMap()
                .entrySet()) {
                currentWeight += entry.getValue();
                if (randomNumber < currentWeight) return entry.getKey()
                    .getItemStack();
            }
            for (Map.Entry<GT_Utility.ItemId, Float> entry : this.extraDropMap.getInternalMap()
                .entrySet()) {
                currentWeight += entry.getValue();
                if (randomNumber < currentWeight) return entry.getKey()
                    .getItemStack();
            }
        }
    }

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
        // #tr VoidMiner
        // # Void Miner
        // #zh_CN 虚空采矿场
        tt.addMachineType(TextEnums.tr("VoidMiner"))
            // #tr Tooltip_Starcore_01
            // # {\ITALIC} " Do you sell them ? "
            // #zh_CN {\ITALIC} " Do you sell them ? "
            .addInfo(TextEnums.tr("Tooltip_Starcore_01"))
            // #tr Tooltip_Starcore_02
            // # {\ITALIC} " I'm afraid not. "
            // #zh_CN {\ITALIC} " I'm afraid not. "
            .addInfo(TextEnums.tr("Tooltip_Starcore_02"))
            // #tr Tooltip_Starcore_03
            // # {\ITALIC} " But, maybe we could make a deal ? "
            // #zh_CN {\ITALIC} " But, maybe we could make a deal ? "
            .addInfo(TextEnums.tr("Tooltip_Starcore_03"))
            // #tr Tooltip_Starcore_04
            // # {\SPACE}
            // #zh_CN {\SPACE}
            .addInfo(TextEnums.tr("Tooltip_Starcore_04"))
            // #tr Tooltip_Starcore_05
            // # To the depths, to gather for you the deepest riches of the planet.
            // #zh_CN 直达深处, 为你采集这个星球最深层的财富.
            .addInfo(TextEnums.tr("Tooltip_Starcore_05"))
            // #tr Tooltip_Starcore_06
            // # Each run produces 24 types of ore, 131072 of each type,
            // #zh_CN 每次运行产出 24 种矿石, 每种 131072个,
            .addInfo(TextEnums.tr("Tooltip_Starcore_06"))
            // #tr Tooltip_Starcore_07
            // # {\SPACE}{\SPACE}and takes 6.4s, 2,013,265,920 EU/t (MAX).
            // #zh_CN {\SPACE}{\SPACE}耗时 6.4s, 耗电 2,013,265,920 EU/t (MAX).
            .addInfo(TextEnums.tr("Tooltip_Starcore_07"))
            // #tr Tooltip_Starcore_08
            // # The mining access portion of the structure needs to extend at least to a height of 20 below.
            // #zh_CN 结构中采矿通道部分需要至少延伸至高度20以下.
            .addInfo(TextEnums.tr("Tooltip_Starcore_08"))
            .addInfo(TextLocalization.Tooltips_JoinWirelessNetWithoutEnergyHatch)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .addStructureInfo(TextLocalization.Tooltip_DoNotNeedMaintenance)
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
