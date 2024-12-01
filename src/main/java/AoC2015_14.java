import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.StringOps.splitLines;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_14 extends SolutionBase<List<AoC2015_14.Reindeer>, Integer, Integer> {
    
    private AoC2015_14(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_14 create() {
        return new AoC2015_14(false);
    }

    public static final AoC2015_14 createDebug() {
        return new AoC2015_14(true);
    }
    
    @Override
    protected List<Reindeer> parseInput(final List<String> inputs) {
        return inputs.stream().map(Reindeer::fromInput).collect(toList());
    }
    
    private int distance(final Reindeer reindeer, final int time) {
        final int periodDistance = reindeer.speed * reindeer.go;
        final int periodTime = reindeer.go + reindeer.stop;
        final int periods = time / periodTime;
        final int left = time % periodTime;
        if (left >= reindeer.go) {
            return periods * periodDistance + periodDistance;
        } else {
            return periods * periodDistance + reindeer.speed * left;
        }
    }

    private int solve1(final List<Reindeer> reindeer, final int time) {
        return reindeer.stream()
           .mapToInt(r -> distance(r, time))
           .max().orElseThrow();
    }

    private int solve2(final List<Reindeer> reindeer, final int time) {
        final Map<String, Integer> points = new HashMap<>();
        IntStream.rangeClosed(1, time).forEach(i -> {
            final Map<String, Integer> distances
                    = reindeer.stream().collect(toMap(r -> r.name, r -> distance(r, i)));
            final int lead = distances.values().stream().mapToInt(Integer::intValue).max().orElseThrow();
            distances.entrySet().stream()
                .filter(e -> e.getValue() == lead)
                .forEach(e -> points.merge(e.getKey(), 1, Integer::sum));
        });
        return points.values().stream().mapToInt(Integer::intValue).max().orElseThrow();
    }
    
    @Override
    public Integer solvePart1(final List<Reindeer> reindeer) {
        return solve1(reindeer, 2503);
    }
    
    @Override
    public Integer solvePart2(final List<Reindeer> reindeer) {
        return solve2(reindeer, 2503);
    }

    public static void main(final String[] args) throws Exception {
        final AoC2015_14 test = AoC2015_14.createDebug();
        final List<Reindeer> input = test.parseInput(TEST);
        assert test.solve1(input, 1000) == 1120;
        assert test.solve2(input, 1000) == 689;

        AoC2015_14.create().run();
    }
    
    private static final List<String> TEST = splitLines(
        "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.\r\n" +
        "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."
    );
    
    record Reindeer(String name, int speed, int go, int stop) {
        
        private static final String REGEXP
                = "([A-Za-z]+) can fly ([0-9]+) km/s for ([0-9]+) seconds," +
                        " but then must rest for ([0-9]+) seconds\\.";
        private static final Pattern pattern = Pattern.compile(REGEXP);
        
        public static Reindeer fromInput(final String input) {
           final Matcher m = pattern.matcher(input);
           assertTrue(m.matches(), () -> "No match found");
           return new Reindeer(m.group(1),
                   Integer.parseInt(m.group(2)),
                   Integer.parseInt(m.group(3)),
                   Integer.parseInt(m.group(4)));
        }
    }
}
