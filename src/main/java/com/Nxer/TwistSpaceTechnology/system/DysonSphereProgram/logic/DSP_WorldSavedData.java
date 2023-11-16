package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DSP_TeamName;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DysonSpheres;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.UUID_Name;

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

public class DSP_WorldSavedData extends WorldSavedData {

    public static DSP_WorldSavedData INSTANCE;
    private static final String DATA_NAME = "TST_DysonSphereProgramWorldSavedData";
    private static final String NBTTag_DysonSpheres = "TST_NBTTag_DysonSpheres";
    private static final String NBTTag_DSP_TeamName = "TST_NBTTag_DSP_TeamName";
    private static final String NBTTag_UUID_Name = "TST_NBTTag_UUID_Name";

    private static void loadInstance(World world) {
        TwistSpaceTechnology.LOG.info("test loadInstance");
        DysonSpheres.clear();
        DSP_TeamName.clear();
        UUID_Name.clear();

        MapStorage storage = world.mapStorage;
        INSTANCE = (DSP_WorldSavedData) storage.loadData(DSP_WorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new DSP_WorldSavedData();
            storage.setData(DATA_NAME, INSTANCE);
        }
        INSTANCE.markDirty();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            loadInstance(event.world);
        }
    }

    public DSP_WorldSavedData() {
        super(DATA_NAME);
    }

    @SuppressWarnings("unused")
    public DSP_WorldSavedData(String name) {
        super(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

        TwistSpaceTechnology.LOG.info("test readFromNBT");

        try {
            byte[] ba = nbtTagCompound.getByteArray(NBTTag_UUID_Name);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            UUID_Name = (Map<String, String>) data;
            // objectInputStream.close();
            // byteArrayInputStream.close();
            TwistSpaceTechnology.LOG.info("test readFromNBT: NBTTag_UUID_Name Success!");
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(NBTTag_UUID_Name + " FAILED");
            exception.printStackTrace();
        }

        try {
            byte[] ba = nbtTagCompound.getByteArray(NBTTag_DSP_TeamName);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            DSP_TeamName = (Map<String, String>) data;
            // objectInputStream.close();
            // byteArrayInputStream.close();
            TwistSpaceTechnology.LOG.info("test readFromNBT: NBTTag_DSP_TeamName Success!");
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(NBTTag_DSP_TeamName + " FAILED");
            exception.printStackTrace();
        }

        try {
            byte[] ba = nbtTagCompound.getByteArray(NBTTag_DysonSpheres);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            DysonSpheres = (Map<String, HashMap<DSP_Galaxy, DSP_DataCell>>) data;
            // objectInputStream.close();
            // byteArrayInputStream.close();
            TwistSpaceTechnology.LOG.info("test readFromNBT: NBTTag_DysonSpheres Success!");
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(NBTTag_DysonSpheres + " FAILED");
            exception.printStackTrace();
        }

        // TODO testing
        TwistSpaceTechnology.LOG.info("test readFromNBT map");
        for (HashMap<DSP_Galaxy, DSP_DataCell> map : DysonSpheres.values()) {
            if (map == null) {
                TwistSpaceTechnology.LOG.info("test readFromNBT map == null");
            } else {
                TwistSpaceTechnology.LOG.info("test readFromNBT map is not null");
                for (DSP_DataCell dataCell : map.values()) {
                    if (dataCell != null) {
                        TwistSpaceTechnology.LOG.info("test readFromNBT map dataCell: " + dataCell);
                    } else {
                        TwistSpaceTechnology.LOG.info("test readFromNBT map dataCell: null");
                    }
                }
            }
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {

        TwistSpaceTechnology.LOG.info("test writeToNBT");

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(DysonSpheres);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(NBTTag_DysonSpheres, data);
            // objectOutputStream.close();
            // byteArrayOutputStream.close();
            TwistSpaceTechnology.LOG.info("test writeToNBT: NBTTag_DysonSpheres Success!");
        } catch (IOException exception) {
            System.out.println(NBTTag_DysonSpheres + " SAVE FAILED");
            exception.printStackTrace();
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(DSP_TeamName);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(NBTTag_DSP_TeamName, data);
            // objectOutputStream.close();
            // byteArrayOutputStream.close();
            TwistSpaceTechnology.LOG.info("test writeToNBT: NBTTag_DSP_TeamName Success!");
        } catch (IOException exception) {
            System.out.println(NBTTag_DSP_TeamName + " SAVE FAILED");
            exception.printStackTrace();
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(UUID_Name);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(NBTTag_UUID_Name, data);
            // objectOutputStream.close();
            // byteArrayOutputStream.close();
            TwistSpaceTechnology.LOG.info("test writeToNBT: NBTTag_UUID_Name Success!");
        } catch (IOException exception) {
            System.out.println(NBTTag_UUID_Name + " SAVE FAILED");
            exception.printStackTrace();
        }

    }
}
