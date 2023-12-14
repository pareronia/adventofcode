import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_14
        extends SolutionBase<CharGrid, Integer, Integer> {
    
    private AoC2023_14(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_14 create() {
        return new AoC2023_14(false);
    }
    
    public static AoC2023_14 createDebug() {
        return new AoC2023_14(true);
    }
    
    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(inputs);
    }
    
    private Set<Cell> tiltUp(final CharGrid grid) {
        final Set<Cell> os = new HashSet<>();
        for (final int c : grid.colIndices()) {
            Cell cell = Cell.at(0, c);
            final Iterator<Cell> it = grid.getCellsS(cell).iterator();
            int lastCube = 0;
            int count = 0;
            while (true) {
                if (grid.getValue(cell) == '#') {
                    lastCube = cell.getRow() + 1;
                    count = 0;
                } else if (grid.getValue(cell) == 'O') {
                    os.add(Cell.at(lastCube + count, c));
                    count++;
                }
                if (!it.hasNext()) {
                    break;
                }
                cell = it.next();
            }
        }
        return os;
    }
    
    private void redraw(final CharGrid grid, final Set<Cell> os) {
        grid.getCells().forEach(cell -> {
            final char val = grid.getValue(cell);
            if (os.contains(cell)) {
                grid.setValue(cell, 'O');
            } else if (val != '#') {
                grid.setValue(cell, '.');
            }
        });
    }
    
    private int calcLoad(final CharGrid grid, final Set<Cell> os) {
        return os.stream()
            .mapToInt(o -> grid.getHeight() - o.getRow())
            .sum();
    }
    
    record SpinResult(CharGrid grid, Set<Cell> os) {}
    
    private SpinResult spinCycle(CharGrid grid) {
        this.redraw(grid, this.tiltUp(grid));
        grid = grid.rotate();
        this.redraw(grid, this.tiltUp(grid));
        grid = grid.rotate();
        this.redraw(grid, this.tiltUp(grid));
        grid = grid.rotate();
        this.redraw(grid, this.tiltUp(grid));
        grid = grid.rotate();
        final Set<Cell> os = grid.getAllEqualTo('O').collect(toSet());
        return new SpinResult(grid, os);
    }

    @Override
    public Integer solvePart1(final CharGrid grid) {
        final Set<Cell> os = this.tiltUp(grid);
        this.redraw(grid, os);
        return this.calcLoad(grid, os);
    }
    
    @Override
    public Integer solvePart2(final CharGrid grid) {
        CharGrid g = grid.doClone();
        final Map<Set<Cell>, List<Integer>> map = new HashMap<>();
        final int total = 1_000_000_000;
        SpinResult result = null;
        int cycles = 0;
        while (true) {
            cycles++;
            result = this.spinCycle(g);
            map.computeIfAbsent(result.os(), k -> new ArrayList<>()).add(cycles);
            g = result.grid;
            if (cycles > 100 && map.getOrDefault(result.os(), List.of()).size() > 1) {
                break;
            }
        }
        final List<Integer> cycle = map.get(result.os());
        final int period = cycle.get(cycle.size() - 1) - cycle.get(cycle.size() - 2);
        final int loops = Math.floorDiv(total - cycles, period);
        final int left = total - (cycles + loops * period);
        log("cycles: %d, cycle: %s, period: %d, loops: %d, left: %d".formatted(
                cycles, cycle, period, loops, left));
        assert cycles + loops * period + left == total;
        for (int i = 0; i < left; i++) {
            result = this.spinCycle(g);
            g = result.grid;
        }
        return this.calcLoad(result.grid(), result.os());
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "136"),
        @Sample(method = "part2", input = TEST, expected = "64"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_14.create().run();
    }

    private static final String TEST = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
            """;
}
