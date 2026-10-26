package com.Nxer.TwistSpaceTechnology.common.block.meta;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.client.render.BottledChaosCapacityRenderer;
import com.gtnewhorizon.structurelib.StructureLibAPI;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;

/** Five capacity tiers; each contiguous column selects its sixteen side textures by height. */
public class BottledChaosCapacityBlock extends AbstractTstMetaBlock {

    public static final String STRUCTURE_CHANNEL = "bottledchaoscapacity";

    public void registerStructureChannel() {
        // #tr tst.bottledchaos.structure.capacity_channel
        // # Bottled Chaos Capacity: 1 = Air, 2-6 = Capacity Blocks I-V
        // #zh_CN 瓶中混沌容量区: 1 = 空气, 2-6 = 容量方块 I-V
        StructureLibAPI.registerChannelDescription(
            STRUCTURE_CHANNEL,
            TwistSpaceTechnology.MODID,
            "tst.bottledchaos.structure.capacity_channel");
        for (int tier = 0; tier < 5; tier++) {
            StructureLibAPI.registerChannelItem(
                STRUCTURE_CHANNEL,
                TwistSpaceTechnology.MODID,
                tier + 2,
                new ItemStack(this, 1, tier));
        }
    }

    @SideOnly(Side.CLIENT)
    private IIcon[][][] sideIcons;
    @SideOnly(Side.CLIENT)
    private IIcon[][] capIcons;

    public BottledChaosCapacityBlock() {
        super("BottledChaosCapacity");
        GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public String getUnlocalizedName() {
        return "tst.bottledchaos.capacity_block";
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
        sideIcons = new IIcon[5][5][];
        capIcons = new IIcon[5][];
        for (int tier = 0; tier < sideIcons.length; tier++) {
            String path = RESOURCE_ROOT_ID + ":BottledChaosCapacity/" + (tier + 1);
            for (int variant = 0; variant < 5; variant++) {
                sideIcons[tier][variant] = BottledChaosCapacityRenderer
                    .splitAtlas(register.registerIcon(path + "/side_" + variant), 16);
            }
            capIcons[tier] = BottledChaosCapacityRenderer.splitAtlas(register.registerIcon(path + "/caps"), 10);
        }
        blockIcon = sideIcons[0][1][0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return getCapacityIcon(side, meta, 0, 1);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getCapacityIcon(int side, int meta, int layer, int variant) {
        int tier = meta >= 0 && meta < sideIcons.length ? meta : 0;
        return side < 2 ? capIcons[tier][(side == 0 ? 5 : 0) + variant] : sideIcons[tier][variant][layer];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        return BottledChaosCapacityRenderer
            .getWorldIcon(getCapacityIcon(side, meta, side < 2 ? 0 : getColumnLayer(world, x, y, z), 1));
    }

    @SideOnly(Side.CLIENT)
    public int getColumnLayer(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        IBlockAccess column = BottledChaosCapacityRenderer.getColumnAccess(world);
        int layer = 0;
        while (layer < 15 && y > layer
            && column.getBlock(x, y - layer - 1, z) == this
            && column.getBlockMetadata(x, y - layer - 1, z) == meta) layer++;
        return layer;
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
        // A changed base affects the next fifteen layers; include their connected neighbors as well.
        if (world.isRemote) world.markBlockRangeForRenderUpdate(x - 1, y - 1, z - 1, x + 1, y + 16, z + 1);
        return true;
    }
}
