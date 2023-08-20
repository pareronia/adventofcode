import static com.github.pareronia.aoc.AssertUtils.unreachable;

import java.util.List;
import java.util.function.Predicate;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;

public final class AoC2015_01 extends AoCBase {
    
    private static final char UP = '(';
    private static final char DOWN = ')';
    
    private final transient String input;
    
    private AoC2015_01(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }
    
    public static AoC2015_01 create(final List<String> input) {
        return new AoC2015_01(input, false);
    }
    
    public static AoC2015_01 createDebug(final List<String> input) {
        return new AoC2015_01(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        return Utils.asCharacterStream(this.input)
            .map(Direction::fromChar)
            .mapToInt(Direction::getValue)
            .sum();
    }
    
    @Override
    public Integer solvePart2() {
        final Predicate<Direction> dropCondition = new Predicate<>() {
            private int sum = 0;
            
            @Override
            public boolean test(final Direction dir) {
                sum += dir.getValue();
                return sum != -1;
            }
        };
        return Utils.enumerate(Utils.asCharacterStream(this.input)
                                .map(Direction::fromChar))
            .dropWhile(e -> dropCondition.test(e.getValue()))
            .map(e -> e.getIndex() + 1)
            .findFirst().orElseThrow();
    }
    
    public static void main(final String[] args) throws Exception {
        assert AoC2015_01.createDebug(TEST1).solvePart1() == 0;
        assert AoC2015_01.createDebug(TEST2).solvePart1() == 0;
        assert AoC2015_01.createDebug(TEST3).solvePart1() == 3;
        assert AoC2015_01.createDebug(TEST4).solvePart1() == 3;
        assert AoC2015_01.createDebug(TEST5).solvePart1() == -1;
        assert AoC2015_01.createDebug(TEST6).solvePart1() == -1;
        assert AoC2015_01.createDebug(TEST7).solvePart1() == -3;
        assert AoC2015_01.createDebug(TEST8).solvePart1() == -3;
        assert AoC2015_01.createDebug(TEST9).solvePart2() == 1;
        assert AoC2015_01.createDebug(TEST10).solvePart2() == 5;

        final Puzzle puzzle = Aocd.puzzle(2015, 1);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_01.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_01.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines("(())");
    private static final List<String> TEST2 = splitLines("()()");
    private static final List<String> TEST3 = splitLines("(((");
    private static final List<String> TEST4 = splitLines("))(((((");
    private static final List<String> TEST5 = splitLines("())");
    private static final List<String> TEST6 = splitLines("))(");
    private static final List<String> TEST7 = splitLines(")))");
    private static final List<String> TEST8 = splitLines(")())())");
    private static final List<String> TEST9 = splitLines(")");
    private static final List<String> TEST10 = splitLines("()())");
    
    @Getter
    private enum Direction {
        UP(1), DOWN(-1);
        
        private int value;
        
        Direction(final int value) {
            this.value = value;
        }
        
        public static Direction fromChar(final Character ch) {
            switch (ch) {
            case AoC2015_01.UP:
                return Direction.UP;
            case AoC2015_01.DOWN:
                return Direction.DOWN;
            default:
                throw unreachable();
            }
        }
    }
}