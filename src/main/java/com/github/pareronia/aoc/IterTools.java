package com.github.pareronia.aoc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class IterTools {

    @SafeVarargs
    public static <T> Set<List<T>> product(final Iterator<T>... iterators) {
        Set<List<T>> ans = new HashSet<>(Set.of(List.of()));
        for (final Iterator<T> range : iterators) {
            final Set<List<T>> set = new HashSet<>();
            for (final T i : (Iterable<T>) () -> range) {
                for (final List<T> tmp : ans) {
                    final List<T> lst = new ArrayList<>();
                    lst.add(i);
                    lst.addAll(tmp);
                    set.add(lst);
                }
            }
            ans = set;
        }
        return ans;
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> Set<List<T>> product(final Iterable<T>... iterables) {
        final Iterator<T>[] iterators = new Iterator[iterables.length];
        for (int i = 0; i < iterables.length; i++) {
            iterators[i] = iterables[i].iterator();
        }
        return IterTools.product(iterators);
    }
}
