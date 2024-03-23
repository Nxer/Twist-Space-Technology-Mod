package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.AdvancedHighPowerCoilBlock;
import static com.Nxer.TwistSpaceTechnology.util.MetaItemStackUtils.initMetaItemStack;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import java.util.*;

import net.minecraft.client.renderer.texture.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import com.Nxer.TwistSpaceTechnology.client.*;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.*;

import cpw.mods.fml.relauncher.*;

public class AdvancedHighPowerCoil extends BlockBase01 {

    public AdvancedHighPowerCoil(String unlocalizedName, String localName) {
        this.setUnlocalizedName(unlocalizedName);
        texter(localName, unlocalizedName + ".name");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
        this.setCreativeTab(GTCMCreativeTabs.tabGTCMGeneralTab);
        AdvancedHighPowerCoilSet.add(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < BlockStaticDataClientOnly.iconsAdvancedHighPowerCoil.size()
            ? BlockStaticDataClientOnly.iconsAdvancedHighPowerCoil.get(meta)
            : BlockStaticDataClientOnly.iconsAdvancedHighPowerCoil.get(0);
    }

    public static final Set<Integer> AdvancedHighPowerCoilSet = new HashSet<>();

    public static ItemStack AdvancedCompactFusionCoilMeta(String i18nName, int meta) {
        return initMetaItemStack(i18nName, meta, AdvancedHighPowerCoilBlock, AdvancedHighPowerCoilSet);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTabs, List list) {
        for (int Meta : AdvancedHighPowerCoilSet) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gtnhcommunitymod:Special/0");
        for (int Meta : AdvancedHighPowerCoilSet) {
            BlockStaticDataClientOnly.iconsAdvancedHighPowerCoil
                .put(Meta, reg.registerIcon("gtnhcommunitymod:Special/" + Meta));
        }
    }
}
