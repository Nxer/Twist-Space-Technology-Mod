package com.GTNH_Community.gtnhcommunitymod.loader.loadingUtils;

import java.util.HashSet;

public class AdderRegistry {

    private static final HashSet<Runnable> toRun = new HashSet<>();

    private AdderRegistry() {}

    public static void addAdder(Runnable adder) {
        toRun.add(adder);
    }

    public static void run() {
        for (Runnable r : toRun) r.run();
    }
}
