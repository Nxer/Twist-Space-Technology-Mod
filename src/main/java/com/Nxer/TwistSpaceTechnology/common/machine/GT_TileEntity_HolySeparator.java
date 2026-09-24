package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.ParallelPerPiece_HolySeparator;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Piece_EnablePerfectOverclock_HolySeparator;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MultiplyPerTier_HolySeparator;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.structure.error.StructureErrorRegistry.TOO_SHORT_HEIGHT;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;
import gtPlusPlus.core.block.ModBlocks;

@SkipGenerateDescription
public class GT_TileEntity_HolySeparator extends GTCM_MultiMachineBase<GT_TileEntity_HolySeparator> {

    // region Class Constructor
    public GT_TileEntity_HolySeparator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public GT_TileEntity_HolySeparator(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_HolySeparator(this.mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "mainHolySeparator";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleHolySeparator";
    private static final String STRUCTURE_PIECE_END = "endHolySeparator";
    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 2;
    private final int depthOffSet = 0;
    private static IStructureDefinition<GT_TileEntity_HolySeparator> STRUCTURE_DEFINITION = null;

    // spotless:off
	private final String[][] shapeMain = new String[][]{
		{"               ","               ","               ","               ","               ","               ","       G       ","      GFG      ","       G       ","               ","               ","               ","               ","               ","               "},
		{"      AAA      ","      AAA      ","    AABBBAA    ","   ABBBBBBBA   ","  ABBBBBBBBBA  ","  ABBBBBBBBBA  ","AABBBBDDDBBBBAA","AABBBBDDDBBBBAA","AABBBBDDDBBBBAA","  ABBBBBBBBBA  ","  ABBBBBBBBBA  ","   ABBBBBBBA   ","    AABBBAA    ","      AAA      ","      AAA      "},
		{"      A~A      ","      AAA      ","    AABBBAA    ","   ABBBBBBBA   ","  ABBBBBBBBBA  ","  ABBBBBBBBBA  ","AABBBBBBBBBBBAA","AABBBBBBBBBBBAA","AABBBBBBBBBBBAA","  ABBBBBBBBBA  ","  ABBBBBBBBBA  ","   ABBBBBBBA   ","    AABBBAA    ","      AAA      ","      AAA      "},
		{"      AAA      ","    AAAAAAA    ","   AAABBBAAA   ","  AABBBBBBBAA  "," AABBBBBBBBBAA "," AABBBBBBBBBAA ","AABBBBBBBBBBBAA","AABBBBBBBBBBBAA","AABBBBBBBBBBBAA"," AABBBBBBBBBAA "," AABBBBBBBBBAA ","  AABBBBBBBAA  ","   AAABBBAAA   ","    AAAAAAA    ","      AAA      "}
	};

	private final String[][] shapeMiddle = new String[][]{
		{"       E       ","     AAAAA     ","               ","               ","               "," A           A "," A     G     A ","EA    GFG    AE"," A     G     A "," A           A ","               ","               ","               ","     AAAAA     ","       E       "},
		{"      ECE      ","    AAACAAA    ","       C       ","       C       "," A           A "," A           A ","EA     G     AE","CCCC  GFG  CCCC","EA     G     AE"," A           A "," A           A ","       C       ","       C       ","    AAACAAA    ","      ECE      "},
		{"       E       ","     AAAAA     ","               ","               ","               "," A           A "," A     G     A ","EA    GFG    AE"," A     G     A "," A           A ","               ","               ","               ","     AAAAA     ","       E       "},
		{"               ","               ","               ","               ","               ","               ","       G       ","      GFG      ","       G       ","               ","               ","               ","               ","               ","               "}
	};

	private final String[][] shapeEnd = new String[][]{
		{"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
		{"               ","      CCC      ","    CCDDDCC    ","   CDDDDDDDC   ","  CDDDDDDDDDC  ","  CDDDDDDDDDC  "," CDDDDDEDDDDDC "," CDDDDEEEDDDDC "," CDDDDDEDDDDDC ","  CDDDDDDDDDC  ","  CDDDDDDDDDC  ","   CDDDDDDDC   ","    CCDDDCC    ","      CCC      ","               "},
		{"               ","               ","               ","               ","               ","               ","       G       ","      GFG      ","       G       ","               ","               ","               ","               ","               ","               "},
		{"               ","               ","               ","               ","               ","               ","       G       ","      GFG      ","       G       ","               ","               ","               ","               ","               ","               "}

	};
    // spotless:on

    @Override
    public IStructureDefinition<GT_TileEntity_HolySeparator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_HolySeparator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addShape(STRUCTURE_PIECE_MIDDLE, transpose(shapeMiddle))
                .addShape(STRUCTURE_PIECE_END, transpose(shapeEnd))
                .addElement(
                    'A',
                    HatchElementBuilder.<GT_TileEntity_HolySeparator>builder()
                        .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                        .adder(GT_TileEntity_HolySeparator::addToMachineList)
                        .hint(1)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 10))
                .addElement('C', ofBlock(sBlockCasingsTT, 0))
                .addElement('D', ofBlock(sBlockCasingsTT, 4))
                .addElement('E', ofBlock(sBlockCasingsTT, 6))
                .addElement('F', ofBlock(sBlockCasingsTT, 8))
                .addElement('G', ofBlock(ModBlocks.blockCasings3Misc, 15))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
        int piece = stackSize.stackSize;
        for (int i = 1; i <= piece; i++) {
            this.buildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                hintsOnly,
                horizontalOffSet,
                verticalOffSet + i * 4,
                depthOffSet);
        }
        this.buildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            hintsOnly,
            horizontalOffSet,
            verticalOffSet + piece * 4 + 4,
            depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int[] built = new int[stackSize.stackSize + 2];

        built[0] = survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);

