package com.Nxer.TwistSpaceTechnology.compatibility.GTNH251;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public class Reflector {

    public static final CheckRecipeResult OUTPUT_FULL;
    private static final Method strongCheckOrAddUserMethod;
    private static final Method addEUToGlobalEnergyMapMethod;

    static {
        CheckRecipeResult OUTPUT_FULL0 = null;
        try {
            Class<CheckRecipeResultRegistry> clazz = CheckRecipeResultRegistry.class;
            Field field = clazz.getField("OUTPUT_FULL");
            field.setAccessible(true);
            OUTPUT_FULL0 = (CheckRecipeResult) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        OUTPUT_FULL = OUTPUT_FULL0;

        Method strongCheckOrAddUserMethod0 = null;
        try {
            Class<?> clazz = IGlobalWirelessEnergy.class;

            // 获取myInterfaceMethod方法的Method对象
            strongCheckOrAddUserMethod0 = clazz.getMethod("strongCheckOrAddUser", String.class, String.class);
            strongCheckOrAddUserMethod0.setAccessible(true);

        } catch (NoSuchMethodException ignored) {}
        strongCheckOrAddUserMethod = strongCheckOrAddUserMethod0;

        Method addEUToGlobalEnergyMapMethod0 = null;
        try {
            Class<?> clazz = IGlobalWirelessEnergy.class;

            // 获取myInterfaceMethod方法的Method对象
            addEUToGlobalEnergyMapMethod0 = clazz.getMethod("addEUToGlobalEnergyMap", String.class, long.class);
            addEUToGlobalEnergyMapMethod0.setAccessible(true);

        } catch (NoSuchMethodException ignored) {}
        addEUToGlobalEnergyMapMethod = addEUToGlobalEnergyMapMethod0;

    }

    public static void strongCheckOrAddUser(Object object, String user_uuid, String user_name) {
        try {
            strongCheckOrAddUserMethod.invoke(object, user_uuid, user_name);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addEUToGlobalEnergyMap(Object object, String user_uuid, long EU) {
        try {
            return (boolean) addEUToGlobalEnergyMapMethod.invoke(object, user_uuid, EU);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
