package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.HighPowerRadiationProofBlock;
import static com.Nxer.TwistSpaceTechnology.util.MetaItemStackUtils.initMetaItemStack;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockBase01;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockStaticDataClientOnly;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.*;
import gregtech.api.util.*;
import gtPlusPlus.xmod.gregtech.api.enums.*;
import gtPlusPlus.xmod.gregtech.api.objects.*;

public class HighPowerRadiationProofCasing extends BlockBase01 {

    public HighPowerRadiationProofCasing(String unlocalizedName, String localName) {
        this.setUnlocalizedName(unlocalizedName);
        texter(localName, unlocalizedName + ".name");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
        this.setCreativeTab(GTCMCreativeTabs.tabGTCMGeneralTab);
        HighPowerRadiationProofCasingSet.add(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < BlockStaticDataClientOnly.iconsHighPowerRadiationProofCasing.size()
            ? BlockStaticDataClientOnly.iconsHighPowerRadiationProofCasing.get(meta)
            : BlockStaticDataClientOnly.iconsHighPowerRadiationProofCasing.get(0);
    }

    public static final Set<Integer> HighPowerRadiationProofCasingSet = new HashSet<>();

    public static ItemStack HighPowerRadiationProofCasingMeta(String i18nName, int meta) {
        return initMetaItemStack(i18nName, meta, HighPowerRadiationProofBlock, HighPowerRadiationProofCasingSet);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTabs, List list) {
        for (int Meta : HighPowerRadiationProofCasingSet) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gtnhcommunitymod:StructureBlock/0");
        for (int Meta : HighPowerRadiationProofCasingSet) {
            BlockStaticDataClientOnly.iconsHighPowerRadiationProofCasing
                .put(Meta, reg.registerIcon("gtnhcommunitymod:StructureBlock/" + Meta));
        }
    }
}
