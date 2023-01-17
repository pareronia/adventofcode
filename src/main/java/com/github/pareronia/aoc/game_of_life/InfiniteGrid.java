package com.github.pareronia.aoc.game_of_life;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aoc.Range;
import com.github.pareronia.aoc.game_of_life.GameOfLife.Type;

public final class InfiniteGrid implements Type<List<Integer>> {
    private final Map<List<Integer>, Set<List<Integer>>> neighboursCache
            = new HashMap<>();
    
    @Override
    public Map<List<Integer>, Long> getNeighbourCounts(final Set<List<Integer>> alive) {
        final Map<List<Integer>, Long> neighbourCounts = new HashMap<>();
        for (final List<Integer> cell : alive) {
            for (final List<Integer> n : neighbours(cell)) {
                neighbourCounts.merge(n, 1L, Long::sum);
            }
        }
        return neighbourCounts;
    }

    private Set<List<Integer>> neighbours(final List<Integer> cell) {
        return this.neighboursCache.computeIfAbsent(cell, this::getNeighbours);
    }
    
    private Set<List<Integer>> getNeighbours(final List<Integer> cell) {
        final List<Range> ranges = new ArrayList<>();
        final List<Integer> zeroes = new ArrayList<>();
        for (int i = 0; i < cell.size(); i++) {
            ranges.add(Range.between(-1, 1));
            zeroes.add(0);
        }
        final Set<List<Integer>> ans = product(ranges);
        ans.remove(zeroes);
        for (final List<Integer> a : ans) {
            for (int i = 0; i < a.size(); i++) {
                a.set(i, cell.get(i) + a.get(i));
            }
        }
        return ans;
    }
    
    @SuppressWarnings("unchecked")
    private Set<List<Integer>> product(final List<Range> ranges) {
        final Iterator<Integer>[] iterators = ranges.stream()
            .map(Range::iterator)
            .toArray(Iterator[]::new);
        return IterTools.product(iterators);
    }
}