package com.Nxer.TwistSpaceTechnology.network.packet;

import net.minecraft.entity.player.EntityPlayerMP;

import com.Nxer.TwistSpaceTechnology.command.ContainerDumpMode;
import com.Nxer.TwistSpaceTechnology.command.TST_CommandMethods;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ContainerDumpTargetPacket implements IMessage, IMessageHandler<ContainerDumpTargetPacket, IMessage> {

    private int x;
    private int y;
    private int z;
    private ContainerDumpMode mode = ContainerDumpMode.GET_MOD_ITEM;

    public ContainerDumpTargetPacket() {}

    public ContainerDumpTargetPacket(int x, int y, int z) {
        this(x, y, z, ContainerDumpMode.GET_MOD_ITEM);
    }

    public ContainerDumpTargetPacket(int x, int y, int z, ContainerDumpMode mode) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.mode = mode == null ? ContainerDumpMode.GET_MOD_ITEM : mode;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        ContainerDumpMode parsed = ContainerDumpMode.parse(ByteBufUtils.readUTF8String(buf));
        mode = parsed == null ? ContainerDumpMode.GET_MOD_ITEM : parsed;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        // Carry the mode back with the crosshair target.
        ByteBufUtils.writeUTF8String(buf, mode.name());
    }

    @Override
    public IMessage onMessage(ContainerDumpTargetPacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        TST_CommandMethods.INSTANCE.dumpContainer(player, message.x, message.y, message.z, message.mode);
        return null;
    }
}
