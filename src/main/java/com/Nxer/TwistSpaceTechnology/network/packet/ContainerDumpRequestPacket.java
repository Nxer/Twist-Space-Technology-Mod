package com.Nxer.TwistSpaceTechnology.network.packet;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.command.ContainerDumpMode;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ContainerDumpRequestPacket implements IMessage, IMessageHandler<ContainerDumpRequestPacket, IMessage> {

    private ContainerDumpMode mode = ContainerDumpMode.GET_MOD_ITEM;

    public ContainerDumpRequestPacket() {}

    public ContainerDumpRequestPacket(ContainerDumpMode mode) {
        this.mode = mode == null ? ContainerDumpMode.GET_MOD_ITEM : mode;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ContainerDumpMode parsed = ContainerDumpMode.parse(ByteBufUtils.readUTF8String(buf));
        mode = parsed == null ? ContainerDumpMode.GET_MOD_ITEM : parsed;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // The target is resolved on the client; carry the mode along.
        ByteBufUtils.writeUTF8String(buf, mode.name());
    }

    @Override
    public IMessage onMessage(ContainerDumpRequestPacket message, MessageContext ctx) {
        // Ask the client for the block under the crosshair.
        // FML creates a new handler per message, so read the mode from the message.
        TwistSpaceTechnology.proxy.sendContainerDumpTarget(message.mode);
        return null;
    }
}
