package com.github.pareronia.aoc;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import org.junit.Test;

public class StringOpsTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void getDigitsExpectedNotMatched() {
        StringOps.getDigits("a b 1 c 2", 3);
    }

    @Test
    public void getDigits() {
        assertThat(asList(StringOps.getDigits("a b 1 c 2", 2)), is(asList(1, 2)));
        assertThat(asList(StringOps.getDigits("abc ", 0)), is(empty()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void moveSamePositions() {
        StringOps.move("".toCharArray(), 1, 1);
    }

    @Test
    public void move() {
        assertThat(StringOps.move("bcdea".toCharArray(), 1, 4), is("bdeac".toCharArray()));
        assertThat(StringOps.move("bcdea".toCharArray(), 0, 4), is("cdeab".toCharArray()));
        assertThat(StringOps.move("bcdea".toCharArray(), 0, 1), is("cbdea".toCharArray()));
        assertThat(StringOps.move("bcdea".toCharArray(), 3, 4), is("bcdae".toCharArray()));
        
        assertThat(StringOps.move("bcdea".toCharArray(), 4, 1), is("bacde".toCharArray()));
        assertThat(StringOps.move("bcdea".toCharArray(), 4, 0), is("abcde".toCharArray()));
        assertThat(StringOps.move("bcdea".toCharArray(), 1, 0), is("cbdea".toCharArray()));
        assertThat(StringOps.move("bcdea".toCharArray(), 4, 3), is("bcdae".toCharArray()));
    }

    @Test
    public void rotateLeft() {
        assertThat(StringOps.rotateLeft("abcde".toCharArray(), 1), is("bcdea".toCharArray()));
    }

    @Test
    public void rotateRight() {
        assertThat(StringOps.rotateRight("bcdea".toCharArray(), 1), is("abcde".toCharArray()));
        assertThat(StringOps.rotateRight("ecabd".toCharArray(), 6), is("decab".toCharArray()));
    }
    
    @Test
    public void swapChars() {
        assertThat(StringOps.swap("ebcda".toCharArray(), 'd', 'b'), is("edcba".toCharArray()));
    }

    @Test
    public void swapPositions() {
        assertThat(StringOps.swap("abcde".toCharArray(), 4, 0), is("ebcda".toCharArray()));
    }
}
