import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.util.List;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2017_01 extends SolutionBase<String, Integer, Integer> {

    private AoC2017_01(final boolean debug) {
        super(debug);
    }

    public static AoC2017_01 create() {
        return new AoC2017_01(false);
    }

    public static AoC2017_01 createDebug() {
        return new AoC2017_01(true);
    }

    private int sumSameCharsAt(final String input, final int distance) {
        final String test = input + input.substring(0, distance);
        return range(input.length()).intStream()
                .filter(i -> test.charAt(i) == test.charAt(i + distance))
                .map(i -> Character.digit(test.charAt(i), 10))
                .sum();
    }
 
    @Override
    protected String parseInput(final List<String> inputs) {
        return inputs.get(0);
    }

    @Override
    public Integer solvePart1(final String input) {
        return sumSameCharsAt(input, 1);
    }
    
    @Override
    public Integer solvePart2(final String input) {
        return sumSameCharsAt(input, input.length() / 2);
    }

    @Samples({
        @Sample(method = "part1", input = "1122", expected = "3"),
        @Sample(method = "part1", input = "1111", expected = "4"),
        @Sample(method = "part1", input = "1234", expected = "0"),
        @Sample(method = "part1", input = "91212129", expected = "9"),
        @Sample(method = "part2", input = "1212", expected = "6"),
        @Sample(method = "part2", input = "1221", expected = "0"),
        @Sample(method = "part2", input = "123425", expected = "4"),
        @Sample(method = "part2", input = "123123", expected = "12"),
        @Sample(method = "part2", input = "12131415", expected = "4"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2017_01.create().run();
    }
}
