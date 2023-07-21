package com.github.pareronia.aocd;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class PuzzleTestCase {
	
	private static final String FS = File.separator;
	
	private Puzzle puzzle;
	
	private User user;
	private SystemUtils systemUtils;
	
	@BeforeEach
	public void setUp() {
		systemUtils = mock(SystemUtils.class);
		user = mock(User.class);
		when(user.getMemoDir()).thenReturn(Paths.get("memoDir"));
		final Path aocdDir = Paths.get("aocdDir");
		
		puzzle = Puzzle.create(systemUtils, 2015, 7, user, aocdDir);
	}
	
	@Test
	public void isReleased() {
	    when(systemUtils.getLocalDateTime()).thenReturn(
	            LocalDateTime.of(2015, Month.JANUARY, 1, 0, 0, 0),
	            LocalDateTime.of(2015, Month.DECEMBER, 1, 0, 0, 0),
	            LocalDateTime.of(2015, Month.DECEMBER, 6, 23, 59, 59),
	            LocalDateTime.of(2015, Month.DECEMBER, 7, 0, 0, 0),
	            LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0)
	    );
	    assertThat(puzzle.isReleased()).isFalse();
	    assertThat(puzzle.isReleased()).isFalse();
	    assertThat(puzzle.isReleased()).isFalse();
	    assertThat(puzzle.isReleased()).isTrue();
	    assertThat(puzzle.isReleased()).isTrue();
	}

    @Test
    @SuppressWarnings("unchecked")
	public void testGetAnswer1() {
		when(systemUtils.readFirstLineIfExists(Mockito.any(Path.class)))
				.thenReturn(Optional.empty(), Optional.of("answer1"));
		
	    assertThat(puzzle.getAnswer1()).isEqualTo("");
	    assertThat(puzzle.getAnswer1()).isEqualTo("answer1");
	    
	    final ArgumentCaptor<Path> captor = ArgumentCaptor.forClass(Path.class);
	    verify(systemUtils, times(2)).readFirstLineIfExists(captor.capture());
	    assertThat(captor.getValue().toString())
	            .isEqualTo("memoDir" + FS + "2015_07a_answer.txt");
	}

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAnswer2() {
        when(systemUtils.readFirstLineIfExists(Mockito.any(Path.class)))
                .thenReturn(Optional.empty(), Optional.of("answer2"));
        
	    assertThat(puzzle.getAnswer2()).isEqualTo("");
	    assertThat(puzzle.getAnswer2()).isEqualTo("answer2");
        
        final ArgumentCaptor<Path> captor = ArgumentCaptor.forClass(Path.class);
        verify(systemUtils, times(2)).readFirstLineIfExists(captor.capture());
	    assertThat(captor.getValue().toString())
	            .isEqualTo("memoDir" + FS + "2015_07b_answer.txt");
    }
	
	@Test
	public void getTitle() {
		when(systemUtils.readFirstLineIfExists(Mockito.any(Path.class)))
				.thenReturn(Optional.of("title"));
		
		assertThat(puzzle.getTitle()).isEqualTo("title");
		assertThat(puzzle.getTitle()).isEqualTo("title");
		final ArgumentCaptor<Path> captor = ArgumentCaptor.forClass(Path.class);
		verify(systemUtils).readFirstLineIfExists(captor.capture());
		verifyNoMoreInteractions(systemUtils);
		assertThat(captor.getValue().toString())
				   .isEqualTo("aocdDir" + FS + "titles" + FS + "2015_07.txt");
	}
	
	@Test
	public void getInputData() {
		when(systemUtils.readAllLinesIfExists(Mockito.any(Path.class)))
				.thenReturn(asList("line1", "line2"));
		
		final List<String> result = puzzle.getInputData();
		
		final ArgumentCaptor<Path> captor = ArgumentCaptor.forClass(Path.class);
		verify(systemUtils).readAllLinesIfExists(captor.capture());
		verifyNoMoreInteractions(systemUtils);
		assertThat(result).containsExactly("line1", "line2");
		assertThat(captor.getValue().toString())
				   .isEqualTo("memoDir" + FS + "2015_07_input.txt");
	}
}
