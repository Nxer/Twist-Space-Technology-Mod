package com.Nxer.TwistSpaceTechnology.common.material;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;

import com.Nxer.TwistSpaceTechnology.util.TextHandler;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.util.Pair;

import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
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
    /*
     * public static final Werkstoff TestingMaterial = new Werkstoff(
     * new short[] { 216, 240, 240 }, // RGB for auto texture
     * // Name, if text need i18n, please use the method Texthandler.texter(String textAsDefault, String i18nKey)
     * // Also don't at this mod translate the auto generator extend gt.materials
     * // These material name will be the baseName.
     * // Translate these text when in GregTech.lang .
     * TextHandler.texter("Testing Material", "bw.Testing Material.notTrans"),
     * subscriptNumbers("Tt"), // Chemical formula
     * new Werkstoff.Stats().setBlastFurnace(true) // Auto generate the Properties , auto generate EBF recipe
     * .setProtons(0)
     * .setMass(0)
     * .setMeltingPoint(11451) // EBF recipe's special value
     * .setSpeedOverride(1919.0F) // Mining speed
     * .setDurOverride(19198100) // Durability
     * .setQualityOverride((byte) 114), // Mining Level
     * Werkstoff.Types.ELEMENT, // Choose Type of generation , there 'ELEMENT' means a new matter
     * // What Pattern want to auto generate , onlyDust means has Dust pattern not 'only'
     * new Werkstoff.GenerationFeatures().onlyDust()
     * .addMolten() // has Molten
     * .addMetalItems()
     * .addCraftingMetalWorkingItems()
     * .addSimpleMetalWorkingItems()
     * .addMultipleIngotMetalWorkingItems()
     * .addMetalCraftingSolidifierRecipes()
     * .addMetaSolidifierRecipes(),
     * offsetID_01, // manage ID here, some IDs has been used , read the README.md
     * TextureSet.SET_SHINY); // What Type will the auto Texture be ,SHINY means like Palladium Ingot
     */

    public static final Werkstoff HolmiumGarnet = new Werkstoff(
        new short[] { 96, 96, 240 },
        TextHandler.texter("Holmium Garnet", "bw.HolmiumGarnet.notTrans"),
        subscriptNumbers("Ho3Al5O12"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        offsetID_01 + 1,
        TextureSet.SET_SHINY);

    public static final Werkstoff FluxedElementium = new Werkstoff(
        new short[] { 218, 112, 214 },
        TextHandler.texter("FluxedElementium", "bw.FluxedElementium.notTrans"),
        subscriptNumbers("EfMa3"),
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(5300)
            .setSpeedOverride(60.0F)
            .setDurOverride(2180000)
            .setQualityOverride((byte) 8), 
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addMultipleIngotMetalWorkingItems()
            .addMetalCraftingSolidifierRecipes()
            .addMetaSolidifierRecipes()
            .addCasings().removeOres(),
        offsetID_01 + 2,
        TextureSet.SET_SHINY,
        new Pair<>(MaterialsBotania.ElvenElementium, 1),
        new Pair<>(Materials.Magic, 4));

    public static final Werkstoff PureMana = new Werkstoff(
        new short[] { 176 ,196,222 },//LightSteelBlue
        TextHandler.texter("PureMana", "bw.PureMana.notTrans"),
        subscriptNumbers("Ma⨕"),
        new Werkstoff.Stats(),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable().addCells(),
        offsetID_01 + 3,
        TextureSet.SET_FLUID);

    public static final Werkstoff LiquidMana = new Werkstoff(
        new short[] {135,206,235 },//skyblue
        TextHandler.texter("LiquidMana", "bw.LiquidMana.notTrans"),
        subscriptNumbers("??Ma⨕??"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable().addCells(),
        offsetID_01 + 4,
        TextureSet.SET_FLUID,
        new Pair<>(PureMana, 1),
        new Pair<>(Materials.Stone, 2));

    public static final Werkstoff PurifiedMana = new Werkstoff(
        new short[] {173,216,230},//LightBLue
        TextHandler.texter("PurifiedMana", "bw.PurifiedMana.notTrans"),
        subscriptNumbers("??Ma⨕??"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable().addCells(),
        offsetID_01 + 5,
        TextureSet.SET_FLUID,
        new Pair<>(PureMana, 1),
        new Pair<>(Materials.Stone, 2));
    
    // Bartworks' Material System run on Runnable.class
    @Override
    public void run() {}
}
