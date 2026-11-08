package com.Nxer.TwistSpaceTechnology.mixin;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.Nxer.TwistSpaceTechnology.common.material.BECCondensates;

import gregtech.api.enums.CondensateType;
import tectech.thing.metaTileEntity.multi.base.parameter.FluidParameter;
import tectech.thing.metaTileEntity.multi.bec.MTEBECDiode;

@Mixin(value = MTEBECDiode.class, remap = false)
public abstract class MTEBECDiodeMixin {

    @Shadow
    private FluidParameter[] filterParameters;

    @Inject(method = "initParameters", at = @At("RETURN"), require = 1)
    private void tst$addCondensateFilterSlots(CallbackInfo ci) {
        int nativeCount = filterParameters.length;
        filterParameters = Arrays.copyOf(filterParameters, nativeCount + BECCondensates.getFilterCount());
        for (int slot = nativeCount; slot < filterParameters.length; slot++) {
            filterParameters[slot] = new FluidParameter(null, "GT5U.gui.text.bec-filter-n", "filter" + slot, slot + 1);
        }
    }

    @Inject(method = "deriveFiltersFromInputHatches", at = @At("HEAD"), cancellable = true, require = 1)
    private void tst$deriveExtendedFilters(CallbackInfo ci) {
        EnumSet<CondensateType> present = EnumSet.noneOf(CondensateType.class);
        Set<BECCondensates.Condensate> additional = new HashSet<>();

        for (FluidStack stack : ((MTEBECDiode) (Object) this).getStoredFluids()) {
            Fluid fluid = stack.getFluid();
            BECCondensates.Condensate extra = BECCondensates.find(fluid);
            if (extra != null) additional.add(extra);
            CondensateType type = CondensateType.getCondensateTypeBySource(fluid);
            if (type == null) type = CondensateType.getCondensateType(fluid);
            if (type != null) present.add(type);
        }

        // Keep native ordinal order, then append additional types in declaration order.
        int slot = 0;
        for (CondensateType type : present) {
            tst$setFilter(slot++, type.getEntangledFluid());
        }
        for (BECCondensates.Condensate type : BECCondensates.getTypes()) {
            if (additional.contains(type)) tst$setFilter(slot++, type.getEntangledFluid());
        }
        for (; slot < filterParameters.length; slot++) {
            tst$setFilter(slot, null);
        }
        ci.cancel();
    }

    @Unique
    private void tst$setFilter(int slot, Fluid fluid) {
        if (filterParameters[slot].getValue() != fluid) filterParameters[slot].setValue(fluid);
    }
}
