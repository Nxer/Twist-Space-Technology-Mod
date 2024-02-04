package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.solarSailCanHoldDefault;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.solarSailCanHoldPerNode;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.solarSailPowerPoint;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_WorldSavedData.markDataDirty;

import java.io.Serializable;
import java.math.BigInteger;

import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.config.Config;

import gregtech.api.util.GT_Utility;

// spotless:off
public class DSP_DataCell implements Serializable {

    // region Variables
    private String ownerName;
    private DSP_Galaxy galaxy;
    private long amountDSPSolarSail;
    private long amountDSPNode;
    private boolean dirty;
    /**
     * MaxPowerPoint = <p>PowerPointPerSolarSail * SolarSail * (Node + 1)^0.8
     */
    private long maxDSPPowerPoint;
    private long usedDSPPowerPoint;
    /**
     * A simple synced signal to confirm DSP data cell and data cell's user machine synced.
     */
    private byte dataSyncFlag = 0;
    private boolean useBigInteger = false;
    private BigInteger maxDSPPowerPoint_BigInteger = BigInteger.valueOf(0);
    private BigInteger usedDSPPowerPoint_BigInteger = BigInteger.valueOf(0);
    public static final BigInteger LongMaxValue_BigInteger = BigInteger.valueOf(Long.MAX_VALUE);
    public static final BigInteger NEGATIVE_ONE = BigInteger.valueOf(-1);

    // endregion

    // region Class Constructor
    public DSP_DataCell(String ownerName, DSP_Galaxy galaxy) {
        this.ownerName = ownerName;
        this.galaxy = galaxy;
        this.amountDSPSolarSail = 0;
        this.amountDSPNode = 0;
        this.dirty = true;
        this.maxDSPPowerPoint = 0;
        this.usedDSPPowerPoint = 0;
    }

    public DSP_DataCell() {
        this.ownerName = "defaultPlayerWithErrorInformation";
        this.galaxy = DSP_Galaxy.DSP_Galaxy_NULL;
        this.amountDSPNode = 0;
        this.amountDSPSolarSail = 0;
        this.dirty = true;
        this.maxDSPPowerPoint = 0;
        this.usedDSPPowerPoint = 0;
    }

    // endregion

    // region Methods

    public long getSolarSailToDelete(){
        return Math.max(0, this.amountDSPSolarSail - this.amountDSPNode * solarSailCanHoldPerNode - solarSailCanHoldDefault );
    }

    /**
     * @return Remaining Available DSP Power Points
     */
    public long getDSPPowerPointCanUse(){
        if (useBigInteger) {
            BigInteger canUse = getDSPPowerPointCanUseBigInteger();
            if (canUse.compareTo(LongMaxValue_BigInteger) >= 0) {
                return Long.MAX_VALUE;
            } else {
                return canUse.longValue();
            }
        }
        return this.getMaxDSPPowerPoint() - this.usedDSPPowerPoint;
    }

    public BigInteger getDSPPowerPointCanUseBigInteger() {
        return maxDSPPowerPoint_BigInteger.add(BigInteger.valueOf(-1).multiply(usedDSPPowerPoint_BigInteger));
    }

