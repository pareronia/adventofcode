import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
    
    @Override
    public Integer solvePart1() {
        final int max = this.positions.stream().max(Comparator.naturalOrder()).orElseThrow();
        int min = Integer.MAX_VALUE;
        for (int p = 0; p <= max; p++) {
            int sum = 0;
            for (final int q : this.positions) {
                sum += Math.abs(p - q);
            }
            min = Math.min(min, sum);
        }
        return min;
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_07.create(TEST).solvePart1() == 37;
        assert AoC2021_07.create(TEST).solvePart2() == 0;

        final List<String> input = Aocd.getData(2021, 7);
        lap("Part 1", () -> AoC2021_07.create(input).solvePart1());
        lap("Part 2", () -> AoC2021_07.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
        "16,1,2,0,4,2,7,1,2,14"
    );
}
