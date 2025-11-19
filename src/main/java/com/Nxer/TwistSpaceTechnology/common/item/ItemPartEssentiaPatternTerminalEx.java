package com.Nxer.TwistSpaceTechnology.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.tile.PartEssentiaPatternTerminalEx;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import appeng.api.AEApi;
import appeng.api.parts.IPartItem;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPartEssentiaPatternTerminalEx extends net.minecraft.item.Item implements IPartItem {

    public ItemPartEssentiaPatternTerminalEx() {
        this.setMaxStackSize(64);
        this.setCreativeTab(TstCreativeTabs.TabGeneral);
        this.setUnlocalizedName("item_part_essentia_pattern_terminal_ex");
        AEApi.instance()
            .partHelper()
            .setItemBusRenderer(this);
    }

    @Nullable
    @Override
    public PartEssentiaPatternTerminalEx createPartFromItemStack(ItemStack stack) {
        return new PartEssentiaPatternTerminalEx(stack);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float xOffset, float yOffset, float zOffset) {

        return AEApi.instance()
            .partHelper()
            .placeBus(stack, x, y, z, side, player, world);
    }

    @Override
    public void registerIcons(IIconRegister _iconRegister) {
        // Use Part to render the texture, and there is no need to manually load the icons.
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return 0;
    }

    public ItemPartEssentiaPatternTerminalEx register() {
        // #tr item.item_part_essentia_pattern_terminal_ex.name
        // #en_US TST Fluid and Essentia Pattern Terminal
        // #zh_CN TST增广流体源质样板终端
        GameRegistry
            .registerItem(this, TextEnums.tr("item_part_essentia_pattern_terminal_ex"), TwistSpaceTechnology.MODID);
        return this;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        // spotless:off
        // #tr item.essentia.pattern.terminal.tooltip.0
        // #en_US Craft Through Fun™ — TST Pattern Terminal
        // #zh_CN 欢乐就是TST牌终端
        list.add(EnumChatFormatting.GOLD + TextEnums.tr("item.essentia.pattern.terminal.tooltip.0"));
        // #tr item.essentia.pattern.terminal.tooltip.1
        // #en_US Functions identically to the Fluid Pattern Terminal Ex, but additionally provides the ability to replace certain items in a pattern with their corresponding Crystal Essentia.
        // #zh_CN 同ME增广流体样板终端功能一致,但是额外添加了可以将样板中某些物品替换为对应的晶化源质的功能
        list.add(TextEnums.tr("item.essentia.pattern.terminal.tooltip.1"));
        // #tr item.essentia.pattern.terminal.tooltip.2
        // #en_US The replacements mainly involve two categories of recipes: (1) phials that contain Essentia, and (2) Essentia items from NEI.
        // #zh_CN 主要包括两类物品,第一类为含有源质的玻璃安瓿,第二类为NEI的源质物品
        list.add(TextEnums.tr("item.essentia.pattern.terminal.tooltip.2"));
        // spotless:on
    }
}
