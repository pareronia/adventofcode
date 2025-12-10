package com.github.pareronia.aoc.itertools;

import static java.util.stream.Collectors.toList;

import org.apache.commons.math3.util.CombinatoricsUtils;

import com.github.pareronia.aoc.Utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings({"PMD.CouplingBetweenObjects", "PMD.TooManyMethods"})
public final class IterTools {

	private IterTools() {}

    // TODO potentially huge storage cost -> make iterative version
    public static <T> Stream<List<T>> permutations(final Iterable<T> iterable) {
        final Stream.Builder<List<T>> builder = Stream.builder();
        permutations(iterable, builder::add);
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    public static <T> void permutations(
            final Iterable<T> iterable, final Consumer<List<T>> consumer) {
        final T[] o = (T[]) Utils.stream(iterable.iterator()).toArray();
        final int[] a = new int[o.length];
        for (int j = 0; j < a.length; j++) {
            a[j] = j;
        }
        Heap.accept(
                a, p -> consumer.accept(Arrays.stream(p).mapToObj(i -> o[i]).collect(toList())));
    }

    @SuppressWarnings("PMD.UseVarargs")
    public static Stream<int[]> permutations(final int[] a) {
        final Stream.Builder<int[]> builder = Stream.builder();
        Heap.accept(a, p -> builder.add(Arrays.copyOf(p, p.length)));
        return builder.build();
    }

    public static <T> IterToolsIterator<Pair<T>> pairwise(final Stream<T> stream) {
        return pairwise(stream.iterator());
    }

    public static <T> IterToolsIterator<Pair<T>> pairwise(final Iterator<T> iterator) {
        return asIterToolsIterator(Pairwise.pairwise(iterator));
    }

    public static IterToolsIterator<int[]> combinations(final int n, final int k) {
        return asIterToolsIterator(CombinatoricsUtils.combinationsIterator(n, k));
    }

    public static <T> IterToolsIterator<Enumerated<T>> enumerate(final Stream<T> stream) {
        return enumerateFrom(0, stream);
    }

    public static <T> IterToolsIterator<Enumerated<T>> enumerate(final Iterable<T> iterable) {
        return enumerateFrom(0, iterable);
    }

    public static <T> IterToolsIterator<Enumerated<T>> enumerateFrom(
            final int startIndex, final Iterable<T> iterable) {

        return asIterToolsIterator(Enumerate.enumerateFrom(startIndex, iterable.iterator()));
    }

    public static <T> IterToolsIterator<Enumerated<T>> enumerateFrom(
            final int startIndex, final Stream<T> stream) {

        return asIterToolsIterator(Enumerate.enumerateFrom(startIndex, stream.iterator()));
    }

    public static <T> IterToolsIterator<ZippedPair<T>> zip(
            final Iterable<T> iterable1, final Iterable<T> iterable2) {

        return asIterToolsIterator(Zip.zip(iterable1.iterator(), iterable2.iterator()));
    }

    public static <T> IterToolsIterator<T> cycle(final Iterable<T> iterable) {
        return asIterToolsIterator(Cycle.cycle(iterable.iterator()));
    }

    public static <T, U> IterToolsIterator<ProductPair<T, U>> product(
            final Iterable<T> first, final Iterable<U> second) {

        return product(first.iterator(), second.iterator());
    }

    public static <T, U> IterToolsIterator<ProductPair<T, U>> product(
            final Iterator<T> first, final Iterator<U> second) {

        return asIterToolsIterator(Product.product(first, second));
    }

    public static <T> IterToolsIterator<T> chain(
            final Iterator<T> iterator1, final Iterator<T> iterator2) {

        return asIterToolsIterator(Chain.chain(iterator1, iterator2));
    }

    public interface IterToolsIterator<T> extends Iterator<T> {
        default Stream<T> stream() {
            return Utils.stream(this);
        }

        default Iterable<T> iterable() {
            return () -> this;
        }
    }

    private static <T> IterToolsIterator<T> asIterToolsIterator(final Iterator<T> iterator) {
        return new IterToolsIterator<>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }
}
