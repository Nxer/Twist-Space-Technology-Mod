package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.LaserBeaconRender;
import static com.Nxer.TwistSpaceTechnology.config.Config.StandardRecipeDuration_Second_LaserMeteorMiner;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.Author_Totto;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.Mod_TwistSpaceTechnology;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.entity.TileEntityLaserBeacon;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.BlockCasings8;
import gtPlusPlus.core.block.ModBlocks;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_LaserMeteorMiner extends MTEEnhancedMultiBlockBase<TST_LaserMeteorMiner>
    implements ISurvivalConstructable {

    public static Textures.BlockIcons.CustomIcon OVERLAY_FRONT_METEOR_MINER = new Textures.BlockIcons.CustomIcon(
        "gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER");
    public static Textures.BlockIcons.CustomIcon OVERLAY_FRONT_METEOR_MINER_ACTIVE = new Textures.BlockIcons.CustomIcon(
        "gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER_ACTIVE");
    public static Textures.BlockIcons.CustomIcon OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW = new Textures.BlockIcons.CustomIcon(
        "gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW");
    public static Textures.BlockIcons.CustomIcon OVERLAY_FRONT_METEOR_MINER_GLOW = new Textures.BlockIcons.CustomIcon(
        "gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER_GLOW");

    private static final int distanceFromMeteor = 48;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int horizontalOffSet_T1 = 9;
    protected static final int verticalOffSet_T1 = 13;
    protected static final int depthOffSet_T1 = 7;
    private static final String STRUCTURE_PIECE_TIER2 = "tier2";
    protected static final int horizontalOffSet_T2 = 9;
    protected static final int verticalOffSet_T2 = 15;
    protected static final int depthOffSet_T2 = 3;
    private static final int MAX_RADIUS = 40;
    private static IStructureDefinition<TST_LaserMeteorMiner> STRUCTURE_DEFINITION;
    protected TileEntityLaserBeacon renderer;
    private int currentRadius = MAX_RADIUS;
    private int xDrill, yDrill, zDrill;
    private int xStart, yStart, zStart;
    private int fortuneTier = 0;
    private boolean isStartInitialized = false;
    private boolean hasFinished = true;
    private boolean isWaiting = false;
    private boolean isResetting = false;
    Collection<ItemStack> res = new HashSet<>();
    private int multiTier = 0;

    @Override
    public IStructureDefinition<TST_LaserMeteorMiner> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_LaserMeteorMiner>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape_T1))
                .addShape(STRUCTURE_PIECE_TIER2, transpose(shape_T2))
                .addElement('A', chainAllGlasses())
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings1, 15)) // Superconducting Coil
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings4, 7)) // Fusion Coil Block
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 2)) // Mining Neutronium Casing
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 3)) // Mining Black Plutonium Casing
                .addElement('F', ofBlock(GregTechAPI.sBlockCasings9, 11)) // Heat-Resistant Trinium Plated Casing
                .addElement('G', ofFrame(Materials.Neutronium)) // Neutronium Frame
                .addElement('H', ofFrame(Materials.BlackPlutonium)) // Black Plutonium Frame
                .addElement('I', ofBlock(GregTechAPI.sBlockCasings5, 5)) // Naquadah Coil
                .addElement('J', ofFrame(Materials.StainlessSteel))
                .addElement('K', ofBlock(ModBlocks.blockSpecialMultiCasings, 6)) // Structural Solar Casings
                .addElement('L', ofBlock(ModBlocks.blockSpecialMultiCasings, 8)) // Thermally Insulated Casing
                .addElement(
                    'W',
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(OutputBus, Energy, Maintenance)
                        .casingIndex(TAE.getIndexFromPage(3, 9))
                        .dot(1)
                        .buildAndChain(ofBlock(ModBlocks.blockSpecialMultiCasings, 6)))
                .addElement(
                    'Y',
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(InputBus)
                        .adder(TST_LaserMeteorMiner::addInjector)
                        .casingIndex(TAE.getIndexFromPage(3, 9))
                        .dot(2)
                        .buildAndChain(ofBlock(ModBlocks.blockSpecialMultiCasings, 6)))
                .addElement(
                    'X',
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(OutputBus, Energy, Maintenance)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                        .dot(3)
                        .buildAndChain(ofBlock(GregTechAPI.sBlockCasings8, 2)))
                .addElement('Z', ofBlock(LaserBeaconRender, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // spotless:off

    /*
    A -> ofBlock...(blockAlloyGlass, 0, ...);
    B -> ofBlock...(gt.blockcasings, 15, ...);
    C -> ofBlock...(gt.blockcasings4, 7, ...);
    D -> ofBlock...(gt.blockcasings8, 2, ...);
    E -> ofBlock...(gt.blockcasings8, 3, ...);
    F -> ofBlock...(gt.blockcasings9, 11, ...);
    G -> ofBlock...(gt.blockframes, 129, ...);
    H -> ofBlock...(gt.blockframes, 388, ...);
    I -> ofBlock...(gt.blockcasings5, 5, ...);
    J -> ofBlock...(gt.blockframes, 306, ...);
    K -> ofBlock...(gtplusplus.blockspecialcasings.1, 6, ...); //
    L -> ofBlock...(gtplusplus.blockspecialcasings.1, 8, ...);
    W -> ofBlock...(tile.wood, 0, ...); // T1 hatches
    X -> ofBlock...(tile.wood, 0, ...); // T2 hatches
    Y -> ofBlock...(tile.stone, 0, ...); // special input bus
    Z -> ofSpecialTileAdder(com.Nxer.TwistSpaceTechnology.common.entity.TileEntityLaserBeacon, ...);
     */
    protected static final String[][] shape_T1 = new String[][]{
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         J         ","        J J        ","         J         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         J         ","        J J        ","       J   J       ","        J J        ","         J         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","         J         ","       J   J       ","                   ","      J     J      ","                   ","       J   J       ","         J         ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","         J         ","      J     J      ","                   ","                   ","     J   Z   J     ","                   ","                   ","      J     J      ","         J         ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","         J         ","     J       J     ","                   ","                   ","    J              ","    J    B    J    ","                   ","                   ","                   ","     J       J     ","         J         ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","         J         ","    J         J    ","                   ","                   ","                   ","         I         ","   J    IBI    J   ","         I         ","                   ","                   ","                   ","    J         J    ","         J         ","                   ","                   ","                   "},
        {"                   ","                   ","    JJJJJJJJJJJ    ","   JJLLLLLLLLLJJ   ","  JJLL       LLJJ  ","  JLL         LLJ  ","  JL           LJ  ","  JL           LJ  ","  JL     I     LJ  ","  JL    IBI    LJ  ","  JL     I     LJ  ","  JL           LJ  ","  JL           LJ  ","  JLL         LLJ  ","  JJLL       LLJJ  ","   JJLLLLLLLLLJJ   ","    JJJJJJJJJJJ    ","                   ","                   "},
        {"                   ","                   ","                   ","         J         ","      LLLLLLL      ","     LL     LL     ","    LL       LL    ","    L         L    ","    L    I    L    ","   JL   IBI   LJ   ","    L    I    L    ","    L         L    ","    LL       LL    ","     LL     LL     ","      LLLLLLL      ","         J         ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","         J         ","       LLLLL       ","      LL   LL      ","     LL  I  LL     ","     L  III  L     ","    JL IIBII LJ    ","     L  III  L     ","     LL  I  LL     ","      LL   LL      ","       LLLLL       ","         J         ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","         J         ","        LLL        ","       LLLLL       ","      LLLLLLL      ","     JLLLBLLLJ     ","      LLLLLLL      ","       LLLLL       ","        LLL        ","         J         ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","         J         ","        JJJ        ","       JKAKJ       ","      JJABAJJ      ","       JKAKJ       ","        JJJ        ","         J         ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","        KAK        ","        ABA        ","        KAK        ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","        KAK        ","        ABA        ","        KAK        ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","        W~W        ","       WKKKW       ","       WKBKW       ","       WKKKW       ","        WWW        ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","        KYK        ","       KKKKK       ","      KKBBBKK      ","      KKBBBKK      ","      KKBBBKK      ","       KKKKK       ","        KKK        ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","        K K        ","       K   K       ","      A     A      ","     K       K     ","    K         K    ","                   ","    K         K    ","     K       K     ","      A     A      ","       K   K       ","        K K        ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","        K K        ","                   ","       K   K       ","      A     A      ","     K       K     ","   K           K   ","                   ","   K           K   ","     K       K     ","      A     A      ","       K   K       ","                   ","        K K        ","                   ","                   ","                   "},
        {"                   ","                   ","        K K        ","                   ","                   ","       K   K       ","      A     A      ","     K       K     ","  K             K  ","                   ","  K             K  ","     K       K     ","      A     A      ","       K   K       ","                   ","                   ","        K K        ","                   ","                   "},
        {"         K         ","        K K        ","       K   K       ","       K   K       ","       K   K       ","      K     K      ","     KK     KK     ","  KKK         KKK  "," K               K ","K                 K"," K               K ","  KKK         KKK  ","     KK     KK     ","      K     K      ","       K   K       ","       K   K       ","       K   K       ","        K K        ","         K         "}
    };


    protected static final String[][] shape_T2 = new String[][]{
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         Z         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         B         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         B         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        EBE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        EBE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        EBE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        ECE        ","       ECBCE       ","        ECE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        ECE        ","       ECBCE       ","        ECE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        CCC        ","       ECBCE       ","        CCC        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","         E         ","         E         ","        C C        ","      EE B EE      ","        C C        ","         E         ","         E         ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        HEH        ","       HC CH       ","      EE B EE      ","       HC CH       ","        HEH        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","        HEH        ","       H C H       ","      H C C H      ","      EC B CE      ","      H C C H      ","       H C H       ","        HEH        ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","         E         ","        AEA        ","       E C E       ","      A     A      ","     EEC B CEE     ","      A     A      ","       E C E       ","        AEA        ","         E         ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","         E         ","        A A        ","       E C E       ","      A     A      ","     E C B C E     ","      A     A      ","       E C E       ","        A A        ","         E         ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","         E         ","         E         ","       EA AE       ","      EE   EE      ","      A     A      ","    EE       EE    ","      A     A      ","      EE   EE      ","       EA AE       ","         E         ","         E         ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","         ~         ","       DD DD       ","      D     D      ","     D  FFF  D     ","    D  F   F  D    ","    D F     F D    ","   E  F     F  E   ","    D F     F D    ","    D  F   F  D    ","     D  FFF  D     ","      D     D      ","       DD DD       ","         E         ","                   ","                   ","                   "},
        {"                   ","                   ","         X         ","        E E        ","                   ","                   ","                   ","                   ","   E           E   ","  X             X  ","   E           E   ","                   ","                   ","                   ","                   ","        E E        ","         X         ","                   ","                   "},
        {"                   ","         X         ","        X X        ","      GGG GGG      ","     GG     GG     ","    GG       GG    ","   GG         GG   ","   G           G   ","  XG           GX  "," X               X ","  XG           GX  ","   G           G   ","   GG         GG   ","    GG       GG    ","     GG     GG     ","      GGG GGG      ","        X X        ","         X         ","                   "},
        {"         X         ","        X X        ","       X   X       ","                   ","                   ","                   ","                   ","  X             X  "," X               X ","X                 X"," X               X ","  X             X  ","                   ","                   ","                   ","                   ","       X   X       ","        X X        ","         X         "},
        {"         X         ","        X X        ","       X   X       ","                   ","                   ","                   ","                   ","  X             X  "," X               X ","X                 X"," X               X ","  X             X  ","                   ","                   ","                   ","                   ","       X   X       ","        X X        ","         X         "},
        {"         X         ","        X X        ","                   ","                   ","                   ","                   ","                   ","                   "," X               X ","X                 X"," X               X ","                   ","                   ","                   ","                   ","                   ","                   ","        X X        ","         X         "},
        {"         X         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","X                 X","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         X         "}
    };

    // spotless:on

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    public TST_LaserMeteorMiner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_LaserMeteorMiner(String aName) {
        super(aName);
    }

    @Override
    public void onDisableWorking() {
        if (renderer != null) renderer.setShouldRender(false);
        super.onDisableWorking();
    }

    @Override
    public void onBlockDestroyed() {
        if (renderer != null) renderer.setShouldRender(false);
        super.onBlockDestroyed();
    }

    private boolean addInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof MTEHatchInputBus bus)) return false;
        bus.updateTexture(aBaseCasingIndex);
        return mInputBusses.add(bus);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (stackSize.stackSize > 1) {
            buildPiece(
                STRUCTURE_PIECE_TIER2,
                stackSize,
                hintsOnly,
                horizontalOffSet_T2,
                verticalOffSet_T2,
                depthOffSet_T2);
        } else {
            buildPiece(
                STRUCTURE_PIECE_MAIN,
                stackSize,
                hintsOnly,
                horizontalOffSet_T1,
                verticalOffSet_T1,
                depthOffSet_T1);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return stackSize.stackSize < 2
            ? survivalBuildPiece(
                STRUCTURE_PIECE_MAIN,
                stackSize,
                horizontalOffSet_T1,
                verticalOffSet_T1,
                depthOffSet_T1,
                elementBudget,
                env,
                false,
                true)
            : survivalBuildPiece(
                STRUCTURE_PIECE_TIER2,
                stackSize,
                horizontalOffSet_T2,
                verticalOffSet_T2,
                depthOffSet_T2,
                elementBudget,
                env,
                false,
                true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_LaserMeteorMiner(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        // spotless:off
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr TST_LaserMeteorMiner_tooltips_machineType
        // # Meteor Miner
        // #zh_CN 陨星采矿机
        tt.addMachineType(tr("TST_LaserMeteorMiner_tooltips_machineType"))
            // #tr TST_LaserMeteorMiner_tooltips_01
            // # Controller Block for the Laser Meteor Miner!
            // #zh_CN 激光陨星采矿场的控制器方块！
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_01"))
            // #tr TST_LaserMeteorMiner_tooltips_02
            // # To work properly the center of the meteor has to be 48 blocks above the highest block of the multi.
            // #zh_CN 陨星的中心必须位于机器最高点上方48个方块的位置, 机器方可正常工作.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_02"))
            // #tr TST_LaserMeteorMiner_tooltips_03
            // # The laser will mine in a radius of up to 40 blocks in each direction from the center of the meteor.
            // #zh_CN 激光将在陨星中心半径40方块内进行采矿作业.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_03"))
            // #tr TST_LaserMeteorMiner_tooltips_04
            // # All the chunks involved must be chunkloaded.
            // #zh_CN 所涉及区块都必须保证加载.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_04"))
            // #tr TST_LaserMeteorMiner_tooltips_05
            // # The laser will automatically set its radius based on the meteorite,
            // #zh_CN 激光将根据陨石自动设置其运行半径.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_05"))
            // #tr TST_LaserMeteorMiner_tooltips_06
            // # if it doesn't find any it will wait for a meteor to spawn,
            // #zh_CN 没有找到陨星时机器会等待陨星生成.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_06"))
            // #tr TST_LaserMeteorMiner_tooltips_07
            // # considering the block right above the center of the meteor (like Warded Glass).
            // #zh_CN 顾及了中心正上方的方块(比如守卫者玻璃).
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_07"))
            // #tr TST_LaserMeteorMiner_tooltips_08
            // # The reset button will restart the machine without optimizing the radius.
            // #zh_CN 点击重启按钮将重启机器, 并且不进行半径适配优化.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_08"))
            // #tr TST_LaserMeteorMiner_tooltips_needSchematic
            // # Machine need Meteor Miner Schematic put in controller slot to run.
            // #zh_CN 机器需要在控制器方块内放置陨星采矿场设计图才可运行.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_needSchematic"))
            // #tr TST_LaserMeteorMiner_tooltips_09
            // # {\RED}{\BOLD} TIER I
            // #zh_CN {\RED}{\BOLD} 等级 I
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_09"))
            // #tr TST_LaserMeteorMiner_tooltips_10
            // # Mines one block every cycle.
            // #zh_CN 每次运行挖掘一个方块.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_10"))
            // #tr TST_LaserMeteorMiner_tooltips_11
            // # Default Fortune is 0, it can be increased by putting in the input bus special pickaxes:
            // #zh_CN 默认没有时运效果. 输入总线内放置以下镐子可以获得时运效果:
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_11"))
            // #tr TST_LaserMeteorMiner_tooltips_12
            // # Fortune I: Pickaxe of the Core
            // #zh_CN 时运I : 炽心镐
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_12"))
            // #tr TST_LaserMeteorMiner_tooltips_13
            // # Fortune II: Bound Pickaxe
            // #zh_CN 时运II : 约束之镐
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_13"))
            // #tr TST_LaserMeteorMiner_tooltips_14
            // # Fortune III: Terra Shatterer
            // #zh_CN 时运III : 泰拉粉碎者
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_14"))
            // #tr TST_LaserMeteorMiner_tooltips_15
            // # {\RED}{\BOLD} TIER II
            // #zh_CN {\RED}{\BOLD} 等级 II
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_15"))
            // #tr TST_LaserMeteorMiner_tooltips_16
            // # Always has Fortune III
            // #zh_CN 总是时运III.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_16"))
            // #tr TST_LaserMeteorMiner_tooltips_17
            // # Mines one row every cycle.
            // #zh_CN 每次运行挖掘一行.
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_17"))
            // #tr TST_LaserMeteorMiner_tooltips_18
            // # {\BLUE}{\BOLD}Finally some good Meteors!
            // #zh_CN {\BLUE}{\BOLD}终是好陨星! (Finally some good Meteors!)
            .addInfo(tr("TST_LaserMeteorMiner_tooltips_18"))
            .addInfo(Author_Totto.getText())
            .addSeparator()
            // #tr TST_LaserMeteorMiner_tooltips_T1
            // # {\GOLD}{\BOLD}TIER I
            // #zh_CN {\GOLD}{\BOLD}等级 I
            .addStructureInfo(tr("TST_LaserMeteorMiner_tooltips_T1"))
            // #tr TST_LaserMeteorMiner_structure_info_T1_controller
            // # Center of the second layer above the ritual
            // #zh_CN 仪式上方第二层的中心
            .addController(tr("TST_LaserMeteorMiner_structure_info_T1_controller"))
            // #tr TST_LaserMeteorMiner_structure_info_T1_hatches
            // # Any Structural Solar Casing around the controller
            // #zh_CN 控制器周边的太阳能塔机械方块
            .addOutputBus(tr("TST_LaserMeteorMiner_structure_info_T1_hatches"), 1)
            .addEnergyHatch(tr("TST_LaserMeteorMiner_structure_info_T1_hatches"), 1)
            .addMaintenanceHatch(tr("TST_LaserMeteorMiner_structure_info_T1_hatches"), 1)
            // #tr TST_LaserMeteorMiner_structure_info_T1_hatches_input_bus
            // # Below the controller
            // #zh_CN 控制器下侧
            .addInputBus(tr("TST_LaserMeteorMiner_structure_info_T1_hatches_input_bus"), 2)
            // #tr TST_LaserMeteorMiner_tooltips_T2
            // # {\GOLD}{\BOLD}TIER II
            // #zh_CN {\GOLD}{\BOLD}等级 II
            .addStructureInfo(tr("TST_LaserMeteorMiner_tooltips_T2"))
            // #tr TST_LaserMeteorMiner_structure_info_T2_controller
            // # Highest layer of the ritual
            // #zh_CN 仪式最上层
            .addController(tr("TST_LaserMeteorMiner_structure_info_T2_controller"))
            // #tr TST_LaserMeteorMiner_structure_info_T2_hatches
            // # Any Neutronium Casing below the controller
            // #zh_CN 控制器下方的中子采矿机械方块
            .addOutputBus(tr("TST_LaserMeteorMiner_structure_info_T2_hatches"), 3)
            .addEnergyHatch(tr("TST_LaserMeteorMiner_structure_info_T2_hatches"), 3)
            .addMaintenanceHatch(tr("TST_LaserMeteorMiner_structure_info_T2_hatches"), 3)
            .toolTipFinisher(Mod_TwistSpaceTechnology.getText());
        return tt;
        // spotless:on
    }

    private boolean findLaserRenderer() {
        this.setStartCoords();

        if (getBaseMetaTileEntity().getWorld()
            .getTileEntity(
                xStart,
                getBaseMetaTileEntity().getYCoord() + (this.multiTier == 1 ? 10 : 15),
                zStart) instanceof TileEntityLaserBeacon laser) {
            renderer = laser;
            renderer.setRotationFields(getDirection(), getRotation(), getFlip());
            return true;
        }
        return false;
    }

    private boolean stopAllRendering = false;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack tool) {
        stopAllRendering = !stopAllRendering;
        // #tr TST_LaserMeteorMiner_message_screwdriverRightClick_off
        // # Rendering off
        // #zh_CN 渲染特效关闭
        // #tr TST_LaserMeteorMiner_message_screwdriverRightClick_on
        // # Rendering on
        // #zh_CN 渲染特效开启
        if (stopAllRendering) {
            TstUtils.sendMessageKeyToPlayer(aPlayer, "TST_LaserMeteorMiner_message_screwdriverRightClick_off");
            if (renderer != null) renderer.setShouldRender(false);
        } else {
            TstUtils.sendMessageKeyToPlayer(aPlayer, "TST_LaserMeteorMiner_message_screwdriverRightClick_on");
        }
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.multiTier = 0;
        if (checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet_T1, verticalOffSet_T1, depthOffSet_T1)) {
            multiTier = 1;
        } else if (checkPiece(STRUCTURE_PIECE_TIER2, horizontalOffSet_T2, verticalOffSet_T2, depthOffSet_T2)) {
            multiTier = 2;
        } else return false;

        if (!findLaserRenderer()) return false;

        return !mEnergyHatches.isEmpty();
    }

    private int getMultiTier(ItemStack inventory) {
        if (inventory == null || inventory.stackSize < 1) return 0;
        return GTCMItemList.MeteorMinerSchematic2.equal(inventory) ? 2
            : GTCMItemList.MeteorMinerSchematic1.equal(inventory) ? 1 : 0;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    protected int getXDrill() {
        return xDrill;
    }

    protected int getYDrill() {
        return yDrill;
    }

    protected int getZDrill() {
        return zDrill;
    }

    private int getLaserToEndHeight() {
        return (this.multiTier == 1 ? 3 : 0);
    }

    private void setFortuneTier() {
        this.fortuneTier = 0;
        if (this.multiTier == 2) {
            this.fortuneTier = 3;
            return;
        }
        if (!mInputBusses.isEmpty()) {
            Optional<ItemStack> input = Optional.ofNullable(
                mInputBusses.get(0)
                    .getInventoryHandler()
                    .getStackInSlot(0));
            if (input.isPresent()) {
                this.fortuneTier = getFortuneTier(input.get());
            }
        }
    }

    private static int getFortuneTier(ItemStack itemStack) {
        if (itemStack == null || itemStack.stackSize < 1) return 0;
        Item t = itemStack.getItem();
        if (MiscHelper.PickaxeOfTheCore.equals(t)) return 1;
        if (MiscHelper.BoundPickaxe.equals(t)) return 2;
        if (MiscHelper.TerraShatterer.equals(t)) return 3;
        return 0;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("currentRadius", currentRadius);
        aNBT.setInteger("xDrill", xDrill);
        aNBT.setInteger("yDrill", yDrill);
        aNBT.setInteger("zDrill", zDrill);
        aNBT.setInteger("xStart", xStart);
        aNBT.setInteger("yStart", yStart);
        aNBT.setInteger("zStart", zStart);
        aNBT.setBoolean("isStartInitialized", isStartInitialized);
        aNBT.setBoolean("hasFinished", hasFinished);
        aNBT.setBoolean("isWaiting", isWaiting);
        aNBT.setBoolean("stopAllRendering", stopAllRendering);
        aNBT.setInteger("multiTier", multiTier);
        aNBT.setInteger("fortuneTier", fortuneTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        currentRadius = aNBT.getInteger("currentRadius");
        xDrill = aNBT.getInteger("xDrill");
        yDrill = aNBT.getInteger("yDrill");
        zDrill = aNBT.getInteger("zDrill");
        xStart = aNBT.getInteger("xStart");
        yStart = aNBT.getInteger("yStart");
        zStart = aNBT.getInteger("zStart");
        isStartInitialized = aNBT.getBoolean("isStartInitialized");
        hasFinished = aNBT.getBoolean("hasFinished");
        isWaiting = aNBT.getBoolean("isWaiting");
        stopAllRendering = aNBT.getBoolean("stopAllRendering");
        multiTier = aNBT.getInteger("multiTier");
        fortuneTier = aNBT.getInteger("fortuneTier");
    }

    private void reset() {
        this.isResetting = false;
        this.hasFinished = true;
        this.isWaiting = false;
        currentRadius = MAX_RADIUS;
        this.initializeDrillPos();
    }

    private void startReset() {
        this.isResetting = true;
        stopMachine(ShutDownReasonRegistry.NONE);
        enableWorking();
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (this.multiTier != this.getMultiTier(mInventory[1])) {
            // #tr GT5U.gui.text.missing_schematic
            // # {\LIGHT_PURPLE}Missing Schematic.
            // #zh_CN {\LIGHT_PURPLE}缺少设计图.
            return SimpleCheckRecipeResult.ofFailure("missing_schematic");
        }
        if (renderer != null) {
            renderer.setColors(1, 0, 0);
        }
        if (isResetting) {
            this.reset();
            // #tr GT5U.gui.text.meteor_reset
            // # {\LIGHT_PURPLE}Reset completed!
            // #zh_CN {\LIGHT_PURPLE}重置完成!
            return SimpleCheckRecipeResult.ofSuccess("meteor_reset");
        }

        setElectricityStats();
        if (!isEnergyEnough()) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return SimpleCheckRecipeResult.ofFailure("not_enough_energy");
        }

        if (!isStartInitialized) {
            this.setStartCoords();
            this.findBestRadius();
            this.initializeDrillPos();
        }

        if (!hasFinished) {
            renderer.setShouldRender(true);
            renderer.setRange((double) (this.currentRadius + distanceFromMeteor + 0.5 + this.getLaserToEndHeight()));
            this.setFortuneTier();
            this.startMining(this.multiTier);
            mOutputItems = res.toArray(new ItemStack[0]);
            res.clear();
        } else {
            renderer.setShouldRender(false);
            this.isWaiting = true;
            this.setElectricityStats();
            boolean isReady = checkCenter();
            if (isReady) {
                this.isWaiting = false;
                this.setElectricityStats();
                this.setReady();
                this.hasFinished = false;
            } else {
                // #tr GT5U.gui.text.meteor_waiting
                // # {\LIGHT_PURPLE}Waiting for a Meteor...
                // #zh_CN {\LIGHT_PURPLE}等待陨星中...
                return SimpleCheckRecipeResult.ofSuccess("meteor_waiting");
            }
        }

        // #tr GT5U.gui.text.meteor_mining
        // # {\LIGHT_PURPLE}Currently Mining!
        // #zh_CN {\LIGHT_PURPLE}正在开采!
        return SimpleCheckRecipeResult.ofSuccess("meteor_mining");
    }

    private void startMining(int tier) {
        switch (tier) {
            case 1 -> this.mineSingleBlock();
            case 2 -> this.mineRow();
            default -> throw new IllegalArgumentException("Invalid Multiblock Tier");
        }
    }

    private void mineSingleBlock() {
        while (getBaseMetaTileEntity().getWorld()
            .isAirBlock(this.xDrill, this.yDrill, this.zDrill)) {
            this.moveToNextBlock();
            if (this.hasFinished) return;
        }
        this.mineBlock(this.xDrill, this.yDrill, this.zDrill);
        this.moveToNextBlock();
    }

    private void mineRow() {
        int currentX = this.xDrill;
        int currentY = this.yDrill;
        while (getBaseMetaTileEntity().getWorld() // Skips empty rows
            .isAirBlock(currentX, currentY, this.zStart)) {
            this.moveToNextColumn();
            if (this.hasFinished) return;
            currentX = this.xDrill;
            currentY = this.yDrill;
        }

        int opposite = 0;
        for (int z = -currentRadius; z <= (currentRadius - opposite); z++) {
            int currentZ = this.zStart + z;
            if (!getBaseMetaTileEntity().getWorld()
                .isAirBlock(this.xDrill, this.yDrill, currentZ)) {
                this.mineBlock(this.xDrill, this.yDrill, currentZ);
            } else opposite++;
        }
        this.moveToNextColumn();
    }

    private void mineBlock(int currentX, int currentY, int currentZ) {
        Block target = getBaseMetaTileEntity().getBlock(currentX, currentY, currentZ);
        if (target.getBlockHardness(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ) > 0) {
            final int targetMeta = getBaseMetaTileEntity().getMetaID(currentX, currentY, currentZ);
            Collection<ItemStack> drops = target
                .getDrops(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ, targetMeta, 0);
            if (GTUtility.isOre(target, targetMeta)) {
                res.addAll(getOutputByDrops(drops));
            } else res.addAll(drops);
            getBaseMetaTileEntity().getWorld()
                .setBlockToAir(currentX, currentY, currentZ);
        }
    }

    private Collection<ItemStack> getOutputByDrops(Collection<ItemStack> oreBlockDrops) {
        long voltage = getMaxInputVoltage();
        Collection<ItemStack> outputItems = new HashSet<>();
        oreBlockDrops.forEach(currentItem -> {
            if (!doUseMaceratorRecipe(currentItem)) {
                outputItems.add(multiplyStackSize(currentItem));
                return;
            }
            GTRecipe tRecipe = RecipeMaps.maceratorRecipes.findRecipeQuery()
                .items(currentItem)
                .voltage(voltage)
                .find();
            if (tRecipe == null) {
                outputItems.add(currentItem);
                return;
            }
            for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                ItemStack recipeOutput = tRecipe.mOutputs[i].copy();
                if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(i))
                    multiplyStackSize(recipeOutput);
                outputItems.add(recipeOutput);
            }
        });
        return outputItems;
    }

    private ItemStack multiplyStackSize(ItemStack itemStack) {
        itemStack.stackSize *= getBaseMetaTileEntity().getRandomNumber(this.fortuneTier + 1) + 1;
        return itemStack;
    }

    private boolean doUseMaceratorRecipe(ItemStack currentItem) {
        ItemData itemData = GTOreDictUnificator.getItemData(currentItem);
        return itemData == null || itemData.mPrefix != OrePrefixes.crushed && itemData.mPrefix != OrePrefixes.dustImpure
            && itemData.mPrefix != OrePrefixes.dust
            && itemData.mPrefix != OrePrefixes.gem
            && itemData.mPrefix != OrePrefixes.gemChipped
            && itemData.mPrefix != OrePrefixes.gemExquisite
            && itemData.mPrefix != OrePrefixes.gemFlawed
            && itemData.mPrefix != OrePrefixes.gemFlawless
            && itemData.mMaterial.mMaterial != Materials.Oilsands;
    }

    private void moveToNextBlock() {
        if (this.zDrill <= this.zStart + currentRadius) {
            this.zDrill++;
        } else {
            this.zDrill = this.zStart - currentRadius;
            this.moveToNextColumn();
        }
    }

    private void moveToNextColumn() {
        if (this.xDrill <= this.xStart + currentRadius) {
            this.xDrill++;
        } else if (this.yDrill <= this.yStart + currentRadius) {
            this.xDrill = this.xStart - currentRadius;
            this.yDrill++;
        } else {
            this.hasFinished = true;
        }
    }

    /**
     * Sets the coordinates of the center to the max range meteor center
     *
     */
    private void setStartCoords() {
        ForgeDirection facing = getBaseMetaTileEntity().getBackFacing();
        if (facing == ForgeDirection.NORTH || facing == ForgeDirection.SOUTH) {
            xStart = getBaseMetaTileEntity().getXCoord();
            zStart = (this.multiTier == 1 ? 2 : 6) * getExtendedFacing().getRelativeBackInWorld().offsetZ
                + getBaseMetaTileEntity().getZCoord();
        } else {
            xStart = (this.multiTier == 1 ? 2 : 6) * getExtendedFacing().getRelativeBackInWorld().offsetX
                + getBaseMetaTileEntity().getXCoord();
            zStart = getBaseMetaTileEntity().getZCoord();
        }
        yStart = distanceFromMeteor + (this.multiTier == 1 ? 13 : 15) + getBaseMetaTileEntity().getYCoord();
    }

    private void setReady() {
        this.findBestRadius();
        this.initializeDrillPos();
    }

    private void initializeDrillPos() {
        this.xDrill = this.xStart - currentRadius;
        this.yDrill = this.yStart - currentRadius;
        this.zDrill = this.zStart - currentRadius;

        this.isStartInitialized = true;
        this.hasFinished = false;
    }

    private boolean checkCenter() {
        return !getBaseMetaTileEntity().getWorld()
            .isAirBlock(xStart, yStart + 1, zStart);
    }

    private void findBestRadius() {
        currentRadius = MAX_RADIUS;
        int delta = 0;
        for (int zCoord = zStart - currentRadius; delta < MAX_RADIUS - 1; zCoord++) {
            if (!getBaseMetaTileEntity().getWorld()
                .isAirBlock(xStart, yStart, zCoord)) {
                break;
            }
            delta++;
        }
        currentRadius -= delta;
    }

    protected void setElectricityStats() {
        this.mOutputItems = new ItemStack[0];

        this.mEfficiency = 10000;
        this.mEfficiencyIncrease = 10000;

        OverclockCalculator calculator = new OverclockCalculator().setEUt(getAverageInputVoltage())
            .setAmperage(getMaxInputAmps())
            .setRecipeEUt(RECIPE_MV)
            .setDuration(StandardRecipeDuration_Second_LaserMeteorMiner * 20)
            .setAmperageOC(mEnergyHatches.size() != 1)
            .enablePerfectOC();
        calculator.calculate();
        this.mMaxProgresstime = (isWaiting) ? 20 * StandardRecipeDuration_Second_LaserMeteorMiner
            : calculator.getDuration();
        this.mEUt = (int) (isWaiting ? 0 : -calculator.getConsumption());
    }

    private boolean isEnergyEnough() {
        long requiredEnergy = 512 + getMaxInputVoltage() * 4;
        for (MTEHatchEnergy energyHatch : mEnergyHatches) {
            requiredEnergy -= energyHatch.getEUVar();
            if (requiredEnergy <= 0) return true;
        }
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);

        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> this.startReset())
                .setPlayClickSound(true)
                .setBackground(
                    () -> {
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_CYCLIC };
                    })
                .setPos(new Pos2d(174, 112))
                .addTooltip(tr("TST_LaserMeteorMiner.button.reset"))
                .setSize(16, 16));
        // #tr TST_LaserMeteorMiner.button.reset
        // # Reset machine
        // #zh_CN 重启机器
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("fortune", this.fortuneTier);
        tag.setInteger("tier", this.multiTier);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        // spotless:off
        // #tr Tooltip_METEOR_MINER_CONTROLLER.tier.0
        // # Incomplete Structure
        // #zh_CN 结构未成型
        // #tr Tooltip_METEOR_MINER_CONTROLLER.tier.1
        // # Current Tier: {\WHITE}1
        // #zh_CN 当前等级: {\WHITE}1
        // #tr Tooltip_METEOR_MINER_CONTROLLER.tier.2
        // # Current Tier: {\WHITE}2
        // #zh_CN 当前等级: {\WHITE}2
        // #tr Tooltip_METEOR_MINER_CONTROLLER.fortune.0
        // # Augment: {\WHITE}No Augment Applied
        // #zh_CN 增强效果: 无增强
        // #tr Tooltip_METEOR_MINER_CONTROLLER.fortune.1
        // # Augment: {\WHITE}Fortune I
        // #zh_CN 增强效果: 时运I
        // #tr Tooltip_METEOR_MINER_CONTROLLER.fortune.2
        // # Augment: {\WHITE}Fortune II
        // #zh_CN 增强效果: 时运II
        // #tr Tooltip_METEOR_MINER_CONTROLLER.fortune.3
        // # Augment: {\WHITE}Fortune III
        // #zh_CN 增强效果: 时运III
        // spotless:on
        currentTip.add(tr("Tooltip_METEOR_MINER_CONTROLLER.tier." + tag.getInteger("tier")) + EnumChatFormatting.RESET);
        currentTip
            .add(tr("Tooltip_METEOR_MINER_CONTROLLER.fortune." + tag.getInteger("fortune")) + EnumChatFormatting.RESET);
    }
}
