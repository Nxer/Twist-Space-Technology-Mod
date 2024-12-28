package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.SpaceStationAntiGravityBlock;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.spaceStationStructureBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static net.minecraft.util.EnumChatFormatting.BLUE;
import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.ITALIC;
import static net.minecraft.util.EnumChatFormatting.LIGHT_PURPLE;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.StellarMaterialSiphonRecipePool;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import com.gtnewhorizons.gtnhintergalactic.client.IGTextures;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IChunkLoader;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import tectech.thing.casing.TTCasingsContainer;

public class GT_TileEntity_StellarMaterialSiphon
    extends MTEExtendedPowerMultiBlockBase<GT_TileEntity_StellarMaterialSiphon> implements IChunkLoader {

    /**
     * Lore string, which will be randomly picked from a selection each time the resources are reloaded
     */
    @SuppressWarnings("unused")
    private static String loreTooltip;
    /**
     * Main structure of the machine
     */
    private static final String STRUCTURE_PIECE_MAIN = "main";
    /**
     * Cached value of log10(4)
     */
    private static final double LOG4 = Math.log10(4);
    // region shape
    private static final String[][] shape = new String[][] {
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "      LLL      ", "      L~L      ", "KKKKKKKKKKKKKKK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "      FFF      ", "KDDDDDDCDDDDDDK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "      FFF      ", "     FEEEF     ", "KDGGGGDCDGGGGDK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "      AAA      ", "      AAA      ", "      AAA      ",
            "     FEEEF     ", "    FE   EF    ", "KDGGGGDCDGGGGDK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "      HCH      ", "      HCH      ",
            "      HCH      ", "      HCH      ", "     ABBBA     ", "     ABBBA     ", "     ABBBA     ",
            "    FEBBBEF    ", "   FE     EF   ", "KDGGGGDCDGGGGDK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "       C       ", "       C       ",
            "       C       ", "       C       ", "      HCH      ", "     IEEEI     ", "     IEEEI     ",
            "     IEEEI     ", "     IEEEI     ", "    AB   BA    ", "    AB   BA    ", "    AB   BA    ",
            "   FEB   BEF   ", "  FE       EF  ", "KDGGGGDCDGGGGDK", "       M       ", "       M       ",
            "       M       ", "       M       ", "       M       ", "       M       ", "       M       ",
            "       M       ", "       M       ", "       M       ", "       M       " },
        { "               ", "               ", "               ", "               ", "               ",
            "       H       ", "       H       ", "       H       ", "      HJH      ", "      HJH      ",
            "      HJH      ", "      HJH      ", "     HHJHH     ", "    HEEJEEH    ", "    HE J EH    ",
            "    HE J EH    ", "    HE J EH    ", "   AB  J  BA   ", "   AB  J  BA   ", "   AB  J  BA   ",
            "  FEB  J  BEF  ", " FE    J    EF ", "KDDDDDDCDDDDDDK", "      MJM      ", "      MJM      ",
            "      MJM      ", "      MJM      ", "      MJM      ", "      MJM      ", "      MJM      ",
            "      MJM      ", "      MJM      ", "      MJM      ", "      MJM      " },
        { "       I       ", "       I       ", "       I       ", "       I       ", "       I       ",
            "      HIH      ", "      HIH      ", "      HIH      ", "     CJJJC     ", "     CJ JC     ",
            "     CJ JC     ", "     CJ JC     ", "     CJ JC     ", "    CEJ JEC    ", "    CEJ JEC    ",
            "    CEJ JEC    ", "    CEJ JEC    ", "   AB J J BA   ", "   AB J J BA   ", "   AB J J BA   ",
            "  FEB J J BEF  ", " FE   J J   EF ", "KCCCCCC CCCCCCK", "     MJ JM     ", "     MJ JM     ",
            "     MJ JM     ", "     MJ JM     ", "     MJ JM     ", "     MJ JM     ", "     MJ JM     ",
            "     MJ JM     ", "     MJ JM     ", "     MJ JM     ", "     MJ JM     " },
        { "               ", "               ", "               ", "               ", "               ",
            "       H       ", "       H       ", "       H       ", "      HJH      ", "      HJH      ",
            "      HJH      ", "      HJH      ", "     HHJMH     ", "    HEEJEEH    ", "    HE J EH    ",
            "    HE J EH    ", "    HE J EH    ", "   AB  J  BA   ", "   AB  J  BA   ", "   AB  J  BA   ",
            "  FEB  J  BEF  ", " FE    J    EF ", "KDDDDDDCDDDDDDK", "      MJM      ", "      MJM      ",
            "      MJM      ", "      MJM      ", "      MJM      ", "      MJM      ", "      MJM      ",
            "      MJM      ", "      MJM      ", "      MJM      ", "      MJM      " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "       C       ", "       C       ",
            "       C       ", "       C       ", "      HCH      ", "     IEEEI     ", "     IEEEI     ",
            "     IEEEI     ", "     IEEEI     ", "    AB   BA    ", "    AB   BA    ", "    AB   BA    ",
            "   FEB   BEF   ", "  FE       EF  ", "KDGGGGDCDGGGGDK", "       M       ", "       M       ",
            "       M       ", "       M       ", "       M       ", "       M       ", "       M       ",
            "       M       ", "       M       ", "       M       ", "       M       " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "      HCH      ", "      HCH      ",
            "      HCH      ", "      HCH      ", "     ABBBA     ", "     ABBBA     ", "     ABBBA     ",
            "    FEBBBEF    ", "   FE     EF   ", "KDGGGGDCDGGGGDK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "      AAA      ", "      AAA      ", "      AAA      ",
            "     FEEEF     ", "    FE   EF    ", "KDGGGGDCDGGGGDK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "      FFF      ", "     FEEEF     ", "KDGGGGDCDGGGGDK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "      FFF      ", "KDDDDDDCDDDDDDK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "KKKKKKKKKKKKKKK", "               ", "               ",
            "               ", "               ", "               ", "               ", "               ",
            "               ", "               ", "               ", "               " } };
    // endregion
    private static final String shapeName = "main";

    /**
     * Structure definition of the machine
     */
    private static IStructureDefinition<GT_TileEntity_StellarMaterialSiphon> STRUCTURE_DEFINITION;

    /**
     * Create a new planetary gas siphon
     *
     * @param id           ID of the multiblock
     * @param name         Unlocalized name of the multiblock
     * @param regionalName Localized (english) name of the multiblock
     */
    public GT_TileEntity_StellarMaterialSiphon(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    /**
     * Create a new planetary gas siphon
     *
     * @param name Unlocalized name of the multiblock
     */
    public GT_TileEntity_StellarMaterialSiphon(String name) {
        super(name);
    }

    /**
     * Current pumping depth
     */
    private int depth;
    /**
     * Cached fluid stack using for displaying the pumped fluid
     */
    private FluidStack fluid = new FluidStack(FluidRegistry.WATER, 0) {

        public String getLocalizedName() {
            return "None";
        }
    };
    /**
     * Flag if chunk loading is enabled
     */
    private boolean mChunkLoadingEnabled = true;
    /**
     * Chunk in which the multi is build
     */
    private ChunkCoordIntPair mCurrentChunk = null;
    /**
     * Flag if the chunk of the multi needs to be reloaded
     */
    private boolean mWorkChunkNeedsReload = true;

    /**
     * Construct this machine using the blueprint in creative
     *
     * @param stackSize Blueprint stack
     * @param hintsOnly Whether only hints should be displayed, or it should be build
     */
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 21, 0);
    }

    /**
     * Get a new meta entity for this controller
     *
     * @param tileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return New meta entity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new GT_TileEntity_StellarMaterialSiphon(this.mName);
    }

    /**
     * @return Tooltip builder for this machine
     */
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.type"))
            .addInfo(loreTooltip != null ? ITALIC + loreTooltip : "")
            .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc1"))
            .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc2"))
            .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc3"))
            .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc4"))
            .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc5"))
            .addSeparator()
            .beginStructureBlock(3, 7, 3, false)
            .addController(GCCoreUtil.translate("ig.siphon.structure.ControllerPos"))
            .addOtherStructurePart(
                GCCoreUtil.translate("ig.siphon.structure.AdvMachineFrame"),
                GCCoreUtil.translate("ig.siphon.structure.Base"))
            .addOtherStructurePart(
                GCCoreUtil.translate("ig.siphon.structure.ReboltedRhodiumPalladiumCasing"),
                GCCoreUtil.translate("ig.siphon.structure.PillarMiddle"))
            .addOtherStructurePart(
                GCCoreUtil.translate("ig.siphon.structure.FrameTungstensteel"),
                GCCoreUtil.translate("ig.siphon.structure.Sides"))
            .addEnergyHatch(GCCoreUtil.translate("ig.siphon.structure.AnyAdvMachineFrame"), 1)
            .addMaintenanceHatch(GCCoreUtil.translate("ig.siphon.structure.AnyAdvMachineFrame"), 1)
            .addInputBus(GCCoreUtil.translate("ig.siphon.structure.AnyAdvMachineFrame"), 1)
            .addOutputHatch(GCCoreUtil.translate("ig.siphon.structure.AnyAdvMachineFrame"), 1)
            .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
        return tt;
    }

    /**
     * @return Structure definition for this machine
     */
    @Override
    public IStructureDefinition<GT_TileEntity_StellarMaterialSiphon> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_StellarMaterialSiphon>builder()
                .addShape(shapeName, shape)
                .addElement('A', ofBlock(spaceStationStructureBlock, 12)) // A ->
                // ofBlock...(BW_GlasBlocks2, 0,
                // ...);
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings5, 13))// B -> ofBlock...(gt.blockcasings5, 13, ...);
                .addElement('C', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 12))// C ->
                // ofBlock...(gt.blockcasingsBA0,
                // 12,//
                // ...);
                .addElement('D', ofBlock(IGBlocks.SpaceElevatorCasing, 0))// D -> ofBlock...(gt.blockcasingsSE, 0, ...);
                .addElement('E', ofBlock(IGBlocks.SpaceElevatorCasing, 1))// E -> ofBlock...(gt.blockcasingsSE, 1, ...);
                .addElement('F', ofBlock(IGBlocks.SpaceElevatorCasing, 2))// F -> ofBlock...(gt.blockcasingsSE, 2, ...);
                .addElement('G', ofBlock(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 2))// G ->
                // ofBlock...(gt.spacetime_compression_field_generator,
                // 2, ...);
                .addElement('H', ofBlock(ModBlocks.blockCasings5Misc, 7))// H -> ofBlock...(gtplusplus.blockcasings.5,
                // 7, ...);
                .addElement('I', ofBlock(ModBlocks.blockCasings5Misc, 11))// I -> ofBlock...(gtplusplus.blockcasings.5,
                // 11,//
                // ...);
                .addElement('J', ofBlock(ModBlocks.blockCasings6Misc, 1))// J -> ofBlock...(gtplusplus.blockcasings.6,
                // 1, ...);
                .addElement('K', ofBlock(SpaceStationAntiGravityBlock, 13))// K -> ofBlock...(miscutils.blockcasings, 9,
                // ...);
                .addElement(
                    'L',
                    StructureUtility.ofChain(
                        GTStructureUtility.ofHatchAdder(
                            GT_TileEntity_StellarMaterialSiphon::addMaintenanceToMachineList,
                            IGTextures.CASING_INDEX_SIPHON,
                            1),
                        GTStructureUtility.ofHatchAdder(
                            GT_TileEntity_StellarMaterialSiphon::addExoticEnergyInputToMachineList,
                            IGTextures.CASING_INDEX_SIPHON,
                            1),
                        GTStructureUtility.ofHatchAdder(
                            GT_TileEntity_StellarMaterialSiphon::addInputToMachineList,
                            IGTextures.CASING_INDEX_SIPHON,
                            1),
                        GTStructureUtility.ofHatchAdder(
                            GT_TileEntity_StellarMaterialSiphon::addOutputToMachineList,
                            IGTextures.CASING_INDEX_SIPHON,
                            1),
                        StructureUtility.ofBlock(IGBlocks.SpaceElevatorCasing, 0)))// L -> ofBlock...(tile.stone, 0,
                                                                                   // ...);
                .addElement('M', ofBlock(SpaceStationAntiGravityBlock, 13))
                .build();// IGBlocks.SpaceElevatorCasing
        }
        return STRUCTURE_DEFINITION;
    }

    /**
     * @return Chunk of this machine
     */
    @Override
    public ChunkCoordIntPair getActiveChunk() {
        return mCurrentChunk;
    }

    /**
     * Get the texture for this controller
     *
     * @param baseMetaTileEntity MTE of this controller
     * @param side               is the Side of the Block
     * @param facing             is the direction the Block is facing (or a Bitmask of all Connections in case of Pipes)
     * @param colorIndex         The Minecraft Color the Block is having
     * @param active             if the Machine is currently active (use this instead of calling
     *                           mBaseMetaTileEntity.mActive!!!). Note: In case of Pipes this means if this Side is
     *                           connected to something or not.
     * @param redstone           if the Machine is currently outputting a RedstoneSignal (use this instead of calling
     *                           mBaseMetaTileEntity.mRedstone!!!)
     * @return Texture of this controller for the input conditions
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstone) {
        if (side == facing) {
            if (active) return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(IGTextures.CASING_INDEX_SIPHON),
                TextureFactory.of(IGTextures.SIPHON_OVERLAY_FRONT), TextureFactory.builder()
                    .addIcon(IGTextures.SIPHON_OVERLAY_FRONT_ACTIVE_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(IGTextures.CASING_INDEX_SIPHON),
                TextureFactory.of(IGTextures.SIPHON_OVERLAY_FRONT), TextureFactory.builder()
                    .addIcon(IGTextures.SIPHON_OVERLAY_FRONT_GLOW)
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(IGTextures.CASING_INDEX_SIPHON) };
    }

    /**
     * Check if the input item is the correct machine part
     *
     * @param stack Input item
     * @return True if correct machine part, else false
     */
    @Override
    public boolean isCorrectMachinePart(ItemStack stack) {
        return true;
    }

    /**
     * Check if this machine can perform a recipe
     *
     * @return True if recipe was started, else false
     */
    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        // return early if no input busses are present, the first bus is invalid or the TE is not on a space station
        if (mInputBusses.isEmpty() || !mInputBusses.get(0)
            .isValid()) {
            resetMachine(true);
            return SimpleCheckRecipeResult.ofFailure("no_mining_pipe");
        }
        Map<Integer, FluidStack> planetRecipes = StellarMaterialSiphonRecipePool.RECIPES.get("blackHole");

        MTEHatchInputBus bus = mInputBusses.get(0);

        // count mining pipes, get depth
        for (int i = 0; i < bus.getBaseMetaTileEntity()
            .getSizeInventory(); i++) {
            ItemStack stack = bus.getBaseMetaTileEntity()
                .getStackInSlot(i);
            if (stack == null) {
                continue;
            }
            if (stack.getItem() == ItemList.Circuit_Integrated.getItem()) {
                depth = stack.getItemDamage();
                break;
            }
        }
        FluidStack recipeFluid = planetRecipes.get(1)
            .copy();
        recipeFluid.amount *= (1 << depth);

        if (!canOutputAll(new FluidStack[] { recipeFluid })) {
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        }
        long recipeEUt = (long) Integer.MAX_VALUE << depth;
        // calculate overclockedness
        fluid = recipeFluid.copy();
        lEUt = -recipeEUt;
        // success - check again in 20 ticks
        mOutputFluids = new FluidStack[] { fluid };
        mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;
        return SimpleCheckRecipeResult.ofSuccess("drilling");
    }

    public void repairMachine() {
        mHardHammer = true;
        mSoftHammer = true;
        mScrewdriver = true;
        mCrowbar = true;
        mSolderingTool = true;
        mWrench = true;
    }

    /**
     * No more machine error
     */
    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    /**
     * Check if the machine has a valid structure
     *
     * @param aBaseMetaTileEntity MTE of this controller
     * @param stack               Item in the controller
     * @return True if machine is valid, else false
     */
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack stack) {
        repairMachine();
        return checkPiece(STRUCTURE_PIECE_MAIN, 7, 21, 0);
    }

    /**
     * Get the maximum efficiency of this machine
     *
     * @param stack Item in the controller
     * @return Maximum efficiency
     */
    @Override
    public int getMaxEfficiency(ItemStack stack) {
        return 10000;
    }

    /**
     * Get the damage that will be dealt to the item in the controller
     *
     * @param stack Item in the controller
     * @return Damage that is applied to the item in the controller
     */
    @Override
    public int getDamageToComponent(ItemStack stack) {
        return 0;
    }

    /**
     * Whether this machine should explode, when the structure is broken while running
     *
     * @param stack Item in the controller
     * @return True if it should explode, else false
     */
    @Override
    public boolean explodesOnComponentBreak(ItemStack stack) {
        return false;
    }

    /**
     * Save additional nbt data to this controller
     *
     * @param aNBT Tag to which will be saved
     */
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("chunkLoadingEnabled", mChunkLoadingEnabled);
        aNBT.setBoolean("isChunkloading", mCurrentChunk != null);
        if (mCurrentChunk != null) {
            aNBT.setInteger("loadedChunkXPos", mCurrentChunk.chunkXPos);
            aNBT.setInteger("loadedChunkZPos", mCurrentChunk.chunkZPos);
        }
    }

    /**
     * Read additional nbt data from this controller
     *
     * @param aNBT Tag which will be read
     */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("chunkLoadingEnabled")) mChunkLoadingEnabled = aNBT.getBoolean("chunkLoadingEnabled");
        if (aNBT.getBoolean("isChunkloading")) {
            mCurrentChunk = new ChunkCoordIntPair(
                aNBT.getInteger("loadedChunkXPos"),
                aNBT.getInteger("loadedChunkZPos"));
        }
    }

    /**
     * Callback that wil be invoked when the controller is right-clicked with a soldering tool
     *
     * @param side          Clicked side of the controller
     * @param wrenchingSide Clicked grid side (the grid that gets displayed, when holding a tool while looking at it)
     * @param player        Player that clicked
     * @param x             X coordinate of the machine
     * @param y             Y coordinate of the machine
     * @param z             Z coordinate of the machine
     * @return True if event was processed, else false
     */
    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer player,
        float x, float y, float z) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            mChunkLoadingEnabled = !mChunkLoadingEnabled;
            GTUtility.sendChatToPlayer(
                player,
                mChunkLoadingEnabled ? GTUtility.trans("502", "Mining chunk loading enabled")
                    : GTUtility.trans("503", "Mining chunk loading disabled"));
            return true;
        }
        return super.onSolderingToolRightClick(side, wrenchingSide, player, x, y, z);
    }

    /**
     * Reset the stats of the siphon
     *
     * @param resetDepth should depth be reset too?
     */
    private void resetMachine(boolean resetDepth) {
        if (resetDepth) {
            depth = 0;
        }
        mEUt = 0;
        mEfficiency = 0;
    }

    /**
     * Callback that will be invoked when the machine is broken
     */
    @Override
    public void onRemoval() {
        if (mChunkLoadingEnabled) GTChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        super.onRemoval();
    }

    /**
     * Callback that will be invoked after executing a tick
     *
     * @param baseMetaTileEntity MTE of this controller
     * @param tick               Current tick
     */
    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        super.onPostTick(baseMetaTileEntity, tick);
        if (baseMetaTileEntity.isServerSide() && mCurrentChunk != null
            && !mWorkChunkNeedsReload
            && !baseMetaTileEntity.isAllowedToWork()) {
            // if machine has stopped, stop chunk loading
            GTChunkManager.releaseTicket((TileEntity) baseMetaTileEntity);
            mWorkChunkNeedsReload = true;
        }
    }

    /**
     * @return Info data stick of this controller
     */
    @Override
    public String[] getInfoData() {
        return new String[] { LIGHT_PURPLE + "Operational Data:" + RESET, "Depth: " + YELLOW + depth + RESET,
            "Fluid: " + YELLOW + fluid.amount + RESET + "L/s " + BLUE + fluid.getLocalizedName() + RESET,
            "EU/t required: " + YELLOW + GTUtility.formatNumbers(-mEUt) + RESET + " EU/t",
            "Maintenance Status: " + (getRepairStatus() == getIdealStatus() ? GREEN + "Working perfectly" + RESET
                : RED + "Has problems" + RESET),
            "---------------------------------------------" };
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

}
//
// Structure:
//
// Blocks:
// A -> ofBlock...(BW_GlasBlocks2, 0, ...);
// B -> ofBlock...(gt.blockcasings5, 13, ...);
// C -> ofBlock...(gt.blockcasingsBA0, 12, ...);
// D -> ofBlock...(gt.blockcasingsSE, 0, ...);
// E -> ofBlock...(gt.blockcasingsSE, 1, ...);
// F -> ofBlock...(gt.blockcasingsSE, 2, ...);
// G -> ofBlock...(gt.spacetime_compression_field_generator, 2, ...);
// H -> ofBlock...(gtplusplus.blockcasings.5, 7, ...);
// I -> ofBlock...(gtplusplus.blockcasings.5, 11, ...);
// J -> ofBlock...(gtplusplus.blockcasings.6, 1, ...);
// K -> ofBlock...(miscutils.blockcasings, 9, ...);
// L -> ofBlock...(tile.stone, 0, ...);
//
// Tiles:
//
// Special Tiles:
// M -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...); // You will probably want to change it
// to something else
// N -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaTileEntity, ...); // You will probably want to change it
// to something else
//
// Offsets:
// 7 21 0
//
// Normal Scan:
