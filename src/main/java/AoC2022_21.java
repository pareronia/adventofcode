import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

/**
 * TODO semi-manual solution, needs work to handle other inputs
 *
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
    
    @Override
    public Long solvePart1() {
        return solve(this.input, "root");
    }

    // rhs: 31522134274080
    // lhs: 72750855862928
    // diff = 41228721588848
    // 5000 -> 72750855862928 - 72750855798976 = 63952
    // 10000 -> 72750855798976 - 72750855734976 = 64000
    // diff / 64000 = 644198774.82575
    @Override
    public Long solvePart2() {
        final String root = this.input.get("root");
        this.input.remove("root");
        final String[] splits = root.split(" ");
        long ans = 644198774L * 5000;
        final long rhs = solve(this.input, splits[2]);
        log(String.format("rhs: %s", rhs));
        int cnt = 0;
        while (true) {
            assert cnt++ < 5000;
            if (ans % 1000 == 0) {
                log(cnt);
            }
            this.input.put("humn", String.valueOf(ans));
            final long lhs = solve(this.input, splits[0]);
            if (lhs == rhs) {
                return ans;
            }
            ans++;
        }
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_21.createDebug(TEST).solvePart1() == 152;
//        assert AoC2022_21.createDebug(TEST).solvePart2() == 301;

        final Puzzle puzzle = Aocd.puzzle(2022, 21);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_21.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_21.createDebug(inputData)::solvePart2)
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
