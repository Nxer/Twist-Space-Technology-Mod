package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;
// spotless:off

import java.io.Serializable;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.*;
// spotless:on
public enum DSP_Galaxy implements Serializable {

    SolarSystem(1, OverWorld, TheEnd, Miranda, Haumea, Pluto, Triton, Proteus, Oberon, Callisto, Titan, Ganymede, Ceres,
        Enceladus, Deimos, Venus, Phobos, Mercury, Io, Europa, KuiperBelt, Asteroids, Mars, Moon, Makemake,
        TwilightForest),
    Ross128(0.4, Ross128b, Ross128ba),
    Barnarda(0.851, BarnardaC, BarnardaE, BarnardaF),
    Vega(2.98, VegaB),
    TCeti(1.9, TCetiE),
    CentauriB(2.3, CentauriBb),
    AmunRa(0.9, Seth, Horus, Anubis, Maahes, Neper, MehenBelt),
    DSP_Galaxy_NULL(0, DSP_Planet_NULL)

    ;

    public final DSP_Planet[] containedPlanets;
    public final double stellarCoefficient;

    DSP_Galaxy(double stellarCoefficient, DSP_Planet... containedPlanets) {
        this.stellarCoefficient = stellarCoefficient;
        this.containedPlanets = containedPlanets;
    }

    DSP_Galaxy() {
        this.containedPlanets = new DSP_Planet[] { OverWorld };
        this.stellarCoefficient = 0;
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

        return DSP_Galaxy_NULL;
    }

}
