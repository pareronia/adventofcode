package com.github.pareronia.aocd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PuzzleTestCase {
	
	private User user;
	private List<String> input;
	private SystemUtils systemUtils;
	
	@BeforeEach
	void setUp() {
		systemUtils = mock(SystemUtils.class);
		user = new User("name", "token", "id", Paths.get("memoDir"));
		input = List.of("abc", "def");
	}
	
	@Test
	void puzzle() {
        final Puzzle puzzle = new Puzzle(2015, 7, user, input, "answer1", "answer2");
        
        assertThat(puzzle.answer1()).isEqualTo("answer1");
        assertThat(puzzle.answer2()).isEqualTo("answer2");
        assertThat(puzzle.day()).isEqualTo(7);
        assertThat(puzzle.inputData()).isSameAs(input);
        assertThat(puzzle.user()).isSameAs(user);
        assertThat(puzzle.year()).isEqualTo(2015);
	}
	
    @Test
	void puzzle_inputFromFile() {
	    when(systemUtils.getLocalDateTime())
	        .thenReturn(LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0));
	    when(systemUtils.readAllLinesIfExists(user.memoDir().resolve("2015_07_input.txt")))
	        .thenReturn(input);
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07a_answer.txt")))
	        .thenReturn(Optional.of("answer1"));
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07b_answer.txt")))
	        .thenReturn(Optional.empty());
	    
	    final Puzzle puzzle = new Puzzle.PuzzleBuilder(systemUtils)
	            .year(2015).day(7).user(user).build();
        
        assertThat(puzzle.answer1()).isEqualTo("answer1");
        assertThat(puzzle.answer2()).isEqualTo("");
	    assertThat(puzzle.day()).isEqualTo(7);
        assertThat(puzzle.inputData()).isEqualTo(input);
        assertThat(puzzle.user()).isSameAs(user);
        assertThat(puzzle.year()).isEqualTo(2015);
	}
	
    @Test
	void puzzle_notYetReleased() {
	    when(systemUtils.getLocalDateTime())
	        .thenReturn(LocalDateTime.of(2015, Month.JANUARY, 1, 0, 0, 0));
	    when(systemUtils.readAllLinesIfExists(user.memoDir().resolve("2015_07_input.txt")))
	        .thenReturn(List.of());
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07a_answer.txt")))
	        .thenReturn(Optional.of("answer1"));
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07b_answer.txt")))
	        .thenReturn(Optional.empty());
	    
	    final Puzzle puzzle = new Puzzle.PuzzleBuilder(systemUtils)
	            .year(2015).day(7).user(user).build();
        
        assertThat(puzzle.answer1()).isEqualTo("answer1");
        assertThat(puzzle.answer2()).isEqualTo("");
	    assertThat(puzzle.day()).isEqualTo(7);
        assertThat(puzzle.inputData()).isEmpty();
        assertThat(puzzle.user()).isSameAs(user);
        assertThat(puzzle.year()).isEqualTo(2015);
	}
	
    @Test
	void puzzle_inputOnlineForOfflineUser() {
		user = new User("name", "offline|token", "id", Paths.get("memoDir"));
	    when(systemUtils.getLocalDateTime())
	        .thenReturn(LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0));
	    when(systemUtils.readAllLinesIfExists(user.memoDir().resolve("2015_07_input.txt")))
	        .thenReturn(List.of());
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07a_answer.txt")))
	        .thenReturn(Optional.of("answer1"));
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07b_answer.txt")))
	        .thenReturn(Optional.empty());
	    
	    final Puzzle puzzle = new Puzzle.PuzzleBuilder(systemUtils)
	            .year(2015).day(7).user(user).build();
        
        assertThat(puzzle.answer1()).isEqualTo("answer1");
        assertThat(puzzle.answer2()).isEqualTo("");
	    assertThat(puzzle.day()).isEqualTo(7);
        assertThat(puzzle.inputData()).isEmpty();
        assertThat(puzzle.user()).isSameAs(user);
        assertThat(puzzle.year()).isEqualTo(2015);
	}
	
    @Test
	void puzzle_inputOnline() {
	    when(systemUtils.getLocalDateTime())
	        .thenReturn(LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0));
	    when(systemUtils.readAllLinesIfExists(user.memoDir().resolve("2015_07_input.txt")))
	        .thenReturn(List.of());
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07a_answer.txt")))
	        .thenReturn(Optional.of("answer1"));
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07b_answer.txt")))
	        .thenReturn(Optional.empty());
	    when(systemUtils.getInput("token", 2015, 7)).thenReturn(input);
	    
	    final Puzzle puzzle = new Puzzle.PuzzleBuilder(systemUtils)
	            .year(2015).day(7).user(user).build();
        
        assertThat(puzzle.answer1()).isEqualTo("answer1");
        assertThat(puzzle.answer2()).isEqualTo("");
	    assertThat(puzzle.day()).isEqualTo(7);
        assertThat(puzzle.inputData()).isEqualTo(input);
        assertThat(puzzle.user()).isSameAs(user);
        assertThat(puzzle.year()).isEqualTo(2015);
        verify(systemUtils).writeAllLines(user.memoDir().resolve("2015_07_input.txt"), input);
	}
	
    @Test
	void puzzle_inputNotFoundOnline() {
	    when(systemUtils.getLocalDateTime())
	        .thenReturn(LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0));
	    when(systemUtils.readAllLinesIfExists(user.memoDir().resolve("2015_07_input.txt")))
	        .thenReturn(List.of());
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07a_answer.txt")))
	        .thenReturn(Optional.of("answer1"));
	    when(systemUtils.readFirstLineIfExists(user.memoDir().resolve("2015_07b_answer.txt")))
	        .thenReturn(Optional.empty());
	    when(systemUtils.getInput("token", 2015, 7)).thenReturn(List.of());
	    
	    final Puzzle puzzle = new Puzzle.PuzzleBuilder(systemUtils)
	            .year(2015).day(7).user(user).build();
        
        assertThat(puzzle.answer1()).isEqualTo("answer1");
        assertThat(puzzle.answer2()).isEqualTo("");
	    assertThat(puzzle.day()).isEqualTo(7);
        assertThat(puzzle.inputData()).isEmpty();
        assertThat(puzzle.user()).isSameAs(user);
        assertThat(puzzle.year()).isEqualTo(2015);
	}
}
