package com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;


/**
 * An ItemStack Generator used Meta Item System.
 * <li> Use {@link ItemAdder01#initItem01(String, int)} to create your Item at ItemList01.
 *
 */
public class ItemAdder01 extends ItemAdder_Basic {


    /**
     * An Item Map for managing basic items
     */
    public static Map<String, ItemAdder01> Item01Map = new HashMap<>();

    /**
     * An ItemStack Map for managing MetaItem01
     */
    public static final Map<Integer, ItemStack> MetaItem01Map = new HashMap<>();

    /**
     * Creative Tab for MetaItem01
     */
    public static final CreativeTabs tabMetaItem01 = new CreativeTabs(
        texter("GTCM Meta Items 1", "itemGroup.GTCM Meta Items 1")) {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return MetaItem01;
        }
    };

    /**
     * Create the basic item MetaItem01.
     */
    public static final Item MetaItem01 = new ItemAdder01("MetaItem01Base", "MetaItem01", tabMetaItem01).setTextureName("gtnhcommunitymod:MetaItem01/0");

    public ItemAdder01(String aName, String aMetaName, CreativeTabs aCreativeTabs) {
        super(aName, aMetaName, aCreativeTabs);
        Item01Map.put(aMetaName, this);
    }

    /**
     * The method about creating Items with ItemStack form by Meta Item System.
     * Use this method to create Items at ItemList.
     *
     * @param aName The name of your creating item.
     * @param aMeta The MetaValue of your creating item.
     * @return      Return the Item with ItemStack form you create.
     */
    public static ItemStack initItem01(String aName, int aMeta) {
        // Handle the MetaValue
        // Handle the Name
        String aUnlocalizedName = MetaItem01.getUnlocalizedName() + "." + aMeta;
        texter(aName, aUnlocalizedName + ".name");
        // Generate the new ItemStack
        ItemStack generatedItemStack = new ItemStack(MetaItem01, 1, aMeta);
        // Hold the list of Meta-generated Items
        MetaItem01Map.put(aMeta, generatedItemStack);

        return generatedItemStack;
    }


    /**
     * Init the basic items at the game pre init.
     */
    public static void init() {
        for (String MetaName : Item01Map.keySet()) {
            GameRegistry.registerItem(Item01Map.get(MetaName), MetaName);
        }
    }

    // region Overrides
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        this.itemIcon = iconRegister.registerIcon("gtnhcommunitymod:MetaItem01/0");
        for (int meta : MetaItem01Map.keySet()){
            ItemStaticDataClientOnly.iconsMap01.put(meta,iconRegister.registerIcon("gtnhcommunitymod:MetaItem01texture"));
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int aMetaData) {
        return aMetaData < ItemStaticDataClientOnly.iconsMap01.size() ? ItemStaticDataClientOnly.iconsMap01.get(aMetaData) : ItemStaticDataClientOnly.iconsMap01.get(0);
    }


    /**
     * Override this method to show all ItemStack of MetaItem01.
     *
     * @param aItem
     * @param aCreativeTabs
     * @param aList
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTabs, List aList) {
        for (int Meta : MetaItem01Map.keySet()) {
            aList.add(MetaItem01Map.get(Meta));
        }
    }
    // endregion
}
