package com.github.pareronia.aoc;

/**
 * Fibonacci utilities.
 */
public final class FibonacciUtil {
    
    private static final double SQRT_5 = Math.sqrt(5);
    public static final double GOLDEN_RATIO = (1 + SQRT_5) / 2;
    private static final double PSI = 1 - GOLDEN_RATIO;
    
    private FibonacciUtil() {
        super();
    }

    public static Long binet(final Integer n) {
        final Double fibo = (Math.pow(GOLDEN_RATIO, n) - Math.pow(PSI, n)) / SQRT_5;
        return fibo.longValue();
    }
}
