package com.github.pareronia.aoc;

public class CharArrayUtils {

    public static final int INDEX_NOT_FOUND = -1;

    public static char[] clone(final char[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static char[] subarray(final char[] array, final int startInclusive, final int endExclusive) {
        AssertUtils.assertNotNull(array, () -> "Expected array to be non null");
        AssertUtils.assertTrue(
            startInclusive >= 0 && endExclusive <= array.length,
            () -> "index out of range");
        
        final int newSize = endExclusive - startInclusive;
        AssertUtils.assertTrue(
                newSize >= 0, () -> "Expected size to be non-negative");

        final char[] subarray = new char[newSize];
        System.arraycopy(array, startInclusive, subarray, 0, newSize);
        return subarray;
    }

    public static char[] addAll(final char[] array1, final char... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final char[] joinedArray = new char[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static void reverse(final char[] array, final int startInclusive, final int endExclusive) {
        AssertUtils.assertNotNull(array, () -> "Expected array to be non null");
        AssertUtils.assertTrue(
            startInclusive >= 0 && endExclusive <= array.length,
            () -> "index out of range");

        int i = startInclusive;
        int j = endExclusive - 1;
        char tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static int indexOf(final char[] array, final char value) {
        AssertUtils.assertNotNull(array, () -> "Expected array to be non null");
        
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static boolean contains(final char[] array, final char value) {
        return indexOf(array, value) != INDEX_NOT_FOUND;
    }
}
