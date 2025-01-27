package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;

public class ItemHatchUpdateTool extends Item {

    public ItemHatchUpdateTool(String aMetaName, CreativeTabs aCreativeTabs) {
        super();
        this.setCreativeTab(aCreativeTabs);
        this.unlocalizedName = aMetaName;
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

    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        MovingObjectPosition position = Minecraft.getMinecraft().objectMouseOver;
        TileEntity tileEntity = worldIn.getTileEntity(position.blockX, position.blockY, position.blockZ);
        LOG.info(
            "Triggered TileEntity: " + tileEntity.getBlockType()
                .getLocalizedName());
        if (tileEntity instanceof BaseMetaTileEntity te) {
            if (te.getMetaTileEntity() instanceof MTEHatch hatch) {
                chainUpdateHatch(worldIn, player, position.blockX, position.blockY, position.blockZ, hatch);
            }
        }

        return super.onItemRightClick(itemStackIn, worldIn, player);
    }

    public static boolean chainUpdateHatch(World worldIn, EntityPlayer player, int x, int y, int z, MTEHatch hatch) {
        int tier = hatch.mTier;
        int id = hatch.getBaseMetaTileEntity()
            .getMetaTileID();
        LOG.info("Triggered MetaTileEntity Tiered: " + tier + " meta: " + id);
        InventoryPlayer playerItem = player.inventory;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = playerItem.getStackInSlot(slot);
            if (item != null && item.getItem() != null && item.getItem() instanceof ItemBlock newblock) {
                LOG.info(
                    "Triggered ItemBlock: " + newblock.field_150939_a.getLocalizedName()
                        + "."
                        + newblock.getDamage(item));
                if (newblock.field_150939_a.getClass()
                    .equals(
                        worldIn.getBlock(x, y, z)
                            .getClass())) {
                    if (newblock.getDamage(item) > id && newblock.getDamage(item) <= id - tier + 15) {
                        player.dropItem(
                            worldIn.getBlock(x, y, z)
                                .getItem(worldIn, x, y, z),
                            1);
                        // worldIn.setBlock(position.blockX, position.blockY, position.blockZ, newblock.field_150939_a);
                        NBTTagCompound nbt = new NBTTagCompound();
                        hatch.getBaseMetaTileEntity()
                            .writeToNBT(nbt);
                        nbt.setTag("Inventory", null);
                        worldIn.setBlock(x, y, z, Block.getBlockById(0));
                        newblock.placeBlockAt(item, player, worldIn, x, y, z, 0, 0, 0, 0, newblock.getDamage(item));
                        TileEntity tileEntity = worldIn.getTileEntity(x, y, z);
                        tileEntity.readFromNBT(nbt);
                        LOG.info(nbt.toString());
                        ((BaseMetaTileEntity) tileEntity).getMetaTileEntity()
                            .onPreTick((BaseMetaTileEntity) tileEntity, 0);

                        updateHatchController(worldIn);
                        item.stackSize--;
                        if (item.stackSize <= 0) {
                            playerItem.setInventorySlotContents(slot, null);
                            return true;
                        }
                        // for (int i = -1; i <= 1; i++) {
                        // for (int j = -1; j <= 1; j++) {
                        // for (int k = -1; k <= 1; k++) {
                        // if (i == 0 && j == 0 && k == 0) continue;
                        // TileEntity nearTile = worldIn.getTileEntity(x + i, y + j, z + k);
                        // LOG.info("Triggered TileEntity: " + tileEntity.getBlockType().getLocalizedName());
                        // if (tileEntity instanceof BaseMetaTileEntity te) {
                        // if (te.getMetaTileEntity() instanceof MTEHatch nearHatch) {
                        // if (tier == nearHatch.mTier) continue;
                        // if (!chainUpdateHatch(worldIn, player, x + i, y + j, z + k, hatch)) {
                        // return false;
                        // }
                        //
                        // }
                        // }
                        // }
                        // }
                        // }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void updateHatchController(World worldIn) {
        MovingObjectPosition position = Minecraft.getMinecraft().objectMouseOver;
        int x = position.blockX, y = position.blockY, z = position.blockZ;
        for (int i = -8; i < 8; i++) {
            for (int j = -8; j < 8; j++) {
                for (int k = -8; k < 8; k++) {
                    TileEntity tileEntity = worldIn.getTileEntity(x + i, y + j, z + k);
                    if (tileEntity instanceof BaseMetaTileEntity te) {
                        if (te.getMetaTileEntity() instanceof MTEMultiBlockBase machine) {
                            LOG.info("Find nearby machine named: " + machine.getLocalName());
                            LOG.info("is it ok after force replace and check? " + machine.checkStructure(true));
                        }
                    }
                }
            }
        }
    }

    public static boolean chainUpdateBlock(ItemStack itemStackIn, World worldIn, EntityPlayer player, int x, int y,
        int z, Block block) {
        return false;
    }
}
