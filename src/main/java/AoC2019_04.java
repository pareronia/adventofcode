import static com.github.pareronia.aoc.Utils.asCharacterStream;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.github.pareronia.aoc.Counter;
import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2019_04 extends SolutionBase<Range, Long, Long> {
    
    private AoC2019_04(final boolean debug) {
        super(debug);
    }

    public static AoC2019_04 create() {
        return new AoC2019_04(false);
    }

    public static AoC2019_04 createDebug() {
        return new AoC2019_04(true);
    }

    @Override
    protected Range parseInput(final List<String> inputs) {
        final String[] split = inputs.get(0).split("-");
        return Range.between(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    private boolean doesNotDecrease(final String passw) {
        final char[] sorted = passw.toCharArray();
        Arrays.sort(sorted);
        for (int i = 0; i < passw.length(); i++) {
            if (passw.charAt(i) != sorted[i]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isValid1(final String string) {
        return doesNotDecrease(string) &&
            asCharacterStream(string).collect(toSet()).size() != string.length();
    }
    
    private boolean isValid2(final String string) {
        return doesNotDecrease(string) &&
            new Counter<>(asCharacterStream(string)).containsValue(2L);
    }
    
    private long countValid(final Range range, final Predicate<String> isValid) {
        return range.intStream()
                .mapToObj(String::valueOf)
                .filter(isValid::test)
                .count();
    }

    @Override
    public Long solvePart1(final Range range) {
        return countValid(range, this::isValid1);
    }

    @Override
    public Long solvePart2(final Range range) {
        return countValid(range, this::isValid2);
    }

    @Override
    public void samples() {
        final AoC2019_04 test = AoC2019_04.createDebug();
        assert test.isValid1("122345");
        assert test.isValid1("111123");
        assert test.isValid1("111111");
        assert !test.isValid1("223450");
        assert !test.isValid1("123789");
        assert test.isValid2("112233");
        assert !test.isValid2("123444");
        assert test.isValid2("111122");
    }

    public static void main(final String[] args) throws Exception {
        AoC2019_04.create().run();
    }
}