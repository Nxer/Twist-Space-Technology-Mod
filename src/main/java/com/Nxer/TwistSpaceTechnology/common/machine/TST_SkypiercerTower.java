package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_1;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_4;
import static com.Nxer.TwistSpaceTechnology.config.Config.Parallel_PerRing_SkypiercerTower;
import static com.Nxer.TwistSpaceTechnology.util.AspectLevelCalculator.computeAspectLevel;
import static com.Nxer.TwistSpaceTechnology.util.AspectLevelCalculator.computeAspectSynthesisTime;
import static com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper.createCrystal;
import static com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper.findCombinedAspectCached;
import static com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper.readAspectFromCrystal;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateTile;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateTileAdder;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofAccurateTileExt;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofVariableBlock;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontCenter;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofSpecificTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static emt.init.EMTBlocks.electricCloud;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static thaumcraft.common.config.ConfigBlocks.blockAiry;
import static thaumcraft.common.config.ConfigBlocks.blockCosmeticSolid;
import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.LongConsumer;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_SkypiercerTower;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.cleanroommc.modularui.drawable.UITexture;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import emt.tile.TileElectricCloud;
import goodgenerator.blocks.tileEntity.MTEEssentiaOutputHatch;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import journeymap.shadow.org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileCrucible;
import thaumcraft.common.tiles.TileNitor;
import thaumicenergistics.common.blocks.BlockEnum;
import thaumicenergistics.common.tiles.TileInfusionProvider;
import vazkii.botania.common.block.ModBlocks;

