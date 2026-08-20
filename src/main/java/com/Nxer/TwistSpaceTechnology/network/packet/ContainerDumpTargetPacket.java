package com.Nxer.TwistSpaceTechnology.network.packet;

import net.minecraft.entity.player.EntityPlayerMP;

import com.Nxer.TwistSpaceTechnology.command.TST_CommandMethods;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ContainerDumpTargetPacket implements IMessage, IMessageHandler<ContainerDumpTargetPacket, IMessage> {

    private int x;
    private int y;
    private int z;

    public ContainerDumpTargetPacket() {}

    public ContainerDumpTargetPacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public IMessage onMessage(ContainerDumpTargetPacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        TST_CommandMethods.INSTANCE.dumpContainer(player, message.x, message.y, message.z);
        return null;
    }
}
