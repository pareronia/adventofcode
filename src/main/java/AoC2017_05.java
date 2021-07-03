import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;

import com.github.pareronia.aocd.Aocd;

public final class AoC2017_05 extends AoCBase {

    private final transient List<Integer> input;

    private AoC2017_05(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs.stream().map(Integer::valueOf).collect(toList());
    }

    public static AoC2017_05 create(final List<String> input) {
        return new AoC2017_05(input, false);
    }

    public static AoC2017_05 createDebug(final List<String> input) {
        return new AoC2017_05(input, true);
    }
    
    private Integer countJumps(final Function<Integer, Integer> jumpCalculator) {
        int cnt = 0;
        int i = 0;
        while (i < this.input.size()) {
            final int jump = this.input.get(i);
            this.input.set(i, jumpCalculator.apply(jump));
            i += jump;
            cnt++;
        }
        return cnt;
    }
    
    @Override
    public Integer solvePart1() {
        return countJumps(jump -> jump + 1);
    }
    
    @Override
    public Integer solvePart2() {
        return countJumps(jump -> jump >= 3 ? jump - 1 : jump + 1);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_05.createDebug(TEST).solvePart1() == 5;
        assert AoC2017_05.createDebug(TEST).solvePart2() == 10;

        final List<String> input = Aocd.getData(2017, 5);
        lap("Part 1", () -> AoC2017_05.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_05.create(input).solvePart2());
    }
    
    private static final List<String> TEST = splitLines(
            "0\n" +
            "3\n" +
            "0\n" +
            "1\n" +
            "-3"
    );
}
