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

public class AoC2015_13 extends AoCBase {
    
    private final Happiness input;
    
    private AoC2015_13(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = Happiness.fromInput(inputs);
    }

    public static final AoC2015_13 create(final List<String> input) {
        return new AoC2015_13(input, false);
    }

    public static final AoC2015_13 createDebug(final List<String> input) {
        return new AoC2015_13(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        return this.input.getOptimalHappinessChangeWithoutMe();
    }
    
    @Override
    public Integer solvePart2() {
        return this.input.getOptimalHappinessChangeWithMe();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_13.createDebug(TEST).solvePart1() == 330;

        final Puzzle puzzle = Aocd.puzzle(2015, 13);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_13.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_13.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
            "Alice would gain 54 happiness units by sitting next to Bob.\r\n"
            + "Alice would lose 79 happiness units by sitting next to Carol.\r\n"
            + "Alice would lose 2 happiness units by sitting next to David.\r\n"
            + "Bob would gain 83 happiness units by sitting next to Alice.\r\n"
            + "Bob would lose 7 happiness units by sitting next to Carol.\r\n"
            + "Bob would lose 63 happiness units by sitting next to David.\r\n"
            + "Carol would lose 62 happiness units by sitting next to Alice.\r\n"
            + "Carol would gain 60 happiness units by sitting next to Bob.\r\n"
            + "Carol would gain 55 happiness units by sitting next to David.\r\n"
            + "David would gain 46 happiness units by sitting next to Alice.\r\n"
            + "David would lose 7 happiness units by sitting next to Bob.\r\n"
            + "David would gain 41 happiness units by sitting next to Carol."
    );
    
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class Happiness {
        private final int[][] happinessMatrix;
        
        public static Happiness fromInput(final List<String> inputs) {
            final Map<String, Integer> map = new HashMap<>();
            final Map<int[], Integer> values = new HashMap<>();
            final MutableInt cnt = new MutableInt(0);
            for (final String input : inputs) {
                final String[] s = input.substring(0, input.length() - 1).split(" ");
                final String d1 = s[0];
                final String d2 = s[10];
                final int idx1 = map.computeIfAbsent(d1, x -> cnt.getAndIncrement());
                final int idx2 = map.computeIfAbsent(d2, x -> cnt.getAndIncrement());
                final int value = Integer.parseInt(s[3]);
                values.put(new int[] { idx1, idx2 }, "gain".equals(s[2]) ? value : -value);
            }
            final int[][] happinessMatrix = new int[map.keySet().size() + 1][map.keySet().size() + 1];
            values.entrySet().stream()
                .forEach(e -> {
                    happinessMatrix[e.getKey()[0]][e.getKey()[1]] = e.getValue();
                });
            return new Happiness(happinessMatrix);
        }
        
        private int solve(final int size) {
            final int[] idxs = IntStream.range(0, size).toArray();
            return IterTools.permutations(idxs)
                .mapToInt(p ->
                    IntStream.range(0, p.length)
                        .map(i -> {
                            final int d1 = p[i];
                            final int d2 = p[(i + 1) % p.length];
                            return this.happinessMatrix[d1][d2] + this.happinessMatrix[d2][d1];
                        })
                        .sum())
                .max().orElseThrow();
        }
        
        public int getOptimalHappinessChangeWithoutMe() {
            return solve(this.happinessMatrix.length - 1);
        }
        
        public int getOptimalHappinessChangeWithMe() {
            return solve(this.happinessMatrix.length);
        }
    }
}
