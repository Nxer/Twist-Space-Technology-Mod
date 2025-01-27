package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.SpaceStationStructureBlock;
import static com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockStaticDataClientOnly.iconsSpaceStationStructureCasingMap;
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
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockBase01;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;

public class SpaceStationStructureCasing extends BlockBase01 implements IHasTooltips {

    public SpaceStationStructureCasing() {
        // #tr tile.SpaceStationStructureBlock.name
        // # Space Station Structure Block
        // #zh_CN 空间站结构方块
        super("SpaceStationStructureBlock");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
        this.setCreativeTab(GTCMCreativeTabs.tabGTCMGeneralTab);
        SpaceStationStructureCasingCasingSet.add(0);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    public static Set<Integer> SpaceStationStructureCasingCasingSet = new HashSet<>();

    /**
     * Tooltips of these blocks' ItemBlock.
     */
    public static String[][] SpaceStationStructureCasingTooltipsArray = new String[14][];

    @Override
    public void setTooltips(int metaValue, @Nullable String[] tooltips) {
        SpaceStationStructureCasingTooltipsArray[metaValue] = tooltips;
    }

    @Override
    public @Nullable String[] getTooltips(int metaValue) {
        return SpaceStationStructureCasingTooltipsArray[metaValue];
    }

    // endregion
    // -----------------------
    // region Meta Generator

    @Deprecated
    public static ItemStack SpaceStationStructureCasingMeta(String i18nName, int meta) {

        return initMetaItemStack(i18nName, meta, SpaceStationStructureBlock, SpaceStationStructureCasingCasingSet);
    }

    @Deprecated
    public static ItemStack SpaceStationStructureCasingMeta(String i18nName, int meta, String[] tooltips) {
        // Handle the tooltips
        SpaceStationStructureCasingTooltipsArray[meta] = tooltips;
        return SpaceStationStructureCasingMeta(i18nName, meta);
    }

    // endregion
    // -----------------------
    // region Getters

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < iconsSpaceStationStructureCasingMap.size() ? iconsSpaceStationStructureCasingMap.get(meta)
            : iconsSpaceStationStructureCasingMap.get(0);
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTabs, List list) {
        for (int Meta : SpaceStationStructureCasingCasingSet) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gtnhcommunitymod:SpaceStationStructureCasing/0");
        for (int Meta : SpaceStationStructureCasingCasingSet) {
            iconsSpaceStationStructureCasingMap
                .put(Meta, reg.registerIcon("gtnhcommunitymod:SpaceStationStructureCasing/" + Meta));
        }
    }

    // endregion
}
