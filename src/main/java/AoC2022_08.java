import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
    
    public Set<Stream<Cell>> capitalDirections(final Cell cell) {
        return Set.of(grid.getCellsN(cell), grid.getCellsE(cell), grid.getCellsS(cell), grid.getCellsW(cell));
    }
    
    public Stream<Cell> ignoringBorders() {
        final Stream.Builder<Cell> builder = Stream.builder();
        for (int rr = 1; rr <= grid.getMaxRowIndex() - 1; rr++) {
            for (int cc = 1; cc <= grid.getMaxColIndex() - 1; cc++) {
                builder.add(Cell.at(rr, cc));
            }
        }
        return builder.build();
    }
    
    private boolean visibleFromOutside(final Cell cell) {
        final int val = value(cell);
        for (final Stream<Cell> direction : capitalDirections(cell)) {
            if (direction.allMatch(c -> value(c) < val)) {
                return true;
            }
        }
        return false;
    }
    
    private Integer viewingDistance(final Stream<Cell> direction, final int val) {
        int n = 0;
        boolean stop = false;
        for (final Cell c : direction.collect(toList())) {
            if (stop) {
                break;
            }
            n++;
            stop = value(c) >= val;
        }
        return n;
    }
    
    private Integer scenicScore(final Cell cell) {
        return capitalDirections(cell).stream()
                .map(direction -> viewingDistance(direction, value(cell)))
                .reduce(1, (a, b) -> a * b);
    }
    
    @Override
    public Integer solvePart1() {
        final int ans = 2 * (grid.getHeight() + grid.getWidth()) - 4;
        return (int) (ans + ignoringBorders().filter(this::visibleFromOutside).count());
    }

    @Override
    public Integer solvePart2() {
        return ignoringBorders().mapToInt(this::scenicScore).max().getAsInt();
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
        "30373\r\n" +
        "25512\r\n" +
        "65332\r\n" +
        "33549\r\n" +
        "35390"
    );
}
