package com.Nxer.TwistSpaceTechnology.network.packet;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ContainerDumpRequestPacket implements IMessage, IMessageHandler<ContainerDumpRequestPacket, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(ContainerDumpRequestPacket message, MessageContext ctx) {
        // Ask the client for the block currently selected by its rendered crosshair.
        TwistSpaceTechnology.proxy.sendContainerDumpTarget();
        return null;
    }
}
