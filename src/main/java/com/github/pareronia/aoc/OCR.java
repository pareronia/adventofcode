package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid.Cell;

public class OCR {

	private static final CharGrid A6 = CharGrid.from(asList(".##.",
													"#..#",
													"#..#",
													"####",
													"#..#",
													"#..#"));

	private static final CharGrid B6 = CharGrid.from(asList("###.",
													"#..#",
													"###.",
													"#..#",
													"#..#",
													"###."));

	private static final CharGrid C6 = CharGrid.from(asList(".##.",
													"#..#",
													"#...",
													"#...",
													"#..#",
													".##."));

	private static final CharGrid E6 = CharGrid.from(asList("####",
													"#...",
													"###.",
													"#...",
													"#...",
													"####"));

	private static final CharGrid F6 = CharGrid.from(asList("####",
													"#...",
													"###.",
													"#...",
													"#...",
													"#..."));

	private static final CharGrid G6 = CharGrid.from(asList(".##.",
													"#..#",
													"#...",
													"#.##",
													"#..#",
													".###"));
	
	private static final CharGrid H6 = CharGrid.from(asList("#..#",
													"#..#",
													"####",
													"#..#",
													"#..#",
													"#..#"));
	
	private static final CharGrid I6 = CharGrid.from(asList(".###",
													"..#.",
													"..#.",
													"..#.",
													"..#.",
													".###"));
	
	private static final CharGrid J6 = CharGrid.from(asList("..##",
													"...#",
													"...#",
													"...#",
													"#..#",
													".##."));
	
	private static final CharGrid K6 = CharGrid.from(asList("#..#",
													"#.#.",
													"##..",
													"#.#.",
													"#.#.",
													"#..#"));
	
	private static final CharGrid L6 = CharGrid.from(asList("#...",
													"#...",
													"#...",
													"#...",
													"#...",
													"####"));
	
	private static final CharGrid O6 = CharGrid.from(asList(".##.",
													"#..#",
													"#..#",
													"#..#",
													"#..#",
													".##."));
	
	private static final CharGrid P6 = CharGrid.from(asList("###.",
													"#..#",
													"#..#",
													"###.",
													"#...",
													"#..."));
	
	private static final CharGrid R6 = CharGrid.from(asList("###.",
													"#..#",
													"#..#",
													"###.",
													"#.#.",
													"#..#"));
	
	private static final CharGrid S6 = CharGrid.from(asList(".###",
													"#...",
													"#...",
													".##.",
													"...#",
													"###."));
	
	private static final CharGrid U6 = CharGrid.from(asList("#..#",
													"#..#",
													"#..#",
													"#..#",
													"#..#",
													".##."));
	
	private static final CharGrid Y6 = CharGrid.from(asList("#...",
													"#...",
													".#.#",
													"..#.",
													"..#.",
													"..#."));
	
	private static final CharGrid Z6 = CharGrid.from(asList("####",
													"...#",
													"..#.",
													".#..",
													"#...",
													"####"));
	
	@SuppressWarnings("serial")
	private static final Map<CharGrid, Character> glyphs = new HashMap<CharGrid, Character>() {
		{
			put(A6, 'A');
			put(B6, 'B');
			put(C6, 'C');
			put(E6, 'E');
			put(F6, 'F');
			put(G6, 'G');
			put(H6, 'H');
			put(I6, 'I');
			put(J6, 'J');
			put(K6, 'K');
			put(L6, 'L');
			put(O6, 'O');
			put(P6, 'P');
			put(R6, 'R');
			put(S6, 'S');
			put(U6, 'U');
			put(Y6, 'Y');
			put(Z6, 'Z');
		}

		@Override
		public Character get(Object grid) {
			if (!containsKey(grid)) {
				throw new NoSuchElementException(
						"OCR doesn't understand " + System.lineSeparator() + grid);
			}
			return super.get(grid);
		}
	};
	
	public static String convert6(CharGrid grid, Character fillChar, Character emptyChar) {
		return Stream.iterate(0,  i -> i + 5).limit(grid.getWidth() / 5)
				.map(i -> grid.subGrid(Cell.at(0, i), Cell.at(grid.getHeight(), i + 4)))
				.map(g -> g.replace(fillChar, '#'))
				.map(g -> g.replace(emptyChar, '.'))
				.map(glyphs::get)
				.collect(toAString());
	}
}