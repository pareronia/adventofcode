package com.github.pareronia.aoc.geometry3d;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CuboidTestCase {
    
    @Test
    public void test() {
        final Cuboid cuboid = Cuboid.of(0, 2, 0, 2, 0, 2);
        
        assertThat(cuboid.x1()).isEqualTo(0);
        assertThat(cuboid.y1()).isEqualTo(0);
        assertThat(cuboid.z1()).isEqualTo(0);
        assertThat(cuboid.x2()).isEqualTo(2);
        assertThat(cuboid.y2()).isEqualTo(2);
        assertThat(cuboid.z2()).isEqualTo(2);
        assertThat(cuboid.getVolume()).isEqualTo(27L);
    }
    
    @Test
    public void testOverlap() {
        final Cuboid cuboid1 = Cuboid.of(0, 2, 0, 2, 0, 2);
        assertThat(
            Cuboid.overlap(cuboid1, Cuboid.of(0, 2, 0, 2, 0, 2)))
            .isTrue();
        assertThat(
            Cuboid.overlap(cuboid1, Cuboid.of(0, 1, 0, 1, 0, 1)))
            .isTrue();
        assertThat(
            Cuboid.overlap(cuboid1, Cuboid.of(0, 2, 0, 2, 3, 4)))
            .isFalse();
        assertThat(
            Cuboid.overlap(cuboid1, Cuboid.of(-2, -1, -2, -1, -2, -1)))
            .isFalse();
    }
    
    @Test
    public void testIntersection() {
        final Cuboid cuboid1 = Cuboid.of(0, 2, 0, 2, 0, 2);
        assertThat(
            Cuboid.intersection(cuboid1,
                                Cuboid.of(0, 2, 0, 2, 0, 2))).get()
            .isEqualTo(Cuboid.of(0, 2, 0, 2, 0, 2));
        assertThat(
            Cuboid.intersection(cuboid1,
                                Cuboid.of(0, 1, 0, 1, 0, 1))).get()
            .isEqualTo(Cuboid.of(0, 1, 0, 1, 0, 1));
        assertThat(
            Cuboid.intersection(Cuboid.of(10, 12, 10, 12, 10, 12),
                                Cuboid.of(11, 13, 11, 13, 11, 13))
                .get())
            .isEqualTo(Cuboid.of(11, 12, 11, 12, 11, 12));
        assertThat(
            Cuboid.intersection(cuboid1, Cuboid.of(0, 2, 0, 2, 3, 4)))
            .isEmpty();
        assertThat(
            Cuboid.intersection(cuboid1,
                                Cuboid.of(-2, -1, -2, -1 , -2, -1)))
            .isEmpty();
    }
    
    @Test
    public void testContains() {
        final Cuboid cuboid = Cuboid.of(0, 2, 0, 2, 0, 2);
        assertThat(cuboid.contains(Position3D.of(-1, 0, 0))).isFalse();
        assertThat(cuboid.contains(Position3D.of(0, 0, 0))).isTrue();
        assertThat(cuboid.contains(Position3D.of(0, 1, 1))).isTrue();
        assertThat(cuboid.contains(Position3D.of(2, 1, 1))).isTrue();
        assertThat(cuboid.contains(Position3D.of(2, 2, 3))).isFalse();
    }
    
    @Test
    public void testPositionStream() {
        final Cuboid cuboid = Cuboid.of(0, 2, 0, 1, 0, 1);
        assertThat(cuboid.positions().collect(toSet())).containsExactlyInAnyOrder(
            Position3D.of(0, 0, 0),
            Position3D.of(0, 0, 1),
            Position3D.of(0, 1, 0),
            Position3D.of(0, 1, 1),
            Position3D.of(1, 0, 0),
            Position3D.of(1, 0, 1),
            Position3D.of(1, 1, 0),
            Position3D.of(1, 1, 1),
            Position3D.of(2, 0, 0),
            Position3D.of(2, 0, 1),
            Position3D.of(2, 1, 0),
            Position3D.of(2, 1, 1)
        );
    }
}
