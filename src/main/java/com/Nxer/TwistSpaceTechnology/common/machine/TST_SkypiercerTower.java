package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_1;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BlockArcane_4;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_IndustrialAlchemyTower.Essentia_InsentiaL;
import static com.Nxer.TwistSpaceTechnology.config.Config.Parallel_PerRing_SkypiercerTower;
import static com.Nxer.TwistSpaceTechnology.util.AspectLevelCalculator.computeAspectLevel;
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
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import emt.tile.TileElectricCloud;
import goodgenerator.blocks.tileEntity.MTEEssentiaOutputHatch;
import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
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
    public AspectList mOutputAspects = new AspectList();
    protected double mParallel = 0;

    private int ringCount = 0;

    private static int RECIPE_DURATION = 32;
    private static final int RECIPE_EUT = 1920;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_RINGS = "rings";
    private IStructureDefinition<TST_SkypiercerTower> multiDefinition = null;

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
                                gregtech.api.enums.HatchElement.OutputBus)
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
                .addElement('M', ofBlockAnyMeta(Blocks.beacon, 1))
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

        built[0] = survivialBuildPiece(
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
            built[i + 1] = survivialBuildPiece(
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
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        RECIPE_DURATION = 0;
        // Read the paper and cut it
        ArrayList<ItemStack> tItemsList = getStoredInputs();
        if (tItemsList.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;
        ItemStack itemStack = tItemsList.get(0);
        String localizedName = itemStack.getDisplayName()
            .toUpperCase();
        String[] parts = localizedName.split("\\+");
        // max-heap is used to store preprocessing aspects0
        PriorityQueue<Map.Entry<Integer, AspectList>> PreprocessedAspectMaxHeap = new PriorityQueue<>(
            (entry1, entry2) -> Integer.compare(entry2.getKey(), entry1.getKey()));

        AspectList outputAspects = new AspectList();
        // output a item named completed for easy automation.
        ItemStack outputItem = tItemsList.get(0)
            .copy();
        outputItem.stackSize = 1;
        outputItem.setStackDisplayName("Completed");
        this.mOutputItems = new ItemStack[] { outputItem };

        for (String part : parts) {
            String aspectName = part.replaceAll("[^A-Za-z]", "");
            int amount = Integer.parseInt(part.replaceAll("[^0-9]", ""));
            Aspect aspect = getAspectByName(aspectName);
            if (aspect != null) {
                outputAspects.add(aspect, amount);
                PreprocessedAspectMaxHeap.add(
                    new AbstractMap.SimpleEntry<>(computeAspectLevel(aspect), new AspectList().add(aspect, amount)));
            } else {
                return SimpleCheckRecipeResult.ofFailure("Unknown aspect name: " + aspectName);
            }
        }
        // Check the InfusionProvider
        if (mTileInfusionProvider.isEmpty()) return Essentia_InsentiaL;

        // Get a copy of the Aspect within the network
        Map<Aspect, Integer> simulatedNetwork = new HashMap<>();
        for (Aspect aspect : Aspect.aspects.values()) {
            int totalAmount = mTileInfusionProvider.stream()
                .mapToInt(hatch -> (int) hatch.getAspectAmountInNetwork(aspect))
                .sum();
            simulatedNetwork.put(aspect, totalAmount);
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
            int available = simulatedNetwork.getOrDefault(currentAspect, 0);
            int remaining = available - required;
            if (remaining >= 0) {
                simulatedNetwork.put(currentAspect, remaining);
                consumptionSteps.add(currentAspect, required);
            } else {
                simulatedNetwork.put(currentAspect, 0);
                int deficit = required - available;
                if (available > 0) consumptionSteps.add(currentAspect, available);
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
            for (Aspect aspect : shortageAspects.getAspects()) errorMessage.append(aspect.getName())
                .append(shortageAspects.getAmount(aspect))
                .append(" ");
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
            RECIPE_DURATION += amount * (int) Math.pow(2, aspectLevel);
        }

        // Aspect output and state quantity restoration
        this.mOutputAspects.add(outputAspects);
        consumptionSteps.aspects.clear();
        synthesisOrder.aspects.clear();
        PreprocessedAspectMaxHeap.clear();
        simulatedNetwork.clear();
        outputAspects.aspects.clear();
        itemStack.stackSize--;
        this.mEfficiencyIncrease = 10000;
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(RECIPE_EUT)
            .setEUt(getMaxInputEu())
            .setDuration((int) Math.ceil(20 * RECIPE_DURATION / (mParallel != 0 ? mParallel : 1)))
            .setDurationDecreasePerOC(2)
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
        this.mOutputAspects.aspects.clear();
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return super.onRunningTick(aStack);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_SkypiercerTwoer_MachineType
        // # Essentia Synthesizer
        // #zh_CN 源质合成者
        tt.addMachineType(TextEnums.tr("Tooltip_SkypiercerTwoer_MachineType"))
            // #tr Tooltip_SkypiercerTower_00
            // # Controller block for the SkypiercerTower
            // #zh_CN 穿云尖塔的控制器方块
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_00"))
            // #tr Tooltip_SkypiercerTower_01
            // # §9Wir müssen wissen. Wir werden wissen.
            // #zh_CN §9我们必须知道，我们必将知道.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_01"))
            // #tr Tooltip_SkypiercerTower_02
            // # Thaumaturgical research confirms: Essentia(Hydration Aspect) degradation occurs spontaneously. while recombination demands human intervention to overcome inherent resistance.
            // #zh_CN 神秘学研究表明:源质(水化要素)天然倾向于分解,而重组需要人为干预以克服内阻.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_02"))
            // #tr Tooltip_SkypiercerTower_03
            // # Each aspect is assigned a level, and the synthesis time doubles for each level increase. The exact calculations are somewhat complex, check the Thaumonomicon for details.
            // #zh_CN 每一个要素被赋予了等级,等级每提升一级,合成时间翻倍,具体计算较为复杂,请查看魔导手册.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_03"))
            // #tr Tooltip_SkypiercerTower_04
            // # This machine is driven by command papers. Rename the paper on the anvil to 'AspectValue+...' format to craft the corresponding number of Aspects.
            // #zh_CN 这台机器使用命令纸驱动,将纸在铁砧上重新命名为AspectValue+...的格式即可制取Value数目的Aspect.
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_04"))
            // #tr Tooltip_SkypiercerTower_05
            // # Each ring segment increases parallel processing by 16 units. However, as this machine operates on a single-task principle, the corresponding parallel capacity is converted into a speed multiplier,resulting in a 1600% performance enhancement.
            // #zh_CN 每个环部增加16并行，但由于这台机器是单任务系统，对应的并行能力将转化为速度加成，即提供1600%的速度提升。
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_05"))
            // #tr Tooltip_SkypiercerTower_06
            // # At least EV voltage, use 4/2 overclocking, that is, the processing time is halved for each voltage increase
            // #zh_CN 至少是EV电压,使用4/2超频,即每提升一次电压加工时间减半
            .addInfo(TextEnums.tr("Tooltip_SkypiercerTower_06"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(11, 10, 23, true)
            .addController(textFrontCenter)

            // #tr Tooltip_SkypiercerTower_HatchBusInfo
            // # Replace chemically inert machine casing in any cabin
            // #zh_CN 任何舱室替换化学惰性方块
            .addOutputHatch(TextEnums.tr("Tooltip_SkypiercerTower_HatchBusInfo"))
            // #tr Tooltip_SkypiercerTower_HatchBusInfo
            // # Replace Magic mechanical blocks in any cabin
            // #zh_CN 任何舱室替换化学惰性方块
            // spotless:on
            .addEnergyHatch(TextEnums.tr("Tooltip_SkypiercerTower_EnergyHatch"))
            .toolTipFinisher(ModName);
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
