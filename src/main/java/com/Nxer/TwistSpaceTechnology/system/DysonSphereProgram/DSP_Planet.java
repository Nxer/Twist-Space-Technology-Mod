package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.DSP_Galaxy.AmunRa;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.DSP_Galaxy.SolarSystem;

/**
 * Contains Planets(Dimensions) with properties.
 */
public enum DSP_Planet {

    // SolarSystem
    OverWorld(0, 1, SolarSystem),
    TheEnd(1, 1.01, SolarSystem),

    Miranda(86, 1, SolarSystem),

    // Ross128

    // Barnarda

    // Vega

    // Tceti

    // CentauriB

    // AmunRa
    Seth(94, 1, AmunRa),
    Horus(93, 1, AmunRa),
    Anubis(92, 1, AmunRa),
    Maahes(91, 1, AmunRa),
    Neper(90, 1, AmunRa),

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
