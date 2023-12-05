package com.Nxer.TwistSpaceTechnology.common.material;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;
import static gregtech.api.enums.Mods.GregTech;

import com.Nxer.TwistSpaceTechnology.util.TextHandler;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IMaterialHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.items.UniversiumRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;

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

    public static final MaterialPool pool = new MaterialPool();


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
        new short[]{96, 96, 240},
        TextHandler.texter("Holmium Garnet", "bw.HolmiumGarnet.notTrans"),
        subscriptNumbers("Ho3Al5O12"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        offsetID_01 + 1,
        TextureSet.SET_SHINY);

    // spotless:off

    public static final Werkstoff eventHorizonDiffusers = new Werkstoff(
        new short[]{255, 255, 255},
        TextHandler.texter("Event Horizon Diffusers", "bw.eventHorizonDiffusers.notTrans"),
        subscriptNumbers("when we face it, we can do nothing before, but not now"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable().addMolten().addPrefix(OrePrefixes.nanite).addPrefix(OrePrefixes.plate)
            .addPrefix(OrePrefixes.bolt).addPrefix(OrePrefixes.wire).addPrefix(OrePrefixes.wireGt16).addPrefix(OrePrefixes.wireFine)
            .addPrefix(OrePrefixes.componentCircuit)
            .addMetalItems(),
        offsetID_01 + 2,
        TextureSet.SET_SHINY);

    public static final Werkstoff entropyReductionProcess = new Werkstoff(
        new short[]{0, 0, 0},
        TextHandler.texter("Entropy Reduction Process", "bw.entropyReductionProcess.notTrans"),
        subscriptNumbers("Trying to fight against the demise of the universe"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable().addMolten().addPrefix(OrePrefixes.nanite).addPrefix(OrePrefixes.plate)
            .addPrefix(OrePrefixes.bolt).addPrefix(OrePrefixes.wire).addPrefix(OrePrefixes.wireGt16).addPrefix(OrePrefixes.wireFine)
            .addPrefix(OrePrefixes.componentCircuit)
            .addMetalItems(),
        offsetID_01 + 3,
        TextureSet.SET_SHINY);

    public static final Werkstoff realSingularity = new Werkstoff(
        new short[]{127, 127, 127},
        TextHandler.texter("Real Singularity", "bw.realSingularity.notTrans"),
        subscriptNumbers("Not just a compressed body, but a real miniature black hole"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable().addMolten().addPrefix(OrePrefixes.nanite).addPrefix(OrePrefixes.plate)
            .addPrefix(OrePrefixes.bolt).addPrefix(OrePrefixes.wire).addPrefix(OrePrefixes.wireGt16).addPrefix(OrePrefixes.wireFine)
            .addPrefix(OrePrefixes.componentCircuit)
            .addMetalItems(),
        offsetID_01 + 4,
        TextureSet.SET_SHINY);


    @Override
    public void run() {

    }
}
