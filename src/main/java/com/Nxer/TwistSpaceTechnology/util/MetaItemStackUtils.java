package com.Nxer.TwistSpaceTechnology.util;

import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.ApiStatus;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

/**
 * @deprecated sunset with texter
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class MetaItemStackUtils {

    // generate item stack when init
    @Deprecated
    public static ItemStack initMetaItemStack(String i18nName, int Meta, Item basicItem, Set<Integer> aContainerSet) {

        // Hold the list of Meta-generated Items
        aContainerSet.add(Meta);

        return new ItemStack(basicItem, 1, Meta);
    }

    // generate itemBlock stack when init
    @Deprecated
    public static ItemStack initMetaItemStack(String i18nName, int Meta, Block baseBlock, Set<Integer> aContainerSet) {
        aContainerSet.add(Meta);
        return new ItemStack(baseBlock, 1, Meta);
    }

    public static void metaItemStackTooltipsAdd(Map<Integer, String[]> tooltipsMap, int meta, String[] tooltips) {
        if (tooltipsMap.containsKey(meta)) {
            TwistSpaceTechnology.LOG.info("failed to Replace a tooltips:" + tooltips[0] + " ...");
            return;
        }
        tooltipsMap.put(meta, tooltips);
    }

}
