import static java.util.stream.Collectors.summarizingInt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.IntegerSequence.Range;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_20 extends AoCBase {
    
    private static final char DARK = '.';
    private static final char LIGHT = '#';
    
    private final ImageEnhancement ie;
    
    private AoC2021_20(final List<String> input, final boolean debug) {
        super(debug);
        final String algorithm = input.get(0);
        assert algorithm.length() == 512;
        final Set<Cell> lights = new HashSet<>();
        final List<String> imageLines = input.subList(2, input.size());
        input.subList(2, input.size());
        for (int i = 0; i < imageLines.size(); i++) {
            final String line = imageLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == LIGHT) {
                    lights.add(Cell.at(i, j));
                }
            }
        }
        this.ie = new ImageEnhancement(algorithm, lights);
    }
    
    public static final AoC2021_20 create(final List<String> input) {
        return new AoC2021_20(input, false);
    }

    public static final AoC2021_20 createDebug(final List<String> input) {
        return new AoC2021_20(input, true);
    }
    
    private int solve(final int cycles) {
        for (int i = 0; i < cycles; i++) {
            ie.run();
        }
        return ie.getLitPixels();
    }

    @Override
    public Integer solvePart1() {
        return solve(2);
    }
    
    @Override
    public Integer solvePart2() {
        return solve(50);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_20.createDebug(TEST).solvePart1() == 35;
        assert AoC2021_20.create(TEST).solvePart2() == 3351;

        final Puzzle puzzle = Aocd.puzzle(2021, 20);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_20.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_20.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##" +
        "#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###" +
        ".######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#." +
        ".#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#....." +
        ".#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.." +
        "...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#....." +
        "..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#\r\n" +
        "\r\n" +
        "#..#.\r\n" +
        "#....\r\n" +
        "##..#\r\n" +
        "..#..\r\n" +
        "..###"
    );
    
    private static final class ImageEnhancement {
        private final String algorithm;
        private final boolean isFlicker;
        private Set<Cell> data;
        private boolean storeDark = false;
        
        public ImageEnhancement(final String algorithm, final Set<Cell> data) {
            this.algorithm = algorithm;
            this.isFlicker = algorithm.charAt(0) == LIGHT
                    && algorithm.charAt(algorithm.length() - 1) == DARK;
            this.data = data;
        }

        private Range getRowRange() {
            final IntSummaryStatistics summary = this.data.stream()
                    .map(Cell::getRow)
                    .collect(summarizingInt(Integer::intValue));
            return new Range(summary.getMin() - 2, summary.getMax() + 2, 1);
        }

        private Range getColRange() {
            final IntSummaryStatistics summary = this.data.stream()
                    .map(Cell::getCol)
                    .collect(summarizingInt(Integer::intValue));
            return new Range(summary.getMin() - 2, summary.getMax() + 2, 1);
        }
        
        @SuppressWarnings("unused")
        public List<String> print() {
            final List<String> lines = new ArrayList<>();
            final Range rowRange = this.getRowRange();
            final Range colRange = this.getColRange();
            for (final int row : rowRange) {
                final StringBuilder sb = new StringBuilder();
                for (final int col : colRange) {
                    sb.append(this.data.contains(Cell.at(row, col)) ? LIGHT : DARK);
                }
                lines.add(String.valueOf(sb.toString()));
            }
            return lines;
        }
        
        private String getSquareAround(final int row, final int col) {
            final char found;
            final char notFound;
            if (!this.isFlicker || !this.storeDark) {
                found = '1';
                notFound = '0';
            } else {
                found = '0';
                notFound = '1';
            }
            final StringBuilder sb = new StringBuilder();
            for (final int rr : List.of(-1, 0, 1)) {
                for (final int cc : List.of(-1, 0, 1)) {
                    sb.append(this.data.contains(Cell.at(row + rr, col + cc))
                                ? found : notFound);
                }
            }
            return sb.toString();
        }
        
        public void run() {
            final Set<Cell> data2 = new HashSet<>();
            final Range rowRange = this.getRowRange();
            final Range colRange = this.getColRange();
            for (final int row : rowRange) {
                for (final int col : colRange) {
                    final String square = getSquareAround(row, col);
                    final int idx = Integer.parseInt(square, 2);
                    assert idx >= 0 && idx < this.algorithm.length();
                    final boolean light = this.algorithm.charAt(idx) == LIGHT;
                    if (((!this.isFlicker || this.storeDark) && light)
                        || (this.isFlicker && !this.storeDark && !light)) {
                        data2.add(Cell.at(row, col));
                    }
                }
            }
            this.data = data2;
            this.storeDark = !this.storeDark;
        }
        
        public int getLitPixels() {
            return this.data.size();
        }
    }
}