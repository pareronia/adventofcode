package com.github.pareronia.aoc.geometry;

import com.github.pareronia.aoc.AssertUtils;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@EqualsAndHashCode(callSuper = true)
@NonFinal
public class Vector extends Point {

    protected Vector(final Integer x, final Integer y) {
        super(x, y);
    }
    
    public static Vector of(final Integer x, final Integer y) {
        return new Vector(x, y);
    }
    
    private static Vector from(final Point point) {
        return new Vector(point.getX(), point.getY());
    }

    private Vector rotate90() {
        final Point point = this.withX(this.getY()).withY(-1 * this.getX());
        return Vector.from(point);
    }
    
    private Vector rotate(int degrees) {
        if (degrees < 0) {
            degrees = 360 + degrees;
        }
        AssertUtils.assertTrue(
                degrees % 90 == 0,
                () -> "degrees to rotate should be multiple of 90");
        Vector result = Vector.from(this);
        for (int i = 0; i < degrees / 90; i++) {
            result = result.rotate90();
        }
        return result;
    }
    
    public Vector rotate(final Turn turn) {
        return this.rotate(turn.getDegrees());
    }
    
    public Vector add(final Vector vector, final Integer amplitude) {
        final Point point = this
                .withX(this.getX() + vector.getX() * amplitude)
                .withY(this.getY() + vector.getY() * amplitude);
        return Vector.from(point);
    }
}
