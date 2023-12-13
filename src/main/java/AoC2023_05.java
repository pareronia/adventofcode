import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.LongRange;
import com.github.pareronia.aoc.LongRange.JoinResult;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_05
        extends SolutionBase<AoC2023_05.Almanac, Long, Long> {
    
    private AoC2023_05(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_05 create() {
        return new AoC2023_05(false);
    }
    
    public static AoC2023_05 createDebug() {
        return new AoC2023_05(true);
    }
    
    @Override
    protected Almanac parseInput(final List<String> inputs) {
        return Almanac.fromInput(inputs);
    }
    
    private long solve(final Almanac almanac, final List<LongRange> seedRanges) {
        return almanac.getLocation(seedRanges).stream()
            .sorted()
            .findFirst().orElseThrow()
            .iterator().next();
    }

    @Override
    public Long solvePart1(final Almanac almanac) {
        log(almanac);
        final List<LongRange> seedRanges
            = almanac.seeds().stream()
                .map(s -> LongRange.between(s.longValue(), s.longValue() + 1L))
                .toList();
        return solve(almanac, seedRanges);
    }
    
    @Override
    public Long solvePart2(final Almanac almanac) {
        final List<Long> seeds = almanac.seeds();
        final List<LongRange> seedRanges
            = Stream.iterate(0, i -> i < seeds.size(), i -> i + 2)
                .map(i -> LongRange.between(
                    seeds.get(i).longValue(),
                    seeds.get(i).longValue() + seeds.get(i + 1).longValue()))
                .toList();
        return solve(almanac, seedRanges);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "35"),
        @Sample(method = "part2", input = TEST, expected = "46"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_05.create().run();
    }
    
    record MappingParams(LongRange range, long diff) {}
    
    record Mapping(List<MappingParams> ranges) {
        
        public List<LongRange> map(final List<LongRange> inps) {
            final List<LongRange> ans = new ArrayList<>();
            final Deque<LongRange> q = new ArrayDeque<>(inps);
            while(!q.isEmpty()) {
                final LongRange inp = q.pop();
                boolean found = false;
                for (final MappingParams r : ranges) {
                    final JoinResult result = inp.leftJoin(r.range());
                    if (result.intersection().isPresent()) {
                       final LongRange intersection = result.intersection().get();
                       ans.add(intersection.translate(r.diff()));
                       result.others().forEach(o -> q.add(o));
                       found = true;
                       break;
                    }
                }
                if (!found) {
                    ans.add(inp);
                }
            }
            return ans;
        }
    }
    
    record Almanac(List<Long> seeds, List<Mapping> mappings)  {
        
        public static Almanac fromInput(final List<String> input) {
           final List<List<String>> blocks = StringOps.toBlocks(input);
           final List<Long> seeds = Arrays.stream(
                   blocks.get(0).get(0).split(": ")[1].split(" "))
               .map(Long::valueOf)
               .toList();
           final Function<String, MappingParams> getMappingParams = s -> {
               final long[] x = Arrays.stream(s.split(" ")).mapToLong(Long::valueOf).toArray();
               return new MappingParams(LongRange.between(x[1], x[1] + x[2]), x[0] - x[1]);
           };
           final List<Mapping> mappings = IntStream.range(1, blocks.size())
               .mapToObj(i -> {
                   final List<MappingParams> ranges =
                       blocks.get(i).subList(1, blocks.get(i).size()).stream()
                           .map(line -> getMappingParams.apply(line))
                           .toList();
                   return new Mapping(ranges);
               })
               .toList();
           return new Almanac(seeds, mappings);
        }
        
        public List<LongRange> getLocation(final List<LongRange> seedRanges) {
            List<LongRange> ans = new ArrayList<>(seedRanges);
            for (final Mapping mapping : this.mappings) {
               ans = mapping.map(ans);
            }
            return ans;
        }
    }

    private static final String TEST = """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
            """;
}