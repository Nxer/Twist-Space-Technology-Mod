package com.Nxer.TwistSpaceTechnology.common.material;

import static bartworks.util.BWUtil.subscriptNumbers;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.config.Config;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;

/**
 * Register new material here by Bartworks Material System
 */
public class MaterialPool implements Runnable {

    /*----------- Test the forge item register -----------*/

    // public static ItemAdder_Basic testItem = new ItemAdder_Basic("Testing Item","testingItem.01");
    // public static ItemStack testItem4 = new ItemStack(testItem,1,1);

    /*----------- Test the forge item register -----------*/

    // spotless:off

    // ID manager
    private static final int offsetID_01 = 20_000;
    public static final Werkstoff.GenerationFeatures gf = new Werkstoff.GenerationFeatures();
    public static Werkstoff eventHorizonDiffusers;
    public static Werkstoff entropyReductionProcess;
    public static Werkstoff realSingularity;

    // #tr Material.holmiumgarnet
    // # Holmium Garnet
    // #zh_CN 钬石榴石
    public static final Werkstoff HolmiumGarnet = new Werkstoff(
        new short[] { 96, 96, 240 },
        "Holmium Garnet",
        subscriptNumbers("Ho3Al5O12"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        offsetID_01 + 1,
        TextureSet.SET_SHINY);

    // #tr Material.puremana
    // # Pure Mana
    // #zh_CN 至纯魔力
    public static final Werkstoff PureMana = new Werkstoff(
        new short[] { 176, 196, 222 }, // LightSteelBlue
        "Pure Mana",
        subscriptNumbers("Ma⨕"),
        new Werkstoff.Stats(),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        offsetID_01 + 5,
        TextureSet.SET_FLUID);

    // #tr Material.liquidmana
    // # Liquid Mana
    // #zh_CN 液态魔力
    public static final Werkstoff LiquidMana = new Werkstoff(
        new short[] { 135, 206, 235 }, // skyblue
        "Liquid Mana",
        subscriptNumbers("??Ma⨕??"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        offsetID_01 + 6,
        TextureSet.SET_FLUID,
        Pair.of(PureMana, 1),
        Pair.of(Materials.Stone, 2));

    // #tr Material.purifiedmana
    // # Purified Mana
    // #zh_CN 纯净魔力
    public static final Werkstoff PurifiedMana = new Werkstoff(
        new short[] { 173, 216, 230 }, // LightBLue
        "Purified Mana",
        subscriptNumbers("??Ma⨕??"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        offsetID_01 + 7,
        TextureSet.SET_FLUID,
        Pair.of(PureMana, 1),
        Pair.of(Materials.Stone, 2));

    // #tr Material.stabilisevoidmatter
    // # Stabilise Void Matter
    // #zh_CN 稳定虚空物质
    public static final Werkstoff StabiliseVoidMatter = new Werkstoff(
        new short[] { 0, 0, 0 }, // Dark
        "Stabilise Void Matter",
        subscriptNumbers("??SvM⨕??"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        offsetID_01 + 8,
        TextureSet.SET_FLUID,
        Pair.of(PureMana, 1),
        Pair.of(Materials.Stone, 2));

    // #tr Material.liquidstargate
    // # Liquid Stargate
    // #zh_CN 液态星门
    public static final Werkstoff LiquidStargate = new Werkstoff(
        new short[] { 66, 170, 255 }, // Dark
        "Liquid Stargate",
        subscriptNumbers("??Stargate??"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        offsetID_01 + 9,
        TextureSet.SET_FLUID,
        Pair.of(PureMana, 1),
        Pair.of(Materials.Stone, 2));

    // #tr Material.concentrateduu-matter
    // # Concentrated UU-Matter
    // #zh_CN 浓缩UU物质
    public static final Werkstoff ConcentratedUUMatter = new Werkstoff(
        new short[] { 90, 0, 128 },
        "Concentrated UU-Matter",
        subscriptNumbers("UU+"),
        new Werkstoff.Stats(),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        offsetID_01 + 10,
        TextureSet.SET_FLUID);

    // #tr Material.highlyenergeticdimensionalentropicfluid
    // # Highly Energetic Dimensional Entropic Fluid
    // #zh_CN 高能维度熵流体

    // #tr Material.highlyenergeticdimensionalentropicfluid.ChemicalFormula
    // # Origin of Entropy
    // #zh_CN 熵的起源
    public static final Werkstoff EntropicFlux = new Werkstoff(
        new short[] { 43, 18, 128 },
        "Highly Energetic Dimensional Entropic Fluid",
        subscriptNumbers("Origin of Entropy"),
        new Werkstoff.Stats(),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        offsetID_01 + 11,
        TextureSet.SET_FLUID);

    // #tr Material.chrono-dimensionallyannihilativeentropicfluid
    // # Chrono-Dimensionally Annihilative Entropic Fluid
    // #zh_CN 时空湮灭维度熵流体

    // #tr Material.chrono-dimensionallyannihilativeentropicfluid.ChemicalFormula
    // # Endless Entropy
    // #zh_CN 熵增
    public static final Werkstoff ChronoentropicFlux = new Werkstoff(
        new short[] { 43, 18, 55 },
        "Chrono-Dimensionally Annihilative Entropic Fluid",
        subscriptNumbers("Endless Entropy"),
        new Werkstoff.Stats(),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        offsetID_01 + 12,
        TextureSet.SET_FLUID);

    static {
        if (Config.activateMegaSpaceStation) {
            eventHorizonDiffusers = new Werkstoff(
                new short[] { 255, 255, 255 },
                "Event Horizon Diffusers",
                subscriptNumbers("when we face it, we can do nothing before, but not now"),
                new Werkstoff.Stats(),
                Werkstoff.Types.MATERIAL,
                gf,
                offsetID_01 + 2,
                TextureSet.SET_SHINY);
            entropyReductionProcess = new Werkstoff(
                new short[] { 0, 0, 0 },
                "Entropy Reduction Process",
                subscriptNumbers("Trying to fight against the demise of the universe"),
                new Werkstoff.Stats(),
                Werkstoff.Types.MATERIAL,
                gf,
                offsetID_01 + 3,
                TextureSet.SET_SHINY);
            realSingularity = new Werkstoff(
                new short[] { 127, 127, 127 },
                "Real Singularity",
                subscriptNumbers("Not just a compressed body, but a real miniature black hole"),
                new Werkstoff.Stats(),
                Werkstoff.Types.MATERIAL,
                gf,
                offsetID_01 + 4,
                TextureSet.SET_SHINY);
        }
    }

    // Bartworks' Material System run on Runnable.class
    @Override
    public void run() {
        // for (var prefix : OrePrefixes.values()) {
        // gf.addPrefix(prefix);
        // }
        // gf.removePrefix(OrePrefixes.ore);
    }

}
