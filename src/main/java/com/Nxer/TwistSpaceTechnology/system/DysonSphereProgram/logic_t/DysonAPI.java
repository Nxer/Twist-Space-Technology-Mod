package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic_t;

import net.minecraft.tileentity.TileEntity;

public class DysonAPI {

    public static boolean userHasTeam(String userName) {
        return TeamManager.userHasTeam(userName);
    }

    public static boolean joinUserTeam(String userName, String teamName) {
        return TeamManager.joinUserTeam(userName, teamName);
    }

    public static String getOrInitTeamName(String aUserName) {
        return TeamManager.getOrCreatTeam(aUserName);
    }

    public static int getDimensionID(TileEntity tileEntity) {
        return tileEntity.getWorldObj().provider.dimensionId;
    }

    public static DysonSphereSystem getOrCreatDysonSphereSystem(String userName, int dimID) {
        String teamName = TeamManager.getOrCreatTeam(userName);
        Galaxy currentGalaxy = Galaxy.getGalaxyFromDimID(dimID);
        return currentGalaxy.getOrCreatDysonSphereSystem(teamName);
    }

    public static DysonSphereSystem addSolarSails(long amount, String teamName, int dimID) {
        return getOrCreatDysonSphereSystem(teamName, dimID).addSail(amount);
    }

    public static DysonSphereSystem setSolarSails(long amount, String teamName, int dimID) {
        return getOrCreatDysonSphereSystem(teamName, dimID).setSolarSails(amount);
    }

    public static DysonSphereSystem setNodes(long amount, String teamName, int dimID) {
        return getOrCreatDysonSphereSystem(teamName, dimID).setNodes(amount);
    }

    public static void markDirty() {
        DysonSphereManager.dirty();
    }
}
