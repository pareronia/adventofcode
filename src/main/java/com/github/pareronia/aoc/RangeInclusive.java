package com.github.pareronia.aoc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class RangeInclusive<T> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private enum ComparableComparator implements Comparator {
        INSTANCE;

        @Override
        public int compare(final Object obj1, final Object obj2) {
            return ((Comparable) obj1).compareTo(obj2);
        }
    }

    private final T minimum;
    private final T maximum;
    private final Comparator<T> comparator;
    private String asString;

    @SuppressWarnings({"unchecked", "PMD.AvoidLiteralsInIfCondition"})
    private RangeInclusive(final T element1, final T element2) {
        AssertUtils.assertTrue(
                element1 != null && element2 != null,
                () ->
                        String.format(
                                "Elements in a range must not be null: element1=%s, element2=%s",
                                element1, element2));
        this.comparator = ComparableComparator.INSTANCE;
        if (comparator.compare(element1, element2) < 1) {
            this.minimum = element1;
            this.maximum = element2;
        } else {
            this.minimum = element2;
            this.maximum = element1;
        }
    }

    public static <T> RangeInclusive<T> between(final T fromInclusive, final T toInclusive) {
        return new RangeInclusive<>(fromInclusive, toInclusive);
    }

    @SuppressWarnings({"PMD.UseExplicitTypes", "PMD.LawOfDemeter"})
    public static <T> List<RangeInclusive<T>> mergeRanges(
            final Collection<RangeInclusive<T>> ranges) {
        final var comparator = ComparableComparator.INSTANCE;
        final var merged = new ArrayDeque<RangeInclusive<T>>();
        final var sorted = new ArrayList<>(ranges);
        Collections.sort(
                sorted,
                (r1, r2) -> {
                    final int first = comparator.compare(r1.getMinimum(), r2.getMinimum());
                    if (first == 0) {
                        return comparator.compare(r1.getMaximum(), r2.getMaximum());
                    }
                    return first;
                });
        for (final var range : sorted) {
            if (merged.isEmpty()) {
                merged.addLast(range);
                continue;
            }
            final var last = merged.peekLast();
            if (range.isOverlappedBy(last)) {
                merged.removeLast();
                final T newMaximum =
                        comparator.compare(last.getMaximum(), range.getMaximum()) > 0
                                ? last.getMaximum()
                                : range.getMaximum();
                merged.add(between(last.getMinimum(), newMaximum));
            } else {
                merged.addLast(range);
            }
        }
        return merged.stream().toList();
    }

    public T getMinimum() {
        return minimum;
    }

    public T getMaximum() {
        return maximum;
    }

    public boolean isBefore(final T element) {
        return element != null && comparator.compare(element, maximum) > 0;
    }

    public boolean isAfter(final T element) {
        return element != null && comparator.compare(element, minimum) < 0;
    }

    public boolean contains(final T element) {
        return element != null
                && comparator.compare(element, minimum) > -1
                && comparator.compare(element, maximum) < 1;
    }

    public boolean containsRange(final RangeInclusive<T> otherRange) {
        return otherRange != null && contains(otherRange.minimum) && contains(otherRange.maximum);
    }

    public boolean isOverlappedBy(final RangeInclusive<T> otherRange) {
        return otherRange != null && otherRange.contains(minimum)
                || otherRange.contains(maximum)
                || contains(otherRange.minimum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximum, minimum);
    }

    @Override
    public boolean equals(final Object obj) {
        return switch (obj) {
            case final RangeInclusive<?> other when other != null ->
                    Objects.equals(maximum, other.maximum)
                            && Objects.equals(minimum, other.minimum);
            default -> false;
        };
    }

    @Override
    public String toString() {
        if (asString == null) {
            asString = String.format("[%s..%s]", minimum, maximum);
        }
        return asString;
    }
}
