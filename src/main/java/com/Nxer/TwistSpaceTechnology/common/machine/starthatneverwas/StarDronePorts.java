package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.TSTMEOutputTransaction;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.ITSTSegmentedItemInput;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellHelper.InputSource;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellInputHatch.LongFluidInputs;

import ggfab.mte.MTELinkedInputBus;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;

/** Dedicated positions never enter the host's general recipe input/output lists. */
public final class StarDronePorts {

    public long heldHelium;
    private MTEHatchInputBus input;
    private MTEHatchInput fluid;
    private MTEHatchOutputBus output;

    public void clear() {
        input = null;
        fluid = null;
        output = null;
    }

    public int installedMask() {
        return (input != null && input.isValid() ? 1 : 0) | (output != null && output.isValid() ? 2 : 0)
            | (fluid != null && fluid.isValid() ? 8 : 0);
    }

    public static boolean acceptsItemInput(IMetaTileEntity hatch) {
        return hatch instanceof MTEHatchInputBus && !(hatch instanceof IRecipeProcessingAwareHatch)
            && !(hatch instanceof MTELinkedInputBus)
            && !(hatch instanceof ITSTSegmentedItemInput);
    }

    public static boolean acceptsFluidInput(IMetaTileEntity hatch) {
        return hatch instanceof MTEHatchInput;
    }

    public boolean input(IGregTechTileEntity base, int texture) {
        if (!acceptsItemInput(base.getMetaTileEntity())) return false;
        input = (MTEHatchInputBus) base.getMetaTileEntity();
        input.updateTexture(texture);
        return true;
    }

    public boolean fluid(IGregTechTileEntity base, int texture) {
        if (!(base.getMetaTileEntity() instanceof MTEHatchInput hatch)) return false;
        fluid = hatch;
        hatch.updateTexture(texture);
        return true;
    }

    public boolean output(IGregTechTileEntity base, int texture) {
        if (!acceptsDroneOutput(base.getMetaTileEntity())) return false;
        output = (MTEHatchOutputBus) base.getMetaTileEntity();
        output.updateTexture(texture);
        return true;
    }

    public static boolean acceptsDroneOutput(IMetaTileEntity hatch) {
        // Specialized GT buses can void items. Only the stock inventory and ME may take custody of drones.
        return hatch instanceof MTEHatchOutputBusME || hatch != null && hatch.getClass() == MTEHatchOutputBus.class;
    }

    public List<ItemStack> drones() {
        List<ItemStack> result = new ArrayList<>();
        if (input == null || !input.isValid()) return result;
        for (int i = 0; i < input.getSizeInventory(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack != null && stack.stackSize > 0) result.add(stack);
        }
        return result;
    }

    public InputSource fuelSource() {
        FluidStack helium = Materials.Helium.getPlasma(1);
        if (helium == null) return null;
        InputSource tank = fluid == null || !fluid.isValid() ? null : LongFluidInputs.fromHatch(fluid, helium);
        if (tank == null && heldHelium == 0) return null;
        // Any confirmed draw belongs to this controller until GT accepts the entire startup.
        return new InputSource(
            this,
            helium,
            () -> heldHelium + Math.min(Long.MAX_VALUE - heldHelium, tank == null ? 0 : tank.available.getAsLong()),
            amount -> {
                if (amount > heldHelium && tank != null) heldHelium += tank.extract.applyAsLong(amount - heldHelium);
                return Math.min(amount, heldHelium);
            },
            true);
    }

    public void markInputsDirty() {
        if (input != null) {
            input.updateSlots();
            input.markDirty();
        }
        if (fluid != null) {
            fluid.updateSlots();
            fluid.markDirty();
        }
    }

    public boolean canReturn(List<ItemStackLong> returns) {
        if (returns.isEmpty()) return true;
        if (output == null || !output.isValid()) return false;
        if (output instanceof MTEHatchOutputBusME me) {
            var transaction = TSTMEOutputTransaction.forItems(me, true, true);
            return returns.stream()
                .allMatch(e -> transaction.reserve(e.itemStack(), e.stackSize()) == e.stackSize());
        }
        var transaction = new ItemEjectionHelper(Collections.singletonList(output), true, true);
        return returns.stream()
            .allMatch(e -> storeDrones(transaction, e) == e.stackSize());
    }

    public void deliver(List<ItemStackLong> returns) {
        if (returns.isEmpty() || output == null || !output.isValid()) return;
        StarBatch.deliverItems(returns, entry -> {
            long accepted;
            if (output instanceof MTEHatchOutputBusME me) {
                var transaction = TSTMEOutputTransaction.forItems(me, false, true);
                accepted = transaction.reserve(entry.itemStack(), entry.stackSize());
                transaction.commit();
            } else {
                var transaction = new ItemEjectionHelper(Collections.singletonList(output), true);
                accepted = storeDrones(transaction, entry);
                if (accepted > 0) transaction.commit();
            }
            return entry.stackSize() - accepted;
        });
    }

    private static int storeDrones(ItemEjectionHelper transaction, ItemStackLong entry) {
        // One stock bus holds fewer than Integer.MAX_VALUE items. The return ledger retains the full long balance.
        ItemStack offered = GTUtility.copyAmountUnsafe(GTUtility.longToInt(entry.stackSize()), entry.itemStack());
        return transaction.ejectStack(offered);
    }
}
