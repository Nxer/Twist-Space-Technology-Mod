package com.Nxer.TwistSpaceTechnology.common.block.meta;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.render.BottledChaosCapacityRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;

/** Five capacity tiers; each contiguous column selects its sixteen side textures by height. */
public class BottledChaosCapacityBlock extends AbstractTstMetaBlock {

    @SideOnly(Side.CLIENT)
    private IIcon[][] sideIcons;
    @SideOnly(Side.CLIENT)
    private IIcon[] topIcons;
    @SideOnly(Side.CLIENT)
    private IIcon[] bottomIcons;

    public BottledChaosCapacityBlock() {
        super("BottledChaosCapacity");
        GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public String getHarvestTool(int meta) {
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int meta) {
        return 1;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return Blocks.iron_block.getBlockHardness(world, x, y, z);
    }

    @Override
    public float getExplosionResistance(Entity entity) {
        return Blocks.iron_block.getExplosionResistance(entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BottledChaosCapacityRenderer.RENDER_ID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        sideIcons = new IIcon[5][16];
        topIcons = new IIcon[5];
        bottomIcons = new IIcon[5];
        for (int tier = 0; tier < sideIcons.length; tier++) {
            String path = RESOURCE_ROOT_ID + ":"
                + (tier == 0 ? "EnergyStorageLightBlue" : "EnergyStorageT" + (tier + 1));
            for (int layer = 0; layer < sideIcons[tier].length; layer++) {
                sideIcons[tier][layer] = register.registerIcon(path + "/layer_" + (layer < 10 ? "0" : "") + layer);
            }
            topIcons[tier] = register.registerIcon(path + "/top");
            bottomIcons[tier] = tier == 0 ? topIcons[tier] : register.registerIcon(path + "/bottom");
        }
        blockIcon = sideIcons[0][0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        int tier = meta >= 0 && meta < sideIcons.length ? meta : 0;
        return switch (side) {
            case 0 -> bottomIcons[tier];
            case 1 -> topIcons[tier];
            default -> sideIcons[tier][0];
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        if (side < 2) return getIcon(side, meta);
        IBlockAccess column = BottledChaosCapacityRenderer.getColumnAccess(world);
        int layer = 0;
        while (layer < 15 && y > layer
            && column.getBlock(x, y - layer - 1, z) == this
            && column.getBlockMetadata(x, y - layer - 1, z) == meta) layer++;
        return sideIcons[meta >= 0 && meta < sideIcons.length ? meta : 0][layer];
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if (!world.isRemote) GregTechAPI.causeMachineUpdate(world, x, y, z);
        refreshColumn(world, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMeta) {
        super.breakBlock(world, x, y, z, oldBlock, oldMeta);
        if (!world.isRemote) GregTechAPI.causeMachineUpdate(world, x, y, z);
        refreshColumn(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        super.onNeighborBlockChange(world, x, y, z, neighbor);
        refreshColumn(world, x, y, z);
    }

    private void refreshColumn(World world, int x, int y, int z) {
        if (world.isRemote) return;
        // Removal callbacks see the replacement block; the remaining upper column can carry the event.
        if (world.getBlock(x, y, z) != this) y++;
        if (world.getBlock(x, y, z) == this) world.addBlockEvent(x, y, z, this, 0, 0);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventParameter) {
        if (eventId != 0) return super.onBlockEventReceived(world, x, y, z, eventId, eventParameter);
        // A changed base affects the next fifteen layers; include their CTM neighbors as well.
        if (world.isRemote) world.markBlockRangeForRenderUpdate(x - 1, y - 1, z - 1, x + 1, y + 16, z + 1);
        return true;
    }
}
