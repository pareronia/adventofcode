import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_15 extends SolutionBase<List<AoC2016_15.Disc>, Integer, Integer> {

    private AoC2016_15(final boolean debug) {
        super(debug);
    }

    public static AoC2016_15 create() {
        return new AoC2016_15(false);
    }

    public static AoC2016_15 createDebug() {
        return new AoC2016_15(true);
    }

    @Override
    protected List<Disc> parseInput(final List<String> inputs) {
        return inputs.stream().map(Disc::fromInput).toList();
    }

    private Integer solve(final List<Disc> discs) {
        int time = 0;
        while (true) {
            final int t = time;
            if (discs.stream().allMatch(d -> d.alignedAt(t))) {
                break;
            }
            time++;
        }
        return time;
    }

    @Override
    public Integer solvePart1(final List<Disc> discs) {
        return solve(discs);
    }

    @Override
    public Integer solvePart2(final List<Disc> discs) {
        final List<Disc> newDiscs = new ArrayList<>();
        newDiscs.addAll(discs);
        newDiscs.add(new Disc(11, 0, discs.size() + 1));
        return solve(newDiscs);
    }

    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "5"),
        @Sample(method = "part1", input = TEST2, expected = "1"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST1 =
            """
            Disc #1 has 5 positions; at time=0, it is at position 4.
            Disc #2 has 2 positions; at time=0, it is at position 1.
            """;
    private static final String TEST2 =
            """
            Disc #1 has 5 positions; at time=0, it is at position 3.
            Disc #2 has 2 positions; at time=0, it is at position 1.
            Disc #3 has 3 positions; at time=0, it is at position 2.
            """;

    record Disc(int period, int offset, int delay) {

        public static Disc fromInput(final String string) {
            final int[] nums = Utils.naturalNumbers(string);
            final int period = nums[1];
            final int position = nums[3];
            final int delay = nums[0];
            final int offset = (period - position) % period;
            return new Disc(period, offset, delay);
        }

        public boolean alignedAt(final Integer time) {
            return (time + this.delay) % this.period == this.offset;
        }
    }
}
