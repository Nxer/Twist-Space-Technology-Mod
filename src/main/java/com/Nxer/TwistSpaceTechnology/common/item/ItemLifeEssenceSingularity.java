package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.render.IHaloRenderItem;

public class ItemLifeEssenceSingularity extends Item implements IHaloRenderItem {

    @SideOnly(Side.CLIENT)
    private IIcon singularityBackground;

    @SideOnly(Side.CLIENT)
    private IIcon singularityHalo;

    public ItemLifeEssenceSingularity() {
        // #tr item.tst.life_essence_singularity.name
        // # Life Essence Singularity
        // #zh_CN 生命本源奇点
        setUnlocalizedName("tst.life_essence_singularity");
        setCreativeTab(TstCreativeTabs.TabGeneral);
        setTextureName("avaritia:singularity");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("avaritia:singularity");
        this.singularityBackground = register.registerIcon("avaritia:singularity2");
        this.singularityHalo = register.registerIcon("avaritia:halo");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass) {
        return renderPass == 0 ? singularityBackground : itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return renderPass == 0 ? 0x800000 : 0xC00000;
    }

    @Override
    public boolean drawHalo(ItemStack stack) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getHaloTexture(ItemStack stack) {
        return singularityHalo;
    }

    @Override
    public int getHaloSize(ItemStack stack) {
        return 4;
    }

    @Override
    public int getHaloColour(ItemStack stack) {
        return 0xFF000000;
    }

    @Override
    public boolean drawPulseEffect(ItemStack stack) {
        return false;
    }

}
