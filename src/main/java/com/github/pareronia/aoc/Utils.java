package com.github.pareronia.aoc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

public class Utils {

	public static Stream<Character> asCharacterStream(String string) {
		return Arrays.stream(
				ArrayUtils.toObject(
						Objects.requireNonNull(string).toCharArray()));
	}
	
	public static <T extends Comparable<T>> T max(Stream<T> stream) {
		return max(stream, "Empty stream");
	}
	
	public static <T extends Comparable<T>> T max(Stream<T> stream, String message) {
		return Objects.requireNonNull(stream)
				.max(Comparator.<T> naturalOrder())
				.orElseThrow(() -> new RuntimeException(message));
	}

	public static <T extends Comparable<T>> T min(Stream<T> stream) {
		return min(stream, "Empty stream");
	}
	
	public static <T extends Comparable<T>> T min(Stream<T> stream, String message) {
		return Objects.requireNonNull(stream)
				.min(Comparator.<T> naturalOrder())
				.orElseThrow(() -> new RuntimeException(message));
	}
}
