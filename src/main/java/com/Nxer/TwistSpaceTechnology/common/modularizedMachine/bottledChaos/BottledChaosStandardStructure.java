package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineBase.controllerElement;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineBase.hatchElement;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineBase.threadElement;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.structure.error.StructureErrorRegistry.ENERGY_TIER_EXCEED_GLASS;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.block.meta.BottledChaosCapacityBlock;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

public final class BottledChaosStandardStructure extends BottledChaosStructure {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int HORIZONTAL_OFFSET = 14;
    private static final int VERTICAL_OFFSET = 35;
    private static final int DEPTH_OFFSET = 12;
    private static final int CAPACITY_REGION_COUNT = 4;
    private static final int CAPACITY_REGION_BLOCK_COUNT = 64;
    private static final int CAPACITY_LEVEL_COUNT = 5;

    // spotless:off

    // a marks the immediate capacity enclosures and controller-shaft glass; A remains outer glass.
    private static final String[][] SHAPE = {
        {"                             ","                             ","                             ","     JJJJJJ       JJJJJJ     ","    J                   J    ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","                             ","                             ","                             ","                             ","                             ","                             ","                             ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","    J                   J    ","     JJJJJJ       JJJJJJ     ","                             ","                             ","                             "},
        {"                             ","                             ","          J       J          ","     DDDDDDDDDDDDDDDDDDD     ","    D     J       J     D    ","   D      J       J      D   ","   D      J       J      D   ","   D      J       J      D   ","   D     J         J     D   ","   D    J           J    D   ","  JDJJJJ             JJJJDJ  ","   D                     D   ","   D                     D   ","   D                     D   ","   D                     D   ","   D                     D   ","   D                     D   ","   D                     D   ","  JDJJJJ             JJJJDJ  ","   D    J           J    D   ","   D     J         J     D   ","   D      J       J      D   ","   D      J       J      D   ","   D      J       J      D   ","    D     J       J     D    ","     DDDDDDDDDDDDDDDDDDD     ","          J       J          ","                             ","                             "},
        {"                             ","          JJJJJJJJJ          ","     EEEEEEEEEEEEEEEEEEE     ","    DCCCCCCDDDDDDDCCCCCCD    ","   DCFFFFFCDDDDDDDCFFFFFCD   ","  ECFFFFFFCFFFFFFFCFFFFFFCE  ","  ECFFFFFFCDDDDDDDCFFFFFFCE  ","  ECFFFFFFCDDDDDDDCFFFFFFCE  ","  ECFFFFFCDDAAAAADDCFFFFFCE  ","  ECFFFFCDAAAAAAAAADCFFFFCE  "," JECCCCCDAAAAAAAAAAADCCCCCEJ "," JEDDFDDDAAAAAAAAAAADDDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDDAAAAAAAAAAADDDFDDEJ "," JECCCCCDAAAAAAAAAAADCCCCCEJ ","  ECFFFFCDAAAAAAAAADCFFFFCE  ","  ECFFFFFCDDAAAAADDCFFFFFCE  ","  ECFFFFFFCDDDDDDDCFFFFFFCE  ","  ECFFFFFFCDDDDDDDCFFFFFFCE  ","  ECFFFFFFCFFFFFFFCFFFFFFCE  ","   DCFFFFFCDDDDDDDCFFFFFCD   ","    DCCCCCCDDDDDDDCCCCCCD    ","     EEEEEEEEEEEEEEEEEEE     ","          JJJJJJJJJ          ","                             "},
        {"                             ","          DDDDDDDDD          ","    EDDDDDDEEEEEEEDDDDDDE    ","   ECPNNNNNCCCCCCCNNNNNPCE   ","  ECPN    NI     IN    NPCE  ","  DPN     N       N     NPD  ","  DN      N       N      ND  ","  DN      N       N      ND  ","  DN     N         N     ND  ","  DN    N           N    ND  "," DDNNNNN             NNNNNDD "," DECI                   ICED "," DEC                     CED "," DEC                     CED "," DEC                     CED "," DEC                     CED "," DEC                     CED "," DECI                   ICED "," DDNNNNN             NNNNNDD ","  DN    N           N    ND  ","  DN     N         N     ND  ","  DN      N       N      ND  ","  DN      N       N      ND  ","  DPN     N       N     NPD  ","  ECPN    NI     IN    NPCE  ","   ECPNNNNNCCCCCCCNNNNNPCE   ","    EDDDDDDEEEEEEEDDDDDDE    ","          DDDDDDDDD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDEEEEEEEDDDDDDDE   ","  ECPPNNNNNCDDDDDCNNNNNPPCE  ","  DPPN    NI     IN    NPPD  ","  DPN     N       N     NPD  ","  DN      N       N      ND  ","  DN      N       N      ND  ","  DN     N         N     ND  ","  DN    N           N    ND  "," DDNNNNN             NNNNNDD "," AECI                   ICEA "," AED                     DEA "," AED                     DEA "," AED                     DEA "," AED                     DEA "," AED                     DEA "," AECI                   ICEA "," DDNNNNN             NNNNNDD ","  DN    N           N    ND  ","  DN     N         N     ND  ","  DN      N       N      ND  ","  DN      N       N      ND  ","  DPN     N       N     NPD  ","  DPPN    NI     IN    NPPD  ","  ECPPNNNNNCDDDDDCNNNNNPPCE  ","   EDDDDDDDEEEEEEEDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNJJNNCJJJJJCNNJJNPPCE  ","  DPPN    NI     IN    NPPD  ","  DPN     J       J     NPD  ","  DN      J       J      ND  ","  DJ      J       J      JD  ","  DJ     J         J     JD  ","  DN    J           J    ND  "," DDNNJJJ             JJJNNDD "," AKCI                   ICKA "," A J                     J A "," A J                     J A "," A J                     J A "," A J                     J A "," A J                     J A "," AKCI                   ICKA "," DDNNJJJ             JJJNNDD ","  DN    J           J    ND  ","  DJ     J         J     JD  ","  DJ      J       J      JD  ","  DN      J       J      ND  ","  DPN     J       J     NPD  ","  DPPN    NI     IN    NPPD  ","  ECPPNJJNNCJJJJJCNNJJNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNCDDDDDCNNNNNPPCE  ","  DPPN    NI     IN    NPPD  ","  DPN     N       N     NPD  ","  DN      N       N      ND  ","  DN      N       N      ND  ","  DN     N         N     ND  ","  DN    N           N    ND  "," DDNNNNN             NNNNNDD "," AKCI                   ICKA "," A D                     D A "," A D                     D A "," A D                     D A "," A D                     D A "," A D                     D A "," AKCI                   ICKA "," DDNNNNN             NNNNNDD ","  DN    N           N    ND  ","  DN     N         N     ND  ","  DN      N       N      ND  ","  DN      N       N      ND  ","  DPN     N       N     NPD  ","  DPPN    NI     IN    NPPD  ","  ECPPNNNNNCDDDDDCNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNCQQQQQCNNNNNPPCE  ","  DPPN    NI     IN    NPPD  ","  DPN JJJ N       N JJJ NPD  ","  DN JEEJ N       N JEEJ ND  ","  DN JEEJ N       N JEEJ ND  ","  DN JJJ N         N JJJ ND  ","  DN    N           N    ND  "," DDNNNNN             NNNNNDD "," AKCI                   ICKA "," A Q                     Q A "," A Q                     Q A "," A Q                     Q A "," A Q                     Q A "," A Q                     Q A "," AKCI                   ICKA "," DDNNNNN             NNNNNDD ","  DN    N           N    ND  ","  DN JJJ N         N JJJ ND  ","  DN JEEJ N       N JEEJ ND  ","  DN JEEJ N       N JEEJ ND  ","  DPN JJJ N       N JJJ NPD  ","  DPPN    NI     IN    NPPD  ","  ECPPNNNNNCQQQQQCNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPEEEENCEEEEECNEEEEPPCE  ","  DPPE    EE     EE    EPPD  ","  DPE EEE E       E EEE EPD  ","  DE ECCE E       E ECCE ED  ","  DE ECCE E       E ECCE ED  ","  DE EEE E         E EEE ED  ","  DE    E           E    ED  "," DDNEEEE             EEEENDD "," AKCE                   ECKA "," A E                     E A "," A E                     E A "," A E                     E A "," A E                     E A "," A E                     E A "," AKCE                   ECKA "," DDNEEEE             EEEENDD ","  DE    E           E    ED  ","  DE EEE E         E EEE ED  ","  DE ECCE E       E ECCE ED  ","  DE ECCE E       E ECCE ED  ","  DPE EEE E       E EEE EPD  ","  DPPE    EE     EE    EPPD  ","  ECPPEEEENCEEEEECNEEEEPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPCCCENCCCCCCCNECCCPPCE  ","  DPPC    EC     CE    CPPD  ","  DPC aaa C       C aaa CPD  ","  DC aGGa C       C aGGa CD  ","  DC aGGa C       C aGGa CD  ","  DC aaa C         C aaa CD  ","  DE    C           C    ED  "," DDNECCC             CCCENDD "," AKCC                   CCKA "," A C                     C A "," A C                     C A "," A C                     C A "," A C                     C A "," A C                     C A "," AKCC                   CCKA "," DDNECCC             CCCENDD ","  DE    C           C    ED  ","  DC aaa C         C aaa CD  ","  DC aGGa C       C aGGa CD  ","  DC aGGa C       C aGGa CD  ","  DPC aaa C       C aaa CPD  ","  DPPC    EC     CE    CPPD  ","  ECPPCCCENCCCCCCCNECCCPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPEEEENCEEEEECNEEEEPPCE  ","  DPPE    EE     EE    EPPD  ","  DPE aaa E       E aaa EPD  ","  DE aGGa E       E aGGa ED  ","  DE aGGa E       E aGGa ED  ","  DE aaa E         E aaa ED  ","  DE    E           E    ED  "," DDNEEEE             EEEENDD "," AKCE                   ECKA "," A E                     E A "," A E                     E A "," A E                     E A "," A E                     E A "," A E                     E A "," AKCE                   ECKA "," DDNEEEE             EEEENDD ","  DE    E           E    ED  ","  DE aaa E         E aaa ED  ","  DE aGGa E       E aGGa ED  ","  DE aGGa E       E aGGa ED  ","  DPE aaa E       E aaa EPD  ","  DPPE    EE     EE    EPPD  ","  ECPPEEEENCEEEEECNEEEEPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECPPNNNNNC     CNNNNNPPCE  ","  DPPN    AI     IA    NPPD  ","  DPN aaa A       A aaa NPD  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DN aaa A         A aaa ND  ","  DN    A           A    ND  "," DDNAAAA             AAAANDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDNAAAA             AAAANDD ","  DN    A           A    ND  ","  DN aaa A         A aaa ND  ","  DN aGGa A       A aGGa ND  ","  DN aGGa A       A aGGa ND  ","  DPN aaa A       A aaa NPD  ","  DPPN    AI     IA    NPPD  ","  ECPPNNNNNC     CNNNNNPPCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDEEEEEEEDDDDDDDE   ","  ECPPEEEENC     CNEEEEPPCE  ","  DPPE    ED     DE    EPPD  ","  DPE aaa E       E aaa EPD  ","  DE aGGa E       E aGGa ED  ","  DE aGGa E       E aGGa ED  ","  DE aaa E         E aaa ED  ","  DE    E           E    ED  "," DDNEEEE             EEEENDD "," AECD                   DCEA "," AE                       EA "," AE                       EA "," AE                       EA "," AE                       EA "," AE                       EA "," AECD                   DCEA "," DDNEEEE             EEEENDD ","  DE    E           E    ED  ","  DE aaa E         E aaa ED  ","  DE aGGa E       E aGGa ED  ","  DE aGGa E       E aGGa ED  ","  DPE aaa E       E aaa EPD  ","  DPPE    ED     DE    EPPD  ","  ECPPEEEENC     CNEEEEPPCE  ","   EDDDDDDDEEEEEEEDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DDDDDDDDD          ","    EDDDDDDEEEEEEEDDDDDDE    ","   ECPEEEENCDDDDDCNEEEEPCE   ","  ECPEJJJJED     DEJJJJEPCE  ","  DPEJPPPJE       EJPPPJEPD  ","  DEJPOOPJE       EJPOOPJED  ","  DEJPOOPJE       EJPOOPJED  ","  DEJPPPJE         EJPPPJED  ","  DEJJJJE           EJJJJED  "," DDNEEEE             EEEENDD "," DECD                   DCED "," DED                     DED "," DED                     DED "," DED                     DED "," DED                     DED "," DED                     DED "," DECD                   DCED "," DDNEEEE             EEEENDD ","  DEJJJJE           EJJJJED  ","  DEJPPPJE         EJPPPJED  ","  DEJPOOPJE       EJPOOPJED  ","  DEJPOOPJE       EJPOOPJED  ","  DPEJPPPJE       EJPPPJEPD  ","  ECPEJJJJED     DEJJJJEPCE  ","   ECPEEEENCDDDDDCNEEEEPCE   ","    EDDDDDDEEEEEEEDDDDDDE    ","          DDDDDDDDD          ","                             "},
        {"                             ","          D       D          ","     EEEEEEEEEEEEEEEEEEE     ","    ECCCCCCQQQQQQQCCCCCCE    ","   EQQQQQQQQQQQQQQQQQQQQQE   ","  ECQQQQQQQQQQQQQQQQQQQQQCE  ","  ECQQPPQQQQQQQQQQQQQPPQQCE  ","  ECQQPPQQQQQQQQQQQQQPPQQCE  ","  ECQQQQQQQQQQQQQQQQQQQQQCE  ","  ECQQQQQQQQAAAAAQQQQQQQQCE  "," DECQQQQQQQAAAAAAAQQQQQQQCED ","  EQQQQQQQAAAAAAAAAQQQQQQQE  ","  EQQQQQQAAAAAAAAAAAQQQQQQE  ","  EQQQQQQAAAAAAAAAAAQQQQQQE  ","  EQQQQQQAAAAAAAAAAAQQQQQQE  ","  EQQQQQQAAAAAAAAAAAQQQQQQE  ","  EQQQQQQAAAAAAAAAAAQQQQQQE  ","  EQQQQQQQAAAAAAAAAQQQQQQQE  "," DECQQQQQQQAAAAAAAQQQQQQQCED ","  ECQQQQQQQQAAAAAQQQQQQQQCE  ","  ECQQQQQQQQQQQQQQQQQQQQQCE  ","  ECQQPPQQQQQQQQQQQQQPPQQCE  ","  ECQQPPQQQQQQQQQQQQQPPQQCE  ","  ECQQQQQQQQQQQQQQQQQQQQQCE  ","   EQQQQQQQQQQQQQQQQQQQQQE   ","    ECCCCCCQQQQQQQCCCCCCE    ","     EEEEEEEEEEEEEEEEEEE     ","          D       D          ","                             "},
        {"                             ","          D       D          ","          DDDDDDDDD          ","         DD       DD         ","       DD           DD       ","     DDM             MDD     ","     DCBM           MBCD     ","    DMBBM           MBBMD    ","    D MM             MM D    ","   D                     D   "," DDD                     DDD ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  "," DDD                     DDD ","   D                     D   ","    D MM             MM D    ","    DMBBM           MBBMD    ","     DCBM           MBCD     ","     DDM             MDD     ","       DD           DD       ","         DD       DD         ","          DDDDDDDDD          ","          D       D          ","                             "},
        {"                             ","                             ","           DDDDDDD           ","         DD       DD         ","       DD           DD       ","     DDM             MDD     ","     DCBM           MBCD     ","    DMBBM           MBBMD    ","    D MM             MM D    ","   D                     D   ","   D                     D   ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","   D                     D   ","   D                     D   ","    D MM             MM D    ","    DMBBM           MBBMD    ","     DCBM           MBCD     ","     DDM             MDD     ","       DD           DD       ","         DD       DD         ","           DDDDDDD           ","                             ","                             "},
        {"                             ","                             ","           DDDDDDD           ","         DD       DD         ","       DD           DD       ","     DDM             MDD     ","     DCBM           MBCD     ","    DMBBM           MBBMD    ","    D MMM           MMM D    ","   D                     D   ","   D                     D   ","  D                       D  ","  D                       D  ","  D          NNN          D  ","  D          NNN          D  ","  D          NNN          D  ","  D                       D  ","  D                       D  ","   D                     D   ","   D                     D   ","    D MMM           MMM D    ","    DMBBM           MBBMD    ","     DCBM           MBCD     ","     DDM             MDD     ","       DD           DD       ","         DD       DD         ","           DDDDDDD           ","                             ","                             "},
        {"                             ","                             ","                             ","           EEEEEEE           ","         EE       EE         ","       EE           EE       ","      ECM           MCE      ","     ECBBM         MBBCE     ","     EMBBM         MBBME     ","    E  MM           MM  E    ","    E                   E    ","   EE                   EE   ","   E         NNN         E   ","   E        N   N        E   ","   E        N R N        E   ","   E        N   N        E   ","   E         NNN         E   ","   EE                   EE   ","    E                   E    ","    E  MM           MM  E    ","     EMBBM         MBBME     ","     ECBBM         MBBCE     ","      ECM           MCE      ","       EE           EE       ","         EEE     EEE         ","           EEEEEEE           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           QE   EQ           ","         QQ       QQ         ","       QQ           QQ       ","      QCM           MCQ      ","     QCBBM         MBBCQ     ","     QMBBM         MBBMQ     ","    Q  MM           MM  Q    ","    Q                   Q    ","   QE                   EQ   ","   QE        aaa        EQ   ","   QE       a   a       EQ   ","   QE       a R a       EQ   ","   QE       a   a       EQ   ","   QE        aaa        EQ   ","   QE                   EQ   ","    Q                   Q    ","    Q  MM           MM  Q    ","     QMBBM         MBBMQ     ","     QCBBM         MBBCQ     ","      QCM           MCQ      ","       QQ           QQ       ","         QQEEEEEEEQQ         ","           QQQQQQQ           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           QE   EQ           ","         QQ       QQ         ","       QQ           QQ       ","      QCM           MCQ      ","     QCBBM         MBBCQ     ","     QMBBM         MBBMQ     ","    Q  MM           MM  Q    ","    Q                   Q    ","   QE                   EQ   ","   QU        aaa        UQ   ","   QU       a   a       UQ   ","   QU       a T a       UQ   ","   QU       a   a       UQ   ","   QU        aaa        UQ   ","   QE                   EQ   ","    Q                   Q    ","    Q  MM           MM  Q    ","     QMBBM         MBBMQ     ","     QCBBM         MBBCQ     ","      QCM           MCQ      ","       QQ           QQ       ","         QQEUUUUUEQQ         ","           QQQQQQQ           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           QE   EQ           ","         QQ       QQ         ","       QQ           QQ       ","      QCM           MCQ      ","     QCBBM         MBBCQ     ","     QMBBM         MBBMQ     ","    Q  MM           MM  Q    ","    Q                   Q    ","   QE                   EQ   ","   QV        aaa        VQ   ","   QV       a   a       VQ   ","   QV       a R a       VQ   ","   QV       a   a       VQ   ","   QV        aaa        VQ   ","   QE                   EQ   ","    Q                   Q    ","    Q  MM           MM  Q    ","     QMBBM         MBBMQ     ","     QCBBM         MBBCQ     ","      QCM           MCQ      ","       QQ           QQ       ","         QQEVVVVVEQQ         ","           QQQQQQQ           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           EE   EE           ","         EE       EE         ","       EE           EE       ","      ECM           MCE      ","     ECBBM         MBBCE     ","     EMBBM         MBBME     ","    E  MM           MM  E    ","    E                   E    ","   EE                   EE   ","   EW        NNN        WE   ","   EW       N   N       WE   ","   EW       N R N       WE   ","   EW       N   N       WE   ","   EW        NNN        WE   ","   EE                   EE   ","    E                   E    ","    E  MM           MM  E    ","     EMBBM         MBBME     ","     ECBBM         MBBCE     ","      ECM           MCE      ","       EE           EE       ","         EEEWWWWWEEE         ","           EEEEEEE           ","                             ","                             ","                             "},
        {"                             ","                             ","           DDDDDDD           ","         DDCCEEECCDD         ","       DDCCDDDDDDDCCDD       ","     DDCCDDD     DDDCCDD     ","     DCCCDDJ     JDDCCCD     ","    DCCBBMJ       JMBBCCD    ","    DCCBBMJ       JMBBCCD    ","   DCDDMMJ         JMMDDCD   ","   DCDDJJ           JJDDCD   ","  DCDDJ               JDDCD  ","  DCR       XX~XX       RCD  ","  DCR       XNNNX       RCD  ","  DCR       XNSNX       RCD  ","  DCR       XNNNX       RCD  ","  DCR       XXXXX       RCD  ","  DCDDJ               JDDCD  ","   DCDDJJ           JJDDCD   ","   DCDDMMJ         JMMDDCD   ","    DCCBBMJ       JMBBCCD    ","    DCCBBMJ       JMBBCCD    ","     DCCCDDJ     JDDCCCD     ","     DDCCDDD     DDDCCDD     ","       DDCCDRRRRRDCCDD       ","         DDCCCCCCCDD         ","           DDDDDDD           ","                             ","                             "},
        {"          L       L          ","   LLLLLLLLLLLLLLLLLLLLLLL   ","  LLLLLLLLLLLLLLLLLLLLLLLLL  "," LLLLLLLLLLQQQQQQQLLLLLLLLLL "," LLLLLLLLQCHDDDDDHQQLLLLLLLL "," LLLLLLQQHHHDDDDDHHHQQLLLLLL "," LLLLLQNNHHEJDDDJEHHNNQLLLLL "," LLLLQNBBNE JDDDJ ENBBNQLLLL "," LLLLQNBBNE JDDDJ ENBBNQLLLL "," LLLQHHNNM  JDDDJ  MNNHHQLLL ","LLLLQHHEE  JDDDDDJ  EEHHCLLLL"," LLQHHE   JDDHHHDDJ   EHHQLL "," LLQHDJJJJDDHHHHHDDJJJJDHQLL "," LLQHDDDDDDHHHHHHHDDDDDDHQLL "," LLQHHHHHHHHHHOHHHHHHHHHHQLL "," LLQHDDDDDDHHHHHHHDDDDDDHQLL "," LLQHDJJJJDDHHHHHDDJJJJDHQLL "," LLQHHE   JDDHHHDDJ   EHHQLL ","LLLLCHHEE  JDDHDDJ  EEHHQLLLL"," LLLQHHNNM  JDHDJ  MNNHHQLLL "," LLLLQNBBNE JDHDJ ENBBNQLLLL "," LLLLQNBBNE JDHDJ ENBBNQLLLL "," LLLLLQNNHHEJDHDJEHHNNQLLLLL "," LLLLLLQQHHHDDHDDHHHQQLLLLLL "," LLLLLLLLQQHHHHHHHCQLLLLLLLL "," LLLLLLLLLLQQQQQQQLLLLLLLLLL ","  LLLLLLLLLLLLLLLLLLLLLLLLL  ","   LLLLLLLLLLLLLLLLLLLLLLL   ","          L       L          "},
        {"          L       L          ","   LLLLLLLLLLLLLLLLLLLLLLL   ","  LLLLLLLLLLLLLLLLLLLLLLLLL  "," LLLLLLLLLLQQQQQQQLLLLLLLLLL "," LLLLLLLLQQLLLLLLLQQLLLLLLLL "," LLLLLLQQLLLLLLLLLLLQQLLLLLL "," LLLLLQNNLLDDDDDDDLLNNQLLLLL "," LLLLQNBBNDDDDDDDDDNBBNQLLLL "," LLLLQNBBNDDDDDDDDDNBBNQLLLL "," LLLQLLNNNMDDDDDDDDNNNLLQLLL ","LLLLQLLDDDMMNCCCCDMMDDLLQLLLL"," LLQLLDDDDDCNCCCCCMDDDDLLQLL "," LLQLLDMDDCCNNNCNNNDDMDLLQLL "," LLQLLDNDDCCCCMCNCCDDNDLLQLL "," LLQLLDNDDCCNMOMNCCDDNDLLQLL "," LLQLLDNDDCCNCMCCCCDDNDLLQLL "," LLQLLDMDDNNNCNNNCCDDMDLLQLL "," LLQLLDDDDMCCCCCNCDDDDDLLQLL ","LLLLQLLDDMMDCCCCNMMDDDLLQLLLL"," LLLQLLNNNDDDDDDDDMNNNLLQLLL "," LLLLQNBBNDDDDDDDDDNBBNQLLLL "," LLLLQNBBNDDMNNNMDDNBBNQLLLL "," LLLLLQNNLLDDDDDDDLLNNQLLLLL "," LLLLLLQQLLLLLLLLLLLQQLLLLLL "," LLLLLLLLQQLLLLLLLQQLLLLLLLL "," LLLLLLLLLLQQQQQQQLLLLLLLLLL ","  LLLLLLLLLLLLLLLLLLLLLLLLL  ","   LLLLLLLLLLLLLLLLLLLLLLL   ","          L       L          "},
        {"   LLLLLLLLLLLLLLLLLLLLLLL   ","  LLLLLLLLLLLLLLLLLLLLLLLLL  "," LLLLLLLLLLLLLLLLLLLLLLLLLLL ","LLLLLLLLLLLQQQQQQQLLLLLLLLLLL","LLLLLLLLLQQLLLLLLLQQLLLLLLLLL","LLLLLLLQQLLLLLLLLLLLQQLLLLLLL","LLLLLLQNNLLLLLLLLLLLNNQLLLLLL","LLLLLQNBBNLLLLLLLLLNBBNQLLLLL","LLLLLQNBBNLLLLLLLLNBBBNQLLLLL","LLLLQLLNBBBNNLLLLLNBNNLLQLLLL","LLLLQLLLNNBBBNCCCNBBLLLLQLLLL","LLLQLLLLLLNNBNNCNNBNLLLLLQLLL","LLLQLLLLLLCNBBBNBBBNLLLLLQLLL","LLLQLLLLLLCCNNBNBNNLLLLLLQLLL","LLLQLLLLLLCNBBBBBNCLLLLLLQLLL","LLLQLLLLLLNNBNBNNCCLLLLLLQLLL","LLLQLLLLLNBBBNBBBNCLLLLLLQLLL","LLLQLLLLLNBNNCNNBNNLLLLLLQLLL","LLLLQLLLLBBNCCCNBBBNNLLLQLLLL","LLLLQLLNNBNLLLLLNNBBBNLLQLLLL","LLLLLQNBBBNLLLLLLLLNBBNQLLLLL","LLLLLQNBBNLLLLLLLLLNBBNQLLLLL","LLLLLLQNNLLLLLLLLLLLNNQLLLLLL","LLLLLLLQQLLLLLLLLLLLQQLLLLLLL","LLLLLLLLLQQLLLLLLLQQLLLLLLLLL","LLLLLLLLLLLQQQQQQQLLLLLLLLLLL"," LLLLLLLLLLLLLLLLLLLLLLLLLLL ","  LLLLLLLLLLLLLLLLLLLLLLLLL  ","   LLLLLLLLLLLLLLLLLLLLLLL   "},
        {"   LLLLLLLLLLLLLLLLLLLLLLL   ","  LLLLLLLLLLLLLLLLLLLLLLLLL  "," LLLLLLLLLLLLLLLLLLLLLLLLLLL ","LLLLLLLLLLLQQQQQQQLLLLLLLLLLL","LLLLLLLLLQQQQQQQQQQQLLLLLLLLL","LLLLLLLQQQQQQQQQQQQQQQLLLLLLL","LLLLLLQQQQQQQQQQQQQQQQQLLLLLL","LLLLLQQNNQQQQQQQQQQQNNQQLLLLL","LLLLLQQNNQQQQQQQQQQNNNQQLLLLL","LLLLQQQQNNNQQQQQQQQNQQQQQLLLL","LLLLQQQQQQNNNCCCCQNNQQQQQLLLL","LLLQQQQQQQQCNCCCCCNQQQQQQQLLL","LLLQQQQQQQCCNMMCNNNQQQQQQQLLL","LLLQQQQQQQCCCCMCMCCQQQQQQQLLL","LLLQQQQQQQCCMMDMMCCQQQQQQQLLL","LLLQQQQQQQCCMCMCCCCQQQQQQQLLL","LLLQQQQQQQNNNCMMNCCQQQQQQQLLL","LLLQQQQQQQNCCCCCNCQQQQQQQQLLL","LLLLQQQQQNNQCCCCNNNQQQQQQLLLL","LLLLQQQQQNQQQQQQQQNNNQQQQLLLL","LLLLLQQNNNQQQQQQQQQQNNQQLLLLL","LLLLLQQNNQQQQQQQQQQQNNQQLLLLL","LLLLLLQQQQQQQQQQQQQQQQQLLLLLL","LLLLLLLQQQQQQQQQQQQQQQLLLLLLL","LLLLLLLLLQQQQQQQQQQQLLLLLLLLL","LLLLLLLLLLLQQQQQQQLLLLLLLLLLL"," LLLLLLLLLLLLLLLLLLLLLLLLLLL ","  LLLLLLLLLLLLLLLLLLLLLLLLL  ","   LLLLLLLLLLLLLLLLLLLLLLL   "},
        {"   LLLLLLLLLLLLLLLLLLLLLLL   ","  LLLLLLLLLLLLLLLLLLLLLLLLL  "," LLLLLLLLLLLLLLLLLLLLLLLLLLL ","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLCCCCCLLLLLLLLLLLL","LLLLLLLLLLLCCYYYCCLLLLLLLLLLL","LLLLLLLLLLLCYYYYYCLLLLLLLLLLL","LLLLLLLLLLLCYYYYYCLLLLLLLLLLL","LLLLLLLLLLLCYYYYYCLLLLLLLLLLL","LLLLLLLLLLLCCYYYCCLLLLLLLLLLL","LLLLLLLLLLLLCCCCCLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLL"," LLLLLLLLLLLLLLLLLLLLLLLLLLL ","  LLLLLLLLLLLLLLLLLLLLLLLLL  ","   LLLLLLLLLLLLLLLLLLLLLLL   "},
    };
    // spotless:on

