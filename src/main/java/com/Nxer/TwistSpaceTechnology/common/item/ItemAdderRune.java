package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips;
import com.Nxer.TwistSpaceTechnology.system.ItemCooldown.IItemHasCooldown;

import gregtech.api.util.GTUtility;

/**
 * An ItemStack Generator used Meta Item System.
 * <li>Use {@link com.Nxer.TwistSpaceTechnology.util.TstUtils#registerItemAdder(AbstractTstMetaItem, int)} to create
 * your
 * Item at ItemList01.
 */
public class ItemAdderRune extends AbstractTstMetaItem implements IItemHasCooldown, IHasTooltips {

    /**
     * Create the basic item MetaItemRune.
     */
    public ItemAdderRune() {
        // #tr item.MetaItemRune.name
        // # Meta Item Rune
        // #zh_CN Meta Item Rune
        super("MetaItemRune");
        this.maxStackSize = 1;
        setTextureName("gtnhcommunitymod:MetaItem01/0");
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int p_77663_4_, boolean p_77663_5_) {
        NBTTagCompound itemNBT = stack.getTagCompound();
        if (itemNBT == null) itemNBT = new NBTTagCompound();
        if (!itemNBT.hasKey("CurrentCooldown")) itemNBT.setLong("CurrentCooldown", 0);
        if (itemNBT.getLong("CurrentCooldown") > 0) {
            itemNBT.setLong("CurrentCooldown", itemNBT.getLong("CurrentCooldown") - 1);
            stack.setTagCompound(itemNBT);
        }
    }

    @Override
    public long getCooldown() {
        return 60;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        NBTTagCompound itemNBT = itemStackIn.getTagCompound();
        if (itemNBT == null) itemNBT = new NBTTagCompound();
        TwistSpaceTechnology.LOG.info(itemNBT.toString());
        if (!itemNBT.hasKey("CurrentCooldown")) {
            itemNBT.setLong("CurrentCooldown", getCooldown());
        } else if (itemNBT.getLong("CurrentCooldown") > 0) {
            GTUtility.sendChatToPlayer(
                player,
                "This item has a cooldown of " + (float) (itemNBT.getLong("CurrentCooldown")) / 20.0F + 's');
            return itemStackIn;
        } else {
            itemNBT.setLong("CurrentCooldown", getCooldown());
        }
        itemStackIn.setTagCompound(itemNBT);
        TwistSpaceTechnology.LOG.info("Egg lanuched.");
        worldIn.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            worldIn.spawnEntityInWorld(new EntityEgg(worldIn, player));
        }

        return itemStackIn;
    }
}
