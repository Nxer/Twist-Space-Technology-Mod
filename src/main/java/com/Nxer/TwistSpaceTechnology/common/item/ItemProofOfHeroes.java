package com.Nxer.TwistSpaceTechnology.common.item;

import static net.minecraft.client.gui.GuiScreen.isShiftKeyDown;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.util.CraftedTokens;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemProofOfHeroes extends Item {

    public EnumRarity rarity;

    public ItemProofOfHeroes(String aMetaName, EnumRarity rarity, CreativeTabs aCreativeTabs) {
        super();
        this.setCreativeTab(aCreativeTabs);
        this.setUnlocalizedName(aMetaName);
        this.rarity = rarity;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName.equals("ProofOfGods") ? "item.tst.common.proof_of_gods"
            : "item.tst.common.proof_of_heroes";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return this.rarity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTabs, List<ItemStack> aList) {
        aList.add(new ItemStack(aItem, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List<String> toolTip,
        final boolean advancedToolTips) {
        if (rarity == EnumRarity.common) {
            if (isShiftKeyDown()) {
                toolTip.add(
                    // #tr item.tst.common.proof_of_heroes.tooltip.02
                    // # {\LIGHT_PURPLE}The physical culmination of your journey, capable to rend gods asunder.
                    // #zh_CN {\LIGHT_PURPLE}你旅途的怨种顶点，拥有使神明笑嘻的力量.
                    TSTUtils.tr("item.tst.common.proof_of_heroes.tooltip.02"));
            } else {
                toolTip.add(
                    // #tr item.tst.common.proof_of_heroes.tooltip.01
                    // # The physical culmination of your journey, capable to rend gods asunder.
                    // #zh_CN 你旅途的物理顶点，拥有使神明分崩离析的力量.
                    TSTUtils.tr("item.tst.common.proof_of_heroes.tooltip.01"));
            }
        } else {
            if (isShiftKeyDown()) {
                toolTip.add(
                    // #tr item.tst.common.proof_of_gods.tooltip.02
                    // # {\LIGHT_PURPLE}Go touch some grass
                    // #zh_CN {\LIGHT_PURPLE}多留意下窗外的碧水蓝天
                    TSTUtils.tr("item.tst.common.proof_of_gods.tooltip.02"));
                toolTip.add("");
                // #tr item.tst.common.proof_of_gods.tooltip.03
                // # The first people to make a TwistToken
                // #zh_CN 成功登顶的人们
                toolTip.add(EnumChatFormatting.WHITE + TSTUtils.tr("item.tst.common.proof_of_gods.tooltip.03"));
                toolTip.addAll(CraftedTokens.getAllName());
            } else {
                toolTip.add(
                    // #tr item.tst.common.proof_of_gods.tooltip.01
                    // # Impossible final goal
                    // #zh_CN 可能，也许，最后……
                    TSTUtils.tr("item.tst.common.proof_of_gods.tooltip.01"));
            }
        }
    }
}
