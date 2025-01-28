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

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips;
import com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdderIzumik extends ItemAdder_Basic implements IHasTooltips.Advanced {

    public static final Set<Integer> MetaSet = new HashSet<>();
    public static final Map<Integer, String[]> MetaItemTooltipsMapIzumik = new HashMap<>();
    public static final Map<Integer, String[]> MetaItemTooltipsMapIzumikShift = new HashMap<>();
    public String unlocalizedName;

    public ItemAdderIzumik(CreativeTabs aCreativeTabs) {
        // #tr item.MetaItemIzumik.name
        // # Meta Item Izumik
        // #zh_CN Meta Item Izumik
        super("MetaItemIzumik", aCreativeTabs);
        setTextureName("gtnhcommunitymod:MetaItem01/0");
    }

    @Override
    public ItemStack getVariant(int meta) throws IllegalArgumentException {
        return checkAndGetVariant(this, meta, MetaSet);
    }

    @Override
    public ItemStack[] getVariants() {
        return getAllVariants(this, MetaSet);
    }

    @Override
    public ItemStack registerVariant(int meta) throws IllegalArgumentException {
        return checkAndRegisterVariant(this, meta, MetaSet);
    }

    @Override
    public void setTooltips(int metaValue, @Nullable String[] tooltips, boolean advancedMode) {
        if (advancedMode) {
            MetaItemTooltipsMapIzumikShift.put(metaValue, tooltips);
        } else {
            MetaItemTooltipsMapIzumik.put(metaValue, tooltips);
        }
    }

    @Override
    public @Nullable String[] getTooltips(int metaValue, boolean advancedMode) {
        if (advancedMode) {
            return MetaItemTooltipsMapIzumikShift.get(metaValue);
        } else {
            return MetaItemTooltipsMapIzumik.get(metaValue);
        }
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
