package com.Nxer.TwistSpaceTechnology.common.api;

import net.minecraft.item.ItemStack;

/**
 * A combination of {@link IHasVariant} and {@link IHasTooltips}.
 */
public interface IHasVariantAndTooltips extends IHasVariant, IHasTooltips {

    default ItemStack registerVariantWithTooltips(int meta, String[] tooltips) {
        ItemStack stack = registerVariant(meta);
        setTooltips(meta, tooltips, false);
        return stack;
    }

    default ItemStack registerVariantWithTooltips(int meta, String[] tooltips, String[] advancedTooltips) {
        ItemStack stack = registerVariant(meta);
        setTooltips(meta, tooltips, false);
        setTooltips(meta, advancedTooltips, true);
        return stack;
    }

}
