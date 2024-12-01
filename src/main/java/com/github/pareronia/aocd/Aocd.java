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

import java.time.ZoneId;
import java.util.List;

public class Aocd {
	
	public static final ZoneId AOC_TZ = ZoneId.of("America/New_York");
	
	@Deprecated
	public static List<String> getData(final Integer year, final Integer day) {
	    return Puzzle.builder()
	            .year(year).day(day).user(User.getDefaultUser())
	            .build()
	            .inputData();
	}

	@Deprecated
	public static Puzzle puzzle(final Integer year, final Integer day) {
		return Puzzle.builder()
		        .year(year).day(day).user(User.getDefaultUser())
		        .build();
	}
}
