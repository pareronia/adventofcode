package com.github.pareronia.aoc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class LongRange extends Range<Long> {

    public LongRange(final long element1, final long element2) {
        super(element1, element2);
    }
    
    public static LongRange between(final long fromInclusive, final long toExclusive) {
        return new LongRange(fromInclusive, toExclusive);
    }
    
    public Iterator<Long> iterator() {
        return new Iterator<>() {
            private long n = LongRange.this.minimum;
            
            @Override
            public Long next() {
                final long next = n;
                n += 1;
                return next;
            }
            
            @Override
            public boolean hasNext() {
                return n < LongRange.this.maximum;
            }
        };
    }
    
    public JoinResult leftJoin(final LongRange other) {
        final long o0 = Math.max(this.minimum, other.minimum);
        final long o1 = Math.min(this.maximum, other.maximum);
        if (o0 < o1) {
            final Set<LongRange> others = new HashSet<>();
            if (this.minimum < o0) {
                others.add(LongRange.between(this.minimum, o0));
            }
            if (o1 < this.maximum) {
                others.add(LongRange.between(o1, this.maximum));
            }
            return new JoinResult(
                Optional.of(LongRange.between(o0, o1)), others);
        } else {
            return new JoinResult(
                Optional.empty(),
                Set.of(LongRange.between(this.minimum.longValue(), this.maximum.longValue())));
        }
    }
    
    public LongRange translate(final long amount) {
        return LongRange.between(this.minimum + amount, this.maximum + amount);
    }
    
    public record JoinResult(Optional<LongRange> intersection, Set<LongRange> others) {}
}
