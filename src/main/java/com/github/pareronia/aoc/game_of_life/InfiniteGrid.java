package com.github.pareronia.aoc.game_of_life;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.commons.lang3.Range;

import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.game_of_life.GameOfLife.Type;

public final class InfiniteGrid implements Type {
    private final Map<List<Integer>, Set<List<Integer>>> neighboursCache
            = new HashMap<>();
    
    @Override
    public Set<List<Integer>> cells(final Set<List<Integer>> alive) {
        final int dim = alive.iterator().next().size();
        final List<Range<Integer>> ranges =
            IntStream.iterate(dim - 1, i -> i >= 0, i -> i - 1)
                .mapToObj(i -> expand(i, alive))
                .collect(toUnmodifiableList());
        return product(ranges);
    }

    @Override
    public long getNeighbourCount(
            final List<Integer> cell,
            final Set<List<Integer>> alive
    ) {
        return neighbours(cell).stream()
                .filter(n -> alive.contains(n))
                .count();
    }

    private Range<Integer> expand(final int idx, final Set<List<Integer>> alive) {
        final IntSummaryStatistics stats = alive.stream()
                .mapToInt(a -> a.get(idx))
                .summaryStatistics();
        return Range.between(stats.getMin() - 1, stats.getMax() + 1);
    }

    private Set<List<Integer>> neighbours(final List<Integer> cell) {
        return this.neighboursCache.computeIfAbsent(cell, this::getNeighbours);
    }
    
    private Set<List<Integer>> getNeighbours(final List<Integer> cell) {
        final List<Range<Integer>> ranges = new ArrayList<>();
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
    private Set<List<Integer>> product(final List<Range<Integer>> ranges) {
        final Iterator<Integer>[] iterators = ranges.stream()
            .map(Utils::iterator)
            .toArray(Iterator[]::new);
        return IterTools.product(iterators);
    }
}