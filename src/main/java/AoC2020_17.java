import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.lang3.Range;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

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
	    return List.of(0, row, col);
	}

	private List<Integer> create4D(final int row, final int col) {
	    return List.of(0, 0, row, col);
	}
	
	private Space parseSpace(
	        final BiFunction<Integer, Integer, List<Integer>> factory
	) {
	    final Grid grid = new Grid(this.input);
	    final Map<Space.Dimension, Range<Integer>> ranges = new HashMap<>();
	    ranges.put(Space.Dimension.ROW, Range.between(0, grid.getMaxRowIndex()));
	    ranges.put(Space.Dimension.COL, Range.between(0, grid.getMaxColIndex()));
	    ranges.put(Space.Dimension.LAYER, Range.between(0, 0));
	    ranges.put(Space.Dimension.WTF, Range.between(0, 0));
	    final Set<List<Integer>> on = grid.getAllEqualTo(ON)
	        .map(cell -> factory.apply(cell.getRow(), cell.getCol()))
	        .collect(toSet());
	    return new Space(ranges, on);
	}
	
    @Override
	public Integer solvePart1() {
        final Space space = parseSpace(this::create3D);
        for (int i = 0; i < GENERATIONS; i++) {
            space.nextGeneration(Space.DIM3);
        }
        return space.alive.size();
	}

	@Override
	public Integer solvePart2() {
        final Space space = parseSpace(this::create4D);
        for (int i = 0; i < GENERATIONS; i++) {
            space.nextGeneration(Space.DIM4);
        }
        return space.alive.size();
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
	
	@RequiredArgsConstructor
	private static final class Space {
	    public enum Dimension { ROW, COL, LAYER, WTF }

	    public static final List<Dimension> DIM3 = List.of(
	            Space.Dimension.LAYER,
	            Space.Dimension.ROW,
	            Space.Dimension.COL);
	    
	    public static final List<Dimension> DIM4 = List.of(
	            Space.Dimension.WTF,
	            Space.Dimension.LAYER,
	            Space.Dimension.ROW,
	            Space.Dimension.COL);
	    
	    private final Map<Dimension, Range<Integer>> ranges;
	    private final Set<List<Integer>> alive;
	    private final Map<List<Integer>, Set<List<Integer>>> neighboursCache = new HashMap<>();
	    
	    public void nextGeneration(final List<Dimension> dimensions) {
	        final Set<List<Integer>> newAlive = new HashSet<>();
	        expand();
	        for (final List<Integer> cell : cells(dimensions)) {
	            final long cnt = neighbours(cell).stream()
	                    .filter(this.alive::contains)
	                    .count();
	            if (this.alive.contains(cell) && (cnt == 2 || cnt == 3)) {
	                newAlive.add(cell);
	            }
	            if (!this.alive.contains(cell) && cnt == 3) {
	                newAlive.add(cell);
	            }
	        }
	        this.alive.clear();
	        this.alive.addAll(newAlive);
	    }
	    
	    private void expand() {
	        for (final Dimension dimension : ranges.keySet()) {
	            final Range<Integer> range = ranges.get(dimension);
	            final Range<Integer> newRange = Range.between(
	                range.getMinimum() - 1,
	                range.getMaximum() + 1);
	            ranges.put(dimension, newRange);
	        }
	    }
	    
	    private Set<List<Integer>> cells(final List<Dimension> dimensions) {
	        final List<Dimension> order = new ArrayList<>(dimensions);
            Collections.reverse(order);
	        final List<Range<Integer>> rngs = order.stream()
	                .map(this.ranges::get)
	                .collect(toList());
	        return product(rngs);
	    }
	    
	    private Set<List<Integer>> neighbours(final List<Integer> cell) {
	        return this.neighboursCache.computeIfAbsent(cell, this::getNeighbours);
	    }
	    
	    private Set<List<Integer>> getNeighbours(final List<Integer> cell) {
	        final List<Range<Integer>> rngs = new ArrayList<>();
	        final List<Integer> zeroes = new ArrayList<>();
	        for (int i = 0; i < cell.size(); i++) {
                rngs.add(Range.between(-1, 1));
                zeroes.add(0);
            }
	        final Set<List<Integer>> ans = product(rngs);
	        ans.remove(zeroes);
	        for (final List<Integer> a : ans) {
	            for (int i = 0; i < a.size(); i++) {
                    a.set(i, cell.get(i) + a.get(i));
                }
            }
	        return ans;
	    }

        private Set<List<Integer>> product(final List<Range<Integer>> rngs) {
            Set<List<Integer>> ans = new HashSet<>(Set.of(List.of()));
	        for (final Range<Integer> range : rngs) {
	            final Set<List<Integer>> set = new HashSet<>();
	            for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
                    for (final List<Integer> tmp : ans) {
                        final List<Integer> lst = new ArrayList<>();
                        lst.add(i);
                        lst.addAll(tmp);
                        set.add(lst);
                    }
                }
	            ans = set;
	        }
	        return ans;
        }
    }
	
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