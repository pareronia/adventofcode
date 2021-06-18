package com.github.pareronia.aoc.navigation;

public enum Headings {
    NORTH(0, 1),
    EAST(1, 0),
    SOUTH(0, -1),
    WEST(-1, 0);
 
    private final Integer x;
    private final Integer y;
    
    private Headings(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
    
    public Heading get() {
        return Heading.of(x, y);
    }
}
