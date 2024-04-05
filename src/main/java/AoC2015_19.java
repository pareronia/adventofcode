import static com.github.pareronia.aoc.StringOps.splitLines;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import lombok.RequiredArgsConstructor;

public class AoC2015_19 extends SolutionBase<ReplacementsAndMolecule, Integer, Integer> {
    
    private AoC2015_19(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_19 create() {
        return new AoC2015_19(false);
    }

    public static final AoC2015_19 createDebug() {
        return new AoC2015_19(true);
    }
    
    @Override
    protected ReplacementsAndMolecule parseInput(final List<String> inputs) {
        return ReplacementsAndMolecule.fromInput(inputs);
    }

    private Set<String> runReplacement(
            final Map<String, List<String>> replacements,
            final String m
    ) {
        final Set<String> molecules = new HashSet<>();
        String key = "";
        for (int i = 0; i < m.length(); i ++) {
            if (key.length() == 2) {
                key = "";
                continue;
            }
            final String c = m.substring(i, i + 1);
            if (replacements.containsKey(c)) {
                key = c;
            } else if (replacements.containsKey(m.substring(i, Math.min(m.length(), i + 2)))) {
                key = m.substring(i, Math.min(m.length(), i + 2));
            } else {
                continue;
            }
            for (final String r : replacements.get(key)) {
                molecules.add(m.substring(0, i) + r + m.substring(i + key.length()));
            }
        }
        return molecules;
    }
    
    private int fabricate(
            final Map<String, List<String>> replacements,
            final String target,
            final String molecule,
            final int cnt
    ) {
        final Set<String> newMolecules = runReplacement(replacements, molecule);
        if (newMolecules.contains(target)) {
            return cnt + 1;
        }
        for (final String m : newMolecules) {
            if (m.length() > target.length()) {
                return 0;
            }
            final int result = fabricate(replacements, target, m, cnt + 1);
            if (result > 0) {
                return result;
            }
        }
        return 0;
    }
    
    private int solve2bis(final ReplacementsAndMolecule input) {
        return fabricate(input.replacements, input.molecule, "e", 0);
    }
    
    private int solve2(final ReplacementsAndMolecule input) {
        int cnt = 0;
        String newMolecule = new String(input.molecule);
        while (!"e".equals(newMolecule)) {
            for (final String new_ : input.replacements.keySet()) {
                for (final String old : input.replacements.get(new_)) {
                    if (newMolecule.contains(old)) {
                        newMolecule = newMolecule.replaceFirst(old, new_);
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    @Override
    protected Integer solvePart1(final ReplacementsAndMolecule input) {
        return runReplacement(input.replacements, input.molecule).size();
    }

    @Override
    protected Integer solvePart2(final ReplacementsAndMolecule input) {
        return solve2(input);
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "4"),
        @Sample(method = "part1", input = TEST2, expected = "7"),
        @Sample(method = "part1", input = TEST3, expected = "6"),
    })
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        final AoC2015_19 test = AoC2015_19.createDebug();
        assert test.solve2bis(test.parseInput(TEST4)) == 3;
        assert test.solve2bis(test.parseInput(TEST5)) == 6;
        
        AoC2015_19.createDebug().run();
    }
    
    private static final String TEST1 =
              """
    	H => HO\r
    	H => OH\r
    	O => HH\r
    	\r
    	HOH""";
    private static final String TEST2 =
              """
    	H => HO\r
    	H => OH\r
    	O => HH\r
    	\r
    	HOHOHO""";
    private static final String TEST3 =
              """
    	H => HO\r
    	H => OH\r
    	Oo => HH\r
    	\r
    	HOHOoHO""";
    private static final List<String> TEST4 = splitLines(
                "e => H\r\n"
              + "e => O\r\n"
              + "H => HO\r\n"
              + "H => OH\r\n"
              + "O => HH\r\n"
              + "\r\n"
              + "HOH"
    );
    private static final List<String> TEST5 = splitLines(
                "e => H\r\n"
              + "e => O\r\n"
              + "H => HO\r\n"
              + "H => OH\r\n"
              + "O => HH\r\n"
              + "\r\n"
              + "HOHOHO"
    );
}

@RequiredArgsConstructor
final class ReplacementsAndMolecule {
    
    final Map<String, List<String>> replacements;
    final String molecule;
    
    public static ReplacementsAndMolecule fromInput(final List<String> inputs) {
        final List<List<String>> blocks = StringOps.toBlocks(inputs);
        // replacement result depends on order of input -> using LinkedHashMap
        final Map<String, List<String>> replacements = blocks.get(0).stream()
            .map(s -> s.split(" => "))
            .collect(groupingBy(sp -> sp[0], LinkedHashMap::new, mapping(s -> s[1], toList())));
        assert blocks.get(1).size() == 1;
        final String molecule = blocks.get(1).get(0);
        return new ReplacementsAndMolecule(replacements, molecule);
    }
}
