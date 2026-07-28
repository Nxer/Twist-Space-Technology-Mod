package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;

public enum DirectedMobClonerArmorDropConversion {

    LEATHER((ItemArmor) Items.leather_helmet, Materials.Leather),
    CHAIN((ItemArmor) Items.chainmail_helmet, Materials.Iron),
    IRON((ItemArmor) Items.iron_helmet, Materials.Iron),
    GOLD((ItemArmor) Items.golden_helmet, Materials.Gold),
    DIAMOND((ItemArmor) Items.diamond_helmet, Materials.Diamond);

    private final ItemArmor sample;
    private final Materials material;

    DirectedMobClonerArmorDropConversion(ItemArmor sample, Materials material) {
        this.sample = sample;
        this.material = material;
    }

    public static ItemStack convert(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemArmor armor)) return stack.copy();
        for (DirectedMobClonerArmorDropConversion value : values()) {
            if (armor.getArmorMaterial() == value.sample.getArmorMaterial()) {
                return value.material.getDust(Math.max(1, stack.stackSize));
            }
        }
        return null;
    }
}
