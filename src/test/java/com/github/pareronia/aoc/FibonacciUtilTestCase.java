package com.github.pareronia.aoc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class FibonacciUtilTestCase {

    @Test
    public void binet() {
        assertThat(FibonacciUtil.binet(00)).isEqualTo(0L);
        assertThat(FibonacciUtil.binet(01)).isEqualTo(1L);
        assertThat(FibonacciUtil.binet(02)).isEqualTo(1L);
        assertThat(FibonacciUtil.binet(03)).isEqualTo(2L);
        assertThat(FibonacciUtil.binet(04)).isEqualTo(3L);
        assertThat(FibonacciUtil.binet(05)).isEqualTo(5L);
        assertThat(FibonacciUtil.binet(27)).isEqualTo(196_418L);
        assertThat(FibonacciUtil.binet(35)).isEqualTo(9_227_465L);
    }
}
