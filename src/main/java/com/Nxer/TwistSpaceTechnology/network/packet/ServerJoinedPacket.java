package com.Nxer.TwistSpaceTechnology.network.packet;

import com.Nxer.TwistSpaceTechnology.loader.ServerInitLoader;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ServerJoinedPacket implements IMessage, IMessageHandler<ServerJoinedPacket, ServerJoinedPacket> {

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public ServerJoinedPacket onMessage(ServerJoinedPacket message, MessageContext ctx) {
        ServerInitLoader.initOnPlayerJoinedServer();
        return null;
    }
}
