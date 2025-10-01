package com.Nxer.TwistSpaceTechnology.system.SolarSystem;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * GalaxySystem - Enumeration-based system for managing dimension parameters in Minecraft.
 * <p>
 * This system provides a centralized approach to configure custom properties for different
 * dimensions using the Builder pattern. Each dimension can be configured with various
 * parameters like atmospheric gases, gravity, temperature, etc.
 * <p>
 * <b>Key Features:</b>
 * <ul>
 *   <li>Centralized storage of dimension parameters</li>
 *   <li>Flexible configuration using Builder pattern</li>
 *   <li>Fast dimension lookup by ID</li>
 *   <li>Extensible parameter system</li>
 * </ul>
 * <p>
 * <b>Usage Example:</b>
 * <pre>{@code
 * new GalaxySystemProperties.Builder(dimensionId, dimensionName)
 *     .withGas(fluid)
 *     .withGravity(0.5f)
 *     .withTemperature(300)
 *     .build()
 * }</pre>
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
    OVERWORLD(new GalaxySystemProperties.Builder(0, "Overworld")
        .withGas(Materials.Air.getFluid(1))
        .build()),
    NETHER(new GalaxySystemProperties.Builder(-1, "Nether")
        .withGas(Materials.NetherAir.getFluid(1))
        .build()),
    THE_END(new GalaxySystemProperties.Builder(1, "The End")
        .withGas(null)
        .build()),
    SPECTRE_WORLD(
        new GalaxySystemProperties.Builder(2, "SpectreWorld")
            .build()
    ),
    TWILIGHT_FOREST(
        new GalaxySystemProperties.Builder(7, "Twilight Forest")
            .build()
    ),
    MAKEMAKE(
        new GalaxySystemProperties.Builder(25, "Makemake")
            .build()
    ),
    MOON(
        new GalaxySystemProperties.Builder(28, "Moon")
            .build()
    ),
    MARS(
        new GalaxySystemProperties.Builder(29, "Mars")
            .build()
    ),
    ASTEROIDS(
        new GalaxySystemProperties.Builder(30, "Asteroids")
            .build()
    ),
    CENTAURI_BB(
        new GalaxySystemProperties.Builder(31,"Centauri Bb")
            .build()
    ),
    BARNARDA_C(
        new GalaxySystemProperties.Builder(32, "Barnarda C")
            .build()
    ),
    KUIPER_BELT(
        new GalaxySystemProperties.Builder(33, "Kuiper Belt")
            .build()
    ),
    EUROPA(
        new GalaxySystemProperties.Builder(35, "Europa")
            .build()
    ),
    IO(
        new GalaxySystemProperties.Builder(36, "Io")
            .build()
    ),
    MERCURY(
        new GalaxySystemProperties.Builder(37, "Mercury")
            .build()
    ),
    PHOBOS(
        new GalaxySystemProperties.Builder(38, "Phobos")
            .build()
    ),
    VENUS(
        new GalaxySystemProperties.Builder(39, "Venus")
            .build()
    ),
    DEIMOS(
        new GalaxySystemProperties.Builder(40, "Deimos")
            .build()
    ),
    ENCELADUS(
        new GalaxySystemProperties.Builder(41, "Enceladus")
            .build()
    ),
    CERES(
        new GalaxySystemProperties.Builder(42, "Ceres")
            .build()
    ),
    GANYMEDE(
        new GalaxySystemProperties.Builder(43, "Ganymede")
            .build()
    ),
    TITAN(
        new GalaxySystemProperties.Builder(44, "Titan")
            .build()
    ),
    CALISTO(
        new GalaxySystemProperties.Builder(45, "Calisto")
            .build()
    ),
    OBERON(
        new GalaxySystemProperties.Builder(46, "Oberon")
            .build()
    ),
    PROTEUS(
        new GalaxySystemProperties.Builder(47, "Proteus")
            .build()
    ),
    TRITON(
        new GalaxySystemProperties.Builder(48, "Triton")
            .build()
    ),
    PLUTO(
        new GalaxySystemProperties.Builder(49, "Pluto")
            .build()
    ),
    THE_OUTER_LANDS(
        new GalaxySystemProperties.Builder(50, "The Outer Lands")
            .build()
    ),
    URANUS(
        new GalaxySystemProperties.Builder(51, "Uranus")
            .build()
    ),
    SPIRIT_WORLD(
        new GalaxySystemProperties.Builder(55, "Spirit World")
            .build()
    ),
    TORMENT(
        new GalaxySystemProperties.Builder(56, "Torment")
            .build()
    ),
    BEDROCK(
        new GalaxySystemProperties.Builder(60, "Bedrock")
            .build()
    ),
    ROSS128BA(
        new GalaxySystemProperties.Builder(63, "Ross128ba")
            .build()
    ),
    ROSS128B(
        new GalaxySystemProperties.Builder(64, "Ross128b")
            .build()
    ),
    POCKET_PLANE(
        new GalaxySystemProperties.Builder(69, "Pocket Plane")
            .build()
    ),
    MIRROR(
        new GalaxySystemProperties.Builder(70, "Mirror")
            .build()
    ),
    JUPITER(
        new GalaxySystemProperties.Builder(71, "Jupiter")
            .build()
    ),
    NEPTUNE(
        new GalaxySystemProperties.Builder(74, "Neptune")
            .build()
    ),
    SATURN(
        new GalaxySystemProperties.Builder(77, "Saturn")
            .build()
    ),
    BARNARDA_E(
        new GalaxySystemProperties.Builder(81, "Barnarda E")
            .build()
    ),
    BARNARDA_F(
        new GalaxySystemProperties.Builder(82, "Barnarda F")
            .build()
    ),
    HAUMEA(
        new GalaxySystemProperties.Builder(83, "Haumea")
            .build()
    ),
    VEGA_B(
        new GalaxySystemProperties.Builder(84, "Vega B")
            .build()
    ),
    T_CETI_E(
        new GalaxySystemProperties.Builder(85, "T Ceti E")
            .build()
    ),
    MIRANDA(
        new GalaxySystemProperties.Builder(86, "Miranda")
            .build()
    ),
    NEPER(
        new GalaxySystemProperties.Builder(90, "Neper")
            .build()
    ),
    MAAHES(
        new GalaxySystemProperties.Builder(91, "Maahes")
            .build()
    ),
    ANUBIS(
        new GalaxySystemProperties.Builder(92, "Anubis")
            .build()
    ),
    HORUS(
        new GalaxySystemProperties.Builder(93, "Horus")
            .build()
    ),
    SETH(
        new GalaxySystemProperties.Builder(94, "Seth")
            .build()
    ),
    THE_LAST_MILLENIUM(
        new GalaxySystemProperties.Builder(112, "The Last Millenium")
            .build()
    ),
    DIMENSION_DARK_WORLD(
        new GalaxySystemProperties.Builder(227, "dimensionDarkWorld")
            .build()
    );
    // spotless:on
    // endregion

    // region Variables
    private final GalaxySystemProperties properties;
    private static final Map<Integer, GalaxySystem> DIMENSIONS;
    // endregion

    // region Constructor
    GalaxySystem(GalaxySystemProperties properties) {
        this.properties = properties;
    }
    // endregion

    // region Static initialization
    static {
        Map<Integer, GalaxySystem> dimensionsMap = new HashMap<>();
        for (GalaxySystem system : values()) {
            dimensionsMap.put(system.getProperties().getId(), system);
        }
        DIMENSIONS = Collections.unmodifiableMap(dimensionsMap);
    }
    // endregion

    // region Getters
    /**
     * Gets the galaxy system properties
     * @return the galaxy system properties
     */
    public GalaxySystemProperties getProperties() {
        return properties;
    }

    public int getId() {
        return properties.getId();
    }

    /**
     * Gets GalaxySystem by dimensionId
     * @param dimensionId the dimension ID
     * @return corresponding GalaxySystem or null if not found
     */
    public static GalaxySystem getGalaxySystemById(int dimensionId) {
        return DIMENSIONS.get(dimensionId);
    }

    /**
     * Gets GalaxySystem by BaseMetaTileEntity
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
     * Gets all registered GalaxySystem entries
     * @return unmodifiable map of dimension ID to GalaxySystem
     */
    public static Map<Integer, GalaxySystem> getAllDimensions() {
        return DIMENSIONS;
    }
    // endregion
}
