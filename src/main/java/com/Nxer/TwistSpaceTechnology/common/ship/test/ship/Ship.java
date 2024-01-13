package com.Nxer.TwistSpaceTechnology.common.ship.test.ship;

import java.util.ArrayList;

import com.Nxer.TwistSpaceTechnology.common.ship.test.ship.component.ShieldComponent;
import com.Nxer.TwistSpaceTechnology.common.ship.test.ship.component.WeaponComponent;
import com.Nxer.TwistSpaceTechnology.common.ship.test.ship.system.ShieldSystem;
import com.Nxer.TwistSpaceTechnology.common.ship.test.ship.system.WeaponSystem;

public class Ship {

    public String name;
    public WeaponSystem weaponSystem;
    public ShieldSystem shieldSystem;
    public Ship target;
    public int HP;

    public Ship(int HP, int damage, int fireRate, int maxShieldPoint) {
        this.HP = HP;
        this.weaponSystem = new WeaponSystem(new ArrayList<>() {

            {
                add(new WeaponComponent(damage, fireRate));
            }
        });
        this.shieldSystem = new ShieldSystem(new ArrayList<>() {

            {
                add(new ShieldComponent(maxShieldPoint));
            }
        });
    }

    public void openFire() {
        int damage = this.weaponSystem.getAllDamage();
        if (this.target.shieldSystem.anyShieldOnline() != null) {
            this.target.shieldSystem.anyShieldOnline().shieldPoint -= damage;
        } else {
            this.target.HP -= damage;
        }
        int target_left_sheild = 0;
        for (var i : this.target.shieldSystem.shields) {
            if (i.shieldPoint > 0) target_left_sheild += i.shieldPoint;
        }
        System.out.printf(
            "%s向%s开火，造成了%d点伤害，%s剩余%d护盾，%d血量\n",
            this.name,
            this.target.name,
            damage,
            this.target.name,
            target_left_sheild,
            this.target.HP);
    }
}
