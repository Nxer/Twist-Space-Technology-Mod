package com.Nxer.TwistSpaceTechnology.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class TST_Network {

    public static SimpleNetworkWrapper tst = NetworkRegistry.INSTANCE.newSimpleChannel("TST");
}
