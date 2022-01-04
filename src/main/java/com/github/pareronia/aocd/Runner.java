package com.github.pareronia.aocd;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.pareronia.aocd.RunServer.RequestHandler;

public class Runner {

	public static void main(final String[] args) throws Exception {
		final Response result = Runner.create().run(args);
		System.out.println(result.toString());
	}
	
	public static RequestHandler createRequestHandler() {
		return request -> {
			final String[] args = request.toArray(new String[request.size()]);
			return Runner.create().run(args).toString();
		};
	}

	private static Runner create() {
		return new Runner(() -> LocalDate.now(Aocd.AOC_TZ),
						  className -> Class.forName(className));
	}
	
	static Runner create(final Supplier<LocalDate> dateSupplier,
								final ClassFactory classFactory) {
		return new Runner(dateSupplier, classFactory);
	}
	
	private final Supplier<LocalDate> dateSupplier;
	private final ClassFactory classFactory;
	
	private Runner(final Supplier<LocalDate> dateSupplier,
				   final ClassFactory classFactory) {
		this.dateSupplier = dateSupplier;
		this.classFactory = classFactory;
	}

	Response run(final String args[]) throws Exception {
		final Request request = new Request(this.dateSupplier, args);
		final Class<?> klass;
		try {
			final String className = "AoC" + request.year.toString()
									+ "_" + String.format("%02d", request.day);
			klass = this.classFactory.getClass(className);
		} catch (final ClassNotFoundException e) {
			return Response.EMPTY;
		}
		final Object puzzle1 = createPuzzle(klass, List.copyOf(request.inputs));
		final Object part1 = klass.getDeclaredMethod("solvePart1").invoke(puzzle1);
		final Object puzzle2 = createPuzzle(klass, List.copyOf(request.inputs));
		final Object part2 = klass.getDeclaredMethod("solvePart2").invoke(puzzle2);
		return new Response(part1, part2);
	}

    private Object createPuzzle(final Class<?> klass, final List<String> inputs) throws Exception {
        return klass
				.getDeclaredMethod("create", List.class)
				.invoke(null, inputs);
    }
	
	private static final class Request {
		private final Integer year;
		private final Integer day;
		private final List<String> inputs;
		
		public Request(final Supplier<LocalDate> dateSupplier, final String args[]) {
			if (args == null || args.length < 3) {
				throw new IllegalArgumentException("Missing args: year, day, input");
			}
			final LocalDate now = dateSupplier.get();
			final Integer year = Integer.valueOf(args[0]);
			if (year < 2015 || year > now.getYear()) {
				throw new IllegalArgumentException("Invalid year");
			}
			this.year = year;
			final Integer day = Integer.valueOf(args[1]);
			if ((year == now.getYear() && now.getMonth() == Month.DECEMBER && day > now.getDayOfMonth())
					|| day < 1 || day > 25) {
				throw new IllegalArgumentException("Invalid day");
			}
			this.day = day;
			this.inputs = Stream.iterate(2, i -> i + 1).limit(args.length - 2)
							.map(i -> args[i])
							.flatMap(string -> Stream.of(string.split("\\r?\\n")))
							.collect(toList());
		}
	}
	
	static final class Response {
		public static final Response EMPTY = new Response(null, null);
		
		private final Object part1;
		private final Object part2;
		
		public Response(final Object part1, final Object part2) {
			this.part1 = part1;
			this.part2 = part2;
		}

		@Override
		public String toString() {
			return Stream.of(part1, part2)
				.filter(Objects::nonNull)
				.map(Object::toString)
				.collect(joining(System.lineSeparator()));
		}
	}
	
	interface ClassFactory {
		Class<?> getClass(String className) throws ClassNotFoundException;
	}
}
