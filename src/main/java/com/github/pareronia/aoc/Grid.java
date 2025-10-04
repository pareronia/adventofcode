package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IntegerSequence.Range.rangeClosed;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.geometry.Direction;

public interface Grid<T> {
    
    Cell ORIGIN = Cell.at(0, 0);

    int getHeight();
    
    int getWidth();

    T getValue(final Cell c);

    void setValue(final Cell c, final T value);
    
    String getRowAsString(final int row);
    
    String getColumnAsString(final int col);
    
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

    default Stream<Cell> getCellsWithoutBorder() {
        final Builder<Cell> builder = Stream.builder();
        for (int r = 1; r < this.getHeight() - 1; r++) {
            for (int c = 1; c < this.getWidth() - 1; c++) {
                builder.add(new Cell(r, c));
            }
        }
        return builder.build();
    }

    default Stream<Cell> getCells(final Cell cell, final Direction dir) {
        return switch (dir) {
            case UP -> getCellsN(cell);
            case RIGHT_AND_UP -> getCellsNE(cell);
            case RIGHT -> getCellsE(cell);
            case RIGHT_AND_DOWN -> getCellsSE(cell);
            case DOWN -> getCellsS(cell);
            case LEFT_AND_DOWN -> getCellsSW(cell);
            case LEFT -> getCellsW(cell);
            case LEFT_AND_UP -> getCellsNW(cell);
            default -> throw new IllegalArgumentException();
        };
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

    default Stream<String> getColumnsAsStrings() {
        return Utils.stream(new Iterator<>() {
            int i = 0;
            
            @Override
            public boolean hasNext() {
                return i <= Grid.this.getMaxColIndex();
            }
            
            @Override
            public String next() {
                return Grid.this.getColumnAsString(i++);
            }
        });
    }
    
    default List<String> getRowsAsStringList() {
        return this.getRowsAsStrings().collect(toList());
    }
	
    default String asString() {
	    return this.getRowsAsStrings().collect(joining(System.lineSeparator()));
	}

	public static final class Cell {
		final int row;
		final int col;
		
        private Cell(final int row, final int col) {
            this.row = row;
            this.col = col;
        }

        public static Cell at(final int row, final int col) {
            return new Cell(row, col);
        }
		
		public static Cell fromString(final String string) {
		    final String[] splits = string.split(",");
		    assertTrue(splits.length == 2 && StringUtils.isNumeric(splits[0]) && StringUtils.isNumeric(splits[1]),
		            () -> "Expected string to be like '[0-9]+,[0-9]+'");
		    return Cell.at(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]));
		}
		
		public Cell at(final Direction direction) {
		    return Cell.at(
		            this.row - direction.getY(), this.col + direction.getX());
		}

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    
		public Stream<Cell> allNeighbours() {
		    return Direction.OCTANTS.stream().map(this::at);
		}
		
		public Stream<Cell> capitalNeighbours() {
		    return Direction.CAPITAL.stream().map(this::at);
		}
		
		public Direction to(final Cell other) {
		    if (this.row == other.row) {
		        if (this.col == other.col) {
		            return Direction.NONE;
		        }
		        return this.col < other.col ? Direction.RIGHT : Direction.LEFT;
		    } else if (this.col == other.col) {
		        return this.row < other.row ? Direction.DOWN : Direction.UP;
		    } else {
		        throw new UnsupportedOperationException();
		    }
		}

		public Stream<Cell> allAtManhattanDistance(final int distance) {
			return Stream.iterate(0, dr -> dr <= distance, dr -> dr + 1)
					.flatMap(dr -> {
						final int dc = distance - dr;
						return new HashSet<>(List.of(
							Cell.at(row + dr, col + dc),
							Cell.at(row + dr, col - dc),
							Cell.at(row - dr, col + dc),
							Cell.at(row - dr, col - dc)
						)).stream();
					});
		}

        @Override
        public int hashCode() {
            return Objects.hash(col, row);
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
            final Cell other = (Cell) obj;
            return col == other.col && row == other.row;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Cell [row=").append(row).append(", col=").append(col).append("]");
            return builder.toString();
        }
	}
}
