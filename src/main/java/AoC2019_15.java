import static com.github.pareronia.aoc.Utils.concat;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
            input.add((long) move.value);
            intCode.runTillHasOutput(input, output);
            final Status status = Status.fromValue(output.pop().intValue());
            if (status != Status.WALL) {
                input.add((long) move.reverse().value);
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
            location.moves.forEach(m -> input.add((long) m.value));
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
    
    @RequiredArgsConstructor
    private class DFS {
        private final Set<Position> positions;
        private int max = 0;
        private final Set<Position> seen = new HashSet<>();
        
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
            return Headings.CAPITAL.stream()
                    .map(Headings::get)
                    .map(position::translate)
                    .filter(positions::contains)
                    .collect(toSet());
        }
    }
    
    @Override
    public Integer solvePart1() {
        return new FloodFill().floodFill(Location.START).stream()
                .filter(l -> l.isO2)
                .map(Location::getMoves)
                .map(List::size)
                .findFirst().orElseThrow();
    }
    
    @Override
    public Integer solvePart2() {
        final Set<Location> locations
                = new FloodFill().floodFill(Location.START);
        final Set<Position> positions = locations.stream()
                .map(Location::getPosition)
                .collect(toSet());
        final Position posO2 = locations.stream()
                .filter(Location::isO2).map(Location::getPosition)
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
        NORTH(1), SOUTH(2), WEST(3), EAST(4);
        
        private int value;

        Move(final int value) {
            this.value = value;
        }
        
        private Heading asHeading() {
            switch (this) {
            case NORTH:
                return Headings.NORTH.get();
            case SOUTH:
                return Headings.SOUTH.get();
            case WEST:
                return Headings.WEST.get();
            case EAST:
                return Headings.EAST.get();
            }
            throw new IllegalArgumentException();
        }
        
        public Position applyTo(final Position position) {
            return position.translate(this.asHeading());
        }
        
        public Move reverse() {
            switch (this) {
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            case EAST:
                return WEST;
            }
            throw new IllegalArgumentException();
            
        }
    }
    
    private enum Status {
        WALL(0), MOVED(1), FOUND_O2(2);
        
        private int value;
        
        Status(final int value) {
            this.value = value;
        }
        
        public static Status fromValue(final int value) {
            return Arrays.stream(values())
                .filter(v -> v.value == value)
                .findFirst().orElseThrow();
        }
    }
    
    @RequiredArgsConstructor
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static final class Location {
        
        public static final Location START = new Location(Position.of(0, 0), false, List.of());
        
        @Getter
        @EqualsAndHashCode.Include
        private final Position position;
        @Getter
        private final boolean isO2;
        @Getter
        private final List<Move> moves;
    }
}