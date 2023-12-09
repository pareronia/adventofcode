package com.github.pareronia.aoc.solution;

import java.util.function.Supplier;

public class Logger {

    private final boolean debug;
    private boolean trace;

    public Logger(final boolean debug) {
        this.debug = debug;
    }
    
    public void setTrace(final boolean trace) {
        this.trace = trace;
    }

	public void log(final Object obj) {
		if (!debug) {
			return;
		}
		System.out.println(obj);
	}

	public void trace(final Object obj) {
		if (!trace) {
			return;
		}
		System.out.println(obj);
	}

	public void log(final Supplier<Object> supplier) {
		if (!debug) {
			return;
		}
		System.out.println(supplier.get());
	}

	public void trace(final Supplier<Object> supplier) {
		if (!trace) {
			return;
		}
		System.out.println(supplier.get());
	}
}
