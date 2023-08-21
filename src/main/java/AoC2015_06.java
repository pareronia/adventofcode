import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

// TODO: better -> https://www.reddit.com/r/adventofcode/comments/3vmltn/day_6_solutions/cxozgap/
public final class AoC2015_06 extends AoCBase {
    
    private final transient List<Instruction> input;

    private AoC2015_06(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs.stream().map(Instruction::fromInput).collect(toList());
        log(this.input);
    }

    public static AoC2015_06 create(final List<String> input) {
        return new AoC2015_06(input, false);
    }

    public static AoC2015_06 createDebug(final List<String> input) {
        return new AoC2015_06(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        final Grid lights = new Grid(c -> 1, c -> 0, c -> c == 1 ? 0 : 1);
        lights.processInstructions(this.input);
        return (int) lights.getAllLightValues().filter(l -> l == 1).count();
    }

    @Override
    public Integer solvePart2() {
        final Grid lights = new Grid(c -> c + 1, c -> Math.max(c - 1, 0), c -> c + 2);
        lights.processInstructions(this.input);
        return lights.getAllLightValues().sum();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_06.createDebug(TEST1).solvePart1() == 1_000_000;
        assert AoC2015_06.createDebug(TEST2).solvePart1() == 1_000;
        assert AoC2015_06.createDebug(TEST3).solvePart1() == 0;
        assert AoC2015_06.createDebug(TEST4).solvePart2() == 1;
        assert AoC2015_06.createDebug(TEST5).solvePart2() == 2_000_000;

        final Puzzle puzzle = Aocd.puzzle(2015, 6);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_06.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_06.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines("turn on 0,0 through 999,999");
    private static final List<String> TEST2 = splitLines("toggle 0,0 through 999,0");
    private static final List<String> TEST3 = splitLines("turn off 499,499 through 500,500");
    private static final List<String> TEST4 = splitLines("turn on 0,0 through 0,0");
    private static final List<String> TEST5 = splitLines("toggle 0,0 through 999,999");

    @RequiredArgsConstructor
    @ToString
    private static final class Instruction {
        private enum Action { TURN_ON, TURN_OFF, TOGGLE }
        
        private final Action action;
        private final Cell start;
        private final Cell end;
        
        public static Instruction fromInput(final String input) {
            final String s = input.replace("turn ", "turn_");
            final String[] splits = s.split(" through ");
            final String[] actionAndStartSplits = splits[0].split(" ");
            final Cell start = Cell.fromString(actionAndStartSplits[1]);
            final Cell end = Cell.fromString(splits[1]);
            if ("turn_on".equals(actionAndStartSplits[0])) {
                return new Instruction(Action.TURN_ON, start, end);
            } else if ("turn_off".equals(actionAndStartSplits[0])) {
                return new Instruction(Action.TURN_OFF, start, end);
            } else {
                return new Instruction(Action.TOGGLE, start, end);
            }
        }
    }
    
    @RequiredArgsConstructor
    private static final class Grid {
        private final IntGrid lights = new IntGrid(new int[1_000][1_000]);
        private final Function<Integer, Integer> turnOn;
        private final Function<Integer, Integer> turnOff;
        private final Function<Integer, Integer> toggle;
        
        public IntStream getAllLightValues() {
            return Stream.of(this.lights.getValues()).flatMapToInt(IntStream::of);
        }
        
        public void processInstructions(final List<Instruction> instructions) {
            for (final Instruction instruction : instructions) {
                final Function<Integer, Integer> action =
                        instruction.action == Instruction.Action.TURN_ON
                            ? this.turnOn
                            : instruction.action == Instruction.Action.TURN_OFF
                            ? this.turnOff
                            : this.toggle;
                for (int rr = instruction.start.getRow(); rr <= instruction.end.getRow(); rr++) {
                    for (int cc = instruction.start.getCol(); cc <= instruction.end.getCol(); cc++) {
                        this.lights.setValue(Cell.at(rr, cc), action.apply(this.lights.getValue(Cell.at(rr, cc))));
                    }
                }
            }
        }
    }
}