import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_23 extends AoCBase {
    
    private static final char ELF = '#';
    private static final char GROUND = '.';
    private static final Direction N = Direction.of(-1, 0);
    private static final Direction NW = Direction.of(-1, -1);
    private static final Direction NE = Direction.of(-1, 1);
    private static final Direction S = Direction.of(1, 0);
    private static final Direction SW = Direction.of(1, -1);
    private static final Direction SE = Direction.of(1, 1);
    private static final Direction W = Direction.of(0, -1);
    private static final Direction E = Direction.of(0, 1);
    private static final Set<Direction> ALL_DIRS = Set.of(
            N, NE, E, SE, S, SW, W, NW);
    private static final Map<Direction, Set<Direction>> DIRS = Map.of(
            N, Set.of(N, NW, NE),
            S, Set.of(S, SW, SE),
            W, Set.of(W, NW, SW),
            E, Set.of(E, NE, SE));
             
    private final Set<Cell> input;

    private AoC2022_23(final List<String> input, final boolean debug) {
        super(debug);
		this.input = Grid.from(input)
		        .findAllMatching(ch -> ch == ELF)
		        .collect(toSet());
    }
    
    public static final AoC2022_23 create(final List<String> input) {
        return new AoC2022_23(input, false);
    }

    public static final AoC2022_23 createDebug(final List<String> input) {
        return new AoC2022_23(input, true);
    }
    
    private Bounds getBounds(final Set<Cell> elves) {
        final var statsRow = elves.stream()
                .mapToInt(Cell::getRow).summaryStatistics();
        final var statsCol = elves.stream()
                .mapToInt(Cell::getCol).summaryStatistics();
        return new Bounds(
            statsRow.getMin(), statsRow.getMax(),
            statsCol.getMin(), statsCol.getMax()
        );
    }
    
    private void draw(final Set<Cell> elves) {
        if (!this.debug) {
            return;
        }
        final var bounds = getBounds(elves);
        IntStream.rangeClosed(bounds.minRow, bounds.maxRow).forEach(r -> {
            log(IntStream.rangeClosed(bounds.minCol, bounds.maxCol)
                    .mapToObj(c -> elves.contains(Cell.at(r, c)) ? ELF : GROUND)
                    .collect(toAString()));
        });
    }
    
    private Cell add(final Cell cell, final Direction dir) {
        return Cell.at(
                cell.getRow() + dir.row, cell.getCol() + dir.col);
    }
    
    private boolean allNotOccupied(
            final Set<Cell> elves,
            final Cell elf,
            final Set<Direction> directions
    ) {
        return directions.stream()
                .map(dir -> add(elf, dir))
                .noneMatch(elves::contains);
    }
    
    private Map<Cell, List<Cell>> calculateMoves(
            final Set<Cell> elves, final Deque<Direction> order
    ) {
        final var moves = new HashMap<Cell, List<Cell>>();
        for (final var elf : elves) {
            if (allNotOccupied(elves, elf, ALL_DIRS)) {
                continue;
            }
            for (final var d : order) {
                if (allNotOccupied(elves, elf, DIRS.get(d))) {
                    final Cell n = add(elf, d);
                    moves.computeIfAbsent(n, k -> new ArrayList<>()).add(elf);
                    break;
                }
            }
        }
        return moves;
    }
    
    private void executeMoves(
            final Set<Cell> elves, final Map<Cell, List<Cell>> moves
    ) {
        moves.entrySet().stream()
            .filter(e -> e.getValue().size() == 1)
            .forEach(e -> {
                elves.remove(e.getValue().get(0));
                elves.add(e.getKey());
            });
    }

    @Override
    public Integer solvePart1() {
        final var elves = new HashSet<>(this.input);
        final var order = new ArrayDeque<>(List.of(N, S, W, E));
        IntStream.range(0, 10).forEach(i -> {
            log("Round " + (i + 1));
            final var moves = calculateMoves(elves, order);
            executeMoves(elves, moves);
            draw(elves);
            order.addLast(order.pollFirst());
        });
        final var bounds = getBounds(elves);
        return (bounds.maxRow - bounds.minRow + 1)
                * (bounds.maxCol - bounds.minCol + 1)
                - elves.size();
    }

    @Override
    public Integer solvePart2() {
        final var elves = new HashSet<>(this.input);
        final var order = new ArrayDeque<>(List.of(N, S, W, E));
        int cnt = 1;
        while (true) {
            final var moves = calculateMoves(elves, order);
            if (moves.size() == 0) {
                return cnt;
            }
            executeMoves(elves, moves);
            order.addLast(order.pollFirst());
            cnt++;
        }
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_23.createDebug(TEST).solvePart1() == 110;
        assert AoC2022_23.createDebug(TEST).solvePart2() == 20;

        final Puzzle puzzle = Aocd.puzzle(2022, 23);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_23.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_23.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines("""
        ....#..
        ..###.#
        #...#.#
        .#...##
        #.###..
        ##.#.##
        .#..#..
        """);

    private static final record Direction (
            int row,
            int col
    ) {
        public static Direction of(final int row, final int col) {
            return new Direction(row, col);
        }
    }
    private static final record Bounds (
            int minRow,
            int maxRow,
            int minCol,
            int maxCol
    ) {
    }
}
