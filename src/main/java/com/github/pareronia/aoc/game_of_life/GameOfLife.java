package com.github.pareronia.aoc.game_of_life;

import static java.util.stream.Collectors.toSet;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public record GameOfLife<T>(GameOfLife.Type<T> type, GameOfLife.Rules<T> rules, Set<T> alive) {

    public GameOfLife<T> withAlive(final Set<T> alive) {
        return new GameOfLife<>(this.type, this.rules, alive);
    }

    public GameOfLife<T> nextGeneration() {
        final Set<T> newAlive = this.type.getNeighbourCounts(this.alive).entrySet().stream()
                .filter(e -> this.rules.alive(e.getKey(), e.getValue(), this.alive))
                .map(Entry::getKey)
                .collect(toSet());
        return this.withAlive(newAlive);
    }

    public interface Type<T> {
        Map<T, Long> getNeighbourCounts(Set<T> alive);
    }
    
    public interface Rules<T> {
        boolean alive(T cell, long cnt, Set<T> alive);
    }
    
    @SuppressWarnings("rawtypes")
    public static final Rules classicRules =
        (cell, cnt, alive) -> (cnt == 3 || (cnt == 2 && alive.contains(cell)));
}
