package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import java.io.Serializable;

public class DSP_DataCell implements Serializable {

    // region Variables
    private String ownerName;
    private DSP_Galaxy galaxy;
    private long amountDSPSolarSail;
    private long amountDSPNode;

    // endregion

    // region Class Constructor

    public DSP_DataCell(String ownerName, DSP_Galaxy galaxy) {
        this.ownerName = ownerName;
        this.galaxy = galaxy;
        this.amountDSPSolarSail = 0;
        this.amountDSPNode = 0;
    }

    // endregion

    // region Methods

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

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public DSP_Galaxy getGalaxy() {
        return galaxy;
    }

    public void setGalaxy(DSP_Galaxy galaxy) {
        this.galaxy = galaxy;
    }

    public long getAmountDSPSolarSail() {
        return amountDSPSolarSail;
    }

    public void setAmountDSPSolarSail(long amountDSPSolarSail) {
        this.amountDSPSolarSail = amountDSPSolarSail;
    }

    public long getAmountDSPNode() {
        return amountDSPNode;
    }

    public void setAmountDSPNode(long amountDSPNode) {
        this.amountDSPNode = amountDSPNode;
    }

    // endregion

}
