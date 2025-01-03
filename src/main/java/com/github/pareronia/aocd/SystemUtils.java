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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.github.pareronia.aoc.Json;
import com.github.pareronia.aoc.StringUtils;

public class SystemUtils {

	public Path getAocdDir() {
		final String env = System.getenv("AOCD_DIR");
		if (StringUtils.isNotBlank(env)) {
			return Paths.get(env);
		} else if (isOsWindows()) {
			return Paths.get(System.getenv("APPDATA"), "aocd");
		} else if (isOsLinux()) {
			final Path userHome = getUserHome().toPath();
			return userHome.resolve(".config").resolve("aocd");
		} else {
			throw new UnsupportedOperationException("OS not supported");
		}
	}
	
	public String getTokenFromEnv() {
		return System.getenv("AOC_SESSION");
	}
	
    @SuppressWarnings("unchecked")
    public Map<String, String> readMapFromJsonFile(final Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
	        return Json.fromJson(reader, Map.class);
        } catch (final IOException e) {
			throw new AocdException(e);
        }
    }

	public List<String> readAllLinesIfExists(final Path path) {
 		if (Files.notExists(Objects.requireNonNull(path))) {
			return Collections.emptyList();
		}
		return readAllLines(path);
	}
	
	public Optional<String> readFirstLineIfExists(final Path path) {
		return readAllLinesIfExists(path).stream().findFirst();
	}
	
	public Optional<String> readFirstLine(final Path path) {
		return readAllLines(path).stream().findFirst();
	}
	
	public void writeAllLines(final Path path, final List<String> lines) {
	    try {
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (final IOException e) {
			throw new AocdException(e);
        }
    }
	
	public LocalDate getLocalDate() {
	    return LocalDate.now(Aocd.AOC_TZ);
	}
	
	public LocalDateTime getLocalDateTime() {
	    return LocalDateTime.now(Aocd.AOC_TZ);
	}
	
	public long getSystemNanoTime() {
	    return System.nanoTime();
	}
	
	public List<String> getInput(final String cookie, final int year, final int day) {
	    final HttpClient http = HttpClient.newHttpClient();
	    final HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create("https://adventofcode.com/%d/day/%d/input".formatted(year, day)))
	            .header("Cookie", "session=" + cookie)
	            .build();
        try {
            return http.send(request, BodyHandlers.ofLines()).body().toList();
        } catch (IOException | InterruptedException e) {
			throw new AocdException(e);
        }
	}
	
	public Stream<String> getAllSolutions() {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(
                Path.of("src", "main", "java"), "AoC????_??.java")) {
	        final Builder<String> builder = Stream.builder();
	        ds.iterator().forEachRemaining(path ->
	            builder.add(path.getFileName().toString()
	                            .substring(0, "AoCyyyy_dd".length())));
	        return builder.build();
        } catch (final IOException e) {
			throw new AocdException(e);
        }
	}
	
	public List<String> readAllLines(final Path path) {
		try {
			return Collections.unmodifiableList(
					Files.readAllLines(Objects.requireNonNull(path),
									   StandardCharsets.UTF_8));
		} catch (final IOException e) {
			throw new AocdException(e);
		}
	}
	
	private boolean isOsWindows() {
	    return getOsName().startsWith("Windows");
	}
	
	private boolean isOsLinux() {
	    return getOsName().toLowerCase().startsWith("linux");
	}
    
	public File getUserHome() {
        return new File(getSystemProperty("user.home"));
    }

    private String getOsName() {
        return getSystemProperty("os.name");
    }
	
	private String getSystemProperty(final String property) {
	    return System.getProperty(property);
	}

    public void clrscr() {
        // TODO Auto-generated method stub
        
    }
}
