package com.Nxer.TwistSpaceTechnology.common.machine;

import static gregtech.api.enums.GT_Values.debugCleanroom;
import static gregtech.api.enums.Textures.BlockIcons.BLOCK_PLASCRETE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_GLOW;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ICleanroom;
import gregtech.api.interfaces.ICleanroomReceiver;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;

public class TST_CleanRoom extends GT_MetaTileEntity_MultiblockBase_EM
    implements IConstructable, ISecondaryDescribable, ICleanroom {

    private static final int maxX = 63;
    private static final int maxY = 64;
    private static final int maxZ = 63;

    // private static long bufferedEU = 0;

    private final Set<ICleanroomReceiver> cleanroomReceivers = new HashSet<>();
    private int mHeight = -1;

    public TST_CleanRoom(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        // GT_MetaTileEntity_Cleanroom

    }

    public TST_CleanRoom(String aName) {
        super(aName);
    }

    public void init() {
        ePowerPass = true;
        useLongPower = true;
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return null;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_CleanRoom(mName);
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
        mSoftHammer = false;
        mHardHammer = false;
        mSolderingTool = false;
        mCrowbar = false;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("")
            .addInfo("Controller block for the Cleanroom")
            .addInfo("Consumes 40 EU/t when first turned on")
            .addInfo("and 4 EU/t once at 100% efficiency")
            .addInfo("If you use an LV energy hatch, it will actually accept 2A instead of just 1A.")
            .addInfo(
                "MV+ energy hatches just accept 1A as usual. For HV+ the cleanroom will overclock and gain efficiency faster.")
            .addInfo("tst.cleanroom.desc.0")
            .addInfo("the height of empty space within")
            .addInfo("Machines that cause pollution aren't allowed to be put in.")
            .addInfo("WATCH OUT! DO NOT DESTROY STRUCTURE BLOCK WHEN MACHINE IS WORKING!")
            .addInfo("buffered bt TST")
            .addInfo("author shordinger")
            .addSeparator()
            .beginVariableStructureBlock(3, 15, 4, 15, 3, 15, true)
            .addController("Top center")
            .addCasingInfoRange("Plascrete", 20, 1007, false)
            .addStructureInfo(
                GT_Values.cleanroomGlass
                    + "% of the Plascrete can be replaced with Reinforced Glass (not counting the top layer)")
            .addStructureInfo(
                "Other material can be used in place of Plascrete, even in higher percentages. See config for detail")
            .addOtherStructurePart("Filter Machine Casing", "Top besides controller and edges")
            .addEnergyHatch("Any casing except top layer. Exactly one.")
            .addMaintenanceHatch("Any casing except top layer")
            .addStructureInfo("0-2x Reinforced Door (keep closed or efficiency will reduce)")
            .addStructureInfo("Up to 1 Elevator, Rotating Elevator, and Travel Anchor each")
            .addStructureInfo("Up to 10 Machine Hulls for Item & Energy transfer through walls")
            .addStructureInfo("You can also use Diodes for more power")
            .addStructureInfo("Diodes also count towards 10 Machine Hulls count limit")
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[] { "The base can be rectangular." };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 20 == 0) {
            var a = filterValidMTEs(mInputHatches);
            var b = filterValidMTEs(mOutputHatches);
            var c = filterValidMTEs(mInputBusses);
            var d = filterValidMTEs(mOutputBusses);
            boolean item_me = canDumpItemToME();
            boolean fluid_me = canDumpFluidToME();
            if ((a.size() != b.size() && (!item_me)) || (c.size() != d.size() && (!fluid_me))) {
                stopMachine();
            }
            for (int i = 0; i < c.size(); i++) {
                for (var item : c.get(i).mInventory) {
                    if (item != null) {
                        if (item_me) {
                            item.stackSize -= ((GT_MetaTileEntity_Hatch_OutputBus_ME) d.get(0)).store(item);
                        } else if (d.get(i)
                            .storeAll(item.copy())) {
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
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing_EM() {
        mEfficiencyIncrease = 100;

        // use the standard overclock mechanism to determine duration and estimate a maximum consumption
        calculateOverclockedNessMultiInternal(40, 45 * Math.max(1, mHeight - 1), 1, getMaxInputVoltage(), false);
        // negate it to trigger the special energy consumption function. divide by 10 to get the actual final
        // consumption.
        lEUt /= -10;
        if (ePowerPass) {
            mMaxProgresstime = 20;
        } else {
            mEfficiencyIncrease = 0;
            mMaxProgresstime = 0;
        }
        eAmpereFlow = 0;
        mEUt = 0;
        return ePowerPass ? SimpleCheckRecipeResult.ofSuccess("routing")
            : SimpleCheckRecipeResult.ofFailure("running fine");
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mHardHammer = true;
        mSoftHammer = true;
        mScrewdriver = true;
        mCrowbar = true;
        mSolderingTool = true;
        mWrench = true;
        // Cleanroom
        int x = 1;
        int z = 1;
        int y = 1;
        int mDoorCount = 0;
        int mHullCount = 0;
        int mPlascreteCount = 0;
        final HashMap<String, Integer> otherBlocks = new HashMap<>();
        boolean doorState = false;
        this.mUpdate = 100;
        cleanroomReceivers.forEach(r -> r.setCleanroom(null));
        cleanroomReceivers.clear();
        debugCleanroom = true;
        if (debugCleanroom) {
            GT_Log.out.println("Cleanroom: Checking machine");
        }
        for (int i = 1; i <= maxX / 2; i++) {
            final Block tBlock = aBaseMetaTileEntity.getBlockOffset(i, 0, 0);
            final int tMeta = aBaseMetaTileEntity.getMetaIDOffset(i, 0, 0);
            if (tBlock != GregTech_API.sBlockCasings3 || tMeta != 11) {
                if (tBlock == GregTech_API.sBlockReinforced || tMeta == 2) {
                    x = i;
                    break;
                } else {
                    if (debugCleanroom) {
                        GT_Log.out.println("Cleanroom: Unable to detect room X edge?");
                    }
                    return false;
                }
            }
        }
        for (int i = 1; i <= maxZ / 2; i++) {
            final Block tBlock = aBaseMetaTileEntity.getBlockOffset(0, 0, i);
            final int tMeta = aBaseMetaTileEntity.getMetaIDOffset(0, 0, i);
            if (tBlock != GregTech_API.sBlockCasings3 || tMeta != 11) {
                if (tBlock == GregTech_API.sBlockReinforced || tMeta == 2) {
                    z = i;
                    break;
                } else {
                    if (debugCleanroom) {
                        GT_Log.out.println("Cleanroom: Unable to detect room Z edge?");
                    }
                    return false;
                }
            }
        }
        // detect rectangular area of filters
        for (int i = -x + 1; i < x; i++) {
            for (int j = -z + 1; j < z; j++) {
                if (i == 0 && j == 0) continue;
                final Block tBlock = aBaseMetaTileEntity.getBlockOffset(i, 0, j);
                final int tMeta = aBaseMetaTileEntity.getMetaIDOffset(i, 0, j);
                if (tBlock != GregTech_API.sBlockCasings3 && tMeta != 11) {
                    if (debugCleanroom) {
                        GT_Log.out.println("Cleanroom: This is not a filter.");
                    }
                    return false;
                }
            }
        }
        for (int i = -1; i > -maxY; i--) {
            final Block tBlock = aBaseMetaTileEntity.getBlockOffset(x, i, z);
            final int tMeta = aBaseMetaTileEntity.getMetaIDOffset(x, i, z);
            if (tBlock != GregTech_API.sBlockReinforced || tMeta != 2) {
                y = i + 1;
                break;

            }
        }
        if (y > -2) {
            if (debugCleanroom) {
                GT_Log.out.println("Cleanroom: Room not tall enough?");
            }
            return false;
        }
        for (int dX = -x; dX <= x; dX++) {
            for (int dZ = -z; dZ <= z; dZ++) {
                for (int dY = 0; dY >= y; dY--) {
                    if (dX == -x || dX == x || dY == 0 || dY == y || dZ == -z || dZ == z) {
                        Block tBlock = aBaseMetaTileEntity.getBlockOffset(dX, dY, dZ);
                        int tMeta = aBaseMetaTileEntity.getMetaIDOffset(dX, dY, dZ);
                        if (dY == 0) { // TOP
                            if (dX == -x || dX == x || dZ == -z || dZ == z) { // Top Border
                                if (tBlock != GregTech_API.sBlockReinforced || tMeta != 2) {
                                    if (debugCleanroom) {
                                        GT_Log.out.println("Cleanroom: Non reinforced block on top edge? tMeta != 2");
                                    }
                                    return false;
                                }
                            } else if (dX != 0 || dZ != 0) { // Top Inner exclude center
                                if (tBlock != GregTech_API.sBlockCasings3 || tMeta != 11) {
                                    if (debugCleanroom) {
                                        GT_Log.out.println(
                                            "Cleanroom: Non reinforced block on top face interior? tMeta != 11");
                                    }
                                    return false;
                                }
                            }
                        } else if (tBlock == GregTech_API.sBlockReinforced && tMeta == 2) {
                            mPlascreteCount++;
                        } else {
                            final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity
                                .getIGregTechTileEntityOffset(dX, dY, dZ);
                            if (!this.addToMachineList(tTileEntity, 210)) {
                                if (tBlock instanceof ic2.core.block.BlockIC2Door) {
                                    if ((tMeta & 8) == 0) {
                                        // let's not fiddle with bits anymore.
                                        if (Math.abs(dZ) < z) // on side parallel to z axis
                                            doorState = tMeta == 1 || tMeta == 3 || tMeta == 4 || tMeta == 6;
                                        else if (Math.abs(dX) < x) // on side parallel to x axis
                                            doorState = tMeta == 0 || tMeta == 2 || tMeta == 5 || tMeta == 7;
                                        // corners ignored
                                    }
                                    mDoorCount++;
                                } else {
                                    if (tTileEntity != null) {
                                        final IMetaTileEntity aMetaTileEntity = tTileEntity.getMetaTileEntity();
                                        if (aMetaTileEntity == null) {
                                            if (debugCleanroom) {
                                                GT_Log.out.println("Cleanroom: Missing block? Not a aMetaTileEntity");
                                            }
                                            return false;
                                        }
                                        if (aMetaTileEntity instanceof GT_MetaTileEntity_BasicHull) {
                                            mHullCount++;
                                        } else {
                                            if (debugCleanroom) {
                                                GT_Log.out.println(
                                                    "Cleanroom: Incorrect GT block? " + tBlock.getUnlocalizedName());
                                            }
                                            return false;
                                        }
                                    } else {
                                        String key = tBlock.getUnlocalizedName() + ":" + tMeta;
                                        if (config.containsKey(key)) { // check with meta first
                                            otherBlocks.compute(key, (k, v) -> v == null ? 1 : v + 1);
                                        } else {
                                            key = tBlock.getUnlocalizedName();
                                            if (config.containsKey(key)) {
                                                otherBlocks.compute(key, (k, v) -> v == null ? 1 : v + 1);
                                            } else {
                                                if (debugCleanroom) {
                                                    GT_Log.out.println(
                                                        "Cleanroom: not allowed block " + tBlock.getUnlocalizedName());
                                                }
                                                return false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.mMaintenanceHatches.size() != 1 || this.mEnergyHatches.size() != 1
            || mDoorCount > 4
            || mHullCount > 10) {
            if (debugCleanroom) {
                GT_Log.out.println("Cleanroom: Incorrect number of doors, hulls, or hatches.");
            }
            return false;
        }
        if (mPlascreteCount < 20) {
            if (debugCleanroom) {
                GT_Log.out.println("Cleanroom: Could not find 20 Plascrete.");
            }
            return false;
        }
        final float ratio = (((float) mPlascreteCount) / 100f);
        for (Map.Entry<String, Integer> e : otherBlocks.entrySet()) {
            final ConfigEntry ce = config.get(e.getKey());
            if (ce.allowedCount > 0) { // count has priority
                if (e.getValue() > ce.allowedCount) {
                    if (debugCleanroom) {
                        GT_Log.out.println("Cleanroom: Absolute count too high for a block.");
                    }
                    return false;
                }
            } else if (e.getValue() > ratio * ce.percentage) {
                if (debugCleanroom) {
                    GT_Log.out.println("Cleanroom: Relative count too high for a block.");
                }
                return false;
            }
        }

        setCleanroomReceivers(x, y, z, aBaseMetaTileEntity);

        if (doorState) {
            this.mEfficiency = Math.max(0, this.mEfficiency - 200);
        }
        for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
            final byte t = (byte) Math.max(1, (byte) (15 / (10000f / this.mEfficiency)));
            aBaseMetaTileEntity.setInternalOutputRedstoneSignal(tSide, t);
        }
        this.mHeight = -y;
        if (debugCleanroom) {
            GT_Log.out.println("Cleanroom: Check successful.");
        }
        return true;
    }

    private void setCleanroomReceivers(int x, int y, int z, IGregTechTileEntity aBaseMetaTileEntity) {
        for (int dX = -x + 1; dX <= x - 1; dX++) {
            for (int dZ = -z + 1; dZ <= z - 1; dZ++) for (int dY = -1; dY >= y + 1; dY--) {
                TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityOffset(dX, dY, dZ);
                if (tTileEntity instanceof ICleanroomReceiver receiver) {
                    receiver.setCleanroom(this);
                    cleanroomReceivers.add(receiver);
                }
            }
        }
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

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

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        int i = Math.min(itemStack.stackSize, maxX / 2);
        IGregTechTileEntity baseEntity = this.getBaseMetaTileEntity();
        World world = baseEntity.getWorld();
        int x = baseEntity.getXCoord();
        int y = baseEntity.getYCoord();
        int z = baseEntity.getZCoord();
        int yoff = Math.max(i * 2, 3);
        for (int X = x - i; X <= x + i; X++) for (int Y = y; Y >= y - yoff; Y--) for (int Z = z - i; Z <= z + i; Z++) {
            if (X == x && Y == y && Z == z) continue;
            if (X == x - i || X == x + i || Z == z - i || Z == z + i || Y == y - yoff) {
                if (b) StructureLibAPI.hintParticle(world, X, Y, Z, GregTech_API.sBlockReinforced, 2);
                else world.setBlock(X, Y, Z, GregTech_API.sBlockReinforced, 2, 2);
            } else if (Y == y) {
                if (b) StructureLibAPI.hintParticle(world, X, Y, Z, GregTech_API.sBlockCasings3, 11);
                else world.setBlock(X, Y, Z, GregTech_API.sBlockCasings3, 11, 2);
            }
        }
    }

    // @Override
    // public void loadNBTData(NBTTagCompound aNBT) {
    // aNBT.getLong("bufferedEU");
    // super.loadNBTData(aNBT);
    // }

    // @Override
    // public void saveNBTData(NBTTagCompound aNBT) {
    //// aNBT.setLong("bufferedEU", bufferedEU);
    // super.saveNBTData(aNBT);
    // }

    private static class ConfigEntry {

        final int percentage;
        final int allowedCount;

        ConfigEntry(int percentage, int count) {
            this.percentage = percentage;
            this.allowedCount = count;
        }
    }

    private static final HashMap<String, ConfigEntry> config = new HashMap<>();
    private static final String category = "cleanroom_allowed_blocks";

    private static void setDefaultConfigValues(Configuration cfg) {
        cfg.get("cleanroom_allowed_blocks.manaGlass", "Name", "tile.manaGlass");
        cfg.get("cleanroom_allowed_blocks.manaGlass", "Percentage", 50);
        cfg.get("cleanroom_allowed_blocks.elfGlass", "Name", "tile.elfGlass");
        cfg.get("cleanroom_allowed_blocks.elfGlass", "Percentage", 50);
        cfg.get("cleanroom_allowed_blocks.reinforced_glass", "Name", "blockAlloyGlass");
        cfg.get("cleanroom_allowed_blocks.reinforced_glass", "Percentage", 5);
        cfg.get("cleanroom_allowed_blocks.bw_reinforced_glass_0", "Name", "BW_GlasBlocks");
        cfg.get("cleanroom_allowed_blocks.bw_reinforced_glass_0", "Percentage", 50);
        cfg.get("cleanroom_allowed_blocks.bw_reinforced_glass_0", "Meta", 0);
        cfg.get("cleanroom_allowed_blocks.bw_reinforced_glass", "Name", "BW_GlasBlocks");
        cfg.get("cleanroom_allowed_blocks.bw_reinforced_glass", "Percentage", 100);
        cfg.get("cleanroom_allowed_blocks.elevator", "Name", "tile.openblocks.elevator");
        cfg.get("cleanroom_allowed_blocks.elevator", "Count", 1);
        cfg.get("cleanroom_allowed_blocks.travel_anchor", "Name", "tile.blockTravelAnchor");
        cfg.get("cleanroom_allowed_blocks.travel_anchor", "Count", 1);
        cfg.get("cleanroom_allowed_blocks.warded_glass", "Name", "tile.blockCosmeticOpaque");
        cfg.get("cleanroom_allowed_blocks.warded_glass", "Meta", 2);
        cfg.get("cleanroom_allowed_blocks.warded_glass", "Percentage", 50);
    }

    public static void loadConfig(Configuration cfg) {
        if (!cfg.hasCategory(category)) setDefaultConfigValues(cfg);
        for (ConfigCategory cc : cfg.getCategory(category)
            .getChildren()) {
            final String name = cc.get("Name")
                .getString();
            if (cc.containsKey("Count")) {
                if (cc.containsKey("Meta")) config.put(
                    name + ":"
                        + cc.get("Meta")
                            .getInt(),
                    new ConfigEntry(
                        0,
                        cc.get("Count")
                            .getInt()));
                else config.put(
                    name,
                    new ConfigEntry(
                        0,
                        cc.get("Count")
                            .getInt()));
            } else if (cc.containsKey("Percentage")) {
                if (cc.containsKey("Meta")) config.put(
                    name + ":"
                        + cc.get("Meta")
                            .getInt(),
                    new ConfigEntry(
                        cc.get("Percentage")
                            .getInt(),
                        0));
                else config.put(
                    name,
                    new ConfigEntry(
                        cc.get("Percentage")
                            .getInt(),
                        0));
            }
        }
    }
}
