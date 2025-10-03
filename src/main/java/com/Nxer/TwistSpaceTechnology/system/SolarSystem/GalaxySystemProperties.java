package com.Nxer.TwistSpaceTechnology.system.SolarSystem;

import net.minecraftforge.fluids.FluidStack;

public class GalaxySystemProperties {

    // region Properties
    // Other properties
    private final FluidStack gas;

    // endregion

    // region Builder Class
    public static class Builder {

        // Other properties
        private FluidStack gas;

        public Builder() {}

        public Builder withGas(FluidStack gas) {
            this.gas = gas;
            return this;
        }

        public GalaxySystemProperties build() {
            return new GalaxySystemProperties(this);
        }
    }
    // endregion

    // region Constructor
    private GalaxySystemProperties(Builder builder) {
        // Other properties
        this.gas = builder.gas;
    }
    // endregion

    // region Getters
    public FluidStack getGas() {
        return gas;
    }
    // endregion
}
