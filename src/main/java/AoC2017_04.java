import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.github.pareronia.aoc.Counter;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2017_04
            extends SolutionBase<List<String>, Integer, Integer> {

    private AoC2017_04(final boolean debug) {
        super(debug);
    }

    public static AoC2017_04 create() {
        return new AoC2017_04(false);
    }

    public static AoC2017_04 createDebug() {
        return new AoC2017_04(true);
    }
    
    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    private int solve(
            final List<String> input,
            final Predicate<List<String>> strategy
    ) {
        return (int) input.stream()
            .map(s -> Arrays.asList(s.split(" ")))
            .filter(strategy::test)
            .count();
    }

    @Override
    public Integer solvePart1(final List<String> input) {
        final Predicate<List<String>> hasNoDuplicateWords = words ->
            new Counter<>(words).values().stream().noneMatch(v -> v > 1L);
        return solve(input, hasNoDuplicateWords);
    }
    
    @Override
    public Integer solvePart2(final List<String> input) {
        final Predicate<List<String>> hasNoAnagrams = words ->
            words.size() == words.stream()
                .map(w -> new Counter<>(Utils.asCharacterStream(w)))
                .distinct().count();
        return solve(input, hasNoAnagrams);
    }

    @Samples({
        @Sample(method = "part1", input=TEST1, expected = "1"),
        @Sample(method = "part1", input=TEST2, expected = "0"),
        @Sample(method = "part1", input=TEST3, expected = "1"),
        @Sample(method = "part2", input=TEST4, expected = "1"),
        @Sample(method = "part2", input=TEST5, expected = "0"),
        @Sample(method = "part2", input=TEST6, expected = "1"),
        @Sample(method = "part2", input=TEST7, expected = "1"),
        @Sample(method = "part2", input=TEST8, expected = "0"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2017_04.create().run();
    }
    
    private static final String TEST1 = "aa bb cc dd ee";
    private static final String TEST2 = "aa bb cc dd aa";
    private static final String TEST3 = "aa bb cc dd aaa";
    private static final String TEST4 = "abcde fghij";
    private static final String TEST5 = "abcde xyz ecdab";
    private static final String TEST6 = "a ab abc abd abf abj";
    private static final String TEST7 = "iiii oiii ooii oooi oooo";
    private static final String TEST8 = "oiii ioii iioi iiio";
}
