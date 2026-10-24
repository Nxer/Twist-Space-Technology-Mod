package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.result;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

/** Shared result payload for GUI summaries, Waila and nested pool failures. Translation stays on the viewing side. */
public final class BottledChaosResultData {

    public static void write(NBTTagCompound tag, CheckRecipeResult result) {
        writePayload(tag, result.getID(), result.writeToNBT(new NBTTagCompound()));
    }

    private static void writePayload(NBTTagCompound tag, String id, NBTTagCompound data) {
        tag.setString("resultId", id);
        tag.setTag("result", data);
    }

    public static boolean hasResult(NBTTagCompound tag) {
        return tag.hasKey("resultId", 8);
    }

    /** Read a payload written by write(); callers distinguish text-only entries with hasResult(). */
    public static CheckRecipeResult read(NBTTagCompound tag) {
        CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(tag.getString("resultId"))
            .newInstance();
        result.readFromNBT(tag.getCompoundTag("result"));
        return result;
    }

    public static String displayText(NBTTagCompound tag, String textKeyField) {
        return hasResult(tag) ? read(tag).getDisplayString()
            : StatCollector.translateToLocal(tag.getString(textKeyField));
    }

    /** Merge only identical result payloads; preserve entry order and all affected processing-group bits. */
    public static void addGrouped(NBTTagList entries, CheckRecipeResult result, int slots) {
        String id = result.getID();
        NBTTagCompound data = result.writeToNBT(new NBTTagCompound());
        for (int i = 0; i < entries.tagCount(); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            if (id.equals(entry.getString("resultId")) && data.equals(entry.getCompoundTag("result"))) {
                entry.setInteger("slots", entry.getInteger("slots") | slots);
                return;
            }
        }
        NBTTagCompound entry = new NBTTagCompound();
        writePayload(entry, id, data);
        entry.setInteger("slots", slots);
        entries.appendTag(entry);
    }
}
