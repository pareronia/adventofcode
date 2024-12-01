import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_10 extends AoCBase {
    
    private static final char PAREN_OPEN = '(';
    private static final char PAREN_CLOSE = ')';
    private static final char SQUARE_OPEN = '[';
    private static final char SQUARE_CLOSE = ']';
    private static final char CURLY_OPEN = '{';
    private static final char CURLY_CLOSE = '}';
    private static final char ANGLE_OPEN = '<';
    private static final char ANGLE_CLOSE = '>';
    private static final Set<Character> OPEN = Set.of(PAREN_OPEN, SQUARE_OPEN, CURLY_OPEN, ANGLE_OPEN);
    private static final Map<Character, Character> MAP = Map.of(
        PAREN_OPEN, PAREN_CLOSE,
        SQUARE_OPEN, SQUARE_CLOSE,
        CURLY_OPEN, CURLY_CLOSE,
        ANGLE_OPEN, ANGLE_CLOSE,
        PAREN_CLOSE, PAREN_OPEN,
        SQUARE_CLOSE, SQUARE_OPEN,
        CURLY_CLOSE, CURLY_OPEN,
        ANGLE_CLOSE, ANGLE_OPEN
    );
    private static final Map<Character, Long> CORRUPTION_SCORES = Map.of(
        PAREN_CLOSE, 3L,
        SQUARE_CLOSE, 57L,
        CURLY_CLOSE, 1_197L,
        ANGLE_CLOSE, 25_137L
    );
    private static final Map<Character, Long> INCOMPLETE_SCORES = Map.of(
        PAREN_OPEN, 1L,
        SQUARE_OPEN, 2L,
        CURLY_OPEN, 3L,
        ANGLE_OPEN, 4L
    );
    
    private final List<String> lines;
    
    private AoC2021_10(final List<String> input, final boolean debug) {
        super(debug);
        this.lines = input;
    }
    
    public static final AoC2021_10 create(final List<String> input) {
        return new AoC2021_10(input, false);
    }

    public static final AoC2021_10 createDebug(final List<String> input) {
        return new AoC2021_10(input, true);
    }
    
    private Result check(final String line) {
        final Deque<Character> stack = new ArrayDeque<>();
        for (final char c : line.toCharArray()) {
            if (OPEN.contains(c)) {
                stack.addFirst(c);
            } else {
                if (MAP.get(c) != stack.pop()) {
                    return Result.corrupt(c);
                }
            }
        }
        return Result.incomplete(stack);
    }
    
    @Override
    public Long solvePart1() {
        return this.lines.stream()
            .map(this::check)
            .map(Result::corrupt)
            .filter(Objects::nonNull)
            .map(CORRUPTION_SCORES::get)
            .mapToLong(Long::longValue)
            .sum();
    }
    
    @Override
    public Long solvePart2() {
        final long[] scores = this.lines.stream()
            .map(this::check)
            .map(Result::incomplete)
            .filter(Objects::nonNull)
            .map(Arrays::asList)
            .map(x -> x.stream()
                        .map(INCOMPLETE_SCORES::get)
                        .reduce((total, score) -> 5 * total + score).orElseThrow())
            .mapToLong(Long::longValue)
            .sorted()
            .toArray();
        assert scores.length % 2 == 1;
        return scores[scores.length / 2];
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_10.create(TEST).solvePart1() == 26_397;
        assert AoC2021_10.create(TEST).solvePart2() == 288_957;

        final Puzzle puzzle = Aocd.puzzle(2021, 10);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_10.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_10.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "[({(<(())[]>[[{[]{<()<>>\r\n" +
        "[(()[<>])]({[<{<<[]>>(\r\n" +
        "{([(<{}[<>[]}>{[]{[(<()>\r\n" +
        "(((({<>}<{<{<>}{[]{[]{}\r\n" +
        "[[<[([]))<([[{}[[()]]]\r\n" +
        "[{[{({}]{}}([{[{{{}}([]\r\n" +
        "{<[[]]>}<{[{[{[]{()[[[]\r\n" +
        "[<(<(<(<{}))><([]([]()\r\n" +
        "<{([([[(<>()){}]>(<<{{\r\n" +
        "<{([{{}}[<[[[<>{}]]]>[]]"
    );
    
    record Result(Character corrupt, Character[] incomplete) {

        public static Result corrupt(final Character c) {
            return new Result(c, null);
        }
        
        public static Result incomplete(final Deque<Character> stack) {
            return new Result(null, stack.toArray(new Character[stack.size()]));
        }
    }
}
