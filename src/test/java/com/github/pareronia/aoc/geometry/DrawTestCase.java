package com.github.pareronia.aoc.geometry;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class DrawTestCase {

    @Test
    void fromPositions() {
	    final List<String> grid1 = Draw.draw(Set.of(Position.of(0, 0), Position.of(3, 3)), '#', '.');
	    
	    assertThat(grid1).containsExactly( "...#.",
	                                       ".....",
	                                       ".....",
	                                       "#....");
	    
	    final List<String> grid2 = Draw.draw(Set.of(Position.of(-3, -3), Position.of(0, 0)), '#', '.');
	    
	    assertThat(grid2).containsExactly( "...#.",
	                                       ".....",
	                                       ".....",
	                                       "#....");
    }
}
