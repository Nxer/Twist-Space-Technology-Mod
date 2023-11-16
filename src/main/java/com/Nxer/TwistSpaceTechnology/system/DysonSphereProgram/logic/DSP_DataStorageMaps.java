package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import java.util.HashMap;
import java.util.Map;

public abstract class DSP_DataStorageMaps {

    /**
     * Maps Dyson Sphere Objects with Team Name.
     * <li>Key: Team Name
     * <li>Value: Dyson Sphere Object of Galaxy
     */
    public static Map<String, HashMap<DSP_Galaxy, DSP_DataCell>> DysonSpheres = new HashMap<>(30, 0.9f);

    /**
     * Maps of Team Name and Member Name or UUID.
     * <li>Key: Member name and UUID, two keys point to one value</li>
     * <li>Value: Team Name</li>
     */
    public static Map<String, String> DSP_TeamName = new HashMap<>(60, 0.9f);

    /**
     * Maps of Name-UUID and UUID-Name
     */
    public static Map<String, String> UUID_Name = new HashMap<>(60, 0.9f);

}
