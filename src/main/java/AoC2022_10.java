import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.mutable.MutableInt;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aoc.Range;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

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
        final var cycles = new MutableInt(0);
        final var x = new MutableInt(1);
        final var builder = Stream.<State>builder();
        this.input.forEach(line -> {
            final var splits = line.split(" ");
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
        return cycles % 40 == 20 ? x * cycles : 0;
    }
    
    private char draw(final int cycles, final int x) {
        return Math.abs(x - cycles % 40) <= 1 ? FILL : EMPTY;
    }
    
    private List<String> getPixels() {
        final String pixels = program()
                .map(state -> draw(state.cycles, state.x))
                .collect(toAString());
        return Range.range(6).stream()
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

    private static final List<String> TEST = splitLines("""
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
        noop
        """);
    
    private static final record State(int cycles, int x) { }
}
