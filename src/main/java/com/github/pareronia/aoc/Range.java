package com.github.pareronia.aoc;

import java.util.Comparator;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Range<T> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private enum ComparableComparator implements Comparator {
        INSTANCE;

        @Override
        public int compare(final Object obj1, final Object obj2) {
            return ((Comparable) obj1).compareTo(obj2);
        }
    }

    @Getter
    @EqualsAndHashCode.Include
    private final T minimum;
    @Getter
    @EqualsAndHashCode.Include
    private final T maximum;
    private final Comparator<T> comparator;
    private String toString;
    
    @SuppressWarnings({ "unchecked" })
    private Range(final T element1, final T element2) {
        AssertUtils.assertTrue(
                element1 != null && element2 != null,
                () -> String.format("Elements in a range must not be null: element1=%s, element2=%s",
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

    public static <T> Range<T> between(final T fromInclusive, final T toInclusive) {
        return new Range<>(fromInclusive, toInclusive);
    }

    public boolean isBefore(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, maximum) > 0;
    }

    public boolean isAfter(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, minimum) < 0;
    }

    public boolean contains(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, minimum) > -1
                && comparator.compare(element, maximum) < 1;
    }

    public boolean containsRange(final Range<T> otherRange) {
        if (otherRange == null) {
            return false;
        }
        return contains(otherRange.minimum) && contains(otherRange.maximum);
    }

    public boolean isOverlappedBy(final Range<T> otherRange) {
        if (otherRange == null) {
            return false;
        }
        return otherRange.contains(minimum)
            || otherRange.contains(maximum)
            || contains(otherRange.minimum);
    }
    
    @Override
    public String toString() {
        if (toString == null) {
            toString = String.format("[%s..%s]", minimum, maximum);
        }
        return toString;
    }
}