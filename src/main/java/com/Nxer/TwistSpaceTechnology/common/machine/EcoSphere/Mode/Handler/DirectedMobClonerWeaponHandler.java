package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public final class DirectedMobClonerWeaponHandler {

    private DirectedMobClonerWeaponHandler() {}

    public static WeaponEffects process(ItemStack[] weapons) {
        if (weapons == null || weapons.length == 0) return WeaponEffects.EMPTY;
        Set<Item> weaponTypes = new HashSet<>();
        Map<Integer, Integer> enchantmentLevels = new HashMap<>();
        int beheadingLevel = 0;
        double draconicSoulMultiplier = 0d;
        for (ItemStack weapon : weapons) {
            if (weapon == null || weapon.getItem() == null) continue;
            // Weapon identities form a set; enchantments and special attributes keep only their highest value.
            Item item = weapon.getItem();
            weaponTypes.add(item);
            for (Map.Entry<Integer, Integer> enchantment : EnchantmentHelper.getEnchantments(weapon)
                .entrySet()) {
                enchantmentLevels.merge(enchantment.getKey(), enchantment.getValue(), Math::max);
            }
            beheadingLevel = Math.max(beheadingLevel, getTinkersBeheadingLevel(weapon));
            int reaperLevel = getEnchantmentLevel(weapon, CachedReferences.DRACONIC_REAPER);
            draconicSoulMultiplier = Math.max(draconicSoulMultiplier, reaperLevel + getDraconicWeaponBonus(item));
        }
        return weaponTypes.isEmpty() ? WeaponEffects.EMPTY
            : new WeaponEffects(weaponTypes, enchantmentLevels, beheadingLevel, draconicSoulMultiplier);
    }

    public static boolean isSupportedWeapon(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return false;
        Item item = stack.getItem();
        return Enchantment.looting.canApply(stack) || stack.isItemStackDamageable()
            || item == CachedReferences.AVARITIA_SKULL_SWORD
            || item == CachedReferences.DRACONIC_STAFF
            || item == CachedReferences.DRACONIC_SWORD
            || item == CachedReferences.DRACONIC_BOW
            || item == CachedReferences.WYVERN_SWORD
            || item == CachedReferences.WYVERN_BOW;
    }

    private static int getTinkersBeheadingLevel(ItemStack weapon) {
        if (CachedReferences.TINKERS_TOOL == null || !CachedReferences.TINKERS_TOOL.isInstance(weapon.getItem())) {
            return 0;
        }
        int level = 0;
        NBTTagCompound root = weapon.getTagCompound();
        if (root != null && root.hasKey("InfiTool")) {
            level = Math.max(
                0,
                root.getCompoundTag("InfiTool")
                    .getInteger("Beheading"));
        }
        return weapon.getItem() == CachedReferences.TINKERS_CLEAVER ? level + 2 : level;
    }

    private static int getDraconicWeaponBonus(Item item) {
        if (item == CachedReferences.DRACONIC_STAFF) return 3;
        if (item == CachedReferences.DRACONIC_SWORD || item == CachedReferences.DRACONIC_BOW) return 2;
        if (item == CachedReferences.WYVERN_SWORD || item == CachedReferences.WYVERN_BOW) return 1;
        return 0;
    }

    private static int getEnchantmentLevel(ItemStack weapon, int enchantmentId) {
        return enchantmentId < 0 ? 0 : EnchantmentHelper.getEnchantmentLevel(enchantmentId, weapon);
    }

    private static Item findModItem(Mods mod, String registryName) {
        if (!mod.isModLoaded()) return null;
        ItemStack stack = GTModHandler.getModItem(mod.ID, registryName, 1);
        return stack == null ? null : stack.getItem();
    }

    private static int getOptionalEnchantmentId(String className, String fieldName) {
        Object value = getStaticField(className, fieldName);
        if (value instanceof Enchantment enchantment) return enchantment.effectId;
        return value instanceof Number ? ((Number) value).intValue() : -1;
    }

    private static Object getStaticField(String className, String fieldName) {
        try {
            Field field = Class.forName(className)
                .getField(fieldName);
            return field.get(null);
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return null;
        }
    }

    private static Class<?> loadOptionalClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException | LinkageError ignored) {
            return null;
        }
    }

    public static final class WeaponEffects {

        private static final WeaponEffects EMPTY = new WeaponEffects(
            Collections.emptySet(),
            Collections.emptyMap(),
            0,
            0d);

        private final Set<Item> weaponTypes;
        private final Map<Integer, Integer> enchantmentLevels;
        private final int beheadingLevel;
        private final double draconicSoulMultiplier;

        private WeaponEffects(Set<Item> weaponTypes, Map<Integer, Integer> enchantmentLevels, int beheadingLevel,
            double draconicSoulMultiplier) {
            this.weaponTypes = Collections.unmodifiableSet(new HashSet<>(weaponTypes));
            this.enchantmentLevels = Collections.unmodifiableMap(new HashMap<>(enchantmentLevels));
            this.beheadingLevel = beheadingLevel;
            this.draconicSoulMultiplier = draconicSoulMultiplier;
        }

        public boolean hasWeapon(Item item) {
            return item != null && weaponTypes.contains(item);
        }

        public boolean hasWeapon() {
            return !weaponTypes.isEmpty();
        }

        public int getEnchantmentLevel(int enchantmentId) {
            return enchantmentLevels.getOrDefault(enchantmentId, 0);
        }

        public boolean hasAvaritiaSkullSword() {
            return hasWeapon(CachedReferences.AVARITIA_SKULL_SWORD);
        }

        public int getBeheadingLevel() {
            return beheadingLevel;
        }

        public double getDraconicSoulMultiplier() {
            return draconicSoulMultiplier;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof WeaponEffects other)) return false;
            return beheadingLevel == other.beheadingLevel
                && Double.compare(draconicSoulMultiplier, other.draconicSoulMultiplier) == 0
                && weaponTypes.equals(other.weaponTypes)
                && enchantmentLevels.equals(other.enchantmentLevels);
        }

        @Override
        public int hashCode() {
            int result = weaponTypes.hashCode();
            result = 31 * result + enchantmentLevels.hashCode();
            result = 31 * result + beheadingLevel;
            long multiplierBits = Double.doubleToLongBits(draconicSoulMultiplier);
            return 31 * result + (int) (multiplierBits ^ multiplierBits >>> 32);
        }
    }

    private static final class CachedReferences {

        // spotless:off
        private static final Item AVARITIA_SKULL_SWORD = findModItem(Mods.Avaritia, "Skull_Sword");
        private static final Item WYVERN_SWORD = findModItem(Mods.DraconicEvolution, "wyvernSword");
        private static final Item WYVERN_BOW = findModItem(Mods.DraconicEvolution, "wyvernBow");
        private static final Item DRACONIC_SWORD = findModItem(Mods.DraconicEvolution, "draconicSword");
        private static final Item DRACONIC_BOW = findModItem(Mods.DraconicEvolution, "draconicBow");
        private static final Item DRACONIC_STAFF = findModItem(Mods.DraconicEvolution, "draconicStaffOfPower");
        private static final int DRACONIC_REAPER = getOptionalEnchantmentId("com.brandon3055.draconicevolution.common.handler.ConfigHandler", "reaperEnchantID");
        private static final Class<?> TINKERS_TOOL = loadOptionalClass("tconstruct.library.tools.ToolCore");
        private static final Item TINKERS_CLEAVER = findModItem(Mods.TinkerConstruct, "cleaver");
        // spotless:on
        private CachedReferences() {}
    }
}
