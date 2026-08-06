package com.Nxer.TwistSpaceTechnology.common.item;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitCalibration;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

/**
 * Registers custom NAC components and keeps their real item associations.
 * Processed components point back to their unprocessed component here.
 */
public final class NACComponentRegistry {

    public static CircuitComponent opticalSOC;
    public static CircuitComponent processedOpticalSOC;
    public static CircuitComponent boltInfinity;
    public static CircuitComponent processedBoltInfinity;

    /**
     * Creates the real and processed fake stacks used by NAC recipes.
     */
    public static CircuitComponent[] register(int firstOrdinal, ComponentFactory factory) {
        opticalSOC = factory.create(
            "TSTOpticalSOC",
            firstOrdinal,
            3000,
            "gt.circuitcomponent.chipopticalsoc",
            () -> GTCMItemList.OpticalSOC.get(1),
            null,
            false,
            CircuitCalibration.NONE);

        // #tr gt.circuitcomponent.processed.chipopticalsoc
        // # Resonant Gravitational Constraint Optical Quantum Crystal
        // #zh_CN 谐振引力约束光量子晶体
        processedOpticalSOC = factory.create(
            "TSTProcessedOpticalSOC",
            firstOrdinal + 1,
            3100,
            "gt.circuitcomponent.processed.chipopticalsoc",
            null,
            () -> opticalSOC,
            true,
            CircuitCalibration.NONE);

        boltInfinity = factory.create(
            "TSTBoltInfinity",
            firstOrdinal + 2,
            3200,
            "gt.circuitcomponent.bolt.infinity",
            () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Infinity, 1),
            null,
            false,
            CircuitCalibration.NONE);

        // #tr gt.circuitcomponent.bolt.processed.infinity
        // # Packaged Infinity Bolt
        // #zh_CN 盒装无尽螺栓
        processedBoltInfinity = factory.create(
            "TSTProcessedBoltInfinity",
            firstOrdinal + 3,
            3300,
            "gt.circuitcomponent.bolt.processed.infinity",
            null,
            () -> boltInfinity,
            true,
            CircuitCalibration.NONE);

        return new CircuitComponent[] { opticalSOC, processedOpticalSOC, boltInfinity, processedBoltInfinity };
    }

    @FunctionalInterface
    public interface ComponentFactory {

        CircuitComponent create(String name, int ordinal, int id, String nameKey, Supplier<ItemStack> realComponent,
            Supplier<CircuitComponent> componentForProcessed, boolean isProcessed, CircuitCalibration circuitType);
    }
}
