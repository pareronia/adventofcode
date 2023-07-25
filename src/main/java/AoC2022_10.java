import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.apache.commons.lang3.mutable.MutableInt;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public class AoC2022_10 extends AoCBase {
    private static final char FILL = '▒';
    private static final char EMPTY = ' ';

    
    private final List<String> input;
    
    private AoC2022_10(final List<String> input, final boolean debug) {
        super(debug);
        this.input = input;
    }
    
    public static final AoC2022_10 create(final List<String> input) {
        return new AoC2022_10(input, false);
    }

    public static final AoC2022_10 createDebug(final List<String> input) {
        return new AoC2022_10(input, true);
    }
    
    private Stream<State> program() {
        final MutableInt cycles = new MutableInt(0);
        final MutableInt x = new MutableInt(1);
        final Builder<State> builder = Stream.builder();
        this.input.forEach(line -> {
            final String[] splits = line.split(" ");
            if (splits[0].equals("noop")) {
                builder.add(new State(cycles.getValue(), x.getValue()));
                cycles.increment();
            } else if (splits[0].equals("addx")) {
                builder.add(new State(cycles.getValue(), x.getValue()));
                cycles.increment();
                builder.add(new State(cycles.getValue(), x.getValue()));
                cycles.increment();
                x.add(Integer.parseInt(splits[1]));
            }
        });
        return builder.build();
    }
    
    private int check(final int cycles, final int x) {
        if (cycles % 40 == 20) {
            return x * cycles;
        } else {
            return 0;
        }
    }
    
    private char draw(final int cycles, final int x) {
        return Math.abs(x - cycles % 40) <= 1 ? FILL : EMPTY;
    }
    
    private List<String> getPixels() {
        final String pixels = program()
                .map(state -> draw(state.cycles, state.x))
                .collect(toAString());
        return range(6).stream()
                .map(i -> pixels.substring(i * 40, i * 40 + 40))
                .collect(toList());
    }
    
    @Override
    public Integer solvePart1() {
        return program()
            .mapToInt(state -> check(state.cycles + 1, state.x))
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
    
    @RequiredArgsConstructor
    private static final class State {
        private final int cycles;
        private final int x;
    }
}
