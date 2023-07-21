package com.github.pareronia.aoc;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import org.junit.jupiter.api.Test;

public class StringOpsTestCase {

    @Test
    public void getDigitsExpectedNotMatched() {
        assertThatThrownBy(() -> StringOps.getDigits("a b 1 c 2", 3))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getDigits() {
        assertThat(asList(StringOps.getDigits("a b 1 c 2", 2)), is(asList(1, 2)));
        assertThat(asList(StringOps.getDigits("abc ", 0)), is(empty()));
    }
    
    @Test
    public void hexToBinIlegalChar() {
        assertThatThrownBy(() -> StringOps.hexToBin("1G"))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void hexToBin() {
        assertThat(StringOps.hexToBin("0"), is("0000"));
        assertThat(StringOps.hexToBin("1"), is("0001"));
        assertThat(StringOps.hexToBin("1F"), is("00011111"));
        assertThat(StringOps.hexToBin("1f"), is("00011111"));
        assertThat(StringOps.hexToBin("FF"), is("11111111"));
        assertThat(StringOps.hexToBin("0F2F"), is("0000111100101111"));
        assertThat(StringOps.hexToBin("CAFEBABE"), is("11001010111111101011101010111110"));
    }
    
    @Test
    public void moveSamePositions() {
        assertThatThrownBy(() -> StringOps.move("".toCharArray(), 1, 1))
            .isInstanceOf(IllegalArgumentException.class);
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
