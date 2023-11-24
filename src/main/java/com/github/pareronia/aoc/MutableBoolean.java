package com.github.pareronia.aoc;

public class MutableBoolean {

    private boolean value;

    public MutableBoolean(final boolean value) {
        this.value = value;
    }

    public void setTrue() {
        value = true;
    }
    
    public void setFalse() {
        value = false;
    }
    
    public boolean isTrue() {
        return value;
    }
}
