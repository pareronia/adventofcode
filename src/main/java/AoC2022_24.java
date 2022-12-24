import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2022_24 extends AoCBase {
    private final List<Blizzard> blizzards;
    private final Position start;
    private final Position end;
    private final int width;
    private final int height;
    
    private AoC2022_24(final List<String> input, final boolean debug) {
        super(debug);
        this.width = input.get(0).length() - 2;
        this.height = input.size() - 2;
        this.start = Position.of(0, height);
        this.end = Position.of(width - 1, -1);
        this.blizzards = new ArrayList<>();
        IntStream.range(0, input.size()).forEach(y -> {
            IntStream.range(0, input.get(y).length()).forEach(x -> {
                final Position position = Position.of(x - 1,  height - y);
                final Heading heading;
                final char ch = input.get(y).charAt(x);
                if (ch == '>') {
                    heading = Headings.EAST.get();
                    this.blizzards.add(new Blizzard(position, heading));
                } else if (ch == 'v') {
                    heading = Headings.SOUTH.get();
                    this.blizzards.add(new Blizzard(position, heading));
                } else if (ch == '<') {
                    heading = Headings.WEST.get();
                    this.blizzards.add(new Blizzard(position, heading));
                } else if (ch == '^') {
                    heading = Headings.NORTH.get();
                    this.blizzards.add(new Blizzard(position, heading));
                }
            });
        });
        log("width: " + this.width);
        log("height: " + this.height);
        log("start: " + this.start);
        log("end: " + this.end);
        draw(this.blizzards.stream().map(b -> b.position).collect(toList()));
    }
    
    public static final AoC2022_24 create(final List<String> input) {
        return new AoC2022_24(input, false);
    }

    public static final AoC2022_24 createDebug(final List<String> input) {
        return new AoC2022_24(input, true);
    }
    
    private void draw(final List<Position> positions) {
        for (int y = this.height - 1; y >= 0; y--) {
            final StringBuilder sb = new StringBuilder();
            for (int x = 0; x < this.width; x++) {
                if (positions.contains(Position.of(x, y))) {
                    sb.append('*');
                } else {
                    sb.append('.');
                }
            }
            log(sb.toString());
        }
        log("");
    }
    
    private final Map<Pair<Position, Integer>, Boolean> blizzardCache = new HashMap<>();
    
    private boolean isBlizzard(final Position position, final int tick) {
        final Pair<Position, Integer> key = Pair.of(position, tick);
        if (this.blizzardCache.containsKey(key)) {
            return this.blizzardCache.get(key);
        }
        final boolean isBlizzard = this.blizzards.stream()
                .map(b -> b.at(tick, this.width, this.height))
                .anyMatch(position::equals);
        this.blizzardCache.put(key, isBlizzard);
        return isBlizzard;
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static final class State {
        private final int time;
        private final Position position;
    }
    
    private int solve(final Position start, final Position end, final int startTime) {
        final Deque<State> q = new ArrayDeque<>();
        q.add(new State(startTime, start));
        final Set<State> seen = new HashSet<>();
        int cnt = 0;
        while (!q.isEmpty()) {
            assert cnt < 5_000_000;
            cnt++;
            final State state = q.poll();
            if (state.position.equals(end)) {
                log("time: " + state.time);
                log("cnt: " + cnt);
                return state.time;
            }
            Stream.concat(Headings.CAPITAL.stream().map(Headings::get), Set.of(Heading.of(0, 0)).stream())
                .map(state.position::translate)
                .filter(p -> p.equals(start) || p.equals(end)
                        || (0 <= p.getX() && p.getX() < this.width
                            && 0 <= p.getY() && p.getY() < this.height))
                .forEach(n -> {
                    if (!isBlizzard(n, state.time + 1)) {
                        final State newState = new State(state.time + 1, n);
                        if (!seen.contains(newState)) {
                            seen.add(newState);
                            q.add(newState);
                        }
                    }
                });
        }
        log("cnt: " + cnt);
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
    @ToString
    @EqualsAndHashCode
    private static final class Blizzard {
        private final Position position;
        private final Heading heading;
        
        public Position at(final int tick, final int width, final int height) {
            int amount;
            if (horizontal()) {
                amount = tick % width;
            } else {
                amount = tick % height;
            }
            final Position newPosition = this.position.translate(this.heading, amount);
            final Position pp = Position.of((width + newPosition.getX()) % width, (height + newPosition.getY()) % height);
            assert 0 <= pp.getX() && pp.getX() < width && 0 <= pp.getY() && pp.getY() < height;
            return pp;
        }
        
        private boolean horizontal() {
            return this.heading.equals(Headings.EAST.get()) || this.heading.equals(Headings.WEST.get());
        }
    }
}
