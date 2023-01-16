package com.github.pareronia.aoc.game_of_life;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.Collections;
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
        final Set<T> newAlive =
            this.type.cells(this.alive).stream()
                .filter(this::isAlive)
                .collect(toUnmodifiableSet());
        return this.withAlive(newAlive);
    }
    
    private boolean isAlive(final T cell) {
        final long cnt = this.type.getNeighbourCount(cell, this.alive);
        return this.rules.alive(cell, cnt, this.alive);
    }
    
    public interface Type<T> {
        Set<T> cells(Set<T> alive);
        long getNeighbourCount(T cell, Set<T> alive);
    }
    
    public interface Rules<T> {
        boolean alive(T cell, long cnt, Set<T> alive);
    }
    
    @SuppressWarnings("rawtypes")
    public static final Rules classicRules =
        (cell, cnt, alive) -> (cnt == 3 || (cnt == 2 && alive.contains(cell)));
}
