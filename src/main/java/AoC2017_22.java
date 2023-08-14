import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public final class AoC2017_22 extends AoCBase {
    
    private final Map<Position, State> input;
    private final Position start;
    
    private AoC2017_22(final List<String> inputs, final boolean debug) {
        super(debug);
        final List<String> lines = new ArrayList<>(inputs);
        Collections.reverse(lines);
        final int size = lines.size();
        this.input = Stream.iterate(0, i -> i < size, i -> i + 1)
                .flatMap(r -> IntStream.range(0, size)
                                    .mapToObj(c -> Position.of(r, c)))
                .collect(toMap(
                    p -> p,
                    p -> State.fromValue(lines.get(p.getY()).charAt(p.getX()))));
        this.start = Position.of(size / 2, size / 2);
    }

    public static AoC2017_22 create(final List<String> input) {
        return new AoC2017_22(input, false);
    }

    public static AoC2017_22 createDebug(final List<String> input) {
        return new AoC2017_22(input, true);
    }
    
    private int solve(
        final Map<Position, State> nodes,
        final Map<State, State> states,
        final Map<State, Turn> turns,
        final Position start,
        final int bursts
    ) {
        Position position = start;
        Direction direction = Direction.UP;
        int count = 0;
        for (int i = 0; i < bursts; i++) {
            final State currentState = nodes.getOrDefault(position, State.CLEAN);
            final State newState = states.get(currentState);
            nodes.put(position, newState);
            if (newState == State.INFECTED) {
                count++;
            }
            final Turn turn = turns.get(currentState);
            if (turn != null) {
                direction = direction.turn(turn);
            }
            position = Position.of(
                position.getX() + direction.getX(),
                position.getY() + direction.getY());
        }
        return count;
    }
    
    @Override
    public Integer solvePart1() {
        final Map<Position, State> nodes = new HashMap<>(this.input);
        final Map<State, State> states = Map.of(
                State.CLEAN, State.INFECTED,
                State.INFECTED, State.CLEAN);
        final Map<State, Turn> turns = Map.of(
                State.CLEAN, Turn.LEFT,
                State.INFECTED, Turn.RIGHT);
        return solve(nodes, states, turns, this.start, 10_000);
    }
    
    @Override
    public Integer solvePart2() {
        final Map<Position, State> nodes = new HashMap<>(this.input);
        final Map<State, State> states = Map.of(
                State.CLEAN, State.WEAKENED,
                State.WEAKENED, State.INFECTED,
                State.INFECTED, State.FLAGGED,
                State.FLAGGED, State.CLEAN);
        final Map<State, Turn> turns = Map.of(
                State.CLEAN, Turn.LEFT,
                State.INFECTED, Turn.RIGHT,
                State.FLAGGED, Turn.AROUND);
        return solve(nodes, states, turns, this.start, 10_000_000);
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
    private enum State {
        CLEAN('.'), WEAKENED('W'), INFECTED('#'), FLAGGED('F');

        private final char value;

        public static State fromValue(final char value) {
            return EnumSet.allOf(State.class).stream()
                    .filter(s -> s.value == value)
                    .findFirst().orElseThrow();
        }
    }
}
