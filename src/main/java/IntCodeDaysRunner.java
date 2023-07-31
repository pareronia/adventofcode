import java.util.Set;

import com.github.pareronia.aocd.MultipleDaysRunner;
import com.github.pareronia.aocd.MultipleDaysRunner.Day;
import com.github.pareronia.aocd.MultipleDaysRunner.Listener;

public class IntCodeDaysRunner {

    private static final Set<Day> DAYS = Set.of(
            Day.at(2019, 2),
            Day.at(2019, 5),
            Day.at(2019, 7),
            Day.at(2019, 9),
            Day.at(2019, 11),
            Day.at(2019, 13)
    );

	public static void main(final String[] args) throws Exception {
	   new MultipleDaysRunner().run(DAYS, new Listener() {});
	}
}
