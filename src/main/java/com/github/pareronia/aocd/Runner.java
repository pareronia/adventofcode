package com.github.pareronia.aocd;

import static com.github.pareronia.aoc.AssertUtils.assertFalse;
import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Json;
import com.github.pareronia.aoc.solution.SolutionBase;
import com.github.pareronia.aoc.solution.Timed;
import com.github.pareronia.aocd.RunServer.RequestHandler;

class Runner {

	protected Runner(final SystemUtils systemUtils, final ClassFactory classFactory) {
        this.systemUtils = systemUtils;
        this.classFactory = classFactory;
    }

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
						  Class::forName);
	}
	
	static Runner create(final SystemUtils systemUtils,
						 final ClassFactory classFactory) {
		return new Runner(systemUtils, classFactory);
	}
	
	private final SystemUtils systemUtils;
	private final ClassFactory classFactory;
	
	Response run(final Request request) throws Exception {
	    return run(request, true);
	}
	
	Response run(final Request request, final boolean warmup) throws Exception {
		final Class<?> klass;
		try {
			final String className = "AoC" + request.year.toString()
									+ "_" + String.format("%02d", request.day);
			klass = this.classFactory.getClass(className);
		} catch (final ClassNotFoundException e) {
			return Response.EMPTY;
		}
		if (SolutionBase.class.isAssignableFrom(klass)) {
		    if (warmup) {
		        warmUpSolutionPart(1, klass, List.copyOf(request.inputs));
		    }
		    final Result result1 = runSolutionPart(1, klass, List.copyOf(request.inputs));
		    if (warmup) {
		        warmUpSolutionPart(2, klass, List.copyOf(request.inputs));
		    }
		    final Result result2 = runSolutionPart(2, klass, List.copyOf(request.inputs));
		    return Response.create(result1, result2);
		} else {
		    if (warmup) {
		        warmUpPart(1, klass, List.copyOf(request.inputs));
		    }
		    final Result result1 = runPart(1, klass, List.copyOf(request.inputs));
		    if (warmup) {
		        warmUpPart(2, klass, List.copyOf(request.inputs));
		    }
		    final Result result2 = runPart(2, klass, List.copyOf(request.inputs));
		    return Response.create(result1, result2);
		}
	}

    private void warmUpPart(final int part, final Class<?> klass, final List<String> input) {
        try {
            final Object puzzle = createPuzzle(klass, input);
            final Method method = klass.getDeclaredMethod("solvePart" + part);
            method.invoke(puzzle);
        } catch (final Exception e) {
        }
    }

    private void warmUpSolutionPart(final int part, final Class<?> klass, final List<String> input) {
        try {
            final Object puzzle = createPuzzle(klass);
            final Method method = klass.getDeclaredMethod("part" + part);
            method.invoke(puzzle, input);
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
        final Timed<Object> timed = Timed.timed(
                () -> method.invoke(puzzle), systemUtils::getSystemNanoTime);
		return new Result(timed.getResult(), timed.getDuration());
    }
    
    private Result runSolutionPart(
            final int part,
            final Class<?> klass,
            final List<String> input)
        throws Exception
    {
        final Object puzzle = createPuzzle(klass);
        final Method method = SolutionBase.class.getDeclaredMethod("part" + part, List.class);
        final Timed<Object> timed = Timed.timed(
                () -> method.invoke(puzzle, input), systemUtils::getSystemNanoTime);
		return new Result(timed.getResult(), timed.getDuration());
    }

    private Object createPuzzle(final Class<?> klass, final List<String> input) throws Exception {
        return klass
		        .getDeclaredMethod("create", List.class)
		        .invoke(null, input);
    }
	
    private Object createPuzzle(final Class<?> klass) throws Exception {
        return klass
		        .getDeclaredMethod("create")
		        .invoke(null);
    }
	
	private static final class Result {
	    protected Result(final Object answer, final Duration duration) {
            this.answer = answer;
            this.duration = duration;
        }
        private final Object answer;
	    private final Duration duration;
	}
	
	static final class Request {
		protected Request(final Integer year, final Integer day, final List<String> inputs) {
            this.year = year;
            this.day = day;
            this.inputs = inputs;
        }

        private final Integer year;
		private final Integer day;
		private final List<String> inputs;
		
		public static Request create(final LocalDate date, final String args[]) {
		    assertTrue(args != null && args.length >= 3,
		            () -> "Missing args: year, day, input");
			final Integer year = Integer.valueOf(args[0]);
			assertTrue(year >= 2015 && year <= date.getYear(),
			        () -> "Invalid year");
			final Integer day = Integer.valueOf(args[1]);
			assertFalse(
			    (year == date.getYear() && date.getMonth() == Month.DECEMBER && day > date.getDayOfMonth())
					    || day < 1 || day > 25,
				() -> "Invalid day");
			final List<String> inputs
			        = Stream.iterate(2, i -> i + 1).limit(args.length - 2)
							.map(i -> args[i])
							.flatMap(string -> Stream.of(string.split("\\r?\\n")))
							.collect(toList());
			return new Request(year, day, inputs);
		}
	}
	
	static final class Response {
		public static final Response EMPTY = new Response(null, null);
		
		private final Part part1;
		private final Part part2;
		
		protected Response(final Part part1, final Part part2) {
            this.part1 = part1;
            this.part2 = part2;
        }

        protected static Response create(
		        final Result result1, final Result result2) {
		    return new Response(
		        new Part(result1.answer.toString(), result1.duration.toNanos()),
		        new Part(result2.answer.toString(), result2.duration.toNanos()));
        }

        public Part getPart1() {
            return part1;
        }

        public Part getPart2() {
            return part2;
        }

        @Override
		public String toString() {
            return Json.toJson(this);
		}
        
        public static final class Part {
            private final String answer;
            private final Long duration;
            
            protected Part(final String answer, final Long duration) {
                this.answer = answer;
                this.duration = duration;
            }

            public String getAnswer() {
                return answer;
            }

            public Long getDuration() {
                return duration;
            }
        }
	}
	
	interface ClassFactory {
		Class<?> getClass(String className) throws ClassNotFoundException;
	}
}
