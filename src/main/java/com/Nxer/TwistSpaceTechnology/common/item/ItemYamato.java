package com.Nxer.TwistSpaceTechnology.common.item;

import static com.Nxer.TwistSpaceTechnology.config.Config.RewriteEIOTravelStaffConfig;
import static crazypants.enderio.config.Config.teleportStaffMaxBlinkDistance;
import static crazypants.enderio.config.Config.teleportStaffMaxDistance;
import static crazypants.enderio.config.Config.travelStaffMaxBlinkDistance;
import static crazypants.enderio.config.Config.travelStaffMaxDistance;

import java.util.List;
import java.util.Optional;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import com.enderio.core.common.util.BlockCoord;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.api.teleport.IItemOfTravel;
import crazypants.enderio.api.teleport.TravelSource;
import crazypants.enderio.teleport.TravelController;

public class ItemYamato extends ItemSword implements IItemOfTravel {

    public static final ToolMaterial YAMATO = EnumHelper.addToolMaterial("YAMATO", 4, -1, 16.0F, 110.514F, 514);

    // TODO It would be the best way that mixin the
    // crazypants.enderio.teleport.TravelController.validatePacketTravelEvent because of its shit coding structure of
    // item checking.
    public static void rewriteEIOTravelStaffConfig() {
        travelStaffMaxBlinkDistance = teleportStaffMaxBlinkDistance;
        travelStaffMaxDistance = teleportStaffMaxDistance;
    }

    public ItemYamato(CreativeTabs aCreativeTabs) {
        super(YAMATO);
        // #tr item.Yamato.name
        // # Yamato
        // #zh_CN 阎魔刀
        this.setUnlocalizedName("Yamato");
        this.setCreativeTab(aCreativeTabs);
        this.setMaxDamage(0);
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
                }
                case 1 -> {
                    itemStackIn.setItemDamage(0);
                }
            }
            return itemStackIn;
        }

        if (worldIn.isRemote) {
            if (RewriteEIOTravelStaffConfig) {
                doTeleport(player);
            } else {
                TravelController.instance.doBlink(itemStackIn, player);
            }
            player.swingItem();
        }

        return itemStackIn;
    }

    public boolean doTeleport(EntityPlayer player) {
        Optional<BlockCoord> destinationOptional = TravelController.instance.findTeleportDestination(player);
        if (destinationOptional.isPresent()) {
            return TravelController.instance
                .travelToLocation(player, TravelSource.STAFF_BLINK, destinationOptional.get(), false);
        }
        return false;
    }

    @Override
    public boolean isActive(EntityPlayer ep, ItemStack equipped) {
        return true;
    }

    @Override
    public void extractInternal(ItemStack equipped, int power) {}

    @Override
    public int receiveEnergy(ItemStack itemStack, int i, boolean b) {
        return 0;
    }

    @Override
    public int extractEnergy(ItemStack itemStack, int i, boolean b) {
        return i;
    }

    @Override
    public int getEnergyStored(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int canExtractInternal(ItemStack equipped, int power) {
        return power;
    }
}
