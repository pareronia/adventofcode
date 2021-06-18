package com.github.pareronia.aoc.navigation;

import java.util.function.Predicate;

import com.github.pareronia.aoc.geometry.Position;

import lombok.Data;

@Data
public class NavigationWithHeading extends Navigation {
    
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
        return translate(this.heading, amount);
    }
    
    public NavigationWithHeading drift(Heading heading, Integer amount) {
        return translate(heading, amount);
    }

    private NavigationWithHeading translate(Heading heading, Integer amount) {
        Position newPosition = this.position;
        for (int i = 0; i < amount; i++) {
            newPosition = newPosition.translate(heading, 1);
            if (inBounds(newPosition))  {
                this.position = newPosition;
            }
        }
        rememberVisitedPosition(this.position);
        return this;
    }
}
