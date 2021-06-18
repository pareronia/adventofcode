package com.github.pareronia.aoc.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.github.pareronia.aoc.geometry.Position;

import lombok.Data;

@Data
public class Navigation {

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