    private static IStructureDefinition<MM_BottledChaos> structureDefinition;
    private static MultiblockTooltipBuilder tooltip;
    private int generatorLevel = -1;
    private int minimumUpgradeGlassTier;
    private int maximumUpgradeGlassTier;

    public BottledChaosStandardStructure(MM_BottledChaos machine) {
        super(machine);
    }

    @Override
    public int getProcessingGroupSlot(int relativeLeft, int relativeBack) {
        // Top view, controller front upwards: right #1..5, rear #6..10, left #11..15.
        if (relativeLeft == -10 && relativeBack >= 0 && relativeBack <= 4) return relativeBack;
        if (relativeBack == 12 && relativeLeft >= -2 && relativeLeft <= 2) return relativeLeft + 7;
        if (relativeLeft == 10 && relativeBack >= 0 && relativeBack <= 4) return 14 - relativeBack;
        return -1;
    }

    @Override
    public int getProcessingGroupLimit() {
        return BottledChaosMachineBase.THREAD_COUNT;
    }

    @Override
    public int getCredentialHatchLimit(int parallelTier) {
        return Math.min(getProcessingGroupLimit(), 1 + parallelTier * 2);
    }

    @Override
    public void reset() {
        generatorLevel = -1;
        minimumUpgradeGlassTier = Integer.MAX_VALUE;
        maximumUpgradeGlassTier = 0;
    }

