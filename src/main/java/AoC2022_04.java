import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.Range;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_04 extends AoCBase {
    
    private static final Pattern REGEX = Pattern.compile("[0-9]+");
    
    private final List<String> input;
    
    private AoC2022_04(final List<String> input, final boolean debug) {
        super(debug);
        this.input = input;
    }
    
    public static final AoC2022_04 create(final List<String> input) {
        return new AoC2022_04(input, false);
    }

    public static final AoC2022_04 createDebug(final List<String> input) {
        return new AoC2022_04(input, true);
    }
    
    private final int[] numbers(final String string) {
        return REGEX.matcher(string).results()
                .map(MatchResult::group)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
    
    private int solve(final BiPredicate<Range<Integer>, Range<Integer>> predicate) {
        return (int) this.input.stream()
            .map(this::numbers)
            .filter(nums -> predicate.test(
                    Range.between(nums[0], nums[1]),
                    Range.between(nums[2], nums[3])))
            .count();
    }
    
    @Override
    public Integer solvePart1() {
        return solve((range1, range2) ->
                range1.containsRange(range2) || range2.containsRange(range1));
    }

    @Override
    public Integer solvePart2() {
        return solve((range1, range2) -> range1.isOverlappedBy(range2));
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_04.createDebug(TEST).solvePart1() == 2;
        assert AoC2022_04.createDebug(TEST).solvePart2() == 4;

        final Puzzle puzzle = Aocd.puzzle(2022, 4);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_04.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_04.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "2-4,6-8\r\n" +
        "2-3,4-5\r\n" +
        "5-7,7-9\r\n" +
        "2-8,3-7\r\n" +
        "6-6,4-6\r\n" +
        "2-6,4-8"
    );
}
