import static com.github.pareronia.aoc.Utils.toAString;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2015_12 extends AoCBase {
    
    private final String input;
    
    private AoC2015_12(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static final AoC2015_12 create(final List<String> input) {
        return new AoC2015_12(input, false);
    }

    public static final AoC2015_12 createDebug(final List<String> input) {
        return new AoC2015_12(input, true);
    }
    
    private int addAllNumbers1(final String in) {
        final String numbers = Utils.asCharacterStream(in)
            .filter(c -> Character.isDigit(c) || Set.of('-', ',').contains(c))
            .collect(toAString());
        return Arrays.stream(numbers.split(","))
                .filter(s -> !s.isEmpty())
                .mapToInt(s -> Integer.valueOf(s).intValue())
                .sum();
    }
    
    @SuppressWarnings("unchecked")
    private int addAllNumbers2(final Object obj) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof String) {
            return 0;
        } else if (obj instanceof List) {
            return ((List<Object>) obj).stream().mapToInt(this::addAllNumbers2).sum();
        } else {
            final Collection<Object> values = ((Map<String, Object>) obj).values();
            if (values.stream().anyMatch("red"::equals)) {
                return 0;
            } else {
                return values.stream().mapToInt(this::addAllNumbers2).sum();
            }
        }
    }
    
    @Override
    public Integer solvePart1() {
        return addAllNumbers1(this.input);
    }

    @Override
    public Integer solvePart2() {
        final JSONObject jo = new JSONObject("{content: " + this.input + "}");
        return addAllNumbers2(jo.toMap());
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_12.createDebug(TEST1).solvePart1() == 6;
        assert AoC2015_12.createDebug(TEST2).solvePart1() == 6;
        assert AoC2015_12.createDebug(TEST3).solvePart1() == 3;
        assert AoC2015_12.createDebug(TEST4).solvePart1() == 3;
        assert AoC2015_12.createDebug(TEST5).solvePart1() == 0;
        assert AoC2015_12.createDebug(TEST6).solvePart1() == 0;
        assert AoC2015_12.createDebug(TEST7).solvePart1() == 0;
        assert AoC2015_12.createDebug(TEST8).solvePart1() == 0;
        assert AoC2015_12.createDebug(TEST1).solvePart2() == 6;
        assert AoC2015_12.createDebug(TEST9).solvePart2() == 4;
        assert AoC2015_12.createDebug(TEST10).solvePart2() == 0;
        assert AoC2015_12.createDebug(TEST11).solvePart2() == 6;

        final Puzzle puzzle = Aocd.puzzle(2015, 12);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2015_12.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2015_12.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST1 = splitLines("[1,2,3]");
    private static final List<String> TEST2 = splitLines("{\"a\":2,\"b\":4}");
    private static final List<String> TEST3 = splitLines("[[[3]]]");
    private static final List<String> TEST4 = splitLines("{\"a\":{\"b\":4},\"c\":-1}");
    private static final List<String> TEST5 = splitLines("{\"a\":[-1,1]}");
    private static final List<String> TEST6 = splitLines("[-1,{\"a\":1}]");
    private static final List<String> TEST7 = splitLines("[]");
    private static final List<String> TEST8 = splitLines("{}");
    private static final List<String> TEST9 = splitLines("[1,{\"c\":\"red\",\"b\":2},3]");
    private static final List<String> TEST10 = splitLines("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}");
    private static final List<String> TEST11 = splitLines("[1,\"red\",5]");
}
