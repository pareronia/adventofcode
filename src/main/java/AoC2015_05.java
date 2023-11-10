import java.util.List;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2015_05
            extends SolutionBase<List<String>, Integer, Integer> {
    
    private static final Pattern VOWEL = Pattern.compile("(a|e|i|o|u)");
    private static final Pattern TWIN = Pattern.compile("([a-z])\\1");
    private static final Pattern BAD = Pattern.compile("(ab|cd|pq|xy)");
    private static final Pattern TWO_TWINS = Pattern.compile("([a-z]{2})[a-z]*\\1");
    private static final Pattern THREE_LETTER_PALINDROME = Pattern.compile("([a-z])[a-z]\\1");

    private AoC2015_05(final boolean debug) {
        super(debug);
    }

    public static AoC2015_05 create() {
        return new AoC2015_05(false);
    }

    public static AoC2015_05 createDebug() {
        return new AoC2015_05(true);
    }
    
    private int countMatches(final Pattern pattern, final String str) {
        return (int) pattern.matcher(str).results().count();
    }
    
    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    @Override
    public Integer solvePart1(final List<String> input) {
        return (int) input.stream()
                .filter(s -> countMatches(VOWEL, s) >= 3)
                .filter(s -> countMatches(TWIN, s) >= 1)
                .filter(s -> countMatches(BAD, s) == 0)
                .count();
    }

    @Override
    public Integer solvePart2(final List<String> input) {
        return (int) input.stream()
                .filter(s -> countMatches(TWO_TWINS, s) >= 1)
                .filter(s -> countMatches(THREE_LETTER_PALINDROME, s) >= 1)
                .count();
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "1"),
        @Sample(method = "part1", input = TEST2, expected = "1"),
        @Sample(method = "part1", input = TEST3, expected = "0"),
        @Sample(method = "part1", input = TEST4, expected = "0"),
        @Sample(method = "part1", input = TEST5, expected = "0"),
        @Sample(method = "part2", input = TEST6, expected = "1"),
        @Sample(method = "part2", input = TEST7, expected = "1"),
        @Sample(method = "part2", input = TEST8, expected = "0"),
        @Sample(method = "part2", input = TEST9, expected = "0"),
        @Sample(method = "part2", input = TEST10, expected = "1"),
    })
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        AoC2015_05.create().run();
    }

    private static final String TEST1 = "ugknbfddgicrmopn";
    private static final String TEST2 = "aaa";
    private static final String TEST3 = "jchzalrnumimnmhp";
    private static final String TEST4 = "haegwjzuvuyypxyu";
    private static final String TEST5 = "dvszwmarrgswjxmb";
    private static final String TEST6 = "qjhvhtzxzqqjkmpb";
    private static final String TEST7 = "xxyxx";
    private static final String TEST8 = "uurcxstgmygtbstg";
    private static final String TEST9 = "ieodomkazucvgmuy";
    private static final String TEST10 = "xyxy";

}