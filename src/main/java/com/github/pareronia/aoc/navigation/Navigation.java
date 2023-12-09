package com.github.pareronia.aoc.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Vector;

public class Navigation {

    protected Position position;
    private final List<Position> visitedPositions = new ArrayList<>();
    private final Predicate<Position> inBoundsPredicate;
    
    public Navigation(final Position position) {
        this.position = position;
        rememberVisitedPosition(position);
        inBoundsPredicate = pos -> true;
    }
    
    public Navigation(final Position position, final Predicate<Position> inBounds) {
        this.position = position;
        rememberVisitedPosition(position);
        inBoundsPredicate = inBounds;
    }
    
    public Position getPosition() {
        return position;
    }

    protected boolean inBounds(final Position position) {
        return inBoundsPredicate.test(position);
    }
    
    protected final void rememberVisitedPosition(final Position position) {
        this.visitedPositions.add(position);
    }
    
    protected void translate(final Vector heading, final Integer amount) {
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
    
    public List<Position> getVisitedPositions(final boolean includeStartPosition) {
        if (includeStartPosition) {
            return this.visitedPositions;
        } else {
            return this.visitedPositions.subList(1, this.visitedPositions.size());
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Navigation [position=").append(position).append("]");
        return builder.toString();
    }
}
