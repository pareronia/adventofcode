package com.github.pareronia.aoc.geometry3d;

import java.util.EnumSet;

import lombok.Getter;

@Getter
public enum Direction3D {

    UP(Vector3D.of(0, 1, 0)),
    RIGHT(Vector3D.of(1, 0, 0)),
    DOWN(Vector3D.of(0, -1, 0)),
    LEFT(Vector3D.of(-1, 0, 0)),
    FORWARD(Vector3D.of(0, 0, 1)),
    BACKWARD(Vector3D.of(0, 0, -1));
    
    public static EnumSet<Direction3D> CAPITAL = EnumSet.of(
        UP,
        RIGHT,
        DOWN,
        LEFT,
        FORWARD,
        BACKWARD
    );
    
    private final Vector3D value;

    Direction3D(final Vector3D value) {
        this.value = value;
    }
}
