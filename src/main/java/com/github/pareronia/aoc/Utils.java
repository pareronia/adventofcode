package com.github.pareronia.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {

	public static Stream<Character> asCharacterStream(final String string) {
	    AssertUtils.assertNotNull(string, () -> "Expected string to be non-null");
	    
	    return IntStream.range(0, string.length())
	            .mapToObj(i -> Character.valueOf(string.charAt(i)));
	}
    
	public static Collector<Character, StringBuilder, String> toAString() {
		return Collector.of(
				StringBuilder::new,
				StringBuilder::append,
				StringBuilder::append,
				StringBuilder::toString);
	}
    
    public static <T> Stream<T> stream(final Iterator<T> iterator) {
        return Stream.generate(() -> null)
                .takeWhile(x -> iterator.hasNext())
                .map(n -> iterator.next());
    }
	
    public static <T> T last(final List<T> list, final int index) {
        return Objects.requireNonNull(list).get(list.size() - index);
    }
    
    public static <T> T last(final T[] list) {
        return Objects.requireNonNull(list)[list.length - 1];
    }
    
    public static char last(final String string) {
        return Objects.requireNonNull(string).charAt(string.length() - 1);
    }
    
    public static <T> List<T> concat(final List<T> list1, final T item) {
        final ArrayList<T> ans = new ArrayList<>(list1);
        ans.add(item);
        return ans;
    }
    
    @SafeVarargs
    public static <T> List<T> concatAll(final List<T>... lists) {
        final List<T> ans = new ArrayList<>(lists[0]);
        Arrays.stream(lists).skip(1).forEach(ans::addAll);
        return ans;
    }
    
    private static final Pattern REGEX_N = Pattern.compile("[0-9]+");
    public static final int[] naturalNumbers(final String string) {
        return REGEX_N.matcher(string).results()
                .map(MatchResult::group)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
    
    private static final Pattern REGEX_Z = Pattern.compile("-?[0-9]+");
    public static final int[] integerNumbers(final String string) {
        return REGEX_Z.matcher(string).results()
                .map(MatchResult::group)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
