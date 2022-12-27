package com.github.pareronia.aocd;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.github.pareronia.aocd.Puzzle.FailDecider.Status;
import com.github.pareronia.aocd.Runner.Request;
import com.github.pareronia.aocd.Runner.Response.Part;

public class MultipleDaysRunner {
    
    private static final Set<Day> DAYS = Set.of(
            Day.at(2018, 1),
            Day.at(2019, 5),
            Day.at(2021, 1),
            Day.at(2021, 2),
            Day.at(2021, 3),
            Day.at(2021, 23),
            Day.at(2021, 25)
    );
    
    private final SystemUtils systemUtils = new SystemUtils();
	
	public void run(final Set<Day> days, final Listener listener) throws Exception {
	    for (final Day day : new TreeSet<>(days)) {
            final var puzzle = Aocd.puzzle(day.year, day.day);
	        final List<String> input = new ArrayList<>();
	        input.add(String.valueOf(puzzle.getYear()));
	        input.add(String.valueOf(puzzle.getDay()));
	        input.addAll(puzzle.getInputData());
			final var args = input.toArray(new String[input.size()]);
			final var request = Request.create(systemUtils.getLocalDate(), args);
			final var response = Runner.create(systemUtils).run(request);
			final String result1 = Optional.ofNullable(response.getPart1())
			        .map(Part::getAnswer).orElse(null);
			listener.result(puzzle, 1, puzzle.getAnswer1(), result1);
			if (day.day == 25) {
			    continue;
			}
			final String result2 = Optional.ofNullable(response.getPart2())
			        .map(Part::getAnswer).orElse(null);
            listener.result(puzzle, 2, puzzle.getAnswer2(), result2);
        }
	}
	
	public static void main(final String[] _args) throws Exception {
	   new MultipleDaysRunner().run(DAYS, new Listener() {});
	}

	public static final record Day(int year, int day) implements Comparable<Day> {
	    
	    public static Day at(final int year, final int day) {
	        return new Day(year, day);
	    }
	    
        @Override
        public int compareTo(final Day other) {
            return comparing(Day::year).thenComparing(comparing(Day::day))
                    .compare(this, other);
        }
    }
	
	public interface Listener {
	    default void result(
	            final Puzzle puzzle,
	            final int part,
	            final String expected,
	            final String actual
	    ) {
	        final var failDecider = new Puzzle.FailDecider();
	        final String message;
	        final Status status = failDecider.fail(expected, actual);
            if (status == Puzzle.FailDecider.Status.FAIL) {
	            message = String.format(
	                "%d/%02d/%d: FAIL - expected '%s', got '%s'",
	                puzzle.getYear(), puzzle.getDay(), part, expected, actual);
            } else if (status == Puzzle.FailDecider.Status.UNKNOWN) {
	            message = String.format(
	                "%d/%02d/%d: UNKNOWN", puzzle.getYear(), puzzle.getDay(), part);
	        } else {
	            message = String.format(
	                "%d/%02d/%d: OK", puzzle.getYear(), puzzle.getDay(), part);
	        }
	        System.out.println(message);
	    }
	}
}
