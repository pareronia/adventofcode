package com.github.pareronia.aoc;

import java.util.Comparator;
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
    private String toString;
    
    @SuppressWarnings({ "unchecked" })
    private RangeInclusive(final T element1, final T element2) {
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

    public static <T> RangeInclusive<T> between(final T fromInclusive, final T toInclusive) {
        return new RangeInclusive<>(fromInclusive, toInclusive);
    }

    public T getMinimum() {
        return minimum;
    }

    public T getMaximum() {
        return maximum;
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

    public boolean containsRange(final RangeInclusive<T> otherRange) {
        if (otherRange == null) {
            return false;
        }
        return contains(otherRange.minimum) && contains(otherRange.maximum);
    }

    public boolean isOverlappedBy(final RangeInclusive<T> otherRange) {
        if (otherRange == null) {
            return false;
        }
        return otherRange.contains(minimum)
            || otherRange.contains(maximum)
            || contains(otherRange.minimum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximum, minimum);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RangeInclusive<?> other = (RangeInclusive<?>) obj;
        return Objects.equals(maximum, other.maximum)
            && Objects.equals(minimum, other.minimum);
    }
    
    @Override
    public String toString() {
        if (toString == null) {
            toString = String.format("[%s..%s]", minimum, maximum);
        }
        return toString;
    }
}