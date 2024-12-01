package com.github.pareronia.aoc.solution;

import static com.github.pareronia.aoc.StringOps.splitLines;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import com.github.pareronia.aocd.Puzzle;
import com.github.pareronia.aocd.SystemUtils;
import com.github.pareronia.aocd.User;

public class SolutionUtils {

    public static Puzzle puzzle(final Class<?> klass) {
       final String[] split
               = klass.getSimpleName().substring("AoC".length()).split("_");
       return Puzzle.builder()
               .year(Integer.parseInt(split[0]))
               .day(Integer.parseInt(split[1]))
               .user(User.getDefaultUser())
               .build();
    }
    
    public static String printDuration(final Duration duration) {
        final long timeSpent = duration.toNanos() / 1_000;
        double time;
        String unit;
        if (timeSpent < 1000) {
            time = timeSpent;
            unit = "µs";
        } else if (timeSpent < 1_000_000) {
            time = timeSpent / 1000.0;
            unit = "ms";
        } else {
            time = timeSpent / 1_000_000.0;
            unit = "s";
        }
        return String.format("%.3f %s", time, unit);
    }

    public static <V> V lap(final String prefix, final Callable<V> callable)
            throws Exception
    {
        final Timed<V> timed = Timed.timed(
                callable,
                () -> new SystemUtils().getSystemNanoTime());
        final V answer = timed.result();
        final String duration = printDuration(timed.duration());
        System.out.println(String.format("%s : %s, took: %s",
                                         prefix, answer, duration));
        return answer;
    }
    
    public static void runSamples(final Class<?> klass)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final List<Sample> samples = Stream.of(klass.getMethods())
                .filter(m -> m.isAnnotationPresent(Samples.class))
                .map(m -> m.getAnnotation(Samples.class))
                .flatMap(ann -> Stream.of(ann.value()))
                .collect(toList());
        for (final Sample sample : samples) {
            SolutionUtils.runSample(klass, sample);
        }
    }
    
    private static void runSample(final Class<?> klass, final Sample sample)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Object puzzle = klass
                .getDeclaredMethod("create" + (sample.debug() ? "Debug" : ""))
                .invoke(null);
        final List<String> input = splitLines(sample.input());
        final Object answer = puzzle.getClass()
                .getMethod(sample.method(), List.class)
                .invoke(puzzle, input);
        assert Objects.equals(sample.expected(), String.valueOf(answer))
            : String.format("FAIL '%s(%s)'. Expected: '%s', got '%s'",
                    sample.method(),
                    input,
                    sample.expected(),
                    String.valueOf(answer));
    }
}
