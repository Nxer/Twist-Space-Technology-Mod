package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_1;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_4;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockBrickTranslucent;
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
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static thaumcraft.common.config.ConfigBlocks.blockAiry;
import static thaumcraft.common.config.ConfigBlocks.blockCosmeticOpaque;
import static thaumcraft.common.config.ConfigBlocks.blockCosmeticSolid;
import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;
import static thaumcraft.common.config.ConfigBlocks.blockSlabStone;
import static thaumcraft.common.config.ConfigBlocks.blockStoneDevice;
import static thaumcraft.common.lib.research.ResearchManager.getResearchForPlayer;

import java.util.ArrayList;
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
import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.tile.TileArcaneHole;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipeTools;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
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
import gregtech.api.util.MultiblockTooltipBuilder;
import thaumcraft.api.aspects.Aspect;
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
    private int mParallel;
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
                int Para = createParallelHelper(recipe).setConsumption(false)
                    .build()
                    .getCurrentParallel();
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
                for (Aspect aspect : recipe1.getInputAspects()
                    .getAspects()) {
                    if (mTileInfusionProvider.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;
                    for (TileInfusionProvider hatch : mTileInfusionProvider) {
                        if (hatch.takeFromContainer(aspect, recipe1.getAspectAmount(aspect) * Para)) {
                            return CheckRecipeResultRegistry.SUCCESSFUL;
                        } else return Essentia_InsentiaL;
                    }
                }
                return CheckRecipeResultRegistry.NO_RECIPE;
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
        return GTCMRecipe.IndustrialAlchemyTowerRecipe;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1.0F;
    }

    @Override
    protected int getLimitedMaxParallel() {
        return getMaxParallelRecipes();
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 4;
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
                        ofAccurateTile(TileArcaneHole.class, BasicBlocks.BlockArcaneHole, 0)))
                .addElement('E', ofBlockStrict(blockSlabStone, 0))
                .addElement('F', ofBlock(blockCosmeticSolid, 0))
                .addElement('G', ofBlock(blockCosmeticSolid, 6))
                .addElement('H', ofBlock(blockCosmeticSolid, 7))
                .addElement('I', ofBlockStrictExt(blockSlabStone, 8, blockSlabStone, 0))
                .addElement('J', ofBlock(blockMetalDevice, 3))
                .addElement('K', ofBlock(blockMetalDevice, 9))
                .addElement('L', ofBlock(BlockTranslucent.getLeft(), BlockTranslucent.getRight()))
                .addElement('M', ofBlock(BlockBrickTranslucent.getLeft(), BlockBrickTranslucent.getRight()))
                .addElement(
                    'N',
                    ofChain(
                        ofBlock(BlockArcane_1.getLeft(), BlockArcane_1.getRight()),
                        ofBlockStrictExt(BlockArcane_1.getLeft(), BlockArcane_1.getRight(), blockCosmeticSolid, 6)))
                .addElement(
                    'O',
                    ofChain(
                        ofBlock(BlockArcane_4.getLeft(), BlockArcane_4.getRight()),
                        ofBlockStrictExt(BlockArcane_4.getLeft(), BlockArcane_4.getRight(), blockCosmeticSolid, 6)))
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
        return survivialBuildPiece(
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

    // WIP
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_IndustrialAlchemyTower_MachineType
        // # Alchemy Tower
        // #zh_CN 工业炼金塔
        tt.addMachineType(TextEnums.tr("Tooltip_IndustrialAlchemyTower_MachineType"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .toolTipFinisher(ModName);
        return tt;
    }

    // endregion

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagList nbtTagList = new NBTTagList();
        for (String string : Research) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ResearchName", string);
            nbtTagList.appendTag(tag);
        }
        aNBT.setInteger("mParallel", this.mParallel);
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
        this.mParallel = aNBT.getInteger("mParallel");
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
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { TextureFactory.of(blockMetalDevice, 9), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { TextureFactory.of(blockMetalDevice, 9) };
    }
}

// Blocks:
// A -> ofBlock...(essentiaCell, 2, ...);
// B -> ofBlock...(magicCasing, 0, ...);
// C -> ofBlock...(tile.FieryBlock, 0, ...);
// D -> ofBlock...(tile.blockCosmeticSlabStone, 0, ...);
// E -> ofBlock...(tile.blockCosmeticSolid, 0, ...);
// F -> ofBlock...(tile.blockCosmeticSolid, 7, ...);
// G -> ofBlock...(tile.blockDiamond, 0, ...);
// H -> ofBlock...(tile.blockLapis, 0, ...);
// I -> ofBlock...(tile.blockMetalDevice, 3, ...);
// J -> ofBlock...(tile.blockStairsArcaneStone, 0, ...);
// K -> ofBlock...(tile.blockTranslucent, 0, ...);
// L -> ofBlock...(tile.blockTranslucent, 1, ...);
// M -> ofBlock...(tile.chisel.arcane, 1, ...);
// N -> ofBlock...(tile.chisel.arcane, 4, ...);
// O -> ofBlock...(tile.chisel.arcane, 6, ...);
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
// U -> ofSpecialTileAdder(emt.tile.TileElectricCloud, ...); // You will probably want to change it to something else
// V -> ofSpecialTileAdder(net.minecraft.tileentity.TileEntityBeacon, ...); // You will probably want to change it to
// something else
// W -> ofSpecialTileAdder(thaumcraft.common.tiles.TileNitor, ...); // You will probably want to change it to something
// else
//
// Offsets:
// 7 17 7
