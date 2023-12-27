package com.Nxer.TwistSpaceTechnology.common.ship.test.ship.component;

import com.Nxer.TwistSpaceTechnology.common.ship.test.ship.bullet.BasicBullet;

public class WeaponComponent {

    public BasicBullet bulletType;
    public int fireRate;
    public int fireTickCount;

    public WeaponComponent(int damage, int fireRate) {
        this.bulletType = new BasicBullet(damage);
        this.fireRate = fireRate;
        this.fireTickCount = fireRate;
    }
}
