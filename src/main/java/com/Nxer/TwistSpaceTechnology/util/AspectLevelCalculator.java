package com.Nxer.TwistSpaceTechnology.util;

import java.util.HashMap;
import java.util.Map;

import thaumcraft.api.aspects.Aspect;

public class AspectLevelCalculator {

    static Map<String, Integer> aspectLevels = new HashMap<>();

    public static int computeAspectLevel(Aspect aspect, Map<String, Integer> aspectLevels) {
        String aspectName = aspect.getTag();
        if (aspectLevels.containsKey(aspectName)) {
            return aspectLevels.get(aspectName);
        }
        if (aspect.isPrimal()) {
            aspectLevels.put(aspectName, 1);
            return 1;
        }
        Aspect[] components = aspect.getComponents();
        if (components == null || components.length != 2) {
            aspectLevels.put(aspectName, 1);
            return 1;
        }
        int level1 = computeAspectLevel(components[0], aspectLevels);
        int level2 = computeAspectLevel(components[1], aspectLevels);
        int finalLevel = (level1 == level2) ? level1 + 1 : Math.max(level1, level2);
        aspectLevels.put(aspectName, finalLevel);
        return finalLevel;
    }

    public static int computeAspectLevel(Aspect aspect) {
        return computeAspectLevel(aspect, aspectLevels);
    }
}
