package com.Nxer.TwistSpaceTechnology.system.DimensionSystem;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * DimensionSystem - Class-based system for managing dimension parameters in Minecraft 1.7.10
 * <p>
 * This system provides a centralized approach to configure custom properties for different
 * dimensions using the Builder pattern. Each dimension can be configured with various
 * parameters like atmospheric gases, and can be extended with additional properties.
 * <p>
 * <b>Architecture:</b>
 * <ul>
 * <li><b>DimensionSystem</b> (this class) - contains dimension instances and methods for working with dimensions</li>
 * <li><b>DimensionBuilder</b> - builder pattern for dimension configuration and creation</li>
 * <li><b>DimensionSystemInit</b> - initializes all dimensions from various mod configurations</li>
 * </ul>
 * <p>
 * <b>Key Features:</b>
 * <ul>
 * <li>Centralized storage of dimension parameters</li>
 * <li>Flexible configuration using Builder pattern</li>
 * <li>Fast dimension lookup by ID and name via internal maps</li>
 * <li>Extensible parameter system</li>
 * <li>Automatic dimension registration upon construction</li>
 * <li>Cross-mod compatibility with fallback dimension IDs</li>
 * </ul>
 * <p>
 * <b>Workflow:</b>
 * <ol>
 * <li>Call {@link DimensionSystemInit#init()} to initialize all dimensions from mod configs</li>
 * <li>Use static getter methods to retrieve dimension information</li>
 * <li>Access dimension properties through instance methods</li>
 * </ol>
 * <p>
 * <b>Adding New Parameters:</b>
 * To extend the system with new parameters, you can:
 * <ol>
 * <li>Add new fields to DimensionSystem class</li>
 * <li>Extend DimensionBuilder with new configuration methods</li>
 * <li>Update DimensionSystemInit to set new parameter values</li>
 * <li>Or use GalaxySystem1 for more complex property management</li>
 * </ol>
 * <p>
 * <b>Usage Example:</b>
 *
 * <pre>
 * {@code
 * // Initialize all dimensions (call during mod initialization)
 * DimensionSystemInit.init();
 *
 * // Getting dimension by ID
 * DimensionSystem overworld = DimensionSystem.getDimensionById(0);
 * FluidStack atmosphere = overworld.getGenerateGas();
 *
 * // Getting dimension by name
 * DimensionSystem nether = DimensionSystem.getDimensionByName("Nether");
 *
 * // Check if dimension exists
 * boolean exists = DimensionSystem.dimensionExists(7); // Twilight Forest
 *
 * // Get all registered dimensions
 * Collection<DimensionSystem> allDimensions = DimensionSystem.getAllDimensions();
 *
 * // Using the builder directly
 * DimensionSystem customDim = new DimensionBuilder()
 *     .setId(100)
 *     .setName("Custom Dimension")
 *     .setGenerateGas(Materials.Air.getGas(1))
 *     .constructDimension();
 * }
 * </pre>
 *
 * @author EvgenWarGold
 * @since 1.0
 * @see DimensionBuilder
 * @see DimensionSystemInit
 */
public class DimensionSystem {

    // region Dimension
    public static DimensionSystem Overworld;
    public static DimensionSystem Nether;
    public static DimensionSystem TheEnd;
    public static DimensionSystem SpectreWorld;
    public static DimensionSystem TwilightForest;
    public static DimensionSystem MakeMake;
    public static DimensionSystem Moon;
    public static DimensionSystem Mars;
    public static DimensionSystem Asteroids;
    public static DimensionSystem CentauriBB;
    public static DimensionSystem BarnardaC;
    public static DimensionSystem KuiperBelt;
    public static DimensionSystem Europa;
    public static DimensionSystem Io;
    public static DimensionSystem Mercury;
    public static DimensionSystem Phobos;
    public static DimensionSystem Venus;
    public static DimensionSystem Deimos;
    public static DimensionSystem Enceladus;
    public static DimensionSystem Ceres;
    public static DimensionSystem Ganymede;
    public static DimensionSystem Titan;
    public static DimensionSystem Calisto;
    public static DimensionSystem Oberon;
    public static DimensionSystem Proteus;
    public static DimensionSystem Triton;
    public static DimensionSystem Pluto;
    public static DimensionSystem TheOuterLands;
    public static DimensionSystem Uranus;
    public static DimensionSystem SpiritWorld;
    public static DimensionSystem Torment;
    public static DimensionSystem Bedrock;
    public static DimensionSystem Ross128BA;
    public static DimensionSystem Ross128BB;
    public static DimensionSystem PocketPlane;
    public static DimensionSystem Mirror;
    public static DimensionSystem Jupiter;
    public static DimensionSystem Neptune;
    public static DimensionSystem Saturn;
    public static DimensionSystem BarnardaE;
    public static DimensionSystem BarnardaF;
    public static DimensionSystem Haumea;
    public static DimensionSystem VegaB;
    public static DimensionSystem TcetiE;
    public static DimensionSystem Miranda;
    public static DimensionSystem Neper;
    public static DimensionSystem Maahes;
    public static DimensionSystem Anubis;
    public static DimensionSystem Horus;
    public static DimensionSystem Seth;
    public static DimensionSystem TheLastMillenium;
    public static DimensionSystem ToxicEverglades;
    // endregion

    // region Parameters
    private final int mId;
    private final String mName;
    private final FluidStack mGenerateGas;
    private static final Map<Integer, DimensionSystem> DIMENSIONS_MAP = new ConcurrentHashMap<>();
    private static final Map<String, DimensionSystem> DIMENSIONS_BY_NAME = new ConcurrentHashMap<>();
    // endregion

    // region Constructor
    DimensionSystem(
        // spotless:off
        int id,
        String name,
        @Nullable FluidStack generateGas
        // spotless:on
    ) {
        mId = id;
        mName = name;
        mGenerateGas = generateGas;

        DIMENSIONS_MAP.put(id, this);
        DIMENSIONS_BY_NAME.put(name, this);
    }
    // endregion

    // region Getters
    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Nullable
    public FluidStack getGenerateGas() {
        return mGenerateGas;
    }
    // endregion

    // region static getters
    public static Map<Integer, DimensionSystem> getDimensionsMap() {
        return Collections.unmodifiableMap(DIMENSIONS_MAP);
    }

    public static Collection<DimensionSystem> getAllDimensions() {
        return Collections.unmodifiableCollection(DIMENSIONS_MAP.values());
    }

    public static DimensionSystem getDimensionById(int id) {
        return DIMENSIONS_MAP.get(id);
    }

    public static DimensionSystem getDimensionByName(String name) {
        return DIMENSIONS_BY_NAME.get(name);
    }

    public static boolean dimensionExists(int id) {
        return DIMENSIONS_MAP.containsKey(id);
    }

    public static boolean dimensionExists(String name) {
        return DIMENSIONS_BY_NAME.containsKey(name);
    }

    public static DimensionSystem getDimensionByGregTechTileEntity(IGregTechTileEntity mBaseTileEntity) {
        if (mBaseTileEntity == null || mBaseTileEntity.getWorld() == null) {
            return null;
        }
        int worldId = mBaseTileEntity.getWorld().provider.dimensionId;
        return getDimensionById(worldId);
    }
    // endregion
}
