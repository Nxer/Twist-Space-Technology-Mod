package com.Nxer.TwistSpaceTechnology.util;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;

public enum CraftedTokens {

    Aditya("Aditya & Hive Run", EnumChatFormatting.LIGHT_PURPLE),
    HoroborosAndKoneko("Horoboros & Koneko", EnumChatFormatting.RED);

    private final String name;
    private final EnumChatFormatting color;

    CraftedTokens(String name, EnumChatFormatting color) {
        this.name = name;
        this.color = color;
    }

    public int getId() {
        return this.ordinal() + 1;
    }

    public static ArrayList<String> getAllName() {
        ArrayList<String> list = new ArrayList<>();
        for (CraftedTokens craftedTokens : CraftedTokens.values()) {
            list.add(
                "    " + craftedTokens
                    .getId() + ": " + craftedTokens.color + craftedTokens.name + EnumChatFormatting.RESET);
        }
        return list;
    }
}
