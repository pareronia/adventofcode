package com.github.pareronia.aoc;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import java.util.stream.StreamSupport;

import lombok.Value;

public class Grid {
	private final char[][] cells;
	
	public Grid(char[][] cells) {
		this.cells = cells;
	}
	
	public static Grid from(List<String> strings) {
		final char[][] cells = new char[strings.size()][strings.get(0).length()];
		for (int i = 0; i < strings.size(); i++) {
			cells[i] = strings.get(i).toCharArray();
		}
		return new Grid(cells);
	}
	
	public char[][] getCells() {
		return this.cells;
	}

	public Integer getHeight() {
		return this.cells.length;
	}
	
	public Integer getWidth() {
		return this.cells[0].length;
	}
	
	public boolean isSquare() {
		return getWidth().equals(getHeight());
	}
	
	public Integer getMaxRowIndex() {
		return getHeight() - 1;
	}
	
	public Integer getMaxColIndex() {
		return getWidth() - 1;
	}
	
	public char[] getRow(Integer row) {
		validateRowIndex(row);
		return Arrays.copyOf(this.cells[row], getWidth());
	}

	public char[] getRowReversed(Integer row) {
		validateRowIndex(row);
		final char[] column = new char[getWidth()];
		for (int col = getMaxColIndex(); col >= 0; col--) {
			column[getMaxColIndex() - col] = this.cells[row][col];
		}
		return column;
	}
	
	public char[] getColumn(Integer col) {
		validateColumnIndex(col);
		final char[] column = new char[getHeight()];
		for (int row = 0; row < getHeight(); row++) {
			column[row] = this.cells[row][col];
		}
		return column;
	}
	
	public char[] getColumnReversed(Integer col) {
		validateColumnIndex(col);
		final char[] column = new char[getHeight()];
		for (int row = getMaxRowIndex(); row >= 0; row--) {
			column[getMaxRowIndex() - row] = this.cells[row][col];
		}
		return column;
	}

	public Set<char[]> getAllEdgesReversed() {
		final Set<char[]> set = new HashSet<>();
		set.add(getTopEdgeReversed());
		set.add(getBottomEdgeReversed());
		set.add(getLeftEdgeReversed());
		set.add(getRightEdgeReversed());
		return set;
	}

	public Set<char[]> getAllEdges() {
		final Set<char[]> set = new HashSet<>();
		set.add(getTopEdge());
		set.add(getBottomEdge());
		set.add(getLeftEdge());
		set.add(getRightEdge());
		return set;
	}
	
	public String getRowAsString(Integer row) {
		return new String(getRow(row));
	}
	
	public Iterable<String> getRowsAsStrings() {
		return () -> new Iterator<String>() {
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
	
	public List<String> getRowsAsStringList() {
		return StreamSupport.stream(getRowsAsStrings().spliterator(), false)
				.collect(toList());
	}
	
	public char[] getTopEdge() {
		return getRow(0);
	}

	public char[] getTopEdgeReversed() {
		return getRowReversed(0);
	}
	
	public char[] getBottomEdge() {
		return getRow(getMaxRowIndex());
	}
	
	public char[] getBottomEdgeReversed() {
		return getRowReversed(getMaxRowIndex());
	}
	
	public char[] getLeftEdge() {
		return getColumn(0);
	}
	
	public char[] getLeftEdgeReversed() {
		return getColumnReversed(0);
	}
	
	public char[] getRightEdge() {
		return getColumn(getMaxColIndex());
	}
	
	public char[] getRightEdgeReversed() {
		return getColumnReversed(getMaxColIndex());
	}
	
	public Stream<Cell> getAllEqualTo(char ch) {
		final Builder<Cell> builder = Stream.builder();
		for (int row = 0; row < getHeight(); row++) {
			final char[] cs = cells[row];
			for (int col = 0; col < getWidth(); col++) {
				if (cs[col] == ch) {
					builder.add(Cell.at(row, col));
				}
			}
		}
		return builder.build();
	}
	
	public long countAllEqualTo(char ch) {
		return getAllEqualTo(ch).count();
	}
	
	public Grid getWithEdgesRemoved() {
		final char[][] cells = new char[getHeight() - 2][];
		for (int row = 1; row < getMaxRowIndex(); row++) {
			cells[row - 1] = Arrays.copyOfRange(this.cells[row], 1, getWidth() - 1);
		}
		return new Grid(cells);
	}
	
	public Grid rotate() {
		final char[][] cells = new char[getWidth()][];
		for (int col = 0; col < getWidth(); col++) {
			final char[] newRow = new char[getHeight()];
			for (int row = getMaxRowIndex(); row >= 0; row--) {
				newRow[getMaxRowIndex() - row] = this.cells[row][col];
			}
			cells[col] = newRow;
		}
		return new Grid(cells);
	}
	
	public Grid flipHorizontally() {
		final char[][] cells = new char[getWidth()][];
		for (int row = getMaxRowIndex(); row >= 0; row--) {
			cells[getMaxRowIndex() - row] = this.cells[row];
		}
		return new Grid(cells);
	}
	
	/**
	 * <a href="https://solitaryroad.com/c308.html">The Dihedral Group of the Square</a>
	 */
	public Iterator<Grid> getPermutations() {
		if (!this.isSquare()) {
			throw new UnsupportedOperationException("Grid should be square.");
		}
		return new Iterator<Grid>() {
			int i = 0;
			Grid grid = Grid.this;
			
			@Override
			public boolean hasNext() {
				return i < 8;
			}

			@Override
			public Grid next() {
				if (i == 4) {
					grid = grid.flipHorizontally();
				} else if (i > 0)  {
					grid = grid.rotate();
				}
				i++;
				return grid;
			}
		};

	}

	private void validateRowIndex(Integer row) {
		Objects.requireNonNull(row);
		if (row < 0 || row > getMaxRowIndex()) {
			throw new IllegalArgumentException("Invalid row index.");
		}
	}

	private void validateColumnIndex(Integer col) {
		Objects.requireNonNull(col);
		if (col < 0 || col > getMaxColIndex()) {
			throw new IllegalArgumentException("Invalid column index.");
		}
	}
	
	@Value
	public static final class Cell {
		private final Integer row;
		private final Integer col;
		
		public static Cell at(Integer row, Integer col) {
			return new Cell(row, col);
		}
	}
}
