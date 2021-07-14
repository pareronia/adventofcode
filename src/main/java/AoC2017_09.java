import static java.util.stream.Collectors.summingInt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aocd.Aocd;

public final class AoC2017_09 extends AoCBase {

    private static final char ESCAPE = '!';
    private static final char OPEN_GROUP = '{';
    private static final char CLOSE_GROUP = '}';
    private static final char OPEN_GARBAGE = '<';
    private static final char CLOSE_GARBAGE = '>';
    
    private final transient String input;

    private AoC2017_09(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static AoC2017_09 create(final List<String> input) {
        return new AoC2017_09(input, false);
    }

    public static AoC2017_09 createDebug(final List<String> input) {
        return new AoC2017_09(input, true);
    }
    
    private Pair<Integer, Integer> solve() {
        int open = 0;
        int cnt = 0;
        final List<Integer> scores = new ArrayList<>();
        boolean inGarbage = false;
        boolean escaped = false;
        char prev = ' ';
        for (final char c : this.input.toCharArray()) {
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
        final Integer totalScore = scores.stream()
                .collect(summingInt(Integer::valueOf));
        return Tuples.pair(totalScore, cnt);
    }
    
    @Override
    public Integer solvePart1() {
        return solve().getOne();
    }
    
    @Override
    public Integer solvePart2() {
        return solve().getTwo();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_09.createDebug(TEST1).solvePart1() == 1;
        assert AoC2017_09.createDebug(TEST2).solvePart1() == 6;
        assert AoC2017_09.createDebug(TEST3).solvePart1() == 5;
        assert AoC2017_09.createDebug(TEST4).solvePart1() == 16;
        assert AoC2017_09.createDebug(TEST5).solvePart1() == 1;
        assert AoC2017_09.createDebug(TEST6).solvePart1() == 9;
        assert AoC2017_09.createDebug(TEST7).solvePart1() == 9;
        assert AoC2017_09.createDebug(TEST8).solvePart1() == 3;
        assert AoC2017_09.createDebug(TEST9).solvePart2() == 0;
        assert AoC2017_09.createDebug(TEST10).solvePart2() == 17;
        assert AoC2017_09.createDebug(TEST11).solvePart2() == 3;
        assert AoC2017_09.createDebug(TEST12).solvePart2() == 2;
        assert AoC2017_09.createDebug(TEST13).solvePart2() == 0;
        assert AoC2017_09.createDebug(TEST14).solvePart2() == 0;
        assert AoC2017_09.createDebug(TEST15).solvePart2() == 10;

        final List<String> input = Aocd.getData(2017, 9);
        lap("Part 1", () -> AoC2017_09.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_09.create(input).solvePart2());
    }
    
    public static final List<String> TEST1 = splitLines("{}");
    public static final List<String> TEST2 = splitLines("{{{}}}");
    public static final List<String> TEST3 = splitLines("{{},{}}");
    public static final List<String> TEST4 = splitLines("{{{},{},{{}}}}");
    public static final List<String> TEST5 = splitLines("{<a>,<a>,<a>,<a>}");
    public static final List<String> TEST6 = splitLines("{{<ab>},{<ab>},{<ab>},{<ab>}}");
    public static final List<String> TEST7 = splitLines("{{<!!>},{<!!>},{<!!>},{<!!>}}");
    public static final List<String> TEST8 = splitLines("{{<a!>},{<a!>},{<a!>},{<ab>}}");
    public static final List<String> TEST9 = splitLines("<>");
    public static final List<String> TEST10 = splitLines("<random characters>");
    public static final List<String> TEST11 = splitLines("<<<<>");
    public static final List<String> TEST12 = splitLines("<{!>}>");
    public static final List<String> TEST13 = splitLines("<!!>");
    public static final List<String> TEST14 = splitLines("<!!!>>");
    public static final List<String> TEST15 = splitLines("<{o\"i!a,<{i<a>");
}
