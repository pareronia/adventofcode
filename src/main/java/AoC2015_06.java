import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

// TODO: better -> https://www.reddit.com/r/adventofcode/comments/3vmltn/day_6_solutions/cxozgap/
public final class AoC2015_06
            extends SolutionBase<List<AoC2015_06.Instruction>, Integer, Integer> {
    
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

    private int solve(final List<Instruction> instructions, final Mode mode) {
        final int[] lights = new int[1_000_000];
        for (final Instruction instruction : instructions) {
            instruction.action.apply(
                    lights, instruction.start, instruction.end, mode);
        }
        return Arrays.stream(lights).sum();
    }

    @Override
    public Integer solvePart1(final List<Instruction> instructions) {
        return solve(instructions, Mode.MODE_1);
    }

    @Override
    public Integer solvePart2(final List<Instruction> instructions) {
        return solve(instructions, Mode.MODE_2);
    }
    
    enum Mode { MODE_1, MODE_2 }

    record Instruction(Action action, Cell start, Cell end) {
        public static Instruction fromInput(final String input) {
            final String s = input.replace("turn ", "turn_");
            final StringSplit splits = StringOps.splitOnce(s, " through ");
            final StringSplit actionAndStartSplits
                    = StringOps.splitOnce(splits.left(), " ");
            return new Instruction(
                Action.fromString(actionAndStartSplits.left()),
                Cell.fromString(actionAndStartSplits.right()),
                Cell.fromString(splits.right()));
        }
        
        enum Action {
            TURN_ON, TURN_OFF, TOGGLE;

            public static Action fromString(final String s) {
                return switch (s) {
                    case "turn_on" -> TURN_ON;
                    case "turn_off" -> TURN_OFF;
                    default -> TOGGLE;
                };
            }

            public void apply(
                    final int[] grid,
                    final Cell start,
                    final Cell end,
                    final Mode mode
            ) {
                final IntFunction<Integer> f = switch (this) {
                    case TURN_ON -> switch (mode) {
                            case MODE_1: yield v -> 1;
                            case MODE_2: yield v -> v + 1;
                        };
                    case TURN_OFF -> switch (mode) {
                            case MODE_1: yield v -> 0;
                            case MODE_2: yield v -> Math.max(v - 1, 0);
                        };
                    case TOGGLE -> switch (mode) {
                            case MODE_1: yield v -> v == 1 ? 0 : 1;
                            case MODE_2: yield v -> v + 2;
                        };
                };
                final int rStart = start.getRow() * 1_000;
                final int rEnd = end.getRow() * 1_000;
                for (int r = rStart; r <= rEnd; r += 1_000) {
                    for (int idx = r + start.getCol(); idx <= r + end.getCol(); idx++) {
                        grid[idx] = f.apply(grid[idx]);
                    }
                }
            } //apply
        } // Action
    } // Instruction

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