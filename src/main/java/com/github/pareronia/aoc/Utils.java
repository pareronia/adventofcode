package com.github.pareronia.aoc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Range;

public class Utils {

	public static Stream<Character> asCharacterStream(final String string) {
		return Arrays.stream(
				ArrayUtils.toObject(
						Objects.requireNonNull(string).toCharArray()));
	}
	
	public static <T extends Comparable<T>> T max(final Stream<T> stream) {
		return max(stream, "Empty stream");
	}
	
	public static <T extends Comparable<T>> T max(final Stream<T> stream, final String message) {
		return Objects.requireNonNull(stream)
				.max(Comparator.<T> naturalOrder())
				.orElseThrow(() -> new RuntimeException(message));
	}

	public static <T extends Comparable<T>> T min(final Stream<T> stream) {
		return min(stream, "Empty stream");
	}
	
	public static <T extends Comparable<T>> T min(final Stream<T> stream, final String message) {
		return Objects.requireNonNull(stream)
				.min(Comparator.<T> naturalOrder())
				.orElseThrow(() -> new RuntimeException(message));
	}
	
	public static Collector<Character, StringBuilder, String> toAString() {
		return Collector.of(
				StringBuilder::new,
				StringBuilder::append,
				StringBuilder::append,
				StringBuilder::toString);
	}
	
    public static Iterator<Integer> iterator(final Range<Integer> range) {
        return new Iterator<>() {
            private int i = range.getMinimum();

            @Override
            public Integer next() {
                return i++;
            }
            
            @Override
            public boolean hasNext() {
                return i <= range.getMaximum();
            }
        };
    }
    
    public static <T> Stream<T> stream(final Iterator<T> iterator) {
        return Stream.generate(() -> null)
                .takeWhile(x -> iterator.hasNext())
                .map(n -> iterator.next());
    }
}
