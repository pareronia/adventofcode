import static com.github.pareronia.aoc.Utils.last;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_07
        extends SolutionBase<List<AoC2024_07.Equation>, Long, Long> {
    
    private AoC2024_07(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_07 create() {
        return new AoC2024_07(false);
    }
    
    public static AoC2024_07 createDebug() {
        return new AoC2024_07(true);
    }
    
    @Override
    protected List<Equation> parseInput(final List<String> inputs) {
        final List<Equation> equations = new ArrayList<>();
        for (final String line : inputs) {
            final StringSplit splits = StringOps.splitOnce(line, ": ");
            final Long sol = Long.valueOf(splits.left());
            final List<Long> terms = Arrays.stream(splits.right()
                    .split(" "))
                    .map(Long::valueOf).toList();
            equations.add(new Equation(sol, terms));
        }
        return equations;
    }
    
    private long solve(final List<Equation> equations, final Set<Op> ops) {
        class Test {
           static boolean canObtain(
                   final long sol,
                   final List<Long> terms,
                   final Set<Op> ops
           ) {
               if (terms.size() == 1) {
                   return sol == terms.get(0);
               }
               if (ops.contains(Op.ADD)
                       && sol % last(terms) == 0
                       && canObtain(
                               sol / last(terms),
                               terms.subList(0, terms.size() - 1),
                               ops)) {
                   return true;
               }
               if (ops.contains(Op.MULTIPLY)
                       && sol > last(terms)
                       && canObtain(
                               sol - last(terms),
                               terms.subList(0, terms.size() - 1),
                               ops)) {
                   return true;
               }
               if (ops.contains(Op.CONCATENATE)) {
                   final String sSol = String.valueOf(sol);
                   final String sLast = String.valueOf(last(terms));
                   if (sSol.length() > sLast.length()
                           && sSol.endsWith(sLast)) {
                       final String newSol = sSol.substring(
                                       0, sSol.length() - sLast.length());
                       if (canObtain(
                               Long.parseLong(newSol),
                               terms.subList(0, terms.size() - 1),
                               ops)) {
                           return true;
                       }
                   }
               }
               return false;
           }
        }

        return equations.stream()
                .filter(eq -> Test.canObtain(eq.sol, eq.terms, ops))
                .mapToLong(Equation::sol)
                .sum();
    }
    
    @Override
    public Long solvePart1(final List<Equation> equations) {
        return solve(equations, Set.of(Op.ADD, Op.MULTIPLY));
    }
    
    @Override
    public Long solvePart2(final List<Equation> equations) {
        return solve(equations, Set.of(Op.ADD, Op.MULTIPLY, Op.CONCATENATE));
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "3749"),
        @Sample(method = "part2", input = TEST, expected = "11387"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_07.create().run();
    }

    private static final String TEST = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """;

    private enum Op {
        ADD,
        MULTIPLY,
        CONCATENATE;
    }

    record Equation(Long sol, List<Long> terms) {}
}