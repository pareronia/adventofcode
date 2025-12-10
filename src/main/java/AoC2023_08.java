import static com.github.pareronia.aoc.Utils.last;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.itertools.IterTools;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_08
        extends SolutionBase<AoC2023_08.Map, Long, Long> {
    
    private AoC2023_08(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_08 create() {
        return new AoC2023_08(false);
    }
    
    public static AoC2023_08 createDebug() {
        return new AoC2023_08(true);
    }
    
    @Override
    protected Map parseInput(final List<String> inputs) {
        return Map.fromInput(inputs);
    }

    @Override
    public Long solvePart1(final Map map) {
        return map.steps("AAA");
    }
    
    @Override
    public Long solvePart2(final Map map) {
        return map.forks().keySet().stream()
            .filter(key -> last(key) == 'A')
            .map(map::steps)
            .map(BigInteger::valueOf)
            .reduce((a, b) -> a.multiply(b).divide(a.gcd(b)))
            .get().longValue();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "2"),
        @Sample(method = "part1", input = TEST2, expected = "6"),
        @Sample(method = "part2", input = TEST3, expected = "6"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_08.create().run();
    }
    
    record Map(List<Character> instructions, java.util.Map<String, Fork> forks) {
        private record Fork(String left, String right) {}
        
        public static Map fromInput(final List<String> lines) {
            final List<List<String>> blocks = StringOps.toBlocks(lines);
            final List<Character> instructions
                    = Utils.asCharacterStream(blocks.get(0).get(0)).toList();
            final java.util.Map<String, Fork> forks = new HashMap<>();
            for (final String line : blocks.get(1)) {
                final String[] splits = line.split(" = ");
                forks.put(
                    splits[0],
                    new Fork(splits[1].substring(1, 4), splits[1].substring(6, 9)));
            }
            return new Map(instructions, forks);
        }
        
        public long steps(String key) {
            final Iterator<Character> inss = IterTools.cycle(this.instructions);
            Fork fork = this.forks.get(key);
            long ans = 0;
            while (last(key) != 'Z') {
                key = inss.next() == 'L' ? fork.left : fork.right;
                fork = this.forks.get(key);
                ans++;
            }
            return ans;
        }
    }

    private static final String TEST1 = """
            RL
            
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """;
    private static final String TEST2 = """
            LLR
            
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
            """;
    private static final String TEST3 = """
            LR
            
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
            """;
}
