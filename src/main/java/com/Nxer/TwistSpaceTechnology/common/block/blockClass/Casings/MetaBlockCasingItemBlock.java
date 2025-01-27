package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MetaBlockCasingItemBlock extends ItemBlockBase01 {

    public MetaBlockCasingItemBlock(Block p_i45328_1_) {
        super(p_i45328_1_);
        setHasSubtypes(true);
        setMaxDamage(0);
        this.setCreativeTab(GTCMCreativeTabs.TAB_META_BLOCKS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @SuppressWarnings({ "unchecked" })
    public void addInformation(ItemStack aItemStack, EntityPlayer p_77624_2_, List theTooltipsList,
        boolean p_77624_4_) {
        if (null == aItemStack) return;
        int meta = aItemStack.getItemDamage();

        MetaBlockCasing01.TOOLTIPS.put(0, new String[] { "TestCasing", "Test Casing" });
        MetaBlockCasing01.TOOLTIPS.put(1, TextLocalization.Tooltips_HighPowerRadiationProofCasing);
        MetaBlockCasing01.TOOLTIPS.put(2, TextLocalization.Tooltips_AdvancedHighPowerCoil);

        if (MetaBlockCasing01.TOOLTIPS.containsKey(meta)) {
            theTooltipsList.addAll(Arrays.asList(MetaBlockCasing01.TOOLTIPS.get(meta)));
        }
        theTooltipsList.add(mNoMobsToolTip);
        theTooltipsList.add(mNoTileEntityToolTip);
    }
}
