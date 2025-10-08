package com.Nxer.TwistSpaceTechnology.system.DimensionSystem;

import com.emoniph.witchery.util.Config;

import de.katzenpapst.amunra.AmunRa;
import galaxyspace.core.config.GSConfigDimensions;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gtPlusPlus.core.config.Configuration;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import thaumic.tinkerer.common.core.handler.ConfigHandler;
import twilightforest.TwilightForestMod;

/**
 * DimensionSystemInit - Initialization class for all dimensions from various Minecraft mods
 *
 * <p>
 * This class is responsible for loading and initializing dimensions from multiple mods
 * with proper cross-mod compatibility. It checks if mods are loaded and uses their
 * configured dimension IDs, falling back to default IDs if mods are not present.
 * </p>
 *
 * @author EvgenWarGold
 * @version 1.0
 * @see DimensionSystem
 * @see DimensionBuilder
 */
public class DimensionSystemInit {

    public static void init() {
        loadVanillaDimension();
        loadRandomThingsDimension();
        loadExtraUtilitiesDimension();
        loadThaumcraftDimension();
        loadThaumicTinkererDimension();
        loadThaumcraftHorizonsDimension();
        loadTwilightForestDimension();
        loadWitcheryDimension();
        loadGregTechDimension();
        loadGalacticraftAmunRaDimension();
        loadGalacticraftCoreDimension();
        loadGalacticraftMarsDimension();
        loadGalaxySpaceDimension();
    }

    // spotless:off
    // region VanillaDimension
    private static void loadVanillaDimension() {
        DimensionSystem.Overworld = loadOverworld();
        DimensionSystem.Nether = loadNether();
        DimensionSystem.TheEnd = loadTheEnd();
    }

    private static DimensionSystem loadOverworld() {
        return new DimensionBuilder()
            .setId(0)
            .setGenerateGas(Materials.Air.getGas(1))
            .setName("Overworld")
            .constructDimension();
    }

    private static DimensionSystem loadNether() {
        return new DimensionBuilder()
            .setId(-1)
            .setGenerateGas(Materials.NetherAir.getFluid(1))
            .setName("Nether")
            .constructDimension();
    }

    private static DimensionSystem loadTheEnd() {
        return new DimensionBuilder()
            .setId(1)
            .setName("The End")
            .constructDimension();
    }
    // endregion

    // region RandomThings
    private static void loadRandomThingsDimension() {
        DimensionSystem.SpectreWorld = loadSpectreWorld();
    }

    private static DimensionSystem loadSpectreWorld() {
        return new DimensionBuilder()
            .setId(2)
            .setName("Spectre World")
            .constructDimension();
    }
    // endregion

    // region ExtraUtilities
    private static void loadExtraUtilitiesDimension() {
        DimensionSystem.TheLastMillenium = loadTheLastMillenium();
    }

    private static DimensionSystem loadTheLastMillenium() {
        return new DimensionBuilder()
            .setId(112)
            .setName("The Last Millenium")
            .constructDimension();
    }
    // endregion

    // region Thaumcraft
    private static void loadThaumcraftDimension() {
        DimensionSystem.TheOuterLands = loadTheOuterLands();
    }

    private static DimensionSystem loadTheOuterLands() {
        return new DimensionBuilder()
            .setId(Mods.Thaumcraft.isModLoaded() ? thaumcraft.common.config.Config.dimensionOuterId : 50)
            .setName("Bedrock")
            .constructDimension();
    }
    // endregion

    // region ThaumicTinkerer
    private static void loadThaumicTinkererDimension() {
        DimensionSystem.Bedrock = loadBedrock();
    }

    private static DimensionSystem loadBedrock() {
        return new DimensionBuilder()
            .setId(Mods.ThaumicTinkerer.isModLoaded() ? ConfigHandler.bedrockDimensionID : 60)
            .setName("Bedrock")
            .constructDimension();
    }
    // endregion

    // region ThaumcraftHorizons
    private static void loadThaumcraftHorizonsDimension() {
        DimensionSystem.PocketPlane = loadPocketPlane();
    }

    private static DimensionSystem loadPocketPlane() {
        return new DimensionBuilder()
            .setId(69)
            .setName("Bedrock")
            .constructDimension();
    }
    // endregion

    // region TwilightForest
    private static void loadTwilightForestDimension() {
        DimensionSystem.TwilightForest = loadTwilightForest();
    }

    private static DimensionSystem loadTwilightForest() {
        return new DimensionBuilder()
            .setId(Mods.TwilightForest.isModLoaded() ? TwilightForestMod.dimensionID : 7)
            .setName("Twilight Forest")
            .constructDimension();
    }
    // endregion

    // region Witchery
    private static void loadWitcheryDimension() {
        DimensionSystem.SpiritWorld = loadSpiritWorld();
        DimensionSystem.Mirror = loadMirror();
        DimensionSystem.Torment = loadTorment();
    }

    private static DimensionSystem loadSpiritWorld() {
        return new DimensionBuilder()
            .setId(Mods.Witchery.isModLoaded() ? Config.instance().dimensionDreamID : 55)
            .setName("Spirit World")
            .constructDimension();
    }

