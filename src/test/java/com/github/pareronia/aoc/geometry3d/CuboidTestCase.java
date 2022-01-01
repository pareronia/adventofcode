package com.github.pareronia.aoc.geometry3d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class CuboidTestCase {
    
    @Test
    public void test() {
        final Cuboid cuboid = Cuboid.of(0, 2, 0, 2, 0, 2);
        
        assertThat(cuboid.x1, is(0));
        assertThat(cuboid.y1, is(0));
        assertThat(cuboid.z1, is(0));
        assertThat(cuboid.x2, is(2));
        assertThat(cuboid.y2, is(2));
        assertThat(cuboid.z2, is(2));
        assertThat(cuboid.getVolume(), is(27L));
    }
    
    @Test
    public void testOverlap() {
        final Cuboid cuboid1 = Cuboid.of(0, 2, 0, 2, 0, 2);
        assertThat(
            Cuboid.overlap(cuboid1, Cuboid.of(0, 2, 0, 2, 0, 2)),
            is(true));
        assertThat(
            Cuboid.overlap(cuboid1, Cuboid.of(0, 1, 0, 1, 0, 1)),
            is(true));
        assertThat(
            Cuboid.overlap(cuboid1, Cuboid.of(0, 2, 0, 2, 3, 4)),
            is(false));
        assertThat(
            Cuboid.overlap(cuboid1, Cuboid.of(-2, -1, -2, -1, -2, -1)),
            is(false));
    }
    
    @Test
    public void testIntersection() {
        final Cuboid cuboid1 = Cuboid.of(0, 2, 0, 2, 0, 2);
        assertThat(
            Cuboid.intersection(cuboid1,
                                Cuboid.of(0, 2, 0, 2, 0, 2))
                .get(),
            is(Cuboid.of(0, 2, 0, 2, 0, 2)));
        assertThat(
            Cuboid.intersection(cuboid1,
                                Cuboid.of(0, 1, 0, 1, 0, 1))
                .get(),
            is(Cuboid.of(0, 1, 0, 1, 0, 1)));
        assertThat(
            Cuboid.intersection(Cuboid.of(10, 12, 10, 12, 10, 12),
                                Cuboid.of(11, 13, 11, 13, 11, 13))
                .get(),
            is(Cuboid.of(11, 12, 11, 12, 11, 12)));
        assertThat(
            Cuboid.intersection(cuboid1, Cuboid.of(0, 2, 0, 2, 3, 4)).isPresent(),
            is(false));
        assertThat(
            Cuboid.intersection(cuboid1,
                                Cuboid.of(-2, -1, -2, -1 , -2, -1))
                .isPresent(),
            is(false));
    }
}
