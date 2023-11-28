package com.Nxer.TwistSpaceTechnology.common.ship.component;

import static java.lang.Math.sqrt;

public abstract class ShipComponent {

    private double hp = 1.0;

    private double weight = 1.0;
    private int x = 0, y = 0, z = 0;

    private ShipComponent up, down, left, right, front, back;
    private double energyCost;
    private double activated;

    public void setPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

    }

    public double distanceTo(double x, double y, double z) {
        var _x = this.x - x;
        var _y = this.y - y;
        var _z = this.z - z;
        return sqrt(_x * _x + _y * _y + _z * _z);
    }
}
