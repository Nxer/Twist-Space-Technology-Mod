package com.Nxer.TwistSpaceTechnology.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerEventHandler {

    public static PlayerEventHandler instance;

    @SubscribeEvent
    public void onPlayerConstruction(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            IExtendedEntityProperties stats = event.entity.getExtendedProperties("COMBAT_STATS");
            if (stats == null) {
                stats = new PlayerExtendedProperties((EntityPlayer) event.entity);
                event.entity.registerExtendedProperties("COMBAT_STATS", stats);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        NBTTagCompound data = new NBTTagCompound();
        event.original.getExtendedProperties("COMBAT_STATS")
            .saveNBTData(data);
        event.entityPlayer.getExtendedProperties("COMBAT_STATS")
            .loadNBTData(data);
    }
}
