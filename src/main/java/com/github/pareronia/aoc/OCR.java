package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid.Cell;

public class OCR {

	private static final Grid A6 = Grid.from(asList(".##.",
													"#..#",
													"#..#",
													"####",
													"#..#",
													"#..#"));

	private static final Grid B6 = Grid.from(asList("###.",
													"#..#",
													"###.",
													"#..#",
													"#..#",
													"###."));

	private static final Grid C6 = Grid.from(asList(".##.",
													"#..#",
													"#...",
													"#...",
													"#..#",
													".##."));

	private static final Grid E6 = Grid.from(asList("####",
													"#...",
													"###.",
													"#...",
													"#...",
													"####"));

	private static final Grid F6 = Grid.from(asList("####",
													"#...",
													"###.",
													"#...",
													"#...",
													"#..."));

	private static final Grid G6 = Grid.from(asList(".##.",
													"#..#",
													"#...",
													"#.##",
													"#..#",
													".###"));
	
	private static final Grid H6 = Grid.from(asList("#..#",
													"#..#",
													"####",
													"#..#",
													"#..#",
													"#..#"));
	
	private static final Grid I6 = Grid.from(asList(".###",
													"..#.",
													"..#.",
													"..#.",
													"..#.",
													".###"));
	
	private static final Grid J6 = Grid.from(asList("..##",
													"...#",
													"...#",
													"...#",
													"#..#",
													".##."));
	
	private static final Grid K6 = Grid.from(asList("#..#",
													"#.#.",
													"##..",
													"#.#.",
													"#.#.",
													"#..#"));
	
	private static final Grid L6 = Grid.from(asList("#...",
													"#...",
													"#...",
													"#...",
													"#...",
													"####"));
	
	private static final Grid O6 = Grid.from(asList(".##.",
													"#..#",
													"#..#",
													"#..#",
													"#..#",
													".##."));
	
	private static final Grid P6 = Grid.from(asList("###.",
													"#..#",
													"#..#",
													"###.",
													"#...",
													"#..."));
	
	private static final Grid R6 = Grid.from(asList("###.",
													"#..#",
													"#..#",
													"###.",
													"#.#.",
													"#..#"));
	
	private static final Grid S6 = Grid.from(asList(".###",
													"#...",
													"#...",
													".##.",
													"...#",
													"###."));
	
	private static final Grid U6 = Grid.from(asList("#..#",
													"#..#",
													"#..#",
													"#..#",
													"#..#",
													".##."));
	
	private static final Grid Y6 = Grid.from(asList("#...",
													"#...",
													".#.#",
													"..#.",
													"..#.",
													"..#."));
	
	private static final Grid Z6 = Grid.from(asList("####",
													"...#",
													"..#.",
													".#..",
													"#...",
													"####"));
	
	@SuppressWarnings("serial")
	private static final Map<Grid, Character> glyphs = new HashMap<Grid, Character>() {
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
	
	public static String convert6(Grid grid, Character fillChar, Character emptyChar) {
		return Stream.iterate(0,  i -> i + 5).limit(grid.getWidth() / 5)
				.map(i -> grid.subGrid(Cell.at(0, i), Cell.at(grid.getHeight(), i + 4)))
				.map(g -> g.replace(fillChar, '#'))
				.map(g -> g.replace(emptyChar, '.'))
				.map(glyphs::get)
				.collect(toAString());
	}
}