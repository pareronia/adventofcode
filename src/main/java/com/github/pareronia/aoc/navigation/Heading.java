package com.github.pareronia.aoc.navigation;

import com.github.pareronia.aoc.geometry.Vector;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class Heading extends Vector {

    public Heading(Integer x, Integer y) {
        super(x, y);
    }
    
    public static Heading of(Integer x, Integer y) {
        return new Heading(x, y);
    }
    
    public static Heading from(Vector vector) {
        return new Heading(vector.getX(), vector.getY());
    }

    @Override
    public Heading rotate(Integer degrees) {
        return Heading.from(super.rotate(degrees));
    }
}
