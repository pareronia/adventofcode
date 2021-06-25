package com.github.pareronia.aoc.navigation;

import java.util.function.Predicate;

import com.github.pareronia.aoc.geometry.Position;

import lombok.ToString;

@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class NavigationWithHeading extends Navigation {
    
    @ToString.Include
    private Heading heading;

    public NavigationWithHeading(Position position, Heading heading) {
        super(position);
        this.heading = heading;
    }

    public NavigationWithHeading(
            Position position,
            Heading heading,
            Predicate<Position> inBounds
    ) {
        super(position, inBounds);
        this.heading = heading;
    }

    public NavigationWithHeading right(Integer degrees) {
        this.heading = this.heading.rotate(degrees);
        return this;
    }

    public NavigationWithHeading left(Integer degrees) {
        this.heading = this.heading.rotate(-degrees);
        return this;
    }
    
    public NavigationWithHeading forward(Integer amount) {
        translate(this.heading, amount);
        return this;
    }
    
    public NavigationWithHeading drift(Heading heading, Integer amount) {
        translate(heading, amount);
        return this;
    }
}
