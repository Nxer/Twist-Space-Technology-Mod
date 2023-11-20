package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic_t;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class DysonSphereManager extends WorldSavedData {

    private static final String DATA_NAME = "TST_DysonSphereProgramWorldSavedData";
    private static final String NBTTag_DysonSpheres = "TST_NBTTag_DysonSpheres";
    private static final String NBTTag_DSP_TeamName = "TST_NBTTag_DSP_TeamName";
    public static DysonSphereManager instance = new DysonSphereManager();

    public DysonSphereManager() {
        super(DATA_NAME);
    }

    public DysonSphereManager(String name) {
        super(name);
    }

    public static void dirty() {
        instance.markDirty();
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            MapStorage storage = event.world.mapStorage;
            instance = (DysonSphereManager) storage.loadData(DysonSphereManager.class, DATA_NAME);
            if (instance == null) {
                instance = new DysonSphereManager();
                storage.setData(DATA_NAME, instance);
                instance.markDirty();
            }
        }
    }

    @SubscribeEvent
    public static void onUpdate(TickEvent.WorldTickEvent event) {
        if (!(event.world instanceof WorldServer) || event.phase == TickEvent.Phase.END) return;
        Galaxy.foreach(DysonSphereSystem::doUpdate);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        NBTTagList list = compound.getTagList(NBTTag_DysonSpheres, Constants.NBT.TAG_LIST);
        if (list.tagCount() != 0) Galaxy.readFromNBT(list);

        TeamManager.clear();
        TeamManager.readFromNBT(compound.getCompoundTag(NBTTag_DSP_TeamName));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setTag(NBTTag_DysonSpheres, Galaxy.writeToNBT());
        compound.setTag(NBTTag_DSP_TeamName, TeamManager.writeToNBT());
    }
}
