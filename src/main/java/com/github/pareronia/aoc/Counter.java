package com.github.pareronia.aoc;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;

public class Counter<T> {

    private final Map<T, Long> counts;
    
    public Counter(final Iterable<T> iterable) {
        this.counts = Utils.stream(iterable.iterator())
            .collect(groupingBy(o -> o, counting()));
    }
    
    public boolean isEmpty() {
        return this.counts.isEmpty();
    }

    public List<Entry<T>> mostCommon() {
        return this.counts.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .map(e -> new Entry<T>(e.getKey(), e.getValue()))
            .toList();
    }
    
    public record Entry<T>(T value, long count) {}
}
