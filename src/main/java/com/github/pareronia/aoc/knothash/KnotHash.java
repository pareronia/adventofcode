package com.github.pareronia.aoc.knothash;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.Utils;

public class KnotHash {
    
    public static final List<Integer> SEED =
            IntStream.range(0, 256).boxed().collect(toList());
    private static final List<Integer> PAD = List.of(17, 31, 73, 47, 23);
    
    public static String hexString(final String input) {
        return toHexString(calculate(paddedLengths(input)));
    }
    
    public static String binString(final String input) {
        return toBinaryString(calculate(paddedLengths(input)));
    }

    private static List<Integer> paddedLengths(final String input) {
        return Stream.concat(
                Utils.asCharacterStream(input).map(c -> (int) c.charValue()),
                PAD.stream())
            .collect(toList());
    }
    
    private static int[] calculate(final List<Integer> lengths) {
        final List<Integer> elements = new ArrayList<>(SEED);
        State state = new State(elements, lengths, 0, 0);
        for (int i = 0; i < 64; i++) {
            state = KnotHash.round(state);
        }
        final int[] dense = new int[16];
        for (int i = 0; i < 16; i++) {
            for (int j = i * 16; j < i * 16 + 16; j++) {
                dense[i] ^= elements.get(j);
            }
        }
        return dense;
    }
    
    public static State round(final State state) {
        final List<Integer> elements = state.elements();
        final List<Integer> lengths = state.lengths();
        int cur = state.cur();
        int skip = state.skip();
        for (final int len : lengths) {
            reverse(elements, cur, len);
            cur = (cur + len + skip) % elements.size();
            skip++;
        }
        return new State(elements, lengths, cur, skip);
    }

    private static void reverse(
            final List<Integer> elements,
            final int start, final int length
    ) {
        final int size = elements.size();
        for (int i = 0; i < length / 2; i++) {
            final int first = (start + i) % size;
            final int second = (start + length - 1 - i) % size;
            final Integer temp = elements.get(first);
            elements.set(first, elements.get(second));
            elements.set(second, temp);
        }
    }
    
    private static String toHexString(final int[] dense) {
        return Arrays.stream(dense)
                .boxed()
                .map(Integer::toHexString)
                .map(s -> StringUtils.leftPad(s, 2, '0'))
                .collect(joining());
    }
    
    private static String toBinaryString(final int[] dense) {
        return Arrays.stream(dense)
                .boxed()
                .map(Integer::toBinaryString)
                .map(s -> StringUtils.leftPad(s, 8, '0'))
                .collect(joining());
    }

    public record State(List<Integer> elements, List<Integer> lengths, int cur, int skip) {}
}
