package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.init.TstItems;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaCraftingCenter;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;

/**
 * An ItemStack to save multiplied pattern for TST_MegaCraftingCenter.
 */
public class ItemActualPattern extends Item implements ICraftingPatternItem {

    public ItemActualPattern() {
        super();
        setUnlocalizedName("ActualPattern");

    }

    @Override
    public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) {
            return null;
        }
        ItemStack originPatternItem = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("originPattern"));
        int multiplier = tag.getInteger("multiplier");
        if (originPatternItem == null) {
            return null;
        }
        ICraftingPatternDetails originPattern = ((ICraftingPatternItem) originPatternItem.getItem())
            .getPatternForItem(originPatternItem, w);
        if (originPattern == null) {
            return null;
        }
        return new TST_MegaCraftingCenter.ActualPattern(originPattern, multiplier);
    }

    public static ItemStack getStackFromPattern(TST_MegaCraftingCenter.ActualPattern pattern) {
        ItemStack is = new ItemStack(TstItems.ActualPattern);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("multiplier", pattern.multiplier);
        ItemStack item = pattern.originPattern.getPattern();
        if (item != null) tag.setTag(
            "originPattern",
            pattern.originPattern.getPattern()
                .writeToNBT(new NBTTagCompound()));
        is.setTagCompound(tag);
        return is;
    }

}
