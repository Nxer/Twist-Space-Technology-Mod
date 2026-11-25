package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LaserMeteorMiner;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_StarThatNeverWas;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/** Shared front panel. Neighbour observation stays outside chunk rendering. */
public final class StarControllerFrame {

    public static final int OFF = 0, STANDBY = 1, RUNNING = 2;
    private static final int CONNECTED = 4;
    private static final Panel SINGLE = new Panel("OVERLAY_FRONT_STAR_FRAME", false);
    private static final Panel UPPER = new Panel("OVERLAY_FRONT_STAR_FRAME_UPPER", false);
    private static final Panel LOWER = new Panel("OVERLAY_FRONT_STAR_FRAME_LOWER", true);

    private final boolean upper;
    private volatile int display;

    public StarControllerFrame(boolean upper) {
        this.upper = upper;
    }

    public void update(IGregTechTileEntity base) {
        if (!base.isClientSide()) return;
        TST_StarThatNeverWas host = findConnectedHost(base);
        // Both halves read the host's single synchronized state, never the miner's independent active packet.
        int next = host != null ? CONNECTED | host.getStarControllerState()
            : upper && base.getMetaTileEntity() instanceof TST_StarThatNeverWas standalone
                ? standalone.getStarControllerState()
                : OFF;
        if (next == display) return;
        display = next;
        base.issueTextureUpdate();
    }

    public boolean hasLinkedNeighbour(IGregTechTileEntity base) {
        return findConnectedHost(base) != null;
    }

    private TST_StarThatNeverWas findConnectedHost(IGregTechTileEntity base) {
        if (base == null || base.isDead()) return null;
        ForgeDirection facing = base.getFrontFacing();
        if (facing == ForgeDirection.UNKNOWN || facing.offsetY != 0) return null;
        IGregTechTileEntity neighbour = base
            .getIGregTechTileEntityAtSide(upper ? ForgeDirection.DOWN : ForgeDirection.UP);
        if (neighbour == null || neighbour.isDead() || neighbour.getFrontFacing() != facing) return null;
        IGregTechTileEntity hostBase = upper ? base : neighbour;
        IGregTechTileEntity minerBase = upper ? neighbour : base;
        if (hostBase.getMetaTileEntity() instanceof TST_StarThatNeverWas host
            && minerBase.getMetaTileEntity() instanceof TST_LaserMeteorMiner miner
            && host.getExtendedFacing() == miner.getExtendedFacing()
            && (base.isClientSide() ? miner.isStarControllerLinked() : miner.hasStarLink(host))) return host;
        return null;
    }

    /** A disconnected miner returns null so its caller retains the original controller textures. */
    public ITexture[] getTexture(ForgeDirection side, ForgeDirection facing, ITexture casing) {
        int snapshot = display;
        boolean connected = (snapshot & CONNECTED) != 0;
        if (!upper && !connected) return null;
        return (upper ? connected ? UPPER : SINGLE : LOWER).getTexture(side, facing, casing, snapshot & 3);
    }

    private static final class Panel {

        private final IIconContainer off, standby, running, standbyGlow, runningGlow;

        private Panel(String name, boolean hasStandbyGlow) {
            String path = "gtnhcommunitymod:iconSets/" + name;
            off = Textures.BlockIcons.custom(path);
            standby = Textures.BlockIcons.custom(path + "_STANDBY");
            running = Textures.BlockIcons.custom(path + "_ACTIVE");
            standbyGlow = hasStandbyGlow ? Textures.BlockIcons.custom(path + "_STANDBY_GLOW") : null;
            runningGlow = Textures.BlockIcons.custom(path + "_ACTIVE_GLOW");
        }

        private ITexture[] getTexture(ForgeDirection side, ForgeDirection facing, ITexture casing, int state) {
            return TSTControllerTextures.getTexture(
                side,
                facing,
                false,
                casing,
                state == RUNNING ? running : state == STANDBY ? standby : off,
                state == RUNNING ? runningGlow : state == STANDBY ? standbyGlow : null,
                null,
                null);
        }
    }
}
