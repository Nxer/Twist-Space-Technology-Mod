package com.Nxer.TwistSpaceTechnology.common.ship.test.ship.system;

import com.Nxer.TwistSpaceTechnology.common.ship.test.ship.component.WeaponComponent;

import java.util.List;

public class WeaponSystem{
    public List<WeaponComponent> weapons;
    public WeaponSystem(List<WeaponComponent> weapons) {
        this.weapons = weapons;
    }
    public int getAllDamage(){
        int result=0;
        for(var i:this.weapons)
            if(i.fireTickCount==i.fireRate){
                result+=i.bulletType.damage;
                i.fireTickCount=1;
            }else i.fireTickCount++;
        return result;
    }
}
