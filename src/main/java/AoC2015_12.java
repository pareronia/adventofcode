import static com.github.pareronia.aoc.Utils.toAString;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.Json;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_12 extends SolutionBase<String, Integer, Integer> {
    
    private AoC2015_12(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_12 create() {
        return new AoC2015_12(false);
    }

    public static final AoC2015_12 createDebug() {
        return new AoC2015_12(true);
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
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
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
    protected String parseInput(final List<String> inputs) {
        assert inputs.size() == 1;
        return inputs.get(0);
    }

    @Override
    public Integer solvePart1(final String input) {
        return addAllNumbers1(input);
    }

    @Override
    public Integer solvePart2(final String input) {
        return addAllNumbers2(
                Json.fromJson("{content: " + input + "}", Map.class));
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "6"),
        @Sample(method = "part1", input = TEST2, expected = "6"),
        @Sample(method = "part1", input = TEST3, expected = "3"),
        @Sample(method = "part1", input = TEST4, expected = "3"),
        @Sample(method = "part1", input = TEST5, expected = "0"),
        @Sample(method = "part1", input = TEST6, expected = "0"),
        @Sample(method = "part1", input = TEST7, expected = "0"),
        @Sample(method = "part1", input = TEST8, expected = "0"),
        @Sample(method = "part2", input = TEST1, expected = "6"),
        @Sample(method = "part2", input = TEST9, expected = "4"),
        @Sample(method = "part2", input = TEST10, expected = "0"),
        @Sample(method = "part2", input = TEST11, expected = "6"),
    })
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        AoC2015_12.create().run();
    }
    
    private static final String TEST1 = "[1,2,3]";
    private static final String TEST2 = "{\"a\":2,\"b\":4}";
    private static final String TEST3 = "[[[3]]]";
    private static final String TEST4 = "{\"a\":{\"b\":4},\"c\":-1}";
    private static final String TEST5 = "{\"a\":[-1,1]}";
    private static final String TEST6 = "[-1,{\"a\":1}]";
    private static final String TEST7 = "[]";
    private static final String TEST8 = "{}";
    private static final String TEST9 = "[1,{\"c\":\"red\",\"b\":2},3]";
    private static final String TEST10 = "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}";
    private static final String TEST11 = "[1,\"red\",5]";
}
