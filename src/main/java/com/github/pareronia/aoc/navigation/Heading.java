package com.github.pareronia.aoc.navigation;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.geometry.Vector;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Heading {
    public static final Heading NORTH = Heading.fromDirection(Direction.UP);
    public static final Heading NORTHEAST = Heading.fromDirection(Direction.RIGHT_AND_UP);
    public static final Heading EAST = Heading.fromDirection(Direction.RIGHT);
    public static final Heading SOUTHEAST = Heading.fromDirection(Direction.RIGHT_AND_DOWN);
    public static final Heading SOUTH = Heading.fromDirection(Direction.DOWN);
    public static final Heading SOUTHWEST = Heading.fromDirection(Direction.LEFT_AND_DOWN);
    public static final Heading WEST = Heading.fromDirection(Direction.LEFT);
    public static final Heading NORTHWEST = Heading.fromDirection(Direction.LEFT_AND_UP);
    
    @Getter
    private final Vector vector;
    
    private Heading(final Vector direction) {
        this.vector = direction;
    }
    
    public static final Heading of(final int x, final int y) {
        return new Heading(Vector.of(x, y));
    }
    
    public static Heading fromDirection(final Direction direction) {
        return new Heading(direction.getVector());
    }
    
    public static Heading fromString(final String string) {
        return Heading.fromDirection(Direction.fromString(string));
    }
    
    public Heading turn(final Turn turn) {
        return new Heading(this.vector.rotate(turn));
    }
    
    public Heading add(final Direction direction, final int amplitude) {
        return new Heading(this.getVector().add(direction.getVector(), amplitude));
    }
}
