import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.collections4.SetUtils;

import com.github.pareronia.aoc.Grid;
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
	
	private List<Integer> create3D(final int row, final int col) {
	    return unmodifiableList(List.of(0, row, col));
	}

	private List<Integer> create4D(final int row, final int col) {
	    return unmodifiableList(List.of(0, 0, row, col));
	}
	
	private Set<List<Integer>> parse(
	        final BiFunction<Integer, Integer, List<Integer>> factory
	) {
	    return new Grid(this.input).getAllEqualTo(ON)
	        .map(cell -> factory.apply(cell.getRow(), cell.getCol()))
	        .collect(toSet());
	}
	
	private final Map<List<Integer>, Set<List<Integer>>> nbrs3D = new HashMap<>();

	private Set<List<Integer>> neighbours3D(final List<Integer> cube) {
	    return nbrs3D.computeIfAbsent(cube, this::getNeighbours3D);
	}
	
	private Set<List<Integer>> getNeighbours3D(final List<Integer> cube) {
	    final int layer = cube.get(0);
	    final int row = cube.get(1);
	    final int col = cube.get(2);
	    final Set<List<Integer>> neighbours = new HashSet<>();
	    for (int l = -1; l <= 1; l++) {
	        for (int r = -1; r <= 1; r++) {
	            for (int c = -1; c <= 1; c++) {
	                if (!(l == 0 && r == 0 && c == 0)) {
	                    neighbours.add(unmodifiableList(
	                            List.of(layer + l, row + r, col + c)));
	                }
	            }
	        }
	    }
	    return neighbours;
	}

	private final Map<List<Integer>, Set<List<Integer>>> nbrs4D = new HashMap<>();
	
	private Set<List<Integer>> neighbours4D(final List<Integer> cube) {
	    return nbrs4D.computeIfAbsent(cube, this::getNeighbours4D);
	}
	
	private Set<List<Integer>> getNeighbours4D(final List<Integer> cube) {
	    final int wtf = cube.get(0);
	    final int layer = cube.get(1);
	    final int row = cube.get(2);
	    final int col = cube.get(3);
	    final Set<List<Integer>> neighbours = new HashSet<>();
	    for (int w = -1; w <= 1; w++) {
            for (int l = -1; l <= 1; l++) {
                for (int r = -1; r <= 1; r++) {
                    for (int c = -1; c <= 1; c++) {
                        if (!(w == 0 && l == 0 && r == 0 && c == 0)) {
                            neighbours.add(unmodifiableList(
                                List.of(wtf + w, layer + l, row + r, col + c)));
                        }
                    }
                }
            }
	    }
	    return neighbours;
	}
	
	private Set<List<Integer>> nextGeneration(
	        final Set<List<Integer>> space,
	        final Function<List<Integer>, Set<List<Integer>>> neighbourFactory
	) {
	    final Set<List<Integer>> toOn = new HashSet<>();
	    final Set<List<Integer>> toOff = new HashSet<>();
	    for (final List<Integer> cube : space) {
	        final Set<List<Integer>> neigbours = neighbourFactory.apply(cube);
            final long nOn = neigbours.stream().filter(space::contains).count();
            if (nOn == 2 || nOn == 3) {
                toOn.add(cube);
            } else {
                toOff.add(cube);
            }
            for (final List<Integer> n : SetUtils.difference(neigbours, space)) {
                final long nnOn = neighbourFactory.apply(n)
                    .stream()
                    .filter(space::contains)
                    .count();
                if (nnOn == 3) {
                    toOn.add(n);
                }
            }
	    }
	    final Set<List<Integer>> ans = SetUtils.union(space, toOn);
	    return SetUtils.disjunction(ans, toOff);
	}

    private Integer solve(
            Set<List<Integer>> space,
            final Function<List<Integer>, Set<List<Integer>>> neighbourFactory
    ) {
        for (int i = 0; i < GENERATIONS; i++) {
            space = nextGeneration(space, neighbourFactory);
        }
        return space.size();
    }
	
    @Override
	public Integer solvePart1() {
        final Set<List<Integer>> space = parse(this::create3D);
        return solve(space, this::neighbours3D);
	}

	@Override
	public Integer solvePart2() {
        final Set<List<Integer>> space = parse(this::create4D);
        return solve(space, this::neighbours4D);
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