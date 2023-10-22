package com.GTNH_Community.gtnhcommunitymod.common.material;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import gregtech.api.enums.TextureSet;

/**
 * Register new material here by Bartworks Material System
 */
public class MaterialPool implements Runnable {

    // ID manager
    private static final int offsetID_01 = 20_000;

    // TODO test demo : use Bartworks Werkstoff to new new Item(Material)
    public static final Werkstoff TestingMaterial = new Werkstoff(
        new short[] { 216, 240, 240 }, // RGB for auto texture
        "Testing Material", // Name
        subscriptNumbers("Tt"), // Chemical formula
        new Werkstoff.Stats().setBlastFurnace(true) // Auto generate the Properties , auto generate EBF recipe
            .setProtons(0)
            .setMass(0)
            .setMeltingPoint(11451) // EBF recipe's special value
            .setSpeedOverride(1919.0F) // Mining speed
            .setDurOverride(19198100) // Durability
            .setQualityOverride((byte) 114), // Mining Level
        Werkstoff.Types.ELEMENT, // Choose Type of generation , there 'ELEMENT' means a new matter
        new Werkstoff.GenerationFeatures().onlyDust() // What Pattern want to auto generate , onluDust means has Dust
                                                      // pattern
            .addMolten() // has Molten
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addMultipleIngotMetalWorkingItems()
            .addMetalCraftingSolidifierRecipes()
            .addMetaSolidifierRecipes(),
        offsetID_01, // manage ID here, some IDs has been used , read the README.md
        TextureSet.SET_SHINY); // What Type will the auto Texture be ,SHINY means like Palladium Ingot

    // Bartworks' Material System run on Runnable.class
    @Override
    public void run() {}
}
