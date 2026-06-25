package com.Nxer.TwistSpaceTechnology.common.cropsNH;

import java.awt.*;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.cropsNH.cropBases.TST_CropBase;
import com.gtnewhorizon.cropsnh.api.CropsNHSoilTypes;
import com.gtnewhorizon.cropsnh.api.IPlantRenderShape;
import com.gtnewhorizon.cropsnh.api.ISeedShape;
import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.api.PlantRenderShape;
import com.gtnewhorizon.cropsnh.api.SeedShape;

public class CropPurpleMagnolia extends TST_CropBase {

    public CropPurpleMagnolia() {
        // #tr cropsnh_crops.purpleMagnolia
        // # Purple Magnolia
        // #zh_CN 紫玉兰
        super("purpleMagnolia", Color.pink, Color.white);

        addDrop(GTCMItemList.PurpleMagnoliaPetal.get(1), 10000);
        addAlternateSeed(GTCMItemList.PurpleMagnoliaSapling.get(1));

    }

    @Override
    public String getCreator() {
        return "The_Flames";
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public int getGrowthDuration() {
        return 2000;
    }

    @Override
    public ISoilList getSoilTypes() {
        return CropsNHSoilTypes.thaumLogs;
    }

    @Override
    public IPlantRenderShape getRenderShape() {
        return PlantRenderShape.X;
    }

    @Override
    public ISeedShape getSeedShape() {
        return SeedShape.magic;
    }

    @Override
    public int getMaxGrowthStage() {
        return 3;
    }
}
