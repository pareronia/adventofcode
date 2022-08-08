package com.github.pareronia.aoc.game_of_life;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.With;

public class GameOfLife {

    private final Type type;
    private final Rules rules;
    @Getter
    @With
    private final Set<List<Integer>> alive;
    
    public GameOfLife(final Type type, final Rules rules, final Set<List<Integer>> alive) {
        this.type = type;
        this.rules = rules;
        this.alive = Collections.unmodifiableSet(alive);
    }

    public GameOfLife nextGeneration() {
        final Set<List<Integer>> newAlive =
            this.type.cells(this.alive).stream()
                .filter(this::isAlive)
                .collect(toUnmodifiableSet());
        return this.withAlive(newAlive);
    }
    
    private boolean isAlive(final List<Integer> cell) {
        final long cnt = this.type.getNeighbourCount(cell, this.alive);
        return this.rules.alive(cell, cnt, this.alive);
    }
    
    public interface Type {
        Set<List<Integer>> cells(Set<List<Integer>> alive);
        long getNeighbourCount(List<Integer> cell, Set<List<Integer>> alive);
    }
    
    public interface Rules {
        boolean alive(List<Integer> cell, long cnt, Set<List<Integer>> alive);
    }
    
    public static final Rules classicRules =
        (cell, cnt, alive) -> (cnt == 3 || (cnt == 2 && alive.contains(cell)));
}
