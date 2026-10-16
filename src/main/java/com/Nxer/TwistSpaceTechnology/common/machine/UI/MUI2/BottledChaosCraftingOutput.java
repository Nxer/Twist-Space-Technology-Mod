package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import java.io.IOException;
import java.util.function.BooleanSupplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.Preview;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

/** Per-viewer output slots. Native container transfers retain their normal capacity and click handling. */
public final class BottledChaosCraftingOutput {

    private static final int SELECT_RESULT = 2;
    private static final int SYNC_REVISION = 3;
    private final MM_BottledChaos machine;
    private final BooleanSupplier panelOpen;
    private final ItemStackHandler display = new ItemStackHandler(2);
    private Preview selected;
    private int selectedVariant;
    private long selectionExpires;
    private long clientRevision = -1;

    public BottledChaosCraftingOutput(MM_BottledChaos machine, BooleanSupplier panelOpen) {
        this.machine = machine;
        this.panelOpen = panelOpen;
    }

    private BottledChaosCrafting crafting() {
        return machine.getCredentialCrafting();
    }

    private long tick() {
        return machine.getBaseMetaTileEntity()
            .getWorld()
            .getTotalWorldTime();
    }

    private boolean isClient() {
        return !machine.getBaseMetaTileEntity()
            .isServerSide();
    }

    private ItemStack output(int slot) {
        if (isClient()) {
            return BottledChaosCrafting.displayStack(display.getStackInSlot(slot - 9));
        }
        ItemStack result = crafting().result(slot);
        if (result != null) return result;
        Preview preview = crafting().preview();
        if (preview == null) return null;
        int variant = selectionValid() ? selectedVariant : (int) ((tick() / 40) % preview.variants());
        return preview.output(slot, variant);
    }

    private boolean selectionValid() {
        return tick() <= selectionExpires && crafting().isCurrent(selected);
    }

    /** A client selects only an already validated output, never arbitrary module NBT. */
    private void select(long revision, ItemStack primary) {
        selected = null;
        if (!panelOpen.getAsBoolean()) return;
        Preview preview = crafting().preview();
        if (preview == null || preview.revision() != revision || !crafting().canTake(preview)) return;
        for (int variant = 0; variant < preview.variants(); variant++) {
            if (!ItemStack.areItemStacksEqual(primary, preview.output(9, variant))) continue;
            selected = preview;
            selectedVariant = variant;
            selectionExpires = tick() + 10;
            return;
        }
    }

    private boolean take(int slot) {
        if (crafting().result(slot) != null) {
            crafting().takeResult(slot);
            return true;
        }
        return selectionValid() && crafting().take(selected, selectedVariant, slot);
    }

    public ItemSlotSH slot(int index, String group) {
        ModularSlot slot = new ResultSlot(index).slotGroup(group)
            .accessibility(false, true);
        return new ItemSlotSH(slot) {

            private long sentRevision = Long.MIN_VALUE;

            @Override
            public void detectAndSendChanges(boolean init) {
                if (!getSyncManager().isClient()) {
                    Preview preview = crafting().preview();
                    long revision = preview == null ? -1 : preview.revision();
                    if (init || sentRevision != revision) {
                        sentRevision = revision;
                        syncToClient(SYNC_REVISION, buffer -> buffer.writeLong(revision));
                    }
                }
                super.detectAndSendChanges(init);
            }

            @Override
            protected void onSlotUpdate(ItemStack stack, boolean onlyAmountChanged, boolean client, boolean init) {
                if (client) display.setStackInSlot(index - 9, stack == null ? null : stack.copy());
                super.onSlotUpdate(stack, onlyAmountChanged, client, init);
            }

            @Override
            public void readOnClient(int id, PacketBuffer buffer) {
                if (id == SYNC_REVISION) clientRevision = buffer.readLong();
                else super.readOnClient(id, buffer);
            }

            @Override
            public void readOnServer(int id, PacketBuffer buffer) throws IOException {
                if (id == SELECT_RESULT) select(buffer.readLong(), NetworkUtils.readItemStack(buffer));
                else super.readOnServer(id, buffer);
            }
        };
    }

    private final class ResultSlot extends ModularSlot {

        private final int index;
        private boolean extracted;

        private ResultSlot(int index) {
            super(display, index - 9);
            this.index = index;
        }

        private CraftingContainer container() {
            return (CraftingContainer) getSyncHandler().getSyncManager()
                .getContainer();
        }

        private void takeChecked() {
            if (extracted) return;
            if (!take(index)) throw new RejectedTake();
            extracted = true;
        }

        @Override
        public ItemStack getStack() {
            return output(index);
        }

        @Override
        public boolean func_111238_b() {
            return panelOpen.getAsBoolean() && super.func_111238_b();
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }

