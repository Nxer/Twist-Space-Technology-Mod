package com.Nxer.TwistSpaceTechnology.common.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import tectech.recipe.TecTechRecipeMaps;

/** Additional fluids supplied through the native BEC generator and network. */
public final class BECCondensates {

    private static final List<Condensate> TYPES = new ArrayList<>();
    private static final List<Condensate> TYPES_VIEW = Collections.unmodifiableList(TYPES);
    private static final Map<Fluid, Condensate> BY_FLUID = new IdentityHashMap<>();
    private static boolean registrationClosed;

    public static final Condensate ConcentratedUUMatter = register(
        () -> MaterialsTST.EntangledConcentratedUUMatter,
        () -> MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000),
        800,
        TierEU.RECIPE_UMV);

    /** Declare types before GT initializes controller parameters; resolve source fluids later in postInit. */
    public static Condensate register(Supplier<Materials> material, Supplier<FluidStack> source, int duration,
        long eut) {
        if (registrationClosed)
            throw new IllegalStateException("Declare BEC condensates before controller initialization");
        if (duration <= 0 || eut <= 0) throw new IllegalArgumentException("BEC duration and EU/t must be positive");
        Condensate type = new Condensate(
            Objects.requireNonNull(material),
            Objects.requireNonNull(source),
            duration,
            eut);
        TYPES.add(type);
        return type;
    }

    public static List<Condensate> getTypes() {
        return TYPES_VIEW;
    }

    /** Freeze declarations when the first controller allocates its parameter slots. */
    public static int getFilterCount() {
        registrationClosed = true;
        return TYPES.size();
    }

    public static Condensate find(Fluid sourceOrCondensate) {
        return BY_FLUID.get(sourceOrCondensate);
    }

    public static boolean isCondensate(Fluid fluid) {
        Condensate type = find(fluid);
        return type != null && type.fluid == fluid;
    }

    public static void initializeFluids() {
        registrationClosed = true;
        for (Condensate type : TYPES) {
            type.sourceStack = Objects.requireNonNull(type.source.get())
                .copy();
            Fluid source = type.sourceStack.getFluid();
            Materials material = Objects.requireNonNull(type.material.get());
            type.fluid = Objects.requireNonNull(material.mGas, "Missing GT condensate gas: " + material.mName);
            if (type.sourceStack.amount <= 0 || BY_FLUID.containsKey(source)) {
                throw new IllegalArgumentException("Invalid or duplicate BEC source: " + material.mName);
            }
            if (source == type.fluid || BY_FLUID.containsKey(type.fluid)) {
                throw new IllegalArgumentException("Invalid or duplicate BEC condensate: " + material.mName);
            }
            type.fluid.setTemperature(0);
            BY_FLUID.put(source, type);
            BY_FLUID.put(type.fluid, type);
        }
    }

    public static void loadRecipes() {
        for (Condensate type : TYPES) {
            GTValues.RA.stdBuilder()
                .fluidInputs(type.getSourceFluid())
                .fluidOutputs(type.getEntangled(type.sourceStack.amount))
                .duration(type.duration)
                .eut(type.eut)
                .addTo(TecTechRecipeMaps.condensateGeneratorRecipes);
        }
    }

    public static final class Condensate {

        private final Supplier<Materials> material;
        private final Supplier<FluidStack> source;
        private final int duration;
        private final long eut;
        private FluidStack sourceStack;
        private Fluid fluid;

        private Condensate(Supplier<Materials> material, Supplier<FluidStack> source, int duration, long eut) {
            this.material = material;
            this.source = source;
            this.duration = duration;
            this.eut = eut;
        }

        public FluidStack getSourceFluid() {
            return Objects.requireNonNull(sourceStack, "BEC source fluid is not registered yet")
                .copy();
        }

        public Fluid getEntangledFluid() {
            return Objects.requireNonNull(fluid, "BEC condensate fluid is not registered yet");
        }

        public FluidStack getEntangled(int amount) {
            return new FluidStack(getEntangledFluid(), amount);
        }
    }
}
