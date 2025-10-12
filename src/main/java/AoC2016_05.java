import com.github.pareronia.aoc.CharArrayUtils;
import com.github.pareronia.aoc.codec.MD5;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_05 extends SolutionBase<String, String, String> {

    private AoC2016_05(final boolean debug) {
        super(debug);
    }

    public static AoC2016_05 create() {
        return new AoC2016_05(false);
    }

    public static AoC2016_05 createDebug() {
        return new AoC2016_05(true);
    }

    @Override
    protected String parseInput(final List<String> inputs) {
        return inputs.get(0);
    }

    private ValueAndIndex findMd5StartingWith5Zeroes(final String input, final Integer index) {
        String val = "XXXXX";
        int i = index;
        while (!"00000".equals(val.substring(0, 5))) {
            i++;
            final String toHash = input + i;
            val = MD5.md5Hex(toHash);
        }
        return ValueAndIndex.of(val, i);
    }

    @Override
    public String solvePart1(final String input) {
        Integer index = 0;
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            log(i);
            final ValueAndIndex md5 = findMd5StartingWith5Zeroes(input, index);
            index = md5.index;
            result.append(md5.value.charAt(5));
            log(result);
        }
        return result.toString();
    }

    @Override
    public String solvePart2(final String input) {
        final char[] validPositions = {'0', '1', '2', '3', '4', '5', '6', '7'};
        Integer index = 0;
        final char[] result = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        final Set<Character> seen = new HashSet<>();
        while (true) {
            final ValueAndIndex md5 = findMd5StartingWith5Zeroes(input, index);
            final String val = md5.value;
            index = md5.index;
            final char temp = val.charAt(5);
            if (!CharArrayUtils.contains(validPositions, temp) || seen.contains(temp)) {
                continue;
            }
            seen.add(temp);
            final Integer position = Integer.valueOf(String.valueOf(temp));
            result[position] = val.charAt(6);
            log(String.valueOf(result));
            if (!CharArrayUtils.contains(result, ' ')) {
                break;
            }
        }
        return String.valueOf(result);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "18f47a30"),
        @Sample(method = "part2", input = TEST, expected = "05ace8e3"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST = "abc";

    record ValueAndIndex(String value, int index) {
        public static ValueAndIndex of(final String value, final int index) {
            return new ValueAndIndex(value, index);
        }
    }
}
