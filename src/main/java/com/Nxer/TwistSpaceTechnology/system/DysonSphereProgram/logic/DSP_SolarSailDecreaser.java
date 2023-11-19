package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DysonSpheres;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import gregtech.api.objects.XSTR;

public class DSP_SolarSailDecreaser {

    public static DSP_SolarSailDecreaser INSTANCE;
    private final Set<DSP_DataCell> dataCell = new HashSet<>();
    private int catchedAmountDSP;

    public DSP_SolarSailDecreaser() {
        reloadDSPs();
    }

    private int tick = 0;

    /**
     * Init this INSTANCE when loading saves.
     */
    public static void init() {
        INSTANCE = new DSP_SolarSailDecreaser();
    }

    /**
     * Called every tick.
     */
    public void onTick() {
        this.tick++;

        if (tick >= 20 * 1800) {
            this.checkFlushed()
                .tryDecrease();
        }
    }

    private void tryDecrease() {
        this.tick = 0;
        if (1 == XSTR.XSTR_INSTANCE.nextInt(5000)) {
            for (DSP_DataCell dsp : dataCell) {
                if (1 == XSTR.XSTR_INSTANCE.nextInt(4)) {
                    decreaseSolarSail(dsp);
                }
            }
        }
    }

    private void decreaseSolarSail(DSP_DataCell dsp) {
        if (dsp.getSolarSailToDelete() < 1) return;
        dsp.addDSPSolarSail(dsp.getSolarSailToDelete() / 2);
    }

    public DSP_SolarSailDecreaser checkFlushed() {
        int sizeSum = 0;
        for (HashMap<DSP_Galaxy, DSP_DataCell> dsps : DysonSpheres.values()) {
            sizeSum += dsps.size();
        }
        if (!(sizeSum == catchedAmountDSP)) {
            TwistSpaceTechnology.LOG.info("Flush DSP SolarSailDecreaser.");
            reloadDSPs();
            this.catchedAmountDSP = sizeSum;
        }
        return this;
    }

    public void reloadDSPs() {
        for (HashMap<DSP_Galaxy, DSP_DataCell> DSPs : DysonSpheres.values()) {
            this.dataCell.addAll(DSPs.values());
        }
    }

}
