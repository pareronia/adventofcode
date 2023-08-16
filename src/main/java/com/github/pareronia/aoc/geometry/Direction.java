package com.github.pareronia.aoc.geometry;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.github.pareronia.aoc.AssertUtils;

import lombok.Getter;

public enum Direction {

    NONE(Vector.of(0, 0), Optional.empty(), Optional.empty()),
    UP(Vector.of(0, 1), Optional.of('U'), Optional.of('^')),
    RIGHT_AND_UP(Vector.of(1, 1), Optional.empty(), Optional.empty()),
    RIGHT(Vector.of(1, 0), Optional.of('R'), Optional.of('>')),
    RIGHT_AND_DOWN(Vector.of(1, -1), Optional.empty(), Optional.empty()),
    DOWN(Vector.of(0, -1), Optional.of('D'), Optional.of('v')),
    LEFT_AND_DOWN(Vector.of(-1, -1), Optional.empty(), Optional.empty()),
    LEFT(Vector.of(-1, 0), Optional.of('L'), Optional.of('<')),
    LEFT_AND_UP(Vector.of(-1, 1), Optional.empty(), Optional.empty());

    public static final Set<Direction> CAPITAL = EnumSet.of(
        UP,
        RIGHT,
        DOWN,
        LEFT
    );
    
    public static final Set<Direction> OCTANTS = EnumSet.of(
        UP,
        RIGHT_AND_UP,
        RIGHT,
        RIGHT_AND_DOWN,
        DOWN,
        LEFT_AND_DOWN,
        LEFT,
        LEFT_AND_UP
    );
    
    public static final Set<Character> CAPITAL_ARROWS = CAPITAL.stream()
            .map(d -> d.arrow.get()).collect(toSet());
    
    @Getter
    private final Vector vector;
    private final Optional<Character> letter;
    private final Optional<Character> arrow;

    Direction(
        final Vector value,
        final Optional<Character> letter,
        final Optional<Character> arrow
    ) {
        this.vector = value;
        this.letter = letter;
        this.arrow = arrow;
    }
    
    public static final Direction fromChar(final char ch) {
        return Arrays.stream(values())
            .filter(v -> v.letter.isPresent() && v.letter.get() == ch
                    || v.arrow.isPresent() && v.arrow.get() == ch)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                                String.format("Invalid Direction: '%s'", ch)));
    }
    
    public static final Direction fromString(final String string) {
        AssertUtils.assertNotNull(string, () -> "Expected string to be non-null");
        if (string.length() == 1) {
            return fromChar(string.charAt(0));
        } else {
            throw new IllegalArgumentException(String.format("Invalid Direction: '%s'", string));
        }
    }
    
    public Integer getX() {
        return this.vector.getX();
    }
    
    public Integer getY() {
        return this.vector.getY();
    }
    
    public Direction turn(final Turn turn) {
        AssertUtils.assertNotNull(turn, () -> "Expected turn be non-null");
        switch (this) {
        case UP:
            return turn == Turn.AROUND ? DOWN : turn == Turn.LEFT ? LEFT : RIGHT;
        case RIGHT:
            return turn == Turn.AROUND ? LEFT : turn == Turn.LEFT ? UP : DOWN;
        case DOWN:
            return turn == Turn.AROUND ? UP : turn == Turn.LEFT ? RIGHT : LEFT;
        case LEFT:
            return turn == Turn.AROUND ? RIGHT : turn == Turn.LEFT ? DOWN : UP;
        default:
            throw new UnsupportedOperationException();
        }
    }
}
