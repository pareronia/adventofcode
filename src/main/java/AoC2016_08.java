import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aocd.Puzzle;

public class AoC2016_08 extends AoCBase {

    private static final char ON = '#';
	private static final char OFF = '.';
    
    private final List<String> inputs;
	
	private AoC2016_08(final List<String> inputs, final boolean debug) {
		super(debug);
		this.inputs = inputs;
	}
	
	public static final AoC2016_08 create(final List<String> input) {
		return new AoC2016_08(input, false);
	}

	public static final AoC2016_08 createDebug(final List<String> input) {
		return new AoC2016_08(input, true);
	}
	
    private Grid createGrid(final Integer rows, final Integer columns) {
        return Grid.from(Stream.iterate(0, i -> i++)
                .limit(rows)
	            .map(i -> StringUtils.repeat(OFF, columns))
	            .collect(toList()));
    }
	
	private Grid solve(final Integer rows, final Integer columns) {
	    Grid grid = createGrid(rows, columns);
	    for (final String input : this.inputs) {
            if (input.startsWith("rect ")) {
                final String[] coords = input.substring("rect ".length()).split("x");
                final Set<Cell> cells = new HashSet<>();
                for (int r = 0; r < Integer.valueOf(coords[1]); r++) {
                    for (int c = 0; c < Integer.valueOf(coords[0]); c++) {
                        cells.add(Cell.at(r, c));
                    }
                }
                grid = grid.update(cells, ON);
            } else if (input.startsWith("rotate row ")) {
                final String[] coords = input.substring("rotate row ".length()).split(" by ");
                final Integer row = Integer.valueOf(coords[0].substring("y=".length()));
                final Integer amount = Integer.valueOf(coords[1]);
                grid = grid.rollRow(row, amount);
            } else if (input.startsWith("rotate column ")) {
                final String[] coords = input.substring("rotate column ".length()).split(" by ");
                final Integer column = Integer.valueOf(coords[0].substring("x=".length()));
                final Integer amount = Integer.valueOf(coords[1]);
                grid = grid.rollColumn(column, amount);
            } else {
                throw new IllegalArgumentException("Invalid input");
            }
            log("");
            log(grid);
        }
	    return grid;
	}

	@Override
	public Integer solvePart1() {
	    return (int) solve(6, 50).countAllEqualTo(ON);
	}
	
	@Override
	public String solvePart2() {
	    return OCR.convert6(solve(6, 50), ON, OFF);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2016_08.createDebug(TEST).solve(3, 7).countAllEqualTo(ON) == 6;
		
		final Puzzle puzzle = Puzzle.create(2016, 8);
		final List<String> input = puzzle.getInputData();
		puzzle.check(
		    () -> lap("Part 1", AoC2016_08.create(input)::solvePart1),
		    () -> lap("Part 2", AoC2016_08.create(input)::solvePart2)
		);
	}

	private static final List<String> TEST = splitLines(
	        "rect 3x2\r\n" +
	        "rotate column x=1 by 1\r\n" +
	        "rotate row y=0 by 4\r\n" +
	        "rotate column x=1 by 1"
	);
}