    private static DimensionSystem loadMirror() {
        return new DimensionBuilder()
            .setId(Mods.Witchery.isModLoaded() ? Config.instance().dimensionMirrorID : 70)
            .setName("Mirror")
            .constructDimension();
    }

    private static DimensionSystem loadTorment() {
        return new DimensionBuilder()
            .setId(Mods.Witchery.isModLoaded() ? Config.instance().dimensionTormentID : 56)
            .setName("Torment")
            .constructDimension();
    }
    // endregion

    // region GregTech
    private static void loadGregTechDimension() {
        DimensionSystem.ToxicEverglades = loadToxicEverglades();
        DimensionSystem.Ross128BA = loadRoss128BA();
        DimensionSystem.Ross128BB = loadRoss128B();
    }

    private static DimensionSystem loadToxicEverglades() {
        return new DimensionBuilder()
            .setId(Mods.GregTech.isModLoaded() ? Configuration.worldgen.EVERGLADES_ID : 277)
            .setName("Toxic Everglades")
            .constructDimension();
    }

    private static DimensionSystem loadRoss128BA() {
        return new DimensionBuilder()
            .setId(Mods.GregTech.isModLoaded() ? bartworks.common.configs.Configuration.crossModInteractions.ross128BAID : 63)
            .setName("Ross128ba")
            .constructDimension();
    }

    private static DimensionSystem loadRoss128B() {
        return new DimensionBuilder()
            .setId(Mods.GregTech.isModLoaded() ? bartworks.common.configs.Configuration.crossModInteractions.ross128BID : 64)
            .setName("Ross128b")
            .constructDimension();
    }
    // endregion

    // region GalacticraftAmunRa
    private static void loadGalacticraftAmunRaDimension() {
        DimensionSystem.Neper = loadNeper();
        DimensionSystem.Maahes = loadMaahes();
        DimensionSystem.Anubis = loadAnubis();
        DimensionSystem.Horus = loadHorus();
        DimensionSystem.Seth = loadSeth();

    }

    private static DimensionSystem loadNeper() {
        return new DimensionBuilder()
            .setId(Mods.GalacticraftAmunRa.isModLoaded() ? AmunRa.config.dimNeper : 90)
            .setName("Neper")
            .constructDimension();
    }

    private static DimensionSystem loadMaahes() {
        return new DimensionBuilder()
            .setId(Mods.GalacticraftAmunRa.isModLoaded() ? AmunRa.config.dimMaahes : 91)
            .setName("Maahes")
            .constructDimension();
    }

    private static DimensionSystem loadAnubis() {
        return new DimensionBuilder()
            .setId(Mods.GalacticraftAmunRa.isModLoaded() ? AmunRa.config.dimAnubis : 92)
            .setName("Anubis")
            .constructDimension();
    }

    private static DimensionSystem loadHorus() {
        return new DimensionBuilder()
            .setId(Mods.GalacticraftAmunRa.isModLoaded() ? AmunRa.config.dimHorus : 93)
            .setName("Horus")
            .constructDimension();
    }

    private static DimensionSystem loadSeth() {
        return new DimensionBuilder()
            .setId(Mods.GalacticraftAmunRa.isModLoaded() ? AmunRa.config.dimSeth : 94)
            .setName("Seth")
            .constructDimension();
    }
    // endregion

    // region GalacticraftCore
    private static void loadGalacticraftCoreDimension() {
        DimensionSystem.Moon = loadMoon();
    }

    private static DimensionSystem loadMoon() {
        return new DimensionBuilder()
            .setId(Mods.GalacticraftCore.isModLoaded() ? ConfigManagerCore.idDimensionMoon : -28)
            .setName("Moon")
            .constructDimension();
    }
    // endregion

    // region GalacticraftMars
    private static void loadGalacticraftMarsDimension() {
        DimensionSystem.Mars = loadMars();
        DimensionSystem.Asteroids = loadAsteroids();
    }

    private static DimensionSystem loadMars() {
        return new DimensionBuilder()
            .setId(Mods.GalacticraftMars.isModLoaded() ? ConfigManagerMars.dimensionIDMars : 29)
            .setName("Mars")
            .constructDimension();
    }

    private static DimensionSystem loadAsteroids() {
        return new DimensionBuilder()
            .setId(Mods.GalacticraftMars.isModLoaded() ? ConfigManagerAsteroids.dimensionIDAsteroids : 30)
            .setName("Asteroids")
            .constructDimension();
    }
    // endregion

