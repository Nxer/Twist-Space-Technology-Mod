package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_1;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_4;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockTranslucent;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockTravelAnchor;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.CarvedEminenceStone;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.EldritchArk;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.FieryBlock;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.StructureDefinitionBuilder;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateBlockAdder;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateTile;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateTileAdder;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateTileExt;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofBlockStrict;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofBlockStrictExt;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofVariableBlock;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static emt.init.EMTBlocks.electricCloud;
import static goodgenerator.loader.Loaders.essentiaCell;
import static goodgenerator.loader.Loaders.magicCasing;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static thaumcraft.common.config.ConfigBlocks.blockAiry;
import static thaumcraft.common.config.ConfigBlocks.blockCosmeticOpaque;
import static thaumcraft.common.config.ConfigBlocks.blockCosmeticSolid;
import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;
import static thaumcraft.common.config.ConfigBlocks.blockSlabStone;
import static thaumcraft.common.config.ConfigBlocks.blockStoneDevice;
import static thaumcraft.common.lib.research.ResearchManager.getResearchForPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.tile.TileArcaneHole;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipeTools;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import emt.tile.TileElectricCloud;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileCrucible;
import thaumcraft.common.tiles.TileNitor;
import thaumcraft.common.tiles.TileNodeConverter;
import thaumcraft.common.tiles.TileNodeEnergized;
import thaumcraft.common.tiles.TileNodeStabilizer;
import thaumcraft.common.tiles.TileOwned;
import thaumicenergistics.common.blocks.BlockEnum;
import thaumicenergistics.common.storage.EnumEssentiaStorageTypes;
import thaumicenergistics.common.tiles.TileInfusionProvider;

public class TST_IndustrialAlchemyTower extends GTCM_MultiMachineBase<TST_IndustrialAlchemyTower> {

    // region default value
    private double mSpeedBonus;
    private int essentiaCellTier = 0;
    private final ItemStack EssentiaCell_Creative = EnumEssentiaStorageTypes.Type_Creative.getCell();
    private final ItemStack ProofOfHeroes = GTCMItemList.ProofOfHeroes.get(1, 0);
    protected ArrayList<TileInfusionProvider> mTileInfusionProvider = new ArrayList<>();
    protected ArrayList<TileNodeEnergized> mNodeEnergized = new ArrayList<>();
    protected ArrayList<String> Research = new ArrayList<>();
    public static final CheckRecipeResult Essentia_InsentiaL = SimpleCheckRecipeResult
        .ofFailurePersistOnShutdown("Essentiainsentia");
    public static final CheckRecipeResult Research_not_completed = SimpleCheckRecipeResult
        .ofFailurePersistOnShutdown("Research_not_completed");

    // endregion

    // region constructor

