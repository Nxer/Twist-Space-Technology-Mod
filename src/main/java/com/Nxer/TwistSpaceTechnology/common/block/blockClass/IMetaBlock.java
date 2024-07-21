package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import java.util.Map;
import java.util.Set;

import com.Nxer.TwistSpaceTechnology.common.api.Interfaces.ITooltips;
import net.minecraft.util.IIcon;

public interface IMetaBlock extends ITooltips {

    Set<Integer> getUsedMetaSet();

    Map<Integer, String[]> getTooltipsMap();

    Map<Integer, IIcon> getIconMap();

    default String[] getTooltips(int meta) {
        return getTooltipsMap().get(meta);
    }

}