    // region GalaxySpace
    private static void loadGalaxySpaceDimension() {
        DimensionSystem.Mercury = loadMercury();
        DimensionSystem.Venus = loadVenus();
        DimensionSystem.Ceres = loadCeres();

        DimensionSystem.Pluto = loadPluto();
        DimensionSystem.KuiperBelt = loadKuiperBelt();
        DimensionSystem.Haumea = loadHaumea();
        DimensionSystem.MakeMake = loadMakeMake();
        DimensionSystem.Phobos = loadPhobos();
        DimensionSystem.Deimos = loadDeimos();
        DimensionSystem.Io = loadIo();
        DimensionSystem.Europa = loadEuropa();
        DimensionSystem.Ganymede = loadGanymede();
        DimensionSystem.Calisto = loadCalisto();
        DimensionSystem.Enceladus = loadEnceladus();
        DimensionSystem.Titan = loadTitan();
        DimensionSystem.Oberon = loadOberon();
        DimensionSystem.Miranda = loadMiranda();
        DimensionSystem.Proteus = loadProteus();
        DimensionSystem.Triton = loadTriton();
        DimensionSystem.CentauriBB = loadCentauriBB();
        DimensionSystem.BarnardaC = loadBarnardaC();
        DimensionSystem.BarnardaE = loadBarnardaE();
        DimensionSystem.BarnardaF = loadBarnardaF();
        DimensionSystem.VegaB = loadVegaB();
        DimensionSystem.TcetiE = loadTcetiE();
        DimensionSystem.Jupiter = loadJupiter();
        DimensionSystem.Neptune = loadNeptune();
        DimensionSystem.Saturn = loadSaturn();
        DimensionSystem.Uranus = loadUranus();
    }

    private static DimensionSystem loadMercury() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDMercury : 37)
            .setName("Mercury")
            .constructDimension();
    }

    private static DimensionSystem loadVenus() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDVenus : 39)
            .setName("Venus")
            .constructDimension();
    }

    private static DimensionSystem loadCeres() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDCeres : 42)
            .setName("Ceres")
            .constructDimension();
    }

    private static DimensionSystem loadPluto() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDPluto : 49)
            .setName("Pluto")
            .constructDimension();
    }

    private static DimensionSystem loadKuiperBelt() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDKuiperBelt : 33)
            .setName("Kuiper Belt")
            .constructDimension();
    }

    private static DimensionSystem loadHaumea() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDHaumea : 83)
            .setName("Haumea")
            .constructDimension();
    }

    private static DimensionSystem loadMakeMake() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDMakemake : 25)
            .setName("Makemake")
            .constructDimension();
    }

    private static DimensionSystem loadPhobos() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDPhobos : 38)
            .setName("Phobos")
            .constructDimension();
    }

    private static DimensionSystem loadDeimos() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDDeimos : 40)
            .setName("Deimos")
            .constructDimension();
    }

    private static DimensionSystem loadIo() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDIo : 36)
            .setName("Io")
            .constructDimension();
    }

    private static DimensionSystem loadEuropa() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDEuropa : 35)
            .setName("Europa")
            .constructDimension();
    }

    private static DimensionSystem loadGanymede() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDGanymede : 43)
            .setName("Ganymede")
            .constructDimension();
    }

    private static DimensionSystem loadCalisto() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDCallisto : 45)
            .setName("Calisto")
            .constructDimension();
    }

    private static DimensionSystem loadEnceladus() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDEnceladus : 41)
            .setName("Enceladus")
            .constructDimension();
    }

    private static DimensionSystem loadTitan() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDTitan : 44)
            .setName("Titan")
            .constructDimension();
    }

    private static DimensionSystem loadOberon() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDOberon : 46)
            .setName("Oberon")
            .constructDimension();
    }

    private static DimensionSystem loadMiranda() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDMiranda : 86)
            .setName("Miranda")
            .constructDimension();
    }

    private static DimensionSystem loadProteus() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDProteus : 47)
            .setName("Proteus")
            .constructDimension();
    }

    private static DimensionSystem loadTriton() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDTriton : 48)
            .setName("Triton")
            .constructDimension();
    }

    private static DimensionSystem loadCentauriBB() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDCentauriBb : 31)
            .setName("Centauri Bb")
            .constructDimension();
    }

    private static DimensionSystem loadBarnardaC() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDBarnardaC : 32)
            .setName("Barnarda C")
            .constructDimension();
    }

    private static DimensionSystem loadBarnardaE() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDBarnardaE : 81)
            .setName("Barnarda E")
            .constructDimension();
    }

    private static DimensionSystem loadBarnardaF() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDBarnardaF : 82)
            .setName("Barnarda F")
            .constructDimension();
    }

    private static DimensionSystem loadVegaB() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDVegaB : 84)
            .setName("Vega B")
            .constructDimension();
    }

    private static DimensionSystem loadTcetiE() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDTCetiE : 85)
            .setName("T Ceti E")
            .constructDimension();
    }

    private static DimensionSystem loadJupiter() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDJupiter : 71)
            .setName("Jupiter")
            .constructDimension();
    }

    private static DimensionSystem loadNeptune() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDNeptune : 74)
            .setName("Neptune")
            .constructDimension();
    }

    private static DimensionSystem loadSaturn() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDSaturn : 77)
            .setName("Saturn")
            .constructDimension();
    }

    private static DimensionSystem loadUranus() {
        return new DimensionBuilder()
            .setId(Mods.GalaxySpace.isModLoaded() ? GSConfigDimensions.dimensionIDUranus : 51)
            .setName("Uranus")
            .constructDimension();
    }
    // endregion
    // spotless:on
}
