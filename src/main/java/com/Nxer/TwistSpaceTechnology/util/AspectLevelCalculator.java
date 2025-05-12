package com.Nxer.TwistSpaceTechnology.util;

import java.util.HashMap;
import java.util.Map;

import thaumcraft.api.aspects.Aspect;

public class AspectLevelCalculator {

    static Map<String, Integer> aspectLevels = new HashMap<>();

    public static int computeAspectLevel(Aspect aspect) {
        String aspectName = aspect.getTag();

        // Is this aspect already calculated? if so skip
        if (aspectLevels.containsKey(aspectName)) {
            return aspectLevels.get(aspectName);
        }

        // If this aspect is a primal aspect add it with a level of 1
        if (aspect.isPrimal()) {
            aspectLevels.put(aspectName, 0);
            return aspectLevels.get(aspectName);
        }

        Aspect[] components = aspect.getComponents();
        if (components == null || components.length != 2) {
            aspectLevels.put(aspectName, 0);
            return aspectLevels.get(aspectName);
        }

        int level1 = computeAspectLevel(components[0]);
        int level2 = computeAspectLevel(components[1]);
        int finalLevel = Math.max(level1, level2) + 1;

        aspectLevels.put(aspectName, finalLevel);
        return aspectLevels.get(aspectName);
    }
}
