package com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders;

import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdder01 extends ItemAdder_Basic {

    public static Map<String, ItemAdder01> Item01Map = new HashMap<>();
    public static final CreativeTabs tabMetaItem01 = new CreativeTabs(
        texter("GTCM Meta Items 1", "CreativeTabsGTCMMetaItems")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return MetaItem01;
        }
    };

    public static final Item MetaItem01 = new ItemAdder01("MetaItem01Base", "MetaItem01", tabMetaItem01);

    public ItemAdder01(String Name, String MetaName, CreativeTabs aCreativeTabs) {
        super(Name, MetaName, aCreativeTabs);
        Item01Map.put(MetaName, this);
    }

    public static final Map<Short, ItemStack> MetaItem01Map = new HashMap<>();
    public static short TempMetaPointer = 0;

    private static short getAutoMeta(String aName) {
        if (TempMetaPointer == 32767)
            throw new IllegalArgumentException("It's not enough MetaValue when init " + aName);

        return TempMetaPointer++;
    }

    public static ItemStack initItem01(String aName) {
        // Auto generate the MetaValue
        final short Meta = getAutoMeta(aName);
        // Handle the Name
        String aUnlocalizedName = MetaItem01.getUnlocalizedName() + "." + Meta;
        texter(aName, aUnlocalizedName + ".name");

        ItemStack generatedItemStack = new ItemStack(MetaItem01, 1, Meta);

        // Hold the list of Meta-generated Items
        MetaItem01Map.put(Meta, generatedItemStack);

        return generatedItemStack;
    }

    public static void init() {
        for (String MetaName : Item01Map.keySet()) {
            GameRegistry.registerItem(Item01Map.get(MetaName), MetaName);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTabs, List aList) {
        for (Short Meta : MetaItem01Map.keySet()) {
            aList.add(MetaItem01Map.get(Meta));
        }
    }

}
