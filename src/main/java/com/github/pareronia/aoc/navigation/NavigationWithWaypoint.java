package com.github.pareronia.aoc.navigation;

import com.github.pareronia.aoc.geometry.Position;

import lombok.ToString;

@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class NavigationWithWaypoint extends Navigation {
    
    @ToString.Include
    private WayPoint wayPoint;

    public NavigationWithWaypoint(Position position, WayPoint wayPoint) {
        super(position);
        this.wayPoint = wayPoint;
    }
    
    public NavigationWithWaypoint right(Integer degrees) {
        this.wayPoint = this.wayPoint.rotate(degrees);
        return this;
    }
    
    public NavigationWithWaypoint left(Integer degrees) {
        this.wayPoint = this.wayPoint.rotate(-degrees);
        return this;
    }
    
    public NavigationWithWaypoint forward(Integer amount) {
        translate(this.wayPoint, amount);
        return this;
    }
    
    public NavigationWithWaypoint updateWaypoint(Heading heading, Integer amount) {
        this.wayPoint = this.wayPoint.add(heading, amount);
        return this;
    }
}
