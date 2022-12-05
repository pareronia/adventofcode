package com.github.pareronia.aoc;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
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
    
    public static <T> T last(final List<T> list) {
        return Objects.requireNonNull(list).get(list.size() - 1);
    }
    
    public static char last(final String string) {
        return Objects.requireNonNull(string).charAt(string.length() - 1);
    }
    
    private static final Pattern REGEX = Pattern.compile("[0-9]+");
    public static final int[] naturalNumbers(final String string) {
        return REGEX.matcher(string).results()
                .map(MatchResult::group)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
