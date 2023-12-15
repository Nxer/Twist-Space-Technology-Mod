package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Mode_Default_HyperSpacetimeTransformer;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.ParallelMultiplier_HyperSpacetimeTransformer;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedMultiplier_QuantumForceTransformerMode_HyperSpacetimeTransformer;
import static com.github.technus.tectech.thing.block.QuantumGlassBlock.INSTANCE;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GTCM_HyperSpacetimeTransformer extends GTCM_MultiMachineBase<GTCM_HyperSpacetimeTransformer> {

    // region Class Constructor
    public GTCM_HyperSpacetimeTransformer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_HyperSpacetimeTransformer(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic
    private byte mode = Mode_Default_HyperSpacetimeTransformer;
    private int SCfieldGeneratorTier = 0;
    private int TAfieldGeneratorTier = 0;
    private int STfieldGeneratorTier = 0;
    private int mCraftingTier = 0;
    private int mFocusingTier = 0;

    protected float getEuModifier() {
        float EuUsage = 1.0F;
        if (mode == 0) {
            if (SCfieldGeneratorTier == 9) EuUsage -= 0.25F;
            if (TAfieldGeneratorTier == 9) EuUsage -= 0.25F;
            if (STfieldGeneratorTier == 9) EuUsage -= 0.25F;
        } else {}
        return EuUsage;
    };

    protected float getSpeedBonus() {
        return 1F / (mode == 0 ? SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer
            : SpeedMultiplier_QuantumForceTransformerMode_HyperSpacetimeTransformer);
    };

    protected int getMaxParallelRecipes() {
        if (mode == 0) return Math.min(
            ValueEnum.MAX_PARALLEL_LIMIT,
            Math.min(SCfieldGeneratorTier * TAfieldGeneratorTier * STfieldGeneratorTier, 512)
                * GT_Utility.getTier(this.getMaxInputVoltage())
                * ParallelMultiplier_HyperSpacetimeTransformer);
        else return 1;
    };

    protected boolean isEnablePerfectOverclock() {
        if (mode == 0) return EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer;
        return false;
    };

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        switch (mode) {
            case 1:
                return GTPP_Recipe.GTPP_Recipe_Map.sQuantumForceTransformerRecipes;
            default:
                return GTPP_Recipe.GTPP_Recipe_Map.sMolecularTransformerRecipes;
        }
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 2);
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("HyperSpacetimeTransformer.modeMsg." + this.mode));
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        boolean sign = checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
        return sign;
    }
    // endregion

    // region Structure
    // spotless:off
	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
	}

	@Override
	public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
		if (this.mMachine) return -1;
		int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
		return this.survivialBuildPiece(
			STRUCTURE_PIECE_MAIN,
			stackSize,
			horizontalOffSet,
			verticalOffSet,
			depthOffSet,
			realBudget,
			source,
			actor,
			false,
			true);
	}

	public static int getBlockFieldGeneratorTier(Block block, int meta){
			return meta+1;
	}
    public static int getBlockTier(Block block, int meta){
			return (meta+1)%4+1;
	}
	private static final String STRUCTURE_PIECE_MAIN = "mainHyperSpacetimeTransformer";
	private final int horizontalOffSet = 22;
	private final int verticalOffSet = 0;
	private final int depthOffSet = 5;
	@Override
	public IStructureDefinition<GTCM_HyperSpacetimeTransformer> getStructureDefinition() {
		return StructureDefinition.<GTCM_HyperSpacetimeTransformer>builder()
			       .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
			       .addElement(
				       'A',GT_HatchElementBuilder.<GTCM_HyperSpacetimeTransformer>builder()
                       .atLeast(Maintenance, Energy.or(ExoticEnergy),InputBus, OutputBus, InputHatch, OutputBus)
                       .adder(GTCM_HyperSpacetimeTransformer::addToMachineList)
                       .dot(1)
                       .casingIndex(1028)
                       .buildAndChain(sBlockCasingsBA0, 11))
			       .addElement(
					   'B',
					   ofBlock(sBlockCasingsBA0, 12))
			       .addElement(
					   'C',
					   ofBlock(sBlockCasingsTT, 10))
			       .addElement(
					   'D',
					   withChannel("fieldGeneratorTier",
					               ofBlocksTiered(
						               GTCM_HyperSpacetimeTransformer::getBlockFieldGeneratorTier,
						               ImmutableList.of(
							               Pair.of(SpacetimeCompressionFieldGenerators, 0),
							               Pair.of(SpacetimeCompressionFieldGenerators, 1),
							               Pair.of(SpacetimeCompressionFieldGenerators, 2),
							               Pair.of(SpacetimeCompressionFieldGenerators, 3),
							               Pair.of(SpacetimeCompressionFieldGenerators, 4),
							               Pair.of(SpacetimeCompressionFieldGenerators, 5),
							               Pair.of(SpacetimeCompressionFieldGenerators, 6),
							               Pair.of(SpacetimeCompressionFieldGenerators, 7),
							               Pair.of(SpacetimeCompressionFieldGenerators, 8)
						               ),
						               0,
						               (m, t) -> m.SCfieldGeneratorTier = t,
						               m -> m.SCfieldGeneratorTier))
					   ).addElement(
					   'E',
					   withChannel("fieldGeneratorTier",
					               ofBlocksTiered(
						               GTCM_HyperSpacetimeTransformer::getBlockFieldGeneratorTier,
						               ImmutableList.of(
							               Pair.of(StabilisationFieldGenerators, 0),
							               Pair.of(StabilisationFieldGenerators, 1),
							               Pair.of(StabilisationFieldGenerators, 2),
							               Pair.of(StabilisationFieldGenerators, 3),
							               Pair.of(StabilisationFieldGenerators, 4),
							               Pair.of(StabilisationFieldGenerators, 5),
							               Pair.of(StabilisationFieldGenerators, 6),
							               Pair.of(StabilisationFieldGenerators, 7),
							               Pair.of(StabilisationFieldGenerators, 8)
						               ),
						               0,
						               (m, t) -> m.STfieldGeneratorTier = t,
						               m -> m.STfieldGeneratorTier))
					   ).addElement(
					   'F',
					   withChannel("fieldGeneratorTier",
					               ofBlocksTiered(
						               GTCM_HyperSpacetimeTransformer::getBlockFieldGeneratorTier,
						               ImmutableList.of(
							               Pair.of(TimeAccelerationFieldGenerator, 0),
							               Pair.of(TimeAccelerationFieldGenerator, 1),
							               Pair.of(TimeAccelerationFieldGenerator, 2),
							               Pair.of(TimeAccelerationFieldGenerator, 3),
							               Pair.of(TimeAccelerationFieldGenerator, 4),
							               Pair.of(TimeAccelerationFieldGenerator, 5),
							               Pair.of(TimeAccelerationFieldGenerator, 6),
							               Pair.of(TimeAccelerationFieldGenerator, 7),
							               Pair.of(TimeAccelerationFieldGenerator, 8)
						               ),
						               0,
						               (m, t) -> m.TAfieldGeneratorTier = t,
						               m -> m.TAfieldGeneratorTier))
					   ).addElement(
					   'G',
					   ofBlock(ModBlocks.blockCasings4Misc,4)
					   ).addElement(
					   'H',
					   withChannel("manipulator",
					               ofBlocksTiered(
						               GTCM_HyperSpacetimeTransformer::getBlockTier,
						               ImmutableList.of(
							               Pair.of(ModBlocks.blockCasings5Misc, 7),
							               Pair.of(ModBlocks.blockCasings5Misc, 8),
							               Pair.of(ModBlocks.blockCasings5Misc, 9),
							               Pair.of(ModBlocks.blockCasings5Misc, 10)
						               ),
						               0,
						               (m, t) -> m.mCraftingTier = t,
						               m -> m.mCraftingTier))
					   ).addElement(
					   'I',
					   withChannel("shielding",
					               ofBlocksTiered(
						               GTCM_HyperSpacetimeTransformer::getBlockTier,
						               ImmutableList.of(
							               Pair.of(ModBlocks.blockCasings5Misc, 11),
							               Pair.of(ModBlocks.blockCasings5Misc, 12),
							               Pair.of(ModBlocks.blockCasings5Misc, 13),
							               Pair.of(ModBlocks.blockCasings5Misc, 14)
						               ),
						               0,
						               (m, t) -> m.mFocusingTier = t,
						               m -> m.mFocusingTier))
					   ).addElement(
					   'J',ofBlockAnyMeta(INSTANCE)
					   )
			       .build();
	}
	/*
	Blocks:
A -> ofBlock...(gt.blockcasingsBA0, 11, ...);
B -> ofBlock...(gt.blockcasingsBA0, 12, ...);
C -> ofBlock...(gt.blockcasingsTT, 10, ...);
D -> ofBlock...(gt.spacetime_compression_field_generator, 0, ...);
E -> ofBlock...(gt.stabilisation_field_generator, 0, ...);
F -> ofBlock...(gt.time_acceleration_field_generator, 0, ...);
G -> ofBlock...(gtplusplus.blockcasings.4, 4, ...);
H -> ofBlock...(gtplusplus.blockcasings.5, 7, ...);
I -> ofBlock...(gtplusplus.blockcasings.5, 11, ...);
J -> ofBlock...(tile.quantumGlass, 0, ...);
	 */
	public static final String[][] shape = new String[][]{{
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                  AAAAAAAAA                  ",
        "                  AAAA~AAAA                  ",
        "                  AAAAAAAAA                  ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                                             ",
        "                  JJJJJJJJJ                  ",
        "              AAAA         AAAA              ",
        "              AAAA         AAAA              ",
        "              AAAA         AAAA              ",
        "                  JJJJJJJJJ                  ",
        "                                             ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                  AAAAAAAAA                  ",
        "              AJJJ         JJJA              ",
        "            AA                 AA            ",
        "            AA    HHHHHHHHH    AA            ",
        "            AA                 AA            ",
        "              AJJJ         JJJA              ",
        "                  AAAAAAAAA                  ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "               AAAAAAACAAAAAAA               ",
        "            JJJ               JJJ            ",
        "          AA      IIIIIIIII      AA          ",
        "          AA   HHHDDDDDDDDDHHH   AA          ",
        "          AA      IIIIIIIII      AA          ",
        "            JJJ               JJJ            ",
        "               AAAAAAACAAAAAAA               ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "             AAACAAAAAAAAAAACAAA             ",
        "          AJA                   AJA          ",
        "         A     III         III     A         ",
        "         A   HHDDDHHHHHHHHHDDDHH   A         ",
        "         A     III         III     A         ",
        "          AJ                     JA          ",
        "            AAAACAAAAAAAAAAACAAAA            ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "           AAAAAAA         AAAAAAA           ",
        "         JJ       JJJJJJJJJ       JJ         ",
        "        A    II               II    A        ",
        "        A  HHDDHHH         HHHDDHH  A        ",
        "        A    II               II    A        ",
        "         JJ       JJJJJJJJJ       JJ         ",
        "           AAAAAAA         AAAAAAA           ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "          AAAAA               AAAAA          ",
        "        JA     JJJ   FFF   JJJ     AJ        ",
        "       A   II     AAE   EAA     II   A       ",
        "       A  HDDHH   AAE   EAA   HHDDH  A       ",
        "       A   II     AAE   EAA     II   A       ",
        "        J      JJJ   FFF   JJJ      J        ",
        "         AAAAAA               AAAAAA         ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "         AACA        AAA        ACAA         ",
        "       JA    JJ     AIIIA     JJ    AJ       ",
        "      A   I    AAA AI   IA AAA    I   A      ",
        "      A  HDHH   AA AI   IA AA   HHDH  A      ",
        "      A   I     AA AI   IA AA     I   A      ",
        "       J     JJA    AIIIA    AJJ     J       ",
        "        AAACA        AAA        ACAAA        ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "        AAAA                     AAAA        ",
        "      JA    J        IJI        J    AJ      ",
        "     A   I   AAA    IG GI    AAA   I   A     ",
        "     A  HDH   AA    J   J    AA   HDH  A     ",
        "     A   I    AA    IG GI    AA    I   A     ",
        "      J     JA       IJI       AJ     J      ",
        "       AAAAA                     AAAAA       ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "       AAA                         AAA       ",
        "     JA   AJ         HJH         JA   AJ     ",
        "    A   I   AA      HG GH      AA   I   A    ",
        "    A  HDH  AA      J   J      AA  HDH  A    ",
        "    A   I   AA      HG GH      AA   I   A    ",
        "     J     J         HJH         J     J     ",
        "      AAAAA                       AAAAA      ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "      AAA                           AAA      ",
        "    AJ   AJ          HJH          JA   JA    ",
        "   A   I   A        HG GH        A   I   A   ",
        "   A  HDH  A        J   J        A  HDH  A   ",
        "   A   I   A        HG GH        A   I   A   ",
        "    AJ    J          HJH          J    JA    ",
        "      AAAA                         AAAA      ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "     AACA                           ACAA     ",
        "    J    J           HJH           J    J    ",
        "   A  I   A         HG GH         A   I  A   ",
        "   A HDH  A         J   J         A  HDH A   ",
        "   A  I   A         HG GH         A   I  A   ",
        "    J    J           HJH           J    J    ",
        "     AACA                           ACAA     ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "     AAA                             AAA     ",
        "   JA   J            HBH            J   AJ   ",
        "  A   I  A          HG GH          A  I   A  ",
        "  A  HDH A    J     B   B     J    A HDH  A  ",
        "  A   I  A          HG GH          A  I   A  ",
        "   J    J            HBH            J    J   ",
        "    AAAA                             AAAA    ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "    AAA                               AAA    ",
        "   J   J             HJH             J   J   ",
        "  A  I  AA    J     HG GH     J    AA  I  A  ",
        "  A HDH  A   CEC    J   J    CEC   A  HDH A  ",
        "  A  I   A    J     HG GH     J    A   I  A  ",
        "   J   JA            HJH            AJ   J   ",
        "    AAA                               AAA    ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "    AAA                               AAA    ",
        "  AJ   J      J      HJH      J      J   JA  ",
        " A   I  A    JEJ    HG GH    JEJ    A  I   A ",
        " A  HDH A   JEBEJ   J   J   JEBEJ   A HDH  A ",
        " A   I  A    JEJ    HG GH    JEJ    A  I   A ",
        "  AJ   J      J      HJH      J      J   JA  ",
        "    AAA                               AAA    ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "   AAA                                 AAA   ",
        "  J   J              HJH              J   J  ",
        " A  I  AA     J     HG GH     J     AA  I  A ",
        " A HDH  A    CEC    J   J    CEC    A  HDH A ",
        " A  I   A     J     HG GH     J     A   I  A ",
        "  J   JA             HJH             AJ   J  ",
        "   AAA                                 AAA   ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "   ACA                                 ACA   ",
        "  J   J              IJI              J   J  ",
        " A  I  A            IG GI            A  I  A ",
        " A HDH A      J     J   J     J      A HDH A ",
        " A  I  A            IG GI            A  I  A ",
        "  J   J              IJI              J   J  ",
        "   ACA                                 ACA   ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "   AAA               AAA               AAA   ",
        "  J   J             AIIIA             J   J  ",
        " A  I  A           AI   IA           A  I  A ",
        " A HDH A           AI   IA           A HDH A ",
        " A  I  A           AI   IA           A  I  A ",
        "  J   J             AIIIA             J   J  ",
        "   AAA               AAA               AAA   ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "  AAA               JJJJJ               AAA  ",
        " J   J             CJ   JC             J   J ",
        "A  I  A            J     J            A  I  A",
        "A HDH A            J     J            A HDH A",
        "A  I  A            J     J            A  I  A",
        " J   J             CJ   JC             J   J ",
        "  AAA               JJJJJ               AAA  ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                    JJJJJ                    ",
        "  AAA              J     J              AAA  ",
        " J   J            C       C            J   J ",
        "A  I  AA         AJ       JA         AA  I  A",
        "A HDH AA         AJ       JA         AA HDH A",
        "A  I  AA         AJ       JA         AA  I  A",
        " J   J            C       C            J   J ",
        "  AAA              J     J              AAA  ",
        "                    JJJJJ                    ",
        "                                             "
    },{
        "                     JJJ                     ",
        "                   JJ   JJ                   ",
        "  AAA             J       J             AAA  ",
        " J   J A         AJ       JA         A J   J ",
        "A  I  EIIHHHHHHHII         IHHHHHHHHIIE  I  A",
        "A HDH EIJJJJBJJJJI    J    IJJJJBJJJJIE HDH A",
        "A  I  EIIHHHHHHHII         IHHHHHHHHIIE  I  A",
        " J   J A         AJ       JA         A J   J ",
        "  AAA             J       J             AAA  ",
        "                   JJ   JJ                   ",
        "                     JJJ                     "
    },{
        "                    JJJJJ                    ",
        "                   J     J                   ",
        "  AAA  A         AJ       JA         A  AAA  ",
        " J   JFIIHHHHHHHII         IHHHHHHHHIIFJ   J ",
        "A  I    GGGGGGGGG     J      GGGGGGGG    I  A",
        "A HDH                JFJ                HDH A",
        "A  I    GGGGGGGGG     J     GGGGGGGGG    I  A",
        " J   JFIIHHHHHHHII         IHHHHHHHHIIFJ   J ",
        "  AAA  A         AJ       JA         A  AAA  ",
        "                   J     J                   ",
        "                    JJJJJ                    "
    },{
        "                    JJJJJ                    ",
        "                   J     J                   ",
        "  ACA  A         AJ       JA         A  ACA  ",
        " J   JFIJJJJBJJJJI    J    IJJJJBJJJJIFJ   J ",
        "A  I                 JFJ                 I  A",
        "A HDH               JFBFJ               HDH A",
        "A  I                 JFJ                 I  A",
        " J   JFIJJJJBJJJJI    J    IJJJJBJJJJIFJ   J ",
        "  ACA  A         AJ       JA         A  ACA  ",
        "                   J     J                   ",
        "                    JJJJJ                    "
    },{
        "                    JJJJJ                    ",
        "                   J     J                   ",
        "  AAA  A         AJ       JA         A  AAA  ",
        " J   JFIIHHHHHHHII         IHHHHHHHHIIFJ   J ",
        "A  I    GGGGGGGGG     J     GGGGGGGGG    I  A",
        "A HDH                JFJ                HDH A",
        "A  I    GGGGGGGGG     J     GGGGGGGGG    I  A",
        " J   JFIIHHHHHHHII         IHHHHHHHHIIFJ   J ",
        "  AAA  A         AJ       JA         A  AAA  ",
        "                   J     J                   ",
        "                    JJJJJ                    "
    },{
        "                     JJJ                     ",
        "                   JJ   JJ                   ",
        "  AAA             J       J             AAA  ",
        " J   J A         AJ       JA         A J   J ",
        "A  I  EIIHHHHHHHII         IHHHHHHHHIIE  I  A",
        "A HDH EIJJJJBJJJJI    J    IJJJJBJJJJIE HDH A",
        "A  I  EIIHHHHHHHII         IHHHHHHHHIIE  I  A",
        " J   J A         AJ       JA         A J   J ",
        "  AAA             J       J             AAA  ",
        "                   JJ   JJ                   ",
        "                     JJJ                     "
    },{
        "                                             ",
        "                    JJJJJ                    ",
        "  AAA              J     J              AAA  ",
        " J   J            C       C            J   J ",
        "A  I  AA         AJ       JA         AA  I  A",
        "A HDH AA         AJ       JA         AA HDH A",
        "A  I  AA         AJ       JA         AA  I  A",
        " J   J            C       C            J   J ",
        "  AAA              J     J              AAA  ",
        "                    JJJJJ                    ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "  AAA               JJJJJ               AAA  ",
        " J   J             CJ   JC             J   J ",
        "A  I  A            J     J            A  I  A",
        "A HDH A            J     J            A HDH A",
        "A  I  A            J     J            A  I  A",
        " J   J             CJ   JC             J   J ",
        "  AAA               JJJJJ               AAA  ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "   AAA               AAA               AAA   ",
        "  J   J             AIIIA             J   J  ",
        " A  I  A           AI   IA           A  I  A ",
        " A HDH A           AI   IA           A HDH A ",
        " A  I  A           AI   IA           A  I  A ",
        "  J   J             AIIIA             J   J  ",
        "   AAA               AAA               AAA   ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "   ACA                                 ACA   ",
        "  J   J              IJI              J   J  ",
        " A  I  A            IG GI            A  I  A ",
        " A HDH A      J     J   J     J      A HDH A ",
        " A  I  A            IG GI            A  I  A ",
        "  J   J              IJI              J   J  ",
        "   ACA                                 ACA   ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "   AAA                                 AAA   ",
        "  J   J              HJH              J   J  ",
        " A  I  AA     J     HG GH     J     AA  I  A ",
        " A HDH  A    CEC    J   J    CEC    A  HDH A ",
        " A  I   A     J     HG GH     J     A   I  A ",
        "  J   JA             HJH             AJ   J  ",
        "   AAA                                 AAA   ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "    AAA                               AAA    ",
        "  AJ   J      J      HJH      J      J   JA  ",
        " A   I  A    JEJ    HG GH    JEJ    A  I   A ",
        " A  HDH A   JEBEJ   J   J   JEBEJ   A HDH  A ",
        " A   I  A    JEJ    HG GH    JEJ    A  I   A ",
        "  AJ   J      J      HJH      J      J   JA  ",
        "    AAA                               AAA    ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "    AAA                               AAA    ",
        "   J   J             HJH             J   J   ",
        "  A  I  AA    J     HG GH     J    AA  I  A  ",
        "  A HDH  A   CEC    J   J    CEC   A  HDH A  ",
        "  A  I   A    J     HG GH     J    A   I  A  ",
        "   J   JA            HJH            AJ   J   ",
        "    AAA                               AAA    ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "     AAA                             AAA     ",
        "   JA   J            HBH            J   AJ   ",
        "  A   I  A          HG GH          A  I   A  ",
        "  A  HDH A    J     B   B     J    A HDH  A  ",
        "  A   I  A          HG GH          A  I   A  ",
        "   J    J            HBH            J    J   ",
        "    AAAA                             AAAA    ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "     AACA                           ACAA     ",
        "    J    J           HJH           J    J    ",
        "   A  I   A         HG GH         A   I  A   ",
        "   A HDH  A         J   J         A  HDH A   ",
        "   A  I   A         HG GH         A   I  A   ",
        "    J    J           HJH           J    J    ",
        "     AACA                           ACAA     ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "      AAA                           AAA      ",
        "    AJ   AJ          HJH          JA   JA    ",
        "   A   I   A        HG GH        A   I   A   ",
        "   A  HDH  A        J   J        A  HDH  A   ",
        "   A   I   A        HG GH        A   I   A   ",
        "    AJ    J          HJH          J    JA    ",
        "      AAAA                         AAAA      ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "       AAA                         AAA       ",
        "     JA   AJ         HJH         JA   AJ     ",
        "    A   I   AA      HG GH      AA   I   A    ",
        "    A  HDH  AA      J   J      AA  HDH  A    ",
        "    A   I   AA      HG GH      AA   I   A    ",
        "     J     J         HJH         J     J     ",
        "      AAAAA                       AAAAA      ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "        AAAA                     AAAA        ",
        "      JA    J        IJI        J    AJ      ",
        "     A   I   AAA    IG GI    AAA   I   A     ",
        "     A  HDH   AA    J   J    AA   HDH  A     ",
        "     A   I    AA    IG GI    AA    I   A     ",
        "      J     JA       IJI       AJ     J      ",
        "       AAAAA                     AAAAA       ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "         AACA        AAA        ACAA         ",
        "       JA    JJ     AIIIA     JJ    AJ       ",
        "      A   I    AAA AI   IA AAA    I   A      ",
        "      A  HDHH   AA AI   IA AA   HHDH  A      ",
        "      A   I     AA AI   IA AA     I   A      ",
        "       J     JJA    AIIIA    AJJ     J       ",
        "        AAACA        AAA        ACAAA        ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "          AAAAA               AAAAA          ",
        "        JA     JJJ   FFF   JJJ     AJ        ",
        "       A   II     AAE   EAA     II   A       ",
        "       A  HDDHH   AAE   EAA   HHDDH  A       ",
        "       A   II     AAE   EAA     II   A       ",
        "        J      JJJ  AFFFA  JJJ      J        ",
        "         AAAAAA               AAAAAA         ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "           AAAAAAA         AAAAAAA           ",
        "         JJ       JJJJJJJJJ       JJ         ",
        "        A    II               II    A        ",
        "        A  HHDDHHH         HHHDDHH  A        ",
        "        A    II               II    A        ",
        "         JJ       JJJJJJJJJ       JJ         ",
        "           AAAAAAA         AAAAAAA           ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "             AAACAAAAAAAAAAACAAA             ",
        "          AJA                   AJA          ",
        "         A     III         III     A         ",
        "         A   HHDDDHHHHHHHHHDDDHH   A         ",
        "         A     III         III     A         ",
        "          AJ                     JA          ",
        "            AAAACAAAAAAAAAAACAAAA            ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "               AAAAAAACAAAAAAA               ",
        "            JJJ               JJJ            ",
        "          AA      IIIIIIIII      AA          ",
        "          AA   HHHDDDDDDDDDHHH   AA          ",
        "          AA      IIIIIIIII      AA          ",
        "            JJJ               JJJ            ",
        "               AAAAAAACAAAAAAA               ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                  AAAAAAAAA                  ",
        "              AJJJ         JJJA              ",
        "            AA                 AA            ",
        "            AA    HHHHHHHHH    AA            ",
        "            AA                 AA            ",
        "              AJJJ         JJJA              ",
        "                  AAAAAAAAA                  ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                                             ",
        "                  JJJJJJJJJ                  ",
        "              AAAA         AAAA              ",
        "              AAAA         AAAA              ",
        "              AAAA         AAAA              ",
        "                  JJJJJJJJJ                  ",
        "                                             ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                  AAAAAAAAA                  ",
        "                  AAAAAAAAA                  ",
        "                  AAAAAAAAA                  ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             "
    }}
    ;

	// spotless:on
    // endregion

    // region Overrides

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mCraftingTier", mCraftingTier);
        aNBT.setInteger("mFocusingTier", mFocusingTier);
        aNBT.setInteger("SCfieldGeneratorTier", SCfieldGeneratorTier);
        aNBT.setInteger("STfieldGeneratorTier", STfieldGeneratorTier);
        aNBT.setInteger("TAfieldGeneratorTier", TAfieldGeneratorTier);
        aNBT.setByte("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mCraftingTier = aNBT.getInteger("mCraftingTier");
        mFocusingTier = aNBT.getInteger("mFocusingTier");
        SCfieldGeneratorTier = aNBT.getInteger("SCfieldGeneratorTier");
        STfieldGeneratorTier = aNBT.getInteger("STfieldGeneratorTier");
        TAfieldGeneratorTier = aNBT.getInteger("TAfieldGeneratorTier");
        mode = aNBT.getByte("mode");
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_HyperSpacetimeTransformer_MachineType)
            .addInfo(TextLocalization.Tooltip_HyperSpacetimeTransformer_00)
            .addInfo(TextLocalization.Tooltip_HyperSpacetimeTransformer_01)
            .addInfo(TextLocalization.Tooltip_HyperSpacetimeTransformer_02)
            .addInfo(TextLocalization.Tooltip_HyperSpacetimeTransformer_03)
            .addInfo(TextLocalization.Tooltip_HyperSpacetimeTransformer_04)
            .addInfo(TextLocalization.Tooltip_HyperSpacetimeTransformer_05)
            .addInfo(TextLocalization.Tooltip_HyperSpacetimeTransformer_06)
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(45, 11, 45, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 2)
            .addOutputHatch(TextLocalization.textUseBlueprint, 2)
            .addInputBus(TextLocalization.textUseBlueprint, 2)
            .addOutputBus(TextLocalization.textUseBlueprint, 2)
            .addMaintenanceHatch(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_HyperSpacetimeTransformer(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) {
                return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                .extFacing()
                .build() };
        }

        return new ITexture[] { casingTexturePages[0][12] };
    }
    // endregion
}
