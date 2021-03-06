package com.github.pareronia.aoc;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.ArrayUtils.subarray;

import org.apache.commons.lang3.ArrayUtils;

public class StringOps {
    
    public static Integer[] getDigits(String s, int expected) {
        final Integer[] digits = Utils.asCharacterStream(s)
                .filter(Character::isDigit)
                .map(c -> Integer.valueOf(Character.digit(c, 10)))
                .collect(toList())
                .toArray(Integer[]::new);
        if (digits.length != expected) {
            throw new IllegalArgumentException(
                String.format("Expected %d, got %d", expected, digits.length));
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
    
    public static char[] swap(char[] ch, char first, char second) {
        final int i1 = ArrayUtils.indexOf(ch, first);
        final int i2 = ArrayUtils.indexOf(ch, second);
        final char temp = ch[i1];
        ch[i1] = ch[i2];
        ch[i2] = temp;
        return ch;
    }
    
    public static char[] swap(char[] ch, int first, int second) {
        final char temp = ch[first];
        ch[first] = ch[second];
        ch[second] = temp;
        return ch;
    }
}