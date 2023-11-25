package com.Nxer.TwistSpaceTechnology.common.crop;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import speiger.src.crops.api.ICropCardInfo;

public abstract class BasicGTCMCrop extends CropCard implements ICropCardInfo {

    @SideOnly(Side.CLIENT)
    public void registerSprites(IIconRegister iconRegister) {
        textures = new IIcon[maxSize()];

        for (int i = 1; i <= textures.length; i++) {
            // ic2:crop/blockCrop.NAME.n is the legacy name for backwards compatiblity
            textures[i - 1] = iconRegister.registerIcon("gtnhcommunitymod:crop/blockCrop." + name() + "." + i);
        }
    }


    @Override
    public float dropGainChance() {
        return (float) (Math.pow(0.95, (float) tier()));
    }

    @Override
    public boolean canCross(ICropTile crop) {
        return crop.getSize() == maxSize();
    }

    @Override
    public boolean canBeHarvested(ICropTile crop) {
        return crop.getSize() == maxSize();
    }

    @Override
    public int getOptimalHavestSize(ICropTile crop) {
        return maxSize();
    }

    @Override
    public int getrootslength(ICropTile crop) {
        return 5;
    }

    @Override
    public String discoveredBy() {
        return "TwistSpaceTechnology Team";
    }

    @Override
    public String owner() {
        return "TwistSpaceTechnology";
    }

    @Override
    public List<String> getCropInformation() {
        return null;
    }

    public ItemStack getDisplayItem() {
        return OreDict.ISget("crop" + this.name());
    }

}
