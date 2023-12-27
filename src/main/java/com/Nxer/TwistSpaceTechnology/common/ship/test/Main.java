package com.Nxer.TwistSpaceTechnology.common.ship.test;

import com.Nxer.TwistSpaceTechnology.common.ship.test.ship.Ship;

public class Main {

    public void test() {

        Ship ship1 = new Ship(10, 10, 1, 10);
        Ship ship2 = new Ship(10, 5, 1, 5);

        ship1.name = "Ship1";
        ship2.name = "Ship2";

        ship1.target = ship2;
        ship2.target = ship1;

        while (ship1.HP > 0 && ship2.HP > 0) {
            ship1.openFire();
            ship2.openFire();
        }
    }
}
