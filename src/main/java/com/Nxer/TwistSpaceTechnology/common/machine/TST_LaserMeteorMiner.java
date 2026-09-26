package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.LaserBeaconRender;
import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.mixed_energy_hatches;
import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.special_block_structure_issue;
import static com.Nxer.TwistSpaceTechnology.config.Config.StandardRecipeDuration_Second_LaserMeteorMiner;
import static com.Nxer.TwistSpaceTechnology.util.TSTUtils.tr;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.entity.TileEntityLaserBeacon;
import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_LaserMeteorMiner;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GlassTier;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gtPlusPlus.core.block.ModBlocks;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@SkipGenerateDescription
public class TST_LaserMeteorMiner extends MTEExtendedPowerMultiBlockBase<TST_LaserMeteorMiner>
    implements ISurvivalConstructable, TSTTooltipCredit {

    // region Class Constructor
    public TST_LaserMeteorMiner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.TOTTO);
    }

    public TST_LaserMeteorMiner(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_LaserMeteorMiner(this.mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int horizontalOffSet_T1 = 9;
    protected static final int verticalOffSet_T1 = 13;
    protected static final int depthOffSet_T1 = 7;
    private static final String STRUCTURE_PIECE_TIER2 = "tier2";
    protected static final int horizontalOffSet_T2 = 9;
    protected static final int verticalOffSet_T2 = 15;
    protected static final int depthOffSet_T2 = 3;
    private static IStructureDefinition<TST_LaserMeteorMiner> STRUCTURE_DEFINITION;

    // spotless:off
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
    public IStructureDefinition<TST_LaserMeteorMiner> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_LaserMeteorMiner>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape_T1))
                .addShape(STRUCTURE_PIECE_TIER2, transpose(shape_T2))
                .addElement('A', ofAnyGlass())
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
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(OutputBus, Energy.or(ExoticEnergy), Maintenance)
                        .casingIndex(TAE.getIndexFromPage(3, 9))
                        .hint(1)
                        .buildAndChain(ofBlock(ModBlocks.blockSpecialMultiCasings, 6)))
                .addElement(
                    'Y',
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(InputBus)
                        .adder(TST_LaserMeteorMiner::addInjector)
                        .casingIndex(TAE.getIndexFromPage(3, 9))
                        .hint(2)
                        .buildAndChain(ofBlock(ModBlocks.blockSpecialMultiCasings, 6)))
                .addElement(
                    'X',
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(OutputBus, Energy.or(ExoticEnergy), Maintenance)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                        .hint(3)
                        .buildAndChain(ofBlock(GregTechAPI.sBlockCasings8, 2)))
                .addElement('Z', ofBlock(LaserBeaconRender, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    /**
     * Any glass, like {@code chainAllGlasses()} but without its glass tier channel: in NEI that channel raises the tier
     * slider to the number of glass tiers and adds a "Glass" slider, while this multi accepts every glass anyway.
     */
    private static IStructureElement<TST_LaserMeteorMiner> ofAnyGlass() {
        return lazy(() -> {
            List<IStructureElement<TST_LaserMeteorMiner>> glasses = new ArrayList<>();
            for (Pair<Block, Integer> glass : GlassTier.getGlassList()) {
                glasses.add(ofBlock(glass.getLeft(), glass.getRight()));
            }
            return ofChain(glasses);
        });
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
        // Only for the hologram projector's hints: not part of the structure, of auto-build or of the NEI preview
        if (hintsOnly) hintMeteorCenter(stackSize.stackSize > 1 ? 2 : 1);
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        this.multiTier = 0;
        if (checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet_T1, verticalOffSet_T1, depthOffSet_T1, errors)) {
            multiTier = 1;
        } else {
            clearHatches();
            errors.clear();
            if (checkPiece(STRUCTURE_PIECE_TIER2, horizontalOffSet_T2, verticalOffSet_T2, depthOffSet_T2, errors)) {
                multiTier = 2;
            } else return;
        }

        if (!findLaserRenderer()) {
            errors.add(special_block_structure_issue);
            return;
        }

        checkEnergyHatches(errors);

    }

    /**
     * At least one energy hatch: any number of normal ones, or TecTech multi Amp / laser ones, but not mixed.
     */
    private void checkEnergyHatches(List<StructureError> errors) {
        if (mEnergyHatches.isEmpty() && mExoticEnergyHatches.isEmpty()) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, Energy, 0, 1));
        } else if (!mEnergyHatches.isEmpty() && !mExoticEnergyHatches.isEmpty()) {
            errors.add(mixed_energy_hatches);
        }
    }
    // endregion

    // region Processing Logic
    private static final int distanceFromMeteor = 48;
    private static final int MAX_RADIUS = 40;
    /** Max air blocks the tier 1 drill skips in a single cycle, to avoid lag spikes. */
    private static final int MAX_AIR_CHECKS_PER_CYCLE = 4096;
    protected TileEntityLaserBeacon renderer;
    private int currentRadius = MAX_RADIUS;
    private int xDrill, yDrill, zDrill;
    private int xStart, yStart, zStart;
    private int fortuneTier = 0;
    private boolean isStartInitialized = false;
    private boolean hasFinished = true;
    private boolean isWaiting = false;
    private boolean isResetting = false;
    private final List<ItemStack> res = new ArrayList<>();
    private int multiTier = 0;
    private boolean stopAllRendering = false;

    /** Window of the stats shown in the GUI: 5 seconds. */
    private static final int RECENT_WINDOW_TICKS = 5 * 20;

    /** A mining cycle, kept for the GUI stats of the last {@link #RECENT_WINDOW_TICKS} ticks (not saved). */
    @Desugar
    private record MiningCycle(long tick, List<ItemStack> outputs, int blocksMined) {}

    private final ArrayDeque<MiningCycle> recentCycles = new ArrayDeque<>();
    private int blocksMinedThisCycle = 0;
    /** Cache of {@link #sphereWork} for the whole meteor, which only depends on radius and tier. */
    private long totalWork = 0;
    private int totalWorkRadius = -1, totalWorkTier = -1;

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (this.multiTier != this.getMultiTier(mInventory[1])) {
            // spotless:off
            // #tr GT5U.gui.text.recipe_result.missing_schematic
            // # {\LIGHT_PURPLE}Missing Schematic.
            // #zh_CN {\LIGHT_PURPLE}缺少设计图.
            return SimpleCheckRecipeResult.ofFailure("missing_schematic");
        }
        if (isResetting) {
            this.reset();
            // #tr GT5U.gui.text.recipe_result.meteor_reset
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
            if (!isMeteorAreaLoaded(MAX_RADIUS)) return meteorAreaNotLoaded();
            this.findBestRadius();
            this.initializeDrillPos();
        }

        if (!hasFinished) {
            if (!isMeteorAreaLoaded(currentRadius)) return meteorAreaNotLoaded();
            updateLaser(true, this.currentRadius + distanceFromMeteor + 0.5 + this.getLaserToEndHeight());
            this.setFortuneTier();
            this.startMining(this.multiTier);
            final List<ItemStack> outputs = mergeStacks(res);
            mOutputItems = toOutputArray(outputs);
            res.clear();
            recordCycle(outputs);
        } else {
            updateLaser(false, 0);
            this.isWaiting = true;
            this.setElectricityStats();
            // Finding the new radius reads blocks up to the max radius
            if (!isMeteorAreaLoaded(MAX_RADIUS)) return meteorAreaNotLoaded();
            boolean isReady = checkCenter();
            if (isReady) {
                this.isWaiting = false;
                this.setElectricityStats();
                this.setReady();
                this.hasFinished = false;
            } else {
                // #tr GT5U.gui.text.recipe_result.meteor_waiting
                // # {\LIGHT_PURPLE}Waiting for a Meteor...
                // #zh_CN {\LIGHT_PURPLE}等待陨星中...
                return SimpleCheckRecipeResult.ofSuccess("meteor_waiting");
            }
        }

        // #tr GT5U.gui.text.recipe_result.meteor_mining
        // # {\LIGHT_PURPLE}Currently Mining!
        // #zh_CN {\LIGHT_PURPLE}正在开采!
        return SimpleCheckRecipeResult.ofSuccess("meteor_mining");
            // spotless:on
    }

    @Override
    public void onDisableWorking() {
        updateLaser(false, 0);
        super.onDisableWorking();
    }

    @Override
    public void onBlockDestroyed() {
        updateLaser(false, 0);
        super.onBlockDestroyed();
    }

    /**
     * Updates the laser only when something changed: every setter of the laser tile entity sends a block update to the
     * clients.
     *
     * @param mining whether the miner is mining; the laser is shown only if the screwdriver didn't turn rendering off
     * @param range  length of the laser, used only while it is shown
     */
    private void updateLaser(boolean mining, double range) {
        if (renderer == null) return;
        boolean shouldRender = mining && !stopAllRendering;
        if (renderer.getShouldRender() != shouldRender) renderer.setShouldRender(shouldRender);
        if (shouldRender && renderer.getRange() != range) renderer.setRange(range);
    }

    /**
     * Whether all the chunks of the cube mined around the meteor center are loaded, so that mining never loads chunks
     * by itself.
     */
    private boolean isMeteorAreaLoaded(int radius) {
        return getBaseMetaTileEntity().getWorld()
            .checkChunksExist(
                xStart - radius,
                yStart - radius,
                zStart - radius,
                xStart + radius + 1,
                yStart + radius + 1,
                zStart + radius + 1);
    }

    private static CheckRecipeResult meteorAreaNotLoaded() {
        // #tr GT5U.gui.text.recipe_result.meteor_area_not_loaded
        // # {\LIGHT_PURPLE}Meteor area not loaded, waiting...
        // #zh_CN {\LIGHT_PURPLE}陨星区域未加载, 等待中...
        return SimpleCheckRecipeResult.ofFailure("meteor_area_not_loaded");
    }

    private boolean findLaserRenderer() {
        this.setStartCoords();

        if (getBaseMetaTileEntity().getWorld()
            .getTileEntity(
                xStart,
                getBaseMetaTileEntity().getYCoord() + (this.multiTier == 1 ? 10 : 15),
                zStart) instanceof TileEntityLaserBeacon laser) {
            renderer = laser;
            renderer.setRotationFields(ExtendedFacing.of(getDirection(), getRotation(), getFlip()));
            renderer.setColors(1, 0, 0);
            return true;
        }
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack tool) {
        stopAllRendering = !stopAllRendering;
        // #tr tst.common.machine.MeteorMiner.message.render.off
        // # Rendering off
        // #zh_CN 渲染特效关闭

        // #tr tst.common.machine.MeteorMiner.message.render.on
        // # Rendering on
        // #zh_CN 渲染特效开启
        if (stopAllRendering) {
            TSTUtils.sendMessageKeyToPlayer(aPlayer, "tst.common.machine.MeteorMiner.message.render.off");
            updateLaser(false, 0);
        } else {
            // the laser comes back on the next mining cycle
            TSTUtils.sendMessageKeyToPlayer(aPlayer, "tst.common.machine.MeteorMiner.message.render.on");
        }
    }

    private int getMultiTier(ItemStack inventory) {
        if (inventory == null || inventory.stackSize < 1) return 0;
        return GTCMItemList.MeteorMinerSchematic2.equal(inventory) ? 2
            : GTCMItemList.MeteorMinerSchematic1.equal(inventory) ? 1 : 0;
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

    private void reset() {
        this.isResetting = false;
        this.hasFinished = true;
        this.isWaiting = false;
        currentRadius = MAX_RADIUS;
        this.initializeDrillPos();
    }

    public void startReset() {
        this.isResetting = true;
        stopMachine(ShutDownReasonRegistry.NONE);
        enableWorking();
    }

    private void startMining(int tier) {
        switch (tier) {
            case 1 -> this.mineSingleBlock();
            case 2 -> this.mineRow();
            default -> throw new IllegalArgumentException("Invalid Multiblock Tier");
        }
    }

    private void mineSingleBlock() {
        final World world = getBaseMetaTileEntity().getWorld();
        int airChecks = 0;
        while (true) {
            if (this.zDrill == this.zStart - currentRadius && world.isAirBlock(this.xDrill, this.yDrill, this.zStart)) {
                // Meteors are symmetric: if the center of the row is air, the whole row is air
                this.moveToNextColumn();
            } else if (world.isAirBlock(this.xDrill, this.yDrill, this.zDrill)) {
                this.moveToNextBlock();
            } else break;
            if (this.hasFinished || ++airChecks >= MAX_AIR_CHECKS_PER_CYCLE) return;
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
        // Negative hardness means unbreakable (e.g. bedrock); zero is valid (e.g. Et Futurum's honey block)
        if (target.getBlockHardness(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ) >= 0) {
            final int targetMeta = getBaseMetaTileEntity().getMetaID(currentX, currentY, currentZ);
            Collection<ItemStack> drops = target
                .getDrops(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ, targetMeta, 0);
            if (GTUtility.isOre(target, targetMeta)) {
                res.addAll(getOutputByDrops(drops));
            } else res.addAll(drops);
            getBaseMetaTileEntity().getWorld()
                .setBlockToAir(currentX, currentY, currentZ);
            blocksMinedThisCycle++;
        }
    }

    private List<ItemStack> getOutputByDrops(Collection<ItemStack> oreBlockDrops) {
        long voltage = getMaxInputVoltage();
        List<ItemStack> outputItems = new ArrayList<>();
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
                if (tRecipe.mOutputs[i] == null) continue;
                // Chanced outputs (e.g. byproducts) only come out when the roll succeeds
                if (getBaseMetaTileEntity().getRandomNumber(10000) >= tRecipe.getOutputChance(i)) continue;
                outputItems.add(multiplyStackSize(tRecipe.mOutputs[i].copy()));
            }
        });
        return outputItems;
    }

    /**
     * Merges equal stacks (same item, meta and NBT). The resulting stacks can be bigger than their max stack size.
     */
    private static List<ItemStack> mergeStacks(List<ItemStack> stacks) {
        List<ItemStack> merged = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack == null || stack.stackSize <= 0) continue;
            ItemStack same = null;
            for (ItemStack candidate : merged) {
                if (GTUtility.areStacksEqual(candidate, stack)) {
                    same = candidate;
                    break;
                }
            }
            if (same == null) merged.add(stack.copy());
            else same.stackSize += stack.stackSize;
        }
        return merged;
    }

    /**
     * Splits merged stacks into stacks of at most their max stack size.
     */
    private static ItemStack[] toOutputArray(List<ItemStack> mergedStacks) {
        List<ItemStack> outputs = new ArrayList<>();
        for (ItemStack stack : mergedStacks) {
            final int maxSize = Math.max(1, stack.getMaxStackSize());
            for (int left = stack.stackSize; left > 0; left -= maxSize) {
                outputs.add(GTUtility.copyAmountUnsafe(Math.min(maxSize, left), stack));
            }
        }
        return outputs.toArray(new ItemStack[0]);
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
        final ChunkCoordinates center = getMeteorCenter(this.multiTier);
        xStart = center.posX;
        yStart = center.posY;
        zStart = center.posZ;
    }

    /**
     * Center of the meteor for the given tier: right above the laser beacon, {@code distanceFromMeteor} blocks above
     * the highest block of the multi. This is where players put the Warded Glass that stops the meteor.
     */
    private ChunkCoordinates getMeteorCenter(int tier) {
        final IGregTechTileEntity base = getBaseMetaTileEntity();
        final int backDistance = tier == 1 ? 2 : 6;
        int x = base.getXCoord();
        int z = base.getZCoord();
        ForgeDirection facing = base.getBackFacing();
        if (facing == ForgeDirection.NORTH || facing == ForgeDirection.SOUTH) {
            z += backDistance * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        } else {
            x += backDistance * getExtendedFacing().getRelativeBackInWorld().offsetX;
        }
        return new ChunkCoordinates(x, distanceFromMeteor + (tier == 1 ? 13 : 15) + base.getYCoord(), z);
    }

    /**
     * Shows where the Warded Glass goes, together with the hologram projector's structure hints.
     */
    private void hintMeteorCenter(int tier) {
        final ChunkCoordinates center = getMeteorCenter(tier);
        final World world = getBaseMetaTileEntity().getWorld();
        final Block wardedGlass = GameRegistry.findBlock(Mods.Thaumcraft.ID, "blockCosmeticOpaque");
        if (wardedGlass != null) {
            StructureLibAPI.hintParticle(world, center.posX, center.posY, center.posZ, wardedGlass, 2);
        } else {
            StructureLibAPI
                .hintParticle(world, center.posX, center.posY, center.posZ, StructureLibAPI.getBlockHint(), 0);
        }
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
        // a new meteor (or a reset): the stats of the previous one don't apply
        recentCycles.clear();
        blocksMinedThisCycle = 0;
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
            .setAmperageOC(!mExoticEnergyHatches.isEmpty() || mEnergyHatches.size() != 1)
            .enablePerfectOC();
        calculator.calculate();
        this.mMaxProgresstime = (isWaiting) ? 20 * StandardRecipeDuration_Second_LaserMeteorMiner
            : calculator.getDuration();
        this.lEUt = isWaiting ? 0 : -calculator.getConsumption();
    }

    private boolean isEnergyEnough() {
        long requiredEnergy = 512 + getMaxInputVoltage() * 4;
        for (MTEHatch energyHatch : getExoticAndNormalEnergyHatchList()) {
            requiredEnergy -= energyHatch.getEUVar();
            if (requiredEnergy <= 0) return true;
        }
        return false;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new TST_Gui_LaserMeteorMiner(this);
    }

    /**
     * The GUI shows the outputs of the last seconds instead of the outputs and progress of the single (short) cycle.
     */
    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    // region GUI stats (server side, read by TST_Gui_LaserMeteorMiner's sync values)

    private void recordCycle(List<ItemStack> outputs) {
        final long now = getWorldTime();
        recentCycles.addLast(new MiningCycle(now, outputs, blocksMinedThisCycle));
        blocksMinedThisCycle = 0;
        pruneRecentCycles(now);
    }

    /**
     * Drops the cycles older than the window, but keeps the last two to compute rates when cycles are slow.
     */
    private void pruneRecentCycles(long now) {
        while (recentCycles.size() > 2 && now - recentCycles.peekFirst()
            .tick() > RECENT_WINDOW_TICKS) {
            recentCycles.pollFirst();
        }
    }

    private long getWorldTime() {
        return getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime();
    }

    public boolean isMining() {
        return getBaseMetaTileEntity().isActive() && isStartInitialized && !hasFinished && !isWaiting;
    }

    public int getCurrentRadius() {
        return currentRadius;
    }

    public int getFortuneTier() {
        return fortuneTier;
    }

    /**
     * Mining cycles needed for the meteor, a sphere of the given radius around its center, from the given drill
     * position (relative to the center) to the end. The drill scans y, then x, then (tier 1 only) z, each from
     * {@code -radius} to {@code radius + 1}. Tier 1 mines one block per cycle and tier 2 one row per cycle, while empty
     * space is skipped within a cycle, so it costs nothing.
     */
    static long sphereWork(int radius, int tier, int dxFrom, int dyFrom, int dzFrom) {
        final long radiusSquared = (long) radius * radius;
        long work = 0;
        for (int dy = dyFrom; dy <= radius + 1; dy++) {
            for (int dx = dy == dyFrom ? dxFrom : -radius; dx <= radius + 1; dx++) {
                final long left = radiusSquared - (long) dx * dx - (long) dy * dy;
                if (left < 0) continue; // the row misses the sphere
                if (tier != 1) {
                    work++;
                    continue;
                }
                final int halfLength = (int) Math.sqrt(left); // blocks of the row at |dz| <= halfLength
                final int firstDz = dy == dyFrom && dx == dxFrom ? Math.max(dzFrom, -halfLength) : -halfLength;
                if (firstDz <= halfLength) work += halfLength - firstDz + 1;
            }
        }
        return work;
    }

    private long getRemainingWork() {
        return sphereWork(currentRadius, multiTier, xDrill - xStart, yDrill - yStart, zDrill - zStart);
    }

    private long getTotalWork() {
        if (totalWorkRadius != currentRadius || totalWorkTier != multiTier) {
            totalWork = sphereWork(currentRadius, multiTier, -currentRadius, -currentRadius, -currentRadius);
            totalWorkRadius = currentRadius;
            totalWorkTier = multiTier;
        }
        return totalWork;
    }

    /**
     * Fraction (0-1) of the meteor already mined, counted in mining cycles: it grows at a steady pace.
     */
    public double getProgress() {
        final long total = getTotalWork();
        if (total <= 0) return 0;
        return Math.max(0, Math.min(1, 1 - (double) getRemainingWork() / total));
    }

    /**
     * Blocks mined per second over the recent cycles.
     */
    public double getBlocksPerSecond() {
        pruneRecentCycles(getWorldTime());
        if (recentCycles.size() < 2) return 0;
        final long ticks = recentCycles.peekLast()
            .tick()
            - recentCycles.peekFirst()
                .tick();
        if (ticks <= 0) return 0;
        int blocks = -recentCycles.peekFirst()
            .blocksMined(); // the first cycle happened before the measured interval
        for (MiningCycle cycle : recentCycles) blocks += cycle.blocksMined();
        return blocks * 20.0 / ticks;
    }

    /**
     * Seconds until the meteor is mined: the remaining mining cycles times the current cycle duration; -1 if unknown.
     */
    public int getEtaSeconds() {
        if (mMaxProgresstime <= 0) return -1;
        final long ticks = getRemainingWork() * mMaxProgresstime;
        return (int) Math.min(Integer.MAX_VALUE, (ticks + 19) / 20);
    }

    /**
     * Everything produced in the last {@link #RECENT_WINDOW_TICKS} ticks, merged, biggest amounts first.
     */
    public List<ItemStackLong> getRecentOutputs() {
        final long now = getWorldTime();
        pruneRecentCycles(now);
        final List<ItemStack> produced = new ArrayList<>();
        for (MiningCycle cycle : recentCycles) {
            if (now - cycle.tick() <= RECENT_WINDOW_TICKS) produced.addAll(cycle.outputs());
        }
        final List<ItemStackLong> result = new ArrayList<>();
        for (ItemStack stack : mergeStacks(produced)) result.add(new ItemStackLong(stack, stack.stackSize));
        result.sort(
            Comparator.comparingLong(ItemStackLong::stackSize)
                .reversed());
        return result;
    }

    // endregion

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
        // #tr tst.common.machine.MeteorMiner.waila.tier.0
        // # Incomplete Structure
        // #zh_CN 结构未成型

        // #tr tst.common.machine.MeteorMiner.waila.tier.1
        // # Current Tier: {\WHITE}1
        // #zh_CN 当前等级: {\WHITE}1

        // #tr tst.common.machine.MeteorMiner.waila.tier.2
        // # Current Tier: {\WHITE}2
        // #zh_CN 当前等级: {\WHITE}2

        // #tr tst.common.machine.MeteorMiner.waila.fortune.0
        // # Augment: {\WHITE}No Augment Applied
        // #zh_CN 增强效果: 无增强

        // #tr tst.common.machine.MeteorMiner.waila.fortune.1
        // # Augment: {\WHITE}Fortune I
        // #zh_CN 增强效果: 时运I

        // #tr tst.common.machine.MeteorMiner.waila.fortune.2
        // # Augment: {\WHITE}Fortune II
        // #zh_CN 增强效果: 时运II

        // #tr tst.common.machine.MeteorMiner.waila.fortune.3
        // # Augment: {\WHITE}Fortune III
        // #zh_CN 增强效果: 时运III
        currentTip.add(tr("tst.common.machine.MeteorMiner.waila.tier." + tag.getInteger("tier")) + EnumChatFormatting.RESET);
        // spotless:on
        currentTip.add(
            tr("tst.common.machine.MeteorMiner.waila.fortune." + tag.getInteger("fortune")) + EnumChatFormatting.RESET);
    }

    // endregion

    // region NBT

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

    // endregion

    // region Textures
    public static IIconContainer OVERLAY_FRONT_METEOR_MINER = Textures.BlockIcons
        .custom("gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER");

    public static IIconContainer OVERLAY_FRONT_METEOR_MINER_ACTIVE = Textures.BlockIcons
        .custom("gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER_ACTIVE");

    public static IIconContainer OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW = Textures.BlockIcons
        .custom("gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW");

    public static IIconContainer OVERLAY_FRONT_METEOR_MINER_GLOW = Textures.BlockIcons
        .custom("gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER_GLOW");

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return TSTControllerTextures.getTexture(
            side,
            aFacing,
            aActive,
            Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)),
            OVERLAY_FRONT_METEOR_MINER,
            OVERLAY_FRONT_METEOR_MINER_GLOW,
            OVERLAY_FRONT_METEOR_MINER_ACTIVE,
            OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.MeteorMiner.tooltip.machine_type
        // # Meteor Miner
        // #zh_CN 陨星采矿机
        tt.addMachineType(tr("tst.common.machine.MeteorMiner.tooltip.machine_type"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.08
            // # Controller Block for the Laser Meteor Miner!
            // #zh_CN 激光陨星采矿场的控制器方块！
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.08"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.09
            // # To work properly the center of the meteor has to be 48 blocks above the highest block of the multi.
            // #zh_CN 陨星的中心必须位于机器最高点上方48个方块的位置, 机器方可正常工作.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.09"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.10
            // # The laser will mine in a radius of up to 40 blocks in each direction from the center of the meteor.
            // #zh_CN 激光将在陨星中心半径40方块内进行采矿作业.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.10"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.11
            // # All the chunks involved must be chunkloaded.
            // #zh_CN 所涉及区块都必须保证加载.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.11"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.12
            // # The laser will automatically set its radius based on the meteorite,
            // #zh_CN 激光将根据陨石自动设置其运行半径.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.12"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.13
            // # if it doesn't find any it will wait for a meteor to spawn,
            // #zh_CN 没有找到陨星时机器会等待陨星生成.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.13"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.14
            // # considering the block right above the center of the meteor (like Warded Glass).
            // #zh_CN 顾及了中心正上方的方块(比如守卫者玻璃).
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.14"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.hologram
            // # Use the Hologram Projector on the controller to see where the Warded Glass goes (center of the meteor).
            // #zh_CN 对控制器使用全息投影仪, 可显示守卫者玻璃的放置位置(陨星中心).
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.hologram"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.15
            // # The reset button will restart the machine without optimizing the radius.
            // #zh_CN 点击重启按钮将重启机器, 并且不进行半径适配优化.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.15"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.16
            // # Machine need Meteor Miner Schematic put in controller slot to run.
            // #zh_CN 机器需要在控制器方块内放置陨星采矿场设计图才可运行.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.16"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.energy_hatches
            // # Accepts normal energy hatches or TecTech multi Amp / laser energy hatches, but not mixed.
            // #zh_CN 可使用普通能源仓, 或TecTech高电流/激光能源仓, 但不可混用.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.energy_hatches"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.17
            // # {\RED}{\BOLD} TIER I
            // #zh_CN {\RED}{\BOLD} 等级 I
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.17"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.18
            // # Mines one block every cycle.
            // #zh_CN 每次运行挖掘一个方块.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.18"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.19
            // # Default Fortune is 0, it can be increased by putting in the input bus special pickaxes:
            // #zh_CN 默认没有时运效果. 输入总线内放置以下镐子可以获得时运效果:
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.19"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.20
            // # Fortune I: Pickaxe of the Core
            // #zh_CN 时运I : 炽心镐
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.20"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.21
            // # Fortune II: Bound Pickaxe
            // #zh_CN 时运II : 约束之镐
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.21"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.22
            // # Fortune III: Terra Shatterer
            // #zh_CN 时运III : 泰拉粉碎者
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.22"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.23
            // # {\RED}{\BOLD} TIER II
            // #zh_CN {\RED}{\BOLD} 等级 II
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.23"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.24
            // # Always has Fortune III
            // #zh_CN 总是时运III.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.24"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.25
            // # Mines one row every cycle.
            // #zh_CN 每次运行挖掘一行.
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.25"))
            // #tr tst.common.machine.MeteorMiner.tooltip.info.26
            // # {\BLUE}{\BOLD}Finally some good Meteors!
            // #zh_CN {\BLUE}{\BOLD}终是好陨星! (Finally some good Meteors!)
            .addInfo(tr("tst.common.machine.MeteorMiner.tooltip.info.26"))
            // #tr tst.common.machine.MeteorMiner.tooltip.structure.01
            // # {\GOLD}{\BOLD}TIER I
            // #zh_CN {\GOLD}{\BOLD}等级 I
            .addStructureInfo(tr("tst.common.machine.MeteorMiner.tooltip.structure.01"))
            // #tr tst.common.machine.MeteorMiner.tooltip.structure.02
            // # Center of the second layer above the ritual
            // #zh_CN 仪式上方第二层的中心
            .addController(tr("tst.common.machine.MeteorMiner.tooltip.structure.02"))
            // #tr tst.common.machine.MeteorMiner.tooltip.structure.03
            // # Any Structural Solar Casing around the controller
            // #zh_CN 控制器周边的太阳能塔机械方块
            .addOutputBus(tr("tst.common.machine.MeteorMiner.tooltip.structure.03"), 1)
            .addEnergyHatch(tr("tst.common.machine.MeteorMiner.tooltip.structure.03"), 1)
            .addMaintenanceHatch(tr("tst.common.machine.MeteorMiner.tooltip.structure.03"), 1)
            // #tr tst.common.machine.MeteorMiner.tooltip.structure.04
            // # ULV only, below the controller
            // #zh_CN 仅限ULV, 控制器下侧
            .addInputBus(tr("tst.common.machine.MeteorMiner.tooltip.structure.04"), 2)
            // #tr tst.common.machine.MeteorMiner.tooltip.structure.05
            // # {\GOLD}{\BOLD}TIER II
            // #zh_CN {\GOLD}{\BOLD}等级 II
            .addStructureInfo(tr("tst.common.machine.MeteorMiner.tooltip.structure.05"))
            // #tr tst.common.machine.MeteorMiner.tooltip.structure.06
            // # Highest layer of the ritual
            // #zh_CN 仪式最上层
            .addController(tr("tst.common.machine.MeteorMiner.tooltip.structure.06"))
            // #tr tst.common.machine.MeteorMiner.tooltip.structure.07
            // # Any Neutronium Casing below the controller
            // #zh_CN 控制器下方的中子采矿机械方块
            .addOutputBus(tr("tst.common.machine.MeteorMiner.tooltip.structure.07"), 3)
            .addEnergyHatch(tr("tst.common.machine.MeteorMiner.tooltip.structure.07"), 3)
            .addMaintenanceHatch(tr("tst.common.machine.MeteorMiner.tooltip.structure.07"), 3)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Hatch Registration

    private boolean addInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        // Only an ULV input bus (a single slot) for the fortune pickaxe
        if (!(aMetaTileEntity instanceof MTEHatchInputBus bus) || bus.mTier != 0) return false;
        bus.updateTexture(aBaseCasingIndex);
        addIfSmartInput(bus);
        return mInputBusses.add(bus);
    }

    // endregion

}
