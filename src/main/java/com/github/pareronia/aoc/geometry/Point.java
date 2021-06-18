package com.github.pareronia.aoc.geometry;

import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Point {
    @With private final Integer x;
    @With private final Integer y;
}
