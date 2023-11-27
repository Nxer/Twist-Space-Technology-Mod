package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class SpaceStationStructureCasingItemBlock extends ItemBlockBase01 {
    public SpaceStationStructureCasingItemBlock(Block aBlock) {
        super(aBlock);
        this.setCreativeTab(GTCMCreativeTabs.tabGTCMGeneralTab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @SuppressWarnings({ "unchecked" })
    public void addInformation(ItemStack aItemStack, EntityPlayer p_77624_2_, List theTooltipsList,
                               boolean p_77624_4_) {
        if (null == aItemStack) return;

        Collections.addAll(theTooltipsList, TextLocalization.TooltipsUpgrades[aItemStack.getItemDamage()]);

        theTooltipsList.add(mNoMobsToolTip);
        theTooltipsList.add(mNoTileEntityToolTip);
    }
}
