package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic_t;

import net.minecraft.nbt.NBTTagCompound;

public class DysonSphereSystem {

    public static final DysonSphereSystem Unknown = new DysonSphereSystem();
    public static final int solarSailPowerPoint = 1000;
    private static final String Tag_owner = "owner";
    private static final String Tag_NodeCount = "nodes";
    private static final String Tag_SailCount = "sails";
    private static final String Tag_GalaxyName = "galaxy";
    private final Galaxy galaxy;
    private String ownedTeam;
    private long solarSailCount, nodeCount;
    /**
     * 无论何时都不应该直接调用,选择{@link #getMaxPoints()}
     */
    private long usedPowerPoints = 0;
    private long maxPowerPoints = 0;
    private boolean hasChanged = true;

    private DysonSphereSystem() {
        this.galaxy = Galaxy.Unknown;
        ownedTeam = "null";
        solarSailCount = 0;
        nodeCount = 0;
    }

    public DysonSphereSystem(String owner, Galaxy galaxy) {
        this(owner, galaxy, 0, 0);
    }

    public DysonSphereSystem(String owner, Galaxy galaxy, long solarSails, long nodes) {
        this.galaxy = galaxy;
        ownedTeam = owner;
        solarSailCount = solarSails;
        nodeCount = nodes;
        galaxy.addSystem(this);
    }

    public static DysonSphereSystem readFromNBT(NBTTagCompound compound) {
        Galaxy in;
        if ((in = Galaxy.getGalaxyFromName(compound.getString(Tag_GalaxyName))) == Galaxy.Unknown) return Unknown;
        return new DysonSphereSystem(
            compound.getString(Tag_owner),
            in,
            compound.getLong(Tag_SailCount),
            compound.getLong(Tag_NodeCount));
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setLong(Tag_SailCount, solarSailCount);
        compound.setLong(Tag_NodeCount, nodeCount);
        compound.setString(Tag_GalaxyName, galaxy.name);
        compound.setString(Tag_owner, ownedTeam);
        return compound;
    }

    public boolean tryReleasePoints(long amount) {
        if (usedPowerPoints < amount) return false;
        usedPowerPoints -= amount;
        return true;
    }

    public boolean tryOccupyPoints(long amount) {
        if (amount > getSparePoints()) return false;
        usedPowerPoints += amount;
        return true;
    }

    public void forceReleasePoints(long amount) {
        usedPowerPoints = Math.max(0, usedPowerPoints - amount);
    }

    public void setUsedPoints(long points) {
        usedPowerPoints = points;
    }

    public void addNode(long amount) {
        nodeCount += amount;
        hasChanged = true;
    }

    public DysonSphereSystem addSail(long amount) {
        solarSailCount += amount;
        hasChanged = true;
        return this;
    }

    @Override
    public String toString() {
        return "DSP_DataCell{ ownerName:" + ownedTeam
            + " , galaxy:"
            + galaxy
            + " , amountDSPSolarSail:"
            + solarSailCount
            + " , amountDSPNode:"
            + nodeCount
            + " , maxDSPPowerPoint:"
            + getMaxPoints()
            + " , usedDSPPowerPoint:"
            + usedPowerPoints
            + " , PowerPointCanUse: "
            + getSparePoints()
            + " }";
    }

    private void refreshMaxPoints() {
        maxPowerPoints = (long) (solarSailPowerPoint * solarSailCount * Math.sqrt(nodeCount + 1));
        hasChanged = false;
    }

    public long getMaxPoints() {
        if (hasChanged) refreshMaxPoints();
        return maxPowerPoints;
    }

    public long getSparePoints() {
        return getMaxPoints() - usedPowerPoints;
    }

    public String getOwnerTeam() {
        return ownedTeam;
    }

    public void setOwner(String teamName) {
        ownedTeam = teamName;
    }

    public Galaxy getGalaxyIn() {
        return galaxy;
    }

    public long countNodes() {
        return nodeCount;
    }

    public DysonSphereSystem setNodes(long nodeCount) {
        this.nodeCount = nodeCount;
        return this;
    }

    public long countSolarSail() {
        return solarSailCount;
    }

    public DysonSphereSystem setSolarSails(long solarSailCount) {
        this.solarSailCount = solarSailCount;
        return this;
    }

    public void doUpdate() {
        // TODO:光帆寿命处理
    }
}
