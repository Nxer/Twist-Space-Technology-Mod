package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockBase01;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.SpaceStationAntiGravityBlock;
import static com.Nxer.TwistSpaceTechnology.util.MetaItemStackUtils.initMetaItemStack;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

public class SpaceStationAntiGravityCasing extends BlockBase01 {

    public SpaceStationAntiGravityCasing() {
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
        this.setCreativeTab(GTCMCreativeTabs.tabGTCMGeneralTab);
        SpaceStationAntiGravityCasingCasingSet.add(0);
        GregTech_API.registerMachineBlock(this, -1);
    }

    public SpaceStationAntiGravityCasing(String unlocalizedName, String localName) {
        this();
        this.unlocalizedName = unlocalizedName;
        texter(localName, unlocalizedName + ".name");
    }

    public static Set<Integer> SpaceStationAntiGravityCasingCasingSet = new HashSet<>();

    /**
     * Tooltips of these blocks' ItemBlock.
     */
    public static String[][] SpaceStationAntiGravityCasingTooltipsArray = new String[14][];
    private IIcon blockIcon;
    private String unlocalizedName;

    // endregion
    // -----------------------
    // region Meta Generator

    public static ItemStack SpaceStationAntiGravityCasingMeta(String i18nName, int meta) {

        return initMetaItemStack(i18nName, meta, SpaceStationAntiGravityBlock, SpaceStationAntiGravityCasingCasingSet);
    }

    public static ItemStack SpaceStationAntiGravityCasingMeta(String i18nName, int meta, String[] tooltips) {
        // Handle the tooltips
        SpaceStationAntiGravityCasingTooltipsArray[meta] = tooltips;
        return SpaceStationAntiGravityCasingMeta(i18nName, meta);
    }

    // endregion
    // -----------------------
    // region Getters

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        return Block.getBlockById(1).getIcon(1,0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {


    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTabs, List list) {
        for (int Meta : SpaceStationAntiGravityCasingCasingSet) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

    // endregion
}
