package com.GTNH_Community.gtnhcommunitymod.loader;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import gregtech.api.enums.TextureSet;

/**
 * New Material Pool
 * Used Bartworks Werkstoff system
 *
 */
public class MaterialLoader implements Runnable {

    // ID manager
    private static final int offsetID_01 = 20_000;

    public static final Werkstoff TestingMaterial = new Werkstoff(
        new short[] { 114, 114, 114 },
        "Testing Material",
        subscriptNumbers("Ts"),
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
