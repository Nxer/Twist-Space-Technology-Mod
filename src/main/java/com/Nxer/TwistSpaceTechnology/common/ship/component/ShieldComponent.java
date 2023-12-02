package com.Nxer.TwistSpaceTechnology.common.ship.component;

public class ShieldComponent extends ShipComponent {

    public enum ShieldType {
        ZhuangJia,  //装甲，不是庄稼，更不是金坷垃
        HuDun  //护盾
    }

    // charge eu cost for every second
    public double energyCostForMaintenance;
    // charge eu cost for every single shield point
    public double energyCostForRecharge;
    public double shieldPoint;
    public double maxShieldPoint;
    public double radio;
    public boolean isNumberShield;
    public boolean overLocked;
    public ShieldType shieldType;
}

// S,M,L,X,T
//
