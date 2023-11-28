package com.Nxer.TwistSpaceTechnology.common.TechTree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public class TechMap implements Serializable {

    private HashMap<Integer, Technology> techMap = new HashMap<>();

    private Integer techId = 0;

    private static HashMap<UUID, TechMap> playerTeamTechMap = new HashMap<>();

    public void addTech(Technology tech) {
        techId++;
        tech.id = techId;
        techMap.put(techId, tech);
    }

    public void addDependency(Integer tech, Integer dependency) {
        if (checkLoop(tech, dependency)) {
            return;
        }
        techMap.get(tech)
            .setDependency(dependency);
    }

    public void addDependency(Technology tech, Technology dependency) {
        addDependency(tech.id, dependency.id);
    }

    // check if the technology can be researched or not
    public boolean checkValid(Integer tech) {
        for (var dependency : techMap.get(tech).dependency) {
            if (!techMap.get(dependency).isResearched) {
                return false;
            }
        }
        return true;
    }

    // check if the technology (will) have loop dependency or not
    public boolean checkLoop(Integer tech, Integer dependency) {
        for (var preDependency : techMap.get(dependency).dependency) {
            if (Objects.equals(preDependency, tech) || checkLoop(tech, preDependency)) {
                return true;
            }
        }
        return false;
    }

    public static void initTechMap(EntityPlayer aPlayer) {
        var uuid = aPlayer.getUniqueID();
        if (playerTeamTechMap.containsKey(uuid)) {
            return;
        }
        var map = new TechMap();
        for (var tech : TechPool.values()) {
            map.addTech(tech.tech);
        }
        playerTeamTechMap.put(uuid, map);

    }

}
