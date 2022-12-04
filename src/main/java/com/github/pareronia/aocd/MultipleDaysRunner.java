package com.github.pareronia.aocd;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.github.pareronia.aocd.Puzzle.FailDecider.Status;
import com.github.pareronia.aocd.Runner.Request;
import com.github.pareronia.aocd.Runner.Response;
import com.github.pareronia.aocd.Runner.Response.Part;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
            final Puzzle puzzle = Aocd.puzzle(day.year, day.day);
	        final List<String> input = new ArrayList<>();
	        input.add(String.valueOf(puzzle.getYear()));
	        input.add(String.valueOf(puzzle.getDay()));
	        input.addAll(puzzle.getInputData());
			final String[] args = input.toArray(new String[input.size()]);
			final Request request = Request.create(systemUtils.getLocalDate(), args);
			final Response response = Runner.create(systemUtils).run(request);
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

	@RequiredArgsConstructor(staticName = "at")
	@Getter
	public static final class Day implements Comparable<Day> {
	    private final int year;
	    private final int day;
	    
        @Override
        public int compareTo(final Day other) {
        return comparing(Day::getYear)
                .thenComparing(comparing(Day::getDay))
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
	        final Puzzle.FailDecider failDecider = new Puzzle.FailDecider();
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
