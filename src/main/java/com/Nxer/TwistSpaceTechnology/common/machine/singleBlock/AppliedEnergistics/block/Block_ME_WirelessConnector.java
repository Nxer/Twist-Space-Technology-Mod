package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.AppliedEnergistics.block;

import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.AppliedEnergistics.block.base.TST_AEBaseTileBlock;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.AppliedEnergistics.tile.Tile_ME_WirelessConnector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Block_ME_WirelessConnector extends TST_AEBaseTileBlock {
    public Block_ME_WirelessConnector() {
        // #tr ME.WirelessConnector.0.name
        // # ME Wireless Connector
        // #zh_CN ME网络无线接入点
        super("ME.WirelessConnector");
        setTileEntity(Tile_ME_WirelessConnector.class);
    }

    @Override
    public boolean onActivated(World w, int x, int y, int z, EntityPlayer player, int facing, float hitX,
        float hitY, float hitZ) {

        if (player != null && !w.isRemote) {
            final ItemStack is = player.inventory.getCurrentItem();
            if (null == is) return false;


        }


        return false;
    }

}
