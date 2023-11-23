package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic_t;

import java.util.HashMap;
import java.util.Map;

public class Planet {

    private final static Map<Integer, Planet> registeredPlanets = new HashMap<>();
    private final static Planet Unknown = new Planet();

    public final double planetaryCoefficient;
    public final Galaxy dependentGalaxy;
    public final int dimensionID;

    private Planet() {
        dependentGalaxy = Galaxy.Unknown;
        planetaryCoefficient = 0;
        dimensionID = 0;
    }

    public Planet(int dimID, double planetaryCoefficient, Galaxy dependentGalaxy) {
        dimensionID = dimID;
        this.planetaryCoefficient = planetaryCoefficient;
        this.dependentGalaxy = dependentGalaxy;
        registeredPlanets.put(dimID, this);
    }

    public static Planet getPlanetFromDimID(int dimID) {
        if (dimID >= 180 && dimID != 227) dimID = 0;
        Planet planet = registeredPlanets.get(dimID);
        return planet == null ? Unknown : planet;
    }

    public static double getPlanetaryCoefficientWithDimID(int dimID) {
        return getPlanetFromDimID(dimID).getDimensionID();
    }

    public Galaxy getDependentGalaxy() {
        return dependentGalaxy;
    }

    public double getPlanetaryCoefficient() {
        return planetaryCoefficient;
    }

    public int getDimensionID() {
        return dimensionID;
    }
}
