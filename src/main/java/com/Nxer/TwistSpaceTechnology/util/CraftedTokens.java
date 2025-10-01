package com.Nxer.TwistSpaceTechnology.util;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;

public enum CraftedTokens {

    Aditya("Aditya & Hive Run", EnumChatFormatting.LIGHT_PURPLE),
    Horoboros_Koneko("Horoboros & Koneko", EnumChatFormatting.RED),
    Monroth_HyperCreep("Monroth & HyperCreep", EnumChatFormatting.AQUA),
    Tuna("Tuna", EnumChatFormatting.AQUA),
    AdmiralGUMI_MAIM0_LKOperan("Admiral_GUMI & MAIM0 & LK_Operan", EnumChatFormatting.GREEN),
    Komart("Komart", EnumChatFormatting.RED);

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
