package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import static com.Nxer.TwistSpaceTechnology.util.TSTUtils.tr;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.ART;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.client.effect.MegaNqReactorParticleBatch;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_MegaNqReactor;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TST_GeneratorBase;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.loader.Loaders;
import goodgenerator.util.CrackRecipeAdder;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.render.IMTERenderer;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * 可使用的激发流体/冷却液与大型硅岩反应堆一致
 * 最大并行可在配置文件中调整。
 * 连续运行可提升发电量，24 小时达到 2.25 倍上限，停机时衰减。
 */
@SkipGenerateDescription
public class TST_MegaNqReactor extends TST_GeneratorBase<TST_MegaNqReactor>
    implements IConstructable, ISurvivalConstructable, IMTERenderer {

    // region Class Constructor
    public TST_MegaNqReactor(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        registerTooltipCredits(ID.LONEI, ART, ID.HUAN_F);
    }

    public TST_MegaNqReactor(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaNqReactor(this.mName);
    }
    // endregion

    // region Structure
    protected static final String STRUCTURE_PIECE_MAIN = "STRUCTURE_PIECE_MAIN_MNG";
    protected final int hOffset = 15, vOffset = 25, dOffset = 1;
    protected static IStructureDefinition<TST_MegaNqReactor> STRUCTURE_DEFINITION = null;

    // spotless:off
    // structure by Tuna
    protected static final String[][] SHAPE_MAIN = new String[][]{
        {"                               ","    FFFFFFF         FFFFFFF    ","   FDDDDDDDF       FDDDDDDDF   ","  FDDDDDDDDDFF   FFDDDDDDDDDF  "," FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF"," FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ","   FDDDDDDDDDDDDDDDDDDDDDDDF   ","   FDDDDDDDDDDDDDDDDDDDDDDDF   ","   FDDDDDDDDDDDDDDDDDDDDDDDF   ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  "," FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF"," FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ","  FDDDDDDDDDFF   FFDDDDDDDDDF  ","   FDDDDDDDF       FDDDDDDDF   ","    FFFFFFF         FFFFFFF    "},
        {"                               ","                               ","  FDBBBBBBB         BBBBBBBDF  "," FDDDDDDDDDB       BDDDDDDDDDF "," DDDDDDDDDDDBB   BBDDDDDDDDDDD "," BDDDDDDDDDDDDBBBDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ","  BDDDDDDDDDDDDDDDDDDDDDDDDDB  ","   BDDDDDDDDDDDDDDDDDDDDDDDB   ","   BDDDDDDDDDDDDDDDDDDDDDDDB   ","    BDDDDDDDDDDDDDDDDDDDDDB    ","    BDDDDDDDDDDDDDDDDDDDDDB    ","    BDDDDDDDDDDDDDDDDDDDDDB    ","   BDDDDDDDDDDDDDDDDDDDDDDDB   ","   BDDDDDDDDDDDDDDDDDDDDDDDB   ","  BDDDDDDDDDDDDDDDDDDDDDDDDDB  "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDBBBDDDDDDDDDDDDB "," DDDDDDDDDDDBB   BBDDDDDDDDDDD "," FDDDDDDDDDB       BDDDDDDDDDF ","  FDBBBBBBB         BBBBBBBDF  ","                               "},
        {"                               ","    FFFFFFF         FFFFFFF    "," FDFDDDDDDDF       FDDDDDDDFDF "," DDDDDDDDDDDFF   FFDDDDDDDDDDD "," FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ","FDDDFF                   FFDDDF","FDDDFED                 DEFDDDF","FDDD DED               DED DDDF","FDDD  DED             DED  DDDF","FDDD   DED           DED   DDDF","FDDD    DED         DED    DDDF","FDDD     DED       DED     DDDF"," FDD      DED     DED      DDF ","  FD       DED   DED       DF  ","  FD        DED DED        DF  ","   F         DDFDD         F   ","   F          FCF          F   ","   F         DDFDD         F   ","  FD        DED DED        DF  ","  FD       DED   DED       DF  "," FDD      DED     DED      DDF ","FDDD     DED       DED     DDDF","FDDD    DED         DED    DDDF","FDDD   DED           DED   DDDF","FDDD  DED             DED  DDDF","FDDD DED               DED DDDF","FDDDFED                 DEFDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF"," FDDDDDDDDDDDDFFFDDDDDDDDDDDDF "," DDDDDDDDDDDFF   FFDDDDDDDDDDD "," FDFDDDDDDDF       FDDDDDDDFDF ","    FFFFFFF         FFFFFFF    "},
        {"                               ","                               "," FF FF                   FF FF "," FDFFFF                 FFFFDF ","  FFFFF                 FFFFF  "," FFFB                     BFFF "," FFF A                   A FFF ","  FF  A                 A  FF  ","       A               A       ","        A             A        ","         A           A         ","          A         A          ","           A       A           ","            A     A            ","             A   A             ","              DFD              ","              FCF              ","              DFD              ","             A   A             ","            A     A            ","           A       A           ","          A         A          ","         A           A         ","        A             A        ","       A               A       ","  FF  A                 A  FF  "," FFF A                   A FFF "," FFFFF                   FFFFF ","  FFFFF                 FFFFF  "," FDFFFF                 FFFFDF "," FF FF                   FF FF ","                               "},
        {"                               ","                               "," FD                         DF "," DDFFFF                 FFFFDD ","  FEBB                   BBEF  ","  FBB                     BBF  ","  FB                       BF  ","  F                         F  ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              DFD              ","              FCF              ","              DFD              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","  F                         F  ","  FB                       BF  ","  FBB                     BBF  ","  FEBB                   BBEF  "," DDFFFF                 FFFFDD "," FD                         DF ","                               "},
        {"                               ","                               ","                               ","  FFFF                   FFFF  ","  FEB                     BEF  ","  FB                       BF  ","  F                         F  ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              DFD              ","              FCF              ","              DFD              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","  F                         F  ","  FB                       BF  ","  FEB                     BEF  ","  FFFF                   FFFF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FFF                     FFF  ","  FE                       EF  ","  F                         F  ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              AAA              ","             AAAAA             ","             AAAAA             ","             AAAAA             ","              AAA              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","  F                         F  ","  FE                       EF  ","  FFF                     FFF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","             AAAAA             ","            AAAAAAA            ","           AAA   AAA           ","           AA     AA           ","           AA     AA           ","           AA     AA           ","           AAA   AAA           ","            AAAAAAA            ","             AAAAA             ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","                               ","                               ","             AAAAA             ","            A     A            ","           A       A           ","          A         A          ","          A         A          ","          A         A          ","          A         A          ","          A         A          ","           A       A           ","            A     A            ","             AAAAA             ","                               ","                               ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","                               ","             AAAAA             ","            A     A            ","           A       A           ","          A         A          ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","          A         A          ","           A       A           ","            A     A            ","             AAAAA             ","                               ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","                               ","            AAAAAAA            ","           A       A           ","          A         A          ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","          A         A          ","           A       A           ","            AAAAAAA            ","                               ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","              AAA              ","           AAA   AAA           ","          A         A          ","         A           A         ","         A           A         ","         A           A         ","        A             A        ","        A             A        ","        A             A        ","         A           A         ","         A           A         ","         A           A         ","          A         A          ","           AAA   AAA           ","              AAA              ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","            DDDDDDD            ","          DD       DD          ","         D   AAAAA   D         ","        D  AA     AA  D        ","       D  A         A  D       ","       D A           A D       ","      D  A           A  D      ","      D A             A D      ","      D A             A D      ","      D A             A D      ","      D A             A D      ","      D A             A D      ","      D  A           A  D      ","       D A           A D       ","       D  A         A  D       ","        D  AA     AA  D        ","         D   AAAAA   D         ","          DD       DD          ","            DDDDDDD            ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","            DDDDDDD            ","          DDBBBBBBBDD          ","         DBB       BBD         ","        DB   AAAAA   BD        ","       DB  AA     AA  BD       ","      DB  A         A  BD      ","      DB A           A BD      ","     DB  A           A  BD     ","     DB A             A BD     ","     DB A             A BD     ","     DB A             A BD     ","     DB A             A BD     ","     DB A             A BD     ","     DB  A           A  BD     ","      DB A           A BD      ","      DB  A         A  BD      ","       DB  AA     AA  BD       ","        DB   AAAAA   BD        ","         DBB       BBD         ","          DDBBBBBBBDD          ","            DDDDDDD            ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","            DDDDDDD            ","          DD       DD          ","         D   AAAAA   D         ","        D  AA     AA  D        ","       D  A         A  D       ","       D A           A D       ","      D  A           A  D      ","      D A             A D      ","      D A             A D      ","      D A             A D      ","      D A             A D      ","      D A             A D      ","      D  A           A  D      ","       D A           A D       ","       D  A         A  D       ","        D  AA     AA  D        ","         D   AAAAA   D         ","          DD       DD          ","            DDDDDDD            ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","              AAA              ","           AAA   AAA           ","          A         A          ","         A           A         ","         A           A         ","         A           A         ","        A             A        ","        A             A        ","        A             A        ","         A           A         ","         A           A         ","         A           A         ","          A         A          ","           AAA   AAA           ","              AAA              ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","                               ","            AAAAAAA            ","           A       A           ","          A         A          ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","          A         A          ","           A       A           ","            AAAAAAA            ","                               ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","                               ","             AAAAA             ","            A     A            ","           A       A           ","          A         A          ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","         A           A         ","          A         A          ","           A       A           ","            A     A            ","             AAAAA             ","                               ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","                               ","                               ","             AAAAA             ","            A     A            ","           A       A           ","          A         A          ","          A         A          ","          A         A          ","          A         A          ","          A         A          ","           A       A           ","            A     A            ","             AAAAA             ","                               ","                               ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FF                       FF  ","  FE                       EF  ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","             AAAAA             ","            AAAAAAA            ","           AAA   AAA           ","           AA     AA           ","           AA     AA           ","           AA     AA           ","           AAA   AAA           ","            AAAAAAA            ","             AAAAA             ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","  FE                       EF  ","  FF                       FF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FFF                     FFF  ","  FE                       EF  ","  F                         F  ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              AAA              ","             AAAAA             ","             AAAAA             ","             AAAAA             ","              AAA              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","  F                         F  ","  FE                       EF  ","  FFF                     FFF  ","                               ","                               "},
        {"                               ","                               ","                               ","  FFFF                   FFFF  ","  FEB                     BEF  ","  FB                       BF  ","  F                         F  ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              DFD              ","              FCF              ","              DFD              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","  F                         F  ","  FB                       BF  ","  FEB                     BEF  ","  FFFF                   FFFF  ","                               ","                               "},
        {"                               ","                               ","  F                         F  "," FDFFFF                 FFFFDF ","  FEBB                   BBEF  ","  FBB                     BBF  ","  FB A                   A BF  ","  F   A                 A   F  ","       A               A       ","        A             A        ","         A           A         ","          A         A          ","           A       A           ","            A     A            ","             A   A             ","              DFD              ","              FCF              ","              DFD              ","             A   A             ","            A     A            ","           A       A           ","          A         A          ","         A           A         ","        A             A        ","       A               A       ","  F   A                 A   F  ","  FB A                   A BF  ","  FBB                     BBF  ","  FEBB                   BBEF  "," FDFFFF                 FFFFDF ","  F                         F  ","                               "},
        {"                               ","                               ","  F FF                   FF F  "," FDFFFF                 FFFFDF ","  FFFFF                 FFFFF  "," FFFFF                   FFFFF "," FFFFED                 DEFFFF ","  FF DED               DED FF  ","      DED             DEDD     ","       DED           DED       ","        DED         DED        ","         DED       DED         ","          DED     DED          ","           DED   DED           ","            DED DED            ","             DDFDD             ","              FCF              ","             DDFDD             ","            DED DED            ","           DED   DED           ","          DED     DED          ","         DED       DED         ","        DED         DED        ","       DED           DED       ","      DED             DED      ","  FF DED               DED FF  "," FFFFED                 DEFFFF "," FFFFF                   FFFFF ","  FFFFF                 FFFFF  "," FDFFFF                 FFFFDF ","  F FF                   FF F  ","                               "},
        {"                               ","    FFFFFFF         FFFFFFF    ","  FFDDDDDDDF       FDDDDDDDFF  "," FDDDDDDDDDDFF   FFDDDDDDDDDDF "," FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF"," FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ","   FDDDDDDDDDDDDDDDDDDDDDDDF   ","   FDDDDDDDDDDDCDDDDDDDDDDDF   ","   FDDDDDDDDDDDDDDDDDDDDDDDF   ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  "," FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF"," FDDDDDDDDDDDDFFFDDDDDDDDDDDDF "," FDDDDDDDDDDFF   FFDDDDDDDDDDF ","  FFDDDDDDDF       FDDDDDDDFF  ","    FFFFFFF         FFFFFFF    "},
        {"                               ","               ~               ","  FDBBBBBBB    C    BBBBBBBDF  "," FDDDDDDDDDB   C   BDDDDDDDDDF "," DDDDDDDDDDDBB C BBDDDDDDDDDDD "," BDDDDDDDDDDDDBBBDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ","  BDDDDDDDDDDDDDDDDDDDDDDDDDB  ","   BDDDDDDDDDDDDDDDDDDDDDDDB   ","   BDDDDDDDDDDDDDDDDDDDDDDDB   ","    BDDDDDDDDDDDDDDDDDDDDDB    ","    BDDDDDDDDDDDDDDDDDDDDDB    ","    BDDDDDDDDDDDDDDDDDDDDDB    ","   BDDDDDDDDDDDDDDDDDDDDDDDB   ","   BDDDDDDDDDDDDDDDDDDDDDDDB   ","  BDDDDDDDDDDDDDDDDDDDDDDDDDB  "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDDDDDDDDDDDDDDDDB "," BDDDDDDDDDDDDBBBDDDDDDDDDDDDB ","  DDDDDDDDDDBB   BBDDDDDDDDDDD "," FDDDDDDDDDB       BDDDDDDDDDF ","  FDBBBBBBB         BBBBBBBDF  ","                               "},
        {"              DDD              ","    FFFFFFF   DDD   FFFFFFF    ","   FDDDDDDDF  DDD  FDDDDDDDF   ","  FDDDDDDDDDFFDDDFFDDDDDDDDDF  "," FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF"," FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ","   FDDDDDDDDDDDDDDDDDDDDDDDF   ","   FDDDDDDDDDDDDDDDDDDDDDDDF   ","   FDDDDDDDDDDDDDDDDDDDDDDDF   ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ","  FDDDDDDDDDDDDDDDDDDDDDDDDDF  "," FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF","FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF"," FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ","  FDDDDDDDDDFF   FFDDDDDDDDDF  ","   FDDDDDDDF       FDDDDDDDF   ","    FFFFFFF         FFFFFFF    "}
    };
    // spotless:on

    @Override
    public IStructureDefinition<TST_MegaNqReactor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaNqReactor>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(SHAPE_MAIN))
                .addElement(
                    'D',
                    GTStructureUtility.buildHatchAdder(TST_MegaNqReactor.class)
                        .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 3))
                        .hint(1)
                        .atLeast(ExoticDynamo.or(Dynamo), ExoticEnergy.or(Energy), InputHatch, OutputHatch, Maintenance)
                        .buildAndChain(GregTechAPI.sBlockCasings8, 3))
                .addElement('A', chainAllGlasses())
                .addElement('B', ofBlock(Loaders.MAR_Casing, 0))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings1, 14))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings9, 14))
                .addElement('F', ofBlock(TstBlocks.MetaBlockCasing02, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, hOffset, vOffset, dOffset);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) {
            return -1;
        }
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hOffset,
            vOffset,
            dOffset,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        boolean formed = checkPiece(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset, errors);
        if (formed) {
            checkHasInputHatch(errors);
            checkHasOutputHatch(errors);
            updateCoreFxCenter();
        }
    }
    // endregion

    // region Processing Logic
    protected static final int LIQUID_AIR_PER_SECOND = 2400;
    protected static final int TICKS_PER_SECOND = 20;
    protected static final int[] COOLANT_EFFICIENCY = { 500, 275, 150, 105 };
    protected static final int[] EXCITED_LIQUID_COEFF = { 64, 16, 4, 3, 2 };
    protected static final long TICKS_TO_MAX_BONUS = 24L * 60 * 60 * TICKS_PER_SECOND;
    protected static final long DECAY_PER_IDLE_TICK = 20L;
    protected static final int DEFAULT_COOLANT_EFFICIENCY = 100;

    protected static final int OUTPUT_UNIT_FIRST_TIER = 8;
    protected static final int OUTPUT_UNIT_LAST_TIER_EXCLUSIVE = 15;
    public static final long[] OUTPUT_UNIT_VALUES = Arrays
        .copyOfRange(GTValues.V, OUTPUT_UNIT_FIRST_TIER, OUTPUT_UNIT_LAST_TIER_EXCLUSIVE);
    public static final String[] OUTPUT_UNIT_NAMES = Arrays
        .stream(GTValues.VN, OUTPUT_UNIT_FIRST_TIER, OUTPUT_UNIT_LAST_TIER_EXCLUSIVE)
        .map(name -> name + "/t")
        .toArray(String[]::new);

    protected static List<Pair<FluidStack, Integer>> excitedTiers;
    protected static List<Pair<FluidStack, Integer>> coolantTiers;

    protected static List<Pair<FluidStack, Integer>> getExcitedTiers() {
        if (excitedTiers == null) {
            List<Pair<FluidStack, Integer>> tiers = new ArrayList<>(5);
            addTier(tiers, materialFluid(Materials.Space, 20), EXCITED_LIQUID_COEFF[0]);
            addTier(tiers, GGMaterial.atomicSeparationCatalyst.getMolten(20), EXCITED_LIQUID_COEFF[1]);
            addTier(tiers, materialFluid(Materials.Naquadah, 20), EXCITED_LIQUID_COEFF[2]);
            addTier(tiers, materialFluid(Materials.Uranium235, 180), EXCITED_LIQUID_COEFF[3]);
            addTier(tiers, materialFluid(Materials.Caesium, 180), EXCITED_LIQUID_COEFF[4]);
            excitedTiers = tiers;
        }
        return excitedTiers;
    }

    protected static List<Pair<FluidStack, Integer>> getCoolantTiers() {
        if (coolantTiers == null) {
            List<Pair<FluidStack, Integer>> tiers = new ArrayList<>(4);
            addTier(tiers, materialFluid(Materials.Time, 20), COOLANT_EFFICIENCY[0]);
            addTier(tiers, new FluidStack(TFFluids.fluidCryotheum, 1_000), COOLANT_EFFICIENCY[1]);
            addTier(tiers, materialFluid(Materials.SuperCoolant, 1_000), COOLANT_EFFICIENCY[2]);
            addTier(tiers, GTModHandler.getIC2Coolant(1_000), COOLANT_EFFICIENCY[3]);
            coolantTiers = tiers;
        }
        return coolantTiers;
    }

    @Nullable
    protected static FluidStack materialFluid(@Nonnull Materials material, int amount) {
        FluidStack stack = material.getMolten(amount);
        if (stack != null) {
            return stack;
        }
        stack = material.getFluid(amount);
        if (stack != null) {
            return stack;
        }
        return material.getGas(amount);
    }

    protected static void addTier(List<Pair<FluidStack, Integer>> tiers, @Nullable FluidStack fluid, int value) {
        if (fluid != null) {
            tiers.add(Pair.of(fluid, value));
        }
    }

    protected FluidStack lockedFluid = null;
    protected int times = 1;
    protected int basicOutput;
    protected int parallel = 1;
    protected long runTimeTicks = 0;
    protected int cachedCoolantEfficiency = DEFAULT_COOLANT_EFFICIENCY;
    protected int cachedTimeMultiplier = 1;
    protected int outputUnitIndex = 0;
    protected String cachedFuelName = "";
    protected long cachedFuelAmount = 0;
    protected UUID wirelessOwner = null;
    protected boolean wirelessModeActive = false;
    protected BigInteger wirelessBufferedEU = BigInteger.ZERO;
    protected double coreFxX;
    protected double coreFxY;
    protected double coreFxZ;
    protected long lastCoreParticleBatchTick = -1L;
    protected boolean isRenderActive = false;
    protected static final double CORE_CENTER_OFFSET_X = 15.0D;
    protected static final double CORE_CENTER_OFFSET_Y = 12.0D;
    protected static final double CORE_CENTER_OFFSET_Z = 0.0D;

    /**
     * 与 {@code MegaNqReactorParticle.LIFETIME_TICKS}（200）配合：约每 1/3 寿命一批，平均近 3 层叠加。
     */
    protected static final int CORE_PARTICLE_SPAWN_INTERVAL_TICKS = 67;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GoodGeneratorRecipeMaps.naquadahReactorFuels;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        FluidInventoryView fluidView = createFluidInventoryView(getStoredFluids());
        FluidStack[] fluidArray = fluidView.toFluidArray();

        GTRecipe tRecipe = GoodGeneratorRecipeMaps.naquadahReactorFuels.findRecipeQuery()
            .fluids(fluidArray)
            .find();
        if (tRecipe == null) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        Pair<FluidStack, Integer> excitedInfo = getExcited(fluidView);
        int coefficient = excitedInfo == null ? 1 : excitedInfo.getValue();
        FluidStack fuelInput = tRecipe.mFluidInputs[0];

        long perParallelFuelLong = (long) fuelInput.amount * coefficient;
        if (perParallelFuelLong <= 0 || perParallelFuelLong > Integer.MAX_VALUE) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }
        int perParallelFuel = (int) perParallelFuelLong;
        int fuelAmount = fluidView.getAmount(fuelInput);
        int intLimitCap = Integer.MAX_VALUE / perParallelFuel;
        int maxParallel = Math.min(Math.min(Config.Parallel_MegaNqReactor, fuelAmount / perParallelFuel), intLimitCap);
        if (maxParallel <= 0) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }
        int consumedFuelAmount = perParallelFuel * maxParallel;
        if (!fluidView.hasAtLeast(fuelInput, consumedFuelAmount)) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        int recipeTicks = tRecipe.mDuration;
        int recipeSeconds = recipeSecondsFromDurationTicks(recipeTicks);
        int efficiencyForRun = DEFAULT_COOLANT_EFFICIENCY;
        int timeMultForRun = 1;

        ArrayList<FluidStack> requirements = new ArrayList<>(6);
        FluidStack fuelReq = copyFluid(fuelInput, consumedFuelAmount);
        if (fuelReq == null) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }
        requirements.add(fuelReq);

        if (recipeSeconds > 0 && maxParallel > 0) {
            long airTotalLong = (long) LIQUID_AIR_PER_SECOND * maxParallel * recipeSeconds;
            if (airTotalLong > Integer.MAX_VALUE) {
                return CheckRecipeResultRegistry.NO_FUEL_FOUND;
            }
            int airAmount = (int) airTotalLong;
            if (airAmount > 0) {
                FluidStack liquidAir = Materials.LiquidAir.getFluid(airAmount);
                if (!fluidView.hasAtLeast(liquidAir, airAmount)) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                requirements.add(liquidAir);
            }
        }

        Pair<FluidStack, Integer> coolantSelection = selectCoolant(fluidView, maxParallel);
        if (coolantSelection != null) {
            efficiencyForRun = coolantSelection.getValue();
            if (recipeSeconds > 0) {
                FluidStack oneSecondCoolant = coolantSelection.getKey();
                int totalCoolantAmt = safeMulFluidAmount((long) oneSecondCoolant.amount, recipeSeconds);
                if (totalCoolantAmt < 0) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                FluidStack fullCoolant = copyFluid(oneSecondCoolant, totalCoolantAmt);
                if (fullCoolant == null) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                if (!fluidView.hasAtLeast(fullCoolant, totalCoolantAmt)) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                requirements.add(fullCoolant);
            }
        }

        FluidStack lockedForRun = null;
        if (excitedInfo != null) {
            lockedForRun = excitedInfo.getKey()
                .copy();
            timeMultForRun = coefficient;
            if (recipeSeconds > 0) {
                long perSecondExcited = (long) lockedForRun.amount * maxParallel;
                int totalExcitedAmt = safeMulFluidAmount(perSecondExcited, recipeSeconds);
                if (totalExcitedAmt < 0) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                FluidStack excitedReq = copyFluid(lockedForRun, totalExcitedAmt);
                if (excitedReq == null) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                if (!fluidView.hasAtLeast(excitedReq, totalExcitedAmt)) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                requirements.add(excitedReq);
            }
        }

        if (!consumeFluidRequirements(requirements)) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        basicOutput = tRecipe.mSpecialValue;
        times = coefficient;
        parallel = maxParallel;
        lockedFluid = lockedForRun;
        cachedFuelName = fuelInput.getLocalizedName();
        cachedFuelAmount = consumedFuelAmount;
        mMaxProgresstime = recipeTicks;
        mEfficiency = Math.max(0, 10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mOutputFluids = copyFluidOutputs(tRecipe.mFluidOutputs, (long) maxParallel * coefficient);
        cachedCoolantEfficiency = efficiencyForRun;
        cachedTimeMultiplier = timeMultForRun;
        lEUt = (long) ((double) basicOutput * cachedCoolantEfficiency
            * cachedTimeMultiplier
            / 100d
            * maxParallel
            * getRuntimeBonus());
        return CheckRecipeResultRegistry.GENERATING;
    }

    protected static FluidInventoryView createFluidInventoryView(List<FluidStack> storedFluids) {
        FluidInventoryView view = new FluidInventoryView();
        for (FluidStack storedFluid : storedFluids) {
            view.add(storedFluid);
        }
        return view;
    }

    @Nullable
    protected static FluidStack copyFluid(@Nullable FluidStack fluidStack, int amount) {
        if (fluidStack == null || fluidStack.getFluid() == null || amount <= 0) {
            return null;
        }
        return CrackRecipeAdder.copyFluidWithAmount(fluidStack, amount);
    }

    protected static FluidStack[] copyFluidOutputs(@Nullable FluidStack[] outputs, long multiplier) {
        if (outputs == null || outputs.length == 0 || multiplier <= 0) {
            return null;
        }

        int totalSize = 0;
        for (FluidStack output : outputs) {
            if (output == null) {
                continue;
            }

            long totalAmount = (long) output.amount * multiplier;

            totalSize += (int) ((totalAmount + Integer.MAX_VALUE - 1L) / Integer.MAX_VALUE);
        }

        FluidStack[] result = new FluidStack[totalSize];

        int index = 0;

        for (FluidStack output : outputs) {
            if (output == null) {
                continue;
            }

            long totalAmount = (long) output.amount * multiplier;

            while (totalAmount > 0) {
                int splitAmount = (int) Math.min(Integer.MAX_VALUE, totalAmount);

                result[index++] = copyFluid(output, splitAmount);

                totalAmount -= splitAmount;
            }
        }

        return result;
    }

    protected boolean consumeFluidStacks(@Nullable FluidStack template, int requiredAmount) {
        FluidStack requiredFluid = copyFluid(template, requiredAmount);
        if (requiredFluid == null) {
            return false;
        }
        ArrayList<FluidStack> requirements = new ArrayList<>(1);
        requirements.add(requiredFluid);
        return consumeFluidRequirements(requirements);
    }

    /**
     * 统一通过 {@link #depleteInput(FluidStack)} 抽取，兼容普通输入仓与 ME 输入仓。
     */
    protected boolean consumeFluidRequirements(List<FluidStack> requirements) {
        List<FluidStack> mergedRequirements = mergeFluidRequirements(requirements);
        if (mergedRequirements.isEmpty()) {
            return true;
        }
        for (FluidStack requirement : mergedRequirements) {
            if (!depleteInput(requirement)) {
                return false;
            }
        }
        updateInputHatchFluids();
        return true;
    }

    protected static List<FluidStack> mergeFluidRequirements(List<FluidStack> requirements) {
        Object2ObjectOpenHashMap<FluidId, FluidStack> mergedById = new Object2ObjectOpenHashMap<>();
        ArrayList<FluidStack> mergedRequirements = new ArrayList<>(requirements.size());
        for (FluidStack requirement : requirements) {
            if (requirement == null || requirement.amount <= 0 || requirement.getFluid() == null) {
                continue;
            }
            FluidId id = FluidId.create(requirement);
            FluidStack mergedRequirement = mergedById.get(id);
            if (mergedRequirement == null) {
                mergedRequirement = copyFluid(requirement, requirement.amount);
                if (mergedRequirement == null) {
                    continue;
                }
                mergedById.put(id, mergedRequirement);
                mergedRequirements.add(mergedRequirement);
            } else {
                mergedRequirement.amount += requirement.amount;
            }
        }
        return mergedRequirements;
    }

    protected void updateInputHatchFluids() {
        for (MTEHatchInput inputHatch : GTUtility.validMTEList(mInputHatches)) {
            inputHatch.updateSlots();
        }
    }

    protected static int recipeSecondsFromDurationTicks(int durationTicks) {
        if (durationTicks <= 0) {
            return 0;
        }
        return (durationTicks + TICKS_PER_SECOND - 1) / TICKS_PER_SECOND;
    }

    protected static int safeMulFluidAmount(long a, long b) {
        long p = a * b;
        if (p <= 0L || p > (long) Integer.MAX_VALUE) {
            return -1;
        }
        return (int) p;
    }

    public double getRuntimeBonus() {
        double timeBonus = 1d + Math.min(0.5d, (double) runTimeTicks / (double) TICKS_TO_MAX_BONUS);
        return timeBonus * timeBonus;
    }

    public int getOutputUnitIndex() {
        return outputUnitIndex;
    }

    public void setOutputUnitIndex(int index) {
        this.outputUnitIndex = Math.max(0, Math.min(index, OUTPUT_UNIT_NAMES.length - 1));
    }

    public int getBasicOutput() {
        return basicOutput;
    }

    public int getCachedCoolantEfficiency() {
        return cachedCoolantEfficiency;
    }

    public int getCachedTimeMultiplier() {
        return cachedTimeMultiplier;
    }

    public int getParallel() {
        return parallel;
    }

    public String getCachedFuelName() {
        return cachedFuelName;
    }

    public long getCachedFuelAmount() {
        return cachedFuelAmount;
    }

    public long getCurrentOutput() {
        return lEUt;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    protected void incrementRunTimeTicks() {
        runTimeTicks = Math.min(runTimeTicks + 1, TICKS_TO_MAX_BONUS);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && mMaxProgresstime <= 0 && runTimeTicks > 0) {
            runTimeTicks = Math.max(0, runTimeTicks - DECAY_PER_IDLE_TICK);
        }
    }

    @Override
    public void onValueUpdate(byte aValue) {
        isRenderActive = (aValue & 0x01) != 0;
    }

    @Override
    public byte getUpdateData() {
        byte data = 0;
        if (getBaseMetaTileEntity().isActive()) {
            data |= 0x01;
        }
        return data;
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        boolean isRunning = mMaxProgresstime != 0;
        boolean isServer = this.getBaseMetaTileEntity()
            .isServerSide();
        if (isServer) {
            incrementRunTimeTicks();
        }
        boolean lastTick = isServer && isRunning && mProgresstime >= mMaxProgresstime - 1;
        boolean result = super.onRunningTick(stack);
        if (lastTick) {
            flushWirelessEU();
        }
        return result;
    }

    @Override
    public boolean addEnergyOutput(long EU) {
        if (EU <= 0) {
            return true;
        }
        if (mDynamoHatches.isEmpty() && mExoticDynamoHatches.isEmpty()) {
            if (wirelessOwner == null) {
                wirelessOwner = getBaseMetaTileEntity().getOwnerUuid();
                if (wirelessOwner == null) {
                    return false;
                }
            }
            wirelessModeActive = true;
            wirelessBufferedEU = wirelessBufferedEU.add(BigInteger.valueOf(EU));
            return true;
        }
        boolean result = super.addEnergyOutput(EU);
        if (!result) {
            stopMachine(ShutDownReasonRegistry.INSUFFICIENT_DYNAMO);
        }
        return result;
    }

    protected void flushWirelessEU() {
        if (!wirelessModeActive || wirelessBufferedEU.signum() <= 0) {
            wirelessModeActive = false;
            wirelessBufferedEU = BigInteger.ZERO;
            return;
        }
        if (wirelessOwner == null) {
            wirelessOwner = getBaseMetaTileEntity().getOwnerUuid();
        }
        if (wirelessOwner != null) {
            addEUToGlobalEnergyMap(wirelessOwner, wirelessBufferedEU);
        }
        wirelessModeActive = false;
        wirelessBufferedEU = BigInteger.ZERO;
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        flushWirelessEU();
        lEUt = 0;
        cachedCoolantEfficiency = DEFAULT_COOLANT_EFFICIENCY;
        cachedTimeMultiplier = 1;
        super.stopMachine(reason);
    }

    protected void updateCoreFxCenter() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        ExtendedFacing facing = getExtendedFacing();
        ForgeDirection back = facing.getRelativeBackInWorld();
        ForgeDirection up = facing.getRelativeUpInWorld();
        coreFxX = base.getXCoord() + 0.5D
            + CORE_CENTER_OFFSET_X * back.offsetX
            + CORE_CENTER_OFFSET_Y * up.offsetX
            + CORE_CENTER_OFFSET_Z * back.offsetX;
        coreFxY = base.getYCoord() + 0.5D
            + CORE_CENTER_OFFSET_X * back.offsetY
            + CORE_CENTER_OFFSET_Y * up.offsetY
            + CORE_CENTER_OFFSET_Z * back.offsetY;
        coreFxZ = base.getZCoord() + 0.5D
            + CORE_CENTER_OFFSET_X * back.offsetZ
            + CORE_CENTER_OFFSET_Y * up.offsetZ
            + CORE_CENTER_OFFSET_Z * back.offsetZ;
    }

    /**
     * 运行时在客户端按 {@link #CORE_PARTICLE_SPAWN_INTERVAL_TICKS} 追加粒子批次；
     * 间隔约为单粒子寿命的三分之一，通常可见 2～3 批叠加；新批次不强制结束上一批。
     */
    @SideOnly(Side.CLIENT)
    protected void scheduleCoreParticleBatch() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        if (world == null || !world.isRemote) {
            return;
        }
        if (world.getTotalWorldTime() % CORE_PARTICLE_SPAWN_INTERVAL_TICKS != 0L) {
            return;
        }
        if (lastCoreParticleBatchTick == world.getTotalWorldTime()) {
            return;
        }
        lastCoreParticleBatchTick = world.getTotalWorldTime();
        updateCoreFxCenter();
        MegaNqReactorParticleBatch.spawn(world, coreFxX, coreFxY, coreFxZ);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        if (!isRenderActive) {
            return;
        }
        scheduleCoreParticleBatch();
    }

    public Pair<FluidStack, Integer> getExcited(FluidInventoryView fluidView) {
        for (Pair<FluidStack, Integer> tier : getExcitedTiers()) {
            FluidStack template = tier.getKey();
            if (fluidView.hasAtLeast(template, template.amount)) {
                return tier;
            }
        }
        return null;
    }

    @Nullable
    protected Pair<FluidStack, Integer> selectCoolant(FluidInventoryView fluidView, int count) {
        for (Pair<FluidStack, Integer> tier : getCoolantTiers()) {
            FluidStack template = tier.getKey();
            int requiredAmount = template.amount * count;
            FluidStack requiredFluid = copyFluid(template, requiredAmount);
            if (requiredFluid == null || !fluidView.hasAtLeast(requiredFluid, requiredAmount)) {
                continue;
            }
            return Pair.of(requiredFluid, tier.getValue());
        }
        return null;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new TST_Gui_MegaNqReactor(this);
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        // spotless:off
        // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.running_time
        // # Running Time:
        // #zh_CN 持续运行时间:
        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(() -> tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.running_time") + formatRunTime(runTimeTicks))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> runTimeTicks, val -> runTimeTicks = val))
            .widget(
                new TextWidget()
                    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.runtime_bonus
                    // # Runtime Bonus
                    // #zh_CN 持续运行奖励
                    .setStringSupplier(
                        () -> tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.runtime_bonus") + " "
                            + String.format("%.2f", getRuntimeBonus()) + "x")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(
                new TextWidget()
                    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.current_output
                    // # Current Output:
                    // #zh_CN 当前输出:
                    .setStringSupplier(() -> tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.current_output") + formatNumber(lEUt) + " EU/t")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> lEUt, val -> lEUt = val))
            .widget(new FakeSyncWidget.LongSyncer(() -> runTimeTicks, val -> runTimeTicks = val));
        // spotless:on
    }

    protected static String formatRunTime(long ticks) {
        long seconds = ticks / TICKS_PER_SECOND;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mTimes", this.times);
        aNBT.setInteger("mbasicOutput", this.basicOutput);
        aNBT.setInteger("mParallel", this.parallel);
        aNBT.setLong("mRunTimeTicks", this.runTimeTicks);
        if (lockedFluid != null) {
            aNBT.setString("mLockedFluidName", FluidRegistry.getFluidName(this.lockedFluid));
            aNBT.setInteger("mLockedFluidAmount", this.lockedFluid.amount);
        } else {
            aNBT.removeTag("mLockedFluidName");
            aNBT.removeTag("mLockedFluidAmount");
        }
        aNBT.setInteger("mCachedCoolantEff", this.cachedCoolantEfficiency);
        aNBT.setInteger("mCachedTimeMult", this.cachedTimeMultiplier);
        aNBT.setString("mWirelessBufferedEU", this.wirelessBufferedEU.toString());
        aNBT.setBoolean("mWirelessModeActive", this.wirelessModeActive);
        if (this.wirelessOwner != null) {
            aNBT.setString("mWirelessOwner", this.wirelessOwner.toString());
        } else {
            aNBT.removeTag("mWirelessOwner");
        }
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.times = aNBT.getInteger("mTimes");
        this.basicOutput = aNBT.getInteger("mbasicOutput");
        this.parallel = aNBT.getInteger("mParallel");
        this.runTimeTicks = aNBT.getLong("mRunTimeTicks");
        String lockedName = aNBT.getString("mLockedFluidName");
        if (FluidRegistry.getFluid(lockedName) != null) {
            this.lockedFluid = new FluidStack(
                FluidRegistry.getFluid(lockedName),
                aNBT.getInteger("mLockedFluidAmount"));
        } else {
            this.lockedFluid = null;
        }
        this.cachedCoolantEfficiency = aNBT.hasKey("mCachedCoolantEff") ? aNBT.getInteger("mCachedCoolantEff")
            : DEFAULT_COOLANT_EFFICIENCY;
        this.cachedTimeMultiplier = aNBT.hasKey("mCachedTimeMult") ? aNBT.getInteger("mCachedTimeMult") : 1;
        String bufferedEU = aNBT.getString("mWirelessBufferedEU");
        this.wirelessBufferedEU = bufferedEU.isEmpty() ? BigInteger.ZERO : new BigInteger(bufferedEU);
        this.wirelessModeActive = aNBT.getBoolean("mWirelessModeActive");
        String ownerString = aNBT.getString("mWirelessOwner");
        this.wirelessOwner = ownerString.isEmpty() ? null : UUID.fromString(ownerString);
        super.loadNBTData(aNBT);
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return TSTControllerTextures.getTexture(
            side,
            facing,
            aActive,
            getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 3)),
            NAQUADAH_REACTOR_SOLID_FRONT,
            NAQUADAH_REACTOR_SOLID_FRONT_GLOW,
            NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE,
            NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final TSTMultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.MegaNqReactor.tooltip.machine_type
        // # Naquadah Reactor,LNR
        // #zh_CN 大型硅岩反应堆
        tt.addMachineType(tr("tst.common.machine.MegaNqReactor.tooltip.machine_type"))
            // #tr tst.common.machine.MegaNqReactor.tooltip.info.01
            // # §aHuman resource Friendly!
            // #zh_CN §a人力友好型!
            .addInfo(tr("tst.common.machine.MegaNqReactor.tooltip.info.01"))
            // #tr tst.common.machine.MegaNqReactor.tooltip.info.02
            // # Similar to §5LNR§7, Maximum of§b 1,000 §7parallels
            // #zh_CN 就像§5大型硅岩反应堆§7一样, 但是拥有1000并行
            .addInfo(tr("tst.common.machine.MegaNqReactor.tooltip.info.02"))
            // #tr tst.common.machine.MegaNqReactor.tooltip.info.03
            // # §aContinuous operation increases power output
            // #zh_CN §a连续运行可提升发电量!
            .addInfo(tr("tst.common.machine.MegaNqReactor.tooltip.info.03"))
            // #tr tst.common.machine.MegaNqReactor.tooltip.info.04
            // # Up to §52.25x§7. Max in 24h, decays when idle.
            // #zh_CN 最多 §52.25x§7, 24小时达上限, 停机时衰减.
            .addInfo(tr("tst.common.machine.MegaNqReactor.tooltip.info.04"))
            // #tr tst.common.machine.MegaNqReactor.tooltip.info.05
            // # §bWithout a dynamo hatch, energy is output directly to the wireless EU network.§7
            // #zh_CN §b未安装动力仓时，能量将直接输出到无线EU网络§7.
            .addInfo(tr("tst.common.machine.MegaNqReactor.tooltip.info.05"))
            // #tr tst.common.machine.MegaNqReactor.tooltip.info.06
            // # §cWith dynamo hatches, ensure sufficient capacity or the machine will shut down.§7
            // #zh_CN §c安装动力仓时请保证容量足够, 否则机器会停机§7.
            .addInfo(tr("tst.common.machine.MegaNqReactor.tooltip.info.06"))
            .beginStructureBlock(SHAPE_MAIN)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Nested Classes

    public static class FluidInventoryView {

        protected final Object2IntOpenHashMap<FluidId> amounts = new Object2IntOpenHashMap<>();
        protected final Object2ObjectOpenHashMap<FluidId, FluidStack> mergedById = new Object2ObjectOpenHashMap<>();
        protected final List<FluidStack> mergedFluids = new ArrayList<>();

        public FluidInventoryView() {
            amounts.defaultReturnValue(0);
        }

        public void add(@Nullable FluidStack stack) {
            if (stack == null || stack.amount <= 0 || stack.getFluid() == null) {
                return;
            }
            FluidId id = FluidId.createNoCopy(stack.getFluid(), null, null);
            int previousAmount = amounts.addTo(id, stack.amount);
            FluidStack mergedFluid = mergedById.get(id);
            if (mergedFluid == null) {
                mergedFluid = copyFluid(stack, stack.amount);
                mergedById.put(id, mergedFluid);
                mergedFluids.add(mergedFluid);
            } else {
                mergedFluid.amount = previousAmount + stack.amount;
            }
        }

        public FluidStack[] toFluidArray() {
            return mergedFluids.toArray(new FluidStack[0]);
        }

        public List<FluidStack> getMergedFluids() {
            return mergedFluids;
        }

        public int getAmount(@Nullable FluidStack template) {
            if (template == null || template.getFluid() == null) {
                return 0;
            }
            return amounts.getInt(FluidId.createNoCopy(template.getFluid(), null, null));
        }

        public boolean hasAtLeast(@Nullable FluidStack template, int requiredAmount) {
            return requiredAmount <= 0 || getAmount(template) >= requiredAmount;
        }

        public boolean consume(@Nullable FluidStack template, int requiredAmount) {
            if (template == null || template.getFluid() == null || requiredAmount <= 0) {
                return false;
            }
            FluidId id = FluidId.createNoCopy(template.getFluid(), null, null);
            int currentAmount = amounts.getInt(id);
            if (currentAmount < requiredAmount) {
                return false;
            }
            int remainingAmount = currentAmount - requiredAmount;
            if (remainingAmount > 0) {
                amounts.put(id, remainingAmount);
            } else {
                amounts.removeInt(id);
            }
            updateMergedAmount(id, remainingAmount);
            return true;
        }

        public void updateMergedAmount(FluidId id, int newAmount) {
            FluidStack mergedFluid = mergedById.get(id);
            if (mergedFluid == null) {
                return;
            }
            if (newAmount > 0) {
                mergedFluid.amount = newAmount;
                return;
            }
            mergedById.remove(id);
            for (int i = 0; i < mergedFluids.size(); i++) {
                if (mergedFluids.get(i) == mergedFluid) {
                    mergedFluids.remove(i);
                    return;
                }
            }
        }
    }

    // endregion

}
