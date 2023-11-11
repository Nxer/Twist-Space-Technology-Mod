package com.GTNH_Community.gtnhcommunitymod.common.machine;

import static com.GTNH_Community.gtnhcommunitymod.common.machine.ValueEnum.MAX_PARALLEL_LIMIT;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.BLUE_PRINT_INFO;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.ModName;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.StructureTooComplex;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_CrystallineInfinitier_00;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_CrystallineInfinitier_01;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_CrystallineInfinitier_02;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_CrystallineInfinitier_03;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_CrystallineInfinitier_04;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_CrystallineInfinitier_05;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_CrystallineInfinitier_06;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_CrystallineInfinitier_MachineType;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textScrewdriverChangeMode;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textUseBlueprint;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.StabilisationFieldGenerators;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.GTNH_Community.gtnhcommunitymod.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.GTNH_Community.gtnhcommunitymod.common.machine.recipeMap.GTCMRecipe;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings8;

public class GTCM_CrystallineInfinitier extends GTCM_MultiMachineBase<GTCM_CrystallineInfinitier> {

    // region Class Constructor
    public GTCM_CrystallineInfinitier(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_CrystallineInfinitier(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic
    private byte mode = 0;
    public byte glassTier = 0;
    private int fieldGeneratorTier = 0;

    protected float getEuModifier() {
        return 1.0F / Math.max(fieldGeneratorTier, 1);
    };

    protected float getSpeedBonus() {
        return mode == 0 ? 0.25F : 1.0F;
    };

    protected int getMaxParallelRecipes() {
        return Math
            .min(MAX_PARALLEL_LIMIT, glassTier * fieldGeneratorTier * GT_Utility.getTier(this.getMaxInputVoltage()));
    };

    protected boolean isEnablePerfectOverclock() {
        return fieldGeneratorTier >= 3;
    };

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        switch (mode) {
            case 1:
                return GTCMRecipe.instance.CrystallineInfinitierRecipes;
            default:
                return GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes;
        }
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 2);
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("CrystallineInfinitier.modeMsg." + this.mode));
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.fieldGeneratorTier = 0;
        this.glassTier = 0;
        boolean sign = checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
        if (this.fieldGeneratorTier == 0) {
            return false;
        }
        if (glassTier < 12) {
            for (GT_MetaTileEntity_Hatch hatch : this.mExoticEnergyHatches) {
                if (this.glassTier < hatch.mTier) return false;
            }
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
		if (block == sBlockCasingsTT){
			switch (meta) {
				case 6:
					return 1;
				case 14:
					return 2;
				default:
					return 0;
			}
		}
		if (block == StabilisationFieldGenerators){
			return meta + 3;
		}
		return 0;
	}
	private static final String STRUCTURE_PIECE_MAIN = "mainCrystallineInfinitier";
	private final int horizontalOffSet = 15;
	private final int verticalOffSet = 34;
	private final int depthOffSet = 0;
	@Override
	public IStructureDefinition<GTCM_CrystallineInfinitier> getStructureDefinition() {
		return StructureDefinition.<GTCM_CrystallineInfinitier>builder()
			       .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
			       .addElement(
				       'A',
				       withChannel("glass",
				                   BorosilicateGlass.ofBoroGlass(
					                   (byte) 0,
					                   (byte) 1,
					                   Byte.MAX_VALUE,
					                   (te, t) -> te.glassTier = t,
					                   te -> te.glassTier
				                   )))
			       .addElement(
					   'B',
					   GT_HatchElementBuilder.<GTCM_CrystallineInfinitier>builder()
						   .atLeast(Maintenance, Energy.or(ExoticEnergy))
						   .adder(GTCM_CrystallineInfinitier::addToMachineList)
						   .dot(1)
						   .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(10))
						   .buildAndChain(GregTech_API.sBlockCasings8, 10))
			       .addElement(
					   'C',
					   GT_HatchElementBuilder.<GTCM_CrystallineInfinitier>builder()
						   .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
						   .adder(GTCM_CrystallineInfinitier::addToMachineList)
						   .dot(2)
						   .casingIndex(1028)
						   .buildAndChain(sBlockCasingsTT, 4))
			       .addElement(
					   'D',
					   withChannel("fieldGeneratorTier",
					               ofBlocksTiered(
						               GTCM_CrystallineInfinitier::getBlockFieldGeneratorTier,
						               ImmutableList.of(
							               Pair.of(sBlockCasingsTT, 6),
							               Pair.of(sBlockCasingsTT, 14),
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
						               (m, t) -> m.fieldGeneratorTier = t,
						               m -> m.fieldGeneratorTier))
					   )
			       .addElement('E',ofBlock(sBlockCasingsTT, 8))
			       .addElement('F',ofBlock(sBlockCasingsTT, 9))
			       .addElement('G',ofFrame(Materials.NaquadahAlloy))
			       .build();
	}
	/*
	Blocks:
A -> ofBlock...(BW_GlasBlocks, 0, ...); // glass
B -> ofBlock...(gt.blockcasings8, 10, ...); // energy maintenance
C -> ofBlock...(gt.blockcasingsTT, 4, ...); // hatches
D -> ofBlock...(gt.blockcasingsTT, 6, ...); // tier generator
E -> ofBlock...(gt.blockcasingsTT, 8, ...);
F -> ofBlock...(gt.blockcasingsTT, 9, ...);
G -> ofFrame;
	 */
	public static final String[][] shape = new String[][]{
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              BBB              ","            BBBBBBB            ","           BBBBBBBBB           ","          BBBBBBBBBBB          ","          BBBBBBBBBBB          ","         BBBBBBBBBBBBB         ","         BBBBBBBBBBBBB         ","         BBBBBBBBBBBBB         ","          BBBBBBBBBBB          ","          BBBBBBBBBBB          ","           BBBBBBBBB           ","            BBBBBBB            ","              BBB              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","          G   CDC   G          ","          B   DED   B          ","          G   CDC   G          ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","          G   CDC   G          ","          B   DED   B          ","          G   CDC   G          ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","          G   CDC   G          ","          B   DED   B          ","          G   CDC   G          ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","          G   CDC   G          ","          B   DED   B          ","          G   CDC   G          ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","               A               ","         G    AAA    G         ","         B   AAEAA   B         ","         G    AAA    G         ","               A               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","              AAA              ","             AA AA             ","         G  AA   AA  G         ","         B  A     A  B         ","         G  AA   AA  G         ","             AA AA             ","              AAA              ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","             AAAAA             ","            AA   AA            ","           AA     AA           ","         G A       A G         ","         B A       A B         ","         G A       A G         ","           AA     AA           ","            AA   AA            ","             AAAAA             ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","              AAA              ","            AA   AA            ","           A       A           ","           A       A           ","        G A         A G        ","        B A         A B        ","        G A         A G        ","           A       A           ","           A       A           ","            AA   AA            ","              AAA              ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","           A       A           ","          A         A          ","        G A         A G        ","        B A         A B        ","        G A         A G        ","          A         A          ","           A       A           ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","              AAA              ","            AA   AA            ","           A       A           ","          A         A          ","          A         A          ","        GA           AG        ","        BA           AB        ","        GA           AG        ","          A         A          ","          A         A          ","           A       A           ","            AA   AA            ","              AAA              ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","          A         A          ","          A         A          ","         A           A         ","       G A           A G       ","       B A           A B       ","       G A           A G       ","         A           A         ","          A         A          ","          A         A          ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","            AAAAAAA            ","           A       A           ","          A         A          ","         A           A         ","         A           A         ","       G A           A G       ","       B A           A B       ","       G A           A G       ","         A           A         ","         A           A         ","          A         A          ","           A       A           ","            AAAAAAA            ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","              AAA              ","            AA   AA            ","          AA       AA          ","          A         A          ","         A           A         ","         A           A         ","       GA             AG       ","       BA             AB       ","       GA             AG       ","         A           A         ","         A           A         ","          A         A          ","          AA       AA          ","            AA   AA            ","              AAA              ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","          A         A          ","         A           A         ","         A           A         ","        A             A        ","      G A             A G      ","      B A             A B      ","      G A             A G      ","        A             A        ","         A           A         ","         A           A         ","          A         A          ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","          A         A          ","         A           A         ","         A           A         ","        A             A        ","      G A             A G      ","      B A             A B      ","      G A             A G      ","        A             A        ","         A           A         ","         A           A         ","          A         A          ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","          A         A          ","         A           A         ","         A           A         ","        A             A        ","      G A             A G      ","      B A             A B      ","      G A             A G      ","        A             A        ","         A           A         ","         A           A         ","          A         A          ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","              GBG              ","             DDDDD             ","           DDAAAAADD           ","          DAA     AAD          ","         DA         AD         ","        DA           AD        ","        DA           AD        ","       DA             AD       ","     GGDA             ADGG     ","     BBDA      F      ADBB     ","     GGDA             ADGG     ","       DA             AD       ","        DA           AD        ","        DA           AD        ","         DA         AD         ","          DAA     AAD          ","           DDAAAAADD           ","             DDDDD             ","              GBG              ","              GBG              ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","          A         A          ","         A           A         ","         A           A         ","        A             A        ","     GG A             A GG     ","     BB A             A BB     ","     GG A             A GG     ","        A             A        ","         A           A         ","         A           A         ","          A         A          ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","              GBG              ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","          A         A          ","         A           A         ","         A           A         ","        A             A        ","     GG A             A GG     ","     BB A             A BB     ","     GG A             A GG     ","        A             A        ","         A           A         ","         A           A         ","          A         A          ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","              GBG              ","                               ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","          A         A          ","         A           A         ","         A           A         ","        A             A        ","    G G A             A G G    ","    B B A             A B B    ","    G G A             A G G    ","        A             A        ","         A           A         ","         A           A         ","          A         A          ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","                               ","              GBG              ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","              GBG              ","              AAA              ","            AA   AA            ","          AA       AA          ","          A         A          ","         A           A         ","         A           A         ","    G  GA             AG  G    ","    B  BA             AB  B    ","    G  GA             AG  G    ","         A           A         ","         A           A         ","          A         A          ","          AA       AA          ","            AA   AA            ","              AAA              ","              GBG              ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","              GBG              ","                               ","            AAAAAAA            ","           A       A           ","          A         A          ","         A           A         ","         A           A         ","    G  G A           A G  G    ","    B  B A           A B  B    ","    G  G A           A G  G    ","         A           A         ","         A           A         ","          A         A          ","           A       A           ","            AAAAAAA            ","                               ","              GBG              ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","          A         A          ","          A         A          ","         A           A         ","   G   G A           A G   G   ","   B   B A           A B   B   ","   G   G A           A G   G   ","         A           A         ","          A         A          ","          A         A          ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","              GBG              ","              AAA              ","            AA   AA            ","           A       A           ","          A         A          ","          A         A          ","   G    GA           AG    G   ","   B    BA           AB    B   ","   G    GA           AG    G   ","          A         A          ","          A         A          ","           A       A           ","            AA   AA            ","              AAA              ","              GBG              ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","              GBG              ","                               ","             AAAAA             ","           AA     AA           ","           A       A           ","          A         A          ","   G    G A         A G    G   ","   B    B A         A B    B   ","   G    G A         A G    G   ","          A         A          ","           A       A           ","           AA     AA           ","             AAAAA             ","                               ","              GBG              ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               "},
		{"                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","              AAA              ","            AA   AA            ","           A       A           ","           A       A           ","  G     G A         A G     G  ","  B     B A         A B     B  ","  G     G A         A G     G  ","           A       A           ","           A       A           ","            AA   AA            ","              AAA              ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               "},
		{"                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","             AAAAA             ","            AA   AA            ","           AA     AA           ","  G      G A       A G      G  ","  B      B A       A B      B  ","  G      G A       A G      G  ","           AA     AA           ","            AA   AA            ","             AAAAA             ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               "},
		{"                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","              AAA              ","             AA AA             ","  G      G  AA   AA  G      G  ","  B      B  A     A  B      B  ","  G      G  AA   AA  G      G  ","             AA AA             ","              AAA              ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               "},
		{"                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","               A               "," G       G    AAA    G       G "," B       B   AAEAA   B       B "," G       G    AAA    G       G ","               A               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               "},
		{"                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               "," G        G   CDC   G        G "," B        B   DED   B        B "," G        G   CDC   G        G ","                               ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               "},
		{"                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","              CCC              "," G        G  CCDCC  G        G "," B        B  CDEDC  B        B "," G        G  CCDCC  G        G ","              CCC              ","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               "},
		{"                               ","             BBBBB             ","             BBBBB             ","             BBBBB             ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","              GBG              ","                               ","                               ","BBB           CCC           BBB","BBB       G  CCDCC  G       BBB","BBB       B  CDEDC  B       BBB","BBB       G  CCDCC  G       BBB","BBB           CCC           BBB","                               ","                               ","              GBG              ","                               ","                               ","                               ","                               ","                               ","                               ","                               ","             BBBBB             ","             BBBBB             ","             BBBBB             "},
		{"              BBB              ","             BBBBB             ","             BBBBB             ","             BBBBB             ","                               ","                               ","                               ","                               ","                               ","                               ","             BBBBB             ","             BGBGB             ","             BBBBB             ","                               ","BBB      BBB  CCC  BBB      BBB","BBB      BGB CCDCC BGB      BBB","BBB      BBB CDEDC BBB      BBB","BBB      BGB CCDCC BGB      BBB","BBB      BBB  CCC  BBB      BBB","                               ","             BBBBB             ","             BGBGB             ","             BBBBB             ","                               ","                               ","                               ","                               ","                               ","                               ","             BBBBB             ","             BBBBB             ","             BBBBB             "},
		{"              B~B              ","             BBBBB             ","             BBBBB             ","             BBBBB             ","             BBBBB             ","            BBBBBBB            ","            BBBBBBB            ","            BBBBBBB            ","           BBBBBBBBB           ","           BBBBBBBBB           ","          BBBBBBBBBBB          ","         BBBBBBBBBBBBB         ","       BBBBBBBBBBBBBBBBB       ","    BBBBBBBBBBBBBBBBBBBBBBB    ","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","    BBBBBBBBBBBBBBBBBBBBBBB    ","       BBBBBBBBBBBBBBBBB       ","         BBBBBBBBBBBBB         ","          BBBBBBBBBBB          ","           BBBBBBBBB           ","           BBBBBBBBB           ","            BBBBBBB            ","            BBBBBBB            ","            BBBBBBB            ","             BBBBB             ","             BBBBB             ","             BBBBB             ","             BBBBB             "},
		{"              BBB              ","             BBBBB             ","             BBBBB             ","             BBBBB             ","             BBBBB             ","            BBBBBBB            ","            BBBBBBB            ","            BBBBBBB            ","           BBBBBBBBB           ","           BBBBBBBBB           ","          BBBBBBBBBBB          ","         BBBBBBBBBBBBB         ","       BBBBBBBBBBBBBBBBB       ","    BBBBBBBBBBBBBBBBBBBBBBB    ","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB","    BBBBBBBBBBBBBBBBBBBBBBB    ","       BBBBBBBBBBBBBBBBB       ","         BBBBBBBBBBBBB         ","          BBBBBBBBBBB          ","           BBBBBBBBB           ","           BBBBBBBBB           ","            BBBBBBB            ","            BBBBBBB            ","            BBBBBBB            ","             BBBBB             ","             BBBBB             ","             BBBBB             ","             BBBBB             "}
	};
	
