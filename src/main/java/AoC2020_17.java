import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.game_of_life.GameOfLife;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_17 extends AoCBase {
    
    private static final char ON = '#';
    private static final char OFF = '.';
    private static final int GENERATIONS = 6;
    
    private final List<String> input;
    
	private AoC2020_17(final List<String> input, final boolean debug) {
		super(debug);
		this.input = input;
	}
    
    public static AoC2020_17 create(final List<String> input) {
        return new AoC2020_17(input, false);
    }

    public static AoC2020_17 createDebug(final List<String> input) {
        return new AoC2020_17(input, true);
    }
	
	private List<Integer> create3DCell(final int row, final int col) {
	    return List.of(0, row, col);
	}

	private List<Integer> create4DCell(final int row, final int col) {
	    return List.of(0, 0, row, col);
	}
	
	private GameOfLife parse(
	        final BiFunction<Integer, Integer, List<Integer>> cellFactory
	) {
	    final Grid grid = new Grid(this.input);
	    final Set<List<Integer>> on = grid.getAllEqualTo(ON)
	        .map(cell -> cellFactory.apply(cell.getRow(), cell.getCol()))
	        .collect(toSet());
	    return new GameOfLife(on);
	}
	
	private int solve(
	        final BiFunction<Integer, Integer, List<Integer>> cellFactory
	) {
        final GameOfLife gol = parse(cellFactory);
        for (int i = 0; i < GENERATIONS; i++) {
            gol.nextGeneration(cnt -> (cnt == 2 || cnt == 3), cnt -> cnt == 3);
        }
        return gol.getAlive().size();
	}
	
    @Override
	public Integer solvePart1() {
        return solve(this::create3DCell);
	}

	@Override
	public Integer solvePart2() {
	    return solve(this::create4DCell);
	}

	public static void main(final String[] args) throws Exception {
		assert createDebug(TEST).solvePart1() == 112;
		assert createDebug(TEST).solvePart2() == 848;
		
        final Puzzle puzzle = puzzle(AoC2020_17.class);
		final List<String> input = puzzle.getInputData();
        puzzle.check(
           () -> lap("Part 1", create(input)::solvePart1),
           () -> lap("Part 2", create(input)::solvePart2)
	    );
	}
	
	private static final List<String> TEST = splitLines(
			".#.\r\n" +
			"..#\r\n" +
			"###"
	);
	
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