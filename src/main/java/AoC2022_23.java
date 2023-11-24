import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public class AoC2022_23 extends AoCBase {
    
    private static final char ELF = '#';
    private static final char GROUND = '.';
    private static final Map<Direction, Set<Direction>> DIRS = Map.of(
        Direction.UP, Set.of(Direction.UP, Direction.LEFT_AND_UP, Direction.RIGHT_AND_UP),
        Direction.DOWN, Set.of(Direction.DOWN, Direction.LEFT_AND_DOWN, Direction.RIGHT_AND_DOWN),
        Direction.LEFT, Set.of(Direction.LEFT, Direction.LEFT_AND_DOWN, Direction.LEFT_AND_UP),
        Direction.RIGHT, Set.of(Direction.RIGHT, Direction.RIGHT_AND_UP, Direction.RIGHT_AND_DOWN));
             
    private final Set<Cell> input;

    private AoC2022_23(final List<String> input, final boolean debug) {
        super(debug);
		this.input = CharGrid.from(input)
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
        final IntSummaryStatistics statsRow = elves.stream()
                .mapToInt(Cell::getRow).summaryStatistics();
        final IntSummaryStatistics statsCol = elves.stream()
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
        final Bounds bounds = getBounds(elves);
        IntStream.rangeClosed(bounds.minRow, bounds.maxRow).forEach(r -> {
            log(IntStream.rangeClosed(bounds.minCol, bounds.maxCol)
                    .mapToObj(c -> elves.contains(Cell.at(r, c)) ? ELF : GROUND)
                    .collect(toAString()));
        });
    }
    
    private boolean allNotOccupied(
            final Set<Cell> elves,
            final Cell elf,
            final Set<Direction> directions
    ) {
        return directions.stream()
                .map(dir -> elf.at(dir))
                .noneMatch(elves::contains);
    }
    
    private Map<Cell, List<Cell>> calculateMoves(
            final Set<Cell> elves, final Deque<Direction> order
    ) {
        final Map<Cell, List<Cell>> moves = new HashMap<>();
        for (final Cell elf : elves) {
            if (allNotOccupied(elves, elf, Direction.OCTANTS)) {
                continue;
            }
            for (final Direction d : order) {
                if (allNotOccupied(elves, elf, DIRS.get(d))) {
                    final Cell n = elf.at(d);
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
        final Set<Cell> elves = new HashSet<>(this.input);
        final Deque<Direction> order = new ArrayDeque<>(
            List.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));
        IntStream.range(0, 10).forEach(i -> {
            log("Round " + (i + 1));
            final Map<Cell, List<Cell>> moves = calculateMoves(elves, order);
            executeMoves(elves, moves);
            draw(elves);
            order.addLast(order.pollFirst());
        });
        final Bounds bounds = getBounds(elves);
        return (bounds.maxRow - bounds.minRow + 1)
                * (bounds.maxCol - bounds.minCol + 1)
                - elves.size();
    }

    @Override
    public Integer solvePart2() {
        final Set<Cell> elves = new HashSet<>(this.input);
        final Deque<Direction> order = new ArrayDeque<>(
            List.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));
        int cnt = 1;
        while (true) {
            final Map<Cell, List<Cell>> moves = calculateMoves(elves, order);
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

    private static final List<String> TEST = splitLines(
        "....#..\r\n" +
        "..###.#\r\n" +
        "#...#.#\r\n" +
        ".#...##\r\n" +
        "#.###..\r\n" +
        "##.#.##\r\n" +
        ".#..#.."
    );

    @RequiredArgsConstructor
    private static final class Bounds {
        private final int minRow;
        private final int maxRow;
        private final int minCol;
        private final int maxCol;
    }
}
