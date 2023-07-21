package com.github.pareronia.aoc.geometry3d;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import org.junit.jupiter.api.Test;

public class Position3DTestCase {
    
    @Test
    public void translate() {
        final Position3D point = new Position3D(2, 4, 6);
        final Vector3D vector = new Vector3D(-2, -2, -2);
        
        final Point3D translated = point.translate(vector);
        
        assertThat(translated.getX()).isEqualTo(0);
        assertThat(translated.getY()).isEqualTo(2);
        assertThat(translated.getZ()).isEqualTo(4);
    }
    
    @Test
    public void distance() {
        final Position3D p1 = new Position3D(7, 4, 3);
        final Position3D p2 = new Position3D(17, 6, 2);
        
        assertThat(p1.distance(p2)).isCloseTo(10.25d, offset(0.01d));
        assertThat(p2.distance(p1)).isCloseTo(10.25d, offset(0.01d));
        
        final Position3D p3 = new Position3D(404, -588, -901);
        final Position3D p4 = new Position3D(686, 422, 578);
        
        assertThat(p3.distance(p4)).isCloseTo(1813.02d, offset(0.01d));
        assertThat(p4.distance(p3)).isCloseTo(1813.02d, offset(0.01d));
    }
}
