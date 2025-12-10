import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.StringOps.splitLines;
import static com.github.pareronia.aoc.StringOps.splitOnce;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.itertools.IterTools;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_08 extends SolutionBase<List<String>, Integer, String> {

    private static final char ON = '#';
	private static final char OFF = '.';
    
	private AoC2016_08(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2016_08 create() {
		return new AoC2016_08(false);
	}

	public static final AoC2016_08 createDebug() {
		return new AoC2016_08(true);
	}
    
    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }
	
    private CharGrid solve(
            final List<String> inputs,
            final Integer rows,
            final Integer columns
    ) {
	    CharGrid grid = CharGrid.from(
	            range(rows).intStream()
	                .mapToObj(i -> StringUtils.repeat(OFF, columns))
	                .toList());
	    for (final String input : inputs) {
            if (input.startsWith("rect ")) {
                final StringSplit coords = splitOnce(
                        input.substring("rect ".length()), "x");
                final Set<Cell> cells = IterTools.product(
                        range(Integer.parseInt(coords.right())),
                        range(Integer.parseInt(coords.left()))).stream()
                    .map(lst -> Cell.at(lst.first(), lst.second()))
                    .collect(toSet());
                grid = grid.update(cells, ON);
            } else if (input.startsWith("rotate row ")) {
                final StringSplit coords = splitOnce(
                        input.substring("rotate row ".length()), " by ");
                final int row = Integer.parseInt(coords.left().substring("y=".length()));
                final int amount = Integer.parseInt(coords.right());
                grid = grid.rollRow(row, amount);
            } else {
                final StringSplit coords = splitOnce(
                        input.substring("rotate column ".length()), " by ");
                final int column = Integer.parseInt(coords.left().substring("x=".length()));
                final int amount = Integer.parseInt(coords.right());
                grid = grid.rollColumn(column, amount);
            }
            log("");
            log(grid);
        }
	    return grid;
	}

	@Override
	public Integer solvePart1(final List<String> inputs) {
	    return (int) solve(inputs, 6, 50).countAllEqualTo(ON);
	}
	
	@Override
	public String solvePart2(final List<String> inputs) {
	    return OCR.convert6(solve(inputs, 6, 50), ON, OFF);
	}

    @Override
    public void samples() {
        final AoC2016_08 test = AoC2016_08.createDebug();
        assert test.solve(test.parseInput(splitLines(TEST)), 3, 7)
                .countAllEqualTo(ON) == 6;
    }

    public static void main(final String[] args) throws Exception {
        AoC2016_08.create().run();
	}

	private static final String TEST = """
	        rect 3x2
	        rotate column x=1 by 1
	        rotate row y=0 by 4
	        rotate column x=1 by 1
	        """;
}
