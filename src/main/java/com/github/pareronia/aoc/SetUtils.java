package com.github.pareronia.aoc;

import static java.util.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class SetUtils {

    public static <T> Set<T> intersection(final Set<T> a, final Set<T> b) {
        return Objects.requireNonNull(a).stream()
                .filter(Objects.requireNonNull(b)::contains)
                .collect(toSet());
    }
    
    public static <T> Set<T> union(final Set<T> a, final Set<T> b) {
        return Stream.concat(
                    Objects.requireNonNull(a).stream(),
                    Objects.requireNonNull(b).stream())
                .collect(toSet());
    }
    
    public static <T> Set<T> disjunction(final Set<T> a, final Set<T> b) {
        return Stream.concat(
                    Objects.requireNonNull(a).stream(),
                    Objects.requireNonNull(b).stream())
                .filter(e -> a.contains(e) ^ b.contains(e))
                .collect(toSet());
    }
    
    public static <T> Set<T> difference(final Set<T> a, final Set<T> b) {
        return Stream.concat(
                    Objects.requireNonNull(a).stream(),
                    Objects.requireNonNull(b).stream())
                .filter(e -> a.contains(e) && !b.contains(e))
                .collect(toSet());
    }
}
