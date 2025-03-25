package com.Nxer.TwistSpaceTechnology.client.sound;

import net.minecraft.util.ResourceLocation;

import com.Nxer.TwistSpaceTechnology.config.Config;

public class SoundLoader {

    public static ResourceLocation BGM;

    public static void init() {
        if (Config.Enable_PowerChairBGM) {
            BGM = new ResourceLocation("gtnhcommunitymod:PowerChair");
        }
    }
}