    /**
     * Try to decrease the DSP Power Points used part amount.
     * @param amount    The <b style:"color=#00FF00">amount</b>.
     * @return          True means ok; <p>False means something was error(example: Solar Sail amount has been decreased.).
     */
    public boolean tryDecreaseUsedPowerPoint(long amount){
        if (useBigInteger) {
            BigInteger amountBig = BigInteger.valueOf(amount);
            if (this.usedDSPPowerPoint_BigInteger.compareTo(amountBig) >= 0) {
                this.markDirty();
                markDataDirty();
                usedDSPPowerPoint_BigInteger = usedDSPPowerPoint_BigInteger.add(amountBig.multiply(NEGATIVE_ONE));
                return true;
            } else {
                TwistSpaceTechnology.LOG.info("Error ! Trying decrease an amount larger than used DSP Power Point !");
                TwistSpaceTechnology.LOG.info("Trying amount: "+amount+" ; Used point: "+this.usedDSPPowerPoint_BigInteger.toString()+" ;");
                TwistSpaceTechnology.LOG.info("Please check your Dyson Sphere Program information use command: /tst dsp_check ");
                return false;
            }
        }

        if (this.usedDSPPowerPoint >= amount){
            this.markDirty().usedDSPPowerPoint -= amount;
            markDataDirty();
            return true;
        }
        TwistSpaceTechnology.LOG.info("Error ! Trying decrease an amount larger than used DSP Power Point !");
        TwistSpaceTechnology.LOG.info("Trying amount: "+amount+" ; Used point: "+this.usedDSPPowerPoint+" ;");
        TwistSpaceTechnology.LOG.info("Please check your Dyson Sphere Program information use command: /tst dsp_check ");
        return false;
    }

    /**
     * Try to request using DSP Power Points.
     *
     * @param amount    The points request.
     * @return          True means success; <p>False means nothing happened, request failed.</p>
     */
    public boolean tryUsePowerPoint(long amount){
        // TODO add Big Integer calculation
        if (this.canUsePowerPoint(amount)){
            markDataDirty();
            this.markDirty();
            if (useBigInteger) {
                this.usedDSPPowerPoint_BigInteger = this.usedDSPPowerPoint_BigInteger.add(BigInteger.valueOf(amount));
            } else {
                this.usedDSPPowerPoint += amount;
            }
            return true;
        }
        return false;
    }

    /**
     *
     * @param amount    The trying amount.
     * @return          False means don't do.
     */
    public boolean canUsePowerPoint(long amount){
        return amount <= this.getDSPPowerPointCanUse();
    }

    @SuppressWarnings("unsafe")
    public boolean decreaseUsedPowerPointUnsafely(long amount){
        markDataDirty();
        if (tryDecreaseUsedPowerPoint(amount)){
            return true;
        }
        this.markDirty().usedDSPPowerPoint -= amount;
        return false;
    }

    @SuppressWarnings("unsafe")
    public DSP_DataCell setUsedPowerPointUnsafely(long amount){
        this.markDirty().usedDSPPowerPoint = amount;
        markDataDirty();
        TwistSpaceTechnology.LOG.info("Set 0 to UsedPowerPoint Unsafely at: "+this);
        return this;
    }

    @SuppressWarnings("unsafe")
    public DSP_DataCell setUsedPowerPointUnsafely(BigInteger amount) {
        this.markDirty().usedDSPPowerPoint_BigInteger = amount;
        markDataDirty();
        TwistSpaceTechnology.LOG.info("Set 0 to UsedPowerPoint Unsafely at: "+this);
        return this;
    }

    /**
     * MaxPowerPoint = <p>PowerPointPerSolarSail * SolarSail * (Node + 1)^0.8
     */

    public DSP_DataCell flushMaxDSPPowerPoint(){
        this.markDirty().cancelDirty();
        if (useBigInteger) {
            // big integer mode
            flushMaxDSPPowerPointBigInteger();
        } else {
            if (shouldUseBigInteger()) {
                // turn to big integer mode
                turnToBigIntegerMode();
            } else {
                // normal mode
                flushMaxDSPPowerPointNormal();
            }
        }
        return this;
    }

    public DSP_DataCell turnToBigIntegerMode() {
        useBigInteger = true;
        flushMaxDSPPowerPointBigInteger();
        usedDSPPowerPoint_BigInteger = BigInteger.valueOf(usedDSPPowerPoint);
        return this;
    }

    public DSP_DataCell flushMaxDSPPowerPointNormal() {
        this.setMaxDSPPowerPoint(
            (long) (
                solarSailPowerPoint
                    * this.amountDSPSolarSail
                    * Math.pow(this.amountDSPNode + 1, 0.8)));
        return this;
    }

