package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import com.Nxer.TwistSpaceTechnology.util.TextHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemYamato extends ItemSword {

    public static final ToolMaterial YAMATO = EnumHelper.addToolMaterial("YAMATO", 4, -1, 16.0F, 110.514F, 514);

    public ItemYamato(String aName, String unlocalizedName, CreativeTabs aCreativeTabs) {
        super(YAMATO);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(aCreativeTabs);
        this.setMaxDamage(0);
        TextHandler.texter(aName, unlocalizedName + ".name");
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        switch (itemStack.getItemDamage()) {
            case 0 -> {
                return this.getUnlocalizedName() + "Unsheathed";
            }
            case 1 -> {
                return this.getUnlocalizedName();
            }
        }
        return null;
    }

    @Override
    public int getMetadata(int Meta) {
        return Meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        p_150895_3_.add(new ItemStack(p_150895_1_, 1, 0));
        /* p_150895_3_.add(new ItemStack(p_150895_1_, 1, 1)); */
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (player.isSneaking()) {
            switch (itemStackIn.getItemDamage()) {
                case 0 -> {
                    itemStackIn.setItemDamage(1);
                    return itemStackIn;
                }
                case 1 -> {
                    itemStackIn.setItemDamage(0);
                    return itemStackIn;
                }
            }

        }
        player.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }

}
