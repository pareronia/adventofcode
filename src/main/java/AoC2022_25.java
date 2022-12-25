import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_25 extends AoCBase {
    
    private static final Map<Character, Integer> DECODE = Map.of(
            '0', 0, '1', 1, '2', 2, '-', -1, '=', -2);
    private static final Map<Long, Pair<String, Integer>> ENCODE = Map.of(
            0L, Tuples.pair("0", 0),
            1L, Tuples.pair("1", 0),
            2L, Tuples.pair("2", 0),
            3L, Tuples.pair("=", 1),
            4L, Tuples.pair("-", 1),
            5L, Tuples.pair("0", 1));

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
            final Pair<String, Integer> pair = ENCODE.get(total % 5);
            ans.append(pair.getOne());
            total = total / 5 + pair.getTwo();
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

    private static final List<String> TEST = splitLines(
        "1=-0-2\r\n" +
        "12111\r\n" +
        "2=0=\r\n" +
        "21\r\n" +
        "2=01\r\n" +
        "111\r\n" +
        "20012\r\n" +
        "112\r\n" +
        "1=-1=\r\n" +
        "1-12\r\n" +
        "12\r\n" +
        "1=\r\n" +
        "122"
    );
    private static final List<String> TEST1 = List.of("1=11-2");
    private static final List<String> TEST2 = List.of("1-0---0");
    private static final List<String> TEST3 = List.of("1121-1110-1=0");
}
