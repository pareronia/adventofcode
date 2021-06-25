package com.github.pareronia.aoc.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Vector;

import lombok.Getter;
import lombok.ToString;

@ToString(onlyExplicitlyIncluded = true)
public class Navigation {

    @Getter
    @ToString.Include
    protected Position position;
    private final List<Position> visitedPositions = new ArrayList<>();
    private final Predicate<Position> inBoundsPredicate;
    
    public Navigation(Position position) {
        this.position = position;
        rememberVisitedPosition(position);
        inBoundsPredicate = pos -> true;
    }
    
    public Navigation(Position position, Predicate<Position> inBounds) {
        this.position = position;
        rememberVisitedPosition(position);
        inBoundsPredicate = inBounds;
    }
    
    protected boolean inBounds(Position position) {
        return inBoundsPredicate.test(position);
    }
    
    protected final void rememberVisitedPosition(Position position) {
        this.visitedPositions.add(position);
    }
    
    protected void translate(Vector heading, Integer amount) {
        Position newPosition = this.position;
        for (int i = 0; i < amount; i++) {
            newPosition = newPosition.translate(heading, 1);
            if (inBounds(newPosition))  {
                this.position = newPosition;
            }
        }
        rememberVisitedPosition(this.position);
    }
    
    public List<Position> getVisitedPositions() {
        return getVisitedPositions(false);
    }
    
    public List<Position> getVisitedPositions(boolean includeStartPosition) {
        if (includeStartPosition) {
            return this.visitedPositions;
        } else {
            return this.visitedPositions.subList(1, this.visitedPositions.size());
        }
    }
}
