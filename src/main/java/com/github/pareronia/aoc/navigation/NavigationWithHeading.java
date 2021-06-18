package com.github.pareronia.aoc.navigation;

import com.github.pareronia.aoc.geometry.Position;

import lombok.Data;

@Data
public class NavigationWithHeading extends Navigation {
    
    private Heading heading;

    public NavigationWithHeading(Position position, Heading heading) {
        super(position);
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
        this.position = this.position.translate(this.heading, amount);
        rememberVisitedPosition(this.position);
        return this;
    }
    
    public NavigationWithHeading drift(Heading heading, Integer amount) {
        this.position = this.position.translate(heading, amount);
        rememberVisitedPosition(this.position);
        return this;
    }
}
