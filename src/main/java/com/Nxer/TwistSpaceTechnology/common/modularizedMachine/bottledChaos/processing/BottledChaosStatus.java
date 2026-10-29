package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumChatFormatting;

/** Shared status presentation. Detailed recipe failures retain their own result payloads. */
public enum BottledChaosStatus {

    // spotless:off
    ACTIVE("GT5U.gui.text.status.active", EnumChatFormatting.GREEN, 0xFF55DD77),
    IDLE("GT5U.waila.machine.idle", EnumChatFormatting.AQUA, 0xFF39DCEC),
    // #tr tst.bottledchaos.paused
    // # Paused
    // #zh_CN 已暂停
    PAUSED("tst.bottledchaos.paused", EnumChatFormatting.YELLOW, 0xFFFFD75F),
    // #tr tst.bottledchaos.waiting_power
    // # Waiting
    // #zh_CN 等待
    WAITING_POWER("tst.bottledchaos.waiting_power", EnumChatFormatting.YELLOW, 0xFFFFD75F),
    CAPACITY_EXCEEDED("tst.bottledchaos.capacity_exceeded", EnumChatFormatting.RED, 0xFFFF6262),
    UNBOUND("tst.bottledchaos.unbound", EnumChatFormatting.RED, 0xFFFF6262),
    UNINSTALLED("tst.bottledchaos.gui.module_missing", EnumChatFormatting.DARK_RED, 0xFF52616A),
    FAILURE(null, EnumChatFormatting.RED, 0xFFFF6262);
    // spotless:on

    private static final Map<String, BottledChaosStatus> BY_KEY = new HashMap<>();

    static {
        for (BottledChaosStatus status : values()) if (status.key != null) BY_KEY.put(status.key, status);
    }

    private final String key;
    private final EnumChatFormatting textColor;
    private final int indicatorColor;

    BottledChaosStatus(String key, EnumChatFormatting textColor, int indicatorColor) {
        this.key = key;
        this.textColor = textColor;
        this.indicatorColor = indicatorColor;
    }

    public String key() {
        return key;
    }

    public EnumChatFormatting textColor() {
        return textColor;
    }

    public int indicatorColor() {
        return indicatorColor;
    }

    public static BottledChaosStatus fromKey(String key) {
        return BY_KEY.getOrDefault(key, FAILURE);
    }
}
