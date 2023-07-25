import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

public class AoC2022_24 extends AoCBase {
    private static final Set<Heading> DIRS = Set.of(
        Headings.NORTH.get(), Headings.EAST.get(), Headings.SOUTH.get(),
        Headings.WEST.get(), Heading.of(0, 0)
    );
    private static final Map<Character, Heading> BLIZZARD_DIRS = Map.of(
        '^', Headings.NORTH.get(), '>', Headings.EAST.get(),
        'v', Headings.SOUTH.get(), '<', Headings.WEST.get()
    );
    
    private final boolean[][][] blizzardsByTime;
    private final Position start;
    private final Position end;
    private final int width;
    private final int height;
    private final int period;
    
    private AoC2022_24(final List<String> input, final boolean debug) {
        super(debug);
        this.width = input.get(0).length() - 2;
        this.height = input.size() - 2;
        this.period = height * (width /
            BigInteger.valueOf(height).gcd(BigInteger.valueOf(width)).intValue());
        this.start = Position.of(0, height);
        this.end = Position.of(width - 1, -1);
        final List<Blizzard> blizzards = new ArrayList<>();
        IntStream.range(0, input.size()).forEach(y -> {
            for (int x = 0; x < input.get(y).length(); x++) {
                final char ch = input.get(y).charAt(x);
                if (ch != '.' && ch != '#') {
                    final Position position = Position.of(x - 1,  height - y);
                    blizzards.add(new Blizzard(position, BLIZZARD_DIRS.get(ch)));
                }
            }
        });
        this.blizzardsByTime = new boolean[this.period][this.width][this.height];
        for (final int i : range(this.period)) {
            for (final Blizzard blizzard : blizzards) {
                final Position b = blizzard.at(i, this.width, this.height);
                this.blizzardsByTime[i][b.getX()][b.getY()] = true;
            }
        }
        log("width: " + this.width);
        log("height: " + this.height);
        log("period: " + this.period);
        log("start: " + this.start);
        log("end: " + this.end);
    }
    
    public static final AoC2022_24 create(final List<String> input) {
        return new AoC2022_24(input, false);
    }

    public static final AoC2022_24 createDebug(final List<String> input) {
        return new AoC2022_24(input, true);
    }
    
    private boolean isBlizzard(final Position position, final int time) {
        return this.blizzardsByTime[time % this.period][position.getX()][position.getY()];
    }

    private boolean inBounds(final Position p) {
        return 0 <= p.getX() && p.getX() < this.width
            && 0 <= p.getY() && p.getY() < this.height;
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class State {
        private final int time;
        private final Position position;
    }
    
    private int solve(final Position start, final Position end, final int startTime) {
        final Deque<State> q = new ArrayDeque<>();
        q.add(new State(startTime, start));
        final Set<State> seen = new HashSet<>();
        int time = -1;
        while (!q.isEmpty()) {
            final State state = q.poll();
            if (state.position.equals(end)) {
                log("time: " + state.time);
                return state.time;
            }
            if (time < state.time) {
                time = state.time;
                seen.clear();
            }
            final int nextTime = state.time + 1;
            for (final Heading d : DIRS) {
                final Position n = state.position.translate(d);
                if (n.equals(start) || n.equals(end)
                        || inBounds(n) && !isBlizzard(n, nextTime)
                ) {
                    final State newState = new State(nextTime, n);
                    if (!seen.contains(newState)) {
                        seen.add(newState);
                        q.add(newState);
                    }
                }
            }
        }
        throw new IllegalStateException("Unsolvable");
    }

    @Override
    public Integer solvePart1() {
        return solve(this.start, this.end, 0);
    }

    @Override
    public Integer solvePart2() {
        final int there = solve(this.start, this.end, 0);
        final int thereAndBack = solve(this.end, this.start, there);
        final int thereAndBackAndThereAgain = solve(this.start, this.end, thereAndBack);
        return thereAndBackAndThereAgain;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_24.createDebug(TEST).solvePart1() == 18;
        assert AoC2022_24.createDebug(TEST).solvePart2() == 54;

        final Puzzle puzzle = Aocd.puzzle(2022, 24);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_24.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_24.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "#.######\r\n" +
        "#>>.<^<#\r\n" +
        "#.<..<<#\r\n" +
        "#>v.><>#\r\n" +
        "#<^v^^>#\r\n" +
        "######.#"
    );
    
    @RequiredArgsConstructor
    private static final class Blizzard {
        private final Position position;
        private final Heading heading;
        
        public Position at(final int tick, final int width, final int height) {
            final int amount = horizontal() ? tick % width : tick % height;
            final Position newPosition
                    = this.position.translate(this.heading, amount);
            final Position pp = Position.of(
                    (width + newPosition.getX()) % width,
                    (height + newPosition.getY()) % height);
            assert 0 <= pp.getX() && pp.getX() < width
                    && 0 <= pp.getY() && pp.getY() < height;
            return pp;
        }
        
        private boolean horizontal() {
            return this.heading.equals(Headings.EAST.get())
                    || this.heading.equals(Headings.WEST.get());
        }
    }
}
