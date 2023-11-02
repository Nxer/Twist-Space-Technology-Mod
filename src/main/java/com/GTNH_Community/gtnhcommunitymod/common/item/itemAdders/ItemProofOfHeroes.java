package com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders;

import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;
import static net.minecraft.client.gui.GuiScreen.isShiftKeyDown;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.GTNH_Community.gtnhcommunitymod.util.TextHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemProofOfHeroes extends Item {

    public String unlocalizedName;

    public ItemProofOfHeroes(String aName, String aMetaName, CreativeTabs aCreativeTabs) {
        super();
        this.setCreativeTab(aCreativeTabs);
        this.unlocalizedName = aMetaName;
        TextHandler.texter(aName, this.unlocalizedName + ".name");
    }

    @Override
    public Item setUnlocalizedName(String aUnlocalizedName) {
        this.unlocalizedName = aUnlocalizedName;
        return this;
    }

    @Override
    public String getUnlocalizedName(ItemStack aItemStack) {
        return this.unlocalizedName;
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
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
            toolTip.add(
                texter(
                    EnumChatFormatting.LIGHT_PURPLE
                        + "The physical culmination of your journey, capable to rend gods asunder.",
                    "tooltips.ProofOfHeroes.line2"));
        } else {
            toolTip.add(
                texter(
                    "The physical culmination of your journey, capable to rend gods asunder.",
                    "tooltips.ProofOfHeroes.line1"));
        }
    }
}
