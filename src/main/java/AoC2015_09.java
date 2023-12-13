import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_09 extends SolutionBase<AoC2015_09.Distances, Integer, Integer> {
    
    private AoC2015_09(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_09 create() {
        return new AoC2015_09(false);
    }

    public static final AoC2015_09 createDebug() {
        return new AoC2015_09(true);
    }
    
    @Override
    protected Distances parseInput(final List<String> inputs) {
        return Distances.fromInput(inputs);
    }

    @Override
    public Integer solvePart1(final Distances distances) {
        return distances.getDistancesOfCompleteRoutes().min().getAsInt();
    }

    @Override
    public Integer solvePart2(final Distances distances) {
        return distances.getDistancesOfCompleteRoutes().max().getAsInt();
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "605"),
        @Sample(method = "part2", input = TEST, expected = "982"),
    })
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        AoC2015_09.create().run();
    }

    private static final String TEST =
            """
    	London to Dublin = 464\r
    	London to Belfast = 518\r
    	Dublin to Belfast = 141""";

    record Distances(int[][] matrix) {
        
        public static Distances fromInput(final List<String> inputs) {
            final Map<String, Integer> map = new HashMap<>();
            final Map<int[], Integer> values = new HashMap<>();
            final MutableInt cnt = new MutableInt(0);
            for (final String input : inputs) {
                final String[] ss1 = input.split(" = ");
                final String[] ss2 = ss1[0].split(" to ");
                final int idx1 = map.computeIfAbsent(ss2[0], x -> cnt.getAndIncrement());
                final int idx2 = map.computeIfAbsent(ss2[1], x -> cnt.getAndIncrement());
                final int value = Integer.parseInt(ss1[1]);
                values.put(new int[] { idx1, idx2 }, value);
                values.put(new int[] { idx2, idx1 }, value);
            }
            final int[][] matrix = new int[map.size()][map.size()];
            values.entrySet().stream()
                .forEach(e -> {
                    matrix[e.getKey()[0]][e.getKey()[1]] = e.getValue();
                });
            return new Distances(matrix);
        }
        
        public IntStream getDistancesOfCompleteRoutes() {
            final int[] idxs = IntStream.range(0, this.matrix.length).toArray();
            return IterTools.permutations(idxs).mapToInt(p ->
                    IntStream.range(1, p.length)
                        .map(i -> this.matrix[p[i -1]][p[i]])
                        .sum());
        }
    }
}