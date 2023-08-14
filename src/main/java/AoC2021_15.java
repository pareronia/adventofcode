import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aoc.IntGrid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.graph.AStar;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_15 extends AoCBase {
    
    private static final Cell START = Cell.at(0,0);

    private final IntGrid grid;

    private AoC2021_15(final List<String> input, final boolean debug) {
        super(debug);
        this.grid = IntGrid.from(input);
    }
    
    public static final AoC2021_15 create(final List<String> input) {
        return new AoC2021_15(input, false);
    }

    public static final AoC2021_15 createDebug(final List<String> input) {
        return new AoC2021_15(input, true);
    }
    
    private int getRisk(final Cell cell) {
        int value = this.grid.getValue(
                        Cell.at(cell.getRow() % this.grid.getHeight(),
                                cell.getCol() % this.grid.getWidth()))
                + cell.getRow() / this.grid.getHeight()
                + cell.getCol() / this.grid.getWidth();
        while (value > 9) {
            value -= 9;
        }
        return value;
    }
    
    private Stream<Cell> findNeighbours(final Cell c, final int tiles) {
        return Direction.CAPITAL.stream()
            .map(d -> Cell.at(c.getRow() + d.getX(), c.getCol() + d.getY()))
            .filter(n -> n.getRow() >= 0)
            .filter(n -> n.getCol() >= 0)
            .filter(n -> n.getRow() < tiles * this.grid.getHeight())
            .filter(n -> n.getCol() < tiles * this.grid.getWidth());
    }
    
    private AStar.Result<Cell> runAStar(final int tiles) {
        final Cell end = Cell.at(
                tiles * this.grid.getHeight() - 1,
                tiles * this.grid.getWidth() - 1);
        return AStar.execute(
                START,
                end::equals,
                cell -> findNeighbours(cell, tiles),
                this::getRisk);
    }
    
    private int solve(final int tiles) {
        final Cell end = Cell.at(
                tiles * this.grid.getHeight() - 1,
                tiles * this.grid.getWidth() - 1);
        return (int) runAStar(tiles).getDistance(end);
    }
    
    private void visualize(final int tiles) {
        final Cell end = Cell.at(
                tiles * this.grid.getHeight() - 1,
                tiles * this.grid.getWidth() - 1);
        final Set<Cell> path = runAStar(tiles).getPath(end).stream().collect(toSet());
        IntStream.range(0, tiles * this.grid.getHeight()).forEach(row -> {
            final String string = IntStream.range(0, tiles * this.grid.getWidth())
                .mapToObj(col -> {
                    if (path.contains(Cell.at(row, col))) {
                        return '#';
                    } else {
                        return '.';
                    }
                })
                .collect(toAString());
            System.out.println(string);
        });
    }
    
    public void visualizePart1() {
        visualize(1);
    }
    
    public void visualizePart2() {
        visualize(5);
    }

    @Override
    public Integer solvePart1() {
        return solve(1);
    }
    
    @Override
    public Integer solvePart2() {
        return solve(5);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 1)) == 3;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 1)) == 3;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(11, 1)) == 4;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 2)) == 8;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 22)) == 1;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 32)) == 2;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(11, 1)) == 4;
        assert AoC2021_15.createDebug(TEST).solvePart1() == 40;
        AoC2021_15.createDebug(TEST).visualizePart1();
        assert AoC2021_15.createDebug(TEST).solvePart2() == 315;
        AoC2021_15.createDebug(TEST).visualizePart2();

        final Puzzle puzzle = Aocd.puzzle(2021, 15);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "1163751742\r\n" +
        "1381373672\r\n" +
        "2136511328\r\n" +
        "3694931569\r\n" +
        "7463417111\r\n" +
        "1319128137\r\n" +
        "1359912421\r\n" +
        "3125421639\r\n" +
        "1293138521\r\n" +
        "2311944581"
    );
}