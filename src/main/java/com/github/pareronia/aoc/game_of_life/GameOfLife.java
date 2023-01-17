package com.github.pareronia.aoc.game_of_life;

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.Getter;
import lombok.With;

public class GameOfLife<T> {

    private final Type<T> type;
    private final Rules<T> rules;
    @Getter
    @With
    private final Set<T> alive;
    
    public GameOfLife(final Type<T> type, final Rules<T> rules, final Set<T> alive) {
        this.type = type;
        this.rules = rules;
        this.alive = Collections.unmodifiableSet(alive);
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
