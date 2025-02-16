package com.Nxer.TwistSpaceTechnology.common.api;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * A combination of {@link IHasVariant} and {@link IHasTooltips}.
 */
public interface IHasVariantAndTooltips extends IHasVariant, IHasTooltips {

    default ItemStack registerVariantWithTooltips(int meta, String[] tooltips) {
        ItemStack stack = registerVariant(meta);
        setTooltips(meta, tooltips);
        return stack;
    }

    /**
     * A variant of {@link IHasVariantAndTooltips} with {@link com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips.Advanced}.
     */
    interface Advanced extends IHasVariantAndTooltips {

        default ItemStack registerVariantWithTooltips(int meta, String[] tooltips, String[] advancedTooltips) {
            ItemStack stack = registerVariant(meta);
            setTooltips(meta, tooltips, false);
            setTooltips(meta, advancedTooltips, true);
            return stack;
        }
    }

}
