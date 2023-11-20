package com.Nxer.TwistSpaceTechnology.common.ship.bullet;

import net.minecraft.entity.Entity;

public abstract class BasicBullet {

    public int count=0;
    public Entity createEntityBullet(){
        count--;
        return  null;
    }
}
