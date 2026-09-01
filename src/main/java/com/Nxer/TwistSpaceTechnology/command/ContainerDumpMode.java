package com.Nxer.TwistSpaceTechnology.command;

public enum ContainerDumpMode {

    SOURCE,
    GET_MOD_ITEM;

    public static ContainerDumpMode parse(String value) {
        if ("source".equalsIgnoreCase(value)) return SOURCE;
        if ("getModItem".equalsIgnoreCase(value) || "GET_MOD_ITEM".equalsIgnoreCase(value)) return GET_MOD_ITEM;
        return null;
    }
}
