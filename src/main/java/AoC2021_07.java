import static java.util.stream.Collectors.summarizingInt;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.github.pareronia.aocd.Aocd;

public class AoC2021_07 extends AoCBase {
    
    private final List<Integer> positions;
    
    private AoC2021_07(final List<String> input, final boolean debug) {
        super(debug);
        this.positions = Arrays.stream(input.get(0).split(","))
                .map(Integer::valueOf)
               .collect(toList());
    }

    public static final AoC2021_07 create(final List<String> input) {
        return new AoC2021_07(input, false);
    }

    public static final AoC2021_07 createDebug(final List<String> input) {
        return new AoC2021_07(input, true);
    }
    
    private int solve(final BiFunction<Integer, Integer, Integer> calc) {
        final IntSummaryStatistics summary = this.positions.stream()
                .collect(summarizingInt(Integer::intValue));
        return IntStream.rangeClosed(summary.getMin(), summary.getMax())
                .map(a -> this.positions.stream()
                            .mapToInt(Integer::valueOf)
                            .map(b -> calc.apply(a, b))
                            .sum())
                .min().orElseThrow();
    }
    
    @Override
    public Integer solvePart1() {
        return solve((a, b) -> Math.abs(a - b));
    }

    @Override
    public Integer solvePart2() {
        return solve((a, b) -> {
            final int diff = Math.abs(a - b);
            return (diff * (diff + 1)) / 2;
        });
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_07.create(TEST).solvePart1() == 37;
        assert AoC2021_07.create(TEST).solvePart2() == 168;

        final List<String> input = Aocd.getData(2021, 7);
        lap("Part 1", () -> AoC2021_07.create(input).solvePart1());
        lap("Part 2", () -> AoC2021_07.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
        "16,1,2,0,4,2,7,1,2,14"
    );
}
