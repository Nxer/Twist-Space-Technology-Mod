package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import java.util.Map;
import java.util.Set;

import net.minecraft.util.IIcon;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips;

public interface IMetaBlock extends IHasTooltips {

    Set<Integer> getUsedMetaSet();

    Map<Integer, String[]> getTooltipsMap();

    Map<Integer, IIcon> getIconMap();

    default String[] getTooltips(int meta) {
        return getTooltipsMap().get(meta);
    }

    @Override
    default void setTooltips(int metaValue, @Nullable String[] tooltips) {
        getTooltipsMap().put(metaValue, tooltips);
    }
}
