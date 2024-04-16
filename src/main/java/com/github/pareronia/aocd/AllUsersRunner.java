package com.github.pareronia.aocd;
import java.util.Set;

import com.github.pareronia.aocd.MultipleDaysRunner.Day;
import com.github.pareronia.aocd.MultipleDaysRunner.Listener;

public class AllUsersRunner {

    private static final Set<User> ALL_USERS = User.builder().getAllUsers();
    private static final Set<Day> DAYS = Set.of(
            Day.at(
               Integer.getInteger("year"),
               Integer.getInteger("day")
            )
    );

	public static void main(final String[] args) throws Exception {
	   new MultipleDaysRunner().run(DAYS, ALL_USERS, new Listener() {});
	}
}
