import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aocd.Puzzle;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

public class AoC2015_16 extends AoCBase {
    
    private static final int[] VALUES = new int[] {
            /* Thing.CHILDREN */ 3,
            /* Thing.CATS */ 7,
            /* Thing.SAMOYEDS */ 2,
            /* Thing.POMERANIANS */ 3,
            /* Thing.AKITAS */ 0,
            /* Thing.VIZSLAS */ 0,
            /* Thing.GOLDFISH */ 5,
            /* Thing.TREES */ 3,
            /* Thing.CARS */ 2,
            /* Thing.PERFUMES */ 1 };
    private final List<AuntSue> auntSues;
    
    private AoC2015_16(final List<String> inputs, final boolean debug) {
        super(debug);
        this.auntSues = inputs.stream().map(AuntSue::fromInput).collect(toList());
    }

    public static final AoC2015_16 create(final List<String> input) {
        return new AoC2015_16(input, false);
    }

    public static final AoC2015_16 createDebug(final List<String> input) {
        return new AoC2015_16(input, true);
    }
    
    private AuntSue findAuntSueWithBestScore(final Op[] ops) {
        return this.auntSues.stream()
            .collect(toMap(
                Function.identity(),
                sue -> IntStream.range(0, Thing.values().length)
                    .filter(thing -> ops[thing].matches(sue.things[thing], VALUES[thing]))
                    .count()))
            .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .findFirst()
                .map(Entry::getKey)
                .orElseThrow();
    }
    
    @Override
    public Integer solvePart1() {
        final Op[] ops = new Op[] {
                Op.EQ, Op.EQ, Op.EQ, Op.EQ, Op.EQ,
                Op.EQ, Op.EQ, Op.EQ, Op.EQ, Op.EQ };
        return findAuntSueWithBestScore(ops).nbr;
    }
    
    @Override
    public Integer solvePart2() {
        final Op[] ops = new Op[] {
                Op.EQ, Op.GT, Op.EQ, Op.LT, Op.EQ,
                Op.EQ, Op.LT, Op.GT, Op.EQ, Op.EQ };
        return findAuntSueWithBestScore(ops).nbr;
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = puzzle(AoC2015_16.class);
        final AoC2015_16 solution = AoC2015_16.create(puzzle.getInputData());
        puzzle.check(
            () -> lap("Part 1", solution::solvePart1),
            () -> lap("Part 2", solution::solvePart2)
        );
    }
    
    private enum Thing {
        CHILDREN("children"),
        CATS("cats"),
        SAMOYEDS("samoyeds"),
        POMERANIANS("pomeranians"),
        AKITAS("akitas"),
        VIZSLAS("vizslas"),
        GOLDFISH("goldfish"),
        TREES("trees"),
        CARS("cars"),
        PERFUMES("perfumes");
        
        private final String value;
        
        Thing(final String value) {
            this.value = value;
        }
        
        public static final Thing fromString(final String string) {
            return Stream.of(values())
                    .filter(v -> v.value.equals(string))
                    .findFirst().orElseThrow();
        }
    }
    
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static final class AuntSue {
        @EqualsAndHashCode.Include
        private final int nbr;
        private final Integer[] things;
        
        public static AuntSue fromInput(final String s) {
            final String[] splits = s.replaceAll("[,:]", "").split(" ");
            final Integer[] things = new Integer[Thing.values().length];
            Range.range(2, splits.length, 2).intStream()
                .forEach(i -> {
                   final int idx = Thing.fromString(splits[i]).ordinal();
                   things[idx] = Integer.valueOf(splits[i + 1]);
                });
            return new AuntSue(Integer.parseInt(splits[1]), things);
        }
    }
    
    private enum Op {
        EQ, LT, GT;
        
        public boolean matches(final Integer lhs, final int rhs) {
            if (this == Op.EQ) {
                return lhs != null && lhs == rhs;
            } else if (this == Op.LT) {
                return lhs != null && lhs < rhs;
            } else {
                return lhs != null && lhs > rhs;
            }
        }
    }
}
