package com.Nxer.TwistSpaceTechnology.event;

import net.minecraft.entity.player.EntityPlayerMP;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.Nxer.TwistSpaceTechnology.network.packet.ServerJoinedPacket;

import bartworks.API.SideReference;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class ServerEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void PlayerJoinServerEvent(PlayerEvent.PlayerLoggedInEvent event) {
        TwistSpaceTechnology.LOG.info("PlayerJoinServerEvent running.");
        if (event == null || !(event.player instanceof EntityPlayerMP player) || !SideReference.Side.Server) return;

        TST_Network.tst.sendTo(new ServerJoinedPacket(), player);

        TwistSpaceTechnology.LOG.info("PlayerJoinServerEvent run finish.");

    }
}
