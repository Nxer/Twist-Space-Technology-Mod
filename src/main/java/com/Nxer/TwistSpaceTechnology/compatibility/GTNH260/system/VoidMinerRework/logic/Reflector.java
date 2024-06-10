package com.Nxer.TwistSpaceTechnology.compatibility.GTNH260.system.VoidMinerRework.logic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.crossmod.galacticgreg.GT_TileEntity_VoidMiner_Base;
import com.google.common.collect.ArrayListMultimap;

public class Reflector {

    private final static Method getExtraDropsDimMapMethod;
    static {
        try {
            Class<GT_TileEntity_VoidMiner_Base> clazz = GT_TileEntity_VoidMiner_Base.class;
            getExtraDropsDimMapMethod = clazz.getMethod("getExtraDropsDimMap");
            getExtraDropsDimMapMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            TwistSpaceTechnology.LOG.warn(
                "Method com.github.bartimaeusnek.crossmod.galacticgreg.GT_TileEntity_VoidMiner_Base.getExtraDropsDimMap() of Bartwork could not be found");
            throw new RuntimeException(e);
        }
    }

    public static ArrayListMultimap<Integer, Pair<Pair<Integer, Boolean>, Float>> getExtraDropsDimMap() {
        try {
            return (ArrayListMultimap<Integer, Pair<Pair<Integer, Boolean>, Float>>) getExtraDropsDimMapMethod
                .invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
