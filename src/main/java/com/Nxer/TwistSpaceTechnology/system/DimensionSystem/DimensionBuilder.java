package com.Nxer.TwistSpaceTechnology.system.DimensionSystem;

import net.minecraftforge.fluids.FluidStack;

/**
 * DimensionBuilder - Builder pattern implementation for creating DimensionSystem instances
 *
 * <p>
 * Provides a fluent interface for configuring and constructing dimension objects.
 * This pattern allows for clear, readable dimension configuration with method chaining.
 * </p>
 *
 * <p>
 * <b>Usage Example:</b>
 * 
 * <pre>
 * 
 * {
 *     &#64;code
 *     DimensionSystem overworld = new DimensionBuilder().setId(0)
 *         .setName("Overworld")
 *         .setGenerateGas(Materials.Air.getGas(1))
 *         .constructDimension();
 * }
 * </pre>
 *
 * @author EvgenWarGold
 * @version 1.0
 * @see DimensionSystem
 */
public class DimensionBuilder {

    // region Parameters
    private int id;
    private String name;
    private FluidStack generateGas;
    // endregion

    // region Constructor
    public DimensionSystem constructDimension() {
        return new DimensionSystem(
            // spotless:off
            id,
            name,
            generateGas
            // spotless:on
        );
    }
    // endregion

    // region Setters
    public DimensionBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public DimensionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DimensionBuilder setGenerateGas(FluidStack generateGas) {
        this.generateGas = generateGas;
        return this;
    }
    // endregion
}
