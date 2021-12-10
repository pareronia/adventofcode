import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.github.pareronia.aocd.Aocd;

import lombok.Getter;

public class AoC2021_10 extends AoCBase {
    
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
    
    private static final Map<Character, Character> MAP = Map.of(
        '(', ')',
        '[', ']',
        '{', '}',
        '<', '>'
    );
    private static final Map<Character, Integer> CORRUPTION_SCORES = Map.of(
        ')', 3,
        ']', 57,
        '}', 1_197,
        '>', 25_137
    );
    private static final Map<Character, Long> INCOMPLETE_SCORES = Map.of(
        '(', 1L,
        '[', 2L,
        '{', 3L,
        '<', 4L
    );
    
    @Getter
    private static final class Score {
        private final Character corrupt;
        private final Character[] incomplete;

        private Score(final Character corrupt, final Character[] incomplete) {
            this.corrupt = corrupt;
            this.incomplete = incomplete;
        }
        
        public static Score corrupt(final Character c) {
            return new Score(c, null);
        }
        
        public static Score incomplete(final Deque<Character> stack) {
            return new Score(null, stack.toArray(new Character[stack.size()]));
        }
    }
    
    private Score score(final String line) {
        final Deque<Character> stack = new ArrayDeque<>();
        for (final char c : line.toCharArray()) {
            if (MAP.keySet().contains(c)) {
                stack.addFirst(c);
            } else {
                final Character a = stack.pop();
                final Character e = MAP.entrySet().stream()
                    .filter(x -> x.getValue() == c)
                    .map(Entry::getKey)
                    .findFirst().orElseThrow();
                if (e != a) {
                    return Score.corrupt(c);
                }
            }
        }
        return Score.incomplete(stack);
    }
    
    @Override
    public Integer solvePart1() {
        return this.lines.stream()
            .map(this::score)
            .map(Score::getCorrupt)
            .filter(Objects::nonNull)
            .map(CORRUPTION_SCORES::get)
            .mapToInt(Integer::intValue)
            .sum();
    }
    
    @Override
    public Long solvePart2() {
        final long[] scores = this.lines.stream()
            .map(this::score)
            .map(Score::getIncomplete)
            .filter(Objects::nonNull)
            .map(Arrays::asList)
            .peek(this::log)
            .map(x -> x.stream()
                        .map(INCOMPLETE_SCORES::get)
                        .reduce((a, b) -> 5 * a + b).orElseThrow())
            .mapToLong(Long::longValue)
            .peek(this::log)
            .sorted()
            .toArray();
        assert scores.length % 2 == 1;
        return scores[scores.length / 2];
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_10.create(TEST).solvePart1() == 26_397;
        assert AoC2021_10.create(TEST).solvePart2() == 288_957;

        final List<String> input = Aocd.getData(2021, 10);
        lap("Part 1", () -> AoC2021_10.create(input).solvePart1());
        lap("Part 2", () -> AoC2021_10.create(input).solvePart2());
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
}
