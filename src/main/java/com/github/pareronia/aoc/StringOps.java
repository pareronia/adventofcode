package com.github.pareronia.aoc;
import static com.github.pareronia.aoc.CharArrayUtils.addAll;
import static com.github.pareronia.aoc.CharArrayUtils.subarray;
import static java.util.stream.Collectors.toList;

import org.apache.commons.lang3.ArrayUtils;

public class StringOps {
    
    public static Integer[] getDigits(final String s, final int expected) {
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
    
    public static int[] getDigitsPrimitive(final String s, final int expected) {
        final int[] digits = Utils.asCharacterStream(s)
                .filter(Character::isDigit)
                .map(c -> Integer.valueOf(Character.digit(c, 10)))
                .mapToInt(Integer::intValue)
                .toArray();
        if (digits.length != expected) {
            throw new IllegalArgumentException(
                    String.format("Expected %d, got %d", expected, digits.length));
        }
        return digits;
    }
    
    public static String hexToBin(final String hex) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            final char c = hex.charAt(i);
            final int n = Integer.parseInt(String.valueOf(c), 16);
            final String b = Integer.toBinaryString(n);
            for (int j = b.length(); j < 4; j++) {
                sb.append('0');
            }
            sb.append(b);
        }
        return sb.toString();
    }
    
    public static char[] move(final char[] ch, final int from, final int to) {
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
    
    public static char[] rotateLeft(final char[] ch, int amount) {
        amount = amount % ch.length;
        return addAll(subarray(ch, amount, ch.length), subarray(ch, 0, amount));
    }
    
    public static char[] rotateRight(final char[] ch, int amount) {
        amount = amount % ch.length;
        return addAll(subarray(ch, ch.length - amount, ch.length),
                      subarray(ch, 0, ch.length - amount));
    }
    
    public static char[] swap(final char[] ch, final char first, final char second) {
        final int i1 = ArrayUtils.indexOf(ch, first);
        final int i2 = ArrayUtils.indexOf(ch, second);
        final char temp = ch[i1];
        ch[i1] = ch[i2];
        ch[i2] = temp;
        return ch;
    }
    
    public static char[] swap(final char[] ch, final int first, final int second) {
        final char temp = ch[first];
        ch[first] = ch[second];
        ch[second] = temp;
        return ch;
    }
}