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

import org.apache.commons.lang3.StringUtils;

import lombok.Value;

public class Grid {
	private final char[][] cells;
	
	public Grid(char[][] cells) {
		this.cells = cells;
	}
	
	public Grid(List<String> strings) {
		final char[][] cells = new char[strings.size()][strings.get(0).length()];
		for (int i = 0; i < strings.size(); i++) {
			cells[i] = strings.get(i).toCharArray();
		}
		this.cells = cells;
	}
	
	public static Grid from(List<String> strings) {
		return new Grid(strings);
	}
	
	public static Grid from(String string, int width) {
		return Grid.from(
				Stream.iterate(0, i -> i + width)
					.limit(string.length() / width)
					.map(i -> string.substring(i, i + width))
					.collect(toList()));
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
	
	public Grid replace(char ch1, char ch2) {
		final char[][] cells = new char[getHeight()][getWidth()];
		for (int row = 0; row < getHeight(); row++) {
			for (int col = 0; col < getWidth(); col++) {
				cells[row][col] = this.cells[row][col] == ch1 ? ch2 : this.cells[row][col];
			}
		}
		return new Grid(cells);
	}
	
	public Grid update(Set<Cell> toUpdate, char ch) {
		final char[][] cells = new char[getHeight()][getWidth()];
		for (int row = 0; row < getHeight(); row++) {
			for (int col = 0; col < getWidth(); col++) {
			    if (toUpdate.contains(Cell.at(row, col))) {
			        cells[row][col] = ch;
			    } else {
			        cells[row][col] = this.cells[row][col];
			    }
			}
		}
		return new Grid(cells);
	}
	
	public Grid subGrid(Cell from, Cell to) {
		Objects.requireNonNull(from);
		Objects.requireNonNull(to);
		if (from.row > to.row || from.col > to.col) {
			throw new IllegalArgumentException("Invalid coordinates");
		}
		
		final char[][] cells = new char[to.row - from.row][to.col - from.col];
		for (int row = from.row, i = 0; row < to.row && i < cells.length; row++, i++) {
			for (int col = from.col, j = 0; col < to.col && j < cells[0].length; col++, j++) {
				cells[i][j] = this.cells[row][col];
			}
		}
		return new Grid(cells);
	}
	
	public Grid getWithEdgesRemoved() {
		final char[][] cells = new char[getHeight() - 2][];
		for (int row = 1; row < getMaxRowIndex(); row++) {
			cells[row - 1] = Arrays.copyOfRange(this.cells[row], 1, getWidth() - 1);
		}
		return new Grid(cells);
	}
	
	private char[] rollCharArray(char[] oldChars, Integer amount) {
	    final char[] newChars = new char[oldChars.length];
	    for (int col = 0; col < oldChars.length; col++) {
	        newChars[col] = oldChars[Math.floorMod(col - amount, oldChars.length)];
	    }
	    return newChars;
	}
	
	public Grid rollColumn(Integer colIndex, Integer amount) {
	    validateColumnIndex(colIndex);
	    final char[] newCol = rollCharArray(getColumn(colIndex), amount);
		final char[][] cells = new char[getHeight()][getWidth()];
		for (int row = 0; row < getHeight(); row++) {
			for (int col = 0; col < getWidth(); col++) {
			    if (col == colIndex) {
			        cells[row][col] = newCol[row];
			    } else {
			        cells[row][col] = this.cells[row][col];
			    }
			}
		}
		return new Grid(cells);
	}
	
	public Grid rollRow(Integer rowIndex, Integer amount) {
	    validateRowIndex(rowIndex);
		final char[][] cells = new char[getHeight()][];
		for (int row = 0; row < getHeight(); row++) {
		    final char[] oldRow = this.cells[row];
			if (row != rowIndex) {
			    cells[row] = oldRow;
			} else {
			    cells[row] = rollCharArray(oldRow, amount);
			}
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
		return new Iterator<>() {
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

	@Override
	public String toString() {
		return StringUtils.join(getRowsAsStrings(), System.lineSeparator());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(cells);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Grid other = (Grid) obj;
		if (!Arrays.deepEquals(cells, other.cells)) {
			return false;
		}
		return true;
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
