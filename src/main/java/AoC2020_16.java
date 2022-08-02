import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Range;

import com.github.pareronia.aocd.Puzzle;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.ToString;

public class AoC2020_16 extends AoCBase {
    
    private final Set<Rule> rules;
    private final List<Integer> myTicket;
    private final List<List<Integer>> tickets;
	
	private AoC2020_16(final List<String> input, final boolean debug) {
		super(debug);
		final List<List<String>> blocks = toBlocks(input);
		this.rules = blocks.get(0).stream()
		                .map(this::parseRule)
		                .collect(toSet());
		this.myTicket = parseTicket(blocks.get(1).get(1));
		this.tickets = blocks.get(2).stream().skip(1)
		                .map(this::parseTicket)
		                .collect(toList());
	}

    private final Rule parseRule(final String s) {
        final String[] splits1 = s.split(": ");
        final Rule.RuleBuilder builder = Rule.builder();
        builder.field(splits1[0]);
        for (final String split : splits1[1].split(" or ")) {
            final String[] splits3 = split.split("-");
            final int start = Integer.parseInt(splits3[0]);
            final int end = Integer.parseInt(splits3[1]);
            builder.validRange(Range.between(start, end));
        }
        return builder.build();
    }
    
    private final List<Integer> parseTicket(final String s) {
        return Arrays.stream(s.split(","))
                .map(Integer::parseInt)
                .collect(toList());
    }
    
	public static AoC2020_16 create(final List<String> input) {
		return new AoC2020_16(input, false);
	}

	public static AoC2020_16 createDebug(final List<String> input) {
		return new AoC2020_16(input, true);
	}
	
	@Override
	public Integer solvePart1() {
	    return this.tickets.stream()
	            .flatMap(List::stream)
	            .filter(v -> this.rules.stream().noneMatch(r -> r.validate(v)))
	            .mapToInt(Integer::valueOf)
	            .sum();
	}

	@Override
	public Integer solvePart2() {
	    return 0;
	}

	public static void main(final String[] args) throws Exception {
		assert createDebug(TEST1).solvePart1() == 71;
		assert createDebug(TEST2).solvePart2() == 1716;
		
        final Puzzle puzzle = puzzle(AoC2020_16.class);
		final List<String> input = puzzle.getInputData();
        puzzle.check(
           () -> lap("Part 1", create(input)::solvePart1),
           () -> lap("Part 2", create(input)::solvePart2)
	    );
	}
	
	private static final List<String> TEST1 = splitLines(
			"class: 1-3 or 5-7\r\n"    +
			"row: 6-11 or 33-44\r\n"   +
			"seat: 13-40 or 45-50\r\n" +
			"\r\n"                     +
			"your ticket:\r\n"         +
			"7,1,14\r\n"               +
			"\r\n"                     +
			"nearby tickets:\r\n"      +
			"7,3,47\r\n"               +
			"40,4,50\r\n"              +
			"55,2,20\r\n"              +
			"38,6,12"
	);
	private static final List<String> TEST2 = splitLines(
			"departure date: 0-1 or 4-19\r\n"    +
			"departure time: 0-5 or 8-19\r\n"    +
			"departure track: 0-13 or 16-19\r\n" +
			"\r\n"                               +
			"your ticket:\r\n"                   +
			"11,12,13\r\n"                       +
			"\r\n"                               +
			"nearby tickets:\r\n"                +
			"3,9,18\r\n"                         +
			"15,1,5\r\n"                         +
			"5,14,9"
	);
	
	@RequiredArgsConstructor
	@Builder
	@ToString
	private static final class Rule {
	    private final String field;
	    @Singular
	    private final Set<Range<Integer>> validRanges;
	    
	    public boolean validate(final int value) {
	        return this.validRanges.stream().anyMatch(r -> r.contains(value));
	    }
	}
}