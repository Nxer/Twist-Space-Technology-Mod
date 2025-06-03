package com.Nxer.TwistSpaceTechnology.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class TstReflectionUtils {

    private static final Map<Class<?>, ClassReflectionCache> CACHE_MAP = new HashMap<>();

    private static class ClassReflectionCache {

        public final Class<?> forClass;

        public final Map<String, Field> fieldCache = new HashMap<>();
        public final Table<String, Class<?>[], Method> methodCache = HashBasedTable.create();

        public ClassReflectionCache(Class<?> forClass) {
            this.forClass = forClass;
        }
    }

    @Nullable
    public static Field getFieldNoCache(Class<?> clazz, String fieldName, boolean findSuper) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (findSuper && clazz.getSuperclass() != Object.class) {
                return getFieldNoCache(clazz.getSuperclass(), fieldName, true);
            }
            return null;
        }
    }

    @Nullable
    public static Field getField(Class<?> clazz, String fieldName) {
        ClassReflectionCache cache = CACHE_MAP.computeIfAbsent(clazz, ClassReflectionCache::new);
        return cache.fieldCache.computeIfAbsent(fieldName, k -> getFieldNoCache(clazz, fieldName, true));
    }

    public static boolean isFieldFinal(Field f) {
        return Modifier.isFinal(f.getModifiers());
    }

    public static void unlockFieldFinal(Field f) {
        if (!isFieldFinal(f)) return;

        // Field#modifiers (int)
        Field fModifiers = Objects.requireNonNull(getField(Field.class, "modifiers"));
        try {
            fModifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to unlock final field: " + f.getName(), e);
        }
    }

    public static void setFieldValue(Object ref, String fieldName, Object value) {
        // from ref, get the required stuffs
        Class<?> clazz = ref instanceof Class<?> ? (Class<?>) ref : ref.getClass();
        Object instance = ref instanceof Class<?> ? null : ref;

        // find the field or throw
        Field f = getField(clazz, fieldName);
        if (f == null) {
            throw new RuntimeException("Could not find field " + fieldName + " in " + clazz.getName());
        }

        // unlock final
        if (isFieldFinal(f)) {
            unlockFieldFinal(f);
        }

        // set the value
        try {
            f.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field " + fieldName + " in " + clazz.getName(), e);
        }
    }

}
