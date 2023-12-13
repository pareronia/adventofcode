package com.github.pareronia.aoc;
import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.CharArrayUtils.addAll;
import static com.github.pareronia.aoc.CharArrayUtils.subarray;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StringOps {
    
	public static List<String> splitLines(final String input) {
		return asList((Objects.requireNonNull(input) + "\n").split("\\r?\\n"));
	}
	
	public record StringSplit(String left, String right) {}
	
	public static StringSplit splitOnce(final String string, final String regex) {
	    final String[] splits = Objects.requireNonNull(string).split(regex);
	    return new StringSplit(splits[0], splits[1]);
	}
	
	public static List<List<String>> toBlocks(final List<String> inputs) {
		if (inputs.isEmpty()) {
			return Collections.emptyList();
		}
		final List<List<String>> blocks = new ArrayList<>();
		int i = 0;
		final int last = inputs.size() - 1;
		blocks.add(new ArrayList<>());
		for (int j = 0; j <= last; j++) {
			if (inputs.get(j).isEmpty()) {
				if (j != last) {
					blocks.add(new ArrayList<>());
					i++;
				}
			} else {
				blocks.get(i).add(inputs.get(j));
			}
		}
		return blocks;
	}
	
    public static Integer[] getDigits(final String s, final int expected) {
        final Integer[] digits = Utils.asCharacterStream(s)
                .filter(Character::isDigit)
                .map(c -> Integer.valueOf(Character.digit(c, 10)))
                .collect(toList())
                .toArray(Integer[]::new);
        AssertUtils.assertTrue(digits.length == expected,
                () -> String.format("Expected %d, got %d", expected, digits.length));
        return digits;
    }
    
    public static int[] getDigitsPrimitive(final String s, final int expected) {
        final int[] digits = Utils.asCharacterStream(s)
                .filter(Character::isDigit)
                .map(c -> Integer.valueOf(Character.digit(c, 10)))
                .mapToInt(Integer::intValue)
                .toArray();
        AssertUtils.assertTrue(digits.length == expected,
                () -> String.format("Expected %d, got %d", expected, digits.length));
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
        assertTrue(from != to, () -> "Expected from and to to be different");
        if (from < to) {
            final char[] ch1 = subarray(ch, 0, from);
            final char[] ch2 = { ch[from] };
            final char[] ch3 = subarray(ch, from + 1, to + 1);
            final char[] ch4 = subarray(ch, to + 1, ch.length);
            return addAll(ch1, addAll(ch3, addAll(ch2, ch4)));
        } else {
            final char[] ch1 = subarray(ch, 0, to);
            final char[] ch2 = subarray(ch, to, from);
            final char[] ch3 = { ch[from] };
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
        final int i1 = CharArrayUtils.indexOf(ch, first);
        final int i2 = CharArrayUtils.indexOf(ch, second);
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
    
    public static char nextLetter(final char c, final int shift) {
        AssertUtils.assertTrue(shift >= 0, () -> "expected shift to be non-negative");
        if (Character.isLowerCase(c)) {
            return (char) ((c + shift - 97) % 26 + 97);
        } else if (Character.isUpperCase(c)) {
            return (char) ((c + shift - 65) % 26 + 65);
        } else {
            throw new IllegalArgumentException("Expected alphabetic char");
        }
    }
    
    public static String translate(final String string, final String from, final String to) {
        AssertUtils.assertTrue(
            Objects.requireNonNull(from).length() == Objects.requireNonNull(to).length(),
            () -> "from and to should be same length");
        final char[] tmp = new char[string.length()];
        for (int i = 0; i < string.length(); i++) {
            final char ch = string.charAt(i);
            final int idx = from.indexOf(ch);
            tmp[i] = idx < 0 ? ch : to.charAt(idx);
        }
        return new String(tmp);
    }
}