    @Override
    public boolean checkStructurePieces(List<StructureError> errors) {
        return checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET, errors)
            && checkMachineTier(errors);
    }

    private boolean checkMachineTier(List<StructureError> errors) {
        if (minimumUpgradeGlassTier < VoltageIndex.UHV + generatorLevel
            || maximumUpgradeGlassTier != minimumUpgradeGlassTier) {
            // #tr tst.machine.BottledChaos.structure_error.upgrade_glass_mismatch
            // # Shaft and capacity-region glass must share a tier no lower than the machine tier.
            // #zh_CN 主轴与容量区玻璃须同级, 且不得低于机器等级.
            errors.add(StructureErrors.of("tst.machine.BottledChaos.structure_error.upgrade_glass_mismatch"));
            return false;
        }
        for (MTEHatch hatch : machine.mEnergyHatches) {
            if (hatch.mTier > minimumUpgradeGlassTier) {
                errors.add(ENERGY_TIER_EXCEED_GLASS);
                return false;
            }
        }
        for (MTEHatch hatch : machine.getExoticEnergyHatches()) {
            if (hatch.mTier > minimumUpgradeGlassTier) {
                errors.add(ENERGY_TIER_EXCEED_GLASS);
                return false;
            }
        }
        return true;
    }

    @Override
    public StructureError getInvalidCapacityRegionError() {
        // #tr tst.machine.BottledChaos.structure_error.invalid_capacity_region
        // # Leave each capacity region empty or fill it with capacity blocks of one tier.
        // #zh_CN 容量区须留空, 或填满同级容量方块.
        return StructureErrors.of("tst.machine.BottledChaos.structure_error.invalid_capacity_region");
    }

    @Override
    public int getCapacityRegionCount() {
        return CAPACITY_REGION_COUNT;
    }

    @Override
    public int getCapacityRegionBlockCount() {
        return CAPACITY_REGION_BLOCK_COUNT;
    }

    @Override
    public int getCapacityLevelCount() {
        return CAPACITY_LEVEL_COUNT;
    }

    @Override
    public int getCapacityRegion(int x, int y, int z) {
        IGregTechTileEntity base = machine.getBaseMetaTileEntity();
        int dx = x - base.getXCoord();
        int dz = z - base.getZCoord();
        ForgeDirection left = machine.getExtendedFacing()
            .getRelativeLeftInWorld();
        ForgeDirection back = machine.getExtendedFacing()
            .getRelativeBackInWorld();
        // The four fixed columns remain independently tiered after sharing one structure symbol.
        return (dx * left.offsetX + dz * left.offsetZ > 0 ? 1 : 0)
            + (dx * back.offsetX + dz * back.offsetZ > 0 ? 2 : 0);
    }

    @Override
    public boolean isWirelessMode() {
        return generatorLevel >= 4 && machine.mEnergyHatches.isEmpty()
            && machine.getExoticEnergyHatches()
                .isEmpty();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment environment) {
        if (machine.mMachine) return -1;
        // Preview placement must update hatch counts and textures without an incomplete full check clearing them.
        boolean preview = GregTechAPI.isDummyWorld(
            machine.getBaseMetaTileEntity()
                .getWorld());
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            environment,
            preview,
            !preview);
    }

    @Override
    public IStructureDefinition<MM_BottledChaos> getStructureDefinition() {
        if (structureDefinition == null) {
            structureDefinition = StructureDefinition.<MM_BottledChaos>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(SHAPE))
                // GuideNH retains only channels discovered from the registered controller's structure.
                .addChannel(BottledChaosPrototypeStructure.PREVIEW_CHANNEL, 1, 1)
                .addElement('A', Casings.BorosilicateGlassAny.asElement())
                .addElement('a', chainAllGlasses(-1, (machine, tier) -> {
                    ((BottledChaosStandardStructure) machine.getMachineStructure()).minimumUpgradeGlassTier = Math.min(
                        ((BottledChaosStandardStructure) machine.getMachineStructure()).minimumUpgradeGlassTier,
                        tier);
                    ((BottledChaosStandardStructure) machine.getMachineStructure()).maximumUpgradeGlassTier = Math.max(
                        ((BottledChaosStandardStructure) machine.getMachineStructure()).maximumUpgradeGlassTier,
                        tier);
                },
                    // Collect all tiers; checkMachine validates uniform glass and machine/energy hatch tier limits.
                    machine -> -1))
                .addElement('B', Casings.SuperconductingCoilBlock.asElement())
                .addElement('C', Casings.ContainmentFieldMachineCasing.asElement())
                .addElement('D', Casings.AdvancedIridiumPlatedMachineCasing.asElement())
                .addElement('E', Casings.RadiantNaquadahAlloyCasing.asElement())
                .addElement('F', Casings.AdvancedFilterCasing.asElement())
                .addElement(
                    'G',
                    withChannel(
                        BottledChaosCapacityBlock.STRUCTURE_CHANNEL,
                        BottledChaosMachineBase.<MM_BottledChaos>capacityElement()))
                .addElement('H', Casings.HighPowerCasing.asElement())
                .addElement('I', Casings.IronFence.asElement())
                .addElement('J', ofFrame(Materials.Neutronium))
                .addElement('K', ofFrame(Materials.NaquadahAlloy))
                .addElement('L', ofBlock(GregTechAPI.sBlockCasingsDyson, 9))
                .addElement('M', ofBlock(TstBlocks.MetaBlockCasing01, 1))
                .addElement('N', ofBlock(TstBlocks.MetaBlockCasing02, 2))
                .addElement('O', ofBlock(TstBlocks.MetaBlockCasing02, 4))
                .addElement('P', ofBlock(TstBlocks.MetaBlockCasing02, 7))
                .addElement('Q', ofBlock(TstBlocks.MetaBlockCasing02, 8))
                .addElement('R', ofBlock(TstBlocks.MetaBlockGlass01, 0))
                .addElement('S', withChannel("energymatrix", generatorElement(TstBlocks.EnergySustainmentMatrix)))
                .addElement(
                    'T',
                    withChannel("fieldgeneratortier", generatorElement(TstBlocks.StabilisationFieldGenerator)))
                .addElement('U', threadElement(0))
                .addElement('V', threadElement(1))
                .addElement('W', threadElement(2))
                .addElement('X', controllerElement())
                .addElement(
                    'Y',
                    hatchElement(
                        HatchElementBuilder.<MM_BottledChaos>builder()
                            .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy)),
                        5))
                .build();
        }
        return structureDefinition;
    }

    @Override
    public long getMaximumRecipeVoltage() {
        return GTValues.V[generatorLevel >= 4 ? VoltageIndex.MAX : VoltageIndex.UHV + Math.max(0, generatorLevel)];
    }

    private static IStructureElement<MM_BottledChaos> generatorElement(Block block) {
        return ofBlocksTiered(
            (candidate, meta) -> candidate == block && meta >= 0 && meta <= 4 ? meta : null,
            ImmutableList
                .of(Pair.of(block, 0), Pair.of(block, 1), Pair.of(block, 2), Pair.of(block, 3), Pair.of(block, 4)),
            -1,
            (machine, level) -> ((BottledChaosStandardStructure) machine.getMachineStructure()).generatorLevel = level,
            machine -> ((BottledChaosStandardStructure) machine.getMachineStructure()).generatorLevel);
    }

    @Override
    public MultiblockTooltipBuilder getTooltip() {
        if (tooltip == null) {
            // spotless:off
            tooltip = new TSTMultiblockTooltipBuilder()
                // #tr tst.machine.BottledChaos.tooltip.machine_type
                // # {\WHITE}Modularized Machine {\GRAY}- {\YELLOW}Production Chain Processor
                // #zh_CN {\WHITE}模块化机械 {\GRAY}- {\YELLOW}生产链处理器
                .addMachineType(TSTUtils.tr("tst.machine.BottledChaos.tooltip.machine_type"))
                // #tr tst.machine.BottledChaos.tooltip.info.1
                // # Controller block for Bottled Chaos
                // #zh_CN 瓶中混沌的控制方块
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.1"))
                // #tr tst.machine.BottledChaos.tooltip.info.2
                // # {\AQUA}{\ITALIC}Countless processes, one bottle.
                // #zh_CN {\AQUA}{\ITALIC}万千工序, 尽在瓶中.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.2"))
                // #tr tst.machine.BottledChaos.tooltip.info.3
                // # {\NULL}
                // #zh_CN {\NULL}
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.3"))
                // #tr tst.machine.BottledChaos.tooltip.info.4
                // # Links multiple processing steps internally, automatically turning inputs into target products.
                // #zh_CN 在内部衔接多道工序, 完成从原料到目标产物的自动加工.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.4"))
                // #tr tst.machine.BottledChaos.tooltip.info.5
                // # Structure modules provide the corresponding machines' recipes and capabilities.
                // #zh_CN 结构模组提供对应机器的配方与性能.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.5"))
                // #tr tst.machine.BottledChaos.tooltip.info.6
                // # Credentials select processing chains; each group runs independently.
                // #zh_CN 凭证指定加工路线, 各加工组独立运行.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.6"))
                // #tr tst.machine.BottledChaos.tooltip.info.7
                // # Processing pauses when power is insufficient.
                // #zh_CN 供电不足时暂停加工.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.7"))
                // #tr tst.machine.BottledChaos.tooltip.info.8
                // # At machine tier UXV with no energy hatches installed, wireless mode is enabled.
                // #zh_CN 机器达到UXV级且未安装能源仓时, 启用无线模式.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.8"))
                // #tr tst.machine.BottledChaos.tooltip.info.9
                // # In wireless mode, each group prepays its energy cost and runs at the minimum overclock time.
                // #zh_CN 无线模式下, 各加工组预付电量并按最低超频时间运行.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.9"))
                // #tr tst.machine.BottledChaos.tooltip.info.10
                // # Capacity regions form a shared pool, allocated by the route weights of enabled groups.
                // #zh_CN 各容量区提供全机共享容量, 按启用组的路线占用量分配.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.10"))
                // #tr tst.machine.BottledChaos.tooltip.info.11
                // # When identical credentials share inputs, capacity usage counts every corresponding credential hatch.
                // #zh_CN 相同凭证共用输入时, 容量占用按对应凭证仓数量累计.
                .addInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.info.11"))
                .beginStructureBlock(SHAPE)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.ModularizedMachineSystemDescription01)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.ModularizedMachineSystemDescription02)
                // #tr tst.machine.BottledChaos.tooltip.structure.1
                // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}Parallel Controller Module - Increase processing groups and structure module slots
                // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}并行控制器模块 - 增加加工组数量与结构模组槽位
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.1"))
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.SpeedControllerDescription)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.PowerConsumptionControllerDescription)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.OverclockControllerDescription)
                .addStructureInfo(TSTSharedLocalization.ModularizedMachine.NotMultiplyInstallSameTypeModuleAll)
                .addStructureInfo(TSTSharedLocalization.General.Text_SeparatingLine)
                // #tr tst.machine.BottledChaos.tooltip.structure.2
                // # Capacity regions can be upgraded independently or left empty.
                // #zh_CN 容量区可独立升级或留空.
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.2"))
                // #tr tst.machine.BottledChaos.tooltip.structure.3
                // # Stabilisation Field Generators and Energy Sustainment Matrices must share a tier, which determines the machine tier.
                // #zh_CN 稳定力场发生器与能量维持矩阵须同级, 决定机器等级.
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.3"))
                // #tr tst.machine.BottledChaos.tooltip.structure.4
                // # Shaft and capacity-region glass must share a tier no lower than the machine tier.
                // #zh_CN 主轴与容量区玻璃须同级, 且不得低于机器等级.
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.4"))
                .addStructureInfo(TSTSharedLocalization.MachineTooltip.Tooltip_GlassTierLimitEnergyHatchTier)
                // #tr tst.machine.BottledChaos.tooltip.structure.5
                // # Central inputs supply every processing group.
                // #zh_CN 中心输入供全部加工组共享.
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.5"))
                // #tr tst.machine.BottledChaos.tooltip.structure.6
                // # The two positions below each credential hatch are dedicated inputs for its processing group.
                // #zh_CN 凭证仓下方两格为本组专用输入位.
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.6"))
                // #tr tst.machine.BottledChaos.tooltip.structure.7
                // # Groups with identical credentials share dedicated inputs; only one may be enabled at a time.
                // #zh_CN 凭证完全相同的加工组共用专用输入, 同时仅可启用一组.
                .addStructureInfo(TSTUtils.tr("tst.machine.BottledChaos.tooltip.structure.7"))
                .addStructureInfo(TSTSharedLocalization.General.Text_SeparatingLine)
                .addOtherStructurePart(TSTSharedLocalization.ModularizedMachine.ModularHatch,
                    TSTSharedLocalization.Structure.getBlueprintWithDot(1), 1)
                .addOtherStructurePart(TSTUtils.tr("tst.machine.BottledChaosRecipeTokenHatch.name"),
                    TSTSharedLocalization.Structure.getBlueprintWithDot(2), 2)
                .addInputBus("", TSTSharedLocalization.Structure.getBlueprintWithDot(1, 3, 5), 1, 3, 5)
                .addInputHatch("", TSTSharedLocalization.Structure.getBlueprintWithDot(1, 3, 5), 1, 3, 5)
                .addOutputBus("", TSTSharedLocalization.Structure.getBlueprintWithDot(1, 5), 1, 5)
                .addOutputHatch("", TSTSharedLocalization.Structure.getBlueprintWithDot(1, 5), 1, 5)
                .addEnergyHatch("", TSTSharedLocalization.Structure.getBlueprintWithDot(1, 5), 1, 5)
                .addStructureHint(TSTSharedLocalization.ModularizedMachine.ModularHatchKey, 1)
                .toolTipFinisher();
            // spotless:on
        }
        return tooltip;
    }
}
