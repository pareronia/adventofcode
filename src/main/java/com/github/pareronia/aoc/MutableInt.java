package com.github.pareronia.aoc;

public class MutableInt {

    private int value;

    public MutableInt() {
       this(0);
    }
    public MutableInt(final int value) {
        this.value = value;
    }
    
    public void increment() {
        value++;
    }
    
    public int incrementAndGet() {
        value++;
        return value;
    }
    
    public int getAndIncrement() {
        final int last = value;
        value++;
        return last;
    }
    
    public void add(final int value) {
        this.value += value;
    }
    
    public int intValue() {
        return value;
    }

    public Integer getValue() {
        return Integer.valueOf(value);
    }
}
