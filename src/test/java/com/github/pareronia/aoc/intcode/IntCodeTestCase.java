package com.github.pareronia.aoc.intcode;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Test;

public class IntCodeTestCase {

    @Test
    public void test1() {
        final List<Integer> program = asList(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50);

        final Integer result = new IntCode().run(program);
        
        assertThat(result, is(3500));
    }

    @Test
    public void test2() {
        final List<Integer> program = asList(1, 0, 0, 0, 99);
        
        final Integer result = new IntCode().run(program);
        
        assertThat(result, is(2));
    }

    @Test
    public void test3() {
        final List<Integer> program = asList(1, 1, 1, 4, 99, 5, 6, 0, 99);
        
        final Integer result = new IntCode().run(program);
        
        assertThat(result, is(30));
    }
}
