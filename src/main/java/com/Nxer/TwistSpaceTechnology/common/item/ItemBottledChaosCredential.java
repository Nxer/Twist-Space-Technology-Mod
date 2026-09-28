package com.Nxer.TwistSpaceTechnology.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;

import gregtech.api.util.GTUtility;

/** Capacity is part of the item identity; the folded recipe family is bound separately. */
public final class ItemBottledChaosCredential extends AbstractTstMetaItem {

    public static final int MAX_META = 5;
    private static final String FAMILY_TAG = "bottledChaosRecipeFamily";

    public ItemBottledChaosCredential() {
        super("BottledChaosCredential", "bottled_chaos.credential");
        setMaxStackSize(1);
    }

    public static long getCapacity(ItemStack stack) {
        if (stack == null || stack.getItem() != TstItems.BottledChaosCredential || stack.stackSize != 1) return 0;
        int meta = stack.getItemDamage();
        return meta >= 0 && meta <= MAX_META ? 1L << (2 * meta) : 0;
    }

    public static ItemStack getRecipeFamily(ItemStack stack) {
        if (getCapacity(stack) == 0 || !stack.hasTagCompound()) return null;
        return ItemStack.loadItemStackFromNBT(
            stack.getTagCompound()
                .getCompoundTag(FAMILY_TAG));
    }

    /** For future stage-gated recipe registrations. Blank credentials do not authorize any family. */
    public static ItemStack bindRecipeFamily(ItemStack credential, ItemStack family) {
        if (getCapacity(credential) == 0 || family == null) throw new IllegalArgumentException("Invalid credential");
        ItemStack bound = credential.copy();
        if (!bound.hasTagCompound()) bound.setTagCompound(new NBTTagCompound());
        bound.getTagCompound()
            .setTag(
                FAMILY_TAG,
                GTUtility.copyAmountUnsafe(1, family)
                    .writeToNBT(new NBTTagCompound()));
        return bound;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        // Reuse a temporary existing icon until the credential artwork is supplied.
        itemIcon = register.registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":MetaItemNamedCircuit/0");
        for (int meta : usedMetaIds) iconMap.put(meta, itemIcon);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lines, boolean advanced) {
        lines.add("bottled_chaos.credential.capacity: " + getCapacity(stack));
        ItemStack family = getRecipeFamily(stack);
        lines.add(
            "bottled_chaos.credential.family: "
                + (family == null ? "bottled_chaos.unbound" : family.getUnlocalizedName()));
    }
}
