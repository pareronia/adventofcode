import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;

/**
 * TODO Extract some geometry lib stuff from this
 */
public class AoC2021_05 extends AoCBase {
    
    private final List<String> inputs;

    private AoC2021_05(final List<String> input, final boolean debug) {
        super(debug);
        this.inputs = input;
    }

    private Map<Cell, Integer> parseMap(final List<String> input, final boolean diag) {
        final Map<Cell, Integer> map = new HashMap<>();
        for (final String s : input) {
            final List<Integer> p = Arrays.stream(s.split(" -> "))
                    .flatMap(q -> Arrays.stream(q.split(",")))
                    .map(Integer::valueOf)
                    .collect(toList());
            assert p.size() == 4;
            assert p.stream().allMatch(pp -> pp >= 0);
            final int x1 = p.get(0);
            final int y1 = p.get(1);
            final int x2 = p.get(2);
            final int y2 = p.get(3);
            final int mx = x1 == x2 ? 0 : (x1 < x2 ? 1 : -1);
            final int my = y1 == y2 ? 0 : (y1 < y2 ? 1 : -1);
            if (!diag && mx != 0 && my != 0) {
                continue;
            }
            final int len = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
            for (int i = 0; i <= len; i++) {
                final Cell cell = Cell.at(x1 + mx * i, y1 + my * i);
                map.put(cell, map.getOrDefault(cell, 0) + 1);
            }
        }
        return map;
    }

    public static final AoC2021_05 create(final List<String> input) {
        return new AoC2021_05(input, false);
    }

    public static final AoC2021_05 createDebug(final List<String> input) {
        return new AoC2021_05(input, true);
    }
    
    private int ans(final Map<Cell, Integer> map) {
        return (int) map.values().stream().filter(v -> v > 1).count();
    }

    @Override
    public Integer solvePart1() {
        final Map<Cell, Integer> map = parseMap(this.inputs, false);
        log(map);
        return ans(map);
    }

    @Override
    public Integer solvePart2() {
        final Map<Cell, Integer> map = parseMap(this.inputs, true);
        log(map);
        return ans(map);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_05.create(TEST).solvePart1() == 5;
        assert AoC2021_05.create(TEST).solvePart2() == 12;

        final List<String> input = Aocd.getData(2021, 5);
        lap("Part 1", () -> AoC2021_05.create(input).solvePart1());
        lap("Part 2", () -> AoC2021_05.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
        "0,9 -> 5,9\r\n" +
        "8,0 -> 0,8\r\n" +
        "9,4 -> 3,4\r\n" +
        "2,2 -> 2,1\r\n" +
        "7,0 -> 7,4\r\n" +
        "6,4 -> 2,0\r\n" +
        "0,9 -> 2,9\r\n" +
        "3,4 -> 1,4\r\n" +
        "0,0 -> 8,8\r\n" +
        "5,5 -> 8,2"
    );
}
