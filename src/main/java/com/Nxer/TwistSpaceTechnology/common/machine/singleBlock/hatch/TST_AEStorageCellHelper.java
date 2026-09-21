package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import appeng.api.AEApi;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEStack;

final class TST_AEStorageCellHelper {

    static final int MAX_TYPES = 16;

    /** Selects the amount that the GUI and Waila report as pullable. */
    enum PullMode {
        GT_SINGLE_STACK,
        TST_SEGMENTED,
        LONG
    }

    private TST_AEStorageCellHelper() {}

    @Nullable
    @SuppressWarnings("unchecked")
    static <T extends IAEStack<T>> IMEInventoryHandler<T> getCellInventory(ItemStack cell, ISaveProvider saveProvider,
        StorageChannel channel) {
        if (cell == null || cell.stackSize != 1) return null;
        return (IMEInventoryHandler<T>) AEApi.instance()
            .registries()
            .cell()
            .getCellInventory(cell, saveProvider, channel);
    }

    static boolean isCellForChannel(ItemStack cell, StorageChannel channel) {
        if (cell == null) return false;
        ItemStack singleCell = cell.copy();
        singleCell.stackSize = 1;
        return getCellInventory(singleCell, null, channel) != null;
    }

    static long limitPullableAmount(long availableAmount, int allocatedSegments, PullMode pullMode) {
        if (availableAmount <= 0) return 0;
        if (pullMode == PullMode.LONG) return availableAmount;
        long limit = pullMode == PullMode.TST_SEGMENTED ? (long) Integer.MAX_VALUE * allocatedSegments
            : Integer.MAX_VALUE;
        return Math.min(availableAmount, limit);
    }

    static int[] distributeIntSegments(long[] amounts, int totalSegmentLimit) {
        int[] allocations = new int[amounts.length];
        long[] requiredSegments = new long[amounts.length];
        int activeTypes = 0;

        for (int i = 0; i < amounts.length; i++) {
            if (amounts[i] <= 0) continue;
            requiredSegments[i] = (amounts[i] - 1) / Integer.MAX_VALUE + 1;
            activeTypes++;
        }

        int remainingSegments = Math.max(0, totalSegmentLimit);
        // Share the limit across all types, then give out any unused segments again.
        while (activeTypes > 0 && remainingSegments > 0) {
            int segmentsPerType = remainingSegments / activeTypes;
            int typesWithExtraSegment = remainingSegments % activeTypes;

            if (segmentsPerType == 0) {
                for (int i = 0; i < amounts.length && remainingSegments > 0; i++) {
                    if (allocations[i] >= requiredSegments[i]) continue;
                    allocations[i]++;
                    remainingSegments--;
                }
                break;
            }

            int activeIndex = 0;
            for (int i = 0; i < amounts.length; i++) {
                long segmentsNeeded = requiredSegments[i] - allocations[i];
                if (segmentsNeeded <= 0) continue;

                int offeredSegments = segmentsPerType + (activeIndex++ < typesWithExtraSegment ? 1 : 0);
                int grantedSegments = (int) Math.min(segmentsNeeded, offeredSegments);
                allocations[i] += grantedSegments;
                remainingSegments -= grantedSegments;
                if (grantedSegments == segmentsNeeded) activeTypes--;
            }
        }
        return allocations;
    }
}
