package com.github.pareronia.aoc;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.github.pareronia.aoc.geometry.Direction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
public final class IntGrid {
    final int[][] values;
    
    public static IntGrid from(final List<String> strings) {
        final int[][] values = new int[strings.size()][strings.get(0).length()];
        strings.stream()
            .map(s -> StringOps.getDigitsPrimitive(s, s.length()))
            .collect(toList())
            .toArray(values);
        return new IntGrid(values);
    }
    
    public int getWidth() {
        assert this.values.length > 0;
        return this.values[0].length;
    }
    
    public int getHeight() {
        return this.values.length;
    }
    
    public int size() {
        return this.getHeight() * this.getWidth();
    }
	
	public Integer getMaxRowIndex() {
		return getHeight() - 1;
	}
	
	public Integer getMaxColIndex() {
		return getWidth() - 1;
	}
    
    public int getValue(final IntGrid.Cell c) {
        return this.values[c.getRow()][c.getCol()];
    }
    
    public void setValue(final IntGrid.Cell c, final int value) {
        this.values[c.getRow()][c.getCol()] = value;
    }
    
    public void increment(final IntGrid.Cell c) {
        this.values[c.getRow()][c.getCol()]++;
    }
    
    public Stream<IntGrid.Cell> getCells() {
        final Builder<IntGrid.Cell> builder = Stream.builder();
        for (int r = 0; r < this.getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                builder.add(Cell.at(r, c));
            }
        }
        return builder.build();
    }

    public Stream<Cell> getCellsN(final Cell cell) {
        return IntStream.iterate(cell.getRow() - 1, i -> i >= 0, i -> i - 1)
                .mapToObj(i -> Cell.at(i, cell.getCol()));
    }

	public Stream<Cell> getCellsE(final Cell cell) {
	    return IntStream.iterate(cell.getCol() + 1, i -> i < this.getWidth(), i -> i + 1)
	            .mapToObj(i -> Cell.at(cell.getRow(), i));
	}
	
	public Stream<Cell> getCellsS(final Cell cell) {
	    return IntStream.iterate(cell.getRow() + 1, i -> i < this.getHeight(), i -> i + 1)
	            .mapToObj(i -> Cell.at(i, cell.getCol()));
	}
	
	public Stream<Cell> getCellsW(final Cell cell) {
	    return IntStream.iterate(cell.getCol() - 1, i -> i >= 0, i -> i - 1)
	            .mapToObj(i -> Cell.at(cell.getRow(), i));
	}
	
    public Stream<Cell> findAllMatching(final Predicate<Integer> test) {
        final Builder<Cell> builder = Stream.builder();
        for (int row = 0; row < getHeight(); row++) {
            final int[] cs = this.values[row];
            for (int col = 0; col < getWidth(); col++) {
                if (test.test(cs[col])) {
                    builder.add(Cell.at(row, col));
                }
            }
        }
        return builder.build();
    }
    
    public Stream<Cell> getAllEqualTo(final int i) {
        return findAllMatching(c -> c == i);
    }
    
    public long countAllEqualTo(final int i) {
        return getAllEqualTo(i).count();
    }

    public Stream<Cell> getCapitalNeighbours(final Cell c) {
        return Direction.CAPITAL.stream()
            .filter(n -> c.getRow() + n.getX() >= 0)
            .filter(n -> c.getRow() + n.getX() < this.getHeight())
            .filter(n -> c.getCol() + n.getY() >= 0)
            .filter(n -> c.getCol() + n.getY() < this.getWidth())
            .map(n -> Cell.at(c.getRow() + n.getX(), c.getCol() + n.getY()));
    }

    public Stream<Cell> getAllNeighbours(final Cell cell) {
        return Direction.OCTANTS.stream()
                .filter(n -> cell.getRow() + n.getX() >= 0)
                .filter(n -> cell.getRow() + n.getX() < this.getHeight())
                .filter(n -> cell.getCol() + n.getY() >= 0)
                .filter(n -> cell.getCol() + n.getY() < this.getWidth())
                .map(n -> Cell.at(cell.getRow() + n.getX(), cell.getCol() + n.getY()));
    }

    @RequiredArgsConstructor(staticName = "at")
    @Getter
    @EqualsAndHashCode
    @ToString
    public static final class Cell {
        private final int row;
        private final int col;
    }
}