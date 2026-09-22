package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.AUTHOR;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.MAINTAINER;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_DRAGONEGG;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_DRAGONEGG_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TST_GeneratorBase;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TextEnums;
import com.cleanroommc.modularui.drawable.UITexture;
import com.google.common.collect.Lists;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

@SkipGenerateDescription
public class GT_TileEntity_MegaEggGenerator extends TST_GeneratorBase<GT_TileEntity_MegaEggGenerator>
    implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GT_TileEntity_MegaEggGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(AUTHOR, ID.RH_NU, MAINTAINER, ID.TASKEREN);
    }

    public GT_TileEntity_MegaEggGenerator(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaEggGenerator(this.mName);
    }
    // endregion

    // region Structure
    private IStructureDefinition<GT_TileEntity_MegaEggGenerator> structureDef = null;
    private static final String STRUCTURE_PIECE_BASE = "baseEggGenerator";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleEggGenerator";
    private static final String STRUCTURE_PIECE_TOP = "topEggGenerator";
    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 0;
    private final int depthOffSet = 6;

    // spotless:off
	private final String[][] shapeBase = new String[][]{
        {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A         A  "," A    G~G    A "," A    GGG    A "," A    GGG    A ","  A         A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "}
    };

	private final String[][] shapeMiddle = new String[][]{
        {"               ","      AAA      ","    AAK KAA    ","   AK     KA   ","  AK       KA  ","  A         A  "," AK    D    KA "," A    DFD    A "," AK    D    KA ","  A         A  ","  AK       KA  ","   AK     KA   ","    AAK KAA    ","      AAA      ","               "},
        {"      AAA      ","    AABBBAA    ","   ABBCACBBA   ","  ABCA   ACBA  "," ABC       CBA "," ABA       ABA ","ABC    D    CBA","ABA   DFD   ABA","ABC    D    CBA"," ABA       ABA "," ABC       CBA ","  ABCA   ACBA  ","   ABBCACBBA   ","    AABBBAA    ","      AAA      "}
    };

	private final String[][] shapeTop = new String[][]{
        {"               ","               ","               ","               ","               ","               ","               ","       F       ","               ","               ","               ","               ","               ","               ","               "}
    };
    // spotless:on

    @Override
    public IStructureDefinition<GT_TileEntity_MegaEggGenerator> getStructureDefinition() {
        if (structureDef == null) {
            structureDef = StructureDefinition.<GT_TileEntity_MegaEggGenerator>builder()
                .addShape(STRUCTURE_PIECE_BASE, transpose(shapeBase))
                .addShape(STRUCTURE_PIECE_MIDDLE, transpose(shapeMiddle))
                .addShape(STRUCTURE_PIECE_TOP, transpose(shapeTop))
                .addElement(
                    'A',
                    HatchElementBuilder.<GT_TileEntity_MegaEggGenerator>builder()
                        .atLeast(Dynamo.or(ExoticDynamo))
                        .adder(GT_TileEntity_MegaEggGenerator::addToMachineList)
                        .casingIndex(1536)
                        .hint(1)
                        .buildAndChain(Loaders.magicCasing, 0))
                .addElement('B', ofFrame(Materials.Trinium))
                .addElement('C', ofBlock(Loaders.essentiaCell, 0))
                .addElement('D', ofFrame(Materials.Iridium))
                .addElement('F', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 8))
                .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 7))
                .addElement(
                    'K',
                    ofChain(
                        onElementPass(k -> ++k.mAirPosed, isAir()),
                        onElementPass(k -> ++k.mCreeperEggs, ofBlock(MarsBlocks.creeperEgg, 0)),
                        onElementPass(k -> ++k.mDragonEggs, ofBlock(Blocks.dragon_egg, 0)),
                        onElementPass(k -> ++k.mInfinityEggs, ofBlock(InfinityEgg(), 0))))
                .build();
        }
        return structureDef;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
        int piece = stackSize.stackSize;
        for (int i = 1; i <= piece; i++) {
            this.buildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                hintsOnly,
                horizontalOffSet,
                verticalOffSet + i * 2,
                depthOffSet);
        }
        this.buildPiece(
            STRUCTURE_PIECE_TOP,
            stackSize,
            hintsOnly,
            horizontalOffSet,
            verticalOffSet + piece * 2 + 1,
            depthOffSet);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int built = 0;

        built += survivalBuildPiece(
            STRUCTURE_PIECE_BASE,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            source,
            actor,
            false,
            true);

        int piece = stackSize.stackSize;
        for (int i = 1; i <= piece; i++) {
            built += survivalBuildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                horizontalOffSet,
                verticalOffSet + i * 2,
                depthOffSet,
                elementBudget,
                source,
                actor,
                false,
                true);
        }

        built += survivalBuildPiece(
            STRUCTURE_PIECE_TOP,
            stackSize,
            horizontalOffSet,
            verticalOffSet + piece * 2 + 1,
            depthOffSet,
            elementBudget,
            source,
            actor,
            false,
            true);

        return built;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        // No need for the maintenance hatches!
        repairMachine();

        // Initialize
        this.mInfinityEggs = 0;
        this.mDragonEggs = 0;
        this.mCreeperEggs = 0;
        this.mAirPosed = 0;
        this.mPieces = 0;
        // Main checks
        if (!checkAllPieces(errors)) return;
        if (!checkInfinityEgg()) {
            // #tr TST.MegaEggGeneratorStructureErrors.InfinityEgg
            // # The number of infinite eggs exceeds the number of pieces.
            // #zh_CN 无限之蛋数量超过了层数
            errors.add(StructureErrors.of("TST.MegaEggGeneratorStructureErrors.InfinityEgg"));
        }
        int dynamoCount = mDynamoHatches.size() + mExoticDynamoHatches.size();
        if (dynamoCount == 0) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, Dynamo, 0, 1));
        } else if (dynamoCount > Config.MEG_Dynamo_Limit) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_MANY, Dynamo, dynamoCount, Config.MEG_Dynamo_Limit));
        }
        if (!checkLaser()) {
            // #tr TST.MegaEggGeneratorStructureErrors.Laser
            // # The number of pieces does not meet the requirement for using the laser hatches.
            // #zh_CN 未达到允许使用激光仓的层数要求
            errors.add(StructureErrors.of("TST.MegaEggGeneratorStructureErrors.Laser"));
        }

        initValues();
    }

    /**
     * Check main structure of pieces.
     *
     * @return If pieces are all set
     */
    private boolean checkAllPieces(List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_BASE, horizontalOffSet, verticalOffSet, depthOffSet, errors)) {
            return false;
        }
        while (checkPiece(
            STRUCTURE_PIECE_MIDDLE,
            horizontalOffSet,
            verticalOffSet + (this.mPieces + 1) * 2,
            depthOffSet,
            errors)) {
            this.mPieces++;
        }
        if (mPieces < 1) {
            return false;
        }
        return checkPiece(
            STRUCTURE_PIECE_TOP,
            horizontalOffSet,
            verticalOffSet + this.mPieces * 2 + 1,
            depthOffSet,
            errors);
    }

    /**
     * Check Infinity egg num.
     *
     * @return If Infinity egg num is allowed
     */
    private boolean checkInfinityEgg() {
        return mInfinityEggs <= mPieces;
    }

    /**
     * Check Laser hatch when pieces are fewer than 16.
     *
     * @return If laser is allowed
     */
    @SuppressWarnings("deprecation")
    private boolean checkLaser() {
        if (mPieces < ValueEnum.MEG_Laser_Pieces) {
            for (MTEHatch tHatch : mExoticDynamoHatches) {
                if (tHatch instanceof MTEHatchDynamoTunnel) {
                    return false;
                }
            }
        }
        return true;
    }
    // endregion

    // region Processing Logic
    private int mPieces = 1;
    private int mInfinityEggs = 0;
    private int mDragonEggs = 0;
    private int mCreeperEggs = 0;
    private int mAirPosed = 0;
    private long genPower = 0L;
    private int efficiencyIncrement = 1;

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    /**
     * 2% max efficiency buff every 2^n pieces.
     * 1% max efficiency every 1 infinity egg.
     */
    @SuppressWarnings("deprecation")
    private int getMaxEfficiency() {
        return Math.max(
            0,
            10000 + ValueEnum.MEG_Efficiency_PiecesBuff * (int) (Math.log(mPieces) / Math.log(2))
                + ValueEnum.MEG_Efficiency_InfinityEggBuff * mInfinityEggs
                - ValueEnum.MEG_Efficiency_Lost * mAirPosed);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return getMaxEfficiency();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isRotationChangeAllowed() {
        return ValueEnum.MEG_AllowRotation;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        this.mMaxProgresstime = 20;
        this.lEUt = Math.abs(genPower);
        this.mEfficiencyIncrease = efficiencyIncrement;
        return CheckRecipeResultRegistry.GENERATING;
    }

    /**
     * This efficiency increase is calculated per 20 Tick run
     * When Infinity eggs exist, every infinity egg give 100t efficiency increase boost.
     * Dragon eggs will give 1t efficiency increase boost for each pairs, but cap at 50 in total.
     * AS FOR CreeperEggs, who will use them?
     */
    private void updateEfficiencyIncrement() {
        efficiencyIncrement = 20;
        if (mInfinityEggs != 0) {
            efficiencyIncrement += mInfinityEggs * 100;
        }
        if (mDragonEggs != 0) {
            if (mDragonEggs < 100) {
                efficiencyIncrement += mDragonEggs / 2;
            } else {
                efficiencyIncrement += 50;
            }
        }
    }

    /**
     * @return the total output EUt
     */
    @SuppressWarnings("deprecation")
    private long getOutputEUt() {
        return (long) ValueEnum.MEG_Overall_Multiply
            * (ValueEnum.MEG_CrepperEgg_Gen * mCreeperEggs + ValueEnum.MEG_DragonEgg_Gen * mDragonEggs
                + ValueEnum.MEG_InfinityEgg_Gen * mInfinityEggs);
    }

    /**
     * 1A HV Crepper egg
     * 1A EV Dragon egg
     * 2A IV Infinite egg
     * Can be defined in config
     * 2024.1.21 Fix vol explode
     */
    private void updateOutput() {
        genPower = getOutputEUt();
    }

    /**
     * Calls for initializing the output values (volt, amp) and efficiency increment.
     */
    private void initValues() {
        this.updateOutput();
        this.updateEfficiencyIncrement();
    }

    /**
     * I have to add this shit to deal with runtime conflicts that have nothing to do with TST.
     */
    private Block InfinityEgg() {
        if (Mods.Witchery.isModLoaded()) {
            return Block.getBlockFromName(Mods.Witchery.ID + ":infinityegg");
            // Witchery.Blocks.INFINITY_EGG;
        } else return TstBlocks.PhotonControllerUpgrade;
    }

    @Override
    public String[] getInfoData() {
        List<String> info = Lists.newArrayList(super.getInfoData());
        // basic info
        info.add(EnumChatFormatting.AQUA + "Infinity Eggs: " + EnumChatFormatting.GOLD + this.mInfinityEggs);
        info.add(EnumChatFormatting.AQUA + "Dragon Eggs: " + EnumChatFormatting.GOLD + this.mDragonEggs);
        info.add(EnumChatFormatting.AQUA + "Crepper Eggs: " + EnumChatFormatting.GOLD + this.mCreeperEggs);
        info.add(EnumChatFormatting.AQUA + "Air Voids: " + EnumChatFormatting.GOLD + this.mAirPosed);
        info.add(EnumChatFormatting.AQUA + "Pieces: " + EnumChatFormatting.GOLD + this.mPieces);
        // dynamic info
        info.add(EnumChatFormatting.BLUE + "Generating Power: " + EnumChatFormatting.GOLD + this.genPower);
        info.add(
            EnumChatFormatting.BLUE + "Efficiency: "
                + EnumChatFormatting.GOLD
                + this.mEfficiency
                + EnumChatFormatting.GRAY
                + " ("
                + EnumChatFormatting.GREEN
                + String.format("%.2f%%", (double) this.mEfficiency * 100 / this.getMaxEfficiency())
                + EnumChatFormatting.GRAY
                + ", "
                + EnumChatFormatting.BLUE
                + "Max: "
                + EnumChatFormatting.GOLD
                + this.getMaxEfficiency()
                + EnumChatFormatting.GRAY
                + ", "
                + EnumChatFormatting.BLUE
                + "Incr.: "
                + EnumChatFormatting.GOLD
                + this.efficiencyIncrement
                + EnumChatFormatting.GRAY
                + ")");
        info.add(
            EnumChatFormatting.BLUE + "Possibly Generating EUt: "
                + EnumChatFormatting.GOLD
                + (this.genPower * this.mEfficiency) / 10000
                + EnumChatFormatting.GRAY
                + " ("
                + EnumChatFormatting.BLUE
                + "Max: "
                + EnumChatFormatting.GOLD
                + getOutputEUt()
                + EnumChatFormatting.GRAY
                + ")");
        return info.toArray(new String[0]);
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mInfinityEggs", mInfinityEggs);
        aNBT.setInteger("mDragonEggs", mDragonEggs);
        aNBT.setInteger("mCrepperEggs", mCreeperEggs);
        aNBT.setInteger("mAirPosed", mAirPosed);
        aNBT.setInteger("mPieces", mPieces);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mInfinityEggs = aNBT.getInteger("mInfinityEggs");
        mDragonEggs = aNBT.getInteger("mDragonEggs");
        mCreeperEggs = aNBT.getInteger("mCrepperEggs");
        mAirPosed = aNBT.getInteger("mAirPosed");
        mPieces = aNBT.getInteger("mPieces");
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183),
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_DRAGONEGG_GLOW)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_DRAGONEGG_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183), TextureFactory.builder()
                .addIcon(MACHINE_CASING_DRAGONEGG)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_DRAGONEGG)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183) };
    }

    // endregion

    // region Tooltip

    @SuppressWarnings("deprecation")
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_MegaEggGenerator_MachineType
        // # Magical Energy Absorber
        // #zh_CN 魔法能源吸收者
        tt.addMachineType(TextEnums.tr("Tooltip_MegaEggGenerator_MachineType"))
            // #tr Tooltip_MegaEggGenerator_Controller
            // # Controller block for the Tower of Abstraction
            // #zh_CN 抽象之塔的控制器
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_Controller"))
            // #tr Tooltip_MegaEggGenerator_00
            // # This is where the {\RED}ulti{\AQUA}mate{\GOLD} des{\BLUE}tiny{\GRAY} of the Dragon's Children lies.
            // #zh_CN 这是龙之子嗣的{\RED}终{\AQUA}极{\GOLD}宿{\BLUE}命{\GRAY}所在.
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_00"))
            // #tr Tooltip_MegaEggGenerator_01
            // # With the help of the Magic Egg, it draws in endless magical power as effortlessly as a soul-sucking sorcerer.
            // #zh_CN 借助魔法之卵，汲取无尽魔力，如同{\GOLD}{\ITALIC}{\BOLD}吸魂巫师{\GRAY}般不费吹灰之力。
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_01"))
            // #tr Tooltip_MegaEggGenerator_02
            // # Every dragon egg generates {\GOLD}1A EV{\GRAY} & every creeper's generates {\GOLD}1A HV{\GRAY}.
            // #zh_CN 每个龙蛋将产生{\GOLD}1A EV{\GRAY},而爬行者蛋产生{\GOLD}1A HV{\GRAY}.
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_02"))
            // #tr Tooltip_MegaEggGenerator_03
            // # Infinity egg generates {\GOLD}2A IV{\GRAY}, but you can put only one for one each piece you add.
            // #zh_CN 每个无限之蛋产生{\GOLD}2A IV{\GRAY},但无限之蛋的总数不得多于层数.
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_03"))
            // #tr Tooltip_MegaEggGenerator_04
            // # But quantitative change leads to qualitative change,
            // #zh_CN 数量积累至一定阶段，便能引发{\BOLD}{\ITALIC}{\GOLD}质的飞跃,
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_04"))
            // #tr Tooltip_MegaEggGenerator_05
            // # Every 2^n pieces give {\RED}2%{\GRAY} max efficiency bonus, and every infinity egg gives {\RED}1%{\GRAY}.
            // #zh_CN 每2^n层提供{\RED}2%{\GRAY}的最大效率加成,而每个无限之蛋提高{\RED}1%{\GRAY}.
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_05"))
            // #tr Tooltip_MegaEggGenerator_06
            // # You can also put nothing on the egg pos, but every empty pos decreases {\RED}5%{\GRAY} max efficiency.
            // #zh_CN 在这能量的殿堂里，每一个未被诸蛋之力占据的空缺，都将令整体的效率减损{\RED}5%{\GRAY}。
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_06"))
            // #tr Tooltip_MegaEggGenerator_07
            // # Warm-up process for 0.01%% per second as base, each pair of Dragon Eggs add 0.01%% but limit for 50 pairs, each Infinity Egg add {\RED}1%{\GRAY} with no limits.
            // #zh_CN 基础预热速度0.01%%每秒，一对龙蛋增加0.01%%但最多只计算50对，无限之蛋增加{\RED}1%{\GRAY}且无加速上限.
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_07"))
            // #tr Tooltip_MegaEggGenerator_08
            // # The process will take about {\GOLD}3{\GRAY} hours if put Creeper Eggs only, but who really care about?
            // #zh_CN 如果仅有爬行者蛋，预热将需要大约三小时，只是谁会真的在乎这个?
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_08"))
            // #tr Tooltip_MegaEggGenerator_09
            // # Whether it is the {\DARK_PURPLE} Dragon Egg, {\DARK_GREEN} Creeper Egg, or the {\GOLD}Egg of Infinity, {\GRAY}only their presence can drive the full circulation of power.
            // #zh_CN 无论是{\DARK_PURPLE}龙蛋{\RESET}、{\GREEN}爬行者蛋{\GRAY}，抑或是{\ITALIC}{\GOLD}无尽之蛋{\GRAY}，唯有它们的存在，方能驱动力量的完满流转。
            .addInfo(TextEnums.tr("Tooltip_MegaEggGenerator_09"))
            .addOtherStructurePart(
                // #tr Tooltip_MegaEggGenerator_D
                // # Dynamo or TT Dynamo, one only
                // #zh_CN 动力仓或TecTech动力仓,1个
                TextEnums.tr("Tooltip_MegaEggGenerator_D"),
                // #tr Tooltip_MegaEggGenerator_C
                // # Any Magical Casing.
                // #zh_CN 任何魔法机械外壳
                TextEnums.tr("Tooltip_MegaEggGenerator_C"),
                1)
            // #tr Tooltip_MegaEggGenerator_L
            // # Lasers unlock at >=16 pieces.
            // #zh_CN 激光在16层及以上解锁.
            .addStructureInfo(TextEnums.tr("Tooltip_MegaEggGenerator_L"))
            // #tr Tooltip_MegaEggGenerator_M
            // # No need for maintenance hatch.
            // #zh_CN 不需要维护仓.
            .addStructureInfo(TextEnums.tr("Tooltip_MegaEggGenerator_M"))
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

}
