package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.result;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineConfiguration;

import gregtech.api.recipe.check.CheckRecipeResult;

/** Sync the missing types, so the receiving client can translate their names. */
public final class BottledChaosMissingCategoriesResult implements CheckRecipeResult {

    private long missing;
    private boolean running;

    public BottledChaosMissingCategoriesResult(long missing) {
        this(missing, false);
    }

    public BottledChaosMissingCategoriesResult(long missing, boolean running) {
        this.missing = missing;
        this.running = running;
    }

    @Override
    public @NotNull String getID() {
        return "bottled_chaos_missing_categories";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    public @NotNull String getDisplayString() {
        long displayMissing = 0;
        for (int slot = 0; slot < BottledChaosMachineConfiguration.poolCount(); slot++) {
            long bit = 1L << slot;
            if ((missing & bit) == 0) continue;
            displayMissing |= 1L << (running ? slot : BottledChaosMachineConfiguration.displayPoolSlot(slot));
        }
        Set<String> names = new LinkedHashSet<>();
        for (int slot = 0; slot < BottledChaosMachineConfiguration.poolCount(); slot++) {
            if ((displayMissing & (1L << slot)) != 0) names.add(
                running ? BottledChaosMachineConfiguration.sourceName(slot)
                    : BottledChaosMachineConfiguration.poolName(slot));
        }
        // #tr tst.bottledchaos.missing_recipe_types
        // # Missing machines for recipe pools: %s
        // #zh_CN 缺少配方池对应的机器: %s
        return StatCollector
            .translateToLocalFormatted("tst.bottledchaos.missing_recipe_types", String.join(", ", names));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setLong("missing", missing);
        tag.setBoolean("running", running);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        missing = tag.getLong("missing");
        running = tag.getBoolean("running");
    }

    @Override
    public @NotNull CheckRecipeResult newInstance() {
        return new BottledChaosMissingCategoriesResult(0);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeLong(missing);
        buffer.writeBoolean(running);
    }

    @Override
    public void decode(@NotNull PacketBuffer buffer) {
        missing = buffer.readLong();
        running = buffer.readBoolean();
    }

    @Override
    public boolean persistsOnShutdown() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof BottledChaosMissingCategoriesResult result && result.missing == missing
            && result.running == running;
    }

    @Override
    public int hashCode() {
        return 31 * Long.hashCode(missing) + Boolean.hashCode(running);
    }
}
