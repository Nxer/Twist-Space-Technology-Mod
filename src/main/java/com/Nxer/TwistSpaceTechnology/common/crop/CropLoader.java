package com.Nxer.TwistSpaceTechnology.common.crop;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.crop.crops.PurpleMagnoliaCrop;

import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;

// IC2API
// ItemsFromAPIs

public class CropLoader {

    private static List<CropLoader> list = cropLoader();
    private CropCard cropObj;
    private ItemStack baseseed;

    /*
     * This Class Loades Crops with Base Seed.
     * Call it with:
     * CropLoader.load(preinit);
     * at the preinit phase to load the crops into the config.
     * Then call it at postinit with
     * CropLoader.register();
     * to load the Crops into the game.
     */

    public CropLoader(CropCard cropObj) {
        this.cropObj = cropObj;
    }

    public CropLoader(CropCard cropObj, ItemStack baseseed) {
        this.cropObj = cropObj;
        this.baseseed = baseseed;
    }

    public static CropCard CropunpackerCC(CropLoader inp) {
        return inp.cropObj;
    }

    private static ItemStack CropunpackerCG(CropLoader inp) {
        return inp.baseseed;
    }

    public final static List<CropLoader> cropLoader() {

        List<CropLoader> p = new ArrayList<CropLoader>();

        /*
         * Add your crops with base seed here by
         * p.add(new CropLoader(new YourCropClass(),YourItem));
         */

        p.add(new CropLoader(new PurpleMagnoliaCrop(), GTCMItemList.PurpleMagnoliaSapling.get(1)));
        return p;
    }

    private final static List<CropCard> cropObjs() {
        List<CropCard> p = new ArrayList<CropCard>();
        for (int i = 0; i < list.size(); ++i) {
            p.add(CropunpackerCC(list.get(i)));
        }
        return p;
    }

    private final static List<ItemStack> setBaseSeed() {
        List<ItemStack> p = new ArrayList<ItemStack>();
        for (int i = 0; i < list.size(); ++i) {
            p.add(CropunpackerCG(list.get(i)));
        }
        return p;
    }

    public static void register() {
        for (int i = 0; i < list.size(); ++i) {
            if (cropObjs().get(i) != null) Crops.instance.registerCrop(cropObjs().get(i));
        }
    }

    public static void registerBaseSeed() {

        List<ItemStack> baseseed = new ArrayList<ItemStack>(setBaseSeed());

        for (int i = 0; i < list.size(); ++i) {
            if (baseseed.get(i) != null && cropObjs().get(i) != null)
                Crops.instance.registerBaseSeed(baseseed.get(i), cropObjs().get(i), 1, 1, 1, 1);
        }
    }
}
