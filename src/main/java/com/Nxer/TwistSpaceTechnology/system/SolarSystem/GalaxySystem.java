package com.Nxer.TwistSpaceTechnology.system.SolarSystem;

import java.util.Arrays;
import java.util.List;

import com.emoniph.witchery.util.Config;

import de.katzenpapst.amunra.AmunRa;
import galaxyspace.core.config.GSConfigDimensions;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.config.Configuration;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import twilightforest.TwilightForestMod;

/**
 * GalaxySystem - Enumeration-based system for managing dimension parameters in Minecraft.
 * <p>
 * This system provides a centralized approach to configure custom properties for different
 * dimensions using the Builder pattern. Each dimension can be configured with various
 * parameters like atmospheric gases, gravity, temperature, etc.
 * <p>
 * <b>Key Features:</b>
 * <ul>
 * <li>Centralized storage of dimension parameters</li>
 * <li>Flexible configuration using Builder pattern</li>
 * <li>Fast dimension lookup by ID</li>
 * <li>Extensible parameter system</li>
 * </ul>
 * <p>
 * <b>Usage Example:</b>
 *
 * <pre>
 * {@code
 * new GalaxySystemProperties.Builder(dimensionId, dimensionName)
 *     .withGas(fluid)
 *     .withGravity(0.5f)
 *     .withTemperature(300)
 *     .build()
 * }
 * </pre>
 * <p>
 * <b>Extending Parameters:</b>
 * To add new dimension parameters, extend {@link GalaxySystemProperties.Builder}
 * with additional configuration methods.
 *
 * @author EvgenWarGold
 * @since 1.0
 * @see GalaxySystemProperties
 * @see GalaxySystemProperties.Builder
 */
public enum GalaxySystem {

