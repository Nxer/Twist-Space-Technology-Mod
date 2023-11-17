package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import java.io.Serializable;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import gregtech.api.enums.TierEU;

public class DSP_DataCell implements Serializable {

    // region Variables
    private String ownerName;
    private DSP_Galaxy galaxy;
    private long amountDSPSolarSail;
    private long amountDSPNode;
    public static final long solarSailPowerPoint = TierEU.UV;
    // endregion

    // region Class Constructor
    public DSP_DataCell(String ownerName, DSP_Galaxy galaxy) {
        this.ownerName = ownerName;
        this.galaxy = galaxy;
        this.amountDSPSolarSail = 0;
        this.amountDSPNode = 0;
    }

    public DSP_DataCell() {
        this.ownerName = "defaultPlayerWithErrorInformation";
        this.galaxy = DSP_Galaxy.DSP_Galaxy_NULL;
        this.amountDSPNode = 0;
        this.amountDSPSolarSail = 0;
    }

    // endregion

    // region Methods
    public long getDSPPowerPoint() {
        return (long) (solarSailPowerPoint * this.amountDSPSolarSail * Math.pow(this.amountDSPNode + 1, 0.5));
    }

    public DSP_DataCell addDSPSolarSail(long amount) {
        this.amountDSPSolarSail += amount;
        TwistSpaceTechnology.LOG.info("test addDSPSolarSail: add " + amount + " at " + this);
        return this;
    }

    public DSP_DataCell addDSPNode(long amount) {
        this.amountDSPNode += amount;
        TwistSpaceTechnology.LOG.info("test addDSPNode: add " + amount + " at " + this);
        return this;
    }

    @Override
    public String toString() {
        return "DSP_DataCell{ ownerName:" + ownerName
            + " , galaxy:"
            + galaxy
            + " , amountDSPSolarSail:"
            + amountDSPSolarSail
            + " , amountDSPNode:"
            + amountDSPNode
            + " }";
    }

    // endregion

    // region Getters and Setters

    public String getOwnerName() {
        return ownerName;
    }

    public DSP_DataCell setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public DSP_Galaxy getGalaxy() {
        return galaxy;
    }

    public DSP_DataCell setGalaxy(DSP_Galaxy galaxy) {
        this.galaxy = galaxy;
        return this;
    }

    public long getDSPSolarSail() {
        return amountDSPSolarSail;
    }

    public DSP_DataCell setDSPSolarSail(long amountDSPSolarSail) {
        this.amountDSPSolarSail = amountDSPSolarSail;
        return this;
    }

    public long getDSPNode() {
        return amountDSPNode;
    }

    public DSP_DataCell setDSPNode(long amountDSPNode) {
        this.amountDSPNode = amountDSPNode;
        return this;
    }

    // endregion

}
