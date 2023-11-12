package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PhotonControllerUpgradeCasingItemBlock extends ItemBlockBase01 {

    public PhotonControllerUpgradeCasingItemBlock(Block aBlock) {
        super(aBlock);
        this.setCreativeTab(GTCMCreativeTabs.tabGTCMGeneralTab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @SuppressWarnings({ "unchecked" })
    public void addInformation(ItemStack aItemStack, EntityPlayer p_77624_2_, List theTooltipsList,
        boolean p_77624_4_) {
        if (null == aItemStack) return;

        for (String s : TextLocalization.TooltipsUpgrades[aItemStack.getItemDamage()]) {
            theTooltipsList.add(s);
        }

        theTooltipsList.add(mNoMobsToolTip);
        theTooltipsList.add(mNoTileEntityToolTip);
    }
}
