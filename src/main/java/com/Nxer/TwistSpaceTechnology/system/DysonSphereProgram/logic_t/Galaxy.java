package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic_t;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagList;

public class Galaxy {

    public static final Galaxy Unknown = new Galaxy() {

        @Override
        public DysonSphereSystem getOrCreatDysonSphereSystem(String teamName) {
            return DysonSphereSystem.Unknown;
        }
    };

    private static final Map<String, Galaxy> galaxyMap = new HashMap<>();
    public final String name;
    private final double stellarCoefficient;
    private final Map<String, DysonSphereSystem> createdSystem = new HashMap<>();

    private Galaxy() {
        this("null", 0);
    }

    public Galaxy(String name, double stellarCoefficient) {
        this.stellarCoefficient = stellarCoefficient;
        this.name = name;
        galaxyMap.put(name, this);
    }

    public static Galaxy getGalaxyFromName(String name) {
        Galaxy galaxy = galaxyMap.get(name);
        return galaxy == null ? Unknown : galaxy;
    }

    public static void readFromNBT(NBTTagList galaxyList) {
        galaxyMap.values()
            .forEach(Galaxy::clear);
        int count = galaxyList.tagCount();
        while (count-- > 0) {
            DysonSphereSystem system = DysonSphereSystem.readFromNBT(galaxyList.getCompoundTagAt(count));
            if (system == DysonSphereSystem.Unknown) continue;
            system.getGalaxyIn().createdSystem.put(system.getOwnerTeam(), system);
        }
    }

    public static NBTTagList writeToNBT() {
        NBTTagList list = new NBTTagList();
        galaxyMap.values()
            .forEach(
                galaxy -> galaxy.createdSystem.values()
                    .stream()
                    .map(DysonSphereSystem::writeToNBT)
                    .forEach(list::appendTag));
        return list;
    }

    public static void foreach(Consumer<DysonSphereSystem> func) {
        galaxyMap.values()
            .forEach(galaxy -> galaxy.eachSystem(func));
    }

    public static Galaxy getGalaxyFromDimID(int dimID) {
        return Planet.getPlanetFromDimID(dimID)
            .getDependentGalaxy();
    }

    private void eachSystem(Consumer<DysonSphereSystem> func) {
        createdSystem.values()
            .forEach(func);
    }

    private void clear() {
        createdSystem.clear();
    }

    public boolean has(String teamName) {
        return createdSystem.containsKey(teamName);
    }

    void addSystem(DysonSphereSystem system) {
        createdSystem.put(system.getOwnerTeam(), system);
        DysonSphereManager.dirty();
    }

    public DysonSphereSystem getOrCreatDysonSphereSystem(String teamName) {
        return has(teamName) ? createdSystem.get(teamName) : new DysonSphereSystem(teamName, this);
    }

    public double getStellarCoefficient() {
        return stellarCoefficient;
    }
}
