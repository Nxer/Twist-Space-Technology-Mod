package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips;
import com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * An ItemStack Generator used Meta Item System.
 * <li>Use {@link com.Nxer.TwistSpaceTechnology.util.TstUtils#registerItemAdder(ItemAdder_Basic, int, String[])} to
 * create your Item at ItemList01.
 *
 */
public class ItemAdder01 extends ItemAdder_Basic implements IHasTooltips {

    /**
     * An Item Map for managing basic items
     */
    // public static Map<String, ItemAdder01> Item01Map = new HashMap<>();

    /**
     * A Set contains the meta value that has been used.
     */
    public static final Set<Integer> Meta01Set = new HashSet<>();
    public static final Map<Integer, String[]> MetaItemTooltipsMap01 = new HashMap<>();

    /**
     * Create the basic item MetaItem01.
     */
    public ItemAdder01(CreativeTabs aCreativeTabs) {
        // #tr item.MetaItem01.name
        // # Test Item
        // #zh_CN 测试物品
        super("MetaItem01", aCreativeTabs);
        setTextureName("gtnhcommunitymod:MetaItem01/0");
    }

    @Override
    public ItemStack getVariant(int meta) throws IllegalArgumentException {
        return checkAndGetVariant(this, meta, Meta01Set);
    }

    @Override
    public ItemStack[] getVariants() {
        return getAllVariants(this, Meta01Set);
    }

    @Override
    public ItemStack registerVariant(int meta) throws IllegalArgumentException {
        return checkAndRegisterVariant(this, meta, Meta01Set);
    }

    @Override
    public void setTooltips(int metaValue, @Nullable String[] tooltips) {
        MetaItemTooltipsMap01.put(metaValue, tooltips);
    }

    @Override
    public @Nullable String[] getTooltips(int metaValue) {
        return MetaItemTooltipsMap01.get(metaValue);
    }

    /**
     * Init the basic items at the game pre init.
     */
    // public static void init() {
    // for (String MetaName : Item01Map.keySet()) {
    // GameRegistry.registerItem(Item01Map.get(MetaName), MetaName);
    // }
    // }

    // region Overrides

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        this.itemIcon = iconRegister.registerIcon("gtnhcommunitymod:MetaItem01/0");
        for (int meta : Meta01Set) {
            ItemStaticDataClientOnly.iconsMap01
                .put(meta, iconRegister.registerIcon("gtnhcommunitymod:MetaItem01/" + meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int aMetaData) {
        return ItemStaticDataClientOnly.iconsMap01.containsKey(aMetaData)
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
        for (int Meta : Meta01Set) {
            aList.add(new ItemStack(BasicItems.MetaItem01, 1, Meta));
        }
    }
    // endregion
}