public class TST_SkypiercerTower extends GTCM_MultiMachineBase<TST_SkypiercerTower>
    implements IConstructable, ISurvivalConstructable {

    public TST_SkypiercerTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private final ArrayList<MTEEssentiaOutputHatch> mEssentiaOutputHatches = new ArrayList<>();
    protected ArrayList<TileInfusionProvider> mTileInfusionProvider = new ArrayList<>();
    protected ArrayList<TileNitor> mTileNitors = new ArrayList<>();
    protected ArrayList<TileElectricCloud> mTileElectricCloud = new ArrayList<>();
    protected AspectList mOutputAspects = new AspectList();
    protected String[] mOutputAspectNames = null;
    protected Integer[] mOutputAspectAmounts = null;
    protected double mParallel = 0;

    private int ringCount = 0;

    /**
     * Bitmask of selected aspects for Passive Mode GUI (bit n = aspect at index n in
     * {@link #getAllCompoundAspectsSorted()}).
     */
    private long mAspectSelectionBits = 0L;

    private int RECIPE_DURATION = 32;
    private static final int RECIPE_EUT = 1920;
    private static final int SECOND_IN_TICKS = 20;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_RINGS = "rings";
    private IStructureDefinition<TST_SkypiercerTower> multiDefinition = null;

    private boolean mStopAfterCycle = false;

    // ========================================================
    // Aspect Selection (for Passive Mode GUI)
    // ========================================================

    public long getAspectSelectionBits() {
        return mAspectSelectionBits;
    }

    public void setAspectSelectionBits(long bits) {
        mAspectSelectionBits = bits;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mEssentiaOutputHatches.clear();
    }

    private final int Main_horizontalOffSet = 7;
    private final int Main_verticalOffSet = 17;
    private final int Main_depthOffSet = 1;

    private static final String[][] shapeMain = new String[][] {
        { "    JJJGJJJ    ", "  JJ       JJ  ", " JJ         JJ ", " J           J ", "J             J",
            "J             J", "J      C      J", "G     C C     G", "J      C      J", "J             J",
            "J             J", " J           J ", " JJ         JJ ", "  JJ       JJ  ", "    JJJGJJJ    " },
        { "               ", "    JJJGJJJ    ", "   J       J   ", "  J         J  ", " J           J ",
            " J           J ", " J     C     J ", " G    C C    G ", " J     C     J ", " J           J ",
            " J           J ", "  J         J  ", "   J       J   ", "    JJJGJJJ    ", "               " },
        { "               ", "               ", "    JJJGJJJ    ", "   JJGGGGGJJ   ", "  JJGG G GGJJ  ",
            "  JGG     GGJ  ", "  JG   C   GJ  ", "  GGG C C GGG  ", "  JG   C   GJ  ", "  JGG     GGJ  ",
            "  JJGG G GGJJ  ", "   JJGGGGGJJ   ", "    JJJGJJJ    ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "      G G      ",
            "     G G G     ", "    G  C  G    ", "     GC CG     ", "    G  C  G    ", "     G G G     ",
            "      G G      ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "      AAA      ",
            "     AFFFA     ", "    AFHBHFA    ", "    AFB BFA    ", "    AFHBHFA    ", "     AFFFA     ",
            "      AAA      ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "       A       ",
            "               ", "       C       ", "    A C C A    ", "       C       ", "               ",
            "       A       ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "       A       ",
            "               ", "       C       ", "    A C C A    ", "       C       ", "               ",
            "       A       ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "       A       ",
            "               ", "       C       ", "    A C C A    ", "       C       ", "               ",
            "       A       ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "       A       ",
            "               ", "       C       ", "    A C C A    ", "       C       ", "               ",
            "       A       ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "       A       ", "               ",
            "               ", "       C       ", "   A  C C  A   ", "       C       ", "               ",
            "               ", "       A       ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "       A       ", "       B       ",
            "       H       ", "       B       ", "   ABHB BHBA   ", "       B       ", "       H       ",
            "       B       ", "       A       ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "       A       ", "       A       ",
            "       B       ", "               ", "   AAB   BAA   ", "               ", "       B       ",
            "       A       ", "       A       ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "       A       ", "               ",
            "               ", "       B       ", "   A  B B  A   ", "       B       ", "               ",
            "               ", "       A       ", "               ", "               ", "               " },
        { "               ", "               ", "       A       ", "               ", "               ",
            "               ", "               ", "  A         A  ", "               ", "               ",
            "               ", "               ", "       A       ", "               ", "               " },
        { "               ", "               ", "       A       ", "               ", "               ",
            "               ", "               ", "  A         A  ", "               ", "               ",
            "               ", "               ", "       A       ", "               ", "               " },
        { "               ", "               ", "       A       ", "               ", "       L       ",
            "               ", "               ", "  A L     L A  ", "               ", "               ",
            "       L       ", "               ", "       A       ", "               ", "               " },
        { "               ", "      NAN      ", "               ", "               ", "               ",
            "               ", " N           N ", " A     M     A ", " N           N ", "               ",
            "               ", "               ", "               ", "      NAN      ", "               " },
        { "               ", "      B~B      ", "               ", "               ", "       I       ",
            "               ", " B    DDD    B ", " A  I DDD I  A ", " B    DDD    B ", "               ",
            "       I       ", "               ", "               ", "      BAB      ", "               " },
        { "   AAAAAAAAA   ", "  AACCGGGCCAA  ", " AACGGGGGGGCAA ", "AACGGGGGGGGGCAA", "ACGGGGEGEGGGGCA",
            "ACGGGEGGGEGGGCA", "AGGGEGGGGGEGGGA", "AGGGGGGGGGGGGGA", "AGGGEGGGGGEGGGA", "ACGGGEGGGEGGGCA",
            "ACGGGGEGEGGGGCA", "AACGGGGGGGGGCAA", " AACGGGGGGGCAA ", "  AACCGGGCCAA  ", "   AAAAAAAAA   " } };
    private final int Rings_horizontalOffSet = 4;
    private final int Rings_verticalOffSet = 5;
    private final int Rings_depthOffSet = -2;

    private static final String[][] shapeRings = new String[][] {
        { "         ", "         ", "         ", "    C    ", "   C C   ", "    C    ", "         ", "         ",
            "         " },
        { "         ", "         ", "         ", "    C    ", "   C C   ", "    C    ", "         ", "         ",
            "         " },
        { "  PEOEP  ", " P     P ", "P       P", "E   C   E", "O  C C  O", "E   C   E", "P       P", " P     P ",
            "  PEOEP  " },
        { "         ", "         ", "         ", "    C    ", "   C C   ", "    C    ", "         ", "         ",
            "         " },
        { "         ", "         ", "         ", "    C    ", "   C C   ", "    C    ", "         ", "         ",
            "         " } };

    public TST_SkypiercerTower(String mName) {
        super(mName);
    }

    @Override
    public IStructureDefinition<TST_SkypiercerTower> getStructureDefinition() {
        if (multiDefinition == null) {
            var channel = "chisel";
            var list = ImmutableList.of(
                TstUtils.newItemWithMeta(blockCosmeticSolid, 6),
                TstUtils.newItemWithMeta(BlockArcane_1.getLeft(), BlockArcane_1.getRight()),
                TstUtils.newItemWithMeta(BlockArcane_4.getLeft(), BlockArcane_4.getRight()));
            this.multiDefinition = StructureDefinition.<TST_SkypiercerTower>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addShape(STRUCTURE_PIECE_RINGS, transpose(shapeRings))
                .addElement(
                    'A',
                    ofChain(
                        ofBlock(GregTechAPI.sBlockCasings8, 0),
                        buildHatchAdder(TST_SkypiercerTower.class)
                            .atLeast(
                                gregtech.api.enums.HatchElement.Energy,
                                gregtech.api.enums.HatchElement.InputBus,
                                gregtech.api.enums.HatchElement.OutputBus,
                                gregtech.api.enums.HatchElement.InputHatch,
                                gregtech.api.enums.HatchElement.OutputHatch)
                            .casingIndex(176)
                            .hint(1)
                            .build(),
                        ofAccurateTileAdder(
                            TST_SkypiercerTower::addInfusionProvider,
                            BlockEnum.INFUSION_PROVIDER.getBlock(),
                            0),
                        ofSpecificTileAdder(
                            TST_SkypiercerTower::addEssentiaOutputHatchToMachineList,
                            MTEEssentiaOutputHatch.class,
                            Loaders.essentiaOutputHatch,
                            0)))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 8))
                .addElement('C', ofBlock(GregTechAPI.sBlockFrames, 330))
                .addElement('D', ofBlockAnyMeta(Blocks.iron_block, 1))
                .addElement('E', ofBlock(blockMetalDevice, 9))
                .addElement('F', ofBlock(ConfigBlocks.blockCosmeticOpaque, 2))
                .addElement('G', ofVariableBlock(channel, BlockArcane_1.getLeft(), BlockArcane_1.getRight(), list))
                .addElement('H', ofBlock(ModBlocks.seaLamp, 0))
                .addElement('I', ofAccurateTile(TileCrucible.class, blockMetalDevice, 0))
                .addElement('J', ofBlock(ConfigBlocks.blockCosmeticOpaque, 2))
                .addElement(
                    'L',
                    ofChain(ofAccurateTileAdder(TST_SkypiercerTower::addTileElectricCloud, electricCloud, 0)))
                .addElement('M', TSTStructureUtility.CommonElements.BlockBeacon.get())
                .addElement('N', ofAccurateTileExt(TileNitor.class, blockAiry, 1, ConfigItems.itemResource, 1))
                .addElement(
                    'N',
                    ofChain(
                        ofAccurateTileExt(TileNitor.class, blockAiry, 1, ConfigItems.itemResource, 1),
                        ofAccurateTileAdder(TST_SkypiercerTower::addNitor, blockAiry, 1)))
                .addElement('O', ofBlock(GregTechAPI.sBlockCasings2, 9))
                .addElement('P', ofBlock(blockCosmeticSolid, 6))
                .build();
        }
        return multiDefinition;
    }

    public boolean addInfusionProvider(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileInfusionProvider) {
            TileInfusionProvider provider = (TileInfusionProvider) aTileEntity;
            if (!this.mTileInfusionProvider.contains(provider)) {
                return this.mTileInfusionProvider.add(provider);
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean addNitor(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileNitor) {
            TileNitor nitor = (TileNitor) aTileEntity;
            if (!this.mTileNitors.contains(nitor)) {
                return this.mTileNitors.add(nitor);
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean addTileElectricCloud(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileElectricCloud) {
            TileElectricCloud cloud = (TileElectricCloud) aTileEntity;
            if (!this.mTileElectricCloud.contains(cloud)) {
                return this.mTileElectricCloud.add(cloud);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble("mParallel", this.mParallel);
        aNBT.setInteger("ringCount", this.ringCount);
        aNBT.setLong("aspectSelection", this.mAspectSelectionBits);
        aNBT.setBoolean("stopAfterCycle", this.mStopAfterCycle);
        Aspect[] aspectA = this.mOutputAspects.getAspects();
        NBTTagList nbtTagList = new NBTTagList();
        for (Aspect aspect : aspectA) {
            if (aspect != null) {
                NBTTagCompound f = new NBTTagCompound();
                f.setString("key", aspect.getTag());
                f.setInteger("amount", this.mOutputAspects.getAmount(aspect));
                nbtTagList.appendTag(f);
            }
        }
        aNBT.setTag("Aspects", nbtTagList);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mParallel = aNBT.getDouble("mParallel");
        this.ringCount = aNBT.getInteger("ringCount");
        this.mAspectSelectionBits = aNBT.getLong("aspectSelection");
        this.mStopAfterCycle = aNBT.getBoolean("stopAfterCycle");
        this.mOutputAspects.aspects.clear();
        NBTTagList tlist = aNBT.getTagList("Aspects", 10);
        for (int j = 0; j < tlist.tagCount(); ++j) {
            NBTTagCompound rs = tlist.getCompoundTagAt(j);
            if (rs.hasKey("key"))
                this.mOutputAspects.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int rings = stackSize.stackSize;
        rings--;
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            Main_horizontalOffSet,
            Main_verticalOffSet,
            Main_depthOffSet);
        for (int i = 0; i < rings; i++) {
            buildPiece(
                STRUCTURE_PIECE_RINGS,
                stackSize,
                hintsOnly,
                Rings_horizontalOffSet,
                Main_verticalOffSet + Rings_verticalOffSet * i + Rings_verticalOffSet,
                Rings_depthOffSet);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int rings = stackSize.stackSize;
        rings--;
        int[] built = new int[1 + rings];
        built[0] = survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            Main_horizontalOffSet,
            Main_verticalOffSet,
            Main_depthOffSet,
            elementBudget,
            env,
            false,
            true);
        for (int i = 0; i < rings; i++) {
            built[i + 1] = survivalBuildPiece(
                STRUCTURE_PIECE_RINGS,
                stackSize,
                Rings_horizontalOffSet,
                Main_verticalOffSet + Rings_verticalOffSet * i + Rings_verticalOffSet,
                Rings_depthOffSet,
                elementBudget,
                env,
                false,
                true);
        }
        return TstUtils.multiBuildPiece(built);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        this.mParallel = 0;
        this.ringCount = 0;
        this.mTileInfusionProvider.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, Main_horizontalOffSet, Main_verticalOffSet, Main_depthOffSet, errors)) {
            return;
        }
        while (checkPiece(
            STRUCTURE_PIECE_RINGS,
            Rings_horizontalOffSet,
            Main_verticalOffSet + Rings_verticalOffSet * ringCount + Rings_verticalOffSet,
            Rings_depthOffSet,
            errors)) {
            this.ringCount++;
        }
        errors.clear();
        this.mParallel = (int) Math.min((long) this.ringCount * Parallel_PerRing_SkypiercerTower, Integer.MAX_VALUE);
    }

    private boolean addEssentiaOutputHatchToMachineList(MTEEssentiaOutputHatch aTileEntity) {
        if (aTileEntity != null) {
            return this.mEssentiaOutputHatches.add(aTileEntity);
        }
        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.SkypiercerTower;
    }

    @Override
    @Nonnull
    public CheckRecipeResult checkProcessing() {
        if (mStopAfterCycle) {
            if (mProgresstime >= mMaxProgresstime) {
                super.stopMachine();
                ResetOutputs();
                mStopAfterCycle = false;
                getBaseMetaTileEntity().disableWorking();
                return CheckRecipeResultRegistry.NO_RECIPE;
            }
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
        RECIPE_DURATION = 0;
        ResetOutputs();
        switch (machineMode) {
            case 2:
                return processEssentiaMode();
            case 1:
                return processCrystalEssenceMode();
            case 0:
            default:
                return processPassiveMode();
        }
    }

    @Override
    public int totalMachineMode() {
        return 3;
    }

    public static final UITexture[] tMachineModeIcons = new UITexture[] { UITextures.SKYPIERCER_MODE_PASSIVE,
        UITextures.SKYPIERCER_MODE_CRYSTAL, UITextures.SKYPIERCER_MODE_ESSENTIA };

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        return TextEnums.tr("SkypiercerTower.mode." + machineMode);
        // spotless:off
        // #tr SkypiercerTower.mode.0
        // #en_US Passive Mode
        // #zh_CN 被动模式
        // #tr SkypiercerTower.mode.1
        // #en_US Crystal Essence Mode
        // #zh_CN 晶化源质模式
        // #tr SkypiercerTower.mode.2
        // #en_US Essentia Mode
        // #zh_CN 源质模式
        // spotless:on
    }

    @Override
    protected MTEMultiBlockBaseGui<?> getGui() {
        TST_Gui_SkypiercerTower gui = new TST_Gui_SkypiercerTower(this);
        if (supportsMachineModeSwitch()) {
            gui.withMachineModeIcons(getMachineModeIcons());
        }
        return gui;
    }

    // ========================================================
    // Mode 2: Essentia Mode (formerly Challenge)
    // ========================================================
    private @NotNull CheckRecipeResult processEssentiaMode() {
        if (mTileInfusionProvider.isEmpty()) {
            return SimpleCheckRecipeResult.ofFailurePersistOnShutdown("No Infusion Provider found.");
        }
        if (mTileInfusionProvider.size() > 1) {
            return SimpleCheckRecipeResult
                .ofFailurePersistOnShutdown("Multiple Infusion Providers found. Please connect only one.");
        }
        TileInfusionProvider provider = mTileInfusionProvider.get(0);
        Map<Aspect, Integer> aspectsInNetwork = new HashMap<>();
        for (Aspect aspect : Aspect.aspects.values()) {
            int amount = (int) provider.getAspectAmountInNetwork(aspect);
            if (amount > 0) aspectsInNetwork.put(aspect, amount);
        }
        Aspect compA = null, compB = null, resultAspect = null;
        outer: for (Aspect a : aspectsInNetwork.keySet()) {
            for (Aspect b : aspectsInNetwork.keySet()) {
                if (a == b) continue;
                Aspect cached = findCombinedAspectCached(a, b);
                if (cached != null) {
                    compA = a;
                    compB = b;
                    resultAspect = cached;
                    break outer;
                }
            }
        }
        if (resultAspect == null) return CheckRecipeResultRegistry.NO_RECIPE;

        int parallel = Math.max(ringCount * 16, 1);
        int availableA = aspectsInNetwork.getOrDefault(compA, 0);
        int availableB = aspectsInNetwork.getOrDefault(compB, 0);
        parallel = Math.min(parallel, Math.min(availableA, availableB));

        boolean takenA = provider.takeFromContainer(compA, parallel);
        boolean takenB = provider.takeFromContainer(compB, parallel);
        if (!takenA || !takenB) {
            return SimpleCheckRecipeResult.ofFailure("Failed to consume aspects from Infusion Provider.");
        }

        startSynthesis(resultAspect, parallel, SECOND_IN_TICKS * computeAspectSynthesisTime(resultAspect), true);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    // ========================================================
    // Mode 1: Crystal Essence Mode (formerly Normal)
    // ========================================================
    private @NotNull CheckRecipeResult processCrystalEssenceMode() {
        ArrayList<ItemStack> inputs = getStoredInputs();
        if (inputs.size() < 2) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        Aspect resultAspect = null;
        ItemStack first = null, second = null;
        int availableFirst = 0, availableSecond = 0;
        outer: for (int i = 0; i < inputs.size(); i++) {
            ItemStack aStack = inputs.get(i);
            if (aStack == null) continue;
            Aspect aspectA = readAspectFromCrystal(aStack);
            if (aspectA == null) continue;
            for (int j = i + 1; j < inputs.size(); j++) {
                ItemStack bStack = inputs.get(j);
                if (bStack == null) continue;
                Aspect aspectB = readAspectFromCrystal(bStack);
                if (aspectB == null) continue;
                Aspect candidate = findCombinedAspectCached(aspectA, aspectB);
                if (candidate != null) {
                    resultAspect = candidate;
                    first = aStack;
                    second = bStack;
                    availableFirst = aStack.stackSize;
                    availableSecond = bStack.stackSize;
                    break outer;
                }
            }
        }
        if (resultAspect == null) return CheckRecipeResultRegistry.NO_RECIPE;

        int parallel = Math.max(ringCount * 6, 1);
        parallel = Math.min(parallel, Math.min(availableFirst, availableSecond));

        // Consume input items
        int remainingFirst = parallel;
        int remainingSecond = parallel;
        for (int i = 0; i < inputs.size(); i++) {
            ItemStack s = inputs.get(i);
            if (s == null) continue;
            if (s == first && remainingFirst > 0) {
                int deduct = Math.min(s.stackSize, remainingFirst);
                s.stackSize -= deduct;
                remainingFirst -= deduct;
                if (s.stackSize <= 0) inputs.set(i, null);
            }
            if (s == second && remainingSecond > 0) {
                int deduct = Math.min(s.stackSize, remainingSecond);
                s.stackSize -= deduct;
                remainingSecond -= deduct;
                if (s.stackSize <= 0) inputs.set(i, null);
            }
            if (remainingFirst <= 0 && remainingSecond <= 0) break;
        }

        ItemStack outputCrystal = createCrystal(resultAspect, parallel);
        this.mOutputItems = new ItemStack[] { outputCrystal };
        startSynthesis(
            resultAspect,
            parallel,
            (int) (computeAspectSynthesisTime(resultAspect) * SECOND_IN_TICKS
                / (ringCount == 0 ? 1 : Math.pow(1.2, ringCount))),
            false);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    // ========================================================
    // Mode 0: Passive Mode (formerly Old Mode)
    // ========================================================
    private @NotNull CheckRecipeResult processPassiveMode() {
        if (mAspectSelectionBits == 0) {
            return SimpleCheckRecipeResult.ofFailure("No aspects selected in GUI.");
        }

        List<Aspect> selectedAspects = new ArrayList<>();
        List<Aspect> allCompoundAspects = getAllCompoundAspectsSorted();
        for (int i = 0; i < allCompoundAspects.size(); i++) {
            if ((mAspectSelectionBits >> i & 1L) != 0) {
                selectedAspects.add(allCompoundAspects.get(i));
            }
        }
        if (selectedAspects.isEmpty()) {
            return SimpleCheckRecipeResult.ofFailure("No aspects selected in GUI.");
        }

        int parallel = Math.max(ringCount * 6, 1);

        // Build recipe for ONE unit of each selected aspect (time independent of parallel)
        AspectList outputAspects = new AspectList();
        PriorityQueue<Map.Entry<Integer, AspectList>> PreprocessedAspectMaxHeap = new PriorityQueue<>(
            (entry1, entry2) -> Integer.compare(entry2.getKey(), entry1.getKey()));

        for (Aspect aspect : selectedAspects) {
            outputAspects.add(aspect, 1); // time calculation only for 1 unit
            PreprocessedAspectMaxHeap
                .add(new AbstractMap.SimpleEntry<>(computeAspectLevel(aspect), new AspectList().add(aspect, 1)));
        }

        if (mTileInfusionProvider.isEmpty()) {
            return SimpleCheckRecipeResult.ofFailurePersistOnShutdown("No Infusion Provider found.");
        }

        Map<Aspect, Integer> aspectsInNetwork = new HashMap<>();
        for (Aspect primalAspect : Aspect.getPrimalAspects()) {
            int totalAmount = mTileInfusionProvider.stream()
                .mapToInt(hatch -> (int) hatch.getAspectAmountInNetwork(primalAspect))
                .sum();
            aspectsInNetwork.put(primalAspect, totalAmount);
        }

        AspectList synthesisOrder = new AspectList();
        AspectList consumptionSteps = new AspectList();
        AspectList shortageAspects = new AspectList();
        boolean primalAspectShortage = false;

        while (!PreprocessedAspectMaxHeap.isEmpty()) {
            Map.Entry<Integer, AspectList> entry = PreprocessedAspectMaxHeap.poll();
            AspectList aspectList = entry.getValue();
            Aspect currentAspect = aspectList.getAspects()[0];
            int required = aspectList.getAmount(currentAspect);
            int available = aspectsInNetwork.getOrDefault(currentAspect, 0);
            int remaining = available - required;

            if (remaining >= 0) {
                aspectsInNetwork.put(currentAspect, remaining);
                consumptionSteps.add(currentAspect, required);
            } else {
                aspectsInNetwork.put(currentAspect, 0);
                int deficit = required - available;
                if (available > 0) {
                    consumptionSteps.add(currentAspect, available);
                }
                synthesisOrder.add(currentAspect, deficit);
                if (currentAspect.isPrimal()) {
                    primalAspectShortage = true;
                    shortageAspects.add(currentAspect, deficit);
                    continue;
                }
                for (Aspect component : currentAspect.getComponents()) {
                    PreprocessedAspectMaxHeap
                        .add(new AbstractMap.SimpleEntry<>(deficit, new AspectList().add(component, deficit)));
                }
            }
        }

        if (primalAspectShortage) {
            StringBuilder errorMessage = new StringBuilder("Missing Aspects: ");
            for (Aspect aspect : shortageAspects.getAspects()) {
                errorMessage.append(aspect.getName())
                    .append(shortageAspects.getAmount(aspect))
                    .append(" ");
            }
            return SimpleCheckRecipeResult.ofFailure(
                errorMessage.toString()
                    .trim());
        }

        // Consume from network (only the calculated amount, which is for 1 unit)
        for (Aspect aspect : consumptionSteps.getAspects()) {
            int amount = consumptionSteps.getAmount(aspect);
            for (TileInfusionProvider hatch : mTileInfusionProvider) {
                if (hatch.takeFromContainer(aspect, amount)) break;
            }
        }

        // Compute duration for 1 unit of each requested aspect
        for (int i = 0; i < synthesisOrder.size(); i++) {
            Aspect aspect = synthesisOrder.getAspects()[i];
            int amount = synthesisOrder.getAmount(aspect);
            RECIPE_DURATION += amount * computeAspectSynthesisTime(aspect);
        }

        // Set final outputs with parallel multiplier
        this.mOutputAspects = new AspectList();
        for (Aspect aspect : selectedAspects) {
            this.mOutputAspects.add(aspect, parallel);
        }
        this.mOutputAspectNames = new String[selectedAspects.size()];
        this.mOutputAspectAmounts = new Integer[selectedAspects.size()];
        for (int i = 0; i < selectedAspects.size(); i++) {
            this.mOutputAspectNames[i] = selectedAspects.get(i)
                .getName();
            this.mOutputAspectAmounts[i] = parallel;
        }

        this.mEfficiencyIncrease = 10000;
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(RECIPE_EUT)
            .setEUt(getMaxInputEu())
            .setDuration(
                (int) Math.ceil(SECOND_IN_TICKS * RECIPE_DURATION / (ringCount == 0 ? 1 : Math.pow(1.2, ringCount))))
            .calculate();
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();
        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void startSynthesis(Aspect resultAspect, int parallel, int durationTicks, boolean perfectOC) {
        this.mOutputAspects.add(resultAspect, parallel);
        this.mOutputAspectNames = new String[] { resultAspect.getName() };
        this.mOutputAspectAmounts = new Integer[] { parallel };
        this.mEfficiencyIncrease = 10000;

        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(RECIPE_EUT)
            .setEUt(getMaxInputEu())
            .setDuration(durationTicks);
        if (perfectOC) {
            calculator.setDurationDecreasePerOC(4);
        }
        calculator.calculate();
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();
        this.updateSlots();
    }

    private void ResetOutputs() {
        mOutputAspects.aspects.clear();
        mOutputAspectNames = null;
        mOutputAspectAmounts = null;
    }

    @Override
    protected void outputAfterRecipe() {
        super.outputAfterRecipe();
        fillEssentiaOutputHatch();
    }

    private void fillEssentiaOutputHatch() {
        for (MTEEssentiaOutputHatch outputHatch : this.mEssentiaOutputHatches) {
            for (Map.Entry<Aspect, Integer> entry : this.mOutputAspects.copy().aspects.entrySet()) {
                Aspect aspect = entry.getKey();
                int amount = entry.getValue();
                this.mOutputAspects.remove(aspect, outputHatch.addEssentia(aspect, amount, null));
            }
        }
        this.mOutputAspects.aspects.clear();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (!aBaseMetaTileEntity.isAllowedToWork() && mMaxProgresstime > 0 && !mStopAfterCycle) {
                mStopAfterCycle = true;
                aBaseMetaTileEntity.enableWorking();
            }
            if (mStopAfterCycle && mMaxProgresstime <= 0) {
                mStopAfterCycle = false;
                super.stopMachine();
                ResetOutputs();
                aBaseMetaTileEntity.disableWorking();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void stopMachine() {
        if (this.mMachine && mMaxProgresstime > mProgresstime) {
            mStopAfterCycle = true;
            return;
        }
        super.stopMachine();
        ResetOutputs();
        mStopAfterCycle = false;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public String generateCurrentRecipeInfoString() {
        StringBuffer ret = new StringBuffer(EnumChatFormatting.WHITE + "Progress: ");
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.format((double) mProgresstime / 20, ret);
        ret.append("s / ");
        numberFormat.format((double) mMaxProgresstime / 20, ret);
        ret.append("s (");
        numberFormat.setMinimumFractionDigits(1);
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.format((double) mProgresstime / mMaxProgresstime * 100, ret);
        ret.append("%)\n");
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);

        LongConsumer appendRate = (amount) -> {
            double processPerTick = (double) amount / mMaxProgresstime * 20;
            ret.append(" (");
            if (processPerTick > 1) {
                numberFormat.format(Math.round(processPerTick * 10) / 10.0, ret);
                ret.append("/s)");
            } else {
                numberFormat.format(Math.round(1 / processPerTick * 10) / 10.0, ret);
                ret.append("s/ea)");
            }
        };

        int lines = 0;
        int MAX_LINES = 10;
        if (mOutputAspectNames != null && mOutputAspectAmounts != null
            && mOutputAspectNames.length == mOutputAspectAmounts.length) {
            HashMap<String, Long> nameToAmount = new HashMap<>();
            for (int i = 0; i < mOutputAspectAmounts.length; ++i) {
                nameToAmount.merge(mOutputAspectNames[i], (long) mOutputAspectAmounts[i], Long::sum);
            }
            for (Map.Entry<String, Long> entry : nameToAmount.entrySet()) {
                if (lines >= MAX_LINES) {
                    ret.append("...");
                    return ret.toString();
                }
                lines++;
                ret.append(EnumChatFormatting.AQUA)
                    .append(entry.getKey())
                    .append(EnumChatFormatting.WHITE)
                    .append(" x ")
                    .append(EnumChatFormatting.GOLD);
                numberFormat.format(entry.getValue(), ret);
                ret.append(EnumChatFormatting.WHITE);
                appendRate.accept(entry.getValue());
                ret.append('\n');
            }
        }
        return ret.toString();
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements.widget(
            TextWidget.dynamicString(this::generateCurrentRecipeInfoString)
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(
                    widget -> getErrorDisplayID() == 0 && mOutputAspectNames != null
                        && mOutputAspectNames.length > 0
                        && mOutputAspectAmounts != null
                        && mOutputAspectAmounts.length > 0))
            .widget(
                new FakeSyncWidget.ListSyncer<>(
                    () -> mOutputAspectNames != null ? Arrays.asList(mOutputAspectNames) : Collections.emptyList(),
                    val -> mOutputAspectNames = val.toArray(new String[0]),
                    NetworkUtils::writeStringSafe,
                    NetworkUtils::readStringSafe))
            .widget(
                new FakeSyncWidget.ListSyncer<>(
                    () -> mOutputAspectAmounts != null ? Arrays.asList(mOutputAspectAmounts) : Collections.emptyList(),
                    val -> mOutputAspectAmounts = val.toArray(new Integer[0]),
                    PacketBuffer::writeVarIntToBuffer,
                    PacketBuffer::readVarIntFromBuffer))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> mProgresstime, val -> mProgresstime = val))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> mMaxProgresstime, val -> mMaxProgresstime = val));
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_SkypiercerTwoer_MachineType
        // #en_US Essentia Synthesizer
        // #zh_CN 源质合成者
        tt.addMachineType(TextEnums.tr("Tooltip_SkypiercerTwoer_MachineType"))
            // #tr Tooltip_SkypiercerTower_00
            // #en_US Controller block for the SkypiercerTower
            // #zh_CN 穿云尖塔的控制器方块
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_00"))
            // #tr Tooltip_SkypiercerTower_01
            // #en_US §9Wir müssen wissen. Wir werden wissen.
            // #zh_CN §9我们必须知道，我们必将知道.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_01"))
            // #tr Tooltip_SkypiercerTower_02
            // # Thaumaturgical research confirms: Essentia degradation occurs spontaneously. while recombination demands human intervention to overcome inherent resistance.
            // #zh_CN 神秘学研究表明:源质天然倾向于分解,而重组需要人为干预以克服内阻.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_02"))
            // #tr Tooltip_SkypiercerTower_03
            // #en_US Synthesizes compound aspects. Requires at least 1A EV. Processing time depends on aspect tier: primal aspects are tier 0; a compound aspect's tier is the max tier of its components plus 1.
            // #zh_CN 合成复合要素,至少1A EV ,其时间取决于要素的等级有关,初等要素为0级,父要素等级为子要素等级较大者+1
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_03"))
            // #tr Tooltip_SkypiercerTower_05
            // #en_US A tier 1 aspect takes 2 seconds. Each additional tier multiplies the time by 3/2, rounded down.
            // #zh_CN 初等要素合成的要素需要2s,每增加一级时间变为原先的3/2倍,向下取整.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_05"))
            // #tr Tooltip_SkypiercerTower_06
            // #en_US Essentia Mode: Supply essentia via Infusion Provider. Automatically matches two combinable aspects from the network, outputs through Essentia Output Hatch. Each ring adds 16 parallels and enables perfect overclocking.
            // #zh_CN 源质模式:由注魔供应器提供源质,自动匹配可合成的两种源质,源质输出仓输出,每个环部将增加16并行,并开启无损超频.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_06"))
            // #tr Tooltip_SkypiercerTower_07
            // #en_US Passive Mode: Select aspects manually for continuous synthesis. Each ring adds 6 parallels and divides time by 1.2^rings (≈1/10 time at 13 rings).
            // #zh_CN 被动模式:自行选择要素,将一直合成,每个环部增加6并行,且时间÷环数^1.2(约13环即可将时间降为原先的1/10).
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_07"))
            // #tr Tooltip_SkypiercerTower_08
            // #en_US Crystal Essence Mode: Input crystals via Input Bus, output through Output Bus. Bonuses same as Passive Mode.
            // #zh_CN 晶化源质模式:由输入总线输入,输出总线输出,各加成等与被动模式一致,此模式已不太推荐使用.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_08"))
            // #tr Tooltip_SkypiercerTower_09
            // #en_US Note: Non‑passive modes require blocking to ensure only one type of aspect is synthesized at a time; otherwise it may interfere or even jam (also does not support color input).
            // #zh_CN 注意,非被动模式下均需要阻挡,保证一次只合成一种要素,否则会相互干扰,甚至会卡住(另外不支持染色仓).
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_09"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(11, 10, 23, true)
            .addController(textFrontCenter)

            // #tr Tooltip_SkypiercerTower_InputBusInfo
            // #en_US Replace any chemically inert machine casing
            // #zh_CN 任何舱室替换化学惰性方块
            .addInputBus(TextEnums.tr("Tooltip_SkypiercerTower_InputBusInfo"))
            // #tr Tooltip_SkypiercerTower_EnergyHatch
            // #en_US Replace any chemically inert machine casing
            // #zh_CN 任何舱室替换化学惰性方块
            .addOutputBus(TextEnums.tr("Tooltip_SkypiercerTower_InputBusInfo"))
            // #tr Tooltip_SkypiercerTower_EnergyHatch
            // #en_US Replace any chemically inert machine casing
            // #zh_CN 任何舱室替换化学惰性方块
            .addEnergyHatch(TextEnums.tr("Tooltip_SkypiercerTower_EnergyHatch"))
            // #tr Tooltip_SkypiercerTower_InfusionProvider
            // #en_US Replace any chemically inert machine casing.Only one is allowed.
            // #zh_CN 任何舱室替换化学惰性方块,只允许有一个.
            // #tr Tooltip.InfusionProvider
            // # Infusion Provider
            // #zh_CN 注魔供应器
            .addOtherStructurePart(TextEnums.tr("Tooltip.InfusionProvider"), TextEnums.tr("Tooltip_SkypiercerTower_InfusionProvider"))
            // #tr Tooltip_SkypiercerTower_EssentiaOutputHatch
            // #en_US Replace any chemically inert machine casing
            // #zh_CN 任何舱室替换化学惰性方块
            // #tr Tooltip.EssentiaOutputHatch
            // # Essentia Output Hatch
            // #zh_CN 源质输出仓
            .addOtherStructurePart(TextEnums.tr("Tooltip.EssentiaOutputHatch"), TextEnums.tr("Tooltip_SkypiercerTower_EssentiaOutputHatch"))
            .toolTipFinisher(ModName);
        // spotless:on
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_SkypiercerTower(this.mName);
    }

    public static List<Aspect> getAllCompoundAspectsSorted() {
        List<Aspect> aspects = new ArrayList<>(Aspect.aspects.values());
        aspects.removeIf(Aspect::isPrimal);
        aspects.sort(Comparator.comparingInt(TST_SkypiercerTower::computeAspectLevelSafe));
        return aspects;
    }

    private static int computeAspectLevelSafe(Aspect a) {
        try {
            return computeAspectLevel(a);
        } catch (Exception e) {
            return 999;
        }
    }
}
