package com.Nxer.TwistSpaceTechnology.util;

import java.util.HashMap;
import java.util.Map;

import thaumcraft.api.aspects.Aspect;

public class AspectLevelCalculator {

    public static final int BASE_DURATION = 2;
    private static final Map<String, Integer> aspectLevels = new HashMap<>();

    private static final Map<Integer, Integer> LEVEL_TIME_CACHE = new HashMap<>();
    static {
        LEVEL_TIME_CACHE.put(0, 0);
        LEVEL_TIME_CACHE.put(1, BASE_DURATION);
    }

    public static int computeAspectLevel(Aspect aspect) {
        String aspectName = aspect.getTag();

        if (aspectLevels.containsKey(aspectName)) {
            return aspectLevels.get(aspectName);
        }

        if (aspect.isPrimal()) {
            aspectLevels.put(aspectName, 0);
            return 0;
        }

        Aspect[] components = aspect.getComponents();
        if (components == null || components.length != 2) {
            aspectLevels.put(aspectName, 0);
            return 0;
        }

        int level1 = computeAspectLevel(components[0]);
        int level2 = computeAspectLevel(components[1]);
        int finalLevel = (level1 == level2) ? level1 + 1 : Math.max(level1, level2);

        aspectLevels.put(aspectName, finalLevel);
        return finalLevel;
    }

    public static int computeAspectSynthesisTime(Aspect aspect) {
        if (aspect.isPrimal()) {
            return 0;
        }
        int level = computeAspectLevel(aspect);
        return getTimeForLevel(level);
    }

    private static int getTimeForLevel(int level) {
        if (LEVEL_TIME_CACHE.containsKey(level)) {
            return LEVEL_TIME_CACHE.get(level);
        }
        int maxKnown = LEVEL_TIME_CACHE.keySet()
            .stream()
            .max(Integer::compare)
            .orElse(0);
        for (int l = maxKnown + 1; l <= level; l++) {
            int prev = LEVEL_TIME_CACHE.get(l - 1);
            int current = (prev * 3) / 2;
            LEVEL_TIME_CACHE.put(l, current);
        }
        return LEVEL_TIME_CACHE.get(level);
    }

}
