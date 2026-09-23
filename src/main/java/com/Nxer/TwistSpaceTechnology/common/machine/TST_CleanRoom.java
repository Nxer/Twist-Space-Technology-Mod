package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.simple_structure_issue;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.AUTHOR;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.MAINTAINER;
import static gregtech.api.enums.GTValues.debugCleanroom;
import static gregtech.api.enums.Textures.BlockIcons.BLOCK_PLASCRETE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_GLOW;
import static gregtech.api.structure.error.StructureErrorRegistry.TOO_TALL;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static gregtech.api.util.GlassTier.getGlassBlockTier;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.gtnhlib.capability.Capabilities;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ICleanroom;
import gregtech.api.interfaces.ICleanroomReceiver;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicHull;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTLog;
import gregtech.api.util.MultiblockTooltipBuilder;

@SkipGenerateDescription
public class TST_CleanRoom extends GTCM_MultiMachineBase<TST_CleanRoom>
    implements IConstructable, ISecondaryDescribable, ICleanroom {

    // region Class Constructor
    public TST_CleanRoom(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(AUTHOR, ID.SHORDINGER, MAINTAINER, ID.LUO_YANG_YU_LI);
    }

    public TST_CleanRoom(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_CleanRoom(mName);
    }
    // endregion

    // region Structure
    // EV
    private static final int CASING_INDEX = 210;

    @Override
    public IStructureDefinition<TST_CleanRoom> getStructureDefinition() {
        return null;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[] { "The base can be rectangular." };
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        int i = Math.min(itemStack.stackSize, MAX_WIDTH / 2);
        IGregTechTileEntity baseEntity = this.getBaseMetaTileEntity();
        World world = baseEntity.getWorld();
        int x = baseEntity.getXCoord();
        int y = baseEntity.getYCoord();
        int z = baseEntity.getZCoord();
        int yoff = Math.max(i * 2, 3);
        for (int X = x - i; X <= x + i; X++) for (int Y = y; Y >= y - yoff; Y--) for (int Z = z - i; Z <= z + i; Z++) {
            if (X == x && Y == y && Z == z) continue;
            if (X == x - i || X == x + i || Z == z - i || Z == z + i || Y == y - yoff) {
                if (b) StructureLibAPI.hintParticle(world, X, Y, Z, GregTechAPI.sBlockReinforced, 2);
                else world.setBlock(X, Y, Z, GregTechAPI.sBlockReinforced, 2, 2);
            } else if (Y == y) {
                if (b) StructureLibAPI.hintParticle(world, X, Y, Z, GregTechAPI.sBlockCasings3, 11);
                else world.setBlock(X, Y, Z, GregTechAPI.sBlockCasings3, 11, 2);
            }
        }
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();

        this.mUpdate = 100;
        cleanroomReceivers.forEach(r -> r.setCleanroom(null));
        cleanroomReceivers.clear();

        casingCount = 0;
        otherCount = 0;
        isDoorOpen = false;

        if (debugCleanroom) GTLog.out.println("Cleanroom: Starting structure check.");

        // Step 1: Detect size & verify ceiling.
        // Optimization: try cached size first, only re-detect if ceiling check fails.
        if (dyMin == 0 || !checkCeiling(aBaseMetaTileEntity)) {
            casingCount = 0;
            otherCount = 0;
            if (!checkSize(aBaseMetaTileEntity)) {
                errors.add(simple_structure_issue);
                return;
            }
            if (!checkCeiling(aBaseMetaTileEntity)) {
                errors.add(simple_structure_issue);
                return;
            }
        }

        // Step 2: Scan downward. Each level is either a wall or the floor.
        for (dyMin = -1; dyMin >= -(MAX_HEIGHT - 1); --dyMin) {
            if (dyMin < -2 && checkFloor(aBaseMetaTileEntity, dyMin)) {
                // Found valid floor interior. Now check floor edges.
                for (int dx = dxMin; dx <= dxMax; ++dx) {
                    if (!addStructureBlock(aBaseMetaTileEntity, dx, dyMin, dzMin, MASK_FLOOR_EDGE)) {
                        errors.add(simple_structure_issue);
                        return;
                    }
                    if (!addStructureBlock(aBaseMetaTileEntity, dx, dyMin, dzMax, MASK_FLOOR_EDGE)) {
                        errors.add(simple_structure_issue);
                        return;
                    }
                }
                for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
                    if (!addStructureBlock(aBaseMetaTileEntity, dxMin, dyMin, dz, MASK_FLOOR_EDGE)) {
                        errors.add(simple_structure_issue);
                        return;
                    }
                    if (!addStructureBlock(aBaseMetaTileEntity, dxMax, dyMin, dz, MASK_FLOOR_EDGE)) {
                        errors.add(simple_structure_issue);
                        return;
                    }
                }
                break;
            } else {
                // Not floor, check as wall layer
                if (!checkWall(aBaseMetaTileEntity, dyMin)) {
                    errors.add(simple_structure_issue);
                    return;
                }
            }
        }
        if (dyMin < -(MAX_HEIGHT - 1)) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too tall.");
            errors.add(simple_structure_issue);
            errors.add(TOO_TALL);

            return;
        }
        mHeight = -dyMin + 1;

        // if (debugCleanroom) GTLog.out.println(
        // "Cleanroom: Structure complete. Found " + casingCount + " casings, " + otherCount + " other blocks.");

        // Step 3: Validate totals
        if (casingCount < 20) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Could not find 20 Plascrete.");
            checkCasingMin(errors, casingCount, 20);
            return;
        }
        if (casingCount + otherCount > 0
            && (otherCount * 100) / (casingCount + otherCount) > maxReplacementPercentage) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too many non-plascrete blocks.");
            checkCasingMin(errors, casingCount, (casingCount + otherCount) * (100 - maxReplacementPercentage) / 100);
            return;
        }

        // Step 4: Register cleanroom receivers inside the room
        for (int dy = dyMin + 1; dy < 0; ++dy) {
            for (int dx = dxMin + 1; dx <= dxMax - 1; ++dx) {
                for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
                    TileEntity te = aBaseMetaTileEntity.getTileEntityOffset(dx, dy, dz);
                    if (te == null) continue;
                    ICleanroomReceiver receiver;
                    if (te instanceof ICleanroomReceiver r) {
                        receiver = r;
                    } else { // new cleanroom system with Capabilities
                        receiver = Capabilities.getCapability(te, ICleanroomReceiver.class);
                    }
                    if (receiver != null) {
                        receiver.setCleanroom(this);
                        cleanroomReceivers.add(receiver);
                    }
                }
            }
        }

        // Step 5: Apply door penalty and redstone output
        if (isDoorOpen) {
            this.mEfficiency = Math.max(0, this.mEfficiency - 200);
        }
        for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
            final byte t = (byte) Math.max(1, (byte) (15 / (10000f / this.mEfficiency)));
            aBaseMetaTileEntity.setInternalOutputRedstoneSignal(tSide, t);
        }

        if (debugCleanroom) GTLog.out.println("Cleanroom: Check successful.");
    }
    // endregion

    // region Processing Logic
    private static final int MAX_WIDTH = 63;
    private static final int MAX_HEIGHT = 64;
    private static final int MIN_GLASS_TIER = 4;
    private final Set<ICleanroomReceiver> cleanroomReceivers = new HashSet<>();
    private int mHeight = -1;
    private static final int MASK_CASING = 1;
    private static final int MASK_FILTER = 1 << 1;
    private static final int MASK_GLASS = 1 << 2;
    private static final int MASK_OTHER = 1 << 3;
    private static final int MASK_DOOR = 1 << 4;
    private static final int MASK_HATCH = 1 << 5;
    private static final int MASK_CEILING_INTERNAL = MASK_FILTER;
    private static final int MASK_CEILING_EDGE = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_HATCH;
    private static final int MASK_WALL_INTERNAL = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_DOOR | MASK_HATCH;
    private static final int MASK_WALL_EDGE = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_HATCH;
    private static final int MASK_FLOOR_INTERNAL = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_HATCH;
    private static final int MASK_FLOOR_EDGE = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_HATCH;
    protected int dxMin = 0, dxMax = 0, dzMin = 0, dzMax = 0, dyMin = 0;
    protected int casingCount;
    protected int otherCount;
    protected boolean isDoorOpen;
    private static final HashSet<String> allowedBlocks = new HashSet<>();
    private static int maxReplacementPercentage = 30;
    private static final String CONFIG_CATEGORY = "tst_cleanroom";

    private static final String[] DEFAULT_ALLOWED_BLOCKS = { "BW_GlasBlocks", "tile.openblocks.elevator",
        "tile.openblocks.elevator_rotating", "tile.blockTravelAnchor", "tile.blockCosmeticOpaque:2",
        "tile.extrautils:etherealglass", };

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        mEfficiency = 10000;

        // negate it to trigger the special energy consumption function. divide by 10 to get the actual final
        // consumption.
        lEUt = -10;
        mMaxProgresstime = 20;

        {
            var a = filterValidMTEs(mInputHatches);
            var b = filterValidMTEs(mOutputHatches);
            var c = filterValidMTEs(mInputBusses);
            var d = filterValidMTEs(mOutputBusses);
            boolean item_me = true;
            boolean fluid_me = canDumpFluidToME();
            if ((a.size() != b.size() && (!item_me)) || (c.size() != d.size() && (!fluid_me))) {
                stopMachine();
            }
            for (int i = 0; i < c.size(); i++) {
                for (var item : c.get(i).mInventory) {
                    if (item != null) {
                        if (item_me) {
                            d.get(0)
                                .storePartial(item);
                        } else if (d.get(i)
                            .storePartial(item.copy())) {
                                item.stackSize = 0;
                            }
                    }
                }
            }
            for (int i = 0; i < a.size(); i++) {
                if (a.get(i)
                    .getFluid() == null) continue;
                a.get(i)
                    .getFluid().amount -= b.get(i)
                        .fill(
                            a.get(i)
                                .getFluid(),
                            true);
            }
        }

        return SimpleCheckRecipeResult.ofSuccess("running fine");
    }

    @Override
    public int getCleanness() {
        return mEfficiency;
    }

    @Override
    public boolean isValidCleanroom() {
        return isValid() && mMachine;
    }

    @Override
    public void pollute() {
        mEfficiency = 0;
        mWrench = false;
        mScrewdriver = false;
        mSoftMallet = false;
        mHardHammer = false;
        mSolderingTool = false;
        mCrowbar = false;
    }

    private boolean isPlascrete(Block block, int meta) {
        return block == GregTechAPI.sBlockReinforced && meta == 2;
    }

    private boolean isFilter(Block block, int meta) {
        return block == GregTechAPI.sBlockCasings3 && meta == 11;
    }

    private boolean isInAllowedList(Block block, int meta) {
        return allowedBlocks.contains(block.getUnlocalizedName() + ":" + meta)
            || allowedBlocks.contains(block.getUnlocalizedName());
    }

    /**
     * Identify the type of block at the given offset. Only checks types permitted by allowedMask.
     * This method has no side effects (does not add hatches or modify counts).
     */
    private BlockType getBlockType(IGregTechTileEntity base, int dx, int dy, int dz, int allowedMask) {
        Block block = base.getBlockOffset(dx, dy, dz);
        int meta = base.getMetaIDOffset(dx, dy, dz);

        if ((allowedMask & MASK_CASING) != 0 && isPlascrete(block, meta)) return BlockType.CASING;

        if ((allowedMask & MASK_FILTER) != 0 && isFilter(block, meta)) return BlockType.FILTER;

        if ((allowedMask & MASK_GLASS) != 0) {
            Integer glassTier = getGlassBlockTier(block, meta);
            if (glassTier != null && glassTier >= MIN_GLASS_TIER) return BlockType.GLASS;
        }

        if ((allowedMask & MASK_OTHER) != 0 && isInAllowedList(block, meta)) return BlockType.OTHER;

        if ((allowedMask & MASK_DOOR) != 0 && block instanceof ic2.core.block.BlockIC2Door) {
            if (!isDoorOpen && (meta & 8) == 0) {
                if (dz == dzMin || dz == dzMax) {
                    // Door on Z-boundary wall (runs parallel to X axis)
                    isDoorOpen = meta == 0 || meta == 2 || meta == 5 || meta == 7;
                } else if (dx == dxMin || dx == dxMax) {
                    // Door on X-boundary wall (runs parallel to Z axis)
                    isDoorOpen = meta == 1 || meta == 3 || meta == 4 || meta == 6;
                }
            }
            return BlockType.DOOR;
        }

        if ((allowedMask & MASK_HATCH) != 0) {
            IGregTechTileEntity te = base.getIGregTechTileEntityOffset(dx, dy, dz);
            if (te != null && te.getMetaTileEntity() != null) return BlockType.HATCH;
        }

        return BlockType.INVALID;
    }

    /**
     * Add a structure block at the given offset. Identifies the block type, updates counts,
     * and registers hatches. Returns false if the block is invalid for this position.
     */
    private boolean addStructureBlock(IGregTechTileEntity base, int dx, int dy, int dz, int allowedMask) {
        BlockType type = getBlockType(base, dx, dy, dz, allowedMask);
        switch (type) {
            case CASING:
                casingCount++;
                return true;
            case FILTER:
                return true;
            case GLASS:
            case OTHER:
            case DOOR:
                otherCount++;
                return true;
            case HATCH:
                IGregTechTileEntity te = base.getIGregTechTileEntityOffset(dx, dy, dz);
                if (this.addToMachineList(te, CASING_INDEX)) {
                    otherCount++;
                    return true;
                }
                IMetaTileEntity mte = te.getMetaTileEntity();
                if (mte instanceof MTEBasicHull) {
                    otherCount++;
                    return true;
                }
                if (debugCleanroom) {
                    GTLog.out.println("Cleanroom: Incorrect GT block at offset (" + dx + ", " + dy + ", " + dz + ").");
                }
                return false;
            case INVALID:
            default:
                if (debugCleanroom) {
                    GTLog.out.println("Cleanroom: Invalid block at offset (" + dx + ", " + dy + ", " + dz + ").");
                }
                return false;
        }
    }

    /**
     * Find the horizontal size of the cleanroom by scanning in both directions from the controller.
     * Supports both odd and even widths. For even widths, the controller can be in either of the
     * two middle positions.
     */
    private boolean checkSize(IGregTechTileEntity base) {
        // Scan -X direction
        for (dxMin = -1; dxMin >= -MAX_WIDTH / 2; --dxMin) {
            if (getBlockType(base, dxMin, 0, 0, MASK_CEILING_INTERNAL) == BlockType.INVALID) break;
        }
        if (dxMin < -MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (x-axis).");
            return false;
        }
        // Scan +X direction
        for (dxMax = 1; dxMax <= MAX_WIDTH / 2; ++dxMax) {
            if (getBlockType(base, dxMax, 0, 0, MASK_CEILING_INTERNAL) == BlockType.INVALID) break;
        }
        if (dxMax > MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (x-axis).");
            return false;
        }
        // Controller must be centered (or off by 1 for even widths)
        if (Math.abs(dxMin + dxMax) > 1) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Controller not centered (x-axis).");
            return false;
        }

        // Scan -Z direction
        for (dzMin = -1; dzMin >= -MAX_WIDTH / 2; --dzMin) {
            if (getBlockType(base, 0, 0, dzMin, MASK_CEILING_INTERNAL) == BlockType.INVALID) break;
        }
        if (dzMin < -MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (z-axis).");
            return false;
        }
        // Scan +Z direction
        for (dzMax = 1; dzMax <= MAX_WIDTH / 2; ++dzMax) {
            if (getBlockType(base, 0, 0, dzMax, MASK_CEILING_INTERNAL) == BlockType.INVALID) break;
        }
        if (dzMax > MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (z-axis).");
            return false;
        }
        if (Math.abs(dzMin + dzMax) > 1) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Controller not centered (z-axis).");
            return false;
        }

        if (debugCleanroom) GTLog.out
            .println("Cleanroom: dxMin=" + dxMin + ", dxMax=" + dxMax + ", dzMin=" + dzMin + ", dzMax=" + dzMax);
        return true;
    }

    /**
     * Verify the ceiling layer: edges must be valid edge blocks, interior must be filters.
     */
    private boolean checkCeiling(IGregTechTileEntity base) {
        for (int dx = dxMin; dx <= dxMax; ++dx) {
            for (int dz = dzMin; dz <= dzMax; ++dz) {
                if (dx == 0 && dz == 0) continue; // controller position
                if (dx == dxMin || dx == dxMax || dz == dzMin || dz == dzMax) {
                    // Edge
                    if (!addStructureBlock(base, dx, 0, dz, MASK_CEILING_EDGE)) return false;
                } else {
                    // Interior - must be filter
                    if (!addStructureBlock(base, dx, 0, dz, MASK_CEILING_INTERNAL)) return false;
                }
            }
        }
        return true;
    }

    /**
     * Check one horizontal layer of walls. Doors are only allowed on wall internals, not edges.
     */
    private boolean checkWall(IGregTechTileEntity base, int dy) {
        // Wall internals (allow doors)
        for (int dx = dxMin + 1; dx <= dxMax - 1; ++dx) {
            if (!addStructureBlock(base, dx, dy, dzMin, MASK_WALL_INTERNAL)) return false;
            if (!addStructureBlock(base, dx, dy, dzMax, MASK_WALL_INTERNAL)) return false;
        }
        for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
            if (!addStructureBlock(base, dxMin, dy, dz, MASK_WALL_INTERNAL)) return false;
            if (!addStructureBlock(base, dxMax, dy, dz, MASK_WALL_INTERNAL)) return false;
        }
        // Wall edges/corners (no doors)
        if (!addStructureBlock(base, dxMin, dy, dzMin, MASK_WALL_EDGE)) return false;
        if (!addStructureBlock(base, dxMin, dy, dzMax, MASK_WALL_EDGE)) return false;
        if (!addStructureBlock(base, dxMax, dy, dzMin, MASK_WALL_EDGE)) return false;
        if (!addStructureBlock(base, dxMax, dy, dzMax, MASK_WALL_EDGE)) return false;
        return true;
    }

    /**
     * Try to interpret a layer as the floor. Only checks interior blocks first (no side effects
     * on failure). If valid, commits the interior blocks and returns true.
     */
    private boolean checkFloor(IGregTechTileEntity base, int dy) {
        int addedCasings = 0;
        int addedOther = 0;
        ArrayList<IGregTechTileEntity> hatchTiles = new ArrayList<>();

        // Check interior blocks without committing
        for (int dx = dxMin + 1; dx <= dxMax - 1; ++dx) {
            for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
                BlockType type = getBlockType(base, dx, dy, dz, MASK_FLOOR_INTERNAL);
                switch (type) {
                    case CASING:
                        addedCasings++;
                        break;
                    case GLASS:
                    case OTHER:
                        addedOther++;
                        break;
                    case HATCH:
                        IGregTechTileEntity te = base.getIGregTechTileEntityOffset(dx, dy, dz);
                        hatchTiles.add(te);
                        addedOther++;
                        break;
                    case INVALID:
                        return false; // not a floor at this level
                    default:
                        break;
                }
            }
        }

        // Register hatches first; abort without modifying counts if any fails
        for (IGregTechTileEntity te : hatchTiles) {
            if (!this.addToMachineList(te, CASING_INDEX)) {
                IMetaTileEntity mte = te.getMetaTileEntity();
                if (!(mte instanceof MTEBasicHull)) {
                    if (debugCleanroom) GTLog.out.println("Cleanroom: Incorrect GT block on floor.");
                    return false;
                }
            }
        }

        // All valid, commit counts
        casingCount += addedCasings;
        otherCount += addedOther;
        return true;
    }

    public static void loadConfig(Configuration cfg) {
        maxReplacementPercentage = cfg.getInt(
            "maxReplacementPercentage",
            CONFIG_CATEGORY,
            30,
            0,
            100,
            "Maximum percentage of plascrete blocks that can be replaced by other valid blocks (glass, doors, hatches, etc.).");

        String[] blockList = cfg.getStringList(
            "allowedBlocks",
            CONFIG_CATEGORY,
            DEFAULT_ALLOWED_BLOCKS,
            "Additional blocks allowed in the cleanroom walls. Format: <unlocalized name> or <unlocalized name>:<meta>. "
                + "Note: EV+ tier glass (as registered in GlassTier) is always allowed regardless of this list.");

        allowedBlocks.clear();
        Collections.addAll(allowedBlocks, blockList);
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if ((sideDirection.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) != 0) {
            return new ITexture[] { TextureFactory.of(BLOCK_PLASCRETE), active
                ? TextureFactory.of(
                    TextureFactory.of(OVERLAY_TOP_CLEANROOM_ACTIVE),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_TOP_CLEANROOM_ACTIVE_GLOW)
                        .glow()
                        .build())
                : TextureFactory.of(
                    TextureFactory.of(OVERLAY_TOP_CLEANROOM),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_TOP_CLEANROOM_GLOW)
                        .glow()
                        .build()) };
        }
        return new ITexture[] { TextureFactory.of(BLOCK_PLASCRETE) };
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.TSTcleanroom.tooltip.machine_type
        // # Cleanroom
        // #zh_CN 超净间
        tt.addMachineType(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.machine_type"));
        // #tr tst.common.machine.TSTcleanroom.tooltip.info.01
        // # Controller block for the Cleanroom
        // #zh_CN TST超净间的控制器方块
        tt.addInfo(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.info.01"));
        // #tr tst.common.machine.TSTcleanroom.tooltip.info.02
        // # Consumes §640 EU/t§7 when first turned on
        // #zh_CN 初次启动功率: §640EU/t§7
        tt.addInfo(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.info.02"));
        // #tr tst.common.machine.TSTcleanroom.tooltip.info.03
        // # and §64 EU/t§7 once at §c100%§7 efficiency
        // #zh_CN §c100%§7洁净度稳定运行功率: §64EU/t§7
        tt.addInfo(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.info.03"));
        // #tr tst.common.machine.TSTcleanroom.tooltip.info.04
        // # If you use an LV energy hatch, it will actually accept §62A§7 instead of just §61A§7.
        // #zh_CN 如果你使用一个LV能源仓供电, 它将接受§62A§7电流
        tt.addInfo(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.info.04"));
        // #tr tst.common.machine.TSTcleanroom.tooltip.info.05
        // # MV+ energy hatches just accept 1A as usual. For HV+ the cleanroom will overclock and gain efficiency faster.
        // #zh_CN MV能源仓使用1A电流, 使用HV以上能源仓, TST超净间将会超频, 更快地提升洁净度.
        tt.addInfo(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.info.05"));
        // #tr tst.common.machine.TSTcleanroom.tooltip.info.06
        // # always clean，can be use as laser nodes，max §664*64*64§7
        // #zh_CN 任何情况不会掉落洁净度, 可以作为有源变压器使用, 最大§664*64*64§7, 属于超净间的输入仓/总线的流体/物品会按一定顺序输出到输出仓/总线, 如果有me输出仓/总线, 则会统一输出到此内部.
        tt.addInfo(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.info.06"));
        // #tr tst.common.machine.TSTcleanroom.tooltip.info.07
        // # Machines that cause pollution aren't allowed to be put in.
        // #zh_CN 不可放入会产生污染的机器
        tt.addInfo(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.info.07"));
        // #tr tst.common.machine.TSTcleanroom.tooltip.info.08
        // # §4§lWATCH OUT! DO NOT DESTROY STRUCTURE BLOCK WHEN MACHINE IS WORKING!
        // #zh_CN §4§o§l小心, 不要在开机状态下破坏结构!
        tt.addInfo(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.info.08"));
        // #tr tst.common.machine.TSTcleanroom.tooltip.info.09
        // # §6§lBuffered by TST
        // #zh_CN §6§lBuffered by TST
        tt.addInfo(translateToLocal("tst.common.machine.TSTcleanroom.tooltip.info.09"));
        tt.beginVariableStructureBlock(3, MAX_WIDTH, 4, MAX_HEIGHT, 3, MAX_WIDTH, true);
        tt.addController(translateToLocal("Tooltip_TST_CleanRoom_Controller"));
        tt.addCasingInfoRange(translateToLocal("Tooltip_TST_CleanRoom_Plascrete"), 20, 19592, false);
        tt.addStructureInfo(
            String.format(translateToLocal("Tooltip_TST_CleanRoom_ReplaceLimit"), maxReplacementPercentage));
        tt.addStructureInfo(translateToLocal("Tooltip_TST_CleanRoom_GlassInfo"));
        tt.addStructureInfo(translateToLocal("Tooltip_TST_CleanRoom_ConfigInfo"));
        tt.addOtherStructurePart(
            translateToLocal("Tooltip_TST_CleanRoom_FilterCasing"),
            translateToLocal("Tooltip_TST_CleanRoom_FilterCasingPos"));
        tt.addEnergyHatch(translateToLocal("Tooltip_TST_CleanRoom_EnergyHatch"));
        tt.addStructureInfo(translateToLocal("Tooltip_TST_CleanRoom_Door"));
        tt.addStructureInfo(translateToLocal("Tooltip_TST_CleanRoom_Hull"));
        tt.toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Nested Classes

    private enum BlockType {
        CASING,
        FILTER,
        GLASS,
        OTHER,
        DOOR,
        HATCH,
        INVALID
    }

    // endregion

}
