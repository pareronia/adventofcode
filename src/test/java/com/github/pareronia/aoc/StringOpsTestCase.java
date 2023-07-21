package com.github.pareronia.aoc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class StringOpsTestCase {

    @Test
    public void getDigitsExpectedNotMatched() {
        assertThatThrownBy(() -> StringOps.getDigits("a b 1 c 2", 3))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getDigits() {
        assertThat(StringOps.getDigits("a b 1 c 2", 2)).containsExactly(1, 2);
        assertThat(StringOps.getDigits("abc ", 0)).isEmpty();
    }
    
    @Test
    public void hexToBinIlegalChar() {
        assertThatThrownBy(() -> StringOps.hexToBin("1G"))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void hexToBin() {
        assertThat(StringOps.hexToBin("0")).isEqualTo("0000");
        assertThat(StringOps.hexToBin("1")).isEqualTo("0001");
        assertThat(StringOps.hexToBin("1F")).isEqualTo("00011111");
        assertThat(StringOps.hexToBin("1f")).isEqualTo("00011111");
        assertThat(StringOps.hexToBin("FF")).isEqualTo("11111111");
        assertThat(StringOps.hexToBin("0F2F")).isEqualTo("0000111100101111");
        assertThat(StringOps.hexToBin("CAFEBABE")).isEqualTo("11001010111111101011101010111110");
    }
    
    @Test
    public void moveSamePositions() {
        assertThatThrownBy(() -> StringOps.move("".toCharArray(), 1, 1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void move() {
        assertThat(StringOps.move("bcdea".toCharArray(), 1, 4)).isEqualTo("bdeac".toCharArray());
        assertThat(StringOps.move("bcdea".toCharArray(), 0, 4)).isEqualTo("cdeab".toCharArray());
        assertThat(StringOps.move("bcdea".toCharArray(), 0, 1)).isEqualTo("cbdea".toCharArray());
        assertThat(StringOps.move("bcdea".toCharArray(), 3, 4)).isEqualTo("bcdae".toCharArray());
        
        assertThat(StringOps.move("bcdea".toCharArray(), 4, 1)).isEqualTo("bacde".toCharArray());
        assertThat(StringOps.move("bcdea".toCharArray(), 4, 0)).isEqualTo("abcde".toCharArray());
        assertThat(StringOps.move("bcdea".toCharArray(), 1, 0)).isEqualTo("cbdea".toCharArray());
        assertThat(StringOps.move("bcdea".toCharArray(), 4, 3)).isEqualTo("bcdae".toCharArray());
    }

    @Test
    public void rotateLeft() {
        assertThat(StringOps.rotateLeft("abcde".toCharArray(), 1)).isEqualTo("bcdea".toCharArray());
    }

    @Test
    public void rotateRight() {
        assertThat(StringOps.rotateRight("bcdea".toCharArray(), 1)).isEqualTo("abcde".toCharArray());
        assertThat(StringOps.rotateRight("ecabd".toCharArray(), 6)).isEqualTo("decab".toCharArray());
    }
    
    @Test
    public void swapChars() {
        assertThat(StringOps.swap("ebcda".toCharArray(), 'd', 'b')).isEqualTo("edcba".toCharArray());
    }

    @Test
    public void swapPositions() {
        assertThat(StringOps.swap("abcde".toCharArray(), 4, 0)).isEqualTo("ebcda".toCharArray());
    }
}
