package com.github.pareronia.aoc.navigation;

import java.util.function.Predicate;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Turn;

public class NavigationWithHeading extends Navigation {
    
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

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(final Heading heading) {
        this.heading = heading;
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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("NavigationWithHeading [heading=").append(heading).append("]");
        return builder.toString();
    }
}
