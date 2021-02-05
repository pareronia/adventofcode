package com.github.pareronia.aoc;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

public class Utils {

	public static Stream<Character> asCharacterStream(String string) {
		return Arrays.stream(
				ArrayUtils.toObject(
						Objects.requireNonNull(string).toCharArray()));
	}
}
