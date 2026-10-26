package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.render.IHaloRenderItem;

/**
 * An ItemStack Generator used Meta Item System.
 */
public class ItemAdder01 extends AbstractTstMetaItem implements IHaloRenderItem {

    @SideOnly(Side.CLIENT)
    private IIcon singularityBackground;

    @SideOnly(Side.CLIENT)
    private IIcon singularityHalo;

    /**
     * Create the basic item MetaItem01.
     */
    public ItemAdder01() {
        // #tr item.tst.common.MetaItem01.name
        // # Test Item
        // #zh_CN 测试物品
        super("MetaItem01");
        setTextureName("gtnhcommunitymod:MetaItem01/0");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.iconMap = registerAllVariantIcons(
            register,
            meta -> meta == 47 ? "avaritia:singularity"
                : TwistSpaceTechnology.RESOURCE_ROOT_ID + ":" + unlocalizedName + "/" + meta);
        this.itemIcon = iconMap.get(0);
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
        return meta == 47 ? 2 : 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass) {
        return stack.getItemDamage() == 47 && renderPass == 0 ? singularityBackground
            : super.getIcon(stack, renderPass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return stack.getItemDamage() == 47 ? (renderPass == 0 ? 0x800000 : 0xC00000)
            : super.getColorFromItemStack(stack, renderPass);
    }

    @Override
    public boolean drawHalo(ItemStack stack) {
        return stack.getItemDamage() == 47;
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
