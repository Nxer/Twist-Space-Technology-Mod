package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public enum DirectedMobClonerDropWhenExclusion {

    CRYSTALLIZED_ESSENCE("Thaumcraft", "ItemCrystalEssence"),
    OPENBLOCKS_TROPHY("OpenBlocks", "trophy"),
    WEAK_BLOOD_SHARD("AWWayofTime", "weakBloodShard"),
    DRACONIC_MOB_SOUL("DraconicEvolution", "mobSoul"),
    FORBIDDEN_EMERALD_FRAGMENT("ForbiddenMagic", "FMResource", 0),
    FORBIDDEN_SIN_SHARDS("ForbiddenMagic", "NetherShard"),
    WITCHERY_TORN_PAGE("witchery", "ingredient", 160),
    TCONSTRUCT_RED_HEART("TConstruct", "heartCanister", 1),
    TCONSTRUCT_YELLOW_HEART("TConstruct", "heartCanister", 3),
    ETFUTURUM_WITHER_ROSE("etfuturum", "wither_rose"),
    EXTRA_UTILITIES_SOUL_FRAGMENT("ExtraUtilities", "mini-soul", 3);

    private static final int ANY_META = -1;

    private final String modId;
    private final String registryName;
    private final int metadata;

    DirectedMobClonerDropWhenExclusion(String modId, String registryName) {
        this(modId, registryName, ANY_META);
    }

    DirectedMobClonerDropWhenExclusion(String modId, String registryName, int metadata) {
        this.modId = modId;
        this.registryName = registryName;
        this.metadata = metadata;
    }

    public static boolean contains(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return false;
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (identifier == null) return false;
        for (DirectedMobClonerDropWhenExclusion exclusion : values()) {
            if (!exclusion.modId.equalsIgnoreCase(identifier.modId)) continue;
            if (!exclusion.registryName.equals(identifier.name)) continue;
            if (exclusion.metadata == ANY_META || exclusion.metadata == stack.getItemDamage()) return true;
        }
        return false;
    }
}
