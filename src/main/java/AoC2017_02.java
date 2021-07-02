import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.math3.util.CombinatoricsUtils.combinationsIterator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import com.github.pareronia.aocd.Aocd;

public final class AoC2017_02 extends AoCBase {

    private final transient List<List<Integer>> input;
    
    private AoC2017_02(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs.stream()
                .map(s -> Arrays.stream(s.split("\\s+"))
                            .map(Integer::valueOf).collect(toList()))
                .collect(toList());
        log(this.input);
    }

    public static AoC2017_02 create(final List<String> input) {
        return new AoC2017_02(input, false);
    }

    public static AoC2017_02 createDebug(final List<String> input) {
        return new AoC2017_02(input, true);
    }
    
    private Integer differenceHighestLowest(final List<Integer> numbers) {
        final Integer max = numbers.stream()
                .max(Comparator.naturalOrder())
                .orElseThrow();
        final Integer min = numbers.stream()
                .min(Comparator.naturalOrder())
                .orElseThrow();
        return max - min;
    }
    
    private Integer evenlyDivisibleQuotient(final List<Integer> numbers) {
        final Iterator<int[]> combinations
                = combinationsIterator(numbers.size(), 2);
        while (combinations.hasNext()) {
            final int[] c = combinations.next();
            final int n1 = numbers.get(c[0]);
            final int n2 = numbers.get(c[1]);
            if (n1 > n2) {
                if (n1 % n2 == 0) {
                    return n1 / n2;
                }
            } else if (n2 % n1 == 0) {
                return n2 / n1;
            }
        }
        throw new IllegalStateException("Unsolvable");
    }
    
    private Integer sum(final Function<List<Integer>, Integer> mapper) {
        return this.input.stream()
                .map(mapper)
                .collect(summingInt(Integer::valueOf));
    }

    @Override
    public Integer solvePart1() {
        return sum(this::differenceHighestLowest);
    }
    
    @Override
    public Integer solvePart2() {
        return sum(this::evenlyDivisibleQuotient);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_02.createDebug(TEST1).solvePart1() == 18;
        assert AoC2017_02.createDebug(TEST2).solvePart2() == 9;

        final List<String> input = Aocd.getData(2017, 2);
        lap("Part 1", () -> AoC2017_02.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_02.create(input).solvePart2());
    }
    
    private static final List<String> TEST1 = splitLines(
            "5 1 9 5\n" +
            "7 5 3\n" +
            "2 4 6 8"
    );
    private static final List<String> TEST2 = splitLines(
            "5 9 2 8\n" +
            "9 4 7 3\n" +
            "3 8 6 5"
    );
}
