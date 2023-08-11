import static com.github.pareronia.aoc.IntegerSequence.Range.rangeClosed;
import static com.github.pareronia.aoc.Utils.enumerate;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

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
        final String pixels = enumerate(getXValues())
                .map(e -> draw(e.getIndex(), e.getValue()))
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
        return OCR.convert6(Grid.from(pixels), FILL, EMPTY);
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
        "addx 15\r\n" +
        "addx -11\r\n" +
        "addx 6\r\n" +
        "addx -3\r\n" +
        "addx 5\r\n" +
        "addx -1\r\n" +
        "addx -8\r\n" +
        "addx 13\r\n" +
        "addx 4\r\n" +
        "noop\r\n" +
        "addx -1\r\n" +
        "addx 5\r\n" +
        "addx -1\r\n" +
        "addx 5\r\n" +
        "addx -1\r\n" +
        "addx 5\r\n" +
        "addx -1\r\n" +
        "addx 5\r\n" +
        "addx -1\r\n" +
        "addx -35\r\n" +
        "addx 1\r\n" +
        "addx 24\r\n" +
        "addx -19\r\n" +
        "addx 1\r\n" +
        "addx 16\r\n" +
        "addx -11\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx 21\r\n" +
        "addx -15\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx -3\r\n" +
        "addx 9\r\n" +
        "addx 1\r\n" +
        "addx -3\r\n" +
        "addx 8\r\n" +
        "addx 1\r\n" +
        "addx 5\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx -36\r\n" +
        "noop\r\n" +
        "addx 1\r\n" +
        "addx 7\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx 2\r\n" +
        "addx 6\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx 1\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx 7\r\n" +
        "addx 1\r\n" +
        "noop\r\n" +
        "addx -13\r\n" +
        "addx 13\r\n" +
        "addx 7\r\n" +
        "noop\r\n" +
        "addx 1\r\n" +
        "addx -33\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx 2\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx 8\r\n" +
        "noop\r\n" +
        "addx -1\r\n" +
        "addx 2\r\n" +
        "addx 1\r\n" +
        "noop\r\n" +
        "addx 17\r\n" +
        "addx -9\r\n" +
        "addx 1\r\n" +
        "addx 1\r\n" +
        "addx -3\r\n" +
        "addx 11\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx 1\r\n" +
        "noop\r\n" +
        "addx 1\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx -13\r\n" +
        "addx -19\r\n" +
        "addx 1\r\n" +
        "addx 3\r\n" +
        "addx 26\r\n" +
        "addx -30\r\n" +
        "addx 12\r\n" +
        "addx -1\r\n" +
        "addx 3\r\n" +
        "addx 1\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx -9\r\n" +
        "addx 18\r\n" +
        "addx 1\r\n" +
        "addx 2\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx 9\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx -1\r\n" +
        "addx 2\r\n" +
        "addx -37\r\n" +
        "addx 1\r\n" +
        "addx 3\r\n" +
        "noop\r\n" +
        "addx 15\r\n" +
        "addx -21\r\n" +
        "addx 22\r\n" +
        "addx -6\r\n" +
        "addx 1\r\n" +
        "noop\r\n" +
        "addx 2\r\n" +
        "addx 1\r\n" +
        "noop\r\n" +
        "addx -10\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "addx 20\r\n" +
        "addx 1\r\n" +
        "addx 2\r\n" +
        "addx 2\r\n" +
        "addx -6\r\n" +
        "addx -11\r\n" +
        "noop\r\n" +
        "noop\r\n" +
        "noop"
    );
    
    private enum OpCode {
        NOOP, ADDX;
        
        public static OpCode fromString(final String value) {
            return Stream.of(values())
                    .filter(v -> v.name().equalsIgnoreCase(value))
                    .findFirst().orElseThrow();
        }
    }
    
    @RequiredArgsConstructor
    private static final class Instruction {
        private final OpCode operation;
        private final Optional<Integer> operand;
        
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
