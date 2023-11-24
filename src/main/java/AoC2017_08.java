import static com.github.pareronia.aoc.Utils.last;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_08 extends AoCBase {

    private final transient List<String> input;
    private static final Map<String, Test> TESTS
            = Map.of("==", Tests::eq,
                     "!=", Tests::ne,
                     ">=", Tests::ge,
                     ">", Tests::gt,
                     "<=", Tests::le,
                     "<", Tests::lt);
    private static final Map<String, Operation> OPERATIONS
            = Map.of("inc", Operations::inc,
                     "dec", Operations::dec);

    private AoC2017_08(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs;
    }

    public static AoC2017_08 create(final List<String> input) {
        return new AoC2017_08(input, false);
    }

    public static AoC2017_08 createDebug(final List<String> input) {
        return new AoC2017_08(input, true);
    }
    
    private int max(final Collection<Integer> list) {
        return list.stream().mapToInt(Integer::valueOf).max().orElse(0);
    }
    
    private List<Integer> solve() {
        final Map<String, Integer> registers = new HashMap<>();
        final List<Integer> maxValues = new ArrayList<>();
        for (final String input_ : this.input) {
            final String[] sp = input_.split(" ");
            final String test = sp[5];
            final String creg = sp[4];
            final int testValue = Integer.valueOf(sp[6]);
            if (TESTS.get(test).test(registers, creg, testValue)) {
                final String operation = sp[1];
                final String reg = sp[0];
                final Integer value = Integer.valueOf(sp[2]);
                OPERATIONS.get(operation).execute(registers, reg, value);
            }
            maxValues.add(max(registers.values()));
        }

        log(registers);
        return maxValues;
    }
    
    @Override
    public Integer solvePart1() {
        return last(solve());
    }
    
    @Override
    public Integer solvePart2() {
        return max(solve());
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_08.createDebug(TEST).solvePart1() == 1;
        assert AoC2017_08.createDebug(TEST).solvePart2() == 10;

        final Puzzle puzzle = Aocd.puzzle(2017, 8);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2017_08.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2017_08.create(inputData)::solvePart2)
        );
    }
    
    public static final List<String> TEST = splitLines(
            "b inc 5 if a > 1\n" +
            "a inc 1 if b < 5\n" +
            "c dec -10 if a >= 1\n" +
            "c inc -20 if c == 10"
    );
    
    @FunctionalInterface
    private interface Test {
        boolean test(final Map<String, Integer> registers,
                     final String register, final Integer value);
    }
    
    @FunctionalInterface
    private interface Operation{
        void  execute(final Map<String, Integer> registers,
                      final String register, final Integer value);
    }

    private static final class Tests {
        public static boolean eq(final Map<String, Integer> registers,
                                 final String register, final int value) {
            return registers.getOrDefault(register, 0) == value;
        }
        
        public static boolean ne(final Map<String, Integer> registers,
                                 final String register, final int value) {
            return registers.getOrDefault(register, 0) != value;
        }
        
        public static boolean le(final Map<String, Integer> registers,
                                 final String register, final int value) {
            return registers.getOrDefault(register, 0) <= value;
        }
        
        public static boolean ge(final Map<String, Integer> registers,
                                 final String register, final int value) {
            return registers.getOrDefault(register, 0) >= value;
        }
        
        public static boolean lt(final Map<String, Integer> registers,
                                 final String register, final int value) {
            return registers.getOrDefault(register, 0) < value;
        }
        
        public static boolean gt(final Map<String, Integer> registers,
                                 final String register, final int value) {
            return registers.getOrDefault(register, 0) > value;
        }
    }
    
    public static final class Operations {
        public static void dec(final Map<String, Integer> registers,
                               final String register, final Integer value) {
            registers.put(register, registers.getOrDefault(register, 0) - value);
        }
        
        public static void inc(final Map<String, Integer> registers,
                               final String register, final Integer value) {
            registers.put(register, registers.getOrDefault(register, 0) + value);
        }
    }
}
