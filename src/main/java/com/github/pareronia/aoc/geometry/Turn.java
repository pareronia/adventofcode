package com.github.pareronia.aoc.geometry;

import java.util.Arrays;
import java.util.Optional;

import com.github.pareronia.aoc.AssertUtils;

public enum Turn {

    LEFT(270, Optional.of('L')),
    RIGHT(90, Optional.of('R')),
    AROUND(180, Optional.empty());
    
    private final int degrees;
    private final Optional<Character> letter;
    
    Turn(final int degrees, final Optional<Character> letter) {
        this.degrees = degrees;
        this.letter = letter;
    }

    public static Turn fromString(final String string) {
        AssertUtils.assertNotNull(string, () -> "Expected string to be non-null");
        if (string.length() == 1) {
            return fromChar(string.charAt(0));
        } else {
            throw new IllegalArgumentException(
                    String.format("Invalid Direction: '%s'", string));
        }
    }
    
    public static final Turn fromChar(final char ch) {
        return Arrays.stream(values())
            .filter(v -> v.letter.isPresent() && v.letter.get() == ch)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                                String.format("Invalid Direction: '%s'", ch)));
    }
    
    public static Turn fromDegrees(final int degrees) {
        final int val = degrees % 360;
        return Arrays.stream(values())
            .filter(v -> v.degrees == val)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                                String.format("Invalid degrees: '%s'", degrees)));
    }
}
