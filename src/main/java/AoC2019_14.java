import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2019_14
        extends SolutionBase<Map<String, AoC2019_14.Reaction>, Long, Long> {
    
    private static final String ORE = "ORE";
    private static final String FUEL = "FUEL";
    private static final long ONE_TRILLION = 1_000_000_000_000L;

    private AoC2019_14(final boolean debug) {
        super(debug);
    }

    public static AoC2019_14 create() {
        return new AoC2019_14(false);
    }

    public static AoC2019_14 createDebug() {
        return new AoC2019_14(true);
    }
    
    @Override
    protected Map<String, Reaction> parseInput(final List<String> inputs) {
        return inputs.stream()
            .map(Reaction::fromString)
            .collect(toMap(r -> r.material.name, r -> r));
    }
    
    @Override
    public Long solvePart1(final Map<String, Reaction> input) {
        return oreNeededFor(FUEL, 1, input, new HashMap<>());
   }
    
    @Override
    public Long solvePart2(final Map<String, Reaction> input) {
        final long part1 = oreNeededFor(FUEL, 1, input, new HashMap<>());
        long min = ONE_TRILLION / part1 / 10;
        long max = ONE_TRILLION / part1 * 10;
        while (min <= max) {
            final long mid = (min + max) / 2;
            final long ans = oreNeededFor(FUEL, mid, input, new HashMap<>());
            if (ans == ONE_TRILLION) {
                return mid;
            } else if (ans < ONE_TRILLION) {
                min = mid + 1;
            } else {
                max = mid - 1;
            }
        }
        return min - 1;
    }
    
    private long oreNeededFor(
            final String material,
            final long amount,
            final Map<String, Reaction> reactions,
            final Map<String, Long> inventory
    ) {
        if (ORE.equals(material)) {
            return amount;
        }
        final long available = inventory.getOrDefault(material, 0L);
        if (amount <= available) {
            inventory.put(material, available - amount);
            return 0;
        } else {
            inventory.put(material, 0L);
        }
        
        final Reaction reaction = reactions.get(material);
        final long produced = reaction.material.amount;
        final long needed = amount - available;
        final long runs = (long) Math.ceil((double) needed / produced);
        if (needed < produced * runs) {
            inventory.merge(material, produced * runs - needed, Long::sum);
        }
        return reaction.reactants.stream()
            .mapToLong(r -> oreNeededFor(r.name, r.amount * runs, reactions, inventory))
            .sum();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "31"),
        @Sample(method = "part1", input = TEST2, expected = "165"),
        @Sample(method = "part1", input = TEST3, expected = "13312"),
        @Sample(method = "part1", input = TEST4, expected = "180697"),
        @Sample(method = "part1", input = TEST5, expected = "2210736"),
        @Sample(method = "part2", input = TEST3, expected = "82892753"),
        @Sample(method = "part2", input = TEST4, expected = "5586022"),
        @Sample(method = "part2", input = TEST5, expected = "460664"),
    })
    public void samples() {}

    public static void main(final String[] args) throws Exception {
        AoC2019_14.create().run();
    }

    private static final String TEST1 = """
            10 ORE => 10 A
            1 ORE => 1 B
            7 A, 1 B => 1 C
            7 A, 1 C => 1 D
            7 A, 1 D => 1 E
            7 A, 1 E => 1 FUEL
            """;
    private static final String TEST2 = """
            9 ORE => 2 A
            8 ORE => 3 B
            7 ORE => 5 C
            3 A, 4 B => 1 AB
            5 B, 7 C => 1 BC
            4 C, 1 A => 1 CA
            2 AB, 3 BC, 4 CA => 1 FUEL
            """;
    private static final String TEST3 = """
            157 ORE => 5 NZVS
            165 ORE => 6 DCFZ
            44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
            12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
            179 ORE => 7 PSHF
            177 ORE => 5 HKGWZ
            7 DCFZ, 7 PSHF => 2 XJWVT
            165 ORE => 2 GPVTF
            3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT
            """;
    private static final String TEST4 = """
            2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG
            17 NVRVD, 3 JNWZP => 8 VPVL
            53 STKFG, 6 MNCFX, 46 VJHF, 81 HVMC, 68 CXFTF, 25 GNMV => 1 FUEL
            22 VJHF, 37 MNCFX => 5 FWMGM
            139 ORE => 4 NVRVD
            144 ORE => 7 JNWZP
            5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC
            5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV
            145 ORE => 6 MNCFX
            1 NVRVD => 8 CXFTF
            1 VJHF, 6 MNCFX => 4 RFSQX
            176 ORE => 6 VJHF
            """;
    private static final String TEST5 = """
            171 ORE => 8 CNZTR
            7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL
            114 ORE => 4 BHXH
            14 VRPVC => 6 BMBT
            6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL
            6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT
            15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW
            13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW
            5 BMBT => 4 WPTQ
            189 ORE => 9 KTJDG
            1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP
            12 VRPVC, 27 CNZTR => 2 XDBXC
            15 KTJDG, 12 BHXH => 5 XCVML
            3 BHXH, 2 VRPVC => 7 MZWV
            121 ORE => 7 VRPVC
            7 XCVML => 6 RJRHP
            5 BHXH, 4 VRPVC => 5 LTCX
            """;
    
    record Material(String name, int amount) {
        
        public static Material fromString(final String string) {
            final StringSplit splitR = StringOps.splitOnce(string, " ");
            return new Material(splitR.right(), Integer.parseInt(splitR.left()));
        }
    }
    
    record Reaction(Material material, Set<Material> reactants) {
        
        public static Reaction fromString(final String string) {
           final StringSplit split = StringOps.splitOnce(string, " => ");
           final Material target = Material.fromString(split.right());
           final Set<Material> reactants = Arrays.stream(split.left().split(", "))
               .map(Material::fromString)
               .collect(toSet());
           return new Reaction(target, reactants);
        }
    }
}