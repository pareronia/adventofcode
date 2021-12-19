package com.github.pareronia.aoc.geometry3d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class Transformations3DTestCase {

    @Test
    public void translate() {
        final Position3D position = Position3D.of(-5, 0, 0);
        final Vector3D vector = Vector3D.of(5, 2, 0);
        
        final Position3D result = Transformations3D.translate(position, vector);
        
        assertThat(result, is(Position3D.of(0, 2, 0)));
    }
}
