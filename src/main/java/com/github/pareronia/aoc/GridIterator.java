package com.github.pareronia.aoc;

import java.util.Iterator;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
final class GridIterator<T> implements Iterator<Cell> {
    
    enum IterDir {
        FORWARD(null),
        UP(Direction.UP),
        RIGHT_AND_UP(Direction.RIGHT_AND_UP),
        RIGHT(Direction.RIGHT),
        RIGHT_AND_DOWN(Direction.RIGHT_AND_DOWN),
        DOWN(Direction.DOWN),
        LEFT_AND_DOWN(Direction.LEFT_AND_DOWN),
        LEFT(Direction.LEFT),
        LEFT_AND_UP(Direction.LEFT_AND_UP);
        
        private final Direction delegate;
        
        IterDir(final Direction delegate) {
            this.delegate = delegate;
        }
    }
    
    private final Grid<T> grid;
    private Cell next;
    private final GridIterator.IterDir direction;

    @Override
    public boolean hasNext() {
        if (this.next == null) {
            return false;
        }
        switch (this.direction) {
        case FORWARD:
            return true;
        default:
            final Cell next = this.next.at(this.direction.delegate);
            if (this.grid.isInBounds(next)) {
                this.next = next;
                return true;
            } else {
                this.next = null;
                return false;
            }
        }
    }

    @Override
    public Cell next() {
        final Cell prev = this.next;
        switch (this.direction) {
        case FORWARD:
            if (prev.col + 1 < this.grid.getWidth()) {
                this.next = Cell.at(prev.row, prev.col + 1);
            } else if (prev.row + 1 < this.grid.getHeight()) {
                this.next = Cell.at(prev.row + 1, 0);
            } else {
                this.next = null;
            }
            return prev;
        default:
            return this.next;
        }
    }
}