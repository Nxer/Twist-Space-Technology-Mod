package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.util.TstSharedLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

// TODO: Remove this transitional controller and its conversion recipes in the next version.
@IMetaTileEntity.SkipGenerateDescription
public class TST_SwelegfyrBlastFurnaceLegacy extends TST_SwelegfyrBlastFurnace {

    public TST_SwelegfyrBlastFurnaceLegacy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_SwelegfyrBlastFurnaceLegacy(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_SwelegfyrBlastFurnaceLegacy(this.mName);
    }

    // Structure before the casing and coil layout update; keep the old hatch positions.
    // spotless:off
    private static final String[][] OLD_MAIN_T1 = new String[][]{
        {"           ","           ","   NNNNN   ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","   NNNNN   ","           ","           "},
        {"           ","   NNNNN   ","  NHHHHHN  "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN ","  NHHHHHN  ","   NNNNN   ","           "},
        {"   FFFFF   ","  FMMMMMF  "," FMFKKKFMF ","FMFEEEEEFMF","FMKEEEEEKMF","FMKEECEEKMF","FMKEEEEEKMF","FMFEEEEEFMF"," FMFKKKFMF ","  FMMMMMF  ","   FFFFF   "},
        {"           ","   HHHHH   ","  HFFFFFH  "," HFEEEEEFH "," HFEEEEEFH "," HFEECEEFH "," HFEEEEEFH "," HFEEEEEFH ","  HFFFFFH  ","   HHHHH   ","           "},
        {"           ","   J   J   ","  BFAAAFB  "," JFEEEEEFJ ","  AEEEEEA  ","  AEECEEA  ","  AEEEEEA  "," JFEEEEEFJ ","  BFAAAFB  ","   J   J   ","           "},
        {"           ","   J   J   ","  BFAAAFB  "," JFEEEEEFJ ","  AEEEEEA  ","  AEECEEA  ","  AEEEEEA  "," JFEEEEEFJ ","  BFAAAFB  ","   J   J   ","           "},
        {"           ","   HHHHH   ","  HFFFFFH  "," HFEEEEEFH "," HFEEEEEFH "," HFEECEEFH "," HFEEEEEFH "," HFEEEEEFH ","  HFFFFFH  ","   HHHHH   ","           "},
        {"   FFFFF   ","  FMMMMMF  "," FMFJJJFMF ","FMFEEEEEFMF","FMJEEEEEJMF","FMJEECEEJMF","FMJEEEEEJMF","FMFEEEEEFMF"," FMFJJJFMF ","  FMMMMMF  ","   FFFFF   "},
        {"           ","   JJ JJ   ","  JNNNNNJ  "," JNEDDDDNJ ","JJNDEEEDNJ ","  NDECEDN  ","JJNDEEEDNJ "," JNEDDDENJ ","  JNNNNNJ  ","   JJ JJ   ","           "},
        {"           ","    J J    ","   NHNHN   ","  NEDDDEN  "," JHDEEEDHJ ","  NDECEDN  "," JHDEEEDHJ ","  NEDDDEN  ","   NHNHN   ","    J J    ","           "},
        {"           ","    J J    ","   NHNHN   ","  NEDDDEN  "," JHDEEEDHJ ","  NDECEDN  "," JHDEEEDHJ ","  NEDDDEN  ","   NHNHN   ","    J J    ","           "},
        {"           ","           ","   NHNHN   ","  NEDDDEN  ","  HDEEEDH  ","  NDECEDN  ","  HDEEEDH  ","  NEDDDEN  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NHNHN   ","  NEDDDEN  ","  HDEEEDH  ","  NDECEDN  ","  HDEEEDH  ","  NEDDDEN  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NHNHN   ","  NEDDDEN  ","  HDEEEDH  ","  NDECEDN  ","  HDEEEDH  ","  NEDDDEN  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NNNNN   ","  NNDDDNN  ","  NDEEEDN  ","  NDECEDN  ","  NDEEEDN  ","  NNDDDNN  ","   NNNNN   ","           ","           "},
        {"           ","   NNNNN   ","  N     N  "," N  DDD  N "," N DEEED N "," N DECED N "," N DEEED N "," N  DDD  N ","  N     N  ","   NNNNN   ","           "},
        {"           ","   MOOOM   ","  M     M  "," M  MMM  M "," M MEEEM M "," M MECEM M "," M MEEEM M "," M  MMM  M ","  M     M  ","   MMMMM   ","           "},
        {"           ","   MO~OM   ","  A     A  "," A  KKK  A "," A KEEEK A "," A KECEK A "," A KEEEK A "," A  KKK  A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   MOSOM   ","  M     M  "," M  MMM  M "," M MEEEM M "," M MECEM M "," M MEEEM M "," M  MMM  M ","  M     M  ","   MMMMM   ","           "},
        {"           ","   PPPPP   ","  PDDDDDP  "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP ","  PDDDDDP  ","   PPPPP   ","           "}
    };

    private static final String[][] OLD_MAIN_T2 = new String[][]{
        {"                      ","                      ","   NNNNN              ","  NNNNNNN             ","  NNNNNNN             ","  NNNNNNN             ","  NNNNNNN             ","  NNNNNNN             ","   NNNNN              ","                      ","                      "},
        {"                      ","   NNNNN              ","  NHHHHHN             "," NHDDDDDHN       NNN  "," NHDDDDDHN      NNNNN "," NHDDDDDHN      NNNNN "," NHDDDDDHN      NNNNN "," NHDDDDDHN       NNN  ","  NHHHHHN             ","   NNNNN              ","                      "},
        {"   FFFFF              ","  FMMMMMF             "," FMFKKKFMF      GGGGG ","FMFEEEEEFMF    GGGGGGG","FMKEEEEEKMFJJJJGGGGGGG","FMKEECEEKMF    GGGGGGG","FMKEEEEEKMFJJJJGGGGGGG","FMFEEEEEFMF    GGGGGGG"," FMFKKKFMF      GGGGG ","  FMMMMMF             ","   FFFFF              "},
        {"                      ","   HHHHH              ","  HFFFFFH        HHH  "," HFEEEEEFH      HNNNH "," HFEEEEEFHHHHHHHNLLLNH"," HFEECEEFNNNNNNNNLLLNh"," HFEEEEEFHHHHHHHNLLLNH"," HFEEEEEFH      HNNNH ","  HFFFFFH        HHH  ","   HHHHH              ","                      "},
        {"                      ","   J   J              ","  BFAAAFB             "," JFEEEEEFJ      GAAAG ","  AEEEEEHAAAAAAAA   A ","  AEECEEN       - L a ","  AEEEEEHAAAAAAAA   A "," JFEEEEEFJ      GAAAG ","  BFAAAFB             ","   J   J              ","                      "},
        {"                      ","   J   J              ","  BFAAAFB             "," JFEEEEEFJ      GAAAG ","  AEEEEEHAAAAAAAA   A ","  AEECEEN       - L a ","  AEEEEEHAAAAAAAA   A "," JFEEEEEFJ      GAAAG ","  BFAAAFB             ","   J   J              ","                      "},
        {"                      ","   HHHHH              ","  HFFFFFH        HHH  "," HFEEEEEFH      HNNNH "," HFEEEEEFHHHHHHHN   NH"," HFEECEEFNNNNNNNN L Nh"," HFEEEEEFHHHHHHHN   NH"," HFEEEEEFH      HNNNH ","  HFFFFFH        HHH  ","   HHHHH              ","                      "},
        {"   FFFFF              ","  FMMMMMF             "," FMFJJJFMF       GGG  ","FMFEEEEEFMF     GAAAG ","FMJEEEEEJMFJJJJGA   AG","FMJEECEEJMF    GN L NG","FMJEEEEEJMFJJJJGA   AG","FMFEEEEEFMF     GAAAG "," FMFJJJFMF       GGG  ","  FMMMMMF             ","   FFFFF              "},
        {"                      ","   JJ JJ              ","  JNNNNNJ        JJJ  "," JNEDDDDNJ      JAAAJ ","JJNDEEEDNJJ    JA   AJ","  NDECEDN      JN L NJ","JJNDEEEDNJJ    JA   AJ"," JNEDDDENJ      JAAAJ ","  JNNNNNJ        JJJ  ","   JJ JJ              ","                      "},
        {"                      ","    J J               ","   NHNHN              ","  NEDDDEN       BAAAB "," JHDEEEDHJ      A   A ","  NDECEDN       N L N "," JHDEEEDHJ      A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","    J J               ","                      "},
        {"                      ","    J J               ","   NHNHN              ","  NEDDDEN       BAAAB "," JHDEEEDHJ      A   A ","  NDECEDN       N L N "," JHDEEEDHJ      A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","    J J               ","                      "},
        {"                      ","                      ","   NHNHN              ","  NEDDDEN       BAAAB ","  HDEEEDH       A   A ","  NDECEDN       N L N ","  HDEEEDH       A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","                      ","                      "},
        {"                      ","                      ","   NHNHN              ","  NEDDDEN       BAAAB ","  HDEEEDH       A   A ","  NDECEDN       N L N ","  HDEEEDH       A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","                      ","                      "},
        {"                      ","                      ","   NHNHN              ","  NEDDDEN       BAAAB ","  HDEEEDH       A   A ","  NDECEDN       N L N ","  HDEEEDH       A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","                      ","                      "},
        {"                      ","                      ","   NNNNN              ","  NNDDDNN       BAAAB ","  NDEEEDN       A   A ","  NDECEDN       N L N ","  NDEEEDN       A   A ","  NNDDDNN       BAAAB ","   NNNNN              ","                      ","                      "},
        {"                      ","   NNNNN              ","  N     N       GGGGG "," N  DDD  N     GGNNNGG"," N DEEED N     GN   NG"," N DECED N     GN L NG"," N DEEED N     GN   NG"," N  DDD  N     GGNNNGG","  N     N       GGGGG ","   NNNNN              ","                      "},
        {"                      ","   MOOOM              ","  M     M       BGIGB "," M  MMM  M     BHDIDHB"," M MEEEM M     GD   DG"," M MECEM M     II L II"," M MEEEM M     GD   DG"," M  MMM  M     BHDIDHB","  M     M       BGIGB ","   MMMMM              ","                      "},
        {"                      ","   MO~OM              ","  A     A       BNSNB "," A  KKK  A     BHDDDHB"," A KEEEK A     ND   DN"," A KECEK A     ND L DN"," A KEEEK A     ND   DN"," A  KKK  A     BHDDDHB","  A     A       BNONB ","   AAAAA              ","                      "},
        {"                      ","   MOOOM              ","  M     M       BGIGB "," M  MMM  M     BHDIDHB"," M MEEEM M     GD   DG"," M MECEM M     II L II"," M MEEEM M     GD   DG"," M  MMM  M     BHDIDHB","  M     M       BGIGB ","   MMMMM              ","                      "},
        {"                      ","   PPPPP              ","  PDDDDDP       GGGGG "," PDDDDDDDP     GGNNNGG"," PDDDDDDDP     GNNNNNG"," PDDDDDDDP     GNNNNNG"," PDDDDDDDP     GNNNNNG"," PDDDDDDDP     GGNNNGG","  PDDDDDP       GGGGG ","   PPPPP              ","                      "}
    };
    // spotless:on

    private static IStructureDefinition<TST_SwelegfyrBlastFurnace> oldStructureDefinition;

    @Override
    public IStructureDefinition<TST_SwelegfyrBlastFurnace> getStructureDefinition() {
        if (oldStructureDefinition == null) {
            oldStructureDefinition = StructureDefinition.<TST_SwelegfyrBlastFurnace>builder()
                .addShape(STRUCTURE_PIECE_MAIN_T1, transpose(OLD_MAIN_T1))
                .addShape(STRUCTURE_PIECE_MAIN_T2, transpose(OLD_MAIN_T2))
                .addShape(STRUCTURE_PIECE_Blaze_T1, transpose(shapeBlazeT1))
                .addShape(STRUCTURE_PIECE_Blaze_T2, transpose(shapeBlazeT2))
                .addElement('-', isAir())
                .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
                .addElement('a', ofChain(chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier), isAir()))
                .addElement('B', ofBlock(GameRegistry.findBlock(Mods.IndustrialCraft2.ID, "blockFenceIron"), 0))
                .addElement('C', ofBlock(compactFusionCoil, 0))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings1, 11))
                .addElement(
                    'E',
                    withChannel(
                        "coil",
                        ofCoil(TST_SwelegfyrBlastFurnace::setCoilLevel, TST_SwelegfyrBlastFurnace::getCoilLevel)))
                .addElement('F', ofBlock(GregTechAPI.sBlockCasings8, 5))
                .addElement('G', ofBlock(TstBlocks.MetaBlockCasing02, 2))
                .addElement('H', ofBlock(GregTechAPI.sBlockCasings8, 10))
                .addElement(
                    'h',
                    ofChain(ofBlock(GregTechAPI.sBlockCasings8, 10), ofBlock(TstBlocks.MetaBlockCasing01, 15)))
                .addElement('I', ofFrame(Materials.Neutronium))
                .addElement('J', ofFrame(Materials.NaquadahAlloy))
                .addElement('K', ofFrame(Materials.CosmicNeutronium))
                .addElement('L', ofBlock(ModBlocks.blockCustomMachineCasings, 3))
                .addElement('M', ofBlock(ModBlocks.blockCasingsMisc, 14))
                .addElement('N', ofBlock(TstBlocks.MetaBlockCasing01, 15))
                .addElement(
                    'O',
                    HatchElementBuilder.<TST_SwelegfyrBlastFurnace>builder()
                        .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy))
                        .adder(TST_SwelegfyrBlastFurnace::addToMachineList)
                        .hint(1)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                        .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
                .addElement(
                    'P',
                    HatchElementBuilder.<TST_SwelegfyrBlastFurnace>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(TST_SwelegfyrBlastFurnace::addToMachineList)
                        .hint(2)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                        .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
                .addElement(
                    'S',
                    buildHatchAdder(TST_SwelegfyrBlastFurnace.class).hatchClass(MTEHatchInput.class)
                        .adder(TST_SwelegfyrBlastFurnace::addBlazeHatch)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                        .hint(3)
                        .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
                .addElement('Z', ofBlock(TFFluids.fluidPyrotheum.getBlock(), 0))
                .build();
        }
        return oldStructureDefinition;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder()
            .addDeprecatedLine(TstSharedLocalization.MachineTooltip.temporaryController())
            .toolTipFinisher(ModName);
    }
}
