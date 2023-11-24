package com.github.pareronia.aocd;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import com.github.pareronia.aocd.MultipleDaysRunner.Day;
import com.github.pareronia.aocd.MultipleDaysRunner.Listener;

public class AllDaysRunner {
    
    private final SystemUtils systemUtils = new SystemUtils();
    private final MultipleDaysRunner multipleDaysRunner = new MultipleDaysRunner();
    
	public void run(final Listener listener) throws Exception {
	   final Set<Day> allDays = systemUtils.getAllSolutions()
	       .map(s -> {
	           final int year = Integer.parseInt(s.substring(3, 7));
	           final int day = Integer.parseInt(s.substring(8, 10));
	           return Day.at(year, day);
	       })
	       .collect(toSet());
	   multipleDaysRunner.run(allDays, listener);
	}

	public static void main(final String[] _args) throws Exception {
	    new AllDaysRunner().run(new Listener() {});
	}
}
