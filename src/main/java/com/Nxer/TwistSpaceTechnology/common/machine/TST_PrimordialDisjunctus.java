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
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
    protected int nodePurificationEfficiency = 0;
    private static final int RECIPE_DURATION = 20;
    private static final int RECIPE_EUT = 1920;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private IStructureDefinition<TST_PrimordialDisjunctus> multiDefinition = null;

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();
        mEssentiaOutputHatches.clear();
    }

    protected int nodeIncrease = 0;
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
                .addElement('C', ofBlock(Loaders.essentiaHatch, 0))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 0))
                .addElement('E', gregtech.api.enums.HatchElement.Muffler.newAny(CASING_INDEX, 2))
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
        String[] info = super.getInfoData();
        info[7] = EnumChatFormatting.RESET + " Purification Efficiency: "
            + EnumChatFormatting.AQUA
            + this.nodePurificationEfficiency
            + "%"
            + EnumChatFormatting.RESET
            + " Speed Up: "
            + EnumChatFormatting.GRAY
            + this.nodeIncrease
            + "%"
            + EnumChatFormatting.RESET;
        return info;
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
        this.mCasing = 0;
        this.mParallel = 0;
        this.pTier = 0;
        this.nodePurificationEfficiency = 0;
        this.nodeIncrease = 0;

        return checkPiece(STRUCTURE_PIECE_MAIN, 7, 16, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 16, 1, elementBudget, env, false, true);
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
        this.mParallel += (1 << tier);
        this.pTier = Math.max(this.pTier, tier);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        int p = (int) this.mParallel;
        if (p <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        AspectList generatedAspects = new AspectList();

        generatedAspects.add(Aspect.AIR, p);
        generatedAspects.add(Aspect.EARTH, p);
        generatedAspects.add(Aspect.FIRE, p);
        generatedAspects.add(Aspect.WATER, p);
        generatedAspects.add(Aspect.ORDER, p);
        generatedAspects.add(Aspect.ENTROPY, p);

        this.mOutputAspects.add(generatedAspects);

        this.mEfficiencyIncrease = 10000;

        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(RECIPE_EUT)
            .setEUt(getMaxInputEu())
            .setDuration(
                (int) Math.ceil(this.mOutputAspects.visSize() * RECIPE_DURATION * (1 - this.nodeIncrease * 0.005)))
            .setDurationDecreasePerOC(2)
            .calculate();

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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 5 == 0 && this.mMachine) {
            final World WORLD = this.getBaseMetaTileEntity()
                .getWorld();
            int x = this.getBaseMetaTileEntity()
                .getXCoord();
            int y = this.getBaseMetaTileEntity()
                .getYCoord();
            int z = this.getBaseMetaTileEntity()
                .getZCoord();
            this.nodePurificationEfficiency = Math.max(0, this.nodePurificationEfficiency - 1);
            if (this.nodePurificationEfficiency < 100) {
                this.nodePurificationEfficiency = (int) Math.min(
                    100,
                    this.nodePurificationEfficiency
                        + Math.ceil(VisNetHandler.drainVis(WORLD, x, y, z, Aspect.ORDER, 200) * 0.05));
            }
            this.nodeIncrease = Math.min(100, VisNetHandler.drainVis(WORLD, x, y, z, Aspect.ENTROPY, 125));
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        this.nodePurificationEfficiency = Math.max(0, this.nodePurificationEfficiency - 1);
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
        // #tr Tooltip_PrimordialDisjunctus_MachineType
        // # Essentia Extractor
        // #zh_CN 初始源质提取者
        tt.addMachineType(TextEnums.tr("Tooltip_PrimordialDisjunctus_MachineType"))
            // #tr Tooltip_PrimordialDisjunctus_00
            // # Controller block for the Primordial Disjunctus
            // #zh_CN 初源解离机的控制器方块
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_00"))
            // #tr Tooltip_PrimordialDisjunctus_01
            // # §9Still sweating over bubbling crucibles?Huff-puff... Let the Primordial Disjunctus coax primal
            // essentia forth with but a whisper of electric power
            // #zh_CN §9还在哼哧哼哧守着沸腾的坩埚？嘘——初源解离仪只需一丝电力，便能诱出本源灵质。
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_01"))
            // #tr Tooltip_PrimordialDisjunctus_02
            // # Draw raw auram essence into the arcane containment vessel and initiate centrifugal separation. However,
            // due to the cascade reaction of quintessential dissociation, the process will inevitably degrade the
            // complex aura into its primal form, leaving only rudimentary Essentia as residue.
            // #zh_CN 将灵气吸引进罐子内然后离心,不幸的是灵气的相互扰动使得相互降解最后仅剩下初等源质
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_02"))
            // #tr Tooltip_PrimordialDisjunctus_03
            // # parallel = The sum of several 2^essentiaCell
            // #zh_CN 并行 = 若干 2^扩散单元等级 之和 ,最高为128并行
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_03"))
            // #tr Tooltip_PrimordialDisjunctus_04
            // # At least EV voltage, use 4/2 overclocking, that is, the processing time is halved for each voltage
            // increase
            // #zh_CN 至少是EV电压,使用4/2超频,即每提升一次电压加工时间减半
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_04"))
            // #tr Tooltip_PrimordialDisjunctus_05
            // # With the power of technology, we no longer need fire vis and water vis as starting conditions
            // #zh_CN 借助科技的力量我们不再需要火vis和水vis来作为启动条件
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_05"))
            // #tr Tooltip_PrimordialDisjunctus_06
            // # Order vis can inhibit the generation of spell waves, and its generation is independent of the silo
            // level, while Perditio vis will accelerate the machine speed up to 200%%
            // #zh_CN 秩序vis可以遏制咒波的产生,另外咒波产生与消声仓等级无关,而混沌vis会加速机器速度,最高200%%
            .addInfo(TextEnums.tr("Tooltip_PrimordialDisjunctus_06"))
            // #tr Tooltip_PrimordialDisjunctus_07
            // # Please do not give too high voltage, this machine is really fast, otherwise pay attention to your
            // storage
            // #zh_CN 请不要给予过高的电压,这台机器的速度真的很快,否则注意你的存储
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
            // #tr Tooltip_PrimordialDisjunctus_HatchBusInfo
            // # Replace Magic mechanical blocks in any cabin
            // #zh_CN 任何舱室替换魔法机械方块
            .addEnergyHatch(TextEnums.tr("Tooltip_PrimordialDisjunctus_EnergyHatch"))

            .toolTipFinisher(ModName);
        return tt;
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
}
