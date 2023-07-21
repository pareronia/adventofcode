package com.github.pareronia.aoc.geometry3d;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class Vector3DTestCase {

    @Test
    public void from() {
        final Vector3D result = Vector3D.from(Position3D.of(2, 2, 2), Position3D.of(0, 0, 0));
        
        assertThat(result).isEqualTo(Vector3D.of(-2, -2, -2));
    }
}
