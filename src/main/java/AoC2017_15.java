import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2017_15
        extends SolutionBase<AoC2017_15.Generators, Integer, Integer> {
    
    private static final long FACTOR_A = 16807;
    private static final long FACTOR_B = 48271;
    private static final long MOD = 2147483647;
    private static final Predicate<Long> NONE = x -> true;

    private AoC2017_15(final boolean debug) {
        super(debug);
    }

    public static AoC2017_15 create() {
        return new AoC2017_15(false);
    }

    public static AoC2017_15 createDebug() {
        return new AoC2017_15(true);
    }

    @Override
    protected AoC2017_15.Generators parseInput(final List<String> inputs) {
        return new Generators(
                Long.parseLong(inputs.get(0).split(" ")[4]),
                Long.parseLong(inputs.get(1).split(" ")[4]));
    }

    private Integer solve(
            final Generators generators,
            final int reps,
            final Predicate<Long> conditionA,
            final Predicate<Long> conditionB
    ) {
        final Iterator<Long> iteratorA = generators.iteratorA(conditionA);
        final Iterator<Long> iteratorB = generators.iteratorB(conditionB);
        return (int) range(reps).intStream()
            .filter(i -> iteratorA.next().shortValue() == iteratorB.next().shortValue())
            .count();
    }
    
    private Predicate<Long> isMultipleOf(final int d) {
        return x -> x % d == 0;
    }
    
    @Override
    public Integer solvePart1(final Generators generators) {
        return solve(generators, 40_000_000, NONE, NONE);
    }
    
    @Override
    public Integer solvePart2(final Generators generators) {
        return solve(generators, 5_000_000, isMultipleOf(4), isMultipleOf(8));
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "588"),
        @Sample(method = "part2", input = TEST, expected = "309"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2017_15.create().run();
    }
    
    private static final String TEST = """
            Generator A starts with 65
            Generator B starts with 8921
            """;
    
    record Generators(long a, long b) {

        public Iterator<Long> iteratorA(final Predicate<Long> condition) {
            return createIterator(a, FACTOR_A, condition);
        }

        public Iterator<Long> iteratorB(final Predicate<Long> condition) {
            return createIterator(b, FACTOR_B, condition);
        }

        private Iterator<Long> createIterator(
                final long seed,
                final long factor,
                final Predicate<Long> condition
        ) {
            return new Iterator<>() {
                private long prev = seed;
                
                @Override
                public Long next() {
                    long val = prev;
                    while (true) {
                        val = (val * factor) % MOD;
                        if (condition.test(val)) {
                            prev = val;
                            return val;
                        }
                    }
                }
                
                @Override
                public boolean hasNext() {
                    return true;
                }
            };
        }
    }
}
