package com.github.pareronia.aoc.navigation;

import com.github.pareronia.aoc.geometry.Vector;

public class WayPoint extends Vector {

    public WayPoint(Integer x, Integer y) {
        super(x, y);
    }

    public static WayPoint from(Vector vector) {
        return new WayPoint(vector.getX(), vector.getY());
    }

    @Override
    public WayPoint rotate(Integer degrees) {
        return WayPoint.from(super.rotate(degrees));
    }

    @Override
    public WayPoint add(Vector vector, Integer amplitude) {
        return WayPoint.from(super.add(vector, amplitude));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("WayPoint(x=").append(getX()).append(", y=")
                .append(getY()).append(")");
        return builder.toString();
    }
}
