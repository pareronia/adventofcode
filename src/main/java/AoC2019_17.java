import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2019_17 extends AoCBase {
    
    private static final char SCAFFOLD = '#';
    private static final char NEWLINE = '\n';
    
    private final List<Long> program;
    
    private AoC2019_17(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_17 create(final List<String> input) {
        return new AoC2019_17(input, false);
    }

    public static AoC2019_17 createDebug(final List<String> input) {
        return new AoC2019_17(input, true);
    }
    
    private List<String> asStrings(final Deque<Long> output) {
        final List<String> strings = new ArrayList<>();
        final StringBuilder sb = new StringBuilder();
        while (!output.isEmpty()) {
            final char out = (char) (long) output.pop();
            if (out == NEWLINE && sb.length() > 0) {
                strings.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(out);
            }
        }
        return strings;
    }
    
    private CharGrid buildGrid() {
        final IntCode intCode = new IntCode(this.program, this.debug);
        final Deque<Long> input = new ArrayDeque<>();
        final Deque<Long> output = new ArrayDeque<>();
        intCode.run(input, output);
        final List<String> strings = asStrings(output);
        return CharGrid.from(strings);
    }
    
    @Override
    public Integer solvePart1() {
        final CharGrid grid = buildGrid();
        return grid.getAllEqualTo(SCAFFOLD)
            .filter(cell -> grid.getCapitalNeighbours(cell)
                        .allMatch(n -> grid.getValue(n) == SCAFFOLD))
            .mapToInt(cell -> cell.getRow() * cell.getCol())
            .sum();
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @ToString
    private static final class State {
        @EqualsAndHashCode.Include
        private final Cell prev;
        @EqualsAndHashCode.Include
        private final Cell curr;
    }
    
    private boolean dfs(
            final CharGrid grid,
            final Deque<Cell> path,
            final Set<State> seen,
            final State start,
            final Predicate<State> isEnd
    ) {
        if (isEnd.test(start)) {
            return true;
        }
        final Set<Cell> scaffolds = grid.getCapitalNeighbours(start.curr)
            .filter(n -> grid.getValue(n) == SCAFFOLD)
            .collect(toSet());
        Set<Cell> adjacent;
        if (scaffolds.size() == 4) {
            adjacent = scaffolds.stream()
                    .filter(s -> s.getRow() == start.prev.getRow()
                    || s.getCol() == start.prev.getCol()).collect(toSet());
        } else {
            adjacent = scaffolds;
        }
        for (final Cell cell : adjacent) {
            final State state = new State(start.curr, cell);
            if (seen.contains(state)) {
                continue;
            }
            path.addLast(cell);
            seen.add(state);
            if (dfs(grid, path, seen, state, isEnd)) {
                return true;
            } else {
                path.removeLast();
                seen.remove(state);
            }
        }
        return false;
    }

    private List<String> getPath(final CharGrid grid) {
        final Cell robot = grid.findAllMatching(Direction.CAPITAL_ARROWS::contains)
            .findFirst().orElseThrow();
        log(String.format("start: %s", robot));
        final Cell end = grid.getAllEqualTo(SCAFFOLD)
            .filter(cell -> grid.getCapitalNeighbours(cell)
                                .filter(n -> grid.getValue(n) == SCAFFOLD)
                                .count() == 1)
            .findFirst().orElseThrow();
        log(String.format("end: %s", end));
        final Predicate<State> isEnd = state -> state.curr.equals(end);
        final Deque<Cell> path = new ArrayDeque<>(List.of(robot));
        final State startState = new State(robot, robot);
        final Set<State> seen = new HashSet<>(List.of(startState));
        dfs(grid, path, seen, startState, isEnd);
//        log(path);
        log(path.size());
        final List<Cell> list = path.stream().collect(toList());
        assert list.get(0).equals(robot);
        final List<Direction> moves = new ArrayList<>();
        moves.add(Direction.fromChar(grid.getValue(robot)));
        range(list.size() - 1).forEach(i -> {
            final Direction move = list.get(i).to(list.get(i + 1));
            log(String.format("%s -> %s : %s", list.get(i), list.get(i + 1), move));
            moves.add(move);
        });
        final Deque<Character> program = new ArrayDeque<>();
        range(moves.size() - 1).forEach(i -> {
            final Character last = program.peekLast();
            if (moves.get(i) == moves.get(i + 1)) {
                program.addLast(last);
            } else {
                final Turn turn = Turn.fromDirections(moves.get(i), moves.get(i + 1));
                if (last != null) {
                    program.addLast('/');
                }
                program.addLast(turn.getLetter().get());
            }
        });
        program.addLast('/');
        log(program);
        final List<String> chars = new ArrayList<>();
        final Deque<Character> tmp = new ArrayDeque<>();
        tmp.addLast(program.removeFirst());
        while (!program.isEmpty()) {
            final Character ch = program.removeFirst();
            final Character last = tmp.peekLast();
            if (ch == '/') {
                final int size = tmp.size();
                tmp.clear();
                chars.add(last.toString());
                chars.add(String.valueOf(size));
            } else {
                tmp.addLast(ch);
            }
        }
        log(chars);
        return chars;
    }
    
    @Override
    public Integer solvePart2() {
        final CharGrid grid = buildGrid();
        log(grid);
        getPath(grid);
        // A = L, 4, L, 6, L, 8, L, 12
        // B = L, 8, R, 12, L, 12
        // C = R, 12, L, 6, L, 6, L, 8
        //
        // A, B, B, A, B, C, A, C, B, C
        final String a = List.of("L", "4", "L", "6", "L", "8", "L", "12").stream().collect(joining(","));
        final String b = List.of("L", "8", "R", "12", "L", "12").stream().collect(joining(","));
        final String c = List.of("R", "12", "L", "6", "L", "6", "L", "8").stream().collect(joining(","));
        final String main = List.of("A", "B", "B", "A", "B", "C", "A", "C", "B", "C").stream().collect(joining(","));

        final List<Long> newProgram = new ArrayList<>();
        newProgram.add(2L);
        newProgram.addAll(this.program.subList(1, this.program.size()));
        final IntCode intCode = new IntCode(newProgram, this.debug);
        final Deque<Long> input = new ArrayDeque<>();
        final Deque<Long> output = new ArrayDeque<>();
        intCode.runTillInputRequired(input, output);
        log(asStrings(output));
        for (final char ch : main.toCharArray()) {
            input.addLast((long) ch);
        }
        input.addLast((long) NEWLINE);
        intCode.runTillInputRequired(input, output);
        log(asStrings(output));
        for (final char ch : a.toCharArray()) {
            input.addLast((long) ch);
        }
        input.addLast((long) NEWLINE);
        intCode.runTillInputRequired(input, output);
        log(asStrings(output));
        for (final char ch : b.toCharArray()) {
            input.addLast((long) ch);
        }
        input.addLast((long) NEWLINE);
        intCode.runTillInputRequired(input, output);
        log(asStrings(output));
        for (final char ch : c.toCharArray()) {
            input.addLast((long) ch);
        }
        input.addLast((long) NEWLINE);
        intCode.runTillInputRequired(input, output);
        log(asStrings(output));
        input.addLast((long) 'n');
        input.addLast((long) NEWLINE);
        while (!intCode.isHalted()) {
            intCode.runTillHasOutput(input, output);
            final long out = output.pop();
            if (out > 255) {
                return (int) out;
            }
        }
        throw new IllegalStateException("Unsolvable");
    }


    public static void main(final String[] args) throws Exception {
        assert AoC2019_17.createDebug(List.of("1")).getPath(CharGrid.from(TEST))
            .stream().collect(joining(","))
            .equals("R,8,R,8,R,4,R,4,R,8,L,6,L,2,R,4,R,4,R,8,R,8,R,8,L,6,L,2");
        
        final Puzzle puzzle = Aocd.puzzle(2019, 17);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_17.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_17.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
            "#######...#####\r\n" +
            "#.....#...#...#\r\n" +
            "#.....#...#...#\r\n" +
            "......#...#...#\r\n" +
            "......#...###.#\r\n" +
            "......#.....#.#\r\n" +
            "^########...#.#\r\n" +
            "......#.#...#.#\r\n" +
            "......#########\r\n" +
            "........#...#..\r\n" +
            "....#########..\r\n" +
            "....#...#......\r\n" +
            "....#...#......\r\n" +
            "....#...#......\r\n" +
            "....#####......"
    );
}