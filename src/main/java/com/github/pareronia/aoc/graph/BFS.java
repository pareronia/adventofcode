package com.github.pareronia.aoc.graph;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class BFS {
    
    public static <T> int execute(
            final T start,
            final Predicate<T> isEnd,
            final Function<T, Stream<T>> adjacent
    ) {
        final Deque<State<T>> q = new ArrayDeque<>(Set.of(new State<>(start, 0)));
        final Set<T> seen = new HashSet<>(Set.of(start));
        while (!q.isEmpty()) {
            final State<T> state = q.poll();
            if (isEnd.test(state.node)) {
                return state.distance;
            }
            adjacent.apply(state.node)
                .filter(n -> !seen.contains(n))
                .forEach(n -> {
                    seen.add(n);
                    q.add(new State<>(n, state.distance + 1));
                });
        }
        throw new IllegalStateException("Unsolvable");
    }

    public static <T> Set<T> floodFill(
            final T start,
            final Function<T, Stream<T>> adjacent
    ) {
        final Deque<T> q = new ArrayDeque<>(Set.of(start));
        final Set<T> seen = new HashSet<>(Set.of(start));
        while (!q.isEmpty()) {
            final T node = q.poll();
            adjacent.apply(node)
                .filter(n -> !seen.contains(n))
                .forEach(n -> {
                    seen.add(n);
                    q.add(n);
                });
        }
        return seen;
    }

    @RequiredArgsConstructor
    private static final class State<T> {
        private final T node;
        private final int distance;
    }
}