import java.util.List;
import java.util.regex.Pattern;

import com.github.pareronia.aocd.Aocd;

public final class AoC2015_05 extends AoCBase {
    
    private static final Pattern VOWEL = Pattern.compile("(a|e|i|o|u)");
    private static final Pattern TWIN = Pattern.compile("([a-z])\\1");
    private static final Pattern BAD = Pattern.compile("(ab|cd|pq|xy)");
    private static final Pattern TWO_TWINS = Pattern.compile("([a-z]{2})[a-z]*\\1");
    private static final Pattern THREE_LETTER_PALINDROME = Pattern.compile("([a-z])[a-z]\\1");

    private final transient List<String> input;

    private AoC2015_05(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs;
    }

    public static AoC2015_05 create(final List<String> input) {
        return new AoC2015_05(input, false);
    }

    public static AoC2015_05 createDebug(final List<String> input) {
        return new AoC2015_05(input, true);
    }
    
    private int countMatches(final Pattern pattern, final String str) {
        return (int) pattern.matcher(str).results().count();
    }
    
    @Override
    public Integer solvePart1() {
        return (int) this.input.stream()
                .filter(s -> countMatches(VOWEL, s) >= 3)
                .filter(s -> countMatches(TWIN, s) >= 1)
                .filter(s -> countMatches(BAD, s) == 0)
                .count();
    }

    @Override
    public Integer solvePart2() {
        return (int) this.input.stream()
                .filter(s -> countMatches(TWO_TWINS, s) >= 1)
                .filter(s -> countMatches(THREE_LETTER_PALINDROME, s) >= 1)
                .count();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_05.createDebug(TEST1).solvePart1() == 1;
        assert AoC2015_05.createDebug(TEST2).solvePart1() == 1;
        assert AoC2015_05.createDebug(TEST3).solvePart1() == 0;
        assert AoC2015_05.createDebug(TEST4).solvePart1() == 0;
        assert AoC2015_05.createDebug(TEST5).solvePart1() == 0;
        assert AoC2015_05.createDebug(TEST6).solvePart2() == 1;
        assert AoC2015_05.createDebug(TEST7).solvePart2() == 1;
        assert AoC2015_05.createDebug(TEST8).solvePart2() == 0;
        assert AoC2015_05.createDebug(TEST9).solvePart2() == 0;
        assert AoC2015_05.createDebug(TEST10).solvePart2() == 1;

        final List<String> input = Aocd.getData(2015, 5);
        lap("Part 1", () -> AoC2015_05.create(input).solvePart1());
        lap("Part 2", () -> AoC2015_05.create(input).solvePart2());
    }

    private static final List<String> TEST1 = splitLines("ugknbfddgicrmopn");
    private static final List<String> TEST2 = splitLines("aaa");
    private static final List<String> TEST3 = splitLines("jchzalrnumimnmhp");
    private static final List<String> TEST4 = splitLines("haegwjzuvuyypxyu");
    private static final List<String> TEST5 = splitLines("dvszwmarrgswjxmb");
    private static final List<String> TEST6 = splitLines("qjhvhtzxzqqjkmpb");
    private static final List<String> TEST7 = splitLines("xxyxx");
    private static final List<String> TEST8 = splitLines("uurcxstgmygtbstg");
    private static final List<String> TEST9 = splitLines("ieodomkazucvgmuy");
    private static final List<String> TEST10 = splitLines("xyxy");

}