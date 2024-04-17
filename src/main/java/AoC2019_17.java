import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.StringOps.splitLines;
import static com.github.pareronia.aoc.Utils.concatAll;
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
import com.github.pareronia.aoc.solution.LoggerEnabled;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2019_17 extends SolutionBase<List<Long>, Integer, Integer> {
    
    private static final char SCAFFOLD = '#';
    private static final char NEWLINE = '\n';
    
    private AoC2019_17(final boolean debug) {
        super(debug);
    }

    public static AoC2019_17 create() {
        return new AoC2019_17(false);
    }

    public static AoC2019_17 createDebug() {
        return new AoC2019_17(true);
    }

    @Override
    protected List<Long> parseInput(final List<String> inputs) {
        return IntCode.parse(inputs.get(0));
    }
    
    @Override
    public Integer solvePart1(final List<Long> program) {
        final CharGrid grid = new GridBuilder().build(
            new IntCodeComputer(program, this.debug).runCamera());
        log(grid);
        return grid.getAllEqualTo(SCAFFOLD)
            .filter(cell -> grid.getCapitalNeighbours(cell)
                        .allMatch(n -> grid.getValue(n) == SCAFFOLD))
            .mapToInt(cell -> cell.getRow() * cell.getCol())
            .sum();
    }

    @Override
    public Integer solvePart2(final List<Long> program) {
        final IntCodeComputer computer = new IntCodeComputer(program, this.debug);
        final CharGrid grid = new GridBuilder().build(computer.runCamera());
        final List<String> input = new PathFinder(grid, this.logger).createAsciiInput(5, 3);
        return computer.runRobot(input).getLast().intValue();
    }

    @Override
    protected void samples() {
        assert new PathFinder(CharGrid.from(splitLines(TEST)), new Logger(true))
            .createAsciiInput(3, 2)
            .equals(List.of("A,B,C,B,A,C", "R,8,R,8", "R,4,R,4,R,8", "L,6,L,2", "n"));
    }

    public static void main(final String[] args) throws Exception {
        AoC2019_17.create().run();
    }
    
    private static final String TEST = """
            #######...#####
            #.....#...#...#
            #.....#...#...#
            ......#...#...#
            ......#...###.#
            ......#.....#.#
            ^########...#.#
            ......#.#...#.#
            ......#########
            ........#...#..
            ....#########..
            ....#...#......
            ....#...#......
            ....#...#......
            ....#####......
            """;
    
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
    
    record Command(char letter, int count) {
        
        @Override
        public String toString() {
            if (this.count == 1) {
                return String.valueOf(this.letter);
            } else {
                return String.format("%s(%d)", this.letter, this.count);
            }
        }
    }
    
    private static final class PathFinder implements LoggerEnabled {
        private final CharGrid grid;
        private final Logger logger;
        
        public PathFinder(final CharGrid grid, final Logger logger) {
            this.grid = grid;
            this.logger = logger;
        }

        @Override
        public Logger getLogger() {
            return this.logger;
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
        
        public List<Command> toCommands(final List<Direction> moves) {
            final List<List<Command>> commands = new ArrayList<>();
            final Deque<Command> curr = new ArrayDeque<>();
            range(moves.size() - 1).forEach(i -> {
                final Command last = curr.peekLast();
                if (moves.get(i) == moves.get(i + 1)){
                    curr.addLast(new Command(last.letter, 1));
                } else {
                    final Turn turn = Turn.fromDirections(
                            moves.get(i), moves.get(i + 1));
                    if (last != null) {
                        commands.add(curr.stream().collect(toList()));
                        curr.clear();
                    }
                    curr.addLast(new Command(turn.getLetter().get(), 1));
                }
            });
            commands.add(curr.stream().collect(toList()));
            return commands.stream()
                    .map(lst -> new Command(lst.get(0).letter, lst.size()))
                    .toList();
        }
        
        public List<String> createAsciiInput(
                final int maxSize, final int minRepeats
        ) {
            final List<Cell> path = this.findPath();
            log("Path: " + path);
            final List<Direction> moves = concatAll(
                List.of(Direction.fromChar(grid.getValue(path.get(0)))),
                range(path.size() - 1).intStream()
                    .mapToObj(i1 -> path.get(i1).to(path.get(i1 + 1)))
                    .toList());
            log("Moves: " + moves);
            final List<Command> commands = this.toCommands(moves);
            log("Commands: " + commands);
            List<Command> lst = new ArrayList<>(commands);
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
            assertTrue(map.size() == 3, () -> "could not find functions");
            final List<String> main = new ArrayList<>();
            lst = new ArrayList<>(commands);
            int cnt = 0;
            while (!lst.isEmpty()) {
                assertTrue(cnt < 100, () -> "infinite loop");
                for (final Entry<String, List<Command>> func : map.entrySet()) {
                    if (Collections.indexOfSubList(lst, func.getValue()) == 0) {
                        main.add(func.getKey());
                        lst = lst.subList(func.getValue().size(), lst.size());
                        log(lst);
                        break;
                    }
                }
                cnt++;
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
            log(input);
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
            
            record State(Cell prev, Cell curr) {}
        }
    }
    
    record IntCodeComputer(List<Long> program, boolean debug) {
        
        public Deque<Long> runCamera() {
            final IntCode intCode = new IntCode(this.program, this.debug);
            final Deque<Long> input = new ArrayDeque<>();
            final Deque<Long> output = new ArrayDeque<>();
            intCode.run(input, output);
            return output;
        }
        
        public Deque<Long> runRobot(final List<String> commands) {
            final List<Long> newProgram = concatAll(
                    List.of(2L),
                    this.program.subList(1, this.program.size()));
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