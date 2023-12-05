package com.Nxer.TwistSpaceTechnology.common.ship.system;

import java.util.ArrayList;
import java.util.List;

public class WeaponSystem extends energyConsumer {

    public List<WeaponSystem> weapons = new ArrayList<>();
    public long sWeaponSlot;
    public long mWeaponSlot;
    public long lWeaponSlot;
    public long retrofitSlot; // 改装件槽位

    @Override
    void getEnengyConsumeinAFrame() {
        // TODO
    }
}
