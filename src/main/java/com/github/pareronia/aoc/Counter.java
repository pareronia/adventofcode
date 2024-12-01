package com.github.pareronia.aoc;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Counter<T> {

    private final Map<T, Long> counts;
    
    public Counter(final Stream<T> stream) {
        this.counts = stream.collect(groupingBy(o -> o, counting()));
    }
    
    public Counter(final Iterable<T> iterable) {
        this(Utils.stream(iterable.iterator()));
    }
    
    public boolean isEmpty() {
        return this.counts.isEmpty();
    }
    
    public boolean containsValue(final Long value) {
        return this.counts.containsValue(value);
    }
    
    public Long get(final T value) {
        return this.counts.getOrDefault(value, 0L);
    }
    
    public Collection<Long> values() {
        return this.counts.values();
    }

    public List<Entry<T>> mostCommon() {
        return this.counts.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry<T, Long>::getValue).reversed())
            .map(e -> new Entry<T>(e.getKey(), e.getValue()))
            .toList();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(counts);
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
        @SuppressWarnings("rawtypes")
        final Counter other = (Counter) obj;
        return Objects.equals(counts, other.counts);
    }

    public record Entry<T>(T value, long count) {}
}
