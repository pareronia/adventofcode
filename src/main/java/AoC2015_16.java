import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_16 extends SolutionBase<List<AoC2015_16.AuntSue>, Integer, Integer> {
    
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
    
    private AoC2015_16(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_16 create() {
        return new AoC2015_16(false);
    }

    public static final AoC2015_16 createDebug() {
        return new AoC2015_16(true);
    }
    
    private AuntSue findAuntSueWithBestScore(final List<AuntSue> auntSues, final Op[] ops) {
        return auntSues.stream()
            .collect(toMap(
                Function.identity(),
                sue -> IntStream.range(0, Thing.values().length)
                    .filter(thing -> ops[thing].matches(sue.getThings()[thing], VALUES[thing]))
                    .count()))
            .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .findFirst()
                .map(Entry::getKey)
                .orElseThrow();
    }

    @Override
    public List<AuntSue> parseInput(final List<String> inputs) {
        return inputs.stream().map(AuntSue::fromInput).collect(toList());
    }

    @Override
    public Integer solvePart1(final List<AuntSue> sues) {
        final Op[] ops = new Op[] {
                Op.EQ, Op.EQ, Op.EQ, Op.EQ, Op.EQ,
                Op.EQ, Op.EQ, Op.EQ, Op.EQ, Op.EQ };
        return findAuntSueWithBestScore(sues, ops).getNbr();
    }

    @Override
    public Integer solvePart2(final List<AuntSue> sues) {
        final Op[] ops = new Op[] {
                Op.EQ, Op.GT, Op.EQ, Op.LT, Op.EQ,
                Op.EQ, Op.LT, Op.GT, Op.EQ, Op.EQ };
        return findAuntSueWithBestScore(sues, ops).getNbr();
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2015_16.create().run();
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

    enum Thing {
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

    static final class AuntSue {
        private final int nbr;
        private final Integer[] things;
        
        protected AuntSue(int nbr, Integer[] things) {
            this.nbr = nbr;
            this.things = things;
        }
        
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
        
        public int getNbr() {
            return nbr;
        }
        
        public Integer[] getThings() {
            return things;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            AuntSue other = (AuntSue) obj;
            return nbr == other.nbr;
        }

        @Override
        public int hashCode() {
            return Objects.hash(nbr);
        }
    }
}
