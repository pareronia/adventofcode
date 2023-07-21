package com.github.pareronia.aoc.geometry3d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.Test;

public class Position3DTestCase {
    
    @Test
    public void translate() {
        final Position3D point = new Position3D(2, 4, 6);
        final Vector3D vector = new Vector3D(-2, -2, -2);
        
        final Point3D translated = point.translate(vector);
        
        assertThat(translated.getX(), is(0));
        assertThat(translated.getY(), is(2));
        assertThat(translated.getZ(), is(4));
    }
    
    @Test
    public void distance() {
        final Position3D p1 = new Position3D(7, 4, 3);
        final Position3D p2 = new Position3D(17, 6, 2);
        
        assertThat(p1.distance(p2), is(closeTo(10.25d, 2)));
        assertThat(p2.distance(p1), is(closeTo(10.25d, 2)));
        
        final Position3D p3 = new Position3D(404, -588, -901);
        final Position3D p4 = new Position3D(686, 422, 578);
        
        assertThat(p3.distance(p4), is(closeTo(1813.02d, 2)));
        assertThat(p4.distance(p3), is(closeTo(1813.02d, 2)));
    }
}
