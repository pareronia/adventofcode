package com.github.pareronia.aoc.solution;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;

public final class Timed<V> {
    private final V result;
    private final Duration duration;
    
    private Timed(final V result, final Duration duration) {
        this.result = result;
        this.duration = duration;
    }

    public static <V> Timed<V> timed(final Callable<V> callable) throws Exception {
	    final long timerStart = System.nanoTime();
	    final V answer = callable.call();
	    return new Timed<>(
	            answer,
	            Duration.of(System.nanoTime() - timerStart, ChronoUnit.NANOS));
    }

    public V getResult() {
        return result;
    }

    public Duration getDuration() {
        return duration;
    }
}
