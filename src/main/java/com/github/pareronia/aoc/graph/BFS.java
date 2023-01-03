package com.github.pareronia.aoc.graph;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid.Cell;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class BFS {
    
    public static int execute(
            final Cell start,
            final Predicate<Cell> isEnd,
            final Function<Cell, Stream<Cell>> adjacent
    ) {
        final Deque<State> q = new ArrayDeque<>(Set.of(new State(start, 0)));
        final Set<Cell> seen = new HashSet<>(Set.of(start));
        while (!q.isEmpty()) {
            final State state = q.poll();
            if (isEnd.test(state.cell)) {
                return state.distance;
            }
            adjacent.apply(state.cell)
                .filter(n -> !seen.contains(n))
                .forEach(n -> {
                    seen.add(n);
                    q.add(new State(n, state.distance + 1));
                });
        }
        throw new IllegalStateException("Unsolvable");
    }

    @RequiredArgsConstructor
    private static final class State {
        private final Cell cell;
        private final int distance;
    }
}