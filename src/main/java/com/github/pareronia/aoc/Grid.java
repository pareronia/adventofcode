package com.github.pareronia.aoc;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import lombok.Value;

public class Grid {
	private final char[][] cells;
	
	private Grid(char[][] cells) {
		this.cells = cells;
	}
	
	public static Grid from(List<String> strings) {
		final char[][] cells = new char[strings.size()][strings.get(0).length()];
		for (int i = 0; i < strings.size(); i++) {
			cells[i] = strings.get(i).toCharArray();
		}
		return new Grid(cells);
	}
	
	public Integer getHeight() {
		return this.cells.length;
	}
	
	public Integer getWidth() {
		return this.cells[0].length;
	}
	
	public Stream<Cell> getAllEqualTo(char ch) {
		final Builder<Cell> builder = Stream.builder();
		for (int row = 0; row < cells.length; row++) {
			final char[] cs = cells[row];
			for (int col = 0; col < cs.length; col++) {
				if (cs[col] == ch) {
					builder.add(Cell.at(row, col));
				}
			}
		}
		return builder.build();
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
