package com.github.pareronia.aoc.geometry;

import lombok.Value;

@Value
public class Position extends Point {
    
    private Position(Integer x, Integer y) {
        super(x, y);
    }
    
    public static Position of(Integer x, Integer y) {
        return new Position(x, y);
    }
    
    public static Position from(Point point) {
        return new Position(point.getX(), point.getY());
    }
    
    public Position translate(Vector vector, Integer amplitude) {
        final Point point = this.withX(this.getX() + vector.getX() * amplitude)
                .withY(this.getY() + vector.getY() * amplitude);
        return Position.from(point);
    }

    public Position translate(Vector vector) {
        return translate(vector, 1);
    }
}
