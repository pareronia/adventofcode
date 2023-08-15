package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.AssertUtils.assertNotNull;
import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IntegerSequence.Range.rangeClosed;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import java.util.stream.StreamSupport;

import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.geometry.Direction;

import lombok.Value;

public class Grid {
	private final char[][] cells;
	
	public Grid(final char[][] cells) {
		this.cells = cells;
	}
	
	public Grid(final List<String> strings) {
		final char[][] cells = new char[strings.size()][strings.get(0).length()];
		for (int i = 0; i < strings.size(); i++) {
			cells[i] = strings.get(i).toCharArray();
		}
		this.cells = cells;
	}
	
	public static Grid from(final List<String> strings) {
		return new Grid(strings);
	}
	
	public static Grid from(final String string, final int width) {
		return Grid.from(
				Stream.iterate(0, i -> i + width)
					.limit(string.length() / width)
					.map(i -> string.substring(i, i + width))
					.collect(toList()));
	}
	
	public Grid addRow(final String string ) {
	    assertTrue(string.length() == getWidth(), () -> "Invalid row length.");
	    final List<String> list = new ArrayList<>(getRowsAsStringList());
	    list.add(string);
	    return Grid.from(list);
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

	public Range rowIndices() {
	    return range(getHeight());
	}
	
	public Range rowIndicesReversed() {
	    return rangeClosed(getHeight() - 1, 0, -1);
	}
	
	public Range colIndices() {
	    return range(getWidth());
	}
	
	public Range colIndicesReversed() {
        return rangeClosed(getWidth() - 1, 0, -1);
    }
	
	public char[] getRow(final Integer row) {
		validateRowIndex(row);
		return Arrays.copyOf(this.cells[row], getWidth());
	}

	public char[] getRowReversed(final Integer row) {
		validateRowIndex(row);
		final char[] column = new char[getWidth()];
		for (final int col : colIndicesReversed()) {
			column[getMaxColIndex() - col] = this.cells[row][col];
		}
		return column;
	}
	
	public char[] getColumn(final Integer col) {
		validateColumnIndex(col);
		final char[] column = new char[getHeight()];
		for (final int row : rowIndices()) {
			column[row] = this.cells[row][col];
		}
		return column;
	}
	
	public char[] getColumnReversed(final Integer col) {
		validateColumnIndex(col);
		final char[] column = new char[getHeight()];
		for (final int row : rowIndicesReversed()) {
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
	
	public Stream<Cell> getCapitalNeighbours(final Cell cell) {
	    return cell.capitalNeighbours()
	            .filter(n -> n.row >= 0 && n.row < this.getHeight())
	            .filter(n -> n.col >= 0 && n.col < this.getWidth());
	}

    public Stream<Cell> getAllNeighbours(final Cell cell) {
        return cell.allNeighbours()
	            .filter(n -> n.row >= 0 && n.row < this.getHeight())
	            .filter(n -> n.col >= 0 && n.col < this.getWidth());
    }
	
	public String getRowAsString(final Integer row) {
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
	
	public Stream<Cell> getCellsN(final Cell cell) {
	    return IntStream.iterate(cell.getRow() - 1, i -> i >= 0, i -> i - 1)
	            .mapToObj(i -> Cell.at(i, cell.getCol()));
	}
	
	public Stream<Cell> getCellsNE(final Cell cell) {
	    final int cntRight = this.getWidth() - 1 - cell.getCol();
	    final int cntUp = cell.getRow();
	    return IntStream.iterate(1, i -> i <= Math.min(cntRight, cntUp), i -> i + 1)
	            .mapToObj(i -> Cell.at(cell.getRow() - i, cell.getCol() + i));
	}
	
	public Stream<Cell> getCellsE(final Cell cell) {
	    return IntStream.iterate(cell.getCol() + 1, i -> i < this.getWidth(), i -> i + 1)
	            .mapToObj(i -> Cell.at(cell.getRow(), i));
	}
	
	public Stream<Cell> getCellsSE(final Cell cell) {
	    final int cntRight = this.getWidth() - 1 - cell.getCol();
	    final int cntDown = this.getHeight() - 1 - cell.getRow();
	    return IntStream.iterate(1, i -> i <= Math.min(cntRight, cntDown), i -> i + 1)
	            .mapToObj(i -> Cell.at(cell.getRow() + i, cell.getCol() + i));
	}
	
	public Stream<Cell> getCellsS(final Cell cell) {
	    return IntStream.iterate(cell.getRow() + 1, i -> i < this.getHeight(), i -> i + 1)
	            .mapToObj(i -> Cell.at(i, cell.getCol()));
	}
	
	public Stream<Cell> getCellsSW(final Cell cell) {
	    final int cntLeft = cell.getCol();
	    final int cntDown = this.getHeight() - 1 - cell.getRow();
	    return IntStream.iterate(1, i -> i <= Math.min(cntLeft, cntDown), i -> i + 1)
	            .mapToObj(i -> Cell.at(cell.getRow() + i, cell.getCol() - i));
	}
	
	public Stream<Cell> getCellsW(final Cell cell) {
	    return IntStream.iterate(cell.getCol() - 1, i -> i >= 0, i -> i - 1)
	            .mapToObj(i -> Cell.at(cell.getRow(), i));
	}
	
	public Stream<Cell> getCellsNW(final Cell cell) {
	    final int cntLeft = cell.getCol();
	    final int cntUp = cell.getRow();
	    return IntStream.iterate(1, i -> i <= Math.min(cntLeft, cntUp), i -> i + 1)
	            .mapToObj(i -> Cell.at(cell.getRow() - i, cell.getCol() - i));
	}
	
	public Stream<Cell> findAllMatching(final Predicate<Character> test) {
		final Builder<Cell> builder = Stream.builder();
		for (final int row : rowIndices()) {
			final char[] cs = cells[row];
			for (final int col : colIndices()) {
				if (test.test(cs[col])) {
					builder.add(Cell.at(row, col));
				}
			}
		}
		return builder.build();
	}
    
    public Stream<Cell> getAllEqualTo(final char ch) {
        return findAllMatching(c -> c == ch);
    }
	
	public long countAllEqualTo(final char ch) {
		return getAllEqualTo(ch).count();
	}
	
	public Grid replace(final char ch1, final char ch2) {
		final char[][] cells = new char[getHeight()][getWidth()];
		for (final int row : rowIndices()) {
			for (final int col : colIndices()) {
				cells[row][col] = this.cells[row][col] == ch1 ? ch2 : this.cells[row][col];
			}
		}
		return new Grid(cells);
	}
	
	public Grid update(final Set<Cell> toUpdate, final char ch) {
		final char[][] cells = new char[getHeight()][getWidth()];
		for (final int row : rowIndices()) {
			for (final int col : colIndices()) {
			    if (toUpdate.contains(Cell.at(row, col))) {
			        cells[row][col] = ch;
			    } else {
			        cells[row][col] = this.cells[row][col];
			    }
			}
		}
		return new Grid(cells);
	}
	
	public char getValueAt(final Cell cell) {
	    return this.cells[cell.getRow()][cell.getCol()];
	}
	
	public Grid subGrid(final Cell from, final Cell to) {
	    assertNotNull(from, () -> "from should not be null");
	    assertNotNull(to, () -> "from should not be null");
		assertTrue(from.row <= to.row && from.col <= to.col,
		        () -> "Invalid coordinates");
		
		final int endRow = Math.min(to.row, this.getHeight());
		final int endCol = Math.min(to.col, this.getWidth());
		final char[][] cells = new char[endRow - from.row][endCol - from.col];
		for (int row = from.row, i = 0; row < endRow && i < cells.length; row++, i++) {
			for (int col = from.col, j = 0; col < endCol && j < cells[0].length; col++, j++) {
				final char temp = this.cells[row][col];
                cells[i][j] = temp;
			}
		}
		return new Grid(cells);
	}
	
	public Grid[][] divide(final int partSize) {
	    validateIsSquare();
        final int parts = this.getHeight() / partSize;
        final Grid[][] subGrids = new Grid[parts][parts];
        for (final int r : range(parts)) {
            for (final int c : range(parts)) {
                subGrids[r][c] = this.subGrid(
                        Cell.at(r * partSize, c * partSize),
                        Cell.at((r + 1) * partSize, (c + 1) * partSize));
            }
        }
        return subGrids;
    }
	
	public static Grid merge(final Grid[][] grids) {
	    assertTrue(Arrays.stream(grids)
	            .flatMap(Arrays::stream)
	            .mapToInt(Grid::getHeight)
	            .distinct().count() == 1
	        &&
	        Arrays.stream(grids)
	            .flatMap(Arrays::stream)
	            .mapToInt(Grid::getWidth)
	            .distinct().count() == 1,
	        () -> "Grids should be same size");
        final List<String> strings = new ArrayList<>();
        for (final int r : range(grids.length)) {
            final List<List<String>> rowsList = new ArrayList<>();
            for (final int c : range(grids[r].length)) {
                rowsList.add(grids[r][c].getRowsAsStringList());
            }
            final MutableInt n = new MutableInt();
            for (int j = 0; j < rowsList.get(0).size(); j++) {
                strings.add(rowsList.stream()
                    .map(l -> l.get(n.getValue()))
                    .collect(joining()));
                n.increment();
            }
        }
        return Grid.from(strings);
    }
	
	public Grid getWithEdgesRemoved() {
		final char[][] cells = new char[getHeight() - 2][];
		for (final int row : range(1, getMaxRowIndex(), 1)) {
			cells[row - 1] = Arrays.copyOfRange(this.cells[row], 1, getWidth() - 1);
		}
		return new Grid(cells);
	}
	
	private char[] rollCharArray(final char[] oldChars, final Integer amount) {
	    final char[] newChars = new char[oldChars.length];
	    for (final int col : range(oldChars.length)) {
	        newChars[col] = oldChars[Math.floorMod(col - amount, oldChars.length)];
	    }
	    return newChars;
	}
	
	public Grid rollColumn(final Integer colIndex, final Integer amount) {
	    validateColumnIndex(colIndex);
	    final char[] newCol = rollCharArray(getColumn(colIndex), amount);
		final char[][] cells = new char[getHeight()][getWidth()];
		for (final int row : rowIndices()) {
			for (final int col : colIndices()) {
			    if (col == colIndex) {
			        cells[row][col] = newCol[row];
			    } else {
			        cells[row][col] = this.cells[row][col];
			    }
			}
		}
		return new Grid(cells);
	}
	
	public Grid rollRow(final Integer rowIndex, final Integer amount) {
	    validateRowIndex(rowIndex);
		final char[][] cells = new char[getHeight()][];
		for (final int row : rowIndices()) {
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
		for (final int col : colIndices()) {
			final char[] newRow = new char[getHeight()];
			for (final int row : rowIndicesReversed()) {
				newRow[getMaxRowIndex() - row] = this.cells[row][col];
			}
			cells[col] = newRow;
		}
		return new Grid(cells);
	}
	
	public Grid flipHorizontally() {
		final char[][] cells = new char[getWidth()][];
		for (final int row : rowIndicesReversed()) {
			cells[getMaxRowIndex() - row] = this.cells[row];
		}
		return new Grid(cells);
	}
	
	/**
	 * <a href="https://solitaryroad.com/c308.html">The Dihedral Group of the Square</a>
	 */
	public Iterator<Grid> getPermutations() {
		validateIsSquare();
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
	    return Utils.stream(getRowsAsStrings().iterator())
	            .collect(joining(System.lineSeparator()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(cells);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
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

	private void validateRowIndex(final Integer row) {
		assertNotNull(row, () -> "row index should not be null");
		assertTrue(0 <= row && row <= getMaxRowIndex(),
		        () -> "Invalid row index: " + row);
	}

	private void validateColumnIndex(final Integer col) {
	    assertNotNull(col, () -> "column index should not be null");
		assertTrue(0 <= col && col <= getMaxColIndex(),
		        () -> "Invalid column index: " + col);
	}

    private void validateIsSquare() {
        if (!this.isSquare()) {
            throw new UnsupportedOperationException("Grid should be square.");
        }
    }
	
	@Value
	public static final class Cell {
		private final Integer row;
		private final Integer col;
		
		public static Cell at(final Integer row, final Integer col) {
			return new Cell(row, col);
		}
		
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
