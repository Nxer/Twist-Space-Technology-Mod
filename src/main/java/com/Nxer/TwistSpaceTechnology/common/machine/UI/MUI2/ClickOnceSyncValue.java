package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;

public class ClickOnceSyncValue extends BooleanSyncValue {

    public ClickOnceSyncValue(@NotNull Runnable onClick) {
        super(() -> false, b -> onClick.run());
    }
}
