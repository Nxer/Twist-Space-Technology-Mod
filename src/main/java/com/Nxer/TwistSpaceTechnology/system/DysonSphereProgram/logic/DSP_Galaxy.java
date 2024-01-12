package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;
// spotless:off

import java.io.Serializable;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Anubis;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Asteroids;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.BarnardaC;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.BarnardaE;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.BarnardaF;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Callisto;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.CentauriBb;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Ceres;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.DSP_Planet_NULL;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Deimos;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Enceladus;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Europa;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Ganymede;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Haumea;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Horus;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Io;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.KuiperBelt;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Maahes;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Makemake;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Mars;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.MehenBelt;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Mercury;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Miranda;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Moon;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Neper;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Oberon;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.OverWorld;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Phobos;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Pluto;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Proteus;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Ross128b;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Ross128ba;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Seth;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.TCetiE;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.TheEnd;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Titan;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Triton;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.TwilightForest;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.VegaB;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.Venus;


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
