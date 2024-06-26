import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_25 extends AoCBase {
    
    private static final Map<Character, Integer> DECODE = Map.of(
            '0', 0, '1', 1, '2', 2, '-', -1, '=', -2);
    private static final Map<Long, DigitAndCarry> ENCODE = Map.of(
            0L, new DigitAndCarry("0", 0),
            1L, new DigitAndCarry("1", 0),
            2L, new DigitAndCarry("2", 0),
            3L, new DigitAndCarry("=", 1),
            4L, new DigitAndCarry("-", 1),
            5L, new DigitAndCarry("0", 1));

    private final List<String> input;

    private AoC2022_25(final List<String> input, final boolean debug) {
        super(debug);
        this.input = input;
    }
    
    public static final AoC2022_25 create(final List<String> input) {
        return new AoC2022_25(input, false);
    }

    public static final AoC2022_25 createDebug(final List<String> input) {
        return new AoC2022_25(input, true);
    }
    
    @Override
    public String solvePart1() {
        long total = this.input.stream()
            .mapToLong(line -> IntStream.range(0, line.length())
                    .mapToLong(i -> {
                        final int coefficient = DECODE.get(line.charAt(i));
                        final int exponent = line.length() - 1 - i;
                        return coefficient * (long) Math.pow(5, exponent);
                    })
                    .sum())
            .sum();
        final StringBuilder ans = new StringBuilder();
        while (total > 0) {
            final DigitAndCarry pair = ENCODE.get(total % 5);
            ans.append(pair.digit());
            total = total / 5 + pair.carry();
        }
        return ans.reverse().toString();
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_25.createDebug(TEST).solvePart1().equals("2=-1=0");
        assert AoC2022_25.createDebug(TEST1).solvePart1().equals("1=11-2");
        assert AoC2022_25.createDebug(TEST2).solvePart1().equals("1-0---0");
        assert AoC2022_25.createDebug(TEST3).solvePart1().equals("1121-1110-1=0");
        assert AoC2022_25.createDebug(TEST).solvePart2() == 0;

        final Puzzle puzzle = Aocd.puzzle(2022, 25);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_25.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_25.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines("""
        1=-0-2
        12111
        2=0=
        21
        2=01
        111
        20012
        112
        1=-1=
        1-12
        12
        1=
        122
        """);
    private static final List<String> TEST1 = List.of("1=11-2");
    private static final List<String> TEST2 = List.of("1-0---0");
    private static final List<String> TEST3 = List.of("1121-1110-1=0");
    
    private static final record DigitAndCarry(String digit, int carry) { }
}
