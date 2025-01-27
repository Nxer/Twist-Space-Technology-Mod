package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import static net.minecraft.client.gui.GuiScreen.isShiftKeyDown;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockPowerChair extends ItemBlock {

    public ItemBlockPowerChair(Block p_i45328_1_) {
        super(p_i45328_1_);
        setHasSubtypes(true);
        setMaxDamage(0);
        this.setCreativeTab(GTCMCreativeTabs.TAB_META_BLOCKS);
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + this.getDamage(aStack);
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List<String> toolTip,
        final boolean advancedToolTips) {
        if (isShiftKeyDown()) {
            // #tr tooltips.PowerChair.page2.line1
            // # {\LIGHT_PURPLE}If you want it, then you'll have to take it.
            // #zh_CN {\LIGHT_PURPLE}If you want it, then you'll have to take it.
            toolTip.add(TextEnums.tr("tooltips.PowerChair.page2.line1"));
        } else {
            // #tr tooltips.PowerChair.page1.line1
            // # Your portal opening day's over.
            // #zh_CN Your portal opening day's over.
            toolTip.add(TextEnums.tr("tooltips.PowerChair.page1.line1"));
        }

    }
}
