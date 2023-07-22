package com.github.pareronia.aoc;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.math3.util.CombinatoricsUtils;

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
    
    // TODO potentially huge storage cost -> make iterative version
    public static <T> Stream<List<T>> permutations(final Iterable<T> iterable) {
        final Stream.Builder<List<T>> builder = Stream.builder();
        IterTools.permutations(iterable, builder::add);
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    public static <T> Stream<List<T>> permutations(
            final Iterable<T> iterable,
            final Consumer<List<T>> consumer
    ) {
        final T[] o = (T[]) Utils.stream(iterable.iterator()).toArray();
        final int[] a = new int[o.length];
        for (int j = 0; j < a.length; j++) {
            a[j] = j;
        }
        final Stream.Builder<List<T>> builder = Stream.builder();
        Heap.accept(a, p -> consumer.accept(
                Arrays.stream(p).mapToObj(i -> o[i]).collect(toList())));
        return builder.build();
    }

    public static Iterator<int[]> combinationsIterator(final int n, final int k) {
        return CombinatoricsUtils.combinationsIterator(n, k);
    }

    public static Iterable<int[]> combinations(final int n, final int k) {
        return () -> combinationsIterator(n, k);
    }

    private static final class Heap {
        
        public static void accept(final int[] a, final Consumer<int[]> consumer) {
            heaps_algorithm(a, a.length, consumer);
        }
        
        private static void heaps_algorithm(
                final int[] a,
                final int n,
                final Consumer<int[]> consumer
        ) {
            if (n == 1) {
                // (got a new permutation)
                consumer.accept(a);
                return;
            }
            for (int i = 0; i < n - 1; i++) {
                heaps_algorithm(a, n - 1, consumer);
                // always swap the first when odd,
                // swap the i-th when even
                if (n % 2 == 0) {
                    swap(a, n - 1, i);
                } else {
                    swap(a, n - 1, 0);
                }
            }
            heaps_algorithm(a, n - 1, consumer);
        }
        
        private static void swap(final int[] a, final int i, final int j) {
            final int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
    }
}
