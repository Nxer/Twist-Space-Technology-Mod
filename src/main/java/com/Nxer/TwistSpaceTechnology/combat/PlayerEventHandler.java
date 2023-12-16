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
            IExtendedEntityProperties basicstats = event.entity.getExtendedProperties("BASIC_COMBAT_STATS");
            IExtendedEntityProperties stats = event.entity.getExtendedProperties("COMBAT_STATS");
            if (stats == null) {
                stats = new PlayerExtendedProperties();
                event.entity.registerExtendedProperties("COMBAT_STATS", stats);
            }
            if (basicstats == null) {
                basicstats = new BasicPlayerExtendedProperties();
                event.entity.registerExtendedProperties("BASIC_COMBAT_STATS", basicstats);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        NBTTagCompound data = new NBTTagCompound();
        PlayerExtendedProperties.from(event.original)
            .saveNBTData(data);
        BasicPlayerExtendedProperties.from(event.original)
            .saveNBTData(data);
        PlayerExtendedProperties.from(event.entityPlayer)
            .loadNBTData(data);
        BasicPlayerExtendedProperties.from(event.entityPlayer)
            .loadNBTData(data);
    }
}
