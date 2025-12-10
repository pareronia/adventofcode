package com.github.pareronia.aoc.itertools;

import com.github.pareronia.aoc.Utils;

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
}
