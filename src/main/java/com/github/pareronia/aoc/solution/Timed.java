package com.github.pareronia.aoc.solution;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public record Timed<V>(V result, Duration duration) {
    
    public static <V> Timed<V> timed(
            final Callable<V> callable,
            final Supplier<Long> nanoTimeSupplier
    ) throws Exception
    {
	    final long timerStart = nanoTimeSupplier.get();
	    final V answer = callable.call();
	    final long timerEnd = nanoTimeSupplier.get();
	    return new Timed<>(
	            answer,
	            Duration.of(timerEnd - timerStart, ChronoUnit.NANOS));
    }
}
