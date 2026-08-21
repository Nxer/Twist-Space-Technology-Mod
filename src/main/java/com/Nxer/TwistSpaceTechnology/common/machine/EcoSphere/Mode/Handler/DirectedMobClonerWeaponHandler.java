package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public final class DirectedMobClonerWeaponHandler {

    private DirectedMobClonerWeaponHandler() {}

    public static WeaponTags process(ItemStack[] weapons) {
        if (weapons == null || weapons.length == 0) return WeaponTags.EMPTY;
        Set<Item> weaponTypes = new HashSet<>();
        Map<Integer, Integer> enchantmentLevels = new HashMap<>();
        EnumMap<FunctionTag, Double> functions = new EnumMap<>(FunctionTag.class);
        for (ItemStack weapon : weapons) {
            if (weapon == null || weapon.getItem() == null) continue;
            Item item = weapon.getItem();
            weaponTypes.add(item);
            for (Map.Entry<Integer, Integer> enchantment : EnchantmentHelper.getEnchantments(weapon)
                .entrySet()) {
                enchantmentLevels.merge(enchantment.getKey(), enchantment.getValue(), Math::max);
            }
            functions.merge(
                FunctionTag.ALL_OUTPUTS_CHANCE_BONUS,
                getEnchantmentLevel(weapon, Enchantment.looting.effectId) * 5_000d,
                Math::max);
            functions.merge(FunctionTag.TINKERS_BEHEADING_LEVEL, (double) getTinkersBeheadingLevel(weapon), Math::max);
            int reaperLevel = getEnchantmentLevel(weapon, CachedReferences.DRACONIC_REAPER);
            functions.merge(
                FunctionTag.DRACONIC_SOUL_MULTIPLIER,
                reaperLevel + (double) getDraconicWeaponBonus(item),
                Math::max);
            if (item == CachedReferences.AVARITIA_SKULL_SWORD) {
                functions.put(FunctionTag.AVARITIA_SKULL_CHANCE, 10_000d);
            }
            if (matches(weapon, CachedReferences.WITCHING_GADGETS_END_DEVICE)) {
                functions.put(FunctionTag.END_DROP_CHANCE, 10_000d);
            }
            if (matches(weapon, CachedReferences.WITCHING_GADGETS_NETHER_DEVICE)) {
                functions.put(FunctionTag.NETHER_DROP_CHANCE, 10_000d);
            }
        }
        return weaponTypes.isEmpty() ? WeaponTags.EMPTY : new WeaponTags(weaponTypes, enchantmentLevels, functions);
    }

    private static boolean matches(ItemStack stack, ItemStack reference) {
        return stack != null && reference != null
            && stack.getItem() == reference.getItem()
            && stack.getItemDamage() == reference.getItemDamage();
    }

    private static int getTinkersBeheadingLevel(ItemStack weapon) {
        NBTTagCompound root = weapon.getTagCompound();
        if (root == null || !root.hasKey("InfiTool")) return 0;
        int level = Math.max(
            0,
            root.getCompoundTag("InfiTool")
                .getInteger("Beheading"));
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(weapon.getItem());
        if (identifier != null && "TConstruct".equalsIgnoreCase(identifier.modId) && "cleaver".equals(identifier.name))
            level += 2;
        return level;
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

    public enum FunctionTag {
        ALL_OUTPUTS_CHANCE_BONUS,
        TINKERS_BEHEADING_LEVEL,
        DRACONIC_SOUL_MULTIPLIER,
        AVARITIA_SKULL_CHANCE,
        END_DROP_CHANCE,
        NETHER_DROP_CHANCE
    }

    public static final class WeaponTags {

        private static final WeaponTags EMPTY = new WeaponTags(
            Collections.emptySet(),
            Collections.emptyMap(),
            new EnumMap<>(FunctionTag.class));

        private final Set<Item> weaponTypes;
        private final Map<Integer, Integer> enchantmentLevels;
        private final Map<FunctionTag, Double> functions;

        private WeaponTags(Set<Item> weaponTypes, Map<Integer, Integer> enchantmentLevels,
            EnumMap<FunctionTag, Double> functions) {
            this.weaponTypes = Collections.unmodifiableSet(new HashSet<>(weaponTypes));
            this.enchantmentLevels = Collections.unmodifiableMap(new HashMap<>(enchantmentLevels));
            this.functions = Collections.unmodifiableMap(new EnumMap<>(functions));
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

        public double get(FunctionTag tag) {
            return functions.getOrDefault(tag, 0d);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof WeaponTags other)) return false;
            return weaponTypes.equals(other.weaponTypes) && enchantmentLevels.equals(other.enchantmentLevels)
                && functions.equals(other.functions);
        }

        @Override
        public int hashCode() {
            int result = weaponTypes.hashCode();
            result = 31 * result + enchantmentLevels.hashCode();
            return 31 * result + functions.hashCode();
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
        private static final ItemStack WITCHING_GADGETS_END_DEVICE = findModStack(
            Mods.WitchingGadgets,
            "WG_MetalDevice",
            12);
        private static final ItemStack WITCHING_GADGETS_NETHER_DEVICE = findModStack(
            Mods.WitchingGadgets,
            "WG_MetalDevice",
            7);
        // spotless:on
        private CachedReferences() {}
    }

    private static ItemStack findModStack(Mods mod, String registryName, int meta) {
        if (!mod.isModLoaded()) return null;
        return GTModHandler.getModItem(mod.ID, registryName, 1, meta);
    }
}
