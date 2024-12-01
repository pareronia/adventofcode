package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.AssertUtils.assertNotNull;
import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CharGrid implements Grid<Character>, Cloneable {
	private final char[][] cells;
	
	public CharGrid(final char[][] cells) {
		this.cells = cells;
	}
	
	public CharGrid(final List<String> strings) {
		final char[][] cells = new char[strings.size()][strings.get(0).length()];
		for (int i = 0; i < strings.size(); i++) {
			cells[i] = strings.get(i).toCharArray();
		}
		this.cells = cells;
	}
	
	public static CharGrid from(final List<String> strings) {
		return new CharGrid(strings);
	}
	
	public static CharGrid from(final String string, final int width) {
		return CharGrid.from(
				Stream.iterate(0, i -> i + width)
					.limit(string.length() / width)
					.map(i -> string.substring(i, i + width))
					.collect(toList()));
	}
	
	@Override
    protected CharGrid clone() throws CloneNotSupportedException {
        return new CharGrid(this.cells.clone());
    }
	
	public CharGrid doClone() {
	    try {
            return this.clone();
        } catch (final CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
	}

    public CharGrid addRow(final String string ) {
	    assertTrue(string.length() == getWidth(), () -> "Invalid row length.");
	    final List<String> list = new ArrayList<>(getRowsAsStringList());
	    list.add(string);
	    return CharGrid.from(list);
	}
	
	@Override
    public int getHeight() {
		return this.cells.length;
	}
	
	@Override
    public int getWidth() {
		return this.cells[0].length;
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
	
	@Override
    public String getRowAsString(final int row) {
		return new String(getRow(row));
	}
	
	@Override
    public String getColumnAsString(final int col) {
        return new String(getColumn(col));
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
    
	public CharGrid replace(final char ch1, final char ch2) {
		final char[][] cells = new char[getHeight()][getWidth()];
		for (final int row : rowIndices()) {
			for (final int col : colIndices()) {
				cells[row][col] = this.cells[row][col] == ch1 ? ch2 : this.cells[row][col];
			}
		}
		return new CharGrid(cells);
	}
	
	public CharGrid update(final Set<Cell> toUpdate, final char ch) {
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
		return new CharGrid(cells);
	}
	
	@Override
    public Character getValue(final Cell cell) {
	    return this.cells[cell.getRow()][cell.getCol()];
	}

    @Override
    public void setValue(final Cell c, final Character value) {
        this.cells[c.getRow()][c.getCol()] = value;
    }
	
	public CharGrid subGrid(final Cell from, final Cell to) {
	    assertNotNull(from, () -> "from should not be null");
	    assertNotNull(to, () -> "from should not be null");
		assertTrue(from.getRow() <= to.getRow() && from.getCol() <= to.getCol(),
		        () -> "Invalid coordinates");
		
		final int endRow = Math.min(to.getRow(), this.getHeight());
		final int endCol = Math.min(to.getCol(), this.getWidth());
		final char[][] cells = new char[endRow - from.getRow()][endCol - from.getCol()];
		for (int row = from.getRow(), i = 0; row < endRow && i < cells.length; row++, i++) {
			for (int col = from.getCol(), j = 0; col < endCol && j < cells[0].length; col++, j++) {
				final char temp = this.cells[row][col];
                cells[i][j] = temp;
			}
		}
		return new CharGrid(cells);
	}
	
	public CharGrid[][] divide(final int partSize) {
	    validateIsSquare();
        final int parts = this.getHeight() / partSize;
        final CharGrid[][] subGrids = new CharGrid[parts][parts];
        for (final int r : range(parts)) {
            for (final int c : range(parts)) {
                subGrids[r][c] = this.subGrid(
                        Cell.at(r * partSize, c * partSize),
                        Cell.at((r + 1) * partSize, (c + 1) * partSize));
            }
        }
        return subGrids;
    }
	
	public static CharGrid merge(final CharGrid[][] grids) {
	    assertTrue(Arrays.stream(grids)
	            .flatMap(Arrays::stream)
	            .mapToInt(CharGrid::getHeight)
	            .distinct().count() == 1
	        &&
	        Arrays.stream(grids)
	            .flatMap(Arrays::stream)
	            .mapToInt(CharGrid::getWidth)
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
        return CharGrid.from(strings);
    }
	
	public CharGrid getWithEdgesRemoved() {
		final char[][] cells = new char[getHeight() - 2][];
		for (final int row : range(1, getMaxRowIndex(), 1)) {
			cells[row - 1] = Arrays.copyOfRange(this.cells[row], 1, getWidth() - 1);
		}
		return new CharGrid(cells);
	}
	
	private char[] rollCharArray(final char[] oldChars, final Integer amount) {
	    final char[] newChars = new char[oldChars.length];
	    for (final int col : range(oldChars.length)) {
	        newChars[col] = oldChars[Math.floorMod(col - amount, oldChars.length)];
	    }
	    return newChars;
	}
	
	public CharGrid rollColumn(final Integer colIndex, final Integer amount) {
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
		return new CharGrid(cells);
	}
	
	public CharGrid rollRow(final Integer rowIndex, final Integer amount) {
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
		return new CharGrid(cells);
	}
	
	public CharGrid rotate() {
		final char[][] cells = new char[getWidth()][];
		for (final int col : colIndices()) {
			final char[] newRow = new char[getHeight()];
			for (final int row : rowIndicesReversed()) {
				newRow[getMaxRowIndex() - row] = this.cells[row][col];
			}
			cells[col] = newRow;
		}
		return new CharGrid(cells);
	}
	
	public CharGrid flipHorizontally() {
		final char[][] cells = new char[getWidth()][];
		for (final int row : rowIndicesReversed()) {
			cells[getMaxRowIndex() - row] = this.cells[row];
		}
		return new CharGrid(cells);
	}
	
	/**
	 * <a href="https://solitaryroad.com/c308.html">The Dihedral Group of the Square</a>
	 */
	public Iterator<CharGrid> getPermutations() {
		validateIsSquare();
		return new Iterator<>() {
			int i = 0;
			CharGrid grid = CharGrid.this;
			
			@Override
			public boolean hasNext() {
				return i < 8;
			}

			@Override
			public CharGrid next() {
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
	    return this.asString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		final int result = 1;
		return prime * result + Arrays.deepHashCode(cells);
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
		final CharGrid other = (CharGrid) obj;
		if (!Arrays.deepEquals(cells, other.cells)) {
			return false;
		}
		return true;
	}
}
