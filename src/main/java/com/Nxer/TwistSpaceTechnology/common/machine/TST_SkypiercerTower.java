package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_1;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_4;
import static com.Nxer.TwistSpaceTechnology.config.Config.Parallel_PerRing_SkypiercerTower;
import static com.Nxer.TwistSpaceTechnology.util.AspectLevelCalculator.BASE_DURATION;
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
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.LongConsumer;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
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
import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import journeymap.shadow.org.jetbrains.annotations.NotNull;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileCrucible;
import thaumcraft.common.tiles.TileNitor;
import thaumicenergistics.common.blocks.BlockEnum;
import thaumicenergistics.common.tiles.TileInfusionProvider;
import vazkii.botania.common.block.ModBlocks;

public class TST_SkypiercerTower extends MTETooltipMultiBlockBaseEM implements IConstructable, ISurvivalConstructable {

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

    private static int RECIPE_DURATION = 32;
    private static final int RECIPE_EUT = 1920;
    private static final int SECOND_IN_TICKS = 20;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_RINGS = "rings";
    private IStructureDefinition<TST_SkypiercerTower> multiDefinition = null;
    private static int MachineMode = 0;

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();
        mEssentiaOutputHatches.clear();
    }

    private final int Main_horizontalOffSet = 7;
    private final int Main_verticalOffSet = 17;
    private final int Main_depthOffSet = 1;

    // x-offset = 7 , y-of = 19 , z-of = 1
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
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
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
                            .dot(1)
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

    // Originally wanted to write some features, but a little difficult, later on
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
                Main_verticalOffSet + Rings_verticalOffSet * i + Rings_verticalOffSet, // 19 + 5n
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
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.mParallel = 0;
        this.ringCount = 0;
        this.mTileInfusionProvider.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, Main_horizontalOffSet, Main_verticalOffSet, Main_depthOffSet)) {
            return false;
        }

        while (checkPiece(
            STRUCTURE_PIECE_RINGS,
            Rings_horizontalOffSet,
            Main_verticalOffSet + Rings_verticalOffSet * ringCount + Rings_verticalOffSet,
            Rings_depthOffSet)) {
            this.ringCount++;
        }

        this.mParallel = (int) Math.min((long) this.ringCount * Parallel_PerRing_SkypiercerTower, Integer.MAX_VALUE);
        // FMLLog.info("[SkypiercerTower] Parallel: %f | Rings: %d", mParallel, ringCount);
        return true;
    }

    private boolean addEssentiaOutputHatchToMachineList(MTEEssentiaOutputHatch aTileEntity) {
        if (aTileEntity instanceof MTEEssentiaOutputHatch) {
            return this.mEssentiaOutputHatches.add(aTileEntity);
        }
        return false;
    }

    private static Aspect getAspectByName(String name) {
        for (Aspect aspect : Aspect.aspects.values()) {
            if (aspect.getName()
                .equalsIgnoreCase(name)) {
                return aspect;
            }
        }
        return null;
    }

    // wtf, why is AspectList a LinkHashMap that I find out after I write it, so there's no way to check the aspects in
    // full order.

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.SkypiercerTower;
    }

    // createProcessingLogic hava some bizarre problems which I can't solve.
    // So, in reality, recipesPool doesn't work.
    @Override
    protected ProcessingLogic createProcessingLogic() {
        return super.createProcessingLogic();
    };

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        RECIPE_DURATION = 0;
        ResetOutputs();
        ArrayList<ItemStack> tItemsList = getStoredInputs();

        // === [CHALLENGE MODE] ===
        MachineMode = 0;
        if (getControllerSlot() != null && getControllerSlot().getDisplayName() != null) {
            String name = getControllerSlot().getDisplayName()
                .toUpperCase();
            if (name.contains("NORMAL")) {
                MachineMode = 1;
            }
            if (name.contains("CHALLENGE")) {
                MachineMode = 2;
            }
        }

        if (MachineMode == 2) {
            if (mTileInfusionProvider.isEmpty()) {
                return SimpleCheckRecipeResult.ofFailurePersistOnShutdown("No Infusion Provider found.");
            }
            // I can't stand it anymore. Having too many providers will make the code complicated unnecessarily . Here,
            // only one is allowed to simplify the code.
            if (mTileInfusionProvider.size() > 1) {
                return SimpleCheckRecipeResult
                    .ofFailurePersistOnShutdown("Multiple Infusion Providers found. Please connect only one.");
            }
            TileInfusionProvider provider = mTileInfusionProvider.get(0);
            // Count all Aspects and their quantities in the supply.
            Map<Aspect, Integer> aspectsInNetwork = new HashMap<>();
            for (Aspect aspect : Aspect.aspects.values()) {
                int amount = (int) provider.getAspectAmountInNetwork(aspect);
                if (amount > 0) {
                    aspectsInNetwork.put(aspect, amount);
                }
            }

            // Find any Aspects that can be combined.
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

            if (resultAspect == null) {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            // Determine parallel synthesis count
            int parallel = Math.max(ringCount * 16, 1);
            int availableA = aspectsInNetwork.getOrDefault(compA, 0);
            int availableB = aspectsInNetwork.getOrDefault(compB, 0);
            parallel = Math.min(parallel, Math.min(availableA, availableB));

            // Consume aspects
            boolean takenA = provider.takeFromContainer(compA, parallel);
            boolean takenB = provider.takeFromContainer(compB, parallel);

            if (!takenA || !takenB) {
                return SimpleCheckRecipeResult.ofFailure("Failed to consume aspects from Infusion Provider.");
            }

            // Prepare output
            AspectList outputAspects = new AspectList().add(resultAspect, parallel);
            this.mOutputAspects.add(outputAspects);
            this.mOutputAspectNames = new String[] { resultAspect.getName() };
            this.mOutputAspectAmounts = new Integer[] { parallel };

            // Calculate processing times and power
            RECIPE_DURATION = BASE_DURATION * computeAspectSynthesisTime(resultAspect);
            this.mEfficiencyIncrease = 10000;

            OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(RECIPE_EUT)
                .setEUt(getMaxInputEu())
                .setDuration(SECOND_IN_TICKS * RECIPE_DURATION)
                .setDurationDecreasePerOC(4)
                .calculate();

            useLongPower = true;
            lEUt = -calculator.getConsumption();
            mMaxProgresstime = calculator.getDuration();

            this.updateSlots();
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        // === [NORMAL MODE] ===
        if (MachineMode == 1) {
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

            if (resultAspect == null) {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            int parallel = Math.max(ringCount * 16, 1);
            parallel = Math.min(parallel, Math.min(availableFirst, availableSecond));

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

            AspectList outputAspects = new AspectList().add(resultAspect, parallel);
            this.mOutputAspects.add(outputAspects);
            this.mOutputAspectNames = new String[] { resultAspect.getName() };
            this.mOutputAspectAmounts = new Integer[] { parallel };

            this.updateSlots();

            RECIPE_DURATION = BASE_DURATION * computeAspectSynthesisTime(resultAspect);
            this.mEfficiencyIncrease = 10000;

            OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(RECIPE_EUT)
                .setEUt(getMaxInputEu())
                .setDuration(
                    (int) (RECIPE_DURATION * SECOND_IN_TICKS / (ringCount == 0 ? 1 : Math.pow(1.2, ringCount))))
                .calculate();

            useLongPower = true;
            lEUt = -calculator.getConsumption();
            mMaxProgresstime = calculator.getDuration();

            this.updateSlots();
            return CheckRecipeResultRegistry.SUCCESSFUL;

        }

        // === [OLD MODE] ===
        if (tItemsList.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        // Max-heap is used to store preprocessing aspects0
        AspectList outputAspects = new AspectList();
        PriorityQueue<Map.Entry<Integer, AspectList>> PreprocessedAspectMaxHeap = new PriorityQueue<>(
            (entry1, entry2) -> Integer.compare(entry2.getKey(), entry1.getKey()));

        // Add all the requested aspects
        for (ItemStack itemStack : tItemsList) {
            String localizedName = itemStack.getDisplayName()
                .toUpperCase();

            String[] parts = localizedName.split("\\+");

            for (String part : parts) {
                String aspectName = part.replaceAll("[^A-Za-z]", "");
                if (aspectName.isEmpty()) {
                    return SimpleCheckRecipeResult.ofFailure(
                        "Invalid request [" + localizedName + "] Couldn't find aspect name in part [" + part + "]");
                }

                String literalAmount = part.replaceAll("[^0-9]", "");
                if (literalAmount.isEmpty()) {
                    return SimpleCheckRecipeResult.ofFailure(
                        "Invalid request [" + localizedName + "] Couldn't find amount in part [" + literalAmount + "]");
                }

                Aspect aspect = getAspectByName(aspectName);
                if (aspect == null) {
                    return SimpleCheckRecipeResult.ofFailure("Unknown aspect name: " + aspectName);
                }

                int amount = Integer.parseInt(literalAmount) * itemStack.stackSize;
                outputAspects.add(aspect, amount);
                PreprocessedAspectMaxHeap.add(
                    new AbstractMap.SimpleEntry<>(computeAspectLevel(aspect), new AspectList().add(aspect, amount)));
            }
        }

        // Check if the Infusion Provider exists
        if (mTileInfusionProvider.isEmpty()) {
            return SimpleCheckRecipeResult.ofFailurePersistOnShutdown("No Infusion Provider found.");
        }

        // Get a copy of the primal aspect within the network
        Map<Aspect, Integer> aspectsInNetwork = new HashMap<>();
        for (Aspect primalAspect : Aspect.getPrimalAspects()) {
            int totalAmount = mTileInfusionProvider.stream()
                .mapToInt(hatch -> (int) hatch.getAspectAmountInNetwork(primalAspect))
                .sum();
            aspectsInNetwork.put(primalAspect, totalAmount);
        }

        // SynthesisOrder is stored for computing the processing time
        // consumptionSteps to eliminate the intrinsic Aspects of the network
        // shortageAspects count the cases where the Primal Aspect is insufficient
        AspectList synthesisOrder = new AspectList();
        AspectList consumptionSteps = new AspectList();
        AspectList shortageAspects = new AspectList();
        Boolean primalAspectShortage = false;

        // The synthesis process is simulated and the Aspect is calculated to be sufficient
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

        // Determine whether the network Aspect are sufficient
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

        // Consume network aspect correctly
        for (Aspect aspect : consumptionSteps.getAspects()) {
            int amount = consumptionSteps.getAmount(aspect);
            for (TileInfusionProvider hatch : mTileInfusionProvider) {
                if (hatch.takeFromContainer(aspect, amount)) {
                    break;
                }
            }
        }

        // Calculate the original machining time
        for (int i = 0; i < synthesisOrder.size(); i++) {
            Aspect aspect = synthesisOrder.getAspects()[i];
            int amount = synthesisOrder.getAmount(aspect);
            int aspectLevel = computeAspectLevel(aspect);
            RECIPE_DURATION += amount * BASE_DURATION * aspectLevel;
        }

        // Aspect output and state quantity restoration
        this.mOutputAspects.add(outputAspects);
        this.mOutputAspectNames = new String[outputAspects.aspects.size()];
        this.mOutputAspectAmounts = new Integer[outputAspects.aspects.size()];
        int i = 0;
        for (Map.Entry<Aspect, Integer> outputAspect : outputAspects.aspects.entrySet()) {
            this.mOutputAspectNames[i] = outputAspect.getKey()
                .getName();
            this.mOutputAspectAmounts[i] = outputAspect.getValue();
            ++i;
        }

        consumptionSteps.aspects.clear();
        synthesisOrder.aspects.clear();
        PreprocessedAspectMaxHeap.clear();
        aspectsInNetwork.clear();
        outputAspects.aspects.clear();

        this.mEfficiencyIncrease = 10000;

        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(RECIPE_EUT)
            .setEUt(getMaxInputEu())
            .setDuration((int) Math.ceil(SECOND_IN_TICKS * RECIPE_DURATION / (mParallel != 0 ? mParallel : 1)))
            .setDurationDecreasePerOC(4)
            .calculate();

        // The time coefficient of 20 comes from: Base synthesis time = 1 second (set 10)
        // ×2 adjustment for unexpected EV overclocking effects halving the duration.This serves as a compensation
        // factor.
        useLongPower = true;
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();

        this.updateSlots();

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void ResetOutputs() {
        mOutputAspects.aspects.clear();
        mOutputAspectNames = null;
        mOutputAspectAmounts = null;
    }

    @Override
    protected void addClassicOutputs_EM() {
        super.addClassicOutputs_EM();
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
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        ResetOutputs();
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return super.onRunningTick(aStack);
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
            int outputAspectTypesAmounts = mOutputAspectAmounts.length;
            HashMap<String, Long> nameToAmount = new HashMap<>();
            for (int i = 0; i < outputAspectTypesAmounts; ++i) {
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

        // Custoom widget
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
            // # Thaumaturgical research confirms: Essentia(Hydration Aspect) degradation occurs spontaneously. while recombination demands human intervention to overcome inherent resistance.
            // #zh_CN 神秘学研究表明:源质(水化要素)天然倾向于分解,而重组需要人为干预以克服内阻.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_02"))
            // #tr Tooltip_SkypiercerTower_03
            // #en_US Synthesizes aspects from primal aspects. At 1A EV, an aspect takes its tier amount of seconds to synthesize.
            // #zh_CN 由初等要素合成复合要素,至少1A EV,一个要素合成最少需要2s.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_03"))
            // #tr Tooltip_SkypiercerTower_05
            // #en_US This machine is controlled using renamed items following this format 'AspectValue(+AspectValue+...)'. Where 'Aspect' is the aspect and 'Value' is the number requested per cycle. The '+' is an optional way to request multiple aspects from a single item.
            // #zh_CN 这台机器采用重命名的物体驱动,格式'AspectValue(+AspectValue+...)'即可制取Value数目的Aspect,而+可以继续书写多个请求要素.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_05"))
            // #tr Tooltip_SkypiercerTower_06
            // #en_US Yes, the above is the old mode. You can still use it, but we recommend using the new mode instead.Specifically, it is necessary to insert the renamed paper into the controller.
            // #zh_CN 是的,上面是旧模式,你仍然可以使用它,但是我们推荐使用新模式.具体来说需要在控制器内放入重命名的纸
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_06"))
            // #tr Tooltip_SkypiercerTower_07
            // #en_US Rename the paper 'NORMAL'.it is the Item Mode. Two CrystalEssences enter the input bus and be synthesized, then pop out onto the output bus. Each ring segment increases by 4 parallels and increases processing speed by 120%, with multiplicative stacking.
            // #zh_CN 重命名为'Normal',此时为普通模式,两个晶化源质从输入总线进入合成后弹出至输出总线,每个环部增加4并行,并且处理速度提升至120%,叠乘计算.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_07"))
            // #tr Tooltip_SkypiercerTower_08
            // #en_US Rename the paper 'Challenge'.it is the Challenge Mode.The Infusion Provider supplies essentia, which is output through the Essentia Output Bus. Each ring adds 16 parallel operations and use prefect overclocks.
            // #zh_CN 重命名为'Challenge',此时为挑战模式,由注魔供应器提供源质,源质输出仓输出,每个环部将增加16并行,并开启无损超频.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_08"))
            // #tr Tooltip_SkypiercerTower_09
            // #en_US Min voltage 1A EV, standard overclocks
            // #zh_CN 至少是EV电压,使用4/2超频,即每提升一次电压加工时间减半
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_09"))
            // #tr Tooltip_SkypiercerTower_010
            // #en_US If you are really unsure about how to complete the challenge without using the input/output bus to achieve higher efficiency, you can refer to the Thaumonomicon for hints.
            // #zh_CN 如果你实在不清楚如何在不使用输入输出总线的情况下完成挑战以获取更高的产能,可以翻看魔导手册以获取提示.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_010"))
            // #tr Tooltip_SkypiercerTower_011
            // #en_US Finally, whether bus or priveder, the blocking mode should be enabled, which means only one Essence should be synthesized at a time
            // #zh_CN 最后无论是输入总线或者注魔供应器,都应该开启阻挡模式,也就是一次只进行一种源质的合成
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_011"))
            // #tr Tooltip_SkypiercerTower_012
            // #en_US To avoid interference in the synthesis or other unexpected situations caused by the code (These processing logic are handwritten, so they are not as complete as the original code. For instance, they do not support color storage and do not support parallel processing across formulas..).
            // #zh_CN 以避免合成干扰或者因为代码而出现的额外情况(这些处理逻辑是手写的,因此不像原版的代码健壮,譬如不支持染色仓,也没有跨配方并行).
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_012"))
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

    @Override
    public final boolean shouldCheckMaintenance() {
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

}
