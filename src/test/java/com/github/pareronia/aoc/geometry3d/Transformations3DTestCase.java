package com.github.pareronia.aoc.geometry3d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Test;

public class Transformations3DTestCase {

    @Test
    public void translate() {
        final Position3D position = Position3D.of(-5, 0, 0);
        final Vector3D vector = Vector3D.of(5, 2, 0);
        
        final Position3D result = Transformations3D.translate(position, vector);
        
        assertThat(result, is(Position3D.of(0, 2, 0)));
    }
    
    @Test
    public void rotateX90() {
        assertThat(Transformations3D.rotateX90(Position3D.of(0, 1, 0)), is(Position3D.of(0, 0, 1)));
        assertThat(Transformations3D.rotateX90(Position3D.of(0, 0, 1)), is(Position3D.of(0, -1, 0)));
        assertThat(Transformations3D.rotateX90(Position3D.of(0, -1, 0)), is(Position3D.of(0, 0, -1)));
        assertThat(Transformations3D.rotateX90(Position3D.of(0, 0, -1)), is(Position3D.of(0, 1, 0)));
        
        assertThat(Transformations3D.rotateX90(Position3D.of(0, 1, 1)), is(Position3D.of(0, -1, 1)));
        assertThat(Transformations3D.rotateX90(Position3D.of(0, -1, 1)), is(Position3D.of(0, -1, -1)));
        assertThat(Transformations3D.rotateX90(Position3D.of(0, -1, -1)), is(Position3D.of(0, 1, -1)));
        assertThat(Transformations3D.rotateX90(Position3D.of(0, 1, -1)), is(Position3D.of(0, 1, 1)));
        
        final List<Position3D> result = Transformations3D.rotateX90(
                Transformations3D.rotateX90(
                        Transformations3D.rotateX90(
                                Transformations3D.rotateX90(List.of(Position3D.of(1, 1, 1))))));
        assertThat(result.get(0), is(Position3D.of(1, 1, 1)));
    }

    @Test
    public void rotateY90() {
        assertThat(Transformations3D.rotateY90(Position3D.of(1, 0, 0)), is(Position3D.of(0, 0, -1)));
        assertThat(Transformations3D.rotateY90(Position3D.of(0, 0, -1)), is(Position3D.of(-1, 0, 0)));
        assertThat(Transformations3D.rotateY90(Position3D.of(-1, 0, 0)), is(Position3D.of(0, 0, 1)));
        assertThat(Transformations3D.rotateY90(Position3D.of(0, 0, 1)), is(Position3D.of(1, 0, 0)));
        
        assertThat(Transformations3D.rotateY90(Position3D.of(1, 0, 1)), is(Position3D.of(1, 0, -1)));
        assertThat(Transformations3D.rotateY90(Position3D.of(1, 0, -1)), is(Position3D.of(-1, 0, -1)));
        assertThat(Transformations3D.rotateY90(Position3D.of(-1, 0, -1)), is(Position3D.of(-1, 0, 1)));
        assertThat(Transformations3D.rotateY90(Position3D.of(-1, 0, 1)), is(Position3D.of(1, 0, 1)));
        
        final List<Position3D> result = Transformations3D.rotateY90(
                Transformations3D.rotateY90(
                        Transformations3D.rotateY90(
                                Transformations3D.rotateY90(List.of(Position3D.of(1, 1, 1))))));
        assertThat(result.get(0), is(Position3D.of(1, 1, 1)));
    }
    
    @Test
    public void rotateZ90() {
        final List<Position3D> result = Transformations3D.rotateZ90(
                Transformations3D.rotateZ90(
                        Transformations3D.rotateZ90(
                                Transformations3D.rotateZ90(List.of(Position3D.of(1, 1, 1))))));
        assertThat(result.get(0), is(Position3D.of(1, 1, 1)));
    }
}
