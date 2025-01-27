package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.nuclear;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.NuclearReactorBlock;
import static com.Nxer.TwistSpaceTechnology.util.MetaItemStackUtils.initMetaItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockBase01;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockStaticDataClientOnly;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNuclearReactor extends BlockBase01 {

    public BlockNuclearReactor() {
        // #tr tile.MegaNuclearReactor.name
        // # Mega Nuclear Reactor
        // #zh_CN Mega Nuclear Reactor
        super("MegaNuclearReactor");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
        this.setCreativeTab(GTCMCreativeTabs.tabGTCMGeneralTab);
        NuclearReactorBlockSet.add(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < BlockStaticDataClientOnly.iconsNuclearReactor.size()
            ? BlockStaticDataClientOnly.iconsNuclearReactor.get(meta)
            : BlockStaticDataClientOnly.iconsNuclearReactor.get(0);
    }

    public static class innerItemBlock extends ItemBlockBase01 {

        public innerItemBlock(Block aBlock) {
            super(aBlock);
        }
    }

    public static final Set<Integer> NuclearReactorBlockSet = new HashSet<>();

    @Deprecated
    public static ItemStack NuclearReactorBlockMeta(String i18nName, int meta) {

        return initMetaItemStack(i18nName, meta, NuclearReactorBlock, NuclearReactorBlockSet);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTabs, List list) {
        for (int Meta : NuclearReactorBlockSet) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gtnhcommunitymod:nuclear/0");
        for (int Meta : NuclearReactorBlockSet) {
            BlockStaticDataClientOnly.iconsNuclearReactor
                .put(Meta, reg.registerIcon("gtnhcommunitymod:nuclear/" + Meta));
        }
    }
}
