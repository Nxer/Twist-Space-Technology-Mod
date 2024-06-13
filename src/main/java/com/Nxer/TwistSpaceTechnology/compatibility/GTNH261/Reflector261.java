package com.Nxer.TwistSpaceTechnology.compatibility.GTNH261;

import java.lang.reflect.Field;

import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;

public class Reflector261 {

    public final static Materials MagMatter;

    static {
        Materials MagMatter0 = null;
        try {
            Class<MaterialsUEVplus> clazz = MaterialsUEVplus.class;
            Field field = clazz.getField("MagMatter");
            field.setAccessible(true);
            MagMatter0 = (Materials) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        MagMatter = MagMatter0;
    }

}
