package com.Nxer.TwistSpaceTechnology.common.ic2Crop;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.ic2Crop.crops.PurpleMagnoliaCrop;

import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;

public class CropInfo {

    private static final List<CropInfo> CROPS = new ArrayList<>();

    static {
        CROPS.add(new CropInfo(new PurpleMagnoliaCrop(), GTCMItemList.PurpleMagnoliaSapling.get(1)));
    }

    private final CropCard cropCard;
    private final ItemStack baseSeed;

    public CropInfo(CropCard cropCard, ItemStack baseSeed) {
        this.cropCard = cropCard;
        this.baseSeed = baseSeed;
    }

    public CropCard getCropCard() {
        return cropCard;
    }

    public ItemStack getBaseSeed() {
        return baseSeed;
    }

    public static void registerAllCropInfo() {
        for (CropInfo cropInfo : CROPS) {
            Crops.instance.registerCrop(cropInfo.getCropCard());
            Crops.instance.registerBaseSeed(cropInfo.getBaseSeed(), cropInfo.getCropCard(), 1, 1, 1, 1);
        }
    }

}
