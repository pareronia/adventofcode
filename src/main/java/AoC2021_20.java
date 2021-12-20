import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_20 extends AoCBase {
    
    private static final char DARK = '.';
    private static final char LIGHT = '#';
    
    private final String algorithm;
    private final Set<Cell> image;
    
    private AoC2021_20(final List<String> input, final boolean debug) {
        super(debug);
        this.algorithm = input.get(0);
        assert this.algorithm.length() == 512;
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
        this.image = lights;
    }
    
    public static final AoC2021_20 create(final List<String> input) {
        return new AoC2021_20(input, false);
    }

    public static final AoC2021_20 createDebug(final List<String> input) {
        return new AoC2021_20(input, true);
    }
    
    private Set<Cell> cycle(final Set<Cell> image, final boolean flip, final boolean storedOn) {
        final Set<Cell> newImage = new HashSet<>();
        final int rowMin = image.stream().mapToInt(Cell::getRow).min().orElseThrow();
        final int rowMax = image.stream().mapToInt(Cell::getRow).max().orElseThrow();
        final int colMin = image.stream().mapToInt(Cell::getCol).min().orElseThrow();
        final int colMax = image.stream().mapToInt(Cell::getCol).max().orElseThrow();
        for (int row = rowMin - 5; row <= rowMax + 5; row++) {
            for (int col = colMin - 5; col <= colMax + 5; col++) {
                final StringBuilder sb = new StringBuilder();
                for (final int rr : List.of(-1, 0, 1)) {
                    for (final int cc : List.of(-1, 0, 1)) {
                        final boolean found = image.contains(Cell.at(row + rr, col + cc));
                        sb.append(found == (!flip || storedOn) ? '1' : '0');
                    }
                }
                final String value = sb.toString();
                assert value.length() == 9;
                final int idx = Integer.parseInt(value, 2);
                final char correction = this.algorithm.charAt(idx);
                assert idx >= 0 && idx < 512;
                if ((correction == LIGHT) == (!flip || !storedOn)) {
                    newImage.add(Cell.at(row, col));
                }
            }
        }
        return newImage;
    }
    
    private void printImage(final Set<Cell> image) {
        if (!this.debug) {
            return;
        }
        final int rowMin = image.stream().mapToInt(Cell::getRow).min().orElseThrow();
        final int rowMax = image.stream().mapToInt(Cell::getRow).max().orElseThrow();
        final int colMin = image.stream().mapToInt(Cell::getCol).min().orElseThrow();
        final int colMax = image.stream().mapToInt(Cell::getCol).max().orElseThrow();
        for (int row = rowMin - 5; row <= rowMax + 5; row++) {
            final StringBuilder sb = new StringBuilder();
            for (int col = colMin - 5; col <= colMax + 5; col++) {
                sb.append(image.contains(Cell.at(row, col)) ? LIGHT : DARK);
            }
            log(String.valueOf(sb.toString()));
        }
        log("");
    }
    
    private int solve(final int cycles) {
        Set<Cell> temp = this.image;
        printImage(temp);
        final boolean flip = this.algorithm.charAt(0) == LIGHT;
        for (int i = 0; i < cycles; i++) {
            final boolean storedOn = i % 2 == 0;
            temp = cycle(temp, flip, storedOn);
            printImage(temp);
        }
        return temp.size();
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
        assert AoC2021_20.create(TEST).solvePart1() == 35;
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
}