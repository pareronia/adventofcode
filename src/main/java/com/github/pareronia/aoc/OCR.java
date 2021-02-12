package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid.Cell;

public class OCR {

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
	
	private static final Grid J6 = Grid.from(asList("..##",
													"...#",
													"...#",
													"...#",
													"#..#",
													".##."));
	
	private static final Grid V6 = Grid.from(asList("#..#",
													"#..#",
													"#..#",
													"#..#",
													"#..#",
													".##."));
	
	private static final Grid Z6 = Grid.from(asList("####",
													"...#",
													"..#.",
													".#..",
													"#...",
													"####"));
	
	@SuppressWarnings("serial")
	private static final Map<Grid, Character> glyphs = new HashMap<Grid, Character>() {{
		put(F6, 'F');
		put(G6, 'G');
		put(J6, 'J');
		put(V6, 'V');
		put(Z6, 'Z');
	}};
	
	public static String convert6(Grid grid, Character fillChar, Character emptyChar) {
		return Stream.iterate(0,  i -> i + 5).limit(grid.getWidth() / 5)
				.map(i -> grid.subGrid(Cell.at(0, i), Cell.at(grid.getHeight(), i + 4)))
				.map(g -> g.replace(fillChar, '#'))
				.map(g -> g.replace(emptyChar, '.'))
				.map(glyphs::get)
				.collect(toAString());
	}
}