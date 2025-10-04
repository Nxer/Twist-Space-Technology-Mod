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

// spotless:off
/**
 * GalaxySystem - Enumeration-based system for managing dimension parameters in Minecraft.
 * <p>
 * This system provides a centralized approach to configure custom properties for different
 * dimensions using the Builder pattern. Each dimension can be configured with various
 * environmental and atmospheric parameters.
 * <p>
 * <b>Architecture:</b>
 * <ul>
 * <li><b>GalaxySystem</b> (this class) - contains dimension enumeration and methods for working with dimensions</li>
 * <li><b>GalaxySystemProperties</b> - stores dimension parameters (gases, gravity, temperature, etc.)</li>
 * <li><b>GalaxySystemProperties.Builder</b> - builder pattern for parameter configuration</li>
 * </ul>
 * <p>
 * <b>Key Features:</b>
 * <ul>
 * <li>Centralized storage of dimension parameters</li>
 * <li>Flexible configuration using Builder pattern</li>
 * <li>Fast dimension lookup by ID</li>
 * <li>Extensible parameter system</li>
 * <li>Automatic dimension ID initialization from mod configurations</li>
 * </ul>
 * <p>
 * <b>Workflow:</b>
 * <ol>
 * <li>Call {@link #initStatics()} to initialize dimension IDs from mod configs</li>
 * <li>Call {@link #initProperties()} to configure parameters for each dimension</li>
 * <li>Use getter methods to retrieve dimension information</li>
 * </ol>
 * <p>
 * <b>Adding New Parameters:</b>
 * To declare new system parameters, you need to:
 * <ol>
 * <li>Add field in {@link GalaxySystemProperties}</li>
 * <li>Add configuration method in {@link GalaxySystemProperties.Builder}</li>
 * <li>Configure values in {@link #initProperties()} method for relevant dimensions</li>
 * </ol>
 * <p>
 * <b>Usage Example:</b>
 *
 * <pre>
 * {@code
 * // Getting dimension properties
 * GalaxySystem system = GalaxySystem.getGalaxySystemById(0);
 * GalaxySystemProperties properties = system.getProperties();
 *
 * // Configuration in initProperties()
 * OVERWORLD.properties = new GalaxySystemProperties.Builder()
 *     .withGas(Materials.Air.getGas(1))
 *     .withGravity(1.0f)
 *     .withTemperature(293)
 *     .build();
 * }
 * </pre>
 *
 * @author EvgenWarGold
 * @since 1.0
 * @see GalaxySystemProperties
 * @see GalaxySystemProperties.Builder
 */
// spotless:on
public enum GalaxySystem {

    // region Dimensions
    // spotless:off
    OVERWORLD(0, "Overworld", null),
    NETHER(-1, "Nether", null),
    THE_END(1, "The End", null),
    SPECTRE_WORLD(2, "SpectreWorld", null),
    TWILIGHT_FOREST(7, "Twilight Forest", null),
    MAKEMAKE(25, "Makemake", null),
    MOON(-28, "Moon", null),
    MARS(29, "Mars", null),
    ASTEROIDS(30, "Asteroids", null),
    CENTAURI_BB(31,"Centauri Bb", null),
    BARNARDA_C(32, "Barnarda C", null),
    KUIPER_BELT(33, "Kuiper Belt", null),
    EUROPA(35, "Europa", null),
    IO(36, "Io", null),
    MERCURY(37, "Mercury", null),
    PHOBOS(38, "Phobos", null),
    VENUS(39, "Venus", null),
    DEIMOS(40, "Deimos", null),
    ENCELADUS(41, "Enceladus", null),
    CERES(42, "Ceres", null),
    GANYMEDE(43, "Ganymede", null),
    TITAN(44, "Titan", null),
    CALISTO(45, "Calisto", null),
    OBERON(46, "Oberon", null),
    PROTEUS(47, "Proteus", null),
    TRITON(48, "Triton", null),
    PLUTO(49, "Pluto", null),
    THE_OUTER_LANDS(50, "The Outer Lands", null),
    URANUS(51, "Uranus", null),
    SPIRIT_WORLD(55, "Spirit World", null),
    TORMENT(56, "Torment", null),
    BEDROCK(60, "Bedrock", null),
    ROSS128BA(63, "Ross128ba", null),
    ROSS128B(64, "Ross128b", null ),
    POCKET_PLANE(69, "Pocket Plane", null),
    MIRROR(70, "Mirror", null),
    JUPITER(71, "Jupiter", null),
    NEPTUNE(74, "Neptune", null),
    SATURN(77, "Saturn", null),
    BARNARDA_E(81, "Barnarda E", null),
    BARNARDA_F(82, "Barnarda F", null),
    HAUMEA(83, "Haumea", null),
    VEGA_B(84, "Vega B", null),
    T_CETI_E(85, "T Ceti E", null),
    MIRANDA(86, "Miranda", null),
    NEPER(90, "Neper", null),
    MAAHES(91, "Maahes", null),
    ANUBIS(92, "Anubis", null),
    HORUS(93, "Horus", null),
    SETH(94, "Seth", null),
    THE_LAST_MILLENIUM(112, "The Last Millenium", null),
    // ToxicEverglades
    DIMENSION_DARK_WORLD(227, "dimensionDarkWorld", null);
    // spotless:on
    // endregion

