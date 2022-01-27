import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2015_14 extends AoCBase {
    
    private static final String REGEXP
            = "([A-Za-z]+) can fly ([0-9]+) km/s for ([0-9]+) seconds, but then must rest for ([0-9]+) seconds\\.";
    
    private final List<Reindeer> reindeer;

    private AoC2015_14(final List<String> inputs, final boolean debug) {
        super(debug);
        final Pattern pattern = Pattern.compile(REGEXP);
        this.reindeer = inputs.stream()
            .map(input -> {
                final Matcher m = pattern.matcher(input);
                if (m.matches()) {
                    return new Reindeer(m.group(1),
                            Integer.valueOf(m.group(2)),
                            Integer.valueOf(m.group(3)),
                            Integer.valueOf(m.group(4)));
                } else {
                    throw new IllegalArgumentException();
                }
            })
            .collect(toList());
    }

    public static final AoC2015_14 create(final List<String> input) {
        return new AoC2015_14(input, false);
    }

    public static final AoC2015_14 createDebug(final List<String> input) {
        return new AoC2015_14(input, true);
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
    
    private int solve1(final int time) {
        return this.reindeer.stream()
           .mapToInt(r -> distance(r, time))
           .max().orElseThrow();
    }

    private int solve2(final int time) {
        final Map<String, Integer> points = new HashMap<>();
        IntStream.rangeClosed(1, time).forEach(i -> {
            final Map<String, Integer> distances
                    = this.reindeer.stream().collect(toMap(r -> r.name, r -> distance(r, i)));
            final int lead = distances.values().stream().mapToInt(Integer::intValue).max().orElseThrow();
            distances.entrySet().stream()
                .filter(e -> e.getValue() == lead)
                .forEach(e -> points.merge(e.getKey(), 1, Integer::sum));
        });
        return points.values().stream().mapToInt(Integer::intValue).max().orElseThrow();
    }
    
    @Override
    public Integer solvePart1() {
        return solve1(2503);
    }
    
    @Override
    public Integer solvePart2() {
        return solve2(2503);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_14.createDebug(TEST).solve1(1000) == 1120;
        assert AoC2015_14.createDebug(TEST).solve2(1000) == 689;

        final Puzzle puzzle = Aocd.puzzle(2015, 14);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2015_14.createDebug(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2015_14.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST = splitLines(
            "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.\r\n"
            + "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."
    );
    
    @RequiredArgsConstructor
    @ToString
    private static final class Reindeer {
        private final String name;
        private final int speed;
        private final int go;
        private final int stop;
    }
}
