package com.Nxer.TwistSpaceTechnology.common.machine;

import static WayofTime.alchemicalWizardry.ModBlocks.blockLifeEssence;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BloodInfusedDiamondBlock;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BloodInfusedGlowstone;
import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.BloodInfusedIronBlock;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.MetaBlockCasing02;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textAnyCasing;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static net.minecraft.block.Block.getBlockFromName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellAlchemicTierKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellTierKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeResult.ResultInsufficientTier;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;
import com.Nxer.TwistSpaceTechnology.util.InfoDataHelper;
import com.Nxer.TwistSpaceTechnology.util.TaskerenAdvancedMathUtils;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.dreammaster.block.BlockList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import WayofTime.alchemicalWizardry.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.LightingHelper;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.render.GTRenderUtil;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class TST_BloodyHell extends GTCM_MultiMachineBase<TST_BloodyHell> implements ISurvivalConstructable {

    private int speedRuneCount = 0;
    private int tbSpeedRuneCount = 0;
    private int mTier = 0;
    private boolean isBloodChecked = false;
    private boolean mIsAnimated = true;
    protected boolean mFormed;

    public TST_BloodyHell(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_BloodyHell(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BloodyHell(mName);
    }

    public int getAltarTierForSpeed() {
        return mTier;
    }

    /*
     * BH is now placed at the graduation of the Blood Magic mod, so the recipes should be all available at this time.
     * So the tier of BH for recipes is constantly 6 for now.
     * May it be changed?
     * Who knows.
     */
    public int getAltarTierForRecipe() {
        return 6;
    }

    public int getOrbTier() {
        return BloodMagicHelper.getBloodOrbTier(getControllerSlot());
    }

    /*
     * The activation crystal is now included in the ritual recipes.
     * So it is no need to check the crystal in the controller.
     * But this method is still here, just in case.
     */
    @Deprecated
    public int getActivationCrystalTier() {
        return BloodMagicHelper.getActivationCrystalTier(getControllerSlot());
    }

    private float getTierSpeedBonus() {
        // for more, you should go to the Pull Request of this block
        return (getAltarTierForSpeed() - 2) * 0.5F;
    }

    private float getSpeedRuneSpeedBonus() {
        // for more, you should go to the Pull Request of this block
        return (float) TaskerenAdvancedMathUtils.calcBloodyHellSpeedRuneBonus(speedRuneCount + 3 * tbSpeedRuneCount);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1.0F / ((1.0F + getTierSpeedBonus()) * (1.0F + getSpeedRuneSpeedBonus()));
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // Prevent tilting or inversion
        return (d, r, f) -> d == ForgeDirection.UP;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        var tt = new MultiblockTooltipBuilder();
        // spotless:off

        // #tr Tooltip_BloodyHell_MachineType
        // # Altar of Blood | Alchemic Chemistry Set | Ritual of Binding
        // #zh_CN 血之祭坛 | 炼金术台 | 绑定仪式
        tt.addMachineType(TextEnums.tr("Tooltip_BloodyHell_MachineType"))
            // #tr Tooltip_BloodyHell_0
            // # After years of researching, you finally find the last piece to mass-produce the Blood Magic things!
            // #zh_CN 经过多年的研究，你终于找到了量产血魔法物品的最后一块拼图！
            .addInfo(TextEnums.tr("Tooltip_BloodyHell_0"))
            .addSeparator()
            // #tr Tooltip_BloodyHell_1
            // # Researches show that the speed of soaking ritual is highly related to the greatness of the altar and the runes it used.
            // #zh_CN 研究表明浸血仪式的速度与祭坛的精致度和它使用的符文高度相关。
            .addInfo(TextEnums.tr("Tooltip_BloodyHell_1"))
            // #tr Tooltip_BloodyHell_2
            // # And there is a small text on the corner said, "Speed Runes can be the key."
            // #zh_CN 在角落里有一行小字写道，“速度符文也许是关键。”
            .addInfo(TextEnums.tr("Tooltip_BloodyHell_2"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addController(textUseBlueprint)
            .addInputBus(textAnyCasing)
            .addOutputBus(textAnyCasing)
            .addInputHatch(textAnyCasing)
            .addInfo(TextEnums.Author_Taskeren.getText())
            .addInfo(TextEnums.Author_Goderium.getText())
            .toolTipFinisher(ModName);
        return tt;
        // spotless:on
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        speedRuneCount = 0;
        tbSpeedRuneCount = 0;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mTier = 0;
        isBloodChecked = false;
        for (int i = 6; i > 0; i--) {
            if (checkPiece("tier" + i, getOffset(0, i, 0), getOffset(0, i, 1), getOffset(0, i, 2))) {
                mTier = i;
                break;
            }
        }

        int fluidTier = (mTier == 6) ? 2 : (mTier > 2) ? 1 : 0;
        if (fluidTier > 0 && checkPiece(
            "fluid" + fluidTier,
            getOffset(1, mTier, 0),
            getOffset(1, mTier, 1),
            getOffset(1, mTier, 2))) {
            isBloodChecked = true;
        }

        return mTier > 0;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aTick % 20 == 0) { // for every second
            if (aBaseMetaTileEntity.isServerSide()) {
                if (!isBloodChecked) { // check blood if it has not been checked yet
                    checkBlood();
                }
            }
        }
    }

    // note that the actual altar tier is machine structure tier + 1!
    // which means, the machine structure tier starts at 2 at the view of the altar

    private static final String STRUCTURE_PIECE_1 = "tier1";
    private static final String STRUCTURE_PIECE_2 = "tier2";
    private static final String STRUCTURE_PIECE_3 = "tier3";
    private static final String STRUCTURE_PIECE_4 = "tier4";
    private static final String STRUCTURE_PIECE_5 = "tier5";
    private static final String STRUCTURE_PIECE_6 = "tier6";
    private static final String STRUCTURE_FLUID_1 = "fluid1";
    private static final String STRUCTURE_FLUID_2 = "fluid2";

    @Override
    public IStructureDefinition<TST_BloodyHell> getStructureDefinition() {
        if (StructureDef == null) {
            StructureDef = StructureDefinition.<TST_BloodyHell>builder()
                .addShape(STRUCTURE_PIECE_1, transpose(STRUCTURE_TIER_1))
                .addShape(STRUCTURE_PIECE_2, transpose(STRUCTURE_TIER_2))
                .addShape(STRUCTURE_PIECE_3, transpose(STRUCTURE_TIER_3))
                .addShape(STRUCTURE_PIECE_4, transpose(STRUCTURE_TIER_4))
                .addShape(STRUCTURE_PIECE_5, transpose(STRUCTURE_TIER_5))
                .addShape(STRUCTURE_PIECE_6, transpose(STRUCTURE_TIER_6))
                .addShape(STRUCTURE_FLUID_1, transpose(STRUCTURE_BLOOD_1))
                .addShape(STRUCTURE_FLUID_2, transpose(STRUCTURE_BLOOD_2))
                .addElement('A', ofBlock(BasicBlocks.MetaBlockCasing01, 9))
                .addElement('B', ofBlock(MetaBlockCasing02, 0))
                .addElement('C', ofBlock(MetaBlockCasing02, 1))
                .addElement('D', ofBlockAnyMeta(BlockList.BloodyIchorium.getBlock()))
                .addElement('E', ofBlockAnyMeta(BlockList.BloodyThaumium.getBlock()))
                .addElement('F', ofBlockAnyMeta(BlockList.BloodyVoid.getBlock()))
                .addElement(
                    'G',
                    ofChain(
                        ofBlockAnyMeta(ModBlocks.bloodRune),
                        onElementPass((x) -> { x.speedRuneCount += 1; }, ofBlockAnyMeta(ModBlocks.speedRune)),
                        onElementPass(
                            (x) -> { x.tbSpeedRuneCount += 1; },
                            ofBlockAnyMeta(BasicBlocks.timeBendingSpeedRune))))
                .addElement('H', ofBlockAnyMeta(ModBlocks.largeBloodStoneBrick))
                .addElement('I', ofBlock(BloodInfusedIronBlock.getLeft(), BloodInfusedIronBlock.getRight()))
                .addElement('J', ofBlockAnyMeta(ModBlocks.blockCrystal))
                .addElement('K', ofBlock(BloodInfusedGlowstone.getLeft(), BloodInfusedGlowstone.getRight()))
                .addElement('L', ofBlock(BloodInfusedDiamondBlock.getLeft(), BloodInfusedDiamondBlock.getRight()))
                .addElement(
                    'M',
                    Mods.Chisel.isModLoaded() ? ofBlock(getBlockFromName("chisel:beacon"), 1)
                        : ofBlockAnyMeta(Blocks.beacon))
                .addElement(
                    'N',
                    ofChain(
                        ofBlock(MetaBlockCasing02, 0),
                        HatchElementBuilder.<TST_BloodyHell>builder()
                            .atLeast(InputBus, OutputBus, InputHatch)
                            .adder(TST_BloodyHell::addToMachineList)
                            .dot(1)
                            .casingIndex(MetaBlockCasing02.getTextureIndex(0))
                            .buildAndChain(MetaBlockCasing02, 0)))
                .addElement('Z', ofBlock(blockLifeEssence, 0))
                .build();
        }
        return StructureDef;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        var tier = stackSize.stackSize;
        if (tier > 6) tier = 6;
        this.buildPiece(
            "tier" + tier,
            stackSize,
            hintsOnly,
            getOffset(0, tier, 0),
            getOffset(0, tier, 1),
            getOffset(0, tier, 2));
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return super.getStructureDescription(stackSize);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int tier = stackSize.stackSize;
        if (tier > 6) tier = 6;
        if (mMachine && tier <= mTier) return -1;

        int blocksBuilt = this.survivialBuildPiece(
            "tier" + tier,
            stackSize,
            getOffset(0, tier, 0),
            getOffset(0, tier, 1),
            getOffset(0, tier, 2),
            elementBudget,
            env,
            false,
            true);

        if (tier < 3) return blocksBuilt;
        else {
            int tierF = tier == 6 ? 2 : 1;
            int fluidBuilt = this.survivialBuildPiece(
                "fluid" + tierF,
                stackSize,
                getOffset(1, tier, 0),
                getOffset(1, tier, 1),
                getOffset(1, tier, 2),
                elementBudget,
                env,
                false,
                true);
            return blocksBuilt + fluidBuilt;
        }
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTCMRecipe.BloodyHellRecipes);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.BloodyHellRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {

                // check structure blood
                if (!isBloodChecked && !(isBloodChecked = checkBlood()))
                    return SimpleCheckRecipeResult.ofFailure("no_enough_blood");

                // check altar tier
                int requiredTier = recipe.getMetadataOrDefault(BloodyHellTierKey.INSTANCE, 0);
                if (requiredTier > getAltarTierForRecipe()) {
                    return ResultInsufficientTier.ofBloodAltar(requiredTier);
                }

                // check blood orb tier
                int requiredOrbTier = recipe.getMetadataOrDefault(BloodyHellAlchemicTierKey.INSTANCE, 0);
                if (requiredOrbTier > getOrbTier()) {
                    return ResultInsufficientTier.ofBloodOrb(requiredOrbTier);
                }

                return super.validateRecipe(recipe);
            }

            @Override
            public @NotNull CheckRecipeResult process() {
                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);

                return super.process();
            }
        }.setMaxParallelSupplier(this::getLimitedMaxParallel);
    }

    /**
     * Check the blood fluids in the structure and place them if not valid and has enough bloods.
     * <p>
     * Invoking this in tiers without fluid structures is ok, and always return true.
     *
     * @return true if the blood is valid, or placed.
     */
    private boolean checkBlood() {
        if (mTier <= 0) return false; // invalid tiers
        else if (mTier < 3) return true; // no blood needed
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        String[][] structureDef = mTier > 5 ? STRUCTURE_BLOOD_2 : STRUCTURE_BLOOD_1;
        int bloodAmountNeeded = mTier > 5 ? BLOOD_AMOUNT_NEEDED_2 : BLOOD_AMOUNT_NEEDED_1;
        int offsetX = getOffset(1, mTier, 0);
        int offsetY = getOffset(1, mTier, 1);
        int offsetZ = getOffset(1, mTier, 2);
        Block blood = blockLifeEssence;

        int lengthX = structureDef.length;
        int lengthY = structureDef[0].length;
        int lengthZ = structureDef[0][0].length();

        // BloodMagic has not registered block flowing life essence, so it is necessary to place all fluids at once
        ArrayList<FluidStack> inputFluids = this.getStoredFluids();
        int mBloodAmount = 0;
        for (FluidStack aFluid : inputFluids) {
            if (aFluid.isFluidEqual(FluidUtils.getFluidStack("lifeessence", 1))) {
                mBloodAmount += aFluid.amount;
            }
        }
        if (bloodAmountNeeded > mBloodAmount) return false;

        int fixX = 0;
        int fixY = mTier > 5 ? -1 : -24;
        int fixZ = mTier > 5 ? 1 : 24;

        int setCount = 0;
        for (int y = 0; y < lengthY; y++) {
            for (int x = 0; x < lengthX; x++) {
                for (int z = 0; z < lengthZ; z++) {
                    String strList = String.valueOf(structureDef[x][y].charAt(z));
                    if (!Objects.equals(strList, "Z")) continue;

                    int aX = offsetX - x + fixX;
                    int aY = offsetY - y + fixY;
                    int aZ = offsetZ - z + fixZ;

                    aX += aBaseMetaTileEntity.getXCoord();
                    aY += aBaseMetaTileEntity.getYCoord();
                    aZ += aBaseMetaTileEntity.getZCoord();

                    boolean isVaildFluid = false;
                    if (this.getStoredFluids() != null) {
                        for (FluidStack aFluid : inputFluids) {
                            if (aFluid.isFluidEqual(FluidUtils.getFluidStack("lifeessence", 1))
                                && aFluid.amount >= 1000) {
                                aFluid.amount -= 1000;
                                isVaildFluid = true;
                                break;
                            }
                        }
                    }
                    setCount++;
                    if (isVaildFluid) {
                        aBaseMetaTileEntity.getWorld()
                            .setBlock(aX, aY, aZ, blood);
                    } else {
                        return false;
                    }

                    if (setCount == bloodAmountNeeded / 1000) break;
                }
            }
        }
        isBloodChecked = true;
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("isBloodChecked", isBloodChecked);
        aNBT.setInteger("speedRuneCount", speedRuneCount);
        aNBT.setInteger("tbSpeedRuneCount", tbSpeedRuneCount);
        aNBT.setInteger("mTier", mTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        isBloodChecked = aNBT.getBoolean("isBloodChecked");
        speedRuneCount = aNBT.getInteger("speedRuneCount");
        tbSpeedRuneCount = aNBT.getInteger("tbSpeedRuneCount");
        mTier = aNBT.getInteger("mTier");
    }

    @Override
    public String[] getInfoData() {
        return InfoDataHelper.buildInfoData(super.getInfoData(), (info) -> {
            info.add(EnumChatFormatting.BLUE + "Structure Tier: " + EnumChatFormatting.GOLD + mTier);
            info.add(
                EnumChatFormatting.BLUE + "Speed Rune Count: "
                    + EnumChatFormatting.AQUA
                    + speedRuneCount
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + tbSpeedRuneCount
                    + EnumChatFormatting.GRAY);
            info.add(
                EnumChatFormatting.BLUE + "Speed Bonus from Tier: " + EnumChatFormatting.GOLD + getTierSpeedBonus());
            info.add(
                EnumChatFormatting.BLUE + "Speed Bonus from Rune: "
                    + EnumChatFormatting.GOLD
                    + getSpeedRuneSpeedBonus());
        });
    }

    // region STRUCTURES
    private static IStructureDefinition<TST_BloodyHell> StructureDef = null;
    // spotless:off

    /**
     *
     * @param mode 0 is Base Structure, 1 is Fluid Structure
     * @param tier tier of the machine
     * @param offset 0,1,2 corresponds to a, b, c of Structure Printer
     * @return horizontalOffset, verticalOffset, depthOffset
     */
    private int getOffset(int mode, int tier, int offset) {
        int[][] Offsets= {offsetA,offsetB,offsetC};
        if (mode > 0) {
            if (tier > 5) {
                return Offsets[offset][7];
            } else if (tier > 2) {
                return Offsets[offset][6];
            }
        } else
            return Offsets[offset][tier - 1];
        return 0;
    }

    private static final int BLOOD_AMOUNT_NEEDED_1 = 324_000;
    private static final int BLOOD_AMOUNT_NEEDED_2 = 2244_000;

    private static final int[] offsetA = {6,6,23,23,23,23,20,20};
    private static final int[] offsetB = {6,6,23,23,23,23,20,20};
    private static final int[] offsetC = {0,0,0,0,1,19,-4,19};
    // offsets 6,0,6
    private static final String[][] STRUCTURE_TIER_1 = new String[][]{
        {"             ","             ","             ","             ","             ","    AAAAA    "},
        {"     ABA     ","      A      ","             ","             ","             ","  AAAAAAAA   "},
        {"    AABAA    ","      A      ","             ","             ","             "," AAAAAAAAAA  "},
        {"   ANNNNNA   ","    AAAAA    ","      A      ","             ","    AAAAA    "," AAAAAAAAAAA "},
        {"  ANNNNNNNA  ","   AAAAAAA   ","      A      ","      A      ","   AGGAGGA   ","AAAAAAAAAAAAA"},
        {" AANNBBBNNAA ","   AAAAAAA   ","             ","      A      ","   AGAAAGA   ","AAAAAAAAAAAAA"},
        {" BBNNB~BNNBB "," AAAAAAAAAAA ","   AA   AA   ","    AAAAA    ","   AAAAAAA   ","AAAAAAAAAAAAA"},
        {" AANNBBBNNAA ","   AAAAAAA   ","             ","      A      ","   AGAAAGA   ","AAAAAAAAAAAAA"},
        {"  ANNNNNNNA  ","   AAAAAAA   ","      A      ","      A      ","   AGGAGGA   ","AAAAAAAAAAAAA"},
        {"   ANNNNNA   ","    AAAAA    ","      A      ","             ","    AAAAA    "," AAAAAAAAAAA "},
        {"    AABAA    ","      A      ","             ","             ","             "," AAAAAAAAAAA "},
        {"     ABA     ","      A      ","             ","             ","             ","  AAAAAAAAA  "},
        {"             ","             ","             ","             ","             ","    AAAAA    "}
    };
    private static final String[][] STRUCTURE_TIER_2 = new String[][]{
        {"             ","             ","             ","             ","             ","    AAAAA    "},
        {"     ABA     ","      A      ","             ","             ","             ","  AAAAAAAAA  "},
        {"    AABAA    ","      A      ","             ","             ","      A      "," AAAAAAAAAAA "},
        {"   ANNNNNA   ","    AAAAA    ","    G A G    ","    G G G    ","    AAAAA    "," AAAAAAAAAAA "},
        {"  ANNNNNNNA  ","   AAAAAAA   ","   GE A EG   ","   GE A EG   ","   AGGAGGA   ","AAAAAAAAAAAAA"},
        {" AANNBBBNNAA ","   AAAAAAA   ","             ","      A      ","   AGAAAGA   ","AAAAAAAAAAAAA"},
        {" BBNNB~BNNBB "," AAAAAAAAAAA ","   AA   AA   ","   GAAAAAG   ","  AAAAAAAAA  ","AAAAAAAAAAAAA"},
        {" AANNBBBNNAA ","   AAAAAAA   ","             ","      A      ","   AGAAAGA   ","AAAAAAAAAAAAA"},
        {"  ANNNNNNNA  ","   AAAAAAA   ","   GE A EG   ","   GE A EG   ","   AGGAGGA   ","AAAAAAAAAAAAA"},
        {"   ANNNNNA   ","    AAAAA    ","    G A G    ","    G G G    ","    AAAAA    "," AAAAAAAAAAA "},
        {"    AABAA    ","      A      ","             ","             ","      A      "," AAAAAAAAAAA "},
        {"     ABA     ","      A      ","             ","             ","             ","  AAAAAAAAA  "},
        {"             ","             ","             ","             ","             ","    AAAAA    "}
    };
    private static final String[][] STRUCTURE_TIER_3 = new String[][]{
        {"                                               ","                                               ","                                               ","                                               ","                   A       A                   ","                   AAAAAAAAA                   "},
        {"                                               ","                                               ","                                               ","                                               ","                   AAAAAAAAA                   ","               AAAAAAAAAAAAAAAAA               "},
        {"                                               ","                                               ","                                               ","                                               ","               AAAAABBBBBBBAAAAA               ","             AAAAAAAAAAAAAAAAAAAAA             "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","             AA     AAAAAAA     AA             ","           AAAAAAAAAAAAAAAAAAAAAAAAA           "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","           AA                     AA           ","          AAAAAAAAAAAAAAAAAAAAAAAAAAA          "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","          A        AAAAAAAAA        A          ","        AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA        "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","         A      AAA  AAAAA  AAA      A         ","       AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA       "},
        {"                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","        A     AA               AA     A        ","      AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA      "},
        {"                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","       A     A                   A     A       ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","      A    AA                     AA    A      ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","     A    A                         A    A     ","    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA    "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","    A    A                           A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                     ABBBA                     ","                      AAA                      ","                                               ","    A    A                           A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                     ABBBA                     ","                      AAA                      ","                                               ","   A    A                             A    A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","                     ABBBA                     ","                      AAA                      ","                                               ","   A   A                               A   A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","                      ABA                      ","                       A                       ","                                               ","  A    A                               A    A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                      ABA                      ","                       A                       ","                                               ","  A   A                                 A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                      ABA                      ","                       A                       ","                       G                       ","                       A                       ","  A   A                                 A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                      ABA                      ","                       A                       ","                       G                       ","                       G                       ","  A   A                A                A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                     AABAA                     ","                       A                       ","                       G                       ","                       G                       ","AAA  A                 A                 A  AAA","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                    ANNNNNA                    ","                     AAAAA                     ","       AA            GGAGG            AA       ","   AAAA              GGGGG              AAAA   "," ABA A               AAAAA               A ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                   ANNNNNNNA                   ","            AAA     AAAAAAA     AAA            ","       BBAAA        GEFAFEG        AAABB       ","   BBBBAA           GEFAFEG           AABBBB   "," ABA AA             AGGAGGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                 AAANNBBBNNAAA                 ","            BBBAA   AAAAAAA   AABBB            ","       BBBBBAAA     GFFHFFG     AAABBBBB       ","   BBBBAAAAA        GFFAFFG        AAAAABBBB   "," ABA AA             AGAAAGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                 BBBNNB~BNNBBB                 ","            BBBBBAAAAAAAAAAAAABBBBB            ","       BBBBBAAAAAGGGAAHKHAAGGGAAAAABBBBB       ","   BBBBAAAAA     AGGGAAAAAGGGA     AAAAABBBB   "," ABA AA           AAAAAAAAAAA           AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                 AAANNBBBNNAAA                 ","            BBBAA   AAAAAAA   AABBB            ","       BBBBBAAA     GFFHFFG     AAABBBBB       ","   BBBBAAAAA        GFFAFFG        AAAAABBBB   "," ABA AA             AGAAAGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                   ANNNNNNNA                   ","            AAA     AAAAAAA     AAA            ","       BBAAA        GEFAFEG        AAABB       ","   BBBBAA           GEFAFEG           AABBBB   "," ABA AA             AGGAGGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                    ANNNNNA                    ","                     AAAAA                     ","       AA            GGAGG            AA       ","   AAAA              GGGGG              AAAA   "," ABA A               AAAAA               A ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                     AABAA                     ","                       A                       ","                       G                       ","                       G                       ","AAA  A                 A                 A  AAA","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                      ABA                      ","                       A                       ","                       G                       ","                       G                       ","  A   A                A                A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                      ABA                      ","                       A                       ","                       G                       ","                       A                       ","  A   A                                 A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                      ABA                      ","                       A                       ","                                               ","  A   A                                 A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                      ABA                      ","                       A                       ","                                               ","  A    A                               A    A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                     ABBBA                     ","                      AAA                      ","                                               ","   A   A                               A   A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","                     ABBBA                     ","                      AAA                      ","                                               ","   A    A                             A    A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","                     ABBBA                     ","                      AAA                      ","                                               ","    A    A                           A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","    A    A                           A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","     A    A                         A    A     ","    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA    "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","      A    AA                     AA    A      ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","       A     A                   A     A       ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","        A     AA               AA     A        ","      AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA      "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","         A      AAA  AAAAA  AAA      A         ","       AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA       "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","          A        AAAAAAAAA        A          ","        AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA        "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","           AA                     AA           ","          AAAAAAAAAAAAAAAAAAAAAAAAAAA          "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","             AA     AAAAAAA     AA             ","           AAAAAAAAAAAAAAAAAAAAAAAAA           "},
        {"                                               ","                                               ","                                               ","                                               ","               AAAAABBBBBBBAAAAA               ","             AAAAAAAAAAAAAAAAAAAAA             "},
        {"                                               ","                                               ","                                               ","                                               ","                   AAAAAAAAA                   ","               AAAAAAAAAAAAAAAAA               "},
        {"                                               ","                                               ","                                               ","                                               ","                   A       A                   ","                   AAAAAAAAA                   "}
    };
    private static final String[][] STRUCTURE_TIER_4 = new String[][]{
        {"                                               ","                                               ","                                               ","                                               ","                   A       A                   ","                   AAAAAAAAA                   "},
        {"                                               ","                                               ","                                               ","                                               ","                   AAAAAAAAA                   ","               AAAAAAAAAAAAAAAAA               "},
        {"                                               ","                                               ","                                               ","                                               ","               AAAAABBBBBBBAAAAA               ","             AAAAAAAAAAAAAAAAAAAAA             "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","             AA     AAAAAAA     AA             ","           AAAAAAAAAAAAAAAAAAAAAAAAA           "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","           AA                     AA           ","          AAAAAAAAAAAAAAAAAAAAAAAAAAA          "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","          A        AAAAAAAAA        A          ","        AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA        "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","         A      AAA  AAAAA  AAA      A         ","       AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA       "},
        {"                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","        A     AA               AA     A        ","      AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA      "},
        {"                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","       A     A                   A     A       ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","      A    AA                     AA    A      ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","     A    A                         A    A     ","    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA    "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","    A    A   AAA               AAA   A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                     ABBBA                     ","             GGG      AAA      GGG             ","             GGG               GGG             ","    A    A  AAAAA             AAAAA  A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                     ABBBA                     ","            G   G     AAA     G   G            ","            GAAAG             GAAAG            ","   A    A  AAIIIAA           AAIIIAA  A    A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","                     ABBBA                     ","            G   G     AAA     G   G            ","            GAMAG             GAMAG            ","   A   A   AAIIIAA           AAIIIAA   A   A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","                      ABA                      ","            G   G      A      G   G            ","            GAAAG             GAAAG            ","  A    A   AAIIIAA           AAIIIAA   A    A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                      ABA                      ","             GGG       A       GGG             ","             GGG               GGG             ","  A   A     AAAAA             AAAAA     A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                      ABA                      ","                       A                       ","                       G                       ","                       A                       ","  A   A      AAA               AAA      A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                      ABA                      ","                       A                       ","                       G                       ","                       G                       ","  A   A                A                A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                     AABAA                     ","                       A                       ","                       G                       ","                       G                       ","AAA  A                 A                 A  AAA","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                    ANNNNNA                    ","                     AAAAA                     ","       AA            GGAGG            AA       ","   AAAA              GGGGG              AAAA   "," ABA A               AAAAA               A ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                   ANNNNNNNA                   ","            AAA     AAAAAAA     AAA            ","       BBAAA        GDEAEDG        AAABB       ","   BBBBAA           GDEAEDG           AABBBB   "," ABA AA             AGGAGGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                 AAANNBBBNNAAA                 ","            BBBAA   AAAAAAA   AABBB            ","       BBBBBAAA     GEEHEEG     AAABBBBB       ","   BBBBAAAAA        GEEAEEG        AAAAABBBB   "," ABA AA             AGAAAGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                 BBBNNB~BNNBBB                 ","            BBBBBAAAAAAAAAAAAABBBBB            ","       BBBBBAAAAAGGGAAHKHAAGGGAAAAABBBBB       ","   BBBBAAAAA     AGGGAAAAAGGGA     AAAAABBBB   "," ABA AA           AAAAAAAAAAA           AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                 AAANNBBBNNAAA                 ","            BBBAA   AAAAAAA   AABBB            ","       BBBBBAAA     GEEHEEG     AAABBBBB       ","   BBBBAAAAA        GEEAEEG        AAAAABBBB   "," ABA AA             AGAAAGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                   ANNNNNNNA                   ","            AAA     AAAAAAA     AAA            ","       BBAAA        GDEAEDG        AAABB       ","   BBBBAA           GDEAEDG           AABBBB   "," ABA AA             AGGAGGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                    ANNNNNA                    ","                     AAAAA                     ","       AA            GGAGG            AA       ","   AAAA              GGGGG              AAAA   "," ABA A               AAAAA               A ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                     AABAA                     ","                       A                       ","                       G                       ","                       G                       ","AAA  A                 A                 A  AAA","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                      ABA                      ","                       A                       ","                       G                       ","                       G                       ","  A   A                A                A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                      ABA                      ","                       A                       ","                       G                       ","                       A                       ","  A   A      AAA               AAA      A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                      ABA                      ","             GGG       A       GGG             ","             GGG               GGG             ","  A   A     AAAAA             AAAAA     A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                      ABA                      ","            G   G      A      G   G            ","            GAAAG             GAAAG            ","  A    A   AAIIIAA           AAIIIAA   A    A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                     ABBBA                     ","            G   G     AAA     G   G            ","            GAMAG             GAMAG            ","   A   A   AAIIIAA           AAIIIAA   A   A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","                     ABBBA                     ","            G   G     AAA     G   G            ","            GAAAG             GAAAG            ","   A    A  AAIIIAA           AAIIIAA  A    A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","                     ABBBA                     ","             GGG      AAA      GGG             ","             GGG               GGG             ","    A    A  AAAAA             AAAAA  A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","    A    A   AAA               AAA   A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","     A    A                         A    A     ","    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA    "},
        {"                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","      A    AA                     AA    A      ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","       A     A                   A     A       ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","        A     AA               AA     A        ","      AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA      "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","         A      AAA  AAAAA  AAA      A         ","       AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA       "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","          A        AAAAAAAAA        A          ","        AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA        "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","           AA                     AA           ","          AAAAAAAAAAAAAAAAAAAAAAAAAAA          "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","             AA     AAAAAAA     AA             ","           AAAAAAAAAAAAAAAAAAAAAAAAA           "},
        {"                                               ","                                               ","                                               ","                                               ","               AAAAABBBBBBBAAAAA               ","             AAAAAAAAAAAAAAAAAAAAA             "},
        {"                                               ","                                               ","                                               ","                                               ","                   AAAAAAAAA                   ","               AAAAAAAAAAAAAAAAA               "},
        {"                                               ","                                               ","                                               ","                                               ","                   A       A                   ","                   AAAAAAAAA                   "}
    };
    private static final String[][] STRUCTURE_TIER_5 = new String[][]{
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                   A       A                   ","                   AAAAAAAAA                   "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                   AAAAAAAAA                   ","               AAAAAAAAAAAAAAAAA               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","               AAAAABBBBBBBAAAAA               ","             AAAAAAAAAAAAAAAAAAAAA             "},
        {"                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","             AA     AAAAAAA     AA             ","           AAAAAAAAAAAAAAAAAAAAAAAAA           "},
        {"                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","           AA                     AA           ","          AAAAAAAAAAAAAAAAAAAAAAAAAAA          "},
        {"                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","          A        AAAAAAAAA        A          ","        AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA        "},
        {"                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","         A      AAA  AAAAA  AAA      A         ","       AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA       "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","        A     AA               AA     A        ","      AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA      "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","       A     A                   A     A       ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","      A    AA                     AA    A      ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","     A    A                         A    A     ","    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA    "},
        {"                                               ","                                               ","                                               ","             A A     ABBBA     A A             ","             A A      AAA      A A             ","    A    A   AAA               AAA   A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                                               ","             AGA     ABBBA     AGA             ","             GGG      AAA      GGG             ","             GGG               GGG             ","    A    A  AAAAA             AAAAA  A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"             AGA               AGA             ","             AGA               AGA             ","            ADJDA    ABBBA    ADJDA            ","           AGDDDGA    AAA    AGDDDGA           ","           AGAAAGA           AGAAAGA           ","   A    A  AAIIIAA           AAIIIAA  A    A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"             G G               G G             ","             G G               G G             ","            GJ JG    ABBBA    GJ JG            ","            GD DG     AAA     GD DG            ","            GAMAG             GAMAG            ","   A   A   AAIIIAA           AAIIIAA   A   A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"             AGA               AGA             ","             AGA               AGA             ","            ADJDA     ABA     ADJDA            ","           AGDDDGA     A     AGDDDGA           ","           AGAAAGA           AGAAAGA           ","  A    A   AAIIIAA           AAIIIAA   A    A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                                               ","             AGA      ABA      AGA             ","             GGG       A       GGG             ","             GGG               GGG             ","  A   A     AAAAA             AAAAA     A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                      ABA                      ","                       A                       ","             A A       G       A A             ","             A A       A       A A             ","  A   A      AAA               AAA      A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                      ABA                      ","                       A                       ","                       G                       ","                       G                       ","  A   A                A                A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                     AABAA                     ","                       A                       ","                       G                       ","                       G                       ","AAA  A                 A                 A  AAA","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                                               ","                    ANNNNNA                    ","                     AAAAA                     ","       AA            GGAGG            AA       ","   AAAA              GGGGG              AAAA   "," ABA A               AAAAA               A ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                                               ","                   ANNNNNNNA                   ","            AAA     AAAAAAA     AAA            ","       BBAAA        GEFAFEG        AAABB       ","   BBBBAA           GEFAFEG           AABBBB   "," ABA AA             AGGAGGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                                               ","                 AAANNBBBNNAAA                 ","            BBBAA   AAAAAAA   AABBB            ","       BBBBBAAA     GFFHFFG     AAABBBBB       ","   BBBBAAAAA        GFFAFFG        AAAAABBBB   "," ABA AA             AGAAAGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                                               ","                 BBBNNB~BNNBBB                 ","            BBBBBAAAAAAAAAAAAABBBBB            ","       BBBBBAAAAAGGGAAHKHAAGGGAAAAABBBBB       ","   BBBBAAAAA     AGGGAAAAAGGGA     AAAAABBBB   "," ABA AA           AAAAAAAAAAA           AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                                               ","                 AAANNBBBNNAAA                 ","            BBBAA   AAAAAAA   AABBB            ","       BBBBBAAA     GFFHFFG     AAABBBBB       ","   BBBBAAAAA        GFFAFFG        AAAAABBBB   "," ABA AA             AGAAAGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                                               ","                   ANNNNNNNA                   ","            AAA     AAAAAAA     AAA            ","       BBAAA        GEFAFEG        AAABB       ","   BBBBAA           GEFAFEG           AABBBB   "," ABA AA             AGGAGGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                                               ","                    ANNNNNA                    ","                     AAAAA                     ","       AA            GGAGG            AA       ","   AAAA              GGGGG              AAAA   "," ABA A               AAAAA               A ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                                               ","                     AABAA                     ","                       A                       ","                       G                       ","                       G                       ","AAA  A                 A                 A  AAA","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"                                               ","                      ABA                      ","                       A                       ","                       G                       ","                       G                       ","  A   A                A                A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                      ABA                      ","                       A                       ","             A A       G       A A             ","             A A       A       A A             ","  A   A      AAA               AAA      A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","                                               ","             AGA      ABA      AGA             ","             GGG       A       GGG             ","             GGG               GGG             ","  A   A     AAAAA             AAAAA     A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"             AGA               AGA             ","             AGA               AGA             ","            ADJDA     ABA     ADJDA            ","           AGDDDGA     A     AGDDDGA           ","           AGAAAGA           AGAAAGA           ","  A    A   AAIIIAA           AAIIIAA   A    A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"             G G               G G             ","             G G               G G             ","            GJ JG    ABBBA    GJ JG            ","            GD DG     AAA     GD DG            ","            GAMAG             GAMAG            ","   A   A   AAIIIAA           AAIIIAA   A   A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"             AGA               AGA             ","             AGA               AGA             ","            ADJDA    ABBBA    ADJDA            ","           AGDDDGA    AAA    AGDDDGA           ","           AGAAAGA           AGAAAGA           ","   A    A  AAIIIAA           AAIIIAA  A    A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","                                               ","             AGA     ABBBA     AGA             ","             GGG      AAA      GGG             ","             GGG               GGG             ","    A    A  AAAAA             AAAAA  A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                                               ","                                               ","             A A     ABBBA     A A             ","             A A      AAA      A A             ","    A    A   AAA               AAA   A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"                                               ","                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","     A    A                         A    A     ","    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA    "},
        {"                                               ","                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","      A    AA                     AA    A      ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","       A     A                   A     A       ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","        A     AA               AA     A        ","      AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA      "},
        {"                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","         A      AAA  AAAAA  AAA      A         ","       AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA       "},
        {"                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","          A        AAAAAAAAA        A          ","        AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA        "},
        {"                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","           AA                     AA           ","          AAAAAAAAAAAAAAAAAAAAAAAAAAA          "},
        {"                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","             AA     AAAAAAA     AA             ","           AAAAAAAAAAAAAAAAAAAAAAAAA           "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","               AAAAABBBBBBBAAAAA               ","             AAAAAAAAAAAAAAAAAAAAA             "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                   AAAAAAAAA                   ","               AAAAAAAAAAAAAAAAA               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                   A       A                   ","                   AAAAAAAAA                   "}
    };
    private static final String[][] STRUCTURE_TIER_6 = new String[][]{
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                   A       A                   ","                   AAAAAAAAA                   "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                   AAAAAAAAA                   ","               AAAAAAAAAAAAAAAAA               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","               AAAAABBBBBBBAAAAA               ","             AAAAAAAAAAAAAAAAAAAAA             "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","             AA     AAAAAAA     AA             ","           AAAAAAAAAAAAAAAAAAAAAAAAA           "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","           AA                     AA           ","          AAAAAAAAAAAAAAAAAAAAAAAAAAA          "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","          A        AAAAAAAAA        A          ","        AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA        "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","         A      AAA  AAAAA  AAA      A         ","       AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA       "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","        A     AA               AA     A        ","      AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA      "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","       A     A                   A     A       ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"            AA AA             AA AA            ","             AAA              AAAA             ","            GGGGG             GGGGG            ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","      A    AA                     AA    A      ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"           A     A           A     A           ","            AAAAA             AAAAA            ","           GAAAAAG           GAAAAAG           ","             AGA               AGA             ","             AGA               AGA             ","              G                 G              ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","     A    A                         A    A     ","    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA    "},
        {"          A       AAAAAAAAAAA       A          ","           A     AAA       AAA     A           ","          GAAAAAAAGGG     GGGAAAAAAAG          ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","             AGA               AGA             ","              G                 G              ","              G                 G              ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","             A A     ABBBA     A A             ","             A A      AAA      A A             ","    A    A   AAA               AAA   A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"         A                           A         ","          A         AAAAAAA         A          ","         GAAA     AAAGGGGGAAA     AAAG         ","           AAAAAAAAGGAAAAAGGAAAAAAAA           ","           AACCCCAGAA     AAGAACCCAA           ","           AACCCAGA         AGACCCAA           ","            AAAAGA           AGAAAA            ","            AAAGA             AGAAA            ","            AAAGA             AGAAA            ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","             AGA     ABBBA     AGA             ","             GGG      AAA      GGG             ","             GGG               GGG             ","    A    A  AAAAA             AAAAA  A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"         A                           A         ","         AA                         AA         ","         GAA         AAAAA         AAG         ","          AAAAAAAAAAACCCCCAAAAAAAAAAA          ","          AACLLLCCCCCAAAAACCCCCLLLCAA          ","           ACCCCCCCAA     AACCCCCCCA           ","           AACCCCCA         ACCCCCAA           ","            ACCCCA           ACCCCA            ","            ACCCCA           ACCCCA            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","            ADJDA    ABBBA    ADJDA            ","           AGDDDGA    AAA    AGDDDGA           ","           AGAAAGA           AGAAAGA           ","   A    A  AAIIIAA           AAIIIAA  A    A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"                                               ","         AA                         AA         ","         GAA         AAAAA         AAG         ","          GAAA AAAAAALLLLLAAAAAA AAAG          ","          GACL LLLLLLGGGGGLLLLLL LCAG          ","          GACC CCCCGG     GGCCCC CCAG          ","           GAC CCCG         GCCC CAG           ","           GAC CCG           GCC CAG           ","           GAC CCG           GCC CAG           ","            GA AG             GA AG            ","            GA AG             GA AG            ","            GA AG             GA AG            ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","            GJ JG    ABBBA    GJ JG            ","            GD DG     AAA     GD DG            ","            GAMAG             GAMAG            ","   A   A   AAIIIAA           AAIIIAA   A   A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"         A                           A         ","         AA                         AA         ","         GAA         AAAAA         AAG         ","          AAAAAAAAAAACCCCCAAAAAAAAAAA          ","          AACLLLCCCCCAAAAACCCCCLLLCAA          ","           ACCCCCCCAA     AACCCCCCCA           ","           AACCCCCA         ACCCCCAA           ","            GCCCCA           ACCCCG            ","            GCCCCA           ACCCCG            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","            ADJDA     ABA     ADJDA            ","           AGDDDGA     A     AGDDDGA           ","           AGAAAGA           AGAAAGA           ","  A    A   AAIIIAA           AAIIIAA   A    A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"         A                           A         ","          A          AAAAA          A          ","         GAA       AAGGGGGAA       AAG         ","           AAAAAAAACCAAAAACCAAAAAAAA           ","           AACLCCCCAA     AACCCCLCCA           ","           AACCCCCA         ACCCCCAA           ","            GCCCCA           ACCCCG            ","            ACCCA             ACCCA            ","            ACCCA             ACCCA            ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","             AGA      ABA      AGA             ","             GGG       A       GGG             ","             GGG               GGG             ","  A   A     AAAAA             AAAAA     A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"          A          AAAAA          A          ","           A       AAAAAAAAA       A           ","          GA      AGG     GGA      AG          ","            AAAAAACAA     AACAAAAAA            ","            ACLCCCA         ACCCLCA            ","            GCCCCA           ACCCCG            ","            ACCCA             ACCCA            ","             AGA               AGA             ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      ABA                      ","                       A                       ","             A A       G       A A             ","             A A       A       A A             ","  A   A      AAA               AAA      A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"           A        A     A        A           ","           A      AAA     AAA      A           ","           GA    AG         GA    AG           ","            AAAAACA         ACAAAAA            ","            GCLCCA           ACCLCG            ","            ACCCA             ACCCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      ABA                      ","                       A                       ","                       G                       ","                       G                       ","  A   A                A                A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"           A                       A           ","           A     AA         AA     A           ","           GA   AG           GA   AG           ","            GAAACA           ACAAAG            ","            ACLCA             ACLCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     AABAA                     ","                       A                       ","                       G                       ","                       G                       ","AAA  A                 A                 A  AAA","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"           A      A         A      A           ","            A    AA         AA    A            ","           GA   AG           GA   AG           ","            GAAACA           ACAAAG            ","            ACLCA             ACLCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ANNNNNA                    ","                     AAAAA                     ","       AA            GGAGG            AA       ","   AAAA              GGGGG              AAAA   "," ABA A               AAAAA               A ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"           A     A           A     A           ","            A   AA           AA   A            ","            GAAAG             GAAAG            ","            ACLCA             ACLCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                   ANNNNNNNA                   ","            AAA     AAAAAAA     AAA            ","       BBAAA        GEFAFEG        AAABB       ","   BBBBAA           GEFAFEG           AABBBB   "," ABA AA             AGGAGGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"           A     A           A     A           ","            A   AA           AA   A            ","            GAAAG             GAAAG            ","            ACLCA             ACLCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                 AAANNBBBNNAAA                 ","            BBBAA   AAAAAAA   AABBB            ","       BBBBBAAA     GFFHFFG     AAABBBBB       ","   BBBBAAAAA        GFFAFFG        AAAAABBBB   "," ABA AA             AGAAAGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"           A     A           A     A           ","            A   AA           AA   A            ","            GAAAG             GAAAG            ","            ACLCA             ACLCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                 BBBNNB~BNNBBB                 ","            BBBBBAAAAAAAAAAAAABBBBB            ","       BBBBBAAAAAGGGAAHKHAAGGGAAAAABBBBB       ","   BBBBAAAAA     AGGGAAAAAGGGA     AAAAABBBB   "," ABA AA           AAAAAAAAAAA           AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"           A     A           A     A           ","            A   AA           AA   A            ","            GAAAG             GAAAG            ","            ACLCA             ACLCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                 AAANNBBBNNAAA                 ","            BBBAA   AAAAAAA   AABBB            ","       BBBBBAAA     GFFHFFG     AAABBBBB       ","   BBBBAAAAA        GFFAFFG        AAAAABBBB   "," ABA AA             AGAAAGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"           A     A           A     A           ","            A   AA           AA   A            ","            GAAAG             GAAAG            ","            ACLCA             ACLCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                   ANNNNNNNA                   ","            AAA     AAAAAAA     AAA            ","       BBAAA        GEFAFEG        AAABB       ","   BBBBAA           GEFAFEG           AABBBB   "," ABA AA             AGGAGGA             AA ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"           A      A         A      A           ","            A    AA         AA    A            ","           GA   AG           GA   AG           ","            GAAACA           ACAAAG            ","            ACLCA             ACLCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ANNNNNA                    ","                     AAAAA                     ","       AA            GGAGG            AA       ","   AAAA              GGGGG              AAAA   "," ABA A               AAAAA               A ABA ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"           A                       A           ","           A     AA         AA     A           ","           GA   AG           GA   AG           ","            GAAACA           ACAAAG            ","            ACLCA             ACLCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     AABAA                     ","                       A                       ","                       G                       ","                       G                       ","AAA  A                 A                 A  AAA","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"},
        {"           A        A     A        A           ","           A      AAA     AAA      A           ","           GA    AG         GA    AG           ","            AAAAACA         ACAAAAA            ","            GCLCCA           ACCLCG            ","            ACCCA             ACCCA            ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      ABA                      ","                       A                       ","                       G                       ","                       G                       ","  A   A                A                A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"          A          AAAAA          A          ","           A       AAAAAAAAA       A           ","          GA      AGG     GGA      AG          ","            AAAAAACAA     AACAAAAAA            ","            ACLCCCA         ACCCLCA            ","            GCCCCA           ACCCCG            ","            ACCCA             ACCCA            ","             AGA               AGA             ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      ABA                      ","                       A                       ","             A A       G       A A             ","             A A       A       A A             ","  A   A      AAA               AAA      A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"         A                           A         ","          A          AAAAA          A          ","         GAA       AAGGGGGAA       AAG         ","           AAAAAAAACCAAAAACCAAAAAAAA           ","           ACCLCCCCAA     AACCCCLCAA           ","           AACCCCCA         ACCCCCAA           ","            GCCCCA           ACCCCG            ","            ACCCA             ACCCA            ","            ACCCA             ACCCA            ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","             AGA      ABA      AGA             ","             GGG       A       GGG             ","             GGG               GGG             ","  A   A     AAAAA             AAAAA     A   A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"         A                           A         ","         AA                         AA         ","         GAA         AAAAA         AAG         ","          AAAAAAAAAAACCCCCAAAAAAAAAAA          ","          AACLLLCCCCCAAAAACCCCCLLLCAA          ","           ACCCCCCCAA     AACCCCCCCA           ","           AACCCCCA         ACCCCCAA           ","            GCCCCA           ACCCCG            ","            GCCCCA           ACCCCG            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","            ADJDA     ABA     ADJDA            ","           AGDDDGA     A     AGDDDGA           ","           AGAAAGA           AGAAAGA           ","  A    A   AAIIIAA           AAIIIAA   A    A  "," AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "},
        {"                                               ","         AA                         AA         ","         GAA         AAAAA         AAG         ","          GAAA AAAAAALLLLLAAAAAA AAAG          ","          GACL LLLLLLGGGGGLLLLLL LCAG          ","          GACC CCCCGG     GGCCCC CCAG          ","           GAC CCCG         GCCC CAG           ","           GAC CCG           GCC CAG           ","           GAC CCG           GCC CAG           ","            GA AG             GA AG            ","            GA AG             GA AG            ","            GA AG             GA AG            ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","             G G               G G             ","            GJ JG    ABBBA    GJ JG            ","            GD DG     AAA     GD DG            ","            GAMAG             GAMAG            ","   A   A   AAIIIAA           AAIIIAA   A   A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"         A                           A         ","         AA                         AA         ","         GAA         AAAAA         AAG         ","          AAAAAAAAAAACCCCCAAAAAAAAAAA          ","          AACLLLCCCCCAAAAACCCCCLLLCAA          ","           ACCCCCCCAA     AACCCCCCCA           ","           AACCCCCA         ACCCCCAA           ","            ACCCCA           ACCCCA            ","            ACCCCA           ACCCCA            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","            ADJDA    ABBBA    ADJDA            ","           AGDDDGA    AAA    AGDDDGA           ","           AGAAAGA           AGAAAGA           ","   A    A  AAIIIAA           AAIIIAA  A    A   ","  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "},
        {"         A                           A         ","          A         AAAAAAA         A          ","         GAAA     AAAGGGGGAAA     AAAG         ","           AAAAAAAAGGAAAAAGGAAAAAAAA           ","           AACCCAAGAA     AAGACCCCAA           ","           AACCCAGA         AGACCCAA           ","            AAAAGA           AGAAAA            ","            AAAGA             AGAAA            ","            AAAGA             AGAAA            ","             AGA               AGA             ","             AGA               AGA             ","             AGA               AGA             ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","             AGA     ABBBA     AGA             ","             GGG      AAA      GGG             ","             GGG               GGG             ","    A    A  AAAAA             AAAAA  A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"          A       AAAAAAAAAAA       A          ","           A     AAA       AAA     A           ","          GAAAAAAAGGG     GGGAAAAAAAG          ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","            AAAAA             AAAAA            ","             AGA               AGA             ","              G                 G              ","              G                 G              ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","             A A     ABBBA     A A             ","             A A      AAA      A A             ","    A    A   AAA               AAA   A    A    ","   AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   "},
        {"           A     A           A     A           ","            AAAAA             AAAAA            ","           GAAAAAG           GAAAAAG           ","             AGA               AGA             ","             AGA               AGA             ","              G                 G              ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","     A    A                         A    A     ","    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA    "},
        {"            AA AA             AA AA            ","             AAAA              AAA             ","            GGGGG             GGGGG            ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     ABBBA                     ","                      AAA                      ","      A    AA                     AA    A      ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","       A     A                   A     A       ","     AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","                     AAAAA                     ","        A     AA               AA     A        ","      AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA      "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","         A      AAA  AAAAA  AAA      A         ","       AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA       "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","          A        AAAAAAAAA        A          ","        AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA        "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","           AA                     AA           ","          AAAAAAAAAAAAAAAAAAAAAAAAAAA          "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    ABBBBBA                    ","             AA     AAAAAAA     AA             ","           AAAAAAAAAAAAAAAAAAAAAAAAA           "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","               AAAAABBBBBBBAAAAA               ","             AAAAAAAAAAAAAAAAAAAAA             "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                   AAAAAAAAA                   ","               AAAAAAAAAAAAAAAAA               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                   A       A                   ","                   AAAAAAAAA                   "}
    };
    private static final String[][] STRUCTURE_BLOOD_1 = new String[][]{
        {"            ZZZZZ       ZZZZZ            "},
        {"          ZZZZZZZZZZZZZZZZZZZZZ          "},
        {"        ZZZZZZZZ         ZZZZZZZZ        "},
        {"       ZZZZZZ               ZZZZZZ       "},
        {"      ZZZZZ                   ZZZZZ      "},
        {"     ZZZZZ                     ZZZZZ     "},
        {"    ZZZZ                         ZZZZ    "},
        {"   ZZZZ                           ZZZZ   "},
        {"  ZZZZ                             ZZZZ  "},
        {"  ZZZZ                             ZZZZ  "},
        {" ZZZZ                               ZZZZ "},
        {" ZZZ                                 ZZZ "},
        {"ZZZZ                                 ZZZZ"},
        {"ZZZ                                   ZZZ"},
        {"ZZZ                                   ZZZ"},
        {"ZZZ                                   ZZZ"},
        {"ZZ                                     ZZ"},
        {" Z                                     Z "},
        {" Z                                     Z "},
        {" Z                                     Z "},
        {" Z                                     Z "},
        {" Z                                     Z "},
        {" Z                                     Z "},
        {" Z                                     Z "},
        {"ZZ                                     ZZ"},
        {"ZZZ                                   ZZZ"},
        {"ZZZ                                   ZZZ"},
        {"ZZZ                                   ZZZ"},
        {"ZZZZ                                 ZZZZ"},
        {" ZZZ                                 ZZZ "},
        {" ZZZZ                               ZZZZ "},
        {"  ZZZZ                             ZZZZ  "},
        {"  ZZZZ                             ZZZZ  "},
        {"   ZZZZ                           ZZZZ   "},
        {"    ZZZZ                         ZZZZ    "},
        {"     ZZZZZ                     ZZZZZ     "},
        {"      ZZZZZ                   ZZZZZ      "},
        {"       ZZZZZZ               ZZZZZZ       "},
        {"        ZZZZZZZZ         ZZZZZZZZ        "},
        {"          ZZZZZZZZZZZZZZZZZZZZZ          "},
        {"            ZZZZZ       ZZZZZ            "}
    };
    private static final String[][] STRUCTURE_BLOOD_2 = new String[][]{
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","            ZZZZZ       ZZZZZ            "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","          ZZZZZZZZZZZZZZZZZZZZZ          "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","        ZZZZZZZZ         ZZZZZZZZ        "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","       ZZZZZZ   ZZ     ZZ   ZZZZZZ       "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","      ZZZZZ  ZZZZZZZZZZZZZZZ  ZZZZZ      "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","     ZZZZZ ZZZZZZZZZZZZZZZZZZZ ZZZZZ     "},
        {"           Z                 Z           ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","    ZZZZ  ZZZZZZZZZZZZZZZZZZZZZ  ZZZZ    "},
        {"         ZZZZZ             ZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","   ZZZZ ZZZZZZZZZZZZZZZZZZZZZZZZZ ZZZZ   "},
        {"        ZZZZZZZ           ZZZZZZZ        ","         ZZZZZ             ZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","  ZZZZ ZZZ   ZZZZZZZZZZZZZZZ   ZZZ ZZZZ  "},
        {"       ZZZZZZZZZZZZZZZZZZZZZZZZZZZ       ","        ZZZZZZZZZ       ZZZZZZZZZ        ","          ZZZZZ           ZZZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","  ZZZZ ZZ     ZZZZZZZZZZZZZ     ZZ ZZZZ  "},
        {"       ZZZZZZZZZZZZZZZZZZZZZZZZZZZ       ","        ZZZZZZZZZZZZZZZZZZZZZZZZZ        ","         ZZZZZZZZZ     ZZZZZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         "," ZZZZ ZZ       ZZZZZZZZZZZ       ZZ ZZZZ "},
        {"      ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ      ","        ZZZZZZZZZZZZZZZZZZZZZZZZZ        ","         ZZZZZZZZZ     ZZZZZZZZZ         ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","                                         "," ZZZ ZZZ       ZZZZZZZZZZZ       ZZZ ZZZ "},
        {"       ZZZZZZZZZZZZZZZZZZZZZZZZZZZ       ","        ZZZZZZZZZZZZZZZZZZZZZZZZZ        ","         ZZZZZZZZZ     ZZZZZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZZZ ZZZ       ZZZZZZZZZZZ       ZZZ ZZZZ"},
        {"       ZZZZZZZZZZZZZZZZZZZZZZZZZZZ       ","        ZZZZZZZZZZ     ZZZZZZZZZZ        ","         ZZZZZZZ         ZZZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZZ ZZZZZ     ZZZZZZZZZZZZZ     ZZZZZ ZZZ"},
        {"        ZZZZZZZZZZ     ZZZZZZZZZZ        ","         ZZZZZZZ         ZZZZZZZ         ","         ZZZZZZ           ZZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZZ ZZZZZZ   ZZZZZZZZZZZZZZZ   ZZZZZZ ZZZ"},
        {"         ZZZZZZZZ       ZZZZZZZZ         ","         ZZZZZZ           ZZZZZZ         ","          ZZZZ             ZZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZZ ZZZZZZZZZZZZZZZZ ZZZZZZZZZZZZZZZZ ZZZ"},
        {"         ZZZZZZZ         ZZZZZZZ         ","         ZZZZZ             ZZZZZ         ","          ZZZ               ZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZ ZZZZZZZZZZZZZZZZZ ZZZZZZZZZZZZZZZZZ ZZ"},
        {"         ZZZZZZ           ZZZZZZ         ","          ZZZZ             ZZZZ          ","          ZZZ               ZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         "," Z ZZZZZZZZZZZZZZZ     ZZZZZZZZZZZZZZZ Z "},
        {"         ZZZZZ             ZZZZZ         ","          ZZZ               ZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         "," Z  ZZZZZZZZZZZZZ       ZZZZZZZZZZZZZ  Z "},
        {"         ZZZZZ             ZZZZZ         ","          ZZZ               ZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         "," Z  ZZZZZZZZZZZZZ       ZZZZZZZZZZZZZ  Z "},
        {"         ZZZZZ             ZZZZZ         ","          ZZZ               ZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         "," Z  ZZZZZZZZZZZ           ZZZZZZZZZZZ  Z "},
        {"         ZZZZZ             ZZZZZ         ","          ZZZ               ZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         "," Z  ZZZZZZZZZZZZZ       ZZZZZZZZZZZZZ  Z "},
        {"         ZZZZZ             ZZZZZ         ","          ZZZ               ZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         "," Z  ZZZZZZZZZZZZZ       ZZZZZZZZZZZZZ  Z "},
        {"         ZZZZZZ           ZZZZZZ         ","          ZZZZ             ZZZZ          ","          ZZZ               ZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         "," Z ZZZZZZZZZZZZZZZ     ZZZZZZZZZZZZZZZ Z "},
        {"         ZZZZZZZ         ZZZZZZZ         ","         ZZZZZ             ZZZZZ         ","          ZZZ               ZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZ ZZZZZZZZZZZZZZZZZ ZZZZZZZZZZZZZZZZZ ZZ"},
        {"         ZZZZZZZZ       ZZZZZZZZ         ","         ZZZZZZ           ZZZZZZ         ","          ZZZZ             ZZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZZ ZZZZZZZZZZZZZZZZ ZZZZZZZZZZZZZZZZ ZZZ"},
        {"        ZZZZZZZZZZ     ZZZZZZZZZZ        ","         ZZZZZZZ         ZZZZZZZ         ","         ZZZZZZ           ZZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZZ ZZZZZZ   ZZZZZZZZZZZZZZZ   ZZZZZZ ZZZ"},
        {"       ZZZZZZZZZZZZZZZZZZZZZZZZZZZ       ","        ZZZZZZZZZZ     ZZZZZZZZZZ        ","         ZZZZZZZ         ZZZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZZ ZZZZZ     ZZZZZZZZZZZZZ     ZZZZZ ZZZ"},
        {"       ZZZZZZZZZZZZZZZZZZZZZZZZZZZ       ","        ZZZZZZZZZZZZZZZZZZZZZZZZZ        ","         ZZZZZZZZZ     ZZZZZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","ZZZZ ZZZ       ZZZZZZZZZZZ       ZZZ ZZZZ"},
        {"      ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ      ","        ZZZZZZZZZZZZZZZZZZZZZZZZZ        ","         ZZZZZZZZZ     ZZZZZZZZZ         ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","           Z                 Z           ","                                         "," ZZZ ZZZ       ZZZZZZZZZZZ       ZZZ ZZZ "},
        {"       ZZZZZZZZZZZZZZZZZZZZZZZZZZZ       ","        ZZZZZZZZZZZZZZZZZZZZZZZZZ        ","         ZZZZZZZZZ     ZZZZZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         "," ZZZZ ZZ       ZZZZZZZZZZZ       ZZ ZZZZ "},
        {"       ZZZZZZZZZZZZZZZZZZZZZZZZZZZ       ","        ZZZZZZZZZ       ZZZZZZZZZ        ","          ZZZZZ           ZZZZZ          ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","  ZZZZ ZZ     ZZZZZZZZZZZZZ     ZZ ZZZZ  "},
        {"        ZZZZZZZ           ZZZZZZZ        ","         ZZZZZ             ZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","  ZZZZ ZZZ   ZZZZZZZZZZZZZZZ   ZZZ ZZZZ  "},
        {"         ZZZZZ             ZZZZZ         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","   ZZZZ ZZZZZZZZZZZZZZZZZZZZZZZZZ ZZZZ   "},
        {"           Z                 Z           ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","    ZZZZ  ZZZZZZZZZZZZZZZZZZZZZ  ZZZZ    "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","     ZZZZZ ZZZZZZZZZZZZZZZZZZZ ZZZZZ     "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","      ZZZZZ  ZZZZZZZZZZZZZZZ  ZZZZZ      "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","       ZZZZZZ   ZZ     ZZ   ZZZZZZ       "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","        ZZZZZZZZ         ZZZZZZZZ        "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","          ZZZZZZZZZZZZZZZZZZZZZ          "},
        {"                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","                                         ","            ZZZZZ       ZZZZZ            "}
    };
    // spotless:on

    // endregion

    // region Texture
    private static CustomIcon BH_1_Active;
    private static CustomIcon BH_1;
    private static CustomIcon BH_2_Active;
    private static CustomIcon BH_2;
    private static CustomIcon BH_3_Active;
    private static CustomIcon BH_3;
    private static CustomIcon BH_4_Active;
    private static CustomIcon BH_4;
    private static CustomIcon BH_5_Active;
    private static CustomIcon BH_5;
    private static CustomIcon BH_6_Active;
    private static CustomIcon BH_6;
    private static CustomIcon BH_7_Active;
    private static CustomIcon BH_7;
    private static CustomIcon BH_8_Active;
    private static CustomIcon BH_8;
    private static CustomIcon BH_9_Active;
    private static CustomIcon BH_9;

    private static IIconContainer[] BloodyHellIcons;
    private static IIconContainer[] BloodyHellIconsActive;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        BH_1_Active = new CustomIcon("gtnhcommunitymod:iconSets/BloodHellActive_1");
        BH_1 = new CustomIcon("gtnhcommunitymod:iconSets/BloodHell_1");
        BH_2_Active = new CustomIcon("gtnhcommunitymod:iconSets/BloodHellActive_2");
        BH_2 = new CustomIcon("gtnhcommunitymod:iconSets/BloodHell_2");
        BH_3_Active = new CustomIcon("gtnhcommunitymod:iconSets/BloodHellActive_3");
        BH_3 = new CustomIcon("gtnhcommunitymod:iconSets/BloodHell_3");
        BH_4_Active = new CustomIcon("gtnhcommunitymod:iconSets/BloodHellActive_4");
        BH_4 = new CustomIcon("gtnhcommunitymod:iconSets/BloodHell_4");
        BH_5_Active = new CustomIcon("gtnhcommunitymod:iconSets/BloodHellActive_5");
        BH_5 = new CustomIcon("gtnhcommunitymod:iconSets/BloodHell_5");
        BH_6_Active = new CustomIcon("gtnhcommunitymod:iconSets/BloodHellActive_6");
        BH_6 = new CustomIcon("gtnhcommunitymod:iconSets/BloodHell_6");
        BH_7_Active = new CustomIcon("gtnhcommunitymod:iconSets/BloodHellActive_7");
        BH_7 = new CustomIcon("gtnhcommunitymod:iconSets/BloodHell_7");
        BH_8_Active = new CustomIcon("gtnhcommunitymod:iconSets/BloodHellActive_8");
        BH_8 = new CustomIcon("gtnhcommunitymod:iconSets/BloodHell_8");
        BH_9_Active = new CustomIcon("gtnhcommunitymod:iconSets/BloodHellActive_9");
        BH_9 = new CustomIcon("gtnhcommunitymod:iconSets/BloodHell_9");
        BloodyHellIcons = new IIconContainer[] { BH_1, BH_2, BH_3, BH_4, BH_5, BH_6, BH_7, BH_8, BH_9, BH_1, BH_2, BH_3,
            BH_4, BH_5, BH_6, BH_7, BH_8, BH_9, BH_1, BH_2, BH_3, BH_4, BH_5, BH_6, BH_7 };
        BloodyHellIconsActive = new IIconContainer[] { BH_1_Active, BH_2_Active, BH_3_Active, BH_4_Active, BH_5_Active,
            BH_6_Active, BH_7_Active, BH_8_Active, BH_9_Active, BH_1_Active, BH_2_Active, BH_3_Active, BH_4_Active,
            BH_5_Active, BH_6_Active, BH_7_Active, BH_8_Active, BH_9_Active, BH_1_Active, BH_2_Active, BH_3_Active,
            BH_4_Active, BH_5_Active, BH_6_Active, BH_7_Active };
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        ITexture base = casingTexturePages[115][MetaBlockCasing02.getTextureIndexInPage(0)];
        ITexture[] FACING_ACTIVE = { TextureFactory.of(base), TextureFactory.builder()
            .addIcon(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW)
            .extFacing()
            .glow()
            .build() };
        ITexture[] FACING_SIDE = { TextureFactory.of(base) };
        if (side == facing) {
            return FACING_ACTIVE;
        }
        return FACING_SIDE;
    }

    // endregion

    // region render

    public boolean isNewStyleRendering() {
        return true;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mFormed = (aValue & 0x2) != 0;
        super.onValueUpdate(aValue);
    }

    @Override
    public byte getUpdateData() {
        return (byte) (mMachine ? 2 : 0);
    }

    @Override
    public boolean renderInWorld(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer) {
        if (!isNewStyleRendering() || !mFormed) return false;
        int[] tABCCoord = new int[] { -1, -1, 0 };
        int[] tXYZOffset = new int[3];
        final IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        boolean mActive = aBaseMetaTileEntity.isActive();
        final ForgeDirection tFacing = aBaseMetaTileEntity.getFrontFacing();
        final ExtendedFacing tExtendedFacing = getExtendedFacing();
        final ForgeDirection tDirection = tExtendedFacing.getDirection();
        final LightingHelper tLighting = new LightingHelper(aRenderer);

        if (tFacing == ForgeDirection.NORTH || tFacing == ForgeDirection.EAST) aRenderer.field_152631_f = true;
        final Block tBlock = MetaBlockCasing02;

        IIconContainer[] tTextures;
        if (mActive) tTextures = BloodyHellIconsActive;
        else tTextures = BloodyHellIcons;
        assert tTextures != null && tTextures.length == tABCCoord.length;

        for (int i = 0; i < 9; i++) {
            tExtendedFacing.getWorldOffset(tABCCoord, tXYZOffset);
            int tX = tXYZOffset[0] + aX;
            int tY = tXYZOffset[1] + aY;
            int tZ = tXYZOffset[2] + aZ;

            Tessellator.instance.setBrightness(
                tBlock.getMixedBrightnessForBlock(
                    aWorld,
                    aX + tDirection.offsetX,
                    tY + tDirection.offsetY,
                    aZ + tDirection.offsetZ));
            tLighting.setupLighting(tBlock, tX, tY, tZ, tFacing)
                .setupColor(tFacing, Dyes._NULL.mRGBa);
            GTRenderUtil.renderBlockIcon(
                aRenderer,
                tBlock,
                tX + tDirection.offsetX * 0.01,
                tY + tDirection.offsetY * 0.01,
                tZ + tDirection.offsetZ * 0.01,
                tTextures[i].getIcon(),
                tFacing);

            if (++tABCCoord[0] == 2) {
                tABCCoord[0] = -1;
                tABCCoord[1]++;
            }
        }

        aRenderer.field_152631_f = false;
        return false;
    }

    // endregion
}
