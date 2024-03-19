package com.Nxer.TwistSpaceTechnology.system.VoidMinerRework.logic;

import java.util.HashMap;
import java.util.Map;

import com.github.bartimaeusnek.bartworks.util.Pair;

public class VoidMinerData {

    /**
     * Dim ID -> Drop Map
     */
    public static Map<Integer, Map<Pair<Integer, Boolean>, Float>> OrePool = new HashMap<>();
    public static Map<Integer, Float> OrePoolTotalWeightPool = new HashMap<>();

}
