package com.github.pareronia.aoc.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;

public class Dijkstra {

    public static <T> Result<T> execute(
            final T start,
            final Predicate<T> end,
            final Function<T, Stream<T>> adjacent,
            final BiFunction<T, T, Integer> cost
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
                    final long newRisk = cTotal + cost.apply(state.node, n);
                    if (newRisk < best.getOrDefault(n, Long.MAX_VALUE)) {
                        best.put(n, newRisk);
                        parent.put(n, state.node);
                        q.add(new State<>(n, newRisk));
                    }
            });
        }
        return new Result<>(start, best, parent);
    }

    public static <T> AllResults<T> all(
            final T start,
            final Predicate<T> end,
            final Function<T, Stream<T>> adjacent,
            final BiFunction<T, T, Integer> cost
    ) {
        final PriorityQueue<State<T>> q = new PriorityQueue<>();
        q.add(new State<>(start, 0));
        final Map<T, Long> distances = new HashMap<>();
        distances.put(start, 0L);
        final Map<T, List<T>> predecessors = new HashMap<>();
        while (!q.isEmpty()) {
            final State<T> state = q.poll();
            if (end.test(state.node)) {
                break;
            }
            final long total = distances.getOrDefault(state.node, Long.MAX_VALUE);
            adjacent.apply(state.node)
                .forEach(n -> {
                    final long newDistance = total + cost.apply(state.node, n);
                    final Long dist_n = distances.getOrDefault(n, Long.MAX_VALUE);
                    if (newDistance < dist_n) {
                        distances.put(n, newDistance);
                        predecessors.put(n, new ArrayList<>(List.of(state.node)));
                        q.add(new State<>(n, newDistance));
                    } else if (newDistance == dist_n) {
                        predecessors.computeIfAbsent(n, k -> new ArrayList<>())
                                .add(state.node);
                    }
            });
        }
        return new AllResults<>(start, distances, predecessors);
    }

    public static <T> long distance(
            final T start,
            final Predicate<T> end,
            final Function<T, Stream<T>> adjacent,
            final BiFunction<T, T, Integer> cost
    ) {
        final PriorityQueue<State<T>> q = new PriorityQueue<>();
        q.add(new State<>(start, 0));
        final Map<T, Long> best = new HashMap<>();
        best.put(start, 0L);
        while (!q.isEmpty()) {
            final State<T> state = q.poll();
            if (end.test(state.node)) {
                return state.cost;
            }
            final long cTotal = best.getOrDefault(state.node, Long.MAX_VALUE);
            adjacent.apply(state.node)
                .forEach(n -> {
                    final long newRisk = cTotal + cost.apply(state.node, n);
                    if (newRisk < best.getOrDefault(n, Long.MAX_VALUE)) {
                        best.put(n, newRisk);
                        q.add(new State<>(n, newRisk));
                    }
            });
        }
        throw new IllegalStateException("Unsolvable");
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

    public record Result<T>(T source, Map<T, Long> distances, Map<T, T> paths) {
        
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
    
    public record AllResults<T>(
            T source, Map<T, Long> distances, Map<T, List<T>> predecessors) {
        
        public List<List<T>> getPaths(final T t) {
            if (t.equals(source)) {
                return List.of(List.of(source));
            }
            final List<List<T>> paths = new ArrayList<>();
            for (final T predecessor : predecessors.getOrDefault(t, List.of())) {
                for (final List<T> path : getPaths(predecessor)) {
                    paths.add(Utils.concat(path, t));
                }
            }
            return paths;
        }
    }
}
