package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockStaticDataClientOnly {

    @SideOnly(Side.CLIENT)
    public static Map<Integer, IIcon> iconsBlockMap01 = new HashMap<>();
    @SideOnly(Side.CLIENT)
    public static Map<Integer, IIcon> iconsBlockPhotonControllerUpgradeMap = new HashMap<>();
    @SideOnly(Side.CLIENT)
    public static Map<Integer, IIcon> iconsSpaceStationAntiGravityCasingMap = new HashMap<>();
    @SideOnly(Side.CLIENT)
    public static Map<Integer, IIcon> iconsSpaceStationStructureCasingMap = new HashMap<>();
    @SideOnly(Side.CLIENT)
    public static Map<Integer, IIcon> iconsNuclearReactor = new HashMap<>();
    @SideOnly(Side.CLIENT)
    public static Map<Integer, IIcon> iconsHighPowerRadiationProofCasing = new HashMap<>();
    @SideOnly(Side.CLIENT)
    public static Map<Integer, IIcon> iconsAdvancedHighPowerCoil = new HashMap<>();

}
