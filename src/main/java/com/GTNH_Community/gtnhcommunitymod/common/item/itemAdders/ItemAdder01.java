package com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders;

import static com.GTNH_Community.gtnhcommunitymod.common.GTCMCreativeTabs.tabMetaItem01;
import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * An ItemStack Generator used Meta Item System.
 * <li>Use {@link ItemAdder01#initItem01(String, int)} to create your Item at ItemList01.
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
     *
     */
    public static final Map<Integer, String[]> MetaItemTooltipsMap01 = new HashMap<>();

    /**
     * Create the basic item MetaItem01.
     */
    public static final Item MetaItem01 = new ItemAdder01("MetaItem01Base", "MetaItem01", tabMetaItem01)
        .setTextureName("gtnhcommunitymod:MetaItem01/0");

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
     * @return Return the Item with ItemStack form you create.
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

    public static ItemStack initItem01(String aName, int aMeta, String[] tooltips) {
        // Handle the MetaValue
        // Handle the Name
        String aUnlocalizedName = MetaItem01.getUnlocalizedName() + "." + aMeta;
        texter(aName, aUnlocalizedName + ".name");
        // Generate the new ItemStack
        ItemStack generatedItemStack = new ItemStack(MetaItem01, 1, aMeta);
        // Hold the list of Meta-generated Items
        MetaItem01Map.put(aMeta, generatedItemStack);

        if (null != tooltips) {
            addTooltips(aMeta, tooltips);
        }

        return generatedItemStack;
    }

    /**
     * Add tooltips with the MetaValue of ItemStack.
     * <li>Mind to call texter() if using this to handle tooltips.
     *
     * @param aMeta    The MetaValue of ItemStack.
     * @param tooltips Tooltips in String[].
     */
    public static void addTooltips(int aMeta, String[] tooltips) {
        MetaItemTooltipsMap01.put(aMeta, tooltips);
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
        for (int meta : MetaItem01Map.keySet()) {
            ItemStaticDataClientOnly.iconsMap01
                .put(meta, iconRegister.registerIcon("gtnhcommunitymod:MetaItem01texture"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int aMetaData) {
        return aMetaData < ItemStaticDataClientOnly.iconsMap01.size()
            ? ItemStaticDataClientOnly.iconsMap01.get(aMetaData)
            : ItemStaticDataClientOnly.iconsMap01.get(0);
    }

    /**
     * Handle the tooltips.
     *
     * @param aItemStack
     * @param theTooltipsList
     */
    @SideOnly(Side.CLIENT)
    @Override
    @SuppressWarnings({ "unchecked" })
    public void addInformation(ItemStack aItemStack, EntityPlayer p_77624_2_, List theTooltipsList,
        boolean p_77624_4_) {
        int meta = aItemStack.getItemDamage();
        if (null != MetaItemTooltipsMap01.get(meta)) {
            String[] tooltips = MetaItemTooltipsMap01.get(meta);
            theTooltipsList.addAll(Arrays.asList(tooltips));
        }
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
