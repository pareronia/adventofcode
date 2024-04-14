import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.pareronia.aoc.RangeInclusive;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_19
        extends SolutionBase<AoC2023_19.System, Long, Long> {
    
    private static final String IN = "in";

    private AoC2023_19(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_19 create() {
        return new AoC2023_19(false);
    }
    
    public static AoC2023_19 createDebug() {
        return new AoC2023_19(true);
    }
    
    @Override
    protected System parseInput(final List<String> inputs) {
        final List<List<String>> blocks = StringOps.toBlocks(inputs);
        final Map<String, Workflow> workflows = blocks.get(0).stream()
                .map(Workflow::fromString)
                .collect(toMap(w -> w.name, w -> w));
        final List<Part> parts = blocks.get(1).stream()
                .map(Part::fromString)
                .toList();
        return new System(workflows, parts);
    }

    @Override
    public Long solvePart1(final System system) {
        final List<Rule.Result> prs = system.parts.stream()
                .map(p -> new Rule.Result(PartRange.fromPart(p), IN))
                .toList();
        final Map<String, List<PartRange>> solution = this.solve(
                system.workflows, prs);
        return system.parts.stream()
            .filter(part -> solution.getOrDefault(Rule.Result.ACCEPT, List.of()).stream()
                        .anyMatch(acc -> acc.matches(part)))
            .mapToLong(Part::score)
            .sum();
    }
    
    @Override
    public Long solvePart2(final System system) {
        final Rule.Result pr = new Rule.Result(
                new PartRange(
                    RangeInclusive.between(1, 4000),
                    RangeInclusive.between(1, 4000),
                    RangeInclusive.between(1, 4000),
                    RangeInclusive.between(1, 4000)),
                IN);
        final Map<String, List<PartRange>> solution = this.solve(
                system.workflows, List.of(pr));
        return solution.entrySet().stream()
            .filter(e -> Rule.Result.ACCEPT.equals(e.getKey()))
            .mapToLong(e -> e.getValue().stream()
                        .mapToLong(PartRange::score)
                        .sum())
            .sum();
    }
    
    private Map<String, List<PartRange>> solve(
            final Map<String, Workflow> workflows,
            List<Rule.Result> prs
    ) {
        final Map<String, List<PartRange>> solution = new HashMap<>();
        while (!prs.isEmpty()) {
            final List<Rule.Result> newprs = new ArrayList<>();
            for (final Rule.Result pr : prs) {
                for(final Rule.Result res : workflows.get(pr.result).eval(pr.range)) {
                    if (Rule.Result.ACCEPT.equals(res.result)
                     || Rule.Result.REJECT.equals(res.result)) {
                        solution.computeIfAbsent(res.result, k -> new ArrayList<>()).add(res.range);
                    } else {
                        newprs.add(new Rule.Result(res.range, res.result));
                    }
                }
            }
            prs = newprs;
        }
        return solution;
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "19114"),
        @Sample(method = "part2", input = TEST, expected = "167409079868000"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_19.create().run();
    }

    private static final String TEST = """
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}

            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
            """;
    
    private record Part(int x, int m, int a, int s) {

        public static Part fromString(final String string) {
            final String[] splits
                    = string.substring(1, string.length() - 1).split(",");
            final int[] a = Arrays.stream(splits)
                    .map(sp -> sp.split("=")[1])
                    .mapToInt(Integer::parseInt)
                    .toArray();
            return new Part(a[0], a[1], a[2], a[3]);
        }
        
        public int score() {
            return x + m + a + s;
        }
    }
    
    private record PartRange(
            RangeInclusive<Integer> x,
            RangeInclusive<Integer> m,
            RangeInclusive<Integer> a,
            RangeInclusive<Integer> s
    ) {

        public static PartRange fromPart(final Part part) {
            return new PartRange(
                RangeInclusive.between(part.x, part.x),
                RangeInclusive.between(part.m, part.m),
                RangeInclusive.between(part.a, part.a),
                RangeInclusive.between(part.s, part.s));
        }
        
        public PartRange copyWith(
                final char prop, final RangeInclusive<Integer> value
        ) {
            return new PartRange(
                'x' == prop ? value : this.x,
                'm' == prop ? value : this.m,
                'a' == prop ? value : this.a,
                's' == prop ? value : this.s);
        }
        
        public PartRange copy() {
            return new PartRange(x, m, a, s);
        }
        
        public boolean matches(final Part part) {
            return x.contains(part.x) && m.contains(part.m)
                && a.contains(part.a) && s.contains(part.s);
        }
        
        public long score() {
            return List.of(x, m, a, s).stream()
                .mapToLong(r -> r.getMaximum() - r.getMinimum() + 1)
                .reduce(1L, (a, b) -> a * b);
        }
    }

    private record Rule(
            Optional<Character> operand1,
            Character operation,
            Optional<Integer> operand2,
            String result
    ) {
        
        public static final char CATCHALL = '_';
        
        public static Rule fromString(final String string) {
            if (string.contains(":")) {
                final StringSplit sp = StringOps.splitOnce(string, ":");
                return new Rule(
                    Optional.of(sp.left().charAt(0)),
                    sp.left().charAt(1),
                    Optional.of(Integer.parseInt(sp.left().substring(2))),
                    sp.right());
            } else {
                return new Rule(
                    Optional.empty(), CATCHALL, Optional.empty(), string);
            }
        }
        
        public List<Result> eval(final PartRange range) {
            if (operation == CATCHALL) {
                return List.of(new Result(range.copy(), result));
            } else {
                assert operand1.isPresent() && operand2.isPresent();
                final RangeInclusive<Integer> r = switch (operand1.get()) {
                    case 'x' -> range.x();
                    case 'm' -> range.m();
                    case 'a' -> range.a();
                    case 's' -> range.s();
                    default -> throw new IllegalArgumentException(
                            "Unexpected value: " + operand1.get());
                };
                final int[] match;
                final int[] noMatch;
                if (operation == '<') {
                    match = new int[] { r.getMinimum(), operand2.get() - 1 };
                    noMatch = new int[] { operand2.get(), r.getMaximum() };
                } else {
                    match = new int[] { operand2.get() + 1, r.getMaximum() };
                    noMatch = new int[] { r.getMinimum(), operand2.get() };
                }
                final List<Result> ans = new ArrayList<>();
                if (match[0] <= match[1]) {
                    final PartRange nr = range.copyWith(
                        operand1.get(),
                        RangeInclusive.between(match[0], match[1]));
                    ans.add(new Result(nr, result));
                }
                if (noMatch[0] <= noMatch[1]) {
                    final PartRange nr = range.copyWith(
                        operand1.get(),
                        RangeInclusive.between(noMatch[0], noMatch[1]));
                    ans.add(new Result(nr, Result.CONTINUE));
                }
                return ans;
            }
        }
        
        public record Result(PartRange range, String result) {
            
            public static final String CONTINUE = "=continue=";
            public static final String ACCEPT = "A";
            public static final String REJECT = "R";
        }
    }
    
    
    private record Workflow(String name, List<Rule> rules) {
        
        public static Workflow fromString(final String string) {
            final int i = string.indexOf('{');
            final String name = string.substring(0, i);
            final List<Rule> rules = Arrays.stream(
                    string
                        .substring(0, string.length() - 1)
                        .substring(i + 1)
                        .split(","))
                .map(Rule::fromString)
                .toList();
            return new Workflow(name, rules);
        }
        
        public List<Rule.Result> eval(final PartRange range) {
            final List<Rule.Result> ans = new ArrayList<>();
            List<PartRange> ranges = List.of(range);
            for (final Rule rule : rules) {
                final List<PartRange> newRanges = new ArrayList<>();
                for (final PartRange r : ranges) {
                    final List<Rule.Result> ress = rule.eval(r);
                    for (final Rule.Result res : ress) {
                        if (!Rule.Result.CONTINUE.equals(res.result)) {
                            ans.add(new Rule.Result(res.range, res.result));
                        } else {
                            newRanges.add(res.range);
                        }
                    }
                }
                ranges = newRanges;
            }
            return ans;
        }
    }
    
    record System(Map<String, Workflow> workflows, List<Part> parts) {}
}
