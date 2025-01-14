package com.Nxer.TwistSpaceTechnology.network;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_BigBroArray;
import com.Nxer.TwistSpaceTechnology.network.packet.ServerJoinedPacket;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class TST_Network {

    public static SimpleNetworkWrapper tst = NetworkRegistry.INSTANCE.newSimpleChannel("TST");

    public static void registryNetwork() {
        tst.registerMessage(
            TST_BigBroArray.PackRequestMachineType.class,
            TST_BigBroArray.PackRequestMachineType.class,
            0,
            Side.SERVER);
        tst.registerMessage(
            TST_BigBroArray.PackSyncMachineType.class,
            TST_BigBroArray.PackSyncMachineType.class,
            1,
            Side.CLIENT);
        tst.registerMessage(ServerJoinedPacket.class, ServerJoinedPacket.class, 2, Side.SERVER);
        tst.registerMessage(ServerJoinedPacket.class, ServerJoinedPacket.class, 3, Side.CLIENT);
    }

}
