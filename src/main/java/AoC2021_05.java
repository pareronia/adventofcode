import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;

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
                if (!diag) {
                    continue;
                }
                final List<Integer> xs = new ArrayList<>();
                if (x1 <= x2) {
                    for (int x = x1; x <= x2; x++) {
                        xs.add(x);
                    }
                } else {
                    for (int x = x1; x >= x2; x--) {
                        xs.add(x);
                    }
                }
                final List<Integer> ys = new ArrayList<>();
                if (y1 <= y2) {
                    for (int y = y1; y <= y2; y++) {
                        ys.add(y);
                    }
                } else {
                    for (int y = y1; y >= y2; y--) {
                        ys.add(y);
                    }
                }
                assert xs.size() == ys.size();
                for (int i = 0; i < xs.size(); i++) {
                    final Cell cell = Cell.at(xs.get(i), ys.get(i));
                    final Integer value = map.getOrDefault(cell, 0);
                    map.put(cell, value + 1);
                }
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
    
    private Integer ans(final Map<Cell, Integer> map) {
        int count = 0;
        for (final Integer value : map.values()) {
            if (value >= 2) {
                count++;
            }
        }
        return count;
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
