package com.Nxer.TwistSpaceTechnology.common.ship.test.ship.component;

public class ShieldComponent{
    public int shieldPoint;
    public int maxShieldPoint;
    public ShieldComponent(int maxShieldPoint) {
        this.maxShieldPoint = maxShieldPoint;
        this.shieldPoint=maxShieldPoint; // init the shield
    }
}
