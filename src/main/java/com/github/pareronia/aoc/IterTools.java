package com.github.pareronia.aoc;

import static java.util.stream.Collectors.toList;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class IterTools {
    
    // TODO potentially huge storage cost -> make iterative version
    public static <T> Stream<List<T>> permutations(final Iterable<T> iterable) {
        final Stream.Builder<List<T>> builder = Stream.builder();
        IterTools.permutations(iterable, builder::add);
        return builder.build();
    }
   
    public static <T> IterToolsIterator<Pairwise.Pair<T>> pairwise(final Stream<T> stream) {
        return asIterToolsIterator(Pairwise.pairwise(stream.iterator()));
    }

    @SuppressWarnings("unchecked")
    public static <T> void permutations(
            final Iterable<T> iterable,
            final Consumer<List<T>> consumer
    ) {
        final T[] o = (T[]) Utils.stream(iterable.iterator()).toArray();
        final int[] a = new int[o.length];
        for (int j = 0; j < a.length; j++) {
            a[j] = j;
        }
        Heap.accept(a, p -> consumer.accept(
                Arrays.stream(p).mapToObj(i -> o[i]).collect(toList())));
    }
    
    public static Stream<int[]> permutations(final int[] a) {
        final Stream.Builder<int[]> builder = Stream.builder();
        Heap.accept(a, p -> builder.add(Arrays.copyOf(p, p.length)));
        return builder.build();
    }

    public static IterToolsIterator<int[]> combinations(
            final int n, final int k
    ) {
        final Iterator<int[]> ans
                = CombinatoricsUtils.combinationsIterator(n, k);
        return new IterToolsIterator<>() {
            @Override
            public boolean hasNext() {
                return ans.hasNext();
            }

            @Override
            public int[] next() {
                return ans.next();
            }
        };
    }

    public static <T> Stream<Enumerated<T>> enumerate(final Stream<T> stream) {
        return enumerateFrom(0, stream);
    }
    
    public static <T> Stream<Enumerated<T>>
    enumerateFrom(
            final int startIndex,
            final Stream<T> stream
    ) {
        return Utils.stream(new Iterator<Enumerated<T>>() {
            private final Iterator<T> iterator = stream.iterator();
            private int i = startIndex;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Enumerated<T> next() {
                return new Enumerated<>(i++, iterator.next());
            }
        });
    }
    
    private static <T> IterToolsIterator<ZippedPair<T>>
    zip(
        final Iterator<T> iterator1,
        final Iterator<T> iterator2
    ) {
        return new IterToolsIterator<>() {

            @Override
            public boolean hasNext() {
                return iterator1.hasNext() && iterator2.hasNext();
            }

            @Override
            public ZippedPair<T> next() {
                return new ZippedPair<>(iterator1.next(), iterator2.next());
            }
        };
    }

    public static <T> IterToolsIterator<ZippedPair<T>> zip(
            final Iterable<T> iterable1,
            final Iterable<T> iterable2
    ) {
        return zip(iterable1.iterator(), iterable2.iterator());
    }
    
    private static <T> IterToolsIterator<T> cycle(final Iterator<T> iterator) {
        return new IterToolsIterator<>() {
            List<T> saved = new ArrayList<>();
            int i = 0;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                if (iterator.hasNext()) {
                    final T next = iterator.next();
                    saved.add(next);
                    return next;
                }
                return saved.get(i++ % saved.size());
            }
        };
    }

    public static <T> IterToolsIterator<T> cycle(final Iterable<T> iterable) {
        return cycle(iterable.iterator());
    }
    
    public static <T> IterToolsIterator<WindowPair<T>> windows(
            final List<T> list
    ) {
        return new IterToolsIterator<>() {
            int i = 0;
            
            @Override
            public boolean hasNext() {
                return i < list.size() - 1;
            }

            @Override
            public WindowPair<T> next() {
                final WindowPair<T> next
                        = new WindowPair<>(list.get(i), list.get(i + 1));
                i++;
                return next;
            }
        };
    }

    public static <T, U> IterToolsIterator<ProductPair<T, U>> product(
            final Iterable<T> first,
            final Iterable<U> second
    ) {
        return product(first.iterator(), second.iterator());
    }

    public static <T, U> IterToolsIterator<ProductPair<T, U>> product(
            final Iterator<T> first,
            final Iterator<U> second
    ) {
        final List<U> lstU = Utils.stream(second).toList();
        final Iterator<ProductPair<T, U>> ans = Utils.stream(first)
                .flatMap(a -> lstU.stream()
                        .map(b -> new ProductPair<>(a, b)))
                .iterator();
        return new IterToolsIterator<>() {
            @Override
            public boolean hasNext() {
                return ans.hasNext();
            }

            @Override
            public ProductPair<T, U> next() {
                return ans.next();
            }
        };
    }
    
    public static <T> IterToolsIterator<T> chain(final Iterator<T> iterator1, final Iterator<T> iterator2) {
        return new IterToolsIterator<>() {
            @Override
            public boolean hasNext() {
                return iterator1.hasNext() || iterator2.hasNext();
            }

            @Override
            public T next() {
                if (iterator1.hasNext()) {
                    return iterator1.next();
                } else if (iterator2.hasNext()) {
                    return iterator2.next();
                }
                throw new NoSuchElementException();
            }
        };
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
    
    public record ZippedPair<T>(T first, T second) {}
    
    public record WindowPair<T>(T first, T second) {}

    public record ProductPair<T, U>(T first, U second) {
        public static <T, U> ProductPair<T, U> of(final T first, final U second) {
            return new ProductPair<>(first, second);
        }
    }
    
    public record Enumerated<T>(int index, T value) {}
    
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
