package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DSP_TeamName;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DysonSpheres;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.UUID_Name;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet.getPlanetFromDimID;

import java.util.HashMap;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;

public interface IDSP_IO {

    default void joinUserTeam(String userUUID, String userUUIDTeam) {
        DSP_TeamName.put(userUUID, getOrInitTeamName(userUUIDTeam));
    }

    /**
     * Init a Dyson Sphere Data Cell with team name and current dimension ID.
     *
     * @param userName The team name of Data Cell.
     * @param dimID    The dimension ID of Data Cell.
     * @return The Data Cell.
     */
    default DSP_DataCell getOrInitDSPData(String userName, int dimID) {

        // If dsp system team has not been created or player was not in a team, create it

        // If something has not been set, set it.
        String teamName = getOrInitTeamName(userName);
        DSP_Galaxy currentGalaxy = DSP_Galaxy.getGalaxyFromDimID(dimID);

        // Check Dyson Sphere Map
        if (!DysonSpheres.containsKey(teamName)) {
            HashMap<DSP_Galaxy, DSP_DataCell> DysonSphereMap = new HashMap<>();
            DysonSpheres.put(teamName, DysonSphereMap);
            dataMarkDirty();
        }

        HashMap<DSP_Galaxy, DSP_DataCell> currentDysonSphereMap = DysonSpheres.get(teamName);

        // Check current galaxy's Dyson Sphere
        if (!currentDysonSphereMap.containsKey(currentGalaxy)) {
            DSP_DataCell dataCell = new DSP_DataCell(teamName, currentGalaxy);
            currentDysonSphereMap.put(currentGalaxy, dataCell);
            dataMarkDirty();
        }

        // Check everything is ok, return the Data Cell
        return currentDysonSphereMap.get(currentGalaxy);
    }

    /**
     * Get DSP Data Cell by a unsafe way. Make sure you have checked everything is ok.
     *
     * @param teamName The team name of data cell you want to get.
     * @param dimID    The dimension ID of dyson sphere.
     * @return The DSP Data Cell.
     */
    default DSP_DataCell getDSPDataCellWithoutCheck(String teamName, int dimID) {
        return DysonSpheres.get(teamName)
            .get(getPlanetFromDimID(dimID).dependentGalaxy);
    }

    default DSP_DataCell setDSPSolarSail(long amountSolarSail, String teamName, int dimID) {
        return getDSPDataCellWithoutCheck(teamName, dimID).setDSPSolarSail(amountSolarSail);
    }

    default DSP_DataCell setDSPSolarSail(long amountSolarSail, DSP_DataCell dataCell) {
        return dataCell.setDSPSolarSail(amountSolarSail);
    }

    default DSP_DataCell setDSPNode(long amountNode, String teamName, int dimID) {
        return getDSPDataCellWithoutCheck(teamName, dimID).setDSPNode(amountNode);
    }

    default DSP_DataCell setDSPNode(long amountNode, DSP_DataCell dataCell) {
        return dataCell.setDSPNode(amountNode);
    }

    default DSP_DataCell addDSPSolarSail(long amountSolarSail, String teamName, int dimID) {
        return getDSPDataCellWithoutCheck(teamName, dimID).addDSPSolarSail(amountSolarSail);
    }

    default int getDimID(IHasWorldObjectAndCoords aBaseMetaTileEntity) {
        return aBaseMetaTileEntity.getWorld().provider.dimensionId;
    }

    default String getOwnerNameAndInitMachine(IGregTechTileEntity aBaseMetaTileEntity) {
        final String ownerName = aBaseMetaTileEntity.getOwnerName();
        final String ownerUUID = aBaseMetaTileEntity.getOwnerUuid()
            .toString();
        if (UUID_Name.isEmpty() || !UUID_Name.containsKey(ownerName) || !UUID_Name.containsKey(ownerUUID)) {
            UUID_Name.put(ownerName, ownerUUID);
            UUID_Name.put(ownerUUID, ownerName);
            dataMarkDirty();
        }
        return ownerName;
    }

    default String getOrInitTeamName(String aUserName) {
        // get
        if (DSP_TeamName.containsKey(aUserName)) {
            return DSP_TeamName.get(aUserName);
        }

        // init
        DSP_TeamName.put(aUserName, aUserName);
        dataMarkDirty();
        return aUserName;

    }

    default void dataMarkDirty() {
        try {
            DSP_WorldSavedData.INSTANCE.markDirty();
        } catch (Exception e) {
            TwistSpaceTechnology.LOG.info("try DSP_WorldSavedData.INSTANCE.markDirty() FAILED!");
            e.printStackTrace();
        }
    }

}
