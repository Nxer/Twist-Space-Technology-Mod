package com.Nxer.TwistSpaceTechnology.common.ship.test.ship.system;

import java.util.List;

import com.Nxer.TwistSpaceTechnology.common.ship.test.ship.component.ShieldComponent;

public class ShieldSystem {

    public List<ShieldComponent> shields;

    public ShieldSystem(List<ShieldComponent> shields) {
        this.shields = shields;
    }

    public ShieldComponent anyShieldOnline() {
        // If one shield or more are still alive, return that. If all shields are offline, return null.
        for (var i : shields) {
            if (i.shieldPoint > 0) return i;
        }
        return null;
    }

}
