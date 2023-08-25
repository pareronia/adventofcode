import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2015_09 extends AoCBase {
    
    private final Distances distances;

    private AoC2015_09(final List<String> input, final boolean debug) {
        super(debug);
        this.distances = Distances.fromInput(input);
        log(this.distances);
    }

    public static final AoC2015_09 create(final List<String> input) {
        return new AoC2015_09(input, false);
    }

    public static final AoC2015_09 createDebug(final List<String> input) {
        return new AoC2015_09(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        return this.distances.getDistancesOfCompleteRoutes().min().getAsInt();
    }

    @Override
    public Integer solvePart2() {
        return this.distances.getDistancesOfCompleteRoutes().max().getAsInt();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_09.createDebug(TEST).solvePart1() == 605;
        assert AoC2015_09.createDebug(TEST).solvePart2() == 982;

        final Puzzle puzzle = Aocd.puzzle(2015, 9);
        final List<String> inputData = puzzle.getInputData();

        puzzle.check(
            () -> lap("Part 1", AoC2015_09.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_09.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
            "London to Dublin = 464\r\n" +
            "London to Belfast = 518\r\n" +
            "Dublin to Belfast = 141"
    );
    
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    private static final class Distances {
        private final int[][] matrix;
        
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
