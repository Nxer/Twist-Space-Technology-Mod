package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM.HatchElement.DynamoMulti;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Dynamo;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_DRAGONEGG;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_DRAGONEGG_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TT_MultiMachineBase_EM;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.cleanroommc.modularui.utils.MathUtils;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoTunnel;
import com.google.common.collect.Lists;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;

public class GT_TileEntity_MegaEggGenerator extends TT_MultiMachineBase_EM
    implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GT_TileEntity_MegaEggGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        super.useLongPower = true;
    }

    public GT_TileEntity_MegaEggGenerator(String aName) {
        super(aName);
        super.useLongPower = true;
    }
    // endregion

    // region Processing Logic
    private int mPieces = 1;
    private int mInfinityEggs = 0;
    private int mDragonEggs = 0;
    private int mCreeperEggs = 0;
    private int mAirPosed = 0;
    private long genVol = 0L;
    private long genAmp = 0L;
    private int efficiencyIncrement = 1;

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing_EM() {
        this.mMaxProgresstime = 20;
        this.lEUt = Math.abs(genVol);
        this.eAmpereFlow = genAmp;
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
        long hatchVol = 0, hatchAmp = 0;
        for (GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
            hatchVol += tHatch.maxEUOutput();
            hatchAmp += tHatch.maxAmperesOut();
        }
        for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : eDynamoMulti) {
            hatchVol += tHatch.maxEUOutput();
            hatchAmp += tHatch.maxAmperesOut();
        }
        if (hatchVol > Integer.MAX_VALUE) hatchVol = Integer.MAX_VALUE;
        if (hatchAmp > Integer.MAX_VALUE) hatchAmp = Integer.MAX_VALUE;

        long expectedEUt = getOutputEUt();
        if (expectedEUt > hatchVol) { // the expected amp is greater than 1
            genVol = hatchVol;
            long expectedAmp = expectedEUt / hatchVol;
            genAmp = MathUtils.clamp(expectedAmp, 1, hatchAmp); // 1 <= amp <= maxHatchAmp
        } else {
            genVol = expectedEUt;
            genAmp = 1L;
        }
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
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // No need for the maintenance hatches!
        repairMachine();

        // Initialize
        this.mInfinityEggs = 0;
        this.mDragonEggs = 0;
        this.mCreeperEggs = 0;
        this.mAirPosed = 0;
        this.mPieces = 0;
        // Main checks
        return checkAllPieces() && checkInfinityEgg()
            && checkLaser()
            && checkDynamo()
            && checkAnyDynamoHatch()
            && initValues();
    }

    /**
     * Check main structure of pieces.
     *
     * @return If pieces are all set
     */
    private boolean checkAllPieces() {
        if (!checkPiece(STRUCTURE_PIECE_BASE, horizontalOffSet, verticalOffSet, depthOffSet)) {
            return false;
        }
        while (checkPiece(
            STRUCTURE_PIECE_MIDDLE,
            horizontalOffSet,
            verticalOffSet + (this.mPieces + 1) * 2,
            depthOffSet)) {
            this.mPieces++;
        }
        if (mPieces < 1) {
            return false;
        }
        return checkPiece(STRUCTURE_PIECE_TOP, horizontalOffSet, verticalOffSet + this.mPieces * 2 + 1, depthOffSet);
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
            for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : eDynamoMulti) {
                if (tHatch instanceof GT_MetaTileEntity_Hatch_DynamoTunnel) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * No matter what kind of dynamo in use, only one allowed.
     *
     * @return If dynamo is allowed
     */
    @SuppressWarnings("deprecation")
    private boolean checkDynamo() {
        return (mDynamoHatches.size() + eDynamoMulti.size()) <= ValueEnum.MEG_Dynamo_Limit;
    }

    /**
     * Must have at least one dynamo hatch
     */
    private boolean checkAnyDynamoHatch() {
        return !mDynamoHatches.isEmpty() || !eDynamoMulti.isEmpty();
    }

    /**
     * Calls for initializing the output values (volt, amp) and efficiency increment.
     */
    private boolean initValues() {
        this.updateOutput();
        this.updateEfficiencyIncrement();
        return true;
    }

    // endregion

    // region Structure
    // spotless:off
	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		this.buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
		int piece = stackSize.stackSize;
		for (int i=1; i<=piece; i++){
			this.buildPiece(STRUCTURE_PIECE_MIDDLE, stackSize, hintsOnly, horizontalOffSet, verticalOffSet + i * 2, depthOffSet);
		}
		this.buildPiece(STRUCTURE_PIECE_TOP, stackSize, hintsOnly, horizontalOffSet, verticalOffSet + piece * 2 +1, depthOffSet);
	}

	@Override
    @SuppressWarnings("deprecation")
	public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor){
		if (this.mMachine) return -1;
		int built = 0;

		built += survivialBuildPiece(
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
		for (int i=1; i<=piece; i++){
			built += survivialBuildPiece(
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

		built += survivialBuildPiece(
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

	private IStructureDefinition<GT_TileEntity_MegaEggGenerator> structureDef = null;
    private static final String STRUCTURE_PIECE_BASE = "baseEggGenerator";
	private static final String STRUCTURE_PIECE_MIDDLE = "middleEggGenerator";
	private static final String STRUCTURE_PIECE_TOP = "topEggGenerator";
	private final int horizontalOffSet = 7;
	private final int verticalOffSet = 0;
	private final int depthOffSet = 6;

    /**
     * I have to add this shit to deal with runtime conflicts that have nothing to do with TST.
     */
    private Block InfinityEgg() {
        if (Mods.Witchery.isModLoaded()) {
            return Block.getBlockFromName(Mods.Witchery.ID + ":infinityegg");
//             Witchery.Blocks.INFINITY_EGG;
        } else return BasicBlocks.PhotonControllerUpgrade;
    }

	@Override
	public IStructureDefinition<GT_TileEntity_MegaEggGenerator> getStructure_EM() {
        if (structureDef == null) {
		structureDef = StructureDefinition.<GT_TileEntity_MegaEggGenerator>builder()
			       .addShape(STRUCTURE_PIECE_BASE, transpose(shapeBase))
			       .addShape(STRUCTURE_PIECE_MIDDLE, transpose(shapeMiddle))
			       .addShape(STRUCTURE_PIECE_TOP, transpose(shapeTop))
			       .addElement('A',
                       GT_HatchElementBuilder.<GT_TileEntity_MegaEggGenerator>builder()
                                       .atLeast(Dynamo.or(DynamoMulti))
                           .adder(GT_TileEntity_MegaEggGenerator::addToMachineList)
                                       .casingIndex(1536)
                                       .dot(1)
                                       .buildAndChain(Loaders.magicCasing, 0))
			       .addElement('B',ofFrame(Materials.Trinium))
			       .addElement('C',ofBlock(Loaders.essentiaCell, 0))
			       .addElement('D',ofFrame(Materials.Iridium))
			       .addElement('F',ofBlock(TT_Container_Casings.sBlockCasingsBA0, 8))
			       .addElement('G',ofBlock(GregTech_API.sBlockCasings8, 7))
			       .addElement('K',
                       ofChain(
                           onElementPass(k -> ++k.mAirPosed, isAir()),
                           onElementPass(k -> ++k.mCreeperEggs, ofBlock(MarsBlocks.creeperEgg, 0)),
                           onElementPass(k -> ++k.mDragonEggs, ofBlock(Blocks.dragon_egg,0)),
                           onElementPass(k -> ++k.mInfinityEggs, ofBlock(InfinityEgg(), 0))
                       )
                   )
			       .build();}
        return structureDef;
	}

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
    @SuppressWarnings("deprecation")
    @Override
    public boolean isRotationChangeAllowed() {
        return ValueEnum.MEG_AllowRotation;
    }
    // endregion

    // region Overrides

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
        info.add(EnumChatFormatting.BLUE + "Generating Vol: " + EnumChatFormatting.GOLD + this.genVol);
        info.add(EnumChatFormatting.BLUE + "Generating Amp: " + EnumChatFormatting.GOLD + this.genAmp);
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
                + (this.genVol * this.mEfficiency) / 10000
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

    @SuppressWarnings("deprecation")
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_MegaEggGenerator_MachineType)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_Controller)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_00)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_01)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_02)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_03)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_04)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_05)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_06)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_07)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_08)
            .addInfo(TextLocalization.Tooltip_MegaEggGenerator_09)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addOtherStructurePart(
                TextLocalization.Tooltip_MegaEggGenerator_D,
                TextLocalization.Tooltip_MegaEggGenerator_C,
                1)
            .addStructureInfo(TextLocalization.Tooltip_MegaEggGenerator_L)
            .addStructureInfo(TextLocalization.Tooltip_MegaEggGenerator_M)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return getMaxEfficiency();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaEggGenerator(this.mName);
    }

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
}
