package com.github.pareronia.aoc.navigation;

import java.util.ArrayList;
import java.util.List;

import com.github.pareronia.aoc.geometry.Position;

import lombok.Data;

@Data
public class Navigation {

    protected Position position;
    private final List<Position> visitedPositions = new ArrayList<>();
    
    public Navigation(Position position) {
        this.position = position;
        rememberVisitedPosition(position);
        
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
