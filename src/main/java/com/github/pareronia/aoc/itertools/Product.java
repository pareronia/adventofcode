package com.github.pareronia.aoc.itertools;

import com.github.pareronia.aoc.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class Product {

    private Product() {}

    public static <T, U> Iterator<ProductPair<T, U>> product(
            final Iterator<T> first, final Iterator<U> second) {
        final List<U> lstU = Utils.stream(second).toList();
        final Iterator<ProductPair<T, U>> ans =
                Utils.stream(first)
                        .flatMap(a -> lstU.stream().map(b -> new ProductPair<>(a, b)))
                        .iterator();
        return new Iterator<>() {
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

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public static <T> Iterator<List<T>> product(final Iterator<T> iterator, final int repeat) {
        final List<T> items = Utils.stream(iterator).toList();
        List<List<T>> ans = new ArrayList<>();
        ans.add(List.of());
        for (int i = 0; i < repeat; i++) {
            final List<List<T>> newAns = new ArrayList<>();
            for (final T item : items) {
                for (final List<T> tmp : ans) {
                    final List<T> lst = new ArrayList<>();
                    lst.addAll(tmp);
                    lst.add(item);
                    newAns.add(lst);
                }
            }
            ans = newAns;
        }
        return ans.iterator();
    }
}