    public DSP_DataCell flushMaxDSPPowerPointBigInteger() {
        this.setMaxDSPPowerPoint(
            BigInteger.valueOf(amountDSPSolarSail)
                .multiply(Config.solarSailPowerPoint_BigInteger)
                .multiply(BigInteger.valueOf((long) Math.ceil(Math.pow(this.amountDSPNode + 1, 0.8))))
        );
        return this;
    }

    public boolean shouldUseBigInteger() {
        if (amountDSPSolarSail < 1 || amountDSPNode < 1) return false;
        return Long.MAX_VALUE / solarSailPowerPoint / amountDSPSolarSail - 1 <= Math.pow(this.amountDSPNode + 1, 0.8);
    }

    public long getMaxDSPPowerPoint() {
        if (this.dirty) flushMaxDSPPowerPoint();
        if (useBigInteger) {
            if (maxDSPPowerPoint_BigInteger.compareTo(LongMaxValue_BigInteger) >= 0) {
                return Long.MAX_VALUE;
            } else {
                return maxDSPPowerPoint_BigInteger.longValue();
            }
        } else {
            return this.maxDSPPowerPoint;
        }
    }

    public DSP_DataCell addDSPSolarSail(long amount) {
        this.amountDSPSolarSail += amount;
        this.markDirty().flushMaxDSPPowerPoint();
        markDataDirty();
        return this;
    }

    public DSP_DataCell addDSPNode(long amount) {
        this.amountDSPNode += amount;
        this.markDirty().flushMaxDSPPowerPoint();
        markDataDirty();
        return this;
    }

    public DSP_DataCell setMaxDSPPowerPoint(long amount){
        this.markDirty().maxDSPPowerPoint = amount;
        markDataDirty();
        return this;
    }

    public DSP_DataCell setMaxDSPPowerPoint(BigInteger amount) {
        this.markDirty().maxDSPPowerPoint_BigInteger = amount;
        markDataDirty();
        return this;
    }

    public DSP_DataCell markDirty(){
        this.dirty = true;
        this.dataSyncFlag++;
        if (this.dataSyncFlag > 64) this.dataSyncFlag = 1;
        return this;
    }

    public DSP_DataCell cancelDirty(){
        this.dirty = false;
        return this;
    }

    public byte getDataSyncFlag(){
        return this.dataSyncFlag;
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
                   + " , maxDSPPowerPoint:"
                   + (useBigInteger ? GT_Utility.formatNumbers(maxDSPPowerPoint_BigInteger) : maxDSPPowerPoint)
                   + " , usedDSPPowerPoint:"
                   + (useBigInteger ? GT_Utility.formatNumbers(usedDSPPowerPoint_BigInteger) : usedDSPPowerPoint)
                   + " , PowerPointCanUse: "
                   + (useBigInteger ? GT_Utility.formatNumbers(getDSPPowerPointCanUseBigInteger())  : getDSPPowerPointCanUse())
                   + (useBigInteger ? " , "+EnumChatFormatting.RED+"Using Big Integer Calculation"+EnumChatFormatting.RESET : "")
                   + " }";
    }

    // endregion

    // region Getters and Setters

    public String getOwnerName() {
        return ownerName;
    }

    public DSP_DataCell setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        markDataDirty();
        return this;
    }

    public DSP_Galaxy getGalaxy() {
        return galaxy;
    }

    public DSP_DataCell setGalaxy(DSP_Galaxy galaxy) {
        this.galaxy = galaxy;
        markDataDirty();
        return this;
    }

    public long getDSPSolarSail() {
        return amountDSPSolarSail;
    }

    public DSP_DataCell setDSPSolarSail(long amountDSPSolarSail) {
        this.amountDSPSolarSail = amountDSPSolarSail;
        this.flushMaxDSPPowerPoint();
        markDataDirty();
        return this;
    }

    public long getDSPNode() {
        return amountDSPNode;
    }

    public DSP_DataCell setDSPNode(long amountDSPNode) {
        this.amountDSPNode = amountDSPNode;
        this.flushMaxDSPPowerPoint();
        markDataDirty();
        return this;
    }

    // endregion

    // spotless:on
}
