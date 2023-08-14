package com.github.pareronia.aoc.navigation;

import java.util.Arrays;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.geometry.Vector;

import lombok.ToString;

@ToString
public enum Heading {
    NORTH(Direction.UP),
    NORTHEAST(Direction.RIGHT_AND_UP),
    EAST(Direction.RIGHT),
    SOUTHEAST(Direction.RIGHT_AND_DOWN),
    SOUTH(Direction.DOWN),
    SOUTHWEST(Direction.LEFT_AND_DOWN),
    WEST(Direction.LEFT),
    NORTHWEST(Direction.LEFT_AND_UP);
    
    private final Direction direction;

    Heading(final Direction direction) {
        this.direction = direction;
    }
    
    public static Heading fromDirection(final Direction direction) {
        return Arrays.stream(values())
                .filter(v -> v.direction == direction)
                .findFirst().orElseThrow();
    }
    
    public static final Heading fromChar(final char ch) {
        return fromDirection(Direction.fromChar(ch));
    }
    
    public Vector getVector() {
        return this.direction.getVector();
    }
    
    public Heading turn(final Turn turn) {
        return fromDirection(this.direction.turn(turn));
    }
}
