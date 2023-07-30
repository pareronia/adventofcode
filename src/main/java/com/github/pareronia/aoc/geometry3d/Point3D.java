package com.github.pareronia.aoc.geometry3d;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.With;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Point3D {
    @With private final int x;
    @With private final int y;
    @With private final int z;
}
