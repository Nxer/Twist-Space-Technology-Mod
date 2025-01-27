package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemAdder_Basic extends Item {

    private List<String> tooltips = new ArrayList<>();

    public ItemAdder_Basic(String unlocalizedName, CreativeTabs aCreativeTabs/* , String aIconPath */) {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(aCreativeTabs);
        this.setUnlocalizedName(unlocalizedName);
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }

    /**
     * Returns the unlocalized name of this item.
     * <p>
     * The item damage is used as a part of key. eg: {@code item.{ITEM_NAME}.{ITEM_DAMAGE}}.
     * <p>
     * NOTE: "final", because we don't want subclasses to modify this
     */
    @Override
    public final String getUnlocalizedName(ItemStack aItemStack) {
        return super.getUnlocalizedName() + "." + aItemStack.getItemDamage();
    }

    @Override
    public final String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTabs, List aList) {
        aList.add(new ItemStack(aItem, 1, 0));
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked" })
    public void addInformation(ItemStack aItemStack, EntityPlayer aEntityPlayer, List aTooltipsList,
        boolean p_77624_4_) {
        if (tooltips.size() > 0) {
            aTooltipsList.addAll(tooltips);
        }
    }

}
