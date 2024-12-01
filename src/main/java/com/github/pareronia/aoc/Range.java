package com.github.pareronia.aoc;

import java.util.Comparator;
import java.util.Objects;

public class Range<T> implements Comparable<Range<T>> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private enum ComparableComparator implements Comparator {
        INSTANCE;

        @Override
        public int compare(final Object obj1, final Object obj2) {
            return ((Comparable) obj1).compareTo(obj2);
        }
    }

    protected final T minimum;
    protected final T maximum;
    private final Comparator<T> comparator;
    private String toString;
    
    @SuppressWarnings({ "unchecked" })
    protected Range(final T element1, final T element2) {
        AssertUtils.assertTrue(
                element1 != null && element2 != null,
                () -> String.format("Elements in a range must not be null: element1=%s, element2=%s",
                        element1, element2));
        this.comparator = ComparableComparator.INSTANCE;
        final int compare = comparator.compare(element1, element2);
        AssertUtils.assertFalse(compare == 0,
                () -> String.format("Elements must not be equal: element1=%s, element2=%s",
                        element1, element2));
        if (compare < 1) {
            this.minimum = element1;
            this.maximum = element2;
        } else {
            this.minimum = element2;
            this.maximum = element1;
        }
    }

    public boolean contains(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, minimum) > -1
                && comparator.compare(element, maximum) < 0;
    }
    
    private T min(final T t1, final T t2) {
        if (this.comparator.compare(t1, t2) < 0) {
            return t1;
        }
        return t2;
    }
    
    private T max(final T t1, final T t2) {
        if (this.comparator.compare(t1, t2) > 0) {
            return t1;
        }
        return t2;
    }
    
    public boolean isOverlappedBy(final Range<T> other) {
        final T o0 = max(this.minimum, other.minimum);
        final T o1 = min(this.maximum, other.maximum);
        return this.comparator.compare(o0, o1) < 0;
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
        final Range<?> other = (Range<?>) obj;
        return Objects.equals(maximum, other.maximum)
            && Objects.equals(minimum, other.minimum);
    }
    
    @Override
    public String toString() {
        if (toString == null) {
            toString = String.format("[%s..%s)", minimum, maximum);
        }
        return toString;
    }

    @Override
    public int compareTo(final Range<T> other) {
        final int cmin = this.comparator.compare(this.minimum, other.minimum);
        if (cmin == 0) {
            return this.comparator.compare(this.maximum, other.maximum);
        }
        return cmin;
    }
}
