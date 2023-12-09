package com.github.pareronia.aoc.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStar {

    public static <T> Result<T> execute(
            final T start,
            final Predicate<T> end,
            final Function<T, Stream<T>> adjacent,
            final Function<T, Integer> cost
    ) {
        final PriorityQueue<State<T>> q = new PriorityQueue<>();
        q.add(new State<>(start, 0));
        final Map<T, Long> best = new HashMap<>();
        best.put(start, 0L);
        final Map<T, T> parent = new HashMap<>();
        while (!q.isEmpty()) {
            final State<T> state = q.poll();
            if (end.test(state.node)) {
                break;
            }
            final long cTotal = best.getOrDefault(state.node, Long.MAX_VALUE);
            adjacent.apply(state.node)
                .forEach(n -> {
                    final long newRisk = cTotal + cost.apply(n);
                    if (newRisk < best.getOrDefault(n, Long.MAX_VALUE)) {
                        best.put(n, newRisk);
                        parent.put(n, state.node);
                        q.add(new State<>(n, newRisk));
                    }
            });
        }
        return new Result<>(start, best, parent);
    }

    private static final class State<T> implements Comparable<State<T>> {
        private final T node;
        private final long cost;
    
        protected State(final T node, final long cost) {
            this.node = node;
            this.cost = cost;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("State [node=").append(node).append(", cost=").append(cost).append("]");
            return builder.toString();
        }

        @Override
        public int compareTo(final State<T> other) {
            return Long.compare(this.cost, other.cost);
        }
    }

    public static class Result<T> {
        private final T source;
        private final Map<T, Long> distances;
        private final Map<T, T> paths;
        
        protected Result(final T source, final Map<T, Long> distances, final Map<T, T> paths) {
            this.source = source;
            this.distances = distances;
            this.paths = paths;
        }

        public Map<T, Long> getDistances() {
            return distances;
        }

        public long getDistance(final T v) {
            return distances.get(v);
        }

        public List<T> getPath(final T v) {
            final List<T> p = new ArrayList<>();
            T parent = v;
            if (v != this.source) {
                while (parent != this.source) {
                    p.add(0, parent);
                    parent = this.paths.get(parent);
                }
                p.add(0, this.source);
            } else {
                p.add(this.source);
            }
            return p;
        }
    }
}
