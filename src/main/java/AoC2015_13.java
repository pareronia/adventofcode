import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_13 extends SolutionBase<AoC2015_13.Happiness, Integer, Integer> {
    
    private AoC2015_13(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_13 create() {
        return new AoC2015_13(false);
    }

    public static final AoC2015_13 createDebug() {
        return new AoC2015_13(true);
    }
    
    @Override
    protected Happiness parseInput(final List<String> inputs) {
        return Happiness.fromInput(inputs);
    }

    @Override
    public Integer solvePart1(final Happiness input) {
        return input.getOptimalHappinessChangeWithoutMe();
    }
    
    @Override
    public Integer solvePart2(final Happiness input) {
        return input.getOptimalHappinessChangeWithMe();
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "330"),
    })
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        AoC2015_13.create().run();
    }
    
    private static final String TEST =
            """
    	Alice would gain 54 happiness units by sitting next to Bob.\r
    	Alice would lose 79 happiness units by sitting next to Carol.\r
    	Alice would lose 2 happiness units by sitting next to David.\r
    	Bob would gain 83 happiness units by sitting next to Alice.\r
    	Bob would lose 7 happiness units by sitting next to Carol.\r
    	Bob would lose 63 happiness units by sitting next to David.\r
    	Carol would lose 62 happiness units by sitting next to Alice.\r
    	Carol would gain 60 happiness units by sitting next to Bob.\r
    	Carol would gain 55 happiness units by sitting next to David.\r
    	David would gain 46 happiness units by sitting next to Alice.\r
    	David would lose 7 happiness units by sitting next to Bob.\r
    	David would gain 41 happiness units by sitting next to Carol.""";
    
    record Happiness(int[][] happinessMatrix) {
        
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
            final int[][] happinessMatrix = new int[map.size() + 1][map.size() + 1];
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