    public TST_IndustrialAlchemyTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_IndustrialAlchemyTower(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            final HashMap<Aspect, TileInfusionProvider> aspectProvider = new HashMap<>();
            AspectList aspects = null;

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
            }

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                ItemStack input1 = recipe.mInputs[0];
                ItemStack circuit = recipe.mInputs[1];
                String key = TCRecipeTools.toStringWithoutStackSize(input1);
                int circuitNum = circuit.getItemDamage();
                if (circuitNum <= 0) return CheckRecipeResultRegistry.NO_RECIPE;
                ArrayList<TCRecipeTools.CrucibleCraftingRecipe> recipes = TCRecipeTools.CCR.get(key);
                if (recipes.size() < circuitNum) return CheckRecipeResultRegistry.NO_RECIPE;
                TCRecipeTools.CrucibleCraftingRecipe recipe1 = recipes.get(circuitNum - 1);
                if (!isResearchComplete(recipe1.getResearch())) return Research_not_completed;
                if (!(getControllerSlot() == null)) {
                    if (getControllerSlot().isItemEqual(EssentiaCell_Creative)
                        || getControllerSlot().isItemEqual(ProofOfHeroes)) {
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
                }

                aspectProvider.clear();
                aspects = recipe1.getInputAspects();
                if (aspects.visSize() == 0) {
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                if (mTileInfusionProvider.isEmpty()) {
                    return Essentia_InsentiaL;
                }

                HashMap<Aspect, Integer> aspectMaxParallel = new HashMap<>();
                for (Aspect aspect : aspects.getAspects()) {
                    int amount = aspects.getAmount(aspect);
                    if (amount <= 0) {
                        continue;
                    }

                    for (TileInfusionProvider hatch : mTileInfusionProvider) {
                        int possibleParallel = GTUtility.safeInt(hatch.getAspectAmountInNetwork(aspect) / amount, 1);
                        if (possibleParallel <= 0) {
                            continue;
                        }

                        if (possibleParallel > aspectMaxParallel.computeIfAbsent(aspect, k -> 0)) {
                            aspectMaxParallel.put(aspect, possibleParallel);
                            aspectProvider.put(aspect, hatch);
                        }
                    }

                    if (aspectMaxParallel.get(aspect) == 0) {
                        return Essentia_InsentiaL;
                    }
                }
                maxParallel = Integer.min(Collections.min(aspectMaxParallel.values()), maxParallel);

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                for (Aspect aspect : aspectProvider.keySet()) {
                    aspectProvider.get(aspect)
                        .takeFromContainer(aspect, aspects.getAmount(aspect) * getCurrentParallels());
                }
                return super.onRecipeStart(recipe);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public boolean isResearchComplete(String key) {
        if (!key.startsWith("@") && ResearchCategories.getResearch(key) == null) {
            return false;
        } else {
            return this.Research != null && !this.Research.isEmpty() && this.Research.contains(key);
        }
    }

    // WIP
    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        return super.checkProcessing();
    }

    private String getUsername() {
        return this.getBaseMetaTileEntity()
            .getOwnerName();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.IndustrialAlchemyTowerRecipes;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return mSpeedBonus == (double) 1 / 11.4514;
    }

    @Override
    protected float getSpeedBonus() {
        mSpeedBonus = 0.0;
        countSpeedBonus();
        return (float) mSpeedBonus;
    }

    private void countSpeedBonus() {
        if (mNodeEnergized.isEmpty()) {
            mSpeedBonus = 1.0;
            return;
        } else if (mNodeEnergized.size() < 4) {
            mSpeedBonus = 1 - mNodeEnergized.size() * 0.1;
            return;
        } else {
            int fire = getMaxPurityNodeAmount(mNodeEnergized, Aspect.FIRE);
            int air = getMaxPurityNodeAmount(mNodeEnergized, Aspect.AIR);
            int order = getMaxPurityNodeAmount(mNodeEnergized, Aspect.ORDER);
            int entropy = getMaxPurityNodeAmount(mNodeEnergized, Aspect.ENTROPY);
            List<Integer> data = Arrays.asList(fire, air, order, entropy, 10);
            int min = Collections.min(data);
            int max = Collections.max(data);
            double baseSpeedBonus = 1 / 11.4514 + (0.7 - 1 / 11.4514) * (1 - Math.pow(Math.E, min - 10));
            double punishSpeedBonus = (0.7 - baseSpeedBonus) * (max - min) / max;
            mSpeedBonus = baseSpeedBonus + punishSpeedBonus;
            return;
        }
    }

    private int findMaxIndex(ArrayList<Double> arrayList) {
        int index = 0;
        for (int i = 1; i < arrayList.size(); i++) {
            if (arrayList.get(i) > arrayList.get(index)) {
                index = i;
            }
        }
        return index;
    }

    private int getMaxPurityNodeAmount(ArrayList<TileNodeEnergized> mNodeEnergized, Aspect aspect) {
        ArrayList<Double> Purity = new ArrayList<>();
        for (TileNodeEnergized node : mNodeEnergized) {
            AspectList aspects = node.getAspects();
            double sum = 0;
            for (Aspect a : node.getAspects()
                .getPrimalAspects()) {
                sum += aspects.getAmount(a);
            }
            double sAspect = node.getAspects()
                .getAmount(aspect);
            Purity.add(sAspect / sum);
        }
        return mNodeEnergized.get(findMaxIndex(Purity))
            .getAspects()
            .getAmount(aspect);
    }

    private int getmParallel() {
        return (int) Math.pow(essentiaCellTier, 5);
    }

    @Override
    public int getMaxParallelRecipes() {
        if (getControllerSlot() == null) {
            return getmParallel();
        } else if (getControllerSlot().isItemEqual(ProofOfHeroes)) {
            return Integer.MAX_VALUE;
        } else return getmParallel();
    }

    // endregion

    @Override
    public String[] getInfoData() {
        return super.getInfoData();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 15;
    private final int depthOffSet = 1;
    // spotless:off
    @SuppressWarnings("SpellCheckingInspection")
    private static final String[][] shape = new String[][]{
        {"               ","               ","               ","               ","               ","               ","      HHH      ","      H H      ","      HHH      ","               ","               ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","               ","      HHH      ","     H   H     ","     H   H     ","     H   H     ","      HHH      ","               ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","      ONO      ","     O   O     ","    O     O    ","    N     N    ","    O     O    ","     O   O     ","      ONO      ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","     GSSSG     ","    G     G    ","    D     D    ","    D     D    ","    D     D    ","    G     G    ","     GSSSG     ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","     GSSSG     ","    G     G    ","    D     D    ","    D     D    ","    D     D    ","    G     G    ","     GSSSG     ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","     GSSSG     ","    GX   XG    ","    D     D    ","    D  Y  D    ","    D     D    ","    GX   XG    ","     GSSSG     ","               ","               ","               ","               "},
        {"               ","               ","      EEE      ","     EHHHE     ","    EHBBBHE    ","   EHFBBBFHE   ","  EHBBCCCBBHE  ","  EHBBCCCBBHE  ","  EHBBCCCBBHE  ","   EHFBBBFHE   ","    EHBBBHE    ","     EHHHE     ","      EEE      ","               ","               "},
        {"               ","               ","     OONOO     ","    OHQQQHO    ","   OHQQBQQHO   ","  OHQQBCBQQHO  ","  OQQBCCCBQQO  ","  NQBCCCCCBQN  ","  OQQBCCCBQQO  ","  OHQQBCBQQHO  ","   OHQQBQQHO   ","    OHQQQHO    ","     OONOO     ","               ","               "},
        {"               ","               ","     GSSSG     ","    S     S    ","   S       S   ","  G         G  ","  D         D  ","  D         D  ","  D         D  ","  G         G  ","   S       S   ","    S     S    ","     GSSSG     ","               ","               "},
        {"               ","               ","     GSSSG     ","    S     S    ","   S       S   ","  G         G  ","  D         D  ","  D         D  ","  D         D  ","  G         G  ","   S       S   ","    S     S    ","     GSSSG     ","               ","               "},
        {"               ","               ","     GSSSG     ","    S  X  S    ","   S       S   ","  G         G  ","  D         D  ","  DX   X   XD  ","  D         D  ","  G         G  ","   S       S   ","    S  X  S    ","     GSSSG     ","               ","               "},
        {"               ","     EEEEE     ","    EHBBBHE    ","   EBBBFBBBE   ","  EBAABBBAABE  "," EHBABBSBBABHE "," EBBBBSSSBBBBE "," EBFBSSSSSBFBE "," EBBBBSSSBBBBE "," EHBABBSBBABHE ","  EBAABBBAABE  ","   EBBBFBBBE   ","    EHBBBHE    ","     EEEEE     ","               "},
        {"               ","    OOONOOO    ","   OHBBBBBHO   ","  OHBBALABBHO  "," OHBBBAJABBBHO "," OBBBJAJAJBBBO "," OBAAAJJJAAABO "," NBLJJJJJJJLBN "," OBAAAJJJAAABO "," OBBBJAJAJBBBO "," OHBBBAJABBBHO ","  OHBBALABBHO  ","   OHBBBBBHO   ","    OOONOOO    ","               "},
        {"               ","    IGHHHGI    ","   IB     BI   ","  IB       BI  "," IB         BI "," G           G "," H           H "," H           H "," H           H "," G           G "," IB         BI ","  IB       BI  ","   IB     BI   ","    IGHHHGI    ","               "},
        {"               ","     GSSSG     ","    S     S    ","   S       S   ","  S V     V S  "," G           G "," D           D "," D     K     D "," D           D "," G           G ","  S V     V S  ","   S       S   ","    S     S    ","     GSSSG     ","               "},
        {"               ","     GS~SG     ","    S     S    ","   S       S   ","  S W     W S  "," G           G "," D           D "," D     K     D "," D           D "," G           G ","  S W     W S  ","   S       S   ","    S     S    ","     GSSSG     ","               "},
        {"     Z   Z     ","    EGSSSGE    ","   EB     BE   ","  EB       BE  "," EB U     U BE ","ZG           GZ"," D           D "," D     R     D "," D           D ","ZG           GZ"," EB U     U BE ","  EB       BE  ","   EB     BE   ","    EGSSSGE    ","     Z   Z     "},
        {"     NEEEN     ","   OOHHHHHOO   ","  OHMMMMMMMHO  "," OHMMBBBBBMMHO "," OMMBBPPPBBMMO ","NHMBBPPCPPBBMHN","EHMBPPCCCPPBMHE","EHMBPCCCCCPBMHE","EHMBPPCCCPPBMHE","NHMBBPPCPPBBMHN"," OMMBBPPPBBMMO "," OHMMBBBBBMMHO ","  OHMMMMMMMHO  ","   OOHHHHHOO   ","     NEEEN     "}};
    // spotless:on
    private static IStructureDefinition<TST_IndustrialAlchemyTower> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_IndustrialAlchemyTower> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            var channel = "chisel";
            var list = ImmutableList.of(
                TstUtils.newItemWithMeta(blockCosmeticSolid, 6),
                TstUtils.newItemWithMeta(BlockArcane_1.getLeft(), BlockArcane_1.getRight()),
                TstUtils.newItemWithMeta(BlockArcane_4.getLeft(), BlockArcane_4.getRight()));
            STRUCTURE_DEFINITION = StructureDefinitionBuilder(TST_IndustrialAlchemyTower.class)
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    withChannel(
                        "essentia_cell",
                        ofBlocksTiered(
                            (a, b) -> a == essentiaCell ? b + 1 : 0,
                            ImmutableList.of(
                                Pair.of(essentiaCell, 0),
                                Pair.of(essentiaCell, 1),
                                Pair.of(essentiaCell, 2),
                                Pair.of(essentiaCell, 3)),
                            0,
                            (x, y) -> x.essentiaCellTier = y,
                            x -> x.essentiaCellTier)))
                .addElement(
                    'B',
                    ofChain(
                        buildHatchAdder(TST_IndustrialAlchemyTower.class).atLeast(InputBus, OutputBus, Energy)
                            .adder(TST_IndustrialAlchemyTower::addToMachineList)
                            .casingIndex(1536)
                            .dot(1)
                            .buildAndChain(magicCasing, 0),
                        ofAccurateTileAdder(
                            TST_IndustrialAlchemyTower::addInfusionProvider,
                            BlockEnum.INFUSION_PROVIDER.getBlock(),
                            0),
                        ofAccurateBlockAdder(
                            TST_IndustrialAlchemyTower::addTravelAnchor,
                            BlockTravelAnchor.getLeft(),
                            BlockTravelAnchor.getRight())))
                .addElement('C', ofBlock(FieryBlock.getLeft(), FieryBlock.getRight()))
                .addElement(
                    'D',
                    ofChain(
                        ofAccurateTileAdder(TST_IndustrialAlchemyTower::addCosmeticOpaque, blockCosmeticOpaque, 2),
                        ofAccurateTile(TileArcaneHole.class, TstBlocks.BlockArcaneHole, 0)))
                .addElement('E', ofBlockStrict(blockSlabStone, 0))
                .addElement('F', ofBlock(blockCosmeticSolid, 0))
                .addElement('G', ofVariableBlock(channel, blockCosmeticSolid, 6, list))
                .addElement('H', ofBlock(blockCosmeticSolid, 7))
                .addElement('I', ofBlockStrictExt(blockSlabStone, 8, blockSlabStone, 0))
                .addElement('J', ofBlock(blockMetalDevice, 3))
                .addElement('K', ofBlock(blockMetalDevice, 9))
                .addElement('L', ofBlock(BlockTranslucent.getLeft(), BlockTranslucent.getRight()))
                // .addElement('M', ofBlock(BlockBrickTranslucent.getLeft(), BlockBrickTranslucent.getRight()))
                .addElement('M', ofBlock(BlockTranslucent.getLeft(), BlockTranslucent.getRight()))
                .addElement('N', ofVariableBlock(channel, BlockArcane_1.getLeft(), BlockArcane_1.getRight(), list))
                .addElement('O', ofVariableBlock(channel, BlockArcane_4.getLeft(), BlockArcane_4.getRight(), list))
                .addElement('P', ofBlock(EldritchArk.getLeft(), EldritchArk.getRight()))
                .addElement('Q', ofBlock(CarvedEminenceStone.getLeft(), CarvedEminenceStone.getRight()))
                .addElement('R', ofAccurateTile(TileCrucible.class, blockMetalDevice, 0))
                .addElement(
                    'S',
                    ofAccurateTileAdder(TST_IndustrialAlchemyTower::addCosmeticOpaque, blockCosmeticOpaque, 2))
                .addElement('U', ofAccurateTile(TileNodeStabilizer.class, blockStoneDevice, 10))
                .addElement('V', ofAccurateTile(TileNodeConverter.class, blockStoneDevice, 11))
                .addElement(
                    'W',
                    ofChain(
                        ofTileAdder(TST_IndustrialAlchemyTower::addNodeEnergized, blockAiry, 0),
                        ofBlock(Blocks.air, 0)))
                .addElement('X', ofAccurateTile(TileElectricCloud.class, electricCloud, 0))
                .addElement('Y', ofAccurateTile(TileEntityBeacon.class, Blocks.beacon, 0))
                .addElement('Z', ofAccurateTileExt(TileNitor.class, blockAiry, 1, ConfigItems.itemResource, 1))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private String getPlayName() {
        return this.getBaseMetaTileEntity()
            .getOwnerName();
    }

    public boolean addTravelAnchor(Block block, int meta) {
        if (block instanceof ITileEntityProvider) {
            return block.getClass()
                .getSimpleName()
                .equals("BlockTravelAnchor");
        } else return block == Blocks.stone; // dev environment subs
    }

    public boolean addCosmeticOpaque(TileEntity tileEntity) {
        if (tileEntity instanceof TileOwned tileOwned) {
            if (getPlayName() != null && Objects.equals(tileOwned.owner, "")) {
                tileOwned.owner = getPlayName();
            }
            return true;
        }
        return false;
    }

    public final boolean addNodeEnergized(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileNodeEnergized) {
            if (!(mNodeEnergized.size() == 4)) {
                return this.mNodeEnergized.add((TileNodeEnergized) aTileEntity);
            } else return true;
        }
        return false;
    }

