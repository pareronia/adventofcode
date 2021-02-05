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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public class SystemUtils {

	public Path getAocdDir() {
		final String env = System.getenv("AOCD_DIR");
		if (StringUtils.isNotBlank(env)) {
			return Paths.get(env);
		} else if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
			return Paths.get(System.getenv("APPDATA"), "aocd");
		} else if (org.apache.commons.lang3.SystemUtils.IS_OS_UNIX) {
			final Path userHome = org.apache.commons.lang3.SystemUtils.getUserHome().toPath();
			return userHome.resolve(".config").resolve("aocd");
		} else {
			throw new UnsupportedOperationException("OS not supported");
		}
	}
	
	public String getToken() {
		final String tokenFromEnv = System.getenv("AOC_SESSION");
		if (StringUtils.isNotBlank(tokenFromEnv)) {
			return tokenFromEnv;
		}
		return readAlLines(getAocdDir().resolve("token")).stream()
				.findFirst()
				.orElseThrow(() -> new AocdException("Missing session ID"));
	}

	public List<String> readAllLinesIfExists(Path path) {
 		if (Files.notExists(Objects.requireNonNull(path))) {
			return Collections.emptyList();
		}
		return readAlLines(path);
	}
	
	public Optional<String> readFirstLineIfExists(Path path) {
		return readAllLinesIfExists(path).stream().findFirst();
	}
	
	private List<String> readAlLines(Path path) {
		try {
			return Collections.unmodifiableList(
					Files.readAllLines(Objects.requireNonNull(path),
									   StandardCharsets.UTF_8));
		} catch (final IOException e) {
			throw new AocdException(e);
		}
	}
}
