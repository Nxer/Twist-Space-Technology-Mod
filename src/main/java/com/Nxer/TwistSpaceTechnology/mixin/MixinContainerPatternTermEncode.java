package com.Nxer.TwistSpaceTechnology.mixin;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper;
import com.Nxer.TwistSpaceTechnology.util.PatternConversionWorldSavedData;

import appeng.container.AEBaseContainer;
import appeng.container.implementations.ContainerPatternTerm;
import appeng.container.slot.SlotRestrictedInput;

/**
 * Mixin to intercept ContainerPatternTerm.encode() at RETURN.
 * encode() is void — after it returns, the pattern ItemStack is already
 * in patternSlotOUT. We modify its NBT in-place and re-put to trigger sync.
 * <p>
 * The conversion is per-player toggleable: glass ampoules are converted by
 * default, crystal essences only when the player opted in (see
 * {@link PatternConversionWorldSavedData#isPatternConversionEnabledFor} /
 * {@link PatternConversionWorldSavedData#isCrystalConversionEnabledFor}).
 */

// Previous failed attempts targeted decode/read paths (getPatternForItem,
// setTagCompound, writeToNBT), not the actual encoding method.
@Mixin(value = ContainerPatternTerm.class, remap = false)
public abstract class MixinContainerPatternTermEncode {

    @Shadow(remap = false)
    private SlotRestrictedInput patternSlotOUT;

    @Inject(method = "encode", at = @At("RETURN"))
    private void tst$convertOnEncode(CallbackInfo ci) {
        // getPlayerInv() is inherited from AEBaseContainer; call it via cast to avoid
        // the mixin AP not resolving parent-class methods in @Shadow.
        InventoryPlayer playerInv = ((AEBaseContainer) (Object) this).getPlayerInv();
        if (playerInv == null || playerInv.player == null) {
            return;
        }
        String playerName = playerInv.player.getCommandSenderName();
        // The two conversions are independent: each one reads its own per-player toggle.
        // Only skip entirely when BOTH are disabled (so turning off ampoules alone must
        // not suppress crystal essence conversion).
        boolean convertAmpoule = PatternConversionWorldSavedData.isPatternConversionEnabledFor(playerName);
        boolean convertCrystal = PatternConversionWorldSavedData.isCrystalConversionEnabledFor(playerName);
        if (!convertAmpoule && !convertCrystal) {
            return;
        }
        // FMLLog.info("[TST Mixin] encode() RETURN reached");
        ItemStack out = this.patternSlotOUT.getStack();
        /*
         * if (out == null) {
         * FMLLog.info("[TST Mixin] patternSlotOUT stack is null");
         * return;
         * }
         * if (!out.hasTagCompound()) {
         * FMLLog.info("[TST Mixin] patternSlotOUT stack has no NBT tag");
         * return;
         * }
         * FMLLog.info("[TST Mixin] pattern NBT: %s", out.getTagCompound().toString());
         */

        if (ItemEssentiaHelper.convertPatternNBT(out.getTagCompound(), convertAmpoule, convertCrystal)) {
            // FMLLog.info("[TST Mixin] Converted glass ampoules on encode() RETURN");
            this.patternSlotOUT.putStack(out); // re-put to force slot change sync
        } else {
            // FMLLog.info("[TST Mixin] convertPatternNBT returned false - no glass ampoule matched");
        }
    }
}
