package com.GTNH_Community.gtnhcommunitymod.common.material;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import gregtech.api.enums.TextureSet;

public class MaterialPool implements Runnable {

    // ID manager
    private static final int offsetID_01 = 20_000;

    // TODO test demo : use Bartworks Werkstoff to new new Item(Material)
    public static final Werkstoff TestingMaterial = new Werkstoff(
        new short[] { 216, 240, 240 },
        "Testing Material",
        subscriptNumbers("Tt"),
        new Werkstoff.Stats().setBlastFurnace(true)
            .setProtons(0)
            .setMass(0)
            .setMeltingPoint(11451)
            .setSpeedOverride(1919.0F)
            .setDurOverride(19198100)
            .setQualityOverride((byte) 114),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addMultipleIngotMetalWorkingItems()
            .addMetalCraftingSolidifierRecipes()
            .addMetaSolidifierRecipes(),
        offsetID_01,
        TextureSet.SET_SHINY);

    @Override
    public void run() {}
}
