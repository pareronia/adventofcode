import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.Utils.last;
import static com.github.pareronia.aoc.Utils.naturalNumbers;
import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_11 extends AoCBase {
    
    private final List<Monkey> monkeys;
    
    private AoC2022_11(final List<String> input, final boolean debug) {
        super(debug);
        this.monkeys = toBlocks(input).stream()
                .map(this::parseMonkey)
                .collect(toList());
        log(this.monkeys);
    }
    
    private final Monkey parseMonkey(final List<String> block) {
        final List<Long> items
                = Arrays.stream(naturalNumbers(block.get(1)))
                    .mapToLong(Long::valueOf)
                    .boxed().collect(toList());
        final Function<Long, Long> operation;
        final String[] splits = block.get(2).split(" = ")[1].split(" ");
        if ("old".equals(splits[2])) {
            operation = x -> x * x;
        } else if ("+".equals(splits[1])) {
            operation = x -> x + Integer.parseInt(splits[2]);
        } else {
            operation = x -> x * Integer.parseInt(splits[2]);
        }
        final int test = Integer.parseInt(last(block.get(3).split(" ")));
        final int throwTrue = Integer.parseInt(last(block.get(4).split(" ")));
        final int throwFalse = Integer.parseInt(last(block.get(5).split(" ")));
        return new Monkey(items, operation, test, throwTrue, throwFalse);
    }
    
    public static final AoC2022_11 create(final List<String> input) {
        return new AoC2022_11(input, false);
    }

    public static final AoC2022_11 createDebug(final List<String> input) {
        return new AoC2022_11(input, true);
    }
    
    private void round(
            final Map<Integer, Integer> counter,
            final Function<Long, Long> manage
    ) {
        range(monkeys.size()).forEach(i -> {
            final Monkey monkey = this.monkeys.get(i);
            for (final long item : monkey.items) {
                final long level = manage.apply(monkey.operation.apply(item));
                final int monkeyTo = level % monkey.test == 0
                        ? monkey.throwTrue : monkey.throwFalse;
                this.monkeys.get(monkeyTo).items.add(level);
            }
            counter.merge(i, monkey.items.size(), Integer::sum);
            monkey.items.clear();
        });
    }
    
    private long solve(final int rounds, final Function<Long, Long> manage) {
        final var counter = new HashMap<Integer, Integer>();
        Range.range(rounds).forEach(i -> round(counter, manage));
        return counter.values().stream()
                .sorted(reverseOrder())
                .limit(2)
                .mapToLong(Integer::longValue)
                .reduce(1L, (a, b) -> a * b);
    }
    
    @Override
    public Long solvePart1() {
        return solve(20, x -> x / 3);
    }

    @Override
    public Long solvePart2() {
        final int mod = this.monkeys.stream()
                .mapToInt(Monkey::test)
                .reduce(1, (a, b) -> a * b);
        return solve(10_000, x -> x % mod);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_11.createDebug(TEST).solvePart1() == 10_605;
        assert AoC2022_11.createDebug(TEST).solvePart2() == 2_713_310_158L;

        final Puzzle puzzle = Aocd.puzzle(2022, 11);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_11.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_11.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines("""
        Monkey 0:
          Starting items: 79, 98
          Operation: new = old * 19
          Test: divisible by 23
            If true: throw to monkey 2
            If false: throw to monkey 3
        
        Monkey 1:
          Starting items: 54, 65, 75, 74
          Operation: new = old + 6
          Test: divisible by 19
            If true: throw to monkey 2
            If false: throw to monkey 0
        
        Monkey 2:
          Starting items: 79, 60, 97
          Operation: new = old * old
          Test: divisible by 13
            If true: throw to monkey 1
            If false: throw to monkey 3
        
        Monkey 3:
          Starting items: 74
          Operation: new = old + 3
          Test: divisible by 17
            If true: throw to monkey 0
            If false: throw to monkey 1
        """);
    
    private static final record Monkey(
        List<Long> items,
        Function<Long, Long> operation,
        int test,
        int throwTrue,
        int throwFalse
    ) { }
}
