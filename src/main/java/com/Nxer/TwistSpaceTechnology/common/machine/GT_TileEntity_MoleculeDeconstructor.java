package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_PerPiece_MoleculeDeconstructor;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MultiplyPerTier_MoleculeDeconstructor;
import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.internal_structure_issue;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.MachineTooltip.textScrewdriverChangeMode;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.structure.error.StructureErrorRegistry.ENERGY_TIER_EXCEED_GLASS;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
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
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

@SkipGenerateDescription
public class GT_TileEntity_MoleculeDeconstructor extends GTCM_MultiMachineBase<GT_TileEntity_MoleculeDeconstructor>
    implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GT_TileEntity_MoleculeDeconstructor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public GT_TileEntity_MoleculeDeconstructor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MoleculeDeconstructor(this.mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "mainMoleculeDeconstructor";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleMoleculeDeconstructor";
    private static final String STRUCTURE_PIECE_END = "endMoleculeDeconstructor";
    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 9;
    private final int depthOffSet = 0;
    private static IStructureDefinition<GT_TileEntity_MoleculeDeconstructor> STRUCTURE_DEFINITION = null;

    // spotless:off
	private final String[][] shapeMain = new String[][]{
	    {"               ","IIIIIIIIIIIIIII","               ","IIIIIIIIIIIIIII"},
	    {"               ","I     FFF     I","      FFF      ","I     FFF     I"},
	    {"               ","I     DDD     I","      DED      ","I     DDD     I"},
	    {"               ","I             I","       B       ","I             I"},
	    {"               ","I             I","       B       ","I             I"},
	    {"               ","I             I","       B       ","I             I"},
	    {"      HHH      ","IFD   AAA   DFI"," FD   AEA   DF ","IFD   AAA   DFI"},
	    {"      HHH      ","IFD   A A   DFI"," FEBBBE EBBBEF ","IFD   A A   DFI"},
	    {"      HHH      ","IFD   CCC   DFI"," FD   CCC   DF ","IFD   CCC   DFI"},
	    {"      G~G      ","CCD         DCC","CCD         DCC","CCD         DCC"}
	};

	private final String[][] shapeMiddle = new String[][]{
	    {"               ","IIIIIIIIIIIIIII","               ","IIIIIIIIIIIIIII"},
	    {"               ","I     FFF     I","      FFF      ","I     FFF     I"},
	    {"               ","I     DDD     I","      DED      ","I     DDD     I"},
	    {"               ","I             I","       B       ","I             I"},
	    {"               ","I             I","       B       ","I             I"},
	    {"               ","I             I","       B       ","I             I"},
	    {"      AAA      ","IFD   AAA   DFI"," FD   AEA   DF ","IFD   AAA   DFI"},
	    {"      A A      ","IFD   A A   DFI"," FEBBBE EBBBEF ","IFD   A A   DFI"},
	    {"      CCC      ","IFD   CCC   DFI"," FD   CCC   DF ","IFD   CCC   DFI"},
	    {"               ","CCD         DCC","CCD         DCC","CCD         DCC"}
	};

	private final String[][] shapeEnd = new String[][]{
	    {"               "},
	    {"               "},
	    {"               "},
	    {"               "},
	    {"               "},
	    {"               "},
	    {"      FFF      "},
	    {"      FFF      "},
	    {"      FFF      "},
	    {"      GGG      "}
	};
    // spotless:on

    @Override
    public IStructureDefinition<GT_TileEntity_MoleculeDeconstructor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MoleculeDeconstructor>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addShape(STRUCTURE_PIECE_MIDDLE, transpose(shapeMiddle))
                .addShape(STRUCTURE_PIECE_END, transpose(shapeEnd))
                .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 15))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings4, 14))
                .addElement(
                    'D',
                    HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
                        .hint(1)
                        .casingIndex(1024)
                        .buildAndChain(sBlockCasingsTT, 0))
                .addElement('E', ofBlock(sBlockCasingsTT, 8))
                .addElement(
                    'F',
                    HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
                        .atLeast(OutputBus, OutputHatch)
                        .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
                        .hint(2)
                        .casingIndex(62)
                        .buildAndChain(GregTechAPI.sBlockCasings4, 14))
                .addElement(
                    'G',
                    HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
                        .atLeast(Maintenance)
                        .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
                        .hint(3)
                        .casingIndex(62)
                        .buildAndChain(GregTechAPI.sBlockCasings4, 14))
                .addElement(
                    'H',
                    HatchElementBuilder.<GT_TileEntity_MoleculeDeconstructor>builder()
                        .atLeast(InputBus, InputHatch)
                        .adder(GT_TileEntity_MoleculeDeconstructor::addToMachineList)
                        .hint(4)
                        .casingIndex(62)
                        .buildAndChain(GregTechAPI.sBlockCasings4, 14))
                .addElement('I', ofFrame(Materials.CosmicNeutronium))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
        int piece = stackSize.stackSize;
        for (int i = 1; i < piece; i++) {
            this.buildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                hintsOnly,
                horizontalOffSet,
                verticalOffSet,
                depthOffSet - i * 4);
        }
        this.buildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            hintsOnly,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet - piece * 4);
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
        if (piece > 1) {
            for (int i = 1; i < piece; i++) {
                built[i] = survivalBuildPiece(
                    STRUCTURE_PIECE_MIDDLE,
                    stackSize,
                    horizontalOffSet,
                    verticalOffSet,
                    depthOffSet - i * 4,
                    elementBudget,
                    env,
                    false,
                    true);
            }
        }

        built[piece + 1] += survivalBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet - piece * 4,
            elementBudget,
            env,
            false,
            true);

        return TSTUtils.multiBuildPiece(built);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        this.glassTier = -1;
        this.piece = 1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) {
            return;
        }

        while (checkPiece(STRUCTURE_PIECE_MIDDLE, horizontalOffSet, verticalOffSet, depthOffSet - piece * 4, errors)) {
            this.piece++;
        }

        errors.clear();

        if (!checkPiece(STRUCTURE_PIECE_END, horizontalOffSet, verticalOffSet, depthOffSet - piece * 4, errors)) {
            return;
        }

        if (this.glassTier <= 0) {
            // there shouldn't be
            errors.add(internal_structure_issue);
            return;
        }

        if (glassTier < 12) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (this.glassTier < hatch.mTier) {
                    errors.add(ENERGY_TIER_EXCEED_GLASS);
                    return;
                }
            }
        }

        speedBonus = (float) (Math.pow(SpeedBonus_MultiplyPerTier_MoleculeDeconstructor, getTotalPowerTier()));

    }
    // endregion

    // region Processing Logic
    private int glassTier = -1;
    private int piece = 1;

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_CHEMBATH, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR };

    @Override
    public RecipeMap<?> getRecipeMap() {
        return switch (machineMode) {
            case 1 -> RecipeMaps.centrifugeNonCellRecipes;
            default -> RecipeMaps.electrolyzerNonCellRecipes;
        };
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.centrifugeNonCellRecipes, RecipeMaps.electrolyzerNonCellRecipes);
    }

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Electrolyzer
         * 1 - Centrifuge
         */
        return 2;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        // #tr tst.common.machine.MoleculeDeconstructor.mode.0
        // # Mode: Electrolyzer
        // #zh_CN 电解机模式

        // #tr tst.common.machine.MoleculeDeconstructor.mode.1
        // # Mode: Centrifuge
        // #zh_CN 离心机模式
        return StatCollector.translateToLocal("tst.common.machine.MoleculeDeconstructor.mode." + machineMode);
    }

    public int getMaxParallelRecipes() {
        return Parallel_PerPiece_MoleculeDeconstructor * this.piece;
    }

    protected float getSpeedBonus() {
        return speedBonus;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return piece >= PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor;
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
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(62), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(62), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(62) };
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.MoleculeDeconstructor.tooltip.machine_type
        // # Electrolyzer | Centrifuge
        // #zh_CN 电解机 | 离心机
        tt.addMachineType(TSTUtils.tr("tst.common.machine.MoleculeDeconstructor.tooltip.machine_type"))
            // #tr tst.common.machine.MoleculeDeconstructor.tooltip.info.01
            // # Controller block for the Molecule Deconstructor
            // #zh_CN 分子解构器的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.MoleculeDeconstructor.tooltip.info.01"))
            // #tr tst.common.machine.MoleculeDeconstructor.tooltip.info.02
            // # {\AQUA}The lightning seemed to roll down a ladder.
            // #zh_CN {\AQUA}雷电好像从一架梯子上滚下来.
            .addInfo(TSTUtils.tr("tst.common.machine.MoleculeDeconstructor.tooltip.info.02"))
            // #tr tst.common.machine.MoleculeDeconstructor.tooltip.info.03
            // # Separate the molecules one by one with tweezers.
            // #zh_CN 用镊子将分子一个一个一个分开.
            .addInfo(TSTUtils.tr("tst.common.machine.MoleculeDeconstructor.tooltip.info.03"))
            // #tr tst.common.machine.MoleculeDeconstructor.tooltip.info.04
            // # Extra {\AQUA}24x{\GRAY} Parallel per Piece. {\GOLD}16{\GRAY} Piece enable Perfect Overclock.
            // #zh_CN 每层提供{\AQUA}24x{\GRAY}并行. {\GOLD}16{\GRAY}层启用无损超频.
            .addInfo(TSTUtils.tr("tst.common.machine.MoleculeDeconstructor.tooltip.info.04"))
            // #tr tst.common.machine.MoleculeDeconstructor.tooltip.info.05
            // # Additional {\RED}10%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
            // #zh_CN 电压每提高1级, 额外降低{\RED}10%{\GRAY}配方耗时, 叠乘计算.
            .addInfo(TSTUtils.tr("tst.common.machine.MoleculeDeconstructor.tooltip.info.05"))
            // #tr tst.common.machine.MoleculeDeconstructor.tooltip.info.06
            // # The Glass Tier limit the Energy hatch voltage Tier.
            // #zh_CN 玻璃等级限制能源仓等级.
            .addInfo(TSTUtils.tr("tst.common.machine.MoleculeDeconstructor.tooltip.info.06"))
            .addInfo(textScrewdriverChangeMode)
            .addController(textFrontBottom)
            .addInputHatch(textUseBlueprint, 4)
            .addOutputHatch(textUseBlueprint, 2)
            .addInputBus(textUseBlueprint, 4)
            .addOutputBus(textUseBlueprint, 2)
            .addEnergyHatch(textUseBlueprint, 1)
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
