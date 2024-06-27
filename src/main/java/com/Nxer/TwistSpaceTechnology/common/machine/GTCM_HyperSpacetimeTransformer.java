package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Mode_Default_HyperSpacetimeTransformer;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.ParallelMultiplier_HyperSpacetimeTransformer;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedMultiplier_SpaceTimeTransformerMode_HyperSpacetimeTransformer;
import static com.github.technus.tectech.thing.block.QuantumGlassBlock.INSTANCE;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.SpacetimeCompressionFieldGenerators;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.StabilisationFieldGenerators;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.TimeAccelerationFieldGenerator;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
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
        } else {
            EuUsage /= ((1F + (SCfieldGeneratorTier + 1) / 10) * (1F + (TAfieldGeneratorTier + 1) / 10)
                * (1F + (STfieldGeneratorTier + 1) / 10));
        }
        return EuUsage;
    };

    protected float getSpeedBonus() {
        return 1F / (mode == 0 ? SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer
            : SpeedMultiplier_SpaceTimeTransformerMode_HyperSpacetimeTransformer);
    };

    protected int getMaxParallelRecipes() {
        if (mode == 0) return Math.min(
            ValueEnum.MAX_PARALLEL_LIMIT,
            Math.min(SCfieldGeneratorTier * TAfieldGeneratorTier * STfieldGeneratorTier, 512)
                * ParallelMultiplier_HyperSpacetimeTransformer);
        else return mCraftingTier * mFocusingTier;
    };

    protected boolean isEnablePerfectOverclock() {
        if (mode == 0) return EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer;
        return false;
    };

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (mode) {
            case 1:
                return GTCMRecipe.HyperSpacetimeTransformerRecipe;
            default:
                return GTPPRecipeMaps.molecularTransformerRecipes;
        }
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTCMRecipe.HyperSpacetimeTransformerRecipe, GTPPRecipeMaps.molecularTransformerRecipes);
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
        repairMachine();
        this.SCfieldGeneratorTier = 0;
        this.TAfieldGeneratorTier = 0;
        this.STfieldGeneratorTier = 0;
        this.mCraftingTier = 0;
        this.mFocusingTier = 0;
        boolean sign = checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
        if (this.SCfieldGeneratorTier == 0 || this.TAfieldGeneratorTier == 0
            || this.STfieldGeneratorTier == 0
            || this.mCraftingTier == 0
            || this.mFocusingTier == 0) {
            return false;
        }
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
        if(block == SpacetimeCompressionFieldGenerators || block == TimeAccelerationFieldGenerator || block == StabilisationFieldGenerators)
			return meta+1;
        return 0;
	}
    public static int getBlockTier(Block block, int meta){
        if(block == ModBlocks.blockCasings5Misc)
			return (meta+1)%4+1;
        return 0;
	}
	private static final String STRUCTURE_PIECE_MAIN = "mainHyperSpacetimeTransformer";
	private final int horizontalOffSet = 22;
	private final int verticalOffSet = 5;
	private final int depthOffSet = 0;
    private static IStructureDefinition<GTCM_HyperSpacetimeTransformer> STRUCTURE_DEFINITION = null;
	@Override
	public IStructureDefinition<GTCM_HyperSpacetimeTransformer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GTCM_HyperSpacetimeTransformer>builder()
                                                      .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                                                      .addElement(
                                                          'A',GT_HatchElementBuilder.<GTCM_HyperSpacetimeTransformer>builder()
                                                                                    .atLeast(Energy.or(ExoticEnergy),InputBus, OutputBus, InputHatch, OutputHatch)
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
                                                          withChannel("fieldgeneratortier",
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
                    withChannel("fieldgeneratortier",
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
                    withChannel("fieldgeneratortier",
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
		return STRUCTURE_DEFINITION;
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
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                     JJJ                     ",
        "                    JJJJJ                    ",
        "                    JJJJJ                    ",
        "                    JJJJJ                    ",
        "                     JJJ                     ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                    JJJJJ                    ",
        "                   JJ   JJ                   ",
        "                   J     J                   ",
        "                   J     J                   ",
        "                   J     J                   ",
        "                   JJ   JJ                   ",
        "                    JJJJJ                    ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                  AAAAAAAAA                  ",
        "               AAAAAAACAAAAAAA               ",
        "             AAACAAAAAAAAAAACAAA             ",
        "           AAAAAAA         AAAAAAA           ",
        "          AAAAA               AAAAA          ",
        "         AACA        AAA        ACAA         ",
        "        AAAA                     AAAA        ",
        "       AAA                         AAA       ",
        "      AAA                           AAA      ",
        "     AACA                           ACAA     ",
        "     AAA                             AAA     ",
        "    AAA                               AAA    ",
        "    AAA                               AAA    ",
        "   AAA                                 AAA   ",
        "   ACA                                 ACA   ",
        "   AAA               AAA               AAA   ",
        "  AAA               JJJJJ               AAA  ",
        "  AAA              J     J              AAA  ",
        "  AAA             J       J             AAA  ",
        "  AAA  A         AJ       JA         A  AAA  ",
        "  ACA  A         AJ       JA         A  ACA  ",
        "  AAA  A         AJ       JA         A  AAA  ",
        "  AAA             J       J             AAA  ",
        "  AAA              J     J              AAA  ",
        "  AAA               JJJJJ               AAA  ",
        "   AAA               AAA               AAA   ",
        "   ACA                                 ACA   ",
        "   AAA                                 AAA   ",
        "    AAA                               AAA    ",
        "    AAA                               AAA    ",
        "     AAA                             AAA     ",
        "     AACA                           ACAA     ",
        "      AAA                           AAA      ",
        "       AAA                         AAA       ",
        "        AAAA                     AAAA        ",
        "         AACA        AAA        ACAA         ",
        "          AAAAA               AAAAA          ",
        "           AAAAAAA         AAAAAAA           ",
        "             AAACAAAAAAAAAAACAAA             ",
        "               AAAAAAACAAAAAAA               ",
        "                  AAAAAAAAA                  ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                  JJJJJJJJJ                  ",
        "              AJJJ         JJJA              ",
        "            JJJ               JJJ            ",
        "          AJA                   AJA          ",
        "         JJ       JJJJJJJJJ       JJ         ",
        "        JA     JJJ   FFF   JJJ     AJ        ",
        "       JA    JJ     AIIIA     JJ    AJ       ",
        "      JA    J        IJI        J    AJ      ",
        "     JA   AJ         HJH         JA   AJ     ",
        "    AJ   AJ          HJH          JA   JA    ",
        "    J    J           HJH           J    J    ",
        "   JA   J            HBH            J   AJ   ",
        "   J   J             HJH             J   J   ",
        "  AJ   J      J      HJH      J      J   JA  ",
        "  J   J              HJH              J   J  ",
        "  J   J              IJI              J   J  ",
        "  J   J             AIIIA             J   J  ",
        " J   J             CJ   JC             J   J ",
        " J   J            C       C            J   J ",
        " J   J A         AJ       JA         A J   J ",
        " J   JFIIHHHHHHHII         IHHHHHHHHIIFJ   J ",
        " J   JFIJJJJBJJJJI    J    IJJJJBJJJJIFJ   J ",
        " J   JFIIHHHHHHHII         IHHHHHHHHIIFJ   J ",
        " J   J A         AJ       JA         A J   J ",
        " J   J            C       C            J   J ",
        " J   J             CJ   JC             J   J ",
        "  J   J             AIIIA             J   J  ",
        "  J   J              IJI              J   J  ",
        "  J   J              HJH              J   J  ",
        "  AJ   J      J      HJH      J      J   JA  ",
        "   J   J             HJH             J   J   ",
        "   JA   J            HBH            J   AJ   ",
        "    J    J           HJH           J    J    ",
        "    AJ   AJ          HJH          JA   JA    ",
        "     JA   AJ         HJH         JA   AJ     ",
        "      JA    J        IJI        J    AJ      ",
        "       JA    JJ     AIIIA     JJ    AJ       ",
        "        JA     JJJ   FFF   JJJ     AJ        ",
        "         JJ       JJJJJJJJJ       JJ         ",
        "          AJA                   AJA          ",
        "            JJJ               JJJ            ",
        "              AJJJ         JJJA              ",
        "                  JJJJJJJJJ                  ",
        "                                             "
    },{
        "                  AAAAAAAAA                  ",
        "              AAAA         AAAA              ",
        "            AA                 AA            ",
        "          AA      IIIIIIIII      AA          ",
        "         A     III         III     A         ",
        "        A    II               II    A        ",
        "       A   II     AAE   EAA     II   A       ",
        "      A   I    AAA AI   IA AAA    I   A      ",
        "     A   I   AAA    IG GI    AAA   I   A     ",
        "    A   I   AA      HG GH      AA   I   A    ",
        "   A   I   A        HG GH        A   I   A   ",
        "   A  I   A         HG GH         A   I  A   ",
        "  A   I  A          HG GH          A  I   A  ",
        "  A  I  AA    J     HG GH     J    AA  I  A  ",
        " A   I  A    JEJ    HG GH    JEJ    A  I   A ",
        " A  I  AA     J     HG GH     J     AA  I  A ",
        " A  I  A            IG GI            A  I  A ",
        " A  I  A           AI   IA           A  I  A ",
        "A  I  A            J     J            A  I  A",
        "A  I  AA         AJ       JA         AA  I  A",
        "A  I  EIIHHHHHHHII         IHHHHHHHHIIE  I  A",
        "A  I    GGGGGGGGG     J     GGGGGGGGG    I  A",
        "A  I                 JFJ                 I  A",
        "A  I    GGGGGGGGG     J      GGGGGGGG    I  A",
        "A  I  EIIHHHHHHHII         IHHHHHHHHIIE  I  A",
        "A  I  AA         AJ       JA         AA  I  A",
        "A  I  A            J     J            A  I  A",
        " A  I  A           AI   IA           A  I  A ",
        " A  I  A            IG GI            A  I  A ",
        " A  I  AA     J     HG GH     J     AA  I  A ",
        " A   I  A    JEJ    HG GH    JEJ    A  I   A ",
        "  A  I  AA    J     HG GH     J    AA  I  A  ",
        "  A   I  A          HG GH          A  I   A  ",
        "   A  I   A         HG GH         A   I  A   ",
        "   A   I   A        HG GH        A   I   A   ",
        "    A   I   AA      HG GH      AA   I   A    ",
        "     A   I   AAA    IG GI    AAA   I   A     ",
        "      A   I    AAA AI   IA AAA    I   A      ",
        "       A   II     AAE   EAA     II   A       ",
        "        A    II               II    A        ",
        "         A     III         III     A         ",
        "          AA      IIIIIIIII      AA          ",
        "            AA                 AA            ",
        "              AAAA         AAAA              ",
        "                  AAAAAAAAA                  "
    },{
        "                  AAAA~AAAA                  ",
        "              AAAA         AAAA              ",
        "            AA    HHHHHHHHH    AA            ",
        "          AA   HHHDDDDDDDDDHHH   AA          ",
        "         A   HHDDDHHHHHHHHHDDDHH   A         ",
        "        A  HHDDHHH         HHHDDHH  A        ",
        "       A  HDDHH   AAE   EAA   HHDDH  A       ",
        "      A  HDHH   AA AI   IA AA   HHDH  A      ",
        "     A  HDH   AA    J   J    AA   HDH  A     ",
        "    A  HDH  AA      J   J      AA  HDH  A    ",
        "   A  HDH  A        J   J        A  HDH  A   ",
        "   A HDH  A         J   J         A  HDH A   ",
        "  A  HDH A    J     B   B     J    A HDH  A  ",
        "  A HDH  A   CEC    J   J    CEC   A  HDH A  ",
        " A  HDH A   JEBEJ   J   J   JEBEJ   A HDH  A ",
        " A HDH  A    CEC    J   J    CEC    A  HDH A ",
        " A HDH A      J     J   J     J      A HDH A ",
        " A HDH A           AI   IA           A HDH A ",
        "A HDH A            J     J            A HDH A",
        "A HDH AA         AJ       JA         AA HDH A",
        "A HDH EIJJJJBJJJJI    J    IJJJJBJJJJIE HDH A",
        "A HDH                JFJ                HDH A",
        "A HDH               JFBFJ               HDH A",
        "A HDH                JFJ                HDH A",
        "A HDH EIJJJJBJJJJI    J    IJJJJBJJJJIE HDH A",
        "A HDH AA         AJ       JA         AA HDH A",
        "A HDH A            J     J            A HDH A",
        " A HDH A           AI   IA           A HDH A ",
        " A HDH A      J     J   J     J      A HDH A ",
        " A HDH  A    CEC    J   J    CEC    A  HDH A ",
        " A  HDH A   JEBEJ   J   J   JEBEJ   A HDH  A ",
        "  A HDH  A   CEC    J   J    CEC   A  HDH A  ",
        "  A  HDH A    J     B   B     J    A HDH  A  ",
        "   A HDH  A         J   J         A  HDH A   ",
        "   A  HDH  A        J   J        A  HDH  A   ",
        "    A  HDH  AA      J   J      AA  HDH  A    ",
        "     A  HDH   AA    J   J    AA   HDH  A     ",
        "      A  HDHH   AA AI   IA AA   HHDH  A      ",
        "       A  HDDHH   AAE   EAA   HHDDH  A       ",
        "        A  HHDDHHH         HHHDDHH  A        ",
        "         A   HHDDDHHHHHHHHHDDDHH   A         ",
        "          AA   HHHDDDDDDDDDHHH   AA          ",
        "            AA    HHHHHHHHH    AA            ",
        "              AAAA         AAAA              ",
        "                  AAAAAAAAA                  "
    },{
        "                  AAAAAAAAA                  ",
        "              AAAA         AAAA              ",
        "            AA                 AA            ",
        "          AA      IIIIIIIII      AA          ",
        "         A     III         III     A         ",
        "        A    II               II    A        ",
        "       A   II     AAE   EAA     II   A       ",
        "      A   I     AA AI   IA AA     I   A      ",
        "     A   I    AA    IG GI    AA    I   A     ",
        "    A   I   AA      HG GH      AA   I   A    ",
        "   A   I   A        HG GH        A   I   A   ",
        "   A  I   A         HG GH         A   I  A   ",
        "  A   I  A          HG GH          A  I   A  ",
        "  A  I   A    J     HG GH     J    A   I  A  ",
        " A   I  A    JEJ    HG GH    JEJ    A  I   A ",
        " A  I   A     J     HG GH     J     A   I  A ",
        " A  I  A            IG GI            A  I  A ",
        " A  I  A           AI   IA           A  I  A ",
        "A  I  A            J     J            A  I  A",
        "A  I  AA         AJ       JA         AA  I  A",
        "A  I  EIIHHHHHHHII         IHHHHHHHHIIE  I  A",
        "A  I    GGGGGGGGG     J     GGGGGGGGG    I  A",
        "A  I                 JFJ                 I  A",
        "A  I    GGGGGGGGG     J     GGGGGGGGG    I  A",
        "A  I  EIIHHHHHHHII         IHHHHHHHHIIE  I  A",
        "A  I  AA         AJ       JA         AA  I  A",
        "A  I  A            J     J            A  I  A",
        " A  I  A           AI   IA           A  I  A ",
        " A  I  A            IG GI            A  I  A ",
        " A  I   A     J     HG GH     J     A   I  A ",
        " A   I  A    JEJ    HG GH    JEJ    A  I   A ",
        "  A  I   A    J     HG GH     J    A   I  A  ",
        "  A   I  A          HG GH          A  I   A  ",
        "   A  I   A         HG GH         A   I  A   ",
        "   A   I   A        HG GH        A   I   A   ",
        "    A   I   AA      HG GH      AA   I   A    ",
        "     A   I    AA    IG GI    AA    I   A     ",
        "      A   I     AA AI   IA AA     I   A      ",
        "       A   II     AAE   EAA     II   A       ",
        "        A    II               II    A        ",
        "         A     III         III     A         ",
        "          AA      IIIIIIIII      AA          ",
        "            AA                 AA            ",
        "              AAAA         AAAA              ",
        "                  AAAAAAAAA                  "
    },{
        "                                             ",
        "                  JJJJJJJJJ                  ",
        "              AJJJ         JJJA              ",
        "            JJJ               JJJ            ",
        "          AJ                     JA          ",
        "         JJ       JJJJJJJJJ       JJ         ",
        "        J      JJJ  AFFFA  JJJ      J        ",
        "       J     JJA    AIIIA    AJJ     J       ",
        "      J     JA       IJI       AJ     J      ",
        "     J     J         HJH         J     J     ",
        "    AJ    J          HJH          J    JA    ",
        "    J    J           HJH           J    J    ",
        "   J    J            HBH            J    J   ",
        "   J   JA            HJH            AJ   J   ",
        "  AJ   J      J      HJH      J      J   JA  ",
        "  J   JA             HJH             AJ   J  ",
        "  J   J              IJI              J   J  ",
        "  J   J             AIIIA             J   J  ",
        " J   J             CJ   JC             J   J ",
        " J   J            C       C            J   J ",
        " J   J A         AJ       JA         A J   J ",
        " J   JFIIHHHHHHHII         IHHHHHHHHIIFJ   J ",
        " J   JFIJJJJBJJJJI    J    IJJJJBJJJJIFJ   J ",
        " J   JFIIHHHHHHHII         IHHHHHHHHIIFJ   J ",
        " J   J A         AJ       JA         A J   J ",
        " J   J            C       C            J   J ",
        " J   J             CJ   JC             J   J ",
        "  J   J             AIIIA             J   J  ",
        "  J   J              IJI              J   J  ",
        "  J   JA             HJH             AJ   J  ",
        "  AJ   J      J      HJH      J      J   JA  ",
        "   J   JA            HJH            AJ   J   ",
        "   J    J            HBH            J    J   ",
        "    J    J           HJH           J    J    ",
        "    AJ    J          HJH          J    JA    ",
        "     J     J         HJH         J     J     ",
        "      J     JA       IJI       AJ     J      ",
        "       J     JJA    AIIIA    AJJ     J       ",
        "        J      JJJ   FFF   JJJ      J        ",
        "         JJ       JJJJJJJJJ       JJ         ",
        "          AJ                     JA          ",
        "            JJJ               JJJ            ",
        "              AJJJ         JJJA              ",
        "                  JJJJJJJJJ                  ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                  AAAAAAAAA                  ",
        "               AAAAAAACAAAAAAA               ",
        "            AAAACAAAAAAAAAAACAAAA            ",
        "           AAAAAAA         AAAAAAA           ",
        "         AAAAAA               AAAAAA         ",
        "        AAACA        AAA        ACAAA        ",
        "       AAAAA                     AAAAA       ",
        "      AAAAA                       AAAAA      ",
        "      AAAA                         AAAA      ",
        "     AACA                           ACAA     ",
        "    AAAA                             AAAA    ",
        "    AAA                               AAA    ",
        "    AAA                               AAA    ",
        "   AAA                                 AAA   ",
        "   ACA                                 ACA   ",
        "   AAA               AAA               AAA   ",
        "  AAA               JJJJJ               AAA  ",
        "  AAA              J     J              AAA  ",
        "  AAA             J       J             AAA  ",
        "  AAA  A         AJ       JA         A  AAA  ",
        "  ACA  A         AJ       JA         A  ACA  ",
        "  AAA  A         AJ       JA         A  AAA  ",
        "  AAA             J       J             AAA  ",
        "  AAA              J     J              AAA  ",
        "  AAA               JJJJJ               AAA  ",
        "   AAA               AAA               AAA   ",
        "   ACA                                 ACA   ",
        "   AAA                                 AAA   ",
        "    AAA                               AAA    ",
        "    AAA                               AAA    ",
        "    AAAA                             AAAA    ",
        "     AACA                           ACAA     ",
        "      AAAA                         AAAA      ",
        "      AAAAA                       AAAAA      ",
        "       AAAAA                     AAAAA       ",
        "        AAACA        AAA        ACAAA        ",
        "         AAAAAA               AAAAAA         ",
        "           AAAAAAA         AAAAAAA           ",
        "            AAAACAAAAAAAAAAACAAAA            ",
        "               AAAAAAACAAAAAAA               ",
        "                  AAAAAAAAA                  ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                    JJJJJ                    ",
        "                   JJ   JJ                   ",
        "                   J     J                   ",
        "                   J     J                   ",
        "                   J     J                   ",
        "                   JJ   JJ                   ",
        "                    JJJJJ                    ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             "
    },{
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                     JJJ                     ",
        "                    JJJJJ                    ",
        "                    JJJJJ                    ",
        "                    JJJJJ                    ",
        "                     JJJ                     ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
        "                                             ",
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

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
    // endregion
}
