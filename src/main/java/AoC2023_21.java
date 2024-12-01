import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_21 extends SolutionBase<CharGrid, Long, Long> {
    
    private static final int STEPS = 26_501_365;
    
    private AoC2023_21(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_21 create() {
        return new AoC2023_21(false);
    }
    
    public static AoC2023_21 createDebug() {
        return new AoC2023_21(true);
    }
    
    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(inputs);
    }

    @Override
    public Long solvePart1(final CharGrid grid) {
        return (long) this.solve(grid, List.of(64)).get(0);
    }
    
    @Override
    public Long solvePart2(final CharGrid grid) {
        final int w = grid.getWidth();
        final int mod = STEPS % w;
        final int x = STEPS / w;
        // https://old.reddit.com/r/adventofcode/comments/18nni6t/2023_21_part_2_optimization_hint/
        final List<Integer> steps = new ArrayList<>();
        steps.add(w - mod - 2);
        range(2).intStream().map(i -> i * w + mod).forEach(steps::add);
        log(steps);
        final List<Integer> values = this.solve(grid, steps);
        log(values);
        final long a = (values.get(2) + values.get(0)) / 2 - values.get(1);
        final long b = (values.get(2) - values.get(0)) / 2;
        final long c = values.get(1);
        log("a: %d, b: %d, c: %d".formatted(a, b, c));
        return a * x * x + b * x + c;
    }
    
    private record Plot(long r, long c) {}
    
    private List<Integer> solve(final CharGrid grid, final List<Integer> steps) {
        final int w = grid.getWidth();
        Set<Plot> plots = new HashSet<>();
        plots.add(new Plot(w / 2, w / 2));
        final List<Integer> ans = new ArrayList<>();
        final int max = steps.stream().mapToInt(Integer::intValue).max().getAsInt();
        for (int i = 1; i <= max; i++) {
            final Set<Plot> newPlots = new HashSet<>();
            for (final Plot p : plots) {
                for (final Direction d : Direction.CAPITAL) {
                    final long rr = p.r() + d.getY();
                    final long cc = p.c() + d.getX();
                    final int wr = Math.floorMod(rr, w);
                    final int wc = Math.floorMod(cc, w);
                    if (0 <= wr && wr < w && 0 <= wc && wc < w
                            && grid.getValue(Cell.at(wr, wc)) != '#') {
                        newPlots.add(new Plot(rr, cc));
                    }
                }
            }
            plots = newPlots;
            if (steps.contains(i)) {
                ans.add(plots.size());
            }
        }
        return ans;
    }
    
    @Override
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_21.create().run();
    }
}