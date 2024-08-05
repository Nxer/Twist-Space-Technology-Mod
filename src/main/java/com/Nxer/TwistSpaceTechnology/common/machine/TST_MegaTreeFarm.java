package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.Nxer.TwistSpaceTechnology.util.Utils.metaItemEqual;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm.Mode;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm.treeProductsMap;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler;
import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.TreeManager;
import galaxyspace.BarnardsSystem.BRFluids;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_IntegratedCircuit_Item;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_MegaTreeFarm extends GTCM_MultiMachineBase<TST_MegaTreeFarm> {

    // region Class Constructor
    public TST_MegaTreeFarm(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_MegaTreeFarm(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaTreeFarm(this.mName);
    }

    // region Structure
    private int controllerTier = 0;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aTick % 20 == 0 && controllerTier == 0) {
            ItemStack ControllerSlot = this.getControllerSlot();
            if (metaItemEqual(GTCMItemList.TestItem0.get(1), ControllerSlot)) {
                controllerTier = 1;
                mInventory[1] = ItemUtils.depleteStack(ControllerSlot);
                markDirty();
                // schedule a structure check
                mUpdated = true;
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (controllerTier == 0 && !aPlayer.isSneaking()) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (metaItemEqual(GTCMItemList.TestItem0.get(1), heldItem)) {
                controllerTier = 1;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem));
                if (getBaseMetaTileEntity().isServerSide()) {
                    markDirty();
                    aPlayer.inventory.markDirty();
                    // schedule a structure check
                    mUpdated = true;
                }
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

    @Override
    public void onValueUpdate(byte aValue) {
        controllerTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) controllerTier;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mTier", (byte) controllerTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        controllerTier = aNBT.getByte("mTier");
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        aNBT.setByte("mTier", (byte) controllerTier);
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        super.addAdditionalTooltipInformation(stack, tooltip);
        NBTTagCompound aNBT = stack.getTagCompound();
        int tier = aNBT.getInteger("mTier") + 1;
        tooltip.add(StatCollector.translateToLocalFormatted("tooltip.large_macerator.tier", tier));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tier", controllerTier + 1);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("tier")) {
            currentTip.add(
                "Tier: " + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(tag.getInteger("tier"))
                    + EnumChatFormatting.RESET);
        }
    }

    private static final String STRUCTURE_PIECE_MAIN = "mainMegaTreeFarm0";
    private static final String STRUCTURE_PIECE_MAIN1 = "mainMegaTreeFarm1";
    private static final String STRUCTURE_PIECE_WATER = "waterMegaTreeFarm";
    private static IStructureDefinition<TST_MegaTreeFarm> STRUCTURE_DEFINITION = null;

    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        int structureTier = stackSize.stackSize + controllerTier - 1;
        if (structureTier > 1) structureTier = 1;
        this.buildPiece("mainMegaTreeFarm" + structureTier, stackSize, hintsOnly, 16, 38, 7);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built;
        int builtW;
        int structureTier = stackSize.stackSize + controllerTier - 1;
        if (structureTier > 1) structureTier = 1;
        built = survivialBuildPiece(
            "mainMegaTreeFarm" + structureTier,
            stackSize,
            16,
            38,
            7,
            elementBudget,
            env,
            false,
            true);
        builtW = survivialBuildPiece(STRUCTURE_PIECE_WATER, stackSize, 0, 37, -9, elementBudget, env, false, true);
        if (built >= 0) return built;
        return built + builtW;

    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        // setDebugEnabled(true);
        return checkPiece("mainMegaTreeFarm" + controllerTier, 16, 38, 7)
            & checkPiece(STRUCTURE_PIECE_WATER, 0, 37, -9);
    }

    @Override
    public IStructureDefinition<TST_MegaTreeFarm> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaTreeFarm>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addShape(STRUCTURE_PIECE_MAIN1, transpose(shape2))
                .addShape(STRUCTURE_PIECE_WATER, transpose(water))
                .addElement('A', BorosilicateGlass.ofBoroGlassAnyTier())
                .addElement('B', ofBlock(MetaBlockCasing01, 9))
                .addElement('C', ofBlock(MetaBlockCasing01, 10))
                .addElement('D', ofBlock(GregTech_API.sBlockCasings1, 10))
                .addElement('d', ofBlock(GregTech_API.sBlockCasings4, 1))
                .addElement('E', ofBlock(GregTech_API.sBlockCasings8, 5))
                .addElement('F', ofBlock(GregTech_API.sBlockCasings8, 10))
                .addElement('G', ofBlock(GregTech_API.sBlockCasings9, 1))
                .addElement('H', ofBlock(ModBlocks.blockCasings2Misc, 15))
                .addElement('h', ofBlock(MetaBlockCasing01, 13))
                .addElement('I', ofBlock(ModBlocks.blockCasingsTieredGTPP, 8))
                .addElement(
                    'J',
                    ofBlock(ModBlocksHandler.BlockTranslucent.getLeft(), ModBlocksHandler.BlockTranslucent.getRight()))
                .addElement(
                    'K',
                    ofBlock(ModBlocksHandler.AirCrystalBlock.getLeft(), ModBlocksHandler.AirCrystalBlock.getRight()))
                .addElement(
                    'L',
                    ofBlock(
                        ModBlocksHandler.WaterCrystalBlock.getLeft(),
                        ModBlocksHandler.WaterCrystalBlock.getRight()))
                .addElement(
                    'M',
                    ofBlock(
                        ModBlocksHandler.EarthCrystalBlock.getLeft(),
                        ModBlocksHandler.EarthCrystalBlock.getRight()))
                .addElement(
                    'N',
                    Mods.Forestry.isModLoaded() ? ofBlock(Block.getBlockFromName("Forestry:soil"), 0)
                        : ofBlock(Blocks.dirt, 0))
                .addElement(
                    'O',
                    Mods.ProjectRedIllumination.isModLoaded()
                        ? ofBlock(Block.getBlockFromName("ProjRed|Illumination:projectred.illumination.lamp"), 10)
                        : ofBlock(Blocks.redstone_lamp, 0))
                .addElement('P', ofBlock(Blocks.water, 0))
                .addElement(
                    'Q',
                    ofChain(
                        ofBlock(ModBlocks.blockCasings2Misc, 15),
                        GT_HatchElementBuilder.<TST_MegaTreeFarm>builder()
                            .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy))
                            .adder(TST_MegaTreeFarm::addToMachineList)
                            .dot(1)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .build()))
                .addElement(
                    'q',
                    ofChain(
                        ofBlock(MetaBlockCasing01, 13),
                        GT_HatchElementBuilder.<TST_MegaTreeFarm>builder()
                            .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy))
                            .adder(TST_MegaTreeFarm::addToMachineList)
                            .dot(1)
                            .casingIndex(BasicBlocks.MetaBlockCasing01.getTextureIndex(13))
                            .build()))
                .addElement(
                    'R',
                    ofChain(
                        ofBlock(ModBlocks.blockCasings2Misc, 15),
                        GT_HatchElementBuilder.<TST_MegaTreeFarm>builder()
                            .atLeast(Energy.or(ExoticEnergy))
                            .adder(TST_MegaTreeFarm::addToMachineList)
                            .dot(2)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .build()))
                .addElement(
                    'r',
                    ofChain(
                        ofBlock(MetaBlockCasing01, 13),
                        GT_HatchElementBuilder.<TST_MegaTreeFarm>builder()
                            .atLeast(Energy.or(ExoticEnergy))
                            .adder(TST_MegaTreeFarm::addToMachineList)
                            .dot(2)
                            .casingIndex(BasicBlocks.MetaBlockCasing01.getTextureIndex(13))
                            .build()))
                .addElement('S', ofFrame(Materials.Vulcanite))
                .addElement('s', ofFrame(Materials.AstralSilver))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) {
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(
                        controllerTier == 0 ? TAE.getIndexFromPage(1, 15)
                            : BasicBlocks.MetaBlockCasing01.getTextureIndex(13)),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(
                controllerTier == 0 ? TAE.getIndexFromPage(1, 15) : BasicBlocks.MetaBlockCasing01.getTextureIndex(13)),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                    .extFacing()
                    .build() };
        }

        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(
            controllerTier == 0 ? TAE.getIndexFromPage(1, 15) : BasicBlocks.MetaBlockCasing01.getTextureIndex(13)) };
    }

    // spotless:off

    /*
    A -> ofBlock...(BW_GlasBlocks, 0, ...);
    B -> ofBlock...(MetaBlockCasing01, 9, ...);
    C -> ofBlock...(MetaBlockCasing01, 10, ...);
    D -> ofBlock...(gt.blockcasings, 10, ...);
    E -> ofBlock...(gt.blockcasings8, 5, ...);
    F -> ofBlock...(gt.blockcasings8, 10, ...);
    G -> ofBlock...(gt.blockcasings9, 1, ...);
    H -> ofBlock...(gtplusplus.blockcasings.2, 15, ...);
    I -> ofBlock...(gtplusplus.blocktieredcasings.1, 8, ...);
    J -> ofBlock...(tile.blockTranslucent, 0, ...);
    K -> ofBlock...(tile.crystalBlock, 0, ...);
    L -> ofBlock...(tile.crystalBlock, 2, ...);
    M -> ofBlock...(tile.crystalBlock, 3, ...);
    N -> ofBlock...(tile.for.soil, 0, ...);
    O -> ofBlock...(tile.redstoneLight, 0, ...);
    P -> ofBlock...(tile.water, 0, ...);
    Q -> ofBlock...(tile.wood, 0, ...);
    R -> ofBlock...(tile.wood, 1, ...);
    S -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...);
    */

    private final String[][] shape = new String[][]{
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              DDDDD              ","             DDDDDDD             ","             DDDDDDD             ","             DDDDDDD             ","             DDDDDDD             ","             DDDDDDD             ","              DDDDD              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","             GGD DGG             ","            GGGD DGGG            ","           GGGHH HHGGG           ","           GGHSH HSHGG           ","          DDDHHH HHHDDD          ","                                 ","          DDDHHH HHHDDD          ","           GGHSH HSHGG           ","           GGGHH HHGGG           ","            GGGD DGGG            ","             GGD DGG             ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             GGD DGG             ","           DGGGSDSGGGD           ","          DGG  SDS  GGD          ","          GG  HHDHH  GG          ","         GG  HIIDIIH  GG         ","         GG HIIIDIIIH GG         ","         DSSHIIIDIIIHSSD         ","          DDDDDDDDDDDDD          ","         DSSHIIIDIIIHSSD         ","         GG HIIIDIIIH GG         ","         GG  HIIDIIH  GG         ","          GG  HHDHH  GG          ","          DGG  SDS  GGD          ","           DGGGSDSGGGD           ","             GGD DGG             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","            DGGD DGGD            ","           AD  SDS  DA           ","          A     S     A          ","         A      S      A         ","        DD    DDDDD    DD        ","        G    DOOOOOD    G        ","        G   DOOOOOOOD   G        ","       DDS  DOOOOOOOD  SDD       ","         DSSDOOOOOOODSSD         ","       DDS  DOOOOOOOD  SDD       ","        G   DOOOOOOOD   G        ","        G    DOOOOOD    G        ","        DD    DDDDD    DD        ","         A      S      A         ","          A     S     A          ","           AD  SDS  DA           ","            DGGD DGGD            ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","            ADDSDSDDA            ","          AA   SDS   AA          ","         AA           AA         ","        AA             AA        ","        A               A        ","       A                 A       ","       D                 D       ","       D                 D       ","      DSS               SSD      ","       DD               DD       ","      DSS               SSD      ","       D                 D       ","       D                 D       ","       A                 A       ","        A               A        ","        AA             AA        ","         AA           AA         ","          AA   SDS   AA          ","            ADDSDSDDA            ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","            AAAD DAAA            ","          AA   SDS   AA          ","         A             A         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","      A                   A      ","      DS                 SD      ","       D                 D       ","      DS                 SD      ","      A                   A      ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         A             A         ","          AA   SDS   AA          ","            AAAD DAAA            ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","             AAD DAA             ","           AA  SDS  AA           ","         AA           AA         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","     DS                   SD     ","      D                   D      ","     DS                   SD     ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         AA           AA         ","           AA  SDS  AA           ","             AAD DAA             ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               D D               ","           AAAASDSAAAA           ","          A           A          ","        AA             AA        ","       A                 A       ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","    DS                     SD    ","     D                     D     ","    DS                     SD    ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","       A                 A       ","        AA             AA        ","          A           A          ","           AAAASDSAAAA           ","               D D               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            DDDD DDDD            ","          DD   S S   DD          ","        DD             DD        ","       D                 D       ","      D                   D      ","      D                   D      ","     D                     D     ","     D                     D     ","    D                       D    ","    D                       D    ","    D                       D    ","    DS                     SD    ","                                 ","    DS                     SD    ","    D                       D    ","    D                       D    ","    D                       D    ","     D                     D     ","     D                     D     ","      D                   D      ","      D                   D      ","       D                 D       ","        DD             DD        ","          DD   S S   DD          ","            DDDD DDDD            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            AAADDDAAA            ","          AA   S S   AA          ","        AA             AA        ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    DS                     SD    ","    D                       D    ","    DS                     SD    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","        AA             AA        ","          AA   S S   AA          ","            AAADDDAAA            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              AA AA              ","           AAA SDS AAA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   AS                       SA   ","    D                       D    ","   AS                       SA   ","    A                        A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AAA SDS AAA           ","              AA AA              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              AADAA              ","           AAA     AAA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AAA     AAA           ","              AADAA              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            AAAADAAAA            ","          AA         AA          ","        AA             AA        ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    A                       A    ","    D                       D    ","    A                       A    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","        AA             AA        ","          AA         AA          ","            AAAADAAAA            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             AAADAAA             ","          AAA       AAA          ","         A             A         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    D                       D    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         A             A         ","          AAA       AAA          ","             AAADAAA             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               ADA               ","           AAAA   AAAA           ","          A           A          ","        AA             AA        ","       A                 A       ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","    A                       A    ","    D                       D    ","    A                       A    ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","       A                 A       ","        AA             AA        ","          A           A          ","           AAAA   AAAA           ","               ADA               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                D                ","             AAAHAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","    DH                     HD    ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAAHAAA             ","                D                ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","            DDDDDDDDD            ","          DDHHHHHHHHHDD          ","         DHHNNNN NNNNHHD         ","        DHNNNNNNNNNNNNNHD        ","       DHNNNNNNNNNNNNNNNHD       ","      DHNNNNNNNNNNNNNNNNNHD      ","      DHNNNNNNNNNNNNNNNNNHD      ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DH NNNNNNNNNNNNNNNNN HD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","      DHNNNNNNNNNNNNNNNNNHD      ","      DHNNNNNNNNNNNNNNNNNHD      ","       DHNNNNNNNNNNNNNNNHD       ","        DHNNNNNNNNNNNNNHD        ","         DHHNNNN NNNNHHD         ","          DDHHHHHHHHHDD          ","            DDDDDDDDD            ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","            SSSDHDSSS            ","          SSAAAN NAAASS          ","         SAANNNN NNNNAAS         ","        SAANNNNN NNNNNAAS        ","       SAANNNNNNNNNNNNNAAS       ","       SANNNNNNNNNNNNNNNAS       ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","      DNNNNNNNNNNNNNNNNNNND      ","     DH   NNNNNNNNNNNNN   HD     ","      DNNNNNNNNNNNNNNNNNNND      ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","       SANNNNNNNNNNNNNNNAS       ","       SAANNNNNNNNNNNNNAAS       ","        SAANNNNN NNNNNAAS        ","         SAANNNN NNNNAAS         ","          SSAAAN NAAASS          ","            SSSDHDSSS            ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","               AHA               ","            AAAAHAAAA            ","           AANNN NNNAA           ","          ANNNNN NNNNNA          ","         ANNNNNNNNNNNNNA         ","        AANNNNNNNNNNNNNAA        ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","       AANNNNNNNNNNNNNNNAA       ","      DHH  NNNNNNNNNNN  HHD      ","       AANNNNNNNNNNNNNNNAA       ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","        AANNNNNNNNNNNNNAA        ","         ANNNNNNNNNNNNNA         ","          ANNNNN NNNNNA          ","           AANNN NNNAA           ","            AAAAHAAAA            ","               AHA               ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","                D                ","             AAAHAAA             ","           AAAAN NAAAA           ","          AAANNN NNNAAA          ","          AANNNN NNNNAA          ","         AANNNNN NNNNNAA         ","         AANNNNNNNNNNNAA         ","         ANNNNNNNNNNNNNA         ","       DDH    NNNNN    HDD       ","         ANNNNNNNNNNNNNA         ","         AANNNNNNNNNNNAA         ","         AANNNNN NNNNNAA         ","          AANNNN NNNNAA          ","          AAANNN NNNAAA          ","           AAAAN NAAAA           ","             AAAHAAA             ","                D                ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","               AHA               ","             AAAHAAA             ","            AAAAHAAAA            ","           AAANN NNAAA           ","           AANNN NNNAA           ","          AAANNN NNNAAA          ","         DHHH       HHHD         ","          AAANNN NNNAAA          ","           AANNN NNNAA           ","           AAANN NNAAA           ","            AAAAHAAAA            ","             AAAHAAA             ","               AHA               ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","                D                ","              DDDDD              ","             DHHHHHD             ","            DHHHHHHHD            ","            DHHHHHHHD            ","          DDDHHH HHHDDD          ","            DHHHHHHHD            ","            DHHHHHHHD            ","             DHHHHHD             ","              DDDDD              ","                D                ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              DDDDD              ","             DDJDJDD             ","             DJJDJJD             ","             DDD DDD             ","             DJJDJJD             ","             DDJDJDD             ","              DDDDD              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","               LLL               ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","               LLL               ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              EEEEE              ","             E     E             ","            E       E            ","            E  LLL  E            ","            E  LLL  E            ","            E  LLL  E            ","            E       E            ","             E     E             ","              EEEEE              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             DDDDDDD             ","            DDSSSSSDD            ","          DDSSGGGGGSSDD          ","          DSGGGGGGGGGSD          ","         DSGGGEEEEEGGGSD         ","        DDSGGFBBBBBFGGSDD        ","        DSGGEBB   BBEGGSD        ","        DSGGEB LLL BEGGSD        ","        DSGGEB LLL BEGGSD        ","        DSGGEB LLL BEGGSD        ","        DSGGEBB   BBEGGSD        ","        DDSGGFBBBBBFGGSDD        ","         DSGGGEEEEEGGGSD         ","          DSGGGGGGGGGSD          ","          DDSSGGGGGSSDD          ","            DDSSSSSDD            ","             DDDDDDD             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             D     D             ","             DSQ~QSD             ","            SDIIIIIDS            ","          SSIIIIIIIIISS          ","         SIIIIIIIIIIIIIS         ","         SIIIIIIIIIIIIIS         ","        SIIIIIEEEEEIIIIIS        ","      DDDIIIIFMMMMMFIIIIDDD      ","       SIIIIEMMBBBMMEIIIIS       ","       SIIIIEMBBBBBMEIIIIS       ","       SIIIIEMBBBBBMEIIIIS       ","       SIIIIEMBBBBBMEIIIIS       ","       SIIIIEMMBBBMMEIIIIS       ","      DDDIIIIFMMMMMFIIIIDDD      ","        SIIIIIEEEEEIIIIIS        ","         SIIIIIIIIIIIIIS         ","         SIIIIIIIIIIIIIS         ","          SSIIIIIIIIISS          ","            SDIIIIIDS            ","             DSSSSSD             ","             D     D             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             DDDDDDD             ","           DDDQQQQQDDD           ","         DDQQHQQQQQHQQDD         ","        DQQQQHGHHHGHQQQQD        ","       DQQQQGHIIIIIHGQQQQD       ","      DQQQGGIIKKKKKIIGGQQQD      ","      DQQGIIKKKKKKKKKIIGQQD      ","     DQQQGIKKKKKKKKKKKIGQQQD     ","     DQQGIKKKKEEEEEKKKKIGQQD     ","    DDHHHIKKKFMMMMMFKKKIHHHDD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DDHHHIKKKFMMMMMFKKKIHHHDD    ","     DQQGIKKKKEEEEEKKKKIGQQD     ","     DQQQGIKKKKKKKKKKKIGQQQD     ","      DQQGIIKKKKKKKKKIIGQQD      ","      DQQQGGIIKKKKKIIGGQQQD      ","       DQQQQGHIIIIIHGQQQQD       ","        DQQQQHGGGGGHQQQQD        ","         DDQQHQQQQQHQQDD         ","           DDDQQQQQDDD           ","             DDDDDDD             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","            DHHHHHHHD            ","           HDHHHDHHHDH           ","         HHHHDDDDDDDHHHH         ","        HHHDDCCCCCCCDDHHH        ","       HHDDCCCCCCCCCCCDDHH       ","      HHDCCCCCDDDDDCCCCCDHH      ","     HHDCCCCHHCCCCCHHCCCCDHH     ","     HHDCCHHCCCCCCCCCHHCCDHH     ","    HHDCCCHCCCCCCCCCCCHCCCDHH    ","   DDHDCCHCCCCEEEEECCCCHCCDHDD   ","   HHDCCCHCCCFMMMMMFCCCHCCCDHH   ","   HHDCCDCCCEMMMMMMMECCCDCCDHH   ","   HHDCCDCCCEMMMMMMMECCCDCCDHH   ","   HDDCCDCCCEMMMMMMMECCCDCCDDH   ","   HHDCCDCCCEMMMMMMMECCCDCCDHH   ","   HHDCCDCCCEMMMMMMMECCCDCCDHH   ","   HHDCCCHCCCFMMMMMFCCCHCCCDHH   ","   DDHDCCHCCCCEEEEECCCCHCCDHDD   ","    HHDCCCHCCCCCCCCCCCHCCCDHH    ","     HHDCCHHCCCCCCCCCHHCCDHH     ","     HHDCCCCHHCCCCCHHCCCCDHH     ","      HHDCCCCCDDDDDCCCCCDHH      ","       HHDDCCCCCCCCCCCDDHH       ","        HHHDDCCCCCCCDDHHH        ","         HHHHDDDDDDDHHHH         ","           HDHHHDHHHDH           ","            DHHHHHHHD            ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","            D       D            ","            SHHJJJHHS            ","           HHHHHDHHHHH           ","         JJHHCCCCCCCHHJJ         ","        HHHCCCCCCCCCCCHHH        ","       JHCCCCCCCCCCCCCCCHJ       ","      HHCCCCCCHHHHHCCCCCCHH      ","     JHCCCCCHHGGGGGHHCCCCCHJ     ","     JHCCCHHGGGGGGGGGHHCCCHJ     ","    HHCCCCHGGGGGGGGGGGHCCCCHH    ","  DSHHCCCHGGGGEEEEEGGGGHCCCHHSD  ","   HHCCCCHGGGFMMMMMFGGGHCCCCHH   ","   HHCCCHGGGEMMMMMMMEGGGHCCCHH   ","   JHCCCHGGGEMMMMMMMEGGGHCCCHJ   ","   JDCCCHGGGEMMMMMMMEGGGHCCCDJ   ","   JHCCCHGGGEMMMMMMMEGGGHCCCHJ   ","   HHCCCHGGGEMMMMMMMEGGGHCCCHH   ","   HHCCCCHGGGFMMMMMFGGGHCCCCHH   ","  DSHHCCCHGGGGEEEEEGGGGHCCCHHSD  ","    HHCCCCHGGGGGGGGGGGHCCCCHH    ","     JHCCCHHGGGGGGGGGHHCCCHJ     ","     JHCCCCCHHGGGGGHHCCCCCHJ     ","      HHCCCCCCHHHHHCCCCCCHH      ","       JHCCCCCCCCCCCCCCCHJ       ","        HHHCCCCCCCCCCCHHH        ","         JJHHCCCCCCCHHJJ         ","           HHHHHDHHHHH           ","            SHHJJJHHS            ","            D       D            ","                                 ","                                 "},
        {"                                 ","            D       D            ","            SS     SS            ","            SHRRRRRHS            ","           HHHHHDHHHHH           ","         HHHHHHHHHHHHHHH         ","        HHHRRHHHHHHHRRHHH        ","       HHRRRJRHHHHHRJRRRHH       ","      HHRRJRRRHHHHHRRRJRRHH      ","     HHRRRRRHH     HHRRRRRHH     ","     HHRJRHH         HHRJRHH     ","    HHRRRRH           HRRRRHH    "," DSSHHRJRH    EEEEE    HRJRHHSSD ","  SHHHHRRH   FMMMMMF   HRRHHHHS  ","   RHHHHH   EMMMMMMME   HHHHHR   ","   RHHHHH   EMMMMMMME   HHHHHR   ","   RDHHHH   EMMMMMMME   HHHHDR   ","   RHHHHH   EMMMMMMME   HHHHHR   ","   RHHHHH   EMMMMMMME   HHHHHR   ","  SHHHHRRH   FMMMMMF   HRRHHHHS  "," DSSHHRJRH    EEEEE    HRJRHHSSD ","    HHRRRRH           HRRRRHH    ","     HHRJRHH         HHRJRHH     ","     HHRRRRRHH     HHRRRRRHH     ","      HHRRJRRRHHHHHRRRJRRHH      ","       HHRRRJRHHHHHRJRRRHH       ","        HHHRRHHHHHHHRRHHH        ","         HHHHHHHHHHHHHHH         ","           HHHHHDHHHHH           ","            SHRRRRRHS            ","            SS     SS            ","            D       D            ","                                 "},
        {"            DDDDDDDDD            ","            DSSSSSSSD            ","           DDD     DDD           ","           DDDDDDDDDDD           ","           DDDDDDDDDDD           ","         DDDDDDDDDDDDDDD         ","        DDD  DDDDDDD  DDD        ","       DD     DDDDD     DD       ","      DD      DDDDD      DD      ","     DD     DD     DD     DD     ","     DD   DD         DD   DD     ","  DDDD    D           D    DDDD  ","DDDDDD   D    EJJJE    D   DDDDDD","DSDDDDD  D   FMMMMMF   D  DDDDDSD","DS DDDDDD   EMMMMMMME   DDDDDD SD","DS DDDDDD   JMMMMMMMJ   DDDDDD SD","DS DDDDDD   JMMMMMMMJ   DDDDDD SD","DS DDDDDD   JMMMMMMMJ   DDDDDD SD","DS DDDDDD   EMMMMMMME   DDDDDD SD","DSDDDDD  D   FMMMMMF   D  DDDDDSD","DDDDDD   D    EJJJE    D   DDDDDD","  DDDD    D           D    DDDD  ","     DD   DD         DD   DD     ","     DD     DD     DD     DD     ","      DD      DDDDD      DD      ","       DD     DDDDD     DD       ","        DDD  DDDDDDD  DDD        ","         DDDDDDDDDDDDDDD         ","           DDDDDDDDDDD           ","           DDDDDDDDDDD           ","           DDD     DDD           ","            DSSSSSSSD            ","            DDDDDDDDD            "},
        {"            BBBBBBBBB            ","          BBB       BBB          ","        BBBBBB     BBBBBB        ","      BB  BBBBBBBBBBBBB  BB      ","     B   BBBBBBBBBBBBBBB   B     ","    B   BBBBBBBBBBBBBBBBB   B    ","   B   BBBB  BBBBBBB  BBBB   B   ","   B  BBB     BBBBB     BBB  B   ","  B  BBB      BBBBB      BBB  B  ","  B BBB     BB     BB     BBB B  "," BBBBBB   BB         BB   BBBBBB "," BBBBB    B           B    BBBBB ","BBBBBB   B    EEEEE    B   BBBBBB","B BBBBB  B   EFFFFFE   B  BBBBB B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B BBBBB  B   EFFFFFE   B  BBBBB B","BBBBBB   B    EEEEE    B   BBBBBB"," BBBBB    B           B    BBBBB "," BBBBBB   BB         BB   BBBBBB ","  B BBB     BB     BB     BBB B  ","  B  BBB      BBBBB      BBB  B  ","   B  BBB     BBBBB     BBB  B   ","   B   BBBB  BBBBBBB  BBBB   B   ","    B   BBBBBBBBBBBBBBBBB   B    ","     B   BBBBBBBBBBBBBBB   B     ","      BB  BBBBBBBBBBBBB  BB      ","        BBBBBB     BBBBBB        ","          BBB       BBB          ","            BBBBBBBBB            "}
    };

    private final String [][] shape2 =new String[][]{
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              ddddd              ","             ddddddd             ","             ddddddd             ","             ddddddd             ","             ddddddd             ","             ddddddd             ","              ddddd              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               d d               ","             GGd dGG             ","            GGGd dGGG            ","           GGGhh hhGGG           ","           GGhsh hshGG           ","          dddhhh hhhddd          ","                                 ","          dddhhh hhhddd          ","           GGhsh hshGG           ","           GGGhh hhGGG           ","            GGGd dGGG            ","             GGd dGG             ","               d d               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             GGd dGG             ","           dGGGsdsGGGd           ","          dGG  sds  GGd          ","          GG  hhdhh  GG          ","         GG  hIIdIIh  GG         ","         GG hIIIdIIIh GG         ","         dsshIIIdIIIhssd         ","          ddddddddddddd          ","         dsshIIIdIIIhssd         ","         GG hIIIdIIIh GG         ","         GG  hIIdIIh  GG         ","          GG  hhdhh  GG          ","          dGG  sds  GGd          ","           dGGGsdsGGGd           ","             GGd dGG             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               d d               ","            dGGd dGGd            ","           Ad  sds  dA           ","          A     s     A          ","         A      s      A         ","        dd    ddddd    dd        ","        G    dOOOOOd    G        ","        G   dOOOOOOOd   G        ","       dds  dOOOOOOOd  sdd       ","         dssdOOOOOOOdssd         ","       dds  dOOOOOOOd  sdd       ","        G   dOOOOOOOd   G        ","        G    dOOOOOd    G        ","        dd    ddddd    dd        ","         A      s      A         ","          A     s     A          ","           Ad  sds  dA           ","            dGGd dGGd            ","               d d               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               d d               ","            AddsdsddA            ","          AA   sds   AA          ","         AA           AA         ","        AA             AA        ","        A               A        ","       A                 A       ","       d                 d       ","       d                 d       ","      dss               ssd      ","       dd               dd       ","      dss               ssd      ","       d                 d       ","       d                 d       ","       A                 A       ","        A               A        ","        AA             AA        ","         AA           AA         ","          AA   sds   AA          ","            AddsdsddA            ","               d d               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","            AAAd dAAA            ","          AA   sds   AA          ","         A             A         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","      A                   A      ","      ds                 sd      ","       d                 d       ","      ds                 sd      ","      A                   A      ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         A             A         ","          AA   sds   AA          ","            AAAd dAAA            ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","             AAd dAA             ","           AA  sds  AA           ","         AA           AA         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","     ds                   sd     ","      d                   d      ","     ds                   sd     ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         AA           AA         ","           AA  sds  AA           ","             AAd dAA             ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               d d               ","           AAAAsdsAAAA           ","          A           A          ","        AA             AA        ","       A                 A       ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","    ds                     sd    ","     d                     d     ","    ds                     sd    ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","       A                 A       ","        AA             AA        ","          A           A          ","           AAAAsdsAAAA           ","               d d               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            dddd dddd            ","          dd   s s   dd          ","        dd             dd        ","       d                 d       ","      d                   d      ","      d                   d      ","     d                     d     ","     d                     d     ","    d                       d    ","    d                       d    ","    d                       d    ","    ds                     sd    ","                                 ","    ds                     sd    ","    d                       d    ","    d                       d    ","    d                       d    ","     d                     d     ","     d                     d     ","      d                   d      ","      d                   d      ","       d                 d       ","        dd             dd        ","          dd   s s   dd          ","            dddd dddd            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            AAAdddAAA            ","          AA   s s   AA          ","        AA             AA        ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    ds                     sd    ","    d                       d    ","    ds                     sd    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","        AA             AA        ","          AA   s s   AA          ","            AAAdddAAA            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              AA AA              ","           AAA sds AAA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   As                       sA   ","    d                       d    ","   As                       sA   ","    A                        A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AAA sds AAA           ","              AA AA              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              AAdAA              ","           AAA     AAA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AAA     AAA           ","              AAdAA              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            AAAAdAAAA            ","          AA         AA          ","        AA             AA        ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    A                       A    ","    d                       d    ","    A                       A    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","        AA             AA        ","          AA         AA          ","            AAAAdAAAA            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             AAAdAAA             ","          AAA       AAA          ","         A             A         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    d                       d    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         A             A         ","          AAA       AAA          ","             AAAdAAA             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               AdA               ","           AAAA   AAAA           ","          A           A          ","        AA             AA        ","       A                 A       ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","    A                       A    ","    d                       d    ","    A                       A    ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","       A                 A       ","        AA             AA        ","          A           A          ","           AAAA   AAAA           ","               AdA               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                d                ","             AAAhAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","    dh                     hd    ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAAhAAA             ","                d                ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","            ddddddddd            ","          ddhhhhhhhhhdd          ","         dhhNNNN NNNNhhd         ","        dhNNNNNNNNNNNNNhd        ","       dhNNNNNNNNNNNNNNNhd       ","      dhNNNNNNNNNNNNNNNNNhd      ","      dhNNNNNNNNNNNNNNNNNhd      ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dh NNNNNNNNNNNNNNNNN hd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","      dhNNNNNNNNNNNNNNNNNhd      ","      dhNNNNNNNNNNNNNNNNNhd      ","       dhNNNNNNNNNNNNNNNhd       ","        dhNNNNNNNNNNNNNhd        ","         dhhNNNN NNNNhhd         ","          ddhhhhhhhhhdd          ","            ddddddddd            ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","            sssdhdsss            ","          ssAAAN NAAAss          ","         sAANNNN NNNNAAs         ","        sAANNNNN NNNNNAAs        ","       sAANNNNNNNNNNNNNAAs       ","       sANNNNNNNNNNNNNNNAs       ","      sANNNNNNNNNNNNNNNNNAs      ","      sANNNNNNNNNNNNNNNNNAs      ","      sANNNNNNNNNNNNNNNNNAs      ","      dNNNNNNNNNNNNNNNNNNNd      ","     dh   NNNNNNNNNNNNN   hd     ","      dNNNNNNNNNNNNNNNNNNNd      ","      sANNNNNNNNNNNNNNNNNAs      ","      sANNNNNNNNNNNNNNNNNAs      ","      sANNNNNNNNNNNNNNNNNAs      ","       sANNNNNNNNNNNNNNNAs       ","       sAANNNNNNNNNNNNNAAs       ","        sAANNNNN NNNNNAAs        ","         sAANNNN NNNNAAs         ","          ssAAAN NAAAss          ","            sssdhdsss            ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","               AhA               ","            AAAAhAAAA            ","           AANNN NNNAA           ","          ANNNNN NNNNNA          ","         ANNNNNNNNNNNNNA         ","        AANNNNNNNNNNNNNAA        ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","       AANNNNNNNNNNNNNNNAA       ","      dhh  NNNNNNNNNNN  hhd      ","       AANNNNNNNNNNNNNNNAA       ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","        AANNNNNNNNNNNNNAA        ","         ANNNNNNNNNNNNNA         ","          ANNNNN NNNNNA          ","           AANNN NNNAA           ","            AAAAhAAAA            ","               AhA               ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","                d                ","             AAAhAAA             ","           AAAAN NAAAA           ","          AAANNN NNNAAA          ","          AANNNN NNNNAA          ","         AANNNNN NNNNNAA         ","         AANNNNNNNNNNNAA         ","         ANNNNNNNNNNNNNA         ","       ddh    NNNNN    hdd       ","         ANNNNNNNNNNNNNA         ","         AANNNNNNNNNNNAA         ","         AANNNNN NNNNNAA         ","          AANNNN NNNNAA          ","          AAANNN NNNAAA          ","           AAAAN NAAAA           ","             AAAhAAA             ","                d                ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","               AhA               ","             AAAhAAA             ","            AAAAhAAAA            ","           AAANN NNAAA           ","           AANNN NNNAA           ","          AAANNN NNNAAA          ","         dhhh       hhhd         ","          AAANNN NNNAAA          ","           AANNN NNNAA           ","           AAANN NNAAA           ","            AAAAhAAAA            ","             AAAhAAA             ","               AhA               ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","                d                ","              ddddd              ","             dhhhhhd             ","            dhhhhhhhd            ","            dhhhhhhhd            ","          dddhhh hhhddd          ","            dhhhhhhhd            ","            dhhhhhhhd            ","             dhhhhhd             ","              ddddd              ","                d                ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              ddddd              ","             dd d dd             ","             d  d  d             ","             ddd ddd             ","             d  d  d             ","             dd d dd             ","              ddddd              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","               LLL               ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","               LLL               ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              EEEEE              ","             E     E             ","            E       E            ","            E  LLL  E            ","            E  LLL  E            ","            E  LLL  E            ","            E       E            ","             E     E             ","              EEEEE              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             ddddddd             ","            ddsssssdd            ","          ddssGGGGGssdd          ","          dsGGGGGGGGGsd          ","         dsGGGEEEEEGGGsd         ","        ddsGGFBBBBBFGGsdd        ","        dsGGEBB   BBEGGsd        ","        dsGGEB LLL BEGGsd        ","        dsGGEB LLL BEGGsd        ","        dsGGEB LLL BEGGsd        ","        dsGGEBB   BBEGGsd        ","        ddsGGFBBBBBFGGsdd        ","         dsGGGEEEEEGGGsd         ","          dsGGGGGGGGGsd          ","          ddssGGGGGssdd          ","            ddsssssdd            ","             ddddddd             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             d     d             ","             dsq~qsd             ","            sdIIIIIds            ","          ssIIIIIIIIIss          ","         sIIIIIIIIIIIIIs         ","         sIIIIIIIIIIIIIs         ","        sIIIIIEEEEEIIIIIs        ","      dddIIIIFMMMMMFIIIIddd      ","       sIIIIEMMBBBMMEIIIIs       ","       sIIIIEMBBBBBMEIIIIs       ","       sIIIIEMBBBBBMEIIIIs       ","       sIIIIEMBBBBBMEIIIIs       ","       sIIIIEMMBBBMMEIIIIs       ","      dddIIIIFMMMMMFIIIIddd      ","        sIIIIIEEEEEIIIIIs        ","         sIIIIIIIIIIIIIs         ","         sIIIIIIIIIIIIIs         ","          ssIIIIIIIIIss          ","            sdIIIIIds            ","             dsssssd             ","             d     d             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             ddddddd             ","           dddqqqqqddd           ","         ddqqhqqqqqhqqdd         ","        dqqqqhGhhhGhqqqqd        ","       dqqqqGhIIIIIhGqqqqd       ","      dqqqGGIIKKKKKIIGGqqqd      ","      dqqGIIKKKKKKKKKIIGqqd      ","     dqqqGIKKKKKKKKKKKIGqqqd     ","     dqqGIKKKKEEEEEKKKKIGqqd     ","    ddhhhIKKKFMMMMMFKKKIhhhdd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    ddhhhIKKKFMMMMMFKKKIhhhdd    ","     dqqGIKKKKEEEEEKKKKIGqqd     ","     dqqqGIKKKKKKKKKKKIGqqqd     ","      dqqGIIKKKKKKKKKIIGqqd      ","      dqqqGGIIKKKKKIIGGqqqd      ","       dqqqqGhIIIIIhGqqqqd       ","        dqqqqhGGGGGhqqqqd        ","         ddqqhqqqqqhqqdd         ","           dddqqqqqddd           ","             ddddddd             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","            dhhhhhhhd            ","           hdhhhdhhhdh           ","         hhhhdddddddhhhh         ","        hhhddCCCCCCCddhhh        ","       hhddCCCCCCCCCCCddhh       ","      hhdCCCCCdddddCCCCCdhh      ","     hhdCCCChhCCCCChhCCCCdhh     ","     hhdCChhCCCCCCCCChhCCdhh     ","    hhdCCChCCCCCCCCCCChCCCdhh    ","   ddhdCChCCCCEEEEECCCChCCdhdd   ","   hhdCCChCCCFMMMMMFCCChCCCdhh   ","   hhdCCdCCCEMMMMMMMECCCdCCdhh   ","   hhdCCdCCCEMMMMMMMECCCdCCdhh   ","   hddCCdCCCEMMMMMMMECCCdCCddh   ","   hhdCCdCCCEMMMMMMMECCCdCCdhh   ","   hhdCCdCCCEMMMMMMMECCCdCCdhh   ","   hhdCCChCCCFMMMMMFCCChCCCdhh   ","   ddhdCChCCCCEEEEECCCChCCdhdd   ","    hhdCCChCCCCCCCCCCChCCCdhh    ","     hhdCChhCCCCCCCCChhCCdhh     ","     hhdCCCChhCCCCChhCCCCdhh     ","      hhdCCCCCdddddCCCCCdhh      ","       hhddCCCCCCCCCCCddhh       ","        hhhddCCCCCCCddhhh        ","         hhhhdddddddhhhh         ","           hdhhhdhhhdh           ","            dhhhhhhhd            ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","            d       d            ","            shhJJJhhs            ","           hhhhhdhhhhh           ","         JJhhCCCCCCChhJJ         ","        hhhCCCCCCCCCCChhh        ","       JhCCCCCCCCCCCCCCChJ       ","      hhCCCCCChhhhhCCCCCChh      ","     JhCCCCChhGGGGGhhCCCCChJ     ","     JhCCChhGGGGGGGGGhhCCChJ     ","    hhCCCChGGGGGGGGGGGhCCCChh    ","  dshhCCChGGGGEEEEEGGGGhCCChhsd  ","   hhCCCChGGGFMMMMMFGGGhCCCChh   ","   hhCCChGGGEMMMMMMMEGGGhCCChh   ","   JhCCChGGGEMMMMMMMEGGGhCCChJ   ","   JdCCChGGGEMMMMMMMEGGGhCCCdJ   ","   JhCCChGGGEMMMMMMMEGGGhCCChJ   ","   hhCCChGGGEMMMMMMMEGGGhCCChh   ","   hhCCCChGGGFMMMMMFGGGhCCCChh   ","  dshhCCChGGGGEEEEEGGGGhCCChhsd  ","    hhCCCChGGGGGGGGGGGhCCCChh    ","     JhCCChhGGGGGGGGGhhCCChJ     ","     JhCCCCChhGGGGGhhCCCCChJ     ","      hhCCCCCChhhhhCCCCCChh      ","       JhCCCCCCCCCCCCCCChJ       ","        hhhCCCCCCCCCCChhh        ","         JJhhCCCCCCChhJJ         ","           hhhhhdhhhhh           ","            shhJJJhhs            ","            d       d            ","                                 ","                                 "},
        {"                                 ","            d       d            ","            ss     ss            ","            shrrrrrhs            ","           hhhhhdhhhhh           ","         hhhhhhhhhhhhhhh         ","        hhhrrhhhhhhhrrhhh        ","       hhrrrJrhhhhhrJrrrhh       ","      hhrrJrrrhhhhhrrrJrrhh      ","     hhrrrrrhh     hhrrrrrhh     ","     hhrJrhh         hhrJrhh     ","    hhrrrrh           hrrrrhh    "," dsshhrJrh    EEEEE    hrJrhhssd ","  shhhhrrh   FMMMMMF   hrrhhhhs  ","   rhhhhh   EMMMMMMME   hhhhhr   ","   rhhhhh   EMMMMMMME   hhhhhr   ","   rdhhhh   EMMMMMMME   hhhhdr   ","   rhhhhh   EMMMMMMME   hhhhhr   ","   rhhhhh   EMMMMMMME   hhhhhr   ","  shhhhrrh   FMMMMMF   hrrhhhhs  "," dsshhrJrh    EEEEE    hrJrhhssd ","    hhrrrrh           hrrrrhh    ","     hhrJrhh         hhrJrhh     ","     hhrrrrrhh     hhrrrrrhh     ","      hhrrJrrrhhhhhrrrJrrhh      ","       hhrrrJrhhhhhrJrrrhh       ","        hhhrrhhhhhhhrrhhh        ","         hhhhhhhhhhhhhhh         ","           hhhhhdhhhhh           ","            shrrrrrhs            ","            ss     ss            ","            d       d            ","                                 "},
        {"            ddddddddd            ","            dsssssssd            ","           ddd     ddd           ","           ddddddddddd           ","           ddddddddddd           ","         ddddddddddddddd         ","        ddd  ddddddd  ddd        ","       dd     ddddd     dd       ","      dd      ddddd      dd      ","     dd     dd     dd     dd     ","     dd   dd         dd   dd     ","  dddd    d           d    dddd  ","dddddd   d    EJJJE    d   dddddd","dsddddd  d   FMMMMMF   d  dddddsd","ds dddddd   EMMMMMMME   dddddd sd","ds dddddd   JMMMMMMMJ   dddddd sd","ds dddddd   JMMMMMMMJ   dddddd sd","ds dddddd   JMMMMMMMJ   dddddd sd","ds dddddd   EMMMMMMME   dddddd sd","dsddddd  d   FMMMMMF   d  dddddsd","dddddd   d    EJJJE    d   dddddd","  dddd    d           d    dddd  ","     dd   dd         dd   dd     ","     dd     dd     dd     dd     ","      dd      ddddd      dd      ","       dd     ddddd     dd       ","        ddd  ddddddd  ddd        ","         ddddddddddddddd         ","           ddddddddddd           ","           ddddddddddd           ","           ddd     ddd           ","            dsssssssd            ","            ddddddddd            "},
        {"            BBBBBBBBB            ","          BBB       BBB          ","        BBBBBB     BBBBBB        ","      BB  BBBBBBBBBBBBB  BB      ","     B   BBBBBBBBBBBBBBB   B     ","    B   BBBBBBBBBBBBBBBBB   B    ","   B   BBBB  BBBBBBB  BBBB   B   ","   B  BBB     BBBBB     BBB  B   ","  B  BBB      BBBBB      BBB  B  ","  B BBB     BB     BB     BBB B  "," BBBBBB   BB         BB   BBBBBB "," BBBBB    B           B    BBBBB ","BBBBBB   B    EEEEE    B   BBBBBB","B BBBBB  B   EFFFFFE   B  BBBBB B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B BBBBB  B   EFFFFFE   B  BBBBB B","BBBBBB   B    EEEEE    B   BBBBBB"," BBBBB    B           B    BBBBB "," BBBBBB   BB         BB   BBBBBB ","  B BBB     BB     BB     BBB B  ","  B  BBB      BBBBB      BBB  B  ","   B  BBB     BBBBB     BBB  B   ","   B   BBBB  BBBBBBB  BBBB   B   ","    B   BBBBBBBBBBBBBBBBB   B    ","     B   BBBBBBBBBBBBBBB   B     ","      BB  BBBBBBBBBBBBB  BB      ","        BBBBBB     BBBBBB        ","          BBB       BBB          ","            BBBBBBBBB            "}
    };
    private final String[][] water = new String[][]{
        {"P"}
    };

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        // #tr Tooltip_MegaTreeFarm_MachineType
        // # Tree Farm
        // #zh_CN 树厂
        tt.addMachineType(TextEnums.tr("Tooltip_MegaTreeFarm_MachineType"))
            // #tr Tooltip_MegaTreeFarm_Controller
            // # Controller block for the Eco-Sphere Growth Simulator
            // #zh_CN 拟似生态圈的控制方块
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm_Controller"))
            // #tr Tooltip_MegaTreeFarm.0.01
            // # {\SPACE}
            // #zh_CN {\SPACE}
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm.0.01"))
            // #tr Tooltip_MegaTreeFarm.0.02
            // # Hark to the whispers of all creation......
            // #zh_CN 聆听万物之声......
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm.0.02"))
            // #tr Tooltip_MegaTreeFarm.0.03
            // # Yet, save the bees, for they do buzz too loudly.
            // #zh_CN 等一下, 蜜蜂除外. 它实在是太吵了.
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm.0.03"))
            // #tr Tooltip_MegaTreeFarm.0.04
            // #
            // #zh_CN
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm.0.04"))
            // #tr Tooltip_MegaTreeFarm.0.05
            // #
            // #zh_CN 得益于神秘使们在推动魔电一体化进程的贡献
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm.0.05"))
            // #tr Tooltip_MegaTreeFarm.0.06
            // #
            // #zh_CN
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm.0.06"))
            // #tr Tooltip_MegaTreeFarm.0.07
            // #
            // #zh_CN
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm.0.07"))
            // #tr Tooltip_MegaTreeFarm.0.08
            // #
            // #zh_CN
            .addInfo(TextEnums.tr("Tooltip_MegaTreeFarm.0.08"))
            .addController(textUseBlueprint)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 1)
            .addEnergyHatch(textUseBlueprint, 2)
            .toolTipFinisher(ModName);
        return tt;
    }

    // spotless:on
    // region Processing Logic
    double tierMultiplier = 1;
    int tier = 1;

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    private static int getTierMultiplier(int tier) {
        return (int) Math
            .floor(2 * Math.pow(2, 0.1 * (tier - 1) * (8 + Math.log(25 + Math.exp(25 - tier)) / Math.log(5))));
    }

    /**
     * Use the highest bonus from the original Recipe.
     */

    public static int getModeMultiplier(Mode mode) {
        return switch (mode) {
            case LOG -> 20;
            case SAPLING -> 3;
            case LEAVES -> 8;
            case FRUIT -> 1;
        };

    }

    public Map<Integer, Mode> damageModeMap = new HashMap<>();
    {
        damageModeMap.put(1, Mode.LOG);
        damageModeMap.put(2, Mode.SAPLING);
        damageModeMap.put(3, Mode.LEAVES);
        damageModeMap.put(4, Mode.FRUIT);
    }

    public int getModeOutput(Mode mode) {
        for (ItemStack stack : getStoredInputs()) {
            if (stack.getItem() instanceof GT_IntegratedCircuit_Item && stack.getItemDamage() > 0) {
                Mode mappedMode = damageModeMap.get(stack.getItemDamage());
                if (mode == mappedMode) {
                    return 1;
                }
            }
        }
        return -1;
    }

    public static EnumMap<Mode, ItemStack> queryTreeProduct(ItemStack sapling) {
        String key = Item.itemRegistry.getNameForObject(sapling.getItem()) + ":" + sapling.getItemDamage();
        EnumMap<Mode, ItemStack> ProductMap = treeProductsMap.get(key);
        if (ProductMap != null) {
            return ProductMap;
        }
        return getOutputsForForestrySapling(sapling);
    }

    public static EnumMap<Mode, ItemStack> getOutputsForForestrySapling(ItemStack sapling) {
        // copy form GTPP_TGS
        ITree tree = TreeManager.treeRoot.getMember(sapling);
        if (tree == null) return null;

        String speciesUUID = tree.getIdent();

        EnumMap<Mode, ItemStack> defaultMap = treeProductsMap.get("Forestry:sapling:" + speciesUUID);
        if (defaultMap == null) return null;

        // We need to make a new map so that we don't modify the stored amounts of outputs.
        EnumMap<Mode, ItemStack> adjustedMap = new EnumMap<>(Mode.class);

        ItemStack log = defaultMap.get(Mode.LOG);
        if (log != null) {
            double height = Math.max(
                3 * (tree.getGenome()
                    .getHeight() - 1),
                0) + 1;
            double girth = tree.getGenome()
                .getGirth();

            log = log.copy();
            log.stackSize = (int) (log.stackSize * height * girth);
            adjustedMap.put(Mode.LOG, log);
        }

        ItemStack saplingOut = defaultMap.get(Mode.SAPLING);
        if (saplingOut != null) {
            // Lowest = 0.01 ... Average = 0.05 ... Highest = 0.3
            double fertility = tree.getGenome()
                .getFertility() * 10;

            // Return a copy of the *input* sapling, retaining its genetics.
            int stackSize = Math.max(1, (int) (saplingOut.stackSize * fertility));
            saplingOut = sapling.copy();
            saplingOut.stackSize = stackSize;
            adjustedMap.put(Mode.SAPLING, saplingOut);
        }

        ItemStack leaves = defaultMap.get(Mode.LEAVES);
        if (leaves != null) {
            adjustedMap.put(Mode.LEAVES, leaves.copy());
        }

        ItemStack fruit = defaultMap.get(Mode.FRUIT);
        if (fruit != null) {
            // Lowest = 0.025 ... Average = 0.2 ... Highest = 0.4
            double yield = tree.getGenome()
                .getYield() * 10;

            fruit = fruit.copy();
            fruit.stackSize = (int) (fruit.stackSize * yield);
            adjustedMap.put(Mode.FRUIT, fruit);
        }

        return adjustedMap;
    }

    @Override
    public GTCM_ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @Override
            @Nonnull
            public CheckRecipeResult process() {
                if (inputItems == null) {
                    inputItems = new ItemStack[0];
                }
                if (inputFluids == null) {
                    inputFluids = new FluidStack[0];
                }

                ItemStack sapling = getControllerSlot();
                if (sapling == null) return SimpleCheckRecipeResult.ofFailure("no_sapling");
                EnumMap<Mode, ItemStack> outputPerMode = queryTreeProduct(sapling);
                if (outputPerMode == null) return SimpleCheckRecipeResult.ofFailure("no_sapling");

                tier = (int) Math.max(0, Math.log((double) (availableVoltage * availableAmperage) / 8) / Math.log(4));
                if(tier<1) return SimpleCheckRecipeResult.ofFailure("no_energy");
                int tier_temp = tier;
                tierMultiplier = getTierMultiplier(tier);

                // different liquid = different running logic
                Fluid RecipeLiquid = null;
                int RecipeLiquidCost = 100000;
                ArrayList<FluidStack> InputFluids = getStoredFluids();
                for (FluidStack aFluid : InputFluids) {
                    if (aFluid.getFluid()
                        .equals(FluidRegistry.WATER)) {
                        // Normal Trees
                        RecipeLiquid = FluidRegistry.WATER;
                        break;
                    }
                    if (aFluid.getFluid()
                        .equals(BRFluids.UnknowWater) && Mods.GalaxySpace.isModLoaded()) {
                        // Normal to BarnardaC`
                        RecipeLiquid = BRFluids.UnknowWater;
                        RecipeLiquidCost = 10000;
                        sapling = GT_ModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 1, 1);
                        outputPerMode = queryTreeProduct(sapling);
                        break;
                    }
                    if (aFluid.getFluid()
                        .equals(FluidRegistry.getFluid("temporalfluid")) && Mods.TwilightForest.isModLoaded()) {
                        // Normal to Time Tree
                        RecipeLiquid = FluidRegistry.getFluid("temporalfluid");
                        RecipeLiquidCost = 1000;
                        sapling = GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 5);
                        outputPerMode = queryTreeProduct(sapling);
                        break;
                    }
                    if (aFluid.getFluid()
                        .equals(FluidRegistry.getFluid("ic2uumatter"))) {
                        // Random
                        Random random = new Random();
                        RecipeLiquid = FluidRegistry.getFluid("ic2uumatter");
                        RecipeLiquidCost = 10000;
                        EnumMap<Mode, ItemStack> outputPerModeTemp = new EnumMap<>(Mode.class);
                        for (int i = 0; i < Mode.values().length; i++) {
                            int num = random.nextInt(allProducts[i].length);
                            outputPerModeTemp.put(damageModeMap.get(i + 1), allProducts[i][num]);
                        }
                        outputPerMode = outputPerModeTemp;
                        break;
                    }
                }
                if (RecipeLiquid == null) return SimpleCheckRecipeResult.ofFailure("no_fluid");

                if (controllerTier > 0) RecipeLiquidCost /= 100;
                // multi input hatch available
                List<ItemStack> outputs = new ArrayList<>();
                long inputWaterAmount = 0;
                ArrayList<FluidStack> WaterHatchStack = new ArrayList<>();
                for (FluidStack aFluid : InputFluids) {
                    if (aFluid.getFluid()
                        .equals(RecipeLiquid)) {
                        inputWaterAmount += aFluid.amount;
                        WaterHatchStack.add(aFluid);
                    }
                }
                if (inputWaterAmount < Math.pow(2, tier) * RecipeLiquidCost) {
                    tier_temp = (int) Math.floor(Math.log((double) inputWaterAmount / RecipeLiquidCost) / Math.log(2));
                    if (tier_temp < 1) return SimpleCheckRecipeResult.ofFailure("no_enough_input");
                    tierMultiplier = getTierMultiplier(tier_temp);
                }
                long costWaterAmount = (long) (Math.pow(2, tier_temp) * RecipeLiquidCost);
                if (inputWaterAmount < costWaterAmount) return SimpleCheckRecipeResult.ofFailure("no_enough_input");

                for (FluidStack aFluid : WaterHatchStack) {
                    if (costWaterAmount > aFluid.amount) {
                        costWaterAmount -= aFluid.amount;
                        aFluid.amount = 0;
                    } else {
                        aFluid.amount -= (int) costWaterAmount;
                        break;
                    }
                }

                // output < recipe, output++
                int outputNum = 0;
                int availableNum = 0;
                for (Mode mode : Mode.values()) {
                    if (getModeOutput(mode) > 0) outputNum++;
                    if (outputPerMode.get(mode) != null) availableNum++;
                }
                float extraOutput = 1;
                if (outputNum < availableNum) extraOutput = 1 + (float) (availableNum - outputNum) / outputNum / 3;

                // get running output
                for (Mode mode : Mode.values()) {
                    int checkMode = getModeOutput(mode);
                    ItemStack output = outputPerMode.get(mode);
                    if (output == null) continue; // This sapling has no output in this mode.
                    int ModeMultiplier = getModeMultiplier(mode);
                    if (checkMode < 0) continue; // No valid Circuit for this mode found.

                    ItemStack out = output.copy();
                    long outputStackSize = (long) (out.stackSize * ModeMultiplier
                        * tierMultiplier
                        * checkMode
                        * extraOutput);
                    while (outputStackSize > Integer.MAX_VALUE) {
                        ItemStack outUnion = out.copy();
                        outUnion.stackSize = Integer.MAX_VALUE;
                        outputs.add(outUnion);
                        outputStackSize -= Integer.MAX_VALUE;
                    }
                    out.stackSize = (int) outputStackSize;
                    outputs.add(out);
                }

                if (outputs.isEmpty()) {
                    // No outputs can be produced With the Circuit.
                    return SimpleCheckRecipeResult.ofFailure("no_correct_Circuit");
                }

                for (ItemStack stack : outputs) {
                    if (stack.stackSize < 1) return SimpleCheckRecipeResult.ofFailure("no_enough_input");
                }
                outputItems = outputs.toArray(new ItemStack[0]);

                duration = 20;
                calculatedEut = (long) (8 * Math.pow(4, tier_temp) * 15 / 16);
                return SimpleCheckRecipeResult.ofSuccess("growing_trees");
            }

        };
    }

    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 2];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "tierMultiplier"
            + " : "
            + EnumChatFormatting.GOLD
            + (int) this.tierMultiplier;
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "Eu tier" + " : " + EnumChatFormatting.GOLD + this.tier;
        return ret;
    }

}
