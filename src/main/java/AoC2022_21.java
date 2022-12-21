import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

/**
 * TODO binary search solution, try calculated solution
 */
public class AoC2022_21 extends AoCBase {
    
    private final Map<String, String> input;
    
    private AoC2022_21(final List<String> input, final boolean debug) {
        super(debug);
        this.input = new HashMap<>();
        for (final String line : input) {
            final String[] splits = line.split(": ");
            this.input.put(splits[0], splits[1]);
        }
    }
    
    public static final AoC2022_21 create(final List<String> input) {
        return new AoC2022_21(input, false);
    }

    public static final AoC2022_21 createDebug(final List<String> input) {
        return new AoC2022_21(input, true);
    }
    
    private long solve(final Map<String, String> map, final String term) {
        final String string = map.get(term);
        if (Character.isDigit(Utils.last(string))) {
            return Long.parseLong(string);
        }
        final String[] splits = string.split(" ");
        if ("+".equals(splits[1])) {
            return solve(map, splits[0]) + solve(map, splits[2]);
        } else if ("-".equals(splits[1])) {
            return solve(map, splits[0]) - solve(map, splits[2]);
        } else if ("/".equals(splits[1])) {
            return solve(map, splits[0]) / solve(map, splits[2]);
        } else if ("*".equals(splits[1])) {
            return solve(map, splits[0]) * solve(map, splits[2]);
        }
        throw new IllegalStateException("Unsolvable");
    }

    private long solveFor(final String term, final long humn) {
        this.input.put("humn", String.valueOf(humn));
        return solve(this.input, term);
    }
    
    private long findLowest(final String lhterm, final long start, final long rhs) {
        long val = start;
        while (true) {
            val--;
            final long _lhs = solveFor(lhterm, val);
            if (_lhs != rhs) {
                break;
            }
        }
        return ++val;
    }
    
    private long solve2(final boolean decreasing) {
        final String[] splits = this.input.get("root").split(" ");
        final long rhs = solve(this.input, splits[2]);
        final String lhterm = splits[0];
        long a = 0;
        long b = rhs * 4;
        while (true) {
            final long mid = (a + b) / 2;
            final long lhs = solveFor(lhterm, mid);
            if (lhs == rhs) {
                return findLowest(lhterm, mid, rhs);
            } else if (lhs > rhs) {
                a = decreasing ? mid + 1 : a;
                b = decreasing ? b : mid;
            } else {
                a = decreasing ? a : mid + 1;
                b = decreasing ? mid : b;
            }
        }
    }
    
    @Override
    public Long solvePart1() {
        return solve(this.input, "root");
    }
    
    @Override
    public Long solvePart2() {
        return solve2(true);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_21.createDebug(TEST).solvePart1() == 152;
        assert AoC2022_21.createDebug(TEST).solve2(false) == 301;

        final Puzzle puzzle = Aocd.puzzle(2022, 21);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_21.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_21.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "root: pppw + sjmn\r\n" +
        "dbpl: 5\r\n" +
        "cczh: sllz + lgvd\r\n" +
        "zczc: 2\r\n" +
        "ptdq: humn - dvpt\r\n" +
        "dvpt: 3\r\n" +
        "lfqf: 4\r\n" +
        "humn: 5\r\n" +
        "ljgn: 2\r\n" +
        "sjmn: drzm * dbpl\r\n" +
        "sllz: 4\r\n" +
        "pppw: cczh / lfqf\r\n" +
        "lgvd: ljgn * ptdq\r\n" +
        "drzm: hmdt - zczc\r\n" +
        "hmdt: 32"
    );
}
