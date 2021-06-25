package com.github.pareronia.aoc.geometry;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@EqualsAndHashCode(callSuper = true)
@NonFinal
public class Vector extends Point {

    protected Vector(Integer x, Integer y) {
        super(x, y);
    }
    
    public static Vector of(Integer x, Integer y) {
        return new Vector(x, y);
    }
    
    public static Vector from(Point point) {
        return new Vector(point.getX(), point.getY());
    }

    private Vector rotate90() {
        final Point point = this.withX(this.getY()).withY(-1 * this.getX());
        return Vector.from(point);
    }
    
    public Vector rotate(Integer degrees) {
        if (degrees < 0) {
            degrees = 360 + degrees;
        }
        if (degrees % 90 != 0) {
            throw new IllegalArgumentException("degrees to rotate should be multiple of 90");
        }
        Vector result = Vector.from(this);
        for (int i = 0; i < degrees / 90; i++) {
            result = result.rotate90();
        }
        return result;
    }
    
    public Vector add(Vector vector, Integer amplitude) {
        final Point point = this
                .withX(this.getX() + vector.getX() * amplitude)
                .withY(this.getY() + vector.getY() * amplitude);
        return Vector.from(point);
    }
}
