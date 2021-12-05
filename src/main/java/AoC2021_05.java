import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;

public class AoC2021_05 extends AoCBase {
    
    private final Map<Cell, Integer> map = new HashMap<>();

    private AoC2021_05(final List<String> input, final boolean debug) {
        super(debug);
        for (final String s : input) {
            final List<Integer> p = Arrays.stream(s.split(" -> "))
                    .flatMap(q -> Arrays.stream(q.split(",")))
                    .map(Integer::valueOf)
                    .collect(toList());
            int x1 = p.get(0);
            int y1 = p.get(1);
            int x2 = p.get(2);
            int y2 = p.get(3);
            if (x1 == x2) {
                if (y1 > y2) {
                    final int temp = y2;
                    y2 = y1;
                    y1 = temp;
                }
                for (int y = y1; y <= y2; y++) {
                    final Cell cell = Cell.at(x1, y);
                    final Integer value = map.getOrDefault(cell, 0);
                    map.put(cell, value + 1);
                }
            } else if (y1 == y2) {
                if (x1 > x2) {
                    final int temp = x2;
                    x2 = x1;
                    x1 = temp;
                }
                for (int x = x1; x <= x2; x++) {
                    final Cell cell = Cell.at(x, y1);
                    final Integer value = map.getOrDefault(cell, 0);
                    map.put(cell, value + 1);
                }
            } else {
                continue;
            }
        }
        log(map);
    }

    public static final AoC2021_05 create(final List<String> input) {
        return new AoC2021_05(input, false);
    }

    public static final AoC2021_05 createDebug(final List<String> input) {
        return new AoC2021_05(input, true);
    }

    @Override
    public Integer solvePart1() {
        int count = 0;
        for (final Integer value : map.values()) {
            if (value >= 2) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_05.createDebug(TEST).solvePart1() == 5;
        assert AoC2021_05.createDebug(TEST).solvePart2() == 0;

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
