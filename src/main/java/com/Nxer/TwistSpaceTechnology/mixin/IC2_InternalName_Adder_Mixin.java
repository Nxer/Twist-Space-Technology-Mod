package com.Nxer.TwistSpaceTechnology.mixin;

import ic2.core.init.InternalName;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = InternalName.class, remap = false)
public class IC2_InternalName_Adder_Mixin {

    @Shadow
    @Final
    @Mutable
    private static InternalName[] $VALUES;

    @Invoker("<init>")
    public static InternalName tst$createInternalName(String name, int ordinal) {
        throw new AssertionError();
    }

    /**
     * Use {@link Enum#valueOf(Class, String)} to get this instance.
     */
    @Unique
    private static final InternalName TST$CARDIGAN = tst$newInternalName("Cardigan");

    @Unique
    private static InternalName tst$newInternalName(String name) {
        InternalName newCreated = tst$createInternalName(name, $VALUES.length);
        $VALUES = ArrayUtils.add($VALUES, newCreated);
        return newCreated;
    }

}
