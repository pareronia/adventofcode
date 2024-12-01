import static com.github.pareronia.aoc.IterTools.enumerate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_13 extends SolutionBase<AoC2020_13.Notes, Integer, Long> {

    private AoC2020_13(final boolean debug) {
        super(debug);
    }

    public static AoC2020_13 create() {
        return new AoC2020_13(false);
    }

    public static AoC2020_13 createDebug() {
        return new AoC2020_13(true);
    }

    @Override
    protected Notes parseInput(final List<String> inputs) {
        return Notes.fromInput(inputs);
    }

    @Override
    public Integer solvePart1(final Notes notes) {
        return Stream.iterate(0, cnt -> cnt + 1)
            .flatMap(cnt -> notes.buses.stream().map(b -> new int[] { cnt, b.period }))
            .filter(a -> (notes.target + a[0]) % a[1] == 0)
            .map(a -> a[0] * a[1])
            .findFirst().orElseThrow();
    }

    @Override
    public Long solvePart2(final Notes notes) {
        long r = 0;
        long lcm = notes.buses.get(0).period();
        for (int i = 1; i < notes.buses.size(); i++) {
            final Bus bus = notes.buses.get(i);
            while ((r + bus.offset()) % bus.period() != 0) {
                r += lcm;
            }
            lcm *= bus.period();
        }
        return r;
    }

    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "295"),
        @Sample(method = "part2", input = TEST2, expected = "3417"),
        @Sample(method = "part2", input = TEST3, expected = "754018"),
        @Sample(method = "part2", input = TEST4, expected = "779210"),
        @Sample(method = "part2", input = TEST5, expected = "1261476"),
        @Sample(method = "part2", input = TEST6, expected = "1202161486"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2020_13.create().run();
    }

    private static final String TEST1 = """
            939
            7,13,x,x,59,x,31,19
            """;
    private static final String TEST2 = """
            999
            17,x,13,19
            """;
    private static final String TEST3 = """
            999
            67,7,59,61
            """;
    private static final String TEST4 = """
            999
            67,x,7,59,61
            """;
    private static final String TEST5 = """
            999
            67,7,x,59,61
            """;
    private static final String TEST6 = """
            999
            1789,37,47,1889
            """;

    record Bus(int period, int offset) {}

    record Notes(int target, List<Bus> buses) {

        public static Notes fromInput(final List<String> inputs) {
            final int target = Integer.parseInt(inputs.get(0));
            final List<Bus> buses = enumerate(Arrays.stream(inputs.get(1).split(",")))
                .filter(e -> !"x".equals(e.value()))
                .map(e -> new Bus(Integer.parseInt(e.value()), e.index()))
                .toList();
            return new Notes(target, buses);
        }
    }
}