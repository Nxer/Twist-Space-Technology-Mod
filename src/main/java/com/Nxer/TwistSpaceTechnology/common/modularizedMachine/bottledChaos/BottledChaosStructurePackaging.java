package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.CARRIER_SLOT;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.MATERIAL_SLOT_START;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.OUTPUT_SLOT;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.SECOND_OUTPUT_SLOT;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructureCatalog.Structure;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;

/** GUI-only asset transaction. All AE mutations occur on an isolated disk copy before committing slot changes. */
public final class BottledChaosStructurePackaging {

    private static final String PREFIX = "tst.bottledchaos.gui.structure_packaging.";

    // #tr tst.bottledchaos.gui.structure_packaging.success
    // # Structure transaction completed
    // #zh_CN 结构转移完成

    // #tr tst.bottledchaos.gui.structure_packaging.invalid_structure
    // # Insert one supported controller type and its complete structure
    // #zh_CN 需要一种受支持的主机及其完整结构

    // #tr tst.bottledchaos.gui.structure_packaging.missing_materials
    // # Insufficient materials for one complete structure
    // #zh_CN 材料不足以封装一套完整结构

    // #tr tst.bottledchaos.gui.structure_packaging.invalid_materials
    // # Every material must match the exact quantity per controller; extra or mixed materials are invalid
    // #zh_CN 每种材料必须符合单台主机的准确用量, 多余材料或混合配置无效

    // #tr tst.bottledchaos.gui.structure_packaging.incompatible_module
    // # Modules must contain the same machine and upgrades
    // #zh_CN 合并模组的机器及升级配置必须一致

    // #tr tst.bottledchaos.gui.structure_packaging.invalid_disk
    // # ME item disk is unavailable, or more than one disk was inserted
    // #zh_CN ME物品磁盘不可用, 或放入了多张磁盘

    // #tr tst.bottledchaos.gui.structure_packaging.disk_full
    // # Disk capacity or item-type limit is insufficient; nothing was transferred
    // #zh_CN 磁盘容量或物品类型槽不足, 本次转移未执行

    public static String execute(ItemStackHandler inventory, boolean unload) {
        return execute(inventory, unload, new ArrayList<>());
    }

    /** Run on a detached inventory to prepare all outputs, sharing a single AE deduction plan. */
    public static String execute(ItemStackHandler inventory, boolean unload, List<ItemStack> outputs) {
        ItemStack target = inventory.getStackInSlot(CARRIER_SLOT);
        if (!ItemBottledChaosMachineModule.isModule(target)) return PREFIX + "invalid_structure";
        if (inventory.getStackInSlot(OUTPUT_SLOT) != null || inventory.getStackInSlot(SECOND_OUTPUT_SLOT) != null)
            return "tst.bottledchaos.gui.credential_injection.output_occupied";

        int diskSlot = -1;
        for (int slot = MATERIAL_SLOT_START; slot < OUTPUT_SLOT; slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null && AEApi.instance()
                .registries()
                .cell()
                .isCellHandled(stack)) {
                if (diskSlot >= 0 || stack.stackSize != 1) return PREFIX + "invalid_disk";
                diskSlot = slot;
            }
        }
        ItemStack disk = diskSlot < 0 ? null
            : inventory.getStackInSlot(diskSlot)
                .copy();
        IMEInventoryHandler<IAEItemStack> cell = disk == null ? null
            : AEApi.instance()
                .registries()
                .cell()
                .getCellInventory(disk, null, StorageChannel.ITEMS);
        if (disk != null && cell == null) return PREFIX + "invalid_disk";

