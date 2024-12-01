import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.game_of_life.GameOfLife;
import com.github.pareronia.aoc.game_of_life.InfiniteGrid;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_17 extends SolutionBase<CharGrid, Integer, Integer> {
    
    private static final char ON = '#';
    private static final char OFF = '.';
    private static final int GENERATIONS = 6;
    
	private AoC2020_17(final boolean debug) {
		super(debug);
	}
    
    public static AoC2020_17 create() {
        return new AoC2020_17(false);
    }

    public static AoC2020_17 createDebug() {
        return new AoC2020_17(true);
    }
    
    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(inputs);
    }

    @Override
    public Integer solvePart1(final CharGrid grid) {
        return solve(grid, this::create3DCell);
    }

    @Override
    public Integer solvePart2(final CharGrid grid) {
        return solve(grid, this::create4DCell);
    }
	
	private List<Integer> create3DCell(final Cell cell) {
	    return List.of(0, cell.getRow(), cell.getCol());
	}

	private List<Integer> create4DCell(final Cell cell) {
	    return List.of(0, 0, cell.getRow(), cell.getCol());
	}
	
    @SuppressWarnings("unchecked")
	private int solve(
	        final CharGrid grid,
	        final Function<Cell, List<Integer>> cellFactory
	) {
	    final Set<List<Integer>> on = grid.getAllEqualTo(ON)
	            .map(cell -> cellFactory.apply(cell))
	            .collect(toSet());
        GameOfLife<List<Integer>> gol = new GameOfLife<>(
                        new InfiniteGrid(), GameOfLife.classicRules, on);

        for (int i = 0; i < GENERATIONS; i++) {
            gol = gol.nextGeneration();
        }
        return gol.alive().size();
	}

	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "112"),
	    @Sample(method = "part2", input = TEST, expected = "848"),
	})
	public static void main(final String[] args) throws Exception {
		AoC2020_17.create().run();
	}
	
	private static final String TEST = """
	        .#.\r
	        ..#\r
	        ###
	        """;
	
	@SuppressWarnings("unused")
    private static final class Printer {

	    public static void print3D(final boolean debug, final Set<List<Integer>> space) {
	        if (!debug) {
	            return;
	        }
	        final Map<Integer, Set<List<Integer>>> layers = space.stream()
	            .collect(groupingBy(l -> l.get(0),
	                     mapping(l -> List.of(l.get(1), l.get(2)), toSet())));
	        final List<String> ans = new ArrayList<>();
	        layers.keySet().stream()
	            .sorted()
	            .forEach(l -> {
	                ans.add(String.format("z=%d", l));
	                final IntSummaryStatistics rowStats = layers.get(l).stream()
	                        .mapToInt(rc -> rc.get(0))
	                        .summaryStatistics();
	                final int rowMin = rowStats.getMin();
	                final int rowMax = rowStats.getMax();
	                final IntSummaryStatistics colStats = layers.get(l).stream()
	                        .mapToInt(rc -> rc.get(1))
	                        .summaryStatistics();
	                final int colMin = colStats.getMin();
	                final int colMax = colStats.getMax();
	                for (int rr = rowMin; rr <= rowMax; rr++) {
	                    final StringBuilder sb = new StringBuilder();
	                    for (int cc = colMin; cc <= colMax; cc++) {
	                        sb.append(layers.get(l).contains(List.of(rr, cc)) ? ON : OFF);
	                    }
	                    ans.add(sb.toString());
	                }
	                ans.add("");
	            });
	        ans.stream().forEach(System.out::println);
	    }
	}
}