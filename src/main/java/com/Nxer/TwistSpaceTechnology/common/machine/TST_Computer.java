package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_NEUTRAL;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.util.CommonValues.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static goodgenerator.loader.Loaders.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static gtPlusPlus.core.block.ModBlocks.blockCasings3Misc;
import static net.minecraft.util.StatCollector.translateToLocal;
import static vazkii.botania.common.block.ModBlocks.pylon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.technus.tectech.mechanics.dataTransport.QuantumDataPacket;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_switch;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.IGT_HatchAdder;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;

public class TST_Computer extends GT_MetaTileEntity_MultiblockBase_EM implements ISurvivalConstructable {

    // region variables
    private final ArrayList<GT_MetaTileEntity_Hatch_Rack> eRacks = new ArrayList<>();

    private double multiplier = 1;

    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    // endregion
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.computer.hint.0"), // 1 - Classic/Data Hatches or
        // Computer casing
        translateToLocal("gt.blockmachines.multimachine.em.computer.hint.1"), // 2 - Rack Hatches or Advanced
        // computer casing
    };

    public static final int offsetX = 23, offsetY = 34, offsetZ = 0;
    // region structure
    public static final String[][] shape = new String[][] {
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "EEEEEEEEEEEEEEEEEEEEEEE~EEEEEEEEEEEEEEEEEEEEEEE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "EFFFFFIFIFFFFFFFFFFFIFIFIFIFFFFFFFFFFFIFIFFFFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "EFFFFFIFIFFFFFFFFFFFIFIFIFIFFFFFFFFFFFIFIFFFFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                       M                       ",
            "                       M                       ", "                      MMM                      ",
            "                     MMMMM                     ", "                      MMM                      ",
            "                       M                       ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                     CCCCC                     ",
            "EFFGGGIGIGGGGGGGGGGGIGGGGGIGGGGGGGGGGGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                     MM MM                     ", "                    MM   MM                    ",
            "                    M     M                    ", "                    MM   MM                    ",
            "                     MM MM                     ", "                      MMM                      ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                      CCC                      ", "    PPEEEPPPPPPPPPPPCCCCCCCPPPPPPPPPPPEEEPP    ",
            "EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "      JJJ                             JJJ      ", "      JJJ                             JJJ      ",
            "      JJJ                             JJJ      ", "                                               ",
            "                    M     M                    ", "      JJJ           M     M           JJJ      ",
            "                   M       M                   ", "      JJJ           M     M           JJJ      ",
            "                    M     M                    ", "      JJJ            MM MM            JJJ      ",
            "                       M                       ", "      JJJ                             JJJ      ",
            "                                               ", "                      CCC                      ",
            "      BBB            C   C            BBB      ", "    PEEEEE         CCCCCCCCC         EEEEEP    ",
            "EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "      JJJ                             JJJ      ",
            "     JJJJJ                           JJJJJ     ", "     JJJJJ                           JJJJJ     ",
            "     JJJJJ                           JJJJJ     ", "      JJJ                             JJJ      ",
            "                                               ", "     J L J         M       M         J L J     ",
            "                   M       M                   ", "     J L J         M       M         J L J     ",
            "                    M     M                    ", "     J L J          MM   MM          J L J     ",
            "                      MMM                      ", "     J L J                           J L J     ",
            "                      CCC                      ", "                     C   C                     ",
            "     B   B          C     C          B   B     ", "    EEBBBEEPPPPPPPPCCCCCCCCCPPPPPPPPEEBBBEE    ",
            "EIIIGGGGGGGIIIIIIIIGGGGGGGGGIIIIIIIIGGGGGGGIIIE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "      JJJ                             JJJ      ",
            "     JJJJJ                           JJJJJ     ", "     JJJJJ                           JJJJJ     ",
            "     JJJJJ                           JJJJJ     ", "      JJJ                             JJJ      ",
            "       A                               A       ", "     JLALJ                           JLALJ     ",
            "       A           M       M           A       ", "     JLALJ         M   O   M         JLALJ     ",
            "       A           M       M           A       ", "     JLALJ          M     M          JLALJ     ",
            "       A             MMMMM             A       ", "     JLALJ             C             JLALJ     ",
            "       A              CCC              A       ", "       A             C C C             A       ",
            "     B A B          C  C  C          B A B     ", "    EEBABEE       PCCCCCCCCCP       EEBABEE    ",
            "EFFGGGGGGGGGGGGGGGIGGGGGGGGGIGGGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "      JJJ                             JJJ      ",
            "     JJJJJ                           JJJJJ     ", "     JJJJJ                           JJJJJ     ",
            "     JJJJJ                           JJJJJ     ", "      JJJ                             JJJ      ",
            "                                               ", "     J L J                           J L J     ",
            "                                               ", "     J L J         M       M         J L J     ",
            "                    M     M                    ", "     J L J          MM   MM          J L J     ",
            "                      MMM                      ", "     J L J                           J L J     ",
            "                      CCC                      ", "                     C   C                     ",
            "     B   B          C     C          B   B     ", "    EEBBBEEPPPPPP PCCCCCCCCCP PPPPPPEEBBBEE    ",
            "EIIIGGGGGGGIIIIIIGIGGGGGGGGGIGIIIIIIGGGGGGGIIIE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "      JJJ                             JJJ      ", "      JJJ                             JJJ      ",
            "      JJJ                             JJJ      ", "                                               ",
            "                                               ", "      JJJ                             JJJ      ",
            "                                               ", "      JJJ                             JJJ      ",
            "                    M     M                    ", "      JJJ            MM MM            JJJ      ",
            "                       M                       ", "      JJJ                             JJJ      ",
            "                                               ", "                      CCC                      ",
            "      BBB            C   C            BBB      ", "    PEEEEE      P PCCCCCCCCCP P      EEEEEP    ",
            "EFFGGGGGGGGGGGGGIGIGGGGGGGGGIGIGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                      MMM                      ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                      CCC                      ", "    P EEE       P P CCCCCCC P P       EEE P    ",
            "EFFGGGGGGGIIIIIGIGIGGGGGGGGGIGIGIIIIIGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P PCCCCCP P P       P P P    ",
            "EFFGGGIGIGIGGGIGIGIGIGGGGGIGIGIGIGGGIGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P P P P P P P       P P P    ",
            "EFFGGGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P P P P P P P       P P P    ",
            "EFFGGGIGIGIGGGIGIGIGIGIGIGIGIGIGIGGGIGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P P P P P P P       P P P    ",
            "EFFGGGIGIGIIIIIGIGIGIGIGIGIGIGIGIIIIIGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P P P P P P P       P P P    ",
            "EFFGGGIGIGGGGGGGIGIGIGIGIGIGIGIGGGGGGGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                KKKKKKKKKKKKKKK                ",
            "                KKKKKKKKKKKKKKK                ", "    P P PPPPPPPPPKPKPKPKPKPKPKPPPPPPPPP P P    ",
            "EFFGGGIGIIIIIIIIGGGGGGGGGGGGGGGIIIIIIIIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                     MMMMM                     ", "                KKKKKKKKKKKKKKK                ",
            "                KIHHHHHHHHHHHIK                ", "    P P         KHQQQQQQQQQQQHK         P P    ",
            "EFFGGGIGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                     MMMMM                     ",
            "                     MMMMM                     ", "                      MMM                      ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                     MMMMM                     ",
            "                   MM     MM                   ", "                KKKKKKKKKKKKKKK                ",
            "                KHIHHHHHHHHHIHK                ", "    P PPPPPPPPPPPQHQQQQQQQQQHQPPPPPPPPPPP P    ",
            "EFFGGGIIIIIIIIIIGGGGGGGGGGGGGGGIIIIIIIIIIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                    M     M                    ",
            "                    M     M                    ", "                    MM   MM                    ",
            "                     MMMMM                     ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "      M                                 M      ",
            "     MMM                               MMM     ", "      MMM                             MMM      ",
            "       M                               M       ", "                     MMMMM                     ",
            "                     MMMMM                     ", "                    M     M                    ",
            "                  M         M                  ", "                KKKKKKKKKKKKKKK                ",
            "                KHHIHHHHHHHIHHK                ", "    PCCCCC      KQQHQQQQQQQHQQK      CCCCCP    ",
            "EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                      III                      ", "                   M  III  M                   ",
            "                   M  III  M                   ", "                   M       M                   ",
            "                    M     M                    ", "                     MMMMM                     ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "     M                                   M     ", "    MM                                   MM    ",
            "    M                 MMM                 M    ", "    MM                MMM                MM    ",
            "     MM MM            MMM            MM MM     ", "      MMM           M     M           MMM      ",
            "                    M     M                    ", "                   M       M                   ",
            "                  M         M                  ", "                KKKKKKKKKKKKKKK                ",
            "      CCC       KHHHIHHHHHIHHHK       CCC      ", "    CCCCCCCPPPPPPQQQHQQQQQHQQQPPPPPPCCCCCCC    ",
            "EIIIGGGGGGGIIIIIGGGGGGGGGGGGGGGIIIIIGGGGGGGIIIE" },
        { "                                               ", "                     IIIII                     ",
            "                     IIIII                     ", "                  M  IIIII  M                  ",
            "                  M  IIIII  M                  ", "                   M IIIII M                   ",
            "                   M       M                   ", "                    MM   MM                    ",
            "                      MMM                      ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                      MMM                      ",
            "                      MMM                      ", "                      MMM                      ",
            "    M                 MMM                 M    ", "    M                 MMM                 M    ",
            "   M                 M   M                 M   ", "    M                M   M                M    ",
            "    M                M   M                M    ", "     MM MM         M       M         MM MM     ",
            "       M           M       M           M       ", "                  M         M                  ",
            "                 M           M                 ", "      CCC       KKKKKKKKKKKKKKK       CCC      ",
            "     C   C      KHHHHIHHHIHHHHK      C   C     ", "   CCCCCCCCC    KQQQQHQQQHQQQQK    CCCCCCCCC   ",
            "EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE" },
        { "                      III                      ", "                     IIIII                     ",
            "                    IIIIIII                    ", "                  M IIIIIII M                  ",
            "                  M IIIIIII M                  ", "                  M  IIIII  M                  ",
            "                   M  III  M                   ", "                    M     M                    ",
            "                     MMMMM                     ", "                       M                       ",
            "                       M                       ", "                       M                       ",
            "                       M                       ", "                       M                       ",
            "                       M                       ", "                       M                       ",
            "                      MMM                      ", "                      MMM                      ",
            "                      MMM                      ", "                     M   M                     ",
            "                     M   M                     ", "                     M   M                     ",
            "    M                M   M                M    ", "   M                 M   M                 M   ",
            "   M                M     M                M   ", "   M                M     M                M   ",
            "    M               M     M               M    ", "    MM   MM        M       M        MM   MM    ",
            "      MMM          M       M          MMM      ", "                  M         M                  ",
            "      CCC        M           M        CCC      ", "     C   C      KKKKKKKKKKKKKKK      C   C     ",
            "    C     C     KHHHHHIHIHHHHHK     C     C    ", "   CCCCCCCCCPPPPPQQQQQHQHQQQQQPPPPPCCCCCCCCC   ",
            "EIIGGGGGGGGGIIIIGGGGGGGGGGGGGGGIIIIGGGGGGGGGIIE" },
        { "                      III                      ", "                     IIIII                     ",
            "                    IIIIIII                    ", "                  M IIIIIII M                  ",
            "                  M IIIIIII M                  ", "                  M  IIIII  M                  ",
            "                   M  III  M                   ", "                    M     M                    ",
            "                     MMMMM                     ", "                      MDM                      ",
            "                      MDM                      ", "                      MDM                      ",
            "                      MDM                      ", "                      MDM                      ",
            "                      MDM                      ", "                      MDM                      ",
            "                      MDM                      ", "                      MDM                      ",
            "                      MDM                      ", "                     M D M                     ",
            "                     M D M                     ", "   M                 M D M                 M   ",
            "   M                 M D M                 M   ", "   M                 M D M                 M   ",
            "   M                M  D  M                M   ", "   M   O            M  D  M            O   M   ",
            "   M                M  D  M                M   ", "    M     M        M   D   M        M     M    ",
            "     MMMMM         M   D   M         MMMMM     ", "       C          M    D    M          C       ",
            "      CCC        M     D     M        CCC      ", "     C C C      KKKKKKKDKKKKKKK      C C C     ",
            "    C  C  C     KHHHHHHDHHHHHHK     C  C  C    ", "   CCCCCCCCC    KQQQQQQDQQQQQQK    CCCCCCCCC   ",
            "EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE" },
        { "                      III                      ", "                     IIIII                     ",
            "                    IIIIIII                    ", "                  M IIIIIII M                  ",
            "                  M IIIIIII M                  ", "                  M  IIIII  M                  ",
            "                   M  III  M                   ", "                    M     M                    ",
            "                     MMMMM                     ", "                       M                       ",
            "                       M                       ", "                       M                       ",
            "                       M                       ", "                       M                       ",
            "                       M                       ", "                       M                       ",
            "                      MMM                      ", "                      MMM                      ",
            "                      MMM                      ", "                     M   M                     ",
            "                     M   M                     ", "                     M   M                     ",
            "    M                M   M                M    ", "   M                 M   M                 M   ",
            "   M                M     M                M   ", "   M                M     M                M   ",
            "    M               M     M               M    ", "    MM   MM        M       M        MM   MM    ",
            "      MMM          M       M          MMM      ", "                  M         M                  ",
            "      CCC        M           M        CCC      ", "     C   C      KKKKKKKKKKKKKKK      C   C     ",
            "    C     C     KHHHHHIHIHHHHHK     C     C    ", "   CCCCCCCCCPPPPPQQQQQHQHQQQQQPPPPPCCCCCCCCC   ",
            "EIIGGGGGGGGGIIIIGGGGGGGGGGGGGGGIIIIGGGGGGGGGIIE" },
        { "                                               ", "                     IIIII                     ",
            "                     IIIII                     ", "                  M  IIIII  M                  ",
            "                  M  IIIII  M                  ", "                   M IIIII M                   ",
            "                   M       M                   ", "                    MM   MM                    ",
            "                      MMM                      ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                      MMM                      ",
            "                      MMM                      ", "                      MMM                      ",
            "    M                 MMM                 M    ", "    M                 MMM                 M    ",
            "   M                 M   M                 M   ", "    M                M   M                M    ",
            "    M                M   M                M    ", "     MM MM         M       M         MM MM     ",
            "       M           M       M           M       ", "                  M         M                  ",
            "                 M           M                 ", "      CCC       KKKKKKKKKKKKKKK       CCC      ",
            "     C   C      KHHHHIHHHIHHHHK      C   C     ", "   CCCCCCCCC    KQQQQHQQQHQQQQK    CCCCCCCCC   ",
            "EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                      III                      ", "                   M  III  M                   ",
            "                   M  III  M                   ", "                   M       M                   ",
            "                    M     M                    ", "                     MMMMM                     ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "     M                                   M     ", "    MM                                   MM    ",
            "    M                 MMM                 M    ", "    MM                MMM                MM    ",
            "     MM MM            MMM            MM MM     ", "      MMM           M     M           MMM      ",
            "                    M     M                    ", "                   M       M                   ",
            "                  M         M                  ", "                KKKKKKKKKKKKKKK                ",
            "      CCC       KHHHIHHHHHIHHHK       CCC      ", "    CCCCCCCPPPPPPQQQHQQQQQHQQQPPPPPPCCCCCCC    ",
            "EIIIGGGGGGGIIIIIGGGGGGGGGGGGGGGIIIIIGGGGGGGIIIE" },
        { "                                               ", "                                               ",
            "                                               ", "                    M     M                    ",
            "                    M     M                    ", "                    MM   MM                    ",
            "                     MMMMM                     ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "      M                                 M      ",
            "     MMM                               MMM     ", "      MMM                             MMM      ",
            "       M                               M       ", "                     MMMMM                     ",
            "                     MMMMM                     ", "                    M     M                    ",
            "                  M         M                  ", "                KKKKKKKKKKKKKKK                ",
            "                KHHIHHHHHHHIHHK                ", "    PCCCCC      KQQHQQQQQQQHQQK      CCCCCP    ",
            "EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                     MMMMM                     ",
            "                     MMMMM                     ", "                      MMM                      ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                     MMMMM                     ",
            "                   MM     MM                   ", "                KKKKKKKKKKKKKKK                ",
            "                KHIHHHHHHHHHIHK                ", "    P PPPPPPPPPPPQHQQQQQQQQQHQPPPPPPPPPPP P    ",
            "EFFGGGIIIIIIIIIIGGGGGGGGGGGGGGGIIIIIIIIIIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                     MMMMM                     ", "                KKKKKKKKKKKKKKK                ",
            "                KIHHHHHHHHHHHIK                ", "    P P         KHQQQQQQQQQQQHK         P P    ",
            "EFFGGGIGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                KKKKKKKKKKKKKKK                ",
            "                KKKKKKKKKKKKKKK                ", "    P P PPPPPPPPPKPKPKPKPKPKPKPPPPPPPPP P P    ",
            "EFFGGGIGIIIIIIIIGGGGGGGGGGGGGGGIIIIIIIIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P P P P P P P       P P P    ",
            "EFFGGGIGIGGGGGGGIGIGIGIGIGIGIGIGGGGGGGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P P P P P P P       P P P    ",
            "EFFGGGIGIGIIIIIGIGIGIGIGIGIGIGIGIIIIIGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P P P P P P P       P P P    ",
            "EFFGGGIGIGIGGGIGIGIGIGIGIGIGIGIGIGGGIGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P P P P P P P       P P P    ",
            "EFFGGGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "    P P P       P P PCCCCCP P P       P P P    ",
            "EFFGGGIGIGIGGGIGIGIGIGGGGGIGIGIGIGGGIGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                      MMM                      ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                      CCC                      ", "    P EEE       P P CCCCCCC P P       EEE P    ",
            "EFFGGGGGGGIIIIIGIGIGGGGGGGGGIGIGIIIIIGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "      JJJ                             JJJ      ", "      JJJ                             JJJ      ",
            "      JJJ                             JJJ      ", "                                               ",
            "                                               ", "      JJJ                             JJJ      ",
            "                                               ", "      JJJ                             JJJ      ",
            "                    M     M                    ", "      JJJ            MM MM            JJJ      ",
            "                       M                       ", "      JJJ                             JJJ      ",
            "                                               ", "                      CCC                      ",
            "      BBB            C   C            BBB      ", "    PEEEEE      P PCCCCCCCCCP P      EEEEEP    ",
            "EFFGGGGGGGGGGGGGIGIGGGGGGGGGIGIGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "      JJJ                             JJJ      ",
            "     JJJJJ                           JJJJJ     ", "     JJJJJ                           JJJJJ     ",
            "     JJJJJ                           JJJJJ     ", "      JJJ                             JJJ      ",
            "                                               ", "     J L J                           J L J     ",
            "                                               ", "     J L J         M       M         J L J     ",
            "                    M     M                    ", "     J L J          MM   MM          J L J     ",
            "                      MMM                      ", "     J L J                           J L J     ",
            "                      CCC                      ", "                     C   C                     ",
            "     B   B          C     C          B   B     ", "    EEBBBEEPPPPPP PCCCCCCCCCP PPPPPPEEBBBEE    ",
            "EIIIGGGGGGGIIIIIIGIGGGGGGGGGIGIIIIIIGGGGGGGIIIE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "      JJJ                             JJJ      ",
            "     JJJJJ                           JJJJJ     ", "     JJJJJ                           JJJJJ     ",
            "     JJJJJ                           JJJJJ     ", "      JJJ                             JJJ      ",
            "       A                               A       ", "     JLALJ                           JLALJ     ",
            "       A           M       M           A       ", "     JLALJ         M   O   M         JLALJ     ",
            "       A           M       M           A       ", "     JLALJ          M     M          JLALJ     ",
            "       A             MMMMM             A       ", "     JLALJ             C             JLALJ     ",
            "       A              CCC              A       ", "       A             C C C             A       ",
            "     B A B          C  C  C          B A B     ", "    EEBABEE       PCCCCCCCCCP       EEBABEE    ",
            "EFFGGGGGGGGGGGGGGGIGGGGGGGGGIGGGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "      JJJ                             JJJ      ",
            "     JJJJJ                           JJJJJ     ", "     JJJJJ                           JJJJJ     ",
            "     JJJJJ                           JJJJJ     ", "      JJJ                             JJJ      ",
            "                                               ", "     J L J         M       M         J L J     ",
            "                   M       M                   ", "     J L J         M       M         J L J     ",
            "                    M     M                    ", "     J L J          MM   MM          J L J     ",
            "                      MMM                      ", "     J L J                           J L J     ",
            "                      CCC                      ", "                     C   C                     ",
            "     B   B          C     C          B   B     ", "    EEBBBEEPPPPPPPPCCCCCCCCCPPPPPPPPEEBBBEE    ",
            "EIIIGGGGGGGIIIIIIIIGGGGGGGGGIIIIIIIIGGGGGGGIIIE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "      JJJ                             JJJ      ", "      JJJ                             JJJ      ",
            "      JJJ                             JJJ      ", "                                               ",
            "                    M     M                    ", "      JJJ           M     M           JJJ      ",
            "                   M       M                   ", "      JJJ           M     M           JJJ      ",
            "                    M     M                    ", "      JJJ            MM MM            JJJ      ",
            "                       M                       ", "      JJJ                             JJJ      ",
            "                                               ", "                      CCC                      ",
            "      BBB            C   C            BBB      ", "    PEEEEE         CCCCCCCCC         EEEEEP    ",
            "EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                     MM MM                     ", "                    MM   MM                    ",
            "                    M     M                    ", "                    MM   MM                    ",
            "                     MM MM                     ", "                      MMM                      ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                      CCC                      ", "    PPEEEPPPPPPPPPPPCCCCCCCPPPPPPPPPPPEEEPP    ",
            "EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                       M                       ",
            "                       M                       ", "                      MMM                      ",
            "                     MMMMM                     ", "                      MMM                      ",
            "                       M                       ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                     CCCCC                     ",
            "EFFGGGIGIGGGGGGGGGGGIGGGGGIGGGGGGGGGGGIGIGGGFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "EFFFFFIFIFFFFFFFFFFFIFIFIFIFFFFFFFFFFFIFIFFFFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "EFFFFFIFIFFFFFFFFFFFIFIFIFIFFFFFFFFFFFIFIFFFFFE" },
        { "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE" } };
    // Structure:
    //
    // Blocks:

    //
    // Tiles:
    //
    // Special Tiles:
    // N -> ofSpecialTileAdder(thaumcraft.common.tiles.TileNode, ...); // You will probably want to change it to
    // something else
    // O -> ofSpecialTileAdder(vazkii.botania.common.block.tile.TilePylon, ...); // You will probably want to change it
    // to something else
    // P -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...); // You will probably want to change
    // it to something else
    // Q -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaTileEntity, ...); // You will probably want to change
    // it to something else
    private static final String MAIN = "main";
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn overclock, overvolt;
    protected Parameters.Group.ParameterOut maxCurrentTemp, availableData;

    private static final INameFunction<TST_Computer> OC_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.0"); // Overclock ratio
    private static final INameFunction<TST_Computer> OV_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.1"); // Overvoltage ratio
    private static final INameFunction<TST_Computer> MAX_TEMP_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.0"); // Current max. heat
    private static final INameFunction<TST_Computer> COMPUTE_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.1"); // Produced computation
    private static final IStatusFunction<TST_Computer> OC_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 1, 3);
    private static final IStatusFunction<TST_Computer> OV_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), .7, .8, 1.2, 2);
    private static final IStatusFunction<TST_Computer> MAX_TEMP_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), -10000, 0, 0, 5000);
    private static final IStatusFunction<TST_Computer> COMPUTE_STATUS = (base, p) -> {
        if (base.eAvailableData < 0) {
            return STATUS_TOO_LOW;
        }
        if (base.eAvailableData == 0) {
            return STATUS_NEUTRAL;
        }
        return STATUS_OK;
    };
    // endregion

    public TST_Computer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        eCertainMode = 0;
        useLongPower = true;
    }

    public TST_Computer(String aName) {
        super(aName);
        eCertainMode = 0;
        useLongPower = true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_Computer(mName);
    }

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0);
        overclock = hatch_0.makeInParameter(0, 1, OC_NAME, OC_STATUS);
        overvolt = hatch_0.makeInParameter(1, 1, OV_NAME, OV_STATUS);
        maxCurrentTemp = hatch_0.makeOutParameter(0, 0, MAX_TEMP_NAME, MAX_TEMP_STATUS);
        availableData = hatch_0.makeOutParameter(1, 0, COMPUTE_NAME, COMPUTE_STATUS);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        eRacks.clear();
        if (!structureCheck_EM(MAIN, offsetX, offsetY, offsetZ)) {
            return false;
        }
        for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(iGregTechTileEntity.isActive());
        }
        return mOutputHatches.size() > 0 && mInputHatches.size() > 0
            && mMaintenanceHatches.size() == 1
            && eRacks.size() > 0
            && eOutputData.size() != 0;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setDouble("computation", availableData.get());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (availableData != null) {
            availableData.set(aNBT.getDouble("computation"));
            eAvailableData = (long) availableData.get();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && mMachine
            && !aBaseMetaTileEntity.isActive()
            && aTick % 20 == MULTI_CHECK_AT) {
            double maxTemp = 0;
            for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
                maxTemp = Math.max(maxTemp, rack.heat);
            }
            maxCurrentTemp.set(maxTemp);
        }
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        parametrization.setToDefaults(false, true);
        eAvailableData = 0;
        double maxTemp = 0;
        double overClockRatio = overclock.get();
        double overVoltageRatio = overvolt.get();
        if (Double.isNaN(overClockRatio) || Double.isNaN(overVoltageRatio)) {
            return SimpleCheckRecipeResult.ofFailure("no_computing");
        }
        if (overclock.getStatus(true).isOk && overvolt.getStatus(true).isOk) {
            float eut = V[8] * (float) overVoltageRatio * (float) overClockRatio;
            if (eut < Integer.MAX_VALUE - 7) {
                lEUt = (long) -eut;
            } else {
                lEUt = -V[8];
                return CheckRecipeResultRegistry.POWER_OVERFLOW;
            }
            long thingsActive = 0;
            int rackComputation;
            // mOutputFluids[0] = null;
            // LOG.info("pre racks computation! size:" + filterValidMTEs(eRacks).size());
            for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
                if (rack.heat > maxTemp) {
                    maxTemp = rack.heat;
                }
                rackComputation = rack.tickComponents((float) overClockRatio, (float) overVoltageRatio);
                // LOG.info("preview heat:" + rack.heat + "/preview rackComputation:" + rackComputation);
                rack.heat = coolTheRackHatchByAnyCoolant(rack.heat);
                rackComputation *= multiplier;
                // LOG.info("after heat:" + rack.heat + "/after rackComputation:" + rackComputation);
                if (rackComputation > 0) {
                    eAvailableData += rackComputation;
                    thingsActive += 4 * multiplier * multiplier;
                }
                rack.getBaseMetaTileEntity()
                    .setActive(true);
            }
            // LOG.info("end racks computation!");

            for (GT_MetaTileEntity_Hatch_InputData di : eInputData) {
                if (di.q != null) // ok for power losses
                {
                    thingsActive++;
                }
            }
            if (thingsActive > 0) {
                thingsActive += eOutputData.size();
                // LOG.info("activated " + thingsActive);
                eAmpereFlow = 1 + (thingsActive >> 2);
                // eAmpereFlow *= multiplier * multiplier;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                // addFluidOutputs(mOutputFluids);
                // mOutputFluids[0] = null;
                // LOG.info("activated " + thingsActive + " /A:" + eAmpereFlow + " /maxTemp:" + maxTemp);
                return SimpleCheckRecipeResult.ofSuccess("computing");
            } else {
                eAvailableData = 0;
                lEUt = -V[8];
                eAmpereFlow = 1;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                return SimpleCheckRecipeResult.ofSuccess("no_computing");
            }
        } else {
            LOG.info("what?");
        }
        return SimpleCheckRecipeResult.ofFailure("no_computing");
    }

    public int coolTheRackHatchByAnyCoolant(int prevHeat) {
        FluidStack coolant = null;
        for (var input : filterValidMTEs(mInputHatches)) {
            FluidStack fluid = input.getFluid();
            if (fluid == null) continue;
            if (coolant == null && validCoolant(fluid) > 0) coolant = fluid.copy();
            else if (coolant != null && coolant.getFluid() == fluid.getFluid()) coolant.amount += fluid.amount;
        }
        if (coolant == null) return prevHeat;
        double maxHeatCanCool = validCoolant(coolant) * coolant.amount;
        multiplier = 1.0 + Math.log10(1.0 + maxHeatCanCool * prevHeat);
        long realHeatCanCool = (int) (prevHeat - (prevHeat / multiplier));
        long requiredAmount = (long) (realHeatCanCool / validCoolant(coolant));
        FluidStack output = null;
        for (var input : filterValidMTEs(mInputHatches)) {
            FluidStack fluid = input.getFluid();
            if (requiredAmount == 0) break;
            if (coolant.getFluid() == fluid.getFluid()) {
                int mx = (int) Math.min(requiredAmount, fluid.amount);
                fluid.amount -= mx;
                requiredAmount -= mx;
                if (output == null) output = new FluidStack(getCoolantTransform(coolant.getFluid()), mx);
                else output.amount += mx;
            }
        }
        if (output == null) {
            if (realHeatCanCool != 0) {
                LOG.info("why you can cool without coolant?");
            }
            return (int) (prevHeat - realHeatCanCool);
        }
        output.amount = output.amount * 97 / 100;
        // LOG.info("maxHeat:" + maxHeatCanCool + " /realHeat:" + realHeatCanCool + " /requiredAmount:" + requiredAmount
        // + " /output:" + output.amount);
        addFluidOutputs(new FluidStack[] { output });
        return (int) (prevHeat - realHeatCanCool);
    }

    public static double validCoolant(FluidStack fluid) {
        if (fluid.getFluid() == BlocksItems.getFluid(InternalName.fluidCoolant)) return 0.01;
        if (fluid.getFluid() == Materials.SuperCoolant.mFluid) return 10;
        return -1;
    }

    public static Fluid getCoolantTransform(Fluid fluid) {
        if (fluid == BlocksItems.getFluid(InternalName.fluidCoolant))
            return BlocksItems.getFluid(InternalName.fluidHotCoolant);
        if (fluid == Materials.SuperCoolant.mFluid) return BlocksItems.getFluid(InternalName.fluidCoolant);
        return null;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eOutputData.isEmpty()) {
            Vec3Impl pos = new Vec3Impl(
                getBaseMetaTileEntity().getXCoord(),
                getBaseMetaTileEntity().getYCoord(),
                getBaseMetaTileEntity().getZCoord());

            QuantumDataPacket pack = new QuantumDataPacket(eAvailableData / eOutputData.size()).unifyTraceWith(pos);
            if (pack == null) {
                return;
            }
            for (GT_MetaTileEntity_Hatch_InputData hatch : eInputData) {
                if (hatch.q == null || hatch.q.contains(pos)) {
                    continue;
                }
                pack = pack.unifyPacketWith(hatch.q);
                if (pack == null) {
                    return;
                }
            }

            for (GT_MetaTileEntity_Hatch_OutputData o : eOutputData) {
                o.q = pack;
            }
        }
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.computer.name")) // Machine Type: Quantum
            // Computer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.computer.desc.0")) // Controller block of
            // the Quantum Computer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.computer.desc.1")) // Used to generate
            // computation (and heat)
            .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
            .addSeparator()
            .addInfo("You should put racks on the top the advanced computing block")
            .addInfo("or replace the advanced computing block in the second layer of centre matrix")
            .addInfo("the extra overclock calculated as follow:")
            .addInfo("if you have current heat H and you have input X amount of coolant")
            .addInfo("your extra overclock will be (1+log10(1+0.01*X*H))")
            .addInfo("It will currently return you back 97% percent of hotCoolant ")
            // .beginVariableStructureBlock(2, 2, 4, 4, 5, 16, false)
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.certain.tier.07.name"),
                "no need uncertain hatch!",
                1) // Uncertainty Resolver: Any Computer Casing on first or last slice
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.DataConnector"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"),
                1) // Optical Connector: Any Computer Casing on first or last slice
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.rack.tier.08.name"),
                translateToLocal("tt.keyword.Structure.AnyAdvComputerCasingExceptOuter"),
                2) // Computer Rack: Any Advanced Computer Casing, except the outer ones
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.param.tier.05.name"),
                translateToLocal("tt.keyword.Structure.Optional") + " "
                    + translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"),
                2) // Parametrizer: (optional) Any Computer Casing on first or last slice
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"), 1) // Energy
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"), 1) // Maintenance
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3],
                new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3] };
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return GT_MetaTileEntity_EM_switch.activitySound;
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    @Override
    protected void extraExplosions_EM() {
        for (MetaTileEntity tTileEntity : eRacks) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(V[9]);
        }
    }

    @Override
    protected long getAvailableData_EM() {
        return eAvailableData;
    }

    @Override
    public void stopMachine() {
        // LOG.info("SOMETHING stop the machine");
        super.stopMachine();
        eAvailableData = 0;
        for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    @Override
    protected void afterRecipeCheckFailed() {
        super.afterRecipeCheckFailed();
        for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    public final boolean addRackToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Rack) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eRacks.add((GT_MetaTileEntity_Hatch_Rack) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(MAIN, offsetX, offsetY, offsetZ, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece(
            MAIN,
            stackSize,
            offsetX,
            offsetY,
            offsetZ,
            elementBudget,
            source,
            actor,
            false,
            true);

    }

    @Override
    public IStructureDefinition<TST_Computer> getStructure_EM() {
        return IStructureDefinition.<TST_Computer>builder()
            .addShape(MAIN, shape)// FRF_Coil_1
            .addElement('A', ofBlock(FRF_Coil_1, 0))// A -> ofBlock...(FRF_Coil_1, 0, ...);
            .addElement('B', ofBlock(compactFusionCoil, 0))// B -> ofBlock...(compactFusionCoil, 2, ...);
            .addElement('C', ofBlock(IGBlocks.SpaceElevatorCasing, 0))// C -> ofBlock...(gt.blockcasingsSE, 0, ...);
            .addElement('D', ofBlock(IGBlocks.SpaceElevatorCasing, 1))// D -> ofBlock...(gt.blockcasingsSE, 1, ...);
            // .addElement('E', ofBlock(IGBlocks.SpaceElevatorCasing, 2))// E -> ofBlock...(gt.blockcasingsSE, 2, ...);
            .addElement('F', ofBlock(sBlockCasingsTT, 0))// F -> ofBlock...(gt.blockcasingsTT, 0, ...);
            .addElement('G', ofBlock(sBlockCasingsTT, 1))// G -> ofBlock...(gt.blockcasingsTT, 1, ...);
            .addElement('H', ofBlock(sBlockCasingsTT, 2))// H -> ofBlock...(gt.blockcasingsTT, 2, ...);
            .addElement('I', ofBlock(sBlockCasingsTT, 3))// I -> ofBlock...(gt.blockcasingsTT, 3, ...);
            .addElement('J', ofBlock(sBlockCasingsTT, 7))// J -> ofBlock...(gt.blockcasingsTT, 7, ...);
            .addElement('L', ofBlock(radiationProtectionSteelFrame, 0)) // L ->
            // ofBlock...(radiationProtectionSteelFrame, 0,
            // ...);
            .addElement('K', ofBlock(blockCasings3Misc, 15)) // K -> ofBlock...(gtplusplus.blockcasings.3, 15, ...);
            .addElement('M', ofBlock(QuantumGlassBlock.INSTANCE, 0)) // M -> ofBlock...(tile.quantumGlass, 0, ...);
            .addElement('O', ofBlock(pylon, 1))
            // .addElement('N', ofBlock(Block.getBlockById(1), 0))
            .addElement(
                'P',
                ofChain(RackHatchElement.INSTANCE.newAny(textureOffset + 3, 2), ofBlock(Block.getBlockById(0), 0)))
            // .addElement('P', ofBlock(LASERpipeBlock.getBlock(), 15472))
            // .addElement('K', ofBlock(Block.getBlockById(1), 0))
            // .addElement('k', ofBlock(, 7))
            .addElement(
                'E',
                StructureUtility.ofChain(
                    GT_StructureUtility.ofHatchAdder(TST_Computer::addToMachineList, textureOffset + 2, 1),
                    // GT_StructureUtility
                    // .ofHatchAdder(TST_Computer::addExoticEnergyInputToMachineList, textureOffset + 2, 1),
                    // GT_StructureUtility.ofHatchAdder(TST_Computer::addInputToMachineList, textureOffset + 2, 1),
                    // GT_StructureUtility.ofHatchAdder(TST_Computer::addOutputToMachineList, textureOffset + 2, 1),
                    // GT_StructureUtility.ofHatchAdder(TST_Computer::addDataConnectorToMachineList, textureOffset + 2,
                    // 1),
                    StructureUtility.ofBlock(IGBlocks.SpaceElevatorCasing, 2)))
            .addElement(
                'Q',
                ofChain(RackHatchElement.INSTANCE.newAny(textureOffset + 3, 2), ofBlock(sBlockCasingsTT, 3)))
            .build();
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    private enum RackHatchElement implements IHatchElement<TST_Computer> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(GT_MetaTileEntity_Hatch_Rack.class);
        }

        @Override
        public IGT_HatchAdder<? super TST_Computer> adder() {
            return TST_Computer::addRackToMachineList;
        }

        @Override
        public long count(TST_Computer t) {
            return t.eRacks.size();
        }
    }
}
