package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.DSP_Planet.OverWorld;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.DSP_Planet.Seth;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.DSP_Planet.TheEnd;

public enum DSP_Galaxy {

    _NULL(0),
    SolarSystem(1, OverWorld, TheEnd),
    Ross128(0.2),
    Barnarda(0.851),
    Vega(2.98),
    Tceti(1.9),
    CentauriB(2.3),
    AmunRa(0.9, Seth)

    ;

    public final DSP_Planet[] containedPlanets;
    public final double stellarCoefficient;

    DSP_Galaxy(double stellarCoefficient, DSP_Planet... containedPlanets) {
        this.stellarCoefficient = stellarCoefficient;
        this.containedPlanets = containedPlanets;
    }

    public DSP_Planet[] getContainedDimensions() {
        return containedPlanets;
    }

    public boolean isContainedDim(int dimID) {
        for (DSP_Planet dim : containedPlanets) {
            if (dim.dimensionID == dimID) return true;
        }
        return false;
    }

    public static DSP_Galaxy getGalaxyFromDimID(int dimID) {
        if (dimID >= 180 && dimID != 227) return SolarSystem;

        for (DSP_Galaxy galaxy : DSP_Galaxy.values()) {
            if (galaxy.isContainedDim(dimID)) return galaxy;
        }

        return _NULL;
    }

}