    // region Variables
    private int id;
    private final String name;
    private GalaxySystemProperties properties;
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

    // region init Properties
    /**
     * Initializes galaxy system properties for all dimensions
     * This method should be called during mod initialization to set up
     * atmospheric and environmental properties for various celestial bodies
     * and dimensions from different mod configurations
     */
    public static void initProperties() {
        // spotless:off
        OVERWORLD.properties = new GalaxySystemProperties.Builder().withGas(Materials.Air.getGas(1))
            .build();
        NETHER.properties = new GalaxySystemProperties.Builder().withGas(Materials.NetherAir.getFluid(1))
            .build();
        THE_END.properties = new GalaxySystemProperties.Builder().withGas(null)
            .build();
        SPECTRE_WORLD.properties = new GalaxySystemProperties.Builder()
            .build();
        TWILIGHT_FOREST.properties = new GalaxySystemProperties.Builder()
            .build();
        MAKEMAKE.properties = new GalaxySystemProperties.Builder()
            .build();
        MOON.properties = new GalaxySystemProperties.Builder()
            .build();
        MARS.properties = new GalaxySystemProperties.Builder()
            .build();
        ASTEROIDS.properties = new GalaxySystemProperties.Builder()
            .build();
        CENTAURI_BB.properties = new GalaxySystemProperties.Builder()
            .build();
        BARNARDA_C.properties = new GalaxySystemProperties.Builder()
            .build();
        KUIPER_BELT.properties = new GalaxySystemProperties.Builder()
            .build();
        EUROPA.properties = new GalaxySystemProperties.Builder()
            .build();
        IO.properties = new GalaxySystemProperties.Builder()
            .build();
        MERCURY.properties = new GalaxySystemProperties.Builder()
            .build();
        PHOBOS.properties = new GalaxySystemProperties.Builder()
            .build();
        VENUS.properties = new GalaxySystemProperties.Builder()
            .build();
        DEIMOS.properties = new GalaxySystemProperties.Builder()
            .build();
        ENCELADUS.properties = new GalaxySystemProperties.Builder()
            .build();
        CERES.properties = new GalaxySystemProperties.Builder()
            .build();
        GANYMEDE.properties = new GalaxySystemProperties.Builder()
            .build();
        TITAN.properties = new GalaxySystemProperties.Builder()
            .build();
        CALISTO.properties = new GalaxySystemProperties.Builder()
            .build();
        OBERON.properties = new GalaxySystemProperties.Builder()
            .build();
        PROTEUS.properties = new GalaxySystemProperties.Builder()
            .build();
        TRITON.properties = new GalaxySystemProperties.Builder()
            .build();
        PLUTO.properties = new GalaxySystemProperties.Builder()
            .build();
        THE_OUTER_LANDS.properties = new GalaxySystemProperties.Builder()
            .build();
        URANUS.properties = new GalaxySystemProperties.Builder()
            .build();
        SPIRIT_WORLD.properties = new GalaxySystemProperties.Builder()
            .build();
        TORMENT.properties = new GalaxySystemProperties.Builder()
            .build();
        BEDROCK.properties = new GalaxySystemProperties.Builder()
            .build();
        ROSS128BA.properties = new GalaxySystemProperties.Builder()
            .build();
        ROSS128B.properties = new GalaxySystemProperties.Builder()
            .build();
        POCKET_PLANE.properties = new GalaxySystemProperties.Builder()
            .build();
        MIRROR.properties = new GalaxySystemProperties.Builder()
            .build();
        JUPITER.properties = new GalaxySystemProperties.Builder()
            .build();
        NEPTUNE.properties = new GalaxySystemProperties.Builder()
            .build();
        SATURN.properties = new GalaxySystemProperties.Builder()
            .build();
        BARNARDA_E.properties = new GalaxySystemProperties.Builder()
            .build();
        BARNARDA_F.properties = new GalaxySystemProperties.Builder()
            .build();
        HAUMEA.properties = new GalaxySystemProperties.Builder()
            .build();
        VEGA_B.properties = new GalaxySystemProperties.Builder()
            .build();
        T_CETI_E.properties = new GalaxySystemProperties.Builder()
            .build();
        MIRANDA.properties = new GalaxySystemProperties.Builder()
            .build();
        NEPER.properties = new GalaxySystemProperties.Builder()
            .build();
        MAAHES.properties = new GalaxySystemProperties.Builder()
            .build();
        ANUBIS.properties = new GalaxySystemProperties.Builder()
            .build();
        HORUS.properties = new GalaxySystemProperties.Builder()
            .build();
        SETH.properties = new GalaxySystemProperties.Builder()
            .build();
        THE_LAST_MILLENIUM.properties = new GalaxySystemProperties.Builder()
            .build();
        DIMENSION_DARK_WORLD.properties = new GalaxySystemProperties.Builder()
            .build();
        // spotless:on
    }
    // endregion

    // region init Id
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
