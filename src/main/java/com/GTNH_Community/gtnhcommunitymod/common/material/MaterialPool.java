package com.GTNH_Community.gtnhcommunitymod.common.material;

import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import gregtech.api.enums.TextureSet;

/**
 * Register new material here by Bartworks Material System
 */
public class MaterialPool implements Runnable {

    /*----------- Test the forge item register -----------*/

    // public static ItemAdder_Basic testItem = new ItemAdder_Basic("Testing Item","testingItem.01");
    // public static ItemStack testItem4 = new ItemStack(testItem,1,1);

    /*----------- Test the forge item register -----------*/

    // ID manager
    private static final int offsetID_01 = 20_000;

    // TODO test demo : use Bartworks Werkstoff to new new Item(Material)
    public static final Werkstoff TestingMaterial = new Werkstoff(
        new short[] { 216, 240, 240 }, // RGB for auto texture
        // Name, if text need i18n, please use the method Texthandler.texter(String textAsDefault, String i18nKey)
        // Also don't at this mod translate the auto generator extend gt.materials
        // These material name will be the baseName.
        // Translate these text when in GregTech.lang .
        texter("Testing Material", "bw.Testing Material.notTrans"),
        subscriptNumbers("Tt"), // Chemical formula
        new Werkstoff.Stats().setBlastFurnace(true) // Auto generate the Properties , auto generate EBF recipe
            .setProtons(0)
            .setMass(0)
            .setMeltingPoint(11451) // EBF recipe's special value
            .setSpeedOverride(1919.0F) // Mining speed
            .setDurOverride(19198100) // Durability
            .setQualityOverride((byte) 114), // Mining Level
        Werkstoff.Types.ELEMENT, // Choose Type of generation , there 'ELEMENT' means a new matter
        // What Pattern want to auto generate , onlyDust means has Dust pattern not 'only'
        new Werkstoff.GenerationFeatures().onlyDust()
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
