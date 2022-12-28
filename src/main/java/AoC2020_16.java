import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Range;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_16 extends AoCBase {
    
    private final Set<Rule> rules;
    private final Ticket myTicket;
    private final List<Ticket> tickets;
	
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
        final var splits1 = s.split(": ");
        final var builder = Rule.builder();
        builder.field(splits1[0]);
        for (final var split : splits1[1].split(" or ")) {
            final var splits3 = split.split("-");
            final int start = Integer.parseInt(splits3[0]);
            final int end = Integer.parseInt(splits3[1]);
            builder.validRange(Range.between(start, end));
        }
        return builder.build();
    }
    
    private final Ticket parseTicket(final String s) {
        return new Ticket(Arrays.stream(s.split(","))
                .map(Integer::parseInt)
                .collect(toList()));
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
	            .flatMap(t -> t.invalidValues(rules).stream())
	            .mapToInt(Integer::valueOf)
	            .sum();
	}

	@Override
	public Long solvePart2() {
	    return Matches.create(this.tickets, this.rules).stream()
                .filter(m -> m.rule.field.startsWith("departure "))
                .mapToLong(m -> this.myTicket.values.get(m.idx))
                .reduce(1, (a, b) -> a * b);
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
	
	private static final List<String> TEST1 = splitLines("""
            class: 1-3 or 5-7
            row: 6-11 or 33-44
            seat: 13-40 or 45-50
            
            your ticket:
            7,1,14
            
            nearby tickets:
            7,3,47
            40,4,50
            55,2,20
            38,6,12
            """);
	private static final List<String> TEST2 = splitLines("""
            departure date: 0-1 or 4-19
            departure time: 0-5 or 8-19
            departure track: 0-13 or 16-19
            
            your ticket:
            11,12,13
            
            nearby tickets:
            3,9,18
            15,1,5
            5,14,9
            """);
	
	private static final record Rule(String field, Set<Range> validRanges) {
	    
	    public boolean validate(final int value) {
	        return this.validRanges.stream().anyMatch(r -> r.contains(value));
	    }
	    
	    public static RuleBuilder builder() {
	        return new RuleBuilder();
	    }
	    
	    private static final class RuleBuilder {
	        private String field;
	        private final Set<Range> validRanges = new HashSet<>();
	        
	        public Rule build() {
	            return new Rule(this.field, this.validRanges);
	        }
	        
	        public RuleBuilder field(final String field) {
	            this.field = field;
	            return this;
	        }
	        
	        public RuleBuilder validRange(final Range validRange) {
	            this.validRanges.add(validRange);
	            return this;
	        }
	    }
	}
	
    private static final record Ticket(List<Integer> values) {
        
        public boolean invalid(final Set<Rule> rules) {
            return this.values.stream()
                    .anyMatch(fieldDoesNotMatchAnyRule(rules));
        }
        
        public List<Integer> invalidValues(final Set<Rule> rules) {
            return this.values.stream()
                .filter(fieldDoesNotMatchAnyRule(rules))
                .collect(toList());
        }

        private Predicate<Integer> fieldDoesNotMatchAnyRule(final Set<Rule> rules) {
            return v -> rules.stream().noneMatch(r -> r.validate(v));
        }
    }
    
    private static final record Match(Rule rule, int idx) { }
    
    private static final record Matches(Map<Rule, Set<Integer>> matches) {
    
        private static boolean matchColumn(final List<Ticket> tickets,
                                           final int column, final Rule rule) {
            return tickets.stream()
                    .map(t -> t.values.get(column))
                    .allMatch(rule::validate);
        }

        public static Matches create(
                final List<Ticket> tickets,
                final Set<Rule> rules
        ) {
            final List<Ticket> validTickets = tickets.stream()
                    .filter(t -> !t.invalid(rules))
                    .collect(toList());
            final Set<Integer> idxs = IntStream
                    .range(0, validTickets.get(0).values.size())
                    .boxed()
                    .collect(toSet());
            final Map<Rule, Set<Integer>> matches = new HashMap<>();
            for (final Rule rule : rules) {
                matches.put(rule, new HashSet<>());
            }
            for (final Rule rule : rules) {
                for (final Integer idx : idxs) {
                    if (matchColumn(validTickets, idx, rule)) {
                        matches.get(rule).add(idx);
                    }
                }
            }
            return new Matches(matches);
        }
        
        private void remove(final Match match) {
            this.matches.remove(match.rule);
            this.matches.values().stream()
                .forEach(v -> v.remove(match.idx));
        }
        
        private Match next() {
            final Rule rule = this.matches.entrySet().stream()
                    .filter(e -> e.getValue().size() == 1)
                    .map(Entry::getKey)
                    .findFirst().orElseThrow();
            final Integer idx = matches.get(rule).iterator().next();
            return new Match(rule, idx);
        }
        
        public Stream<Match> stream() {
            final Stream.Builder<Match> builder = Stream.builder();
            while (!this.matches.isEmpty()) {
                final Match nextMatch = next();
                builder.add(nextMatch);
                remove(nextMatch);
            }
            return builder.build();
        }
    }
}