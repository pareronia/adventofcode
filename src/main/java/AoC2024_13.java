import static com.github.pareronia.aoc.StringOps.splitOnce;
import static java.util.stream.Collectors.summingLong;

import java.util.List;
import java.util.Optional;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_13
        extends SolutionBase<List<AoC2024_13.Machine>, Long, Long> {
    
    private AoC2024_13(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_13 create() {
        return new AoC2024_13(false);
    }
    
    public static AoC2024_13 createDebug() {
        return new AoC2024_13(true);
    }
    
    @Override
    protected List<Machine> parseInput(final List<String> inputs) {
        return StringOps.toBlocks(inputs).stream()
                .map(Machine::fromInput)
                .toList();
    }
    
    private long solve(final List<Machine> machines, final long offset) {
        return machines.stream()
                .map(m -> m.calcTokens(offset))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(summingLong(Long::longValue));
    }
    
    @Override
    public Long solvePart1(final List<Machine> machines) {
        return solve(machines, 0);
    }
    
    @Override
    public Long solvePart2(final List<Machine> machines) {
        return solve(machines, 10_000_000_000_000L);
    }
    
    @Override
    @Samples({ @Sample(method = "part1", input = TEST, expected = "480") })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_13.create().run();
    }

    private static final String TEST = """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
            """;

    record Machine(long ax, long bx, long ay, long by, long px, long py) {

        public static Machine fromInput(final List<String> input) {
            final long ax = Long.parseLong(input.get(0).substring(12, 14));
            final long ay = Long.parseLong(input.get(0).substring(18, 20));
            final long bx = Long.parseLong(input.get(1).substring(12, 14));
            final long by = Long.parseLong(input.get(1).substring(18, 20));
            final StringSplit sp = splitOnce(input.get(2), ", ");
            final long px = Long.parseLong(splitOnce(sp.left(), "=").right());
            final long py = Long.parseLong((sp.right().substring(2)));
            return new Machine(ax, bx, ay, by, px, py);
        }
        
        public Optional<Long> calcTokens(final long offset) {
            final long px = this.px + offset;
            final long py = this.py + offset;
            final double div = this.bx * this.ay - this.ax * this.by;
            final double a = (py * this.bx - px * this.by) / div;
            final double b = (px * this.ay - py * this.ax) / div;
            if ((a % 1) == 0 && (b % 1) == 0) {
                return Optional.of((long) a * 3 + (long) b);
            } else {
                return Optional.empty();
            }
        }
    }
}