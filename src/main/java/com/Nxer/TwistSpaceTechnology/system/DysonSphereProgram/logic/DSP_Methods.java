package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DSP_TeamName;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DysonSpheres;

import java.util.HashMap;

import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;

public final class DSP_Methods {

    public static int getDimID(IHasWorldObjectAndCoords aBaseMetaTileEntity) {
        return aBaseMetaTileEntity.getWorld().provider.dimensionId;
    }

    public static DSP_DataCell initDSP_Data(String userName, int dimID) {

        // If dsp system team has not been created or player was not in a team, create it

        if (!DSP_TeamName.containsKey(userName)) {
            // if user was not in a team, create and join a new.
            DSP_TeamName.put(userName, userName);
        }

        // If something has not been set, set it.
        String teamName = DSP_TeamName.get(userName);
        DSP_Galaxy currentGalaxy = DSP_Galaxy.getGalaxyFromDimID(dimID);

        // Check Dyson Sphere Map
        if (!DysonSpheres.containsKey(teamName)) {
            HashMap<DSP_Galaxy, DSP_DataCell> DysonSphereMap = new HashMap<>() {

                {
                    put(currentGalaxy, new DSP_DataCell(teamName, currentGalaxy));
                }
            };
            DysonSpheres.put(teamName, DysonSphereMap);
        }

        HashMap<DSP_Galaxy, DSP_DataCell> currentDysonSphereMap = DysonSpheres.get(teamName);

        // Check current galaxy's Dyson Sphere
        if (!currentDysonSphereMap.containsKey(currentGalaxy)) {
            currentDysonSphereMap.put(currentGalaxy, new DSP_DataCell(teamName, currentGalaxy));
        }

        // Check everything is ok, return the Data Cell
        return currentDysonSphereMap.get(currentGalaxy);
    }

}
