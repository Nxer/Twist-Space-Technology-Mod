package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import java.util.ArrayList;
import java.util.List;

/** Client-side pool ordering: missing, in use, then provided but unused. */
final class BottledChaosModuleTree {

    static final int PAGE_SIZE = 6;
    private final List<Integer> rows = new ArrayList<>();
    private long visible;
    private long missing;
    private long used;
    private int page;
    private int selected = -1;

    void update(long visible, long missing, long used) {
        if (this.visible == visible && this.missing == missing && this.used == used) return;
        this.visible = visible;
        this.missing = missing;
        this.used = used;
        rows.clear();
        for (long section : new long[] { visible & missing, visible & ~missing & used, visible & ~(missing | used) })
            for (int pool = 0; pool < Long.SIZE; pool++) if ((section & (1L << pool)) != 0) rows.add(pool);
        int selectedIndex = rows.indexOf(selected);
        if (selectedIndex >= 0) page = selectedIndex / PAGE_SIZE;
        page = Math.min(page, pageCount() - 1);
        if (selectedIndex < 0) selected = row(0);
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
