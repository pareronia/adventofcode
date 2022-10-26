import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.With;

public final class AoC2017_22 extends AoCBase {
    
    private static final String INFECTED = "#";
    
    private final Set<Position> input;
    private final Carrier carrier;
    
    private AoC2017_22(final List<String> inputs, final boolean debug) {
        super(debug);
        final List<String> lines = new ArrayList<>(inputs);
        Collections.reverse(lines);
        final int size = inputs.size();
        this.input = Stream.iterate(0, i -> i < size, i -> i + 1)
                .flatMap(r -> IntStream.range(0, size).mapToObj(c -> Position.of(r, c)))
                .filter(p -> INFECTED.equals(lines.get(p.getY()).substring(p.getX(), p.getX() + 1)))
                .collect(toSet());
        this.carrier = new Carrier(Position.of(size / 2, size / 2), Headings.NORTH.get(), 0);
    }

    public static AoC2017_22 create(final List<String> input) {
        return new AoC2017_22(input, false);
    }

    public static AoC2017_22 createDebug(final List<String> input) {
        return new AoC2017_22(input, true);
    }
    
    private int solve(
            final int bursts,
            final Function<Position, State> getCurrentState,
            final Function<State, State> calcNewState,
            final BiFunction<Heading, AoC2017_22.State, Heading> calcNewHeading,
            final BiConsumer<Position, State> setNewState
    ) {
        Carrier carrier = this.carrier;
        for (int i = 0; i < bursts; i++) {
            final State currentState = getCurrentState.apply(carrier.position);
            final Heading heading = calcNewHeading.apply(carrier.heading, currentState);
            final State newState = calcNewState.apply(currentState);
            setNewState.accept(carrier.position, newState);
            carrier = carrier
                    .withCount(newState == State.INFECTED ? carrier.count + 1 : carrier.count)
                    .withHeading(heading)
                    .withPosition(carrier.position.translate(heading));
        }
        return carrier.count;
    }
    
    @Override
    public Integer solvePart1() {
        final Set<Position> nodes = new HashSet<>(this.input);
        final Function<Position, State> getCurrentState = p -> (nodes.contains(p) ? State.INFECTED : State.CLEAN);
        final BiFunction<Heading, State, Heading> calcNewHeading = (h, s) -> {
            if (s == State.CLEAN) {
                return h.rotate(-90);
            } else {
                return h.rotate(90);
            }
        };
        final Function<State, State> calcNewState = s -> s == State.CLEAN ? State.INFECTED : State.CLEAN;
        final BiConsumer<Position, State> setNewState = (p, s) -> {
            if (s == State.CLEAN) {
                nodes.remove(p);
            } else {
                nodes.add(p);
            }
        };
        return solve(10_000, getCurrentState, calcNewState, calcNewHeading, setNewState);
    }
    
    @Override
    public Integer solvePart2() {
        final Map<Position, State> nodes = this.input.stream()
                .collect(toMap(p -> p, p -> State.INFECTED));
        final Function<Position, State> getCurrentState = p -> nodes.getOrDefault(p, State.CLEAN);
        final BiFunction<Heading, State, Heading> calcNewHeading = (h, s) -> {
            if (s == State.CLEAN) {
                return h.rotate(-90);
            } else if (s == State.INFECTED) {
                return h.rotate(90);
            } else if (s == State.FLAGGED) {
                return h.rotate(180);
            } else {
                return h;
            }
        };
        final Function<State, State> calcNewState = s -> {
            if (s == State.CLEAN) {
                return State.WEAKENED;
            } else if (s == State.INFECTED) {
                return State.FLAGGED;
            } else if (s == State.FLAGGED) {
                return State.CLEAN;
            } else {
                return State.INFECTED;
            }
        };
        final BiConsumer<Position, State> setNewState = (p, s) -> {
            if (s == State.CLEAN) {
                nodes.remove(p);
            } else {
                nodes.put(p, s);
            }
        };
        return solve(10_000_000, getCurrentState, calcNewState, calcNewHeading, setNewState);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_22.createDebug(TEST).solvePart1() == 5587;
        assert AoC2017_22.createDebug(TEST).solvePart2() == 2511944;

        final Puzzle puzzle = Aocd.puzzle(2017, 22);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
        "..#\r\n" +
        "#..\r\n" +
        "..."
    );
    
    @RequiredArgsConstructor
    @Getter
    @With
    @ToString
    private static final class Carrier {
        private final Position position;
        private final Heading heading;
        private final int count;
    }
    
    private enum State { CLEAN, WEAKENED, INFECTED, FLAGGED }
}
