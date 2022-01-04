import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2021_06 extends AoCBase {
    
    private final List<Integer> initial;
    
    private AoC2021_06(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.initial = Arrays.stream(input.get(0).split(","))
                .map(Integer::valueOf)
                .collect(toList());
    }

    public static final AoC2021_06 create(final List<String> input) {
        return new AoC2021_06(input, false);
    }

    public static final AoC2021_06 createDebug(final List<String> input) {
        return new AoC2021_06(input, true);
    }
    
    private long sumValues(final List<Long> list) {
        return list.stream().collect(summingLong(Long::valueOf));
    }

    private long solve(final int days) {
       final LinkedList<Long> fishies = new LinkedList<>(nCopies(9, 0L));
        for (final Integer i : this.initial) {
            fishies.set(i, fishies.get(i) + 1);
        }
        log(fishies, 0);
        for (int i = 0; i < days; i++) {
            final long zeroes = fishies.pollFirst();
            fishies.addLast(zeroes);
            fishies.set(6, fishies.get(6) + zeroes);
            log(fishies, i + 1);
        }
        return sumValues(fishies);
    }

    private void log(final LinkedList<Long> fishies, final int day) {
        log(() -> "(" + day + ") " + sumValues(fishies) + ": " + fishies);
    }
    
    @Override
    public Long solvePart1() {
        return solve(80);
    }

    @Override
    public Long solvePart2() {
        return solve(256);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_06.create(TEST).solvePart1() == 5_934;
        assert AoC2021_06.create(TEST).solvePart2() == 26_984_457_539L;

        final List<String> input = Aocd.getData(2021, 6);
        lap("Part 1", () -> AoC2021_06.create(input).solvePart1());
        lap("Part 2", () -> AoC2021_06.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
        "3,4,3,1,2"
    );
}
