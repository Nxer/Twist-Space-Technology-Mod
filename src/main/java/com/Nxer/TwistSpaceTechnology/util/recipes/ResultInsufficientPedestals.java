package com.Nxer.TwistSpaceTechnology.util.recipes;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.recipe.check.CheckRecipeResult;

public class ResultInsufficientPedestals implements CheckRecipeResult {

    private int missingCount;

    public ResultInsufficientPedestals(int missingCount) {
        this.missingCount = missingCount;
    }

    @Override
    @Nonnull
    public String getID() {
        return "insufficient_pedestals";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    @Nonnull
    public String getDisplayString() {
        return StatCollector.translateToLocalFormatted("GT5U.gui.text.insufficient_pedestals", missingCount);
    }

    @Override
    public NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setInteger("missing", missingCount);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        missingCount = tag.getInteger("missing");
    }

    @Override
    public void encode(@Nonnull PacketBuffer buffer) {
        buffer.writeInt(missingCount);
    }

    @Override
    public void decode(@Nonnull PacketBuffer buffer) {
        missingCount = buffer.readInt();
    }

    @Override
    @Nonnull
    public CheckRecipeResult newInstance() {
        return new ResultInsufficientPedestals(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultInsufficientPedestals that = (ResultInsufficientPedestals) o;
        return missingCount == that.missingCount;
    }
}
