package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import java.util.ArrayList;
import java.util.List;

/** Client-side ordering and pagination. Expanded children occupy ordinary rows, never sticky headers. */
final class BottledChaosModuleTree {

    static final int PAGE_SIZE = 6;
    private final int[] parents;
    private final int[] preferredRoots;
    private final List<Integer> rows = new ArrayList<>();
    private long visible;
    private long grouped;
    private long expanded;
    private int page;
    private int selected = -1;

    BottledChaosModuleTree(int[] parents, int[] preferredRoots) {
        this.parents = parents.clone();
        this.preferredRoots = preferredRoots.clone();
    }

    void update(long visible, long grouped) {
        if (this.visible == visible && this.grouped == grouped) return;
        this.visible = visible;
        this.grouped = grouped;
        rebuild();
    }

    private void rebuild() {
        rows.clear();
        long roots = visible;
        for (int slot = 0; slot < parents.length; slot++) if (isChild(slot)) {
            roots &= ~(1L << slot);
            roots |= 1L << parents[slot];
        }
        for (int i = 0; i < preferredRoots.length; i++) {
            int root = preferredRoots[i];
            if (root >= 0 && (roots & (1L << root)) != 0 && (i == 0 || hasChildren(root))) {
                append(root);
                roots &= ~(1L << root);
            }
        }
        for (int slot = 0; slot < parents.length; slot++) if ((roots & (1L << slot)) != 0) append(slot);
        int selectedIndex = rows.indexOf(selected);
        if (selectedIndex >= 0) page = selectedIndex / PAGE_SIZE;
        page = Math.min(page, pageCount() - 1);
        if (selectedIndex < 0) selected = row(0);
    }

    private void append(int slot) {
        rows.add(slot);
        if (!isExpanded(slot)) return;
        for (int child = 0; child < parents.length; child++)
            if (parents[child] == slot && isChild(child)) rows.add(child);
    }

    boolean isChild(int slot) {
        return slot >= 0 && parents[slot] >= 0 && ((visible & grouped) & (1L << slot)) != 0;
    }

    boolean hasChildren(int slot) {
        if (slot < 0) return false;
        for (int child = 0; child < parents.length; child++) if (parents[child] == slot && isChild(child)) return true;
        return false;
    }

    boolean isExpanded(int slot) {
        return slot >= 0 && (expanded & (1L << slot)) != 0;
    }

    void toggle(int slot) {
        if (!hasChildren(slot)) return;
        selected = slot;
        expanded ^= 1L << slot;
        rebuild();
    }

    void select(int slot) {
        if (rows.indexOf(slot) / PAGE_SIZE == page && rows.contains(slot)) selected = slot;
    }

    void turnPage(int direction) {
        page = Math.max(0, Math.min(pageCount() - 1, page + direction));
        selected = row(0);
    }

    int row(int index) {
        int offset = page * PAGE_SIZE + index;
        return offset < rows.size() ? rows.get(offset) : -1;
    }

    int selected() {
        return selected;
    }

    int page() {
        return page;
    }

    int pageCount() {
        return Math.max(1, (rows.size() + PAGE_SIZE - 1) / PAGE_SIZE);
    }
}
