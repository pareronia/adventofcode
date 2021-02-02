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
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Puzzle {
	
	private final SystemUtils systemUtils;
	private final Path inputDataFile;
	private final Path titleFile;
	private String title;

	private Puzzle(SystemUtils systemUtils, Integer year, Integer day, User user, Path aocdDir) {
		this.systemUtils = systemUtils;
		this.inputDataFile = user.getMemoDir().resolve(String.format("%d_%02d_input.txt", year, day));
		this.titleFile = aocdDir.resolve("titles").resolve(String.format("%d_%02d.txt", year, day));
	}

	public static final Puzzle create(Integer year, Integer day) {
		return create(year, day, User.getDefaultUser());
	}
	
	public static final Puzzle create(Integer year, Integer day, User user) {
		final SystemUtils systemUtils = new SystemUtils();
		final Path aocdDir = systemUtils.getAocdDir();
		return create(systemUtils, year, day, user, aocdDir);
	}
	
	public static final Puzzle create(SystemUtils systemUtils, Integer year, Integer day, User user, Path aocdDir) {
		return new Puzzle(systemUtils, year, day, user, aocdDir);
	}

	public String getTitle() {
		if (this.title == null) {
			this.title = systemUtils.readFirstLineIfExists(titleFile).orElse(StringUtils.EMPTY);
		}
		return this.title;
	}
	
	public List<String> getInputData() {
		return systemUtils.readAllLinesIfExists(inputDataFile);
	}
}
