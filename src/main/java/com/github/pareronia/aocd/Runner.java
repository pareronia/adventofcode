package com.github.pareronia.aocd;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aocd.RunServer.RequestHandler;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Runner {

	public static void main(final String[] args) throws Exception {
	    final SystemUtils systemUtils = new SystemUtils();
	    final Request request = Request.create(systemUtils.getLocalDate(), args);
		final Response result = Runner.create(systemUtils).run(request);
		System.out.println(result.toString());
	}
	
	public static RequestHandler createRequestHandler(final SystemUtils systemUtils) {
		return input -> {
			final String[] args = input.toArray(new String[input.size()]);
			final Request request = Request.create(systemUtils.getLocalDate(), args);
			return Runner.create(systemUtils).run(request).toString();
		};
	}

	static Runner create(final SystemUtils systemUtils) {
		return new Runner(systemUtils,
						  className -> Class.forName(className));
	}
	
	static Runner create(final SystemUtils systemUtils,
						 final ClassFactory classFactory) {
		return new Runner(systemUtils, classFactory);
	}
	
	private final SystemUtils systemUtils;
	private final ClassFactory classFactory;
	
	Response run(final Request request) throws Exception {
		final Class<?> klass;
		try {
			final String className = "AoC" + request.year.toString()
									+ "_" + String.format("%02d", request.day);
			klass = this.classFactory.getClass(className);
		} catch (final ClassNotFoundException e) {
			return Response.EMPTY;
		}
        warmUpPart(1, klass, List.copyOf(request.inputs));
		final Result result1 = runPart(1, klass, List.copyOf(request.inputs));
		warmUpPart(2, klass, List.copyOf(request.inputs));
		final Result result2 = runPart(2, klass, List.copyOf(request.inputs));
		return Response.create(result1, result2);
	}

    private void warmUpPart(final int part, final Class<?> klass, final List<String> input) {
        try {
            final Object puzzle = createPuzzle(klass, input);
            final Method method = klass.getDeclaredMethod("solvePart" + part);
            method.invoke(puzzle);
        } catch (final Exception e) {
        }
    }

    private Result runPart(
            final int part,
            final Class<?> klass,
            final List<String> input)
        throws Exception
    {
        final Object puzzle = createPuzzle(klass, input);
        final Method method = klass.getDeclaredMethod("solvePart" + part);
		final long start = systemUtils.getSystemNanoTime();
		final Object answer = method.invoke(puzzle);
		final Duration duration = Duration.ofNanos(systemUtils.getSystemNanoTime() - start);
		return new Result(answer, duration);
    }

    private Object createPuzzle(final Class<?> klass, final List<String> input) throws Exception {
        return klass
		        .getDeclaredMethod("create", List.class)
		        .invoke(null, input);
    }
	
	@RequiredArgsConstructor
	private static final class Result {
	    private final Object answer;
	    private final Duration duration;
	}
	
	@RequiredArgsConstructor
	static final class Request {
		private final Integer year;
		private final Integer day;
		private final List<String> inputs;
		
		public static Request create(final LocalDate date, final String args[]) {
			if (args == null || args.length < 3) {
				throw new IllegalArgumentException("Missing args: year, day, input");
			}
			final Integer year = Integer.valueOf(args[0]);
			if (year < 2015 || year > date.getYear()) {
				throw new IllegalArgumentException("Invalid year");
			}
			final Integer day = Integer.valueOf(args[1]);
			if ((year == date.getYear() && date.getMonth() == Month.DECEMBER && day > date.getDayOfMonth())
					|| day < 1 || day > 25) {
				throw new IllegalArgumentException("Invalid day");
			}
			final List<String> inputs
			        = Stream.iterate(2, i -> i + 1).limit(args.length - 2)
							.map(i -> args[i])
							.flatMap(string -> Stream.of(string.split("\\r?\\n")))
							.collect(toList());
			return new Request(year, day, inputs);
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	static final class Response {
		public static final Response EMPTY = new Response(null, null);
		
		private final Part part1;
		private final Part part2;
		
		protected static Response create(
		        final Result result1, final Result result2) {
		    return new Response(
		        new Part(result1.answer.toString(), result1.duration.toNanos()),
		        new Part(result2.answer.toString(), result2.duration.toNanos()));
        }

        @Override
		public String toString() {
            return new Gson().toJson(this);
		}
        
        @RequiredArgsConstructor
        @Getter
        public static final class Part {
            private final String answer;
            private final Long duration;
        }
	}
	
	interface ClassFactory {
		Class<?> getClass(String className) throws ClassNotFoundException;
	}
}
