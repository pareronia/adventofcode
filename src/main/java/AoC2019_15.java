import static com.github.pareronia.aoc.Utils.concat;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_15 extends AoCBase {
    
    private final List<Long> program;
    
    private AoC2019_15(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_15 create(final List<String> input) {
        return new AoC2019_15(input, false);
    }

    public static AoC2019_15 createDebug(final List<String> input) {
        return new AoC2019_15(input, true);
    }
    
    private class FloodFill {
        
        private Status tryMove(final IntCode intCode, final Move move) {
            final Deque<Long> input = new ArrayDeque<>();
            final Deque<Long> output = new ArrayDeque<>();
            input.add(move.value);
            intCode.runTillHasOutput(input, output);
            final Status status = Status.fromValue(output.pop());
            if (status != Status.WALL) {
                input.add(move.reverse().value);
                intCode.runTillHasOutput(input, output);
            }
            return status;
        }
        
        private Optional<Location> move(
                final IntCode intCode,
                final Location location,
                final Move move
        ) {
            final Status status = tryMove(intCode, move);
            if (status == Status.MOVED || status == Status.FOUND_O2) {
                return Optional.of(new Location(
                        move.applyTo(location.position),
                        status == Status.FOUND_O2,
                        concat(location.moves, move)));
            } else {
                return Optional.empty();
            }
        }
        
        private Stream<Location> adjacent(final Location location) {
            final IntCode intCode
                    = new IntCode(AoC2019_15.this.program, AoC2019_15.this.debug);
            final Deque<Long> input = new ArrayDeque<>();
            location.moves.forEach(m -> input.add(m.value));
            intCode.runTillInputRequired(input, new ArrayDeque<>());
            return Arrays.stream(Move.values())
                    .map(move -> move(intCode, location, move))
                    .filter(Optional::isPresent)
                    .map(Optional::get);
        }
        
        public Set<Location> floodFill(final Location start) {
            return BFS.floodFill(start, this::adjacent);
        }
    }
    
    private class DFS {
        private final Set<Position> positions;
        private int max = 0;
        private final Set<Position> seen = new HashSet<>();
        
        protected DFS(final Set<Position> positions) {
            this.positions = positions;
        }

        public int dfs(final Position start) {
            if (seen.size() > max) {
                max = seen.size();
            }
            for (final Position n : adjacent(start)) {
                if (seen.contains(n)) {
                    continue;
                }
                seen.add(n);
                dfs(n);
                seen.remove(n);
            }
            return max;
        }
        
        private Set<Position> adjacent(final Position position) {
            return position.capitalNeighbours()
                    .filter(positions::contains)
                    .collect(toSet());
        }
    }
    
    @Override
    public Integer solvePart1() {
        return new FloodFill().floodFill(Location.START).stream()
                .filter(l -> l.isO2)
                .map(Location::moves)
                .map(List::size)
                .findFirst().orElseThrow();
    }
    
    @Override
    public Integer solvePart2() {
        final Set<Location> locations
                = new FloodFill().floodFill(Location.START);
        final Set<Position> positions = locations.stream()
                .map(Location::position)
                .collect(toSet());
        final Position posO2 = locations.stream()
                .filter(Location::isO2).map(Location::position)
                .findFirst().orElseThrow();
        return new DFS(positions).dfs(posO2);
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2019, 15);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_15.createDebug(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_15.create(inputData)::solvePart2)
        );
    }
    
    private enum Move {
        NORTH(Direction.UP, 1),
        SOUTH(Direction.DOWN, 2),
        WEST(Direction.LEFT, 3),
        EAST(Direction.RIGHT, 4);
        
        private final long value;
        private final Direction direction;

        Move(final Direction direction, final int value) {
            this.value = value;
            this.direction = direction;
        }
        
        public Position applyTo(final Position position) {
            return position.translate(this.direction);
        }
        
        public Move reverse() {
            return Arrays.stream(Move.values())
                .filter(v -> v.direction == this.direction.turn(Turn.AROUND))
                .findFirst().orElseThrow();
        }
    }
    
    private enum Status {
        WALL(0), MOVED(1), FOUND_O2(2);
        
        private final long value;
        
        Status(final int value) {
            this.value = value;
        }
        
        public static Status fromValue(final long value) {
            return Arrays.stream(values())
                .filter(v -> v.value == value)
                .findFirst().orElseThrow();
        }
    }
    
    record Location(Position position, boolean isO2, List<Move> moves) {
        
        public static final Location START = new Location(Position.of(0, 0), false, List.of());

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Location other = (Location) obj;
            return Objects.equals(position, other.position);
        }
    }
}