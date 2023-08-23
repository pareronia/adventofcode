package com.github.pareronia.aoc.navigation;

import java.util.function.Predicate;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Turn;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class NavigationWithHeading extends Navigation {
    
    @ToString.Include
    @Getter
    @Setter
    private Heading heading;

    public NavigationWithHeading(final Position position, final Heading heading) {
        super(position);
        this.heading = heading;
    }

    public NavigationWithHeading(
            final Position position,
            final Heading heading,
            final Predicate<Position> inBounds
    ) {
        super(position, inBounds);
        this.heading = heading;
    }
    
    public NavigationWithHeading navigate(final Heading heading, final int amount) {
        this.heading = heading;
        forward(amount);
        return this;
    }

    public NavigationWithHeading turn(final Turn turn) {
        this.heading = this.heading.turn(turn);
        return this;
    }
    
    public NavigationWithHeading forward(final Integer amount) {
        translate(this.heading.getVector(), amount);
        return this;
    }
    
    public NavigationWithHeading drift(final Heading heading, final Integer amount) {
        translate(heading.getVector(), amount);
        return this;
    }
}
