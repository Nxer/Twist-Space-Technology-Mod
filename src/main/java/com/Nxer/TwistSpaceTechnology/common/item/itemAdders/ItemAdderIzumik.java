package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;

import static net.minecraft.client.gui.GuiScreen.isShiftKeyDown;

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

import com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems;
import com.Nxer.TwistSpaceTechnology.util.MetaItemStackUtils;
import com.Nxer.TwistSpaceTechnology.util.TextHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdderIzumik extends ItemAdder_Basic {

    public static final Set<Integer> MetaSet = new HashSet<>();
    public static final Map<Integer, String[]> MetaItemTooltipsMapIzumik = new HashMap<>();
    public static final Map<Integer, String[]> MetaItemTooltipsMapIzumikShift = new HashMap<>();
    public String unlocalizedName;

    public ItemAdderIzumik(String aName, String aMetaName, CreativeTabs aCreativeTabs) {
        super(aName, aMetaName, aCreativeTabs);
        this.unlocalizedName = aMetaName;
        TextHandler.texter(aName, this.unlocalizedName + ".name");
    }

    public static ItemStack initItemIzumik(String aName, int aMeta) {

        return MetaItemStackUtils.initMetaItemStack(aName, aMeta, BasicItems.MetaItemIzumik, MetaSet);

    }

    public static ItemStack initItemIzumik(String aName, int aMeta, String[] tooltips1, String[] tooltips2) {

        if (tooltips1 != null && tooltips2 != null) {
            MetaItemStackUtils.metaItemStackTooltipsAdd(MetaItemTooltipsMapIzumik, aMeta, tooltips1);
            MetaItemStackUtils.metaItemStackTooltipsAdd(MetaItemTooltipsMapIzumikShift, aMeta, tooltips2);
        }

        return initItemIzumik(aName, aMeta);

    }

    @Override
    public String getUnlocalizedName(ItemStack aItemStack) {
        return this.unlocalizedName + "." + aItemStack.getItemDamage();
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    @Override

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        this.itemIcon = iconRegister.registerIcon("gtnhcommunitymod:MetaItemIzumik/0");
        for (int meta : MetaSet) {
            ItemStaticDataClientOnly.iconsMapIzumik
                .put(meta, iconRegister.registerIcon("gtnhcommunitymod:MetaItemIzumik/" + meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int aMetaData) {
        return ItemStaticDataClientOnly.iconsMapIzumik.containsKey(aMetaData)
            ? ItemStaticDataClientOnly.iconsMapIzumik.get(aMetaData)
            : ItemStaticDataClientOnly.iconsMapIzumik.get(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTabs, List aList) {
        for (int Meta : MetaSet) {
            aList.add(new ItemStack(BasicItems.MetaItemIzumik, 1, Meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack aItemStack, final EntityPlayer player, final List toolTipList,
        final boolean advancedToolTips) {
        int meta = aItemStack.getItemDamage();
        String[] tooltips;
        if (null != MetaItemTooltipsMapIzumik.get(meta)) {
            if (isShiftKeyDown()) {
                tooltips = MetaItemTooltipsMapIzumikShift.get(meta);
                toolTipList.addAll(Arrays.asList(tooltips));
            } else {
                tooltips = MetaItemTooltipsMapIzumik.get(meta);
                toolTipList.addAll(Arrays.asList(tooltips));
            }
        }
    }
}
