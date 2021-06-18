package com.github.pareronia.aoc.geometry;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class PositionTestCase {

    @Test
    public void translateWithAmplitude() {
        final Point result = Position.of(4, 5).translate(Vector.of(2, 2), 2);
        
        assertThat(result, is(Position.of(8, 9)));
    }
    
    @Test
    public void translate() {
        final Point result = Position.of(4, 5).translate(Vector.of(2, -2));
        
        assertThat(result, is(Position.of(6, 3)));
    }
    
    @Test
    public void equals() {
        assertThat(Position.of(1, 2), equalTo(Position.of(1, 2)));
        assertThat(Position.of(1, 2), not(equalTo(Position.of(2, 2))));
    }
}
