package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Structure;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.common.items.ItemIntegratedCircuit;

/** One numeric operating setting shared by every structure in a packaged module. Circuits are never paid assets. */
public final class BottledChaosCircuitParameter {

    public static final long UNSET = -1;
    private static final String NBT_KEY = "circuitParameter";

    @Desugar
    public record Definition(String name, long minimum, long maximum, long defaultValue) {

        boolean accepts(long value) {
            return value >= minimum && value <= maximum;
        }
    }

    /** Machine adapters declare meaning and bounds here; parsing, storage and batch handling remain shared. */
    public static Definition definition(Structure structure) {
        var godforge = BottledChaosComplexStructures.godforgePreset(structure);
        if (godforge != null) {
            // #tr tst.bottledchaos.shared.parameter.fuel_factor
            // # Fuel factor
            // #zh_CN 燃料系数
            return new Definition(
                "tst.bottledchaos.shared.parameter.fuel_factor",
                1,
                godforge.fuelFactor(),
                godforge.fuelFactor());
        }
        return null;
    }

    /** Extract once from the whole transaction, before dividing construction materials by the controller count. */
    public static long extract(Map<TST_ItemID, Long> materials) {
        long value = UNSET;
        Iterator<Map.Entry<TST_ItemID, Long>> iterator = materials.entrySet()
            .iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            ItemStack stack = entry.getKey()
                .getItemStack(1);
            if (!(stack.getItem() instanceof ItemIntegratedCircuit)) continue;
            value = Math
                .addExact(Math.max(0, value), TSTUtils.getIntegratedCircuitConfigurationValue(stack, entry.getValue()));
            iterator.remove();
        }
        return value;
    }

    /** Used by both packaging and saved-module validation; a setting cannot grant an unavailable upgrade. */
    public static Structure configure(Structure structure, long value) {
        if (structure == null) return null;
        Definition definition = definition(structure);
        if (definition == null) return value == UNSET ? structure : null;
        if (value == UNSET) value = definition.defaultValue();
        return definition.accepts(value) ? structure.withCircuitParameter(value) : null;
    }

    public static long read(NBTTagCompound data) {
        if (!data.hasKey(NBT_KEY)) return UNSET;
        return data.hasKey(NBT_KEY, 4) ? data.getLong(NBT_KEY) : -2;
    }

    public static void write(NBTTagCompound data, long value) {
        if (value != UNSET) data.setLong(NBT_KEY, value);
    }

    public static String description(Structure structure) {
        Definition definition = definition(structure);
        if (definition == null) return null;
        // #tr tst.bottledchaos.shared.parameter.value
        // # %s: %s (%s-%s)
        // #zh_CN %s: %s (%s-%s)
        return StatCollector.translateToLocalFormatted(
            "tst.bottledchaos.shared.parameter.value",
            StatCollector.translateToLocal(definition.name()),
            structure.circuitParameter() == UNSET ? definition.defaultValue() : structure.circuitParameter(),
            definition.minimum(),
            definition.maximum());
    }

    public static String instruction() {
        // #tr tst.bottledchaos.shared.parameter.circuits
        // # Sum circuit number x count; one setting per batch, circuits are not consumed.
        // #zh_CN 编程电路编号 x 数量求和; 整批共用, 电路不消耗.
        return StatCollector.translateToLocal("tst.bottledchaos.shared.parameter.circuits");
    }
}
