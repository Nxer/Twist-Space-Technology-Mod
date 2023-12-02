package com.Nxer.TwistSpaceTechnology.common.ship.system;

import java.util.ArrayList;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.common.ship.component.ShieldComponent;

public class ShieldSystem extends energyConsumer{

    public List<ShieldComponent> shields = new ArrayList<>();
    @Override
    void getEnengyConsumeinAFrame() {
        //TODO
    }
}