        @Override
        public boolean canDragIntoSlot() {
            return false;
        }

        @Override
        public int getItemStackLimit(ItemStack stack) {
            // SlotItemHandler's default implementation temporarily clears the handler to simulate insertion.
            return 1;
        }

        @Override
        public boolean canTakeStack(EntityPlayer player) {
            if (isClient()) {
                if (!panelOpen.getAsBoolean() || getStack() == null) return false;
                // Native click prediction runs before the window-click packet, including Shift and hotkeys.
                getSyncHandler().sync(SELECT_RESULT, buffer -> {
                    buffer.writeLong(clientRevision);
                    NetworkUtils.writeItemStack(buffer, display.getStackInSlot(0));
                });
                return true;
            }
            if (!panelOpen.getAsBoolean() || getStack() == null
                || crafting().result(index) == null && (!selectionValid() || !crafting().canTake(selected))) {
                if (container().clicking) throw new RejectedTake();
                return false;
            }
            return container().prepareTake(player);
        }

        @Override
        public void putStack(ItemStack stack) {
            if (isClient()) display.setStackInSlot(index - 9, stack);
            else if (stack == null || stack.stackSize <= 0) takeChecked();
        }

        @Override
        public ItemStack decrStackSize(int amount) {
            ItemStack stack = getStack();
            if (amount <= 0 || stack == null) return null;
            if (isClient()) display.setStackInSlot(index - 9, null);
            else takeChecked();
            return stack;
        }

        @Override
        public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
            selected = null;
            extracted = false;
            if (!isClient()) container().completeTake();
        }
    }

    /** Roll back native cursor/Shift/hotbar edits if the subsequent input commit rejects the take. */
    public static final class CraftingContainer extends ModularContainer {

        private boolean clicking;
        private ItemStack[] inventoryBefore;
        private ItemStack cursorBefore;

        private boolean prepareTake(EntityPlayer player) {
            if (!clicking) return false;
            if (inventoryBefore == null) {
                inventoryBefore = new ItemStack[player.inventory.getSizeInventory()];
                for (int slot = 0; slot < inventoryBefore.length; slot++) {
                    // Native transfers mutate counts, not NBT; do not copy every ME disk in the player's inventory.
                    inventoryBefore[slot] = BottledChaosCrafting.displayStack(player.inventory.getStackInSlot(slot));
                }
                cursorBefore = BottledChaosCrafting.displayStack(player.inventory.getItemStack());
            }
            return true;
        }

        private void completeTake() {
            inventoryBefore = null;
            cursorBefore = null;
        }

        @Override
        public ItemStack slotClick(int slotId, int mouseButton, int mode, EntityPlayer player) {
            if (isClient()) return super.slotClick(slotId, mouseButton, mode, player);
            clicking = true;
            try {
                // An empty server result must also correct a client that still predicts a take.
                if (slotId >= 0 && slotId < inventorySlots.size()
                    && getSlot(slotId) instanceof ResultSlot slot
                    && (mode == 0 || mode == 1 || mode == 2 || mode == 4)
                    && slot.getStack() == null) {
                    throw new RejectedTake();
                }
                return super.slotClick(slotId, mouseButton, mode, player);
            } catch (RejectedTake rejected) {
                if (inventoryBefore != null) {
                    for (int slot = 0; slot < inventoryBefore.length; slot++) {
                        player.inventory.setInventorySlotContents(slot, inventoryBefore[slot]);
                    }
                    player.inventory.setItemStack(cursorBefore);
                    player.inventory.markDirty();
                }
                for (var slot : inventorySlots) {
                    if (slot instanceof ResultSlot result) {
                        result.extracted = false;
                        result.onPickupFromSlot(player, null);
                        result.getSyncHandler()
                            .detectAndSendChanges(true);
                    }
                }
                // Bypass delta-sync and isChangingQuantityOnly: even null/null click results need correction.
                if (player instanceof EntityPlayerMP serverPlayer) {
                    serverPlayer.sendContainerAndContentsToPlayer(this, getInventory());
                }
                return null;
            } finally {
                clicking = false;
                completeTake();
            }
        }

        @Override
        public ItemStack transferStackInSlot(EntityPlayer player, int index) {
            // MUI repeats Shift transfers without another canTakeStack call; validate each result independently.
            if (!isClient() && getSlot(index) instanceof ResultSlot slot) {
                if (slot.getStack() == null || inventoryBefore == null && !slot.canTakeStack(player)) return null;
            }
            return super.transferStackInSlot(player, index);
        }
    }

    private static final class RejectedTake extends RuntimeException {

        private RejectedTake() {
            super(null, null, false, false);
        }
    }
}
