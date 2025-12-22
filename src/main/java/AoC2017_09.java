import static java.util.stream.Collectors.summingInt;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2017_09 extends SolutionBase<String, Integer, Integer> {

    private static final char ESCAPE = '!';
    private static final char OPEN_GROUP = '{';
    private static final char CLOSE_GROUP = '}';
    private static final char OPEN_GARBAGE = '<';
    private static final char CLOSE_GARBAGE = '>';

    private AoC2017_09(final boolean debug) {
        super(debug);
    }

    public static AoC2017_09 create() {
        return new AoC2017_09(false);
    }

    public static AoC2017_09 createDebug() {
        return new AoC2017_09(true);
    }

    @Override
    protected String parseInput(final List<String> inputs) {
        return inputs.getFirst();
    }

    record Result(int totalScore, int nonCancelledChars) {}

    @SuppressWarnings({"PMD.NPathComplexity", "PMD.CyclomaticComplexity"})
    private Result solve(final String input) {
        int open = 0;
        int cnt = 0;
        final List<Integer> scores = new ArrayList<>();
        boolean inGarbage = false;
        boolean escaped = false;
        char prev = ' ';
        for (final char c : input.toCharArray()) {
            if (prev == ESCAPE) {
                escaped = true;
            }
            if (!escaped && !inGarbage && c == OPEN_GROUP) {
                open++;
            } else if (!escaped && !inGarbage && c == CLOSE_GROUP) {
                scores.add(open);
                open--;
            } else if (!escaped && c == OPEN_GARBAGE && !inGarbage) {
                inGarbage = true;
            } else if (!escaped && c == CLOSE_GARBAGE && inGarbage) {
                inGarbage = false;
            } else if (!escaped && inGarbage && c != ESCAPE) {
                cnt++;
            }
            if (escaped && c == ESCAPE) {
                prev = ' ';
            } else {
                prev = c;
            }
            escaped = false;
        }
        final Integer totalScore = scores.stream().collect(summingInt(Integer::valueOf));
        return new Result(totalScore, cnt);
    }

    @Override
    public Integer solvePart1(final String input) {
        return solve(input).totalScore;
    }

    @Override
    public Integer solvePart2(final String input) {
        return solve(input).nonCancelledChars;
    }

    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    @Samples({
        @Sample(method = "part1", input = "{}", expected = "1"),
        @Sample(method = "part1", input = "{{{}}}", expected = "6"),
        @Sample(method = "part1", input = "{{},{}}", expected = "5"),
        @Sample(method = "part1", input = "{{{},{},{{}}}}", expected = "16"),
        @Sample(method = "part1", input = "{<a>,<a>,<a>,<a>}", expected = "1"),
        @Sample(method = "part1", input = "{{<ab>},{<ab>},{<ab>},{<ab>}}", expected = "9"),
        @Sample(method = "part1", input = "{{<!!>},{<!!>},{<!!>},{<!!>}}", expected = "9"),
        @Sample(method = "part1", input = "{{<a!>},{<a!>},{<a!>},{<ab>}}", expected = "3"),
        @Sample(method = "part2", input = "<>", expected = "0"),
        @Sample(method = "part2", input = "<random characters>", expected = "17"),
        @Sample(method = "part2", input = "<<<<>", expected = "3"),
        @Sample(method = "part2", input = "<{!>}>", expected = "2"),
        @Sample(method = "part2", input = "<!!>", expected = "0"),
        @Sample(method = "part2", input = "<!!!>>", expected = "0"),
        @Sample(method = "part2", input = "<{o\"i!a,<{i<a>", expected = "10"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }
}