    // region Dimensions
    // spotless:off
    OVERWORLD(0, "Overworld", new GalaxySystemProperties.Builder()
        .withGas(Materials.Air.getFluid(1))
        .build()),
    NETHER(-1, "Nether", new GalaxySystemProperties.Builder()
        .withGas(Materials.NetherAir.getFluid(1))
        .build()),
    THE_END(1, "The End", new GalaxySystemProperties.Builder()
        .withGas(null)
        .build()),
    SPECTRE_WORLD(2, "SpectreWorld", new GalaxySystemProperties.Builder()
            .build()
    ),
    TWILIGHT_FOREST(7, "Twilight Forest", new GalaxySystemProperties.Builder()
            .build()
    ),
    MAKEMAKE(25, "Makemake", new GalaxySystemProperties.Builder()
            .build()
    ),
    MOON(-28, "Moon", new GalaxySystemProperties.Builder()
            .build()
    ),
    MARS(29, "Mars", new GalaxySystemProperties.Builder()
            .build()
    ),
    ASTEROIDS(30, "Asteroids", new GalaxySystemProperties.Builder()
            .build()
    ),
    CENTAURI_BB(31,"Centauri Bb", new GalaxySystemProperties.Builder()
            .build()
    ),
    BARNARDA_C(32, "Barnarda C", new GalaxySystemProperties.Builder()
            .build()
    ),
    KUIPER_BELT(33, "Kuiper Belt", new GalaxySystemProperties.Builder()
            .build()
    ),
    EUROPA(35, "Europa", new GalaxySystemProperties.Builder()
            .build()
    ),
    IO(36, "Io", new GalaxySystemProperties.Builder()
            .build()
    ),
    MERCURY(37, "Mercury", new GalaxySystemProperties.Builder()
            .build()
    ),
    PHOBOS(38, "Phobos", new GalaxySystemProperties.Builder()
            .build()
    ),
    VENUS(39, "Venus", new GalaxySystemProperties.Builder()
            .build()
    ),
    DEIMOS(40, "Deimos", new GalaxySystemProperties.Builder()
            .build()
    ),
    ENCELADUS(41, "Enceladus", new GalaxySystemProperties.Builder()
            .build()
    ),
    CERES(42, "Ceres", new GalaxySystemProperties.Builder()
            .build()
    ),
    GANYMEDE(43, "Ganymede", new GalaxySystemProperties.Builder()
            .build()
    ),
    TITAN(44, "Titan", new GalaxySystemProperties.Builder()
            .build()
    ),
    CALISTO(45, "Calisto", new GalaxySystemProperties.Builder()
            .build()
    ),
    OBERON(46, "Oberon", new GalaxySystemProperties.Builder()
            .build()
    ),
    PROTEUS(47, "Proteus", new GalaxySystemProperties.Builder()
            .build()
    ),
    TRITON(48, "Triton", new GalaxySystemProperties.Builder()
            .build()
    ),
    PLUTO(49, "Pluto", new GalaxySystemProperties.Builder()
            .build()
    ),
    THE_OUTER_LANDS(50, "The Outer Lands", new GalaxySystemProperties.Builder()
            .build()
    ),
    URANUS(51, "Uranus", new GalaxySystemProperties.Builder()
            .build()
    ),
    SPIRIT_WORLD(55, "Spirit World", new GalaxySystemProperties.Builder()
            .build()
    ),
    TORMENT(56, "Torment", new GalaxySystemProperties.Builder()
            .build()
    ),
    BEDROCK(60, "Bedrock", new GalaxySystemProperties.Builder()
            .build()
    ),
    ROSS128BA(63, "Ross128ba", new GalaxySystemProperties.Builder()
            .build()
    ),
    ROSS128B(64, "Ross128b", new GalaxySystemProperties.Builder()
            .build()
    ),
    POCKET_PLANE(69, "Pocket Plane", new GalaxySystemProperties.Builder()
            .build()
    ),
    MIRROR(70, "Mirror", new GalaxySystemProperties.Builder()
            .build()
    ),
    JUPITER(71, "Jupiter", new GalaxySystemProperties.Builder()
            .build()
    ),
    NEPTUNE(74, "Neptune", new GalaxySystemProperties.Builder()
            .build()
    ),
    SATURN(77, "Saturn", new GalaxySystemProperties.Builder()
            .build()
    ),
    BARNARDA_E(81, "Barnarda E", new GalaxySystemProperties.Builder()
            .build()
    ),
    BARNARDA_F(82, "Barnarda F", new GalaxySystemProperties.Builder()
            .build()
    ),
    HAUMEA(83, "Haumea", new GalaxySystemProperties.Builder()
            .build()
    ),
    VEGA_B(84, "Vega B", new GalaxySystemProperties.Builder()
            .build()
    ),
    T_CETI_E(85, "T Ceti E", new GalaxySystemProperties.Builder()
            .build()
    ),
    MIRANDA(86, "Miranda", new GalaxySystemProperties.Builder()
            .build()
    ),
    NEPER(90, "Neper", new GalaxySystemProperties.Builder()
            .build()
    ),
    MAAHES(91, "Maahes", new GalaxySystemProperties.Builder()
            .build()
    ),
    ANUBIS(92, "Anubis", new GalaxySystemProperties.Builder()
            .build()
    ),
    HORUS(93, "Horus", new GalaxySystemProperties.Builder()
            .build()
    ),
    SETH(94, "Seth", new GalaxySystemProperties.Builder()
            .build()
    ),
    THE_LAST_MILLENIUM(112, "The Last Millenium", new GalaxySystemProperties.Builder()
            .build()
    ),
    // ToxicEverglades
    DIMENSION_DARK_WORLD(227, "dimensionDarkWorld", new GalaxySystemProperties.Builder()
            .build()
    );
    // spotless:on
    // endregion

    // region Variables
    private int id;
    private final String name;
    private final GalaxySystemProperties properties;
    // endregion

    // region Constructor
    /**
     * Constructs a GalaxySystem enum instance
     *
     * @param id         the dimension ID
     * @param name       the dimension name
     * @param properties the galaxy system properties
     */
    GalaxySystem(int id, String name, GalaxySystemProperties properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
    }
    // endregion

