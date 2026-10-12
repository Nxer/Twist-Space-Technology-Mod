package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.StringJoiner;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.recipe.check.CheckRecipeResult;

/** Sync the missing types, so the receiving client can translate their names. */
public final class BottledChaosMissingCategoriesResult implements CheckRecipeResult {

    private long missing;

    public BottledChaosMissingCategoriesResult(long missing) {
        this.missing = missing;
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
        StringJoiner names = new StringJoiner(", ");
        for (int slot = 0; slot < BottledChaosMachineConfiguration.poolCount(); slot++) {
            if ((missing & (1L << slot)) != 0) names.add(BottledChaosMachineConfiguration.poolName(slot));
        }
        // #tr tst.modular.machine.BottledChaos.missing_recipe_types
        // # Missing machines for recipe pools: %s
        // #zh_CN 缺少配方池对应的机器: %s
        return StatCollector
            .translateToLocalFormatted("tst.modular.machine.BottledChaos.missing_recipe_types", names.toString());
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setLong("missing", missing);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        missing = tag.getLong("missing");
    }

    @Override
    public @NotNull CheckRecipeResult newInstance() {
        return new BottledChaosMissingCategoriesResult(0);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeLong(missing);
    }

    @Override
    public void decode(@NotNull PacketBuffer buffer) {
        missing = buffer.readLong();
    }

    @Override
    public boolean persistsOnShutdown() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof BottledChaosMissingCategoriesResult result && result.missing == missing;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(missing);
    }
}
