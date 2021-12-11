import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.apache.commons.lang3.mutable.MutableInt;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_11 extends AoCBase {
    
    private final Grid grid;
    
    private AoC2021_11(final List<String> input, final boolean debug) {
        super(debug);
        final int[][] levels = new int[input.size()][input.get(0).length()];
        input.stream()
            .map(s -> StringOps.getDigitsPrimitive(s, s.length()))
            .collect(toList())
            .toArray(levels);
        this.grid = new Grid(levels);
    }
    
    private void printGrid() {
        if (!this.debug) {
            return;
        }
        Arrays.stream(this.grid.values).forEach(r ->
                log(Arrays.stream(r)
                        .mapToObj(Integer::valueOf)
                        .map(String::valueOf)
                        .collect(joining(" "))));
    }

    public static final AoC2021_11 create(final List<String> input) {
        return new AoC2021_11(input, false);
    }

    public static final AoC2021_11 createDebug(final List<String> input) {
        return new AoC2021_11(input, true);
    }
    
    private Stream<Cell> findNeighbours(final Cell c) {
        return Headings.OCTANTS.stream()
            .filter(n -> c.getRow() + n.getX() >= 0)
            .filter(n -> c.getRow() + n.getX() < this.grid.getHeight())
            .filter(n -> c.getCol() + n.getY() >= 0)
            .filter(n -> c.getCol() + n.getY() < this.grid.getWidth())
            .map(n -> Cell.at(c.getRow() + n.getX(), c.getCol() + n.getY()));
    }
    
    private int cycle() {
        final MutableInt flashes = new MutableInt(0);
        this.grid.getCells().forEach(this.grid::increment);
        while (true) {
            final Set<Cell> toFlash = this.grid.getCells()
                    .filter(c -> this.grid.getValue(c) > 9)
                    .collect(toSet());
            if (toFlash.size() == 0) {
                break;
            }
            toFlash.stream().forEach(f -> {
                findNeighbours(f)
                    .filter(n -> this.grid.getValue(n) != 0)
                    .forEach(this.grid::increment);
                this.grid.setValue(f, 0);
                flashes.increment();
            });
        }
        return flashes.intValue();
    }
    
    @Override
    public Integer solvePart1() {
        int cycle = 0;
        int flashes = 0;
        log("(" + cycle + ")");
        printGrid();
     
        for (cycle = 1; cycle <= 100; cycle++) {
            flashes += cycle();
            log("(" + cycle + ")");
            printGrid();
            log("flashes: " + flashes);
        }

        return flashes;
    }

    @Override
    public Object solvePart2() {
        return null;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_11.create(TEST).solvePart1() == 1656;
        assert AoC2021_11.create(TEST).solvePart2() == null;

        final Puzzle puzzle = Aocd.puzzle(2021, 11);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_11.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_11.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "5483143223\r\n" +
        "2745854711\r\n" +
        "5264556173\r\n" +
        "6141336146\r\n" +
        "6357385478\r\n" +
        "4167524645\r\n" +
        "2176841721\r\n" +
        "6882881134\r\n" +
        "4846848554\r\n" +
        "5283751526"
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
        private final int[][] values;
        
        public int getWidth() {
            assert this.values.length > 0;
            return this.values[0].length;
        }
        
        public int getHeight() {
            return this.values.length;
        }
        
        public int getValue(final Cell c) {
            return this.values[c.getRow()][c.getCol()];
        }
        
        public void setValue(final Cell c, final int value) {
            this.values[c.getRow()][c.getCol()] = value;
        }
        
        public void increment(final Cell c) {
            this.values[c.getRow()][c.getCol()]++;
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
