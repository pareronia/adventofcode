import static com.github.pareronia.aoc.IntegerSequence.Range.rangeClosed;
import static com.github.pareronia.aoc.Utils.toAString;
import static com.github.pareronia.aoc.itertools.IterTools.enumerate;
import static java.util.stream.Collectors.toList;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class AoC2022_10 extends AoCBase {
    private static final int PERIOD = 40;
    private static final int MAX = 220;
    private static final char FILL = '▒';
    private static final char EMPTY = ' ';
    
    private final List<Instruction> input;
    
    private AoC2022_10(final List<String> input, final boolean debug) {
        super(debug);
        this.input = input.stream()
                .map(Instruction::fromString)
                .collect(toList());
    }
    
    public static final AoC2022_10 create(final List<String> input) {
        return new AoC2022_10(input, false);
    }

    public static final AoC2022_10 createDebug(final List<String> input) {
        return new AoC2022_10(input, true);
    }
    
    private Stream<Integer> getXValues() {
        final MutableInt x = new MutableInt(1);
        final Builder<Integer> builder = Stream.builder();
        this.input.forEach(ins -> {
            if (ins.operation == OpCode.NOOP) {
                builder.add(x.getValue());
            } else {
                builder.add(x.getValue());
                builder.add(x.getValue());
                x.add(ins.operand.get());
            }
        });
        return builder.build();
    }
    
    private char draw(final int cycle, final int x) {
        return Math.abs(cycle % PERIOD - x) <= 1 ? FILL : EMPTY;
    }
    
    private List<String> getPixels() {
        final String pixels = enumerate(getXValues()).stream()
                .map(e -> draw(e.index(), e.value()))
                .collect(toAString());
        return rangeClosed(0, MAX, PERIOD).stream()
                .map(i -> pixels.substring(i, i + PERIOD))
                .collect(toList());
    }
    
    @Override
    public Integer solvePart1() {
        final List<Integer> xs = getXValues().collect(toList());
        return rangeClosed(20, MAX, PERIOD).intStream()
                .map(i -> i * xs.get(i - 1))
                .sum();
    }

    @Override
    public String solvePart2() {
        final List<String> pixels = getPixels();
        pixels.forEach(this::log);
        return OCR.convert6(CharGrid.from(pixels), FILL, EMPTY);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_10.createDebug(TEST).solvePart1() == 13_140;
        assert AoC2022_10.createDebug(TEST).getPixels().equals(List.of(
                "▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ",
                "▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒ ",
                "▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ",
                "▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     ",
                "▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒",
                "▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒     "
        ));

        final Puzzle puzzle = Aocd.puzzle(2022, 10);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_10.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_10.createDebug(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        """
        	addx 15
        	addx -11
        	addx 6
        	addx -3
        	addx 5
        	addx -1
        	addx -8
        	addx 13
        	addx 4
        	noop
        	addx -1
        	addx 5
        	addx -1
        	addx 5
        	addx -1
        	addx 5
        	addx -1
        	addx 5
        	addx -1
        	addx -35
        	addx 1
        	addx 24
        	addx -19
        	addx 1
        	addx 16
        	addx -11
        	noop
        	noop
        	addx 21
        	addx -15
        	noop
        	noop
        	addx -3
        	addx 9
        	addx 1
        	addx -3
        	addx 8
        	addx 1
        	addx 5
        	noop
        	noop
        	noop
        	noop
        	noop
        	addx -36
        	noop
        	addx 1
        	addx 7
        	noop
        	noop
        	noop
        	addx 2
        	addx 6
        	noop
        	noop
        	noop
        	noop
        	noop
        	addx 1
        	noop
        	noop
        	addx 7
        	addx 1
        	noop
        	addx -13
        	addx 13
        	addx 7
        	noop
        	addx 1
        	addx -33
        	noop
        	noop
        	noop
        	addx 2
        	noop
        	noop
        	noop
        	addx 8
        	noop
        	addx -1
        	addx 2
        	addx 1
        	noop
        	addx 17
        	addx -9
        	addx 1
        	addx 1
        	addx -3
        	addx 11
        	noop
        	noop
        	addx 1
        	noop
        	addx 1
        	noop
        	noop
        	addx -13
        	addx -19
        	addx 1
        	addx 3
        	addx 26
        	addx -30
        	addx 12
        	addx -1
        	addx 3
        	addx 1
        	noop
        	noop
        	noop
        	addx -9
        	addx 18
        	addx 1
        	addx 2
        	noop
        	noop
        	addx 9
        	noop
        	noop
        	noop
        	addx -1
        	addx 2
        	addx -37
        	addx 1
        	addx 3
        	noop
        	addx 15
        	addx -21
        	addx 22
        	addx -6
        	addx 1
        	noop
        	addx 2
        	addx 1
        	noop
        	addx -10
        	noop
        	noop
        	addx 20
        	addx 1
        	addx 2
        	addx 2
        	addx -6
        	addx -11
        	noop
        	noop
        	noop"""
    );
    
    private enum OpCode {
        NOOP, ADDX;
        
        public static OpCode fromString(final String value) {
            return Stream.of(values())
                    .filter(v -> v.name().equalsIgnoreCase(value))
                    .findFirst().orElseThrow();
        }
    }
    
    record Instruction(OpCode operation, Optional<Integer> operand) {
        
        public static Instruction fromString(final String value) {
            final String[] splits = value.split(" ");
            final OpCode operation = OpCode.fromString(splits[0]);
            if (operation == OpCode.NOOP) {
                return new Instruction(operation, Optional.empty());
            } else {
                final Optional<Integer> operand = Optional.of(Integer.parseInt(splits[1]));
                return new Instruction(operation, operand);
            }
        }
    }
}
