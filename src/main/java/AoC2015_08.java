import static com.github.pareronia.aoc.StringUtils.countMatches;

import java.util.List;
import java.util.regex.Pattern;

import com.github.pareronia.aocd.Puzzle;

public class AoC2015_08 extends AoCBase {
    
    private static final Pattern HEX = Pattern.compile("\\\\x[0-9a-f]{2}");

    private final transient List<String> inputs;

    private AoC2015_08(final List<String> input, final boolean debug) {
        super(debug);
        this.inputs = input;
    }

    public static final AoC2015_08 create(final List<String> input) {
        return new AoC2015_08(input, false);
    }

    public static final AoC2015_08 createDebug(final List<String> input) {
        return new AoC2015_08(input, true);
    }

    @Override
    public Integer solvePart1() {
        return this.inputs.stream().mapToInt(this::countDecodingOverhead).sum();
    }

    @Override
    public Integer solvePart2() {
        return this.inputs.stream().mapToInt(this::countEncodingOverhead).sum();
    }

    private int countDecodingOverhead(String str) {
        assert str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"';
        int cnt = 2;
        while (str.contains("\\\\")) {
            str = str.replaceFirst("[\\\\]{2}", "");
            cnt++;
        }
        cnt += countMatches(str, "\\\"");
        cnt += 3 * HEX.matcher(str).results().count();
        return cnt;
    }
    
    private int countEncodingOverhead(final String str) {
        return 2 + countMatches(str, "\\") + countMatches(str, "\"");
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_08.createDebug(TEST).solvePart1() == 12;
        assert AoC2015_08.createDebug(TEST).solvePart2() == 19;

		final Puzzle puzzle = Puzzle.create(2015, 8);
		final List<String> input = puzzle.getInputData();
		puzzle.check(
		    () -> lap("Part 1", AoC2015_08.create(input)::solvePart1),
		    () -> lap("Part 2", AoC2015_08.create(input)::solvePart2)
		);
    }

    private static final List<String> TEST = splitLines(
            "\"\"\r\n" +
            "\"abc\"\r\n" +
            "\"aaa\\\"aaa\"\r\n" +
            "\"\\x27\""
    );
}
