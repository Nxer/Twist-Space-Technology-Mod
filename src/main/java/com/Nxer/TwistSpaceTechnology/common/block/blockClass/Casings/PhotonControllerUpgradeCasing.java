package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings;

import static com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockStaticDataClientOnly.iconsBlockPhotonControllerUpgradeMap;
import static com.Nxer.TwistSpaceTechnology.config.Config.PhotonControllerUpgradeCasingSpeedIncrement;

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

public class PhotonControllerUpgradeCasing extends BlockBase01 implements IHasTooltips {

    // region Constructors

    public PhotonControllerUpgradeCasing() {
        // #tr tile.PhotonControllerUpgrades.name
        // # Photon Controller Upgrade
        // #zh_CN 光子掌控者升级
        super("PhotonControllerUpgrades");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
        this.setCreativeTab(GTCMCreativeTabs.tabGTCMGeneralTab);
        PhotonControllerUpgradeCasingSet.add(0);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    // endregion
    // -----------------------
    // region Member Variables

    public static Set<Integer> PhotonControllerUpgradeCasingSet = new HashSet<>();

    /**
     * The Speed Increment of the Upgrade Casing.
     * Total Speed Increment is the sum of each Upgrade Casing.
     * Total value 10,000 means time cost *0.5 .
     */
    public static int[] speedIncrement = PhotonControllerUpgradeCasingSpeedIncrement;
    // public static int[] speedIncrement = new int[] { /* LV */100, /* MV */200, /* HV */300, /* EV */400, /* IV */500,
    // /* LuV */1000, /* ZPM */2000, /* UV */4000, /* UHV */7000, /* UEV */10000, /* UIV */14000, /* UMV */19000,
    // /* UXV */25000, /* MAX */32000 };

    /**
     * Tooltips of these blocks' ItemBlock.
     */
    public static String[][] PhCUpgradeCasingTooltipsArray = new String[14][];
    private IIcon blockIcon;

    @Override
    public void setTooltips(int metaValue, @Nullable String[] tooltips) {
        PhCUpgradeCasingTooltipsArray[metaValue] = tooltips;
    }

    @Override
    public @Nullable String[] getTooltips(int metaValue) {
        return PhCUpgradeCasingTooltipsArray[metaValue];
    }

    // endregion
    // -----------------------
    // region Meta Generator

    // endregion
    // -----------------------
    // region Getters

    public int getSpeedIncrement(int meta) {
        return speedIncrement[meta];
    }

    // endregion
    // -----------------------
    // region Setters

    public void setSpeedIncrement(int speedIncrement, int meta) {
        PhotonControllerUpgradeCasing.speedIncrement[meta] = speedIncrement;
    }

    // endregion
    // -----------------------
    // region Overrides
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < iconsBlockPhotonControllerUpgradeMap.size() ? iconsBlockPhotonControllerUpgradeMap.get(meta)
            : iconsBlockPhotonControllerUpgradeMap.get(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gtnhcommunitymod:PhotonControllerUpgrades/0");
        for (int Meta : PhotonControllerUpgradeCasingSet) {
            iconsBlockPhotonControllerUpgradeMap
                .put(Meta, reg.registerIcon("gtnhcommunitymod:PhotonControllerUpgrades/" + Meta));
        }
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
        for (int Meta : PhotonControllerUpgradeCasingSet) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

    // endregion

}
