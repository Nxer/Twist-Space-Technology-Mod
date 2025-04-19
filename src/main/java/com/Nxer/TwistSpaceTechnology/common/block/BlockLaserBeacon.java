package com.Nxer.TwistSpaceTechnology.common.block;

import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.entity.TileEntityLaserBeacon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;

public class BlockLaserBeacon extends Block implements ITileEntityProvider {

    protected IIcon topBlockIcon;
    public static IIcon[] textures;
    public static Textures.BlockIcons.CustomIcon LASER_BEACON = new Textures.BlockIcons.CustomIcon(
        "gtnhcommunitymod:iconSets/LASER_BEACON");

    public BlockLaserBeacon() {
        super(Material.iron);
        setBlockName("LaserBeacon");
        this.setCreativeTab(GregTechAPI.TAB_GREGTECH);
        // #tr LaserBeacon.name
        // # Laser Inducing Beacon
        // #zh_CN 激光激发信标
        GregTechAPI.registerMachineBlock(this, -1);
        GameRegistry.registerBlock(this, ItemBlockLaserBeacon.class, getUnlocalizedName());
        GTCMItemList.Laser_Beacon.set(new ItemStack(this, 1));
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTechAPI.isMachineBlock(this, aMetaData)) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 2;
    }

    @Override
    public String getUnlocalizedName() {
        return "LaserBeacon";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR.getIcon();
        topBlockIcon = LASER_BEACON.getIcon();
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? topBlockIcon : blockIcon;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return 1.0f;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityLaserBeacon();
    }

    public static class ItemBlockLaserBeacon extends ItemBlock {

        public ItemBlockLaserBeacon(Block block) {
            super(block);
        }

        @Override
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean f3_h) {
            // #tr laserbeacontooltip
            // # Produces a high precision laser from a powerful current.
            // #zh_CN 用高电流产生高精度激光.
            tooltip.add(tr("laserbeacontooltip"));
        }
    }
}
