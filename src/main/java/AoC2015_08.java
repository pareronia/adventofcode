import static com.github.pareronia.aoc.StringUtils.countMatches;

import java.util.List;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_08 extends SolutionBase<List<String>, Integer, Integer> {
    
    private static final Pattern HEX = Pattern.compile("\\\\x[0-9a-f]{2}");

    private AoC2015_08(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_08 create() {
        return new AoC2015_08(false);
    }

    public static final AoC2015_08 createDebug() {
        return new AoC2015_08(true);
    }

    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    private int solve(final List<String> inputs, final Mode mode) {
        return inputs.stream().mapToInt(mode::overhead).sum();
    }

    @Override
    public Integer solvePart1(final List<String> inputs) {
        return solve(inputs, Mode.DECODE);
    }

    @Override
    public Integer solvePart2(final List<String> inputs) {
        return solve(inputs, Mode.ENCODE);
    }

    private enum Mode {
        DECODE, ENCODE;

        public int overhead(String str) {
            return switch (this) {
                case DECODE -> {
                    assert str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"';
                    int cnt = 2;
                    while (str.contains("\\\\")) {
                        str = str.replaceFirst("[\\\\]{2}", "");
                        cnt++;
                    }
                    cnt += countMatches(str, "\\\"");
                    cnt += 3 * HEX.matcher(str).results().count();
                    yield cnt;
                }
                case ENCODE ->
                        2 + countMatches(str, "\\") + countMatches(str, "\"");
            };
        }
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "12"),
        @Sample(method = "part2", input = TEST, expected = "19"),
    })
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        AoC2015_08.create().run();
    }

    private static final String TEST =
            """
            ""
            "abc"
            "aaa\\"aaa"
            "\\x27\"\
            """;
}
