import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

// TODO: better -> https://www.reddit.com/r/adventofcode/comments/3vmltn/day_6_solutions/cxozgap/
public final class AoC2015_06
            extends SolutionBase<List<Instruction>, Integer, Integer> {
    
    private AoC2015_06(final boolean debug) {
        super(debug);
    }

    public static AoC2015_06 create() {
        return new AoC2015_06(false);
    }

    public static AoC2015_06 createDebug() {
        return new AoC2015_06(true);
    }
    
    @Override
    protected List<Instruction> parseInput(final List<String> inputs) {
        return inputs.stream().map(Instruction::fromInput).collect(toList());
    }

    @Override
    public Integer solvePart1(final List<Instruction> input) {
        final Grid lights = new Grid(c -> 1, c -> 0, c -> c == 1 ? 0 : 1);
        lights.processInstructions(input);
        return (int) lights.getAllLightValues().filter(l -> l == 1).count();
    }

    @Override
    public Integer solvePart2(final List<Instruction> input) {
        final Grid lights = new Grid(c -> c + 1, c -> Math.max(c - 1, 0), c -> c + 2);
        lights.processInstructions(input);
        return lights.getAllLightValues().sum();
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "1000000"),
        @Sample(method = "part1", input = TEST2, expected = "1000"),
        @Sample(method = "part1", input = TEST3, expected = "0"),
        @Sample(method = "part2", input = TEST4, expected = "1"),
        @Sample(method = "part2", input = TEST5, expected = "2000000"),
    })
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        AoC2015_06.create().run();
    }

    private static final String TEST1 = "turn on 0,0 through 999,999";
    private static final String TEST2 = "toggle 0,0 through 999,0";
    private static final String TEST3 = "turn off 499,499 through 500,500";
    private static final String TEST4 = "turn on 0,0 through 0,0";
    private static final String TEST5 = "toggle 0,0 through 999,999";
}
    
@RequiredArgsConstructor
final class Grid {
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

@RequiredArgsConstructor
@ToString
final class Instruction {
    enum Action { TURN_ON, TURN_OFF, TOGGLE }
    
    final Action action;
    final Cell start;
    final Cell end;
    
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