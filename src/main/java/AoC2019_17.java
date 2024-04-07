import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.ListUtils;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aoc.solution.Logger;
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
    
    @Override
    public Integer solvePart1() {
        final CharGrid grid = new GridBuilder().build(
            new IntCodeComputer(this.program, this.debug).runCamera());
        log(grid);
        return grid.getAllEqualTo(SCAFFOLD)
            .filter(cell -> grid.getCapitalNeighbours(cell)
                        .allMatch(n -> grid.getValue(n) == SCAFFOLD))
            .mapToInt(cell -> cell.getRow() * cell.getCol())
            .sum();
    }

    private List<String> findInput(final CharGrid grid, final int maxSize, final int minRepeats) {
        final PathFinder pathFinder = new PathFinder(grid, this.debug);
        final List<Cell> path = pathFinder.findPath();
        log("Path: " + path);
        final List<Move> moves = pathFinder.toMoves(path);
        log("Moves: " + moves);
        final List<List<Command>> commands = pathFinder.toCommands(moves);
        log("Commands: " + commands);
        final List<Command> compressed = pathFinder.compressCommands(commands);
        log("Compressed: " + compressed.stream().map(Command::toString).collect(joining(", ")));
        final List<String> input = pathFinder.createAsciiInput(
                compressed, maxSize, minRepeats);
        log("Input: " + input);
        return input;
    }
    
    private int solve2(final int maxSize, final int minRepeats) {
        final IntCodeComputer computer = new IntCodeComputer(this.program, this.debug);
        final CharGrid grid = new GridBuilder().build(computer.runCamera());
        final List<String> input = findInput(grid, maxSize, minRepeats);
        return computer.runRobot(input).getLast().intValue();
    }
    
    @Override
    public Integer solvePart2() {
        return solve2(4, 3);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2019_17.createDebug(List.of("1")).findInput(CharGrid.from(TEST), 3, 2)
            .equals(List.of("A,B,C,B,A,C", "R,8,R,8", "R,4,R,4,R,8", "L,6,L,2", "n"));
        
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
    
    @RequiredArgsConstructor
    private static final class GridBuilder {

        public CharGrid build(final Deque<Long> output) {
            return CharGrid.from(asStrings(output));
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
    }
    
    @RequiredArgsConstructor
    private static final class Move {
        private final Cell from;
        private final Cell to;
        private final Direction direction;
        
        @Override
        public String toString() {
            return String.format("%s -> %s : %s", this.from, this.to, this.direction);
        }
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class Command {
        private final char letter;
        private final int count;
        
        @Override
        public String toString() {
            if (this.count == 1) {
                return String.valueOf(this.letter);
            } else {
                return String.format("%s(%d)", this.letter, this.count);
            }
        }
    }
    
    private static final class PathFinder {
        private final CharGrid grid;
        private final Logger logger;
        
        public PathFinder(final CharGrid grid, final boolean debug) {
            this.grid = grid;
            this.logger = new Logger(debug);
        }
        
        private void log(final Object obj) {
            this.logger.log(obj);
        }

        public List<Cell> findPath() {
            final Cell robot = grid.findAllMatching(Direction.CAPITAL_ARROWS::contains)
                .findFirst().orElseThrow();
            final Cell start = robot;
            final Cell end = grid.getAllEqualTo(SCAFFOLD)
                .filter(cell -> grid.getCapitalNeighbours(cell)
                    .filter(n -> grid.getValue(n) == SCAFFOLD || n.equals(robot))
                    .count() == 1)
                .findFirst().orElseThrow();
            final Deque<Cell> path = new ArrayDeque<>();
            final DFS dfs = new DFS(grid, path, start, end);
            dfs.dfs();
            return path.stream().collect(toList());
        }
        
        public List<Move> toMoves(final List<Cell> path) {
            final Cell start = path.get(0);
            final List<Move> moves = new ArrayList<>();
            moves.add(new Move(start, start, Direction.fromChar(grid.getValue(start))));
            range(path.size() - 1).forEach(i -> {
                final Direction move = path.get(i).to(path.get(i + 1));
                moves.add(new Move(path.get(i), path.get(i + 1), move));
            });
            return moves;
        }
        
        public List<List<Command>> toCommands(final List<Move> moves) {
            final List<List<Command>> commands = new ArrayList<>();
            final Deque<Command> curr = new ArrayDeque<>();
            range(moves.size() - 1).forEach(i -> {
                final Command last = curr.peekLast();
                if (moves.get(i).direction == moves.get(i + 1).direction){
                    curr.addLast(new Command(last.letter, 1));
                } else {
                    final Turn turn = Turn.fromDirections(
                            moves.get(i).direction, moves.get(i + 1).direction);
                    if (last != null) {
                        commands.add(curr.stream().collect(toList()));
                        curr.clear();
                    }
                    curr.addLast(new Command(turn.getLetter().get(), 1));
                }
            });
            commands.add(curr.stream().collect(toList()));
            return commands;
        }
        
        public List<Command> compressCommands(final List<List<Command>> commands) {
            final List<Command> compressed = new ArrayList<>();
            for (final List<Command> list : commands) {
                assert list.stream().map(c -> c.letter).distinct().count() == 1;
                compressed.add(new Command(list.get(0).letter, list.size()));
            }
            return compressed;
        }
        
        public List<String> createAsciiInput(final List<Command> compressed,
                final int maxSize, final int minRepeats) {
            List<Command> lst = new ArrayList<>(compressed);
            final Map<String, List<Command>> map = new HashMap<>();
            for (final String x : List.of("A", "B", "C")) {
                for (int i = maxSize; i >= 2; i--) {
                    if (i > lst.size()) {
                        continue;
                    }
                    final List<Integer> idxs = ListUtils.indexesOfSubList(
                                                    lst, lst.subList(0, i));
                    if (idxs.size() < minRepeats) {
                        continue;
                    } else {
                        map.put(x, new ArrayList<>(lst.subList(0, i)));
                        lst = ListUtils.subtractAll(lst, lst.subList(0, i));
                        break;
                    }
                }
            }
            log(map);
            final List<String> main = new ArrayList<>();
            lst = new ArrayList<>(compressed);
            while (!lst.isEmpty()) {
                for (final Entry<String, List<Command>> func : map.entrySet()) {
                    if (Collections.indexOfSubList(lst, func.getValue()) == 0) {
                        main.add(func.getKey());
                        lst = lst.subList(func.getValue().size(), lst.size());
                        log(lst);
                        break;
                    }
                }
            }
            log(main);
            final List<String> input = new ArrayList<>();
            input.add(main.stream().collect(joining(",")));
            List.of("A", "B", "C").stream()
                .map(x -> map.get(x).stream()
                        .map(c -> "%s,%d".formatted(c.letter, c.count))
                        .collect(joining(",")))
                .forEach(input::add);
            input.add("n");
            return input;
        }
        
        private static final class DFS {
            private final CharGrid grid;
            private final Deque<Cell> path;
            private final Set<State> seen;
            private final Predicate<State> isEnd;
            private State state;
            
            public DFS(
                final CharGrid grid,
                final Deque<Cell> path,
                final Cell start,
                final Cell end
            ) {
                this.grid = grid;
                this.path = path;
                this.seen = new HashSet<>();
                this.isEnd = state -> state.curr.equals(end);
                this.state = new State(start, start);
                this.path.addLast(start);
                this.seen.add(this.state);
            }
            
            public boolean dfs() {
                if (isEnd.test(this.state)) {
                    return true;
                }
                final Set<Cell> scaffolds = grid.getCapitalNeighbours(this.state.curr)
                    .filter(n -> grid.getValue(n) == SCAFFOLD)
                    .collect(toSet());
                Set<Cell> adjacent;
                if (scaffolds.size() == 4) {
                    adjacent = scaffolds.stream()
                            .filter(s -> s.getRow() == this.state.prev.getRow()
                            || s.getCol() == this.state.prev.getCol()).collect(toSet());
                } else {
                    adjacent = scaffolds;
                }
                for (final Cell cell : adjacent) {
                    final State newState = new State(this.state.curr, cell);
                    if (seen.contains(newState)) {
                        continue;
                    }
                    final State oldState = this.state;
                    this.state = newState;
                    path.addLast(cell);
                    seen.add(this.state);
                    if (dfs()) {
                        return true;
                    } else {
                        path.removeLast();
                        seen.remove(this.state);
                        this.state = oldState;
                    }
                }
                return false;
            }
            
            @RequiredArgsConstructor
            @EqualsAndHashCode
            @ToString
            private static final class State {
                private final Cell prev;
                private final Cell curr;
            }
        }
    }
    
    @RequiredArgsConstructor
    private static final class IntCodeComputer {
        private final List<Long> program;
        private final boolean debug;
        
        public Deque<Long> runCamera() {
            final IntCode intCode = new IntCode(this.program, this.debug);
            final Deque<Long> input = new ArrayDeque<>();
            final Deque<Long> output = new ArrayDeque<>();
            intCode.run(input, output);
            return output;
        }
        
        public Deque<Long> runRobot(final List<String> commands) {
            final List<Long> newProgram = new ArrayList<>();
            newProgram.add(2L);
            newProgram.addAll(this.program.subList(1, this.program.size()));
            final IntCode intCode = new IntCode(newProgram, this.debug);
            final Deque<Long> input = new ArrayDeque<>();
            final Deque<Long> output = new ArrayDeque<>();
            commands.forEach(s -> (s + NEWLINE).chars()
                    .mapToLong(Long::valueOf)
                    .forEach(input::addLast));
            intCode.run(input, output);
            return output;
        }
    }
}