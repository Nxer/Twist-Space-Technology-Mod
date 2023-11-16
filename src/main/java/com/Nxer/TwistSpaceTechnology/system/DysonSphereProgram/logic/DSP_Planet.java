package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

// spotless:off

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy.*;
// spotless:on

/**
 * Contains Planets(Dimensions) with properties.
 */
public enum DSP_Planet {

    // SolarSystem
    OverWorld(0, 1, SolarSystem),
    TheEnd(1, 1.01, SolarSystem),
    Miranda(86, 1, SolarSystem),
    Haumea(83, 1, SolarSystem),
    Pluto(49, 1, SolarSystem),
    Triton(48, 1, SolarSystem),
    Proteus(47, 1, SolarSystem),
    Oberon(46, 1, SolarSystem),
    Callisto(45, 1, SolarSystem),
    Titan(44, 1, SolarSystem),
    Ganymede(43, 1, SolarSystem),
    Ceres(42, 1, SolarSystem),
    Enceladus(41, 1, SolarSystem),
    Deimos(40, 1, SolarSystem),
    Venus(39, 1, SolarSystem),
    Phobos(38, 1, SolarSystem),
    Mercury(37, 1, SolarSystem),
    Io(36, 1, SolarSystem),
    Europa(35, 1, SolarSystem),
    KuiperBelt(33, 1, SolarSystem),
    Asteroids(30, 1, SolarSystem),
    Mars(29, 1, SolarSystem),
    Moon(28, 1, SolarSystem),
    Makemake(25, 1, SolarSystem),
    TwilightForest(7, 1, SolarSystem),

    // Ross128
    Ross128b(64, 1, Ross128),
    Ross128ba(63, 1, Ross128),

    // Barnarda
    BarnardaF(82, 1, Barnarda),
    BarnardaE(81, 1, Barnarda),
    BarnardaC(32, 1, Barnarda),

    // Vega
    VegaB(84, 1, Vega),

    // TCeti
    TCetiE(85, 1, TCeti),

    // CentauriB
    CentauriBb(31, 1, CentauriB),

    // AmunRa
    Seth(94, 1, AmunRa),
    Horus(93, 1, AmunRa),
    Anubis(92, 1, AmunRa),
    Maahes(91, 1, AmunRa),
    Neper(90, 1, AmunRa),
    MehenBelt(95, 1, AmunRa),

    ;

    public final int dimensionID;
    public final double planetaryCoefficient;
    private final DSP_Galaxy dependentGalaxy;

    DSP_Planet(int dimID, double planetaryCoefficient, DSP_Galaxy dependentGalaxy) {
        this.dimensionID = dimID;
        this.planetaryCoefficient = planetaryCoefficient;
        this.dependentGalaxy = dependentGalaxy;
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

}
