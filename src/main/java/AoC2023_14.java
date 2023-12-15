import static com.github.pareronia.aoc.Utils.last;
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
    
    private int calcLoad(final CharGrid grid) {
        return grid.getAllEqualTo('O')
            .mapToInt(o -> grid.getHeight() - o.getRow())
            .sum();
    }
    
    private CharGrid spinCycle(CharGrid grid) {
        for (int i = 0; i < 4; i++) {
            this.redraw(grid, this.tiltUp(grid));
            grid = grid.rotate();
        }
        return grid;
     }

    @Override
    public Integer solvePart1(final CharGrid gridIn) {
        final CharGrid grid = gridIn.doClone();
        this.redraw(grid, this.tiltUp(grid));
        return this.calcLoad(grid);
    }
    
    @Override
    public Integer solvePart2(final CharGrid grid) {
        CharGrid g = grid.doClone();
        final Map<Set<Cell>, List<Integer>> map = new HashMap<>();
        final int total = 1_000_000_000;
        int cycles = 0;
        while (true) {
            cycles++;
            g = this.spinCycle(g);
            final Set<Cell> os = g.getAllEqualTo('O').collect(toSet());
            map.computeIfAbsent(os, k -> new ArrayList<>()).add(cycles);
            if (cycles > 100 && map.getOrDefault(os, List.of()).size() > 1) {
                final List<Integer> cycle = map.get(os);
                final int period = last(cycle, 1) - last(cycle, 2);
                final int loops = Math.floorDiv(total - cycles, period);
                final int left = total - (cycles + loops * period);
                log("cycles: %d, cycle: %s, period: %d, loops: %d, left: %d"
                        .formatted(cycles, cycle, period, loops, left));
                assert cycles + loops * period + left == total;
                for (int i = 0; i < left; i++) {
                    g = this.spinCycle(g);
                }
                return this.calcLoad(g);
            }
            if (cycles > 1000) {
                throw new IllegalStateException("Unsolvable");
            }
        }
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
