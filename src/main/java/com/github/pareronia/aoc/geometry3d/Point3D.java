package com.github.pareronia.aoc.geometry3d;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Point3D {
    private final int x;
    private final int y;
    private final int z;
}
