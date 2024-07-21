package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.AppliedEnergistics.tile;

import appeng.api.networking.GridFlags;
import appeng.tile.grid.AENetworkTile;

public class Tile_ME_WirelessConnector extends AENetworkTile {

    public Tile_ME_WirelessConnector() {
        getProxy().setFlags(GridFlags.REQUIRE_CHANNEL);
    }

}
