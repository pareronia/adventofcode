import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AoC2017_08 extends SolutionBase<List<String>, Integer, Integer> {

    private static final Map<String, Test> TESTS =
            Map.of(
                    "==", Tests::eq,
                    "!=", Tests::ne,
                    ">=", Tests::ge,
                    ">", Tests::gt,
                    "<=", Tests::le,
                    "<", Tests::lt);
    private static final Map<String, Operation> OPERATIONS =
            Map.of(
                    "inc", Operations::inc,
                    "dec", Operations::dec);

    private AoC2017_08(final boolean debug) {
        super(debug);
    }

    public static AoC2017_08 create() {
        return new AoC2017_08(false);
    }

    public static AoC2017_08 createDebug() {
        return new AoC2017_08(true);
    }

    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    private int max(final Collection<Integer> list) {
        return list.stream().mapToInt(Integer::valueOf).max().orElse(0);
    }

    private List<Integer> solve(final List<String> input) {
        final Map<String, Integer> registers = new HashMap<>();
        final List<Integer> maxValues = new ArrayList<>();
        for (final String input_ : input) {
            final String[] sp = input_.split(" ");
            final String test = sp[5];
            final String creg = sp[4];
            final int testValue = Integer.parseInt(sp[6]);
            if (TESTS.get(test).test(registers, creg, testValue)) {
                final String operation = sp[1];
                final String reg = sp[0];
                final Integer value = Integer.valueOf(sp[2]);
                OPERATIONS.get(operation).execute(registers, reg, value);
            }
            maxValues.add(max(registers.values()));
        }
        return maxValues;
    }

    @Override
    public Integer solvePart1(final List<String> input) {
        return solve(input).getLast();
    }

    @Override
    public Integer solvePart2(final List<String> input) {
        return max(solve(input));
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "1"),
        @Sample(method = "part2", input = TEST, expected = "10"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    public static final String TEST =
            """
            b inc 5 if a > 1
            a inc 1 if b < 5
            c dec -10 if a >= 1
            c inc -20 if c == 10
            """;

    @FunctionalInterface
    private interface Test {
        boolean test(final Map<String, Integer> registers, final String register, final int value);
    }

    @FunctionalInterface
    private interface Operation {
        void execute(final Map<String, Integer> registers, final String register, final int value);
    }

    private static final class Tests {
        public static boolean eq(
                final Map<String, Integer> registers, final String register, final int value) {
            return registers.getOrDefault(register, 0) == value;
        }

        public static boolean ne(
                final Map<String, Integer> registers, final String register, final int value) {
            return registers.getOrDefault(register, 0) != value;
        }

        public static boolean le(
                final Map<String, Integer> registers, final String register, final int value) {
            return registers.getOrDefault(register, 0) <= value;
        }

        public static boolean ge(
                final Map<String, Integer> registers, final String register, final int value) {
            return registers.getOrDefault(register, 0) >= value;
        }

        public static boolean lt(
                final Map<String, Integer> registers, final String register, final int value) {
            return registers.getOrDefault(register, 0) < value;
        }

        public static boolean gt(
                final Map<String, Integer> registers, final String register, final int value) {
            return registers.getOrDefault(register, 0) > value;
        }
    }

    public static final class Operations {
        public static void dec(
                final Map<String, Integer> registers, final String register, final int value) {
            registers.put(register, registers.getOrDefault(register, 0) - value);
        }

        public static void inc(
                final Map<String, Integer> registers, final String register, final int value) {
            registers.put(register, registers.getOrDefault(register, 0) + value);
        }
    }
}
