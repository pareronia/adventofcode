import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_08 extends AoCBase {
    
    private final Grid grid;
    
    private AoC2022_08(final List<String> input, final boolean debug) {
        super(debug);
        this.grid = Grid.from(input);
    }
    
    public static final AoC2022_08 create(final List<String> input) {
        return new AoC2022_08(input, false);
    }

    public static final AoC2022_08 createDebug(final List<String> input) {
        return new AoC2022_08(input, true);
    }
    
    private int value(final Cell cell) {
        return Integer.parseInt(String.valueOf(grid.getValueAt(cell)));
    }
    
    @Override
    public Integer solvePart1() {
        int ans = 2 * (grid.getHeight() + grid.getWidth()) - 4;
        for (int rr = 1; rr <= grid.getMaxRowIndex() - 1; rr++) {
            for (int cc = 1; cc <= grid.getMaxColIndex() - 1; cc++) {
                final Cell cell = Cell.at(rr, cc);
                final int val = value(cell);
                if (grid.getCellsN(cell).allMatch(c -> value(c) < val)
                        || grid.getCellsE(cell).allMatch(c -> value(c) < val)
                        || grid.getCellsS(cell).allMatch(c -> value(c) < val)
                        || grid.getCellsW(cell).allMatch(c -> value(c) < val)) {
                    ans++;
                }
            }
        }
        return ans;
    }

    @Override
    public Long solvePart2() {
        long ans = 0;
        for (int rr = 1; rr <= grid.getMaxRowIndex() - 1; rr++) {
            for (int cc = 1; cc <= grid.getMaxColIndex() - 1; cc++) {
                final Cell cell = Cell.at(rr, cc);
                log(cell);
                final int val = value(cell);
                int n = 0;
                boolean stop = false;
                for (final Cell c : grid.getCellsN(cell).collect(toList())) {
                    if (stop) {
                        break;
                    }
                    n++;
                    stop = value(c) >= val;
                }
                log(n);
                stop = false;
                int w = 0;
                for (final Cell c : grid.getCellsW(cell).collect(toList())) {
                    if (stop) {
                        break;
                    }
                    w++;
                    stop = value(c) >= val;
                }
                log(w);
                stop = false;
                int e = 0;
                for (final Cell c : grid.getCellsE(cell).collect(toList())) {
                    if (stop) {
                        break;
                    }
                    e++;
                    stop = value(c) >= val;
                }
                log(e);
                stop = false;
                int s = 0;
                for (final Cell c : grid.getCellsS(cell).collect(toList())) {
                    if (stop) {
                        break;
                    }
                    s++;
                    stop = value(c) >= val;
                }
                log(s);
                ans = Math.max(n * e * s * w, ans);
            }
        }
        log(ans);
        return ans;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_08.createDebug(TEST).solvePart1() == 21;
        assert AoC2022_08.createDebug(TEST).solvePart2() == 8;

        final Puzzle puzzle = Aocd.puzzle(2022, 8);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_08.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_08.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
          "30373\r\n"
        + "25512\r\n"
        + "65332\r\n"
        + "33549\r\n"
        + "35390"
    );
}
