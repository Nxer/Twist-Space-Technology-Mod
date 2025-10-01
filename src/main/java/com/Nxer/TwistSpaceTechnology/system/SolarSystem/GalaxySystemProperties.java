package com.Nxer.TwistSpaceTechnology.system.SolarSystem;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;

public class GalaxySystemProperties {

    // region Properties
    // Base properties
    private final int id;
    private final String name;
    // Other properties
    private final FluidStack gas;

    // endregion

    // region Builder Class
    public static class Builder {

        // Base properties
        private final int id;
        private final String name;
        // Other properties
        private FluidStack gas;

        public Builder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder withGas(FluidStack gas) {
            this.gas = gas;
            return this;
        }

        public GalaxySystemProperties build() {
            if (name == null || name.isEmpty()) {
                throw new IllegalStateException("Dimension name cannot be null or empty");
            }
            return new GalaxySystemProperties(this);
        }
    }
    // endregion

    // region Constructor
    private GalaxySystemProperties(Builder builder) {
        // Base properties
        this.id = builder.id;
        this.name = builder.name;
        // Other properties
        this.gas = builder.gas;
    }
    // endregion

    // region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FluidStack getGas() {
        return gas;
    }
    // endregion
}
