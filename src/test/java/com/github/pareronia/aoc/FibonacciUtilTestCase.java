package com.github.pareronia.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class FibonacciUtilTestCase {

    @Test
    public void binet() {
        assertThat(FibonacciUtil.binet(00), is(0L));
        assertThat(FibonacciUtil.binet(01), is(1L));
        assertThat(FibonacciUtil.binet(02), is(1L));
        assertThat(FibonacciUtil.binet(03), is(2L));
        assertThat(FibonacciUtil.binet(04), is(3L));
        assertThat(FibonacciUtil.binet(05), is(5L));
        assertThat(FibonacciUtil.binet(27), is(196_418L));
        assertThat(FibonacciUtil.binet(35), is(9_227_465L));
    }
}
