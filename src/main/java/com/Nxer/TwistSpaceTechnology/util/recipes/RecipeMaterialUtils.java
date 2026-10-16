package com.Nxer.TwistSpaceTechnology.util.recipes;

import net.minecraft.item.ItemStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.MetaGeneratedItem01;

/** Startup-only helpers for selecting native full dusts and rejecting fractional-dust recipes. */
public final class RecipeMaterialUtils {

    public static ItemStack nativeDust(Materials material) {
        // Material.getDust may point at another mod's unified target, such as Pam's salt.
        for (ItemStack candidate : GTOreDictUnificator.getOresImmutable(OrePrefixes.dust.get(material))) {
            if (candidate.getItem() == MetaGeneratedItem01.INSTANCE
                && MetaGeneratedItem01.INSTANCE.getOrePrefix(candidate.getItemDamage()) == OrePrefixes.dust
                && MetaGeneratedItem01.INSTANCE.getMaterial(candidate.getItemDamage()) == material) {
                return GTUtility.copyAmountUnsafe(1, candidate);
            }
        }
        return material.getDust(1);
    }

    public static boolean isFractionalDust(ItemStack stack) {
        OrePrefixes prefix = dustPrefix(stack);
        return prefix == OrePrefixes.dustSmall || prefix == OrePrefixes.dustTiny;
    }

    private static OrePrefixes dustPrefix(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;
        if (stack.getItem() instanceof MetaGeneratedItemX32 generated) {
            return generated.getOrePrefix(stack.getItemDamage());
        }
        if (stack.getItem() == WerkstoffLoader.items.get(OrePrefixes.dust)) return OrePrefixes.dust;
        if (stack.getItem() == WerkstoffLoader.items.get(OrePrefixes.dustSmall)) return OrePrefixes.dustSmall;
        if (stack.getItem() == WerkstoffLoader.items.get(OrePrefixes.dustTiny)) return OrePrefixes.dustTiny;
        var association = GTOreDictUnificator.getAssociation(stack);
        return association == null ? null : association.mPrefix;
    }
}
