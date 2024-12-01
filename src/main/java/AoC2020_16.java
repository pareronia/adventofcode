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

import com.github.pareronia.aoc.RangeInclusive;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_16
                extends SolutionBase<AoC2020_16.Notes, Integer, Long> {
    
	private AoC2020_16(final boolean debug) {
		super(debug);
	}

	public static AoC2020_16 create() {
		return new AoC2020_16(false);
	}

	public static AoC2020_16 createDebug() {
		return new AoC2020_16(true);
	}

    @Override
    protected AoC2020_16.Notes parseInput(final List<String> inputs) {
        return Notes.fromInput(inputs);
    }

    @Override
	public Integer solvePart1(final Notes notes) {
	    return notes.tickets.stream()
	            .flatMap(t -> t.invalidValues(notes.rules).stream())
	            .mapToInt(Integer::valueOf)
	            .sum();
	}

	@Override
	public Long solvePart2(final Notes notes) {
	    return Matches.create(notes.tickets, notes.rules).stream()
                .filter(m -> m.rule.field.startsWith("departure "))
                .mapToLong(m -> notes.myTicket.values.get(m.idx))
                .reduce(1, (a, b) -> a * b);
	}

	@Samples({
	    @Sample(method = "part1", input = TEST1, expected = "71"),
	    @Sample(method = "part2", input = TEST2, expected = "1716"),
	})
	public static void main(final String[] args) throws Exception {
		AoC2020_16.create().run();
	}
	
	private static final String TEST1 = """
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
            """;
	private static final String TEST2 = """
            departure date: 0-1 or 4-19
            departure time: 0-5 or 8-19
            departure track: 0-13 or 16-19
            
            your ticket:
            11,12,13
            
            nearby tickets:
            3,9,18
            15,1,5
            5,14,9
            """;
	
	private record Rule(
	        String field,
	        Set<RangeInclusive<Integer>> validRanges
	) {
        
	    public static Rule parseRule(final String s) {
            final var splits1 = s.split(": ");
            final var builder = Rule.builder();
            builder.field(splits1[0]);
            for (final var split : splits1[1].split(" or ")) {
                final var splits3 = split.split("-");
                final int start = Integer.parseInt(splits3[0]);
                final int end = Integer.parseInt(splits3[1]);
                builder.validRange(RangeInclusive.between(start, end));
            }
            return builder.build();
        }
    
	    public boolean validate(final int value) {
	        return this.validRanges.stream().anyMatch(r -> r.contains(value));
	    }
	    
	    public static RuleBuilder builder() {
	        return new RuleBuilder();
	    }
	    
	    private static final class RuleBuilder {
	        private String field;
	        private final Set<RangeInclusive<Integer>> validRanges = new HashSet<>();
	        
	        public Rule build() {
	            return new Rule(this.field, this.validRanges);
	        }
	        
	        public RuleBuilder field(final String field) {
	            this.field = field;
	            return this;
	        }
	        
	        public RuleBuilder validRange(final RangeInclusive<Integer> validRange) {
	            this.validRanges.add(validRange);
	            return this;
	        }
	    }
	}
	
    private record Ticket(List<Integer> values) {
        
        public static Ticket parseTicket(final String s) {
            return new Ticket(Arrays.stream(s.split(","))
                    .map(Integer::parseInt)
                    .collect(toList()));
        }
    
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
    
    record Notes(
            Set<Rule> rules, Ticket myTicket, List<Ticket> tickets) {
        
        public static Notes fromInput(final List<String> input) {
            final List<List<String>> blocks = StringOps.toBlocks(input);
            final Set<Rule> rules = blocks.get(0).stream()
                            .map(Rule::parseRule)
                            .collect(toSet());
            final Ticket myTicket = Ticket.parseTicket(blocks.get(1).get(1));
            final List<Ticket> tickets = blocks.get(2).stream().skip(1)
                            .map(Ticket::parseTicket)
                            .collect(toList());
            return new Notes(rules, myTicket, tickets);
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