        int piece = stackSize.stackSize;
        for (int i = 1; i <= piece; i++) {
            built[i] = survivalBuildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                horizontalOffSet,
                verticalOffSet + i * 4,
                depthOffSet,
                elementBudget,
                env,
                false,
                true);
        }

        built[stackSize.stackSize + 1] += survivalBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            horizontalOffSet,
            verticalOffSet + piece * 4 + 4,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);

        return TSTUtils.multiBuildPiece(built);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        this.piece = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) {
            return;
        }
        while (checkPiece(
            STRUCTURE_PIECE_MIDDLE,
            horizontalOffSet,
            verticalOffSet + (this.piece + 1) * 4,
            depthOffSet,
            errors)) {
            this.piece++;
        }

        if (piece < 1) {
            errors.add(TOO_SHORT_HEIGHT);
            return;
        }

        errors.clear();

        if (!checkPiece(
            STRUCTURE_PIECE_END,
            horizontalOffSet,
            verticalOffSet + (this.piece + 1) * 4,
            depthOffSet,
            errors)) {
            return;
        }

        speedBonus = (float) (Math.pow(SpeedBonus_MultiplyPerTier_HolySeparator, getTotalPowerTier()));

        maxParallel = ParallelPerPiece_HolySeparator * piece;

    }
    // endregion

    // region Processing Logic
    private int piece = 1;

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_BENDING, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SINGULARITY };

    @Override
    public RecipeMap<?> getRecipeMap() {
        return switch (machineMode) {
            case 1 -> RecipeMaps.latheRecipes;
            default -> RecipeMaps.cutterRecipes;
        };
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.latheRecipes, RecipeMaps.cutterRecipes);
    }

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Cutting Machine
         * 1 - Lathe
         */
        return 2;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        // #tr tst.common.machine.HolySeparator.mode.0
        // # Mode: Cutting
        // #zh_CN 切割机模式

        // #tr tst.common.machine.HolySeparator.mode.1
        // # Mode: Lathe
        // #zh_CN 车床模式
        return StatCollector.translateToLocal("tst.common.machine.HolySeparator.mode." + machineMode);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return piece >= Piece_EnablePerfectOverclock_HolySeparator;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    piece >= Piece_EnablePerfectOverclock_HolySeparator ? OverclockType.PerfectOverclock
                        : OverclockType.NormalOverclock);
                return super.process();
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "Parallels: "
            + EnumChatFormatting.GOLD
            + this.getMaxParallelRecipes();
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "Speed multiplier: "
            + EnumChatFormatting.GOLD
            + this.getSpeedBonus();
        ret[origin.length + 2] = EnumChatFormatting.AQUA + "Pieces: " + EnumChatFormatting.GOLD + this.piece;
        return ret;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("piece", piece);
        aNBT.setByte("mode", (byte) machineMode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        piece = aNBT.getInteger("piece");
        machineMode = aNBT.getByte("mode");
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183) };
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.HolySeparator.tooltip.machine_type
        // # Cutter | Slicer | Lathe
        // #zh_CN 切割机 | 切片机 | 车床
        tt.addMachineType(TSTUtils.tr("tst.common.machine.HolySeparator.tooltip.machine_type"))
            // #tr tst.common.machine.HolySeparator.tooltip.info.01
            // # Controller block for the Holy Separator
            // #zh_CN 神圣分离者的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.HolySeparator.tooltip.info.01"))
            // #tr tst.common.machine.HolySeparator.tooltip.info.02
            // # {\YELLOW}Precision {\GRAY}and {\AQUA}Grace.
            // #zh_CN {\YELLOW}精准{\GRAY}而{\AQUA}优雅.
            .addInfo(TSTUtils.tr("tst.common.machine.HolySeparator.tooltip.info.02"))
            // #tr tst.common.machine.HolySeparator.tooltip.info.03
            // # Another form of laser engraving.
            // #zh_CN 激光蚀刻的另一个形式.
            .addInfo(TSTUtils.tr("tst.common.machine.HolySeparator.tooltip.info.03"))
            // #tr tst.common.machine.HolySeparator.tooltip.info.04
            // # You can even slice potato chips with this.
            // #zh_CN 你甚至可以用这机器切薯片.
            .addInfo(TSTUtils.tr("tst.common.machine.HolySeparator.tooltip.info.04"))
            // #tr tst.common.machine.HolySeparator.tooltip.info.05
            // # Extra {\AQUA}8x{\GRAY} Parallel per Piece. {\GOLD}16{\GRAY} Piece enable Perfect Overclock.
            // #zh_CN 每层提供{\AQUA}8x{\GRAY}并行. {\GOLD}16{\GRAY}层启用无损超频.
            .addInfo(TSTUtils.tr("tst.common.machine.HolySeparator.tooltip.info.05"))
            // #tr tst.common.machine.HolySeparator.tooltip.info.06
            // # Additional {\RED}10%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
            // #zh_CN 电压每提高1级, 额外降低{\RED}10%{\GRAY}配方耗时, 叠乘计算.
            .addInfo(TSTUtils.tr("tst.common.machine.HolySeparator.tooltip.info.06"))
            .addInfo(TSTSharedLocalization.MachineTooltip.textScrewdriverChangeMode)
            .addInputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addOutputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addInputBus(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addOutputBus(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addEnergyHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Hatch Registration

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    // endregion

}
