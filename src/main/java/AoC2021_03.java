import java.util.List;
import java.util.function.Function;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2021_03 extends SolutionBase<List<String>, Integer, Integer> {

    private AoC2021_03(final boolean debug) {
        super(debug);
    }

    public static final AoC2021_03 create() {
    	return new AoC2021_03(false);
    }

    public static final AoC2021_03 createDebug() {
    	return new AoC2021_03(true);
    }

    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    private int ans(final String value1, final String value2) {
        return Integer.parseInt(value1, 2) * Integer.parseInt(value2, 2);
    }

    @Override
       public Integer solvePart1(final List<String> lines) {
        final StringBuilder gamma = new StringBuilder();
        final StringBuilder epsilon = new StringBuilder();
        for (int i = 0; i < lines.get(0).length(); i++) {
            final BitCount bitCounts = BitCount.atPos(lines, i);
            gamma.append(bitCounts.mostCommon());
            epsilon.append(bitCounts.leastCommon());
        }
        return ans(gamma.toString(), epsilon.toString());
    }

    private String reduce(
            List<String> strings,
            final Function<BitCount, Character> keep
    ) {
        int pos = 0;
        while (strings.size() > 1) {
            final BitCount bitCounts = BitCount.atPos(strings, pos);
            final int fpos = pos++;
            final char toKeep = keep.apply(bitCounts);
            strings = strings.stream()
                    .filter(s -> s.charAt(fpos) == toKeep)
                    .toList();
        }
        return strings.get(0);
    }

    @Override
    public Integer solvePart2(final List<String> lines) {
        final String o2 = reduce(lines, BitCount::mostCommon);
        final String co2 = reduce(lines, BitCount::leastCommon);
        return ans(o2, co2);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "198"),
        @Sample(method = "part2", input = TEST, expected = "230"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2021_03.create().run();
    }

    private static final String TEST = """
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010
            """;

    record BitCount(int ones, int zeroes) {

        public static BitCount atPos(final List<String> strings, final int pos) {
            final int zeroes = (int) strings.stream()
                    .filter(s -> s.charAt(pos) == '0')
                    .count();
            return new BitCount(strings.size() - zeroes, zeroes);
        }

        public char mostCommon() {
            return ones >= zeroes ? '1' : '0';
        }

        public char leastCommon() {
            return ones < zeroes ? '1' : '0';
        }
    }
}
