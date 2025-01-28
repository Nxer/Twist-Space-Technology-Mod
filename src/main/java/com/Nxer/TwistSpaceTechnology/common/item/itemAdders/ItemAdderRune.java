package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips;
import com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems;
import com.Nxer.TwistSpaceTechnology.system.ItemCooldown.IItemHasCooldown;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;

/**
 * An ItemStack Generator used Meta Item System.
 * <li>Use {@link com.Nxer.TwistSpaceTechnology.util.TstUtils#registerItemAdder(ItemAdder_Basic, int)} to create your
 * Item at ItemList01.
 */
public class ItemAdderRune extends ItemAdder_Basic implements IItemHasCooldown, IHasTooltips {

    /**
     * An Item Map for managing basic items
     */
    // public static Map<String, ItemAdder01> Item01Map = new HashMap<>();

    /**
     * A Set contains the meta value that has been used.
     */
    public static final Set<Integer> Meta01Set = new HashSet<>();
    public static final Map<Integer, String[]> MetaItemTooltipsMapRune = new HashMap<>();

    /**
     * Create the basic item MetaItemRune.
     */
    public ItemAdderRune(CreativeTabs aCreativeTabs) {
        // #tr item.MetaItemRune.name
        // # Meta Item Rune
        // #zh_CN Meta Item Rune
        super("MetaItemRune", aCreativeTabs);
        this.maxStackSize = 1;
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
        MetaItemTooltipsMapRune.put(metaValue, tooltips);
    }

    @Override
    public @Nullable String[] getTooltips(int metaValue) {
        return MetaItemTooltipsMapRune.get(metaValue);
    }

    // region Overrides

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        this.itemIcon = iconRegister.registerIcon("gtnhcommunitymod:MetaItemRune/Rune0");
        for (int meta : Meta01Set) {
            ItemStaticDataClientOnly.iconsMapRune
                .put(meta, iconRegister.registerIcon("gtnhcommunitymod:MetaItemRune/Rune" + meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int aMetaData) {
        return ItemStaticDataClientOnly.iconsMapRune.containsKey(aMetaData)
            ? ItemStaticDataClientOnly.iconsMapRune.get(aMetaData)
            : ItemStaticDataClientOnly.iconsMapRune.get(0);
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
        if (null != MetaItemTooltipsMapRune.get(meta)) {
            String[] tooltips = MetaItemTooltipsMapRune.get(meta);
            theTooltipsList.addAll(Arrays.asList(tooltips));
        }
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
            aList.add(new ItemStack(BasicItems.MetaItemRune, 1, Meta));
        }
    }
    // endregion

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
