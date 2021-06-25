package com.github.pareronia.aoc.geometry;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
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
    
    public Integer manhattanDistance() {
        return manhattanDistance(Position.of(0, 0));
    }
    
    public Integer manhattanDistance(Position from) {
        return Math.abs(this.getX() - from.getX())
                + Math.abs(this.getY() - from.getY());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Position(x=").append(getX()).append(", y=")
                .append(getY()).append(")");
        return builder.toString();
    }
}
