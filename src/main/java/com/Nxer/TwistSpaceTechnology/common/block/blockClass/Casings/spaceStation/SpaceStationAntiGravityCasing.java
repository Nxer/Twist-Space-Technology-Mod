package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation;

import static com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockStaticDataClientOnly.iconsSpaceStationAntiGravityCasingMap;

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

public class SpaceStationAntiGravityCasing extends BlockBase01 implements IHasTooltips {

    public SpaceStationAntiGravityCasing() {
        // #tr tile.SpaceStationAntiGravityBlock.name
        // # Space Station Anti Gravity Block
        // #zh_CN 空间站反重力方块
        super("SpaceStationAntiGravityBlock");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
        this.setCreativeTab(GTCMCreativeTabs.tabGTCMGeneralTab);
        SpaceStationAntiGravityCasingCasingSet.add(0);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    public static Set<Integer> SpaceStationAntiGravityCasingCasingSet = new HashSet<>();

    /**
     * Tooltips of these blocks' ItemBlock.
     */
    public static String[][] SpaceStationAntiGravityCasingTooltipsArray = new String[14][];

    @Override
    public void setTooltips(int metaValue, @Nullable String[] tooltips) {
        SpaceStationAntiGravityCasingTooltipsArray[metaValue] = tooltips;
    }

    @Override
    public @Nullable String[] getTooltips(int metaValue) {
        return SpaceStationAntiGravityCasingTooltipsArray[metaValue];
    }

    // endregion
    // -----------------------
    // region Meta Generator

    // endregion
    // -----------------------
    // region Getters

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < iconsSpaceStationAntiGravityCasingMap.size() ? iconsSpaceStationAntiGravityCasingMap.get(meta)
            : iconsSpaceStationAntiGravityCasingMap.get(0);
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
        for (int Meta : SpaceStationAntiGravityCasingCasingSet) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gtnhcommunitymod:SpaceStationAntiGravityCasing/0");
        for (int Meta : SpaceStationAntiGravityCasingCasingSet) {
            iconsSpaceStationAntiGravityCasingMap
                .put(Meta, reg.registerIcon("gtnhcommunitymod:SpaceStationAntiGravityCasing/" + Meta));
        }
    }

    // endregion
}
