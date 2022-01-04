package com.github.pareronia.aocd;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;
import org.mockito.Mockito;

import com.github.pareronia.aocd.Runner.ClassFactory;
import com.github.pareronia.aocd.Runner.Response;

public class RunnerTestCase {
	
	@Test(expected = IllegalArgumentException.class)
	public void noArgs() throws Exception {
		Runner.create(null, null).run(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void missingArgs() throws Exception {
		Runner.create(null, null).run(new String[] {"1", "2"});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void requestedYearBefore2015() throws Exception {
		final Supplier<LocalDate> dateSupplier = () -> LocalDate.of(2021, Month.FEBRUARY, 7);
		Runner.create(dateSupplier, null).run(new String[] {"2014", "1", ""});
	}

	@Test(expected = IllegalArgumentException.class)
	public void requestedYearAfterCurrent() throws Exception {
		final Supplier<LocalDate> dateSupplier = () -> LocalDate.of(2021, Month.FEBRUARY, 7);
		Runner.create(dateSupplier, null).run(new String[] {"2022", "1", ""});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void requestedDayAfterCurrentInDecember() throws Exception {
		final Supplier<LocalDate> dateSupplier = () -> LocalDate.of(2021, Month.DECEMBER, 7);
		Runner.create(dateSupplier, null).run(new String[] {"2021", "8", ""});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void requestedDayLowerThan1() throws Exception {
		final Supplier<LocalDate> dateSupplier = () -> LocalDate.of(2021, Month.FEBRUARY, 7);
		Runner.create(dateSupplier, null).run(new String[] {"2021", "0", ""});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void requestedDayHigherThan25() throws Exception {
		final Supplier<LocalDate> dateSupplier = () -> LocalDate.of(2021, Month.FEBRUARY, 7);
		Runner.create(dateSupplier, null).run(new String[] {"2021", "31", ""});
	}
	
	@Test
	public void emptyResponseWhenClassNotFound() throws Exception {
		final Supplier<LocalDate> dateSupplier = () -> LocalDate.of(2021, Month.FEBRUARY, 7);
		final ClassFactory classFactory = mock(ClassFactory.class);
		when(classFactory.getClass(Mockito.anyString()))
				.thenThrow(new ClassNotFoundException());
		
		final Response response = Runner.create(dateSupplier, classFactory)
				.run(new String[] {"2020", "1", ""});
		
		assertThat(response.toString(), is(""));
	}

	@Test
	public void happy() throws Exception {
		final Supplier<LocalDate> dateSupplier = () -> LocalDate.of(2021, Month.DECEMBER, 7);
		final ClassFactory classFactory = className -> Stub.class;
		
		final Response response = Runner.create(dateSupplier, classFactory)
				.run(new String[] {
						"2020",
						"8",
						"line1" + System.lineSeparator() + "line2", "line3"});
		
		assertThat(response.toString(), is("3" + System.lineSeparator() + "3"));
	}
	
	private static final class Stub {
	    private final List<String> strings;
		
		public Stub(final List<String> input) {
			assertThat(input, is(asList("line1", "line2", "line3")));
			this.strings = new ArrayList<>(input);
		}

		@SuppressWarnings("unused")
		public static Stub create(final List<String> input) {
			return new Stub(input);
		}
		
		@SuppressWarnings("unused")
		public Integer solvePart1() {
			final int result = this.strings.size();
			this.strings.set(0, "changed");
            return result;
		}

		@SuppressWarnings("unused")
		public Integer solvePart2() {
		    return this.strings.size();
		}
	}
}
