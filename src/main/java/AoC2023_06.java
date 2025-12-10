import static com.github.pareronia.aoc.itertools.IterTools.zip;
import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_06
        extends SolutionBase<List<AoC2023_06.Race>, Long, Long> {
    
    private AoC2023_06(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_06 create() {
        return new AoC2023_06(false);
    }
    
    public static AoC2023_06 createDebug() {
        return new AoC2023_06(true);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected List<Race> parseInput(final List<String> inputs) {
        final List<Long>[] values = IntStream.rangeClosed(0, 1)
            .mapToObj(i -> Arrays.stream(inputs.get(i).split(":")[1].split(" "))
                        .filter(s -> !s.isBlank())
                        .map(Long::parseLong)
                        .toList())
            .toArray(List[]::new);
        return zip(values[0], values[1]).stream()
            .map(z -> new Race(z.first(), z.second()))
            .toList();
    }
    
    @SuppressWarnings("unused")
    private long bruteForce(final Race race) {
       return LongStream.range(1, race.time())
               .filter(t -> t * (race.time() - t) > race.distance())
               .count();
    }
    
    private long equation(final Race race) {
        final double r = Math.sqrt(Math.pow(race.time(), 2) - 4 * race.distance());
        final double fx1 = (race.time() - r) / 2d;
        final double fx2 = (race.time() + r) / 2d;
        final long x1 = fx1 - ((long) fx1) > 0 ? (long) Math.ceil(fx1) : ((long) fx1) + 1;
        final long x2 = fx2 - ((long) fx2) > 0 ? (long) Math.floor(fx2) : ((long) fx2) - 1;
        return x2 - x1 + 1;
    }
    
    private long solve(final List<Race> races) {
        return races.stream()
            .mapToLong(this::equation)
            .reduce((a, b) -> a * b).getAsLong();
    }

    @Override
    public Long solvePart1(final List<Race> races) {
        return solve(races);
    }
    
    @Override
    public Long solvePart2(final List<Race> races) {
        final List<Function<Race, Long>> fs
                = List.<Function<Race, Long>> of(Race::time, Race::distance);
        final long[] a = IntStream.rangeClosed(0, 1)
            .mapToLong(i -> Long.parseLong(races.stream()
                    .map(r -> fs.get(i).apply(r))
                    .map(String::valueOf)
                    .collect(joining())))
            .toArray();
        return solve(List.of(new Race(a[0], a[1])));
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "288"),
        @Sample(method = "part2", input = TEST, expected = "71503"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_06.create().run();
    }
    
    record Race(long time, long distance) {}

    private static final String TEST = """
            Time:      7  15   30
            Distance:  9  40  200
            """;
}
