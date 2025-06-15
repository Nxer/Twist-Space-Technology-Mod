package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontCenter;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofSpecificTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Materials.Steel;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.blocks.tileEntity.MTEEssentiaOutputHatch;
import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.config.ConfigBlocks;

public class TST_PrimordialDisjunctus extends MTETooltipMultiBlockBaseEM
    implements IConstructable, ISurvivalConstructable {

    public TST_PrimordialDisjunctus(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private static final int CASING_INDEX = 1536;
    private final ArrayList<MTEEssentiaOutputHatch> mEssentiaOutputHatches = new ArrayList<>();
    public AspectList mOutputAspects = new AspectList();

    protected int mCasing = 0;
    protected double mParallel = 0;
    private int pTier = 0;
    protected int primalAspectsGenerated = 0;
    protected int nodeIncrease = 0;
    protected int nodePurificationEfficiency = 0;
    private static final int SECOND_IN_TICKS = 20;
    private static final int RECIPE_EUT = 1920;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private IStructureDefinition<TST_PrimordialDisjunctus> multiDefinition = null;

    // Config values
    private static final int STANDARD_RECIPE_DURATION = ValueEnum.BaseRecipeDuration_PrimordialDisjunctus;
    private static final int STANDARD_DIFFUSION_CELL_PARALLEL = ValueEnum.DiffusionCellParallel_PrimordialDisjunctus;
    private static final int STANDARD_PRIMAL_ASPECTS_PER_PARALLEL = ValueEnum.PrimalAspectsPerParallel_PrimordialDisjunctus;
    private static final int STANDARD_PURIFICATION_GAIN_MULTIPLIER = ValueEnum.PurificationGainMultiplier_PrimordialDisjunctus;
    private static final int STANDARD_PURIFICATION_REDUCTION = ValueEnum.PurificationReduction_PrimordialDisjunctus;
    private static final int STANDARD_BOOST_MULTIPLIER = ValueEnum.BoostMultiplier_PrimordialDisjunctus;
    private static final int STANDARD_BOOST_GAIN_MULTIPLIER = ValueEnum.BoostGainMultiplier_PrimordialDisjunctus;
    private static final int STANDARD_BOOST_REDUCTION = ValueEnum.BoostReduction_PrimordialDisjunctus;

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();
        mEssentiaOutputHatches.clear();
    }

    private final XSTR xstr = new XSTR();
    // length=width=15 height = 17 x-offset = 7 y-offset = 16 z-offset = -1
    private static final String[][] shapePrimordialDisjunctus = new String[][] {
        { "               ", "               ", "ABA         ABA", "ABA         ABA", "BCB         BCB",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "   DDDDDDDDD   ", "   DBBBEBBBD   ",
            "   DBFBEBFBD   ", "   DBBBEBBBD   ", "   DEEEKEEED   ", "   DBBBEBBBD   ", "   DBFBEBFBD   ",
            "   DBBBEBBBD   ", "   DDDDDDDDD   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", " J           J ", "   GGGGGGGGG   ", "   G       G   ",
            "   G       G   ", "   G       G   ", "   G   H   G   ", "   G       G   ", "   G       G   ",
            "   G       G   ", "   GGGGGGGGG   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "J J         J J", " J GGGGGGGGG J ", "   G   I   G   ",
            "   G   I   G   ", "   G   I   G   ", "   GIIIHIIIG   ", "   G   I   G   ", "   G   I   G   ",
            "   G   I   G   ", "   GGGGGGGGG   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "J JGGGGGGGGGJ J", " J G       G J ",
            "   G       G   ", "   G       G   ", "   G   H   G   ", "   G       G   ", "   G       G   ",
            "   G       G   ", "   GGGGGGGGG   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "   GGGGGGGGG   ", "J JG       GJ J",
            " J G I   I G J ", "   G  I I  G   ", "   G   H   G   ", "   G  I I  G   ", "   G I   I G   ",
            "   G       G   ", "   GGGGGGGGG   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "   GGGGGGGGG   ", "   G       G   ",
            "J JG       GJ J", " J G       G J ", "   G   H   G   ", "   G       G   ", "   G       G   ",
            "   G       G   ", "   GGGGGGGGG   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "   GGGGGGGGG   ", "   G       G   ",
            "   G   I   G   ", "J JG   I   GJ J", " J G IIHII G J ", "   G   I   G   ", "   G   I   G   ",
            "   G       G   ", "   GGGGGGGGG   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "   GGGGGGGGG   ", "   G       G   ",
            "   G       G   ", "   G       G   ", "J JG   H   GJ J", " J G       G J ", "   G       G   ",
            "   G       G   ", "   GGGGGGGGG   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "   GGGGGGGGG   ", "   G       G   ",
            "   G       G   ", "   G  I I  G   ", "   G   H   G   ", "J JG  I I  GJ J", " J G       G J ",
            "   G       G   ", "   GGGGGGGGG   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "   GGGGGGGGG   ", "   G       G   ",
            "   G       G   ", "   G       G   ", "   G   H   G   ", "   G       G   ", "J JG       GJ J",
            " J G       G J ", "   GGGGGGGGG   ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "   GGGGGGGGG   ", "   G       G   ",
            "   G       G   ", "   G   I   G   ", "   G  IHI  G   ", "   G   I   G   ", "   G       G   ",
            "J JG       GJ J", " J GGGGGGGGG J ", "               ", "               ", "               " },
        { "               ", "ABA         ABA", "               ", "   GGGGGGGGG   ", "   G       G   ",
            "   G       G   ", "   G       G   ", "   G   H   G   ", "   G       G   ", "   G       G   ",
            "   G       G   ", "J JGGGGGGGGGJ J", " J           J ", "               ", "               " },
        { "BBB         BBB", "ABBBBBB~BBBBBBA", "AAA         AAA", "AAADDDDDDDDDAAA", "AAADDDDDDDDDAAA",
            "AAADDDDDDDDDAAA", "AAADDDDDDDDDAAA", "AAADDDDDDDDDAAA", "AAADDDDDDDDDAAA", "AAADDDDDDDDDAAA",
            "AAADDDDDDDDDAAA", "AAADDDDDDDDDAAA", "AAA         AAA", "AAA         AAA", "AAA         AAA" }

    };

    public TST_PrimordialDisjunctus(String mName) {
        super(mName);
    }

    @Override
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
        if (multiDefinition == null) {
            this.multiDefinition = StructureDefinition.<TST_PrimordialDisjunctus>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapePrimordialDisjunctus))
                .addElement('A', ofBlock(GregTechAPI.sBlockCasings2, 0))
                .addElement(
                    'B',
                    ofChain(
                        buildHatchAdder(TST_PrimordialDisjunctus.class)
                            .atLeast(
                                gregtech.api.enums.HatchElement.Energy,
                                gregtech.api.enums.HatchElement.InputBus,
                                gregtech.api.enums.HatchElement.InputHatch)
                            .casingIndex(CASING_INDEX)
                            .dot(1)
                            .build(),
                        ofSpecificTileAdder(
                            TST_PrimordialDisjunctus::addEssentiaOutputHatchToMachineList,
                            MTEEssentiaOutputHatch.class,
                            Loaders.essentiaOutputHatch,
                            0),
                        onElementPass(TST_PrimordialDisjunctus::onCasingFound, ofBlock(Loaders.magicCasing, 0))))
                .addElement('C', gregtech.api.enums.HatchElement.InputHatch.newAny(CASING_INDEX, 2))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 0))
                .addElement('E', gregtech.api.enums.HatchElement.Muffler.newAny(CASING_INDEX, 3))
                .addElement(
                    'F',
                    ofChain(
                        onElementPass(x -> x.onEssentiaCellFound(0), ofBlock(Loaders.essentiaCell, 0)),
                        onElementPass(x -> x.onEssentiaCellFound(1), ofBlock(Loaders.essentiaCell, 1)),
                        onElementPass(x -> x.onEssentiaCellFound(2), ofBlock(Loaders.essentiaCell, 2)),
                        onElementPass(x -> x.onEssentiaCellFound(3), ofBlock(Loaders.essentiaCell, 3))))
                .addElement('G', ofBlock(ConfigBlocks.blockCosmeticOpaque, 2))
                .addElement('H', ofBlock(GregTechAPI.sBlockCasings2, 15))
                .addElement('K', ofBlock(Loaders.essentiaFilterCasing, 0))
                .addElement('J', ofFrame(Steel))
                .addElement('I', ofBlock(GregTechAPI.sBlockCasings8, 0))
                .build();
        }
        return multiDefinition;
    }

    @Override
    public String[] getInfoData() {
        // This info gets shown when scanning the controller block.
        String[] inInfo = super.getInfoData();
        String[] outInfo = new String[inInfo.length + 3];
        System.arraycopy(inInfo, 0, outInfo, 0, inInfo.length);
        outInfo[inInfo.length] = EnumChatFormatting.AQUA + "Generating: "
            + EnumChatFormatting.GOLD
            + this.primalAspectsGenerated
            + " Primal Aspects";
        outInfo[inInfo.length + 1] = EnumChatFormatting.AQUA + "Boost: "
            + EnumChatFormatting.GOLD
            + this.nodeIncrease
            + "%";
        outInfo[inInfo.length + 2] = EnumChatFormatting.AQUA + "Purification Efficiency: "
            + EnumChatFormatting.GOLD
            + this.nodePurificationEfficiency
            + "%";
        return outInfo;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble("mParallel", this.mParallel);
        aNBT.setDouble("nodePurificationEfficiency", this.nodePurificationEfficiency);
        aNBT.setDouble("nodeIncrease", this.nodeIncrease);
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
        this.nodePurificationEfficiency = aNBT.getInteger("nodePurificationEfficiency");
        this.nodeIncrease = aNBT.getInteger("nodeIncrease");
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
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {

        // This data is set through the structure check so we reset it here
        this.mCasing = 0;
        this.mParallel = 0;
        this.pTier = 0;

        boolean bStructureCheck = checkPiece(STRUCTURE_PIECE_MAIN, 7, 16, 1);

        // Only reset this data if we have an invalid structure check
        if (!bStructureCheck) {
            this.nodeIncrease = 0;
            this.nodePurificationEfficiency = 0;
        }

        return bStructureCheck;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 16, 1, elementBudget, env, false, true);
    }

    private boolean addEssentiaOutputHatchToMachineList(MTEEssentiaOutputHatch aTileEntity) {
        if (aTileEntity instanceof MTEEssentiaOutputHatch) {
            return this.mEssentiaOutputHatches.add(aTileEntity);
        }
        return false;
    }

    protected void onCasingFound() {
        this.mCasing++;
    }

    protected void onEssentiaCellFound(int tier) {
        this.mParallel += (STANDARD_DIFFUSION_CELL_PARALLEL << tier); // 1 << 0 = 1, 1 << 1 = 2, 1 << 2 = 4, 1 << 3 = 8
        this.pTier = Math.max(this.pTier, tier);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        int parallel = (int) this.mParallel;
        if (parallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // Parallels (all same tier)
        // T1: 4
        // T2: 8
        // T3: 16
        // T4: 32
        //
        // 16 amount primal base.
        //
        // Base cycle time.
        // 20 s
        // Min cycle time.
        // 1 s
        // Mathematically stops scaling at UMV power
        //
        // Output of each primal (not boosted, not overclocked) [T1][T2][T3][T4]
        // 3.2/s 6.4/s 12.8/s 25.6/s
        // Output of each primal (boosted, not overclocked) [T1][T2][T3][T4]
        // 6.4/s 12.8/s 25.6/s 51.2/s
        // Output of each primal (boosted, overclocked UV) [T1][T2][T3][T4]
        // 102.4/s 204.8/s 409.6/s 819.2/s
        // Output of each primal (boosted, overclocked UMV) [T1][T2][T3][T4]
        // 2560/s 5120/s 10240/s 20480/s
        this.primalAspectsGenerated = (int) (parallel * STANDARD_PRIMAL_ASPECTS_PER_PARALLEL
            * (1.0 + (this.nodeIncrease * 0.01 * STANDARD_BOOST_MULTIPLIER)));

        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(RECIPE_EUT)
            .setEUt(getMaxInputEu())
            .setDuration(SECOND_IN_TICKS * STANDARD_RECIPE_DURATION)
            .setDurationDecreasePerOC(2)
            .calculate();

        useLongPower = true;
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();

        // This keeps the progress time to a minimum of 1 second without decreasing the output.
        if (mMaxProgresstime < SECOND_IN_TICKS) {
            // This caps out at UMV where it 1 ticks and any further overclocking is irrelevant
            this.primalAspectsGenerated = (int) (((double) SECOND_IN_TICKS / mMaxProgresstime)
                * this.primalAspectsGenerated);
            mMaxProgresstime = SECOND_IN_TICKS;
        }

        AspectList generatedAspects = new AspectList();

        generatedAspects.add(Aspect.AIR, this.primalAspectsGenerated);
        generatedAspects.add(Aspect.EARTH, this.primalAspectsGenerated);
        generatedAspects.add(Aspect.FIRE, this.primalAspectsGenerated);
        generatedAspects.add(Aspect.WATER, this.primalAspectsGenerated);
        generatedAspects.add(Aspect.ORDER, this.primalAspectsGenerated);
        generatedAspects.add(Aspect.ENTROPY, this.primalAspectsGenerated);

        this.mOutputAspects.add(generatedAspects);

        this.mEfficiencyIncrease = 10000;

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

        this.mCasing = 0;
        this.mParallel = 0;
        this.pTier = 0;
        this.primalAspectsGenerated = 0;
        this.mOutputAspects.aspects.clear();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        // This happens 4 times per second
        // Meaning we gain at most 12% per second of purification efficiency
        // Meaning we gain at most 8% per second of boost
        if (aTick % 5 == 0 && this.mMachine) {
            final World WORLD = this.getBaseMetaTileEntity()
                .getWorld();
            int x = this.getBaseMetaTileEntity()
                .getXCoord();
            int y = this.getBaseMetaTileEntity()
                .getYCoord();
            int z = this.getBaseMetaTileEntity()
                .getZCoord();

            // Loses 5 every post tick, Gains 10 max every post tick
            this.nodePurificationEfficiency = Math
                .max(0, this.nodePurificationEfficiency - STANDARD_PURIFICATION_REDUCTION);
            if (this.nodePurificationEfficiency < 100) {
                this.nodePurificationEfficiency = Math.min(
                    100,
                    this.nodePurificationEfficiency
                        + (int) (VisNetHandler.drainVis(WORLD, x, y, z, Aspect.ORDER, 200) * 0.02
                            * STANDARD_PURIFICATION_GAIN_MULTIPLIER));
            }

            // Loses 5 every post tick, Gains 7 max every post tick
            this.nodeIncrease = Math.max(0, this.nodeIncrease - STANDARD_BOOST_REDUCTION);
            if (this.nodeIncrease < 100) {
                this.nodeIncrease = Math.min(
                    100,
                    this.nodeIncrease + (int) (VisNetHandler.drainVis(WORLD, x, y, z, Aspect.ENTROPY, 125) * 0.024
                        * STANDARD_BOOST_GAIN_MULTIPLIER));
            }
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (xstr.nextInt(40) == 0) {
            if (xstr.nextInt(100) < Math.max(100 - this.nodePurificationEfficiency, 0)) {
                final World WORLD = this.getBaseMetaTileEntity()
                    .getWorld();
                MTEHatchMuffler mufflerHatch = this.mMufflerHatches.get(xstr.nextInt(this.mMufflerHatches.size()));
                int x = mufflerHatch.getBaseMetaTileEntity()
                    .getXCoord();
                int y = mufflerHatch.getBaseMetaTileEntity()
                    .getYCoord();
                int z = mufflerHatch.getBaseMetaTileEntity()
                    .getZCoord();

                ForgeDirection facing = mufflerHatch.getBaseMetaTileEntity()
                    .getFrontFacing();
                switch (facing) {
                    case SOUTH:
                        z += 1;
                        break;
                    case NORTH:
                        z -= 1;
                        break;
                    case WEST:
                        x -= 1;
                        break;
                    case EAST:
                        x += 1;
                        break;
                    default:
                        y += 1;
                }
                generateFluxGas(WORLD, x, y, z);
            }
        }
        return super.onRunningTick(aStack);
    }

    private void generateFluxGas(World world, int x, int y, int z) {
        world.setBlock(x, y, z, ConfigBlocks.blockFluxGas, 8, 3);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_PrimordialDisjunctus_MachineType
        // # Essentia Extractor
        // #zh_CN 初始源质提取者
        tt.addMachineType(TextEnums.tr("Tooltip_PrimordialDisjunctus_MachineType"))
            // #tr Tooltip_PrimordialDisjunctus_00
            // # Controller block for the Primordial Disjunctus
            // #zh_CN 初源解离机的控制器方块
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_00"))
            // #tr Tooltip_PrimordialDisjunctus_01
            // # §9Still sweating over bubbling crucibles?Huff-puff... Let the Primordial Disjunctus coax primal essentia forth with but a whisper of electric power
            // #zh_CN §9还在哼哧哼哧守着沸腾的坩埚？嘘——初源解离仪只需一丝电力，便能诱出本源灵质。
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_01"))
            // #tr Tooltip_PrimordialDisjunctus_02
            // # Draw raw auram essence into the arcane containment vessel and initiate centrifugal separation.However,due to the cascade reaction of quintessential dissociation, the process will inevitably degrade the complex aura into its primal form, leaving only rudimentary Essentia as residue.
            // #zh_CN 将灵气吸引进罐子内然后离心,不幸的是灵气的相互扰动使得相互降解最后仅剩下初等源质
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_02"))
            // #tr Tooltip_PrimordialDisjunctus_03
            // # parallel = The sum of diffusion cell values (Novice = 1, Adept = 2, Master = 4, Grandmaster = 8)
            // #zh_CN 并行 = 扩散单元等级的总和 ( 新手=1，学徒=2，大师=4，宗师=8 )
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_03"))
            // #tr Tooltip_PrimordialDisjunctus_04
            // # Min voltage 1A EV, standard overclocks
            // #zh_CN 最低使用1A EV, 使用标准超频 ( 即每提升一级电压加工时间减半 )
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_04"))
            // #tr Tooltip_PrimordialDisjunctus_05
            // # With the power of technology, this process only requires energy to produce a base amount of 16 primal aspects per parallel every 20 seconds at 1 amp EV
            // #zh_CN 借助科技的力量, 此过程仅需能量, 在1A EV电压下每20秒每个并行产出16单位基础源质.
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_05"))
            // #tr Tooltip_PrimordialDisjunctus_06
            // # Providing Ordo centi-vis will reduce the flux produced to nothing, flux produced is not affected by muffler tier, while providing Perditio centi-vis will boost primal aspect production up to 200%
            // #zh_CN 提供秩序vis可将咒波污染降为零(与消声仓等级无关),提供混沌vis可提升源质产量最高200%
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_06"))
            // #tr Tooltip_PrimordialDisjunctus_07
            // # This machine maxes out at 1 UMV amp anything more will just void power.
            // #zh_CN 本机最高支持1A UMV,超出的电力将被直接浪费.
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_07"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(11, 10, 23, true)
            .addController(textFrontCenter)
            // #tr Tooltip_PrimordialDisjunctus_HatchBusInfo
            // # Replace Magic mechanical blocks in any cabin
            // #zh_CN 任何舱室替换魔法机械方块
            .addOutputHatch(TextEnums.tr("Tooltip_PrimordialDisjunctus_HatchBusInfo"))
            .addEnergyHatch(TextEnums.tr("Tooltip_PrimordialDisjunctus_HatchBusInfo"))
            .addOtherStructurePart(TextEnums.tr("Tooltip.EssentiaOutputHatch"), TextEnums.tr("Tooltip_PrimordialDisjunctus_HatchBusInfo"))
            .toolTipFinisher(ModName);
        return tt;
        // spotless:on
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.of(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .glow()
                    .build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.of(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 7, 16, 1, stackSize, hintsOnly);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {

        return new TST_PrimordialDisjunctus(this.mName);
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

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("primalAspectsPerCycle", this.primalAspectsGenerated);
        tag.setInteger("boost", this.nodeIncrease);
        tag.setInteger("purificationEfficiency", this.nodePurificationEfficiency);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("primalAspectsPerCycle")) {
            currentTip.add(
                EnumChatFormatting.AQUA + "Generating: "
                    + EnumChatFormatting.GOLD
                    + tag.getInteger("primalAspectsPerCycle")
                    + " Primal Aspects"
                    + EnumChatFormatting.RESET);
        }

        if (tag.hasKey("boost")) {
            currentTip.add(
                EnumChatFormatting.AQUA + "Boost: "
                    + EnumChatFormatting.GOLD
                    + tag.getInteger("boost")
                    + "%"
                    + EnumChatFormatting.RESET);
        }

        if (tag.hasKey("purificationEfficiency")) {
            currentTip.add(
                EnumChatFormatting.AQUA + "Purification Efficiency: "
                    + EnumChatFormatting.GOLD
                    + tag.getInteger("purificationEfficiency")
                    + "%"
                    + EnumChatFormatting.RESET);
        }
    }
}
