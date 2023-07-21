package com.github.pareronia.aoc.geometry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class PositionTestCase {

    @Test
    public void translateWithAmplitude() {
        final Point result = Position.of(4, 5).translate(Vector.of(2, 2), 2);
        
        assertThat(result).isEqualTo(Position.of(8, 9));
    }
    
    @Test
    public void translate() {
        final Point result = Position.of(4, 5).translate(Vector.of(2, -2));
        
        assertThat(result).isEqualTo(Position.of(6, 3));
    }
    
    @Test
    public void equals() {
        assertThat(Position.of(1, 2)).isEqualTo(Position.of(1, 2));
        assertThat(Position.of(1, 2)).isNotEqualTo(Position.of(2, 2));
    }
}
