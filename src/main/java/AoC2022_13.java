import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import com.github.pareronia.aoc.Json;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_13 extends AoCBase {
    
    private final List<String> input;

    private AoC2022_13(final List<String> input, final boolean debug) {
        super(debug);
        this.input = input;
    }
    
    public static final AoC2022_13 create(final List<String> input) {
        return new AoC2022_13(input, false);
    }

    public static final AoC2022_13 createDebug(final List<String> input) {
        return new AoC2022_13(input, true);
    }
    
    @SuppressWarnings("unchecked")
    private int compare(final Object lhs, final Object rhs) {
        if (lhs instanceof Number && rhs instanceof Number) {
            return ((Number) lhs).intValue() - ((Number) rhs).intValue();
        } else if (lhs instanceof List && rhs instanceof Number) {
            return compare(lhs, List.of(rhs));
        } else if (lhs instanceof Number && rhs instanceof List) {
            return compare(List.of(lhs), rhs);
        } else if (lhs instanceof List && rhs instanceof List) {
            final List<Object> lst1 = (List<Object>) lhs;
            final List<Object> lst2 = (List<Object>) rhs;
            for (int i = 0; i < lst1.size(); i++) {
               final Object n1 = lst1.get(i);
               final Object n2;
               try {
                   n2 = lst2.get(i);
               } catch (final IndexOutOfBoundsException e) {
                   return 1;
               }
               final int res = compare(n1, n2);
               if (res == 0) {
                   continue;
               }
               return res;
            }
            return lst1.size() < lst2.size() ? -1 : 0;
        }
        throw new IllegalStateException("Unsolvable");
    }
    
    private List<?> parse(final String line) {
        return Json.fromJson(line, List.class);
    }
    
    @Override
    public Integer solvePart1() {
        final List<List<String>> blocks = toBlocks(this.input);
        return IntStream.rangeClosed(1, blocks.size())
                .filter(i ->  {
                    final List<?> lhs = parse(blocks.get(i - 1).get(0));
                    final List<?> rhs = parse(blocks.get(i - 1).get(1));
                    return compare(lhs, rhs) <= 0;
                })
                .sum();
    }

    @Override
    public Integer solvePart2() {
        final List<List<?>> packets = this.input.stream()
                .filter(line -> !line.isEmpty())
                .map(this::parse)
                .collect(toList());
        final Set<List<Integer>> dividers = Set.of(List.of(2), List.of(6));
        dividers.forEach(packets::add);
        Collections.sort(packets, (p1, p2) -> compare(p1, p2));
        return (int) LongStream.rangeClosed(1, packets.size())
                .filter(i -> dividers.contains(packets.get((int) i - 1)))
                .reduce(1L, (a, b) -> a * b);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_13.createDebug(TEST).solvePart1() == 13;
        assert AoC2022_13.createDebug(TEST).solvePart2() == 140;

        final Puzzle puzzle = Aocd.puzzle(2022, 13);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_13.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_13.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "[1,1,3,1,1]\r\n" +
        "[1,1,5,1,1]\r\n" +
        "\r\n" +
        "[[1],[2,3,4]]\r\n" +
        "[[1],4]\r\n" +
        "\r\n" +
        "[9]\r\n" +
        "[[8,7,6]]\r\n" +
        "\r\n" +
        "[[4,4],4,4]\r\n" +
        "[[4,4],4,4,4]\r\n" +
        "\r\n" +
        "[7,7,7,7]\r\n" +
        "[7,7,7]\r\n" +
        "\r\n" +
        "[]\r\n" +
        "[3]\r\n" +
        "\r\n" +
        "[[[]]]\r\n" +
        "[[]]\r\n" +
        "\r\n" +
        "[1,[2,[3,[4,[5,6,7]]]],8,9]\r\n" +
        "[1,[2,[3,[4,[5,6,0]]]],8,9]"
    );
}
