package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

// spotless:off

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import java.io.Serializable;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy.AmunRa;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy.Barnarda;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy.CentauriB;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy.DSP_Galaxy_NULL;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy.Ross128;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy.SolarSystem;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy.TCeti;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy.Vega;


// spotless:on

/**
 * Contains Planets(Dimensions) with properties.
 */
public enum DSP_Planet implements Serializable {

    // SolarSystem
    OverWorld(0, 1, SolarSystem),
    TheEnd(1, 1.07, SolarSystem),
    Miranda(86, 0.45, SolarSystem),
    Haumea(83, 0.18, SolarSystem),
    Pluto(49, 0.2, SolarSystem),
    Triton(48, 0.35, SolarSystem),
    Proteus(47, 0.33, SolarSystem),
    Oberon(46, 0.44, SolarSystem),
    Callisto(45, 0.67, SolarSystem),
    Titan(44, 0.56, SolarSystem),
    Ganymede(43, 0.66, SolarSystem),
    Ceres(42, 0.7, SolarSystem),
    Enceladus(41, 0.55, SolarSystem),
    Deimos(40, 1.01, SolarSystem),
    Venus(39, 1.6, SolarSystem),
    Phobos(38, 1.01, SolarSystem),
    Mercury(37, 2.3, SolarSystem),
    Io(36, 0.66, SolarSystem),
    Europa(35, 0.66, SolarSystem),
    KuiperBelt(33, 0.1, SolarSystem),
    Asteroids(30, 0.7, SolarSystem),
    Mars(29, 1.01, SolarSystem),
    Moon(28, 1.3, SolarSystem),
    Makemake(25, 0.09, SolarSystem),
    TwilightForest(7, 1, SolarSystem),

    // Ross128
    Ross128b(64, 1, Ross128),
    Ross128ba(63, 1.3, Ross128),

    // Barnarda
    BarnardaF(82, 0.8, Barnarda),
    BarnardaE(81, 0.9, Barnarda),
    BarnardaC(32, 1.5, Barnarda),

    // Vega
    VegaB(84, 2.6, Vega),

    // TCeti
    TCetiE(85, 1.1, TCeti),

    // CentauriB
    CentauriBb(31, 2.2, CentauriB),

    // AmunRa
    Seth(94, 0.79, AmunRa),
    Horus(93, 2.9, AmunRa),
    Anubis(92, 0.66, AmunRa),
    Maahes(91, 0.77, AmunRa),
    Neper(90, 1.15, AmunRa),
    MehenBelt(95, 0.8, AmunRa),

    //
    DSP_Planet_NULL(-114, 0, DSP_Galaxy_NULL),;

    public int dimensionID;
    public final double planetaryCoefficient;
    public final DSP_Galaxy dependentGalaxy;

    DSP_Planet(int dimID, double planetaryCoefficient, DSP_Galaxy dependentGalaxy) {
        this.dimensionID = dimID;
        this.planetaryCoefficient = planetaryCoefficient;
        this.dependentGalaxy = dependentGalaxy;
    }

    public DSP_Planet setDimensionID(int dimID) {
        this.dimensionID = dimID;
        return this;
    }

    public static double getPlanetaryCoefficientWithDimID(int dimID) {
        if (dimID >= 180 && dimID != 227) return OverWorld.planetaryCoefficient;
        for (DSP_Planet planet : DSP_Planet.values()) {
            if (planet.dimensionID == dimID) {
                return planet.planetaryCoefficient;
            }
        }
        return 0;
    }

    public static DSP_Planet getPlanetFromDimID(int dimID) {
        if (dimID >= 180 && dimID != 227) return OverWorld;
        for (DSP_Planet planet : DSP_Planet.values()) {
            if (planet.dimensionID == dimID) {
                return planet;
            }
        }
        return DSP_Planet_NULL;
    }

    public static DSP_Galaxy getGalaxyFromDimID(int dimID) {
        TwistSpaceTechnology.LOG.info(getPlanetFromDimID(dimID).dependentGalaxy);
        return getPlanetFromDimID(dimID).dependentGalaxy;
    }

    public static void readDimIDFromGSCfg() {

    }

}
