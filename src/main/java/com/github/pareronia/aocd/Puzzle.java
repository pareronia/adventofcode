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

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

public class Puzzle {
	
	private final SystemUtils systemUtils;
	@Getter
	private final int year;
	@Getter
	private final int day;
	private final Path inputDataFile;
	private final Path titleFile;
	private final Path answer1File;
	private final Path answer2File;
	private final FailDecider failDecider;
	private final LocalDateTime releaseTime;
	private String title;
	private String answer1;
	private String answer2;

	private Puzzle(final SystemUtils systemUtils, final Integer year, final Integer day, final User user, final Path aocdDir) {
		this.systemUtils = systemUtils;
		this.year = year;
		this.day = day;
		this.inputDataFile = user.getMemoDir().resolve(String.format("%d_%02d_input.txt", year, day));
		this.titleFile = aocdDir.resolve("titles").resolve(String.format("%d_%02d.txt", year, day));
		this.answer1File = user.getMemoDir().resolve(String.format("%d_%02da_answer.txt", year, day));
		this.answer2File = user.getMemoDir().resolve(String.format("%d_%02db_answer.txt", year, day));
		this.failDecider = new FailDecider();
		this.releaseTime = LocalDate.of(year, Month.DECEMBER, day).atStartOfDay(Aocd.AOC_TZ).toLocalDateTime();
	}

	public static final Puzzle create(final Integer year, final Integer day, final String name) {
	    return create(year, day, User.getUser(name));
	}

	public static final Puzzle create(final Integer year, final Integer day) {
		return create(year, day, User.getDefaultUser());
	}
	
	public static final Puzzle create(final Integer year, final Integer day, final User user) {
		final SystemUtils systemUtils = new SystemUtils();
		final Path aocdDir = systemUtils.getAocdDir();
		return create(systemUtils, year, day, user, aocdDir);
	}
	
	public static final Puzzle create(final SystemUtils systemUtils, final Integer year, final Integer day, final User user, final Path aocdDir) {
		return new Puzzle(systemUtils, year, day, user, aocdDir);
	}
	
	public <V1, V2> void check(final Callable<V1> part1, final Callable<V2> part2) throws Exception {
	    final String[] fails = new String[2];
        final String answer1 = getAnswer1();
	    final V1 result1 = part1.call();
        if (failDecider.fail(answer1, result1) == FailDecider.Status.FAIL) {
            fails[0] = String.format(
                         "%sPart 1: Expected: '%s', got '%s'",
                         System.lineSeparator(), answer1, result1);
        }
        final String answer2 = getAnswer2();
	    final V2 result2 = part2.call();
        if (failDecider.fail(answer2, result2) == FailDecider.Status.FAIL) {
            fails[1] = String.format(
                        "%sPart 2: Expected: '%s', got '%s'",
                        System.lineSeparator(), answer2, result2);
        }
        if (StringUtils.isNotBlank(fails[0]) || StringUtils.isNotBlank(fails[1])) {
            throw new AssertionError(StringUtils.join(fails));
        }
	}

	public String getTitle() {
		if (this.title == null) {
			this.title = systemUtils.readFirstLineIfExists(titleFile).orElse(StringUtils.EMPTY);
		}
		return this.title;
	}

	public String getAnswer1() {
	    if (StringUtils.isEmpty(this.answer1)) {
	        this.answer1 = systemUtils.readFirstLineIfExists(answer1File)
	                            .orElse(StringUtils.EMPTY);
	    }
	    return this.answer1;
	}

	public String getAnswer2() {
	    if (StringUtils.isEmpty(this.answer2)) {
	        this.answer2 = systemUtils.readFirstLineIfExists(answer2File)
	                            .orElse(StringUtils.EMPTY);
	    }
	    return this.answer2;
	}
	
	public List<String> getInputData() {
		List<String> inputData = systemUtils.readAllLinesIfExists(inputDataFile);
	    if (CollectionUtils.isEmpty(inputData)) {
	        if (!isReleased()) {
	            System.err.println("!! PUZZLE NOT YET RELEASED !!");
	            return inputData;
	        }
	        systemUtils.getInput(year, day, inputDataFile);
	        inputData = systemUtils.readAllLinesIfExists(inputDataFile);
	    }
	    if (CollectionUtils.isEmpty(inputData)) {
	        System.err.println("!! INPUT DATA MISSING !!");
	    }
	    return inputData;
	}
	
	boolean isReleased() {
	    return !systemUtils.getLocalDateTime().isBefore(releaseTime);
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
}
