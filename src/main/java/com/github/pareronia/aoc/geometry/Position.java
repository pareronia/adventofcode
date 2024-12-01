package com.github.pareronia.aoc.geometry;

import java.util.stream.Stream;

public class Position extends Point {

    public static final Position ORIGIN = Position.of(0, 0);
    
    private Position(final Integer x, final Integer y) {
        super(x, y);
    }
    
    public static Position of(final Integer x, final Integer y) {
        return new Position(x, y);
    }
    
    public static Position from(final Point point) {
        return new Position(point.getX(), point.getY());
    }
    
    public Position translate(final Vector vector, final Integer amplitude) {
        final Point point = this.withX(this.getX() + vector.getX() * amplitude)
                .withY(this.getY() + vector.getY() * amplitude);
        return Position.from(point);
    }

    public Position translate(final Vector vector) {
        return translate(vector, 1);
    }
    
    public Position translate(final Direction direction, final Integer amplitude) {
        return translate(direction.getVector(), amplitude);
    }
    
    public Position translate(final Direction direction) {
        return translate(direction.getVector(), 1);
    }
    
    public Integer manhattanDistance() {
        return manhattanDistance(ORIGIN);
    }
    
    public Integer manhattanDistance(final Position from) {
        return Math.abs(this.getX() - from.getX())
                + Math.abs(this.getY() - from.getY());
    }
    
    public Stream<Position> capitalNeighbours() {
        return Direction.CAPITAL.stream()
            .map(this::translate);
    }
    
    public Stream<Position> allNeighbours() {
        return Direction.OCTANTS.stream()
            .map(this::translate);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Position(x=").append(getX()).append(", y=")
                .append(getY()).append(")");
        return builder.toString();
    }
}
