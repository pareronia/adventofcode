package com.github.pareronia.aoc.game_of_life;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.apache.commons.lang3.Range;

import com.github.pareronia.aoc.IterTools;

public class GameOfLife {

    private final List<Range<Integer>> ranges;
    private final Set<List<Integer>> alive;
    private final Map<List<Integer>, Set<List<Integer>>> neighboursCache = new HashMap<>();
    
    public GameOfLife(final Set<List<Integer>> alive) {
        this.alive = alive;
        this.ranges = ranges();
    }

    private List<Range<Integer>> ranges() {
        return IntStream.range(0, this.alive.iterator().next().size())
            .mapToObj(i -> {
                final IntSummaryStatistics stats = this.alive.stream()
                        .mapToInt(a -> a.get(i))
                        .summaryStatistics();
                return Range.between(stats.getMin(), stats.getMax());
            })
            .collect(toList());
    }
    
    public Set<List<Integer>> getAlive() {
        return alive;
    }

    public void nextGeneration(
        final Predicate<Long> keepAliveCondition,
        final Predicate<Long> getAliveCondition
    ) {
        final Set<List<Integer>> newAlive = new HashSet<>();
        expand();
        for (final List<Integer> cell : cells()) {
            final long cnt = neighbours(cell).stream()
                    .filter(this.alive::contains)
                    .count();
            if (this.alive.contains(cell)) {
                if (keepAliveCondition.test(cnt)) {
                    newAlive.add(cell);
                }
            } else if (getAliveCondition.test(cnt)) {
                newAlive.add(cell);
            }
        }
        this.alive.clear();
        this.alive.addAll(newAlive);
    }
    
    private void expand() {
        for (int i = 0; i < this.ranges.size(); i++) {
            final Range<Integer> range = ranges.get(i);
            final Range<Integer> newRange = Range.between(
                range.getMinimum() - 1,
                range.getMaximum() + 1);
            ranges.set(i, newRange);
        }
    }
    
    private Set<List<Integer>> cells() {
        final List<Range<Integer>> ranges = new ArrayList<>(this.ranges);
        Collections.reverse(ranges);
        return product(ranges);
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
        final Iterator<Integer>[] iterators = new Iterator[ranges.size()];
        for (int i = 0; i < ranges.size(); i++) {
            iterators[i] = iterator(ranges.get(i));
        }
        return IterTools.product(iterators);
    }
    
    private Iterator<Integer> iterator(final Range<Integer> range) {
        return new Iterator<>() {
            private int i = range.getMinimum();

            @Override
            public Integer next() {
                return i++;
            }
            
            @Override
            public boolean hasNext() {
                return i <= range.getMaximum();
            }
        };
    }
}