        // Disk contents belong to this transaction, never to the startup catalogue or a later click.
        IItemList<IAEItemStack> storedItems = cell == null ? null
            : cell.getAvailableItems(
                AEApi.instance()
                    .storage()
                    .createItemList());
        if (storedItems != null)
            for (IAEItemStack stored : storedItems) if (stored.getStackSize() < 0) return PREFIX + "invalid_materials";
        if (unload) return unload(inventory, target, diskSlot, disk, cell);
        Structure structure = ItemBottledChaosMachineModule.structure(target);
        long total = ItemBottledChaosMachineModule.quantity(target);
        if (total < 0) return PREFIX + "invalid_materials";
        if (total > 0 && structure == null) return PREFIX + "invalid_structure";
        Map<TST_ItemID, Long> raw = new LinkedHashMap<>();
        List<Structure> candidates = new ArrayList<>();
        boolean[] merge = new boolean[OUTPUT_SLOT];
        try {
            for (int slot = MATERIAL_SLOT_START; slot < OUTPUT_SLOT; slot++) {
                if (slot == diskSlot) continue;
                ItemStack source = inventory.getStackInSlot(slot);
                if (source == null) continue;
                if (ItemBottledChaosMachineModule.isModule(source)) {
                    Structure candidate = ItemBottledChaosMachineModule.structure(source);
                    if (candidate == null || structure != null && !structure.sameConfiguration(candidate))
                        return PREFIX + "incompatible_module";
                    structure = candidate;
                    long quantity = ItemBottledChaosMachineModule.quantity(source);
                    if (quantity <= 0) return PREFIX + "invalid_materials";
                    total = Math.addExact(total, quantity);
                    merge[slot] = true;
                } else {
                    if (source.stackSize <= 0) return PREFIX + "invalid_materials";
                    raw.merge(TST_ItemID.create(source), (long) source.stackSize, Math::addExact);
                }
            }
            if (storedItems != null) for (IAEItemStack stored : storedItems) {
                if (stored.getStackSize() <= 0) continue;
                raw.merge(TST_ItemID.create(stored.getItemStack()), stored.getStackSize(), Math::addExact);
            }
            // GUI stacks and disk entries are one input per type. Reject overflow before division or any extraction.
            for (long amount : raw.values()) if (amount <= 0) return PREFIX + "invalid_materials";
            ItemStack controller = null;
            long batches = 0;
            // Godforge is one complete host with multiple identical module controllers and paid upgrade items.
            for (var entry : raw.entrySet()) {
                ItemStack candidate = entry.getKey()
                    .getItemStack(1);
                if (BottledChaosComplexStructures.isGodforgeHost(candidate)) {
                    controller = candidate;
                    batches = entry.getValue();
                    break;
                }
            }
            for (var entry : raw.entrySet()) {
                if (BottledChaosComplexStructures.isGodforgeHost(controller)) break;
                ItemStack candidate = entry.getKey()
                    .getItemStack(1);
                if (!BottledChaosStructureCatalog.isController(candidate)) continue;
                if (controller != null) return PREFIX + "invalid_structure";
                controller = candidate;
                batches = entry.getValue();
            }
            if (!raw.isEmpty()) {
                if (controller == null) return PREFIX + "invalid_structure";
                ItemStack[] perSet = new ItemStack[raw.size()];
                int index = 0;
                for (var entry : raw.entrySet()) {
                    long perController = entry.getValue() / batches;
                    if (entry.getValue() % batches != 0 || perController <= 0 || perController > Integer.MAX_VALUE)
                        return PREFIX + "invalid_materials";
                    perSet[index++] = entry.getKey()
                        .getItemStack((int) perController);
                }
                candidates.addAll(BottledChaosStructureCatalog.matches(controller, perSet));
                if (candidates.isEmpty()) return PREFIX + "invalid_materials";
                if (structure != null) {
                    Structure existing = structure;
                    candidates.removeIf(candidate -> !existing.sameConfiguration(candidate));
                    if (candidates.isEmpty()) return PREFIX + "incompatible_module";
                }
                structure = candidates.get(0);
            }
            total = Math.addExact(total, batches);
            if (structure == null || total <= 0) return PREFIX + "missing_materials";
            if (candidates.isEmpty()) candidates.add(structure);
            ItemStack[] materials = structure.materials();
            BaseActionSource action = new BaseActionSource();
            int[] consumed = new int[OUTPUT_SLOT];
            for (int i = 0; i < materials.length; i++) {
                ItemStack material = materials[i];
                long needed = Math.multiplyExact(batches, material.stackSize);
                for (int slot = MATERIAL_SLOT_START; slot < OUTPUT_SLOT && needed > 0; slot++) {
                    ItemStack stack = inventory.getStackInSlot(slot);
                    if (slot == diskSlot || merge[slot] || !sameItem(stack, material)) continue;
                    int taken = (int) Math.min(needed, stack.stackSize - consumed[slot]);
                    consumed[slot] += taken;
                    needed -= taken;
                }
                if (needed > 0) {
                    if (cell == null) return PREFIX + "missing_materials";
                    IAEItemStack taken = cell.extractItems(toAE(material, needed), Actionable.MODULATE, action);
                    if (taken == null || taken.getStackSize() != needed) return PREFIX + "missing_materials";
                }
            }
            for (Structure candidate : candidates) outputs.add(ItemBottledChaosMachineModule.create(candidate, total));
            // Only this block mutates real inventory. Failures above discard the copied disk and deduction plan.
            inventory.setStackInSlot(CARRIER_SLOT, null);
            for (int slot = MATERIAL_SLOT_START; slot < OUTPUT_SLOT; slot++) {
                if (merge[slot] || slot == diskSlot) inventory.setStackInSlot(slot, null);
                else if (consumed[slot] > 0) inventory.extractItem(slot, consumed[slot], false);
            }
            inventory.setStackInSlot(OUTPUT_SLOT, outputs.get(0));
            inventory.setStackInSlot(SECOND_OUTPUT_SLOT, disk);
            return PREFIX + "success";
        } catch (ArithmeticException exception) {
            return PREFIX + "invalid_materials";
        }
    }

    private static String unload(ItemStackHandler inventory, ItemStack module, int diskSlot, ItemStack disk,
        IMEInventoryHandler<IAEItemStack> cell) {
        long quantity = ItemBottledChaosMachineModule.quantity(module);
        ItemStack[] materials = ItemBottledChaosMachineModule.materials(module);
        if (cell == null) return PREFIX + "invalid_disk";
        if (quantity <= 0 || materials.length == 0) return PREFIX + "invalid_structure";
        // Validate content amounts and products. The AE inventory owns capacity and item-type decisions.
        for (ItemStack material : materials) {
            if (material.stackSize <= 0 || quantity > Long.MAX_VALUE / material.stackSize)
                return PREFIX + "invalid_materials";
        }
        BaseActionSource action = new BaseActionSource();
        for (ItemStack material : materials) {
            long amount = quantity * material.stackSize;
            IAEItemStack simulated = cell.injectItems(toAE(material, amount), Actionable.SIMULATE, action);
            if (simulated != null && simulated.getStackSize() > 0) return PREFIX + "disk_full";
            IAEItemStack remainder = cell.injectItems(toAE(material, amount), Actionable.MODULATE, action);
            if (remainder != null && remainder.getStackSize() > 0) return PREFIX + "disk_full";
        }
        inventory.setStackInSlot(CARRIER_SLOT, null);
        inventory.setStackInSlot(diskSlot, null);
        inventory.setStackInSlot(OUTPUT_SLOT, new ItemStack(module.getItem(), 1, 0));
        inventory.setStackInSlot(SECOND_OUTPUT_SLOT, disk);
        return PREFIX + "success";
    }

    private static boolean sameItem(ItemStack first, ItemStack second) {
        return first != null && first.isItemEqual(second) && ItemStack.areItemStackTagsEqual(first, second);
    }

    /** AE owns its returned quantity; the sole local material representation remains the cached ItemStack. */
    public static IAEItemStack toAE(ItemStack material, long amount) {
        return AEItemStack.create(material)
            .setStackSize(amount);
    }
}