	// spotless:on
    // endregion

    // region Overrides

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("fieldGeneratorTier", fieldGeneratorTier);
        aNBT.setByte("glassTier", glassTier);
        aNBT.setByte("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        fieldGeneratorTier = aNBT.getInteger("fieldGeneratorTier");
        glassTier = aNBT.getByte("glassTier");
        mode = aNBT.getByte("mode");
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(Tooltip_CrystallineInfinitier_MachineType)
            .addInfo(Tooltip_CrystallineInfinitier_00)
            .addInfo(Tooltip_CrystallineInfinitier_01)
            .addInfo(Tooltip_CrystallineInfinitier_02)
            .addInfo(Tooltip_CrystallineInfinitier_03)
            .addInfo(Tooltip_CrystallineInfinitier_04)
            .addInfo(Tooltip_CrystallineInfinitier_05)
            .addInfo(Tooltip_CrystallineInfinitier_06)
            .addInfo(textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(31, 36, 32, false)
            .addInputHatch(textUseBlueprint, 2)
            .addOutputHatch(textUseBlueprint, 2)
            .addInputBus(textUseBlueprint, 2)
            .addOutputBus(textUseBlueprint, 2)
            .addMaintenanceHatch(textUseBlueprint, 1)
            .addEnergyHatch(textUseBlueprint, 1)
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_CrystallineInfinitier(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)) };
    }
    // endregion
}
