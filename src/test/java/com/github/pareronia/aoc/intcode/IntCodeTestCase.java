package com.github.pareronia.aoc.intcode;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

public class IntCodeTestCase {

    @Test
    public void test1() {
        final List<Integer> program = asList(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50);

        setUpIntCode().run(program);
        
        assertThat(program, is(asList(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)));
    }

    @Test
    public void test2() {
        final List<Integer> program = asList(1, 0, 0, 0, 99);
        
        setUpIntCode().run(program);
        
        assertThat(program, is(asList(2, 0, 0, 0, 99)));
    }

    @Test
    public void test3() {
        final List<Integer> program = asList(1, 1, 1, 4, 99, 5, 6, 0, 99);
        
        setUpIntCode().run(program);
        
        assertThat(program, is(asList(30, 1, 1, 4, 2, 5, 6, 0, 99)));
    }
    
    @Test
    public void testInputOutput() {
        final List<Integer> program = asList(3, 0, 4, 0, 99);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 123);
        final Integer result = intCode.getOutput();
        
        assertThat(result, is(123));
        assertThat(program, is(asList(123, 0, 4, 0, 99)));
    }
    
    @Test
    public void testModes1() {
        final List<Integer> program = asList(1002, 4, 3, 4, 33);

        setUpIntCode().run(program);
        
        assertThat(program, is(asList(1002, 4, 3, 4, 99)));
    }

    @Test
    public void testModes2() {
        final List<Integer> program = asList(1101, 100, -1, 4, 0);
        
        setUpIntCode().run(program);
        
        assertThat(program, is(asList(1101, 100, -1, 4, 99)));
    }
    
    @Test
    public void testEqualTo8_1() {
        final List<Integer> program = asList(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 8);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(1));
    }

    @Test
    public void testEqualTo8_2() {
        final List<Integer> program = asList(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 88);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(0));
    }

    @Test
    public void testEqualTo8_3() {
        final List<Integer> program = asList(3, 3, 1108, -1, 8, 3, 4, 3, 99);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 8);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(1));
    }

    @Test
    public void testEqualTo8_4() {
        final List<Integer> program = asList(3, 3, 1108, -1, 8, 3, 4, 3, 99);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 89);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(0));
    }

    @Test
    public void testLessThan8_1() {
        final List<Integer> program = asList(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 7);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(1));
    }

    @Test
    public void testLessThan8_2() {
        final List<Integer> program = asList(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 99);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(0));
    }
    
    @Test
    public void testLessThan8_3() {
        final List<Integer> program = asList(3, 3, 1107, -1, 8, 3, 4, 3, 99);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 0);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(1));
    }
    
    @Test
    public void testLessThan8_4() {
        final List<Integer> program = asList(3, 3, 1107, -1, 8, 3, 4, 3, 99);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 8);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(0));
    }
    
    @Test
    public void testJump1() {
        final List<Integer> program = asList(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 0);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(0));
    }
    
    @Test
    public void testJump2() {
        final List<Integer> program = asList(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 1);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(1));
    }
    
    @Test
    public void testJump3() {
        final List<Integer> program = asList(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 0);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(0));
    }
    
    @Test
    public void testJump4() {
        final List<Integer> program = asList(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 1);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(1));
    }

    @Test
    public void test4() {
        final List<Integer> program = asList(3, 21, 1008, 21, 8, 20, 1005, 20,
                22, 107, 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0, 0, 1002,
                21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46, 1101,
                1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 1);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(999));
    }

    @Test
    public void test5() {
        final List<Integer> program = asList(3, 21, 1008, 21, 8, 20, 1005, 20,
                22, 107, 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0, 0, 1002,
                21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46, 1101,
                1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 8);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(1000));
    }

    @Test
    public void test6() {
        final List<Integer> program = asList(3, 21, 1008, 21, 8, 20, 1005, 20,
                22, 107, 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0, 0, 1002,
                21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46, 1101,
                1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99);
        
        final IntCode intCode = setUpIntCode();
        intCode.run(program, 888);
        final Integer result = intCode.getOutput();
        
        assertThat(result , is(1001));
    }

    private IntCode setUpIntCode() {
        return new IntCode(!System.getProperties().containsKey("NDEBUG"));
    }
}
