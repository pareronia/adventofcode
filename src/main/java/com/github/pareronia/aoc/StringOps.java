package com.github.pareronia.aoc;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.ArrayUtils.subarray;

import java.util.List;

public class StringOps {
    
    public static List<Integer> getDigits(String s, int expected) {
        final List<Integer> digits = Utils.asCharacterStream(s)
                .filter(Character::isDigit)
                .map(c -> Integer.valueOf(Character.digit(c, 10)))
                .collect(toList());
        if (digits.size() != expected) {
            throw new IllegalArgumentException(
                String.format("Expected %d, got %d", expected, digits.size()));
        }
        return digits;
    }
    
    public static char[] move(char[] ch, int from, int to) {
        if (from == to) {
            throw new IllegalArgumentException("Expected from and to to be different");
        }
        if (from < to) {
            final char[] ch1 = subarray(ch, 0, from);
            final char[] ch2 = new char[] { ch[from] };
            final char[] ch3 = subarray(ch, from + 1, to + 1);
            final char[] ch4 = subarray(ch, to + 1, ch.length);
            return addAll(ch1, addAll(ch3, addAll(ch2, ch4)));
        } else {
            final char[] ch1 = subarray(ch, 0, to);
            final char[] ch2 = subarray(ch, to, from);
            final char[] ch3 = new char[] { ch[from] };
            final char[] ch4 = subarray(ch, from + 1, ch.length);
            return addAll(ch1, addAll(ch3, addAll(ch2, ch4)));
        }
    }
    
    public static char[] rotateLeft(char[] ch, int amount) {
        amount = amount % ch.length;
        return addAll(subarray(ch, amount, ch.length), subarray(ch, 0, amount));
    }
    
    public static char[] rotateRight(char[] ch, int amount) {
        amount = amount % ch.length;
        return addAll(subarray(ch, ch.length - amount, ch.length),
                      subarray(ch, 0, ch.length - amount));
    }
}