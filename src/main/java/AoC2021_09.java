import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aocd.Aocd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_09 extends AoCBase {
    
    private static final Cell N = Cell.at(-1, 0);
    private static final Cell E = Cell.at(0, 1);
    private static final Cell S = Cell.at(1, 0);
    private static final Cell W = Cell.at(0, -1);
    private static final Set<Cell> NESW = Set.of(N, E, S, W);

    private final Grid grid;
    
    private AoC2021_09(final List<String> input, final boolean debug) {
        super(debug);
        final int[][] heights = new int[input.size()][input.get(0).length()];
        input.stream()
            .map(s -> StringOps.getDigitsPrimitive(s, s.length()))
            .collect(toList())
            .toArray(heights);
        this.grid = new Grid(heights);
    }
    
    public static final AoC2021_09 create(final List<String> input) {
        return new AoC2021_09(input, false);
    }

    public static final AoC2021_09 createDebug(final List<String> input) {
        return new AoC2021_09(input, true);
    }
    
    private Stream<Cell> findNeighbours(final Cell c) {
        return NESW.stream()
            .filter(n -> c.getRow() + n.getRow() >= 0)
            .filter(n -> c.getRow() + n.getRow() < this.grid.getHeight())
            .filter(n -> c.getCol() + n.getCol() >= 0)
            .filter(n -> c.getCol() + n.getCol() < this.grid.getWidth())
            .map(n -> Cell.at(c.getRow() + n.getRow(), c.getCol() + n.getCol()));
    }

    private Stream<Cell> findLows() {
        return this.grid.getCells()
            .filter(c -> findNeighbours(c).allMatch(
                        n -> this.grid.getValue(n) > this.grid.getValue(c)));
    }
    
    @Override
    public Integer solvePart1() {
        return findLows()
            .map(this.grid::getValue)
            .map(v -> v + 1)
            .collect(summingInt(Integer::intValue));
    }

    private int sizeOfBasinAroundLow(final Cell low) {
        final Set<Cell> basin = new HashSet<>();
        basin.add(low);
        while (true) {
            final Set<Cell> add = basin.stream()
                .flatMap(this::findNeighbours)
                .filter(n -> !basin.contains(n))
                .filter(n -> this.grid.getValue(n) != 9)
                .collect(toSet());
            if (add.isEmpty()) {
                break;
            }
            basin.addAll(add);
        }
        return basin.size();
    }

    @Override
    public Integer solvePart2() {
        return findLows()
            .map(this::sizeOfBasinAroundLow)
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .reduce(1, (a, b) -> a * b);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_09.create(TEST).solvePart1() == 15;
        assert AoC2021_09.create(TEST).solvePart2() == 1134;

        final List<String> input = Aocd.getData(2021, 9);
        lap("Part 1", () -> AoC2021_09.create(input).solvePart1());
        lap("Part 2", () -> AoC2021_09.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
        "2199943210\r\n" +
        "3987894921\r\n" +
        "9856789892\r\n" +
        "8767896789\r\n" +
        "9899965678"
    );
    
    @RequiredArgsConstructor(staticName = "at")
    @Getter
    @EqualsAndHashCode
    @ToString
    private static final class Cell {
        private final int row;
        private final int col;
    }
    
    @RequiredArgsConstructor
    private static final class Grid {
        private final int[][] heights;
        
        public int getWidth() {
            assert this.heights.length > 0;
            return this.heights[0].length;
        }
        
        public int getHeight() {
            return this.heights.length;
        }
        
        public int getValue(final Cell c) {
            return this.heights[c.getRow()][c.getCol()];
        }
        
        public Stream<Cell> getCells() {
            final Builder<Cell> builder = Stream.builder();
            for (int r = 0; r < this.getHeight(); r++) {
                for (int c = 0; c < getWidth(); c++) {
                    builder.add(Cell.at(r, c));
                }
            }
            return builder.build();
        }
    }
}
