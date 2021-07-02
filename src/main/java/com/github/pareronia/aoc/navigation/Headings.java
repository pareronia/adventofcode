package com.github.pareronia.aoc.navigation;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum Headings {
    NORTH(0, 1),
    NORTHEAST(1, 1),
    EAST(1, 0),
    SOUTHEAST(1, -1),
    SOUTH(0, -1),
    SOUTHWEST(-1, -1),
    WEST(-1, 0),
    NORTHWEST(-1, 1);
    
    public static final Set<Headings> CAPITAL
            = EnumSet.of(NORTH, EAST, SOUTH, WEST);
    
    public static final List<Headings> UDLR = List.of(NORTH, SOUTH, WEST, EAST);
    
    public static final Set<Headings> OCTANTS = EnumSet.allOf(Headings.class);
 
    private final Integer x;
    private final Integer y;
    
    private Headings(final Integer x, final Integer y) {
        this.x = x;
        this.y = y;
    }
    
    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Heading get() {
        return Heading.of(x, y);
    }
}
