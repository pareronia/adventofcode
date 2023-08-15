package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.AssertUtils.assertNotNull;
import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IntegerSequence.Range.rangeClosed;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import java.util.stream.StreamSupport;

import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.geometry.Direction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public interface Grid<T> {

    int getHeight();
    
    int getWidth();

    T getValue(final Cell c);

    void setValue(final Cell c, final T value);
    
    String getRowAsString(final int row);
    
    default int size() {
        return this.getHeight() * this.getWidth();
    }
    
    default boolean isSquare() {
        return getWidth() == getHeight();
    }
    
    default int getMaxRowIndex() {
        return getHeight() - 1;
    }
    
    default int getMaxColIndex() {
        return getWidth() - 1;
    }

    default void validateIsSquare() {
        if (!this.isSquare()) {
            throw new UnsupportedOperationException("Grid should be square.");
        }
    }

    default void validateRowIndex(final int row) {
        assertNotNull(row, () -> "row index should not be null");
        assertTrue(0 <= row && row <= getMaxRowIndex(),
                () -> "Invalid row index: " + row);
    }

    default void validateColumnIndex(final int col) {
        assertNotNull(col, () -> "column index should not be null");
        assertTrue(0 <= col && col <= getMaxColIndex(),
                () -> "Invalid column index: " + col);
    }

    default Range rowIndices() {
        return range(getHeight());
    }
    
    default Range rowIndicesReversed() {
        return rangeClosed(getHeight() - 1, 0, -1);
    }
    
    default Range colIndices() {
        return range(getWidth());
    }
    
    default Range colIndicesReversed() {
        return rangeClosed(getWidth() - 1, 0, -1);
    }

    default Stream<Cell> findAllMatching(final Predicate<T> test) {
        final Builder<Cell> builder = Stream.builder();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                if (test.test(getValue(Cell.at(row, col)))) {
                    builder.add(Cell.at(row, col));
                }
            }
        }
        return builder.build();
    }
    
    default Stream<Cell> getAllEqualTo(final T i) {
        return findAllMatching(c -> c == i);
    }
    
    default long countAllEqualTo(final T i) {
        return getAllEqualTo(i).count();
    }
    
    default Stream<Cell> getCells() {
        final Builder<Cell> builder = Stream.builder();
        for (int r = 0; r < this.getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                builder.add(Cell.at(r, c));
            }
        }
        return builder.build();
    }

    default Stream<Cell> getCellsN(final Cell cell) {
        return IntStream.iterate(cell.getRow() - 1, i -> i >= 0, i -> i - 1)
                .mapToObj(i -> Cell.at(i, cell.getCol()));
    }
    
    default Stream<Cell> getCellsNE(final Cell cell) {
        final int cntRight = this.getWidth() - 1 - cell.getCol();
        final int cntUp = cell.getRow();
        return IntStream.iterate(1, i -> i <= Math.min(cntRight, cntUp), i -> i + 1)
                .mapToObj(i -> Cell.at(cell.getRow() - i, cell.getCol() + i));
    }

	default Stream<Cell> getCellsE(final Cell cell) {
	    return IntStream.iterate(cell.getCol() + 1, i -> i < this.getWidth(), i -> i + 1)
	            .mapToObj(i -> Cell.at(cell.getRow(), i));
	}
    
    default Stream<Cell> getCellsSE(final Cell cell) {
        final int cntRight = this.getWidth() - 1 - cell.getCol();
        final int cntDown = this.getHeight() - 1 - cell.getRow();
        return IntStream.iterate(1, i -> i <= Math.min(cntRight, cntDown), i -> i + 1)
                .mapToObj(i -> Cell.at(cell.getRow() + i, cell.getCol() + i));
    }
    
    default Stream<Cell> getCellsS(final Cell cell) {
	    return IntStream.iterate(cell.getRow() + 1, i -> i < this.getHeight(), i -> i + 1)
	            .mapToObj(i -> Cell.at(i, cell.getCol()));
	}

    default Stream<Cell> getCellsSW(final Cell cell) {
        final int cntLeft = cell.getCol();
        final int cntDown = this.getHeight() - 1 - cell.getRow();
        return IntStream.iterate(1, i -> i <= Math.min(cntLeft, cntDown), i -> i + 1)
                .mapToObj(i -> Cell.at(cell.getRow() + i, cell.getCol() - i));
    }
	
	default Stream<Cell> getCellsW(final Cell cell) {
	    return IntStream.iterate(cell.getCol() - 1, i -> i >= 0, i -> i - 1)
	            .mapToObj(i -> Cell.at(cell.getRow(), i));
	}
    
    default Stream<Cell> getCellsNW(final Cell cell) {
        final int cntLeft = cell.getCol();
        final int cntUp = cell.getRow();
        return IntStream.iterate(1, i -> i <= Math.min(cntLeft, cntUp), i -> i + 1)
                .mapToObj(i -> Cell.at(cell.getRow() - i, cell.getCol() - i));
    }

    default Stream<Cell> getCapitalNeighbours(final Cell cell) {
        return cell.capitalNeighbours()
	            .filter(n -> n.row >= 0 && n.row < this.getHeight())
	            .filter(n -> n.col >= 0 && n.col < this.getWidth());
    }

    default Stream<Cell> getAllNeighbours(final Cell cell) {
        return cell.allNeighbours()
	            .filter(n -> n.row >= 0 && n.row < this.getHeight())
	            .filter(n -> n.col >= 0 && n.col < this.getWidth());
    }
    
    default Iterable<String> getRowsAsStrings() {
        return () -> new Iterator<>() {
            int i = 0;
            
            @Override
            public boolean hasNext() {
                return i <= Grid.this.getMaxRowIndex();
            }

            @Override
            public String next() {
                return Grid.this.getRowAsString(i++);
            }
        };
    }
    
    default List<String> getRowsAsStringList() {
        return StreamSupport.stream(getRowsAsStrings().spliterator(), false)
                .collect(toList());
    }
	
    default String asString() {
	    return Utils.stream(getRowsAsStrings().iterator())
	            .collect(joining(System.lineSeparator()));
	}

    @RequiredArgsConstructor(staticName = "at")
    @Getter
    @EqualsAndHashCode
    @ToString
	public static final class Cell {
		private final int row;
		private final int col;
		
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
