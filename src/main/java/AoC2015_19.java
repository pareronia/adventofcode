import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2015_19 extends AoCBase {
    
    private final Map<String, List<String>> replacements;
    private final String molecule;
    
    private AoC2015_19(final List<String> inputs, final boolean debug) {
        super(debug);
        final List<List<String>> blocks = toBlocks(inputs);
        this.replacements = blocks.get(0).stream()
            .map(s -> s.split(" => "))
            .collect(groupingBy(sp -> sp[0], mapping(s -> s[1], toList())));
        assert blocks.get(1).size() == 1;
        this.molecule = blocks.get(1).get(0);
    }

    public static final AoC2015_19 create(final List<String> input) {
        return new AoC2015_19(input, false);
    }

    public static final AoC2015_19 createDebug(final List<String> input) {
        return new AoC2015_19(input, true);
    }
    
    private Set<String> runReplacement(final String m) {
        final Set<String> molecules = new HashSet<>();
        String key = "";
        for (int i = 0; i < m.length(); i ++) {
            if (key.length() == 2) {
                key = "";
                continue;
            }
            final String c = m.substring(i, i + 1);
            if (this.replacements.containsKey(c)) {
                key = c;
            } else if (i + 2 <= m.length()) {
                final String cc = m.substring(i, i + 2);
                if (this.replacements.containsKey(cc)) {
                    key = cc;
                } else {
                    continue;
                }
            }
            for (final String r : this.replacements.get(key)) {
                molecules.add(m.substring(0, i) + r + m.substring(i + key.length()));
            }
        }
        return molecules;
    }
    
    private int fabricate(final String target, final String molecule, final int cnt) {
        final Set<String> newMolecules = runReplacement(molecule);
        if (newMolecules.contains(target)) {
            return cnt + 1;
        }
        for (final String m : newMolecules) {
            if (m.length() > target.length()) {
                return 0;
            }
            final int result = fabricate(target, m, cnt + 1);
            if (result > 0) {
                return result;
            }
        }
        return 0;
    }
    
    private int solve2bis() {
        return fabricate(this.molecule, "e", 0);
    }
    
    private int solve2() {
        int cnt = 0;
        String newMolecule = new String(this.molecule);
        while (!"e".equals(newMolecule)) {
            for (final String new_ : this.replacements.keySet()) {
                for (final String old : this.replacements.get(new_)) {
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
    public Integer solvePart1() {
        return runReplacement(this.molecule).size();
    }

    @Override
    public Integer solvePart2() {
        return solve2();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_19.createDebug(TEST1).solvePart1() == 4;
        assert AoC2015_19.createDebug(TEST2).solvePart1() == 7;
        assert AoC2015_19.createDebug(TEST3).solvePart1() == 6;
        assert AoC2015_19.createDebug(TEST4).solve2bis() == 3;
        assert AoC2015_19.createDebug(TEST5).solve2bis() == 6;
        
        final Puzzle puzzle = Aocd.puzzle(2015, 19);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2015_19.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2015_19.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST1 = splitLines(
              "H => HO\r\n"
            + "H => OH\r\n"
            + "O => HH\r\n"
            + "\r\n"
            + "HOH"
    );
    private static final List<String> TEST2 = splitLines(
              "H => HO\r\n"
            + "H => OH\r\n"
            + "O => HH\r\n"
            + "\r\n"
            + "HOHOHO"
    );
    private static final List<String> TEST3 = splitLines(
              "H => HO\r\n"
            + "H => OH\r\n"
            + "Oo => HH\r\n"
            + "\r\n"
            + "HOHOHO"
    );
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
