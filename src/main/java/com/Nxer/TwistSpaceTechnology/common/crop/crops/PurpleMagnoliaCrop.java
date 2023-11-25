package com.Nxer.TwistSpaceTechnology.common.crop.crops;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.crop.BasicGTCMCrop;

import ic2.api.crops.ICropTile;

public class PurpleMagnoliaCrop extends BasicGTCMCrop {

    public PurpleMagnoliaCrop() {
        super();
    }

    @Override
    public int tier() {
        return 5;
    }

    @Override
    public int stat(int n) {
        switch (n) {
            case 0:
                return 2; // chemical usages
            case 1:
                return 0; // not edible
            case 2:
                return 0; // no defensive properties
            case 3:
                return 4; // quite colorful
            case 4:
                return 0; // not particularly weed-like
            default:
                return 0;
        }
    }

    @Override
    public int maxSize() {
        return 5;
    }

    @Override
    public ItemStack getDisplayItem() {
        return GTCMItemList.PurpleMagnoliaPetal.get(1);
    }

    @Override
    public String name() {
        return "Purple Magnolia";
    }

    @Override
    public String[] attributes() {
        return new String[] { "Purple", "Flower", "Magic" };
    }

    @Override
    public ItemStack getGain(ICropTile crop) {
        return GTCMItemList.PurpleMagnoliaPetal.get(1);
    }

}
