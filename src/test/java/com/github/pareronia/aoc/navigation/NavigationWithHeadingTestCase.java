package com.github.pareronia.aoc.navigation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Turn;

public class NavigationWithHeadingTestCase {

    @Test
    public void test() {
        final NavigationWithHeading nav
                = new NavigationWithHeading(Position.of(0, 0), Heading.NORTH)
            .drift(Heading.NORTH, 1)
            .turn(Turn.LEFT)
            .forward(1)
            .turn(Turn.AROUND)
            .forward(1);
        
        assertThat(nav.getPosition()).isEqualTo(Position.of(0, 1));
        assertThat(nav.getVisitedPositions(true))
            .containsExactly(   Position.of(0, 0),
                                Position.of(0, 1),
                                Position.of(-1, 1),
                                Position.of(0, 1));
        assertThat(nav.getVisitedPositions())
            .containsExactly(   Position.of(0, 1),
                                Position.of(-1, 1),
                                Position.of(0, 1));
    }
    
    @Test
    public void testInBounds() {
        final NavigationWithHeading nav = new NavigationWithHeading(
                Position.of(0, 0),
                Heading.NORTH,
                pos -> pos.getX() <= 4 && pos.getY() <= 4)
            .drift(Heading.NORTH, 1)
            .turn(Turn.LEFT)
            .forward(1)
            .turn(Turn.AROUND)
            .forward(10);
        
        log(nav.getVisitedPositions(true));
        assertThat(nav.getPosition()).isEqualTo(Position.of(4, 1));
        assertThat(nav.getVisitedPositions(true))
            .containsExactly(   Position.of(0, 0),
                                Position.of(0, 1),
                                Position.of(-1, 1),
                                Position.of(4, 1));
        assertThat(nav.getVisitedPositions())
            .containsExactly(   Position.of(0, 1),
                                Position.of(-1, 1),
                                Position.of(4, 1));
    }
    
    private void log(final Object string) {
        if (!System.getProperties().containsKey("NDEBUG")) {
            System.out.println(string);
        }
    }
}
