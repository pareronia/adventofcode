package com.github.pareronia.aocd;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

public class Runner {

	public static void main(String[] args) throws Exception {
		new Runner(args).run();
	}

	private final Integer year;
	private final Integer day;
	private final List<String> inputs;
	
	private Runner(String args[]) {
		if (args.length < 3) {
			throw new RuntimeException("Missing args: year, day, input");
		}
		final LocalDate now = LocalDate.now(Aocd.AOC_TZ);
		final Integer year = Integer.valueOf(args[0]);
		if (year < 2015 || year > now.getYear()) {
			throw new RuntimeException("Invalid year");
		}
		this.year = year;
		final Integer day = Integer.valueOf(args[1]);
		if ((now.getMonth() == Month.DECEMBER && day > now.getDayOfMonth())
				|| day < 1 || day > 25) {
			throw new RuntimeException("Invalid day");
		}
		this.day = day;
		this.inputs = Stream.iterate(2, i -> i + 1).limit(args.length - 2)
						.map(i -> args[i])
						.flatMap(string -> Stream.of(string.split("\\r?\\n")))
						.collect(toList());
	}
	
	private void run() throws Exception {
		final Class<?> klass;
		try {
			final String className = "AoC" + this.year.toString()
									+ "_" + String.format("%02d", this.day);
			klass = Class.forName(className);
		} catch (final ClassNotFoundException e) {
			return;
		}
		final Object puzzle = klass
				.getDeclaredMethod("create", List.class)
				.invoke(null, this.inputs);
		System.out.println(klass.getDeclaredMethod("solvePart1").invoke(puzzle));
		System.out.println(klass.getDeclaredMethod("solvePart2").invoke(puzzle));
	}
}
