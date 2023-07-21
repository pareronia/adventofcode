package com.github.pareronia.aocd;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.pareronia.aocd.Runner.ClassFactory;
import com.github.pareronia.aocd.Runner.Request;
import com.github.pareronia.aocd.Runner.Response;

public class RunnerTestCase {
    
    private SystemUtils systemUtils;
    
    @BeforeEach
    public void setUp() {
        systemUtils = mock(SystemUtils.class);
    }
	
	@Test
	public void noArgs() throws Exception {
	    assertThatThrownBy(() -> createRequest(null, null))
	            .isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void missingArgs() throws Exception {
	    assertThatThrownBy(() -> createRequest(null, new String[] {"1", "2"}))
	            .isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void requestedYearBefore2015() throws Exception {
	    assertThatThrownBy(() -> createRequest(
	            LocalDate.of(2021, Month.FEBRUARY, 7),
	            new String[] {"2014", "1", ""})
	    ).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void requestedYearAfterCurrent() throws Exception {
	    assertThatThrownBy(() -> createRequest(
	            LocalDate.of(2021, Month.FEBRUARY, 7),
	            new String[] {"2022", "1", ""})
	    ).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void requestedDayAfterCurrentInDecember() throws Exception {
	    assertThatThrownBy(() -> createRequest(
	            LocalDate.of(2021, Month.DECEMBER, 7),
	            new String[] {"2021", "8", ""})
	    ).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void requestedDayLowerThan1() throws Exception {
	    assertThatThrownBy(() -> createRequest(
	            LocalDate.of(2021, Month.FEBRUARY, 7),
	            new String[] {"2021", "0", ""})
	    ).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void requestedDayHigherThan25() throws Exception {
	    assertThatThrownBy(() -> createRequest(
	            LocalDate.of(2021, Month.FEBRUARY, 7),
	            new String[] {"2021", "31", ""})
	    ).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void emptyResponseWhenClassNotFound() throws Exception {
	    final Request request = createRequest(
	            LocalDate.of(2021, Month.FEBRUARY, 7),
	            new String[] {"2020", "1", ""});
		final ClassFactory classFactory = mock(ClassFactory.class);
		when(classFactory.getClass(Mockito.anyString()))
				.thenThrow(new ClassNotFoundException());
		final Response response = Runner.create(systemUtils, classFactory).run(request);
		
		assertThat(response.toString(), is("{}"));
	}

	@Test
	public void happy() throws Exception {
	    final Request request = createRequest(
	            LocalDate.of(2021, Month.DECEMBER, 7),
	            new String[] {
                        "2020",
                        "8",
                        "line1" + System.lineSeparator() + "line2", "line3"});
	    final ClassFactory classFactory = className -> Stub.class;
		when(systemUtils.getLocalDate()).thenReturn(LocalDate.of(2021, Month.DECEMBER, 7));
		when(systemUtils.getSystemNanoTime()).thenReturn(1_000L, 2_000L, 5_000L, 8_000L);
		final Response response = Runner.create(systemUtils, classFactory).run(request);
		
		assertThat(response.toString(), is(
		        "{\"part1\":"
		        + "{\"answer\":\"3\",\"duration\":1000},"
		        + "\"part2\":"
		        + "{\"answer\":\"3\",\"duration\":3000}}"));
	}
    
    private Request createRequest(final LocalDate date, final String[] args) {
        return Request.create(date, args);
    }
	
	private static final class Stub {
	    private final List<String> strings;
		
		public Stub(final List<String> input) {
			this.strings = new ArrayList<>(input == null ? List.of() : input);
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
