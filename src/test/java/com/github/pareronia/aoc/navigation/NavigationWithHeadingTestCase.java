package com.github.pareronia.aoc.navigation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.junit.Test;

import com.github.pareronia.aoc.geometry.Position;

public class NavigationWithHeadingTestCase {

    @Test
    public void test() {
        final NavigationWithHeading nav
                = new NavigationWithHeading(Position.of(0, 0), Headings.NORTH.get())
            .drift(Headings.NORTH.get(), 1)
            .left(90)
            .forward(1)
            .right(180)
            .forward(1);
        
        assertThat(nav.getPosition(), is(Position.of(0, 1)));
        assertThat(nav.getVisitedPositions(true),
                    contains(   Position.of(0, 0),
                                Position.of(0, 1),
                                Position.of(-1, 1),
                                Position.of(0, 1)));
        assertThat(nav.getVisitedPositions(),
                    contains(   Position.of(0, 1),
                                Position.of(-1, 1),
                                Position.of(0, 1)));
    }
    
    @Test
    public void testInBounds() {
        final NavigationWithHeading nav = new NavigationWithHeading(
                Position.of(0, 0),
                Headings.NORTH.get(),
                pos -> pos.getX() <= 4 && pos.getY() <= 4)
            .drift(Headings.NORTH.get(), 1)
            .left(90)
            .forward(1)
            .right(180)
            .forward(10);
        
        System.out.println(nav.getVisitedPositions(true));
        assertThat(nav.getPosition(), is(Position.of(4, 1)));
        assertThat(nav.getVisitedPositions(true),
                    contains(   Position.of(0, 0),
                                Position.of(0, 1),
                                Position.of(-1, 1),
                                Position.of(4, 1)));
        assertThat(nav.getVisitedPositions(),
                    contains(   Position.of(0, 1),
                                Position.of(-1, 1),
                                Position.of(4, 1)));
    }
}
