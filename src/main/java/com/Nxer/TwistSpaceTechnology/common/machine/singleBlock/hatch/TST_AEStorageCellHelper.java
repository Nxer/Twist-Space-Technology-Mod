package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import appeng.api.AEApi;
import appeng.api.networking.IGrid;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;

public final class TST_AEStorageCellHelper {

    static final int MAX_TYPES = 16;

    /** Selects the amount exposed to recipes and reported by the GUI and Waila. */
    enum PullMode {
        GT_SINGLE_STACK,
        TST_SEGMENTED,
        LONG
    }

    private TST_AEStorageCellHelper() {}

    @Nullable
    public static IGrid getNetwork(AENetworkProxy proxy) {
        if (!proxy.isActive()) return null;
        try {
            return proxy.getGrid();
        } catch (GridAccessException ignored) {
            return null;
        }
    }

    /** Removes shared ME snapshots while retaining GT's selection from other sources. */
    static final class MEInputFilter<K, T> {

        private final Function<T, K> typeOf;
        private final Map<K, T> otherInputs = new HashMap<>();
        private final Set<T> overlappingInputs = Collections.newSetFromMap(new IdentityHashMap<>());

        MEInputFilter(Function<T, K> typeOf) {
            this.typeOf = typeOf;
        }

        void add(T input, boolean overlaps) {
            if (input == null) return;
            if (overlaps) overlappingInputs.add(input);
            else otherInputs.put(typeOf.apply(input), input);
        }

        void applyTo(List<T> inputs) {
            for (ListIterator<T> iterator = inputs.listIterator(); iterator.hasNext();) {
                T input = iterator.next();
                if (!overlappingInputs.contains(input)) continue;
                T replacement = otherInputs.get(typeOf.apply(input));
                if (replacement == null) iterator.remove();
                else iterator.set(replacement);
            }
        }
    }

    /** Restores segments while keeping one supplying hatch per source and resource type. */
    public static final class SegmentedInputs<K, T> {

        private final Function<T, K> typeOf;
        private final Map<Object, Map<K, List<T>>> sources = new IdentityHashMap<>();
        private final List<Object> sourceOrder = new ArrayList<>();
        private final Set<T> replacedInputs = Collections.newSetFromMap(new IdentityHashMap<>());

        public SegmentedInputs(Function<T, K> typeOf) {
            this.typeOf = typeOf;
        }

        public void add(Object source, List<T> segments) {
            Map<K, List<T>> types = new LinkedHashMap<>();
            for (T segment : segments) {
                if (segment == null) continue;
                types.computeIfAbsent(typeOf.apply(segment), ignored -> new ArrayList<>())
                    .add(segment);
                replacedInputs.add(segment);
            }
            if (types.isEmpty()) return;
            if (!sources.containsKey(source)) {
                sources.put(source, new LinkedHashMap<>());
                sourceOrder.add(source);
            }
            // Like GT, the last hatch wins for a shared type, but all of its segments stay together.
            sources.get(source)
                .putAll(types);
        }

        public boolean isEmpty() {
            return sources.isEmpty();
        }

        public void removeOverlap(Object source, T input) {
            Map<K, List<T>> types = sources.get(source);
            if (input != null && types != null && types.containsKey(typeOf.apply(input))) replacedInputs.add(input);
        }

        public void appendTo(List<T> inputs) {
            inputs.removeIf(replacedInputs::contains);
            for (Object source : sourceOrder) {
                for (List<T> segments : sources.get(source)
                    .values()) inputs.addAll(segments);
            }
        }
    }

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
