package com.Nxer.TwistSpaceTechnology.common.item;

import static net.minecraft.client.gui.GuiScreen.isShiftKeyDown;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.ApiStatus;

import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Deprecated
@ApiStatus.ScheduledForRemoval // said by Nxer
public class ItemPowerChair extends Item {

    public ItemPowerChair(CreativeTabs aCreativeTabs) {
        super();
        this.setCreativeTab(aCreativeTabs);
        // #tr item.tst.common.power_chair.name
        // # Power Chair
        // #zh_CN 抛瓦椅
        this.setUnlocalizedName("PowerChair");
    }

    @Override
    public String getUnlocalizedName() {
        return "item.tst.common.power_chair";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTabs, List aList) {
        aList.add(new ItemStack(aItem, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List toolTip,
        final boolean advancedToolTips) {
        if (isShiftKeyDown()) {
            // #tr item.tst.common.power_chair.tooltip.02
            // # {\LIGHT_PURPLE}If you want it, then you'll have to take it.
            // #zh_CN {\LIGHT_PURPLE}If you want it, then you'll have to take it.
            toolTip.add(TSTUtils.tr("item.tst.common.power_chair.tooltip.02"));
        } else {
            // #tr item.tst.common.power_chair.tooltip.01
            // # Your portal opening day's over.
            // #zh_CN Your portal opening day's over.
            toolTip.add(TSTUtils.tr("item.tst.common.power_chair.tooltip.01"));
        }
    }
}
