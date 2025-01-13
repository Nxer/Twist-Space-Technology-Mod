package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_1;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_4;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_6;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockBrickTranslucent;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockTranslucent;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.CarvedEminenceStone;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.EldritchArk;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.FieryBlock;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.StructureDefinitionBuilder;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateTile;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateTileAdder;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateTileExt;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofBlockStrict;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofBlockStrictExt;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
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
import static thaumcraft.common.config.ConfigBlocks.blockStairsArcaneStone;
import static thaumcraft.common.lib.research.ResearchManager.getResearchForPlayer;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nonnull;

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
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
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
import thaumcraft.common.tiles.TileOwned;
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
    private final int verticalOffSet = 17;
    private final int depthOffSet = 7;
    // spotless:off
    @SuppressWarnings("SpellCheckingInspection")
    private static final String[][] shape = new String[][]{
        {"               ","               ","               ","               ","               ","               ","       D       ","      D D      ","       D       ","               ","               ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","               ","               ","      FFF      ","      F F      ","      FFF      ","               ","               ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","               ","      FFF      ","     FDDDF     ","     FD DF     ","     FDDDF     ","      FFF      ","               ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","     DFMFD     ","    DFDDDFD    ","    FD   DF    ","    MD   DM    ","    FD   DF    ","    DFDDDFD    ","     DFMFD     ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","     N   N     ","    N     N    ","               ","               ","               ","    N     N    ","     N   N     ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","     N   N     ","    N     N    ","               ","               ","               ","    N     N    ","     N   N     ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","     N   N     ","    NU   UN    ","               ","       V       ","               ","    NU   UN    ","     N   N     ","               ","               ","               ","               "},
        {"               ","               ","               ","     DDDDD     ","    DFBBBFD    ","   DFEBBBEFD   ","   DBBCCCBBD   ","   DBBCCCBBD   ","   DBBCCCBBD   ","   DFEBBBEFD   ","    DFBBBFD    ","     DDDDD     ","               ","               ","               "},
        {"               ","               ","     FFMFF     ","    FFQQQFF    ","   FFQQBQQFF   ","  FFQQBBBQQFF  ","  FQQBCCCBQQF  ","  MQBBCCCBBQM  ","  FQQBCCCBQQF  ","  FFQQBBBQQFF  ","   FFQQBQQFF   ","    FFQQQFF    ","     FFMFF     ","               ","               "},
        {"               ","               ","     N   N     ","    S     S    ","   S       S   ","  N         N  ","               ","               ","               ","  N         N  ","   S       S   ","    S     S    ","     N   N     ","               ","               "},
        {"               ","               ","     N   N     ","    S     S    ","   S       S   ","  N         N  ","               ","               ","               ","  N         N  ","   S       S   ","    S     S    ","     N   N     ","               ","               "},
        {"               ","               ","     N U N     ","    S     S    ","   S       S   ","  N         N  ","               ","  U    U    U  ","               ","  N         N  ","   S       S   ","    S     S    ","     N U N     ","               ","               "},
        {"               ","     DDDDD     ","    DFBBBFD    ","   DBBHSHBBD   ","  DBBSHSHSBBD  "," DFBSSHSHSSBFD "," DBHHHSSSHHHBD "," DBSSSSRSSSSBD "," DBHHHSSSHHHBD "," DFBSSHSHSSBFD ","  DBBSHSHSBBD  ","   DBBHSHBBD   ","    DFFFFFD    ","     DDDDD     ","               "},
        {"               ","    FFFMFFF    ","   FFBBBBBFF   ","  FFBBAKABBFF  "," FFBBBAIABBBFF "," FBBBIAIAIBBBF "," FBAAAIIIAAABF "," MBKIIIIIIIKBM "," FBAAAIIIAAABF "," FBBBIAIAIBBBF "," FFBBBAIABBBFF ","  FFBBAKABBFF  ","   FFBBBBBFF   ","    FFFMFFF    ","               "},
        {"               ","    GN   NG    ","   GB     BG   ","  GB       BG  "," GB         BG "," N           N ","               ","               ","               "," N           N "," GB         BG ","  GB       BG  ","   GB     BG   ","    GN   NG    ","               "},
        {"               ","     N   N     ","    S     S    ","   S       S   ","  S         S  "," N           N ","               ","               ","               "," N           N ","  S         S  ","   S       S   ","    S     S    ","     N   N     ","               "},
        {"               ","     N   N     ","    S     S    ","   S       S   ","  S         S  "," N           N ","               ","               ","               "," N           N ","  S         S  ","   S       S   ","    S     S    ","     N   N     ","               "},
        {"     W   W     ","    DN   ND    ","   DB     BD   ","  DB       BD  "," DB         BD ","WN           NW","               ","       ~       ","               ","WN           NW"," DB         BD ","  DB       BD  ","   DB     BD   ","    DN   ND    ","     W   W     "},
        {"     MJJJM     ","   FFFFFFFFF   ","  MFLLLLLLLFM  "," FFLLOOOOOLLFF "," FLLOOPPPOOLLF ","MFLOOPPCPPOOLFM","JFLOPPCCCPPOLFJ","JFLOPCCCCCPOLFJ","JFLOPPCCCPPOLFJ","MFLOOPPCPPOOLFM"," FLLOOPPPOOLLF "," FFLLOOOOOLLFF ","  MFLLLLLLLFM  ","   FFFFFFFFF   ","     MJJJM     "}};
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
                    buildHatchAdder(TST_IndustrialAlchemyTower.class).atLeast(InputBus, OutputBus, Energy)
                        .adder(TST_IndustrialAlchemyTower::addToMachineList)
                        .casingIndex(1536)
                        .dot(1)
                        .buildAndChain(magicCasing, 0))
                .addElement('C', ofBlock(FieryBlock.getLeft(), FieryBlock.getRight()))
                .addElement('D', ofBlockStrict(blockSlabStone, 0))
                .addElement('E', ofBlock(blockCosmeticSolid, 0))
                .addElement('F', ofBlock(blockCosmeticSolid, 7))
                .addElement('G', ofBlockStrictExt(blockSlabStone, 8, blockSlabStone, 0))
                .addElement('H', ofBlock(Blocks.lapis_block, 0)) // for updating block
                .addElement('I', ofBlock(blockMetalDevice, 3))
                .addElement('J', ofBlock(blockStairsArcaneStone, 0))
                .addElement('K', ofBlock(BlockTranslucent.getLeft(), BlockTranslucent.getRight()))
                .addElement('L', ofBlock(BlockBrickTranslucent.getLeft(), BlockBrickTranslucent.getRight()))
                .addElement('M', ofBlock(BlockArcane_1.getLeft(), BlockArcane_1.getRight()))
                .addElement('N', ofBlock(BlockArcane_4.getLeft(), BlockArcane_4.getRight()))
                .addElement('O', ofBlock(BlockArcane_6.getLeft(), BlockArcane_6.getRight()))
                .addElement('P', ofBlock(EldritchArk.getLeft(), EldritchArk.getRight()))
                .addElement('Q', ofBlock(CarvedEminenceStone.getLeft(), CarvedEminenceStone.getRight()))
                .addElement('R', ofAccurateTile(TileCrucible.class, blockMetalDevice, 0))
                .addElement(
                    'S',
                    ofAccurateTileAdder(TST_IndustrialAlchemyTower::addCosmeticOpaque, blockCosmeticOpaque, 2))
                .addElement('U', ofAccurateTile(TileElectricCloud.class, electricCloud, 0))
                .addElement('V', ofAccurateTile(TileEntityBeacon.class, Blocks.beacon, 0))
                .addElement('W', ofAccurateTileExt(TileNitor.class, blockAiry, 1, ConfigItems.itemResource, 1))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private String getPlayName() {
        return this.getBaseMetaTileEntity()
            .getOwnerName();
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
            if (active) return new ITexture[] { TextureFactory.of(blockMetalDevice, 3), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { TextureFactory.of(blockMetalDevice, 3), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { TextureFactory.of(blockMetalDevice, 3) };
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