    // region initStatics method
    /**
     * Initializes static dimension IDs based on loaded mod configurations
     * This method should be called during mod initialization to set up
     * correct dimension IDs from various mod configurations
     */
    public static void initStatics() {
        if (Mods.GalacticraftCore.isModLoaded()) {
            MOON.setId(ConfigManagerCore.idDimensionMoon);
        }
        if (Mods.GalacticraftMars.isModLoaded()) {
            MARS.setId(ConfigManagerMars.dimensionIDMars);
            ASTEROIDS.setId(ConfigManagerAsteroids.dimensionIDAsteroids);
        }
        if (Mods.GalaxySpace.isModLoaded()) {
            MERCURY.setId(GSConfigDimensions.dimensionIDMercury);
            VENUS.setId(GSConfigDimensions.dimensionIDVenus);
            CERES.setId(GSConfigDimensions.dimensionIDCeres);
            PLUTO.setId(GSConfigDimensions.dimensionIDPluto);
            KUIPER_BELT.setId(GSConfigDimensions.dimensionIDKuiperBelt);
            HAUMEA.setId(GSConfigDimensions.dimensionIDHaumea);
            MAKEMAKE.setId(GSConfigDimensions.dimensionIDMakemake);
            PHOBOS.setId(GSConfigDimensions.dimensionIDPhobos);
            DEIMOS.setId(GSConfigDimensions.dimensionIDDeimos);
            IO.setId(GSConfigDimensions.dimensionIDIo);
            EUROPA.setId(GSConfigDimensions.dimensionIDEuropa);
            GANYMEDE.setId(GSConfigDimensions.dimensionIDGanymede);
            CALISTO.setId(GSConfigDimensions.dimensionIDCallisto);
            ENCELADUS.setId(GSConfigDimensions.dimensionIDEnceladus);
            TITAN.setId(GSConfigDimensions.dimensionIDTitan);
            OBERON.setId(GSConfigDimensions.dimensionIDOberon);
            MIRANDA.setId(GSConfigDimensions.dimensionIDMiranda);
            PROTEUS.setId(GSConfigDimensions.dimensionIDProteus);
            TRITON.setId(GSConfigDimensions.dimensionIDTriton);
            CENTAURI_BB.setId(GSConfigDimensions.dimensionIDCentauriBb);
            BARNARDA_C.setId(GSConfigDimensions.dimensionIDBarnardaC);
            BARNARDA_E.setId(GSConfigDimensions.dimensionIDBarnardaE);
            BARNARDA_F.setId(GSConfigDimensions.dimensionIDBarnardaF);
            VEGA_B.setId(GSConfigDimensions.dimensionIDVegaB);
            T_CETI_E.setId(GSConfigDimensions.dimensionIDTCetiE);
            JUPITER.setId(GSConfigDimensions.dimensionIDJupiter);
            NEPTUNE.setId(GSConfigDimensions.dimensionIDNeptune);
            SATURN.setId(GSConfigDimensions.dimensionIDSaturn);
            URANUS.setId(GSConfigDimensions.dimensionIDUranus);
        }
        if (Mods.GregTech.isModLoaded()) {
            DIMENSION_DARK_WORLD.setId(Configuration.worldgen.EVERGLADES_ID);
        }
        if (Mods.GalacticraftAmunRa.isModLoaded()) {
            NEPER.setId(AmunRa.config.dimNeper);
            MAAHES.setId(AmunRa.config.dimMaahes);
            ANUBIS.setId(AmunRa.config.dimAnubis);
            HORUS.setId(AmunRa.config.dimHorus);
            SETH.setId(AmunRa.config.dimSeth);
        }
        if (Mods.Witchery.isModLoaded()) {
            SPIRIT_WORLD.setId(Config.instance().dimensionDreamID);
            MIRROR.setId(Config.instance().dimensionMirrorID);
            TORMENT.setId(Config.instance().dimensionTormentID);
        }
        if (Mods.TwilightForest.isModLoaded()) {
            TWILIGHT_FOREST.setId(TwilightForestMod.dimensionID);
        }
    }
    // endregion

    // region Getters
    /**
     * Gets the galaxy system properties
     *
     * @return the galaxy system properties
     */
    public GalaxySystemProperties getProperties() {
        return properties;
    }

    /**
     * Gets the dimension ID
     *
     * @return the dimension ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the dimension name
     *
     * @return the dimension name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets GalaxySystem by dimensionId
     *
     * @param dimensionId the dimension ID
     * @return corresponding GalaxySystem or null if not found
     */
    public static GalaxySystem getGalaxySystemById(int dimensionId) {
        return Arrays.stream(values())
            .filter(system -> system.id == dimensionId)
            .findFirst()
            .orElse(null);
    }

    /**
     * Gets GalaxySystem by BaseMetaTileEntity
     *
     * @param mBaseTileEntity the base meta tile entity
     * @return corresponding GalaxySystem or null if not found
     */
    public static GalaxySystem getDimensionByGregTechTileEntity(IGregTechTileEntity mBaseTileEntity) {
        if (mBaseTileEntity == null || mBaseTileEntity.getWorld() == null) {
            return null;
        }
        int worldId = mBaseTileEntity.getWorld().provider.dimensionId;
        return getGalaxySystemById(worldId);
    }

    /**
     * Gets all GalaxySystem instances as a list
     *
     * @return list of all GalaxySystem instances
     */
    public static List<GalaxySystem> getAllSystemsAsList() {
        return Arrays.asList(values());
    }
    // endregion

    // region Setters
    /**
     * Sets the dimension ID
     *
     * @param id the new dimension ID
     */
    public void setId(int id) {
        this.id = id;
    }
    // endregion
}
