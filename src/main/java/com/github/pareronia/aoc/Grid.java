package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IntegerSequence.Range.rangeClosed;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.geometry.Direction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public interface Grid<T> {
    
    Cell ORIGIN = Cell.at(0, 0);

    int getHeight();
    
    int getWidth();

    T getValue(final Cell c);

    void setValue(final Cell c, final T value);
    
    String getRowAsString(final int row);
    
    default int size() {
        return this.getHeight() * this.getWidth();
    }
    
    default boolean isSquare() {
        return this.getWidth() == this.getHeight();
    }
    
    default int getMaxRowIndex() {
        return this.getHeight() - 1;
    }
    
    default int getMaxColIndex() {
        return this.getWidth() - 1;
    }

    default void validateIsSquare() {
        if (!this.isSquare()) {
            throw new UnsupportedOperationException("Grid should be square.");
        }
    }
    
    default boolean isValidRowIndex(final int row) {
        return 0 <= row && row <= this.getMaxRowIndex();
    }

    default void validateRowIndex(final int row) {
        assertTrue(this.isValidRowIndex(row),
                () -> "Invalid row index: " + row);
    }
    
    default boolean isValidColumnIndex(final int col) {
        return 0 <= col && col <= this.getMaxColIndex();
    }

    default void validateColumnIndex(final int col) {
        assertTrue(this.isValidColumnIndex(col),
                () -> "Invalid column index: " + col);
    }
    
    default boolean isInBounds(final Cell cell) {
        return this.isValidRowIndex(cell.row) && this.isValidColumnIndex(cell.col);
    }

    default Range rowIndices() {
        return range(this.getHeight());
    }
    
    default Range rowIndicesReversed() {
        return rangeClosed(this.getMaxRowIndex(), 0, -1);
    }
    
    default Range colIndices() {
        return range(this.getWidth());
    }
    
    default Range colIndicesReversed() {
        return rangeClosed(this.getMaxColIndex(), 0, -1);
    }

    default Stream<Cell> findAllMatching(final Predicate<T> test) {
        return this.getCells().filter(cell -> test.test(this.getValue(cell)));
    }
    
    default Stream<Cell> getAllEqualTo(final T i) {
        return this.findAllMatching(c -> c == i);
    }
    
    default long countAllEqualTo(final T i) {
        return this.getAllEqualTo(i).count();
    }
    
    default Stream<Cell> getCells() {
        return Utils.stream(
            new GridIterator<>(this, ORIGIN, GridIterator.IterDir.FORWARD));
    }

    default Stream<Cell> getCellsN(final Cell cell) {
        return Utils.stream(
            new GridIterator<>(this, cell, GridIterator.IterDir.UP));
    }
    
    default Stream<Cell> getCellsNE(final Cell cell) {
        return Utils.stream(
            new GridIterator<>(this, cell, GridIterator.IterDir.RIGHT_AND_UP));
    }

	default Stream<Cell> getCellsE(final Cell cell) {
        return Utils.stream(
            new GridIterator<>(this, cell, GridIterator.IterDir.RIGHT));
	}
    
    default Stream<Cell> getCellsSE(final Cell cell) {
        return Utils.stream(
            new GridIterator<>(this, cell, GridIterator.IterDir.RIGHT_AND_DOWN));
    }
    
    default Stream<Cell> getCellsS(final Cell cell) {
        return Utils.stream(
            new GridIterator<>(this, cell, GridIterator.IterDir.DOWN));
	}

    default Stream<Cell> getCellsSW(final Cell cell) {
        return Utils.stream(
            new GridIterator<>(this, cell, GridIterator.IterDir.LEFT_AND_DOWN));
    }
	
	default Stream<Cell> getCellsW(final Cell cell) {
        return Utils.stream(
            new GridIterator<>(this, cell, GridIterator.IterDir.LEFT));
	}
    
    default Stream<Cell> getCellsNW(final Cell cell) {
        return Utils.stream(
            new GridIterator<>(this, cell, GridIterator.IterDir.LEFT_AND_UP));
    }

    default Stream<Cell> getCapitalNeighbours(final Cell cell) {
        return cell.capitalNeighbours().filter(this::isInBounds);
    }

    default Stream<Cell> getAllNeighbours(final Cell cell) {
        return cell.allNeighbours().filter(this::isInBounds);
    }
    
    default Stream<String> getRowsAsStrings() {
        return Utils.stream(new Iterator<>() {
            int i = 0;
            
            @Override
            public boolean hasNext() {
                return i <= Grid.this.getMaxRowIndex();
            }

            @Override
            public String next() {
                return Grid.this.getRowAsString(i++);
            }
        });
    }
    
    default List<String> getRowsAsStringList() {
        return this.getRowsAsStrings().collect(toList());
    }
	
    default String asString() {
	    return this.getRowsAsStrings().collect(joining(System.lineSeparator()));
	}

    @RequiredArgsConstructor(staticName = "at")
    @Getter
    @EqualsAndHashCode
    @ToString
	public static final class Cell {
		final int row;
		final int col;
		
		public Cell at(final Direction direction) {
		    return Cell.at(
		            this.row - direction.getY(), this.col + direction.getX());
		}
    
		public Stream<Cell> allNeighbours() {
		    return Direction.OCTANTS.stream().map(this::at);
		}
		
		public Stream<Cell> capitalNeighbours() {
		    return Direction.CAPITAL.stream().map(this::at);
		}
	}
}
