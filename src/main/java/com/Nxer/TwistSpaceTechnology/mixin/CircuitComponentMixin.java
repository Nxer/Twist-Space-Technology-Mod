package com.Nxer.TwistSpaceTechnology.mixin;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.Nxer.TwistSpaceTechnology.common.item.NACComponentRegistry;

import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitCalibration;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

@Mixin(value = CircuitComponent.class, remap = false)
public abstract class CircuitComponentMixin {

    @Shadow
    @Final
    @Mutable
    private static CircuitComponent[] $VALUES;

    @Shadow
    @Final
    @Mutable
    public static CircuitComponent[] VALUES;

    @Shadow
    @Final
    private static Int2ObjectMap<CircuitComponent> META_IDS;

    @Invoker("<init>")
    public static CircuitComponent tst$createCircuitComponent(String name, int ordinal, int id, String nameKey,
        Supplier<ItemStack> realComponent, Supplier<CircuitComponent> componentForProcessed, boolean isProcessed,
        CircuitCalibration circuitType) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void tst$registerCircuitComponents(CallbackInfo ci) {
        CircuitComponent[] components = NACComponentRegistry
            .register($VALUES.length, CircuitComponentMixin::tst$createCircuitComponent);

        // NAC uses both enum iteration and meta lookup, so every registry must receive the new components.
        $VALUES = ArrayUtils.addAll($VALUES, components);
        VALUES = $VALUES;
        for (CircuitComponent component : components) {
            META_IDS.put(component.metaId, component);
        }
    }
}
