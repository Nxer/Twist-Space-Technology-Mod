package com.Nxer.TwistSpaceTechnology.common.material;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;

import com.Nxer.TwistSpaceTechnology.util.TextHandler;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import gregtech.api.enums.OrePrefixes;
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

    // spotless:off

    public static final Werkstoff.GenerationFeatures gf = new Werkstoff.GenerationFeatures();

    public static final Werkstoff eventHorizonDiffusers = new Werkstoff(
        new short[] { 255, 255, 255 },
        TextHandler.texter("Event Horizon Diffusers", "bw.eventHorizonDiffusers.notTrans"),
        subscriptNumbers("when we face it, we can do nothing before, but not now"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        gf,
        offsetID_01 + 2,
        TextureSet.SET_SHINY);

    public static final Werkstoff entropyReductionProcess = new Werkstoff(
        new short[] { 0, 0, 0 },
        TextHandler.texter("Entropy Reduction Process", "bw.entropyReductionProcess.notTrans"),
        subscriptNumbers("Trying to fight against the demise of the universe"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        gf,
        offsetID_01 + 3,
        TextureSet.SET_SHINY);

    public static final Werkstoff realSingularity = new Werkstoff(
        new short[] { 127, 127, 127 },
        TextHandler.texter("Real Singularity", "bw.realSingularity.notTrans"),
        subscriptNumbers("Not just a compressed body, but a real miniature black hole"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        gf,
        offsetID_01 + 4,
        TextureSet.SET_SHINY);

    @Override
    public void run() {
        for (var prefix : OrePrefixes.values()) {
            gf.addPrefix(prefix);
        }
        gf.removePrefix(OrePrefixes.ore);
    }
}
