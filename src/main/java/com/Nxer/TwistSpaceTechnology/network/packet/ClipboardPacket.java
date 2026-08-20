package com.Nxer.TwistSpaceTechnology.network.packet;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ClipboardPacket implements IMessage, IMessageHandler<ClipboardPacket, IMessage> {

    private String text;

    public ClipboardPacket() {}

    public ClipboardPacket(String text) {
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, text);
    }

    @Override
    public IMessage onMessage(ClipboardPacket message, MessageContext ctx) {
        // Clipboard access is delegated to ClientProxy to keep client classes off dedicated servers.
        TwistSpaceTechnology.proxy.copyToClipboard(message.text);
        return null;
    }
}
