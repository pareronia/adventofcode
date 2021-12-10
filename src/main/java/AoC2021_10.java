import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.pareronia.aocd.Aocd;

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
    private static final Map<Character, Integer> SCORES = Map.of(
        ')', 3,
        ']', 57,
        '}', 1_197,
        '>', 25_137
    );
    
    private int score(final String line) {
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
                    return SCORES.get(c);
                }
            }
        }
        return 0;
    }
    
    @Override
    public Integer solvePart1() {
        return this.lines.stream()
            .map(this::score)
            .peek(this::log)
            .mapToInt(Integer::intValue)
            .sum();
    }
    
    @Override
    public Integer solvePart2() {
        return null;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_10.createDebug(TEST).solvePart1() == 26_397;
        assert AoC2021_10.create(TEST).solvePart2() == null;

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
