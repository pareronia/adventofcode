import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    private Long sumValues(final Map<Integer, Long> map) {
        return map.values().stream().collect(summingLong(Long::valueOf));
    }

    private long solve(final int days) {
        Map<Integer, Long> map = new HashMap<>();
        for (final Integer i : this.initial) {
            map.put(i, map.getOrDefault(i, 0L) + 1);
        }
        log("(Initial) " + sumValues(map) + ": " + map);
        for (int i = 0; i < days; i++) {
            final Map<Integer, Long> newMap = new HashMap<>();
            newMap.put(0, map.getOrDefault(1, 0L));
            newMap.put(1, map.getOrDefault(2, 0L));
            newMap.put(2, map.getOrDefault(3, 0L));
            newMap.put(3, map.getOrDefault(4, 0L));
            newMap.put(4, map.getOrDefault(5, 0L));
            newMap.put(5, map.getOrDefault(6, 0L));
            newMap.put(6, map.getOrDefault(7, 0L) + map.getOrDefault(0, 0L));
            newMap.put(7, map.getOrDefault(8, 0L));
            newMap.put(8, map.getOrDefault(0, 0L));
            final int day = i + 1;
            log(() -> "(" + day + ") " + sumValues(newMap) + ": " + newMap);
            map = newMap;
        }
        return sumValues(map);
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
