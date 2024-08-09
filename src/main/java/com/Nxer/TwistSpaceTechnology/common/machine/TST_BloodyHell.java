package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellAlchemicTierKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellTierKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeResult.ResultInsufficientTier;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;
import com.Nxer.TwistSpaceTechnology.util.InfoDataHelper;
import com.Nxer.TwistSpaceTechnology.util.StructuralStringArrayBuilder;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.dreammaster.block.BlockList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import WayofTime.alchemicalWizardry.ModBlocks;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class TST_BloodyHell extends GTCM_MultiMachineBase<TST_BloodyHell> implements ISurvivalConstructable {

    private static IStructureDefinition<TST_BloodyHell> StructureDef;

    private static final ITexture[] FACING_ACTIVE = {
        TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE), TextureFactory.builder()
            .addIcon(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW)
            .glow()
            .build() };

    private static final int MODE_ALTAR = 0;
    private static final int MODE_ALCHEMIC = 1;
    private static final int MODE_BINDING = 2;

    private static final int[] MODES = new int[] { MODE_ALTAR, MODE_ALCHEMIC, MODE_BINDING };

    private int mode = MODE_ALTAR;

    /**
     * the machine tier, it is always less than the altar tier by 1.
     *
     * @see #getAltarTier()
     */
    private int tier = 0;

    private int speedRuneCount = 0;

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

    public int getAltarTier() {
        return this.tier + 1;
    }

    public int getOrbTier() {
        return BloodMagicHelper.getBloodOrbTier(getControllerSlot());
    }

    public int getActivationCrystalTier() {
        return BloodMagicHelper.getActivationCrystalTier(getControllerSlot());
    }

    public double getSpeedRuneModifier() {
        return 0.0; // TODO
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 0;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        var tt = new GT_Multiblock_Tooltip_Builder();
        // #tr Tooltip_BloodyHell_MachineType
        // # Altar of Blood | Alchemic Chemistry Set | Ritual of Binding
        // #zh_CN 血之祭坛 | 炼金术台 | 绑定仪式
        tt.addMachineType(TextEnums.tr("Tooltip_BloodyHell_MachineType"))
            // #tr Tooltip_BloodyHell_0
            // # After years of researching, you finally find the last piece to mass-produce the Blood Magic things!
            // #zh_CN 经过多年的研究，你终于找到了量产血魔法物品的最后一块拼图！
            .addInfo(TextEnums.tr("Tooltip_BloodyHell_0"))
            .addSeparator()
            .addInfo(textScrewdriverChangeMode)
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addController(textUseBlueprint)
            // TODO: add structure tooltip after done with the structure
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        // TODO: texture
        return FACING_ACTIVE;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        speedRuneCount = 0;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_1, 1, 0, 1)) {
            return false;
        }

        // when the machine structure completes, it begins at 2
        tier = 1;
        if (checkPiece(STRUCTURE_PIECE_2, 3, 1, 3)) {
            tier = 2;
            if (checkPiece(STRUCTURE_PIECE_3, 5, 2, 5)) {
                tier = 3;
                if (checkPiece(STRUCTURE_PIECE_4, 8, -3, 8)) {
                    tier = 4;
                    if (checkPiece(STRUCTURE_PIECE_5, 11, 3, 11)) {
                        tier = 5;
                    }
                }
            }
        }

        // TODO: add rune check maybe?
        return true;
    }

    // note that the actual altar tier is machine structure tier + 1!
    // which means, the machine structure tier starts at 2 at the view of the altar

    /** offsets 1,0,1 */
    private static final String STRUCTURE_PIECE_1 = "tier1";
    /** offsets 3,1,3 */
    private static final String STRUCTURE_PIECE_2 = "tier2";
    /** offset 5,2,5 */
    private static final String STRUCTURE_PIECE_3 = "tier3";
    /** offset 8,-3,8 */
    private static final String STRUCTURE_PIECE_4 = "tier4";
    /** offset 11,3,11 */
    private static final String STRUCTURE_PIECE_5 = "tier5";

    // region STRUCTURES
    // spotless:off

    // offsets 1,0,1
    private static final String[][] STRUCTURE_TIER_1 = new String[][] { { "   ", " ~ ", "   " },
        { "RRR", "R R", "RRR" } };
    // offsets 3,1,3
    private static final String[][] STRUCTURE_TIER_2 = new String[][] {
        { "G     G", "       ", "       ", "       ", "       ", "       ", "G     G" },
        { "A     A", "       ", "       ", "       ", "       ", "       ", "A     A" },
        { "A     A", "       ", "       ", "       ", "       ", "       ", "A     A" },
        { " RRRRR ", "R     R", "R     R", "R     R", "R     R", "R     R", " RRRRR " } };
    // offset 5,2,5
    private static final String[][] STRUCTURE_TIER_3 = StructuralStringArrayBuilder.ofArrays(
        StructuralStringArrayBuilder.of("B         B", 9, "           ", "B         B"),
        4,
        StructuralStringArrayBuilder.of("V         V", 9, "           ", "V         V"),
        StructuralStringArrayBuilder.of("  RRRRRRR  ", "           ", 7, "R         R", "           ", "  RRRRRRR  "));
    // offset 8,-3,8
    private static final String[][] STRUCTURE_TIER_4 = StructuralStringArrayBuilder.ofArrays(
        StructuralStringArrayBuilder.of("N               N", 15, "                 ", "N               N"),
        StructuralStringArrayBuilder.of("  RRRRRRRRRRRRR  ",
            "                 ",
            13,
            "R               R",
            "                 ",
            "  RRRRRRRRRRRRR  "));
    // offset 11,3,11
    private static final String[][] STRUCTURE_TIER_5 = StructuralStringArrayBuilder.ofArrays(
        StructuralStringArrayBuilder.of("C                     C",
            21,
            "                       ",
            "C                     C"),
        7,
        StructuralStringArrayBuilder.of("I                     I",
            21,
            "                       ",
            "I                     I"),
        StructuralStringArrayBuilder.of("  RRRRRRRRRRRRRRRRRRR  ",
            "                       ",
            19,
            "R                     R",
            "                       ",
            "  RRRRRRRRRRRRRRRRRRR  "));

    // spotless:on
    // endregion

    @Override
    public IStructureDefinition<TST_BloodyHell> getStructureDefinition() {
        if (StructureDef == null) {
            StructureDef = StructureDefinition.<TST_BloodyHell>builder()
                .addShape(STRUCTURE_PIECE_1, transpose(STRUCTURE_TIER_1))
                .addShape(STRUCTURE_PIECE_2, transpose(STRUCTURE_TIER_2))
                .addShape(STRUCTURE_PIECE_3, transpose(STRUCTURE_TIER_3))
                .addShape(STRUCTURE_PIECE_4, transpose(STRUCTURE_TIER_4))
                .addShape(STRUCTURE_PIECE_5, transpose(STRUCTURE_TIER_5))
                .addElement(
                    'R', // rune or hatches
                    ofChain(
                        ofBlockAnyMeta(ModBlocks.bloodRune),
                        onElementPass((x) -> { x.speedRuneCount += 1; }, ofBlockAnyMeta(ModBlocks.speedRune)),
                        GT_HatchElementBuilder.<TST_BloodyHell>builder()
                            .atLeast(InputBus, InputHatch, OutputBus)
                            .adder(TST_BloodyHell::addToMachineList)
                            .dot(1)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .build()))
                .addElement('A', ofBlockAnyMeta(BlockList.BloodyThaumium.getBlock()))
                .addElement('G', ofBlockAnyMeta(Blocks.glowstone))
                .addElement('B', ofBlockAnyMeta(ModBlocks.bloodStoneBrick))
                .addElement('V', ofBlockAnyMeta(BlockList.BloodyVoid.getBlock()))
                .addElement('N', ofBlockAnyMeta(Blocks.beacon))
                .addElement('C', ofBlockAnyMeta(ModBlocks.blockCrystal))
                .addElement('I', ofBlockAnyMeta(BlockList.BloodyIchorium.getBlock()))
                .build();
        }
        return StructureDef;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        // buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 0, 2);
        var tier = stackSize.stackSize;
        buildPiece(STRUCTURE_PIECE_1, stackSize, hintsOnly, 1, 0, 1);
        if (tier > 1) {
            buildPiece(STRUCTURE_PIECE_2, stackSize, hintsOnly, 3, 1, 3);
        }
        if (tier > 2) {
            buildPiece(STRUCTURE_PIECE_3, stackSize, hintsOnly, 5, 2, 5);
        }
        if (tier > 3) {
            buildPiece(STRUCTURE_PIECE_4, stackSize, hintsOnly, 8, -3, 8);
        }
        if (tier > 4) {
            buildPiece(STRUCTURE_PIECE_5, stackSize, hintsOnly, 11, 3, 11);
        }
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return super.getStructureDescription(stackSize);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        var size = stackSize.stackSize;
        var blocksBuilt = 0;
        if (size > 0) {
            blocksBuilt += this
                .survivialBuildPiece(STRUCTURE_PIECE_1, stackSize, 1, 0, 1, elementBudget, env, false, true);
            if (size > 1) {
                blocksBuilt += this
                    .survivialBuildPiece(STRUCTURE_PIECE_2, stackSize, 3, 1, 3, elementBudget, env, false, true);
                if (size > 2) {
                    blocksBuilt += this
                        .survivialBuildPiece(STRUCTURE_PIECE_3, stackSize, 5, 2, 5, elementBudget, env, false, true);
                    if (size > 3) {
                        blocksBuilt += this.survivialBuildPiece(
                            STRUCTURE_PIECE_4,
                            stackSize,
                            8,
                            -3,
                            8,
                            elementBudget,
                            env,
                            false,
                            true);
                        if (size > 4) {
                            blocksBuilt += this.survivialBuildPiece(
                                STRUCTURE_PIECE_5,
                                stackSize,
                                11,
                                3,
                                11,
                                elementBudget,
                                env,
                                false,
                                true);
                        }
                    }
                }
            }
        }
        return blocksBuilt;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % MODES.length;

            // #tr BloodyHell.modeMsg.0
            // # Altar Mode
            // #zh_CN 祭坛模式

            // #tr BloodyHell.modeMsg.1
            // # Alchemic Chemistry Mode
            // #zh_CN 炼金模式

            // #tr BloodyHell.modeMsg.2
            // # Binding Ritual Mode
            // #zh_CN 绑定仪式模式

            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("BloodyHell.modeMsg." + this.mode));
        }
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(
            GTCMRecipe.BloodyHellRecipes,
            GTCMRecipe.BloodyHellRecipe_Alchemic,
            GTCMRecipe.BloodyHellRecipe_Binding);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (mode) {
            case MODE_ALTAR -> {
                return GTCMRecipe.BloodyHellRecipes;
            }
            case MODE_ALCHEMIC -> {
                return GTCMRecipe.BloodyHellRecipe_Alchemic;
            }
            case MODE_BINDING -> {
                return GTCMRecipe.BloodyHellRecipe_Binding;
            }
        }
        return GTCMRecipe.BloodyHellRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                // check altar tier
                int requiredTier = recipe.getMetadataOrDefault(BloodyHellTierKey.INSTANCE, 0);
                if (requiredTier > getAltarTier()) {
                    return ResultInsufficientTier.ofBloodAltar(requiredTier);
                }

                // check blood orb tier
                int requiredOrbTier = recipe.getMetadataOrDefault(BloodyHellAlchemicTierKey.INSTANCE, 0);
                if (requiredOrbTier > getOrbTier()) {
                    return ResultInsufficientTier.ofBloodOrb(requiredOrbTier);
                }

                // check weak activation crystal
                if (mode == MODE_BINDING && getActivationCrystalTier() < 1) {
                    return ResultInsufficientTier.ofActivationCrystal(1);
                }

                return super.validateRecipe(recipe);
            }
        };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("mode", this.mode);
        aNBT.setInteger("tier", this.tier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mode = aNBT.getInteger("mode");
        tier = aNBT.getInteger("tier");
    }

    @Override
    public String[] getInfoData() {
        return InfoDataHelper.buildInfoData(super.getInfoData(), (info) -> {
            info.add(
                EnumChatFormatting.BLUE + "Machine Mode: "
                    + EnumChatFormatting.GOLD
                    + StatCollector.translateToLocal("BloodyHell.modeMsg." + this.mode));
            info.add(
                EnumChatFormatting.BLUE + "Machine Tier: "
                    + EnumChatFormatting.GOLD
                    + this.tier
                    + EnumChatFormatting.GRAY
                    + " ("
                    + EnumChatFormatting.BLUE
                    + "Altar Tier: "
                    + EnumChatFormatting.GOLD
                    + this.getAltarTier()
                    + EnumChatFormatting.GRAY
                    + ")");
            info.add(EnumChatFormatting.BLUE + "Speed Rune Count: " + EnumChatFormatting.GOLD + speedRuneCount);
        });
    }
}
