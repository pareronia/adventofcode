package com.github.pareronia.aoc.solution;

import java.util.function.Supplier;

public interface LoggerEnabled {
    
	default void log(final Object obj) {
	    getLogger().log(obj);
	}

	default void trace(final Object obj) {
	    getLogger().trace(obj);
	}

	default void log(final Supplier<Object> supplier) {
	    getLogger().log(supplier);
	}

	default void trace(final Supplier<Object> supplier) {
	    getLogger().trace(supplier);
	}

    Logger getLogger();
}
