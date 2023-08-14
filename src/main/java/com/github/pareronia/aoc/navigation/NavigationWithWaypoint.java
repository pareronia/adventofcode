package com.github.pareronia.aoc.navigation;

import com.github.pareronia.aoc.geometry.Position;

import lombok.ToString;

@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class NavigationWithWaypoint extends Navigation {
    
    @ToString.Include
    private WayPoint wayPoint;

    public NavigationWithWaypoint(final Position position, final WayPoint wayPoint) {
        super(position);
        this.wayPoint = wayPoint;
    }
    
    public NavigationWithWaypoint right(final Integer degrees) {
        this.wayPoint = this.wayPoint.rotate(degrees);
        return this;
    }
    
    public NavigationWithWaypoint left(final Integer degrees) {
        this.wayPoint = this.wayPoint.rotate(-degrees);
        return this;
    }
    
    public NavigationWithWaypoint forward(final Integer amount) {
        translate(this.wayPoint, amount);
        return this;
    }
    
    public NavigationWithWaypoint updateWaypoint(final Heading heading, final Integer amount) {
        this.wayPoint = this.wayPoint.add(heading.getVector(), amount);
        return this;
    }
}
