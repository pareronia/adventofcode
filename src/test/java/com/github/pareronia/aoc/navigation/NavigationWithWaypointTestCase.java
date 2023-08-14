package com.github.pareronia.aoc.navigation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.geometry.Position;

public class NavigationWithWaypointTestCase {

    @Test
    public void test() {
        final NavigationWithWaypoint nav
                = new NavigationWithWaypoint(Position.of(0, 0), new WayPoint(0, 0))
            .updateWaypoint(Heading.NORTH, 1)
            .forward(1)
            .left(90)
            .forward(1)
            .right(180)
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
}