    public boolean addInfusionProvider(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileInfusionProvider) {
            return this.mTileInfusionProvider.add((TileInfusionProvider) aTileEntity);
        }
        return false;
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

    // spotless:off
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_IndustrialAlchemyTower_MachineType
        // # Alchemy Tower
        // #zh_CN 炼金塔
        tt.addMachineType(TextEnums.tr("Tooltip_IndustrialAlchemyTower_MachineType"))
            // #tr Tooltip_IndustrialAlchemyTower_Controller
            // # Controller block for the Industrial Alchemy Tower
            // #zh_CN 工业炼金塔的控制器方块
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_Controller"))
            // #tr Tooltip_IndustrialAlchemyTower_00
            // # Gurgling
            // #zh_CN 咕噜咕噜
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_00"))
            // #tr Tooltip_IndustrialAlchemyTower_01
            // # Please use the Infusion Supplier to supply Essence!
            // #zh_CN 请使用注魔供应器供给源质！
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_01"))
            // #tr Tooltip_IndustrialAlchemyTower_02
            // # Parallelism depends on the level of the structure block.
            // #zh_CN 并行取决于结构方块的等级。
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_02"))
            // #tr Tooltip_IndustrialAlchemyTower_03
            // # Do an 4/2 overclock.Turn on lossless overclocking after reaching the maximum acceleration rate.
            // #zh_CN 进行4/2超频。达到最高加速倍率后开启无损超频.
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_03"))
            // #tr Tooltip_IndustrialAlchemyTower_04
            // # Use Charged Nodes to get acceleration rewards,
            // #zh_CN §b使用充能节点以获得加速奖励§7，
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_04"))
            // #tr Tooltip_IndustrialAlchemyTower_05
            // # No acceleration when there are no charging nodes.
            // #zh_CN 无充能节点时不加速。
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_05"))
            // #tr Tooltip_IndustrialAlchemyTower_06
            // # When the number of nodes is less than 4, each charging node increases acceleration by 10%%,
            // #zh_CN 当节点数量小于4时每有一个充能节点就加速10%%，
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_06"))
            // #tr Tooltip_IndustrialAlchemyTower_07
            // # When the number of nodes is 4, calculate the nodes with the highest rates of air, fire, entropy, and order.
            // #zh_CN 当节点数量为4时，计算含有风、火、混沌、秩序率最高的节点，
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_07"))
            // #tr Tooltip_IndustrialAlchemyTower_08
            // # Use the essence quantity of the node with the highest rate of each essence for subsequent calculations.
            // #zh_CN 将含该要素率最多的节点的该要素量参与后续的计算。
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_08"))
            // #tr Tooltip_IndustrialAlchemyTower_09
            // # min=min(fire,air,entropy,order,10);max=max(fire,air,entropy,order,10);
            // #zh_CN {\SPACE}{\AQUA}min=min(fire,air,entropy,order,10);max=max(fire,air,entropy,order,10);
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_09"))
            // #tr Tooltip_IndustrialAlchemyTower_10
            // # Basic speed bonus equals 1/11.4514 + (0.7-1/11.4514)*(1-exp(min-10)).
            // #zh_CN 基础加速倍率为{\SPACE}{\AQUA}baseSpeedBonus=1/11.4514 + (0.7-1/11.4514)*(1-exp(min-10))
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_10"))
            // #tr Tooltip_IndustrialAlchemyTower_11
            // # If there is too much disparity in the quantity of the above essence, there will be a penalty mechanism.
            // #zh_CN 如果上述的要素量差距过大会有惩罚机制。
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_11"))
            // #tr Tooltip_IndustrialAlchemyTower_12
            // # Punishment speed bonus equals (0.7-baseSpeedBonus)*(max-min)/max
            // #zh_CN 惩罚倍率为{\SPACE}{\AQUA}punishSpeedBonus=(0.7-baseSpeedBonus)*(max-min)/max;
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_12"))
            // #tr Tooltip_IndustrialAlchemyTower_13
            // # Final speed bonus equals the sum of the two.
            // #zh_CN 最终倍率为二者相加。
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_13"))
            // #tr Tooltip_IndustrialAlchemyTower_14
            // # Putting EssentiaCell_Creative in the controller GUI doesn't cost essentia, but if it's a hero's proof,maybe a little bit of an incredible change...
            // #zh_CN 在控制器GUI放入魔导源质元件则无需消耗源质，但如果是某位英雄的证明或许会发生一点不可思议的变化...
            .addInfo(TextEnums.tr("Tooltip_IndustrialAlchemyTower_14"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            // #tr Tooltip_Channel_Helper
            // # You can use the {\BLUE} channel:{\YELLOW}chisel{\GRAY} to automatically convert chisel blocks when building, and you can see nei to preview the available blocks
            // #zh_CN 可以使用{\BLUE}信道:{\YELLOW}chisel{\GRAY}进行搭建时自动转换凿子方块，可以查看nei进行预览可用方块
            .addInfo(TextEnums.tr("Tooltip_Channel_Helper"))
            // #tr Tooltip_IndustrialAlchemyTower_15
            // # Infusion Provider
            // #zh_CN 注魔供应器
            // #tr Tooltip_IndustrialAlchemyTower_16
            // # §bAny magic mechanical block
            // #zh_CN §b任意魔法机械方块
            .addOtherStructurePart(
                TextEnums.tr("Tooltip_IndustrialAlchemyTower_15"),
                TextEnums.tr("Tooltip_IndustrialAlchemyTower_16"))
            // #tr Tooltip_IndustrialAlchemyTower_16
            // # §bAny magic mechanical block
            // #zh_CN §b任意魔法机械方块
            .addInputBus(TextEnums.tr("Tooltip_IndustrialAlchemyTower_16"))
            .addOutputBus(TextEnums.tr("Tooltip_IndustrialAlchemyTower_16"))
            .addEnergyHatch(TextEnums.tr("Tooltip_IndustrialAlchemyTower_16"))
            // #tr Tooltip_IndustrialAlchemyTower_17
            // # Travel anchor
            // #zh_CN 旅行锚
            // #tr Tooltip_IndustrialAlchemyTower_16
            // # §bAny magic mechanical block
            // #zh_CN §b任意魔法机械方块
            .addOtherStructurePart(
                TextEnums.tr("Tooltip_IndustrialAlchemyTower_17"),
                TextEnums.tr("Tooltip_IndustrialAlchemyTower_16"))
            // #tr Tooltip_IndustrialAlchemyTower_18
            // # Essentia diffusion unit
            // #zh_CN 源质扩散单元
            // #tr Tooltip_IndustrialAlchemyTower_19
            // # Each level provides tier^5 parallel
            // #zh_CN §b每级提供tier^5的并行
            .addOtherStructurePart(
                TextEnums.tr("Tooltip_IndustrialAlchemyTower_18"),
                TextEnums.tr("Tooltip_IndustrialAlchemyTower_19"))
            // #tr Tooltip_IndustrialAlchemyTower_20
            // # §l§dArcane Empty Space
            // #zh_CN §l§d奥术裂隙
            // #tr Tooltip_IndustrialAlchemyTower_21
            // # Replaceable warded glass on both sides of the machine
            // #zh_CN 可替换机器两侧守卫者玻璃
            .addOtherStructurePart(
                TextEnums.tr("Tooltip_IndustrialAlchemyTower_20"),
                TextEnums.tr("Tooltip_IndustrialAlchemyTower_21"))
            .toolTipFinisher(ModName);
        return tt;
    }
    // spotless:on

    // endregion

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagList nbtTagList = new NBTTagList();
        for (String string : Research) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ResearchName", string);
            nbtTagList.appendTag(tag);
        }
        aNBT.setInteger("essentiaCellTier", this.essentiaCellTier);
        aNBT.setDouble("mSpeedBonus", this.mSpeedBonus);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.Research.clear();
        for (int i = 0; i < aNBT.getTagList("Research", 10)
            .tagCount(); i++) {
            if (aNBT.getTagList("Research", 10)
                .getCompoundTagAt(i)
                .hasKey("ResearchName")) {
                this.Research.add(
                    aNBT.getTagList("Research", 10)
                        .getCompoundTagAt(i)
                        .getString("ResearchName"));
            }
        }
        this.essentiaCellTier = aNBT.getInteger("essentiaCellTier");
        this.mSpeedBonus = aNBT.getDouble("mSpeedBonus");
        super.loadNBTData(aNBT);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 100 == 0) {
            super.onPreTick(aBaseMetaTileEntity, aTick);
            if (aBaseMetaTileEntity.isServerSide()) {
                ArrayList<String> list = getResearchForPlayer(getUsername());
                if ((this.Research == null && list != null)
                    || (list != null && !list.isEmpty() && this.Research.size() != list.size())) {
                    this.Research = list;
                }
            }
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        essentiaCellTier = 0;
        mNodeEnergized.clear();
        repairMachine();
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_IndustrialAlchemyTower(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { TextureFactory.of(blockMetalDevice, 9), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { TextureFactory.of(blockMetalDevice, 9), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { TextureFactory.of(blockMetalDevice, 9) };
    }
}

// Structure:
//
// Blocks:
// A -> ofBlock...(essentiaCell, 2, ...);
// B -> ofBlock...(magicCasing, 0, ...);
// C -> ofBlock...(tile.FieryBlock, 0, ...);
// D -> ofBlock...(tile.TBoldLapis, 0, ...);
// E -> ofBlock...(tile.blockCosmeticSlabStone, 0, ...);
// F -> ofBlock...(tile.blockCosmeticSolid, 0, ...);
// G -> ofBlock...(tile.blockCosmeticSolid, 6, ...);
// H -> ofBlock...(tile.blockCosmeticSolid, 7, ...);
// I -> ofBlock...(tile.blockDiamond, 0, ...);
// J -> ofBlock...(tile.blockMetalDevice, 3, ...);
// K -> ofBlock...(tile.blockMetalDevice, 9, ...);
// L -> ofBlock...(tile.blockTranslucent, 0, ...);
// M -> ofBlock...(tile.blockTranslucent, 1, ...);
// N -> ofBlock...(tile.chisel.arcane, 1, ...);
// O -> ofBlock...(tile.chisel.arcane, 4, ...);
// P -> ofBlock...(tile.eldritchArk, 0, ...);
// Q -> ofBlock...(tile.extrautils:decorativeBlock1, 14, ...);
//
// Tiles:
//
// Special Tiles:
// R -> ofSpecialTileAdder(thaumcraft.common.tiles.TileCrucible, ...); // You will probably want to change it to
// something else
// S -> ofSpecialTileAdder(thaumcraft.common.tiles.TileOwned, ...); // You will probably want to change it to something
// else
// T -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaTileEntity, ...); // You will probably want to change it
// to something else
// U -> ofSpecialTileAdder(thaumcraft.common.tiles.TileNodeStabilizer, ...); // You will probably want to change it to
// something else
// V -> ofSpecialTileAdder(thaumcraft.common.tiles.TileNodeConverter, ...); // You will probably want to change it to
// something else
// W -> ofSpecialTileAdder(makeo.gadomancy.common.blocks.tiles.TileExtendedNode, ...); // You will probably want to
// change it to something else
// X -> ofSpecialTileAdder(emt.tile.TileElectricCloud, ...); // You will probably want to change it to something else
// Y -> ofSpecialTileAdder(net.minecraft.tileentity.TileEntityBeacon, ...); // You will probably want to change it to
// something else
// Z -> ofSpecialTileAdder(thaumcraft.common.tiles.TileNitor, ...); // You will probably want to change it to something
// else
//
// Offsets:
// 7 15 1
