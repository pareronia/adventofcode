import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;

public final class AoC2017_04 extends AoCBase {

    private final transient List<String> input;

    private AoC2017_04(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs;
    }

    public static AoC2017_04 create(final List<String> input) {
        return new AoC2017_04(input, false);
    }

    public static AoC2017_04 createDebug(final List<String> input) {
        return new AoC2017_04(input, true);
    }
    
    private boolean hasNoDuplicateWords(final String string) {
        return Arrays.stream(string.split(" "))
                .collect(groupingBy(sp -> sp, counting()))
                .values().stream()
                .noneMatch(c -> c > 1);
    }
    
    private boolean hasNoAnagrams(final String string) {
        final List<String> words = Arrays.stream(string.split(" "))
                .collect(toList());
        final Set<Map<Character, Long>> letterCounts = words.stream()
                .map(w -> Utils.asCharacterStream(w)
                            .collect(groupingBy(c -> c, counting())))
                .collect(toSet());
        return words.size() == letterCounts.size();
    }

    @Override
    public Integer solvePart1() {
        return (int) this.input.stream()
            .filter(this::hasNoDuplicateWords)
            .count();
    }
    
    @Override
    public Integer solvePart2() {
        return (int) this.input.stream()
            .filter(this::hasNoAnagrams)
            .count();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_04.createDebug(TEST1).solvePart1() == 1;
        assert AoC2017_04.createDebug(TEST2).solvePart1() == 0;
        assert AoC2017_04.createDebug(TEST3).solvePart1() == 1;
        assert AoC2017_04.createDebug(TEST4).solvePart2() == 1;
        assert AoC2017_04.createDebug(TEST5).solvePart2() == 0;
        assert AoC2017_04.createDebug(TEST6).solvePart2() == 1;
        assert AoC2017_04.createDebug(TEST7).solvePart2() == 1;
        assert AoC2017_04.createDebug(TEST8).solvePart2() == 0;

        final List<String> input = Aocd.getData(2017, 4);
        lap("Part 1", () -> AoC2017_04.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_04.create(input).solvePart2());
    }
    
    private static final List<String> TEST1 = splitLines("aa bb cc dd ee");
    private static final List<String> TEST2 = splitLines("aa bb cc dd aa");
    private static final List<String> TEST3 = splitLines("aa bb cc dd aaa");
    private static final List<String> TEST4 = splitLines("abcde fghij");
    private static final List<String> TEST5 = splitLines("abcde xyz ecdab");
    private static final List<String> TEST6 = splitLines("a ab abc abd abf abj");
    private static final List<String> TEST7 = splitLines("iiii oiii ooii oooi oooo");
    private static final List<String> TEST8 = splitLines("oiii ioii iioi iiio");
}
