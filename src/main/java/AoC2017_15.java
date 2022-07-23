import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aocd.Aocd;

public final class AoC2017_15 extends AoCBase {
    
    private static final long FACTOR_A = 16807;
    private static final long FACTOR_B = 48271;
    private static final long MOD = 2147483647;
    private static final Predicate<Long> NONE = x -> true;

    private final transient long startA;
    private final transient long startB;

    private AoC2017_15(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 2;
        this.startA = Long.parseLong(inputs.get(0).split(" ")[4]);
        this.startB = Long.parseLong(inputs.get(1).split(" ")[4]);
    }

    public static AoC2017_15 create(final List<String> input) {
        return new AoC2017_15(input, false);
    }

    public static AoC2017_15 createDebug(final List<String> input) {
        return new AoC2017_15(input, true);
    }
    
    private long next(final long prev, final long factor, final Predicate<Long> condition) {
        long val = prev;
        while (true) {
            val = (val * factor) % MOD;
            if (condition.test(val)) {
                return val;
            }
        }
    }
    
    private void logValues(final int i, final long prevA, final long prevB) {
        final String a = StringUtils.leftPad(String.valueOf(prevA), 10, ' ');
        final String b = StringUtils.leftPad(String.valueOf(prevB), 10, ' ');
        log("A" + (i + 1) + ": " + a + "\t" + "B" + (i + 1) + ": " + b);
    }

    private Integer solve(
            final int reps,
            final Predicate<Long> conditionA,
            final Predicate<Long> conditionB
    ) {
        int cnt = 0;
        long prevA = this.startA;
        long prevB = this.startB;
        for (int i = 0; i < reps; i++) {
            prevA = next(prevA, FACTOR_A, conditionA);
            prevB = next(prevB, FACTOR_B, conditionB);
            if (i < 5) {
                logValues(i, prevA, prevB);
            }
            if ((short) prevA == (short) prevB) {
                cnt++;
            }
        }
        log(cnt);
        return cnt;
    }
    
    private Predicate<Long> isMultipleOf(final int d) {
        return x -> x % d == 0;
    }
    
    @Override
    public Integer solvePart1() {
        return solve(40_000_000, NONE, NONE);
    }
    
    @Override
    public Integer solvePart2() {
        return solve(5_000_000, isMultipleOf(4), isMultipleOf(8));
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_15.createDebug(TEST).solvePart1().equals(588);
        assert AoC2017_15.createDebug(TEST).solvePart2().equals(309);

        final List<String> input = Aocd.getData(2017, 15);
        lap("Part 1", () -> AoC2017_15.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_15.create(input).solvePart2());
    }
    
    private static final List<String> TEST = splitLines(
            "Generator A starts with 65\n" +
            "Generator B starts with 8921"
    );
}
