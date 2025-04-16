package com.Nxer.TwistSpaceTechnology.util;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;

public enum CraftedTokens {

    Aditya(1, "Aditya & Hive Run", EnumChatFormatting.GOLD);

    public final int id;
    public final String name;
    public final EnumChatFormatting color;

    CraftedTokens(int id, String name, EnumChatFormatting color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static ArrayList<String> getAllName() {
        ArrayList<String> list = new ArrayList<>();
        for (CraftedTokens craftedTokens : CraftedTokens.values()) {
            list.add(
                "    " + craftedTokens.id + ": " + craftedTokens.color + craftedTokens.name + EnumChatFormatting.RESET);
        }
        return list;
    }
}
