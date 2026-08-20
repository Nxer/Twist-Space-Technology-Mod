package com.Nxer.TwistSpaceTechnology.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * World-saved per-player toggles for the AE2 pattern conversion (see
 * {@link ItemEssentiaHelper#convertPatternNBT}). Stored in the world save like
 * {@code DSP_WorldSavedData}, not in the config file.
 * <p>
 * Defaults are fixed in code: glass ampoules convert by default, crystal
 * essences do not. Each player overrides their own state via
 * {@code /tst AE_pattern_conversion <status|ampoule <on|off>|crystal <on|off>>}.
 */
public class PatternConversionWorldSavedData extends WorldSavedData {

    public static PatternConversionWorldSavedData INSTANCE;
    private static final String DATA_NAME = "TST_PatternConversionWorldSavedData";
    private static final String NBTTag_AmpouleOverrides = "TST_NBTTag_AmpouleOverrides";
    private static final String NBTTag_CrystalOverrides = "TST_NBTTag_CrystalOverrides";

    private static final boolean DEFAULT_AMPOULE_CONVERSION = true;
    private static final boolean DEFAULT_CRYSTAL_CONVERSION = false;
    private static final Map<String, Boolean> PatternConversionOverrides = new HashMap<>();
    private static final Map<String, Boolean> CrystalConversionOverrides = new HashMap<>();

    private static void loadInstance(World world) {
        TwistSpaceTechnology.LOG.info("TST PatternConversionWorldSavedData loadInstance");
        PatternConversionOverrides.clear();
        CrystalConversionOverrides.clear();

        MapStorage storage = world.mapStorage;
        INSTANCE = (PatternConversionWorldSavedData) storage.loadData(PatternConversionWorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new PatternConversionWorldSavedData();
            storage.setData(DATA_NAME, INSTANCE);
        }
        INSTANCE.markDirty();
    }

    public static void markDataDirty() {
        try {
            INSTANCE.markDirty();
        } catch (Exception e) {
            TwistSpaceTechnology.LOG.info("Failed to mark World Save Data dirty.");
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            loadInstance(event.world);
        }
    }

    public PatternConversionWorldSavedData() {
        super(DATA_NAME);
    }

    @SuppressWarnings("unused")
    public PatternConversionWorldSavedData(String name) {
        super(name);
    }

    // Get/set per-player pattern conversion state. Falls back to the code default when no override.
    public static boolean isPatternConversionEnabledFor(String playerName) {
        Boolean override = PatternConversionOverrides.get(playerName);
        return override != null ? override : DEFAULT_AMPOULE_CONVERSION;
    }

    public static void setPatternConversionEnabled(String playerName, boolean enabled) {
        if (enabled == DEFAULT_AMPOULE_CONVERSION) {
            PatternConversionOverrides.remove(playerName);
        } else {
            PatternConversionOverrides.put(playerName, enabled);
        }
        markDataDirty();
    }

    public static boolean isCrystalConversionEnabledFor(String playerName) {
        Boolean override = CrystalConversionOverrides.get(playerName);
        return override != null ? override : DEFAULT_CRYSTAL_CONVERSION;
    }

    public static void setCrystalConversionEnabled(String playerName, boolean enabled) {
        if (enabled == DEFAULT_CRYSTAL_CONVERSION) {
            CrystalConversionOverrides.remove(playerName);
        } else {
            CrystalConversionOverrides.put(playerName, enabled);
        }
        markDataDirty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        try {
            byte[] ba = nbtTagCompound.getByteArray(NBTTag_AmpouleOverrides);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            PatternConversionOverrides.clear();
            PatternConversionOverrides.putAll((Map<String, Boolean>) data);
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(NBTTag_AmpouleOverrides + " FAILED");
            exception.printStackTrace();
        }

        try {
            byte[] ba = nbtTagCompound.getByteArray(NBTTag_CrystalOverrides);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            CrystalConversionOverrides.clear();
            CrystalConversionOverrides.putAll((Map<String, Boolean>) data);
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(NBTTag_CrystalOverrides + " FAILED");
            exception.printStackTrace();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(PatternConversionOverrides);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(NBTTag_AmpouleOverrides, data);
        } catch (IOException exception) {
            System.out.println(NBTTag_AmpouleOverrides + " SAVE FAILED");
            exception.printStackTrace();
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(CrystalConversionOverrides);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(NBTTag_CrystalOverrides, data);
        } catch (IOException exception) {
            System.out.println(NBTTag_CrystalOverrides + " SAVE FAILED");
            exception.printStackTrace();
        }
    }
}
