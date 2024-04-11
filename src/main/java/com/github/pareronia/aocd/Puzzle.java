/**
Some parts of this code:

Copyright (c) 2016 wim glenn

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.github.pareronia.aocd;

import static java.util.stream.Collectors.joining;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import com.github.pareronia.aoc.StringUtils;

public record Puzzle(
        int year,
        int day,
        User user,
        List<String> inputData,
        String answer1,
        String answer2
	) {
    
    public static PuzzleBuilder builder() {
        return new PuzzleBuilder(new SystemUtils());
    }

    @Deprecated
	public static final Puzzle create(final Integer year, final Integer day) {
	    return Puzzle.builder()
	                .year(year).day(day).user(User.getDefaultUser())
	            .build();
	}
	
    public <V1, V2> void check(final Callable<V1> part1, final Callable<V2> part2) throws Exception {
		final Puzzle.FailDecider failDecider = new Puzzle.FailDecider();
	    final String[] fails = new String[2];
	    final V1 result1 = part1.call();
        if (failDecider.fail(answer1, result1) == FailDecider.Status.FAIL) {
            fails[0] = String.format(
                         "%sPart 1: Expected: '%s', got '%s'",
                         System.lineSeparator(), answer1, result1);
        }
	    final V2 result2 = part2.call();
        if (failDecider.fail(answer2, result2) == FailDecider.Status.FAIL) {
            fails[1] = String.format(
                        "%sPart 2: Expected: '%s', got '%s'",
                        System.lineSeparator(), answer2, result2);
        }
        if (StringUtils.isNotBlank(fails[0]) || StringUtils.isNotBlank(fails[1])) {
            throw new AssertionError(Stream.of(fails).collect(joining()));
        }
	}
	
	@Deprecated
	public List<String> getInputData() {
	    return this.inputData;
	}
	
	public static final class FailDecider {
	    public enum Status { OK, FAIL, UNKNOWN }
	    
	    public <V> Status fail(final String answer, final V result) {
	        if (StringUtils.isEmpty(answer) || result == null) {
	            return Status.UNKNOWN;
	        }
	        return answer.equals(String.valueOf(result)) ? Status.OK : Status.FAIL;
	    }
	}

	public static class PuzzleBuilder {
        private final SystemUtils systemUtils;
        private int year;
        private int day;
        private User user;
        
        protected PuzzleBuilder(final SystemUtils systemUtils) {
            this.systemUtils = systemUtils;
        }

        public Puzzle build() {
            Objects.requireNonNull(year);
            Objects.requireNonNull(day);
            Objects.requireNonNull(user);
            final List<String> inputData = readInputData();
            final Path answer1File = user.memoDir()
                    .resolve(String.format("%d_%02da_answer.txt", year, day));
            final String answer1 = readAnswer(answer1File);
            final Path answer2File = user.memoDir()
                    .resolve(String.format("%d_%02db_answer.txt", year, day));
            final String answer2 = readAnswer(answer2File);
            return new Puzzle(year, day, user, inputData, answer1, answer2);
        }

        public PuzzleBuilder year(final int year) {
            this.year = year;
            return this;
        }

        public PuzzleBuilder day(final int day) {
            this.day = day;
            return this;
        }

        public PuzzleBuilder user(final User user) {
            this.user = user;
            return this;
        }

        private List<String> readInputData() {
            final Path inputDataFile = user.memoDir()
                    .resolve(String.format("%d_%02d_input.txt", year, day));
            List<String> inputData = systemUtils.readAllLinesIfExists(inputDataFile);
            if (!inputData.isEmpty()) {
                return inputData;
            }
            if (!isReleased()) {
                System.err.println("!! PUZZLE NOT YET RELEASED !!");
                return Collections.emptyList();
            }
            if (user.token().startsWith("offline|")) {
                return Collections.emptyList();
            }
            inputData = systemUtils.getInput(user.token(), year, day);
            if (inputData.isEmpty()) {
                System.err.println("!! INPUT DATA MISSING !!");
                return Collections.emptyList();
            }
            systemUtils.writeAllLines(inputDataFile, inputData);
            return inputData;
        }

        private String readAnswer(final Path answerFile) {
            return systemUtils.readFirstLineIfExists(answerFile)
                    .orElse(StringUtils.EMPTY);
        }
        
        private boolean isReleased() {
            final LocalDateTime releaseTime
                = LocalDate.of(year, Month.DECEMBER, day)
                    .atStartOfDay(Aocd.AOC_TZ)
                    .toLocalDateTime();
            return !systemUtils.getLocalDateTime().isBefore(releaseTime);
        }
    }
}
