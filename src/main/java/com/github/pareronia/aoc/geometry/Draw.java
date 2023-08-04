package com.github.pareronia.aoc.geometry;

import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Draw {

    public static List<String> draw(
	        final Set<Position> positions,
	        final char fill,
	        final char empty
	) {
        final IntSummaryStatistics statsX = positions.stream()
                .mapToInt(Position::getX).summaryStatistics();
        final IntSummaryStatistics statsY = positions.stream()
                .mapToInt(Position::getY).summaryStatistics();
        final int width = statsX.getMax() + 2;
        final List<String> strings
            = IntStream.rangeClosed(statsY.getMin(), statsY.getMax()).mapToObj(
                y -> IntStream.range(statsX.getMin(), width).mapToObj(
                     x -> positions.contains(Position.of(x, y)) ? fill : empty)
                    .collect(toAString()))
                .collect(toList());
        Collections.reverse(strings);
        return strings;
    }
}
