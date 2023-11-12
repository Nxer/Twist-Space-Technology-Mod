package com.Nxer.TwistSpaceTechnology.util;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

public class MetaItemStackUtils {

    // generate item stack when init
    public static ItemStack initMetaItemStack(String i18nName, int Meta, Item basicItem, Set<Integer> aContainerSet) {

        // Handle the Name
        texter(i18nName, basicItem.getUnlocalizedName() + "." + Meta + ".name");
        // Hold the list of Meta-generated Items
        aContainerSet.add(Meta);

        return new ItemStack(basicItem, 1, Meta);
    }

    // generate itemBlock stack when init
    public static ItemStack initMetaItemStack(String i18nName, int Meta, Block baseBlock, Set<Integer> aContainerSet) {
        texter(i18nName, baseBlock.getUnlocalizedName() + "." + Meta + ".name");
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
